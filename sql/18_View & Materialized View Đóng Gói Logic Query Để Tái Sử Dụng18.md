# View & Materialized View: Đóng Gói Logic Query Để Tái Sử Dụng

![View & Materialized View Đóng Gói Logic Query Để Tái Sử Dụng.jpeg](../images/aff21d81-a207-4f35-9a5c-5713b3e3fe57.jpeg)

Khi một query phức tạp JOIN 5 bảng được dùng ở nhiều nơi trong application — dashboard, API, báo cáo — copy-paste nó khắp nơi là cách làm tệ nhất. Thay đổi logic một chỗ phải nhớ sửa ở 10 chỗ. **View** giải quyết vấn đề này bằng cách đóng gói query thành một "bảng ảo" có tên, tái sử dụng được và ẩn đi độ phức tạp bên dưới.

## 1\. View là gì?

**View** là một câu query được lưu lại với một cái tên — khi query vào view, PostgreSQL thực thi câu query gốc phía sau.

```sql
-- Tạo view
CREATE VIEW ten_view AS
SELECT ...
FROM ...
WHERE ...;

-- Dùng view như một bảng thông thường
SELECT * FROM ten_view;
SELECT * FROM ten_view WHERE column = 'value';
```

View **không lưu dữ liệu** — mỗi lần query vào view là một lần chạy câu query gốc.

## 2\. Tạo View Cơ Bản

**Ví dụ 1** — View danh sách khóa học đang bán với đầy đủ thông tin:

```sql
CREATE VIEW v_published_courses AS
SELECT
    c.id,
    c.title,
    c.slug,
    c.course_type,
    c.course_mode,
    c.enrolled_count,
    c.rating,
    c.total_lectures,
    c.duration,
    cp.price,
    cp.currency,
    cat.name         AS category_name,
    cat.slug         AS category_slug,
    u.first_name || ' ' || u.last_name AS instructor_name
FROM courses c
JOIN categories  cat ON cat.id = c.category_id
JOIN users       u   ON u.id   = c.maker_id
LEFT JOIN course_pricing cp
       ON cp.course_id = c.id
      AND cp.is_active = TRUE
      AND cp.currency  = 'VND'
WHERE c.course_status = 'PUBLISHED';
```

Sau đó dùng view đơn giản như bảng thường:

```sql
-- Lấy khóa học theo category
SELECT title, price, rating, instructor_name
FROM v_published_courses
WHERE category_slug = 'sql'
ORDER BY rating DESC;

-- Đếm số khóa học theo loại
SELECT course_type, COUNT(*) AS total
FROM v_published_courses
GROUP BY course_type;
```

**Ví dụ 2** — View thống kê học viên:

```sql
CREATE VIEW v_student_summary AS
SELECT
    u.id,
    u.email,
    u.first_name || ' ' || u.last_name     AS full_name,
    u.account_status,
    u.created_at                            AS registered_at,
    COUNT(DISTINCT e.course_id)             AS enrolled_courses,
    COUNT(DISTINCT ucc.course_id)           AS completed_courses,
    COALESCE(SUM(o.final_amount), 0)        AS total_spent,
    COUNT(DISTINCT o.id)                    AS total_orders,
    MAX(o.created_at)                       AS last_order_at,
    up.point_balance
FROM users u
LEFT JOIN enrollments          e   ON e.user_id   = u.id
LEFT JOIN user_course_certificates ucc ON ucc.student_id = u.id
LEFT JOIN orders               o   ON o.user_id   = u.id
                                    AND o.order_status = 'PAID'
LEFT JOIN user_points          up  ON up.user_id  = u.id
GROUP BY u.id, u.email, u.first_name, u.last_name,
         u.account_status, u.created_at, up.point_balance;
```

```sql
-- Dùng view để tìm top học viên tích cực
SELECT full_name, email, enrolled_courses, total_spent, point_balance
FROM v_student_summary
WHERE account_status = 'ACTIVE'
ORDER BY total_spent DESC
LIMIT 10;
```

