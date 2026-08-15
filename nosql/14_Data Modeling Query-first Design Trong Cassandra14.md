# Data Modeling: Query-first Design Trong Cassandra

![Data Modeling- Query-first Design Trong Cassandra.jpeg](../images/4d260e90-3df0-4f9c-954c-4aaecf09a341.jpeg)

Bài 12 bạn đã học cú pháp CQL. Bài này là bài **quan trọng nhất** trong Cassandra series — **Query-first Design**: quy trình thiết kế schema ngược hoàn toàn với SQL. Trong SQL, bạn thiết kế bảng trước rồi viết query sau. Trong Cassandra, bạn phải biết query trước rồi mới thiết kế table. Hiểu sai điều này là lý do 90% developer gặp vấn đề performance với Cassandra.

## 1\. Tại Sao Phải Query-first?

```java
SQL approach (Entity-first):
  1. Phân tích entities: User, Course, Order, Enrollment
  2. Thiết kế tables, relationships
  3. Viết bất kỳ query nào với JOIN

Cassandra approach (Query-first):
  1. Liệt kê TẤT CẢ queries ứng dụng cần
  2. Thiết kế 1 table cho mỗi query pattern
  3. Chấp nhận data duplication

Tại sao Cassandra không linh hoạt như SQL?
  → Cassandra không có JOIN
  → Cassandra không thể filter tùy ý
  → Cassandra phải biết trước data nằm ở node nào
  → Trade-off: đánh đổi query flexibility lấy write throughput + availability
```

## 2\. Quy Trình Query-first Design

```java
Bước 1: Xác định entities
Bước 2: Liệt kê tất cả queries (access patterns)
Bước 3: Với mỗi query → thiết kế 1 table
Bước 4: Xác định Primary Key cho mỗi table
         → Partition Key = điều kiện WHERE (equality)
         → Clustering Key = điều kiện ORDER BY + range
Bước 5: Xác định data duplication cần thiết
```

## 3\. Case Study — [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev) Tracking System

### Bước 1: Xác định Entities

```java
Entities:
  - User (user_id, email, name)
  - Course (course_id, title, category)
  - Lecture (lecture_id, course_id, title, duration)
  - Video Event (play, pause, seek, end)
  - Learning Progress (watched %, completed)
```

### Bước 2: Liệt Kê Tất Cả Access Patterns

```java
Q1. Lấy tất cả events của course X trong ngày Y
    (admin analytics: "Hôm nay course X có bao nhiêu lượt xem?")

Q2. Lấy tiến độ học của user X trong course Y
    (student dashboard: "Tôi đã học đến đâu?")

Q3. Lấy N events gần nhất của user X
    (user activity feed: "Gần đây bạn đã học...")

Q4. Lấy tất cả lectures user X đã complete trong course Y
    (completion tracking: "Bài nào đã xong?")

Q5. Lấy tổng thời gian xem của user X theo tháng
    (monthly stats: "Tháng này tôi học bao nhiêu giờ?")

Q6. Lấy top 10 courses được xem nhiều nhất tuần này
    (trending: "Khóa học hot nhất tuần")
```

### Bước 3: Thiết Kế Table Cho Mỗi Query

