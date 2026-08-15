# Kiểu dữ liệu & NULL: Nền tảng để thiết kế database đúng

![Kiểu dữ liệu & NULL- Nền tảng để thiết kế database đúng.jpeg](../images/1b01e729-1095-48ca-bb65-5bfcd6b2a272.jpeg)

  
Hiểu rõ kiểu dữ liệu và NULL là nền tảng để bạn thiết kế database đúng ngay từ đầu, tránh những bug âm thầm khó tìm sau này. Bài này sẽ đi qua các kiểu dữ liệu phổ biến trong PostgreSQL và giải thích tại sao NULL lại đặc biệt hơn bạn nghĩ.

## 1\. Tại sao kiểu dữ liệu quan trọng?

Khi tạo bảng trong database, mỗi cột phải được khai báo kiểu dữ liệu cụ thể. Điều này giúp:

*   **Tiết kiệm bộ nhớ** — lưu số nguyên nhỏ dùng `SMALLINT` thay vì `BIGINT`
    
*   **Đảm bảo tính đúng đắn** — không thể nhét chữ vào cột số
    
*   **Tăng hiệu năng** — database tối ưu lưu trữ và tìm kiếm theo từng kiểu
    
*   **Tránh bug ngầm** — so sánh `'9' > '10'` trả về `true` nếu lưu dạng text (vì so sánh theo ký tự), nhưng `9 > 10` trả về `false` nếu lưu dạng số
    

## 2\. Kiểu số (Numeric)

### Số nguyên


| Kiểu | Dung lượng | Phạm vi | Dùng khi nào |
|---|---|---|---|
| SMALLINT | 2 bytes | -32,768 đến 32,767 | Số nhỏ, ít biến động (tuổi, số sao rating 1-5) |
| INTEGER / INT | 4 bytes | -2.1 tỷ đến 2.1 tỷ | ID, số lượng thông thường |
| BIGINT | 8 bytes | -9.2 triệu tỷ đến 9.2 triệu tỷ | ID cho bảng lớn, timestamp dạng epoch |
| SERIAL | 4 bytes | 1 đến 2.1 tỷ | Auto-increment ID (tự tăng) |
| BIGSERIAL | 8 bytes | 1 đến 9.2 triệu tỷ | Auto-increment ID cho bảng rất lớn |



Nhìn vào schema [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev), bạn sẽ thấy:

```sql
-- users dùng BIGSERIAL vì có thể có hàng triệu user
id int8 GENERATED ALWAYS AS IDENTITY  -- int8 = BIGINT

-- roles dùng INT vì số lượng role rất ít
id int4 GENERATED ALWAYS AS IDENTITY  -- int4 = INTEGER
```

### Số thực


| Kiểu | Mô tả | Dùng khi nào |
|---|---|---|
| NUMERIC(p, s) | Số thập phân chính xác tuyệt đối | Tiền tệ, giá cả — không bao giờ dùng FLOAT cho tiền |
| DECIMAL(p, s) | Tương đương NUMERIC | Như trên |
| REAL | Số thực 4 bytes, xấp xỉ | Tọa độ, đo lường không cần chính xác tuyệt đối |
| FLOAT8 / DOUBLE PRECISION | Số thực 8 bytes, xấp xỉ | Tính toán khoa học |



> **Cảnh báo quan trọng:** Không bao giờ dùng `FLOAT` để lưu tiền tệ. `FLOAT` lưu xấp xỉ, không chính xác tuyệt đối — có thể dẫn đến sai số như `0.1 + 0.2 = 0.30000000000000004`. Luôn dùng `NUMERIC` hoặc `DECIMAL` cho mọi thứ liên quan đến tiền.

Trong [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev):

```sql
-- Giá khóa học dùng NUMERIC để đảm bảo chính xác
price  numeric NOT NULL,

-- Số tiền trong đơn hàng
final_amount numeric(18, 2) NOT NULL  -- tối đa 18 chữ số, 2 số lẻ
```

## 3\. Kiểu chuỗi (String)


| Kiểu | Mô tả | Dùng khi nào |
|---|---|---|
| VARCHAR(n) | Chuỗi có độ dài tối đa n ký tự | Email, tên, tiêu đề — khi biết độ dài tối đa |
| CHAR(n) | Chuỗi cố định n ký tự, tự thêm space | Mã quốc gia (VN, US), mã cố định |
| TEXT | Chuỗi không giới hạn độ dài | Nội dung bài viết, mô tả dài |



```sql
-- Trong bảng users của nguyentienkhoi.hashnode.dev
email      varchar(255) NOT NULL,   -- email có độ dài giới hạn
first_name varchar(50)  NOT NULL,   -- tên ngắn
password   varchar(255) NOT NULL,   -- hash password

-- Trong bảng posts
content    text NOT NULL,           -- nội dung bài viết dài
```