## 3\. Sửa và Xóa View

```sql
-- Sửa view — CREATE OR REPLACE thay thế definition cũ
CREATE OR REPLACE VIEW v_published_courses AS
SELECT
    c.id,
    c.title,
    c.slug,
    c.rating,
    c.enrolled_count
    -- thêm hoặc bớt cột tùy ý
FROM courses c
WHERE c.course_status = 'PUBLISHED';

-- Xóa view
DROP VIEW v_published_courses;

-- Xóa view + các view phụ thuộc vào nó
DROP VIEW v_published_courses CASCADE;
```

## 4\. Updatable View — Ghi Dữ Liệu Qua View

Trong một số trường hợp, bạn có thể INSERT/UPDATE/DELETE trực tiếp qua view:

```sql
-- View đơn giản (một bảng, không có GROUP BY, DISTINCT, JOIN)
CREATE VIEW v_active_users AS
SELECT id, email, first_name, last_name, account_status
FROM users
WHERE account_status = 'ACTIVE'
WITH CHECK OPTION;  -- đảm bảo dữ liệu ghi vào vẫn thỏa điều kiện view

-- Update qua view
UPDATE v_active_users
SET first_name = 'Nguyen Van'
WHERE id = 1;

-- WITH CHECK OPTION ngăn insert dữ liệu vi phạm điều kiện view
INSERT INTO v_active_users (email, first_name, last_name, account_status)
VALUES ('test@gmail.com', 'Test', 'User', 'INACTIVE');
-- ERROR: new row violates check option for view "v_active_users"
```

> **Thực tế:** View phức tạp (có JOIN, GROUP BY, DISTINCT) không updatable. Với những view này cần dùng **INSTEAD OF Trigger** nếu muốn ghi dữ liệu qua view.

## 5\. View vs CTE — Khi Nào Dùng Cái Nào?


| Tiêu chí | View | CTE |
|---|---|---|
| Phạm vi | Toàn database, dùng lại nhiều query | Chỉ trong một query |
| Lưu trữ | Lưu definition trong DB | Tạm thời trong query |
| Chia sẻ | Nhiều user/query dùng chung | Không |
| Debug | Dễ — query riêng từng view | Dễ — chạy từng CTE |
| Hiệu năng | Tương đương CTE (không cache) | Tương đương View |
| Dùng khi | Logic dùng nhiều nơi, ẩn phức tạp | Logic phức tạp trong một query |



## 6\. Materialized View — View Có Cache Dữ Liệu

**Materialized View** khác View thường ở chỗ: nó **lưu kết quả query thực sự** vào disk — giống như một bảng thật. Query vào Materialized View không chạy lại query gốc mà đọc dữ liệu đã được cache.

```java
View thường:
  Query → Chạy lại query gốc mỗi lần → Kết quả

Materialized View:
  REFRESH → Chạy query gốc + lưu kết quả → Bảng cache
  Query → Đọc từ cache → Kết quả (nhanh hơn nhiều)
```

## 7\. Tạo Materialized View

```sql
CREATE MATERIALIZED VIEW ten_matview AS
SELECT ...
FROM ...
WHERE ...;

-- Tạo kèm index để query nhanh hơn
CREATE INDEX idx_matview_column ON ten_matview (column);
```

**Ví dụ** — Materialized View thống kê doanh thu (query nặng, chạy trên nhiều bảng lớn):

