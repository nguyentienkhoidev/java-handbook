# Bộ Câu Hỏi Phỏng Vấn MongoDB — Junior đến Senior

![Bộ Câu Hỏi Phỏng Vấn MongoDB — Junior đến Senior.jpeg](../images/9e40455a-82a3-46dd-b7fc-3caa26350b04.jpeg)

## 🟢 JUNIOR (0–2 năm)

Mục tiêu: Kiểm tra hiểu biết cơ bản về Document DB, CRUD, schema design và query cơ bản.

* * *

### Khái Niệm Cơ Bản

**Q1. MongoDB là gì? Khác với SQL như thế nào? Khi nào chọn MongoDB thay vì PostgreSQL?**

Đáp án mong đợi:

*   MongoDB là Document Database — lưu dữ liệu dạng **JSON document** (BSON internally)
    
*   Khác SQL:
    
    *   Không có schema cố định — mỗi document có thể có cấu trúc khác nhau
        
    *   Không có JOIN — embed hoặc reference thủ công
        
    *   Scale horizontal dễ hơn (sharding built-in)
        
*   Chọn MongoDB khi:
    
    *   Dữ liệu không có cấu trúc cố định, hay thay đổi schema
        
    *   Cần flexible document (product catalog, user profiles)
        
    *   Cần scale ngang dễ dàng
        
    *   Write-heavy workload
        

🚩 Red flag: Nói "MongoDB nhanh hơn PostgreSQL" mà không giải thích context — sai hoàn toàn

**Q2. Giải thích cấu trúc: Database → Collection → Document. So sánh với SQL.**

Đáp án mong đợi:

```java
MongoDB                    SQL
──────────────────────     ──────────────────────
Database               ←→  Database
Collection             ←→  Table
Document               ←→  Row
Field                  ←→  Column
_id                    ←→  Primary Key
Embedded Document      ←→  JOIN (nhưng khác bản chất)
```

Điểm quan trọng:

*   Document là **JSON object** tự mô tả, không cần define schema trước
    
*   `_id` mặc định là ObjectId — tự generate, unique, có timestamp bên trong
    
*   Collection không enforce schema — 2 documents trong cùng collection có thể có fields khác nhau
    

**Q3. BSON là gì? Tại sao MongoDB dùng BSON thay vì JSON thuần?**

Đáp án mong đợi:

*   BSON = Binary JSON — JSON được encode thành binary format
    
*   Lợi ích:
    
    *   Thêm kiểu dữ liệu: Date, ObjectId, Binary, Decimal128 (JSON chỉ có string, number, bool, null, array, object)
        
    *   Traverse nhanh hơn: lưu thêm length prefix → skip field không cần đọc
        
    *   Compact hơn khi lưu trữ
        

**Q4. ObjectId là gì? Cấu trúc của nó như thế nào?**

Đáp án mong đợi:

*   ObjectId: 12 bytes, globally unique, auto-generated
    
*   Cấu trúc:
    
    ```java
    [4 bytes timestamp][5 bytes random][3 bytes incrementing counter]
    ```
    
*   Tại sao tốt:
    
    *   Sort theo `_id` = sort theo thời gian tạo (gần đúng)
        
    *   Không cần centralized ID generator — client tự generate
        
    *   Embed timestamp → không cần thêm `created_at` nếu không cần precision cao
        

✅ Điểm cộng: Biết extract timestamp từ ObjectId: `ObjectId("...").getTimestamp()`

**Q5. Embedding vs Referencing trong MongoDB — khi nào dùng cái nào?**

Đáp án mong đợi:

**Embedding (lồng document):**

```json
{
  "_id": "course_1",
  "title": "Spring Boot",
  "instructor": {
    "name": "Nam Nguyen",
    "email": "nam@nguyentienkhoi.hashnode.dev"
  }
}
```

Dùng khi: dữ liệu luôn được đọc cùng nhau, quan hệ 1-1 hoặc 1-few, dữ liệu con không thay đổi nhiều

**Referencing (tham chiếu):**

```json
{
  "_id": "course_1",
  "title": "Spring Boot",
  "instructor_id": ObjectId("...")
}
```

Dùng khi: dữ liệu dùng độc lập, quan hệ many-to-many, document quá lớn (>16MB limit)

