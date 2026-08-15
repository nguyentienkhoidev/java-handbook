# SELECT, FROM, WHERE: Truy vấn dữ liệu cơ bản

![](../images/caf43d30-166f-4f0d-9439-45a724c069b6.png)

`SELECT` là câu lệnh bạn sẽ dùng nhiều nhất trong suốt sự nghiệp làm việc với database. Bài này sẽ đi từ cú pháp đơn giản nhất đến các kỹ thuật lọc, sắp xếp và giới hạn kết quả — tất cả đều thực hành trực tiếp trên database [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev).

## 1\. Cú pháp cơ bản của SELECT

```sql
SELECT column1, column2
FROM table_name;
```

Ví dụ — lấy danh sách học viên trên [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev):

```sql
SELECT id, first_name, last_name, email
FROM users;
```

Kết quả:


| id | first_name | last_name | email |
|---|---|---|---|
| 1 | Nam | Nguyen | nam@gmail.com |
| 2 | Linh | Tran | linh@gmail.com |
| 3 | Minh | Le | minh@gmail.com |
| 4 | Huong | Pham | huong@gmail.com |
| 5 | Tuan | Vu | tuan@gmail.com |



## 2\. SELECT \* — Lấy tất cả các cột

Dấu `*` có nghĩa là lấy toàn bộ cột trong bảng:

```sql
SELECT * FROM courses;
```

> **Lưu ý thực tế:** `SELECT *` tiện khi khám phá dữ liệu, nhưng trong code production bạn nên **liệt kê rõ từng cột** cần dùng. Lý do: tránh lấy thừa dữ liệu, tránh lỗi khi schema thay đổi, và dễ đọc code hơn. Đây là một trong những anti-pattern phổ biến nhất — FoxDev sẽ nói kỹ hơn ở bài Senior.

## 3\. WHERE — Lọc dữ liệu theo điều kiện

`WHERE` giúp bạn lọc chỉ lấy những dòng thỏa mãn điều kiện:

```sql
SELECT column1, column2
FROM table_name
WHERE condition;
```

**Ví dụ 1** — Lấy các khóa học miễn phí:

```sql
SELECT id, title, course_type
FROM courses
WHERE course_type = 'FREE';
```

**Ví dụ 2** — Lấy các học viên đang ACTIVE:

```sql
SELECT id, first_name, last_name, email
FROM users
WHERE account_status = 'ACTIVE';
```

**Ví dụ 3** — Lấy các đơn hàng đã thanh toán:

```sql
SELECT id, user_id, final_amount, created_at
FROM orders
WHERE order_status = 'PAID';
```

### Các toán tử so sánh trong WHERE


| Toán tử | Ý nghĩa | Ví dụ |
|---|---|---|
| = | Bằng | course_type = 'PAID' |
| != hoặc <> | Khác | order_status != 'CANCELLED' |
| > | Lớn hơn | price > 500000 |
| < | Nhỏ hơn | price < 500000 |
| >= | Lớn hơn hoặc bằng | enrolled_count >= 200 |
| <= | Nhỏ hơn hoặc bằng | rating <= 4.5 |



**Ví dụ** — Lấy các khóa học có giá trên 700.000đ:

```sql
SELECT title, price
FROM courses
WHERE price > 700000;
```

Kết quả:


| title | price |
|---|---|
| Spring Boot từ Zero đến Hero | 799000 |
| Docker & Kubernetes thực chiến | 899000 |



## 4\. AND, OR — Kết hợp nhiều điều kiện

Dùng `AND` khi **tất cả** điều kiện phải đúng, `OR` khi **ít nhất một** điều kiện đúng:

```sql
-- AND: Khóa học PAID và có rating >= 4.7
SELECT title, course_type, price, rating
FROM courses
WHERE course_type = 'PAID'
  AND rating >= 4.7;
```

Kết quả:


