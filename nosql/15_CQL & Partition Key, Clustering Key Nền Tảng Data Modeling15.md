# CQL & Partition Key, Clustering Key: Nền Tảng Data Modeling

![CQL & Partition Key, Clustering Key - Nền Tảng Data Modeling.jpeg](../images/1e86f424-d1f3-4d5c-886b-b6660068b9dd.jpeg)

Bài 11 bạn đã cài xong Cassandra và chạy được câu query đầu tiên. Bài này đi sâu vào phần quan trọng nhất: **Primary Key design** — đây là quyết định kiến trúc không thể thay đổi sau khi deploy, ảnh hưởng trực tiếp đến performance và khả năng query. Một Partition Key sai không chỉ làm query chậm mà còn có thể làm cả cluster mất cân bằng.

## 1\. Primary Key Trong Cassandra — Khác Hoàn Toàn Với SQL

```sql
-- SQL: PRIMARY KEY chỉ để uniqueness
CREATE TABLE orders (
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    status  VARCHAR(20)
);
-- Có thể query: WHERE user_id = 1 AND status = 'PAID'

-- Cassandra: PRIMARY KEY quyết định:
-- 1. Data phân phối như thế nào (Partition Key)
-- 2. Data sắp xếp trong partition (Clustering Key)
-- 3. Query nào được phép (chỉ query theo PK)
CREATE TABLE orders (
    user_id  UUID,
    order_id UUID,
    status   TEXT,
    amount   DECIMAL,
    PRIMARY KEY (user_id, order_id)
--              ↑           ↑
--         Partition   Clustering
);
-- Chỉ được query: WHERE user_id = ?
-- KHÔNG được query: WHERE status = 'PAID' (trừ khi dùng ALLOW FILTERING)
```

## 2\. Partition Key — Chìa Khóa Phân Tán Dữ Liệu

**Partition Key** quyết định **node nào** lưu data đó.

```java
Khi ghi row với user_id = "abc-123":
  hash("abc-123") → token 15%
  → Node chịu trách nhiệm token 15% nhận data này

Tất cả rows có cùng Partition Key → lưu trên cùng node
  → Giúp query trong 1 partition cực nhanh (1 network hop)
```

### Simple Partition Key

```sql
-- Partition Key là 1 column
CREATE TABLE video_tracking (
    course_id UUID,   -- ← Partition Key
    user_id   UUID,
    event_at  TIMESTAMP,
    action    TEXT,
    PRIMARY KEY (course_id, event_at, user_id)
--              ↑ partition  ↑ clustering ↑ clustering
);

-- Tất cả events của cùng course_id → cùng node
-- Query nhanh: "lấy tất cả events của course X"
SELECT * FROM video_tracking WHERE course_id = ?;
```

### Composite Partition Key

```sql
-- Partition Key gồm nhiều columns — phân phối đều hơn
CREATE TABLE video_tracking_by_month (
    course_id UUID,
    month     TEXT,       -- "2025-03"
    user_id   UUID,
    event_at  TIMESTAMP,
    action    TEXT,
    PRIMARY KEY ((course_id, month), event_at, user_id)
--              ↑─── composite partition key ───↑
);

-- Partition = (course_id, month) — không phải chỉ course_id
-- → Mỗi tháng là partition riêng → partition size có giới hạn
-- Query PHẢI có cả course_id và month:
SELECT * FROM video_tracking_by_month
WHERE course_id = ? AND month = '2025-03';
```

## 3\. Clustering Key — Thứ Tự Trong Partition

**Clustering Key** quyết định **thứ tự** data được lưu trong partition — cho phép range query.

```sql
CREATE TABLE orders_by_user (
    user_id    UUID,       -- Partition Key
    created_at TIMESTAMP,  -- Clustering Key 1
    order_id   UUID,       -- Clustering Key 2
    status     TEXT,
    amount     DECIMAL,
    PRIMARY KEY (user_id, created_at, order_id)
)
WITH CLUSTERING ORDER BY (created_at DESC, order_id ASC);
--                         ↑ mới nhất lên đầu
```

