# Redis Là Gì? Cài Đặt & Các Kiểu Dữ Liệu

![Redis Là Gì? Cài Đặt & Các Kiểu Dữ Liệu.jpeg](../images/75988274-e112-416c-b80c-063fd8f57f19.jpeg)

Bạn đã có PostgreSQL lưu dữ liệu chính và MongoDB cho flexible content. Nhưng khi user load trang chủ [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev), hệ thống phải query 5-6 bảng, tính toán recommendations, lấy danh sách khóa học — mỗi request mất 200-500ms. **Redis** giải quyết điều này bằng cách lưu kết quả vào bộ nhớ RAM — đọc trong **microseconds** thay vì milliseconds. Đây là công cụ không thể thiếu trong bất kỳ backend production nào.

## 1\. Redis Là Gì?

**Redis** (Remote Dictionary Server) là **in-memory data structure store** — lưu dữ liệu trong RAM với tốc độ cực nhanh.

```java
PostgreSQL:  Lưu trên disk → ~1-50ms
MongoDB:     Lưu trên disk → ~1-20ms
Redis:       Lưu trong RAM → ~0.1-1ms (100-1000x nhanh hơn)
```

**Redis không chỉ là cache** — nó còn là:

*   **Session store**: lưu user sessions
    
*   **Message queue**: Pub/Sub, Streams
    
*   **Rate limiter**: giới hạn request
    
*   **Distributed lock**: đảm bảo chỉ 1 process chạy
    
*   **Leaderboard**: real-time ranking
    
*   **Geospatial**: tìm kiếm theo vị trí
    

**Đặc điểm quan trọng:**

*   **Single-threaded**: 1 thread xử lý tất cả — không có race condition, thread-safe
    
*   **Atomic operations**: mọi command đều atomic
    
*   **Persistence**: có thể lưu xuống disk (RDB snapshot, AOF log)
    
*   **Expiration**: mỗi key có thể set TTL — tự động xóa khi hết hạn
    

## 2\. Cài Đặt Redis

### Cách 1: Docker (Khuyến Nghị)

```bash
# Chạy Redis với password
docker run -d \
  --name redis \
  -p 6379:6379 \
  -v redis_data:/data \
  redis:7.2-alpine \
  redis-server --requirepass "password123" --appendonly yes

# Kiểm tra
docker ps | grep redis
docker logs redis

# Kết nối vào Redis CLI
docker exec -it redis redis-cli -a password123
```

### Cách 2: Docker Compose

```yaml
# docker-compose.yml — thêm vào setup hiện tại
version: '3.8'

services:
  redis:
    image: redis:7.2-alpine
    container_name: redis
    restart: unless-stopped
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    command: >
      redis-server
      --requirepass password123
      --appendonly yes
      --maxmemory 512mb
      --maxmemory-policy allkeys-lru
    healthcheck:
      test: ["CMD", "redis-cli", "-a", "password123", "ping"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  redis_data:
```

```bash
docker-compose up -d redis
```

### Cách 3: Cài Trực Tiếp

**macOS:**

```bash
brew install redis
brew services start redis
redis-cli ping  # → PONG
```

**Ubuntu/Debian:**

```bash
sudo apt install redis-server
sudo systemctl start redis-server
sudo systemctl enable redis-server
redis-cli ping  # → PONG
```

**Windows:** Dùng WSL2 hoặc Docker — Redis không có official Windows binary.

## 3\. Redis CLI — Giao Diện Dòng Lệnh

```bash
# Kết nối
redis-cli                           # local, không auth
redis-cli -h localhost -p 6379      # chỉ định host/port
redis-cli -a password123            # có auth
redis-cli -u redis://:password123@localhost:6379  # URI format

# Trong Redis CLI
127.0.0.1:6379> PING
# → PONG

# Xem tất cả keys (cẩn thận ở production — dùng SCAN thay thế)
127.0.0.1:6379> KEYS *

# Xóa toàn bộ database hiện tại
127.0.0.1:6379> FLUSHDB

# Xem thông tin server
127.0.0.1:6379> INFO server
127.0.0.1:6379> INFO memory

# Chọn database (Redis có 16 databases: 0-15)
127.0.0.1:6379> SELECT 1
```

## 4\. Kiểu Dữ Liệu Trong Redis

Redis có **5 kiểu dữ liệu chính** — mỗi kiểu phù hợp với use case khác nhau:

```java
String  → Cache, counter, session
Hash    → Object/document
List    → Queue, feed
Set     → Unique collection, tags
Sorted Set → Leaderboard, ranking
```

## 5\. String — Kiểu Dữ Liệu Cơ Bản Nhất

String trong Redis lưu được: text, số, binary data (JSON, image bytes...).