🚩 Red flag: Mọi thứ đều embed hoặc mọi thứ đều reference — không hiểu trade-off

**Q6. Viết CRUD operations cơ bản trong MongoDB.**

Đáp án mong đợi:

```javascript
// CREATE
db.courses.insertOne({
  title: "Spring Boot từ Zero đến Hero",
  price: 799000,
  tags: ["java", "spring", "backend"],
  created_at: new Date()
})

// READ
db.courses.find({ price: { $lte: 800000 }, tags: "java" })
db.courses.findOne({ _id: ObjectId("...") })

// UPDATE
db.courses.updateOne(
  { _id: ObjectId("...") },
  {
    $set: { price: 699000 },
    $push: { tags: "microservices" }
  }
)

// DELETE
db.courses.deleteOne({ _id: ObjectId("...") })
db.courses.deleteMany({ price: 0, enrolled_count: 0 })
```

Điểm quan trọng: biết `$set`, `$push`, `$pull`, `$inc`

**Q7. Giải thích các query operators phổ biến: $eq, $gt, $in, $and, $or, $exists.**

Đáp án mong đợi:

```javascript
// Comparison
{ price: { $gt: 500000, $lte: 1000000 } }
{ status: { $in: ["PAID", "PENDING"] } }
{ status: { $ne: "CANCELLED" } }

// Logical
{ $and: [{ price: { $gt: 0 } }, { rating: { $gte: 4.5 } }] }
{ $or: [{ course_type: "FREE" }, { price: { $lt: 300000 } }] }

// Element
{ phone: { $exists: true } }
{ phone: { $exists: true, $ne: null } }

// Array
{ tags: { $all: ["java", "spring"] } }
{ tags: { $size: 3 } }
```

**Q8. Index trong MongoDB hoạt động thế nào? Tại sao cần index?**

Đáp án mong đợi:

*   Không có index → Collection Scan: đọc toàn bộ documents → O(n), chậm
    
*   Có index → B-tree structure: tìm nhanh theo path → O(log n)
    
*   Tạo index:
    
    ```javascript
    db.courses.createIndex({ price: 1 })           // ascendingdb.courses.createIndex({ title: 1, price: -1 }) // compounddb.courses.createIndex({ title: "text" })        // text searchdb.courses.createIndex({ email: 1 }, { unique: true }) // unique
    ```
    
*   Explain để verify:
    
    ```javascript
    db.courses.find({ price: { $lt: 500000 } }).explain("executionStats")// COLLSCAN = chưa có index, IXSCAN = dùng được index
    ```
    

### Coding Question Junior

**Q9. Viết Aggregation Pipeline tính doanh thu theo tháng.**

Đáp án mong đợi:

```javascript
db.orders.aggregate([
  // Stage 1: Filter
  { $match: { status: "PAID" } },

  // Stage 2: Project tháng từ created_at
  { $addFields: {
    month: { $dateToString: { format: "%Y-%m", date: "$created_at" } }
  }},

  // Stage 3: Group và tính tổng
  { $group: {
    _id: "$month",
    total_revenue: { $sum: "$final_amount" },
    total_orders:  { $sum: 1 },
    avg_order:     { $avg: "$final_amount" }
  }},

  // Stage 4: Sort
  { $sort: { _id: -1 } },

  // Stage 5: Format output
  { $project: {
    month:         "$_id",
    total_revenue: 1,
    total_orders:  1,
    avg_order:     { $round: ["$avg_order", 0] },
    _id:           0
  }}
])
```

* * *

## 🟡 INTERMEDIATE (2–4 năm)

Mục tiêu: Schema design patterns, Aggregation nâng cao, Index strategy, Transactions, Performance.

* * *

### Schema Design

**Q10. Giải thích Data Model patterns trong MongoDB: Subset Pattern, Bucket Pattern, Outlier Pattern.**

Đáp án mong đợi:

**Subset Pattern:** Chỉ embed subset hay dùng nhất của dữ liệu liên quan

```json
// Chỉ embed 10 reviews mới nhất, không embed tất cả
{
  "course_id": 1,
  "title": "Spring Boot",
  "recent_reviews": [
    { "user": "Nam", "rating": 5, "comment": "Excellent!" }
    // ... 9 more
  ],
  "total_reviews": 1523  // đếm tổng, không load tất cả
}
```

