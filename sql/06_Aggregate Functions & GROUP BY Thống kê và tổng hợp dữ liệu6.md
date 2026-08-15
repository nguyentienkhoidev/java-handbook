# Aggregate Functions & GROUP BY: Thống kê và tổng hợp dữ liệu

![Aggregate Functions & GROUP BY- Thống kê và tổng hợp dữ liệu.jpeg](../images/ca8ba665-f5ef-4e8a-94dd-b09b79839432.jpeg)

Cho đến bây giờ các query của chúng ta đều trả về từng dòng dữ liệu riêng lẻ. Bài này sẽ học cách **tổng hợp nhiều dòng thành một kết quả** — đếm số học viên, tính tổng doanh thu, tìm khóa học có rating cao nhất... Đây là nền tảng để viết mọi loại báo cáo trong thực tế.

## 1\. Aggregate Functions là gì?

**Aggregate Functions** (hàm tổng hợp) nhận vào nhiều dòng và trả về **một giá trị duy nhất**:


| Hàm | Chức năng | Ví dụ kết quả |
|---|---|---|
| COUNT() | Đếm số dòng | 5 |
| SUM() | Tổng các giá trị | 2997000 |
| AVG() | Giá trị trung bình | 599400 |
| MIN() | Giá trị nhỏ nhất | 0 |
| MAX() | Giá trị lớn nhất | 899000 |



## 2\. COUNT — Đếm số dòng

```sql
-- Đếm tổng số user
SELECT COUNT(*) AS total_users
FROM users;
```


| total_users |
|---|
| 5 |



```sql
-- Đếm số user đang ACTIVE
SELECT COUNT(*) AS active_users
FROM users
WHERE account_status = 'ACTIVE';
```

**Sự khác biệt giữa COUNT(\*) và COUNT(column):**

```sql
-- COUNT(*): đếm tất cả dòng, kể cả dòng có NULL
SELECT COUNT(*) FROM users;

-- COUNT(column): chỉ đếm dòng có giá trị khác NULL ở cột đó
SELECT COUNT(phone) FROM users;  -- Bỏ qua các user chưa có phone
```

Thực tế với [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev):

```sql
-- Bao nhiêu user đã xác thực email?
SELECT
    COUNT(*)                                    AS total_users,
    COUNT(*) FILTER (WHERE email_verified = TRUE)  AS verified_users,
    COUNT(*) FILTER (WHERE email_verified = FALSE) AS unverified_users
FROM users;
```

> `FILTER (WHERE ...)` là cú pháp PostgreSQL cho phép đếm có điều kiện ngay trong aggregate — rất tiện để tổng hợp nhiều metric trong một query.

## 3\. SUM — Tính tổng

```sql
-- Tổng doanh thu từ các đơn hàng đã thanh toán
SELECT SUM(final_amount) AS total_revenue
FROM orders
WHERE order_status = 'PAID';
```

```sql
-- Tổng số học viên đã enroll trên toàn platform
SELECT SUM(enrolled_count) AS total_enrollments
FROM courses;
```

## 4\. AVG — Tính trung bình

```sql
-- Rating trung bình của tất cả khóa học
SELECT ROUND(AVG(rating), 2) AS avg_rating
FROM courses
WHERE course_type = 'PAID';
```

```sql
-- Giá trung bình các đơn hàng đã thanh toán
SELECT ROUND(AVG(final_amount), 0) AS avg_order_value
FROM orders
WHERE order_status = 'PAID';
```

> `AVG` tự động bỏ qua các giá trị `NULL` — chỉ tính trung bình trên các dòng có giá trị thực.

## 5\. MIN và MAX — Giá trị nhỏ nhất và lớn nhất

```sql
-- Khóa học rẻ nhất và đắt nhất
SELECT
    MIN(price) AS cheapest,
    MAX(price) AS most_expensive
FROM courses
WHERE course_type = 'PAID';
```


| cheapest | most_expensive |
|---|---|
| 599000 | 899000 |



```sql
-- Đơn hàng đầu tiên và gần nhất của platform
SELECT
    MIN(created_at) AS first_order,
    MAX(created_at) AS latest_order
FROM orders
WHERE order_status = 'PAID';
```

## 6\. GROUP BY — Tổng hợp theo nhóm

