# Redis Thực Chiến: Ứng Dụng Từng Kiểu Dữ Liệu Vào Production

![Redis Thực Chiến- Ứng Dụng Từng Kiểu Dữ Liệu Vào Production.jpeg](../images/4a5ea9be-0751-478c-bf1d-a4a5b4788226.jpeg)

Bài 7 giới thiệu commands của từng kiểu dữ liệu. Bài này đi sâu hơn — mỗi kiểu dữ liệu giải quyết **bài toán thực tế cụ thể** trong [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev): String cho session management, Hash cho user profile cache, List cho activity feed, Set cho tracking, Sorted Set cho real-time leaderboard. Tất cả đều có code Python/Java sẵn dùng.

## 1\. Kết Nối Redis Từ Application

### Python (redis-py)

```python
import redis
import json
from typing import Optional, Any
from datetime import timedelta

# Kết nối cơ bản
r = redis.Redis(
    host     = "localhost",
    port     = 6379,
    password = "password123",
    db       = 0,
    decode_responses = True  # tự decode bytes → str
)

# Kiểm tra kết nối
r.ping()  # → True

# Connection Pool (production)
pool = redis.ConnectionPool(
    host             = "localhost",
    port             = 6379,
    password         = "password123",
    db               = 0,
    max_connections  = 20,
    decode_responses = True
)
r = redis.Redis(connection_pool=pool)
```

### Java (Spring Boot + Lettuce)

```java
// application.yml
spring:
  data:
    redis:
      host:     localhost
      port:     6379
      password: password123
      lettuce:
        pool:
          max-active: 20
          max-idle:   10
          min-idle:   5

// RedisConfig.java
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // Serialize key là String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Serialize value là JSON
        Jackson2JsonRedisSerializer<Object> serializer =
            new Jackson2JsonRedisSerializer<>(Object.class);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        return template;
    }
}
```

## 2\. String — Session Management

Session là use case kinh điển nhất của Redis String. Thay vì lưu session trong database (chậm) hoặc cookie (không an toàn với sensitive data), lưu trong Redis với TTL tự động expire.

```python
import secrets
import json

class SessionService:
    SESSION_PREFIX = "session:"
    SESSION_TTL    = 86400  # 24 giờ

    def __init__(self, redis_client: redis.Redis):
        self.r = redis_client

    def create_session(self, user_id: int, extra_data: dict = None) -> str:
        """Tạo session mới, trả về session_id"""
        session_id = secrets.token_urlsafe(32)  # random token

        session_data = {
            "user_id":    user_id,
            "created_at": datetime.now().isoformat(),
            **(extra_data or {})
        }

        key = f"{self.SESSION_PREFIX}{session_id}"
        self.r.setex(
            key,
            self.SESSION_TTL,
            json.dumps(session_data)
        )
        return session_id

    def get_session(self, session_id: str) -> Optional[dict]:
        """Lấy session data, None nếu expired hoặc không tồn tại"""
        key  = f"{self.SESSION_PREFIX}{session_id}"
        data = self.r.get(key)

        if not data:
            return None

        # Refresh TTL mỗi khi user active (sliding window)
        self.r.expire(key, self.SESSION_TTL)
        return json.loads(data)

    def delete_session(self, session_id: str) -> bool:
        """Xóa session (logout)"""
        return bool(self.r.delete(f"{self.SESSION_PREFIX}{session_id}"))

    def extend_session(self, session_id: str, ttl: int = None) -> bool:
        """Gia hạn session"""
        key = f"{self.SESSION_PREFIX}{session_id}"
        return bool(self.r.expire(key, ttl or self.SESSION_TTL))


# Sử dụng
session_svc = SessionService(r)

# Login → tạo session
session_id = session_svc.create_session(
    user_id    = 1,
    extra_data = {"device": "mobile", "ip": "1.2.3.4"}
)
# → "abc123xyz..."

# Mỗi request → verify session
session = session_svc.get_session(session_id)
if not session:
    raise Exception("Session expired, please login again")
user_id = session["user_id"]

# Logout → xóa session
session_svc.delete_session(session_id)
```

