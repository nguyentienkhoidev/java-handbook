# Tuyển Tập 20 Câu Hỏi về Spring Boot Dành Cho Senior Java Developer

### **1\. Sự khác nhau giữa** `@Component`**,** `@Service`**,** `@Repository` **trong Spring Boot?**

**📌 Trả lời:**

*   `@Component`_: annotation chung để đánh dấu bean._
    
*   `@Service`_: chuyên biệt cho tầng service (business logic)._
    
*   `@Repository`_: chuyên biệt cho tầng DAO, có thêm_ **_exception translation_** _cho database._
    

### **2\. Khi nào nên dùng** `@Bean` **thay vì** `@Component`**?**

**📌 Trả lời:**

*   `@Component`_: dùng khi Spring có thể tự động phát hiện qua component scanning._
    
*   `@Bean`_: dùng khi cần khởi tạo thủ công bean phức tạp (ví dụ: kết nối Redis, Kafka, ObjectMapper custom)._
    

### **3\. Spring Boot xử lý dependency injection như thế nào?**

**📌 Trả lời:**

_Spring Boot sử dụng_ **_IoC container (ApplicationContext)_** _và_ **_reflection + proxies_** _để inject dependency. Nó tìm kiếm bean theo loại (_`byType`_) hoặc tên (_`byName`_) dựa trên context configuration._

### **4\. Cách Spring Boot khởi động nhanh hơn Spring truyền thống?**

**📌 Trả lời:**

*   _Auto-configuration: thay vì XML phức tạp._
    
*   _Embedded server (Tomcat/Jetty/Undertow)._
    
*   _Starters dependency._
    
*   _Externalized configuration (YAML, properties, env)._
    

### **5\. Spring Boot Actuator là gì và dùng trong production như thế nào?**

**📌 Trả lời:** 

_Cung cấp các endpoint (health, metrics, info, env, loggers). Trong production nên:_

*   _Bật bảo mật (chỉ expose endpoint cần thiết)._
    
*   _Tích hợp với Prometheus/Grafana._
    
*   _Tùy chỉnh health check (database, Kafka, Redis…)._
    

### **6\. Cơ chế AOP trong Spring hoạt động thế nào?**

**📌 Trả lời:**

*   _Dùng JDK Dynamic Proxy (nếu interface có sẵn)._
    
*   _Dùng CGLIB Proxy (nếu không có interface)._  
    _AOP inject cross-cutting concern (logging, transaction, security) vào runtime mà không cần sửa code business._
    

### **7\. Spring Boot xử lý transaction như thế nào?**

**📌 Trả lời:** 

*   _Sử dụng_ `@Transactional` _kết hợp AOP. Khi gọi method, Spring tạo proxy, bắt đầu transaction, commit/rollback tùy thuộc exception._
    

### **8\. Sự khác nhau giữa** `ApplicationContext` **và** `BeanFactory`**?**

**📌 Trả lời:**

*   `BeanFactory`_: IoC container cơ bản, lazy loading._
    
*   `ApplicationContext`_: mở rộng BeanFactory, hỗ trợ AOP, event, message source, eager loading. Spring Boot dùng_ `ApplicationContext`_._
    

### **9\. Ưu nhược điểm của Spring Boot Starter?**

**📌 Trả lời:**

*   **_Ưu điểm:_** _cấu hình nhanh, giảm boilerplate._
    
*   **_Nhược điểm:_** _project có thể phình to vì kéo dependency không cần thiết. Senior dev nên loại bỏ starter thừa (_`spring-boot-starter-data-jpa` _nếu chỉ dùng JDBC)._
    

### **10\. Làm thế nào để xử lý cấu hình khác nhau cho các môi trường (dev, staging, prod)?**

**📌 Trả lời:**

*   _Sử dụng_ `application-{profile}.properties` _hoặc YAML._
    
*   _Dùng_ `@Profile` _để load bean theo môi trường._
    
*   _Kết hợp Spring Cloud Config hoặc Vault để quản lý config tập trung._
    

### **11\. Cách Spring Boot quản lý thread pool cho Async?**

**📌 Trả lời:**