Dùng khi: 80% query chỉ cần N items đầu tiên

**Bucket Pattern:** Gom nhiều documents nhỏ thành một bucket lớn

```json
// Thay vì 1M documents tracking từng view event
// → gom theo ngày
{
  "course_id": 1,
  "date": "2025-03-15",
  "views": [
    { "user_id": 1, "time": "09:00" },
    { "user_id": 2, "time": "09:05" }
  ],
  "total_views": 247
}
```

Dùng khi: Time-series data, IoT, event tracking

**Outlier Pattern:** Handle documents bất thường (có hàng triệu liên kết)

```json
// Course bình thường: embed enrollments
{ "course_id": 1, "enrollments": [...50 items...] }

// Course viral (100k enrollments): dùng flag + separate collection
{ "course_id": 2, "has_extra_enrollments": true }
// Enrollments lưu riêng trong collection khác
```

* * *

**Q11. Aggregation Pipeline hoạt động thế nào? Giải thích $lookup, $unwind, $facet.**

Đáp án mong đợi:

**$lookup** — LEFT JOIN với collection khác:

```javascript
db.orders.aggregate([
  {
    $lookup: {
      from:         "users",
      localField:   "user_id",
      foreignField: "_id",
      as:           "user_info"
    }
  },
  { $unwind: "$user_info" }  // convert array[1] → object
])
```

**$unwind** — Expand array field thành nhiều documents:

```javascript
// { tags: ["java", "spring"] } → 2 documents
{ $unwind: "$tags" }
// { _id: 1, tags: "java" }, { _id: 1, tags: "spring" }
```

**$facet** — Tính nhiều aggregations song song trong 1 pipeline:

```javascript
{ $facet: {
  "by_category": [
    { $group: { _id: "$category", count: { $sum: 1 } } }
  ],
  "by_price_range": [
    { $bucket: {
      groupBy: "$price",
      boundaries: [0, 300000, 600000, 1000000],
      default: "Other"
    }}
  ],
  "total_count": [
    { $count: "count" }
  ]
}}
```

**Q12. Giải thích Index Strategy trong MongoDB: Compound Index, Multikey Index, Partial Index, TTL Index.**

Đáp án mong đợi:

**Compound Index — thứ tự fields quan trọng:**

```javascript
// Query: { status: "PAID", created_at: { $gte: date } } ORDER BY created_at DESC
// Index tốt: ESR rule (Equality → Sort → Range)
db.orders.createIndex({ status: 1, created_at: -1 })

// ESR Rule:
// E: Equality fields trước (status = "PAID")
// S: Sort fields tiếp theo (created_at DESC)
// R: Range fields sau cùng
```

**Multikey Index — index trên array field:**

```javascript
db.courses.createIndex({ tags: 1 })
// Query: { tags: "java" } → dùng được index
// Lưu ý: mỗi element trong array tạo một index entry → index lớn hơn
```

**Partial Index — chỉ index subset documents:**

```javascript
db.orders.createIndex(
  { user_id: 1, created_at: -1 },
  { partialFilterExpression: { status: "PENDING" } }
)
// Chỉ index PENDING orders → index nhỏ hơn, nhanh hơn
```

**TTL Index — tự động xóa documents sau thời gian:**

```javascript
db.sessions.createIndex(
  { expires_at: 1 },
  { expireAfterSeconds: 0 }  // xóa khi expires_at < now
)
```

**Q13. MongoDB Transactions — khi nào cần? Limitations là gì?**

Đáp án mong đợi:

```javascript
// Multi-document transaction (MongoDB 4.0+)
const session = await client.startSession()
session.startTransaction()

try {
  await db.wallets.updateOne(
    { user_id: 1 },
    { $inc: { balance: -799000 } },
    { session }
  )
  await db.orders.insertOne(
    { user_id: 1, amount: 799000, status: "PAID" },
    { session }
  )
  await session.commitTransaction()
} catch (error) {
  await session.abortTransaction()
  throw error
} finally {
  session.endSession()
}
```

**Limitations:**

*   Chỉ hoạt động với **Replica Set hoặc Sharded Cluster** (không phải standalone)
    
*   Performance overhead đáng kể so với single-document operation
    
*   Max transaction runtime: 60 giây (mặc định)
    
