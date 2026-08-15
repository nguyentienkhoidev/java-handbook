# String & Date Functions: Xử Lý Chuỗi và Thời Gian Trong SQL

![String & Date Functions - Xử Lý Chuỗi và Thời Gian Trong SQL.jpeg](../images/31d01208-03af-47ba-a239-3ffd17a9504a.jpeg)

Trong thực tế, dữ liệu thô từ database thường không ở dạng sẵn sàng để hiển thị — email cần được chuẩn hóa, tên cần ghép lại, ngày tháng cần format theo chuẩn riêng, khoảng cách giữa 2 thời điểm cần tính toán. Thay vì đẩy hết về tầng application, SQL cung cấp bộ hàm phong phú để xử lý ngay tại database — nhanh hơn và gọn hơn nhiều.

## PHẦN 1: STRING FUNCTIONS

## 1\. Nối chuỗi — CONCAT và ||

```sql
-- Dùng || (cách phổ biến trong PostgreSQL)
SELECT first_name || ' ' || last_name AS full_name
FROM users;

-- Dùng CONCAT() — an toàn hơn với NULL (NULL được bỏ qua)
SELECT CONCAT(first_name, ' ', last_name) AS full_name
FROM users;
```

> **Khác biệt:** `||` trả về `NULL` nếu **bất kỳ** toán hạng nào là `NULL`. `CONCAT()` bỏ qua `NULL` và nối các giá trị còn lại.

```sql
-- || với NULL
SELECT 'Hello' || NULL || 'World';  -- Kết quả: NULL

-- CONCAT với NULL
SELECT CONCAT('Hello', NULL, 'World');  -- Kết quả: HelloWorld
```

Thực tế với [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev) — tạo display name từ first\_name và last\_name:

```sql
SELECT
    CONCAT(first_name, ' ', last_name)              AS full_name,
    CONCAT(first_name, ' ', last_name, ' <', email, '>') AS display_with_email
FROM users
WHERE account_status = 'ACTIVE';
```

## 2\. Chuyển đổi hoa thường — UPPER, LOWER

```sql
SELECT
    UPPER(email)      AS email_upper,   -- NAM@GMAIL.COM
    LOWER(email)      AS email_lower,   -- nam@gmail.com
    UPPER(first_name) AS name_upper     -- NAM
FROM users;
```

Dùng để chuẩn hóa khi tìm kiếm không phân biệt hoa thường (thay thế cho `ILIKE`):

```sql
-- Tìm user theo email, không phân biệt hoa thường
SELECT *
FROM users
WHERE LOWER(email) = LOWER('NAM@GMAIL.COM');
```

## 3\. Độ dài chuỗi — LENGTH

```sql
SELECT
    title,
    LENGTH(title)           AS title_length,
    LENGTH(seo_description) AS seo_desc_length
FROM posts
WHERE post_status = 'PUBLISHED';
```

Kiểm tra các bài viết có SEO description quá ngắn (dưới 120 ký tự) hoặc quá dài (trên 160 ký tự):

```sql
SELECT title, seo_description, LENGTH(seo_description) AS desc_length
FROM posts
WHERE post_status = 'PUBLISHED'
  AND (
        LENGTH(seo_description) < 120
     OR LENGTH(seo_description) > 160
     OR seo_description IS NULL
  )
ORDER BY desc_length;
```

## 4\. Cắt chuỗi — SUBSTRING, LEFT, RIGHT

```sql
-- SUBSTRING(string, start, length)
SELECT SUBSTRING('nguyentienkhoi.hashnode.dev', 1, 7);  -- 'foxdev'

-- LEFT(string, n) — lấy n ký tự từ trái
SELECT LEFT(email, 10) AS email_preview FROM users;

-- RIGHT(string, n) — lấy n ký tự từ phải
SELECT RIGHT('Spring Boot từ Zero đến Hero', 4);  -- 'Hero'
```

Ví dụ thực tế — lấy domain từ email:

```sql
SELECT
    email,
    SUBSTRING(email, POSITION('@' IN email) + 1) AS email_domain
FROM users;
-- nam@gmail.com    → gmail.com
-- admin@nguyentienkhoi.hashnode.dev → nguyentienkhoi.hashnode.dev
```

## 5\. Tìm kiếm trong chuỗi — POSITION, STRPOS

```sql
-- POSITION(substring IN string) — trả về vị trí xuất hiện đầu tiên, 0 nếu không tìm thấy
SELECT POSITION('@' IN 'nam@gmail.com');  -- 4

-- STRPOS(string, substring) — tương tự POSITION
SELECT STRPOS('nam@gmail.com', '@');       -- 4
```