**Tại sao Redis tốt hơn database cho session:**

```java
Database session:
  → Mỗi request phải query DB để verify
  → DB load tăng theo số concurrent users
  → Khó scale ngang

Redis session:
  → Sub-millisecond lookup
  → TTL tự động expire → không cần cleanup job
  → Scale dễ với Redis Cluster
```

## 3\. String — Cache Pattern

### Cache-aside (Lazy Loading)

Pattern phổ biến nhất — application tự quản lý cache.

```python
class CourseCache:
    CACHE_TTL = 300  # 5 phút

    def __init__(self, redis_client, db_session):
        self.r  = redis_client
        self.db = db_session

    def get_published_courses(self, category: str = None) -> list:
        """
        Cache-aside pattern:
        1. Check cache → hit: return
        2. Miss: query DB → store in cache → return
        """
        cache_key = f"cache:courses:published:{category or 'all'}"

        # 1. Check cache
        cached = self.r.get(cache_key)
        if cached:
            return json.loads(cached)  # Cache HIT

        # 2. Cache MISS — query database
        courses = self.db.query("""
            SELECT id, title, price, rating, enrolled_count, category
            FROM courses
            WHERE course_status = 'PUBLISHED'
            AND (%s IS NULL OR category = %s)
            ORDER BY enrolled_count DESC
        """, (category, category))

        # 3. Store in cache
        self.r.setex(cache_key, self.CACHE_TTL, json.dumps(courses))

        return courses

    def invalidate_courses_cache(self):
        """Xóa cache khi có thay đổi (course thêm/sửa/xóa)"""
        # SCAN thay vì KEYS * — an toàn cho production
        cursor = 0
        while True:
            cursor, keys = self.r.scan(
                cursor, match="cache:courses:*", count=100
            )
            if keys:
                self.r.delete(*keys)
            if cursor == 0:
                break
```

### Write-through Cache

Cache luôn được update đồng thời với database — không bao giờ stale.

```python
def update_course_price(self, course_id: int, new_price: float):
    """Write-through: update DB và cache đồng thời"""
    # 1. Update database
    self.db.execute(
        "UPDATE courses SET price = %s WHERE id = %s",
        (new_price, course_id)
    )

    # 2. Update cache ngay lập tức
    cache_key = f"cache:course:{course_id}"
    cached = self.r.get(cache_key)
    if cached:
        course = json.loads(cached)
        course["price"] = new_price
        self.r.setex(cache_key, 3600, json.dumps(course))
```

### Cache Stampede Protection (Dog-piling)

Khi cache expire, nhiều requests cùng lúc hit database — gây overload.

```python
def get_course_with_lock(self, course_id: int) -> dict:
    """
    Dùng distributed lock để tránh cache stampede.
    Chỉ 1 request rebuild cache, các request khác chờ.
    """
    cache_key = f"cache:course:{course_id}"
    lock_key  = f"lock:rebuild:{cache_key}"

    # Check cache
    cached = self.r.get(cache_key)
    if cached:
        return json.loads(cached)

    # Thử acquire lock
    acquired = self.r.set(lock_key, "1", nx=True, ex=10)

    if acquired:
        # Lock acquired — rebuild cache
        try:
            course = self.db.query_one("SELECT * FROM courses WHERE id = %s", (course_id,))
            self.r.setex(cache_key, 300, json.dumps(course))
            return course
        finally:
            self.r.delete(lock_key)
    else:
        # Lock không acquire được — chờ cache được build
        import time
        for _ in range(10):
            time.sleep(0.1)
            cached = self.r.get(cache_key)
            if cached:
                return json.loads(cached)
        # Fallback: query database trực tiếp
        return self.db.query_one("SELECT * FROM courses WHERE id = %s", (course_id,))
```

## 4\. Hash — User Profile & Course Stats

