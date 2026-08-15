# Window Functions: Tính Toán Trên Tập Dữ Liệu Mà Không Mất Dòng

![Window Functions- Tính Toán Trên Tập Dữ Liệu Mà Không Mất Dòng.jpeg](../images/b583c8f5-494a-480f-aa7f-767c89796b6c.jpeg)

Nếu `GROUP BY` là công cụ để **gộp nhiều dòng thành một**, thì **Window Functions** là công cụ để **tính toán trên nhiều dòng nhưng vẫn giữ nguyên từng dòng**. Đây là một trong những tính năng mạnh và thú vị nhất của SQL — khi bạn hiểu được Window Functions, nhiều bài toán tưởng chừng phức tạp sẽ trở nên cực kỳ đơn giản.

## 1\. Vấn đề mà Window Functions giải quyết

Hãy xem bài toán thực tế: **"Lấy danh sách tất cả khóa học kèm xếp hạng của từng khóa theo doanh thu"**

```sql
-- ❌ GROUP BY làm mất thông tin từng dòng
SELECT course_id, SUM(price) AS revenue
FROM order_items
GROUP BY course_id;
-- Chỉ còn course_id và revenue, mất title, price, và các thông tin khác

-- ❌ Subquery lồng nhau — phức tạp
SELECT
    c.title,
    c.price,
    (SELECT COUNT(*) + 1
     FROM (
         SELECT SUM(oi2.price) AS rev
         FROM order_items oi2
         GROUP BY oi2.course_id
         HAVING SUM(oi2.price) > SUM(oi.price)
     ) AS t
    ) AS rank
FROM order_items oi
JOIN courses c ON c.id = oi.course_id
GROUP BY c.id, c.title, c.price;
-- Khó đọc, khó maintain, chậm

-- ✅ Window Function — gọn, rõ, hiệu quả
SELECT
    c.title,
    c.price,
    SUM(oi.price) AS total_revenue,
    RANK() OVER (ORDER BY SUM(oi.price) DESC) AS revenue_rank
FROM order_items oi
JOIN courses c ON c.id = oi.course_id
GROUP BY c.id, c.title, c.price
ORDER BY revenue_rank;
```

## 2\. Cú pháp Window Function

```sql
WINDOW_FUNCTION() OVER (
    PARTITION BY column   -- chia dữ liệu thành các nhóm (tùy chọn)
    ORDER BY column       -- sắp xếp trong mỗi nhóm (tùy chọn)
    ROWS/RANGE BETWEEN ... -- định nghĩa frame (tùy chọn)
)
```

*   **OVER ()** — bắt buộc, khai báo đây là window function
    
*   **PARTITION BY** — chia dữ liệu thành các "cửa sổ" nhỏ, tương tự GROUP BY nhưng không gộp dòng
    
*   **ORDER BY** — sắp xếp dữ liệu trong mỗi cửa sổ
    
*   **ROWS/RANGE BETWEEN** — định nghĩa phạm vi tính toán trong cửa sổ
    

## 3\. Ranking Functions — Xếp hạng

### ROW\_NUMBER() — Số thứ tự duy nhất

Gán số thứ tự liên tiếp, không có số trùng nhau dù có giá trị bằng nhau.

```sql
-- Xếp số thứ tự học viên theo tổng chi tiêu
SELECT
    u.first_name || ' ' || u.last_name AS student_name,
    SUM(o.final_amount)                 AS total_spent,
    ROW_NUMBER() OVER (
        ORDER BY SUM(o.final_amount) DESC
    ) AS row_num
FROM orders o
JOIN users u ON u.id = o.user_id
WHERE o.order_status = 'PAID'
GROUP BY u.id, u.first_name, u.last_name;
```


| student_name | total_spent | row_num |
|---|---|---|
| Nam Nguyen | 1398000 | 1 |
| Linh Tran | 899000 | 2 |
| Huong Pham | 799000 | 3 |
| Minh Le | 599000 | 4 |



### RANK() — Xếp hạng có khoảng trống

Các dòng có giá trị bằng nhau được cùng hạng, nhưng hạng tiếp theo bị nhảy.

```sql
SELECT
    title,
    rating,
    RANK() OVER (ORDER BY rating DESC) AS rank
FROM courses
WHERE course_type = 'PAID';
```