| title | course_type | price | rating |
|---|---|---|---|
| Spring Boot từ Zero đến Hero | PAID | 799000 | 4.8 |
| Docker & Kubernetes thực chiến | PAID | 899000 | 4.7 |



```sql
-- OR: Đơn hàng bị huỷ HOẶC đang pending
SELECT id, user_id, order_status, final_amount
FROM orders
WHERE order_status = 'CANCELLED'
   OR order_status = 'PENDING';
```

> **Thứ tự ưu tiên:** `AND` được thực thi trước `OR` — tương tự nhân/chia trước cộng/trừ trong toán học. Dùng dấu ngoặc `()` để đảm bảo logic đúng ý muốn:

```sql
-- Sai ý (AND được ưu tiên trước)
WHERE course_type = 'FREE' OR price > 700000 AND rating > 4.7

-- Đúng ý (dùng ngoặc để rõ ràng)
WHERE (course_type = 'FREE' OR price > 700000) AND rating > 4.7
```

## 5\. ORDER BY — Sắp xếp kết quả

```sql
SELECT column1, column2
FROM table_name
ORDER BY column1 ASC;   -- ASC: tăng dần (mặc định)
                        -- DESC: giảm dần
```

**Ví dụ 1** — Sắp xếp khóa học theo giá từ cao đến thấp:

```sql
SELECT title, price, enrolled_count
FROM courses
ORDER BY price DESC;
```

Kết quả:


| title | price | enrolled_count |
|---|---|---|
| Docker & Kubernetes thực chiến | 899000 | 180 |
| Spring Boot từ Zero đến Hero | 799000 | 320 |
| ReactJS cơ bản đến nâng cao | 699000 | 150 |
| SQL cho Developer | 599000 | 210 |
| Java Core nền tảng | 0 | 500 |



**Ví dụ 2** — Sắp xếp theo nhiều cột: rating giảm dần, nếu bằng nhau thì sắp xếp theo enrolled\_count giảm dần:

```sql
SELECT title, rating, enrolled_count
FROM courses
ORDER BY rating DESC, enrolled_count DESC;
```

* * *

## 6\. LIMIT và OFFSET — Giới hạn số dòng trả về

`LIMIT` giới hạn số dòng kết quả, `OFFSET` bỏ qua N dòng đầu tiên — thường dùng để làm **pagination**:

```sql
-- Lấy 3 khóa học đầu tiên
SELECT title, enrolled_count
FROM courses
ORDER BY enrolled_count DESC
LIMIT 3;
```

Kết quả:


| title | enrolled_count |
|---|---|
| Java Core nền tảng | 500 |
| Spring Boot từ Zero đến Hero | 320 |
| SQL cho Developer | 210 |



```sql
-- Pagination: trang 2, mỗi trang 3 kết quả
-- OFFSET = (page - 1) * page_size = (2 - 1) * 3 = 3
SELECT title, enrolled_count
FROM courses
ORDER BY enrolled_count DESC
LIMIT 3 OFFSET 3;
```

> **Lưu ý:** `OFFSET` lớn sẽ gây chậm vì database vẫn phải đọc và bỏ qua toàn bộ N dòng đầu. Tao sẽ đề cập cách pagination hiệu quả hơn ở bài Senior.

## 7\. AS — Đặt tên alias cho cột

`AS` giúp bạn đổi tên cột trong kết quả trả về — hữu ích khi tên cột gốc khó đọc hoặc cần format lại:

```sql
SELECT
    first_name AS "Tên",
    last_name  AS "Họ",
    email      AS "Email"
FROM users;
```

Kết quả:


| Tên | Họ | Email |
|---|---|---|
| Nam | Nguyen | nam@gmail.com |
| Linh | Tran | linh@gmail.com |



Alias còn dùng để đặt tên cho **biểu thức tính toán**:

```sql
SELECT
    title,
    price                            AS gia_goc,
    ROUND(price * 0.9)               AS gia_sau_giam_10,
    enrolled_count * price           AS uoc_tinh_doanh_thu
FROM courses
WHERE course_type = 'PAID';
```

