# Recommendation System: Gợi Ý Khóa Học Cá Nhân Hóa Với Vector DB

![Recommendation System- Gợi Ý Khóa Học Cá Nhân Hóa Với Vector DB.jpeg](../images/2104b488-ae7f-4ecf-949b-2bcd5eb9ae47.jpeg)

Netflix gợi ý phim, Spotify gợi ý nhạc, Amazon gợi ý sản phẩm — recommendation system là tính năng tạo ra **giá trị kinh doanh lớn nhất** trong các nền tảng hiện đại. Với [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev), một recommendation system tốt có thể tăng enrollment rate, giảm bounce rate và tăng time-on-site đáng kể. Bài này sẽ xây dựng 3 loại recommendation dùng Vector DB: content-based, collaborative filtering và hybrid.

## 1\. Tổng Quan Các Loại Recommendation

```java
3 loại chính:

1. Content-based Filtering
   "Khóa này tương tự với khóa bạn đã học"
   → Dựa trên nội dung khóa học (title, description, tags)
   → Không cần dữ liệu user khác
   ✅ Cold start tốt (course mới vẫn được recommend)
   ❌ Thiếu "serendipity" — chỉ gợi ý những thứ giống nhau

2. Collaborative Filtering
   "Học viên giống bạn cũng học những khóa này"
   → Dựa trên hành vi của nhiều user
   → "User-based" hoặc "Item-based"
   ✅ Khám phá được khóa học mới, không bị giới hạn bởi content
   ❌ Cold start kém (user mới / khóa mới không đủ data)

3. Hybrid Recommendation
   → Kết hợp cả hai phương pháp
   ✅ Tốt nhất về mọi mặt, dùng trong production
```

## 2\. Content-based Filtering

### 2.1 Course-to-Course Similarity