```sql
CREATE MATERIALIZED VIEW mv_course_revenue_stats AS
WITH revenue AS (
    SELECT
        oi.course_id,
        COUNT(DISTINCT o.user_id)  AS unique_buyers,
        COUNT(oi.id)               AS total_sales,
        SUM(oi.price)              AS gross_revenue,
        SUM(oi.final_price)        AS net_revenue
    FROM order_items oi
    JOIN orders o ON o.id = oi.order_id
    WHERE o.order_status = 'PAID'
    GROUP BY oi.course_id
),
ratings_stats AS (
    SELECT
        course_id,
        COUNT(*)            AS total_ratings,
        ROUND(AVG(rate), 2) AS avg_rating
    FROM ratings
    GROUP BY course_id
),
completion_stats AS (
    SELECT
        course_id,
        COUNT(*) AS total_completions
    FROM user_course_certificates
    GROUP BY course_id
)
SELECT
    c.id                                  AS course_id,
    c.title,
    c.course_type,
    c.enrolled_count,
    COALESCE(r.unique_buyers, 0)          AS unique_buyers,
    COALESCE(r.total_sales, 0)            AS total_sales,
    COALESCE(r.gross_revenue, 0)          AS gross_revenue,
    COALESCE(r.net_revenue, 0)            AS net_revenue,
    COALESCE(rt.avg_rating, 0)            AS avg_rating,
    COALESCE(rt.total_ratings, 0)         AS total_ratings,
    COALESCE(cs.total_completions, 0)     AS total_completions,
    ROUND(
        COALESCE(cs.total_completions, 0) * 100.0
        / NULLIF(c.enrolled_count, 0), 1
    )                                     AS completion_rate_pct,
    NOW()                                 AS last_refreshed_at
FROM courses c
LEFT JOIN revenue         r  ON r.course_id  = c.id
LEFT JOIN ratings_stats   rt ON rt.course_id = c.id
LEFT JOIN completion_stats cs ON cs.course_id = c.id
WHERE c.course_status = 'PUBLISHED';

-- Tạo index để query nhanh
CREATE INDEX idx_mv_course_revenue_id
    ON mv_course_revenue_stats (course_id);

CREATE INDEX idx_mv_course_revenue_gross
    ON mv_course_revenue_stats (gross_revenue DESC);
```

Query dashboard chỉ mất < 1ms thay vì hàng giây:

```sql
-- Dashboard: top 10 khóa học theo doanh thu
SELECT title, gross_revenue, avg_rating, completion_rate_pct
FROM mv_course_revenue_stats
ORDER BY gross_revenue DESC
LIMIT 10;
```

## 8\. REFRESH MATERIALIZED VIEW

Vì Materialized View lưu cache, dữ liệu có thể **lỗi thời** khi dữ liệu gốc thay đổi. Cần REFRESH định kỳ:

```sql
-- REFRESH bình thường — lock bảng trong lúc refresh (user không query được)
REFRESH MATERIALIZED VIEW mv_course_revenue_stats;

-- REFRESH CONCURRENTLY — không lock, user vẫn query được trong lúc refresh
-- (yêu cầu có UNIQUE index trên materialized view)
REFRESH MATERIALIZED VIEW CONCURRENTLY mv_course_revenue_stats;
```

Để dùng `CONCURRENTLY`, phải có UNIQUE index:

```sql
-- Thêm unique index để cho phép CONCURRENTLY
CREATE UNIQUE INDEX idx_mv_course_revenue_unique
    ON mv_course_revenue_stats (course_id);

-- Giờ có thể dùng CONCURRENTLY
REFRESH MATERIALIZED VIEW CONCURRENTLY mv_course_revenue_stats;
```

### Chiến Lược REFRESH

**Refresh theo lịch (Scheduled Refresh)** — phổ biến nhất:

```sql
-- Dùng pg_cron extension để schedule refresh mỗi giờ
SELECT cron.schedule(
    'refresh-course-stats',
    '0 * * * *',  -- mỗi đầu giờ
    'REFRESH MATERIALIZED VIEW CONCURRENTLY mv_course_revenue_stats'
);
```

**Refresh sau khi có dữ liệu mới (Event-based Refresh)**:

```sql
-- Tạo function refresh
CREATE OR REPLACE FUNCTION refresh_course_stats()
RETURNS TRIGGER AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_course_revenue_stats;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Trigger: refresh mỗi khi có đơn hàng mới được PAID
CREATE TRIGGER trg_refresh_course_stats
AFTER INSERT OR UPDATE ON orders
FOR EACH STATEMENT
WHEN (NEW.order_status = 'PAID')
EXECUTE FUNCTION refresh_course_stats();
```