* * *

## 8\. DISTINCT — Loại bỏ giá trị trùng lặp

`DISTINCT` trả về các giá trị duy nhất, loại bỏ các dòng trùng nhau:

```sql
-- Có những loại trạng thái nào đang tồn tại trong bảng orders?
SELECT DISTINCT order_status
FROM orders;
```

Kết quả:


| order_status |
|---|
| PAID |
| CANCELLED |
| PENDING |



```sql
-- Có những loại khóa học nào?
SELECT DISTINCT course_type
FROM courses;
```

## 9\. Thứ tự thực thi của một câu SELECT

Đây là điểm nhiều người mới hay nhầm — **thứ tự viết** và **thứ tự thực thi** của SQL khác nhau:

```sql
SELECT title, price      -- 5. Chọn cột cần lấy
FROM courses             -- 1. Xác định bảng
WHERE price > 500000     -- 2. Lọc dòng
ORDER BY price DESC      -- 3. Sắp xếp
LIMIT 3;                 -- 4. Giới hạn kết quả
```

Thứ tự thực thi thực sự:

1.  `FROM` — xác định bảng nguồn
    
2.  `WHERE` — lọc dòng
    
3.  `ORDER BY` — sắp xếp
    
4.  `LIMIT` — cắt kết quả
    
5.  `SELECT` — chọn cột trả về
    

> Hiểu thứ tự này giúp bạn tránh lỗi như dùng alias trong `WHERE` (không được vì `SELECT` chạy sau `WHERE`).

## 10\. Thực hành tổng hợp

Hãy thử viết các query sau trên database thực hành:

**Bài 1:** Lấy danh sách khóa học PAID, sắp xếp theo rating giảm dần, chỉ lấy top 3.

```sql
SELECT title, price, rating
FROM courses
WHERE course_type = 'PAID'
ORDER BY rating DESC
LIMIT 3;
```

**Bài 2:** Lấy danh sách đơn hàng đã PAID có giá trị trên 700.000đ, sắp xếp theo thời gian tạo mới nhất.

```sql
SELECT id, user_id, final_amount, created_at
FROM orders
WHERE order_status = 'PAID'
  AND final_amount > 700000
ORDER BY created_at DESC;
```

**Bài 3:** Lấy tên và email của học viên ACTIVE, sắp xếp theo họ (last\_name) từ A-Z.

```sql
SELECT first_name, last_name, email
FROM users
WHERE account_status = 'ACTIVE'
ORDER BY last_name ASC;
```

## Tổng kết


| Keyword | Chức năng |
|---|---|
| SELECT | Chọn cột cần lấy |
| FROM | Xác định bảng nguồn |
| WHERE | Lọc dòng theo điều kiện |
| AND / OR | Kết hợp nhiều điều kiện |
| ORDER BY | Sắp xếp kết quả |
| LIMIT / OFFSET | Giới hạn số dòng, dùng cho pagination |
| AS | Đặt alias cho cột |
| DISTINCT | Loại bỏ giá trị trùng lặp |



Bài tiếp theo chúng ta sẽ tìm hiểu về **kiểu dữ liệu** trong PostgreSQL và cách xử lý giá trị `NULL` — thứ gây ra không ít bug bất ngờ nếu bạn không hiểu rõ bản chất của nó.

> **Khác biệt với các RDBMS khác:**
> 
> *   **MySQL:** Dùng `LIMIT x, y` thay vì `LIMIT x OFFSET y` — ví dụ `LIMIT 3, 10` nghĩa là bỏ qua 3 dòng, lấy 10 dòng tiếp theo
>     
> *   **SQL Server:** Dùng `TOP` thay vì `LIMIT` — ví dụ `SELECT TOP 3 * FROM courses`
>     
> *   **Oracle:** Dùng `FETCH FIRST 3 ROWS ONLY` hoặc `ROWNUM <= 3`
>     

