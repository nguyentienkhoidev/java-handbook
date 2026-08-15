# Các Đặc Điểm Nổi Bật Của Java 20

**Java 20** (phát hành tháng 3/2023). Đây cũng là bản phát hành **non-LTS**, tập trung cải tiến các tính năng **Preview/Incubator** đã giới thiệu ở Java 19, đặc biệt là nhóm **Project Loom** (Virtual Threads, Structured Concurrency) và **Pattern Matching**.

### **1\. Record Patterns (Second Preview – JEP 432)**

Tiếp nối JEP 405 (Java 19). Cho phép **kết hợp record pattern với pattern khác** trong cùng biểu thức, tăng tính biểu đạt.

📌 **Ví dụ:**

```java
record Point(int x, int y) {}

static void printShape(Object obj) {
    if (obj instanceof Point(int x, int y) && x == y) {
        System.out.println("Hình vuông cạnh: " + x);
    }
}
```

👉 Giúp code dễ đọc hơn khi làm việc với record phức tạp.

### **2\. Pattern Matching for Switch (Fourth Preview – JEP 433)**

Bổ sung thêm rule an toàn & ngữ nghĩa mới. → Giúp `switch` xử lý tốt hơn với `null`, sealed class, và guard patterns (`when`).

📌 **Ví dụ:**

```java
static String format(Object obj) {
    return switch (obj) {
        case String s when s.length() > 5 -> "Chuỗi dài";
        case String s -> "Chuỗi ngắn";
        case null -> "Null value";
        default -> "Khác";
    };
}
```

👉 Viết code điều kiện gọn hơn, tránh nhiều `if-else`.

### **3\. Virtual Threads (Second Preview – JEP 436)**

Nâng cấp Virtual Threads từ Java 19 → Ổn định API, chuẩn bị hướng tới **tính năng chính thức trong bản LTS**.

📌 **Ví dụ:**

```java
public class VirtualThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t = Thread.startVirtualThread(() ->
            System.out.println("Hello từ Virtual Thread!")
        );
        t.join();
    }
}
```

👉 Giúp chạy **hàng triệu kết nối song song** mà không cần hàng triệu OS threads.

## 4\. **Structured Concurrency (Second Incubator – JEP 437)**

Tiếp tục cải tiến JEP 428 (Java 19) → Cho phép gom nhóm task song song, dễ cancel, dễ propagate lỗi.

📌 **Ví dụ:**

```java
import jdk.incubator.concurrent.*;

try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<String> user  = scope.fork(() -> getUser());
    Future<Integer> order = scope.fork(() -> getOrders());

    scope.join();           // chờ tất cả task hoàn thành
    scope.throwIfFailed();  // ném exception nếu có lỗi

    System.out.println(user.result() + " có " + order.result() + " đơn hàng.");
}
```

👉 Giúp **quản lý concurrency dễ như lập trình tuần tự**.

### **5\. Scoped Values (Incubator – JEP 429)**

Cung cấp cách **chia sẻ dữ liệu bất biến** giữa các thread, đặc biệt hữu ích cho Virtual Threads → Là giải pháp thay thế `ThreadLocal`, nhưng **nhanh hơn, an toàn hơn**.

📌 **Ví dụ:**

```java
import jdk.incubator.concurrent.ScopedValue;

public class ScopedValueDemo {
    static final ScopedValue<String> USER = ScopedValue.newInstance();

    public static void main(String[] args) {
        ScopedValue.where(USER, "Fox Dev").run(() -> {
            System.out.println("Xin chào " + USER.get());
        });
    }
}
```

👉 Giúp quản lý dữ liệu gắn liền với luồng trong môi trường nhiều virtual threads.

### **6\. Foreign Function & Memory API (Second Preview – JEP 434)**

Tiếp tục cải tiến từ Java 19 (JEP 424) → Hỗ trợ tốt hơn cho việc gọi native code (C/C++) và quản lý memory ngoài heap.

📌 **Ví dụ:**

```java
import java.lang.foreign.*;

public class FFMExample {
    public static void main(String[] args) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment seg = arena.allocate(50);
            seg.setUtf8String(0, "Java 20 FFM API");
            System.out.println(seg.getUtf8String(0));
        }
    }
}
```

👉 Quan trọng cho **AI/ML, hệ thống hiệu năng cao**.

### **✅ Tóm tắt Java 20**

JEPTính năngTrạng tháiMô tả**432**Record PatternsPreviewGiải nén record, kết hợp pattern**433**Pattern Matching for SwitchPreviewSwitch hỗ trợ guard pattern, null**436**Virtual ThreadsPreviewThread nhẹ, scale hàng triệu kết nối**437**Structured ConcurrencyIncubatorGom nhóm task song song, quản lý dễ hơn**429**Scoped ValuesIncubatorChia sẻ dữ liệu bất biến giữa threads**434**Foreign Function & Memory APIPreviewGọi native code, quản lý bộ nhớ an toàn