```sql
-- Q1: Events của course X trong ngày Y
-- WHERE course_id = ? AND event_date = ?
-- ORDER BY event_at DESC
CREATE TABLE events_by_course_day (
    course_id   UUID,
    event_date  DATE,         -- ← composite partition key
    event_at    TIMESTAMP,
    user_id     UUID,
    lecture_id  UUID,
    action      TEXT,
    position    INT,
    duration    INT,
    device      TEXT,
    PRIMARY KEY ((course_id, event_date), event_at, user_id)
) WITH CLUSTERING ORDER BY (event_at DESC, user_id ASC)
AND default_time_to_live = 7776000;  -- 90 ngày

-- Q2: Tiến độ của user X trong course Y
-- WHERE user_id = ? AND course_id = ?
-- GROUP BY lecture (xem từng bài đã đến đâu)
CREATE TABLE progress_by_user_course (
    user_id      UUID,
    course_id    UUID,         -- ← composite partition key
    lecture_id   UUID,
    watched_pct  FLOAT,
    completed    BOOLEAN,
    last_watched TIMESTAMP,
    watch_count  INT,
    PRIMARY KEY ((user_id, course_id), lecture_id)
) WITH CLUSTERING ORDER BY (lecture_id ASC);

-- Q3: N events gần nhất của user X
-- WHERE user_id = ?
-- ORDER BY event_at DESC LIMIT N
CREATE TABLE events_by_user (
    user_id    UUID,
    event_at   TIMESTAMP,
    course_id  UUID,
    lecture_id UUID,
    action     TEXT,
    position   INT,
    PRIMARY KEY (user_id, event_at)
) WITH CLUSTERING ORDER BY (event_at DESC)
AND default_time_to_live = 2592000;  -- 30 ngày

-- Q4: Lectures đã complete của user X trong course Y
-- WHERE user_id = ? AND course_id = ? AND completed = true
CREATE TABLE completed_lectures_by_user (
    user_id      UUID,
    course_id    UUID,
    lecture_id   UUID,
    completed_at TIMESTAMP,
    score        FLOAT,         -- điểm quiz nếu có
    PRIMARY KEY ((user_id, course_id), completed_at, lecture_id)
) WITH CLUSTERING ORDER BY (completed_at DESC, lecture_id ASC);

-- Q5: Thời gian học của user X theo tháng
-- WHERE user_id = ? AND month = ?
CREATE TABLE user_watch_time_by_month (
    user_id     UUID,
    month       TEXT,           -- "2025-03"
    course_id   UUID,
    total_secs  COUNTER,        -- tổng giây đã xem
    session_count COUNTER,      -- số sessions
    PRIMARY KEY ((user_id, month), course_id)
) WITH CLUSTERING ORDER BY (course_id ASC);

-- Q6: Top courses theo views trong tuần
-- Không thể làm trực tiếp trong Cassandra → dùng pre-aggregated table
CREATE TABLE weekly_course_views (
    week        TEXT,           -- "2025-W12"
    course_id   UUID,
    title       TEXT,           -- denormalized
    category    TEXT,           -- denormalized
    view_count  COUNTER,
    PRIMARY KEY (week, view_count, course_id)
) WITH CLUSTERING ORDER BY (view_count DESC, course_id ASC);
-- ⚠️ COUNTER không dùng được trong clustering key!
-- Xem cách giải quyết ở phần 6
```

## 4\. Denormalization — Chấp Nhận Data Duplicate

Trong Cassandra, **data duplication là bình thường và được khuyến khích** — đây là trade-off để có performance tốt.

```sql
-- Ví dụ: Lưu course title trong events table
-- Thay vì JOIN sang courses table khi query

-- events_by_course_day: lưu lecture_title
-- events_by_user: lưu course_title, lecture_title

-- Khi course title thay đổi:
-- → Cần UPDATE tất cả tables có chứa title
-- → Trade-off: write phức tạp hơn, read đơn giản hơn
```

**Chiến lược denormalize đúng:**

```sql
-- Table chính (source of truth)
CREATE TABLE courses (
    course_id UUID PRIMARY KEY,
    title     TEXT,
    category  TEXT,
    price     DECIMAL,
    status    TEXT
);

-- Table denormalized cho user feed
CREATE TABLE user_activity_feed (
    user_id      UUID,
    activity_at  TIMESTAMP,
    -- Denormalized fields — copy từ courses và lectures
    course_id    UUID,
    course_title TEXT,      -- ← duplicate
    category     TEXT,      -- ← duplicate
    lecture_id   UUID,
    lecture_title TEXT,     -- ← duplicate
    action       TEXT,
    PRIMARY KEY (user_id, activity_at)
) WITH CLUSTERING ORDER BY (activity_at DESC);
```

