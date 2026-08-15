# Cassandra Là Gì? Kiến Trúc & Cài Đặt

![Cassandra Là Gì? Kiến Trúc & Cài Đặt.jpeg](../images/43b26569-3e82-4c04-b6ea-244ed1653381.jpeg)

MongoDB tốt cho flexible documents. Redis tốt cho speed. Nhưng khi [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev) cần ghi **10 triệu video tracking events mỗi ngày** — mỗi giây có hàng trăm students đang xem video, mỗi action (play, pause, seek, end) đều cần ghi lại — PostgreSQL bắt đầu chịu không nổi, MongoDB cũng gặp khó. Đây là bài toán của **Apache Cassandra**: database được thiết kế từ đầu để write với throughput cực cao, không có single point of failure.

## 1\. Cassandra Là Gì?

**Apache Cassandra** là **distributed wide-column database** — được thiết kế để:

*   Ghi dữ liệu với throughput cực cao (hàng triệu writes/giây)
    
*   Scale ngang không giới hạn — thêm node là tăng capacity
    
*   Không có single point of failure — mọi node đều bình đẳng
    
*   Chịu được mất node mà không downtime
    

**Ra đời:** Facebook 2008 để xử lý inbox search. Open-source 2009, Apache project 2010.

**Ai đang dùng:**

*   Netflix — ghi billions of events/ngày cho recommendation system
    
*   Apple — 75,000+ Cassandra nodes
    
*   Discord — lưu trữ hàng tỷ messages
    
*   Instagram — activity feed
    

* * *

## 2\. Cassandra vs SQL vs MongoDB


| Tiêu chí | PostgreSQL | MongoDB | Cassandra |
|---|---|---|---|
| Data model | Tables/Rows | Documents | Wide-column rows |
| Write throughput | Tốt | Tốt | Cực cao |
| Read flexibility | Rất tốt (JOIN, filter bất kỳ) | Tốt | Hạn chế (phải biết trước query) |
| Scale | Vertical chủ yếu | Horizontal (sharding) | Horizontal tự nhiên |
| ACID | ✅ Full | ✅ Document-level | ❌ Eventual consistency |
| Single point of failure | ❌ Có (Primary) | ❌ Có (Primary) | ✅ Không có |
| Schema | Strict | Flexible | Semi-strict |
| Best for | OLTP, reporting | Flexible content | Write-heavy, time-series |



## 3\. Kiến Trúc Cassandra

### 3.1 Peer-to-peer, Không Có Master

```java
PostgreSQL / MongoDB:
  Primary ←→ Replica 1
           ←→ Replica 2
  → Primary = single point of failure
  → Primary down = writes bị chặn

Cassandra:
  Node 1 ←→ Node 2 ←→ Node 3 ←→ Node 4
  → Tất cả nodes BÌNH ĐẲNG — không có master
  → Bất kỳ node nào cũng có thể nhận read/write
  → 1 node down → 3 nodes còn lại vẫn phục vụ
```

### 3.2 Ring Architecture & Token

Cassandra tổ chức các nodes thành **ring** — mỗi node chịu trách nhiệm cho một range of tokens:

```java
                 Node 1
                 (0-25%)
              ↗           ↖
    Node 4                  Node 2
    (75-100%)              (25-50%)
              ↘           ↗
                 Node 3
                 (50-75%)
```

Khi ghi data, Cassandra hash **Partition Key** → token → gửi đến đúng node chịu trách nhiệm range đó.

### 3.3 Replication Factor

```java
Replication Factor (RF) = số copies của mỗi row

RF=1: mỗi row lưu ở 1 node → không fault tolerant
RF=2: mỗi row lưu ở 2 nodes → chịu được mất 1 node
RF=3: mỗi row lưu ở 3 nodes → chịu được mất 1 node (production standard)

Với RF=3 và 4 nodes:
Row A → hash → token 15% → Node 1 (primary)
                          → Node 2 (replica 1)
                          → Node 3 (replica 2)
```

