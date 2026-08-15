# Cassandra Thực Chiến: Production, Compaction, Monitoring & Khi Nào Nên Dùng

![Cassandra Thực Chiến- Production, Compaction, Monitoring & Khi Nào Nên Dùng.jpeg](../images/35136ee7-8289-4aac-add3-83fc77ccaf0d.jpeg)

Bài 13 bạn đã thiết kế được schema đúng. Bài này là bài cuối của Cassandra series — tập trung vào **vận hành production**: compaction strategies cho time-series, repair, monitoring performance, và quan trọng nhất là **khi nào nên chọn Cassandra** thay vì PostgreSQL hay MongoDB. Hiểu rõ điều này giúp bạn tránh dùng Cassandra sai chỗ — một sai lầm tốn kém.

## 1\. Compaction — Dọn Dẹp Nội Bộ

### Tại Sao Cần Compaction?

```java
Cassandra writes:
  1. Write → MemTable (RAM)
  2. Flush → SSTable (disk, immutable)
  3. Theo thời gian: nhiều SSTables chồng chất
  4. Compaction: merge SSTables → 1 SSTable mới

Vấn đề không có compaction:
  → Read phải check nhiều SSTables → chậm dần
  → Delete không thực sự xóa — chỉ tạo "tombstone"
  → Storage tăng dần

Compaction giải quyết:
  → Merge SSTables → read nhanh hơn
  → Xóa tombstones thật sự
  → Thu hồi disk space
```

### Các Compaction Strategies

**Size-Tiered Compaction (STCS) — Mặc định:**

```sql
-- Tốt cho: write-heavy, ít reads
-- Cách hoạt động: merge SSTables có kích thước tương tự
CREATE TABLE events_stcs (
    course_id UUID,
    event_at  TIMESTAMP,
    user_id   UUID,
    action    TEXT,
    PRIMARY KEY (course_id, event_at, user_id)
) WITH compaction = {
    'class':               'SizeTieredCompactionStrategy',
    'min_threshold':       4,     -- merge khi có 4 SSTables cùng size
    'max_threshold':       32
};

-- Ưu: write nhanh, ít I/O trong quá trình compaction
-- Nhược: space amplification (cần 2x disk khi compact), read có thể chậm
```

**Leveled Compaction (LCS) — Cho Read-heavy:**

```sql
-- Tốt cho: read-heavy, data ít thay đổi
CREATE TABLE course_metadata (
    course_id UUID PRIMARY KEY,
    title     TEXT,
    category  TEXT,
    status    TEXT
) WITH compaction = {
    'class':              'LeveledCompactionStrategy',
    'sstable_size_in_mb': 160     -- target SSTable size
};

-- Ưu: read nhanh hơn (ít SSTables per level), predictable I/O
-- Nhược: higher write amplification, I/O intensive
```

**Time Window Compaction (TWCS) — Cho Time-series:**

```sql
-- Tốt nhất cho: time-series với TTL
-- Tạo 1 SSTable per time window, chỉ compact data trong cùng window
CREATE TABLE video_events_ts (
    course_id  UUID,
    event_date DATE,
    event_at   TIMESTAMP,
    user_id    UUID,
    action     TEXT,
    PRIMARY KEY ((course_id, event_date), event_at, user_id)
) WITH compaction = {
    'class':                   'TimeWindowCompactionStrategy',
    'compaction_window_size':  1,
    'compaction_window_unit':  'DAYS'   -- 1 SSTable per day
}
AND default_time_to_live = 7776000;    -- 90 days TTL

-- Ưu: SSTables tự expire với TTL → không cần compact tombstones
-- Perfect cho event logs, metrics, tracking data
```

**Chọn Compaction Strategy:**

```java
Write-heavy (events, logs)    → STCS hoặc TWCS (nếu có TTL)
Read-heavy (catalog, config)  → LCS
Time-series với TTL           → TWCS (bắt buộc)
Mixed workload                → STCS (default)
```

## 2\. Repair — Đảm Bảo Data Consistency

### Tại Sao Cần Repair?

```java
Vấn đề:
  Node 1 down → miss 1,000 writes
  Node 1 restart → data inconsistent với Node 2, Node 3
  Read repair: xảy ra khi read (lazy, chậm)
  Full repair: chủ động sync tất cả

Anti-entropy repair:
  → So sánh data giữa các nodes (Merkle tree)
  → Sync data bị thiếu
  → Chạy định kỳ, KHÔNG khi cluster đang dưới tải nặng
```

