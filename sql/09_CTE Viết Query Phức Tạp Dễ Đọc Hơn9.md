# CTE: Viết Query Phức Tạp Dễ Đọc Hơn

![CTE - Viết Query Phức Tạp Dễ Đọc Hơn.jpeg](../images/4a5bd1f5-f048-4a8b-a333-60cbe41d59ce.jpeg)

Ở bài trước bạn đã thấy subquery lồng nhau có thể trở nên rất khó đọc — query 3-4 tầng lồng nhau khiến người khác (hoặc chính bạn sau 2 tuần) không hiểu logic đang làm gì. **CTE (Common Table Expression)** ra đời để giải quyết vấn đề đó: chia query phức tạp thành các bước nhỏ có tên rõ ràng, dễ đọc và có thể tái sử dụng trong cùng một query.

## 1\. CTE là gì?

CTE là một **"bảng tạm có tên"** được định nghĩa bằng mệnh đề `WITH` ngay trước câu SELECT chính. Nó tồn tại trong phạm vi của một câu query duy nhất, không lưu vĩnh viễn vào database.

```sql
WITH ten_cte AS (
    SELECT ...   -- định nghĩa CTE
)
SELECT ...       -- dùng CTE như một bảng thông thường
FROM ten_cte;
```

## 2\. CTE cơ bản — Thay thế subquery trong FROM

Hãy so sánh cùng một bài toán viết theo 2 cách:

**Bài toán:** Tìm các học viên có tổng chi tiêu cao hơn mức trung bình.

```sql
-- ❌ Subquery lồng nhau — khó đọc
SELECT student_name, total_spent
FROM (
    SELECT
        u.first_name || ' ' || u.last_name AS student_name,
        SUM(o.final_amount)                 AS total_spent
    FROM orders o
    JOIN users u ON u.id = o.user_id
    WHERE o.order_status = 'PAID'
    GROUP BY u.id, u.first_name, u.last_name
) AS spending
WHERE total_spent > (
    SELECT AVG(total_per_user)
    FROM (
        SELECT SUM(final_amount) AS total_per_user
        FROM orders
        WHERE order_status = 'PAID'
        GROUP BY user_id
    ) AS totals
);
```

```sql
-- ✅ CTE — rõ ràng từng bước
WITH spending_per_user AS (
    SELECT
        u.id,
        u.first_name || ' ' || u.last_name AS student_name,
        SUM(o.final_amount)                 AS total_spent
    FROM orders o
    JOIN users u ON u.id = o.user_id
    WHERE o.order_status = 'PAID'
    GROUP BY u.id, u.first_name, u.last_name
),
avg_spending AS (
    SELECT AVG(total_spent) AS avg_value
    FROM spending_per_user
)
SELECT
    s.student_name,
    s.total_spent,
    ROUND(a.avg_value, 0) AS platform_avg
FROM spending_per_user s
CROSS JOIN avg_spending a
WHERE s.total_spent > a.avg_value
ORDER BY s.total_spent DESC;
```

Cùng một kết quả nhưng CTE rõ ràng hơn nhiều — người đọc biết ngay bước 1 tính chi tiêu từng học viên, bước 2 tính trung bình, bước 3 lọc.

## 3\. Multiple CTE — Nhiều CTE trong một query

Bạn có thể định nghĩa nhiều CTE liên tiếp, cách nhau bằng dấu phẩy:

```sql
WITH
cte_1 AS (
    SELECT ...
),
cte_2 AS (
    SELECT ...
    FROM cte_1   -- CTE sau có thể dùng CTE trước
),
cte_3 AS (
    SELECT ...
    FROM cte_1
    JOIN cte_2 ON ...
)
SELECT * FROM cte_3;
```

**Ví dụ thực tế** — Báo cáo tổng hợp doanh thu và học viên theo khóa học:

```sql
WITH
-- Bước 1: Doanh thu từng khóa học
course_revenue AS (
    SELECT
        oi.course_id,
        COUNT(DISTINCT o.user_id) AS total_buyers,
        SUM(oi.price)             AS total_revenue
    FROM order_items oi
    JOIN orders o ON o.id = oi.order_id
    WHERE o.order_status = 'PAID'
    GROUP BY oi.course_id
),
-- Bước 2: Rating từng khóa học
course_rating AS (
    SELECT
        course_id,
        COUNT(*)           AS total_ratings,
        ROUND(AVG(rate), 1) AS avg_rating
    FROM ratings
    GROUP BY course_id
),
-- Bước 3: Số học viên đang học (đã enroll nhưng chưa có cert)
active_learners AS (
    SELECT
        e.course_id,
        COUNT(*) AS learner_count
    FROM enrollments e
    WHERE NOT EXISTS (
        SELECT 1 FROM user_course_certificates ucc
        WHERE ucc.student_id = e.user_id
          AND ucc.course_id  = e.course_id
    )
    GROUP BY e.course_id
)
-- Bước 4: Kết hợp tất cả
SELECT
    c.title,
    c.course_type,
    COALESCE(cr.total_buyers, 0)   AS total_buyers,
    COALESCE(cr.total_revenue, 0)  AS total_revenue,
    COALESCE(rat.avg_rating, 0)    AS avg_rating,
    COALESCE(rat.total_ratings, 0) AS total_ratings,
    COALESCE(al.learner_count, 0)  AS active_learners
FROM courses c
LEFT JOIN course_revenue  cr  ON cr.course_id  = c.id
LEFT JOIN course_rating   rat ON rat.course_id = c.id
LEFT JOIN active_learners al  ON al.course_id  = c.id
WHERE c.course_status = 'PUBLISHED'
ORDER BY cr.total_revenue DESC NULLS LAST;
```