## 5\. Hot Partition Problem & Giải Pháp

**Hot partition**: khi một partition nhận quá nhiều traffic hơn các partitions khác — node đó trở thành bottleneck.

```sql
-- ❌ Vấn đề: Partition Key là course_id
-- Course Spring Boot có 10,000 students đang xem cùng lúc
-- → Tất cả writes vào 1 node → node đó quá tải
CREATE TABLE events_bad (
    course_id  UUID,     -- ← hot partition khi course popular
    event_at   TIMESTAMP,
    user_id    UUID,
    action     TEXT,
    PRIMARY KEY (course_id, event_at, user_id)
);

-- ✅ Giải pháp 1: Thêm time bucket vào Partition Key
-- Mỗi ngày là partition riêng → phân tán load theo thời gian
CREATE TABLE events_by_course_day (
    course_id  UUID,
    event_date DATE,      -- ← thêm date để tạo nhiều partitions
    event_at   TIMESTAMP,
    user_id    UUID,
    action     TEXT,
    PRIMARY KEY ((course_id, event_date), event_at, user_id)
);
-- Partition (course_id=spring_boot, date=2025-03-15) chỉ chứa data 1 ngày

-- ✅ Giải pháp 2: Bucket số (shard within partition)
-- Thêm bucket ngẫu nhiên để phân tán trên nhiều partitions
CREATE TABLE events_bucketed (
    course_id  UUID,
    bucket     INT,       -- random 0-9, 0-99...
    event_date DATE,
    event_at   TIMESTAMP,
    user_id    UUID,
    action     TEXT,
    PRIMARY KEY ((course_id, bucket, event_date), event_at, user_id)
);
-- Khi write: bucket = hash(user_id) % 10
-- Khi read: phải query tất cả buckets (0-9) rồi merge
```

```python
# Python: write với bucket
import hashlib

def get_bucket(user_id: str, num_buckets: int = 10) -> int:
    return int(hashlib.md5(user_id.encode()).hexdigest(), 16) % num_buckets

def insert_event(session, course_id, user_id, action, position):
    bucket = get_bucket(str(user_id))
    session.execute("""
        INSERT INTO events_bucketed
        (course_id, bucket, event_date, event_at, user_id, action, position)
        VALUES (%s, %s, %s, toTimestamp(now()), %s, %s, %s)
    """, (course_id, bucket, date.today(), user_id, action, position))

# Read: query tất cả buckets
def get_events(session, course_id, event_date):
    results = []
    for bucket in range(10):
        rows = session.execute("""
            SELECT * FROM events_bucketed
            WHERE course_id  = %s
              AND bucket     = %s
              AND event_date = %s
            LIMIT 1000
        """, (course_id, bucket, event_date))
        results.extend(rows)
    return sorted(results, key=lambda x: x.event_at, reverse=True)
```

## 6\. Pre-aggregation Pattern — Thay Thế GROUP BY

Cassandra không có GROUP BY. Giải pháp: pre-aggregate khi write.

```sql
-- ❌ Không thể làm trong Cassandra:
-- SELECT course_id, COUNT(*) FROM events GROUP BY course_id

-- ✅ Pre-aggregate ngay khi write event
-- Mỗi khi có event mới → update counter

-- Table events (append-only)
CREATE TABLE events_by_course_day (
    course_id  UUID,
    event_date DATE,
    event_at   TIMESTAMP,
    user_id    UUID,
    action     TEXT,
    PRIMARY KEY ((course_id, event_date), event_at, user_id)
);

-- Table aggregate (pre-computed counts)
CREATE TABLE course_daily_stats (
    course_id    UUID,
    date         DATE,
    total_events COUNTER,
    unique_users SET<UUID>,    -- ← KHÔNG ĐƯỢC: SET không dùng với COUNTER
    plays        COUNTER,
    completions  COUNTER,
    PRIMARY KEY (course_id, date)
) WITH CLUSTERING ORDER BY (date DESC);
-- ⚠️ COUNTER table không thể có SET — cần tách table riêng
```