> **Lưu ý:** Trigger refresh trên bảng orders lớn có thể gây chậm — chỉ dùng cho hệ thống nhỏ hoặc khi có debounce. Production thường dùng scheduled job.

## 9\. View vs Materialized View — So Sánh Chi Tiết


| Tiêu chí | View | Materialized View |
|---|---|---|
| Lưu trữ dữ liệu | ❌ Không — chỉ lưu definition | ✅ Có — lưu kết quả vào disk |
| Tốc độ đọc | Bằng query gốc | Rất nhanh (đọc từ cache) |
| Dữ liệu mới nhất | ✅ Luôn mới nhất | ❌ Có thể lỗi thời (cần REFRESH) |
| Dùng Index | Dùng index của bảng gốc | ✅ Tạo index riêng |
| REFRESH cần thiết | ❌ Không | ✅ Phải REFRESH định kỳ |
| Phù hợp cho | Query nhanh, dữ liệu hay thay đổi | Báo cáo nặng, dữ liệu ít thay đổi |



## 10\. Các Pattern Thực Tế

### Pattern 1 — View cho phân quyền

Ẩn cột nhạy cảm, chỉ expose dữ liệu cần thiết:

```sql
-- Ẩn password, reset_token, two_factor_secret
CREATE VIEW v_users_safe AS
SELECT
    id,
    public_id,
    email,
    first_name,
    last_name,
    account_status,
    account_type,
    avatar_url,
    created_at
FROM users;

-- API chỉ query từ v_users_safe, không bao giờ query thẳng vào users
```

### Pattern 2 — Materialized View cho Dashboard

```sql
-- Dashboard admin cần số liệu tổng hợp nhanh
CREATE MATERIALIZED VIEW mv_platform_daily_stats AS
SELECT
    DATE_TRUNC('day', o.created_at)         AS date,
    COUNT(DISTINCT o.user_id)               AS daily_active_buyers,
    COUNT(o.id)                             AS total_orders,
    SUM(CASE WHEN o.order_status = 'PAID'
             THEN o.final_amount ELSE 0 END) AS daily_revenue,
    COUNT(CASE WHEN o.order_status = 'PAID'
               THEN 1 END)                   AS paid_orders,
    COUNT(CASE WHEN o.order_status = 'CANCELLED'
               THEN 1 END)                   AS cancelled_orders
FROM orders o
WHERE o.created_at >= CURRENT_DATE - INTERVAL '90 days'
GROUP BY DATE_TRUNC('day', o.created_at)
ORDER BY date DESC;

CREATE UNIQUE INDEX idx_mv_daily_stats_date
    ON mv_platform_daily_stats (date);
```

### Pattern 3 — View Layer cho API

```sql
-- View tổng hợp thông tin bài viết cho API public
CREATE VIEW v_post_public AS
SELECT
    p.id,
    p.slug,
    p.title,
    p.thumbnail_url,
    p.view_count,
    p.published_at,
    p.post_type,
    u.first_name || ' ' || u.last_name AS author_name,
    u.avatar_url                        AS author_avatar,
    array_agg(DISTINCT t.name)          AS tags,
    array_agg(DISTINCT cat.name)        AS categories
FROM posts p
JOIN users          u   ON u.id   = p.writer_id
LEFT JOIN post_tags pt  ON pt.post_id = p.id
LEFT JOIN tags      t   ON t.id   = pt.tag_id
LEFT JOIN post_categories pc ON pc.post_id = p.id
LEFT JOIN categories cat ON cat.id = pc.category_id
WHERE p.post_status = 'PUBLISHED'
GROUP BY p.id, p.slug, p.title, p.thumbnail_url,
         p.view_count, p.published_at, p.post_type,
         u.first_name, u.last_name, u.avatar_url;
```

## 11\. Thực Hành Tổng Hợp

**Bài 1:** Tạo view tổng hợp thông tin đơn hàng cho trang lịch sử mua hàng của học viên.

