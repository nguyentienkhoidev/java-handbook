# Subquery: Query lồng trong Query

![Subquery - Query lồng trong Query.jpeg](../images/677d2454-e153-4457-b91b-2f91f75b36b1.jpeg)

Bước vào nhóm Intermediate, chúng ta sẽ học cách giải quyết những bài toán phức tạp hơn mà JOIN đơn thuần không đủ sức. **Subquery** (hay còn gọi là nested query) là kỹ thuật nhúng một câu SELECT bên trong một câu SELECT khác — mở ra khả năng viết logic nhiều tầng, linh hoạt và mạnh mẽ hơn nhiều.

## 1\. Subquery là gì?

Subquery là một câu query nằm bên trong câu query khác, được đặt trong dấu ngoặc đơn `()`. Câu query bên ngoài gọi là **outer query**, câu query bên trong gọi là **inner query** hay **subquery**.

```sql
SELECT column
FROM table
WHERE column OPERATOR (
    SELECT column FROM table WHERE condition  -- đây là subquery
);
```

Subquery được thực thi **trước** outer query, kết quả của nó được dùng làm điều kiện hoặc nguồn dữ liệu cho outer query.

## 2\. Subquery trong WHERE

Đây là dạng phổ biến nhất — dùng kết quả của subquery làm điều kiện lọc.

### Subquery trả về một giá trị (scalar subquery)

```sql
-- Tìm các khóa học có giá cao hơn giá trung bình
SELECT title, price
FROM courses
WHERE price > (
    SELECT AVG(price)
    FROM courses
    WHERE course_type = 'PAID'
)
ORDER BY price DESC;
```

Subquery `SELECT AVG(price)...` trả về một con số duy nhất (ví dụ `699000`), outer query dùng con số đó để so sánh.

```sql
-- Tìm đơn hàng có giá trị cao nhất
SELECT id, user_id, final_amount, created_at
FROM orders
WHERE final_amount = (
    SELECT MAX(final_amount)
    FROM orders
    WHERE order_status = 'PAID'
);
```

### Subquery trả về danh sách giá trị — dùng với IN

```sql
-- Tìm các học viên đã từng mua khóa học SQL
SELECT first_name, last_name, email
FROM users
WHERE id IN (
    SELECT DISTINCT o.user_id
    FROM orders o
    JOIN order_items oi ON oi.order_id = o.id
    JOIN courses     c  ON c.id = oi.course_id
    WHERE c.title ILIKE '%SQL%'
      AND o.order_status = 'PAID'
);
```

```sql
-- Tìm các khóa học chưa có ai đăng ký
SELECT title, course_type, price
FROM courses
WHERE id NOT IN (
    SELECT DISTINCT course_id
    FROM enrollments
);
```

> **Nhắc lại cảnh báo từ Bài 5:** Tránh dùng `NOT IN` khi subquery có thể trả về NULL. Dùng `NOT EXISTS` sẽ an toàn hơn — FoxDev sẽ đề cập ngay bên dưới.

## 3\. Subquery trong FROM — Derived Table

Bạn có thể dùng subquery như một bảng tạm trong mệnh đề `FROM`. Kết quả của subquery đóng vai trò như một bảng thực thụ — gọi là **derived table** hay **inline view**.

```sql
SELECT *
FROM (
    SELECT column1, AGG_FUNC(column2) AS alias
    FROM table
    GROUP BY column1
) AS ten_bang_tam
WHERE ten_bang_tam.alias > gia_tri;
```

**Ví dụ** — Tìm các học viên có tổng chi tiêu trên mức trung bình:

```sql
SELECT student_name, total_spent
FROM (
    SELECT
        u.first_name || ' ' || u.last_name AS student_name,
        SUM(o.final_amount)                 AS total_spent
    FROM orders o
    JOIN users u ON u.id = o.user_id
    WHERE o.order_status = 'PAID'
    GROUP BY u.id, u.first_name, u.last_name
) AS spending_summary
WHERE total_spent > (
    SELECT AVG(total_per_user)
    FROM (
        SELECT SUM(final_amount) AS total_per_user
        FROM orders
        WHERE order_status = 'PAID'
        GROUP BY user_id
    ) AS user_totals
)
ORDER BY total_spent DESC;
```

**Ví dụ thực tế hơn** — Thống kê doanh thu theo tháng rồi tìm tháng có doanh thu trên 2 triệu:

```sql
SELECT month, monthly_revenue
FROM (
    SELECT
        DATE_TRUNC('month', created_at) AS month,
        SUM(final_amount)               AS monthly_revenue
    FROM orders
    WHERE order_status = 'PAID'
    GROUP BY DATE_TRUNC('month', created_at)
) AS monthly_stats
WHERE monthly_revenue > 2000000
ORDER BY month DESC;
```