**Giải pháp đúng — tách table:**

```sql
-- Table 1: Counters
CREATE TABLE course_daily_counters (
    course_id   UUID,
    date        DATE,
    total_events COUNTER,
    plays       COUNTER,
    pauses      COUNTER,
    completions COUNTER,
    PRIMARY KEY (course_id, date)
) WITH CLUSTERING ORDER BY (date DESC);

-- Table 2: Unique users (dùng Redis HyperLogLog hoặc Cassandra Set riêng)
-- Không thể dùng SET trong COUNTER table → giải pháp khác:
-- Option A: Bloom filter trong application
-- Option B: Redis HyperLogLog cho approximate count
-- Option C: Separate Cassandra table với TTL
CREATE TABLE course_daily_users (
    course_id UUID,
    date      DATE,
    user_id   UUID,
    PRIMARY KEY ((course_id, date), user_id)
) WITH default_time_to_live = 86400;
-- Xóa sau 24h vì chỉ cần đếm unique users của ngày đó

-- Application logic khi có event mới:
-- 1. INSERT vào events_by_course_day
-- 2. UPDATE course_daily_counters SET total_events = total_events + 1
-- 3. INSERT vào course_daily_users (tự xóa sau 24h)
-- 4. SELECT COUNT(*) FROM course_daily_users → unique users
```

## 7\. Materialized Views

Cassandra hỗ trợ **Materialized View** — tự động sync data từ base table.

```sql
-- Base table
CREATE TABLE video_events (
    course_id  UUID,
    event_date DATE,
    event_at   TIMESTAMP,
    user_id    UUID,
    action     TEXT,
    device     TEXT,
    PRIMARY KEY ((course_id, event_date), event_at, user_id)
);

-- Materialized View: query theo user (partition key khác)
CREATE MATERIALIZED VIEW events_by_user_date AS
    SELECT course_id, event_date, event_at, user_id, action, device
    FROM video_events
    WHERE user_id IS NOT NULL
      AND event_date IS NOT NULL
      AND event_at IS NOT NULL
      AND course_id IS NOT NULL
PRIMARY KEY ((user_id, event_date), event_at, course_id)
WITH CLUSTERING ORDER BY (event_at DESC, course_id ASC);

-- Bây giờ có thể query theo user_id
SELECT * FROM events_by_user_date
WHERE user_id    = aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
  AND event_date = '2025-03-15';
```

**⚠️ Lưu ý Materialized View:**

*   Write vào base table → Cassandra tự sync sang MV (overhead write ~2x)
    
*   Không thể thêm columns không có trong base table
    
*   Tất cả base table columns phải IS NOT NULL trong MV
    
*   Không support ở nhiều production systems (Cassandra 4.0+ cải thiện nhiều)
    

## 8\. Ứng Dụng Python — Cassandra Driver