*   _Dùng_ `@EnableAsync` _+_ `@Async`_._
    
*   _Thread pool config qua_ `TaskExecutor` _(corePoolSize, maxPoolSize, queueCapacity)._
    
*   _Nếu không config, Spring dùng_ `SimpleAsyncTaskExecutor` _(không thực sự tái sử dụng thread → không nên dùng production)._
    

### **12\. Spring Boot xử lý exception toàn cục như thế nào?**

**📌 Trả lời:**

*   _Sử dụng_ `@ControllerAdvice` _+_ `@ExceptionHandler`_._
    
*   _Có thể kết hợp_ `ResponseEntityExceptionHandler`_._
    
*   _Cho REST API, dùng_ `@RestControllerAdvice` _để trả JSON response._
    

### **13\. Sự khác biệt giữa** `@RequestScope`**,** `@SessionScope`**,** `@ApplicationScope`**?**

**📌 Trả lời:**

*   `@RequestScope`_: bean tồn tại trong lifecycle của request._
    
*   `@SessionScope`_: bean tồn tại theo HTTP session._
    
*   `@ApplicationScope`_: bean tồn tại trong lifecycle của ServletContext (singleton toàn ứng dụng)._
    

### **14\. Spring Boot Security xử lý JWT Authentication như thế nào?**

**📌 Trả lời:**

*   _Custom_ `OncePerRequestFilter` _để parse JWT từ header._
    
*   _Validate token, load UserDetails từ DB/cache._
    
*   _Set_ `Authentication` _vào_ `SecurityContextHolder`_._
    
*   _Spring Security filter chain xử lý tiếp._
    

### **15\. Cách Spring Boot hỗ trợ reactive programming?**

**📌 Trả lời:** 

_Sử dụng_ **_Spring WebFlux_** _với Project Reactor (_`Mono`_,_ `Flux`_). Thay vì dùng Servlet blocking IO, WebFlux dựa trên Netty non-blocking event-loop._

### **16\. Sự khác nhau giữa Spring MVC và Spring WebFlux?**

**📌 Trả lời:**

*   _Spring MVC: synchronous, blocking I/O, dựa trên Servlet API._
    
*   _Spring WebFlux: asynchronous, non-blocking I/O, scale tốt hơn khi có nhiều concurrent request._
    

### **17\. Cách Spring Boot tối ưu memory footprint?**

**📌 Trả lời:**

*   _Dùng_ `spring-context-indexer` _để tăng tốc bean scanning._
    
*   _Bật lazy initialization (_`spring.main.lazy-initialization=true`_)._
    
*   _Sử dụng GraalVM native image (Spring Native)._
    
*   _Chỉ load starter thực sự cần._
    

### **18\. Khi nào nên dùng** `@Lookup` **trong Spring?**

**📌 Trả lời:**

_Khi một singleton bean cần inject prototype bean._ `@Lookup` _cho phép Spring override method để lấy prototype mới mỗi lần gọi._

### **19\. Cách debug performance trong Spring Boot?**

**📌 Trả lời:**

*   _Actuator + Micrometer + Prometheus/Grafana._
    
*   _Java Flight Recorder (JFR)._
    
*   _Spring Boot DevTools để reload nhanh._
    
*   _Log_ `org.springframework` _ở DEBUG để xem lifecycle bean._
    

### **20\. Làm thế nào để triển khai Spring Boot microservices ở quy mô lớn?**

**📌 Trả lời:**

*   _API Gateway (Spring Cloud Gateway)._
    
*   _Service Discovery (Eureka/Consul)._
    
*   _Config Server (Spring Cloud Config)._
    
*   _Circuit breaker (Resilience4j)._
    
*   _Distributed tracing (Sleuth + Zipkin/Jaeger)._
    
*   _Centralized logging (ELK, OpenSearch)._
    

#### **👉 Đăng ký ngay khoá học** [**Phát Triển Ứng Dụng Spring Boot - Step by Step**](https://vi.nguyentienkhoi.hashnode.dev/courses/phat-trien-ung-dung-spring-boot-step-by-step) **để nâng cao trình độ tay nghề của bạn.**