**Queries được phép:**

```sql
-- ✅ Full partition key
SELECT * FROM orders_by_user WHERE user_id = ?;

-- ✅ Partition key + đầu của clustering key
SELECT * FROM orders_by_user
WHERE user_id = ? AND created_at >= '2025-01-01';

-- ✅ Range query trên clustering key
SELECT * FROM orders_by_user
WHERE user_id = ?
  AND created_at >= '2025-01-01'
  AND created_at <  '2025-04-01';

-- ✅ Exact match trên clustering key
SELECT * FROM orders_by_user
WHERE user_id = ? AND created_at = '2025-03-15 10:00:00';

-- ❌ Skip clustering key (không theo thứ tự)
SELECT * FROM orders_by_user
WHERE user_id = ? AND order_id = ?;
-- Lỗi: order_id là clustering key thứ 2, phải có created_at trước

-- ❌ Không có partition key
SELECT * FROM orders_by_user WHERE status = 'PAID';
-- Lỗi: phải có partition key (user_id)
```

## 4\. Các Kiểu Dữ Liệu Trong CQL

```sql
-- Primitive types
UUID        -- ví dụ: 550e8400-e29b-41d4-a716-446655440000
TIMEUUID    -- UUID có chứa timestamp (version 1) — tự sort theo thời gian
TEXT        -- UTF-8 string (tương tự VARCHAR không giới hạn)
INT         -- 32-bit integer
BIGINT      -- 64-bit integer
FLOAT       -- 32-bit floating point
DOUBLE      -- 64-bit floating point
DECIMAL     -- Arbitrary precision (dùng cho tiền tệ)
BOOLEAN     -- true/false
TIMESTAMP   -- Date + time với millisecond
DATE        -- Chỉ date (không có time)
TIME        -- Chỉ time (không có date)
DURATION    -- Time interval
BLOB        -- Binary data
INET        -- IP address (IPv4/IPv6)

-- Collection types
LIST<TEXT>      -- ordered list, có thể duplicate
SET<TEXT>       -- unordered set, no duplicate
MAP<TEXT, INT>  -- key-value pairs
TUPLE<INT, TEXT, FLOAT>  -- fixed-length typed list

-- User-defined type
CREATE TYPE address (
    street TEXT,
    city   TEXT,
    zip    TEXT
);

CREATE TABLE users (
    user_id  UUID PRIMARY KEY,
    email    TEXT,
    address  FROZEN<address>  -- FROZEN = immutable, lưu như 1 giá trị
);
```

## 5\. CQL CRUD Operations

### INSERT

```sql
-- Basic insert
INSERT INTO video_tracking (
    course_id, user_id, event_at, action, position, session_id, device
) VALUES (
    11111111-1111-1111-1111-111111111111,
    aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa,
    toTimestamp(now()),
    'play', 0, 'session_abc', 'mobile'
);

-- Insert với TTL (giây)
INSERT INTO video_tracking (course_id, user_id, event_at, action, position, session_id, device)
VALUES (...)
USING TTL 86400;   -- xóa sau 24 giờ

-- Insert với TIMESTAMP tùy chỉnh (microseconds)
INSERT INTO video_tracking (course_id, user_id, event_at, action, position, session_id, device)
VALUES (...)
USING TIMESTAMP 1710000000000000;

-- Cassandra: INSERT = UPSERT mặc định
-- Nếu row đã tồn tại → ghi đè (không có lỗi)
```

### SELECT