```python
from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider
from cassandra.policies import DCAwareRoundRobinPolicy
from cassandra import ConsistencyLevel
from cassandra.query import SimpleStatement, BatchStatement
import uuid
from datetime import datetime, date

# ─── Kết nối ───
def get_session():
    cluster = Cluster(
        contact_points = ["localhost"],
        port           = 9042,
        load_balancing_policy = DCAwareRoundRobinPolicy(local_dc="datacenter1"),
        # auth_provider = PlainTextAuthProvider(
        #     username="cassandra", password="cassandra"
        # )
    )
    session = cluster.connect("foxdev_dev")
    session.default_consistency_level = ConsistencyLevel.LOCAL_QUORUM
    return session


# ─── Prepared Statements — quan trọng cho performance ───
class VideoTrackingRepo:

    def __init__(self, session):
        self.session = session
        self._prepare_statements()

    def _prepare_statements(self):
        """
        Prepared statements: parse 1 lần, execute nhiều lần.
        Nhanh hơn simple statement + giảm overhead.
        """
        self.insert_event = self.session.prepare("""
            INSERT INTO events_by_course_day
                (course_id, event_date, event_at, user_id, lecture_id,
                 action, position, duration, device)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            USING TTL 7776000
        """)

        self.insert_user_event = self.session.prepare("""
            INSERT INTO events_by_user
                (user_id, event_at, course_id, lecture_id, action, position)
            VALUES (?, ?, ?, ?, ?, ?)
            USING TTL 2592000
        """)

        self.update_counter = self.session.prepare("""
            UPDATE course_daily_counters
            SET total_events = total_events + 1,
                plays        = plays + ?
            WHERE course_id = ? AND date = ?
        """)

        self.select_course_events = self.session.prepare("""
            SELECT * FROM events_by_course_day
            WHERE course_id  = ?
              AND event_date = ?
            LIMIT ?
        """)

        self.select_user_events = self.session.prepare("""
            SELECT * FROM events_by_user
            WHERE user_id  = ?
            LIMIT ?
        """)

    def record_event(self, course_id: uuid.UUID, user_id: uuid.UUID,
                      lecture_id: uuid.UUID, action: str,
                      position: int, device: str = "web"):
        """Ghi event vào tất cả tables liên quan"""
        now        = datetime.utcnow()
        today      = date.today()
        is_play    = 1 if action == "play" else 0

        # Batch 3 writes cùng lúc
        batch = BatchStatement(consistency_level=ConsistencyLevel.LOCAL_QUORUM)

        batch.add(self.insert_event, (
            course_id, today, now, user_id, lecture_id,
            action, position, 0, device
        ))

        batch.add(self.insert_user_event, (
            user_id, now, course_id, lecture_id, action, position
        ))

        self.session.execute(batch)

        # Counter update tách riêng (COUNTER không được trong BATCH thường)
        self.session.execute(self.update_counter, (
            is_play, course_id, today
        ))

    def get_course_events(self, course_id: uuid.UUID,
                           event_date: date,
                           limit: int = 100) -> list:
        rows = self.session.execute(
            self.select_course_events,
            (course_id, event_date, limit)
        )
        return list(rows)

    def get_user_recent_events(self, user_id: uuid.UUID,
                                limit: int = 50) -> list:
        rows = self.session.execute(
            self.select_user_events,
            (user_id, limit)
        )
        return list(rows)

    def get_progress(self, user_id: uuid.UUID,
                      course_id: uuid.UUID) -> list:
        rows = self.session.execute("""
            SELECT * FROM progress_by_user_course
            WHERE user_id  = %s
              AND course_id = %s
        """, (user_id, course_id))
        return list(rows)


# ─── Sử dụng ───
session = get_session()
repo    = VideoTrackingRepo(session)

course_id  = uuid.UUID("11111111-1111-1111-1111-111111111111")
user_id    = uuid.UUID("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
lecture_id = uuid.UUID("cccccccc-cccc-cccc-cccc-cccccccccccc")

# Record events
repo.record_event(course_id, user_id, lecture_id, "play", 0, "desktop")
repo.record_event(course_id, user_id, lecture_id, "pause", 333, "desktop")
repo.record_event(course_id, user_id, lecture_id, "end", 2720, "desktop")

# Query
events = repo.get_course_events(course_id, date.today())
print(f"Course events today: {len(events)}")

user_events = repo.get_user_recent_events(user_id, limit=10)
print(f"User recent events: {len(user_events)}")
```

## 9\. Anti-patterns Cần Tránh

**Anti-pattern 1: Thiết kế table như SQL**

```sql
-- ❌ Normalize như SQL — không dùng được trong Cassandra
CREATE TABLE orders (id UUID PRIMARY KEY, user_id UUID, status TEXT);
CREATE TABLE order_items (order_id UUID, product_id UUID, qty INT, PRIMARY KEY (order_id, product_id));
-- → Không JOIN được → phải query 2 tables → N+1 problem

-- ✅ Denormalize theo access pattern
CREATE TABLE orders_with_items (
    user_id    UUID,
    order_id   UUID,
    status     TEXT,
    items      LIST<FROZEN<MAP<TEXT, TEXT>>>,  -- embed items
    PRIMARY KEY (user_id, order_id)
);
```

