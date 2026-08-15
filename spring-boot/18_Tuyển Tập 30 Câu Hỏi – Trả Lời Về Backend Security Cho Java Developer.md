# Tuyển Tập 30 Câu Hỏi – Trả Lời Về Backend Security Cho Java Developer

### **1\. XSS (Cross-Site Scripting) là gì và phòng chống thế nào?**

**📌 Trả lời:**  
_XSS là lỗ hổng cho phép chèn script độc hại vào ứng dụng._  
_👉  Cách phòng chống:_

*   _Escape dữ liệu output (Thymeleaf auto-escape)._
    
*   _Dùng thư viện_ `ESAPI` _hoặc_ `OWASP Java Encoder`_._
    
*   _Bật CSP (Content Security Policy)._
    

### **2\. CSRF (Cross-Site Request Forgery) là gì và xử lý ra sao?**

**📌 Trả lời:**  
_CSRF là tấn công lợi dụng cookie/session để gửi request giả mạo._  
_👉 Phòng chống:_

*   _Bật CSRF token trong Spring Security._
    
*   _Dùng_ `SameSite` _cookie._
    
*   _Xác thực 2FA hoặc reCAPTCHA cho action nhạy cảm._
    

### **3\. JWT có an toàn tuyệt đối không?**

**📌 Trả lời:**

_Không._

_JWT chỉ an toàn nếu:_

*   _Dùng_ **_HS256/RS256_** _thay vì_ `none` _algorithm._
    
*   _Đặt thời gian hết hạn ngắn (_`exp`_)._
    
*   _Refresh token an toàn._
    
*   _Lưu token ở_ **_HTTP-only cookie_**_, không lưu localStorage._
    

### **4\. So sánh Session-based Auth và JWT-based Auth về bảo mật**

**📌 Trả lời:**

*   _Session: server lưu state → dễ kiểm soát nhưng khó scale._
    
*   _JWT: stateless, dễ scale nhưng khó revoke._
    

  
_👉 Phòng chống: Với hệ thống lớn, nên kết hợp JWT + Redis để quản lý blacklist token_.

### **5\. CORS ảnh hưởng gì đến bảo mật?**

**📌 Trả lời:**  
_Nếu cấu hình_ `Access-Control-Allow-Origin: *` _có thể bị tấn công XSS qua domain khác._  
_👉 Phòng chống: Chỉ cho phép domain tin cậy._

### **6\. SQL Injection là gì và phòng chống?**

**📌 Trả lời:**  
_SQL Injection xảy ra khi input không được kiểm soát._  
_👉 Phòng chống:_

*   _Dùng_ **_PreparedStatement_** _thay vì string concat._
    
*   _ORM (Hibernate/JPA)._
    
*   _Validation input._
    

### **7\. Directory Traversal Attack là gì?**

**📌 Trả lời:**  
_Attacker truy cập file ngoài ý muốn bằng_ `../../etc/passwd`_._  
_👉 Phòng chống:_

*   _Normalize path._
    
*   _Giới hạn file chỉ trong thư mục cho phép._
    
*   _Không tin tưởng user input._
    

### **8\. Clickjacking là gì?**

**📌 Trả lời:**  
_Kẻ tấn công nhúng trang trong iframe để dụ người dùng click nhầm._  
_👉 Phòng chống:_

*   _HTTP Header:_ `X-Frame-Options: DENY`_._
    
*   _CSP:_ `frame-ancestors 'none'`_._
    

### **9\. Cách bảo mật API Key trong Java ứng dụng?**

**📌 Trả lời:**

*   _Không commit vào GitHub._
    
*   _Dùng Secret Manager (AWS Secrets Manager, HashiCorp Vault)._
    
*   _Mã hóa khi lưu trong DB._
    

### **10\. Brute Force attack là gì và ngăn chặn thế nào?**

**📌 Trả lời:**  
_Kẻ tấn công thử nhiều username/password._  
_👉 Phòng chống:_

*   _Rate limiting (bucket4j, Redis)._
    
*   _Captcha._
    
*   _Lock tài khoản sau nhiều lần sai._
    

### **11\. HTTPS giúp bảo mật như thế nào?**

**📌 Trả lời:**  
_Mã hóa dữ liệu client ↔ server bằng TLS._  
_👉 Bắt buộc dùng HTTPS, kèm HSTS để ngăn downgrade attack._

### **12\. Spring Security filter chain hoạt động thế nào?**

**📌 Trả lời:**  
_Request đi qua chain filter (_`UsernamePasswordAuthenticationFilter`_,_ `JwtAuthenticationFilter`_...). Nếu authentication/authorization fail → block request._

### **13\. Password nên lưu trữ như thế nào trong DB?**

**📌 Trả lời:**

*   _Không bao giờ lưu plaintext._
    
*   _Hash bằng_ **_bcrypt/argon2_** _với salt._
    
*   _Đặt cost factor hợp lý (10–12)._
    

## **14\. OAuth2 trong Spring Boot dùng để làm gì?**

**📌 Trả lời:**

