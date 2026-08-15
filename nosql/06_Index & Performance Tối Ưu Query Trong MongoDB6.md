# Index & Performance: Tối Ưu Query Trong MongoDB

![Index & Performance - Tối Ưu Query Trong MongoDB.jpeg](../images/4af96fe4-55d7-41b2-98a3-b0bb158ba244.jpeg)

Bạn vừa học cách thiết kế schema đúng. Nhưng schema đúng chưa đủ — query vẫn có thể chậm nếu thiếu index. Bài này đi qua các loại index trong MongoDB, cách đọc execution plan để phát hiện bottleneck, và những anti-patterns performance phổ biến nhất. Sau bài này bạn có thể tự debug và tối ưu query chậm trong production.

## 1\. Tại Sao Cần Index?

```javascript
// Không có index — Collection Scan
db.courses.find({ category: "java" })
// → Đọc TẤT CẢ documents để tìm category = "java"
// → O(n): 1M courses = đọc 1M documents → chậm

// Có index trên category — Index Scan
db.courses.createIndex({ category: 1 })
db.courses.find({ category: "java" })
// → Đi thẳng vào index → tìm được ngay
// → O(log n): 1M courses = đọc ~20 documents → nhanh
```

**Index trong MongoDB** là cấu trúc dữ liệu B-tree lưu riêng — trade-off: tốn thêm storage và làm chậm writes để đổi lấy reads nhanh hơn.

## 2\. Các Loại Index

### 2.1 Single Field Index — Index Một Field

```javascript
// Index ascending (1) hoặc descending (-1)
// Với single field: không quan trọng chiều — MongoDB tự đảo
db.courses.createIndex({ category: 1 })
db.courses.createIndex({ price: -1 })
db.courses.createIndex({ created_at: -1 })

// Unique index — không cho phép duplicate
db.users.createIndex({ email: 1 }, { unique: true })
db.courses.createIndex({ slug: 1 }, { unique: true })

// Sparse index — chỉ index documents có field đó (bỏ qua null/missing)
db.users.createIndex({ phone: 1 }, { sparse: true })
// User không có phone → không được index → index nhỏ hơn

// Index trên nested field
db.courses.createIndex({ "instructor._id": 1 })
db.orders.createIndex({ "user_snapshot.email": 1 })
```

### 2.2 Compound Index — Index Nhiều Fields

Tương tự Composite Index trong SQL — thứ tự fields quan trọng.

```javascript
// Compound index
db.courses.createIndex({ category: 1, price: -1 })

// Queries được hỗ trợ bởi index trên:
db.courses.find({ category: "java" })                         // ✅ dùng được (prefix)
db.courses.find({ category: "java", price: { $lt: 800000 } }) // ✅ dùng được
db.courses.find({ price: { $lt: 800000 } })                   // ❌ không dùng được (không có prefix)

// ESR Rule — Equality, Sort, Range
// Field Equality trước → Sort tiếp → Range cuối
db.orders.createIndex({ user_id: 1, order_status: 1, created_at: -1 })
// Tối ưu cho query:
db.orders.find({ user_id: id, order_status: "PAID" }).sort({ created_at: -1 })
```

