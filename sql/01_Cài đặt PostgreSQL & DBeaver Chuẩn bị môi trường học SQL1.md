# Cài đặt PostgreSQL & DBeaver: Chuẩn bị môi trường học SQL

Trước khi bắt tay vào học SQL, chúng ta cần chuẩn bị 2 thứ:

*   **PostgreSQL** — hệ quản trị cơ sở dữ liệu (DBMS) mà cả series này sẽ dùng
    
*   **DBeaver** — công cụ giao diện đồ họa (GUI) giúp bạn kết nối và thao tác với database một cách trực quan
    

> **Tại sao chọn PostgreSQL?** PostgreSQL là một trong những DBMS mạnh nhất, phổ biến nhất hiện nay, được dùng rộng rãi ở cả startup lẫn enterprise. Cú pháp của PostgreSQL cũng rất chuẩn SQL, học xong bạn dễ dàng chuyển sang MySQL, SQL Server hay các hệ khác.

## 1\. Cài đặt PostgreSQL

### Windows

**Bước 1:** Truy cập trang chủ tải về: [https://www.postgresql.org/download/windows/](https://www.postgresql.org/download/windows/)

**Bước 2:** Tải bản installer mới nhất (ví dụ PostgreSQL 16.x) — chọn file `.exe` phù hợp với máy (thường là Windows x86-64).

**Bước 3:** Chạy file `.exe` vừa tải, làm theo từng bước trong wizard:

*   **Installation Directory:** Để mặc định (`C:\Program Files\PostgreSQL\16`)
    
*   **Components:** Giữ nguyên tất cả (PostgreSQL Server, pgAdmin, Stack Builder, Command Line Tools)
    
*   **Data Directory:** Để mặc định
    
*   **Password:** Đặt mật khẩu cho user `postgres` — **nhớ kỹ mật khẩu này**, sẽ dùng lại khi kết nối DBeaver
    
*   **Port:** Để mặc định `5432`
    
*   **Locale:** Để mặc định
    

**Bước 4:** Nhấn **Next** đến cuối rồi **Finish**. Bỏ qua Stack Builder nếu được hỏi.

**Bước 5:** Kiểm tra cài đặt thành công — mở **Command Prompt** và chạy:

```bash
psql --version
```

Kết quả mong đợi:

```java
psql (PostgreSQL) 16.x
```

### macOS

**Cách 1: Dùng Homebrew (khuyến nghị)**

Nếu chưa có Homebrew, cài trước:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Cài PostgreSQL:

```bash
brew install postgresql@16
```

Khởi động PostgreSQL:

```bash
brew services start postgresql@16
```

Thêm vào PATH (thêm vào file `~/.zshrc` hoặc `~/.bash_profile`):

```bash
export PATH="/opt/homebrew/opt/postgresql@16/bin:$PATH"
```

Áp dụng thay đổi:

```bash
source ~/.zshrc
```

Kiểm tra:

```bash
psql --version
```

**Cách 2: Dùng** [**Postgres.app**](http://Postgres.app) **(dành cho ai không quen terminal)**

*   Tải tại: [https://postgresapp.com/](https://postgresapp.com/)
    
*   Kéo app vào thư mục **Applications**
    
*   Mở app, nhấn **Initialize** để khởi tạo database
    
*   PostgreSQL sẽ chạy ngầm và hiển thị icon voi ở thanh menu bar
    

### Linux (Ubuntu/Debian)

**Bước 1:** Cập nhật package list:

```bash
sudo apt update
```

**Bước 2:** Cài PostgreSQL:

```bash
sudo apt install postgresql postgresql-contrib -y
```

**Bước 3:** Khởi động service:

```bash
sudo systemctl start postgresql
sudo systemctl enable postgresql  # tự động khởi động cùng máy
```

**Bước 4:** Kiểm tra trạng thái:

```bash
sudo systemctl status postgresql
```

**Bước 5:** Đặt mật khẩu cho user `postgres`:

```bash
sudo -u postgres psql
```

Trong psql shell, chạy lệnh sau rồi thoát:

```sql
ALTER USER postgres WITH PASSWORD 'your_password';
\q
```

## 2\. Tạo database thực hành

Sau khi cài xong PostgreSQL, chúng ta tạo một database riêng để học — **không nên dùng thẳng database** `postgres` **mặc định**.

Mở terminal (hoặc Command Prompt trên Windows), kết nối vào PostgreSQL:

```bash
psql -U postgres -h localhost
```

Nhập mật khẩu vừa đặt, sau đó tạo database:

```sql
CREATE DATABASE foxdev_practice;
```

Kiểm tra đã tạo thành công:

```sql
\l
```

Thoát khỏi psql:

```sql
\q
```

## 3\. Cài đặt DBeaver

DBeaver là công cụ quản lý database đa nền tảng, miễn phí, hỗ trợ hầu hết các loại database phổ biến. Đây là công cụ FoxDev dùng hàng ngày và sẽ dùng xuyên suốt cả series.

### Tải DBeaver Community (miễn phí)

Truy cập: [https://dbeaver.io/download/](https://dbeaver.io/download/)

Chọn bản phù hợp với hệ điều hành của bạn:

*   **Windows:** File `.exe` installer
    
*   **macOS:** File `.dmg`
    
*   **Linux:** File `.deb` (Ubuntu/Debian) hoặc `.rpm` (CentOS/Fedora)
    

### Windows

Chạy file `.exe` vừa tải, làm theo hướng dẫn cài đặt, để mặc định tất cả. Sau khi cài xong, mở DBeaver từ Desktop hoặc Start Menu.

### macOS

Mở file `.dmg`, kéo **DBeaver** vào thư mục **Applications**. Lần đầu mở có thể bị chặn bởi Gatekeeper — vào **System Preferences → Security & Privacy → Open Anyway**.

### Linux (Ubuntu)

```bash
sudo dpkg -i dbeaver-ce_xx.x.x_amd64.deb
```

Hoặc cài qua snap:

```bash
sudo snap install dbeaver-ce
```

## 4\. Kết nối DBeaver với PostgreSQL

Đây là bước quan trọng nhất — kết nối DBeaver với database vừa tạo.

**Bước 1:** Mở DBeaver, nhấn nút **New Database Connection** (biểu tượng ổ cắm + dấu cộng) ở góc trên bên trái, hoặc vào menu **Database → New Database Connection**.

**Bước 2:** Trong cửa sổ **Connect to a database**, tìm và chọn **PostgreSQL**, nhấn **Next**.

**Bước 3:** Điền thông tin kết nối:


| Trường | Giá trị |
|---|---|
| Host | localhost |
| Port | 5432 |
| Database | foxdev_practice |
| Username | postgres |
| Password | (mật khẩu bạn đặt lúc cài) |



**Bước 4:** Nhấn **Test Connection** — nếu hiện thông báo `Connected` là thành công. Lần đầu DBeaver sẽ hỏi tải driver PostgreSQL — nhấn **Download** và chờ.

**Bước 5:** Nhấn **Finish** để lưu kết nối.

Sau đó bạn sẽ thấy kết nối `foxdev_practice` xuất hiện ở panel bên trái (**Database Navigator**).

## 5\. Làm quen với giao diện DBeaver

Sau khi kết nối thành công, đây là những thành phần chính bạn cần biết:

**Database Navigator (panel trái):** Hiển thị cây thư mục của database — schemas, tables, views, functions... Click để expand và khám phá cấu trúc database.

**SQL Editor:** Nơi bạn gõ và chạy SQL. Mở bằng cách nhấn **F3** hoặc click chuột phải vào database → **SQL Editor → Open SQL Script**.

**Result Panel (panel dưới):** Hiển thị kết quả sau khi chạy query.

### Chạy query đầu tiên

Mở SQL Editor, gõ câu lệnh sau và nhấn **Ctrl+Enter** (Windows/Linux) hoặc **Cmd+Enter** (macOS) để chạy:

```sql
SELECT version();
```

Kết quả sẽ hiển thị phiên bản PostgreSQL đang chạy — chúc mừng, bạn đã sẵn sàng học SQL!

## 6\. Tạo bảng dữ liệu mẫu để thực hành

Trong suốt series này, chúng ta sẽ dùng một schema mô phỏng hệ thống e-learning — tương tự cấu trúc thực tế của [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev). Chạy đoạn SQL sau trong DBeaver để tạo dữ liệu mẫu:

```sql
-- Tạo bảng users
CREATE TABLE users (
    id        BIGSERIAL PRIMARY KEY,
    email     VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    account_status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng courses
CREATE TABLE courses (
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    course_type  VARCHAR(20)  DEFAULT 'PAID',
    price        NUMERIC      DEFAULT 0,
    enrolled_count INT        DEFAULT 0,
    rating       NUMERIC      DEFAULT 0,
    created_at   TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng orders
CREATE TABLE orders (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT       NOT NULL REFERENCES users(id),
    order_status VARCHAR(20)  DEFAULT 'PENDING',
    final_amount NUMERIC      DEFAULT 0,
    created_at   TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng order_items
CREATE TABLE order_items (
    id        BIGSERIAL PRIMARY KEY,
    order_id  BIGINT  NOT NULL REFERENCES orders(id),
    course_id BIGINT  NOT NULL REFERENCES courses(id),
    price     NUMERIC NOT NULL
);

-- Tạo bảng enrollments (tracking học viên đã đăng ký khóa học)
CREATE TABLE enrollments (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES users(id),
    course_id  BIGINT NOT NULL REFERENCES courses(id),
    enrolled_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, course_id)
);

-- Thêm dữ liệu mẫu
INSERT INTO users (email, first_name, last_name, account_status) VALUES
    ('nam@gmail.com',     'Nam',   'Nguyen', 'ACTIVE'),
    ('linh@gmail.com',    'Linh',  'Tran',   'ACTIVE'),
    ('minh@gmail.com',    'Minh',  'Le',     'ACTIVE'),
    ('huong@gmail.com',   'Huong', 'Pham',   'INACTIVE'),
    ('tuan@gmail.com',    'Tuan',  'Vu',     'ACTIVE');

INSERT INTO courses (title, course_type, price, enrolled_count, rating) VALUES
    ('Spring Boot từ Zero đến Hero',  'PAID', 799000,  320, 4.8),
    ('SQL cho Developer',             'PAID', 599000,  210, 4.9),
    ('Docker & Kubernetes thực chiến','PAID', 899000,  180, 4.7),
    ('Java Core nền tảng',            'FREE', 0,       500, 4.6),
    ('ReactJS cơ bản đến nâng cao',   'PAID', 699000,  150, 4.5);

INSERT INTO orders (user_id, order_status, final_amount) VALUES
    (1, 'PAID',      799000),
    (1, 'PAID',      599000),
    (2, 'PAID',      899000),
    (3, 'PAID',      599000),
    (3, 'CANCELLED', 699000),
    (4, 'PAID',      799000),
    (5, 'PENDING',   899000);

INSERT INTO order_items (order_id, course_id, price) VALUES
    (1, 1, 799000),
    (2, 2, 599000),
    (3, 3, 899000),
    (4, 2, 599000),
    (5, 5, 699000),
    (6, 1, 799000),
    (7, 3, 899000);

INSERT INTO enrollments (user_id, course_id) VALUES
    (1, 1), (1, 2),
    (2, 3),
    (3, 2),
    (4, 1);
```

Chạy xong, click chuột phải vào **Tables** trong Database Navigator → **Refresh** để thấy các bảng vừa tạo.

## 7\. Một số phím tắt hữu ích trong DBeaver


| Phím tắt | Chức năng |
|---|---|
| Ctrl+Enter / Cmd+Enter | Chạy câu query hiện tại |
| Ctrl+Shift+Enter | Chạy toàn bộ script |
| Ctrl+/ | Comment/uncomment dòng |
| Ctrl+Space | Gợi ý code (auto-complete) |
| Ctrl+Shift+F | Format lại SQL cho đẹp |
| F3 | Mở SQL Editor mới |
| Alt+X | Xem Execution Plan của query |



* * *

## Tổng kết

Sau bài này bạn đã có:

*   PostgreSQL đang chạy trên máy
    
*   DBeaver kết nối thành công đến database `foxdev_practice`
    
*   Bộ dữ liệu mẫu gồm 5 bảng sẵn sàng để thực hành
    

Bài tiếp theo chúng ta sẽ đi vào câu lệnh SQL đầu tiên — `SELECT` — và bắt đầu truy vấn dữ liệu thực sự từ những bảng vừa tạo.