```python
class UserProfileCache:

    def __init__(self, redis_client):
        self.r = redis_client

    def cache_user_profile(self, user: dict, ttl: int = 3600):
        """Cache user profile dưới dạng Hash"""
        key = f"user:{user['id']}:profile"
        self.r.hset(key, mapping={
            "id":             user["id"],
            "email":          user["email"],
            "first_name":     user["first_name"],
            "last_name":      user["last_name"],
            "account_status": user["account_status"],
            "account_type":   user["account_type"],
            "point_balance":  user.get("point_balance", 0),
            "enrolled_count": user.get("enrolled_count", 0),
        })
        self.r.expire(key, ttl)

    def get_user_profile(self, user_id: int) -> Optional[dict]:
        key    = f"user:{user_id}:profile"
        result = self.r.hgetall(key)
        return result if result else None

    def update_point_balance(self, user_id: int, delta: int) -> int:
        """
        Atomic increment điểm thưởng — an toàn với concurrent requests.
        Không cần GET → calculate → SET
        """
        key = f"user:{user_id}:profile"
        return self.r.hincrby(key, "point_balance", delta)

    def increment_enrolled_count(self, user_id: int) -> int:
        """Tăng số khóa học đã enroll"""
        key = f"user:{user_id}:profile"
        return self.r.hincrby(key, "enrolled_count", 1)


class CourseStatsCache:

    def __init__(self, redis_client):
        self.r = redis_client

    def increment_view(self, course_id: int) -> int:
        """
        Track số lượt xem — HINCRBY atomic, không race condition.
        Batch flush vào DB mỗi 5 phút thay vì write mỗi view.
        """
        key  = f"course:{course_id}:stats"
        views = self.r.hincrby(key, "views", 1)
        self.r.expire(key, 3600)
        return views

    def increment_enrollment(self, course_id: int) -> int:
        key = f"course:{course_id}:stats"
        return self.r.hincrby(key, "enrolled_count", 1)

    def get_stats(self, course_id: int) -> dict:
        key    = f"course:{course_id}:stats"
        return self.r.hgetall(key) or {}

    def flush_to_database(self, db, course_ids: list):
        """
        Batch update DB từ Redis cache (chạy mỗi 5 phút).
        Giảm DB writes từ 1000 writes/s xuống còn ~10 writes/5min.
        """
        for course_id in course_ids:
            stats = self.get_stats(course_id)
            if stats:
                db.execute("""
                    UPDATE courses
                    SET view_count      = view_count + %s,
                        enrolled_count  = enrolled_count + %s,
                        updated_at      = NOW()
                    WHERE id = %s
                """, (
                    int(stats.get("views", 0)),
                    int(stats.get("enrolled_count", 0)),
                    course_id
                ))
                # Reset counter sau khi flush
                self.r.hdel(f"course:{course_id}:stats", "views", "enrolled_count")
```

## 5\. List — Activity Feed & Job Queue

### Activity Feed

```python
class ActivityFeed:
    MAX_FEED_SIZE = 100  # Giữ 100 activities gần nhất

    def __init__(self, redis_client):
        self.r = redis_client

    def push_activity(self, user_id: int, activity: dict):
        """Thêm activity mới vào đầu feed"""
        key = f"feed:user:{user_id}"
        activity["timestamp"] = datetime.now().isoformat()

        # Thêm vào đầu (newest first)
        self.r.lpush(key, json.dumps(activity))

        # Giữ tối đa MAX_FEED_SIZE items
        self.r.ltrim(key, 0, self.MAX_FEED_SIZE - 1)

    def get_feed(self, user_id: int, page: int = 0,
                 page_size: int = 20) -> list:
        """Lấy activities theo page"""
        key   = f"feed:user:{user_id}"
        start = page * page_size
        end   = start + page_size - 1
        items = self.r.lrange(key, start, end)
        return [json.loads(item) for item in items]

    def get_unread_count(self, user_id: int,
                          last_read_time: str) -> int:
        """Đếm số activities chưa đọc"""
        key   = f"feed:user:{user_id}"
        items = self.r.lrange(key, 0, -1)
        count = 0
        for item in items:
            activity = json.loads(item)
            if activity["timestamp"] > last_read_time:
                count += 1
            else:
                break  # List đã sort theo thời gian → có thể dừng sớm
        return count


# Sử dụng
feed = ActivityFeed(r)

# Khi user enroll khóa học
feed.push_activity(user_id=1, activity={
    "type":       "COURSE_ENROLLED",
    "course_id":  1,
    "course_title": "Spring Boot từ Zero đến Hero",
    "message":    "Bạn đã đăng ký khóa học thành công"
})

# Khi hoàn thành khóa học
feed.push_activity(user_id=1, activity={
    "type":    "COURSE_COMPLETED",
    "course_id": 1,
    "message": "Chúc mừng! Bạn đã hoàn thành khóa học"
})

# Load feed
activities = feed.get_feed(user_id=1, page=0, page_size=10)
```