```sql
-- Bắt buộc có Partition Key
SELECT * FROM video_tracking
WHERE course_id = 11111111-1111-1111-1111-111111111111;

-- Projection
SELECT course_id, user_id, action, position
FROM video_tracking
WHERE course_id = 11111111-1111-1111-1111-111111111111;

-- Limit
SELECT * FROM video_tracking
WHERE course_id = 11111111-1111-1111-1111-111111111111
LIMIT 100;

-- Range trên clustering key
SELECT * FROM video_tracking
WHERE course_id  = 11111111-1111-1111-1111-111111111111
  AND event_at  >= '2025-03-15 00:00:00'
  AND event_at  <  '2025-03-16 00:00:00';

-- IN clause trên partition key
SELECT * FROM video_tracking
WHERE course_id IN (
    11111111-1111-1111-1111-111111111111,
    22222222-2222-2222-2222-222222222222
);
-- ⚠️ IN trên partition key gây multiple partition reads — dùng cẩn thận

-- TOKEN function — scan nhiều partitions
SELECT * FROM video_tracking
WHERE TOKEN(course_id) > TOKEN(11111111-1111-1111-1111-111111111111)
LIMIT 10;
```

### UPDATE

```sql
-- Update phải có đủ Primary Key
UPDATE video_tracking
SET action = 'end', position = 2720
WHERE course_id = 11111111-1111-1111-1111-111111111111
  AND event_at  = '2025-03-15 09:00:00'
  AND user_id   = aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa;

-- Update với TTL
UPDATE video_tracking
USING TTL 3600
SET action = 'pause'
WHERE course_id = ?
  AND event_at  = ?
  AND user_id   = ?;

-- Update collection
UPDATE users
SET tags = tags + {'java', 'spring'}    -- thêm vào SET
WHERE user_id = ?;

UPDATE users
SET tags = tags - {'old_tag'}           -- xóa khỏi SET
WHERE user_id = ?;

UPDATE users
SET metadata['last_login'] = '2025-03-15'  -- update MAP
WHERE user_id = ?;
```

### DELETE

```sql
-- Xóa toàn bộ row
DELETE FROM video_tracking
WHERE course_id = 11111111-1111-1111-1111-111111111111
  AND event_at  = '2025-03-15 09:00:00'
  AND user_id   = aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa;

-- Xóa 1 column (set về null)
DELETE action
FROM video_tracking
WHERE course_id = ? AND event_at = ? AND user_id = ?;

-- Xóa toàn bộ partition
DELETE FROM video_tracking
WHERE course_id = 11111111-1111-1111-1111-111111111111;

-- Xóa với range trên clustering key
DELETE FROM video_tracking
WHERE course_id = 11111111-1111-1111-1111-111111111111
  AND event_at >= '2025-01-01'
  AND event_at <  '2025-02-01';
```

## 6\. ALLOW FILTERING — Dùng Khi Nào?

```sql
-- Cassandra từ chối query không có partition key
SELECT * FROM video_tracking WHERE action = 'play';
-- InvalidRequest: Cannot execute this query as it might involve data
-- filtering and thus may have unpredictable performance.

-- ALLOW FILTERING: bắt Cassandra scan tất cả partitions
SELECT * FROM video_tracking
WHERE action = 'play'
ALLOW FILTERING;
-- → Hoạt động nhưng SCAN TOÀN BỘ — cực chậm với data lớn!
```

**Khi nào ALLOW FILTERING chấp nhận được:**

```sql
-- ✅ OK: đã có partition key, chỉ filter thêm
-- Filter trên non-clustering column trong partition đã biết
SELECT * FROM video_tracking
WHERE course_id = ?           -- partition key có
  AND device    = 'mobile'    -- non-key column
ALLOW FILTERING;              -- chỉ scan 1 partition → chấp nhận được

-- ❌ Không OK: không có partition key — scan toàn cluster
SELECT * FROM video_tracking
WHERE action = 'play'
ALLOW FILTERING;
```

**Giải pháp thay thế ALLOW FILTERING:**

1.  Tạo thêm table với query pattern khác (Query-first design — Bài 13)
    
2.  Dùng Secondary Index
    
3.  Dùng Materialized View
    

## 7\. Secondary Index