```python
import os
import numpy as np
import psycopg2
import psycopg2.extras
from typing import List, Dict, Optional, Tuple
from dataclasses import dataclass
from sentence_transformers import SentenceTransformer
from dotenv import load_dotenv

load_dotenv()

@dataclass
class RecommendedCourse:
    course_id:    int
    title:        str
    slug:         str
    category:     str
    price:        float
    rating:       float
    is_free:      bool
    score:        float
    reason:       str    # lý do tại sao recommend
    rec_type:     str    # 'content', 'collaborative', 'hybrid'

class ContentBasedRecommender:
    """
    Recommend dựa trên nội dung — không cần user history.
    """

    def __init__(self, model_name: str = "paraphrase-multilingual-MiniLM-L12-v2"):
        self.model = SentenceTransformer(model_name)
        self.conn  = psycopg2.connect(
            host=os.getenv("POSTGRES_HOST"),
            port=os.getenv("POSTGRES_PORT"),
            user=os.getenv("POSTGRES_USER"),
            password=os.getenv("POSTGRES_PASSWORD"),
            dbname=os.getenv("POSTGRES_DB")
        )

    def get_similar_courses(
        self,
        course_id: int,
        limit: int = 6,
        exclude_ids: Optional[List[int]] = None
    ) -> List[RecommendedCourse]:
        """
        Tìm khóa học tương tự dựa trên content embedding.
        Dùng cho: "Bạn có thể cũng thích..." trên trang course detail.
        """
        cursor = self.conn.cursor(cursor_factory=psycopg2.extras.DictCursor)

        # Lấy embedding của khóa gốc
        cursor.execute("""
            SELECT cv.embedding, c.title, c.category_id
            FROM course_vectors cv
            JOIN courses c ON c.id = cv.course_id
            WHERE cv.course_id = %s
              AND cv.content_type = 'full'
            LIMIT 1
        """, (course_id,))

        row = cursor.fetchone()
        if not row:
            cursor.close()
            return []

        source_embedding = row['embedding']
        source_title     = row['title']
        source_category  = row['category_id']

        # Build exclusion list
        exclude_ids = exclude_ids or []
        exclude_ids.append(course_id)
        exclude_placeholder = ','.join(['%s'] * len(exclude_ids))

        cursor.execute(f"""
            SELECT
                c.id,
                c.title,
                c.slug,
                cat.name                                    AS category,
                COALESCE(cp.price, 0)                      AS price,
                COALESCE(c.rating, 0)                      AS rating,
                (c.course_type = 'FREE')                   AS is_free,
                1 - (cv.embedding <=> %s::vector)          AS similarity,
                -- Boost score cho cùng category
                CASE WHEN c.category_id = %s THEN 1.1 ELSE 1.0 END AS category_boost
            FROM course_vectors cv
            JOIN courses    c   ON c.id   = cv.course_id
            JOIN categories cat ON cat.id = c.category_id
            LEFT JOIN course_pricing cp
                   ON cp.course_id = c.id
                  AND cp.currency = 'VND'
                  AND cp.is_active = TRUE
            WHERE c.course_status = 'PUBLISHED'
              AND c.id NOT IN ({exclude_placeholder})
              AND cv.content_type = 'full'
            ORDER BY cv.embedding <=> %s::vector
            LIMIT %s
        """, [source_embedding, source_category] + exclude_ids + [source_embedding, limit * 2])

        rows = cursor.fetchall()
        cursor.close()

        results = []
        for row in rows[:limit]:
            score = float(row['similarity']) * float(row['category_boost'])
            results.append(RecommendedCourse(
                course_id = row['id'],
                title     = row['title'],
                slug      = row['slug'],
                category  = row['category'],
                price     = float(row['price']),
                rating    = float(row['rating']),
                is_free   = bool(row['is_free']),
                score     = score,
                reason    = f"Tương tự '{source_title}'",
                rec_type  = "content"
            ))

        return results

    def get_courses_by_topic(
        self,
        topic: str,
        limit: int = 6,
        exclude_ids: Optional[List[int]] = None
    ) -> List[RecommendedCourse]:
        """
        Recommend theo topic/interest tự do.
        Dùng cho: onboarding flow — "Bạn quan tâm đến gì?"
        """
        topic_embedding = self.model.encode(
            topic, normalize_embeddings=True
        ).tolist()

        cursor = self.conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        exclude_ids = exclude_ids or []

        exclude_clause = ""
        params = [topic_embedding]

        if exclude_ids:
            exclude_clause = f"AND c.id NOT IN ({','.join(['%s']*len(exclude_ids))})"
            params.extend(exclude_ids)

        params.extend([topic_embedding, limit])

        cursor.execute(f"""
            SELECT
                c.id,
                c.title,
                c.slug,
                cat.name                           AS category,
                COALESCE(cp.price, 0)             AS price,
                COALESCE(c.rating, 0)             AS rating,
                (c.course_type = 'FREE')          AS is_free,
                1 - (cv.embedding <=> %s::vector) AS similarity
            FROM course_vectors cv
            JOIN courses    c   ON c.id   = cv.course_id
            JOIN categories cat ON cat.id = c.category_id
            LEFT JOIN course_pricing cp
                   ON cp.course_id = c.id
                  AND cp.currency = 'VND'
                  AND cp.is_active = TRUE
            WHERE c.course_status = 'PUBLISHED'
              AND cv.content_type = 'full'
              {exclude_clause}
            ORDER BY cv.embedding <=> %s::vector
            LIMIT %s
        """, params)

        rows = cursor.fetchall()
        cursor.close()

        return [
            RecommendedCourse(
                course_id = row['id'],
                title     = row['title'],
                slug      = row['slug'],
                category  = row['category'],
                price     = float(row['price']),
                rating    = float(row['rating']),
                is_free   = bool(row['is_free']),
                score     = float(row['similarity']),
                reason    = f"Phù hợp với '{topic}'",
                rec_type  = "content"
            )
            for row in rows
        ]
```

## 3\. Collaborative Filtering Với Vector DB

### 3.1 User Embedding Từ Hành Vi