### Job Queue

```python
class EmailJobQueue:
    QUEUE_KEY = "queue:emails"

    def __init__(self, redis_client):
        self.r = redis_client

    def enqueue(self, job: dict):
        """Thêm job vào cuối queue"""
        self.r.rpush(self.QUEUE_KEY, json.dumps(job))

    def dequeue(self, timeout: int = 30) -> Optional[dict]:
        """
        Lấy job từ đầu queue — blocking nếu queue rỗng.
        timeout=0: block mãi mãi
        """
        result = self.r.blpop(self.QUEUE_KEY, timeout=timeout)
        if result:
            _, data = result
            return json.loads(data)
        return None

    def queue_size(self) -> int:
        return self.r.llen(self.QUEUE_KEY)


# Worker process
def email_worker():
    queue = EmailJobQueue(r)
    print("Email worker started...")

    while True:
        job = queue.dequeue(timeout=30)
        if job:
            send_email(
                to      = job["to"],
                subject = job["subject"],
                body    = job["body"]
            )
            print(f"Sent email to {job['to']}")


# Producer (khi có order mới)
queue = EmailJobQueue(r)
queue.enqueue({
    "to":      "nam@gmail.com",
    "subject": "Xác nhận đơn hàng #001",
    "body":    "Cảm ơn bạn đã mua khóa học..."
})
```

## 6\. Set — Tracking & Unique Counting

```python
class TrackingService:

    def __init__(self, redis_client):
        self.r = redis_client

    def track_course_view(self, course_id: int, user_id: int):
        """
        Track unique viewers của course theo ngày.
        Set tự động dedup — 1 user xem 100 lần vẫn chỉ count 1.
        """
        today = datetime.now().strftime("%Y-%m-%d")
        key   = f"views:course:{course_id}:{today}"

        self.r.sadd(key, str(user_id))
        self.r.expire(key, 7 * 86400)  # giữ 7 ngày

    def get_unique_viewers(self, course_id: int, date: str = None) -> int:
        date = date or datetime.now().strftime("%Y-%m-%d")
        key  = f"views:course:{course_id}:{date}"
        return self.r.scard(key)

    def track_online_user(self, user_id: int):
        """Tracking users đang online (active trong 5 phút gần nhất)"""
        key = "online:users"
        self.r.sadd(key, str(user_id))
        self.r.expire(key, 300)  # TTL 5 phút
        # Nếu user không gửi heartbeat trong 5 phút → tự xóa

    def get_online_count(self) -> int:
        return self.r.scard("online:users")

    def get_common_interests(self, user_id_1: int,
                              user_id_2: int) -> set:
        """
        Tìm tags chung giữa 2 users.
        Dùng SINTER — intersection của 2 sets.
        """
        key1 = f"user:{user_id_1}:interests"
        key2 = f"user:{user_id_2}:interests"
        return self.r.sinter(key1, key2)

    def add_user_interests(self, user_id: int, tags: list):
        key = f"user:{user_id}:interests"
        if tags:
            self.r.sadd(key, *tags)
            self.r.expire(key, 86400)

    def find_courses_with_all_tags(self, required_tags: list) -> set:
        """
        Tìm courses có TẤT CẢ tags được yêu cầu.
        Dùng SINTER trên nhiều sets.
        """
        keys = [f"index:tag:{tag}:courses" for tag in required_tags]
        if not keys:
            return set()
        return self.r.sinter(*keys)

    def index_course_tags(self, course_id: int, tags: list):
        """
        Build inverted index: tag → set of course_ids.
        Cho phép tìm courses theo tags nhanh.
        """
        for tag in tags:
            key = f"index:tag:{tag}:courses"
            self.r.sadd(key, str(course_id))
```