**Anti-pattern 2: Partition quá lớn (wide partition)**

```sql
-- ❌ Partition key = user_id → tất cả events của user trong 1 partition
-- User active 5 năm → hàng triệu rows trong 1 partition → chậm
CREATE TABLE events (
    user_id  UUID,
    event_at TIMESTAMP,
    action   TEXT,
    PRIMARY KEY (user_id, event_at)  -- không có time bucket!
);

-- ✅ Thêm time bucket để giới hạn partition size
CREATE TABLE events (
    user_id  UUID,
    month    TEXT,        -- "2025-03"
    event_at TIMESTAMP,
    action   TEXT,
    PRIMARY KEY ((user_id, month), event_at)
);
```

**Anti-pattern 3: Dùng ALLOW FILTERING production**

```sql
-- ❌ Slow — scan toàn bộ cluster
SELECT * FROM events WHERE action = 'play' ALLOW FILTERING;

-- ✅ Tạo table riêng cho query pattern này
CREATE TABLE events_by_action (
    action     TEXT,
    event_date DATE,
    event_at   TIMESTAMP,
    user_id    UUID,
    course_id  UUID,
    PRIMARY KEY ((action, event_date), event_at, user_id)
);
```

**Anti-pattern 4: UPDATE như SQL (read-modify-write)**

```sql
-- ❌ Read trước rồi update → race condition + 2x latency
current = SELECT watched_pct FROM progress WHERE user_id = ? AND course_id = ? AND lecture_id = ?
new_pct = max(current.watched_pct, new_watched_pct)
UPDATE progress SET watched_pct = new_pct WHERE ...

-- ✅ Dùng COUNTER hoặc business logic tránh read-before-write
-- Hoặc chấp nhận last-write-wins nếu appropriate
UPDATE progress SET watched_pct = ?  -- ghi thẳng, không cần đọc trước
WHERE user_id = ? AND course_id = ? AND lecture_id = ?
```

## 10\. Checklist Data Modeling

```java
□ Liệt kê tất cả access patterns TRƯỚC khi thiết kế table
□ Mỗi query pattern → 1 table riêng
□ Partition Key = equality conditions trong WHERE
□ Clustering Key = ORDER BY + range conditions
□ Partition Key đủ cardinality (không quá ít unique values)
□ Ước tính partition size < 100MB (guideline)
□ Thêm time bucket nếu data tăng không giới hạn
□ Chấp nhận data duplication — denormalize cho reads
□ Pre-aggregate nếu cần COUNT/SUM
□ TTL cho data không cần giữ vĩnh viễn
□ Tránh ALLOW FILTERING không có partition key
□ Dùng Prepared Statements trong application
```

## Tổng Kết


| Nguyên tắc | SQL | Cassandra |
|---|---|---|
| Thiết kế bắt đầu từ | Entities/Tables | Queries/Access Patterns |
| Duplication | Tránh tối đa | Chấp nhận, khuyến khích |
| JOIN | ✅ | ❌ — embed hoặc tạo table riêng |
| GROUP BY | ✅ | ❌ — pre-aggregate khi write |
| Filter tùy ý | ✅ | ❌ — chỉ theo Primary Key |
| Thay đổi schema | Tốn kém nhưng làm được | Rất khó — cần migrate data |



```java
Cassandra Data Modeling Rule:
  1 Query = 1 Table
  Table name = query description
  
Ví dụ:
  "Get events by course and date"   → events_by_course_day
  "Get events by user"              → events_by_user
  "Get progress by user and course" → progress_by_user_course
```

Bài tiếp theo chúng ta sẽ học **Cassandra thực chiến** — deployment production, compaction strategies, repair, monitoring và khi nào nên chọn Cassandra cho dự án.

