# Type Annotations

### **1\. Type Annotations là gì?**

**Type Annotations** là một cải tiến từ **Java 8** mở rộng khả năng gắn **annotation**.

Trước Java 8, annotation chỉ gắn được vào:

*   Class
*   Method
*   Field
*   Parameter

Từ Java 8 trở đi, annotation có thể gắn **ở bất kỳ đâu type được dùng** → tức là trên **type usage** (loại biến, generic type, cast, new, throws, implements, v.v.).

### **2\. Cách dùng Type Annotations**

📌 Ví dụ: Gắn trên generic type

```java
import java.util.List;
import java.lang.annotation.*;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface NonEmpty {}
```

```java
public class Example {
    List<@NonEmpty String> names;
}
```

📌 Ví dụ: Gắn khi khởi tạo đối tượng

```java
new @NonEmpty ArrayList<String>();
```

📌 Ví dụ: Gắn trong `throws`

```java
public void read() throws @NonEmpty IOException {
    // ...
}
```

📌 Ví dụ: Gắn trên `implements` hoặc `extends`

```java
class MyClass implements @NonEmpty Runnable {
    public void run() {}
}
```

### **3\. Các ElementType mới hỗ trợ từ Java 8**

ElementType

Ý nghĩa

`TYPE_PARAMETER`

Annotation đặt trên **khai báo type parameter** (ví dụ `<@NonNull T>`).

`TYPE_USE`

Annotation đặt trên **mọi nơi type được sử dụng** (ví dụ `List<@NonNull String>`, cast, new, throws, …).

📌 **Ví dụ: TYPE\_PARAMETER**

```java
@Target(ElementType.TYPE_PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@interface SampleAnnotation {}
```

```java
public class GenericClass<@SampleAnnotation T> {
    T value;
}
```

### **4\. Ứng dụng của Type Annotations**

*   Kiểm tra Null-safety → Dùng với Checker Framework để báo lỗi compile-time.

```java
@NonNull String name; // Không được phép null
```

*   Xác định luồng dữ liệu (Concurrency Annotations)

```java
@ReadOnly List<String> list;
```

*   Security & Validation: Kiểm tra quyền hạn hoặc dữ liệu hợp lệ ngay trên type.
*   Frameworks hiện đại: Spring, Hibernate, Lombok, Checker Framework đang sử dụng type annotations.