*   MongoDB document là **atomic ở cấp document** → thường không cần transaction nếu thiết kế đúng
    

🚩 Red flag: Nói MongoDB không có transaction — sai từ MongoDB 4.0

**Q14. Write Concern và Read Concern là gì? Ảnh hưởng thế nào đến consistency?**

Đáp án mong đợi:

**Write Concern:** Mức độ acknowledgment khi write

```javascript
// w:1 (default) — primary acknowledge, có thể mất nếu primary crash
// w:"majority" — majority of nodes acknowledge, an toàn hơn
// w:0 — fire and forget, nhanh nhất nhưng không biết có thành công không
db.orders.insertOne(doc, { writeConcern: { w: "majority", wtimeout: 5000 } })
```

**Read Concern:** Mức độ consistency khi read

```javascript
// "local" (default) — đọc từ node hiện tại, có thể stale
// "majority" — chỉ đọc dữ liệu đã được majority acknowledge
// "linearizable" — strongest, đảm bảo đọc được data mới nhất nhưng chậm nhất
db.orders.find({}).readConcern("majority")
```

**Practical:**

*   Financial data: `w: "majority"`, `readConcern: "majority"`
    
*   Analytics, read-heavy: `w: 1`, `readConcern: "local"`
    

**Q15. Giải thích $explain() output. Làm thế nào identify và fix slow queries?**

Đáp án mong đợi:

```javascript
db.courses.find({ price: { $lt: 500000 } }).explain("executionStats")
```

Điểm cần chú ý trong output:

*   `winningPlan.stage`: COLLSCAN (bad) vs IXSCAN (good)
    
*   `totalDocsExamined` vs `nReturned`: ratio cao = inefficient
    
*   `executionTimeMillis`: thời gian thực tế
    
*   `indexBounds`: index có được dùng đúng không
    

**Fix slow queries:**

1.  Thêm index phù hợp theo ESR rule
    
2.  Projection để giảm data transferred: `.find({}, { title: 1, price: 1 })`
    
3.  Dùng covered query — tất cả fields cần có trong index
    
4.  Rewrite query để tránh collection scan
    

### Coding Question Intermediate

**Q16. Viết Aggregation Pipeline phân tích user behavior: top 5 users chi tiêu nhiều nhất, kèm số khóa học đã mua và rating trung bình của các khóa đó.**

Đáp án mong đợi:

```javascript
db.orders.aggregate([
  // Filter đơn hàng PAID
  { $match: { status: "PAID" } },

  // Lookup user info
  { $lookup: {
    from:         "users",
    localField:   "user_id",
    foreignField: "_id",
    as:           "user"
  }},
  { $unwind: "$user" },

  // Lookup order items
  { $lookup: {
    from:         "order_items",
    localField:   "_id",
    foreignField: "order_id",
    as:           "items"
  }},

  // Unwind items để lấy course_ids
  { $unwind: "$items" },

  // Lookup course ratings
  { $lookup: {
    from:      "courses",
    localField: "items.course_id",
    foreignField: "_id",
    as:         "course"
  }},
  { $unwind: { path: "$course", preserveNullAndEmptyArrays: true } },

  // Group by user
  { $group: {
    _id:          "$user._id",
    name:         { $first: "$user.name" },
    email:        { $first: "$user.email" },
    total_spent:  { $sum: "$final_amount" },
    courses_bought: { $addToSet: "$items.course_id" },
    avg_rating:   { $avg: "$course.rating" }
  }},

  // Add courses_count
  { $addFields: {
    courses_count: { $size: "$courses_bought" },
    avg_rating:    { $round: ["$avg_rating", 2] }
  }},

  // Sort và limit
  { $sort: { total_spent: -1 } },
  { $limit: 5 },

  // Clean output
  { $project: {
    name:          1,
    email:         1,
    total_spent:   1,
    courses_count: 1,
    avg_rating:    1,
    _id:           0
  }}
])
```

* * *

## 🟠 ADVANCED (4–7 năm)

Mục tiêu: Replication, Sharding, Performance tuning, Schema migration, Production operations.

* * *

### Replication & High Availability

**Q17. Giải thích Replica Set trong MongoDB. Primary election hoạt động thế nào?**

Đáp án mong đợi:

**Replica Set:**