```python
class CollaborativeRecommender:
    """
    Recommend dựa trên hành vi của nhiều user.
    """

    def __init__(self, model_name: str = "paraphrase-multilingual-MiniLM-L12-v2"):
        self.model = SentenceTransformer(model_name)
        self.conn  = psycopg2.connect(
            host=os.getenv("POSTGRES_HOST"),
            port=os.getenv("POSTGRES_PORT"),
            user=os.getenv("POSTGRES_USER"),
            password=os.getenv("POSTGRES_PASSWORD"),
            dbname=os.getenv("POSTGRES_DB")
        )

    def build_user_embedding(self, user_id: int) -> Optional[np.ndarray]:
        """
        Tạo embedding đại diện cho user dựa trên lịch sử học.

        Weighted average của course embeddings:
        - Completed courses: weight = 3.0
        - In-progress (> 50%): weight = 2.0
        - Just started (< 50%): weight = 1.5
        - Just enrolled: weight = 1.0
        """
        cursor = self.conn.cursor(cursor_factory=psycopg2.extras.DictCursor)

        cursor.execute("""
            SELECT
                cv.embedding,
                CASE
                    WHEN ucc.id IS NOT NULL THEN 3.0
                    WHEN COALESCE(progress.pct, 0) > 50 THEN 2.0
                    WHEN COALESCE(progress.pct, 0) > 0  THEN 1.5
                    ELSE 1.0
                END AS weight
            FROM enrollments e
            JOIN course_vectors cv
                ON cv.course_id   = e.course_id
               AND cv.content_type = 'full'
            LEFT JOIN user_course_certificates ucc
                ON ucc.student_id = e.user_id
               AND ucc.course_id  = e.course_id
            LEFT JOIN (
                SELECT
                    tp.student_id,
                    l.course_id,
                    AVG(CASE WHEN tp.completed THEN 100 ELSE 0 END) AS pct
                FROM tracking_progress tp
                JOIN lectures l ON l.id = tp.lecture_id
                WHERE tp.student_id = %s
                GROUP BY tp.student_id, l.course_id
            ) progress
                ON progress.student_id = e.user_id
               AND progress.course_id  = e.course_id
            WHERE e.user_id = %s
        """, (user_id, user_id))

        rows = cursor.fetchall()
        cursor.close()

        if not rows:
            return None

        # Parse embeddings và tính weighted average
        embeddings = []
        weights    = []
        for row in rows:
            emb = [float(x) for x in row['embedding'].strip('[]').split(',')]
            embeddings.append(emb)
            weights.append(float(row['weight']))

        embeddings = np.array(embeddings)
        weights    = np.array(weights)

        weighted_avg = np.average(embeddings, axis=0, weights=weights)

        # Normalize
        norm = np.linalg.norm(weighted_avg)
        if norm > 0:
            weighted_avg /= norm

        return weighted_avg

    def get_recommendations_for_user(
        self,
        user_id: int,
        limit: int = 10,
        exclude_enrolled: bool = True
    ) -> List[RecommendedCourse]:
        """
        Recommend dựa trên preference vector của user.
        """
        user_embedding = self.build_user_embedding(user_id)
        if user_embedding is None:
            return []  # User chưa enroll gì → fallback sang popular courses

        cursor = self.conn.cursor(cursor_factory=psycopg2.extras.DictCursor)

        exclude_clause = ""
        params = [user_embedding.tolist()]

        if exclude_enrolled:
            exclude_clause = """
                AND c.id NOT IN (
                    SELECT course_id FROM enrollments WHERE user_id = %s
                )
            """
            params.append(user_id)

        params.extend([user_embedding.tolist(), limit])

        cursor.execute(f"""
            SELECT
                c.id,
                c.title,
                c.slug,
                cat.name                            AS category,
                COALESCE(cp.price, 0)              AS price,
                COALESCE(c.rating, 0)              AS rating,
                (c.course_type = 'FREE')           AS is_free,
                1 - (cv.embedding <=> %s::vector)  AS similarity
            FROM course_vectors cv
            JOIN courses    c   ON c.id   = cv.course_id
            JOIN categories cat ON cat.id = c.category_id
            LEFT JOIN course_pricing cp
                   ON cp.course_id = c.id
                  AND cp.currency = 'VND'
                  AND cp.is_active = TRUE
            WHERE c.course_status = 'PUBLISHED'
              AND cv.content_type = 'full'
              {exclude_clause}
            ORDER BY cv.embedding <=> %s::vector
            LIMIT %s
        """, params)

        rows = cursor.fetchall()
        cursor.close()

        return [
            RecommendedCourse(
                course_id = row['id'],
                title     = row['title'],
                slug      = row['slug'],
                category  = row['category'],
                price     = float(row['price']),
                rating    = float(row['rating']),
                is_free   = bool(row['is_free']),
                score     = float(row['similarity']),
                reason    = "Dựa trên lịch sử học của bạn",
                rec_type  = "collaborative"
            )
            for row in rows
        ]

    def get_similar_users(
        self,
        user_id: int,
        limit: int = 10
    ) -> List[Dict]:
        """
        Tìm user có sở thích học tập tương tự.
        Dùng cho: "Học viên tương tự cũng học..." feature.
        """
        user_embedding = self.build_user_embedding(user_id)
        if user_embedding is None:
            return []

        cursor = self.conn.cursor(cursor_factory=psycopg2.extras.DictCursor)

        # So sánh với preference vectors của tất cả users khác
        cursor.execute("""
            SELECT
                upv.user_id,
                u.first_name || ' ' || u.last_name   AS name,
                1 - (upv.embedding <=> %s::vector)   AS similarity,
                upv.courses_count
            FROM user_preference_vectors upv
            JOIN users u ON u.id = upv.user_id
            WHERE upv.user_id != %s
              AND u.account_status = 'ACTIVE'
            ORDER BY upv.embedding <=> %s::vector
            LIMIT %s
        """, (user_embedding.tolist(), user_id,
              user_embedding.tolist(), limit))

        rows = cursor.fetchall()
        cursor.close()

        return [dict(row) for row in rows]

    def get_courses_from_similar_users(
        self,
        user_id: int,
        limit: int = 10
    ) -> List[RecommendedCourse]:
        """
        "User có profile giống bạn đã học những khóa này"
        Classic collaborative filtering approach.
        """
        # Tìm users tương tự
        similar_users = self.get_similar_users(user_id, limit=20)
        if not similar_users:
            return []

        similar_user_ids = [u['user_id'] for u in similar_users]

        cursor = self.conn.cursor(cursor_factory=psycopg2.extras.DictCursor)

        # Lấy courses mà similar users đã enroll nhưng current user chưa
        placeholders = ','.join(['%s'] * len(similar_user_ids))
        cursor.execute(f"""
            SELECT
                c.id,
                c.title,
                c.slug,
                cat.name                AS category,
                COALESCE(cp.price, 0)  AS price,
                COALESCE(c.rating, 0)  AS rating,
                (c.course_type = 'FREE') AS is_free,
                COUNT(e.user_id)        AS enrolled_by_similar,
                AVG(
                    CASE WHEN ucc.id IS NOT NULL THEN 1.0 ELSE 0.0 END
                )                       AS completion_rate
            FROM enrollments e
            JOIN courses    c   ON c.id   = e.course_id
            JOIN categories cat ON cat.id = c.category_id
            LEFT JOIN course_pricing cp
                   ON cp.course_id = c.id
                  AND cp.currency = 'VND'
                  AND cp.is_active = TRUE
            LEFT JOIN user_course_certificates ucc
                   ON ucc.student_id = e.user_id
                  AND ucc.course_id  = e.course_id
            WHERE e.user_id IN ({placeholders})
              AND e.course_id NOT IN (
                  SELECT course_id FROM enrollments WHERE user_id = %s
              )
              AND c.course_status = 'PUBLISHED'
            GROUP BY c.id, c.title, c.slug, cat.name, cp.price, c.rating, c.course_type
            ORDER BY enrolled_by_similar DESC, completion_rate DESC
            LIMIT %s
        """, similar_user_ids + [user_id, limit])

        rows = cursor.fetchall()
        cursor.close()

        return [
            RecommendedCourse(
                course_id = row['id'],
                title     = row['title'],
                slug      = row['slug'],
                category  = row['category'],
                price     = float(row['price']),
                rating    = float(row['rating']),
                is_free   = bool(row['is_free']),
                score     = float(row['enrolled_by_similar']) / 20,
                reason    = f"Được {row['enrolled_by_similar']} học viên tương tự bạn học",
                rec_type  = "collaborative"
            )
            for row in rows
        ]
```