```bash
# SET và GET
SET course:1:title "Spring Boot từ Zero đến Hero"
GET course:1:title
# → "Spring Boot từ Zero đến Hero"

# SET với TTL (expire sau 3600 giây = 1 giờ)
SET cache:courses:published "[{...}]" EX 3600
# Hoặc dùng SETEX (cũ hơn, tương đương)
SETEX cache:courses:published 3600 "[{...}]"

# SET chỉ khi key chưa tồn tại (NX = Not eXist)
SET lock:payment:order_1 "processing" NX EX 30
# → OK nếu chưa tồn tại, nil nếu đã tồn tại

# TTL — xem thời gian còn lại (giây)
TTL cache:courses:published
# → 3543 (còn 3543 giây)
# → -1 (không có TTL)
# → -2 (key không tồn tại)

# EXPIRE — đặt TTL cho key đã tồn tại
EXPIRE cache:courses:published 1800

# DEL — xóa key
DEL cache:courses:published

# EXISTS — kiểm tra key có tồn tại
EXISTS course:1:title
# → 1 (tồn tại) hoặc 0 (không tồn tại)

# MSET / MGET — set/get nhiều keys cùng lúc
MSET course:1:price 799000 course:2:price 599000 course:3:price 899000
MGET course:1:price course:2:price course:3:price
# → ["799000", "599000", "899000"]
```

### Counter — Atomic Increment

```bash
# INCR — tăng 1 (atomic)
SET page:views:course_1 100
INCR page:views:course_1
# → 101
INCR page:views:course_1
# → 102

# INCRBY — tăng N
INCRBY page:views:course_1 50
# → 152

# DECR / DECRBY — giảm
DECR page:views:course_1
DECRBY page:views:course_1 10

# INCR là atomic — an toàn khi nhiều clients cùng increment
```

## 6\. Hash — Lưu Object/Document

Hash lưu key-value pairs bên trong một key — tương tự object trong JavaScript hay Map trong Java.

```bash
# HSET — set một hoặc nhiều fields
HSET user:1 email "nam@gmail.com" first_name "Nam" last_name "Nguyen" account_status "ACTIVE"

# HGET — lấy 1 field
HGET user:1 email
# → "nam@gmail.com"

# HMGET — lấy nhiều fields
HMGET user:1 email first_name last_name
# → ["nam@gmail.com", "Nam", "Nguyen"]

# HGETALL — lấy tất cả fields
HGETALL user:1
# → email → nam@gmail.com
# → first_name → Nam
# → last_name → Nguyen
# → account_status → ACTIVE

# HDEL — xóa field
HDEL user:1 account_status

# HEXISTS — kiểm tra field tồn tại
HEXISTS user:1 email
# → 1

# HKEYS / HVALS / HLEN
HKEYS user:1     # → tất cả field names
HVALS user:1     # → tất cả values
HLEN  user:1     # → số fields

# HINCRBY — tăng giá trị số trong hash
HSET course:1 enrolled_count 320
HINCRBY course:1 enrolled_count 1
# → 321
```

**So sánh Hash vs String JSON:**

```bash
# String: lưu toàn bộ object dưới dạng JSON string
SET user:1 '{"email":"nam@gmail.com","name":"Nam","score":100}'
# Muốn update score → phải GET toàn bộ, parse, update, SET lại

# Hash: lưu từng field riêng
HSET user:1 email "nam@gmail.com" name "Nam" score 100
# Muốn update score → HINCRBY user:1 score 10 (atomic, không cần GET)
```

**Dùng Hash khi:** object có nhiều fields cần update riêng lẻ **Dùng String JSON khi:** object nhỏ, luôn đọc/ghi toàn bộ

## 7\. List — Danh Sách Có Thứ Tự

List là linked list doubly — thêm/xóa ở đầu/cuối O(1), truy cập giữa O(n).

```bash
# LPUSH — thêm vào đầu (Left)
LPUSH notifications:user_1 "Khóa học đã được duyệt"
LPUSH notifications:user_1 "Đơn hàng #123 đã thanh toán"
LPUSH notifications:user_1 "Bạn có tin nhắn mới"

# RPUSH — thêm vào cuối (Right)
RPUSH queue:emails "email_task_1"
RPUSH queue:emails "email_task_2"

# LRANGE — lấy range (0 = đầu, -1 = cuối)
LRANGE notifications:user_1 0 -1     # lấy tất cả
LRANGE notifications:user_1 0 9      # lấy 10 cái đầu
# → ["Bạn có tin nhắn mới", "Đơn hàng #123...", "Khóa học..."]

# LLEN — độ dài list
LLEN notifications:user_1
# → 3

# LPOP / RPOP — lấy và xóa phần tử
LPOP notifications:user_1          # lấy từ đầu
RPOP queue:emails                  # lấy từ cuối (FIFO queue)

# BRPOP — blocking pop (chờ nếu list rỗng) → dùng cho job queue
BRPOP queue:emails 30              # chờ tối đa 30 giây

# LTRIM — chỉ giữ lại range nhất định
LTRIM notifications:user_1 0 99    # chỉ giữ 100 notifications gần nhất
```

