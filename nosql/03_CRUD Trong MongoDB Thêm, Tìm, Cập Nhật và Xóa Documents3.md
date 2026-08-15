# CRUD Trong MongoDB: Thêm, Tìm, Cập Nhật và Xóa Documents

![Partitioning - Xử Lý Bảng Hàng Triệu Đến Hàng Tỷ Dòng.jpeg](../images/44a1d4cc-da65-4470-ad4b-e4c2272bbb2a.jpeg)

CRUD là nền tảng của mọi thao tác với database. Trong PostgreSQL bạn dùng `INSERT`, `SELECT`, `UPDATE`, `DELETE`. MongoDB có cú pháp riêng nhưng tư duy tương tự — chỉ khác ở chỗ thay vì thao tác trên rows và columns, bạn thao tác trên **JSON documents**. Bài này đi qua toàn bộ CRUD operations với dữ liệu thực của `foxdev_nosql`.

## 1\. Cấu Trúc Lệnh MongoDB

```javascript
// Pattern chung
db.<collection>.<method>(<query>, <options>)

// Ví dụ
db.courses.find({ price: { $lt: 500000 } })
db.users.insertOne({ email: "test@gmail.com" })
db.orders.updateOne({ _id: id }, { $set: { status: "PAID" } })
db.courses.deleteOne({ _id: id })
```

## 2\. CREATE — Thêm Documents

### insertOne — Thêm 1 Document

```javascript
// Thêm 1 user mới
db.users.insertOne({
    email:          "hung@gmail.com",
    first_name:     "Hung",
    last_name:      "Pham",
    account_status: "ACTIVE",
    account_type:   "INDIVIDUAL",
    enrolled_courses: 0,
    total_spent:    0,
    tags:           [],
    created_at:     new Date()
})

// MongoDB tự generate _id nếu không cung cấp
// Output:
// {
//   acknowledged: true,
//   insertedId: ObjectId("65f1a2b3c4d5e6f7a8b9c0d9")
// }
```

**Tự cung cấp** `_id`**:**

```javascript
// Có thể dùng bất kỳ kiểu nào cho _id
db.courses.insertOne({
    _id:    "spring-boot-advanced",  // string thay vì ObjectId
    title:  "Spring Boot Nâng Cao",
    price:  999000
})
```

### insertMany — Thêm Nhiều Documents

```javascript
db.courses.insertMany([
    {
        title:        "ReactJS cơ bản đến nâng cao",
        slug:         "reactjs-co-ban-den-nang-cao",
        course_type:  "PAID",
        course_status: "PUBLISHED",
        price:        699000,
        rating:       4.5,
        enrolled_count: 150,
        category:     "frontend",
        tags:         ["react", "javascript", "frontend"],
        instructor:   { name: "FoxDev", email: "contact@nguyentienkhoi.hashnode.dev" },
        created_at:   new Date("2024-01-01"),
        updated_at:   new Date("2024-03-01")
    },
    {
        title:        "Node.js API Development",
        slug:         "nodejs-api-development",
        course_type:  "PAID",
        course_status: "PUBLISHED",
        price:        749000,
        rating:       4.4,
        enrolled_count: 130,
        category:     "backend",
        tags:         ["nodejs", "javascript", "api", "backend"],
        instructor:   { name: "FoxDev", email: "contact@nguyentienkhoi.hashnode.dev" },
        created_at:   new Date("2024-02-01"),
        updated_at:   new Date("2024-03-10")
    }
])

// Output:
// {
//   acknowledged: true,
//   insertedCount: 2,
//   insertedIds: { '0': ObjectId("..."), '1': ObjectId("...") }
// }
```

**Ordered vs Unordered Insert:**

```javascript
// Mặc định: ordered = true — dừng lại khi gặp lỗi
db.courses.insertMany([doc1, doc2, doc3])

// ordered: false — tiếp tục insert dù có document lỗi
db.courses.insertMany(
    [doc1, doc2, doc3],
    { ordered: false }
)
```

## 3\. READ — Tìm Documents

### find — Tìm Nhiều Documents

```javascript
// Lấy tất cả (cẩn thận với collection lớn)
db.courses.find()

// Filter theo điều kiện
db.courses.find({ category: "java" })

// Multiple conditions — AND ngầm định
db.courses.find({
    category:      "java",
    course_status: "PUBLISHED"
})
```

### findOne — Tìm 1 Document Đầu Tiên

```javascript
// Trả về document đầu tiên khớp
db.users.findOne({ email: "nam@gmail.com" })

// Tìm theo _id
db.courses.findOne({ _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e1") })
```

