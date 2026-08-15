# CASE WHEN: Conditional Logic Trong SQL

![](../images/929006a8-598a-4cb0-955d-3d9159adf296.jpeg)

Trong lập trình bạn có `if/else`, trong SQL bạn có `CASE WHEN`. Đây là công cụ để viết logic điều kiện ngay trong câu query — phân loại dữ liệu, gán nhãn, chuyển đổi giá trị, thậm chí pivot bảng từ dạng dọc sang dạng ngang. Hiểu và sử dụng tốt `CASE WHEN` giúp bạn giải quyết được nhiều bài toán phức tạp mà không cần xử lý ở tầng application.

## 1\. Cú pháp CASE WHEN

SQL có 2 dạng CASE WHEN:

### Dạng 1 — Searched CASE (phổ biến hơn)

```sql
CASE
    WHEN condition_1 THEN result_1
    WHEN condition_2 THEN result_2
    WHEN condition_3 THEN result_3
    ELSE default_result       -- tùy chọn, nếu không có ELSE → trả về NULL
END
```

### Dạng 2 — Simple CASE (so sánh bằng một giá trị)

```sql
CASE column
    WHEN value_1 THEN result_1
    WHEN value_2 THEN result_2
    ELSE default_result
END
```

## 2\. CASE WHEN trong SELECT — Phân loại và gán nhãn

### Phân loại khóa học theo giá

```sql
SELECT
    title,
    price,
    CASE
        WHEN price = 0              THEN 'Miễn phí'
        WHEN price < 500000         THEN 'Bình dân'
        WHEN price BETWEEN 500000
                       AND 800000   THEN 'Trung bình'
        WHEN price > 800000         THEN 'Cao cấp'
    END AS price_tier
FROM courses
ORDER BY price;
```

Kết quả:


| title | price | price_tier |
|---|---|---|
| Java Core nền tảng | 0 | Miễn phí |
| SQL cho Developer | 599000 | Trung bình |
| ReactJS cơ bản đến nâng cao | 699000 | Trung bình |
| Spring Boot từ Zero đến Hero | 799000 | Trung bình |
| Docker & Kubernetes thực chiến | 899000 | Cao cấp |



### Phân loại học viên theo mức độ hoạt động

```sql
SELECT
    u.first_name || ' ' || u.last_name AS student_name,
    COUNT(o.id)                         AS total_orders,
    SUM(o.final_amount)                 AS total_spent,
    CASE
        WHEN SUM(o.final_amount) >= 2000000 THEN 'Diamond'
        WHEN SUM(o.final_amount) >= 1000000 THEN 'Gold'
        WHEN SUM(o.final_amount) >= 500000  THEN 'Silver'
        ELSE                                     'Bronze'
    END AS customer_tier
FROM users u
LEFT JOIN orders o ON o.user_id = u.id
                   AND o.order_status = 'PAID'
GROUP BY u.id, u.first_name, u.last_name
ORDER BY total_spent DESC NULLS LAST;
```

## 3\. CASE WHEN trong WHERE — Điều kiện động

```sql
-- Lọc theo loại khóa học: nếu là PAID thì lọc thêm theo giá
SELECT title, course_type, price
FROM courses
WHERE CASE
          WHEN course_type = 'PAID' THEN price > 500000
          WHEN course_type = 'FREE' THEN TRUE
          ELSE FALSE
      END;
```

## 4\. CASE WHEN trong ORDER BY — Sắp xếp tùy chỉnh

Mặc định ORDER BY sắp xếp theo alphabet hoặc số — nhưng đôi khi bạn cần thứ tự tùy chỉnh:

```sql
-- Sắp xếp trạng thái đơn hàng theo mức độ ưu tiên xử lý
SELECT id, user_id, order_status, final_amount
FROM orders
ORDER BY
    CASE order_status
        WHEN 'PENDING'    THEN 1   -- ưu tiên xử lý nhất
        WHEN 'PAID'       THEN 2
        WHEN 'PARTIALLY_PAID' THEN 3
        WHEN 'FAILED'     THEN 4
        WHEN 'CANCELLED'  THEN 5
        ELSE                   6
    END,
    created_at DESC;
```

