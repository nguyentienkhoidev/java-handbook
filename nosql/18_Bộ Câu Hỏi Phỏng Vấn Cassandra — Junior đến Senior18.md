# Bộ Câu Hỏi Phỏng Vấn Cassandra — Junior đến Senior

![Bộ Câu Hỏi Phỏng Vấn Cassandra — Junior đến Senior.jpeg](../images/b107aff9-1a8c-43b0-b5a0-7e02bf915d6f.jpeg)

* * *

## 🟢 JUNIOR (0–2 năm)

Mục tiêu: Kiểm tra hiểu biết cơ bản về Cassandra, CQL, Partition Key và khi nào dùng Cassandra.

###   
Khái Niệm Cơ Bản

**Q1. Cassandra là gì? Khi nào nên dùng Cassandra thay vì PostgreSQL?**

Đáp án mong đợi:

*   Cassandra = distributed wide-column database, thiết kế cho **write-heavy, high availability**
    
*   Không có single point of failure — tất cả nodes bình đẳng (peer-to-peer)
    
*   Scale horizontal tự nhiên — thêm node → tăng capacity ngay
    

**Dùng Cassandra khi:**

*   Write throughput cực cao (> 10,000 writes/giây)
    
*   Time-series data: logs, events, metrics, IoT
    
*   Không thể downtime — mất node phải tự recover
    
*   Data tăng liên tục không giới hạn
    

**Không dùng Cassandra khi:**

*   Cần ACID transactions phức tạp
    
*   Cần flexible query (filter tùy ý)
    
*   Dataset nhỏ — overhead không đáng
    
*   Team chưa có kinh nghiệm — learning curve cao
    

🚩 Red flag: Nói "Cassandra nhanh hơn PostgreSQL cho mọi use case" — sai, Cassandra chậm hơn cho read phức tạp

  
**Q2. Giải thích kiến trúc peer-to-peer của Cassandra. Tại sao không có single point of failure?**

Đáp án mong đợi:

```java
PostgreSQL / MongoDB:    Cassandra:
  Primary              Node 1 ←→ Node 2
  ↓↑                         ↕
  Replica 1            Node 4 ←→ Node 3

  Primary down         Tất cả nodes BÌNH ĐẲNG
  → writes bị chặn    → Bất kỳ node nào nhận write
                       → 1 node down, 3 nodes còn lại vẫn phục vụ
```

*   Mỗi request có **coordinator node** (node nhận request)
    
*   Coordinator route đến nodes chịu trách nhiệm partition
    
*   Không có master — mọi node đều có thể là coordinator
    

  
**Q3. Partition Key trong Cassandra là gì? Tại sao bắt buộc phải có trong mọi query?**

Đáp án mong đợi:

*   **Partition Key** = phần đầu của PRIMARY KEY, quyết định **node nào** lưu data
    
*   Cách hoạt động: `hash(partition_key) → token → node chịu trách nhiệm token range`
    
*   Tất cả rows cùng partition key → cùng node → 1 network hop
    

**Tại sao bắt buộc trong query:**

*   Không có partition key → Cassandra không biết đến node nào để lấy data
    
*   Phải scan toàn bộ cluster → N nodes → rất chậm
    
*   Cassandra từ chối query không có partition key (trừ ALLOW FILTERING)
    

```sql
-- ✅ OK: có partition key
SELECT * FROM video_tracking WHERE course_id = ?;

-- ❌ Lỗi: không có partition key
SELECT * FROM video_tracking WHERE action = 'play';
-- InvalidRequest: Cannot execute this query as it might involve data filtering
```

  
**Q4. Clustering Key là gì? Khác Partition Key thế nào?**

Đáp án mong đợi:

```sql
CREATE TABLE orders_by_user (
    user_id    UUID,         -- Partition Key: quyết định node nào
    created_at TIMESTAMP,   -- Clustering Key: thứ tự trong partition
    order_id   UUID,         -- Clustering Key
    status     TEXT,
    amount     DECIMAL,
    PRIMARY KEY (user_id, created_at, order_id)
)
WITH CLUSTERING ORDER BY (created_at DESC, order_id ASC);
```