### Chạy Repair

```bash
# Full repair cho keyspace
docker exec cassandra-1 nodetool repair foxdev_dev

# Repair 1 table
docker exec cassandra-1 nodetool repair foxdev_dev events_by_course_day

# Sequential repair (ít ảnh hưởng hơn parallel)
docker exec cassandra-1 nodetool repair -seq foxdev_dev

# Incremental repair (chỉ repair data chưa repair lần trước)
docker exec cassandra-1 nodetool repair -ir foxdev_dev

# Xem trạng thái repair
docker exec cassandra-1 nodetool compactionstats
docker exec cassandra-1 nodetool tpstats | grep -i repair
```

**Lịch trình repair khuyến nghị:**

```java
Production:
  - Chạy full repair mỗi gc_grace_seconds / 2 (mặc định: 10 ngày)
  - gc_grace_seconds = thời gian tombstones tồn tại trước khi bị xóa
  - Mặc định gc_grace_seconds = 864000 (10 ngày)
  - → Repair mỗi 5 ngày

Lý do:
  Tombstone phải được repair trước khi bị xóa
  → Nếu không repair, node bị down > gc_grace_seconds
     có thể có "zombie data" (data đã xóa xuất hiện lại)
```

## 3\. Monitoring — Metrics Quan Trọng

### 3.1 Nodetool Commands

```bash
# ─── Cluster health ───
docker exec cassandra-1 nodetool status
# UN = Up + Normal ✅, DN = Down, UJ = Joining

docker exec cassandra-1 nodetool ring
# Xem token distribution

# ─── Performance ───
docker exec cassandra-1 nodetool tpstats
# Thread pool stats — blocked > 0 = bottleneck

docker exec cassandra-1 nodetool cfstats foxdev_dev.events_by_course_day
# Per-table stats: read/write latency, partition size

docker exec cassandra-1 nodetool tablestats foxdev_dev
# All tables stats

# ─── Garbage Collection ───
docker exec cassandra-1 nodetool gcstats
# Long GC pauses = performance issue

# ─── Compaction ───
docker exec cassandra-1 nodetool compactionstats
# Đang compact bao nhiêu, còn bao nhiêu

# ─── Node Info ───
docker exec cassandra-1 nodetool info
# Uptime, load, heap usage, key cache, row cache
```

### 3.2 Metrics Quan Trọng

```python
# Dùng Cassandra driver để query system tables
from cassandra.cluster import Cluster

def get_cluster_metrics(session):
    """Lấy metrics từ Cassandra system tables"""

    # Keyspace sizes
    rows = session.execute("""
        SELECT keyspace_name, table_name,
               mean_partition_size, max_partition_size,
               partitions_count
        FROM system.size_estimates
        WHERE keyspace_name = 'foxdev_dev'
    """)

    metrics = {}
    for row in rows:
        metrics[row.table_name] = {
            "mean_partition_size_kb": row.mean_partition_size // 1024,
            "max_partition_size_kb":  row.max_partition_size  // 1024,
            "estimated_partitions":   row.partitions_count,
        }

        # Alert nếu partition quá lớn
        if row.max_partition_size > 100 * 1024 * 1024:  # > 100MB
            print(f"⚠️  LARGE PARTITION: {row.table_name} max={row.max_partition_size//1024//1024}MB")

    return metrics


def check_read_write_latency(session):
    """Check latency từ system.metrics"""
    rows = session.execute("""
        SELECT metric_name, value
        FROM system_views.coordinator_write_latency
        LIMIT 10
    """)
    # Cassandra 4.0+ có system_views keyspace
    for row in rows:
        print(f"{row.metric_name}: {row.value}ms")
```

### 3.3 Python Health Check