## 4\. Hybrid Recommendation System

```python
class HybridRecommendationSystem:
    """
    Kết hợp content-based và collaborative filtering.
    Xử lý các edge case: user mới, khóa mới, cold start.
    """

    def __init__(self):
        self.content_rec = ContentBasedRecommender()
        self.collab_rec  = CollaborativeRecommender()
        self.conn        = psycopg2.connect(
            host=os.getenv("POSTGRES_HOST"),
            port=os.getenv("POSTGRES_PORT"),
            user=os.getenv("POSTGRES_USER"),
            password=os.getenv("POSTGRES_PASSWORD"),
            dbname=os.getenv("POSTGRES_DB")
        )

    def get_enrollment_count(self, user_id: int) -> int:
        cursor = self.conn.cursor()
        cursor.execute(
            "SELECT COUNT(*) FROM enrollments WHERE user_id = %s",
            (user_id,)
        )
        count = cursor.fetchone()[0]
        cursor.close()
        return count

    def get_popular_courses(self, limit: int = 10,
                             exclude_ids: Optional[List[int]] = None) -> List[RecommendedCourse]:
        """
        Fallback: Top khóa học phổ biến nhất.
        Dùng khi: user mới, không có lịch sử.
        """
        cursor = self.conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        exclude_clause = ""
        params = []

        if exclude_ids:
            exclude_clause = f"AND c.id NOT IN ({','.join(['%s']*len(exclude_ids))})"
            params.extend(exclude_ids)

        params.append(limit)

        cursor.execute(f"""
            SELECT
                c.id,
                c.title,
                c.slug,
                cat.name                                      AS category,
                COALESCE(cp.price, 0)                        AS price,
                COALESCE(c.rating, 0)                        AS rating,
                (c.course_type = 'FREE')                     AS is_free,
                -- Popularity score: kết hợp enrolled_count và rating
                (c.enrolled_count * 0.7 + c.rating * 100 * 0.3) AS popularity_score
            FROM courses c
            JOIN categories cat ON cat.id = c.category_id
            LEFT JOIN course_pricing cp
                   ON cp.course_id = c.id
                  AND cp.currency = 'VND'
                  AND cp.is_active = TRUE
            WHERE c.course_status = 'PUBLISHED'
              {exclude_clause}
            ORDER BY popularity_score DESC
            LIMIT %s
        """, params)

        rows = cursor.fetchall()
        cursor.close()

        return [
            RecommendedCourse(
                course_id = row['id'],
                title     = row['title'],
                slug      = row['slug'],
                category  = row['category'],
                price     = float(row['price']),
                rating    = float(row['rating']),
                is_free   = bool(row['is_free']),
                score     = float(row['popularity_score']),
                reason    = "Phổ biến trên nguyentienkhoi.hashnode.dev",
                rec_type  = "popular"
            )
            for row in rows
        ]

    def get_homepage_recommendations(
        self,
        user_id: Optional[int] = None,
        limit: int = 10
    ) -> Dict[str, List[RecommendedCourse]]:
        """
        Recommendations cho trang chủ — nhiều sections khác nhau.
        """
        sections = {}

        if user_id is None:
            # Anonymous user
            sections["popular"] = self.get_popular_courses(limit=6)
            sections["free"]    = self.content_rec.get_courses_by_topic(
                "lập trình miễn phí cho người mới", limit=4
            )
            return sections

        enrollment_count = self.get_enrollment_count(user_id)

        if enrollment_count == 0:
            # New user — no history
            sections["popular"]     = self.get_popular_courses(limit=6)
            sections["trending"]    = self.content_rec.get_courses_by_topic(
                "Java Spring Boot backend development", limit=4
            )
        elif enrollment_count < 3:
            # Early user — some history
            enrolled_ids = self._get_enrolled_ids(user_id)
            sections["because_you_learned"] = self.content_rec.get_similar_courses(
                enrolled_ids[-1], limit=4, exclude_ids=enrolled_ids
            )
            sections["popular"] = self.get_popular_courses(
                limit=4, exclude_ids=enrolled_ids
            )
        else:
            # Active user — full personalization
            enrolled_ids = self._get_enrolled_ids(user_id)

            # Section 1: Based on user preference vector
            sections["for_you"] = self.collab_rec.get_recommendations_for_user(
                user_id, limit=6
            )

            # Section 2: Similar users
            sections["learners_also_enrolled"] = self.collab_rec.get_courses_from_similar_users(
                user_id, limit=4
            )

            # Section 3: Continue learning path
            if enrolled_ids:
                sections["next_in_path"] = self.content_rec.get_similar_courses(
                    enrolled_ids[-1], limit=4, exclude_ids=enrolled_ids
                )

        return sections

    def get_course_page_recommendations(
        self,
        course_id: int,
        user_id: Optional[int] = None
    ) -> Dict[str, List[RecommendedCourse]]:
        """
        Recommendations trên trang course detail.
        """
        sections     = {}
        enrolled_ids = self._get_enrolled_ids(user_id) if user_id else [course_id]

        # Similar courses
        sections["similar"] = self.content_rec.get_similar_courses(
            course_id, limit=4, exclude_ids=enrolled_ids
        )

        # If user is logged in, personalize
        if user_id:
            enrollment_count = self.get_enrollment_count(user_id)
            if enrollment_count >= 2:
                sections["recommended_for_you"] = self.collab_rec.get_recommendations_for_user(
                    user_id, limit=4
                )

        return sections

    def _get_enrolled_ids(self, user_id: Optional[int]) -> List[int]:
        if not user_id:
            return []
        cursor = self.conn.cursor()
        cursor.execute(
            "SELECT course_id FROM enrollments WHERE user_id = %s ORDER BY enrolled_at DESC",
            (user_id,)
        )
        ids = [row[0] for row in cursor.fetchall()]
        cursor.close()
        return ids

    def merge_and_deduplicate(
        self,
        *rec_lists: List[RecommendedCourse],
        limit: int = 10
    ) -> List[RecommendedCourse]:
        """
        Merge nhiều list recommendations, deduplicate và sort theo score.
        """
        seen: Dict[int, RecommendedCourse] = {}

        for rec_list in rec_lists:
            for rec in rec_list:
                if rec.course_id not in seen:
                    seen[rec.course_id] = rec
                elif rec.score > seen[rec.course_id].score:
                    seen[rec.course_id] = rec

        return sorted(seen.values(), key=lambda x: x.score, reverse=True)[:limit]
```