_Chuẩn xác thực ủy quyền, cho phép ứng dụng third-party truy cập API mà không cần chia sẻ mật khẩu._

### **15\. Open Redirect là gì?**

**📌 Trả lời:**  
_Attacker chèn URL độc hại vào redirect._  
_👉 Phòng chống: Chỉ cho phép redirect tới whitelist domain._

### **16\. Mass Assignment vulnerability là gì?**

**📌 Trả lời:**

_Người dùng gửi thêm field nhạy cảm ngoài ý muốn (ví dụ:_ `role=admin`_)._  
_👉 Phòng chống:_

*   _Dùng DTO thay vì bind trực tiếp entity._
    
*   _Validate input._
    

### **17\. Logging có thể gây lộ thông tin nhạy cảm không?**

**📌 Trả lời:**

_Có. Không được log password, token, số thẻ tín dụng._  
_👉 Phòng chống: Dùng_ `masking` _log._

### **18\. RCE (Remote Code Execution) trong Java có thể xảy ra khi nào?**

**📌 Trả lời:**

*   _Deserialization không an toàn (_`ObjectInputStream`_)._
    
*   _Eval code injection._
    

_👉 Phòng chống: Luôn validate input và tránh_ `Serializable` _object không cần thiết._

### **19\. Rate Limiting trong Spring Boot triển khai thế nào?**

**📌 Trả lời:**

*   _Bucket4j._
    
*   _Redis + Lua script._
    
*   _Spring Cloud Gateway filter._
    

### **20\. Cách chống DoS/DDoS ở mức ứng dụng Java?**

**📌 Trả lời:**

*   _Rate limiting._
    
*   _Circuit breaker (Resilience4j)._
    
*   _Thread pool isolation._
    

### **21\. Xác thực 2FA triển khai trong Spring Boot như thế nào?**

**📌 Trả lời:**

*   _Sau khi login (username/password) → phát hành temp token._
    
*   _Yêu cầu OTP (Google Authenticator / SMS / Email)._
    
*   _Sau xác thực thành công → phát hành JWT chính thức._
    

### **22\. JSON Injection là gì?**

**📌 Trả lời:**  
_Khi API nhận JSON chưa validate, attacker có thể chèn field độc hại._  
_👉 Phòng chống: Validate input bằng_ `@Valid` _+ DTO + Bean Validation._

### **23\. Lỗ hổng Insecure Deserialization là gì?**

**📌 Trả lời:**  
_Attacker gửi object serialized chứa payload để thực thi code._  
_👉 Phòng chống: Không deserialize dữ liệu không tin cậy. Dùng JSON thay vì Java Serialization._

### **24\. Cách bảo mật REST API bằng API Gateway?**

**📌 Trả lời:**

*   _Centralized auth (JWT, OAuth2)._
    
*   _Rate limiting._
    
*   _Request validation._
    
*   _Logging & tracing._
    

### **25\. Nguy cơ từ Object Mapping (Jackson)?**

**📌 Trả lời:**  
_Attacker có thể khai thác_ `@JsonTypeInfo` _để thực thi class độc hại._  
_👉 Phòng chống: Tắt default typing:_ `mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)`_._

### **26\. Security Header quan trọng cần bật trong Java backend?**

**📌 Trả lời:**

*   `Strict-Transport-Security`
    
*   `X-Frame-Options`
    
*   `X-Content-Type-Options`
    
*   `Content-Security-Policy`
    

### **27\. LFI/RFI là gì?**

**📌 Trả lời:**

_Local File Inclusion / Remote File Inclusion cho phép attacker đọc/gọi file ngoài ý muốn._  
_👉 Phòng chống: Không bao giờ truyền path/file trực tiếp từ user input._

### **28\. Bảo mật file upload trong Spring Boot như thế nào?**

**📌 Trả lời:**

*   _Kiểm tra MIME type & extension._
    
*   _Giới hạn kích thước._
    
*   _Lưu file ngoài webroot._
    
*   _Scan virus (ClamAV)._
    

### **29\. Security Testing nào cần thực hiện cho Java backend?**

**📌 Trả lời:**

*   _Static Analysis (SonarQube, SpotBugs)._
    
*   _Dynamic testing (OWASP ZAP, Burp Suite)._
    
*   _Penetration test._
    

### **30\. Các lỗ hổng OWASP Top 10 cần chú ý khi viết Java backend?**

**📌 Trả lời:**

*   _Injection (SQL, XSS)._
    
*   _Broken Authentication._
    
*   _Sensitive Data Exposure._
    
*   _XML External Entities (XXE)._
    
*   _Broken Access Control._
    
*   _Security Misconfiguration._
    
*   _XSRF._
    
*   _Insecure Deserialization._
    
*   _Using components with known vulnerabilities._
    
*   _Insufficient logging & monitoring._
    

#### **_👉_  Đăng ký ngay khoá học** [**Chuyên Gia Bảo Mật Ứng Dụng**](https://vi.nguyentienkhoi.hashnode.dev/courses/chuyen-gia-bao-mat-ung-dung) **để nâng cao kỹ năng bảo vệ ứng dụng của bạn.**