| title | rating | rank |
|---|---|---|
| SQL cho Developer | 4.9 | 1 |
| Spring Boot từ Zero đến Hero | 4.8 | 2 |
| Docker & Kubernetes thực chiến | 4.7 | 3 |
| ReactJS cơ bản đến nâng cao | 4.5 | 4 |



### DENSE\_RANK() — Xếp hạng không có khoảng trống

Giống RANK() nhưng hạng tiếp theo không bị nhảy khi có giá trị bằng nhau.

```sql
-- So sánh RANK vs DENSE_RANK
SELECT
    title,
    rating,
    RANK()       OVER (ORDER BY rating DESC) AS rank,
    DENSE_RANK() OVER (ORDER BY rating DESC) AS dense_rank
FROM courses;
```

Nếu 2 khóa cùng rating 4.8:


| title | rating | rank | dense_rank |
|---|---|---|---|
| Khóa A | 4.9 | 1 | 1 |
| Khóa B | 4.8 | 2 | 2 |
| Khóa C | 4.8 | 2 | 2 |
| Khóa D | 4.5 | 4 | 3 |



### PARTITION BY — Xếp hạng theo nhóm

```sql
-- Xếp hạng khóa học theo doanh thu trong từng nhóm course_type
WITH course_revenue AS (
    SELECT
        c.id,
        c.title,
        c.course_type,
        SUM(oi.price) AS total_revenue
    FROM order_items oi
    JOIN courses c ON c.id = oi.course_id
    JOIN orders  o ON o.id = oi.order_id
    WHERE o.order_status = 'PAID'
    GROUP BY c.id, c.title, c.course_type
)
SELECT
    title,
    course_type,
    total_revenue,
    RANK() OVER (
        PARTITION BY course_type          -- xếp hạng trong từng nhóm
        ORDER BY total_revenue DESC
    ) AS rank_in_type
FROM course_revenue
ORDER BY course_type, rank_in_type;
```

## 4\. LAG và LEAD — So sánh với dòng trước/sau

`LAG()` lấy giá trị từ dòng **trước đó**, `LEAD()` lấy giá trị từ dòng **tiếp theo** — cực kỳ hữu ích khi so sánh dữ liệu theo thời gian.

```sql
LAG(column, offset, default)  OVER (ORDER BY ...)
LEAD(column, offset, default) OVER (ORDER BY ...)
-- offset: số dòng muốn nhìn lùi/tiến (mặc định 1)
-- default: giá trị trả về nếu không có dòng trước/sau (mặc định NULL)
```

**Ví dụ** — So sánh doanh thu tháng này với tháng trước:

```sql
WITH monthly_revenue AS (
    SELECT
        DATE_TRUNC('month', created_at) AS month,
        SUM(final_amount)               AS revenue
    FROM orders
    WHERE order_status = 'PAID'
    GROUP BY DATE_TRUNC('month', created_at)
)
SELECT
    month,
    revenue,
    LAG(revenue) OVER (ORDER BY month) AS prev_month_revenue,
    revenue - LAG(revenue) OVER (ORDER BY month) AS revenue_diff,
    ROUND(
        (revenue - LAG(revenue) OVER (ORDER BY month))
        * 100.0
        / NULLIF(LAG(revenue) OVER (ORDER BY month), 0),
        1
    ) AS growth_pct
FROM monthly_revenue
ORDER BY month;
```


| month | revenue | prev_month_revenue | revenue_diff | growth_pct |
|---|---|---|---|---|
| 2025-01 | 1500000 | NULL | NULL | NULL |
| 2025-02 | 2100000 | 1500000 | 600000 | 40.0 |
| 2025-03 | 1800000 | 2100000 | -300000 | -14.3 |



**Ví dụ khác** — Tính thời gian giữa 2 lần đăng nhập liên tiếp của mỗi user:

```sql
SELECT
    user_id,
    login_time,
    LAG(login_time) OVER (
        PARTITION BY user_id
        ORDER BY login_time
    ) AS prev_login,
    login_time - LAG(login_time) OVER (
        PARTITION BY user_id
        ORDER BY login_time
    ) AS time_between_logins
FROM login_histories
WHERE login_status = 'SUCCESS'
ORDER BY user_id, login_time;
```

## 5\. Aggregate Window Functions — Tổng hợp không gộp dòng

