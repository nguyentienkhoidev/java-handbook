# Aggregation Pipeline: Phân Tích Dữ Liệu Mạnh Mẽ Trong MongoDB

![Aggregation Pipeline- Phân Tích Dữ Liệu Mạnh Mẽ Trong MongoDB.jpeg](../images/a12381e7-ac02-4782-8e33-a9245d3c1089.jpeg)

`find()` chỉ lọc và trả về documents. Nhưng khi cần **phân tích dữ liệu** — tính doanh thu theo tháng, tìm top khóa học bán chạy, join dữ liệu từ nhiều collections — bạn cần **Aggregation Pipeline**. Đây là tính năng mạnh nhất của MongoDB, tương đương với `GROUP BY`, `JOIN`, `WINDOW FUNCTION` trong SQL nhưng theo phong cách pipeline.

## 1\. Aggregation Pipeline Là Gì?

Pipeline = **chuỗi các stages (bước xử lý)** — output của stage trước là input của stage sau.

```java
Collection
    │
    ▼
┌─────────┐
│ $match  │  ← Lọc documents (tương tự WHERE)
└────┬────┘
     │
     ▼
┌─────────┐
│ $group  │  ← Nhóm và tính toán (tương tự GROUP BY)
└────┬────┘
     │
     ▼
┌─────────┐
│ $sort   │  ← Sắp xếp (tương tự ORDER BY)
└────┬────┘
     │
     ▼
┌─────────┐
│ $limit  │  ← Giới hạn kết quả (tương tự LIMIT)
└────┬────┘
     │
     ▼
  Kết quả
```

```javascript
// Cú pháp
db.collection.aggregate([
    { $stage1: { ... } },
    { $stage2: { ... } },
    { $stage3: { ... } }
])
```

## 2\. $match — Lọc Documents

Tương tự `WHERE` trong SQL — luôn đặt **$match sớm nhất có thể** để giảm số documents xử lý.

```javascript
// Lọc orders đã thanh toán
db.orders.aggregate([
    { $match: { order_status: "PAID" } }
])

// Nhiều điều kiện
db.courses.aggregate([
    {
        $match: {
            course_status: "PUBLISHED",
            rating:        { $gte: 4.5 },
            price:         { $gt: 0 }
        }
    }
])

// Với $or
db.courses.aggregate([
    {
        $match: {
            $or: [
                { category: "java" },
                { category: "backend" }
            ]
        }
    }
])
```

## 3\. $group — Nhóm và Tính Toán

Tương tự `GROUP BY + aggregate functions` trong SQL.

```javascript
// Cú pháp
{ $group: {
    _id:  <expression>,  // field để group — tương tự GROUP BY
    field: { $accumulator: <expression> }
}}
```

### Ví dụ cơ bản:

```javascript
// Tổng doanh thu và số đơn hàng theo status
db.orders.aggregate([
    {
        $group: {
            _id:           "$order_status",
            total_revenue: { $sum: "$final_amount" },
            order_count:   { $sum: 1 },
            avg_amount:    { $avg: "$final_amount" }
        }
    }
])

// Output:
// { _id: "PAID",      total_revenue: 2897000, order_count: 3, avg_amount: 965666 }
// { _id: "CANCELLED", total_revenue: 599000,  order_count: 1, avg_amount: 599000 }
```

### Group theo nhiều fields:

```javascript
// Doanh thu theo tháng và category
db.orders.aggregate([
    { $match: { order_status: "PAID" } },
    {
        $group: {
            _id: {
                year:  { $year:  "$created_at" },
                month: { $month: "$created_at" }
            },
            total_revenue: { $sum: "$final_amount" },
            order_count:   { $sum: 1 }
        }
    },
    { $sort: { "_id.year": -1, "_id.month": -1 } }
])
```

### Group toàn bộ collection:

```javascript
// Thống kê tổng toàn bộ
db.orders.aggregate([
    { $match: { order_status: "PAID" } },
    {
        $group: {
            _id:           null,   // null = group tất cả thành 1
            total_revenue: { $sum: "$final_amount" },
            total_orders:  { $sum: 1 },
            avg_order:     { $avg: "$final_amount" },
            max_order:     { $max: "$final_amount" },
            min_order:     { $min: "$final_amount" }
        }
    }
])
```

### Các Accumulator Operators:


| Operator | Mô tả |
|---|---|
| $sum | Tính tổng |
| $avg | Tính trung bình |
| $min | Giá trị nhỏ nhất |
| $max | Giá trị lớn nhất |
| $count | Đếm (MongoDB 5.0+) |
| $push | Gom vào array |
| $addToSet | Gom vào array (no dup) |
| $first | Lấy giá trị đầu tiên |
| $last | Lấy giá trị cuối cùng |



```javascript
// $push — gom tất cả tags vào array
db.courses.aggregate([
    { $match: { category: "java" } },
    {
        $group: {
            _id:   "$category",
            titles: { $push: "$title" },
            all_tags: { $push: "$tags" }
        }
    }
])

// $addToSet — gom unique values
db.courses.aggregate([
    {
        $group: {
            _id:             null,
            unique_categories: { $addToSet: "$category" }
        }
    }
])
```

## 4\. $project — Định Hình Output

Tương tự `SELECT` trong SQL — chọn fields, đổi tên, tính toán field mới.

```javascript
// Include/exclude fields
db.courses.aggregate([
    {
        $project: {
            title:    1,
            price:    1,
            rating:   1,
            _id:      0   // ẩn _id
        }
    }
])

// Tính field mới
db.courses.aggregate([
    {
        $project: {
            title: 1,
            price: 1,
            // Tính giá sau giảm 20%
            discounted_price: {
                $round: [{ $multiply: ["$price", 0.8] }, 0]
            },
            // Đổi tên field
            course_rating: "$rating",
            // Nối strings
            full_name: {
                $concat: ["$title", " — by ", "$instructor.name"]
            },
            // Đếm số phần tử array
            sections_count: { $size: "$sections" }
        }
    }
])
```

## 5\. $sort, $limit, $skip

```javascript
// $sort — sắp xếp
db.courses.aggregate([
    { $sort: { rating: -1, enrolled_count: -1 } }
])

// $limit — giới hạn kết quả
db.courses.aggregate([
    { $sort:  { rating: -1 } },
    { $limit: 3 }
])

// $skip + $limit — pagination
db.courses.aggregate([
    { $sort:  { created_at: -1 } },
    { $skip:  10 },   // bỏ qua 10 docs đầu
    { $limit: 5 }     // lấy 5 docs tiếp theo
])
```

## 6\. $unwind — Expand Array

Biến mỗi element trong array thành một document riêng.

```javascript
// Trước $unwind:
// { title: "Spring Boot", tags: ["java", "spring", "backend"] }

db.courses.aggregate([
    { $unwind: "$tags" }
])

// Sau $unwind: 3 documents riêng lẻ
// { title: "Spring Boot", tags: "java" }
// { title: "Spring Boot", tags: "spring" }
// { title: "Spring Boot", tags: "backend" }
```

**Use case thực tế — đếm số courses theo tag:**

```javascript
db.courses.aggregate([
    { $match: { course_status: "PUBLISHED" } },
    { $unwind: "$tags" },
    {
        $group: {
            _id:           "$tags",
            course_count:  { $sum: 1 },
            avg_rating:    { $avg: "$rating" }
        }
    },
    { $sort: { course_count: -1 } }
])

// Output:
// { _id: "java",     course_count: 2, avg_rating: 4.7 }
// { _id: "backend",  course_count: 2, avg_rating: 4.6 }
// { _id: "devops",   course_count: 1, avg_rating: 4.7 }
// ...
```

**preserveNullAndEmptyArrays — giữ lại documents có array rỗng:**

```javascript
db.users.aggregate([
    {
        $unwind: {
            path: "$tags",
            preserveNullAndEmptyArrays: true  // không bỏ user có tags: []
        }
    }
])
```

## 7\. $lookup — JOIN Với Collection Khác

Tương tự `LEFT JOIN` trong SQL.

```javascript
// Cú pháp cơ bản
{
    $lookup: {
        from:         "collection_name",  // collection cần join
        localField:   "field_in_current", // field trong collection hiện tại
        foreignField: "field_in_from",    // field trong collection kia
        as:           "output_field_name" // tên field chứa kết quả
    }
}
```

### Ví dụ — Lấy thông tin user kèm orders:

```javascript
db.users.aggregate([
    {
        $lookup: {
            from:         "orders",
            localField:   "_id",
            foreignField: "user_id",
            as:           "orders"
        }
    },
    {
        $project: {
            first_name: 1,
            last_name:  1,
            email:      1,
            order_count: { $size: "$orders" },
            total_spent: { $sum: "$orders.final_amount" }
        }
    }
])
```

### $lookup với pipeline (lookup nâng cao):