|  | Partition Key | Clustering Key |
|---|---|---|
| Vai trò | Phân tán data | Sắp xếp trong partition |
| Query | Equality only (=) | Range query được (>, <, BETWEEN) |
| Thứ tự | Không | CLUSTERING ORDER BY |
| Bắt buộc trong WHERE | ✅ Luôn luôn | Theo thứ tự (left to right) |



  
**Q5. Giải thích CQL CRUD cơ bản. Điểm khác biệt quan trọng nào so với SQL?**

Đáp án mong đợi:

```sql
-- INSERT — cũng là UPSERT (không có lỗi nếu row đã tồn tại)
INSERT INTO video_tracking (course_id, user_id, watched_at, action)
VALUES (uuid(), uuid(), toTimestamp(now()), 'play');

-- SELECT — PHẢI có partition key
SELECT * FROM video_tracking WHERE course_id = ?;

-- UPDATE — phải có đủ primary key
UPDATE video_tracking SET action = 'end'
WHERE course_id = ? AND watched_at = ? AND user_id = ?;

-- DELETE
DELETE FROM video_tracking
WHERE course_id = ? AND watched_at = ? AND user_id = ?;
```

**Điểm khác biệt quan trọng:**

*   INSERT = UPSERT — không có lỗi duplicate key
    
*   Không có JOIN — phải thiết kế schema khác
    
*   Không có GROUP BY — phải pre-aggregate
    
*   Không thể filter tùy ý như SQL
    
*   Không có subquery
    

🚩 Red flag: Không biết INSERT là UPSERT trong Cassandra

  
**Q6. Replication Factor là gì? Ý nghĩa của RF=3 trong production?**

Đáp án mong đợi:

*   **Replication Factor (RF)** = số copies của mỗi row được lưu
    
*   RF=1: mỗi row lưu ở 1 node → 1 node down = mất data ❌
    
*   RF=2: 2 copies → chịu được mất 1 node, nhưng cần cẩn thận với quorum
    
*   **RF=3**: production standard
    
    *   Chịu được mất 1 node mà không mất data
        
    *   Chịu được mất 1 node mà vẫn có quorum (2/3)
        
    *   Đủ redundancy mà không quá tốn storage
        

```sql
CREATE KEYSPACE foxdev
WITH REPLICATION = {
    'class': 'NetworkTopologyStrategy',
    'datacenter1': 3   -- RF=3
};
```

  
**Q7. TTL trong Cassandra hoạt động như thế nào? Cho ví dụ use case.**

Đáp án mong đợi:

```sql
-- TTL per row
INSERT INTO video_tracking (course_id, user_id, watched_at, action)
VALUES (?, ?, ?, ?)
USING TTL 86400;   -- tự xóa sau 24 giờ

-- TTL default cho toàn table
CREATE TABLE session_logs (
    user_id  UUID,
    log_at   TIMESTAMP,
    action   TEXT,
    PRIMARY KEY (user_id, log_at)
) WITH default_time_to_live = 2592000;  -- 30 ngày

-- Kiểm tra TTL còn lại
SELECT TTL(action) FROM video_tracking
WHERE course_id = ? AND watched_at = ? AND user_id = ?;

-- Update TTL
UPDATE video_tracking USING TTL 3600
SET action = 'pause'
WHERE course_id = ? AND watched_at = ? AND user_id = ?;
```

**Use cases:** event logs, sessions, video tracking, IoT sensor data — bất kỳ data nào không cần giữ vĩnh viễn

  
**Q8. Tombstone trong Cassandra là gì? Tại sao quá nhiều tombstones gây vấn đề?**

Đáp án mong đợi:

*   Cassandra không xóa data ngay khi DELETE — tạo **tombstone** (marker "đã xóa")
    
*   Tombstone tồn tại cho đến khi **compaction** xóa chúng (sau `gc_grace_seconds`, mặc định 10 ngày)
    
*   Lý do: đảm bảo deleted data không "sống lại" ở replica bị offline lúc delete
    

**Vấn đề quá nhiều tombstones:**

*   Read phải scan qua tombstones để tìm live data → chậm
    
*   Memory pressure khi tombstones được load
    
*   Warning: `tombstone_warn_threshold` (mặc định 1,000)
    
*   Error: `tombstone_failure_threshold` (mặc định 100,000)
    

**Nguyên nhân tombstones nhiều:**

*   DELETE lớn, UPDATE nhiều
    
*   TTL expiration (cũng tạo tombstones)
    
*   Design schema không phù hợp
    