## 5\. Learning Path Recommendation

```python
class LearningPathRecommender:
    """
    Gợi ý lộ trình học — chuỗi khóa học theo thứ tự logic.
    """

    LEARNING_PATHS = {
        "java_backend": [
            "Java Core nền tảng",
            "Spring Boot từ Zero đến Hero",
            "Microservices với Spring Boot",
            "Docker & Kubernetes thực chiến",
        ],
        "data_engineer": [
            "SQL cho Developer",
            "Python cho Data Engineer",
            "Docker & Kubernetes thực chiến",
        ],
        "fullstack": [
            "Java Core nền tảng",
            "Spring Boot từ Zero đến Hero",
            "ReactJS cơ bản đến nâng cao",
        ]
    }

    def __init__(self, content_rec: ContentBasedRecommender):
        self.content_rec = content_rec
        self.conn        = content_rec.conn

    def detect_learning_path(self, user_id: int) -> Optional[str]:
        """
        Detect user đang theo lộ trình học nào dựa trên enrolled courses.
        """
        cursor = self.conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        cursor.execute("""
            SELECT c.title, c.category_id, cat.name AS category
            FROM enrollments e
            JOIN courses c    ON c.id   = e.course_id
            JOIN categories cat ON cat.id = c.category_id
            WHERE e.user_id = %s
            ORDER BY e.enrolled_at DESC
            LIMIT 5
        """, (user_id,))

        enrolled = cursor.fetchall()
        cursor.close()

        if not enrolled:
            return None

        # Phân tích category
        categories = [row['category'] for row in enrolled]
        titles     = [row['title'].lower() for row in enrolled]

        # Simple heuristic detection
        if any('java' in c.lower() or 'spring' in t for c, t in zip(categories, titles)):
            if any('microservices' in t for t in titles):
                return "java_backend"
            return "java_backend"

        if any('data' in c.lower() or 'sql' in c.lower() for c in categories):
            return "data_engineer"

        return None

    def get_next_in_path(
        self,
        user_id: int,
        limit: int = 3
    ) -> List[RecommendedCourse]:
        """
        Gợi ý bước tiếp theo trong learning path.
        """
        path_name   = self.detect_learning_path(user_id)
        enrolled_ids = self.content_rec._get_enrolled_ids(user_id) if hasattr(
            self.content_rec, '_get_enrolled_ids'
        ) else []

        if path_name and path_name in self.LEARNING_PATHS:
            path_titles = self.LEARNING_PATHS[path_name]

            # Tìm courses trong path chưa enroll
            cursor = self.content_rec.conn.cursor(
                cursor_factory=psycopg2.extras.DictCursor
            )
            results = []
            for title in path_titles:
                cursor.execute("""
                    SELECT
                        c.id, c.title, c.slug,
                        cat.name AS category,
                        COALESCE(cp.price, 0) AS price,
                        COALESCE(c.rating, 0) AS rating,
                        (c.course_type = 'FREE') AS is_free
                    FROM courses c
                    JOIN categories cat ON cat.id = c.category_id
                    LEFT JOIN course_pricing cp
                           ON cp.course_id = c.id
                          AND cp.currency = 'VND'
                          AND cp.is_active = TRUE
                    WHERE c.title ILIKE %s
                      AND c.course_status = 'PUBLISHED'
                      AND c.id != ALL(%s)
                    LIMIT 1
                """, (f"%{title}%", enrolled_ids or [0]))
                row = cursor.fetchone()
                if row:
                    results.append(RecommendedCourse(
                        course_id = row['id'],
                        title     = row['title'],
                        slug      = row['slug'],
                        category  = row['category'],
                        price     = float(row['price']),
                        rating    = float(row['rating']),
                        is_free   = bool(row['is_free']),
                        score     = 1.0 - (len(results) * 0.1),
                        reason    = f"Bước tiếp theo trong lộ trình {path_name.replace('_', ' ')}",
                        rec_type  = "learning_path"
                    ))
                    if len(results) >= limit:
                        break

            cursor.close()
            return results

        # Fallback: content-based recommendation từ khóa học mới nhất
        if enrolled_ids:
            return self.content_rec.get_similar_courses(
                enrolled_ids[0], limit=limit,
                exclude_ids=enrolled_ids
            )

        return []
```