## 6\. Thay thế chuỗi — REPLACE

```sql
-- REPLACE(string, from, to)
SELECT REPLACE('Spring Boot từ Zero đến Hero', 'Hero', 'Production');
-- Kết quả: 'Spring Boot từ Zero đến Production'
```

Dùng để làm sạch dữ liệu:

```sql
-- Chuẩn hóa số điện thoại — xóa dấu gạch ngang và dấu cách
SELECT
    phone,
    REPLACE(REPLACE(REPLACE(phone, '-', ''), ' ', ''), '.', '') AS clean_phone
FROM users
WHERE phone IS NOT NULL;
```

## 7\. Xóa khoảng trắng — TRIM, LTRIM, RTRIM

```sql
-- TRIM — xóa khoảng trắng cả 2 đầu
SELECT TRIM('  hello world  ');    -- 'hello world'

-- LTRIM — xóa khoảng trắng bên trái
SELECT LTRIM('  hello world  ');   -- 'hello world  '

-- RTRIM — xóa khoảng trắng bên phải
SELECT RTRIM('  hello world  ');   -- '  hello world'

-- TRIM với ký tự tùy chỉnh
SELECT TRIM(BOTH '-' FROM '---hello---');  -- 'hello'
```

Thực tế — làm sạch dữ liệu user nhập vào:

```sql
UPDATE users
SET first_name = TRIM(first_name),
    last_name  = TRIM(last_name),
    email      = TRIM(LOWER(email))
WHERE first_name != TRIM(first_name)
   OR last_name  != TRIM(last_name);
```

## 8\. Tách chuỗi — SPLIT\_PART

```sql
-- SPLIT_PART(string, delimiter, position)
SELECT SPLIT_PART('nam@gmail.com', '@', 1);  -- 'nam'
SELECT SPLIT_PART('nam@gmail.com', '@', 2);  -- 'gmail.com'

SELECT SPLIT_PART('java-spring-boot', '-', 2);  -- 'spring'
```

## 9\. Đệm chuỗi — LPAD, RPAD

```sql
-- LPAD(string, length, pad_char) — đệm bên trái
SELECT LPAD('42', 6, '0');   -- '000042'

-- RPAD(string, length, pad_char) — đệm bên phải
SELECT RPAD('SQL', 10, '-'); -- 'SQL-------'
```

Tạo mã đơn hàng có định dạng cố định:

```sql
SELECT
    id,
    'ORD-' || LPAD(id::TEXT, 8, '0') AS formatted_order_code
FROM orders;
-- 1   → ORD-00000001
-- 123 → ORD-00000123
```

## 10\. Regexp — Biểu thức chính quy

PostgreSQL hỗ trợ regex mạnh mẽ:

```sql
-- ~ : khớp regex (case sensitive)
-- ~* : khớp regex (case insensitive)
-- !~ : không khớp

-- Tìm user có số điện thoại bắt đầu bằng 09
SELECT first_name, phone
FROM users
WHERE phone ~ '^09';

-- Validate email format
SELECT email
FROM users
WHERE email !~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$';
```

## PHẦN 2: DATE & TIME FUNCTIONS

## 11\. Lấy thời gian hiện tại

```sql
SELECT NOW();               -- 2025-03-15 14:30:00.123456+07  (có timezone)
SELECT CURRENT_TIMESTAMP;   -- Tương tự NOW()
SELECT CURRENT_DATE;        -- 2025-03-15  (chỉ ngày)
SELECT CURRENT_TIME;        -- 14:30:00.123456+07  (chỉ giờ)
```

## 12\. EXTRACT — Lấy thành phần từ ngày giờ

```sql
-- EXTRACT(field FROM source)
SELECT EXTRACT(YEAR   FROM created_at) AS year
FROM orders;

SELECT EXTRACT(MONTH  FROM created_at) AS month   FROM orders;
SELECT EXTRACT(DAY    FROM created_at) AS day      FROM orders;
SELECT EXTRACT(HOUR   FROM created_at) AS hour     FROM orders;
SELECT EXTRACT(DOW    FROM created_at) AS day_of_week  FROM orders;
-- DOW: 0 = Sunday, 1 = Monday, ..., 6 = Saturday
```

Thực tế — thống kê đơn hàng theo giờ trong ngày để biết giờ cao điểm:

```sql
SELECT
    EXTRACT(HOUR FROM created_at)::INT AS hour_of_day,
    COUNT(*)                            AS total_orders,
    SUM(final_amount)                   AS revenue
FROM orders
WHERE order_status = 'PAID'
GROUP BY EXTRACT(HOUR FROM created_at)
ORDER BY hour_of_day;
```