### 3.4 Consistency Level

Cassandra cho phép chọn trade-off giữa consistency và availability **per query**:

```java
Write với CL=ONE:
  → Ghi thành công khi 1 node xác nhận
  → Nhanh nhất, eventual consistency

Write với CL=QUORUM:
  → Ghi thành công khi majority nodes xác nhận (ví dụ 2/3)
  → Cân bằng: vừa nhanh vừa đủ consistent

Write với CL=ALL:
  → Tất cả nodes phải xác nhận
  → Chậm nhất, strong consistency

Quy tắc strong consistency:
  Write CL + Read CL > Replication Factor
  → QUORUM + QUORUM > RF=3 (2+2=4 > 3) ✅
```

### 3.5 Write Path — Tại Sao Writes Nhanh

```java
Client ghi data
        ↓
Coordinator Node (nhận request)
        ↓
Ghi vào 2 nơi SONG SONG:
  ┌─────────────────────────────┐
  │ 1. Commit Log (sequential)  │ ← append-only, cực nhanh
  │ 2. MemTable (in-memory)     │ ← in-memory, cực nhanh
  └─────────────────────────────┘
        ↓ (khi MemTable đầy)
  Flush xuống SSTable (disk)
        ↓ (định kỳ)
  Compaction — merge SSTables
```

**Tại sao nhanh hơn PostgreSQL:**

*   PostgreSQL: random I/O (update existing data on disk)
    
*   Cassandra: sequential I/O, append-only → disk nhanh hơn nhiều với sequential writes
    

## 4\. Cài Đặt Cassandra

### Cách 1: Docker (Khuyến Nghị)

```bash
# Single node để học
docker run -d \
  --name cassandra \
  -p 9042:9042 \
  -e CASSANDRA_CLUSTER_NAME=foxdev_cluster \
  -e CASSANDRA_DC=datacenter1 \
  -v cassandra_data:/var/lib/cassandra \
  cassandra:4.1

# Đợi Cassandra khởi động (~30-60 giây)
docker logs -f cassandra
# Đợi thấy: "Created default superuser role 'cassandra'"

# Kiểm tra
docker exec -it cassandra nodetool status
# Datacenter: datacenter1
# ======================
# Status=Up/Down, State=Normal/Leaving/Joining/Moving
# --  Address    Load       Tokens  Owns  Host ID  Rack
# UN  172.17.0.2 75.04 KiB  16      ?     abc123   rack1
# UN = Up + Normal ✅
```

### Cách 2: Docker Compose — 3-node Cluster

```yaml
# docker-compose.yml
version: '3.8'

services:
  cassandra-1:
    image: cassandra:4.1
    container_name: cassandra-1
    hostname: cassandra-1
    ports:
      - "9042:9042"
    environment:
      - CASSANDRA_CLUSTER_NAME=foxdev_cluster
      - CASSANDRA_DC=datacenter1
      - CASSANDRA_RACK=rack1
      - CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch
      - HEAP_NEWSIZE=256M
      - MAX_HEAP_SIZE=512M
    volumes:
      - cassandra1_data:/var/lib/cassandra
    healthcheck:
      test: ["CMD-SHELL", "nodetool status | grep -q '^UN'"]
      interval: 30s
      timeout: 10s
      retries: 10
    networks:
      - cassandra-net

  cassandra-2:
    image: cassandra:4.1
    container_name: cassandra-2
    hostname: cassandra-2
    ports:
      - "9043:9042"
    environment:
      - CASSANDRA_CLUSTER_NAME=foxdev_cluster
      - CASSANDRA_DC=datacenter1
      - CASSANDRA_RACK=rack1
      - CASSANDRA_SEEDS=cassandra-1     # join cluster qua seed node
      - CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch
      - HEAP_NEWSIZE=256M
      - MAX_HEAP_SIZE=512M
    volumes:
      - cassandra2_data:/var/lib/cassandra
    depends_on:
      cassandra-1:
        condition: service_healthy
    networks:
      - cassandra-net

  cassandra-3:
    image: cassandra:4.1
    container_name: cassandra-3
    hostname: cassandra-3
    ports:
      - "9044:9042"
    environment:
      - CASSANDRA_CLUSTER_NAME=foxdev_cluster
      - CASSANDRA_DC=datacenter1
      - CASSANDRA_RACK=rack1
      - CASSANDRA_SEEDS=cassandra-1
      - CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch
      - HEAP_NEWSIZE=256M
      - MAX_HEAP_SIZE=512M
    volumes:
      - cassandra3_data:/var/lib/cassandra
    depends_on:
      cassandra-2:
        condition: service_healthy
    networks:
      - cassandra-net

volumes:
  cassandra1_data:
  cassandra2_data:
  cassandra3_data:

networks:
  cassandra-net:
    driver: bridge
```