```javascript
// Lấy chỉ PAID orders của mỗi user
db.users.aggregate([
    {
        $lookup: {
            from: "orders",
            let:  { userId: "$_id" },  // biến từ collection hiện tại
            pipeline: [
                {
                    $match: {
                        $expr: {
                            $and: [
                                { $eq: ["$user_id", "$$userId"] },
                                { $eq: ["$order_status", "PAID"] }
                            ]
                        }
                    }
                },
                { $sort: { created_at: -1 } }
            ],
            as: "paid_orders"
        }
    },
    {
        $project: {
            email:       1,
            first_name:  1,
            paid_count:  { $size: "$paid_orders" },
            total_spent: { $sum: "$paid_orders.final_amount" }
        }
    }
])
```

### Kết hợp $lookup + $unwind để "flatten" kết quả:

```javascript
// Lấy orders kèm thông tin course trong items
db.orders.aggregate([
    { $match: { order_status: "PAID" } },

    // Unwind items array trước
    { $unwind: "$items" },

    // Lookup course cho từng item
    {
        $lookup: {
            from:         "courses",
            localField:   "items.course_id",
            foreignField: "_id",
            as:           "course_info"
        }
    },
    { $unwind: "$course_info" },

    // Project output
    {
        $project: {
            order_id:     "$_id",
            course_title: "$course_info.title",
            category:     "$course_info.category",
            paid_price:   "$items.price",
            created_at:   1,
            _id:          0
        }
    }
])
```

## 8\. $addFields — Thêm Fields Mới

Thêm fields mới mà không ảnh hưởng fields hiện có (khác với `$project` thay thế output).

```javascript
db.courses.aggregate([
    {
        $addFields: {
            // Giá sau giảm 15%
            sale_price: {
                $round: [{ $multiply: ["$price", 0.85] }, 0]
            },
            // Phân loại popularity
            popularity: {
                $switch: {
                    branches: [
                        { case: { $gte: ["$enrolled_count", 400] }, then: "viral" },
                        { case: { $gte: ["$enrolled_count", 200] }, then: "popular" },
                        { case: { $gte: ["$enrolled_count", 100] }, then: "growing" }
                    ],
                    default: "new"
                }
            },
            // Số sections
            sections_count: { $size: "$sections" }
        }
    },
    {
        $project: {
            title:          1,
            price:          1,
            sale_price:     1,
            popularity:     1,
            sections_count: 1,
            _id:            0
        }
    }
])
```

## 9\. $facet — Nhiều Aggregations Song Song

Chạy nhiều sub-pipeline trên cùng một input — trả về kết quả trong một response.

```javascript
// Dashboard stats: vừa lấy theo category vừa lấy theo price range
db.courses.aggregate([
    { $match: { course_status: "PUBLISHED" } },
    {
        $facet: {
            // Facet 1: Thống kê theo category
            by_category: [
                {
                    $group: {
                        _id:           "$category",
                        course_count:  { $sum: 1 },
                        avg_rating:    { $avg: "$rating" },
                        avg_price:     { $avg: "$price" }
                    }
                },
                { $sort: { course_count: -1 } }
            ],

            // Facet 2: Phân phối theo khoảng giá
            by_price_range: [
                {
                    $bucket: {
                        groupBy:     "$price",
                        boundaries:  [0, 1, 500000, 700000, 1000000],
                        default:     "other",
                        output: {
                            count:     { $sum: 1 },
                            avg_price: { $avg: "$price" }
                        }
                    }
                }
            ],

            // Facet 3: Tổng số
            total: [
                { $count: "count" }
            ]
        }
    }
])

// Output trong 1 document:
// {
//   by_category: [ {...}, {...} ],
//   by_price_range: [ {...}, {...} ],
//   total: [ { count: 6 } ]
// }
```

## 10\. $count — Đếm Documents

```javascript
db.courses.aggregate([
    { $match: { course_status: "PUBLISHED", rating: { $gte: 4.5 } } },
    { $count: "qualified_courses" }
])
// Output: { qualified_courses: 4 }
```

## 11\. Pipeline Thực Chiến — Báo Cáo Doanh Thu [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev)

