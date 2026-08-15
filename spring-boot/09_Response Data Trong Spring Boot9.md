# Response Data Trong Spring Boot

Trong quá trình phát triển API với **Spring Boot**, việc chuẩn hóa dữ liệu phản hồi (Response Data) là cực kỳ quan trọng. Một cấu trúc response rõ ràng giúp frontend dễ dàng xử lý, đồng thời hỗ trợ debug và maintain code lâu dài.

Trong bài viết này, mình sẽ hướng dẫn 2 cách phổ biến:

1.  Sử dụng **ResponseEntity** với class tùy chỉnh.
    
2.  Sử dụng **Generic Class** để trả về dữ liệu động.
    

### **1\. Response Data với** `ResponseEntity`

1\. Tạo `ResponseSuccess`

Đây là class dùng để trả về phản hồi khi API xử lý thành công.

```java
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class ResponseSuccess extends ResponseEntity {

    public ResponseSuccess(HttpStatus status, String message) {
        super(new Payload(status.value(), message), HttpStatus.OK);
    }

    public ResponseSuccess(HttpStatus status, String message, Object data) {
        super(new Payload(status.value(), message, data), status);
    }

    public ResponseSuccess(Payload body, HttpStatus status) {
        super(body, status);
    }

    public ResponseSuccess(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public ResponseSuccess(Payload payload, MultiValueMap<String, String> headers, int rawStatus) {
        super(payload, headers, rawStatus);
    }

    public ResponseSuccess(Payload payload, MultiValueMap<String, String> headers, HttpStatus status) {
        super(payload, headers, status);
    }

    public static class Payload {
        private final int status;
        private final String message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object data;

        public Payload(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public Payload(int status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public int getStatus() { return status; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
}
```

2\. Tạo `ResponseFailure`

Khi xử lý thất bại, ta có thể tái sử dụng `ResponseSuccess` bằng cách kế thừa:

```java
public class ResponseFailure extends ResponseSuccess {
    public ResponseFailure(HttpStatus status, String message) {
        super(status, message);
    }
}
```

3\. Áp dụng vào `UserController`

```java
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @PostMapping("/")
    public ResponseSuccess addUser(@Valid @RequestBody UserRequestDTO user) {
        try {
            return new ResponseSuccess(HttpStatus.CREATED, "User added successfully,", 1);
        } catch (Exception e) {
            return new ResponseFailure(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseSuccess getUser(@PathVariable @Min(1) int userId) {
        try {
            return new ResponseSuccess(HttpStatus.OK, "user",
                new UserRequestDTO("Tay", "Java", "admin@foxdev.vn", "0123456789"));
        } catch (Exception e) {
            return new ResponseFailure(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
```

### **2\. Response Data với Generic**

1\. Tạo class `ResponseData<T>`

Dùng **generic** giúp API trả về nhiều kiểu dữ liệu khác nhau.

```java
import com.fasterxml.jackson.annotation.JsonInclude;

public class ResponseData<T> {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
```

2\. Tạo `ResponseError`

```java
public class ResponseError extends ResponseData<Object> {
    public ResponseError(int status, String message) {
        super(status, message);
    }
}
```

3\. Áp dụng tại `UserController`

```java
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @PostMapping("/")
    public ResponseData<Integer> addUser(@Valid @RequestBody UserRequestDTO user) {
        return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully,", 1);
    }

    @GetMapping("/{userId}")
    public ResponseData<UserRequestDTO> getUser(@PathVariable @Min(1) int userId) {
        return new ResponseData<>(HttpStatus.OK.value(), "user",
                new UserRequestDTO("Tay", "Java", "admin@foxdev.vn", "0123456789"));
    }

    @GetMapping("/list")
    public ResponseData<List<UserRequestDTO>> getAllUser(
            @RequestParam(defaultValue = "0") int pageNo,
            @Min(10) @RequestParam(defaultValue = "20") int pageSize) {
        return new ResponseData<>(HttpStatus.OK.value(), "users",
                List.of(
                        new UserRequestDTO("Tay", "Java", "admin@foxdev.vn", "0123456789"),
                        new UserRequestDTO("Leo", "Messi", "leomessi@email.com", "0123456456")
                ));
    }
}
```

### 3\. Kết luận

*   Nếu muốn **linh hoạt**, dễ customize header, status code → dùng **ResponseEntity**.
    
*   Nếu muốn **đơn giản, dễ tái sử dụng, code gọn hơn** → dùng **Generic ResponseData<T>**.
    

👉 Source Code: [FoxDev Sample Code](https://github.dev/luongquoctay87/tayjava-sample-code/tree/bai-5-response-data)

