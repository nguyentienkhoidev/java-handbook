# Cài Đặt MongoDB & Compass: Chuẩn Bị Môi Trường Học NoSQL

![Cài Đặt MongoDB & Compass- Chuẩn Bị Môi Trường Học NoSQL.png](../images/dd7ad38b-6975-4dad-a52d-cfe41c21569a.png)

Trước khi bắt tay vào học MongoDB, cần chuẩn bị 2 thứ: **MongoDB Server** — nơi lưu trữ dữ liệu, và **MongoDB Compass** — giao diện đồ họa giúp thao tác trực quan thay vì gõ lệnh thuần. Bài này hướng dẫn cài đặt trên cả Windows, macOS và Linux, sau đó tạo database mẫu để thực hành xuyên suốt series.

## 1\. Tổng Quan Môi Trường

```java
┌────────────────────────────────────────────────┐
│              Local Development                 │
│                                                │
│  MongoDB Server                                │
│  ─────────────                                 │
│  Port: 27017                                   │
│  Connection: mongodb://localhost:27017         │
│                                                │
│  MongoDB Compass (GUI)                         │
│  ──────────────────────                        │
│  Tương tự DBeaver cho PostgreSQL               │
│  Kết nối → browse collections → chạy queries   │
└────────────────────────────────────────────────┘
```

**Yêu cầu:**

*   RAM tối thiểu 4GB (MongoDB khuyến nghị 8GB+)
    
*   Disk trống 5GB
    
*   Docker Desktop (nếu dùng Docker — khuyến nghị)
    

## 2\. Cài Đặt MongoDB

### Cách 1: Docker (Khuyến Nghị Nhất)

Đơn giản, sạch sẽ, không ảnh hưởng hệ thống:

```bash
# Pull và chạy MongoDB
docker run -d \
  --name mongodb \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=password123 \
  -p 27017:27017 \
  -v mongodb_data:/data/db \
  mongo:7.0

# Kiểm tra đang chạy
docker ps
# CONTAINER ID   IMAGE     STATUS    PORTS
# abc123         mongo:7.0 Up        0.0.0.0:27017->27017/tcp

# Xem logs
docker logs mongodb
```

**Connection string:**

```java
mongodb://admin:password123@localhost:27017
```

### Cách 2: Cài Trực Tiếp — Windows