```bash
# Khởi động (đợi node 1 healthy trước khi add node 2, 3)
docker-compose up -d cassandra-1
# Đợi ~60 giây
docker-compose up -d cassandra-2
# Đợi ~60 giây
docker-compose up -d cassandra-3

# Kiểm tra cluster
docker exec cassandra-1 nodetool status
# UN  172.18.0.2  ... cassandra-1
# UN  172.18.0.3  ... cassandra-2
# UN  172.18.0.4  ... cassandra-3
# 3 nodes đều Up + Normal ✅
```

## 5\. CQL Shell — Giao Diện Dòng Lệnh

**CQL (Cassandra Query Language)** trông rất giống SQL nhưng có nhiều điểm khác biệt quan trọng.

```bash
# Mở CQL Shell
docker exec -it cassandra cqlsh

# Với authentication
docker exec -it cassandra cqlsh -u cassandra -p cassandra

# Kết nối remote
cqlsh 127.0.0.1 9042 -u cassandra -p cassandra
```

```sql
-- Kiểm tra cluster info
DESCRIBE CLUSTER;
-- Cluster: foxdev_cluster

-- Xem keyspaces (tương tự databases)
DESCRIBE KEYSPACES;
-- system  system_auth  system_distributed  system_schema  system_traces

-- Xem version
SELECT release_version FROM system.local;
```

## 6\. Keyspace & Table Cơ Bản

### Tạo Keyspace (Database)

```sql
-- Tạo keyspace cho nguyentienkhoi.hashnode.dev
CREATE KEYSPACE IF NOT EXISTS foxdev
WITH REPLICATION = {
    'class':              'NetworkTopologyStrategy',
    'datacenter1':        3   -- RF=3 cho datacenter1
}
AND DURABLE_WRITES = true;

-- Development (single node): dùng SimpleStrategy
CREATE KEYSPACE IF NOT EXISTS foxdev_dev
WITH REPLICATION = {
    'class':              'SimpleStrategy',
    'replication_factor': 1   -- RF=1 cho dev
};

-- Sử dụng keyspace
USE foxdev_dev;

-- Xem keyspace info
DESCRIBE KEYSPACE foxdev_dev;
```

### Tạo Table Đầu Tiên

```sql
-- Bảng video tracking events
CREATE TABLE IF NOT EXISTS video_tracking (
    course_id    UUID,
    user_id      UUID,
    watched_at   TIMESTAMP,
    action       TEXT,          -- 'play', 'pause', 'seek', 'end'
    position     INT,           -- giây trong video
    session_id   TEXT,
    device       TEXT,

    -- PRIMARY KEY gồm 2 phần:
    -- Partition Key: (course_id) → quyết định node nào lưu
    -- Clustering Key: (watched_at, user_id) → thứ tự trong partition
    PRIMARY KEY ((course_id), watched_at, user_id)
)
WITH CLUSTERING ORDER BY (watched_at DESC, user_id ASC)
AND compaction = {
    'class': 'TimeWindowCompactionStrategy',  -- tối ưu cho time-series
    'compaction_window_size': 1,
    'compaction_window_unit': 'DAYS'
}
AND default_time_to_live = 7776000;           -- tự xóa sau 90 ngày (giây)
```