```sql
-- Tạo secondary index trên non-key column
CREATE INDEX idx_action ON video_tracking (action);

-- Giờ có thể query theo action
SELECT * FROM video_tracking WHERE action = 'play';
-- ⚠️ Vẫn chậm với large datasets — Cassandra scatter-gather tất cả nodes

-- Secondary index phù hợp khi:
-- 1. Cardinality thấp đến vừa (không quá nhiều unique values)
-- 2. Kết hợp với partition key
-- 3. Dataset không quá lớn

-- ✅ Tốt: kết hợp với partition key
SELECT * FROM video_tracking
WHERE course_id = ?  -- partition key giới hạn scope
  AND action    = 'play';  -- secondary index

-- ❌ Tệ: không có partition key
SELECT * FROM video_tracking WHERE action = 'play';
```

## 8\. COUNTER Table

```sql
-- COUNTER: atomic increment/decrement — không thể set giá trị
CREATE TABLE course_view_counters (
    course_id UUID,
    date      DATE,
    views     COUNTER,
    unique_views COUNTER,
    PRIMARY KEY (course_id, date)
);

-- Chỉ UPDATE, không INSERT
UPDATE course_view_counters
SET views        = views + 1,
    unique_views = unique_views + 1
WHERE course_id = 11111111-1111-1111-1111-111111111111
  AND date      = '2025-03-15';

-- Đọc counter
SELECT * FROM course_view_counters
WHERE course_id = 11111111-1111-1111-1111-111111111111;

-- Lưu ý: COUNTER table không thể có non-counter columns (trừ PK)
-- Không thể delete 1 counter, phải delete cả row
DELETE FROM course_view_counters
WHERE course_id = ? AND date = ?;
```

## 9\. BATCH — Nhóm Statements

```sql
-- BATCH: đảm bảo tất cả statements được ghi cùng lúc
-- ⚠️ KHÔNG phải ACID transaction — chỉ là batch write

-- Unlogged batch (nhanh hơn, không có atomic guarantee)
BEGIN UNLOGGED BATCH
    INSERT INTO video_tracking (course_id, user_id, event_at, action, position, session_id, device)
    VALUES (?, ?, toTimestamp(now()), 'play', 0, ?, 'mobile');

    UPDATE course_view_counters
    SET views = views + 1
    WHERE course_id = ? AND date = toDate(now());
APPLY BATCH;

-- Logged batch (default): atomic — tất cả hoặc không
BEGIN BATCH
    INSERT INTO table1 (...) VALUES (...);
    INSERT INTO table2 (...) VALUES (...);
APPLY BATCH;

-- ⚠️ Chỉ dùng BATCH khi cần atomic writes trên 1 partition
-- KHÔNG dùng BATCH để "tối ưu" — ngược lại làm chậm
```

## 10\. TTL — Time To Live

```sql
-- Set TTL khi insert
INSERT INTO user_sessions (user_id, session_start, session_id)
VALUES (?, toTimestamp(now()), 'sess_001')
USING TTL 86400;  -- 24 giờ

-- Check TTL còn lại
SELECT TTL(session_id) FROM user_sessions
WHERE user_id = ? AND session_start = ?;
-- → 75234 (giây còn lại)

-- Update TTL
UPDATE user_sessions
USING TTL 3600       -- reset TTL về 1 giờ
SET session_id = 'sess_001'
WHERE user_id = ? AND session_start = ?;

-- Default TTL cho toàn table
CREATE TABLE session_logs (
    user_id    UUID,
    logged_at  TIMESTAMP,
    action     TEXT,
    PRIMARY KEY (user_id, logged_at)
) WITH default_time_to_live = 2592000;  -- 30 ngày
-- Mọi row tự xóa sau 30 ngày trừ khi override
```

## 11\. Thực Hành Tổng Hợp