## 6\. Recommendation API

```python
from fastapi import FastAPI, Path, Query
from typing import Optional

app = FastAPI(title="nguyentienkhoi.hashnode.dev Recommendation API")

rec_system = HybridRecommendationSystem()
path_rec   = LearningPathRecommender(rec_system.content_rec)

@app.get("/recommendations/homepage")
async def homepage_recommendations(
    user_id: Optional[int] = None,
    limit:   int = Query(10, ge=1, le=20)
):
    """Recommendations cho trang chủ"""
    sections = rec_system.get_homepage_recommendations(user_id, limit)
    return {
        section: [vars(r) for r in recs]
        for section, recs in sections.items()
    }

@app.get("/recommendations/course/{course_id}")
async def course_recommendations(
    course_id: int  = Path(..., gt=0),
    user_id:   Optional[int] = None
):
    """Similar courses + personalized recs trên course detail page"""
    return rec_system.get_course_page_recommendations(course_id, user_id)

@app.get("/recommendations/learning-path/{user_id}")
async def learning_path(
    user_id: int = Path(..., gt=0),
    limit:   int = Query(3, ge=1, le=10)
):
    """Next courses trong learning path của user"""
    next_courses = path_rec.get_next_in_path(user_id, limit)
    return {
        "path":         path_rec.detect_learning_path(user_id),
        "next_courses": [vars(c) for c in next_courses]
    }

@app.get("/recommendations/similar-users/{user_id}")
async def similar_users(
    user_id: int = Path(..., gt=0),
    limit:   int = Query(5, ge=1, le=20)
):
    """Courses mà users tương tự đã học"""
    recs = rec_system.collab_rec.get_courses_from_similar_users(user_id, limit)
    return [vars(r) for r in recs]
```