## 4\. CTE vs Subquery — Khi nào dùng cái nào?


| Tiêu chí | CTE | Subquery |
|---|---|---|
| Độ dễ đọc | ✅ Rõ ràng, có tên | ❌ Lồng nhau khó đọc |
| Tái sử dụng trong query | ✅ Dùng nhiều lần | ❌ Phải lặp lại |
| Debug từng bước | ✅ Chạy riêng từng CTE được | ❌ Khó isolate |
| Hiệu năng | Tương đương (optimizer xử lý như nhau) | Tương đương |
| Dùng trong INSERT/UPDATE | ✅ Được | ❌ Hạn chế |



> **Nguyên tắc thực tế:** Khi subquery chỉ dùng 1 lần và ngắn (1-2 dòng) → để subquery cho gọn. Khi logic phức tạp hơn, cần tái sử dụng hoặc có nhiều bước → dùng CTE.

## 5\. CTE trong INSERT, UPDATE, DELETE

CTE không chỉ dùng với SELECT — bạn có thể kết hợp với DML:

```sql
-- Cập nhật rating của khóa học dựa trên bảng ratings
WITH latest_ratings AS (
    SELECT
        course_id,
        ROUND(AVG(rate), 2) AS new_rating
    FROM ratings
    GROUP BY course_id
)
UPDATE courses c
SET rating = lr.new_rating
FROM latest_ratings lr
WHERE lr.course_id = c.id;
```

```sql
-- Xóa các bài viết DRAFT quá 90 ngày chưa được cập nhật
WITH old_drafts AS (
    SELECT id
    FROM posts
    WHERE post_status = 'DRAFT'
      AND updated_at < NOW() - INTERVAL '90 days'
)
DELETE FROM posts
WHERE id IN (SELECT id FROM old_drafts);
```

## 6\. Recursive CTE — Duyệt dữ liệu phân cấp

**Recursive CTE** là tính năng mạnh nhất của CTE — cho phép query lặp đi lặp lại cho đến khi không còn dữ liệu để xử lý. Dùng để duyệt **cây phân cấp** như category tree, org chart, menu items.

Cú pháp:

```sql
WITH RECURSIVE ten_cte AS (
    -- Phần 1: Base case — điểm bắt đầu (không đệ quy)
    SELECT ...

    UNION ALL

    -- Phần 2: Recursive case — tham chiếu lại chính CTE
    SELECT ...
    FROM bang
    JOIN ten_cte ON ...  -- đây là phần đệ quy
)
SELECT * FROM ten_cte;
```

**Ví dụ** — Duyệt toàn bộ cây category của [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev), hiển thị level và đường dẫn đầy đủ:

```sql
WITH RECURSIVE category_tree AS (
    -- Base case: lấy các category gốc (không có parent)
    SELECT
        id,
        name,
        parent_id,
        0           AS level,
        name::TEXT  AS path
    FROM categories
    WHERE parent_id IS NULL

    UNION ALL

    -- Recursive case: lấy các category con
    SELECT
        c.id,
        c.name,
        c.parent_id,
        ct.level + 1                    AS level,
        ct.path || ' > ' || c.name     AS path
    FROM categories c
    JOIN category_tree ct ON ct.id = c.parent_id
)
SELECT
    REPEAT('  ', level) || name AS display_name,
    level,
    path
FROM category_tree
ORDER BY path;
```

Kết quả:


| display_name | level | path |
|---|---|---|
| Database | 0 | Database |
| NoSQL | 1 | Database > NoSQL |
| SQL | 1 | Database > SQL |
| DevOps | 0 | DevOps |
| Java | 0 | Java |
| Java Core | 1 | Java > Java Core |
| Microservices | 1 | Java > Microservices |
| Spring Boot | 1 | Java > Spring Boot |



**Ví dụ khác** — Duyệt cây comment (comment có thể reply lồng nhau đến depth 10):

```sql
WITH RECURSIVE comment_thread AS (
    -- Base case: comment gốc của một bài viết cụ thể
    SELECT
        id,
        user_id,
        content,
        parent_comment_id,
        depth,
        id::TEXT AS thread_path
    FROM comments
    WHERE object_id   = 1          -- post_id = 1
      AND object_type = 'POST'
      AND parent_comment_id IS NULL
      AND comment_status = 'VISIBLE'

    UNION ALL

    -- Recursive case: lấy các reply
    SELECT
        c.id,
        c.user_id,
        c.content,
        c.parent_comment_id,
        c.depth,
        ct.thread_path || '->' || c.id::TEXT
    FROM comments c
    JOIN comment_thread ct ON ct.id = c.parent_comment_id
    WHERE c.comment_status = 'VISIBLE'
)
SELECT
    REPEAT('  ', depth) || content AS comment_display,
    depth,
    thread_path
FROM comment_thread
ORDER BY thread_path;
```