> **VARCHAR vs TEXT trong PostgreSQL:** Về hiệu năng, `VARCHAR` và `TEXT` trong PostgreSQL gần như không khác biệt. Dùng `VARCHAR(n)` khi muốn **ràng buộc độ dài tối đa** (ví dụ email không thể dài hơn 255 ký tự), dùng `TEXT` khi không cần giới hạn.

## 4\. Kiểu ngày giờ (Date/Time)


| Kiểu | Mô tả | Ví dụ |
|---|---|---|
| DATE | Chỉ lưu ngày | 2024-01-15 |
| TIME | Chỉ lưu giờ | 14:30:00 |
| TIMESTAMP | Ngày + giờ, không có timezone | 2024-01-15 14:30:00 |
| TIMESTAMPTZ | Ngày + giờ + timezone | 2024-01-15 14:30:00+07 |
| INTERVAL | Khoảng thời gian | 30 days, 2 hours |



> **TIMESTAMP vs TIMESTAMPTZ:** Luôn dùng `TIMESTAMPTZ` (timestamp with time zone) cho các ứng dụng có người dùng ở nhiều múi giờ khác nhau. `TIMESTAMP` không lưu thông tin timezone — có thể gây nhầm lẫn khi server và client ở các timezone khác nhau.

Nhìn vào [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev), toàn bộ các cột thời gian đều dùng `TIMESTAMPTZ`:

```sql
created_at timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
updated_at timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
```

Nhưng ngày sinh nhật hoặc ngày cấp bằng thì dùng `DATE` vì không cần giờ:

```sql
-- Trong bảng users
birth_date date NULL,

-- Trong bảng user_educations
start_year int4 NULL,
end_year   int4 NULL,
```

## 5\. Kiểu Boolean

```sql
is_active bool DEFAULT true NOT NULL,
allow_free_trial bool DEFAULT false NULL,
```

Chỉ nhận 3 giá trị: `TRUE`, `FALSE`, và `NULL`.

```sql
-- Lấy các khóa học cho phép xem thử miễn phí
SELECT title
FROM courses
WHERE allow_free_trial = TRUE;

-- Lấy các user chưa xác thực email
SELECT email
FROM users
WHERE email_verified = FALSE;
```

## 6\. Kiểu đặc biệt trong PostgreSQL

### UUID

```sql
public_id uuid DEFAULT gen_random_uuid() NOT NULL
```

UUID (Universally Unique Identifier) là chuỗi 32 ký tự hex ngẫu nhiên, dùng làm ID khi không muốn lộ thứ tự hay số lượng bản ghi. Trong [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev), `users.public_id` dùng UUID để expose ra API thay vì dùng `id` số nguyên.

### JSONB

```sql
-- Trong bảng promotion_rule
rule_data jsonb NULL,

-- Trong bảng user_experiences
metadata jsonb NULL,
```

`JSONB` lưu dữ liệu JSON dạng binary, hỗ trợ index và query bên trong JSON. Dùng khi cấu trúc dữ liệu linh hoạt, không cố định — ví dụ config của từng loại promotion rule có thể khác nhau hoàn toàn.

## 7\. NULL là gì?

`NULL` là khái niệm đặc biệt nhất trong SQL — nhiều developer kỳ cựu vẫn hay mắc bẫy với nó.

**NULL không phải:**

*   Số `0`
    
*   Chuỗi rỗng `''`
    
*   `false`
    

**NULL có nghĩa là: "không có giá trị" hay "chưa biết".**

Trong [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev):

```sql
-- birth_date NULL = học viên chưa điền ngày sinh, không phải sinh ngày 0
birth_date date NULL,

-- resolved_at NULL = liên hệ chưa được xử lý, không phải xử lý lúc 0 giờ
resolved_at timestamptz NULL,

-- phone NULL = học viên chưa có số điện thoại
phone varchar(20) NULL,
```

## 8\. So sánh với NULL — Cái bẫy phổ biến nhất

Đây là nơi hầu hết người mới đều mắc lỗi:

```sql
-- ❌ SAI — không bao giờ dùng = NULL
SELECT * FROM users WHERE phone = NULL;       -- Luôn trả về 0 dòng
SELECT * FROM users WHERE phone != NULL;      -- Luôn trả về 0 dòng
```

Lý do: `NULL = NULL` trong SQL trả về `NULL` (không phải `TRUE`) vì NULL nghĩa là "không biết" — không biết bằng không biết thì không thể kết luận là bằng nhau.

```sql
-- ✅ ĐÚNG — phải dùng IS NULL hoặc IS NOT NULL
SELECT * FROM users WHERE phone IS NULL;       -- Tìm user chưa có số điện thoại
SELECT * FROM users WHERE phone IS NOT NULL;   -- Tìm user đã có số điện thoại
```