## 13\. DATE\_TRUNC — Làm tròn về đầu kỳ

`DATE_TRUNC` cắt bỏ phần nhỏ hơn đơn vị được chỉ định — rất hay dùng để group theo kỳ:

```sql
-- DATE_TRUNC(precision, timestamp)
SELECT DATE_TRUNC('year',    NOW());  -- 2025-01-01 00:00:00
SELECT DATE_TRUNC('month',   NOW());  -- 2025-03-01 00:00:00
SELECT DATE_TRUNC('week',    NOW());  -- 2025-03-10 00:00:00 (Thứ Hai đầu tuần)
SELECT DATE_TRUNC('day',     NOW());  -- 2025-03-15 00:00:00
SELECT DATE_TRUNC('hour',    NOW());  -- 2025-03-15 14:00:00
```

Thực tế — doanh thu theo tuần:

```sql
SELECT
    DATE_TRUNC('week', created_at) AS week_start,
    COUNT(*)                        AS total_orders,
    SUM(final_amount)               AS weekly_revenue
FROM orders
WHERE order_status = 'PAID'
  AND created_at >= NOW() - INTERVAL '3 months'
GROUP BY DATE_TRUNC('week', created_at)
ORDER BY week_start DESC;
```

## 14\. INTERVAL — Tính toán khoảng thời gian

```sql
-- Cộng/trừ khoảng thời gian
SELECT NOW() + INTERVAL '7 days';    -- 7 ngày sau
SELECT NOW() - INTERVAL '1 month';   -- 1 tháng trước
SELECT NOW() + INTERVAL '2 hours';   -- 2 giờ sau
SELECT NOW() - INTERVAL '30 minutes'; -- 30 phút trước
```

Thực tế — tìm học viên đăng ký trong 7 ngày gần đây:

```sql
SELECT first_name, last_name, email, created_at
FROM users
WHERE created_at >= NOW() - INTERVAL '7 days'
ORDER BY created_at DESC;
```

Tìm các token đã hết hạn:

```sql
SELECT user_id, token_type, expires_at
FROM tokens
WHERE revoked = FALSE
  AND expires_at < NOW();  -- token hết hạn nhưng chưa bị revoke
```

## 15\. AGE — Tính khoảng cách giữa 2 thời điểm

```sql
-- AGE(timestamp1, timestamp2) — trả về khoảng cách dạng interval
SELECT AGE(NOW(), '1990-05-15');
-- Kết quả: 34 years 10 months 5 days

-- AGE(timestamp) — khoảng cách từ hiện tại
SELECT AGE(birth_date) AS age FROM users WHERE birth_date IS NOT NULL;
```

Tính số ngày từ lúc đăng ký đến lúc mua khóa học đầu tiên:

```sql
SELECT
    u.first_name || ' ' || u.last_name AS student_name,
    u.created_at                        AS registered_at,
    MIN(o.created_at)                   AS first_purchase_at,
    EXTRACT(DAY FROM MIN(o.created_at) - u.created_at)::INT
                                        AS days_to_first_purchase
FROM users u
JOIN orders o ON o.user_id = u.id
WHERE o.order_status = 'PAID'
GROUP BY u.id, u.first_name, u.last_name, u.created_at
ORDER BY days_to_first_purchase;
```

## 16\. TO\_CHAR — Format ngày giờ thành chuỗi

```sql
-- TO_CHAR(timestamp, format)
SELECT TO_CHAR(NOW(), 'DD/MM/YYYY');           -- '15/03/2025'
SELECT TO_CHAR(NOW(), 'DD/MM/YYYY HH24:MI');   -- '15/03/2025 14:30'
SELECT TO_CHAR(NOW(), 'Month DD, YYYY');        -- 'March     15, 2025'
SELECT TO_CHAR(NOW(), 'YYYY-MM');              -- '2025-03'
SELECT TO_CHAR(99999999, 'FM999,999,999');     -- '99,999,999' (format số)
```

Thực tế — format doanh thu và ngày tháng cho báo cáo:

```sql
SELECT
    TO_CHAR(DATE_TRUNC('month', created_at), 'MM/YYYY')  AS month,
    TO_CHAR(SUM(final_amount), 'FM999,999,999,999')       AS revenue_formatted
FROM orders
WHERE order_status = 'PAID'
GROUP BY DATE_TRUNC('month', created_at)
ORDER BY DATE_TRUNC('month', created_at) DESC;
```

## 17\. TO\_DATE và TO\_TIMESTAMP — Parse chuỗi thành ngày giờ