**Use cases của List:**

*   **Notification feed**: LPUSH mới vào, LRANGE để paginate, LTRIM để giữ 100 cái gần nhất
    
*   **Job queue**: RPUSH thêm task, BRPOP lấy task xử lý (FIFO)
    
*   **Recent history**: lịch sử tìm kiếm, recently viewed
    

## 8\. Set — Tập Hợp Không Trùng Lặp

Set lưu tập hợp các string duy nhất, không có thứ tự.

```bash
# SADD — thêm members
SADD course:1:tags "java" "spring" "backend"
SADD course:1:tags "java"  # → 0 (đã tồn tại, không thêm)

# SMEMBERS — xem tất cả members
SMEMBERS course:1:tags
# → {"java", "spring", "backend"}

# SISMEMBER — kiểm tra member tồn tại
SISMEMBER course:1:tags "java"
# → 1 (có)
SISMEMBER course:1:tags "php"
# → 0 (không)

# SCARD — số lượng members
SCARD course:1:tags
# → 3

# SREM — xóa member
SREM course:1:tags "backend"

# SUNION — hợp hai sets
SADD course:2:tags "java" "microservices"
SUNION course:1:tags course:2:tags
# → {"java", "spring", "backend", "microservices"}

# SINTER — giao hai sets
SINTER course:1:tags course:2:tags
# → {"java"}

# SDIFF — hiệu hai sets
SDIFF course:1:tags course:2:tags
# → {"spring", "backend"}

# SMISMEMBER — kiểm tra nhiều members cùng lúc (Redis 6.2+)
SMISMEMBER course:1:tags "java" "python" "spring"
# → [1, 0, 1]
```

**Use cases của Set:**

*   **Tags**: mỗi course có set tags, dùng SINTER để tìm courses cùng tag
    
*   **Tracking**: ai đã đọc bài viết này — add user\_id vào set
    
*   **Unique visitors**: SADD daily:visitors:2025-03-15 user\_id
    
*   **Block list**: tập hợp IPs bị ban
    

## 9\. Sorted Set — Tập Hợp Có Điểm Số

Giống Set nhưng mỗi member có thêm **score** — tự động sắp xếp theo score. Đây là kiểu dữ liệu mạnh nhất của Redis.

```bash
# ZADD — thêm member với score
ZADD leaderboard:points:2025-03 100 "user:1"
ZADD leaderboard:points:2025-03 250 "user:2"
ZADD leaderboard:points:2025-03 75  "user:3"
ZADD leaderboard:points:2025-03 300 "user:4"

# ZADD với options
ZADD leaderboard:points:2025-03 NX 500 "user:5"  # chỉ add nếu chưa tồn tại
ZADD leaderboard:points:2025-03 GT 400 "user:1"  # chỉ update nếu score mới > cũ

# ZSCORE — xem score của member
ZSCORE leaderboard:points:2025-03 "user:2"
# → "250"

# ZRANK — xếp hạng (0-based, từ thấp đến cao)
ZRANK leaderboard:points:2025-03 "user:2"
# → 2 (hạng thứ 3 từ dưới lên)

# ZREVRANK — xếp hạng từ cao đến thấp
ZREVRANK leaderboard:points:2025-03 "user:2"
# → 1 (hạng nhì)

# ZRANGE — lấy members theo rank (thấp đến cao)
ZRANGE leaderboard:points:2025-03 0 -1
# → ["user:3", "user:1", "user:2", "user:4", "user:5"]

# ZREVRANGE — từ cao đến thấp (top N)
ZREVRANGE leaderboard:points:2025-03 0 2 WITHSCORES
# → ["user:5", "500", "user:4", "300", "user:2", "250"]
# Top 3 users

# ZINCRBY — tăng score
ZINCRBY leaderboard:points:2025-03 50 "user:1"
# user:1 từ 100 → 150

# ZCARD — số lượng members
ZCARD leaderboard:points:2025-03
# → 5

# ZRANGEBYSCORE — lấy members trong range score
ZRANGEBYSCORE leaderboard:points:2025-03 100 300
# → members có score từ 100 đến 300

# ZCOUNT — đếm members trong range score
ZCOUNT leaderboard:points:2025-03 200 400
```

**Use cases của Sorted Set:**

*   **Leaderboard**: điểm thưởng, ranking users
    
*   **Rate limiting**: timestamp là score, member là request\_id
    
*   **Delayed queue**: execution\_time là score, task\_id là member
    
*   **Top N**: top courses theo views, top users theo spending
    