```python
from cassandra.cluster import Cluster
from cassandra.policies import DCAwareRoundRobinPolicy
import time
from datetime import datetime

class CassandraHealthMonitor:

    def __init__(self, contact_points: list, keyspace: str):
        self.cluster   = Cluster(contact_points)
        self.session   = self.cluster.connect(keyspace)
        self.keyspace  = keyspace

    def check_cluster_health(self) -> dict:
        """Kiểm tra health của cluster"""
        start = time.time()

        # Test read/write latency
        test_id   = uuid.uuid4()
        write_lat = self._measure_write(test_id)
        read_lat  = self._measure_read(test_id)

        # Cleanup test data
        self.session.execute(
            "DELETE FROM health_check WHERE id = %s", (test_id,)
        )

        # Get connected nodes
        hosts    = self.cluster.metadata.all_hosts()
        up_nodes = sum(1 for h in hosts if h.is_up)

        health = {
            "timestamp":    datetime.now().isoformat(),
            "total_nodes":  len(hosts),
            "up_nodes":     up_nodes,
            "down_nodes":   len(hosts) - up_nodes,
            "write_lat_ms": write_lat,
            "read_lat_ms":  read_lat,
            "healthy":      up_nodes == len(hosts)
        }

        # Alerts
        if up_nodes < len(hosts):
            print(f"⚠️  {len(hosts) - up_nodes} node(s) DOWN!")
        if write_lat > 10:
            print(f"⚠️  High write latency: {write_lat:.2f}ms")
        if read_lat > 10:
            print(f"⚠️  High read latency: {read_lat:.2f}ms")

        return health

    def _measure_write(self, test_id) -> float:
        start = time.time()
        self.session.execute("""
            INSERT INTO health_check (id, ts) VALUES (%s, toTimestamp(now()))
            USING TTL 60
        """, (test_id,))
        return (time.time() - start) * 1000

    def _measure_read(self, test_id) -> float:
        start = time.time()
        self.session.execute(
            "SELECT * FROM health_check WHERE id = %s", (test_id,)
        )
        return (time.time() - start) * 1000

    def get_large_partitions(self, threshold_mb: int = 50) -> list:
        """Tìm partitions quá lớn"""
        rows = self.session.execute("""
            SELECT table_name, mean_partition_size, max_partition_size
            FROM system.size_estimates
            WHERE keyspace_name = %s
        """, (self.keyspace,))

        large = []
        for row in rows:
            if row.max_partition_size > threshold_mb * 1024 * 1024:
                large.append({
                    "table":       row.table_name,
                    "max_size_mb": row.max_partition_size // 1024 // 1024
                })
        return large
```

## 4\. Cassandra Thực Chiến — [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev)

### Pipeline Hoàn Chỉnh: User Xem Video

```python
from cassandra.cluster import Cluster
from cassandra.query import BatchStatement, SimpleStatement
from cassandra import ConsistencyLevel
import uuid
from datetime import datetime, date

class VideoEventPipeline:
    """
    Pipeline xử lý events khi user xem video.
    Write vào Cassandra (high throughput, time-series)
    Update stats trong Redis (real-time counter)
    """

    def __init__(self, cassandra_session, redis_client):
        self.cass  = cassandra_session
        self.redis = redis_client
        self._prepare()

    def _prepare(self):
        self.stmt_course_event = self.cass.prepare("""
            INSERT INTO events_by_course_day
                (course_id, event_date, event_at, user_id,
                 lecture_id, action, position, device)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            USING TTL 7776000
        """)

        self.stmt_user_event = self.cass.prepare("""
            INSERT INTO events_by_user
                (user_id, event_at, course_id, lecture_id, action, position)
            VALUES (?, ?, ?, ?, ?, ?)
            USING TTL 2592000
        """)

        self.stmt_progress = self.cass.prepare("""
            INSERT INTO progress_by_user_course
                (user_id, course_id, lecture_id, watched_pct, completed, last_watched)
            VALUES (?, ?, ?, ?, ?, ?)
        """)

        self.stmt_counter = self.cass.prepare("""
            UPDATE course_daily_counters
            SET total_events = total_events + 1,
                plays        = plays + ?
            WHERE course_id = ? AND date = ?
        """)

    def process_event(self, event: dict):
        """
        Xử lý 1 video event.
        event = {
            course_id, user_id, lecture_id,
            action, position, watched_pct, device
        }
        """
        now   = datetime.utcnow()
        today = date.today()

        course_id  = uuid.UUID(event["course_id"])
        user_id    = uuid.UUID(event["user_id"])
        lecture_id = uuid.UUID(event["lecture_id"])
        action     = event["action"]
        position   = event["position"]
        device     = event.get("device", "web")
        watched_pct = event.get("watched_pct", 0.0)
        is_play     = 1 if action == "play" else 0
        completed   = watched_pct >= 95.0  # xem > 95% = completed

        # ─── Cassandra writes ───
        batch = BatchStatement(
            consistency_level=ConsistencyLevel.LOCAL_ONE
        )
        batch.add(self.stmt_course_event,
                  (course_id, today, now, user_id, lecture_id, action, position, device))
        batch.add(self.stmt_user_event,
                  (user_id, now, course_id, lecture_id, action, position))
        batch.add(self.stmt_progress,
                  (user_id, course_id, lecture_id, watched_pct, completed, now))
        self.cass.execute(batch)

        # Counter riêng (không được trong batch với regular statements)
        self.cass.execute(self.stmt_counter, (is_play, course_id, today))

        # ─── Redis updates (real-time) ───
        pipe = self.redis.pipeline()
        pipe.hincrby(f"course:{course_id}:stats", "views", 1)
        if action == "play":
            pipe.hincrby(f"course:{course_id}:stats", "plays", 1)
        if completed:
            pipe.hincrby(f"course:{course_id}:stats", "completions", 1)
        pipe.expire(f"course:{course_id}:stats", 3600)
        pipe.execute()

    def get_course_analytics(self, course_id: uuid.UUID,
                              num_days: int = 7) -> list:
        """Lấy analytics của course trong N ngày qua"""
        from datetime import timedelta
        results = []
        today   = date.today()

        for i in range(num_days):
            d    = today - timedelta(days=i)
            rows = self.cass.execute("""
                SELECT date, total_events, plays, completions
                FROM course_daily_counters
                WHERE course_id = %s AND date = %s
            """, (course_id, d))
            row = rows.one()
            if row:
                results.append({
                    "date":         str(row.date),
                    "total_events": row.total_events or 0,
                    "plays":        row.plays or 0,
                    "completions":  row.completions or 0,
                })
        return results
```