Cho đến giờ các aggregate function trên trả về **một dòng duy nhất** cho toàn bộ bảng. `GROUP BY` cho phép bạn tổng hợp **theo từng nhóm**:

```sql
SELECT column_nhom, AGG_FUNC(column_tinh)
FROM table
GROUP BY column_nhom;
```

**Ví dụ 1** — Đếm số đơn hàng theo từng trạng thái:

```sql
SELECT
    order_status,
    COUNT(*)         AS total_orders,
    SUM(final_amount) AS total_amount
FROM orders
GROUP BY order_status
ORDER BY total_orders DESC;
```

Kết quả:


| order_status | total_orders | total_amount |
|---|---|---|
| PAID | 4 | 3087000 |
| CANCELLED | 1 | 699000 |
| PENDING | 1 | 899000 |



**Ví dụ 2** — Doanh thu theo từng loại khóa học:

```sql
SELECT
    c.course_type,
    COUNT(oi.id)          AS total_sold,
    SUM(oi.price)         AS total_revenue,
    ROUND(AVG(oi.price), 0) AS avg_price
FROM order_items oi
JOIN courses c ON c.id = oi.course_id
JOIN orders  o ON o.id = oi.order_id
WHERE o.order_status = 'PAID'
GROUP BY c.course_type
ORDER BY total_revenue DESC;
```

**Ví dụ 3** — Số học viên đăng ký theo từng khóa học:

```sql
SELECT
    c.title,
    COUNT(e.user_id) AS total_enrolled
FROM enrollments e
JOIN courses c ON c.id = e.course_id
GROUP BY c.title
ORDER BY total_enrolled DESC;
```

### GROUP BY nhiều cột

Bạn có thể group theo nhiều cột cùng lúc — mỗi **tổ hợp duy nhất** của các cột đó tạo thành một nhóm:

```sql
-- Doanh thu theo từng loại khóa học và từng tháng
SELECT
    DATE_TRUNC('month', o.created_at) AS month,
    c.course_type,
    COUNT(*)                           AS total_orders,
    SUM(o.final_amount)                AS revenue
FROM orders o
JOIN order_items oi ON oi.order_id = o.id
JOIN courses     c  ON c.id = oi.course_id
WHERE o.order_status = 'PAID'
GROUP BY DATE_TRUNC('month', o.created_at), c.course_type
ORDER BY month DESC, revenue DESC;
```

## 7\. HAVING — Lọc sau khi GROUP BY

`WHERE` lọc dữ liệu **trước khi** group. `HAVING` lọc **sau khi** đã group — dùng để lọc theo kết quả của aggregate function:

```sql
SELECT column_nhom, AGG_FUNC(column) AS ket_qua
FROM table
GROUP BY column_nhom
HAVING AGG_FUNC(column) > gia_tri;
```

**Ví dụ 1** — Chỉ lấy các khóa học có hơn 200 học viên đăng ký:

```sql
SELECT
    c.title,
    COUNT(e.user_id) AS total_enrolled
FROM enrollments e
JOIN courses c ON c.id = e.course_id
GROUP BY c.title
HAVING COUNT(e.user_id) > 2
ORDER BY total_enrolled DESC;
```

**Ví dụ 2** — Tìm các user có tổng chi tiêu trên 1.000.000đ:

```sql
SELECT
    u.first_name,
    u.last_name,
    u.email,
    COUNT(o.id)        AS total_orders,
    SUM(o.final_amount) AS total_spent
FROM orders o
JOIN users u ON u.id = o.user_id
WHERE o.order_status = 'PAID'
GROUP BY u.id, u.first_name, u.last_name, u.email
HAVING SUM(o.final_amount) > 1000000
ORDER BY total_spent DESC;
```

### WHERE vs HAVING — Khi nào dùng cái nào?

```sql
SELECT
    order_status,
    COUNT(*)          AS total,
    SUM(final_amount) AS revenue
FROM orders
WHERE created_at >= '2025-01-01'    -- WHERE: lọc TRƯỚC khi group (trên từng dòng)
GROUP BY order_status
HAVING COUNT(*) > 1;                -- HAVING: lọc SAU khi group (trên kết quả tổng hợp)
```