```sql
-- Sắp xếp bài viết: PINNED lên đầu, sau đó theo ngày publish
SELECT title, post_status, home_marketing_pinned, published_at
FROM posts
WHERE post_status = 'PUBLISHED'
ORDER BY
    CASE WHEN home_marketing_pinned = TRUE THEN 0 ELSE 1 END,
    home_marketing_sort_order ASC,
    published_at DESC;
```

## 5\. CASE WHEN trong GROUP BY và Aggregate

### Đếm có điều kiện — thay thế cho nhiều query

```sql
-- Thống kê đơn hàng theo trạng thái trong một query duy nhất
SELECT
    COUNT(*)                                          AS total_orders,
    COUNT(CASE WHEN order_status = 'PAID'      THEN 1 END) AS paid,
    COUNT(CASE WHEN order_status = 'PENDING'   THEN 1 END) AS pending,
    COUNT(CASE WHEN order_status = 'CANCELLED' THEN 1 END) AS cancelled,
    COUNT(CASE WHEN order_status = 'FAILED'    THEN 1 END) AS failed,
    SUM(CASE WHEN order_status = 'PAID'
             THEN final_amount ELSE 0 END)            AS paid_revenue
FROM orders;
```

Kết quả gọn hơn rất nhiều so với chạy 4-5 query riêng lẻ:


| total_orders | paid | pending | cancelled | failed | paid_revenue |
|---|---|---|---|---|---|
| 7 | 4 | 1 | 1 | 0 | 3087000 |



### Thống kê theo nhóm với CASE WHEN

```sql
-- Phân tích học viên theo thời gian đăng ký
SELECT
    COUNT(*)                                                    AS total_users,
    COUNT(CASE WHEN created_at >= NOW() - INTERVAL '7 days'
               THEN 1 END)                                      AS new_this_week,
    COUNT(CASE WHEN created_at >= NOW() - INTERVAL '30 days'
               THEN 1 END)                                      AS new_this_month,
    COUNT(CASE WHEN email_verified = TRUE  THEN 1 END)          AS verified,
    COUNT(CASE WHEN email_verified = FALSE THEN 1 END)          AS unverified,
    COUNT(CASE WHEN account_status = 'ACTIVE'   THEN 1 END)    AS active,
    COUNT(CASE WHEN account_status = 'INACTIVE' THEN 1 END)    AS inactive,
    COUNT(CASE WHEN account_status = 'BANNED'   THEN 1 END)    AS banned
FROM users;
```

## 6\. PIVOT với CASE WHEN — Chuyển dữ liệu dọc thành ngang

Đây là một trong những ứng dụng mạnh nhất của CASE WHEN. **Pivot** là kỹ thuật chuyển các giá trị từ một cột thành nhiều cột.

**Bài toán:** Thống kê doanh thu theo từng tháng trong năm 2025, mỗi tháng là một cột riêng.

```sql
-- Dữ liệu gốc (dạng dọc)
SELECT
    EXTRACT(MONTH FROM created_at) AS month,
    SUM(final_amount) AS revenue
FROM orders
WHERE order_status = 'PAID'
  AND EXTRACT(YEAR FROM created_at) = 2025
GROUP BY EXTRACT(MONTH FROM created_at);
```


| month | revenue |
|---|---|
| 1 | 1500000 |
| 2 | 2100000 |
| 3 | 1800000 |



```sql
-- Sau khi PIVOT (dạng ngang) dùng CASE WHEN
SELECT
    SUM(CASE WHEN EXTRACT(MONTH FROM created_at) = 1
             THEN final_amount ELSE 0 END) AS "T1",
    SUM(CASE WHEN EXTRACT(MONTH FROM created_at) = 2
             THEN final_amount ELSE 0 END) AS "T2",
    SUM(CASE WHEN EXTRACT(MONTH FROM created_at) = 3
             THEN final_amount ELSE 0 END) AS "T3",
    SUM(CASE WHEN EXTRACT(MONTH FROM created_at) = 4
             THEN final_amount ELSE 0 END) AS "T4",
    SUM(CASE WHEN EXTRACT(MONTH FROM created_at) = 5
             THEN final_amount ELSE 0 END) AS "T5",
    SUM(CASE WHEN EXTRACT(MONTH FROM created_at) = 6
             THEN final_amount ELSE 0 END) AS "T6",
    -- ... tương tự đến T12
    SUM(CASE WHEN order_status = 'PAID'
             THEN final_amount ELSE 0 END) AS "Tổng"
FROM orders
WHERE EXTRACT(YEAR FROM created_at) = 2025;
```