## 7\. Demo Hoàn Chỉnh

```python
def demo_recommendations():
    system = HybridRecommendationSystem()

    print("=" * 60)
    print("RECOMMENDATION SYSTEM DEMO")
    print("=" * 60)

    # 1. Homepage cho anonymous user
    print("\n🏠 Homepage (Anonymous User):")
    sections = system.get_homepage_recommendations(user_id=None)
    for section, courses in sections.items():
        print(f"\n  [{section}]")
        for c in courses[:3]:
            free = "FREE" if c.is_free else f"{c.price:,.0f}đ"
            print(f"    • {c.title} ({free}) — {c.reason}")

    # 2. Homepage cho user có lịch sử học
    print("\n\n🏠 Homepage (User #1 — active learner):")
    sections = system.get_homepage_recommendations(user_id=1)
    for section, courses in sections.items():
        print(f"\n  [{section}]")
        for c in courses[:3]:
            print(f"    • {c.title} [{c.rec_type}]")

    # 3. Similar courses
    print("\n\n📚 Similar to 'Spring Boot' (course_id=1):")
    similar = system.content_rec.get_similar_courses(1, limit=4)
    for c in similar:
        print(f"  [{c.score:.4f}] {c.title} — {c.reason}")

    # 4. For You recommendation
    print("\n\n⭐ For You (User #1):")
    for_you = system.collab_rec.get_recommendations_for_user(1, limit=5)
    for c in for_you:
        print(f"  [{c.score:.4f}] {c.title}")

    # 5. Learning path
    path_recommender = LearningPathRecommender(system.content_rec)
    path = path_recommender.detect_learning_path(1)
    print(f"\n\n🗺️  Learning Path for User #1: {path}")
    next_courses = path_recommender.get_next_in_path(1, limit=3)
    for c in next_courses:
        print(f"  → {c.title} ({c.reason})")

demo_recommendations()
```

## Tổng Kết


| Loại Recommendation | Dùng khi | Vector DB Role |
|---|---|---|
| Content-based | Similar courses, topic search | Course embedding similarity |
| User preference | Personalized homepage | User embedding vs course embeddings |
| Collaborative | "Users like you" | User-to-user similarity |
| Learning path | Next course in sequence | Content similarity + heuristics |
| Popular | Cold start, anonymous | Không cần Vector DB |
| Hybrid | Production homepage | Kết hợp tất cả |



```java
Decision tree cho recommendation:

User anonymous?
  → Popular + Trending

User mới (< 3 enrollments)?
  → Similar to last enrolled + Popular

User active (>= 3 enrollments)?
  → For You (user embedding)
  + Learners Also Enrolled (collaborative)
  + Next in Learning Path
```

Bài tiếp theo chúng ta sẽ học **Vector DB Performance & Optimization** — benchmark, HNSW tuning, quantization trade-offs và monitoring trong production.