**Tạo đầy đủ schema cho video tracking** [**nguyentienkhoi.hashnode.dev**](http://nguyentienkhoi.hashnode.dev)**:**

```sql
USE foxdev_dev;

-- Table 1: Track sự kiện theo course + thời gian
-- Query: "Lấy tất cả events của course X trong ngày Y"
CREATE TABLE events_by_course_date (
    course_id   UUID,
    event_date  DATE,
    event_at    TIMESTAMP,
    user_id     UUID,
    action      TEXT,
    position    INT,
    device      TEXT,
    PRIMARY KEY ((course_id, event_date), event_at, user_id)
) WITH CLUSTERING ORDER BY (event_at DESC, user_id ASC)
AND default_time_to_live = 7776000;  -- 90 ngày

-- Table 2: Track tiến độ học theo user
-- Query: "User X đã xem những gì trong course Y?"
CREATE TABLE progress_by_user_course (
    user_id     UUID,
    course_id   UUID,
    lecture_id  UUID,
    watched_pct FLOAT,
    completed   BOOLEAN,
    last_watch  TIMESTAMP,
    PRIMARY KEY ((user_id, course_id), lecture_id)
) WITH CLUSTERING ORDER BY (lecture_id ASC);

-- Table 3: Counter cho analytics
-- Query: "Course X có bao nhiêu views ngày Y?"
CREATE TABLE daily_course_stats (
    course_id  UUID,
    date       DATE,
    views      COUNTER,
    plays      COUNTER,
    completions COUNTER,
    PRIMARY KEY (course_id, date)
) WITH CLUSTERING ORDER BY (date DESC);

-- Insert events
INSERT INTO events_by_course_date (course_id, event_date, event_at, user_id, action, position, device)
VALUES (
    11111111-1111-1111-1111-111111111111,
    '2025-03-15',
    '2025-03-15 09:00:00',
    aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa,
    'play', 0, 'desktop'
);

INSERT INTO events_by_course_date (course_id, event_date, event_at, user_id, action, position, device)
VALUES (
    11111111-1111-1111-1111-111111111111,
    '2025-03-15',
    '2025-03-15 09:45:20',
    aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa,
    'end', 2720, 'desktop'
);

-- Update progress
INSERT INTO progress_by_user_course (user_id, course_id, lecture_id, watched_pct, completed, last_watch)
VALUES (
    aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa,
    11111111-1111-1111-1111-111111111111,
    cccccccc-cccc-cccc-cccc-cccccccccccc,
    100.0, true,
    toTimestamp(now())
);

-- Update counters
UPDATE daily_course_stats
SET views = views + 1,
    plays = plays + 1
WHERE course_id = 11111111-1111-1111-1111-111111111111
  AND date      = '2025-03-15';

-- Query kiểm tra
SELECT * FROM events_by_course_date
WHERE course_id  = 11111111-1111-1111-1111-111111111111
  AND event_date = '2025-03-15';

SELECT * FROM progress_by_user_course
WHERE user_id  = aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
  AND course_id = 11111111-1111-1111-1111-111111111111;

SELECT * FROM daily_course_stats
WHERE course_id = 11111111-1111-1111-1111-111111111111
LIMIT 7;
```

## Tổng Kết


| Khái niệm | Ý nghĩa |
|---|---|
| Partition Key | Xác định node lưu data — bắt buộc có trong mọi query |
| Composite PK | Nhiều columns làm partition key — phân phối đều hơn |
| Clustering Key | Thứ tự trong partition — cho phép range query |
| CLUSTERING ORDER BY | Sắp xếp mặc định trong partition |
| ALLOW FILTERING | Scan toàn bộ — tránh dùng trừ khi đã có partition key |
| Secondary Index | Query non-key column — chậm với large dataset |
| COUNTER | Atomic increment/decrement — không thể set trực tiếp |
| TTL | Auto-expire data — tốt cho logs, sessions, events |
| BATCH | Nhóm writes — không phải ACID, tránh cross-partition |



```java
Cassandra query rules:
  1. PHẢI có Partition Key trong WHERE
  2. Clustering Key phải theo thứ tự (left to right)
  3. Range query chỉ được phép trên Clustering Key cuối
  4. Không filter trên non-key columns (trừ ALLOW FILTERING + partition key)
```

Bài tiếp theo chúng ta sẽ học **Data Modeling: Query-first Design** — cách thiết kế schema từ queries mong muốn, denormalization patterns và xử lý những bài toán phức tạp trong Cassandra.