## 7\. Sorted Set — Leaderboard & Real-time Ranking

```python
class LeaderboardService:

    def __init__(self, redis_client):
        self.r = redis_client

    def add_or_update_score(self, leaderboard: str,
                             user_id: int, score: float):
        """Thêm/cập nhật score của user"""
        self.r.zadd(leaderboard, {str(user_id): score})

    def increment_score(self, leaderboard: str,
                         user_id: int, delta: float) -> float:
        """Tăng score của user (khi hoàn thành bài học, mua khóa học...)"""
        return self.r.zincrby(leaderboard, delta, str(user_id))

    def get_top_n(self, leaderboard: str, n: int = 10) -> list:
        """Lấy top N users theo score giảm dần"""
        results = self.r.zrevrange(
            leaderboard, 0, n - 1, withscores=True
        )
        return [
            {"user_id": int(uid), "score": score, "rank": i + 1}
            for i, (uid, score) in enumerate(results)
        ]

    def get_user_rank(self, leaderboard: str,
                       user_id: int) -> Optional[dict]:
        """Lấy rank và score của 1 user"""
        rank  = self.r.zrevrank(leaderboard, str(user_id))
        score = self.r.zscore(leaderboard, str(user_id))

        if rank is None:
            return None

        return {
            "user_id": user_id,
            "rank":    rank + 1,   # 0-based → 1-based
            "score":   score
        }

    def get_surrounding_users(self, leaderboard: str,
                               user_id: int, n: int = 2) -> list:
        """
        Lấy N users xung quanh user hiện tại.
        Ví dụ: user hạng 50 → lấy hạng 48-52.
        """
        rank = self.r.zrevrank(leaderboard, str(user_id))
        if rank is None:
            return []

        start = max(0, rank - n)
        end   = rank + n

        results = self.r.zrevrange(
            leaderboard, start, end, withscores=True
        )
        return [
            {
                "user_id": int(uid),
                "score":   score,
                "rank":    start + i + 1,
                "is_me":   int(uid) == user_id
            }
            for i, (uid, score) in enumerate(results)
        ]


# Sử dụng
lb = LeaderboardService(r)
MONTHLY_LB = "leaderboard:points:2025-03"

# Users hoàn thành bài học → cộng 10 điểm
lb.increment_score(MONTHLY_LB, user_id=1, delta=10)
lb.increment_score(MONTHLY_LB, user_id=2, delta=25)
lb.increment_score(MONTHLY_LB, user_id=3, delta=15)

# Lấy top 3
top3 = lb.get_top_n(MONTHLY_LB, n=3)
# → [{"user_id": 2, "score": 25, "rank": 1}, ...]

# Xem rank của user 1
rank_info = lb.get_user_rank(MONTHLY_LB, user_id=1)
# → {"user_id": 1, "rank": 2, "score": 10.0}

# Xem users xung quanh user 1
surrounding = lb.get_surrounding_users(MONTHLY_LB, user_id=1, n=2)
```

## 8\. Pipeline — Batch Commands

Gom nhiều commands vào một network round trip — giảm latency đáng kể.

```python
def get_multiple_course_stats(course_ids: list) -> dict:
    """
    Thay vì N round trips → 1 round trip với pipeline.
    """
    pipe   = r.pipeline()
    results = {}

    # Queue tất cả commands
    for course_id in course_ids:
        pipe.hgetall(f"course:{course_id}:stats")

    # Execute một lần
    stats_list = pipe.execute()

    for course_id, stats in zip(course_ids, stats_list):
        results[course_id] = stats

    return results


def batch_cache_courses(courses: list, ttl: int = 300):
    """Batch set nhiều course cache cùng lúc"""
    pipe = r.pipeline()

    for course in courses:
        key = f"cache:course:{course['id']}"
        pipe.setex(key, ttl, json.dumps(course))

    pipe.execute()
    print(f"Cached {len(courses)} courses in 1 round trip")
```

