# Java Reflection API

### 1\. Java Reflection API là gì?

**Reflection API trong Java** là một cơ chế cho phép chương trình **quan sát và thao tác động** (runtime) trên các **class**, **interface**, **field**, **method** mà không cần biết chính xác chúng tại thời điểm biên dịch (compile-time).

Nói cách khác, Reflection cho phép bạn:

*   Lấy thông tin về **class** (tên class, modifiers, super class, interface…).
*   Truy cập và thay đổi **fields** (biến) của đối tượng.
*   Gọi **method** (kể cả private method) trong runtime.
*   Tạo đối tượng từ **class name** khi chỉ biết tên chuỗi.

### 2\. Ứng dụng của Reflection API

*   **Frameworks** (Spring, Hibernate…) dùng Reflection để inject dependency, ánh xạ dữ liệu DB ↔ Object.
*   **IDE / Tools**: Kiểm tra code, gợi ý method, autocomplete.
*   **Testing frameworks** (JUnit): Gọi các method test annotated bằng `@Test`.
*   **Serialization / Deserialization**: Chuyển đổi object thành JSON/XML hoặc ngược lại.

– Ví dụ: Lấy thông tin về Class

```java
public class Person {
    private String name;
    public int age;

    public void sayHello() {
        System.out.println("Hello, I am " + name);
    }
}
```

```java
import java.lang.reflect.*;

public class App {
    public static void main(String[] args) {
        try {
            // Lấy Class object
            Class<?> clazz = Class.forName("Person");

            // In ra tên class
            System.out.println("Tên class: " + clazz.getName());

            // In ra các field
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                System.out.println("Field: " + f.getName() + " - Kiểu: " + f.getType());
            }

            // In ra các method
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                System.out.println("Method: " + m.getName());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

– Ví dụ: 2. Tạo đối tượng và gọi method bằng Reflection

```java
import java.lang.reflect.*;

public class ReflectionInvoke {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("Person");

        // Tạo đối tượng Person
        Object obj = clazz.getDeclaredConstructor().newInstance();

        // Truy cập field private "name"
        Field field = clazz.getDeclaredField("name");
        field.setAccessible(true);
        field.set(obj, "Tây Java");

        // Gọi method sayHello()
        Method method = clazz.getDeclaredMethod("sayHello");
        method.invoke(obj);
    }
}
```

– Kết quả:

```plaintext
Hello, I am Tây Java
```

#### ⚠️ Lưu ý khi dùng Reflection

*   **Hiệu năng**: Reflection chậm hơn so với gọi trực tiếp → tránh lạm dụng.
*   **Bảo mật**: Có thể truy cập vào private fields/methods → dễ bị lộ dữ liệu nếu không kiểm soát.
*   **Khó maintain**: Code reflection thường khó đọc và debug.