## 5\. Khi Nào Nên Dùng Cassandra?

### Dùng Cassandra Khi:

```java
✅ Write throughput cực cao
   → > 10,000 writes/giây sustained
   → Event logging, tracking, IoT sensor data
   → nguyentienkhoi.hashnode.dev: video tracking events

✅ Time-series data
   → Metrics, logs, monitoring data
   → Data tự nhiên theo thời gian, query theo range
   → Ví dụ: "events của course X trong ngày Y"

✅ Không thể có single point of failure
   → Cần 99.999% uptime
   → Mất node không được downtime

✅ Scale horizontal đơn giản
   → Thêm node → tự động rebalance
   → Không cần resharding phức tạp như MongoDB

✅ Geo-distributed
   → Multi-datacenter replication built-in
   → Data residency requirements

✅ Data có TTL tự nhiên
   → Logs, sessions, cache
   → Cassandra TTL hiệu quả hơn periodic DELETE
```

### KHÔNG Dùng Cassandra Khi:

```java
❌ Cần flexible query
   → Không biết trước access patterns
   → Cần filter bất kỳ column nào
   → → Dùng PostgreSQL hoặc MongoDB

❌ Cần ACID transaction
   → Payment processing
   → Inventory management
   → → Dùng PostgreSQL

❌ Cần aggregate phức tạp
   → SUM, AVG, GROUP BY nhiều chiều
   → Business reporting, analytics
   → → Dùng PostgreSQL + read replica hoặc Data Warehouse

❌ Dataset nhỏ (< 1M rows)
   → Overhead của distributed system không đáng
   → → PostgreSQL đủ

❌ Team nhỏ, chưa có Cassandra experience
   → Learning curve cao
   → Operational complexity lớn
   → → Bắt đầu với PostgreSQL

❌ Cần schema thay đổi thường xuyên
   → ALTER TABLE trong Cassandra khó hơn nhiều
   → → MongoDB hoặc PostgreSQL với JSONB
```

### Decision Framework Cho [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev)

```java
Video tracking events (10M/ngày)
  → Write-heavy, time-series, có TTL
  → ✅ Cassandra

User orders, payments
  → ACID required, complex queries
  → ✅ PostgreSQL

Course catalog
  → Flexible schema, search
  → ✅ PostgreSQL (hoặc MongoDB nếu schema thực sự flexible)

Session, cache
  → Sub-ms latency, TTL
  → ✅ Redis

Analytics dashboard
  → Complex aggregation, historical
  → ✅ PostgreSQL + Materialized Views
     (hoặc BigQuery nếu scale lớn)
```

## 6\. Performance Tuning

### 6.1 Caching