## 9\. Thực Hành Tổng Hợp — Dashboard Real-time

Kết hợp tất cả để xây dựng dashboard real-time:

```python
class RealtimeDashboard:
    """Dashboard real-time cho admin nguyentienkhoi.hashnode.dev"""

    def __init__(self, redis_client):
        self.r = redis_client

    def get_dashboard_stats(self) -> dict:
        """
        Lấy toàn bộ stats bằng pipeline — 1 round trip.
        """
        pipe = self.r.pipeline()

        # Queue tất cả commands
        pipe.get("stats:total_users")
        pipe.get("stats:total_courses")
        pipe.get("stats:revenue:today")
        pipe.scard("online:users")
        pipe.llen("queue:emails")
        pipe.zcard("leaderboard:points:2025-03")

        # Execute
        (
            total_users,
            total_courses,
            revenue_today,
            online_users,
            email_queue,
            leaderboard_size
        ) = pipe.execute()

        return {
            "total_users":     int(total_users or 0),
            "total_courses":   int(total_courses or 0),
            "revenue_today":   float(revenue_today or 0),
            "online_users":    online_users,
            "email_queue":     email_queue,
            "leaderboard_size": leaderboard_size,
            "updated_at":      datetime.now().isoformat()
        }

    def increment_revenue(self, amount: float):
        """Cộng doanh thu vào counter hôm nay"""
        today = datetime.now().strftime("%Y-%m-%d")
        key   = f"stats:revenue:{today}"
        self.r.incrbyfloat(key, amount)
        self.r.expire(key, 7 * 86400)  # giữ 7 ngày


# Simulate dashboard
dashboard = RealtimeDashboard(r)

# Setup initial stats
r.set("stats:total_users",   1520)
r.set("stats:total_courses", 12)
r.set("stats:revenue:today", 4500000)
r.sadd("online:users",       "user:1", "user:2", "user:3")

# Khi có order mới
dashboard.increment_revenue(799000)

# Lấy dashboard stats (1 round trip)
stats = dashboard.get_dashboard_stats()
print(stats)
# {
#   "total_users": 1520,
#   "total_courses": 12,
#   "revenue_today": 5299000.0,
#   "online_users": 3,
#   "email_queue": 0,
#   "leaderboard_size": 3,
#   "updated_at": "2025-03-15T10:30:00"
# }
```

## Tổng Kết


| Use Case | Kiểu dữ liệu | Key pattern |
|---|---|---|
| Session | String | session:{session_id} |
| Cache object | String / Hash | cache:{entity}:{id} |
| User profile | Hash | user:{id}:profile |
| Course stats | Hash | course:{id}:stats |
| Activity feed | List | feed:user:{id} |
| Job queue | List | queue:{type} |
| Unique visitors | Set | views:course:{id}:{date} |
| Online users | Set | online:users |
| Tag index | Set | index:tag:{tag}:courses |
| Leaderboard | Sorted Set | leaderboard:{type}:{period} |
| Counter | String (INCR) | stats:{metric}:{period} |
| Distributed lock | String (NX) | lock:{resource}:{id} |



**Nguyên tắc sử dụng Redis hiệu quả:**

*   Dùng **Pipeline** khi cần nhiều commands → giảm round trips
    
*   Luôn đặt **TTL** cho keys — tránh memory leak
    
*   Dùng **SCAN** thay vì `KEYS *` ở production
    
*   **Prefix** keys rõ ràng để dễ quản lý và invalidate
    
*   Không lưu object quá lớn trong Redis — Redis là speed layer, không phải primary storage
    

Bài tiếp theo chúng ta sẽ học **Cache Patterns chuyên sâu** — Cache-aside, Write-through, Write-behind, Read-through cùng với Rate Limiting và Distributed Lock trong production.