Tất cả aggregate functions (`SUM`, `COUNT`, `AVG`, `MIN`, `MAX`) đều có thể dùng như window function bằng cách thêm `OVER()`:

```sql
-- Tổng doanh thu toàn platform + doanh thu từng khóa học trong cùng một query
SELECT
    c.title,
    SUM(oi.price)                    AS course_revenue,
    SUM(SUM(oi.price)) OVER ()       AS total_platform_revenue,
    ROUND(
        SUM(oi.price) * 100.0
        / SUM(SUM(oi.price)) OVER (),
        1
    )                                AS revenue_share_pct
FROM order_items oi
JOIN courses c ON c.id = oi.course_id
JOIN orders  o ON o.id = oi.order_id
WHERE o.order_status = 'PAID'
GROUP BY c.id, c.title
ORDER BY course_revenue DESC;
```


| title | course_revenue | total_platform_revenue | revenue_share_pct |
|---|---|---|---|
| Docker & Kubernetes | 899000 | 3095000 | 29.1 |
| Spring Boot | 799000 | 3095000 | 25.8 |
| SQL cho Developer | 1198000 | 3095000 | 38.7 |



### Running Total — Tổng tích lũy

```sql
-- Doanh thu tích lũy theo từng tháng (running total)
WITH monthly AS (
    SELECT
        DATE_TRUNC('month', created_at) AS month,
        SUM(final_amount)               AS revenue
    FROM orders
    WHERE order_status = 'PAID'
    GROUP BY DATE_TRUNC('month', created_at)
)
SELECT
    month,
    revenue,
    SUM(revenue) OVER (
        ORDER BY month
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS cumulative_revenue
FROM monthly
ORDER BY month;
```


| month | revenue | cumulative_revenue |
|---|---|---|
| 2025-01 | 1500000 | 1500000 |
| 2025-02 | 2100000 | 3600000 |
| 2025-03 | 1800000 | 5400000 |



### Moving Average — Trung bình động

```sql
-- Doanh thu trung bình 3 tháng gần nhất (rolling average)
WITH monthly AS (
    SELECT
        DATE_TRUNC('month', created_at) AS month,
        SUM(final_amount)               AS revenue
    FROM orders
    WHERE order_status = 'PAID'
    GROUP BY DATE_TRUNC('month', created_at)
)
SELECT
    month,
    revenue,
    ROUND(AVG(revenue) OVER (
        ORDER BY month
        ROWS BETWEEN 2 PRECEDING AND CURRENT ROW  -- 3 tháng gần nhất
    ), 0) AS moving_avg_3m
FROM monthly
ORDER BY month;
```

## 6\. NTILE — Chia dữ liệu thành N nhóm bằng nhau

`NTILE(n)` chia dữ liệu thành n nhóm có kích thước bằng nhau — dùng để phân loại học viên theo mức độ (top 25%, 50%, 75%...).

```sql
-- Phân loại học viên thành 4 nhóm theo tổng chi tiêu
WITH student_spending AS (
    SELECT
        u.first_name || ' ' || u.last_name AS name,
        SUM(o.final_amount)                 AS total_spent
    FROM orders o
    JOIN users u ON u.id = o.user_id
    WHERE o.order_status = 'PAID'
    GROUP BY u.id, u.first_name, u.last_name
)
SELECT
    name,
    total_spent,
    NTILE(4) OVER (ORDER BY total_spent DESC) AS spending_quartile,
    CASE NTILE(4) OVER (ORDER BY total_spent DESC)
        WHEN 1 THEN 'Diamond'
        WHEN 2 THEN 'Gold'
        WHEN 3 THEN 'Silver'
        ELSE        'Bronze'
    END AS tier
FROM student_spending
ORDER BY total_spent DESC;
```

## 7\. FIRST\_VALUE và LAST\_VALUE

Lấy giá trị đầu tiên hoặc cuối cùng trong cửa sổ:

```sql
-- Với mỗi đơn hàng, hiển thị đơn hàng đầu tiên và gần nhất của cùng user
SELECT
    u.first_name,
    o.id           AS order_id,
    o.final_amount,
    o.created_at,
    FIRST_VALUE(o.created_at) OVER (
        PARTITION BY o.user_id
        ORDER BY o.created_at
    ) AS first_order_date,
    LAST_VALUE(o.created_at) OVER (
        PARTITION BY o.user_id
        ORDER BY o.created_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) AS last_order_date
FROM orders o
JOIN users u ON u.id = o.user_id
WHERE o.order_status = 'PAID'
ORDER BY u.id, o.created_at;
```

