# Parameter Reflection

### **1\. Parameter Reflection là gì?**

*   **Parameter Reflection** là tính năng được giới thiệu từ **Java 8** trong package `java.lang.reflect`.
*   Nó cho phép chúng ta **lấy thông tin chi tiết về tham số của phương thức (method) hoặc constructor** tại runtime.
*   Các thông tin có thể truy vấn:
    *   Tên tham số (nếu biên dịch với `-parameters`)
    *   Kiểu dữ liệu (type)
    *   Modifier (final, synthetic, varargs, …)
    *   Annotation gắn trên tham số

👉 Trước Java 8, **không thể lấy tên tham số thật** (chỉ thấy dạng `arg0`, `arg1`).

📌 **Ví dụ: Lấy thông tin tham số**

```java
import java.lang.reflect.*;

public class App {

    public void greet(String name, int age) {
        System.out.println("Hello " + name + ", age: " + age);
    }

    public static void main(String[] args) throws Exception {
        Method method = ParameterReflectionExample.class.getMethod("greet", String.class, int.class);

        Parameter[] parameters = method.getParameters();
        for (Parameter param : parameters) {
            System.out.println("Tên: " + param.getName());
            System.out.println("Kiểu: " + param.getType());
            System.out.println("Có final không? " + param.isFinal());
            System.out.println("Có varargs không? " + param.isVarArgs());
            System.out.println("--------------");
        }
    }
}
```

– Kết quả:

```java
Tên: name
Kiểu: class java.lang.String
Có final không? false
Có varargs không? false
--------------
Tên: age
Kiểu: int
Có final không? false
Có varargs không? false
--------------
```

### **2\. Các method quan trọng trong** `**Parameter**`

| Method | Mô tả |
| --- | --- |
| `getName()` | Lấy tên tham số (chỉ chính xác khi biên dịch với `-parameters`). |
| `getType()` | Lấy kiểu dữ liệu của tham số. |
| `getParameterizedType()` | Lấy generic type (nếu có). |
| `isNamePresent()` | Kiểm tra xem tên tham số có sẵn không. |
| `isVarArgs()` | Kiểm tra tham số có phải varargs không (`...`). |
| `isFinal()` | Kiểm tra tham số có modifier `final` không. |
| `getAnnotations()` | Lấy tất cả annotation gắn trên tham số. |

📌 **Ví dụ với Annotation**

```java
import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@interface ParamInfo {
    String value();
}
```

```java
public class App {
    public void greet(@ParamInfo("Tên người dùng") String name) {}

    public static void main(String[] args) throws Exception {
        Method method = AnnotationExample.class.getMethod("greet", String.class);

        Parameter param = method.getParameters()[0];
        if (param.isAnnotationPresent(ParamInfo.class)) {
            ParamInfo info = param.getAnnotation(ParamInfo.class);
            System.out.println("Annotation: " + info.value());
        }
    }
}
```

– Kết quả:

```java
Annotation: Tên người dùng
```

### **3\. Ứng dụng của Parameter Reflection**

*   Dùng trong **frameworks** (Spring, Hibernate, Jackson, …) để:
    *   Tự động ánh xạ JSON/XML vào đối tượng.
    *   Tạo API documentation (Swagger, OpenAPI).
    *   Dependency Injection (DI).
*   Dùng để **build tools phân tích mã nguồn** hoặc **framework custom annotation**.

