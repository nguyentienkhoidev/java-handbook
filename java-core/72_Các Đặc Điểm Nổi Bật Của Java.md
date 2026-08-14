# Các Đặc Điểm Nổi Bật Của Java

**Java 18** (phát hành tháng 3/2022) **không phải bản LTS** (Long-Term Support) như Java 17, nhưng nó mang đến nhiều tính năng thử nghiệm (preview/incubator) và cải tiến đáng chú ý.

Dưới đây là các đặc điểm nổi bật:

### **1\. UTF-8 là Charset Mặc Định (JEP 400)**

*   Trước Java 18: charset mặc định phụ thuộc hệ điều hành (Windows: `Cp1252`, Linux/macOS: `UTF-8`).
    
*   Từ Java 18: **UTF-8 mặc định trên mọi nền tảng**.
    

📌 **Lợi ích:**

*   Viết code xử lý chuỗi nhất quán trên mọi hệ điều hành.
    
*   Giảm lỗi khi đọc/ghi file text chứa ký tự đặc biệt.
    

📌 **Ví dụ:**

```java
import java.nio.file.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Utf8Example {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("hello.txt");
        Files.writeString(path, "Xin chào Tây Java!");
        String content = Files.readString(path);
        System.out.println(content);
    }
}
```

👉 Java 18 mặc định sử dụng UTF-8, không cần khai báo thủ công.

### **2\. Simple Web Server (JEP 408)**

*   Cung cấp **HTTP file server nhẹ** tích hợp sẵn trong JDK.
    
*   Phục vụ tĩnh (static files), hữu ích cho học tập, thử nghiệm REST API hoặc SPA.
    

📌 **Cách chạy:**

```java
jwebserver
```

Mặc định: cổng `8000`, root là thư mục hiện tại.

Có thể tùy chỉnh:

```java
jwebserver -p 9000 -d /home/user/site
```

📌 **Ví dụ truy cập:**

```html
http://localhost:8000/index.html
```

👉 Thay vì cài Apache/Nginx, ta có server tích hợp trong JDK.

### **3\. Code Snippets trong Javadoc (JEP 413)**

*   Bổ sung thẻ `@snippet` trong Javadoc.
    
*   Hỗ trợ **highlight, đánh số dòng, chỉ ra lỗi** trong code.
    

📌 **Ví dụ Javadoc:**

```java
/**
 * Tính tổng hai số nguyên.
 *
 * @snippet :
 * int sum = 3 + 4;
 * System.out.println(sum); // 7
 */
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

👉 Javadoc trở nên trực quan và chuyên nghiệp hơn.

### **4\. Pattern Matching cho Switch (Second Preview – JEP 420)**

Mở rộng `switch` để dùng **pattern matching** thay vì `instanceof` + ép kiểu thủ công.

📌 **Ví dụ trước Java 18:**

```java
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println("Length: " + s.length());
}
```

📌 **Với Java 18 (switch + pattern matching):**

```java
static String format(Object obj) {
    return switch (obj) {
        case Integer i -> "Số nguyên: " + i;
        case String s  -> "Chuỗi: " + s.toUpperCase();
        case null      -> "null";
        default        -> "Khác";
    };
}
```

👉 Code ngắn gọn, an toàn hơn.

### **5\. Vector API (Third Incubator – JEP 417)**

*   API cho phép **tận dụng SIMD (Single Instruction Multiple Data)**.
    
*   Xử lý mảng số học nhanh hơn nhiều so với vòng lặp for truyền thống.
    

📌 **Ví dụ:**

```java
import jdk.incubator.vector.*;

public class VectorExample {
    public static void main(String[] args) {
        var species = IntVector.SPECIES_PREFERRED;
        int[] a = {1,2,3,4};
        int[] b = {5,6,7,8};
        int[] c = new int[4];

        var va = IntVector.fromArray(species, a, 0);
        var vb = IntVector.fromArray(species, b, 0);
        var vc = va.add(vb);
        vc.intoArray(c, 0);

        System.out.println(java.util.Arrays.toString(c)); // [6, 8, 10, 12]
    }
}
```

👉 Dùng trong AI/ML, game, xử lý ảnh, big data.

## 6\. **Foreign Function & Memory API (Second Incubator – JEP 419)**

*   Thay thế JNI (Java Native Interface).
    
*   Cho phép:
    
*   Gọi hàm native (C/C++).
    
*   Truy cập bộ nhớ ngoài Java heap an toàn.
    

📌 **Ví dụ:**

```java
import jdk.incubator.foreign.*;

public class ForeignExample {
    public static void main(String[] args) {
        try (var arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(64);
            segment.setUtf8String(0, "Hello từ Java 18!");
            System.out.println(segment.getUtf8String(0));
        }
    }
}
```

👉 Hữu ích trong lập trình hệ thống, high-performance computing.

### **7\. Deprecations & Removals**

*   Một số API cũ bị loại bỏ/dừng hỗ trợ:
    
*   `SecurityManager` (đang deprecated, chuẩn bị loại bỏ).
    
*   Một số cờ JVM cũ bị bỏ.
    

## ✅ Tóm tắt Java 18

JEPTính năngMô tả**400**UTF-8 Default CharsetĐảm bảo encoding nhất quán trên mọi OS**408**Simple Web ServerServer HTTP nhẹ tích hợp sẵn**413**Javadoc SnippetsTài liệu có code minh họa trực quan**420**Switch Pattern MatchingSwitch hỗ trợ pattern, ngắn gọn hơn**417**Vector API (Incubator)Xử lý SIMD hiệu năng cao**419**Foreign Function & Memory API (Incubator)Gọi C/C++ an toàn, quản lý memory ngoài heap
