# Schema Design: Embed vs Reference và Các Patterns Thực Tế

![Schema Design- Embed vs Reference và Các Patterns Thực Tế.jpeg](../images/55199f99-8f62-4e09-9e7e-a2d543400b7a.jpeg)

Đây là bài quan trọng nhất trong toàn bộ series MongoDB. Hầu hết developer mới dùng MongoDB đều mắc sai lầm ở đây: hoặc **embed tất cả** (document phình to, khó update), hoặc **reference tất cả** (query chậm vì nhiều $lookup). Bài này sẽ giúp bạn đưa ra quyết định đúng cho từng tình huống cụ thể.

## 1\. Tư Duy Thiết Kế Schema Trong MongoDB

SQL: **thiết kế schema theo cấu trúc dữ liệu** — normalize, tránh redundancy MongoDB: **thiết kế schema theo access pattern** — data hay được đọc cùng nhau thì lưu cùng nhau

```java
Câu hỏi đầu tiên khi thiết kế MongoDB schema:
  "Ứng dụng sẽ đọc/ghi data này như thế nào?"
  
Không phải:
  "Data này có quan hệ gì với data kia?"
```

## 2\. Embedding — Lồng Document Bên Trong

Lưu dữ liệu liên quan trực tiếp bên trong document cha.

```javascript
// Embedding: instructor lồng trong course
{
    _id:   ObjectId("..."),
    title: "Spring Boot từ Zero đến Hero",
    price: 799000,
    instructor: {           // ← embedded document
        name:   "FoxDev",
        email:  "contact@nguyentienkhoi.hashnode.dev",
        avatar: "https://..."
    },
    sections: [             // ← embedded array
        { title: "Giới thiệu", lectures_count: 5 },
        { title: "REST API",   lectures_count: 12 }
    ]
}
```

**Ưu điểm:**

*   Đọc trong một query — không cần JOIN ($lookup)
    
*   Atomic update — cập nhật course + instructor trong một operation
    
*   Performance tốt hơn cho read-heavy workload
    

**Nhược điểm:**

*   Document phình to nếu embed quá nhiều
    
*   Khó update riêng phần embedded (ví dụ: đổi email instructor)
    
*   Giới hạn 16MB per document
    
*   Dữ liệu duplicate nếu instructor dạy nhiều courses
    

## 3\. Referencing — Tham Chiếu Sang Collection Khác

Lưu `_id` của document ở collection khác, join bằng `$lookup` khi cần.

```javascript
// Collection: courses
{
    _id:           ObjectId("course_1"),
    title:         "Spring Boot từ Zero đến Hero",
    price:         799000,
    instructor_id: ObjectId("user_1")  // ← reference
}

// Collection: users
{
    _id:   ObjectId("user_1"),
    name:  "FoxDev",
    email: "contact@nguyentienkhoi.hashnode.dev"
}
```

**Ưu điểm:**

*   Document gọn nhẹ
    
*   Update instructor chỉ 1 chỗ, tự động reflect ở tất cả courses
    
*   Phù hợp dữ liệu shared giữa nhiều documents
    

**Nhược điểm:**

*   Cần `$lookup` để join — thêm round trip
    
*   Không atomic qua nhiều collections (cần transaction)
    
*   Application phải tự xử lý referential integrity
    

## 4\. Khi Nào Embed, Khi Nào Reference?

### Embed khi:

```java
✅ Quan hệ 1-1: user → address (mỗi user có 1 địa chỉ chính)
✅ Quan hệ 1-few: course → sections (1 course có vài chục sections)
✅ Data luôn được đọc cùng nhau: course + instructor name
✅ Data con không bao giờ tồn tại độc lập
✅ Data con ít thay đổi
✅ Cần atomic updates
```

### Reference khi:

```java
✅ Quan hệ 1-many lớn: user → orders (1 user có thể có 1000 orders)
✅ Quan hệ many-to-many: users ↔ courses
✅ Data được dùng độc lập ở nhiều nơi
✅ Data con thay đổi thường xuyên
✅ Document sẽ phình quá 16MB nếu embed
✅ Cần query riêng data con
```

### Bảng quyết định nhanh:


| Tình huống | Quyết định |
|---|---|
| Một course có nhiều sections | Embed sections |
| Một user có nhiều orders | Reference (orders riêng) |
| Một order có nhiều items | Embed items |
| Một course có một instructor | Embed instructor name, reference instructor_id |
| Một khóa học có nhiều tags | Embed tags (array string) |
| Nhiều courses cùng một category | Reference category_id |