*   Minimum 3 nodes: 1 Primary + 2 Secondary (hoặc 2 Secondary + 1 Arbiter)
    
*   Primary nhận toàn bộ writes, replicates sang Secondary qua **Oplog**
    
*   Secondary có thể serve reads (nếu cấu hình)
    

**Election:**

*   Khi Primary không phản hồi trong `electionTimeoutMillis` (10s mặc định)
    
*   Secondary bắt đầu election — cần **majority votes**
    
*   Vote dựa trên: priority, replication lag, oplog position
    
*   Nếu không đủ majority (ví dụ chỉ còn 1/3 nodes) → **no primary, no writes**
    

**Oplog:**

*   Capped collection lưu tất cả write operations
    
*   Secondary apply oplog để sync
    
*   Oplog size ảnh hưởng đến recovery time sau failover
    

```javascript
// Kiểm tra replication lag
rs.printSecondaryReplicationInfo()
// secondaryDelay: seconds behind primary
```

**Q18. Sharding trong MongoDB là gì? Giải thích Shard Key selection và các vấn đề cần tránh.**

Đáp án mong đợi:

*   Sharding: Horizontal partitioning — chia data ra nhiều nodes
    
*   Shard Key patterns:
    

```javascript
// Range sharding — data phân phối theo range
sh.shardCollection("foxdev.orders", { created_at: 1 })
// Vấn đề: hotspot — tất cả writes vào shard mới nhất (timestamp tăng dần)

// Hashed sharding — phân phối đều
sh.shardCollection("foxdev.orders", { _id: "hashed" })
// Không thể range query hiệu quả

// Zone sharding — kiểm soát data locality
sh.addShardToZone("shard1", "VN")
sh.updateZoneKeyRange("foxdev.users", { country: "VN" }, { country: "VN_end" }, "VN")
```

**Vấn đề cần tránh:**

*   **Monotonically increasing key** (timestamp, ObjectId): tạo write hotspot
    
*   **Low cardinality key** (boolean, gender): không phân phối đều
    
*   **Uneven distribution**: một shard quá lớn → jumbo chunks
    

**Best practices:**

*   Compound shard key: `{ user_id: 1, created_at: 1 }` — phân phối tốt hơn
    
*   `user_id` vào hash để phân phối đều writes
    
*   Chọn shard key phù hợp với query pattern phổ biến nhất
    

**Q19. Giải thích Change Streams trong MongoDB. Use case thực tế.**

Đáp án mong đợi:

```javascript
// Watch changes trên collection
const changeStream = db.orders.watch([
  { $match: { "operationType": { $in: ["insert", "update"] } } },
  { $match: { "fullDocument.status": "PAID" } }
])

changeStream.on("change", async (change) => {
  const order = change.fullDocument
  // Trigger: send email, update analytics, sync Vector DB
  await emailService.sendConfirmation(order)
  await vectorDB.syncCourse(order.course_id)
})
```

**Use cases:**

*   Real-time notifications khi order được thanh toán
    
*   Sync MongoDB → Elasticsearch/Vector DB
    
*   Audit trail — log mọi thay đổi
    
*   Cache invalidation — xóa Redis cache khi document update
    
*   Event sourcing — trigger downstream services
    

**Requirements:**

*   Chỉ hoạt động với Replica Set hoặc Sharded Cluster
    
*   Resume token: có thể resume sau khi connection drop
    

### System Design

**Q20. \[System Design\] Thiết kế Product Catalog cho e-commerce với 10 triệu sản phẩm, 100k concurrent users, hỗ trợ flexible attributes (mỗi category có attributes khác nhau). Tại sao MongoDB phù hợp hơn PostgreSQL ở đây?**

Đáp án mong đợi:

**Vấn đề với PostgreSQL:**

```sql
-- Cách 1: EAV (Entity-Attribute-Value) — rất chậm
product_attributes(product_id, attr_name, attr_value TEXT)
-- Query "màu đỏ AND size L" = 2 JOINs + filter → chậm

-- Cách 2: Cột JSON — OK nhưng thiếu type safety, index kém
products(id, name, attributes JSONB)
```

**MongoDB solution:**

