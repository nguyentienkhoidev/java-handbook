# RESTful API – Khái Niệm và Ví Dụ Cụ Thể

### **1\. RESTful API là gì?**

*   **REST** (Representational State Transfer) là một **kiến trúc phần mềm** để thiết kế các dịch vụ web.
    
*   **RESTful API** là API tuân theo các nguyên tắc REST, thường dùng để giao tiếp giữa **client** (frontend, mobile app) và **server** (backend).
    

👉 Hiểu đơn giản: RESTful API là **“cầu nối”** để client và server trao đổi dữ liệu thông qua **HTTP** (giống như trình duyệt tải web).

### **2\. Các đặc điểm của RESTful API**

*   **Stateless** (phi trạng thái)
    
*   Mỗi request gửi đến server phải chứa đầy đủ thông tin cần thiết (server không lưu trạng thái của client).
    
*   **Resource-based** (dựa trên tài nguyên)
    
*   Tài nguyên (resource) thường được biểu diễn dưới dạng URL.
    

**📌** Ví dụ:

*   `/students` → danh sách sinh viên
    
*   `/students/1` → thông tin sinh viên có id = 1
    

**Sử dụng HTTP Methods chuẩn**

*   `GET` → Lấy dữ liệu
    
*   `POST` → Tạo mới dữ liệu
    
*   `PUT` → Cập nhật toàn bộ
    
*   `PATCH` → Cập nhật một phần
    
*   `DELETE` → Xóa dữ liệu
    

**Trả về dữ liệu dạng JSON** (phổ biến nhất, dễ đọc và xử lý).

### **3\. Ví dụ RESTful API quản lý sinh viên**

Giả sử ta có hệ thống quản lý sinh viên.

#### 3.1. Lấy danh sách sinh viên

*   **Request:**
    

```plaintext
GET /api/students
```

*   **Response:**
    

```plaintext
{
    "status": 200,
    "message": "User list",
    "data": [
        {
            "id": 1,
            "name": "Nguyen Van A",
            "age": 20
        },
        {
            "id": 2,
            "name": "Tran Thi B",
            "age": 22
        }
    ]
}
```

#### 3.2. Lấy thông tin chi tiết 1 sinh viên

*   **Request:**
    

```plaintext
GET /api/students/1
```

*   **Response:**
    

```plaintext
{
    "status": 200,
    "message": "User detail",
    "data": [
        {
            "id": 1,
            "name": "Nguyen Van A",
            "age": 20
        }
}
```

#### 3.3. Tạo mới sinh viên

*   **Request:**
    

```plaintext
POST /api/students
Content-Type: application/json

{
  "name": "Le Van C",
  "age": 21
}
```

*   **Response:**
    

```plaintext
{
    "status": 201,
    "message": "User created successfully",
    "data": 3
}
```

#### 3.4. Cập nhật sinh viên

*   **Request:**
    

```plaintext
PUT /api/students/1
Content-Type: application/json

{
  "name": "Nguyen Van A Updated",
  "age": 21
}
```

*   **Response:**
    

```plaintext
{
    "status": 202,
    "message": "User updated successfully"
}
```

### 3.5. Xóa sinh viên

*   **Request:**
    

```plaintext
DELETE /api/students/1
```

*   **Response:**
    

```plaintext
{
    "status": 204,
    "message": "User deleted successfully"
}
```

### **4\. Ưu điểm của RESTful API**

*   Đơn giản, dễ hiểu, dễ triển khai.
    
*   Dựa trên HTTP nên tương thích mọi nền tảng.
    
*   Dữ liệu thường là JSON → dễ xử lý trên web/mobile.
    
*   Tách biệt frontend và backend, dễ phát triển độc lập.
    

### **5\. Nhược điểm**

Stateless → client phải gửi nhiều thông tin trong mỗi request.

Khi hệ thống phức tạp có thể cần thêm các giải pháp khác (GraphQL, gRPC).

### **6\. Kết luận**

*   RESTful API là kiến trúc phổ biến nhất để xây dựng dịch vụ web.
    
*   Dùng các HTTP methods (GET, POST, PUT, DELETE).
    
*   Dữ liệu trả về thường là JSON.
    
*   Tài nguyên được xác định qua URL.
    

👉 Sinh viên học xong RESTful API có thể áp dụng để:

*   Viết backend bằng **Spring Boot, Node.js, Django**.
    
*   Gọi API từ frontend **React, Angular, Vue**.
    
*   Gọi API từ mobile app **Android, iOS**.
    