```sql
-- Key Cache: cache partition key locations (default: 100MB)
-- → Tránh phải tìm SSTable mỗi lần read

-- Row Cache: cache entire rows (disabled by default)
-- → Chỉ bật cho tables đọc nhiều, ít ghi
ALTER TABLE progress_by_user_course
WITH caching = {
    'keys':       'ALL',
    'rows_per_partition': '100'  -- cache 100 rows per partition
};

-- Xem cache stats
docker exec cassandra-1 nodetool info | grep Cache
```

### 6.2 Tuning JVM Heap

```bash
# cassandra-env.sh hoặc environment variables
# Rule of thumb: MAX_HEAP_SIZE = min(RAM/4, 8GB)
# Không đặt > 16GB (GC pauses tăng)

# Docker:
environment:
  - MAX_HEAP_SIZE=4G
  - HEAP_NEWSIZE=800M   # ~1/4 của MAX_HEAP_SIZE

# Kiểm tra GC pauses
docker exec cassandra-1 nodetool gcstats
# 50th: pause < 200ms ✅
# 99th: pause < 1000ms ✅
```

### 6.3 Compression

```sql
-- LZ4 (default Cassandra 4.0+) — fastest
-- Snappy — fast, good ratio
-- Deflate (zlib) — best ratio, slow
-- Zstd — excellent ratio + good speed (Cassandra 4.0+)

ALTER TABLE video_events_ts
WITH compression = {
    'class':               'ZstdCompressor',
    'compression_level':   3    -- 1 (fast) đến 22 (best)
};
```

## 7\. Migration — Thêm Column & Thay Đổi Schema

```sql
-- ✅ An toàn: thêm column mới
ALTER TABLE events_by_course_day ADD ip_address TEXT;

-- ✅ An toàn: xóa column (data vẫn còn cho đến khi compact)
ALTER TABLE events_by_course_day DROP ip_address;

-- ✅ An toàn: đổi column type tương thích
ALTER TABLE events_by_course_day ALTER position TYPE BIGINT;

-- ❌ KHÔNG thể làm:
-- Đổi PRIMARY KEY (partition key hoặc clustering key)
-- DROP PRIMARY KEY column
-- Đổi column type không tương thích

-- Nếu cần đổi PRIMARY KEY:
-- 1. Tạo table mới với schema mới
-- 2. Migrate data (application dual-write)
-- 3. Verify data đúng
-- 4. Chuyển traffic sang table mới
-- 5. Drop table cũ
```

## 8\. Production Checklist

```java
✅ CLUSTER:
□ Tối thiểu 3 nodes để có fault tolerance
□ Replication Factor = 3 (production)
□ Mỗi datacenter ít nhất 3 nodes
□ nodetool status tất cả UN (Up+Normal)

✅ PERFORMANCE:
□ Compaction strategy phù hợp với access pattern
□ TWCS cho time-series có TTL
□ JVM heap tuned (không > 8-16GB)
□ SSD cho data volumes
□ Không có large partitions (> 100MB)

✅ MAINTENANCE:
□ Repair job chạy định kỳ (< gc_grace_seconds/2)
□ gc_grace_seconds phù hợp với repair schedule
□ Monitor disk space (compact cần 2x space tạm thời)

✅ MONITORING:
□ nodetool tpstats không có blocked threads
□ Read/write latency trong acceptable range (< 10ms P99)
□ GC pauses không quá cao
□ Replication lag alert

✅ APPLICATION:
□ Dùng Prepared Statements cho mọi query
□ Consistency Level phù hợp (LOCAL_QUORUM thường là sweet spot)
□ Connection pool tuned
□ Handle timeout và retry trong application
□ Không dùng ALLOW FILTERING không có partition key
```

## Tổng Kết Cassandra Series


| Bài | Chủ đề | Điểm cốt lõi |
|---|---|---|
| Bài 11 | Kiến trúc | Peer-to-peer, ring, RF, Consistency Level |
| Bài 12 | CQL | Partition Key, Clustering Key, query rules |
| Bài 13 | Data Modeling | Query-first, 1 query = 1 table, denormalize |
| Bài 14 | Production | TWCS, repair, monitoring, khi nào dùng |



```java
Cassandra phù hợp nhất khi:
  Write-heavy + Time-series + High Availability + Scale
  
Không phù hợp khi:
  ACID + Flexible Query + Small Dataset + Team nhỏ
```

Bài tiếp theo chúng ta sẽ học **Neo4j** — Graph Database cho relationship-heavy data, Cypher Query Language và khi nào graph database tốt hơn relational database.