```javascript
// Flexible schema theo category
// Electronics
{ _id: 1, category: "laptop", name: "ThinkPad", price: 25000000,
  specs: { ram: "16GB", storage: "512GB SSD", cpu: "i7-12th" } }

// Clothing
{ _id: 2, category: "tshirt", name: "Polo", price: 299000,
  specs: { size: ["S", "M", "L"], color: ["red", "blue"], material: "cotton" } }

// Index linh hoạt theo category
db.products.createIndex({ "specs.ram": 1 })     // chỉ cho electronics
db.products.createIndex({ "specs.size": 1 })    // chỉ cho clothing
db.products.createIndex({ category: 1, price: 1 }) // cross-category
```

**Scaling:**

*   Shard by `category` + `_id` → data locality per category
    
*   Read replicas cho catalog browse
    
*   Redis cache cho popular products
    
*   Atlas Search (Lucene) cho full-text search
    

**MongoDB tốt hơn PostgreSQL vì:**

*   Không cần ALTER TABLE khi thêm attribute mới cho category
    
*   Native array support (size: \["S","M","L"\])
    
*   Horizontal sharding dễ hơn
    
*   Document model khớp tự nhiên với product concept
    

**Q21. \[System Design\] Thiết kế Notification System dùng MongoDB: gửi 10 triệu notifications/ngày, user query "notifications chưa đọc của tôi". Làm thế nào thiết kế schema efficient?**

Đáp án mong đợi:

**Approach 1 — Naive (BAD):**

```javascript
// 1 document per notification per user
{
  user_id: ObjectId("..."),
  message: "Your order is confirmed",
  type: "ORDER_CONFIRMED",
  is_read: false,
  created_at: new Date()
}
// Vấn đề: 10M docs/ngày = 300M docs/tháng → storage huge, query chậm
```

**Approach 2 — Bucket Pattern (GOOD):**

```javascript
// Bucket: 1 document per user per day
{
  user_id: ObjectId("..."),
  date: "2025-03-15",
  notifications: [
    { id: 1, type: "ORDER_CONFIRMED", message: "...", is_read: false, time: "09:00" },
    { id: 2, type: "COURSE_ENROLLED", message: "...", is_read: true, time: "10:30" }
  ],
  unread_count: 1,  // cache để query nhanh
  total_count:  2
}

// Index
db.notifications.createIndex({ user_id: 1, date: -1 })
db.notifications.createIndex({ user_id: 1, unread_count: 1 })
```

**Query unread:**

```javascript
// Nhanh — dùng index
db.notifications.find({
  user_id: userId,
  unread_count: { $gt: 0 }
}).sort({ date: -1 }).limit(7) // 7 ngày gần nhất
```

**Mark as read:**

```javascript
db.notifications.updateOne(
  { user_id: userId, date: "2025-03-15" },
  {
    $set: { "notifications.$[elem].is_read": true },
    $inc: { unread_count: -1 }
  },
  { arrayFilters: [{ "elem.id": notificationId }] }
)
```

**TTL cho dữ liệu cũ:**

```javascript
db.notifications.createIndex(
  { date: 1 },
  { expireAfterSeconds: 90 * 24 * 3600 } // xóa sau 90 ngày
)
```

**Q22. MongoDB Atlas Search vs Elasticsearch — khi nào chọn cái nào? Trade-offs.**

Đáp án mong đợi:

**Atlas Search (Lucene-based trong MongoDB):**

*   Ưu điểm:
    
    *   Cùng cluster MongoDB, không cần sync data
        
    *   Không có consistency lag
        
    *   Simpler ops — một ít service hơn
        
    *   Native MongoDB operators + search trong cùng aggregation
        
*   Nhược điểm:
    
    *   Ít tính năng hơn Elasticsearch
        
    *   Không có standalone option (chỉ MongoDB Atlas)
        
    *   Kém hơn ES cho log analysis, complex scoring
        

```javascript
// Atlas Search trong aggregation pipeline
db.courses.aggregate([
  { $search: {
    index: "default",
    text: {
      query: "spring boot backend",
      path:  ["title", "description"],
      fuzzy: { maxEdits: 1 }
    }
  }},
  { $limit: 10 },
  { $project: { title: 1, score: { $meta: "searchScore" } } }
])
```

**Elasticsearch:**

*   Ưu điểm: feature-rich, Kibana visualization, log analysis, complex relevance tuning
    
*   Nhược điểm: extra infra, sync lag từ MongoDB, operational complexity
    