## 7\. Insert và Query Đầu Tiên

```sql
-- Insert events
INSERT INTO video_tracking (
    course_id, user_id, watched_at, action, position, session_id, device
) VALUES (
    uuid(), uuid(), toTimestamp(now()),
    'play', 0, 'session_abc123', 'mobile'
);

-- Insert với TTL riêng (override default_time_to_live)
INSERT INTO video_tracking (
    course_id, user_id, watched_at, action, position, session_id, device
) VALUES (
    11111111-1111-1111-1111-111111111111,
    22222222-2222-2222-2222-222222222222,
    '2025-03-15 09:00:00',
    'play', 0, 'session_001', 'desktop'
) USING TTL 86400;   -- chỉ tồn tại 24 giờ

-- Query (PHẢI có partition key)
SELECT * FROM video_tracking
WHERE course_id = 11111111-1111-1111-1111-111111111111;

-- Query với range trên clustering key
SELECT * FROM video_tracking
WHERE course_id  = 11111111-1111-1111-1111-111111111111
  AND watched_at >= '2025-03-15 00:00:00'
  AND watched_at <  '2025-03-16 00:00:00';

-- Giới hạn kết quả
SELECT * FROM video_tracking
WHERE course_id = 11111111-1111-1111-1111-111111111111
LIMIT 100;
```

## 8\. Tạo Dữ Liệu Mẫu [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev)

```sql
USE foxdev_dev;

-- ─── Table: user_activity_by_course ───
-- Query pattern: "Tất cả activities của user trong một course"
CREATE TABLE user_activity_by_course (
    course_id    UUID,
    user_id      UUID,
    activity_at  TIMESTAMP,
    activity_type TEXT,    -- 'watch', 'complete_lecture', 'download', 'comment'
    lecture_id   UUID,
    metadata     TEXT,     -- JSON string
    PRIMARY KEY ((course_id, user_id), activity_at)
) WITH CLUSTERING ORDER BY (activity_at DESC);

-- ─── Table: course_stats_by_date ───
-- Query pattern: "Stats của course theo ngày"
CREATE TABLE course_stats_by_date (
    course_id    UUID,
    date         DATE,
    unique_views COUNTER,
    total_plays  COUNTER,
    PRIMARY KEY (course_id, date)
) WITH CLUSTERING ORDER BY (date DESC);

-- ─── Table: user_sessions ───
-- Query pattern: "Sessions của user trong khoảng thời gian"
CREATE TABLE user_sessions (
    user_id      UUID,
    session_start TIMESTAMP,
    session_id   TEXT,
    device       TEXT,
    ip_address   TEXT,
    duration_sec INT,
    pages_viewed INT,
    PRIMARY KEY (user_id, session_start)
) WITH CLUSTERING ORDER BY (session_start DESC)
AND default_time_to_live = 2592000;  -- giữ 30 ngày

-- Insert dữ liệu mẫu
INSERT INTO video_tracking (course_id, user_id, watched_at, action, position, session_id, device)
VALUES (11111111-1111-1111-1111-111111111111, aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa, '2025-03-15 09:00:00', 'play',  0,    'sess_001', 'desktop');

INSERT INTO video_tracking (course_id, user_id, watched_at, action, position, session_id, device)
VALUES (11111111-1111-1111-1111-111111111111, aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa, '2025-03-15 09:05:33', 'pause', 333,  'sess_001', 'desktop');

INSERT INTO video_tracking (course_id, user_id, watched_at, action, position, session_id, device)
VALUES (11111111-1111-1111-1111-111111111111, aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa, '2025-03-15 09:06:02', 'play',  333,  'sess_001', 'desktop');

INSERT INTO video_tracking (course_id, user_id, watched_at, action, position, session_id, device)
VALUES (11111111-1111-1111-1111-111111111111, bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb, '2025-03-15 10:30:00', 'play',  0,    'sess_002', 'mobile');

INSERT INTO video_tracking (course_id, user_id, watched_at, action, position, session_id, device)
VALUES (11111111-1111-1111-1111-111111111111, bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb, '2025-03-15 10:55:20', 'end',   2720, 'sess_002', 'mobile');

-- Xem dữ liệu
SELECT * FROM video_tracking
WHERE course_id = 11111111-1111-1111-1111-111111111111;

-- Update COUNTER table
UPDATE course_stats_by_date
SET unique_views = unique_views + 1,
    total_plays  = total_plays + 1
WHERE course_id = 11111111-1111-1111-1111-111111111111
  AND date      = '2025-03-15';

-- Xem counter
SELECT * FROM course_stats_by_date
WHERE course_id = 11111111-1111-1111-1111-111111111111;
```

