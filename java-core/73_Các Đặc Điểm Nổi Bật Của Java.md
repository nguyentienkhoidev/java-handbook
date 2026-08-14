# Các Đặc Điểm Nổi Bật Của Java

**Java 19** (phát hành tháng 9/2022), tập trung vào các tính năng chính (JEP) theo nhóm **preview / incubator / ổn định** để bạn có thể dùng làm tài liệu giảng dạy hoặc nghiên cứu.

### **1\. Record Patterns (Preview – JEP 405)**

*   Mở rộng **Pattern Matching** cho phép giải nén (deconstruct) dữ liệu trong **Record** trực tiếp.
    
*   Giúp viết code ngắn gọn, dễ đọc khi làm việc với record.
    

📌 **Ví dụ trước đây:**

```java
record Point(int x, int y) {}

static void print(Object obj) {
    if (obj instanceof Point p) {
        int x = p.x();
        int y = p.y();
        System.out.println("x=" + x + ", y=" + y);
    }
}
```

📌 **Java 19 với Record Patterns:**

```java
record Point(int x, int y) {}

static void print(Object obj) {
    if (obj instanceof Point(int x, int y)) {
        System.out.println("x=" + x + ", y=" + y);
    }
}
```

👉 Ngắn gọn hơn, phù hợp với lập trình hàm.

### **2\. Pattern Matching for Switch (Second Preview – JEP 427)**

*   Tiếp tục cải tiến từ Java 17/18.
    
*   Cho phép `switch` hoạt động trực tiếp với kiểu dữ liệu phức tạp, thay vì chỉ primitive hoặc String.
    

📌 **Ví dụ:**

```java
static String sample(Object obj) {
    return switch (obj) {
        case String s -> "Chuỗi có độ dài " + s.length();
        case Integer i && i > 0 -> "Số nguyên dương " + i;
        case null -> "Null";
        default -> "Khác";
    };
}
```

👉 Linh hoạt, ngắn gọn, tránh nhiều `instanceof` lặp lại.

### **3\. Virtual Threads (Preview – JEP 425)**

*   Tính năng nổi bật nhất của Java 19 🎉.
    
*   **Virtual Thread**: một loại thread nhẹ do JVM quản lý (không gắn trực tiếp với OS thread).
    
*   Giúp xử lý **hàng triệu kết nối đồng thời** mà không tốn quá nhiều tài nguyên.
    

📌 **Ví dụ:**

```java
public class SampleVirtualThread {
    public static void main(String[] args) throws InterruptedException {
        Thread.startVirtualThread(() -> {
            System.out.println("Hello từ Virtual Thread!");
        }).join();
    }
}
```

📌 **Ứng dụng:**

*   Server web, microservices, xử lý đồng thời khối lượng lớn request (giống Go’s goroutine).
    
*   Hướng tới **Project Loom** (lập trình concurrent dễ dàng hơn).
    

### **4\. Structured Concurrency (Incubator – JEP 428)**

*   Cung cấp API giúp quản lý nhiều task chạy đồng thời dễ dàng hơn.
    
*   Giúp gom nhóm các thread ảo lại để quản lý lifecycle đồng bộ, dễ debug, dễ cancel.
    

📌 **Ví dụ:**

```java
import jdk.incubator.concurrent.*;

try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<String> user  = scope.fork(() -> findUser());
    Future<Integer> order = scope.fork(() -> fetchOrders());

    scope.join();           // chờ tất cả task
    scope.throwIfFailed();  // ném exception nếu có lỗi

    System.out.println(user.result() + " có " + order.result() + " đơn hàng.");
}
```

👉 Code xử lý song song trở nên **an toàn, dễ đọc, dễ bảo trì**.

### 5\. **Foreign Function & Memory API (Preview – JEP 424)**

*   Tiếp tục thay thế **JNI**.
    
*   Cung cấp API để gọi code native và truy cập bộ nhớ ngoài heap.
    
*   Ở Java 19: nâng cấp từ incubator → preview.
    

📌 **Ví dụ:**

```java
import java.lang.foreign.*;

public class FFMSample {
    public static void main(String[] args) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(100);
            segment.setUtf8String(0, "Xin chào Java 19!");
            System.out.println(segment.getUtf8String(0));
        }
    }
}
```

👉 Quan trọng cho **AI, ML, đồ họa, high-performance computing**.

### **6\. Vector API (Fourth Incubator – JEP 426)**

*   API cho xử lý SIMD (vectorized computation).
    
*   Cho phép thao tác mảng số học hiệu năng cao hơn vòng lặp thường.
    

📌 **Ví dụ:**

```java
import jdk.incubator.vector.*;

public class VectorSample {
    public static void main(String[] args) {
        var species = FloatVector.SPECIES_PREFERRED;
        float[] a = {1, 2, 3, 4};
        float[] b = {5, 6, 7, 8};
        float[] c = new float[4];

        var va = FloatVector.fromArray(species, a, 0);
        var vb = FloatVector.fromArray(species, b, 0);
        var vc = va.add(vb);
        vc.intoArray(c, 0);

        System.out.println(java.util.Arrays.toString(c)); // [6.0, 8.0, 10.0, 12.0]
    }
}
```

👉 Dùng nhiều trong **Big Data, AI/ML, xử lý ảnh, game engine**.

### **✅ Tóm tắt Java 19**

JEPTính năngTrạng tháiMô tả**405**Record PatternsPreviewGiải nén dữ liệu trực tiếp trong record**427**Pattern Matching for SwitchPreviewSwitch hỗ trợ pattern nâng cao**425**Virtual ThreadsPreviewThread nhẹ, hỗ trợ xử lý hàng triệu task đồng thời**428**Structured ConcurrencyIncubatorAPI quản lý nhóm task song song**424**Foreign Function & Memory APIPreviewGọi native code + quản lý bộ nhớ an toàn**426**Vector APIIncubatorTăng tốc tính toán SIMD