## 4\. Subquery trong SELECT — Scalar Subquery

Bạn có thể đặt subquery ngay trong danh sách cột của SELECT — subquery này phải trả về **đúng một giá trị** cho mỗi dòng:

```sql
-- Hiển thị thông tin khóa học kèm số lượng học viên đã đăng ký
SELECT
    c.title,
    c.price,
    (
        SELECT COUNT(*)
        FROM enrollments e
        WHERE e.course_id = c.id
    ) AS enrollment_count,
    (
        SELECT ROUND(AVG(rate), 1)
        FROM ratings r
        WHERE r.course_id = c.id
    ) AS avg_rating
FROM courses c
WHERE c.course_status = 'PUBLISHED'
ORDER BY enrollment_count DESC;
```

> **Lưu ý hiệu năng:** Scalar subquery trong SELECT chạy **một lần cho mỗi dòng** của outer query — nếu bảng có 10.000 khóa học, subquery chạy 10.000 lần. Trong nhiều trường hợp, viết lại bằng `LEFT JOIN` + `GROUP BY` sẽ hiệu quả hơn. FoxDev sẽ đề cập kỹ ở bài Query Optimization.

## 5\. Correlated Subquery — Subquery tham chiếu outer query

**Correlated Subquery** là subquery có tham chiếu đến cột của outer query — tức là subquery không thể chạy độc lập mà phụ thuộc vào từng dòng của outer query.

```sql
-- Tìm các khóa học có rating cao hơn rating trung bình
-- của chính loại khóa học đó (PAID so với PAID, FREE so với FREE)
SELECT c1.title, c1.course_type, c1.rating
FROM courses c1
WHERE c1.rating > (
    SELECT AVG(c2.rating)
    FROM courses c2
    WHERE c2.course_type = c1.course_type  -- tham chiếu c1 từ outer query
)
ORDER BY c1.course_type, c1.rating DESC;
```

```sql
-- Với mỗi user, lấy đơn hàng gần nhất của họ
SELECT
    u.first_name,
    u.last_name,
    (
        SELECT MAX(o.created_at)
        FROM orders o
        WHERE o.user_id = u.id        -- tham chiếu u từ outer query
          AND o.order_status = 'PAID'
    ) AS last_order_date
FROM users u
WHERE u.account_status = 'ACTIVE';
```

> **Hiệu năng:** Correlated subquery chạy lại cho mỗi dòng của outer query — tương tự vòng lặp lồng nhau trong lập trình. Với bảng lớn có thể rất chậm. Thường có thể viết lại bằng JOIN hoặc Window Function hiệu quả hơn.

## 6\. EXISTS và NOT EXISTS

`EXISTS` kiểm tra xem subquery có trả về **ít nhất một dòng** không — không quan tâm đến giá trị cụ thể.

```sql
-- Tìm học viên đã mua ít nhất một khóa học
SELECT first_name, last_name, email
FROM users u
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.user_id = u.id
      AND o.order_status = 'PAID'
);
```

> `SELECT 1` trong subquery của EXISTS là convention phổ biến — không cần SELECT cột thực vì EXISTS chỉ kiểm tra có dòng hay không, không quan tâm giá trị.

### NOT EXISTS — An toàn hơn NOT IN với NULL

```sql
-- Tìm học viên chưa mua khóa học nào — dùng NOT EXISTS thay NOT IN
SELECT first_name, last_name, email
FROM users u
WHERE NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.user_id = u.id
      AND o.order_status = 'PAID'
);
```

So sánh `NOT IN` vs `NOT EXISTS`:

```sql
-- ❌ NOT IN — nguy hiểm nếu subquery trả về NULL
SELECT * FROM users
WHERE id NOT IN (SELECT user_id FROM orders);
-- Nếu có orders.user_id = NULL → trả về 0 dòng!

-- ✅ NOT EXISTS — luôn hoạt động đúng
SELECT * FROM users u
WHERE NOT EXISTS (
    SELECT 1 FROM orders o WHERE o.user_id = u.id
);
```

**Ví dụ thực tế** — Tìm các khóa học chưa có bài giảng nào được publish:

```sql
SELECT c.title, c.course_status
FROM courses c
WHERE NOT EXISTS (
    SELECT 1
    FROM lectures l
    WHERE l.course_id = c.id
      AND l.lecture_status = 'PUBLISHED'
)
AND c.course_status = 'PUBLISHED';
```

## 7\. Subquery vs JOIN — Khi nào dùng cái nào?

Nhiều bài toán có thể giải bằng cả subquery lẫn JOIN. Đây là hướng dẫn chọn:

**Dùng Subquery khi:**

*   Logic rõ ràng hơn khi tách thành từng bước (đọc dễ hiểu hơn)
    
*   Cần lọc theo kết quả aggregate của bảng khác
    
*   Dùng EXISTS / NOT EXISTS để kiểm tra sự tồn tại
    
*   Scalar subquery trong SELECT khi chỉ cần 1 giá trị bổ sung
    

**Dùng JOIN khi:**

*   Cần lấy cột từ nhiều bảng cùng lúc
    
*   Cần aggregate trên kết quả của nhiều bảng
    
*   Hiệu năng quan trọng — JOIN thường được optimizer xử lý tốt hơn correlated subquery
    

```sql
-- Cách 1: Subquery — rõ ý định hơn
SELECT title, price
FROM courses
WHERE id IN (
    SELECT course_id FROM enrollments
    GROUP BY course_id
    HAVING COUNT(*) > 100
);

-- Cách 2: JOIN — thường nhanh hơn với bảng lớn
SELECT c.title, c.price
FROM courses c
JOIN (
    SELECT course_id, COUNT(*) AS cnt
    FROM enrollments
    GROUP BY course_id
    HAVING COUNT(*) > 100
) AS popular ON popular.course_id = c.id;
```

## 8\. Thực hành tổng hợp

**Bài 1:** Tìm các khóa học có số lượng đánh giá nhiều hơn số lượng đánh giá trung bình của tất cả khóa học.

```sql
SELECT c.title, COUNT(r.id) AS total_ratings
FROM courses c
JOIN ratings r ON r.course_id = c.id
GROUP BY c.id, c.title
HAVING COUNT(r.id) > (
    SELECT AVG(rating_count)
    FROM (
        SELECT COUNT(*) AS rating_count
        FROM ratings
        GROUP BY course_id
    ) AS counts
)
ORDER BY total_ratings DESC;
```

**Bài 2:** Lấy danh sách học viên ACTIVE chưa từng để lại đánh giá nào.

```sql
SELECT first_name, last_name, email
FROM users u
WHERE account_status = 'ACTIVE'
  AND NOT EXISTS (
      SELECT 1
      FROM ratings r
      WHERE r.reviewer_id = u.id
  );
```

**Bài 3:** Với mỗi khóa học, hiển thị tiêu đề và tổng doanh thu — chỉ lấy các khóa có doanh thu cao hơn doanh thu trung bình toàn platform.

```sql
SELECT course_title, total_revenue
FROM (
    SELECT
        c.title       AS course_title,
        SUM(oi.price) AS total_revenue
    FROM order_items oi
    JOIN courses c ON c.id = oi.course_id
    JOIN orders  o ON o.id = oi.order_id
    WHERE o.order_status = 'PAID'
    GROUP BY c.id, c.title
) AS course_revenue
WHERE total_revenue > (
    SELECT AVG(revenue_per_course)
    FROM (
        SELECT SUM(oi2.price) AS revenue_per_course
        FROM order_items oi2
        JOIN orders o2 ON o2.id = oi2.order_id
        WHERE o2.order_status = 'PAID'
        GROUP BY oi2.course_id
    ) AS avg_calc
)
ORDER BY total_revenue DESC;
```

## Tổng kết


| Dạng Subquery | Vị trí | Trả về | Dùng khi nào |
|---|---|---|---|
| Scalar subquery | WHERE, SELECT | 1 giá trị | So sánh với 1 giá trị tổng hợp |
| Multi-row subquery | WHERE + IN | Danh sách giá trị | Lọc theo tập hợp |
| Derived table | FROM | Bảng tạm | Cần xử lý nhiều bước |
| Correlated subquery | WHERE, SELECT | Phụ thuộc outer | Logic liên kết từng dòng |
| EXISTS / NOT EXISTS | WHERE | TRUE / FALSE | Kiểm tra sự tồn tại |



Bài tiếp theo chúng ta sẽ học **CTE (Common Table Expression)** — một cách viết subquery rõ ràng, có thể tái sử dụng và dễ đọc hơn nhiều so với nested subquery thuần túy.

> **Khác biệt với các RDBMS khác:**
> 
> *   **Tất cả RDBMS phổ biến** đều hỗ trợ subquery với cú pháp tương tự
>     
> *   **MySQL trước 5.6:** Optimizer xử lý subquery kém, hay bị chậm với `IN (subquery)` — nên ưu tiên dùng JOIN thay thế. Từ MySQL 8.0 đã cải thiện đáng kể
>     
> *   **SQL Server:** Hỗ trợ đầy đủ, cú pháp giống PostgreSQL
>     