## 5\. Pattern 1: Extended Reference Pattern

Embed **một phần** thông tin hay dùng nhất, vẫn giữ reference `_id` cho các queries cần đầy đủ.

**Vấn đề:** Hiển thị danh sách orders cần tên user — nhưng không muốn $lookup mỗi lần.

```javascript
// ❌ Pure Reference — phải $lookup mỗi lần hiển thị
{
    _id:     ObjectId("order_1"),
    user_id: ObjectId("user_1"),   // phải lookup users
    amount:  799000
}

// ❌ Full Embed — dữ liệu user thay đổi thì phải update tất cả orders
{
    _id: ObjectId("order_1"),
    user: {
        _id:        ObjectId("user_1"),
        email:      "nam@gmail.com",
        first_name: "Nam",
        last_name:  "Nguyen",
        phone:      "0901234567",   // không cần cho order list
        address:    { ... }         // không cần cho order list
    },
    amount: 799000
}

// ✅ Extended Reference — embed chỉ những gì cần thiết + giữ reference
{
    _id:     ObjectId("order_1"),
    user_id: ObjectId("user_1"),   // ← vẫn giữ reference cho query phức tạp
    user_snapshot: {               // ← embed subset cần thiết
        name:  "Nam Nguyen",
        email: "nam@gmail.com"
    },
    amount:  799000,
    created_at: new Date()
}
```

**Dữ liệu** `user_snapshot` **là "snapshot" tại thời điểm order** — đây là behavior mong muốn. Nếu user đổi tên sau đó, lịch sử order vẫn lưu tên cũ.

## 6\. Pattern 2: Subset Pattern

Embed chỉ **N items được dùng nhiều nhất** (ví dụ 5 reviews gần nhất), lưu toàn bộ ở collection riêng.

**Vấn đề:** Course có 5,000 reviews — embed tất cả thì document quá lớn, nhưng trang detail cần hiển thị vài reviews mới nhất ngay lập tức.

```javascript
// ✅ Subset Pattern
// Collection: courses
{
    _id:   ObjectId("course_1"),
    title: "Spring Boot từ Zero đến Hero",
    rating: 4.8,
    total_reviews: 5000,            // ← cache tổng số
    recent_reviews: [               // ← chỉ 5 reviews mới nhất
        {
            user_name: "Nam Nguyen",
            rating:    5,
            comment:   "Khóa học rất tốt!",
            created_at: new Date()
        },
        // ... 4 cái nữa
    ]
}

// Collection: reviews (toàn bộ 5000 reviews)
{
    _id:        ObjectId("..."),
    course_id:  ObjectId("course_1"),
    user_id:    ObjectId("..."),
    rating:     5,
    comment:    "Khóa học rất tốt!",
    created_at: new Date()
}
```

**Khi có review mới:**

```javascript
// Bước 1: Insert vào reviews collection
db.reviews.insertOne({ course_id, user_id, rating, comment, created_at: new Date() })

// Bước 2: Update recent_reviews trong course (giữ 5 cái mới nhất)
db.courses.updateOne(
    { _id: course_id },
    {
        $push: {
            recent_reviews: {
                $each:     [{ user_name, rating, comment, created_at: new Date() }],
                $sort:     { created_at: -1 },
                $slice:    5          // chỉ giữ 5 cái mới nhất
            }
        },
        $inc: { total_reviews: 1 },
        $set: { rating: new_avg_rating }
    }
)
```

## 7\. Pattern 3: Bucket Pattern

Gom nhiều documents nhỏ thành **một bucket** theo tiêu chí nào đó (thời gian, user, ...).

**Vấn đề:** `video_tracking_logs` ghi 10 triệu events mỗi ngày — 1 document per event sẽ tạo 300 triệu documents/tháng.

```javascript
// ❌ 1 document per event — quá nhiều documents
{
    _id:       ObjectId("..."),
    user_id:   ObjectId("user_1"),
    course_id: ObjectId("course_1"),
    action:    "play",
    time:      new Date()
}
// × 10 triệu documents mỗi ngày

// ✅ Bucket Pattern — gom theo user + course + ngày
{
    _id: {
        user_id:   ObjectId("user_1"),
        course_id: ObjectId("course_1"),
        date:      "2025-03-15"
    },
    events: [
        { action: "play",  time: "09:00:01", position: 0 },
        { action: "pause", time: "09:15:33", position: 915 },
        { action: "play",  time: "09:16:02", position: 915 },
        { action: "end",   time: "09:45:20", position: 2720 }
    ],
    total_events:   4,
    total_duration: 2720,   // giây đã xem
    last_position:  2720,
    date:           new Date("2025-03-15")
}
```