Thực hành với [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev):

```sql
-- Các liên hệ (contacts) chưa được xử lý
SELECT name, email, subject, created_at
FROM contacts
WHERE resolved_at IS NULL;

-- Các khóa học chưa có ngày kết thúc (tức là mở mãi mãi)
SELECT title, course_type, start_date
FROM courses
WHERE end_date IS NULL;
```

## 9\. COALESCE — Xử lý NULL linh hoạt

`COALESCE` trả về giá trị đầu tiên **khác NULL** trong danh sách — rất hữu ích khi bạn muốn hiển thị giá trị mặc định thay vì NULL:

```sql
COALESCE(value1, value2, value3, ...)
```

**Ví dụ 1** — Hiển thị username, nếu chưa có thì dùng email:

```sql
SELECT
    COALESCE(username, email) AS display_name,
    account_status
FROM users;
```

**Ví dụ 2** — Hiển thị số điện thoại, nếu chưa có thì hiển thị chữ "Chưa cập nhật":

```sql
SELECT
    first_name,
    last_name,
    COALESCE(phone, 'Chưa cập nhật') AS phone
FROM users;
```

**Ví dụ 3** — Tính giá sau giảm, nếu không có giảm giá thì lấy giá gốc:

```sql
SELECT
    title,
    price,
    COALESCE(discount_price, price) AS final_price
FROM courses;
```

## 10\. NULLIF — Đổi giá trị thành NULL

`NULLIF(a, b)` trả về `NULL` nếu `a = b`, ngược lại trả về `a`. Dùng để tránh lỗi chia cho 0:

```sql
-- ❌ Lỗi chia cho 0 nếu enrolled_count = 0
SELECT title, total_revenue / enrolled_count AS revenue_per_student
FROM courses;

-- ✅ An toàn — trả về NULL thay vì lỗi
SELECT title, total_revenue / NULLIF(enrolled_count, 0) AS revenue_per_student
FROM courses;
```

## 11\. NOT NULL Constraint — Bắt buộc phải có giá trị

Khi thiết kế bảng, thêm `NOT NULL` để đảm bảo cột không bao giờ được để trống:

```sql
CREATE TABLE users (
    id         BIGSERIAL    PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,   -- bắt buộc
    first_name VARCHAR(50)  NOT NULL,   -- bắt buộc
    phone      VARCHAR(20)  NULL,       -- không bắt buộc
    birth_date DATE         NULL        -- không bắt buộc
);
```

> **Nguyên tắc thiết kế:** Mặc định đặt `NOT NULL` cho tất cả các cột, chỉ cho phép `NULL` khi có lý do rõ ràng — ví dụ thông tin tùy chọn mà user chưa điền, hoặc giá trị chỉ có sau một sự kiện nào đó (như `resolved_at` chỉ có giá trị sau khi ticket được giải quyết).

## Tổng kết


| Nhóm | Kiểu phổ biến | Lưu ý quan trọng |
|---|---|---|
| Số nguyên | INT, BIGINT, BIGSERIAL | Dùng BIGSERIAL cho auto-increment ID |
| Số thực | NUMERIC(p,s) | Luôn dùng NUMERIC cho tiền, không dùng FLOAT |
| Chuỗi | VARCHAR(n), TEXT | VARCHAR khi cần giới hạn độ dài |
| Ngày giờ | DATE, TIMESTAMPTZ | Luôn dùng TIMESTAMPTZ thay vì TIMESTAMP |
| Boolean | BOOL | Nhớ rằng có 3 giá trị: TRUE, FALSE, NULL |
| NULL | — | Dùng IS NULL / IS NOT NULL, không dùng = NULL |
| COALESCE | — | Thay thế NULL bằng giá trị mặc định |



Bài tiếp theo chúng ta sẽ học các kỹ thuật lọc dữ liệu nâng cao hơn với `IN`, `BETWEEN`, `LIKE` — những công cụ giúp bạn viết điều kiện `WHERE` linh hoạt và ngắn gọn hơn nhiều.

> **Khác biệt với các RDBMS khác:**
> 
> *   **MySQL:** Không có `TIMESTAMPTZ`, dùng `DATETIME` thay thế — cần xử lý timezone thủ công ở tầng application
>     
> *   **MySQL:** `BOOLEAN` thực chất là `TINYINT(1)` bên dưới, lưu 0/1
>     
> *   **SQL Server:** Dùng `NVARCHAR` thay vì `VARCHAR` để hỗ trợ Unicode đầy đủ
>     
> *   **SQL Server:** Dùng `BIT` thay vì `BOOLEAN`
>     
> *   **Oracle:** Không có kiểu `BOOLEAN` trong SQL (chỉ có trong PL/SQL)
>     