| T1 | T2 | T3 | T4 | T5 | T6 | Tổng |
|---|---|---|---|---|---|---|
| 1500000 | 2100000 | 1800000 | 0 | 0 | 0 | 5400000 |



## 7\. CASE WHEN với Window Functions

Kết hợp CASE WHEN với Window Functions để phân loại dựa trên rank:

```sql
-- Gắn nhãn top/bottom cho từng khóa học theo doanh thu
WITH course_revenue AS (
    SELECT
        c.title,
        SUM(oi.price)  AS total_revenue,
        RANK() OVER (ORDER BY SUM(oi.price) DESC) AS rev_rank,
        COUNT(*)       OVER ()                     AS total_courses
    FROM order_items oi
    JOIN courses c ON c.id = oi.course_id
    JOIN orders  o ON o.id = oi.order_id
    WHERE o.order_status = 'PAID'
    GROUP BY c.id, c.title
)
SELECT
    title,
    total_revenue,
    rev_rank,
    CASE
        WHEN rev_rank = 1                        THEN '🥇 Best Seller'
        WHEN rev_rank <= CEIL(total_courses * 0.25) THEN '🔥 Top 25%'
        WHEN rev_rank >= total_courses            THEN '⚠️ Cần cải thiện'
        ELSE                                          '📚 Bình thường'
    END AS performance_label
FROM course_revenue
ORDER BY rev_rank;
```

## 8\. CASE WHEN để xử lý NULL

```sql
-- Hiển thị thông tin user, xử lý các trường có thể NULL
SELECT
    first_name || ' ' || last_name AS full_name,
    CASE
        WHEN phone IS NULL    THEN 'Chưa cập nhật SĐT'
        ELSE phone
    END AS phone_display,
    CASE
        WHEN birth_date IS NULL THEN 'Chưa cập nhật'
        ELSE TO_CHAR(birth_date, 'DD/MM/YYYY')
    END AS birth_date_display,
    CASE
        WHEN email_verified = TRUE  THEN '✓ Đã xác thực'
        WHEN email_verified = FALSE THEN '✗ Chưa xác thực'
    END AS verification_status
FROM users
WHERE account_status = 'ACTIVE';
```

## 9\. Lồng CASE WHEN

CASE WHEN có thể lồng nhau, nhưng nên hạn chế vì dễ mất khả năng đọc hiểu:

```sql
SELECT
    title,
    course_type,
    price,
    CASE
        WHEN course_type = 'FREE' THEN 'Miễn phí'
        WHEN course_type = 'PAID' THEN
            CASE
                WHEN price < 500000  THEN 'Giá thấp'
                WHEN price < 800000  THEN 'Giá trung bình'
                ELSE                      'Giá cao'
            END
        ELSE course_type
    END AS price_label
FROM courses;
```

> **Lời khuyên:** Nếu CASE WHEN lồng nhau quá 2 cấp, hãy cân nhắc dùng CTE để tách logic thành từng bước rõ ràng hơn.

## 10\. Thực hành tổng hợp

**Bài 1:** Phân loại các bài viết theo mức độ phổ biến dựa trên view\_count, đồng thời thống kê số bài ở mỗi mức.

```sql
SELECT
    CASE
        WHEN view_count >= 10000 THEN 'Viral'
        WHEN view_count >= 5000  THEN 'Phổ biến'
        WHEN view_count >= 1000  THEN 'Tốt'
        WHEN view_count >= 100   THEN 'Bình thường'
        ELSE                          'Chưa có traffic'
    END                    AS popularity_tier,
    COUNT(*)               AS total_posts,
    SUM(view_count)        AS total_views,
    ROUND(AVG(view_count)) AS avg_views
FROM posts
WHERE post_status = 'PUBLISHED'
GROUP BY
    CASE
        WHEN view_count >= 10000 THEN 'Viral'
        WHEN view_count >= 5000  THEN 'Phổ biến'
        WHEN view_count >= 1000  THEN 'Tốt'
        WHEN view_count >= 100   THEN 'Bình thường'
        ELSE                          'Chưa có traffic'
    END
ORDER BY avg_views DESC;
```