## 10\. Naming Convention Cho Keys

Đặt tên key nhất quán giúp quản lý dễ hơn:

```bash
# Pattern: type:id:field
user:1:profile
course:1:stats
order:123:status

# Pattern: namespace:entity:id
foxdev:user:1
foxdev:course:1

# Pattern: feature:params
cache:courses:published
cache:courses:category:java
rate_limit:checkout:ip:1.2.3.4
session:user:1:device:mobile

# Nên dùng: dấu ":" để phân cấp
# Không dùng: spaces, $, *, ?, [, ]

# Một số ví dụ thực tế nguyentienkhoi.hashnode.dev
SET session:user_1     "..."        # User session
SET cache:home_courses "..."        # Cache danh sách courses trang chủ
SET lock:payment:456   "1"          # Distributed lock
HSET user:1:profile email "..."     # User profile hash
ZADD rank:courses:views 1500 "1"   # Course ranking by views
SADD online:users      "user_1"    # Set users đang online
LPUSH feed:user_1      "post_id"   # Notification feed
```

## 11\. Kiểm Tra Kiểu Dữ Liệu và Info

```bash
# TYPE — xem kiểu dữ liệu của key
TYPE user:1           # → hash
TYPE course:1:tags    # → set
TYPE notifications:1  # → list
TYPE leaderboard:2025 # → zset
TYPE course:1:title   # → string

# OBJECT ENCODING — xem encoding thực tế (Redis tự tối ưu)
OBJECT ENCODING user:1
# → ziplist (nhỏ) hoặc hashtable (lớn)

# DEBUG OBJECT — xem memory usage
DEBUG OBJECT user:1

# MEMORY USAGE — xem memory của 1 key
MEMORY USAGE user:1
# → 128 (bytes)

# SCAN — iterate keys an toàn (thay vì KEYS * ở production)
SCAN 0 MATCH "cache:*" COUNT 100
# → cursor + list of keys
# Dùng cursor tiếp theo cho lần sau đến khi cursor = 0
```

## 12\. Thực Hành Tổng Hợp

**Bài 1:** Tạo cache cho danh sách khóa học.

```bash
# Lưu cache với TTL 10 phút
SET cache:courses:published '[{"id":1,"title":"Spring Boot","price":799000},{"id":2,"title":"SQL","price":599000}]' EX 600

# Đọc cache
GET cache:courses:published

# Xóa cache (khi có course mới)
DEL cache:courses:published
```

**Bài 2:** Tạo leaderboard điểm thưởng cho users.

```bash
# Thêm users vào leaderboard tháng 3/2025
ZADD rank:points:2025-03 150 "user:1"
ZADD rank:points:2025-03 320 "user:2"
ZADD rank:points:2025-03 80  "user:3"
ZADD rank:points:2025-03 490 "user:4"

# Xem top 3
ZREVRANGE rank:points:2025-03 0 2 WITHSCORES

# User:2 hoàn thành thêm 1 khóa học → cộng 50 điểm
ZINCRBY rank:points:2025-03 50 "user:2"

# Xem rank của user:2
ZREVRANK rank:points:2025-03 "user:2"
```

**Bài 3:** Tạo notification feed.

```bash
# Thêm notifications (mới nhất ở đầu)
LPUSH notifications:user_1 "Đơn hàng #001 đã thanh toán thành công"
LPUSH notifications:user_1 "Khóa học Spring Boot đã được enroll"
LPUSH notifications:user_1 "Bạn đã đạt chứng chỉ SQL Developer"

# Lấy 10 notifications gần nhất
LRANGE notifications:user_1 0 9

# Giữ tối đa 50 notifications
LTRIM notifications:user_1 0 49

# Đếm tổng notifications
LLEN notifications:user_1
```

## Tổng Kết


| Kiểu | Commands chính | Use case |
|---|---|---|
| String | SET, GET, INCR, EXPIRE | Cache, counter, session, lock |
| Hash | HSET, HGET, HGETALL, HINCRBY | Object/document, user profile |
| List | LPUSH, RPUSH, LRANGE, LPOP | Queue, feed, history |
| Set | SADD, SMEMBERS, SINTER, SUNION | Tags, unique tracking, block list |
| Sorted Set | ZADD, ZREVRANGE, ZINCRBY, ZRANK | Leaderboard, ranking, rate limit |



**Redis so với PostgreSQL và MongoDB:**

```java
PostgreSQL: Dữ liệu chính, ACID, relationships → source of truth
MongoDB:    Flexible documents, catalog, content → secondary store
Redis:      Speed layer, cache, ephemeral data  → performance layer
```

Bài tiếp theo chúng ta sẽ học **Redis trong thực chiến**: Cache patterns, Session management, Rate limiting và Distributed Lock — những tính năng được dùng trong mọi backend production.