### Projection — Chọn Fields Trả Về

```javascript
// Chỉ lấy title và price (1 = include)
db.courses.find(
    { course_status: "PUBLISHED" },
    { title: 1, price: 1, rating: 1 }
)
// _id luôn được trả về trừ khi ẩn tường minh

// Ẩn _id
db.courses.find(
    { course_status: "PUBLISHED" },
    { title: 1, price: 1, _id: 0 }
)

// Ẩn một số fields (0 = exclude) — không mix 0 và 1 (trừ _id)
db.courses.find(
    {},
    { sections: 0, updated_at: 0 }  // ẩn 2 fields nặng
)
```

### Comparison Operators — Toán Tử So Sánh

```javascript
// $eq — bằng (mặc định)
db.courses.find({ price: { $eq: 799000 } })
// Tương đương:
db.courses.find({ price: 799000 })

// $ne — không bằng
db.courses.find({ course_type: { $ne: "FREE" } })

// $gt, $gte, $lt, $lte — lớn hơn, lớn hơn bằng, nhỏ hơn, nhỏ hơn bằng
db.courses.find({ price: { $gt: 500000, $lte: 800000 } })

// $in — nằm trong danh sách
db.courses.find({ category: { $in: ["java", "backend"] } })

// $nin — không nằm trong danh sách
db.courses.find({ category: { $nin: ["frontend", "mobile"] } })
```

### Logical Operators — Toán Tử Logic

```javascript
// $and — tất cả điều kiện đều đúng
db.courses.find({
    $and: [
        { price: { $gt: 500000 } },
        { rating: { $gte: 4.5 } }
    ]
})
// Tương đương ngắn gọn hơn (AND ngầm định):
db.courses.find({ price: { $gt: 500000 }, rating: { $gte: 4.5 } })

// $or — ít nhất 1 điều kiện đúng
db.courses.find({
    $or: [
        { course_type: "FREE" },
        { price: { $lt: 300000 } }
    ]
})

// $nor — không điều kiện nào đúng
db.courses.find({
    $nor: [
        { course_status: "DRAFT" },
        { course_status: "ARCHIVED" }
    ]
})

// $not — phủ định một điều kiện
db.courses.find({
    price: { $not: { $gt: 800000 } }
})

// Kết hợp phức tạp
db.courses.find({
    course_status: "PUBLISHED",
    $or: [
        { category: "java" },
        { rating: { $gte: 4.8 } }
    ]
})
```

### Array Operators — Toán Tử Mảng

```javascript
// Tìm document có chứa giá trị trong array
db.courses.find({ tags: "java" })
// → Tìm courses có "java" trong mảng tags

// $all — array phải chứa tất cả giá trị
db.courses.find({ tags: { $all: ["java", "spring"] } })

// $size — array có đúng N phần tử
db.courses.find({ tags: { $size: 3 } })

// $elemMatch — ít nhất 1 element trong array thỏa điều kiện
db.courses.find({
    sections: {
        $elemMatch: { lectures_count: { $gte: 10 } }
    }
})
// → Tìm course có ít nhất 1 section có từ 10 lectures trở lên
```

### Element Operators

```javascript
// $exists — field có tồn tại không
db.users.find({ phone: { $exists: true } })   // có field phone
db.users.find({ phone: { $exists: false } })  // không có field phone

// $type — kiểm tra kiểu dữ liệu
db.courses.find({ price: { $type: "number" } })
db.courses.find({ tags: { $type: "array" } })
```

### Sort, Limit, Skip

```javascript
// sort: 1 = ASC, -1 = DESC
db.courses.find().sort({ rating: -1, price: 1 })

// limit: giới hạn số kết quả
db.courses.find().sort({ rating: -1 }).limit(3)

// skip: bỏ qua N documents (dùng cho pagination — cẩn thận với skip lớn)
db.courses.find().sort({ created_at: -1 }).skip(10).limit(5)

// Chaining đầy đủ
db.courses
    .find({ course_status: "PUBLISHED" })
    .sort({ enrolled_count: -1 })
    .limit(5)
    .projection({ title: 1, price: 1, enrolled_count: 1, _id: 0 })
```

### Query Trong Nested Document

```javascript
// Dùng dot notation để query field lồng nhau
db.courses.find({ "instructor.name": "FoxDev" })

// Query trong array của objects
db.courses.find({ "sections.title": "REST API với Spring MVC" })

// Query nested với $elemMatch
db.orders.find({
    items: {
        $elemMatch: {
            price: { $gt: 700000 }
        }
    }
})
```