**Bài 2:** Tạo báo cáo tình trạng liên hệ (contacts) — số lượng theo từng trạng thái và thời gian xử lý trung bình.

```sql
SELECT
    contact_status,
    COUNT(*)                                         AS total,
    ROUND(AVG(
        CASE
            WHEN resolved_at IS NOT NULL
            THEN EXTRACT(EPOCH FROM (resolved_at - created_at)) / 3600
        END
    ), 1)                                            AS avg_resolve_hours,
    COUNT(CASE WHEN resolved_at IS NULL
               AND created_at < NOW() - INTERVAL '24 hours'
               THEN 1 END)                           AS overdue_count
FROM contacts
GROUP BY contact_status
ORDER BY
    CASE contact_status
        WHEN 'PENDING'    THEN 1
        WHEN 'RESPONDED'  THEN 2
        WHEN 'RESOLVED'   THEN 3
    END;
```

**Bài 3:** Tạo bảng phân tích cohort đơn giản — phân loại học viên theo thời gian đăng ký và số khóa đã mua.

```sql
WITH student_stats AS (
    SELECT
        u.id,
        u.first_name || ' ' || u.last_name AS name,
        u.created_at,
        COUNT(DISTINCT oi.course_id)        AS courses_bought,
        COALESCE(SUM(o.final_amount), 0)    AS total_spent
    FROM users u
    LEFT JOIN orders      o  ON o.user_id = u.id
                             AND o.order_status = 'PAID'
    LEFT JOIN order_items oi ON oi.order_id = o.id
    GROUP BY u.id, u.first_name, u.last_name, u.created_at
)
SELECT
    name,
    CASE
        WHEN created_at >= NOW() - INTERVAL '30 days'  THEN 'Mới (< 30 ngày)'
        WHEN created_at >= NOW() - INTERVAL '90 days'  THEN 'Gần đây (30-90 ngày)'
        WHEN created_at >= NOW() - INTERVAL '365 days' THEN 'Cũ (90-365 ngày)'
        ELSE                                                 'Lâu năm (> 1 năm)'
    END       AS user_cohort,
    CASE
        WHEN courses_bought = 0 THEN 'Chưa mua'
        WHEN courses_bought = 1 THEN 'Mới bắt đầu'
        WHEN courses_bought <= 3 THEN 'Học đều'
        ELSE                         'Học viên tích cực'
    END       AS learning_tier,
    courses_bought,
    total_spent
FROM student_stats
ORDER BY total_spent DESC;
```

## Tổng kết


| Vị trí dùng | Mục đích |
|---|---|
| SELECT | Phân loại, gán nhãn, chuyển đổi giá trị hiển thị |
| WHERE | Điều kiện lọc động theo logic phức tạp |
| ORDER BY | Sắp xếp theo thứ tự tùy chỉnh, không theo alphabet |
| GROUP BY | Nhóm theo danh mục được tính toán |
| Aggregate | Đếm/tính tổng có điều kiện trong một query |
| PIVOT | Chuyển dữ liệu dọc thành ngang |



Bài tiếp theo chúng ta sẽ học **DML — INSERT, UPDATE, DELETE** — hoàn thiện nhóm Intermediate với các thao tác ghi dữ liệu vào database.

> **Khác biệt với các RDBMS khác:**
> 
> *   **Tất cả RDBMS phổ biến** đều hỗ trợ `CASE WHEN` với cú pháp giống hệt nhau — đây là một trong số ít tính năng SQL hoàn toàn portable
>     
> *   **SQL Server:** Có thêm hàm `IIF(condition, true_val, false_val)` là cách viết tắt của CASE WHEN 2 nhánh
>     
> *   **MySQL / Oracle / SQL Server:** Có hàm `DECODE()` (Oracle) và `IF()` (MySQL) tương tự CASE WHEN nhưng kém linh hoạt hơn — nên dùng CASE WHEN cho chuẩn
>     