| Tiêu chí | WHERE | HAVING |
|---|---|---|
| Chạy khi nào | Trước GROUP BY | Sau GROUP BY |
| Lọc trên | Từng dòng dữ liệu | Kết quả đã tổng hợp |
| Dùng aggregate? | ❌ Không được | ✅ Được |
| Hiệu năng | Tốt hơn (ít dòng hơn phải group) | Chậm hơn nếu lọc thứ có thể đưa vào WHERE |



> **Nguyên tắc:** Bất cứ điều kiện nào có thể đưa vào `WHERE` thì đưa vào `WHERE` — để giảm số dòng cần xử lý trước khi group, giúp query nhanh hơn.

## 8\. Thứ tự thực thi đầy đủ

Kết hợp với những gì đã học, thứ tự thực thi hoàn chỉnh của một câu SELECT:

```sql
SELECT   order_status, COUNT(*), SUM(final_amount)  -- 6. Chọn cột
FROM     orders                                      -- 1. Xác định bảng
WHERE    created_at >= '2025-01-01'                  -- 2. Lọc dòng
GROUP BY order_status                                -- 3. Nhóm dữ liệu
HAVING   COUNT(*) > 1                               -- 4. Lọc nhóm
ORDER BY SUM(final_amount) DESC                      -- 5. Sắp xếp
LIMIT    5;                                          -- 7. Giới hạn kết quả
```

## 9\. Thực hành tổng hợp

**Bài 1:** Thống kê số bài viết theo từng `post_type`, chỉ lấy các loại có từ 2 bài trở lên.

```sql
SELECT
    post_type,
    COUNT(*) AS total_posts
FROM posts
WHERE post_status = 'PUBLISHED'
GROUP BY post_type
HAVING COUNT(*) >= 2
ORDER BY total_posts DESC;
```

**Bài 2:** Tính tổng doanh thu, số đơn hàng và giá trị đơn trung bình theo từng tháng trong năm 2025.

```sql
SELECT
    DATE_TRUNC('month', created_at)  AS month,
    COUNT(*)                          AS total_orders,
    SUM(final_amount)                 AS total_revenue,
    ROUND(AVG(final_amount), 0)       AS avg_order_value
FROM orders
WHERE order_status = 'PAID'
  AND created_at >= '2025-01-01'
  AND created_at <  '2026-01-01'
GROUP BY DATE_TRUNC('month', created_at)
ORDER BY month ASC;
```

**Bài 3:** Tìm top 3 khóa học có tổng doanh thu cao nhất.

```sql
SELECT
    c.title,
    COUNT(oi.id)    AS times_sold,
    SUM(oi.price)   AS total_revenue
FROM order_items oi
JOIN courses c ON c.id = oi.course_id
JOIN orders  o ON o.id = oi.order_id
WHERE o.order_status = 'PAID'
GROUP BY c.id, c.title
ORDER BY total_revenue DESC
LIMIT 3;
```

## Tổng kết


| Keyword | Chức năng |
|---|---|
| COUNT(*) | Đếm tất cả dòng |
| COUNT(col) | Đếm dòng có giá trị khác NULL |
| SUM(col) | Tổng giá trị |
| AVG(col) | Trung bình — bỏ qua NULL |
| MIN(col) | Giá trị nhỏ nhất |
| MAX(col) | Giá trị lớn nhất |
| GROUP BY | Tổng hợp theo nhóm |
| HAVING | Lọc sau khi GROUP BY |
| FILTER (WHERE ...) | Aggregate có điều kiện (PostgreSQL) |



Bài tiếp theo chúng ta sẽ học **JOIN** — cách kết hợp dữ liệu từ nhiều bảng, hoàn thiện bộ công cụ cơ bản để xử lý hầu hết các bài toán thực tế với SQL.

> **Khác biệt với các RDBMS khác:**
> 
> *   **MySQL / SQL Server / Oracle:** Không hỗ trợ `FILTER (WHERE ...)` trong aggregate — phải dùng `SUM(CASE WHEN condition THEN 1 ELSE 0 END)` thay thế
>     
> *   **MySQL:** `ONLY_FULL_GROUP_BY` mode (mặc định từ 5.7) yêu cầu tất cả cột trong SELECT phải nằm trong GROUP BY hoặc là aggregate function — giống PostgreSQL
>     
> *   **SQL Server:** Dùng `TOP` thay `LIMIT` — `SELECT TOP 3 ...`
>     