### Đếm Documents

```javascript
// countDocuments — đếm với filter
db.courses.countDocuments({ course_type: "PAID" })
db.orders.countDocuments({ order_status: "PAID" })

// estimatedDocumentCount — ước tính nhanh tổng số (không filter)
db.courses.estimatedDocumentCount()
```

## 4\. UPDATE — Cập Nhật Documents

### updateOne — Cập Nhật 1 Document

```javascript
// $set — cập nhật giá trị field
db.courses.updateOne(
    { _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e1") },  // filter
    {
        $set: {
            price:      699000,
            updated_at: new Date()
        }
    }
)
```

### updateMany — Cập Nhật Nhiều Documents

```javascript
// Cập nhật tất cả courses của category java
db.courses.updateMany(
    { category: "java" },
    {
        $set: { updated_at: new Date() },
        $inc: { enrolled_count: 0 }  // không thay đổi, chỉ touch document
    }
)
```

### Update Operators

**$set — Đặt giá trị mới:**

```javascript
db.users.updateOne(
    { email: "nam@gmail.com" },
    {
        $set: {
            "account_status": "INACTIVE",
            "updated_at":     new Date()
        }
    }
)
```

**$unset — Xóa field:**

```javascript
// Xóa field tags khỏi document
db.users.updateOne(
    { email: "nam@gmail.com" },
    { $unset: { tags: "" } }
)
```

**$inc — Tăng/giảm giá trị số:**

```javascript
// Tăng enrolled_count lên 1
db.courses.updateOne(
    { _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e1") },
    { $inc: { enrolled_count: 1 } }
)

// Giảm stock (nếu có)
db.products.updateOne(
    { _id: id },
    { $inc: { stock: -1 } }
)
```

**$push — Thêm phần tử vào array:**

```javascript
// Thêm tag mới vào array tags
db.courses.updateOne(
    { _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e1") },
    { $push: { tags: "microservices" } }
)

// $each — thêm nhiều phần tử cùng lúc
db.courses.updateOne(
    { _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e1") },
    {
        $push: {
            tags: {
                $each: ["cloud", "aws"],
                $position: 0  // thêm vào đầu mảng
            }
        }
    }
)
```

**$pull — Xóa phần tử khỏi array:**

```javascript
// Xóa tag "cloud" khỏi array
db.courses.updateOne(
    { _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e1") },
    { $pull: { tags: "cloud" } }
)

// $pull với condition
db.courses.updateOne(
    { _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e1") },
    {
        $pull: {
            sections: { lectures_count: { $lt: 3 } }
        }
    }
)
```

**$addToSet — Thêm vào array nếu chưa tồn tại (no duplicate):**

```javascript
db.users.updateOne(
    { email: "nam@gmail.com" },
    { $addToSet: { tags: "devops" } }
)
// Nếu "devops" đã có trong tags → không thêm nữa
```

**$rename — Đổi tên field:**

```javascript
db.users.updateMany(
    {},
    { $rename: { "enrolled_courses": "courses_enrolled" } }
)
```

### upsert — Insert Nếu Không Tồn Tại

```javascript
// upsert: true — update nếu tìm thấy, insert nếu không tìm thấy
db.users.updateOne(
    { email: "new@gmail.com" },
    {
        $set: {
            email:          "new@gmail.com",
            first_name:     "New",
            account_status: "ACTIVE",
            created_at:     new Date()
        }
    },
    { upsert: true }
)
// Nếu email chưa tồn tại → tạo document mới
// Nếu email đã tồn tại → cập nhật
```

### findOneAndUpdate — Tìm, Update và Trả Về Document

```javascript
// Mặc định trả về document TRƯỚC khi update
const result = db.courses.findOneAndUpdate(
    { _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e1") },
    { $inc: { enrolled_count: 1 } },
    { returnDocument: "after" }  // trả về document SAU khi update
)

// Hữu ích khi cần giá trị mới ngay sau khi update
```

## 5\. DELETE — Xóa Documents

### deleteOne — Xóa 1 Document

```javascript
// Xóa user theo email
db.users.deleteOne({ email: "hung@gmail.com" })

// Xóa theo _id
db.courses.deleteOne({ _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e1") })

// Output:
// { acknowledged: true, deletedCount: 1 }
```

### deleteMany — Xóa Nhiều Documents

