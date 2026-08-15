# Tuyển Tập 15 Câu Hỏi Nâng Cao Về Spring Security Dành Cho Senior Java Developer

**Spring Security** là framework bảo mật mạnh mẽ và phổ biến nhất trong hệ sinh thái Java. Đối với một **Senior Java Developer**, việc nắm chắc cơ chế bên trong Spring Security là điều bắt buộc. Bài viết này tổng hợp **15 câu hỏi nâng cao** kèm lời giải thích chi tiết.

### **1\. Spring Security hoạt động theo nguyên lý Filter Chain như thế nào?**

**📌 Trả lời:**  
_Spring Security cài đặt một chuỗi_ `FilterChainProxy`_, trong đó mỗi filter xử lý một phần của bảo mật (authentication, authorization, CSRF, session, header…). Mọi request HTTP đều phải đi qua chuỗi filter này trước khi đến controller._

### **2\. AuthenticationManager và AuthenticationProvider khác nhau như thế nào?**

**📌 Trả lời:**

*   `AuthenticationManager`_: interface chịu trách nhiệm xác thực tổng quát._
    
*   `AuthenticationProvider`_: xử lý một loại cơ chế xác thực cụ thể (username/password, JWT, LDAP…)._ `AuthenticationManager` _có thể chứa nhiều_ `AuthenticationProvider`_._
    

### **3\. Cơ chế PasswordEncoder trong Spring Security là gì?**

**📌 Trả lời:**  
`PasswordEncoder` _định nghĩa cách hash và verify mật khẩu. Spring Security 5+ dùng_ `DelegatingPasswordEncoder` _với cú pháp_ `{id}encodedPassword` _để hỗ trợ nhiều thuật toán (BCrypt, PBKDF2, SCrypt...)._

### **4\. Phân biệt giữa Authentication và Authorization trong Spring Security?**

**📌 Trả lời:**

*   **_Authentication_**_: Xác thực danh tính (bạn là ai)._
    
*   **_Authorization_**_: Phân quyền (bạn được phép làm gì)._
    

_– Ví dụ: Login thành công là_ **_Authentication_**_, còn kiểm tra role trước khi vào API là_ **_Authorization_**_._

### **5\. Cách tùy chỉnh login form trong Spring Security?**

**📌 Trả lời:**  
_Sử dụng_ `HttpSecurity.formLogin()` _để chỉ định_ `loginPage`_,_ `loginProcessingUrl`_,_ `defaultSuccessUrl`_,_ `failureUrl`_. Ngoài ra có thể viết_ `AuthenticationSuccessHandler` _và_ `AuthenticationFailureHandler` _để xử lý logic sau login._

### **6\. Spring Security lưu trữ thông tin Authentication sau khi login ở đâu?**

**📌 Trả lời:**  
_Thông tin_ `Authentication` _được lưu trong_ `SecurityContext`_. Mặc định_ `SecurityContext` _được lưu trong_ `HttpSession` _thông qua_ `SecurityContextPersistenceFilter`_._

### **7\.** `@PreAuthorize` **và** `@PostAuthorize` **hoạt động thế nào?**

**📌 Trả lời:**

*   `@PreAuthorize`_: kiểm tra quyền_ **_trước_** _khi method chạy._
    
*   `@PostAuthorize`_: kiểm tra quyền_ **_sau_** _khi method chạy (có thể kiểm tra kết quả trả về)._  
    _Cả hai dựa trên Spring Expression Language (SpEL)._
    

### **8\. Cách phân quyền bằng Role và Authority khác nhau thế nào?**

**📌 Trả lời:**

*   **_Role_**_: thường có prefix_ `ROLE_`_._
    
*   **_Authority_**_: quyền chi tiết (_`READ_PRIVILEGE`_,_ `WRITE_PRIVILEGE`_)._  
    _Trong Spring Security,_ `hasRole("ADMIN")` _thực chất =_ `hasAuthority("ROLE_ADMIN")`_._
    

### **9\. CSRF trong Spring Security là gì và tại sao mặc định bật?**

**📌 Trả lời:**  
_CSRF (Cross Site Request Forgery) bảo vệ người dùng khỏi request giả mạo từ trang khác. Spring Security bật mặc định để tránh POST/PUT/DELETE bị khai thác, buộc client phải gửi token CSRF hợp lệ._

### **10\. Stateless Authentication trong Spring Security là gì?**

**📌 Trả lời:**  
_Server không lưu session, mọi request đều phải kèm token (JWT, OAuth2)._  
_Cấu hình bằng:_

```java
sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
```

### 11\. JWT filter được tích hợp trong Spring Security như thế nào?

**📌 Trả lời:**

_Tạo custom_ `OncePerRequestFilter` _để:_

1.  _Lấy JWT từ header Authorization._
    
2.  _Parse & validate._
    
3.  _Tạo_ `UsernamePasswordAuthenticationToken`_._
    
4.  _Set vào_ `SecurityContextHolder`_. Filter này đặt trước_ `UsernamePasswordAuthenticationFilter`_._
    

### **12\. Cách tích hợp OAuth2 với Spring Security?**

**📌 Trả lời:**  
_Spring Security cung cấp_ `spring-security-oauth2-client`_. Chỉ cần cấu hình_ `spring.security.oauth2.client.registration.*` _trong_ `application.yml` _và bật_ `.oauth2Login()` _trong_ `HttpSecurity`_._

### **13\. Tại sao cần SecurityContextHolder?**

**📌 Trả lời:**  
`SecurityContextHolder` _là nơi lưu_ `SecurityContext` _(authentication + authorities) trong ThreadLocal. Nhờ đó có thể lấy thông tin user hiện tại ở bất kỳ đâu trong cùng một luồng xử lý._

### **14\. Cách tùy chỉnh AccessDecisionManager?**

**📌 Trả lời:**

`AccessDecisionManager` _quyết định cho phép/từ chối request dựa trên quyền. Có thể:_

*   _Viết custom_ `AccessDecisionVoter`_._
    
*   _Hoặc override_ `AccessDecisionManager` _để implement logic đặc thù (ví dụ theo business rule phức tạp)._
    

### **15\. Cách bảo mật REST API trong Spring Security?**

**📌 Trả lời:**

*   _Dùng JWT (stateless)._
    
*   _Tắt CSRF (_`csrf().disable()`_)._
    
*   _Phân quyền theo role bằng_ `authorizeHttpRequests()`_._
    
*   _Sử dụng HTTPS._
    
*   _Thêm rate limiting + logging để tăng an toàn._
    

#### **_👉_  Đăng ký ngay khoá học** [**Chuyên Gia Bảo Mật Ứng Dụng**](https://vi.nguyentienkhoi.hashnode.dev/courses/chuyen-gia-bao-mat-ung-dung) **để nâng cao kỹ năng bảo vệ ứng dụng của bạn.**

