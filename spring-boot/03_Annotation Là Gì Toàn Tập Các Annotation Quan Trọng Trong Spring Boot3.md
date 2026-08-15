# Annotation Là Gì? Toàn Tập Các Annotation Quan Trọng Trong Spring Boot

### **1\. Annotation là gì?**

Annotation trong Java là một **dạng siêu dữ liệu (metadata)** được gắn vào **class, method, parameter, field hoặc package**. Annotation không trực tiếp ảnh hưởng đến cách chương trình chạy, nhưng nó cung cấp thông tin quan trọng cho **compiler**, **Spring Container**, hoặc các framework khác.

👉 Nói đơn giản: Annotation giúp **Spring Boot hiểu cách cấu hình, quản lý, và vận hành ứng dụng** mà không cần viết nhiều file XML phức tạp.

### **2\. Annotation trong Spring Framework (cơ bản)**

#### 📌 `@Autowired`

*   Dùng để **tiêm (inject) dependency** vào setter, constructor, hoặc field.
    
*   Spring Container sẽ tự động tìm **bean phù hợp theo data-type** và inject vào.
    

**– Ví dụ:**

```java
@RestController @RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
}
```

#### 📌 `@Configuration`

*   Đánh dấu **class chứa cấu hình** (configuration class).
    
*   Các bean được định nghĩa trong class này sẽ được quản lý bởi Spring Container.
    

```java
@Configuration
public class AppConfig {
    // nơi khai báo các @Bean
}
```

#### 📌 `@ComponentScan`

Dùng để **chỉ định package mà Spring cần scan** để tìm bean (`@Component`, `@Service`, `@Repository`,…).

```java
@ComponentScan(basePackages = "vn.foxdev.model")
@Configuration
public class AppConfig {

}
```

#### 📌 `@Bean`

*   Được dùng trong class `@Configuration`.
    
*   Spring sẽ quản lý method gắn `@Bean` và trả về một bean.
    

```java
@Configuration 
public class AppConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

#### 📌 `@Component`

Đánh dấu một **class là bean** để Spring quản lý.

```java
@Component
public class S3Client {
    @Autowired
    private AmazonS3 amazonS3Client;
}
```

#### 📌 `@Controller`

*   Annotation trong **Spring MVC**, thuộc tầng presentation.
    
*   Trả về **view (HTML/JSP/Thymeleaf)** thay vì JSON.
    

```java
@Controller
@RequestMapping("/api/v1")
public class SampleController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
}
```

#### 📌 `@Service`

Được sử dụng để đánh dấu class thuộc **business logic layer**.

```java
@Service
public class UserService {
    private final UserRepository userRepository;
}
```

#### 📌 `@Repository`

Được dùng trong **DAO layer** để thao tác với database.

```java
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
```

### **3\. Annotation trong Spring Boot (mở rộng)**

#### 📌 `@EnableAutoConfiguration`

Cho phép Spring Boot **tự động cấu hình** ứng dụng dựa trên các dependency có trong _classpath_.

Từ Spring Boot 1.2.0 trở đi, annotation này được gộp vào `@SpringBootApplication`.

```java
@SpringBootApplication
@EnableAutoConfiguration 
public class SampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}
```

#### 📌 `@SpringBootApplication`

Là annotation quan trọng nhất trong Spring Boot. `@SpringBootApplication` là sự kết hợp của 3 annotation sau:

*   `@EnableAutoConfiguration`
    
*   `@ComponentScan`
    
*   `@Configuration`
    

```java
@SpringBootApplication
public class SampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}
```

#### 📌 Annotation trong Spring MVC (REST API)

*   `@RequestMapping`: Định nghĩa URL endpoint cho class hoặc method.
    

```java
@RestController
@RequestMapping("/users") 
public class UserController {
	
}
```

*   `@GetMapping`:  Nhận request **HTTP GET** (lấy dữ liệu).
    

```java
@GetMapping("/list")
public List<User> getUsers() {
    return userService.getUsers();
}
```

*   `@PostMapping`: Nhận request **HTTP POST** (thêm mới dữ liệu).
    

```java
@PostMapping("/add")
public long createUser(@RequestBody UserCreationRequest request) {
    return userService.addUser(request);
}
```

*   `@PutMapping`: Nhận request **HTTP PUT** (cập nhật toàn bộ dữ liệu).
    

```java
@PutMapping("/update")
public void updateUser(@RequestBody UserUpdateRequest request) {
    userService.updateUser(request);
}
```

*   `@PatchMapping`: Nhận request **HTTP PATCH** (cập nhật một phần dữ liệu).
    

```java
@PatchMapping("/user/{id}/change-status")
public void changeStatus(@PathVariable long id, @RequestParam String status) {
    userService.changeStatus(id, status);
}
```

*   `@DeleteMapping`: Nhận request **HTTP DELETE** (xóa dữ liệu).
    

```java
@DeleteMapping("/del/{id}")
public void deleteUser(@PathVariable long id) {
    userService.deleteUser(id);
}
```

### **4\. Annotation xử lý dữ liệu request/response**

*   `@PathVariable`: lấy dữ liệu từ URL.
    
*   `@RequestBody`: ánh xạ JSON body → Java object.
    
*   `@RequestParam`: lấy query param từ URL.
    
*   `@RequestHeader`: lấy thông tin từ HTTP header.
    
*   `@RequestAttribute`: lấy dữ liệu được gắn vào request từ interceptor.
    
*   `@ResponseBody`: trả về dữ liệu JSON/XML.
    
*   `@RestController`: kết hợp `@Controller + @ResponseBody`.
    

### **5\. Kết luận**

Annotation trong Spring Boot giúp **giảm bớt cấu hình phức tạp**, tăng tính **tự động hóa** và **dễ đọc code**. Khi nắm vững các annotation quan trọng (từ `@Component`, `@Autowired`, `@Service` đến `@RestController`, `@SpringBootApplication`), bạn sẽ dễ dàng phát triển ứng dụng Spring Boot **theo chuẩn RESTful API**.

👉 Nắm annotation = nắm chìa khóa để hiểu cách Spring Boot hoạt động.