**Ví dụ thực tế** [**nguyentienkhoi.hashnode.dev**](http://nguyentienkhoi.hashnode.dev)**:**

```javascript
// Query: Lấy khóa học PUBLISHED, sort theo enrolled_count
db.courses.createIndex({ course_status: 1, enrolled_count: -1 })

// Query: Tìm orders của user, filter theo status
db.orders.createIndex({ user_id: 1, order_status: 1, created_at: -1 })

// Query: Tìm user theo email và account_status
db.users.createIndex({ account_status: 1, email: 1 })
```

### 2.3 Multikey Index — Index Trên Array Field

MongoDB tự động tạo multikey index khi field là array.

```javascript
// Tạo index trên array field
db.courses.createIndex({ tags: 1 })

// Mỗi element trong array tạo một index entry
// course có tags: ["java", "spring", "backend"]
// → 3 index entries: "java", "spring", "backend"

// Query dùng được multikey index
db.courses.find({ tags: "java" })              // ✅
db.courses.find({ tags: { $in: ["java", "spring"] } }) // ✅
db.courses.find({ tags: { $all: ["java", "spring"] } }) // ✅

// Lưu ý: compound index chỉ được có 1 multikey field
db.courses.createIndex({ tags: 1, category: 1 }) // ✅ tags là array, category là string
// db.courses.createIndex({ tags: 1, sections: 1 }) // ❌ không được: 2 array fields
```

### 2.4 Text Index — Full-text Search

```javascript
// Tạo text index
db.courses.createIndex({ title: "text", description: "text" })

// Hoặc đánh index toàn bộ string fields
db.courses.createIndex({ "$**": "text" })  // wildcard text index

// Query
db.courses.find({ $text: { $search: "spring boot backend" } })

// Với score (relevance)
db.courses.find(
    { $text: { $search: "spring boot" } },
    { score: { $meta: "textScore" } }
).sort({ score: { $meta: "textScore" } })

// Exact phrase (dùng dấu nháy kép trong chuỗi)
db.courses.find({ $text: { $search: "\"spring boot\"" } })

// Exclude từ (dùng dấu -)
db.courses.find({ $text: { $search: "backend -nodejs" } })
```

**Lưu ý:** Mỗi collection chỉ có thể có **1 text index**. Với Atlas MongoDB, nên dùng **Atlas Search** (Lucene-based) thay vì text index.

### 2.5 TTL Index — Tự Động Xóa Documents

```javascript
// Tự động xóa documents sau 24 giờ kể từ created_at
db.sessions.createIndex(
    { created_at: 1 },
    { expireAfterSeconds: 86400 }
)

// Hoặc xóa tại thời điểm cụ thể trong document
db.password_resets.createIndex(
    { expires_at: 1 },
    { expireAfterSeconds: 0 }
)
// Document tự xóa khi expires_at < current time

// TTL background job chạy mỗi 60 giây — không phải realtime
```

### 2.6 Partial Index — Index Có Điều Kiện

Chỉ index documents thỏa điều kiện — index nhỏ hơn, hiệu quả hơn.

```javascript
// Chỉ index orders đang PENDING — không index PAID/CANCELLED
db.orders.createIndex(
    { created_at: 1 },
    {
        partialFilterExpression: {
            order_status: { $eq: "PENDING" }
        }
    }
)
// → Index nhỏ hơn nhiều, query trên PENDING orders nhanh hơn

// Chỉ index users đang ACTIVE
db.users.createIndex(
    { email: 1 },
    {
        partialFilterExpression: {
            account_status: "ACTIVE"
        },
        unique: true
    }
)
```

### 2.7 Wildcard Index — Index Flexible Fields

Dùng khi schema không cố định, không biết trước sẽ query field nào.

```javascript
// Index tất cả fields trong document
db.products.createIndex({ "$**": 1 })

// Index tất cả sub-fields của specs
db.products.createIndex({ "specs.$**": 1 })

// Query bất kỳ field nào trong specs đều dùng được index
db.products.find({ "specs.ram": "16GB" })       // ✅
db.products.find({ "specs.color": "black" })    // ✅
db.products.find({ "specs.storage": "512GB" })  // ✅
```

## 3\. Quản Lý Indexes

```javascript
// Xem tất cả indexes của collection
db.courses.getIndexes()

// Xem index usage stats — field nào có index nhưng không được dùng
db.courses.aggregate([{ $indexStats: {} }])

// Xóa index theo tên
db.courses.dropIndex("category_1_price_-1")

// Xóa tất cả indexes (trừ _id)
db.courses.dropIndexes()

// Tạo index ở background — không block operations (MongoDB < 4.2)
db.courses.createIndex({ category: 1 }, { background: true })
// MongoDB 4.2+: mặc định là background, không cần option này

// Xem kích thước index
db.courses.stats().indexSizes
```

## 4\. Explain Plan — Đọc Execution Plan

Tương tự `EXPLAIN ANALYZE` trong PostgreSQL.

```javascript
// Xem execution plan
db.courses.find({ category: "java", price: { $lt: 800000 } })
    .explain("executionStats")
```

**Những gì cần chú ý trong output:**

```javascript
{
    "queryPlanner": {
        "winningPlan": {
            "stage": "FETCH",          // ← FETCH: dùng index + đọc document
            "inputStage": {
                "stage": "IXSCAN",     // ← IXSCAN: dùng index ✅ (tốt)
                "keyPattern": { "category": 1 },
                "indexName": "category_1"
            }
        }
    },
    "executionStats": {
        "executionTimeMillis": 2,      // ← thời gian thực tế
        "totalKeysExamined": 8,        // ← số index keys đã scan
        "totalDocsExamined": 8,        // ← số documents đã đọc
        "nReturned": 3                 // ← số documents trả về
    }
}
```

**Stage types — từ tốt đến tệ:**


| Stage | Ý nghĩa | Đánh giá |
|---|---|---|
| IDHACK | Tìm theo _id | ✅ Tốt nhất |
| IXSCAN | Index scan | ✅ Tốt |
| FETCH | Đọc document từ index | ✅ OK |
| COLLSCAN | Collection scan toàn bộ | ❌ Cần xem xét |
| SORT | Sort không dùng index | ⚠️ Cần index |
| SORT_MERGE | Merge sort | ⚠️ Chậm |



**Dấu hiệu cần tối ưu:**

```javascript
// Bad: totalDocsExamined >> nReturned
"totalDocsExamined": 10000,
"nReturned": 5
// → Scan 10,000 documents để trả về 5 → cần index tốt hơn

// Bad: COLLSCAN trên collection lớn
"stage": "COLLSCAN"

// Bad: Sort stage sau COLLSCAN
{ "stage": "SORT", "inputStage": { "stage": "COLLSCAN" } }
```

## 5\. Tối Ưu Query Thực Tế

### Case Study 1: Query chậm vì thiếu index

```javascript
// Query chậm: 500ms
db.orders.find({
    user_id:      ObjectId("user_1"),
    order_status: "PAID"
}).sort({ created_at: -1 })

// EXPLAIN cho thấy COLLSCAN
// → Thêm compound index theo ESR rule
db.orders.createIndex({
    user_id:      1,      // Equality
    order_status: 1,      // Equality
    created_at:   -1      // Sort
})

// Sau khi thêm index: 2ms ✅
```

### Case Study 2: Query chậm vì function trên field

```javascript
// ❌ Query chậm — function trên field → không dùng index
db.users.find({
    $expr: {
        $eq: [{ $toLower: "$email" }, "nam@gmail.com"]
    }
})

// ✅ Fix: Lưu email đã lowercase + index thường
// Khi insert/update: lưu email lowercase
db.users.insertOne({
    email:        "Nam@Gmail.Com",
    email_lower:  "nam@gmail.com"  // ← normalized field
})
db.users.createIndex({ email_lower: 1 }, { unique: true })
db.users.find({ email_lower: "nam@gmail.com" })
```

### Case Study 3: Pagination hiệu quả

```javascript
// ❌ OFFSET-based pagination chậm dần theo trang
db.courses.find()
    .sort({ created_at: -1 })
    .skip(10000)   // phải scan 10,000 documents rồi bỏ qua!
    .limit(10)

// ✅ Cursor-based pagination — luôn nhanh
// Lần đầu (không có cursor)
db.courses.find({ course_status: "PUBLISHED" })
    .sort({ created_at: -1, _id: -1 })
    .limit(10)
// Lưu lại: last_created_at và last_id của document cuối

// Lần sau (có cursor)
db.courses.find({
    course_status: "PUBLISHED",
    $or: [
        { created_at: { $lt: last_created_at } },
        { created_at: last_created_at, _id: { $lt: last_id } }
    ]
}).sort({ created_at: -1, _id: -1 }).limit(10)
```

## 6\. Index Strategy Cho [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev)

```javascript
// ─── Collection: courses ───
// Query: browse by category, sort by enrolled_count
db.courses.createIndex({ course_status: 1, category: 1, enrolled_count: -1 })

// Query: text search
db.courses.createIndex({ title: "text", description: "text" })

// Query: find by slug
db.courses.createIndex({ slug: 1 }, { unique: true })

// Query: find by instructor
db.courses.createIndex({ "instructor._id": 1 })

// Query: find by tags
db.courses.createIndex({ tags: 1 })

// ─── Collection: users ───
// Login
db.users.createIndex({ email: 1 }, { unique: true })

// Find active users
db.users.createIndex({ account_status: 1, created_at: -1 })

// ─── Collection: orders ───
// Lịch sử mua hàng của user
db.orders.createIndex({ user_id: 1, created_at: -1 })

// Orders theo status (admin dashboard)
db.orders.createIndex({ order_status: 1, created_at: -1 })

// ─── Collection: enrollments ───
// User đã enroll khóa nào
db.enrollments.createIndex({ user_id: 1, course_id: 1 }, { unique: true })

// Khóa có bao nhiêu enrollment
db.enrollments.createIndex({ course_id: 1 })
```

## 7\. Performance Anti-patterns

**Anti-pattern 1: Index quá nhiều**

```javascript
// ❌ Mỗi field đều có index riêng
db.courses.createIndex({ title: 1 })
db.courses.createIndex({ price: 1 })
db.courses.createIndex({ category: 1 })
db.courses.createIndex({ rating: 1 })
db.courses.createIndex({ created_at: 1 })
// → 5 indexes = 5x overhead cho mỗi write

// ✅ Compound indexes thay thế nhiều single indexes
db.courses.createIndex({ category: 1, rating: -1, price: 1 })
// Hỗ trợ nhiều query patterns với 1 index
```

**Anti-pattern 2: Không dùng Projection**

```javascript
// ❌ Lấy toàn bộ document
db.courses.find({ category: "java" })
// → Transfer content text dài hàng KB không cần thiết

// ✅ Chỉ lấy fields cần
db.courses.find(
    { category: "java" },
    { title: 1, price: 1, rating: 1, _id: 0 }
)
```

**Anti-pattern 3: $where và JavaScript expressions**

```javascript
// ❌ $where dùng JavaScript → không dùng được index, cực chậm
db.orders.find({
    $where: "this.final_amount > 500000 && this.order_status === 'PAID'"
})

// ✅ Dùng native operators
db.orders.find({
    final_amount: { $gt: 500000 },
    order_status: "PAID"
})
```

**Anti-pattern 4: Regex không có prefix**

```javascript
// ❌ Regex không có prefix → không dùng index (full scan)
db.courses.find({ title: /spring/i })      // chứa "spring" ở bất kỳ đâu

// ✅ Regex có prefix anchor → dùng được index
db.courses.find({ title: /^Spring/i })     // bắt đầu bằng "Spring"

// ✅ Tốt hơn: dùng $text index
db.courses.find({ $text: { $search: "spring" } })
```

**Anti-pattern 5: Negation operators**

```javascript
// ❌ $ne, $nin, $not → thường không dùng index hiệu quả
db.courses.find({ category: { $ne: "frontend" } })
// → Phải scan gần toàn bộ collection

// ✅ Rewrite thành positive condition nếu có thể
db.courses.find({
    category: { $in: ["java", "backend", "devops", "database"] }
})
```

## 8\. Monitoring Performance

```javascript
// Bật Database Profiler — log slow queries
// Level 0: tắt, 1: chỉ slow queries, 2: tất cả
db.setProfilingLevel(1, { slowms: 100 })  // log queries > 100ms

// Xem slow queries
db.system.profile.find().sort({ ts: -1 }).limit(5)

// Xem hiện tại profiling level
db.getProfilingStatus()

// currentOp — xem queries đang chạy
db.currentOp({ "active": true, "secs_running": { $gt: 1 } })

// Xem collection stats
db.courses.stats()
// → Xem: avgObjSize, totalIndexSize, indexSizes

// Xem index usage — cái nào được dùng, cái nào không
db.courses.aggregate([{ $indexStats: {} }])
// ops: 0 → index chưa bao giờ được dùng → cân nhắc xóa
```

## 9\. Thực Hành Tổng Hợp

**Bài 1:** Tạo đầy đủ indexes cho `foxdev_nosql` database và verify bằng explain.

```javascript
// Tạo indexes
db.courses.createIndex({ course_status: 1, category: 1, enrolled_count: -1 })
db.courses.createIndex({ slug: 1 }, { unique: true })
db.courses.createIndex({ tags: 1 })
db.orders.createIndex({ user_id: 1, order_status: 1, created_at: -1 })
db.users.createIndex({ email: 1 }, { unique: true })

// Verify với explain
db.courses.find({
    course_status: "PUBLISHED",
    category:      "java"
}).sort({ enrolled_count: -1 }).explain("executionStats")
// → Kỳ vọng: IXSCAN, totalDocsExamined ≈ nReturned
```

**Bài 2:** Tìm query chậm và tối ưu.

```javascript
// Query cần tối ưu — không có index phù hợp
db.orders.find({
    order_status: "PAID",
    final_amount: { $gte: 500000 }
}).sort({ created_at: -1 }).explain("executionStats")

// Phân tích:
// - stage là gì? COLLSCAN hay IXSCAN?
// - totalDocsExamined vs nReturned?
// - Tạo index phù hợp theo ESR rule
// - Chạy lại explain và so sánh
```

## Tổng Kết


| Loại Index | Dùng khi |
|---|---|
| Single Field | Query/sort trên 1 field |
| Compound | Query nhiều fields, ESR rule |
| Multikey | Field là array |
| Text | Full-text search |
| TTL | Tự động expire documents |
| Partial | Chỉ index subset documents |
| Wildcard | Schema không cố định |



**Checklist tối ưu performance:**

```java
□ Explain plan không có COLLSCAN trên collection lớn
□ totalDocsExamined ≈ nReturned (ratio gần 1)
□ Projection: chỉ lấy fields cần thiết
□ Compound index theo ESR rule
□ Không dùng function/regex trên indexed field
□ Cursor-based pagination thay vì skip() lớn
□ Profiler bật để phát hiện slow queries
□ Xóa indexes không được dùng
```

Bài tiếp theo chúng ta sẽ chuyển sang **Redis** — Key-Value Database tốc độ microsecond, nền tảng của mọi hệ thống cache và session management hiện đại.