**Insert event mới vào bucket:**

```javascript
db.video_tracking.updateOne(
    {
        "_id.user_id":   userId,
        "_id.course_id": courseId,
        "_id.date":      today,
        total_events:    { $lt: 200 }  // giới hạn bucket size
    },
    {
        $push: { events: newEvent },
        $inc:  {
            total_events:   1,
            total_duration: event.duration
        },
        $set: { last_position: event.position }
    },
    { upsert: true }  // tạo bucket mới nếu chưa có
)
```

**Lợi ích:** 10 triệu events → ~100,000 buckets/ngày (giảm 100x số documents).

## 8\. Pattern 4: Computed Pattern

**Pre-compute** và cache kết quả tính toán nặng vào document — tránh tính lại mỗi lần đọc.

**Vấn đề:** Trang course detail cần hiển thị average rating — tính `AVG` trên 5,000 reviews mỗi lần load trang rất chậm.

```javascript
// ✅ Computed Pattern — cache avg rating trong course document
{
    _id:    ObjectId("course_1"),
    title:  "Spring Boot từ Zero đến Hero",
    // Computed fields — cập nhật khi có review mới
    rating:       4.82,         // pre-computed average
    total_reviews: 5000,        // pre-computed count
    rating_breakdown: {         // pre-computed distribution
        "5_star": 3200,
        "4_star": 1400,
        "3_star": 300,
        "2_star": 80,
        "1_star": 20
    }
}
```

**Update khi có review mới:**

```javascript
// Tính lại rating và cập nhật
async function addReview(courseId, newRating) {
    const course = await db.courses.findOne({ _id: courseId })

    const total = course.total_reviews + 1
    const newAvg = (
        (course.rating * course.total_reviews) + newRating
    ) / total

    await db.courses.updateOne(
        { _id: courseId },
        {
            $set: {
                rating:        Math.round(newAvg * 100) / 100,
            },
            $inc: {
                total_reviews: 1,
                [`rating_breakdown.${newRating}_star`]: 1
            }
        }
    )
}
```

## 9\. Pattern 5: Outlier Pattern

Xử lý các documents có dữ liệu "bất thường" (outlier) — quá nhiều items trong array.

**Vấn đề:** Hầu hết users có 1-20 enrolled courses, nhưng một số admin/partner có 5,000 courses. Nếu embed enrollments vào user document thì outlier phình to.

```javascript
// ✅ Outlier Pattern

// User bình thường — embed enrollments
{
    _id:         ObjectId("user_1"),
    email:       "nam@gmail.com",
    enrollments: [                    // embed được vì ít
        ObjectId("course_1"),
        ObjectId("course_2")
    ],
    has_overflow: false               // flag
}

// User outlier (partner, admin) — dùng separate collection
{
    _id:          ObjectId("admin_1"),
    email:        "partner@company.com",
    enrollments:  [                   // chỉ embed 20 cái đầu
        ObjectId("course_1"),
        // ...19 cái nữa
    ],
    has_overflow: true                // flag: còn data ở overflow collection
}

// Collection riêng cho overflow
// enrollments_overflow
{
    user_id:     ObjectId("admin_1"),
    enrollments: [
        ObjectId("course_21"),
        ObjectId("course_22"),
        // ...4,980 cái nữa
    ]
}
```

**Query:** Kiểm tra `has_overflow` flag — nếu true thì query thêm `enrollments_overflow`.

## 10\. Thiết Kế Schema Thực Tế — [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev)

Áp dụng tất cả patterns vào database thực:

```javascript
// Collection: courses
{
    _id:           ObjectId("..."),
    title:         "Spring Boot từ Zero đến Hero",
    slug:          "spring-boot-tu-zero-den-hero",
    course_status: "PUBLISHED",
    course_type:   "PAID",
    price:         799000,

    // Extended Reference: embed thông tin instructor hay hiển thị
    instructor: {
        _id:    ObjectId("user_1"),    // giữ reference để query đầy đủ
        name:   "FoxDev",             // embed để hiển thị không cần lookup
        avatar: "https://..."
    },

    // Computed Pattern: pre-compute stats
    stats: {
        rating:           4.82,
        total_reviews:    5000,
        enrolled_count:   320,
        completion_rate:  0.74
    },

    // Subset Pattern: 5 reviews gần nhất
    recent_reviews: [
        { user_name: "Nam", rating: 5, comment: "Tuyệt vời!", date: new Date() }
        // ... 4 more
    ],

    // Embed trực tiếp: sections ít thay đổi, luôn đọc cùng course
    sections: [
        {
            _id:            ObjectId("..."),
            title:          "Giới thiệu",
            lectures_count: 5,
            duration_mins:  45
        }
    ],

    // Embed array đơn giản: tags
    tags:       ["java", "spring", "backend"],
    category:   "java",

    created_at: new Date(),
    updated_at: new Date()
}

// Collection: enrollments (reference pattern — 1 user có nhiều enrollments)
{
    _id:         ObjectId("..."),
    user_id:     ObjectId("user_1"),     // reference
    course_id:   ObjectId("course_1"),   // reference
    enrolled_at: new Date(),
    progress:    0.45,                   // 45% hoàn thành
    last_watched_lecture: ObjectId("...")
}

// Collection: orders (extended reference)
{
    _id:          ObjectId("..."),
    user_id:      ObjectId("user_1"),
    user_snapshot: {                    // extended reference
        name:  "Nam Nguyen",
        email: "nam@gmail.com"
    },
    order_status: "PAID",
    items: [                            // embed items — luôn đọc cùng order
        {
            course_id:    ObjectId("course_1"),
            course_title: "Spring Boot từ Zero đến Hero",  // snapshot
            price:        799000                            // price tại thời điểm mua
        }
    ],
    final_amount: 799000,
    created_at:   new Date()
}
```

## 11\. Những Sai Lầm Phổ Biến

**Sai lầm 1: Thiết kế MongoDB giống SQL**

```javascript
// ❌ Normalize giống SQL — quá nhiều collections, quá nhiều $lookup
// users, user_addresses, user_preferences, user_notifications...
// → Cần 5 $lookup chỉ để render 1 trang profile

// ✅ Embed những gì luôn đọc cùng nhau
{
    _id: ObjectId("..."),
    email: "nam@gmail.com",
    address: { city: "HCM", district: "Q1" },  // embed
    preferences: { lang: "vi", theme: "dark" }  // embed
}
```

**Sai lầm 2: Embed array không giới hạn**

```javascript
// ❌ Array phình vô hạn — document vượt 16MB
{
    course_id: ObjectId("..."),
    all_enrollments: [userId1, userId2, ..., userId100000]  // KHÔNG LÀM VẬY
}

// ✅ Dùng collection riêng cho quan hệ 1-many lớn
// Collection: enrollments
{ user_id: ..., course_id: ..., enrolled_at: ... }
```

**Sai lầm 3: Không có** `_id` **trong embedded documents**

```javascript
// ❌ Không có _id trong sections — khó update từng section
{
    sections: [
        { title: "Intro", lectures: 5 },    // không có _id!
        { title: "REST API", lectures: 12 }
    ]
}

// ✅ Thêm _id cho embedded documents cần update riêng lẻ
{
    sections: [
        { _id: ObjectId("..."), title: "Intro", lectures: 5 },
        { _id: ObjectId("..."), title: "REST API", lectures: 12 }
    ]
}

// Giờ có thể update chính xác 1 section
db.courses.updateOne(
    { "sections._id": ObjectId("section_id") },
    { $set: { "sections.$.lectures": 6 } }
)
```

**Sai lầm 4: Không dự phòng cho schema evolution**

```javascript
// ✅ Thêm schema_version để dễ migrate sau này
{
    _id: ObjectId("..."),
    schema_version: 2,   // khi thay đổi schema → tăng version
    ...
}
```

## Tổng Kết


| Pattern | Dùng khi | Ví dụ nguyentienkhoi.hashnode.dev |
|---|---|---|
| Embed | 1-1, 1-few, luôn đọc cùng nhau | Course + sections, Order + items |
| Reference | 1-many lớn, many-to-many, shared data | User → Orders, User ↔ Courses |
| Extended Reference | Cần 1 phần data liên quan, không cần full | Order lưu user name + email |
| Subset | Array lớn nhưng thường chỉ xem N items đầu | Course lưu 5 reviews gần nhất |
| Bucket | Event/time-series với volume cao | Video tracking logs theo ngày |
| Computed | Tính toán nặng, đọc nhiều hơn ghi | Average rating trong course |
| Outlier | Hầu hết normal nhưng có vài outlier | User với 5000 enrollments |



**Quy tắc vàng:**

> **"Data that is accessed together should be stored together"** — MongoDB documentation

Bài tiếp theo chúng ta sẽ học **Index và Performance trong MongoDB** — cách tạo đúng index, đọc explain plan và tối ưu query chậm.