```javascript
// Báo cáo tổng hợp: top 3 khóa học bán chạy nhất
// kèm doanh thu và thông tin category
db.orders.aggregate([
    // Bước 1: Chỉ lấy đơn hàng đã thanh toán
    { $match: { order_status: "PAID" } },

    // Bước 2: Tách từng item trong order ra
    { $unwind: "$items" },

    // Bước 3: Group theo course_id
    {
        $group: {
            _id:          "$items.course_id",
            course_title: { $first: "$items.title" },
            total_sold:   { $sum: 1 },
            total_revenue: { $sum: "$items.price" },
            avg_price:    { $avg: "$items.price" }
        }
    },

    // Bước 4: Lookup thêm thông tin từ courses collection
    {
        $lookup: {
            from:         "courses",
            localField:   "_id",
            foreignField: "_id",
            as:           "course_detail"
        }
    },
    { $unwind: { path: "$course_detail", preserveNullAndEmptyArrays: true } },

    // Bước 5: Thêm fields từ course_detail
    {
        $addFields: {
            category:       "$course_detail.category",
            current_rating: "$course_detail.rating"
        }
    },

    // Bước 6: Sắp xếp theo doanh thu giảm dần
    { $sort: { total_revenue: -1 } },

    // Bước 7: Chỉ lấy top 3
    { $limit: 3 },

    // Bước 8: Format output
    {
        $project: {
            _id:           0,
            course_title:  1,
            category:      1,
            total_sold:    1,
            total_revenue: 1,
            avg_price:     { $round: ["$avg_price", 0] },
            current_rating: 1
        }
    }
])
```

## 12\. Thực Hành Tổng Hợp

**Bài 1:** Thống kê số khóa học và doanh thu trung bình theo category.

```javascript
db.orders.aggregate([
    { $match: { order_status: "PAID" } },
    { $unwind: "$items" },
    {
        $lookup: {
            from:         "courses",
            localField:   "items.course_id",
            foreignField: "_id",
            as:           "course"
        }
    },
    { $unwind: "$course" },
    {
        $group: {
            _id:          "$course.category",
            total_sold:   { $sum: 1 },
            total_revenue: { $sum: "$items.price" },
            avg_price:    { $avg: "$items.price" }
        }
    },
    {
        $project: {
            category:     "$_id",
            total_sold:   1,
            total_revenue: 1,
            avg_price:    { $round: ["$avg_price", 0] },
            _id:          0
        }
    },
    { $sort: { total_revenue: -1 } }
])
```

**Bài 2:** Tìm top 2 user chi tiêu nhiều nhất, kèm danh sách khóa học đã mua.

```javascript
db.orders.aggregate([
    { $match: { order_status: "PAID" } },
    {
        $group: {
            _id:          "$user_id",
            total_spent:  { $sum: "$final_amount" },
            order_count:  { $sum: 1 },
            courses_bought: {
                $push: {
                    $map: {
                        input: "$items",
                        as:    "item",
                        in:    "$$item.title"
                    }
                }
            }
        }
    },
    { $sort:  { total_spent: -1 } },
    { $limit: 2 },
    {
        $lookup: {
            from:         "users",
            localField:   "_id",
            foreignField: "_id",
            as:           "user_info"
        }
    },
    { $unwind: "$user_info" },
    {
        $project: {
            _id:          0,
            name:         {
                $concat: ["$user_info.first_name", " ", "$user_info.last_name"]
            },
            email:        "$user_info.email",
            total_spent:  1,
            order_count:  1
        }
    }
])
```

## Tổng Kết


| Stage | Tương đương SQL | Dùng khi |
|---|---|---|
| $match | WHERE | Lọc documents sớm để giảm tải |
| $group | GROUP BY + aggregate | Thống kê, tổng hợp |
| $project | SELECT | Chọn/tính toán fields output |
| $sort | ORDER BY | Sắp xếp kết quả |
| $limit | LIMIT | Giới hạn số kết quả |
| $skip | OFFSET | Phân trang |
| $unwind | (không có tương đương) | Flatten array field |
| $lookup | LEFT JOIN | Join với collection khác |
| $addFields | SELECT ..., computed AS | Thêm computed fields |
| $facet | Multiple CTEs | Nhiều aggregations song song |
| $count | COUNT(*) | Đếm số documents |



**Nguyên tắc viết pipeline hiệu quả:**

1.  `$match` **càng sớm càng tốt** để giảm số documents xử lý
    
2.  `$project` để loại fields không cần ngay sau `$match` — giảm memory
    
3.  `$sort` trước `$limit` để kết quả có ý nghĩa
    
4.  Tránh `$unwind` trên array lớn — có thể tạo hàng triệu documents
    

Bài tiếp theo chúng ta sẽ học **Schema Design trong MongoDB** — Embedding vs Referencing, các patterns thực tế như Subset, Bucket, Outlier và khi nào dùng cái nào.