```sql
CREATE VIEW v_order_history AS
SELECT
    o.id                                    AS order_id,
    o.order_code,
    o.order_status,
    o.final_amount,
    o.currency,
    o.created_at                            AS order_date,
    o.user_id,
    oi.course_id,
    oi.item_title                           AS course_title,
    oi.price                                AS item_price,
    ucc.cert_code,
    ucc.cert_url,
    CASE
        WHEN ucc.id IS NOT NULL THEN TRUE
        ELSE FALSE
    END                                     AS has_certificate
FROM orders o
JOIN order_items             oi  ON oi.order_id  = o.id
LEFT JOIN user_course_certificates ucc
       ON ucc.student_id = o.user_id
      AND ucc.course_id  = oi.course_id
WHERE o.order_status = 'PAID';
```

**Bài 2:** Tạo Materialized View thống kê doanh thu theo tháng cho dashboard admin.

```sql
CREATE MATERIALIZED VIEW mv_monthly_revenue AS
SELECT
    DATE_TRUNC('month', o.created_at)       AS month,
    COUNT(DISTINCT o.user_id)               AS unique_buyers,
    COUNT(o.id)                             AS total_orders,
    SUM(o.final_amount)                     AS total_revenue,
    ROUND(AVG(o.final_amount), 0)           AS avg_order_value,
    COUNT(DISTINCT oi.course_id)            AS courses_sold,
    LAG(SUM(o.final_amount)) OVER (
        ORDER BY DATE_TRUNC('month', o.created_at)
    )                                       AS prev_month_revenue,
    ROUND(
        (SUM(o.final_amount) - LAG(SUM(o.final_amount)) OVER (
            ORDER BY DATE_TRUNC('month', o.created_at)
        )) * 100.0 / NULLIF(
            LAG(SUM(o.final_amount)) OVER (
                ORDER BY DATE_TRUNC('month', o.created_at)
            ), 0
        ), 1
    )                                       AS revenue_growth_pct
FROM orders o
JOIN order_items oi ON oi.order_id = o.id
WHERE o.order_status = 'PAID'
GROUP BY DATE_TRUNC('month', o.created_at);

CREATE UNIQUE INDEX idx_mv_monthly_revenue_month
    ON mv_monthly_revenue (month);
```

## Tổng Kết


| Khái niệm | Ý nghĩa |
|---|---|
| View | Bảng ảo — lưu definition, chạy query gốc mỗi lần truy cập |
| CREATE VIEW | Tạo view mới |
| CREATE OR REPLACE VIEW | Tạo hoặc thay thế view đã có |
| WITH CHECK OPTION | Đảm bảo dữ liệu ghi qua view thỏa điều kiện |
| Materialized View | View có cache dữ liệu thật — nhanh hơn, cần REFRESH |
| REFRESH MATERIALIZED VIEW | Cập nhật lại cache — lock bảng |
| REFRESH CONCURRENTLY | Cập nhật không lock — cần UNIQUE index |



Bài tiếp theo chúng ta sẽ học **Stored Procedures & Functions** — cách đẩy logic xuống tầng database, tái sử dụng code SQL và tự động hóa các tác vụ phức tạp.

> **Khác biệt với các RDBMS khác:**
> 
> *   **MySQL:** Hỗ trợ View nhưng **không có Materialized View** — phải dùng bảng thường + scheduled event để giả lập
>     
> *   **SQL Server:** Có **Indexed View** tương tự Materialized View — tự động cập nhật khi dữ liệu gốc thay đổi (không cần REFRESH thủ công)
>     
> *   **Oracle:** Có Materialized View với nhiều tùy chọn refresh: `ON COMMIT`, `ON DEMAND`, `FAST REFRESH` (chỉ refresh phần thay đổi)
>     
> *   **PostgreSQL REFRESH CONCURRENTLY:** Chỉ PostgreSQL mới có tính năng này — các RDBMS khác không có tương đương trực tiếp
>     