**Bước 1:** Truy cập [https://www.mongodb.com/try/download/community](https://www.mongodb.com/try/download/community)

**Bước 2:** Chọn:

*   Version: 7.0 (Latest)
    
*   Platform: Windows
    
*   Package: MSI
    

**Bước 3:** Chạy file `.msi`, làm theo wizard:

*   Chọn **Complete** installation
    
*   Tick **Install MongoDB as a Service** — tự động khởi động cùng Windows
    
*   Tick **Install MongoDB Compass** — cài luôn GUI (bỏ qua bước cài riêng bên dưới)
    

**Bước 4:** Kiểm tra:

```cmd
mongosh --version
# MongoSH 2.x.x
```

**Connection string:**

```java
mongodb://localhost:27017
```

(Không có auth nếu cài mặc định)

### Cách 3: Cài Trực Tiếp — macOS

**Cách 3a: Homebrew (Khuyến nghị)**

```bash
# Thêm MongoDB tap
brew tap mongodb/brew

# Cài MongoDB Community
brew install mongodb-community@7.0

# Khởi động
brew services start mongodb-community@7.0

# Kiểm tra
mongosh --version
brew services list | grep mongodb
```

**Cách 3b: Tải file trực tiếp**

Truy cập [https://www.mongodb.com/try/download/community](https://www.mongodb.com/try/download/community) → chọn macOS → tải file `.tgz` → extract → thêm vào PATH.

### Cách 4: Cài Trực Tiếp — Linux (Ubuntu/Debian)

```bash
# Import MongoDB GPG key
curl -fsSL https://www.mongodb.org/static/pgp/server-7.0.asc | \
   sudo gpg -o /usr/share/keyrings/mongodb-server-7.0.gpg --dearmor

# Thêm repository
echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-7.0.gpg ] \
https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | \
sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list

# Cài đặt
sudo apt update
sudo apt install -y mongodb-org

# Khởi động
sudo systemctl start mongod
sudo systemctl enable mongod   # tự khởi động cùng hệ thống

# Kiểm tra
sudo systemctl status mongod
mongosh --version
```

## 3\. Docker Compose — Cài Cả MongoDB + Mongo Express

Nếu muốn có thêm **Mongo Express** (web UI nhẹ thay thế cho Compass):

```yaml
# docker-compose.yml
version: '3.8'

services:
  mongodb:
    image: mongo:7.0
    container_name: mongodb
    restart: unless-stopped
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: password123
      MONGO_INITDB_DATABASE: foxdev_nosql
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data/db
    healthcheck:
      test: ["CMD", "mongosh", "--eval", "db.adminCommand('ping')"]
      interval: 10s
      timeout: 5s
      retries: 5

  mongo-express:
    image: mongo-express:latest
    container_name: mongo-express
    restart: unless-stopped
    ports:
      - "8081:8081"
    environment:
      ME_CONFIG_MONGODB_ADMINUSERNAME: admin
      ME_CONFIG_MONGODB_ADMINPASSWORD: password123
      ME_CONFIG_MONGODB_URL: mongodb://admin:password123@mongodb:27017/
      ME_CONFIG_BASICAUTH_USERNAME: admin
      ME_CONFIG_BASICAUTH_PASSWORD: admin123
    depends_on:
      mongodb:
        condition: service_healthy

volumes:
  mongodb_data:
```

```bash
# Khởi động
docker-compose up -d

# Mongo Express UI: http://localhost:8081
# Đăng nhập: admin / admin123
```

## 4\. Cài Đặt MongoDB Compass

**MongoDB Compass** là GUI chính thức — tương tự DBeaver cho PostgreSQL.

### Tải Compass

Truy cập: [https://www.mongodb.com/try/download/compass](https://www.mongodb.com/try/download/compass)

Chọn bản phù hợp:

*   **Windows:** File `.exe`
    
*   **macOS:** File `.dmg`
    
*   **Linux:** File `.deb` hoặc `.rpm`
    

### Cài Đặt

*   **Windows:** Chạy `.exe` → Next → Finish
    
*   **macOS:** Mở `.dmg` → kéo vào Applications
    
*   **Linux (Ubuntu):**
    
    ```bash
    sudo dpkg -i mongodb-compass_x.x.x_amd64.deb
    ```
    

## 5\. Kết Nối Compass Với MongoDB

**Bước 1:** Mở MongoDB Compass

**Bước 2:** Nhập connection string:

```java
# Nếu dùng Docker / có auth:
mongodb://admin:password123@localhost:27017

# Nếu cài trực tiếp, không có auth:
mongodb://localhost:27017
```

**Bước 3:** Nhấn **Connect**

Bạn sẽ thấy giao diện chính với danh sách databases bên trái.

## 6\. Làm Quen Giao Diện Compass

```java
┌─────────────────────────────────────────────┐
│  MongoDB Compass                            │
│                                             │
│  ┌──────────┐  ┌───────────────────────────┐│
│  │Databases │  │   Collections / Documents ││
│  │          │  │                           ││
│  │admin     │  │  ● Schema tab             ││
│  │foxdev   │  │  ● Documents tab          ││
│  │  ├─users │  │  ● Aggregations tab       ││
│  │  ├─posts │  │  ● Indexes tab            ││
│  │  └─...   │  │  ● Explain Plan tab       ││
│  └──────────┘  └───────────────────────────┘│
│                                             │
│  ┌──────────────────────────────────────┐   │
│  │  MongoDB Shell (mongosh)             │   │
│  │  > db.users.find().limit(5)          │   │
│  └──────────────────────────────────────┘   │
└─────────────────────────────────────────────┘
```

**Các tab quan trọng:**

*   **Documents:** Browse và edit documents trực quan
    
*   **Aggregations:** Build aggregation pipeline bằng GUI
    
*   **Indexes:** Xem và tạo indexes
    
*   **Explain Plan:** Phân tích performance của query
    
*   **Shell:** Chạy MongoDB Shell commands trực tiếp
    

## 7\. Tạo Database và Dữ Liệu Mẫu

Mở **MongoDB Shell** trong Compass (icon `>_` phía dưới) hoặc chạy `mongosh` trong terminal:

```javascript
// Kết nối với auth (Docker)
// mongosh "mongodb://admin:password123@localhost:27017"

// Tạo và switch sang database mới
use foxdev_nosql

// ──────────────────────────────────────────
// Collection: users
// ──────────────────────────────────────────
db.users.insertMany([
  {
    _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0d1"),
    email: "nam@gmail.com",
    first_name: "Nam",
    last_name: "Nguyen",
    account_status: "ACTIVE",
    account_type: "INDIVIDUAL",
    enrolled_courses: 3,
    total_spent: 1998000,
    tags: ["java", "backend"],
    created_at: new Date("2024-01-15")
  },
  {
    _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0d2"),
    email: "linh@gmail.com",
    first_name: "Linh",
    last_name: "Tran",
    account_status: "ACTIVE",
    account_type: "INDIVIDUAL",
    enrolled_courses: 1,
    total_spent: 899000,
    tags: ["devops", "docker"],
    created_at: new Date("2024-02-20")
  },
  {
    _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0d3"),
    email: "minh@gmail.com",
    first_name: "Minh",
    last_name: "Le",
    account_status: "INACTIVE",
    account_type: "INDIVIDUAL",
    enrolled_courses: 0,
    total_spent: 0,
    tags: [],
    created_at: new Date("2024-03-10")
  }
])

// ──────────────────────────────────────────
// Collection: courses
// ──────────────────────────────────────────
db.courses.insertMany([
  {
    _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e1"),
    title: "Spring Boot từ Zero đến Hero",
    slug: "spring-boot-tu-zero-den-hero",
    course_type: "PAID",
    course_status: "PUBLISHED",
    price: 799000,
    rating: 4.8,
    enrolled_count: 320,
    category: "java",
    tags: ["java", "spring", "backend", "api"],
    instructor: {
      name: "FoxDev",
      email: "contact@nguyentienkhoi.hashnode.dev"
    },
    sections: [
      { title: "Giới thiệu Spring Boot", lectures_count: 5 },
      { title: "REST API với Spring MVC", lectures_count: 12 },
      { title: "Spring Data JPA", lectures_count: 8 },
      { title: "Spring Security", lectures_count: 10 }
    ],
    created_at: new Date("2023-06-01"),
    updated_at: new Date("2024-01-10")
  },
  {
    _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e2"),
    title: "SQL cho Developer",
    slug: "sql-cho-developer",
    course_type: "PAID",
    course_status: "PUBLISHED",
    price: 599000,
    rating: 4.9,
    enrolled_count: 210,
    category: "database",
    tags: ["sql", "postgresql", "database"],
    instructor: {
      name: "FoxDev",
      email: "contact@nguyentienkhoi.hashnode.dev"
    },
    sections: [
      { title: "SQL Cơ Bản", lectures_count: 7 },
      { title: "JOIN và Subquery", lectures_count: 6 },
      { title: "Index và Optimization", lectures_count: 5 }
    ],
    created_at: new Date("2023-08-15"),
    updated_at: new Date("2024-02-01")
  },
  {
    _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e3"),
    title: "Docker & Kubernetes thực chiến",
    slug: "docker-kubernetes-thuc-chien",
    course_type: "PAID",
    course_status: "PUBLISHED",
    price: 899000,
    rating: 4.7,
    enrolled_count: 180,
    category: "devops",
    tags: ["docker", "kubernetes", "devops", "ci-cd"],
    instructor: {
      name: "FoxDev",
      email: "contact@nguyentienkhoi.hashnode.dev"
    },
    sections: [
      { title: "Docker Cơ Bản", lectures_count: 8 },
      { title: "Docker Compose", lectures_count: 5 },
      { title: "Kubernetes", lectures_count: 15 }
    ],
    created_at: new Date("2023-10-01"),
    updated_at: new Date("2024-03-05")
  },
  {
    _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e4"),
    title: "Java Core nền tảng",
    slug: "java-core-nen-tang",
    course_type: "FREE",
    course_status: "PUBLISHED",
    price: 0,
    rating: 4.6,
    enrolled_count: 500,
    category: "java",
    tags: ["java", "oop", "core"],
    instructor: {
      name: "FoxDev",
      email: "contact@nguyentienkhoi.hashnode.dev"
    },
    sections: [
      { title: "OOP Cơ Bản", lectures_count: 10 },
      { title: "Collections", lectures_count: 8 }
    ],
    created_at: new Date("2023-03-20"),
    updated_at: new Date("2024-01-15")
  }
])

// ──────────────────────────────────────────
// Collection: orders
// ──────────────────────────────────────────
db.orders.insertMany([
  {
    _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0f1"),
    user_id: ObjectId("65f1a2b3c4d5e6f7a8b9c0d1"),
    order_status: "PAID",
    final_amount: 799000,
    currency: "VND",
    items: [
      {
        course_id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e1"),
        title: "Spring Boot từ Zero đến Hero",
        price: 799000
      }
    ],
    created_at: new Date("2024-01-20")
  },
  {
    _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0f2"),
    user_id: ObjectId("65f1a2b3c4d5e6f7a8b9c0d1"),
    order_status: "PAID",
    final_amount: 1199000,
    currency: "VND",
    items: [
      {
        course_id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e2"),
        title: "SQL cho Developer",
        price: 599000
      },
      {
        course_id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e3"),
        title: "Docker & Kubernetes thực chiến",
        price: 899000
      }
    ],
    created_at: new Date("2024-02-05")
  },
  {
    _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0f3"),
    user_id: ObjectId("65f1a2b3c4d5e6f7a8b9c0d2"),
    order_status: "PAID",
    final_amount: 899000,
    currency: "VND",
    items: [
      {
        course_id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e3"),
        title: "Docker & Kubernetes thực chiến",
        price: 899000
      }
    ],
    created_at: new Date("2024-02-20")
  },
  {
    _id: ObjectId("65f1a2b3c4d5e6f7a8b9c0f4"),
    user_id: ObjectId("65f1a2b3c4d5e6f7a8b9c0d3"),
    order_status: "CANCELLED",
    final_amount: 599000,
    currency: "VND",
    items: [
      {
        course_id: ObjectId("65f1a2b3c4d5e6f7a8b9c0e2"),
        title: "SQL cho Developer",
        price: 599000
      }
    ],
    created_at: new Date("2024-03-01")
  }
])

// Kiểm tra
print("Users:", db.users.countDocuments())
print("Courses:", db.courses.countDocuments())
print("Orders:", db.orders.countDocuments())
```

**Output mong đợi:**

```java
Users: 3
Courses: 4
Orders: 4
```

## 8\. Chạy Query Đầu Tiên

```javascript
// Lấy tất cả khóa học đang published
db.courses.find({ course_status: "PUBLISHED" })

// Chỉ lấy title và price
db.courses.find(
  { course_status: "PUBLISHED" },
  { title: 1, price: 1, _id: 0 }
)

// Khóa học có giá dưới 800k, sắp xếp theo rating giảm dần
db.courses.find(
  { price: { $lt: 800000 } }
).sort({ rating: -1 })

// Đếm số khóa học free
db.courses.countDocuments({ course_type: "FREE" })
```

## 9\. Kết Nối Compass Với mongosh CLI

Ngoài GUI, có thể dùng **mongosh** (MongoDB Shell) trong terminal:

```bash
# Kết nối không auth
mongosh

# Kết nối có auth (Docker)
mongosh "mongodb://admin:password123@localhost:27017"

# Một số lệnh cơ bản
show dbs                    # Xem danh sách databases
use foxdev_nosql           # Switch database
show collections            # Xem danh sách collections
db.courses.find().pretty()  # Xem documents (pretty print)
exit                        # Thoát
```

## 10\. Troubleshooting

### Không kết nối được MongoDB

```bash
# Kiểm tra MongoDB có đang chạy không
# Docker:
docker ps | grep mongodb
docker logs mongodb

# Cài trực tiếp:
sudo systemctl status mongod    # Linux
brew services list | grep mongo # macOS
```

### Compass báo lỗi Authentication

```java
MongoServerError: Authentication failed
```

→ Kiểm tra lại username/password trong connection string → Đảm bảo format: `mongodb://username:password@localhost:27017`

### Port 27017 đã bị dùng

```bash
# Kiểm tra port
lsof -i :27017         # macOS/Linux
netstat -ano | findstr :27017  # Windows

# Đổi port trong docker-compose (ví dụ sang 27018)
ports:
  - "27018:27017"
# Connection string: mongodb://admin:password123@localhost:27018
```

## Tổng Kết

Sau bài này bạn đã có:

```java
✅ MongoDB Server đang chạy tại localhost:27017
✅ MongoDB Compass kết nối thành công
✅ Database foxdev_nosql với 3 collections mẫu:
   - users (3 documents)
   - courses (4 documents)
   - orders (4 documents)
✅ Chạy được query đầu tiên
```

**Điểm khác biệt so với PostgreSQL + DBeaver:**

*   Dữ liệu lưu dạng JSON document thay vì rows
    
*   Không cần define schema trước khi insert
    
*   Mỗi document trong cùng collection có thể có cấu trúc khác nhau
    
*   `_id` thay vì `id` — ObjectId tự generate
    

Bài tiếp theo chúng ta sẽ học **CRUD trong MongoDB** — cách thêm, tìm, cập nhật và xóa documents với đầy đủ operators.

> **Khác biệt với các môi trường khác:**
> 
> *   **MongoDB Atlas:** Managed cloud service của MongoDB — không cần cài local, free tier 512MB. Phù hợp khi không muốn setup local: [https://cloud.mongodb.com](https://cloud.mongodb.com)
>     
> *   **Mongo Express vs Compass:** Mongo Express là web UI nhẹ (chạy trong browser), Compass là desktop app đầy đủ tính năng hơn
>     
> *   **mongosh vs mongo:** `mongosh` là MongoDB Shell mới (khuyến nghị), `mongo` là shell cũ đã deprecated từ MongoDB 6.0
>     

