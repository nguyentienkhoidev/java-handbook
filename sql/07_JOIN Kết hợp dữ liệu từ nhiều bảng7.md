# JOIN: Kết hợp dữ liệu từ nhiều bảng

![JOIN - Kết hợp dữ liệu từ nhiều bảng.jpeg](../images/26ac96bd-1241-480a-aa04-6a2038d392d1.jpeg)

Trong thực tế, dữ liệu không bao giờ nằm gọn trong một bảng duy nhất. Thông tin học viên ở bảng `users`, đơn hàng ở bảng `orders`, khóa học ở bảng `courses` — muốn lấy "học viên nào đã mua khóa học nào với giá bao nhiêu" thì phải kết hợp cả 3 bảng lại. Đó chính là việc JOIN làm.

## 1\. Tại sao cần JOIN?

Hãy xem bảng `orders` trong nguyentienkhoi.hashnode.dev:


| id | user_id | order_status | final_amount |
|---|---|---|---|
| 1 | 1 | PAID | 799000 |
| 2 | 1 | PAID | 599000 |
| 3 | 2 | PAID | 899000 |



Bảng này chỉ lưu `user_id = 1` — không có tên, email của học viên. Muốn biết tên học viên, phải sang bảng `users`. Đây là lý do JOIN ra đời — **kết hợp dữ liệu từ nhiều bảng dựa trên cột liên kết chung**.

## 2\. INNER JOIN — Chỉ lấy dòng khớp ở cả hai bảng

`INNER JOIN` (hay viết tắt là `JOIN`) trả về các dòng **có giá trị khớp ở cả hai bảng**. Dòng nào không khớp sẽ bị loại bỏ hoàn toàn.

```sql
SELECT cot1, cot2, ...
FROM bang_trai
INNER JOIN bang_phai ON bang_trai.khoa = bang_phai.khoa_ngoai;
```

**Ví dụ** — Lấy danh sách đơn hàng kèm tên học viên:

```sql
SELECT
    o.id          AS order_id,
    u.first_name,
    u.last_name,
    u.email,
    o.order_status,
    o.final_amount
FROM orders o
INNER JOIN users u ON u.id = o.user_id
ORDER BY o.created_at DESC;
```

Kết quả:


| order_id | first_name | last_name | email | order_status | final_amount |
|---|---|---|---|---|---|
| 1 | Nam | Nguyen | nam@gmail.com | PAID | 799000 |
| 2 | Nam | Nguyen | nam@gmail.com | PAID | 599000 |
| 3 | Linh | Tran | linh@gmail.com | PAID | 899000 |



> Đơn hàng của `user_id = 5` (Tuan) có `order_status = 'PENDING'` vẫn xuất hiện vì user tồn tại — INNER JOIN chỉ loại bỏ khi không tìm thấy bản ghi khớp ở bảng kia.

## 3\. LEFT JOIN — Giữ toàn bộ bảng trái

`LEFT JOIN` trả về **tất cả dòng từ bảng trái**, dù có khớp hay không ở bảng phải. Nếu không khớp, các cột từ bảng phải sẽ là `NULL`.

```sql
SELECT cot1, cot2
FROM bang_trai
LEFT JOIN bang_phai ON bang_trai.khoa = bang_phai.khoa_ngoai;
```

**Ví dụ** — Lấy tất cả học viên, kể cả người chưa từng đặt đơn hàng:

```sql
SELECT
    u.first_name,
    u.last_name,
    u.email,
    COUNT(o.id)        AS total_orders,
    SUM(o.final_amount) AS total_spent
FROM users u
LEFT JOIN orders o ON o.user_id = u.id AND o.order_status = 'PAID'
GROUP BY u.id, u.first_name, u.last_name, u.email
ORDER BY total_spent DESC NULLS LAST;
```

Kết quả — học viên chưa mua hàng vẫn xuất hiện với `total_orders = 0`:


| first_name | last_name | email | total_orders | total_spent |
|---|---|---|---|---|
| Nam | Nguyen | nam@gmail.com | 2 | 1398000 |
| Linh | Tran | linh@gmail.com | 1 | 899000 |
| Minh | Le | minh@gmail.com | 1 | 599000 |
| Huong | Pham | huong@gmail.com | 1 | 799000 |
| Tuan | Vu | tuan@gmail.com | 0 | NULL |



**Dùng LEFT JOIN để tìm dòng không có bản ghi khớp:**

```sql
-- Tìm học viên chưa từng đặt đơn hàng nào
SELECT
    u.first_name,
    u.last_name,
    u.email
FROM users u
LEFT JOIN orders o ON o.user_id = u.id
WHERE o.id IS NULL;
```

Đây là pattern rất phổ biến: `LEFT JOIN ... WHERE bang_phai.id IS NULL` — tìm các bản ghi **không tồn tại** ở bảng phải.

## 4\. RIGHT JOIN — Giữ toàn bộ bảng phải

`RIGHT JOIN` là ngược lại của `LEFT JOIN` — giữ toàn bộ bảng phải, bảng trái có thể NULL.

```sql
SELECT
    c.title,
    oi.price,
    o.order_status
FROM order_items oi
RIGHT JOIN courses c ON c.id = oi.course_id;
```

Trả về tất cả khóa học, kể cả khóa chưa có ai mua.

> **Thực tế:** `RIGHT JOIN` ít được dùng vì bạn luôn có thể đổi thứ tự bảng và dùng `LEFT JOIN` thay thế cho dễ đọc hơn. Hầu hết developer chỉ dùng `LEFT JOIN`.

## 5\. FULL OUTER JOIN — Giữ tất cả dòng từ cả hai bảng

`FULL OUTER JOIN` giữ **toàn bộ dòng từ cả hai bảng**. Dòng nào không khớp ở bảng kia thì cột đó là NULL.

```sql
SELECT
    u.email,
    o.id AS order_id,
    o.final_amount
FROM users u
FULL OUTER JOIN orders o ON o.user_id = u.id;
```

Trả về: tất cả user (kể cả chưa có đơn) + tất cả đơn hàng (kể cả đơn không có user — dù trường hợp này hiếm khi xảy ra nếu có foreign key).

> `FULL OUTER JOIN` ít dùng trong thực tế, thường gặp khi cần **reconcile** dữ liệu từ 2 nguồn khác nhau.

## 6\. CROSS JOIN — Tích Đề-các

`CROSS JOIN` kết hợp **mỗi dòng** của bảng trái với **mỗi dòng** của bảng phải — không cần điều kiện ON. Kết quả có `m × n` dòng.

```sql
-- 5 users × 5 courses = 25 dòng
SELECT u.first_name, c.title
FROM users u
CROSS JOIN courses c;
```

> Dùng khi cần tạo tổ hợp tất cả các cặp — ví dụ tạo ma trận giá theo từng quốc gia và từng loại currency.

## 7\. SELF JOIN — Join một bảng với chính nó

`SELF JOIN` join bảng với chính bảng đó — dùng khi bảng có quan hệ đệ quy (cha-con, quản lý-nhân viên).

Trong nguyentienkhoi.hashnode.dev, bảng `categories` có `parent_id` trỏ về chính bảng đó:

```sql
-- Lấy danh sách category con kèm tên category cha
SELECT
    child.name  AS category_name,
    parent.name AS parent_name
FROM categories child
LEFT JOIN categories parent ON parent.id = child.parent_id
ORDER BY parent.name NULLS FIRST, child.name;
```

Kết quả:


| category_name | parent_name |
|---|---|
| Database | NULL |
| DevOps | NULL |
| Java | NULL |
| SQL | Database |
| NoSQL | Database |
| Java Core | Java |
| Spring Boot | Java |
| Microservices | Java |



## 8\. JOIN nhiều bảng

Trong thực tế bạn thường phải JOIN 3, 4 bảng cùng lúc:

**Ví dụ** — Lấy chi tiết đơn hàng: tên học viên + tên khóa học + giá:

```sql
SELECT
    u.first_name || ' ' || u.last_name AS student_name,
    c.title                             AS course_title,
    oi.price,
    o.order_status,
    o.created_at
FROM orders o
JOIN users       u  ON u.id  = o.user_id
JOIN order_items oi ON oi.order_id = o.id
JOIN courses     c  ON c.id  = oi.course_id
WHERE o.order_status = 'PAID'
ORDER BY o.created_at DESC;
```

Kết quả:


| student_name | course_title | price | order_status |
|---|---|---|---|
| Nam Nguyen | Spring Boot từ Zero đến Hero | 799000 | PAID |
| Nam Nguyen | SQL cho Developer | 599000 | PAID |
| Linh Tran | Docker & Kubernetes thực chiến | 899000 | PAID |
| Minh Le | SQL cho Developer | 599000 | PAID |
| Huong Pham | Spring Boot từ Zero đến Hero | 799000 | PAID |



**Ví dụ phức tạp hơn** — Báo cáo tiến độ học: học viên + khóa học + số bài đã hoàn thành:

```sql
SELECT
    u.first_name || ' ' || u.last_name  AS student_name,
    c.title                              AS course_title,
    COUNT(tp.lecture_id)                 AS completed_lectures,
    c.total_lectures,
    ROUND(
        COUNT(tp.lecture_id) * 100.0 / NULLIF(c.total_lectures, 0),
        1
    )                                    AS progress_percent
FROM enrollments e
JOIN users    u  ON u.id = e.user_id
JOIN courses  c  ON c.id = e.course_id
LEFT JOIN tracking_progress tp
       ON tp.student_id = e.user_id
      AND tp.lecture_id IN (
              SELECT id FROM lectures WHERE course_id = c.id
          )
      AND tp.completed = TRUE
GROUP BY u.id, u.first_name, u.last_name, c.id, c.title, c.total_lectures
ORDER BY student_name, progress_percent DESC;
```

## 9\. Những lỗi phổ biến khi JOIN

### Lỗi 1: Duplicate rows do JOIN nhiều bảng

```sql
-- ❌ Có thể bị duplicate nếu một order có nhiều order_items
SELECT o.id, u.email, SUM(o.final_amount)
FROM orders o
JOIN users       u  ON u.id = o.user_id
JOIN order_items oi ON oi.order_id = o.id  -- mỗi order có thể có nhiều items
GROUP BY o.id, u.email;
-- final_amount bị nhân lên theo số order_items!

-- ✅ Tính SUM trên đúng bảng cần tính
SELECT o.id, u.email, SUM(oi.price) AS total
FROM orders o
JOIN users       u  ON u.id = o.user_id
JOIN order_items oi ON oi.order_id = o.id
GROUP BY o.id, u.email;
```

### Lỗi 2: Quên điều kiện ON — tạo ra CROSS JOIN không mong muốn

```sql
-- ❌ Thiếu ON → CROSS JOIN → hàng triệu dòng
SELECT * FROM orders, users;

-- ✅ Luôn có điều kiện JOIN rõ ràng
SELECT * FROM orders o JOIN users u ON u.id = o.user_id;
```

### Lỗi 3: Không dùng alias khi JOIN nhiều bảng

```sql
-- ❌ Khó đọc, dễ nhầm lẫn khi nhiều bảng có cột cùng tên
SELECT id, first_name, final_amount
FROM orders
JOIN users ON users.id = orders.user_id;
-- id ở đây là của bảng nào?

-- ✅ Luôn dùng alias và prefix tên bảng
SELECT o.id, u.first_name, o.final_amount
FROM orders o
JOIN users u ON u.id = o.user_id;
```

## 10\. Tổng quan các loại JOIN

```java
INNER JOIN          LEFT JOIN           RIGHT JOIN         FULL OUTER JOIN
                                                           
   A  ∩  B             A  ⊇  B             A  ⊆  B            A  ∪  B
  ┌──┬──┐             ┌──┬──┐             ┌──┬──┐            ┌──┬──┐
  │  │██│             │██│██│             │  │██│            │██│██│
  │  │██│             │██│██│             │  │██│            │██│██│
  └──┴──┘             └──┴──┘             └──┴──┘            └──┴──┘
Chỉ dòng khớp    Tất cả A + khớp B   Tất cả B + khớp A   Tất cả A và B
```