```sql
-- TO_DATE(string, format)
SELECT TO_DATE('15/03/2025', 'DD/MM/YYYY');           -- date: 2025-03-15

-- TO_TIMESTAMP(string, format)
SELECT TO_TIMESTAMP('15/03/2025 14:30', 'DD/MM/YYYY HH24:MI');
-- timestamptz: 2025-03-15 14:30:00+07
```

## 18\. Thực hành tổng hợp

**Bài 1:** Tạo báo cáo danh sách học viên với thông tin được format đẹp — họ tên, email domain, ngày đăng ký theo định dạng DD/MM/YYYY, số ngày đã là thành viên.

```sql
SELECT
    CONCAT(first_name, ' ', last_name)              AS full_name,
    SPLIT_PART(email, '@', 2)                        AS email_domain,
    TO_CHAR(created_at, 'DD/MM/YYYY')                AS joined_date,
    EXTRACT(DAY FROM NOW() - created_at)::INT        AS days_as_member
FROM users
WHERE account_status = 'ACTIVE'
ORDER BY created_at;
```

**Bài 2:** Thống kê số bài viết được publish theo từng tháng trong năm 2025, format tháng dạng "Tháng 01/2025".

```sql
SELECT
    'Tháng ' || TO_CHAR(published_at, 'MM/YYYY') AS month_label,
    COUNT(*)                                       AS published_count,
    SUM(view_count)                                AS total_views
FROM posts
WHERE post_status  = 'PUBLISHED'
  AND published_at >= '2025-01-01'
  AND published_at <  '2026-01-01'
GROUP BY DATE_TRUNC('month', published_at), TO_CHAR(published_at, 'MM/YYYY')
ORDER BY DATE_TRUNC('month', published_at);
```

**Bài 3:** Tìm các promotion đang trong thời gian hiệu lực, hiển thị còn bao nhiêu ngày nữa hết hạn.

```sql
SELECT
    name,
    type,
    TO_CHAR(start_at, 'DD/MM/YYYY') AS start_date,
    TO_CHAR(end_at,   'DD/MM/YYYY') AS end_date,
    EXTRACT(DAY FROM end_at - NOW())::INT AS days_remaining
FROM promotions
WHERE status  = 'ACTIVE'
  AND start_at <= NOW()
  AND end_at   >= NOW()
ORDER BY days_remaining;
```

## Tổng kết

### String Functions


| Hàm | Chức năng |
|---|---|
| CONCAT() / || | Nối chuỗi |
| UPPER() / LOWER() | Chuyển hoa/thường |
| LENGTH() | Độ dài chuỗi |
| SUBSTRING(s, start, len) | Cắt chuỗi |
| LEFT() / RIGHT() | Lấy n ký tự trái/phải |
| POSITION() / STRPOS() | Tìm vị trí chuỗi con |
| REPLACE() | Thay thế chuỗi |
| TRIM() / LTRIM() / RTRIM() | Xóa khoảng trắng |
| SPLIT_PART() | Tách chuỗi theo delimiter |
| LPAD() / RPAD() | Đệm chuỗi |



### Date & Time Functions


| Hàm | Chức năng |
|---|---|
| NOW() / CURRENT_TIMESTAMP | Thời gian hiện tại |
| CURRENT_DATE | Ngày hiện tại |
| EXTRACT(field FROM ts) | Lấy thành phần ngày giờ |
| DATE_TRUNC(precision, ts) | Làm tròn về đầu kỳ |
| INTERVAL | Khoảng thời gian |
| AGE(ts1, ts2) | Khoảng cách 2 thời điểm |
| TO_CHAR(ts, format) | Format ngày giờ thành chuỗi |
| TO_DATE() / TO_TIMESTAMP() | Parse chuỗi thành ngày giờ |



Bài tiếp theo chúng ta sẽ học **CASE WHEN** — công cụ để viết logic điều kiện ngay trong SQL, từ phân loại dữ liệu đến pivot bảng.

> **Khác biệt với các RDBMS khác:**
> 
> *   **MySQL:** Dùng `CONCAT()` thay `||`, `DATE_FORMAT()` thay `TO_CHAR()`, `DATEDIFF()` thay `AGE()`, `DATE_ADD()` / `DATE_SUB()` thay `INTERVAL`
>     
> *   **SQL Server:** Dùng `+` để nối chuỗi, `FORMAT()` thay `TO_CHAR()`, `DATEDIFF()` và `DATEADD()` để tính khoảng thời gian, `DATEPART()` thay `EXTRACT()`
>     
> *   **Oracle:** Dùng `||` như PostgreSQL, `TO_CHAR()` giống hệt, `SYSDATE` thay `NOW()`, `MONTHS_BETWEEN()` để tính khoảng cách tháng
>     

