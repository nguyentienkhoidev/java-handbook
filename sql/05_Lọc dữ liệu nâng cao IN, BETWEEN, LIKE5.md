# Lọc dữ liệu nâng cao: IN, BETWEEN, LIKE

![Lọc dữ liệu nâng cao- IN, BETWEEN, LIKE.jpeg](../images/a03e04e0-3510-4da6-9375-5c6672f17de9.jpeg)

  
Ở bài 3 bạn đã biết dùng `WHERE` với các toán tử so sánh cơ bản. Bài này sẽ mở rộng thêm với `IN`, `BETWEEN`, `LIKE` và `NOT` — những công cụ giúp bạn viết điều kiện lọc ngắn gọn, rõ ràng và hiệu quả hơn nhiều trong thực tế.

## 1\. IN — Lọc theo danh sách giá trị

Thay vì viết nhiều điều kiện `OR` liên tiếp, `IN` cho phép bạn kiểm tra một cột có thuộc danh sách giá trị hay không:

```sql
-- ❌ Dài dòng với OR
SELECT title, order_status
FROM orders
WHERE order_status = 'PAID'
   OR order_status = 'PENDING'
   OR order_status = 'CANCELLED';

-- ✅ Gọn hơn với IN
SELECT id, user_id, order_status, final_amount
FROM orders
WHERE order_status IN ('PAID', 'PENDING', 'CANCELLED');
```

**Ví dụ thực tế** — Lấy các bài viết thuộc nhiều loại:

```sql
SELECT title, post_type, post_status, published_at
FROM posts
WHERE post_type IN ('TECHNOLOGY', 'GUIDE', 'CASE_STUDY')
  AND post_status = 'PUBLISHED';
```

**Ví dụ** — Lấy các khóa học theo nhiều danh mục:

```sql
SELECT title, course_type, price
FROM courses
WHERE course_type IN ('PAID', 'CERTIFICATION')
ORDER BY price DESC;
```

### NOT IN — Loại trừ danh sách giá trị

```sql
-- Lấy tất cả đơn hàng trừ các đơn đã hủy và đang pending
SELECT id, user_id, final_amount, order_status
FROM orders
WHERE order_status NOT IN ('CANCELLED', 'PENDING');
```

> **Cảnh báo với NOT IN và NULL:** Nếu danh sách trong `NOT IN` chứa bất kỳ giá trị `NULL` nào, toàn bộ query sẽ trả về 0 dòng. Lý do: `value NOT IN (..., NULL)` tương đương `value != NULL` — mà bất kỳ phép so sánh nào với NULL đều trả về NULL, không phải TRUE hay FALSE.

```sql
-- ❌ Nguy hiểm nếu subquery trả về NULL
SELECT * FROM users
WHERE id NOT IN (SELECT user_id FROM orders);
-- Nếu có bất kỳ orders.user_id nào là NULL → trả về 0 dòng!

-- ✅ An toàn hơn — dùng NOT EXISTS thay thế (sẽ học ở bài Subquery)
SELECT * FROM users u
WHERE NOT EXISTS (
    SELECT 1 FROM orders o WHERE o.user_id = u.id
);
```

## 2\. BETWEEN — Lọc trong khoảng giá trị

`BETWEEN a AND b` kiểm tra giá trị có nằm trong khoảng từ `a` đến `b` — **bao gồm cả 2 đầu mút**:

```sql
SELECT column
FROM table
WHERE column BETWEEN gia_tri_min AND gia_tri_max;
```

**Ví dụ 1** — Lấy khóa học có giá từ 500.000đ đến 800.000đ:

```sql
SELECT title, price, rating
FROM courses
WHERE price BETWEEN 500000 AND 800000
ORDER BY price ASC;
```

Kết quả:


| title | price | rating |
|---|---|---|
| SQL cho Developer | 599000 | 4.9 |
| ReactJS cơ bản đến nâng cao | 699000 | 4.5 |
| Spring Boot từ Zero đến Hero | 799000 | 4.8 |



**Ví dụ 2** — Lấy các đơn hàng được tạo trong tháng 1 năm 2025:

