# Các Đặc Điểm Nổi Bật Của Java

**Java 22** phát hành tháng 3/2024 mang đến nhiều cải tiến về cú pháp, hiệu năng và API. Đây là bản **non-LTS**, nhưng các tính năng mới của nó rất quan trọng cho những phiên bản LTS sau này.

### **1\. Statements Before** `super()` **trong Constructor (Preview)**

*   Trước đây:
    

```java
class Parent {
    Parent(String msg) {
        System.out.println("Parent: " + msg);
    }
}
```

```java
class Child extends Parent {
    Child(String msg) {
        super(msg); // bắt buộc phải gọi đầu tiên
        System.out.println("Child: " + msg);
    }
}
```

*   Java 22:
    

```java
class Child extends Parent {
    Child(String msg) {
        System.out.println("Chuẩn bị khởi tạo...");
        super(msg); // giờ có thể gọi sau
        System.out.println("Child: " + msg);
    }
}
```

→ Giúp code **linh hoạt hơn** khi cần thực hiện logic trước khi gọi constructor cha.

### **2\. String Templates (Preview – Lần 2)**

```java
String name = "Java";
int version = 22;

// Trước đây
String oldWay = "Xin chào, " + name + " " + version;

// Java 22
String newWay = STR."Xin chào, \{name} \{version}";
System.out.println(newWay); // Output: Xin chào, Java 22
```

→ Cú pháp ngắn gọn, **tránh rối rắm khi nối chuỗi** hoặc dùng `String.format()`.

### **3\. Scoped Values (Preview – Lần 2)**

```java
import jdk.incubator.concurrent.ScopedValue;

public class App {
    static final ScopedValue<String> USER = ScopedValue.newInstance();

    public static void main(String[] args) {
        ScopedValue.where(USER, "TayJava").run(() -> {
            System.out.println("Xin chào " + USER.get());
        });
    }
}
```

→ Scoped Value chỉ tồn tại trong **phạm vi định nghĩa**, an toàn hơn **ThreadLocal** và tránh rò rỉ bộ nhớ.

### **4\. Structured Concurrency (Preview – Lần 2)**

```java
import java.util.concurrent.*;
import jdk.incubator.concurrent.StructuredTaskScope;

public class App {
    public static void main(String[] args) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Future<String> f1 = scope.fork(() -> fetchUser());
            Future<String> f2 = scope.fork(() -> fetchOrders());
            
            scope.join().throwIfFailed();
            
            System.out.println(f1.resultNow() + " - " + f2.resultNow());
        }
    }

    static String fetchUser() { return "User: TayJava"; }
    static String fetchOrders() { return "Orders: 5"; }
}
```

→ Nếu một task lỗi, cả scope sẽ dừng, giúp code **dễ quản lý và an toàn hơn**.

### **5\. Foreign Function & Memory API (Third Preview)**

📌 Ví dụ gọi hàm C `strlen` từ Java:

```java
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

public class App {
    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        SymbolLookup stdlib = linker.defaultLookup();

        MethodHandle strlen = linker.downcallHandle(
            stdlib.find("strlen").get(),
            FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS)
        );

        try (Arena arena = Arena.openConfined()) {
            MemorySegment cString = arena.allocateUtf8String("Xin chào Java 22");
            long length = (long) strlen.invoke(cString);
            System.out.println("Độ dài chuỗi: " + length);
        }
    }
}
```

 Cho phép gọi **native code** dễ dàng, thay thế **JNI** phức tạp.

### **6\. Vector API (Seventh Incubator)**

📌 Ví dụ cộng 2 mảng số nguyên bằng Vector API:

```java
import jdk.incubator.vector.*;

public class App {
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4};
        int[] b = {5, 6, 7, 8};
        int[] c = new int[4];

        var species = IntVector.SPECIES_PREFERRED;
        var va = IntVector.fromArray(species, a, 0);
        var vb = IntVector.fromArray(species, b, 0);
        var vc = va.add(vb);
        vc.intoArray(c, 0);

        System.out.println(java.util.Arrays.toString(c)); // [6, 8, 10, 12]
    }
}
```

Tận dụng CPU SIMD → **tăng tốc xử lý dữ liệu lớn**.

### **7\. Cải Tiến Khác**

*   **Virtual Threads** tiếp tục được tối ưu → đơn giản hóa lập trình song song.
    
*   **Pattern Matching** cho `switch` ổn định hơn:
    
*   **Garbage Collection (GC)** nhanh hơn và tiêu tốn ít tài nguyên hơn.
    

```java
static String formatter(Object obj) {
    return switch (obj) {
        case Integer i -> "Số nguyên: " + i;
        case String s -> "Chuỗi: " + s;
        default -> "Khác";
    };
}
```

#### 📌 Kết Luận

Java 22 mang đến nhiều tính năng **đột phá và thực tế** như String Templates, Scoped Values, Structured Concurrency và Foreign Function API. Đây là bước đệm cho các bản **LTS tiếp theo**.