```javascript
// Xóa tất cả orders bị cancelled
db.orders.deleteMany({ order_status: "CANCELLED" })

// Xóa users không active và chưa từng mua hàng
db.users.deleteMany({
    account_status: "INACTIVE",
    total_spent:    0
})

// ⚠️ XÓA TOÀN BỘ COLLECTION — dùng rất cẩn thận!
db.test_collection.deleteMany({})
```

### findOneAndDelete — Tìm, Trả Về Và Xóa

```javascript
// Trả về document vừa bị xóa
const deleted = db.orders.findOneAndDelete({
    order_status: "CANCELLED",
    created_at:   { $lt: new Date("2024-01-01") }
})
// Hữu ích để "pop" task từ queue
```

## 6\. Thực Hành Tổng Hợp

**Bài 1:** Thêm một khóa học mới và tăng enrolled\_count khi có user enroll.

```javascript
// Bước 1: Thêm khóa học
db.courses.insertOne({
    title:         "TypeScript cho Java Developer",
    slug:          "typescript-cho-java-developer",
    course_type:   "PAID",
    course_status: "PUBLISHED",
    price:         649000,
    rating:        0,
    enrolled_count: 0,
    category:      "frontend",
    tags:          ["typescript", "javascript", "frontend"],
    instructor:    { name: "FoxDev", email: "contact@nguyentienkhoi.hashnode.dev" },
    sections:      [],
    created_at:    new Date(),
    updated_at:    new Date()
})

// Bước 2: User enroll → tăng enrolled_count
db.courses.updateOne(
    { slug: "typescript-cho-java-developer" },
    {
        $inc: { enrolled_count: 1 },
        $set: { updated_at: new Date() }
    }
)

// Kiểm tra
db.courses.findOne(
    { slug: "typescript-cho-java-developer" },
    { title: 1, enrolled_count: 1 }
)
```

**Bài 2:** Tìm tất cả khóa học có rating >= 4.7, sort theo enrolled\_count giảm dần, chỉ lấy title, rating, price.

```javascript
db.courses.find(
    { rating: { $gte: 4.7 } },
    { title: 1, rating: 1, price: 1, enrolled_count: 1, _id: 0 }
).sort({ enrolled_count: -1 })
```

**Bài 3:** Cập nhật tất cả khóa học category "java" — thêm tag "jvm" nếu chưa có.

```javascript
db.courses.updateMany(
    { category: "java" },
    { $addToSet: { tags: "jvm" } }
)

// Verify
db.courses.find(
    { category: "java" },
    { title: 1, tags: 1, _id: 0 }
)
```

**Bài 4:** Xóa tất cả orders bị CANCELLED của năm 2024.

```javascript
// Kiểm tra trước
db.orders.countDocuments({
    order_status: "CANCELLED",
    created_at: {
        $gte: new Date("2024-01-01"),
        $lt:  new Date("2025-01-01")
    }
})

// Xóa
db.orders.deleteMany({
    order_status: "CANCELLED",
    created_at: {
        $gte: new Date("2024-01-01"),
        $lt:  new Date("2025-01-01")
    }
})
```

## Tổng Kết


| Operation | Method | Mô tả |
|---|---|---|
| CREATE | insertOne() | Thêm 1 document |
|  | insertMany() | Thêm nhiều documents |
| READ | find() | Tìm nhiều documents |
|  | findOne() | Tìm 1 document |
|  | countDocuments() | Đếm documents |
| UPDATE | updateOne() | Cập nhật 1 document |
|  | updateMany() | Cập nhật nhiều documents |
|  | findOneAndUpdate() | Update và trả về document |
|  | upsert: true | Insert nếu không tìm thấy |
| DELETE | deleteOne() | Xóa 1 document |
|  | deleteMany() | Xóa nhiều documents |
|  | findOneAndDelete() | Xóa và trả về document |




| Operator | Dùng cho |
|---|---|
| $set | Đặt giá trị field |
| $unset | Xóa field |
| $inc | Tăng/giảm số |
| $push | Thêm vào array |
| $pull | Xóa khỏi array |
| $addToSet | Thêm vào array (no dup) |
| $gt, $lt, $gte, $lte | So sánh |
| $in, $nin | Trong/ngoài danh sách |
| $and, $or, $not | Logic |
| $exists | Field có tồn tại |
| $elemMatch | Match element trong array |



Bài tiếp theo chúng ta sẽ học **Aggregation Pipeline** — công cụ mạnh nhất của MongoDB để phân tích dữ liệu phức tạp với `$match`, `$group`, `$lookup`, `$unwind` và nhiều hơn nữa.

