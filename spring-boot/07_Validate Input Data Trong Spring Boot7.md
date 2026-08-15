# Validate Input Data Trong Spring Boot

### **1\. Vì Sao Cần Validate Data Trong Spring Boot?**

*   Validate dữ liệu là bước **quan trọng bắt buộc** khi xây dựng API. Dù frontend đã validate trước đó, backend **vẫn cần kiểm tra lại** bởi:
    
*   Backend là nơi làm việc trực tiếp với **database**. Nếu để dữ liệu không hợp lệ đi vào hệ thống, sẽ dẫn đến nhiều vấn đề nghiêm trọng.
    
*   Tránh nguy cơ **SQL Injection** hoặc hacker chèn dữ liệu độc hại.
    
*   Giúp hệ thống **ổn định và an toàn hơn**.
    

👉 Vì vậy, validation trong Spring Boot là một **best practice** cần áp dụng ở mọi dự án.

### **2\. Thêm Dependency Validation**

Thêm dependency sau vào `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### **3\. Các Annotation Validation Thường Dùng**

Spring Boot hỗ trợ sẵn nhiều annotation giúp validate nhanh chóng:

*   `@NotNull` → không cho phép giá trị **null**.
    
*   `@NotEmpty` → danh sách/collection không được **trống**.
    
*   `@NotBlank` → chuỗi không được rỗng (`""`).
    
*   `@Min`, `@Max` → giới hạn giá trị số.
    
*   `@Pattern` → validate bằng **regex**.
    
*   `@Email` → validate định dạng email.
    

### **4\. Validate Request Body (Payload)**

📌 Ví dụ: Validate api: `POST /user/add`

– Tạo `UserRequestDTO` sử dụng các annotation `@NotBlank`, `@NotNull`, `@NotEmpty`, `@Email`, `@Pattern` để validate giá trị dữ liệu được user truyền vào

```java
public class UserRequestDTO implements Serializable {

    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotNull(message = "lastName must be not null")
    private String lastName;

    @Email(message = "email invalid format")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "phone invalid format")
    private String phone;

    @NotNull(message = "dateOfBirth must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dateOfBirth;

    @NotNull(message = "username must be not null")
    private String username;

    @NotNull(message = "password must be not null")
    private String password;

    @NotEmpty(message = "addresses can not empty")
    private Set<Address> addresses;
}
```

– Tạo `UserController` sử dụng annotation `@Valid` để chỉ định kiểm duyệt giá trị Payload (`UserRequestDTO`).

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/add")
    public String addUser(@Valid @RequestBody UserRequestDTO user) {
        return "User added";
    }
}
```

### **5\. Validate PathVariable và RequestParam**

**– Validate** `PathVariable` sử dụng annotation `@Validated` và `@Min`

```plaintext
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

	@GetMapping("/detail/{userId}")
	public String getUserDetail(@PathVariable @Min(value = 1, message = "userId must be greater than 0") int userId) {
    	return "FoxDev";
	}
}
```

**– Validate Request Parameters** 

```java
@GetMapping("/list")
public List getAllUser(
        @RequestParam(defaultValue = "0") int pageNo, 
        @Min(10) @RequestParam(defaultValue = "20") int pageSize) {
    return List.of(
        new UserRequestDTO("Tay", "Java", "admin@foxdev.vn", "0123456789"),
        new UserRequestDTO("Leo", "Messi", "leomessi@email.com", "0123456456")
    );
}
```

### **6\. Tạo Custom Validator Annotation**

Ngoài annotation có sẵn, bạn có thể tự tạo annotation.  
Ví dụ validate **số điện thoại**:

– `PhoneValidator.class`

```java
public class PhoneValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext cxt) {
        if(phoneNo == null) return false;
        if (phoneNo.matches("\\d{10}")) return true;
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
        else return phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}");
    }
}
```

– `PhoneNumber.class`

```java
@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
    String message() default "Invalid phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

→ Áp dụng trong DTO:

```java
@PhoneNumber(message = "phone invalid format")
private String phone;
```

### **7\. Test API Với Postman**

Bạn có thể test **RequestBody**, **PathVariable**, **RequestParam** bằng Postman hoặc curl.

– Ví dụ test RequestBody:

```plaintext
curl --location 'http://localhost:8080/user/add' \
--header 'Content-Type: application/json' \
--data-raw '{
  "firstName": "",
  "lastName": null,
  "email": "admin@foxdev",
  "phone": "118228"
}'
```

– Kết quả:

```plaintext
{
  "timestamp": "2024-03-28T21:10:38.397+00:00",
  "status": 400,
  "error": "Bad Request",
  "path": "/user/add"
}
```

### **8\. Kết Luận**

*   **Validation trong Spring Boot** là bước quan trọng để bảo mật và nâng cao chất lượng dữ liệu.
    
*   Sử dụng annotation có sẵn (`@NotNull`, `@Email`, `@Min`, ...) kết hợp với **custom validator** để linh hoạt hơn.
    
*   Test kỹ lưỡng bằng Postman trước khi đưa vào production.
    

👉 Xem source code đầy đủ tại: [GitHub FoxDev Sample Code](https://github.dev/luongquoctay87/tayjava-sample-code/tree/bai-3-validate-input-data)

