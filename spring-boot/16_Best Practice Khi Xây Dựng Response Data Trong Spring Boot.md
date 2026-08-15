# Best Practice Khi Xây Dựng Response Data Trong Spring Boot

Để API chuyên nghiệp và dễ bảo trì, ngoài việc chuẩn hóa Response Data, chúng ta nên áp dụng thêm một số **best practice** sau:

### **1\. Sử dụng** `@ControllerAdvice` **và** `@ExceptionHandler` **để xử lý Exception toàn cục**

Thay vì viết `try-catch` lặp lại trong từng controller, ta gom xử lý lỗi tại một chỗ.

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi validate @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
    }

    // Xử lý RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseError> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Xử lý Exception chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleException(Exception ex) {
        return new ResponseEntity<>(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error occurred"),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

👉 Như vậy, trong Controller, anh **không cần try-catch** nữa, code gọn gàng hơn.

### **2\. Log error khi có Exception**

Luôn log lại exception để dễ trace khi debug/monitor production.

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseError> handleRuntimeException(RuntimeException ex) {
        log.error("RuntimeException: ", ex); // log stacktrace
        return new ResponseEntity<>(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

### **3\. Chuẩn hóa Format JSON**

Một response API tốt thường có **3 phần chính**:

```plaintext
{
  "status": 200,
  "message": "OK",
  "data": {
    "id": 1,
    "username": "foxdev"
  }
}
```

*   `status`: mã trạng thái (HTTP status code).
    
*   `message`: thông điệp dễ hiểu cho client.
    
*   `data`: dữ liệu trả về (có thể null).
    

### **4\. Tách** `ResponseData` **thành Common Package**

Để tái sử dụng, anh nên cho `ResponseData`, `ResponseError`, `ResponseSuccess` vào package chung, ví dụ:

```java
vn.nguyentienkhoi.hashnode.devmon.response
```

→ Các service khác (auth, account, order, …) đều có thể dùng chung, không cần viết lại.

### **5\. Gợi ý thêm: dùng** `enum` **cho Message/Error Code**

Thay vì hardcode message, anh có thể quản lý bằng enum:

```java
public enum ApiMessage {
    USER_CREATED("User created successfully"),
    USER_NOT_FOUND("User not found"),
    INTERNAL_ERROR("Internal server error");

    private final String message;

    ApiMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
```

→ Dùng trong Response:

```java
return new ResponseData<>(HttpStatus.CREATED.value(), ApiMessage.USER_CREATED.getMessage(), userId);
```

👉 Ưu điểm: dễ maintain, dễ i18n (đa ngôn ngữ).

### **✅ Tóm lại, Best Practice gồm:**

*   Dùng `@ControllerAdvice` để xử lý lỗi toàn cục.
    
*   Luôn log error.
    
*   Chuẩn hóa response JSON.
    
*   Tách response vào common package.
    
*   Sử dụng enum/code cho message.
    