| Loại JOIN | Giữ lại | Dùng khi nào |
|---|---|---|
| INNER JOIN | Chỉ dòng khớp cả hai | Lấy dữ liệu liên quan chặt chẽ |
| LEFT JOIN | Tất cả bảng trái | Bảng trái là chủ thể chính, bảng phải là thông tin bổ sung |
| RIGHT JOIN | Tất cả bảng phải | Ít dùng — đổi thứ tự và dùng LEFT JOIN cho dễ đọc |
| FULL OUTER JOIN | Tất cả cả hai | Reconcile 2 nguồn dữ liệu |
| CROSS JOIN | Tích Đề-các | Tạo tổ hợp tất cả cặp giá trị |
| SELF JOIN | Quan hệ trong cùng bảng | Dữ liệu phân cấp cha-con |



## 11\. Thực hành tổng hợp

**Bài 1:** Lấy danh sách học viên và rating họ đã để lại cho khóa học, kèm tên khóa học.

```sql
SELECT
    u.first_name || ' ' || u.last_name AS student_name,
    c.title                             AS course_title,
    r.rate,
    r.review,
    r.created_at
FROM ratings r
JOIN users   u ON u.id = r.reviewer_id
JOIN courses c ON c.id = r.course_id
ORDER BY r.created_at DESC;
```

**Bài 2:** Lấy danh sách tất cả khóa học kèm số lượng đánh giá và rating trung bình — kể cả khóa chưa có đánh giá nào.

```sql
SELECT
    c.title,
    COUNT(r.id)           AS total_ratings,
    ROUND(AVG(r.rate), 1) AS avg_rating
FROM courses c
LEFT JOIN ratings r ON r.course_id = c.id
GROUP BY c.id, c.title
ORDER BY avg_rating DESC NULLS LAST;
```

**Bài 3:** Lấy danh sách các category cha kèm số lượng category con.

```sql
SELECT
    parent.name          AS parent_category,
    COUNT(child.id)      AS total_children
FROM categories parent
LEFT JOIN categories child ON child.parent_id = parent.id
WHERE parent.parent_id IS NULL  -- chỉ lấy category gốc
GROUP BY parent.id, parent.name
ORDER BY total_children DESC;
```

## Tổng kết

*   **INNER JOIN** — chỉ lấy dòng khớp ở cả hai bảng, loại bỏ dòng không khớp
    
*   **LEFT JOIN** — giữ toàn bộ bảng trái, bảng phải NULL nếu không khớp — hay dùng nhất
    
*   **RIGHT JOIN** — ngược lại LEFT JOIN, ít dùng trong thực tế
    
*   **FULL OUTER JOIN** — giữ tất cả từ cả hai bảng
    
*   **SELF JOIN** — join bảng với chính nó, dùng cho dữ liệu phân cấp
    
*   Luôn dùng **alias** và **prefix tên bảng** khi JOIN để tránh nhầm lẫn
    
*   Cẩn thận **duplicate rows** khi JOIN với bảng có quan hệ one-to-many
    

Bài tiếp theo chúng ta bước sang **Intermediate** với **Subquery** — cách nhúng một câu query vào bên trong một câu query khác để giải quyết những bài toán phức tạp hơn.

> **Khác biệt với các RDBMS khác:**
> 
> *   **Tất cả RDBMS phổ biến** đều hỗ trợ INNER, LEFT, RIGHT, FULL OUTER, CROSS JOIN với cú pháp giống nhau
>     
> *   **MySQL:** Trước version 8.0 không hỗ trợ `FULL OUTER JOIN` — phải dùng `LEFT JOIN UNION RIGHT JOIN` để giả lập
>     
> *   **Oracle:** Có cú pháp JOIN cũ dùng dấu `(+)` — ví dụ `WHERE a.id = b.id(+)` tương đương `LEFT JOIN`, nhưng cú pháp mới dùng `JOIN ... ON` như các RDBMS khác
>     