**Khi nào chọn:**

*   Startup, search là secondary feature: Atlas Search
    
*   Search là core product, cần tuning sâu: Elasticsearch
    
*   Log analytics, APM: Elasticsearch (ELK stack)
    

**Q23. Giải thích các anti-patterns phổ biến trong MongoDB schema design. Cho ví dụ và cách fix.**

Đáp án mong đợi:

**Anti-pattern 1: Massive Arrays — unbounded array growth**

```javascript
// ❌ BAD: array enrollment không giới hạn → document vượt 16MB
{
  course_id: 1,
  enrollments: [userId1, userId2, ... userId100000]
}

// ✅ GOOD: separate collection
// enrollments collection: { user_id, course_id, enrolled_at }
```

**Anti-pattern 2: Polymorphic Collections Without Discrimination**

```javascript
// ❌ BAD: không biết document type → phải check fields
{ _id: 1, name: "Nam", email: "..." }       // user
{ _id: 2, title: "Spring Boot", ... }       // course

// ✅ GOOD: thêm type discriminator
{ _id: 1, type: "user",   name: "Nam", ... }
{ _id: 2, type: "course", title: "...", ... }
```

**Anti-pattern 3: Using MongoDB như RDBMS — quá nhiều $lookup**

```javascript
// ❌ BAD: 3 $lookup = slow
orders.aggregate([
  { $lookup: { from: "users",... } },
  { $lookup: { from: "courses",... } },
  { $lookup: { from: "payments",... } }
])

// ✅ GOOD: embed data cần thiết vào order
{
  order_id: 1,
  user_snapshot: { name: "Nam", email: "..." },  // embed tại thời điểm order
  items: [{ course_title: "Spring Boot", price: 799000 }]
}
```

**Anti-pattern 4: Không dùng Projection**

```javascript
// ❌ BAD: lấy toàn bộ document kể cả content text (MB)
db.posts.find({ status: "PUBLISHED" })

// ✅ GOOD: chỉ lấy cần thiết
db.posts.find({ status: "PUBLISHED" }, { title: 1, slug: 1, created_at: 1 })
```

* * *

## 🔴 SENIOR / PRINCIPAL (7+ năm)

* * *

**Q24. Bạn cần migrate schema: thêm field** `is_premium` **vào tất cả documents trong collection** `users` **(50 triệu documents) không được downtime. Kế hoạch thế nào?**

Đáp án mong đợi:

*   Strategy: Lazy Migration + Dual-read
    
*   Phase 1: Code change (deploy ngay):
    

```javascript
// Application code: handle cả 2 cases
function getIsPremium(user) {
  // Field chưa có → default false
  return user.is_premium ?? false
}
```

**Phase 2: Background migration (chạy song song):**

```javascript
// Script chạy offline — batch update với rate limiting
const batchSize = 1000
let cursor = null

while (true) {
  const query = cursor
    ? { _id: { $gt: cursor }, is_premium: { $exists: false } }
    : { is_premium: { $exists: false } }

  const docs = await db.users.find(query)
    .sort({ _id: 1 }).limit(batchSize).toArray()

  if (docs.length === 0) break

  const ids = docs.map(d => d._id)
  await db.users.updateMany(
    { _id: { $in: ids } },
    { $set: { is_premium: false } }
  )

  cursor = ids[ids.length - 1]
  await sleep(100) // rate limit để không impact production
}
```

**Phase 3: Cleanup (sau khi migration xong):**

*   Remove `?? false` fallback trong code
    
*   Thêm default value cho new documents
    

**Monitoring:**

*   Track migration progress: count documents where `is_premium: { $exists: false }`
    
*   Monitor: CPU, IOPS, query latency trong quá trình migration
    

✅ Senior indicator: Đề cập đến rate limiting, monitoring, rollback plan

**Q25. \[Trade-off\] Junior developer đề xuất lưu tất cả data trong MongoDB vì "flexible schema". Senior reviewer lo ngại về data integrity. Bạn phân xử thế nào?**

Câu hỏi open-ended — đánh giá tư duy:

*   Điểm cần cover:
    
*   Junior đúng ở:
    

*   Flexible schema thực sự cần thiết cho catalog, user profiles, config
    