> **Lưu ý với LAST\_VALUE:** Mặc định frame của window là từ đầu đến dòng hiện tại (`RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`) — nên `LAST_VALUE` thường không cho kết quả mong muốn. Phải thêm `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING` để lấy đúng giá trị cuối cùng.

## 8\. Thực hành tổng hợp

**Bài 1:** Xếp hạng các bài viết theo lượt xem trong từng `post_type`, chỉ lấy top 3 mỗi loại.

```sql
WITH ranked_posts AS (
    SELECT
        title,
        post_type,
        view_count,
        RANK() OVER (
            PARTITION BY post_type
            ORDER BY view_count DESC
        ) AS rank_in_type
    FROM posts
    WHERE post_status = 'PUBLISHED'
)
SELECT title, post_type, view_count, rank_in_type
FROM ranked_posts
WHERE rank_in_type <= 3
ORDER BY post_type, rank_in_type;
```

**Bài 2:** Tính tỷ lệ tăng trưởng số học viên đăng ký mới theo từng tháng.

```sql
WITH monthly_signups AS (
    SELECT
        DATE_TRUNC('month', created_at) AS month,
        COUNT(*)                         AS new_users
    FROM users
    WHERE account_status != 'DELETED'
    GROUP BY DATE_TRUNC('month', created_at)
)
SELECT
    month,
    new_users,
    LAG(new_users) OVER (ORDER BY month) AS prev_month,
    ROUND(
        (new_users - LAG(new_users) OVER (ORDER BY month))
        * 100.0
        / NULLIF(LAG(new_users) OVER (ORDER BY month), 0),
        1
    ) AS growth_pct
FROM monthly_signups
ORDER BY month;
```

**Bài 3:** Với mỗi học viên, tính số ngày kể từ lần đăng nhập thành công gần nhất.

```sql
WITH last_login AS (
    SELECT DISTINCT ON (user_id)
        user_id,
        login_time AS last_login_time
    FROM login_histories
    WHERE login_status = 'SUCCESS'
    ORDER BY user_id, login_time DESC
)
SELECT
    u.first_name || ' ' || u.last_name AS student_name,
    u.email,
    ll.last_login_time,
    EXTRACT(DAY FROM NOW() - ll.last_login_time)::INT AS days_since_login
FROM users u
LEFT JOIN last_login ll ON ll.user_id = u.id
WHERE u.account_status = 'ACTIVE'
ORDER BY days_since_login DESC NULLS LAST;
```

## Tổng kết


| Window Function | Chức năng |
|---|---|
| ROW_NUMBER() | Số thứ tự duy nhất, không trùng |
| RANK() | Xếp hạng, có khoảng trống khi bằng nhau |
| DENSE_RANK() | Xếp hạng, không có khoảng trống |
| NTILE(n) | Chia thành n nhóm bằng nhau |
| LAG(col, n) | Giá trị từ n dòng trước |
| LEAD(col, n) | Giá trị từ n dòng sau |
| FIRST_VALUE() | Giá trị đầu tiên trong cửa sổ |
| LAST_VALUE() | Giá trị cuối cùng trong cửa sổ |
| SUM() OVER() | Tổng tích lũy, tổng theo nhóm |
| AVG() OVER() | Trung bình động |



Bài tiếp theo chúng ta sẽ học **String & Date Functions** — các hàm xử lý chuỗi và thời gian giúp bạn làm sạch, format và tính toán dữ liệu trực tiếp trong SQL mà không cần xử lý ở tầng application.

> **Khác biệt với các RDBMS khác:**
> 
> *   **MySQL:** Hỗ trợ Window Functions từ version 8.0 — trước đó phải dùng biến hoặc subquery phức tạp để giả lập
>     
> *   **SQL Server:** Hỗ trợ đầy đủ, cú pháp giống PostgreSQL — nhưng `ROWS/RANGE BETWEEN` có một số hạn chế nhỏ
>     
> *   **Oracle:** Hỗ trợ đầy đủ và rất mạnh, thêm `KEEP (DENSE_RANK FIRST/LAST ORDER BY ...)` không có ở PostgreSQL
>     