> **Tránh vòng lặp vô hạn:** Recursive CTE có thể chạy mãi nếu dữ liệu có chu trình (A là cha của B, B là cha của A). PostgreSQL có giới hạn mặc định là 100 vòng lặp — có thể điều chỉnh bằng `SET max_recursion_depth`. Luôn kiểm tra dữ liệu có bị circular reference không trước khi dùng Recursive CTE.

## 7\. Thực hành tổng hợp

**Bài 1:** Dùng CTE viết báo cáo top 3 học viên mua nhiều khóa học nhất, kèm tổng chi tiêu và số khóa đã mua.

```sql
WITH student_stats AS (
    SELECT
        u.id,
        u.first_name || ' ' || u.last_name AS full_name,
        u.email,
        COUNT(DISTINCT oi.course_id)        AS courses_bought,
        SUM(o.final_amount)                 AS total_spent
    FROM users u
    JOIN orders      o  ON o.user_id = u.id
    JOIN order_items oi ON oi.order_id = o.id
    WHERE o.order_status = 'PAID'
    GROUP BY u.id, u.first_name, u.last_name, u.email
)
SELECT
    full_name,
    email,
    courses_bought,
    total_spent
FROM student_stats
ORDER BY courses_bought DESC, total_spent DESC
LIMIT 3;
```

**Bài 2:** Dùng Recursive CTE lấy toàn bộ menu items theo cấu trúc cây, hiển thị level và thứ tự sắp xếp.

```sql
WITH RECURSIVE menu_tree AS (
    SELECT
        id,
        title,
        parent_id,
        sort_order,
        0          AS level,
        title::TEXT AS full_path
    FROM menu_items
    WHERE parent_id IS NULL
      AND is_active = TRUE

    UNION ALL

    SELECT
        m.id,
        m.title,
        m.parent_id,
        m.sort_order,
        mt.level + 1,
        mt.full_path || ' / ' || m.title
    FROM menu_items m
    JOIN menu_tree mt ON mt.id = m.parent_id
    WHERE m.is_active = TRUE
)
SELECT
    REPEAT('— ', level) || title AS menu_display,
    level,
    sort_order,
    full_path
FROM menu_tree
ORDER BY full_path, sort_order;
```

**Bài 3:** Dùng CTE tính conversion rate — tỷ lệ học viên đã enroll có chứng chỉ hoàn thành, theo từng khóa học.

```sql
WITH
enrollments_count AS (
    SELECT course_id, COUNT(*) AS total_enrolled
    FROM enrollments
    GROUP BY course_id
),
completions_count AS (
    SELECT course_id, COUNT(*) AS total_completed
    FROM user_course_certificates
    GROUP BY course_id
)
SELECT
    c.title,
    COALESCE(e.total_enrolled, 0)   AS enrolled,
    COALESCE(co.total_completed, 0) AS completed,
    ROUND(
        COALESCE(co.total_completed, 0) * 100.0
        / NULLIF(e.total_enrolled, 0),
        1
    ) AS completion_rate_pct
FROM courses c
LEFT JOIN enrollments_count e  ON e.course_id  = c.id
LEFT JOIN completions_count co ON co.course_id = c.id
WHERE c.course_status = 'PUBLISHED'
ORDER BY completion_rate_pct DESC NULLS LAST;
```

## Tổng kết


| Loại CTE | Cú pháp | Dùng khi nào |
|---|---|---|
| CTE đơn | WITH cte AS (...) | Thay thế subquery trong FROM cho dễ đọc |
| Multiple CTE | WITH cte1 AS (...), cte2 AS (...) | Query nhiều bước, cần tái sử dụng kết quả trung gian |
| CTE trong DML | WITH cte AS (...) UPDATE/DELETE | Cập nhật hoặc xóa dựa trên logic phức tạp |
| Recursive CTE | WITH RECURSIVE cte AS (base UNION ALL recursive) | Duyệt cây phân cấp, đồ thị |



Bài tiếp theo chúng ta sẽ học **Window Functions** — một trong những tính năng mạnh và thú vị nhất của SQL, cho phép tính toán trên một "cửa sổ" dữ liệu mà không làm mất đi từng dòng như GROUP BY.

> **Khác biệt với các RDBMS khác:**
> 
> *   **MySQL:** Hỗ trợ CTE từ version 8.0 — trước đó phải dùng subquery hoặc temporary table
>     
> *   **SQL Server:** Hỗ trợ CTE đầy đủ, cú pháp giống PostgreSQL
>     
> *   **Oracle:** Hỗ trợ CTE đầy đủ, gọi là **Subquery Factoring**
>     
> *   **Recursive CTE:** Tất cả RDBMS phổ biến đều hỗ trợ, cú pháp `WITH RECURSIVE` giống nhau — riêng SQL Server dùng `WITH` (không cần từ khóa `RECURSIVE`)
>     