*   Development velocity cao hơn khi schema thay đổi nhiều
    

**Senior đúng ở:**

*   Financial data (orders, payments): ACID transactions, referential integrity quan trọng hơn
    
*   Report queries: JOIN nhiều bảng dễ hơn trong SQL
    
*   Aggregation phức tạp: PostgreSQL window functions mạnh hơn
    

**Kết luận mature:**

*   Polyglot persistence: dùng đúng tool cho đúng use case
    
*   MongoDB: catalog, sessions, user preferences, notifications
    
*   PostgreSQL: orders, payments, enrollments, financial reports
    
*   Không có "MongoDB vs PostgreSQL" — có "MongoDB AND PostgreSQL"
    

**Red flags:**

*   "MongoDB cho tất cả" — thiếu hiểu về ACID requirements
    
*   "PostgreSQL cho tất cả" — không tận dụng được flexible schema cho catalog
    

**Q26. Performance Investigation: Query sau đang chậm 5 giây với 20 triệu documents. Quy trình debug và fix như thế nào?**

```javascript
db.orders.find({
  status: "PAID",
  created_at: { $gte: ISODate("2024-01-01") }
}).sort({ created_at: -1 }).limit(20)
```

Đáp án mong đợi — quy trình đầy đủ:

**Bước 1: Explain**

```javascript
db.orders.find({...}).sort({...}).explain("executionStats")
// Tìm: COLLSCAN, totalDocsExamined >> nReturned, executionTimeMillis
```

**Bước 2: Phân tích**

*   Nếu COLLSCAN: chưa có index phù hợp
    
*   Nếu IXSCAN nhưng vẫn chậm: index không optimal, hoặc data skew
    

**Bước 3: Fix**

```javascript
// ESR Rule: Equality (status) → Sort (created_at) → Range (created_at)
db.orders.createIndex({ status: 1, created_at: -1 })

// Nếu status có ít values (low cardinality) → partial index
db.orders.createIndex(
  { created_at: -1 },
  { partialFilterExpression: { status: "PAID" } }
)
```

**Bước 4: Verify**

```javascript
// Sau khi thêm index
db.orders.find({...}).explain("executionStats")
// IXSCAN, totalDocsExamined ≈ nReturned = 20
```

**Bước 5: Monitor long-term**

```javascript
// Bật slow query log
db.setProfilingLevel(1, { slowms: 100 })
db.system.profile.find().sort({ ts: -1 }).limit(10)
```

## Bảng Điểm Đánh Giá


| Level | Câu hỏi | Pass khi |
|---|---|---|
| Junior | Q1–Q9 | Pass 7/9, bắt buộc pass Q5 (embed vs ref) và Q9 (aggregation) |
| Intermediate | Q10–Q16 | Pass 5/7, bắt buộc pass Q12 (index) và Q13 (transaction) |
| Advanced | Q17–Q23 | Pass 4/7, bắt buộc pass Q20 hoặc Q21 (system design) |
| Senior | Q24–Q26 | Pass 2/3, đặc biệt Q25 (trade-off thinking) |



## Câu Hỏi Bẫy Hay Dùng

**Bẫy 1:** "MongoDB không có schema nên không cần thiết kế?" → Sai. **Schema-on-read** không có nghĩa là không có schema. Cần thiết kế schema cẩn thận hơn vì MongoDB không enforce.

**Bẫy 2:** "Cứ embed tất cả vào một document cho nhanh?" → Sai. Document limit 16MB. Array không giới hạn → document phình to theo thời gian.

**Bẫy 3:** "MongoDB không hỗ trợ JOIN?" → Sai. `$lookup` là LEFT OUTER JOIN. Nhưng MongoDB discourage JOIN-heavy design — thiết kế đúng thì không cần nhiều.

**Bẫy 4:** "MongoDB không có transaction?" → Sai từ MongoDB 4.0. Có multi-document ACID transaction, nhưng có performance overhead.

**Bẫy 5:** "Index càng nhiều càng tốt?" → Sai. Mỗi index tốn RAM (working set), làm chậm writes. Index unused là lãng phí.

**Bẫy 6:** "Shard key có thể đổi sau khi sharding?" → Không thể (trước MongoDB 5.0). Từ 5.0 có thể reshard nhưng rất tốn kém. Phải chọn đúng từ đầu.

