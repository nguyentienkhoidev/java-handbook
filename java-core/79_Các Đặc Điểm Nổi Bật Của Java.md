# Các Đặc Điểm Nổi Bật Của Java 23

Ngày **17/09/2024**, Oracle chính thức phát hành **Java SE 23**. Đây là bản **non-LTS** (không phải Long-Term Support), nhưng mang đến nhiều cải tiến quan trọng về **ngôn ngữ, thư viện, JVM và công cụ phát triển**.

### **1\. Markdown trong Javadoc (JEP 467 – Final)**

Từ Java 23, bạn có thể viết tài liệu bằng **Markdown** trực tiếp trong Javadoc, thay vì phải dùng HTML cứng hoặc tag phức tạp. Việc này giúp viết tài liệu dễ đọc hơn, đặc biệt với các dự án lớn.

```java
/**
 * # Calculator Class
 *
 * Đây là ví dụ **Markdown** trong Javadoc.
 *
 * - Hỗ trợ tiêu đề
 * - Hỗ trợ in đậm / in nghiêng
 * - Hỗ trợ danh sách
 */
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

### **2\. Generational ZGC Mặc Định (JEP 474 – Final)**

*   **ZGC (Z Garbage Collector)** vốn nổi tiếng vì độ trễ thấp.
    
*   Từ Java 23, **ZGC mặc định sử dụng generational mode** → tách đối tượng thành **Young** và **Old Generation**, giúp thu gom rác hiệu quả hơn.
    

👉 Bạn không cần chỉnh JVM option, chỉ cần chạy ứng dụng là được hưởng lợi.

### **3\. Pattern Matching hỗ trợ Primitive Types (JEP 455 – Preview)**

Trước đây bạn chỉ dùng được wrapper (`Integer`, `Double`). Với Java 23 bạn có thể dùng **primitive types** (`int`, `double`, `long`) trực tiếp trong `switch` và `instanceof`.

```java
Object o = 42;

String result = switch (o) {
    case int i    -> "Số nguyên: " + i;
    case double d -> "Số thực: " + d;
    case String s -> "Chuỗi: " + s;
    default       -> "Không xác định";
};

System.out.println(result); // Số nguyên: 42
```

### **4\. Flexible Constructor Bodies (JEP 482 – 2nd Preview)**

Cho phép viết **câu lệnh trước** `super()` **hoặc** `this()` trong constructor. Giúp constructor dễ viết, dễ thêm logic kiểm tra/khởi tạo.

```java
public class Parent {
    Parent(int x) {
        System.out.println("Parent: " + x);
    }
}
```

```java
public class Child extends Parent {
    Child(int x) {
        if (x < 0) throw new IllegalArgumentException("x không được âm");
        super(x); // Trước đây phải luôn là dòng đầu tiên
        System.out.println("Child: " + x);
    }
}
```

```java
public class App {
    public static void main(String[] args) {
        new Child(10);
    }
}
```

### **5\. Stream Gatherers (JEP 473 – 2nd Preview)**

Thêm **Gatherers API** để mở rộng Stream pipeline với các thao tác phức tạp hơn, Giúp Stream API linh hoạt hơn nhiều.

📌 Ví dụ: nhóm các phần tử liên tiếp thành cặp.

```java
import java.util.stream.*;
import java.util.stream.Gatherers;

public class App {
    public static void main(String[] args) {
        Stream.of(1, 2, 3, 4, 5, 6)
              .gather(Gatherers.windowFixed(2)) // Gom thành cửa sổ 2 phần tử
              .forEach(System.out::println);
    }
}
```

– Kết quả:

```java
[1, 2]
[2, 3]
[3, 4]
[4, 5]
[5, 6]
```

### **6\. Structured Concurrency (JEP 480 – 3rd Preview)**

Quản lý nhiều tác vụ song song theo **cấu trúc** (giống như scope), dễ kiểm soát hơn. Dễ dùng hơn `CompletableFuture`, code ngắn gọn và an toàn hơn.

```java
import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Future<String> f1 = scope.fork(() -> "Dữ liệu API 1");
            Future<String> f2 = scope.fork(() -> "Dữ liệu API 2");

            scope.join();             // Chờ tất cả xong
            scope.throwIfFailed();    // Ném lỗi nếu có

            System.out.println(f1.resultNow() + " + " + f2.resultNow());
        }
    }
}
```

### **7\. Scoped Values (JEP 481 – 3rd Preview)**

Thay thế `ThreadLocal` để chia sẻ **giá trị bất biến** giữa các thread hoặc virtual thread. An toàn và dễ quản lý hơn `ThreadLocal`

```java
import jdk.incubator.concurrent.ScopedValue;

public class App {
    static final ScopedValue<String> USER = ScopedValue.newInstance();

    public static void main(String[] args) {
        ScopedValue.runWhere(USER, "Fox Dev", () -> {
            System.out.println("Xin chào " + USER.get());
        });
    }
}
```

### **8\. Vector API (JEP 469 – 8th Incubator)**

Giúp tận dụng **SIMD instructions** để xử lý mảng nhanh hơn, Rất hữu ích cho AI/ML, xử lý ảnh, dữ liệu lớn.

```java
import jdk.incubator.vector.*;

public class App {
    public static void main(String[] args) {
        var v1 = IntVector.fromArray(IntVector.SPECIES_256, new int[]{1,2,3,4}, 0);
        var v2 = IntVector.fromArray(IntVector.SPECIES_256, new int[]{5,6,7,8}, 0);
        var result = v1.add(v2);
        System.out.println(result); // [6, 8, 10, 12]
    }
}
```

### **9\. Class-File API (JEP 466 – 2nd Preview)**

API chuẩn để đọc/ghi file `.class` mà không cần thư viện bên ngoài (ASM, BCEL). Giúp lập trình meta (code gen, phân tích bytecode) trở nên dễ dàng hơn.

```java
import jdk.classfile.*;

public class App {
    public static void main(String[] args) {
        ClassModel cm = ClassFile.of().parse(ClassFileExample.class);
        cm.methods().forEach(m -> System.out.println(m.name()));
    }
}
```

### **10\. Thay đổi & Deprecation**

*   **JEP 471**: Deprecate các phương thức truy cập bộ nhớ trong `sun.misc.Unsafe`.
    
*   **String Templates** (preview ở Java 21/22) đã **bị loại bỏ** khỏi Java 23.
    

#### 🎯 Kết luận

Java 23 mang đến nhiều cải tiến mạnh mẽ:

*   Hoàn thiện Markdown Javadoc, Generational ZGC.
    
*   Preview: Pattern Matching với primitive, Stream Gatherers, Structured Concurrency, Scoped Values.
    
*   API mới: Class-File API, Vector API.
    