```sql
SELECT id, user_id, final_amount, created_at
FROM orders
WHERE created_at BETWEEN '2025-01-01 00:00:00' AND '2025-01-31 23:59:59'
ORDER BY created_at DESC;
```

> **Lưu ý với TIMESTAMPTZ:** Cách trên có thể bỏ sót các bản ghi tạo lúc `2025-01-31 23:59:59.999`. Cách chuẩn hơn là dùng `>=` và `<`:

```sql
-- ✅ Cách chuẩn hơn khi lọc theo khoảng thời gian
SELECT id, user_id, final_amount, created_at
FROM orders
WHERE created_at >= '2025-01-01'
  AND created_at <  '2025-02-01';
```

**Ví dụ 3** — Lấy các khóa học có rating từ 4.7 đến 5.0:

```sql
SELECT title, rating, enrolled_count
FROM courses
WHERE rating BETWEEN 4.7 AND 5.0
ORDER BY rating DESC;
```

### NOT BETWEEN — Ngoài khoảng giá trị

```sql
-- Lấy khóa học có giá dưới 500.000đ hoặc trên 800.000đ
SELECT title, price
FROM courses
WHERE price NOT BETWEEN 500000 AND 800000;
```

## 3\. LIKE — Tìm kiếm theo pattern

`LIKE` dùng để tìm kiếm chuỗi theo mẫu (pattern matching) với 2 ký tự đặc biệt:


| Ký tự | Ý nghĩa | Ví dụ |
|---|---|---|
| % | Khớp với 0 hoặc nhiều ký tự bất kỳ | '%boot%' khớp với "Spring Boot", "Bootstrap" |
| _ | Khớp với đúng 1 ký tự bất kỳ | '_n' khớp với "an", "in", "on" |



```sql
SELECT title
FROM courses
WHERE title LIKE pattern;
```

**Ví dụ 1** — Tìm khóa học có chữ "Boot" trong tiêu đề:

```sql
SELECT title, price
FROM courses
WHERE title LIKE '%Boot%';
```

Kết quả:


| title | price |
|---|---|
| Spring Boot từ Zero đến Hero | 799000 |



**Ví dụ 2** — Tìm user có email kết thúc bằng `@gmail.com`:

```sql
SELECT first_name, last_name, email
FROM users
WHERE email LIKE '%@gmail.com';
```

**Ví dụ 3** — Tìm bài viết có slug bắt đầu bằng `sql-`:

```sql
SELECT title, slug, published_at
FROM posts
WHERE slug LIKE 'sql-%'
  AND post_status = 'PUBLISHED';
```

**Ví dụ 4** — Tìm user có username đúng 6 ký tự:

```sql
SELECT username, email
FROM users
WHERE username LIKE '______';  -- 6 dấu gạch dưới
```

### ILIKE — Tìm kiếm không phân biệt hoa thường (PostgreSQL)

```sql
-- LIKE phân biệt hoa thường
SELECT * FROM courses WHERE title LIKE '%spring boot%';  -- Không tìm thấy vì "Spring Boot" viết hoa

-- ILIKE không phân biệt hoa thường
SELECT * FROM courses WHERE title ILIKE '%spring boot%';  -- Tìm thấy "Spring Boot"
```

### NOT LIKE — Loại trừ pattern

```sql
-- Tìm user không dùng Gmail
SELECT first_name, email
FROM users
WHERE email NOT LIKE '%@gmail.com';
```

> **Lưu ý hiệu năng:** `LIKE '%keyword%'` (có `%` ở đầu) không thể tận dụng index thông thường, dẫn đến full table scan khi bảng lớn. Nếu cần tìm kiếm full-text trong production, hãy dùng PostgreSQL Full-Text Search hoặc Elasticsearch — FoxDev sẽ đề cập ở bài Advanced.

## 4\. Kết hợp IN, BETWEEN, LIKE với AND/OR

Trong thực tế, bạn thường kết hợp nhiều điều kiện với nhau:

**Ví dụ 1** — Tìm các bài viết PUBLISHED thuộc loại TECHNOLOGY hoặc GUIDE, có từ "SQL" trong tiêu đề:

```sql
SELECT title, post_type, published_at
FROM posts
WHERE post_status = 'PUBLISHED'
  AND post_type IN ('TECHNOLOGY', 'GUIDE')
  AND title ILIKE '%SQL%'
ORDER BY published_at DESC;
```

**Ví dụ 2** — Tìm các đơn hàng PAID trong Q1 2025 có giá trị từ 500.000đ trở lên:

```sql
SELECT id, user_id, final_amount, created_at
FROM orders
WHERE order_status = 'PAID'
  AND created_at >= '2025-01-01'
  AND created_at <  '2025-04-01'
  AND final_amount >= 500000
ORDER BY final_amount DESC;
```

**Ví dụ 3** — Tìm user ACTIVE có email Gmail hoặc chưa xác thực email:

```sql
SELECT first_name, last_name, email, email_verified
FROM users
WHERE account_status = 'ACTIVE'
  AND (
        email LIKE '%@gmail.com'
     OR email_verified = FALSE
      );
```

## 5\. Thực hành tổng hợp

Hãy tự viết các query sau và kiểm tra kết quả trên database thực hành:

**Bài 1:** Lấy danh sách các khóa học có giá từ 600.000đ đến 900.000đ, sắp xếp theo rating giảm dần.

```sql
SELECT title, price, rating
FROM courses
WHERE price BETWEEN 600000 AND 900000
ORDER BY rating DESC;
```

**Bài 2:** Tìm tất cả user có email chứa từ "nguyen" (không phân biệt hoa thường).

```sql
SELECT first_name, last_name, email
FROM users
WHERE email ILIKE '%nguyen%';
```

**Bài 3:** Lấy các đơn hàng có trạng thái PAID hoặc PARTIALLY\_PAID, tạo trong năm 2025.

```sql
SELECT id, user_id, order_status, final_amount, created_at
FROM orders
WHERE order_status IN ('PAID', 'PARTIALLY_PAID')
  AND created_at >= '2025-01-01'
  AND created_at <  '2026-01-01'
ORDER BY created_at DESC;
```

**Bài 4:** Tìm các bài viết có slug không bắt đầu bằng `sql-` và đang PUBLISHED.

```sql
SELECT title, slug, post_type
FROM posts
WHERE slug NOT LIKE 'sql-%'
  AND post_status = 'PUBLISHED';
```

## Tổng kết


| Keyword | Cú pháp | Dùng khi nào |
|---|---|---|
| IN | col IN (a, b, c) | Lọc theo danh sách giá trị cố định |
| NOT IN | col NOT IN (a, b, c) | Loại trừ danh sách — cẩn thận với NULL |
| BETWEEN | col BETWEEN a AND b | Lọc trong khoảng — bao gồm 2 đầu mút |
| NOT BETWEEN | col NOT BETWEEN a AND b | Ngoài khoảng giá trị |
| LIKE | col LIKE 'pattern' | Tìm kiếm chuỗi theo mẫu, phân biệt hoa thường |
| ILIKE | col ILIKE 'pattern' | Tìm kiếm chuỗi, không phân biệt hoa thường (PostgreSQL) |
| NOT LIKE | col NOT LIKE 'pattern' | Loại trừ theo pattern |



Bài tiếp theo chúng ta sẽ học **Aggregate Functions và GROUP BY** — công cụ để thống kê, tổng hợp dữ liệu theo nhóm, từ đó viết được những báo cáo thực tế như doanh thu theo tháng hay số học viên theo khóa học.

> **Khác biệt với các RDBMS khác:**
> 
> *   **MySQL:** Không có `ILIKE` — dùng `LIKE` kết hợp với collation `utf8_general_ci` để không phân biệt hoa thường, hoặc dùng `LOWER(col) LIKE LOWER(pattern)`
>     
> *   **SQL Server:** Không có `ILIKE` — dùng `LIKE` với collation case-insensitive hoặc `LOWER()`
>     
> *   **SQL Server:** Hỗ trợ thêm ký tự `[abc]` và `[^abc]` trong LIKE pattern để match một ký tự trong tập hợp
>     
> *   **Oracle:** Tương tự MySQL — không có `ILIKE`, dùng `UPPER()` hoặc `LOWER()` để so sánh không phân biệt hoa thường
>     

