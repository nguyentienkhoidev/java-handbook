# SQL là gì? Tại sao developer nào cũng cần biết SQL?

Dù bạn là backend developer, frontend developer, data analyst hay DevOps — SQL là kỹ năng gần như bắt buộc trong ngành. Bài này sẽ giải thích SQL là gì, hoạt động như thế nào, và tại sao bạn nên học nó ngay hôm nay.

## 1\. SQL là gì?

**SQL** (Structured Query Language) là ngôn ngữ dùng để giao tiếp với cơ sở dữ liệu quan hệ (Relational Database). Nói đơn giản hơn — SQL là công cụ để bạn **hỏi** database những câu hỏi như:

*   _"Cho tôi danh sách tất cả học viên đã đăng ký khóa học Spring Boot"_
    
*   _"Tổng doanh thu tháng này là bao nhiêu?"_
    
*   _"Ai là top 10 học viên học nhiều nhất?"_
    

SQL ra đời từ những năm 1970 tại IBM, và đến nay vẫn là ngôn ngữ truy vấn dữ liệu phổ biến nhất thế giới — sau hơn 50 năm vẫn chưa có gì thay thế được.

## 2\. Database là gì? Tại sao cần database?

Hãy hình dung bạn đang quản lý [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev) với hàng nghìn học viên, hàng trăm khóa học, hàng ngàn đơn hàng mỗi ngày. Nếu lưu tất cả vào file Excel thì:

*   File sẽ nặng và chậm khi dữ liệu lớn
    
*   Không thể nhiều người cùng chỉnh sửa một lúc
    
*   Không có cơ chế bảo vệ dữ liệu khi có lỗi xảy ra
    
*   Tìm kiếm và lọc dữ liệu rất thủ công
    

**Database** giải quyết tất cả những vấn đề trên. Nó lưu trữ dữ liệu có cấu trúc, hỗ trợ truy vấn nhanh, cho phép nhiều người dùng đồng thời và đảm bảo dữ liệu không bị mất hay sai lệch.

## 3\. Relational Database là gì?

**Relational Database** (Cơ sở dữ liệu quan hệ) tổ chức dữ liệu thành các **bảng** (table) — tương tự như sheet trong Excel, nhưng mạnh hơn rất nhiều.

Mỗi bảng có:

*   **Cột (Column)** — định nghĩa loại dữ liệu, ví dụ: `id`, `email`, `created_at`
    
*   **Dòng (Row)** — mỗi dòng là một bản ghi thực tế
    

Ví dụ, trong database của [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev) có bảng `users`:


| id | email | first_name | last_name | account_status |
|---|---|---|---|---|
| 1 | nam@gmail.com | Nam | Nguyen | ACTIVE |
| 2 | linh@gmail.com | Linh | Tran | ACTIVE |
| 3 | minh@gmail.com | Minh | Le | ACTIVE |



Và bảng `courses`:


| id | title | course_type | price | enrolled_count |
|---|---|---|---|---|
| 1 | Spring Boot từ Zero đến Hero | PAID | 799000 | 320 |
| 2 | SQL cho Developer | PAID | 599000 | 210 |
| 3 | Docker & Kubernetes thực chiến | PAID | 899000 | 180 |



Điểm mạnh của Relational Database là các bảng có thể **liên kết với nhau**. Ví dụ bảng `orders` lưu `user_id` để biết đơn hàng đó thuộc về học viên nào, `course_id` để biết mua khóa học nào — đây chính là "quan hệ" (relation) trong tên gọi.

## 4\. SQL khác gì các ngôn ngữ lập trình khác?

Đây là điểm nhiều người mới nhầm lẫn. SQL **không phải** ngôn ngữ lập trình theo nghĩa thông thường — bạn không dùng SQL để viết logic điều kiện phức tạp hay xây dựng ứng dụng.

SQL là **ngôn ngữ khai báo** (declarative language) — bạn chỉ cần nói **"muốn lấy gì"**, không cần quan tâm **"lấy như thế nào"**:

```sql
-- Bạn chỉ cần nói: "Cho tôi tên các khóa học có hơn 200 học viên"
SELECT title, enrolled_count
FROM courses
WHERE enrolled_count > 200;
```

Database tự tìm cách thực thi hiệu quả nhất — bạn không cần viết vòng lặp, không cần quản lý bộ nhớ.

So sánh với Java để thấy sự khác biệt:

*   java
    

```java
// Java — phải tự viết logic duyệt từng phần tử
List<Course> result = new ArrayList<>();
for (Course course : allCourses) {
    if (course.getEnrolledCount() > 200) {
        result.add(course);
    }
}
```

*   sql
    

```sql
-- SQL — chỉ cần mô tả kết quả muốn có
SELECT * FROM courses WHERE enrolled_count > 200;
```

## 5\. Các hệ quản trị CSDL phổ biến

Có nhiều phần mềm triển khai SQL, gọi là **RDBMS** (Relational Database Management System). Mỗi cái có điểm mạnh riêng:


| RDBMS | Điểm mạnh | Thường dùng ở đâu |
|---|---|---|
| PostgreSQL | Mạnh nhất, chuẩn SQL nhất, miễn phí | Startup, fintech, SaaS |
| MySQL | Phổ biến, dễ setup, nhanh với read | Web app, WordPress, e-commerce |
| SQL Server | Tích hợp tốt với hệ sinh thái Microsoft | Enterprise, công ty lớn |
| Oracle | Cực kỳ mạnh, nhiều tính năng enterprise | Banking, tập đoàn lớn |
| SQLite | Nhẹ, không cần server, lưu vào file | Mobile app, testing, embedded |



Cả series này sẽ dùng **PostgreSQL** vì:

*   Miễn phí và open source
    
*   Cú pháp chuẩn SQL nhất — học xong dễ chuyển sang hệ khác
    
*   Được dùng ở [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev) thực tế
    
*   Rất phổ biến trong các công ty công nghệ hiện đại
    

> **Lưu ý về syntax:** Hầu hết các câu lệnh trong series này chạy được trên mọi RDBMS. Những chỗ có khác biệt tôi sẽ note rõ ở cuối mỗi bài.

## 6\. SQL gồm những nhóm lệnh nào?

SQL được chia thành các nhóm lệnh theo mục đích sử dụng:

**DDL — Data Definition Language** (Định nghĩa cấu trúc)

```sql
CREATE TABLE courses (...);   -- Tạo bảng
ALTER TABLE courses ADD ...;  -- Sửa cấu trúc bảng
DROP TABLE courses;           -- Xóa bảng
```

**DML — Data Manipulation Language** (Thao tác dữ liệu)

```sql
SELECT * FROM courses;                        -- Truy vấn
INSERT INTO courses (title) VALUES ('...');   -- Thêm dữ liệu
UPDATE courses SET price = 699000 WHERE id = 1; -- Cập nhật
DELETE FROM courses WHERE id = 1;             -- Xóa dữ liệu
```

**DCL — Data Control Language** (Phân quyền)

```sql
GRANT SELECT ON courses TO readonly_user;  -- Cấp quyền
REVOKE SELECT ON courses FROM readonly_user; -- Thu hồi quyền
```

**TCL — Transaction Control Language** (Quản lý transaction)

```sql
BEGIN;    -- Bắt đầu transaction
COMMIT;   -- Xác nhận thay đổi
ROLLBACK; -- Hoàn tác thay đổi
```

Trong series này chúng ta sẽ đi sâu vào từng nhóm theo thứ tự từ dễ đến khó — bắt đầu từ `SELECT` vì đây là lệnh bạn sẽ dùng nhiều nhất trong thực tế (chiếm khoảng 80% công việc hàng ngày với database).

## 7\. SQL được dùng như thế nào trong thực tế?

Để hình dung rõ hơn, đây là một số tình huống thực tế tại [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev) mà SQL giải quyết:

**Báo cáo doanh thu theo tháng:**

```sql
SELECT
    DATE_TRUNC('month', created_at) AS month,
    SUM(final_amount)               AS revenue
FROM orders
WHERE order_status = 'PAID'
GROUP BY DATE_TRUNC('month', created_at)
ORDER BY month DESC;
```

**Tìm học viên chưa hoàn thành khóa học sau 30 ngày:**

```sql
SELECT u.email, u.first_name, c.title
FROM enrollments e
JOIN users   u ON u.id = e.user_id
JOIN courses c ON c.id = e.course_id
WHERE e.enrolled_at < NOW() - INTERVAL '30 days'
  AND e.user_id NOT IN (
      SELECT student_id FROM user_course_certificates
      WHERE course_id = e.course_id
  );
```

**Top 5 khóa học bán chạy nhất:**

```sql
SELECT c.title, COUNT(oi.id) AS total_sold
FROM order_items oi
JOIN courses c ON c.id = oi.course_id
JOIN orders  o ON o.id = oi.order_id
WHERE o.order_status = 'PAID'
GROUP BY c.title
ORDER BY total_sold DESC
LIMIT 5;
```

Những query này trông phức tạp ngay bây giờ — nhưng sau khi học xong series, bạn sẽ đọc và viết được chúng một cách tự nhiên.

## Tổng kết

*   **SQL** là ngôn ngữ giao tiếp với Relational Database, ra đời từ 1970 và vẫn là chuẩn vàng đến hôm nay
    
*   **Relational Database** tổ chức dữ liệu thành các bảng có quan hệ với nhau
    
*   SQL là ngôn ngữ **khai báo** — bạn nói muốn gì, database tự lo phần còn lại
    
*   **PostgreSQL** là lựa chọn của cả series này — mạnh, chuẩn, miễn phí
    
*   SQL có 4 nhóm lệnh chính: DDL, DML, DCL, TCL — chúng ta sẽ bắt đầu từ `SELECT`
    

Bài tiếp theo chúng ta sẽ viết những câu `SELECT` đầu tiên và truy vấn trực tiếp trên database thực hành đã tạo ở Bài 1.