## 9\. Nodetool — Quản Lý Cluster

```bash
# Xem trạng thái cluster
docker exec cassandra-1 nodetool status
# U = Up, D = Down, N = Normal, L = Leaving, J = Joining

# Xem thông tin ring
docker exec cassandra-1 nodetool ring

# Xem thông tin node hiện tại
docker exec cassandra-1 nodetool info

# Xem tải của từng node
docker exec cassandra-1 nodetool tpstats

# Kiểm tra keyspace
docker exec cassandra-1 nodetool describering foxdev_dev

# Repair data (sync giữa các nodes)
docker exec cassandra-1 nodetool repair foxdev_dev

# Flush MemTable xuống SSTable
docker exec cassandra-1 nodetool flush foxdev_dev

# Compaction status
docker exec cassandra-1 nodetool compactionstats
```

## 10\. Troubleshooting Khi Cài Đặt

### Node không join cluster

```bash
# Xem logs
docker logs cassandra-2 | tail -50

# Lỗi phổ biến: "Unable to gossip with any seeds"
# → cassandra-1 chưa sẵn sàng khi cassandra-2 start
# Fix: đợi cassandra-1 healthy trước

# Kiểm tra seed node có accessible không
docker exec cassandra-2 ping cassandra-1
```

### Cassandra dùng quá nhiều RAM

```bash
# Giảm heap size trong docker-compose
environment:
  - HEAP_NEWSIZE=128M    # giảm từ 256M
  - MAX_HEAP_SIZE=256M   # giảm từ 512M

# Minimum để chạy dev: 256MB heap
```

### CQL Shell không kết nối được

```bash
# Đợi Cassandra hoàn toàn khởi động
docker exec cassandra-1 nodetool status
# Phải thấy "UN" (Up + Normal) trước khi cqlsh

# Nếu vẫn lỗi:
docker exec cassandra-1 cqlsh localhost 9042
# Thử với localhost thay vì cassandra-1
```

## Tổng Kết


| Khái niệm | Ý nghĩa |
|---|---|
| Keyspace | Tương tự Database — chứa tables, config replication |
| Partition Key | Quyết định node nào lưu data — phải có trong mọi query |
| Clustering Key | Thứ tự data trong partition — range query được |
| Replication Factor | Số copies của mỗi row — RF=3 là chuẩn production |
| Consistency Level | ONE/QUORUM/ALL — trade-off speed vs consistency |
| MemTable | In-memory write buffer — writes cực nhanh |
| SSTable | Immutable on-disk file — từ MemTable flush xuống |
| Compaction | Merge SSTables — maintain performance theo thời gian |
| nodetool | CLI quản lý cluster |
| cqlsh | CQL Shell — tương tự psql cho PostgreSQL |



**Nguyên tắc quan trọng nhất của Cassandra:**

> **Design your data model based on your queries, not your data structure.**

Bài tiếp theo chúng ta sẽ đi sâu vào **CQL và Data Modeling** — Partition Key, Clustering Key, ALLOW FILTERING, Secondary Index và cách thiết kế table đúng cho từng query pattern.