###   
Coding Question Junior

**Q9. Viết schema CQL cho bài toán: lưu video tracking events của** [**nguyentienkhoi.hashnode.dev**](http://nguyentienkhoi.hashnode.dev)**. Query cần hỗ trợ: "Lấy tất cả events của course X trong ngày Y, mới nhất trước."**

Đáp án mong đợi:

```sql
CREATE TABLE events_by_course_day (
    course_id   UUID,
    event_date  DATE,
    event_at    TIMESTAMP,
    user_id     UUID,
    action      TEXT,
    position    INT,
    device      TEXT,
    PRIMARY KEY ((course_id, event_date), event_at, user_id)
)
WITH CLUSTERING ORDER BY (event_at DESC, user_id ASC)
AND compaction = {
    'class': 'TimeWindowCompactionStrategy',
    'compaction_window_size': 1,
    'compaction_window_unit': 'DAYS'
}
AND default_time_to_live = 7776000;  -- 90 ngày

-- Query
SELECT * FROM events_by_course_day
WHERE course_id  = ?
  AND event_date = '2025-03-15'
LIMIT 100;
```

Điểm đánh giá:

*   Composite partition key `(course_id, event_date)` — tránh hot partition
    
*   CLUSTERING ORDER BY DESC — mới nhất lên đầu
    
*   TWCS compaction cho time-series
    
*   TTL phù hợp
    

* * *

## 🟡 INTERMEDIATE (2–4 năm)

Mục tiêu: Query-first design, Consistency Level, Compaction, data modeling patterns.

  
**Q10. Giải thích Consistency Level trong Cassandra. Khi nào dùng QUORUM vs ONE?**

Đáp án mong đợi:

**Write Consistency Level:**

*   `ONE`: 1 node acknowledge → nhanh nhất, eventual consistency
    
*   `QUORUM`: majority nodes acknowledge (RF=3 → cần 2/3) → cân bằng
    
*   `ALL`: tất cả nodes acknowledge → chậm nhất, strong consistency
    

**Read Consistency Level — tương tự**

**Strong consistency rule:**

```java
Write CL + Read CL > Replication Factor
QUORUM + QUORUM > RF=3  (2 + 2 = 4 > 3) ✅ strong consistent
ONE + ONE > RF=3         (1 + 1 = 2 > 3) ❌ eventual consistent
```

**Khi nào dùng:**

```java
Financial data, critical ops:
  → Write: QUORUM, Read: QUORUM (strong consistency)

Event logging, metrics (volume cao):
  → Write: ONE, Read: ONE (tốc độ tối đa, ok mất ít data)

User-facing features (cần đọc đúng):
  → Write: QUORUM, Read: LOCAL_QUORUM
```

  
**Q11. Query-first Design là gì? Tại sao Cassandra bắt buộc phải thiết kế schema theo query?**

Đáp án mong đợi:

**SQL (Entity-first):**

*   Thiết kế tables theo entities, relationships
    
*   Viết query tùy ý với JOIN, filter bất kỳ column
    

**Cassandra (Query-first):**

*   Liệt kê tất cả queries cần thiết TRƯỚC
    
*   Mỗi query pattern → 1 table riêng
    
*   Schema phụ thuộc hoàn toàn vào access patterns
    

**Tại sao bắt buộc:**

*   Không có JOIN — không thể combine data lúc query
    
*   Chỉ filter theo Primary Key — không thể WHERE tùy ý
    
*   Cassandra phải biết data ở node nào → phải có Partition Key
    
*   Trade-off: đánh đổi query flexibility lấy write throughput
    

**Ví dụ:**

```java
Query 1: "Events của course X ngày Y"
→ Table: events_by_course_day → PK: (course_id, event_date)

Query 2: "Events của user X gần đây"
→ Table: events_by_user → PK: (user_id) + SK: event_at DESC

→ 2 queries khác nhau = 2 tables khác nhau (data duplication = OK)
```

  
**Q12. Giải thích 3 Compaction Strategies: STCS, LCS, TWCS. Khi nào dùng mỗi loại?**

Đáp án mong đợi:

**Size-Tiered Compaction (STCS) — Default:**

*   Merge SSTables có kích thước tương tự
    
*   Tốt cho: write-heavy workload
    
*   Nhược: space amplification (cần 2x disk tạm thời), read có thể chậm
    

**Leveled Compaction (LCS):**

*   Organize SSTables thành levels, compact cross-levels
    
*   Tốt cho: read-heavy, data ổn định
    
*   Nhược: write amplification cao, I/O intensive liên tục
    

**Time Window Compaction (TWCS) — Tốt nhất cho time-series:**

*   Tạo 1 SSTable per time window (e.g., 1 ngày)
    
*   Data cũ expire theo TTL → SSTable tự drop
    
*   Tốt cho: time-series với TTL
    
*   Không nên dùng cho data không có TTL
    

```sql
-- TWCS cho video tracking
WITH compaction = {
    'class':                   'TimeWindowCompactionStrategy',
    'compaction_window_size':  1,
    'compaction_window_unit':  'DAYS'
}
AND default_time_to_live = 7776000;
```

```java
Rule of thumb:
  Write-heavy, no TTL      → STCS
  Read-heavy, catalog data → LCS
  Time-series + TTL        → TWCS (always)
```

  
**Q13. Tại sao Cassandra không nên dùng cho financial transactions? Cách thiết kế để đảm bảo data integrity?**

Đáp án mong đợi:

**Vấn đề:**

*   Cassandra không có multi-row ACID transaction
    
*   Lightweight Transactions (LWT) chỉ cho single partition, chậm
    
*   No foreign key, no referential integrity
    
*   Eventual consistency: read ngay sau write có thể thấy data cũ
    

**Lightweight Transactions (so sánh-và-đặt):**

```sql
-- Conditional INSERT: chỉ insert nếu chưa tồn tại
INSERT INTO orders (order_id, user_id, status, amount)
VALUES (uuid(), ?, 'PENDING', ?)
IF NOT EXISTS;
-- → Dùng Paxos protocol, chậm hơn 3-5x thường

-- Conditional UPDATE
UPDATE orders SET status = 'PAID'
WHERE order_id = ?
IF status = 'PENDING';   -- chỉ update nếu status = PENDING
```

**Kết luận:** Cassandra phù hợp cho **append-only** use cases (events, logs) không phải OLTP transactions. Dùng PostgreSQL cho financial data, dùng Cassandra cho event tracking alongside.

  
**Q14. Giải thích vấn đề Hot Partition và cách giải quyết.**

Đáp án mong đợi:

**Hot Partition:** Một partition nhận traffic nhiều hơn hẳn các partitions khác → node đó quá tải.

**Nguyên nhân phổ biến:**

```sql
-- ❌ Partition key = course_id
-- Course Spring Boot có 10,000 students đang xem cùng lúc
-- → tất cả writes vào 1 node!
PRIMARY KEY (course_id, watched_at, user_id)
```

**Giải pháp 1: Time Bucket**

```sql
-- ✅ Thêm ngày vào partition key
-- Mỗi ngày = partition riêng, phân tán load theo thời gian
PRIMARY KEY ((course_id, event_date), watched_at, user_id)
```

**Giải pháp 2: Shard Number**

```python
# Thêm bucket ngẫu nhiên
import hashlib
bucket = int(hashlib.md5(user_id.encode()).hexdigest(), 16) % 10
# PK = (course_id, bucket, event_date)
# Write: phân tán đều 10 nodes
# Read: query cả 10 buckets rồi merge
```

**Giải pháp 3: Composite Partition Key**

```sql
-- Kết hợp nhiều fields có cardinality cao
PRIMARY KEY ((user_id, course_id), event_at)
```

  
**Q15. ALLOW FILTERING là gì? Khi nào có thể dùng, khi nào không?**

Đáp án mong đợi:

```sql
-- Cassandra từ chối query không có partition key
SELECT * FROM events WHERE action = 'play';
-- → Error

-- ALLOW FILTERING: bắt scan toàn cluster
SELECT * FROM events WHERE action = 'play'
ALLOW FILTERING;
-- → Hoạt động nhưng chậm kinh khủng với data lớn!
```

**Khi nào ALLOW FILTERING chấp nhận được:**

```sql
-- ✅ Đã có partition key, chỉ filter thêm column khác
-- Scan chỉ trong 1 partition → chấp nhận được
SELECT * FROM events
WHERE course_id  = ?           -- partition key
  AND event_date = '2025-03-15'
  AND device     = 'mobile'   -- non-key column
ALLOW FILTERING;
```

**Khi nào KHÔNG bao giờ dùng:**

```sql
-- ❌ Không có partition key → scan toàn cluster
SELECT * FROM events WHERE action = 'play' ALLOW FILTERING;
-- → Với 100M rows = chậm hàng phút, tốn tài nguyên toàn cluster
```

**Giải pháp thay thế:** Tạo table mới với query pattern đó.

###   
Coding Question Intermediate

**Q16. Thiết kế schema cho bài toán sau:** [**nguyentienkhoi.hashnode.dev**](http://nguyentienkhoi.hashnode.dev) **cần 3 queries:**

1.  Lấy tất cả enrollments của user X
    
2.  Lấy tất cả users đã enroll course Y
    
3.  Check xem user X đã enroll course Y chưa
    

**Đáp án mong đợi:**

```sql
-- Query 1: enrollments của user X
CREATE TABLE enrollments_by_user (
    user_id      UUID,
    enrolled_at  TIMESTAMP,
    course_id    UUID,
    progress     FLOAT,
    completed    BOOLEAN,
    PRIMARY KEY (user_id, enrolled_at, course_id)
) WITH CLUSTERING ORDER BY (enrolled_at DESC, course_id ASC);

-- Query 2: users đã enroll course Y
CREATE TABLE enrollments_by_course (
    course_id    UUID,
    enrolled_at  TIMESTAMP,
    user_id      UUID,
    progress     FLOAT,
    PRIMARY KEY (course_id, enrolled_at, user_id)
) WITH CLUSTERING ORDER BY (enrolled_at DESC, user_id ASC);

-- Query 3: check specific enrollment (exact lookup)
CREATE TABLE enrollment_lookup (
    user_id   UUID,
    course_id UUID,
    enrolled_at TIMESTAMP,
    completed   BOOLEAN,
    PRIMARY KEY (user_id, course_id)
);

-- Khi enroll: write vào CẢ 3 tables (denormalization)
-- Đây là Query-first Design: 3 queries = 3 tables
```

* * *

## 🟠 ADVANCED (4–7 năm)

Mục tiêu: Production operations, Repair, data modeling nâng cao, performance tuning.

  
**Q17. Giải thích Cassandra Write Path. Tại sao writes nhanh hơn nhiều so với SQL?**

Đáp án mong đợi:

```java
Client write
     ↓
Coordinator Node
     ↓ (song song đến N replicas)
Primary Replica:
  1. Ghi Commit Log (sequential, append-only) → cực nhanh
  2. Ghi MemTable (in-memory) → cực nhanh
  → Trả về ACK ngay khi cả 2 hoàn thành

Background (async):
  MemTable đầy → Flush xuống SSTable (disk)
  SSTable tích lũy → Compaction (merge + cleanup)
```

**Tại sao nhanh hơn SQL:**

*   PostgreSQL: **random I/O** — update existing data trên disk (B-tree page modification)
    
*   Cassandra: **sequential I/O** — append-only Commit Log → disk sequential write nhanh gấp 10-100x
    
*   Không lock — không có contention
    
*   MemTable absorbs write bursts
    

  
**Q18. Repair trong Cassandra là gì? Tại sao cần chạy định kỳ? Liên quan gì đến gc\_grace\_seconds?**

Đáp án mong đợi:

**Tại sao cần repair:**

*   Node down trong thời gian ngắn → miss một số writes
    
*   Khi restart: data không nhất quán với các nodes khác
    
*   Read repair: lazy, chỉ sync khi đọc
    
*   Full repair: chủ động sync toàn bộ
    

**Repair hoạt động:**

*   So sánh Merkle trees giữa các replicas
    
*   Tìm differences
    
*   Sync data bị thiếu
    

**gc\_grace\_seconds (mặc định 10 ngày):**

*   Thời gian tombstones tồn tại trước khi bị xóa
    
*   Đảm bảo: nếu node bị offline rồi restart, node đó vẫn biết về các deletes trong thời gian offline
    

**Quy tắc repair:**

```java
Phải chạy repair ít nhất 1 lần trong gc_grace_seconds
→ Mặc định: ít nhất mỗi 10 ngày
→ Thực tế: mỗi 5 ngày để an toàn

Nếu không repair trong gc_grace_seconds:
→ Node offline > 10 ngày → restart → tombstones đã bị xóa
→ Deleted data có thể "sống lại" (zombie data)!
```

  
**Q19. \[System Design\] Thiết kế schema Cassandra cho hệ thống tracking video của** [**nguyentienkhoi.hashnode.dev**](http://nguyentienkhoi.hashnode.dev) **với yêu cầu: 10 triệu events/ngày, giữ 90 ngày, 5 queries khác nhau.**

Đáp án mong đợi:

**Access Patterns:**

```java
AP1: Events của course X ngày Y (admin analytics)
AP2: Tiến độ học của user X trong course Y (student dashboard)
AP3: N events gần nhất của user X (activity feed)
AP4: Tổng giờ học của user X theo tháng (monthly stats)
AP5: Lectures đã complete của user X trong course Y
```

**Schema:**

```sql
-- AP1: Composite PK tránh hot partition
CREATE TABLE events_by_course_day (
    course_id   UUID,
    event_date  DATE,
    event_at    TIMESTAMP,
    user_id     UUID,
    action      TEXT,
    position    INT,
    PRIMARY KEY ((course_id, event_date), event_at, user_id)
) WITH CLUSTERING ORDER BY (event_at DESC)
AND compaction = {'class': 'TimeWindowCompactionStrategy',
                  'compaction_window_size': 1,
                  'compaction_window_unit': 'DAYS'}
AND default_time_to_live = 7776000;

-- AP2: User + course trong partition
CREATE TABLE progress_by_user_course (
    user_id     UUID,
    course_id   UUID,
    lecture_id  UUID,
    watched_pct FLOAT,
    completed   BOOLEAN,
    last_watch  TIMESTAMP,
    PRIMARY KEY ((user_id, course_id), lecture_id)
);

-- AP3: Events của user (partition per month để limit size)
CREATE TABLE events_by_user_month (
    user_id    UUID,
    month      TEXT,   -- "2025-03"
    event_at   TIMESTAMP,
    course_id  UUID,
    action     TEXT,
    PRIMARY KEY ((user_id, month), event_at)
) WITH CLUSTERING ORDER BY (event_at DESC)
AND default_time_to_live = 7776000;

-- AP4: Counter table
CREATE TABLE user_monthly_watch_time (
    user_id    UUID,
    month      TEXT,
    total_secs COUNTER,
    session_count COUNTER,
    PRIMARY KEY (user_id, month)
) WITH CLUSTERING ORDER BY (month DESC);

-- AP5: Completed lectures
CREATE TABLE completed_lectures (
    user_id      UUID,
    course_id    UUID,
    lecture_id   UUID,
    completed_at TIMESTAMP,
    PRIMARY KEY ((user_id, course_id), completed_at, lecture_id)
) WITH CLUSTERING ORDER BY (completed_at DESC);
```

✅ Senior indicator: Biết composite partition key, TWCS, COUNTER table tách riêng, TTL

  
**Q20. Nodetool — kể 5 commands quan trọng nhất trong production và giải thích từng cái.**

Đáp án mong đợi:

```bash
# 1. nodetool status — health check cluster
nodetool status
# UN = Up+Normal ✅, DN = Down ❌, UJ = Joining
# Kiểm tra: tất cả nodes UN trước khi deploy

# 2. nodetool repair — sync data between replicas
nodetool repair -ir foxdev_keyspace
# -ir = incremental repair (chỉ repair data chưa repair)
# Chạy định kỳ < gc_grace_seconds/2

# 3. nodetool compactionstats — xem tiến trình compaction
nodetool compactionstats
# Biết: bao nhiêu compaction đang chạy, ETA

# 4. nodetool cfstats — per-table statistics
nodetool cfstats foxdev.events_by_course_day
# Read/write latency, partition size, bloom filter efficiency

# 5. nodetool tpstats — thread pool stats
nodetool tpstats
# Blocked > 0 = bottleneck!
# CompactionExecutor blocked → disk I/O quá tải
# ReadStage blocked → query load quá cao
```

  
**Q21. Giải thích wide partition problem. Khi nào partition bị coi là "quá lớn"?**

Đáp án mong đợi:

**Wide partition:** Một partition có quá nhiều rows → node chứa partition đó bị overloaded.

**Ngưỡng:**

*   Cassandra guideline: partition < **100MB**
    
*   Query performance degraded khi > 100MB
    
*   Risk: GC pressure, read timeout
    

**Ví dụ:**

```sql
-- ❌ Partition bị wide: PK = user_id, không có time bucket
-- User active 5 năm = hàng triệu events trong 1 partition
PRIMARY KEY (user_id, event_at)

-- ✅ Fix: thêm time bucket
PRIMARY KEY ((user_id, month), event_at)
-- Mỗi tháng = partition riêng, kích thước có giới hạn
```

**Phát hiện:**

```bash
nodetool cfstats keyspace.table
# max_row_size → nếu > 100MB = vấn đề

# Hoặc dùng system.size_estimates
SELECT table_name, mean_partition_size, max_partition_size
FROM system.size_estimates
WHERE keyspace_name = 'foxdev';
```

* * *

## 🔴 SENIOR / PRINCIPAL (7+ năm)

  
**Q22. \[System Design\]** [**nguyentienkhoi.hashnode.dev**](http://nguyentienkhoi.hashnode.dev) **cần migrate 500 triệu video tracking events từ PostgreSQL sang Cassandra. Zero downtime. Kế hoạch migration như thế nào?**

Đáp án mong đợi:

**Phase 1: Preparation (2 tuần)**

*   Design Cassandra schema theo Query-first
    
*   Setup Cassandra cluster (3+ nodes, RF=3)
    
*   Benchmark với sample data
    
*   Viết migration script
    

**Phase 2: Dual-write (2 tuần)**

*   Application ghi vào CẢ HAI PostgreSQL và Cassandra
    
*   Đọc vẫn từ PostgreSQL
    
*   Monitor: Cassandra performance, data consistency
    

**Phase 3: Backfill (3-7 ngày)**

*   Migrate historical data từ PG sang Cassandra (batch, offline)
    
*   Rate limit để không overload Cassandra: 10,000 inserts/giây
    
*   Verify data integrity (row count, sample checks)
    

**Phase 4: Switch reads (A/B testing)**

*   10% traffic đọc từ Cassandra
    
*   Monitor: latency, error rate, data correctness
    
*   Tăng dần: 25% → 50% → 100%
    

**Phase 5: Cleanup**

*   Remove PostgreSQL writes sau 2 tuần confirm stable
    
*   Drop PostgreSQL video\_tracking table sau 1 tháng
    

✅ Senior indicator: Dual-write period, data verification, gradual rollout, rollback plan

  
**Q23. Cassandra Lightweight Transaction (LWT) là gì? Tại sao chậm và khi nào nên tránh?**

Đáp án mong đợi:

**LWT = Compare-and-Set (CAS) operations:**

```sql
-- INSERT IF NOT EXISTS
INSERT INTO users (user_id, email, name)
VALUES (?, ?, ?)
IF NOT EXISTS;

-- UPDATE IF condition
UPDATE orders SET status = 'PAID'
WHERE order_id = ?
IF status = 'PENDING';
```

**Tại sao chậm (3-5x thường):**

*   Dùng **Paxos consensus protocol** (4 round trips thay vì 1)
    
*   Prepare → Promise → Propose → Commit
    
*   Cần majority quorum agreement
    
*   Sequential — không có parallel execution
    

**Khi nào tránh:**

*   Không dùng LWT trong hot path (checkout flow)
    
*   Không dùng thay thế cho distributed locking phức tạp
    
*   10,000 LWT/giây = significant overhead
    

**Giải pháp thay thế:**

*   Idempotent design: thiết kế operation tự nhiên idempotent
    
*   Application-level locking (Redis distributed lock)
    
*   Re-design schema để tránh cần CAS
    

  
**Q24. \[Trade-off\] Team muốn dùng Cassandra để lưu tất cả data của** [**nguyentienkhoi.hashnode.dev**](http://nguyentienkhoi.hashnode.dev) **(users, courses, orders, enrollments, events). Senior reviewer phản đối. Phân tích.**

Câu hỏi open-ended — đánh giá tư duy:

**Cassandra phù hợp cho:**

*   Video tracking events (write-heavy, time-series) ✅
    
*   Activity logs ✅
    

**Cassandra KHÔNG phù hợp cho:**

*   **Users/Courses**: Schema thay đổi thường xuyên → ALTER TABLE khó. Cần flexible queries (search by name, filter by price) → ALLOW FILTERING
    
*   **Orders/Payments**: Cần ACID → Cassandra không có multi-row transaction. LWT quá chậm cho high-volume checkout
    
*   **Enrollments**: Cần check unique constraint (không được enroll 2 lần) → LWT quá đắt, hoặc phải thiết kế phức tạp
    
*   **Reporting/Analytics**: GROUP BY, JOIN, aggregate → Cassandra không support
    

**Kết luận mature:**

*   Cassandra cho: events, logs, tracking (write-heavy, time-series)
    
*   PostgreSQL cho: users, courses, orders, enrollments (OLTP, complex queries)
    
*   Polyglot persistence = đúng công cụ cho đúng bài toán
    
*   Over-engineering với Cassandra cho mọi thứ = unnecessary complexity + operational burden
    

  
**Q25. Giải thích Cassandra Gossip Protocol. Cassandra biết trạng thái của tất cả nodes bằng cách nào?**

Đáp án mong đợi:

**Gossip Protocol:**

*   Mỗi giây, mỗi node "gossip" với 1-3 nodes ngẫu nhiên
    
*   Trao đổi thông tin về trạng thái của mình và nodes khác
    
*   Lan truyền như virus: sau vài giây, mọi node đều biết về mọi thứ
    

**Thông tin được gossip:**

*   Node alive/dead (heartbeat)
    
*   Load (token ranges)
    
*   Schema version
    
*   Datacenter/rack information
    

**Failure Detection:**

*   Phi Accrual Failure Detector
    
*   Tính xác suất node down dựa trên heartbeat intervals
    
*   Không binary (alive/dead) mà là suspicion score
    
*   Giảm false positives trong network với high latency
    

**Seed nodes:**

```yaml
# cassandra.yaml
seed_provider:
  - class_name: org.apache.cassandra.locator.SimpleSeedProvider
    parameters:
      - seeds: "cassandra-1,cassandra-2"  # ít nhất 1 seed per DC
```

*   Seed nodes = initial contact points
    
*   Không cần tất cả nodes là seeds
    
*   Mới join cluster → gossip với seeds trước
    

## Bảng Điểm Đánh Giá


| Level | Câu hỏi | Pass khi |
|---|---|---|
| Junior | Q1–Q9 | Pass 7/9, bắt buộc Q3 (Partition Key) + Q5 (INSERT = UPSERT) + Q9 (coding) |
| Intermediate | Q10–Q16 | Pass 5/7, bắt buộc Q11 (Query-first) + Q12 (Compaction) + Q16 (coding) |
| Advanced | Q17–Q21 | Pass 4/5, bắt buộc Q17 (Write Path) + Q19 (System Design) |
| Senior | Q22–Q25 | Pass 3/4, đặc biệt Q22 (migration) + Q24 (trade-off thinking) |



## Câu Hỏi Bẫy Hay Dùng

**Bẫy 1:** "Cassandra có transaction không?" → Có, nhưng rất hạn chế. **Lightweight Transaction (LWT)** chỉ cho single partition, dùng Paxos, chậm 3-5x. Không có multi-row, multi-partition ACID transaction.

**Bẫy 2:** "INSERT trong Cassandra báo lỗi nếu row đã tồn tại?" → Không. Cassandra INSERT = UPSERT mặc định — ghi đè row cũ không có lỗi. Muốn "insert only if not exists" phải dùng `IF NOT EXISTS` (LWT).

**Bẫy 3:** "Tăng Replication Factor lên cao nhất có thể để an toàn nhất?" → Sai. RF cao → nhiều writes cần propagate → chậm hơn. RF > số nodes = không thể. RF=3 là chuẩn production.

**Bẫy 4:** "ALLOW FILTERING giải quyết được vấn đề query flexibility?" → ALLOW FILTERING hoạt động nhưng **scan toàn bộ cluster** — không scale. Giải pháp đúng: tạo table riêng cho query pattern đó.

**Bẫy 5:** "Cassandra đảm bảo data không bao giờ mất?" → Không. Nếu không repair đủ thường xuyên và node offline > gc\_grace\_seconds → **zombie data** (deleted data sống lại). Repair là bắt buộc.

**Bẫy 6:** "Compaction xảy ra ngay khi gọi nodetool compact?" → `nodetool compact` trigger manual compaction nhưng nó chạy async. Cassandra cũng tự compact theo policy. Quá nhiều compaction cùng lúc → disk I/O spike.

