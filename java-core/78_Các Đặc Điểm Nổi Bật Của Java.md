# Các Đặc Điểm Nổi Bật Của Java

Ngày 18/03/2025, **Java 24** chính thức ra mắt. Đây **không phải bản LTS** (Long-Term Support), nhưng mang đến nhiều cải tiến quan trọng về hiệu suất, bảo mật và ngôn ngữ. Bản phát hành này gồm **24 JEPs** (JDK Enhancement Proposals), trong đó có tính năng permanent, preview, incubator và experimental.

### **1\. Stream Gatherers (JEP 485)**

Java 24 mở rộng `Stream` API với **Gatherers**, cho phép định nghĩa các thao tác trung gian tùy chỉnh.

📌 Ví dụ: **Tạo gatherer nhóm phần tử theo cặp**

```java
import java.util.stream.*;
import java.util.stream.Gatherers;

public class App {
    public static void main(String[] args) {
        var result = Stream.of(1, 2, 3, 4, 5, 6)
                .gather(Gatherers.windowFixed(2)) // gom thành cửa sổ 2 phần tử
                .toList();

        System.out.println(result); // [[1, 2], [3, 4], [5, 6]]
    }
}
```

### **2\. Scoped Values (JEP 487 – Preview)**

Cho phép chia sẻ dữ liệu **bất biến** giữa các thread mà không cần `ThreadLocal`. Giúp code an toàn hơn, tránh lỗi rò rỉ dữ liệu khi dùng nhiều **thread** hoặc **virtual thread.**

📌 Ví dụ:

```java
import jdk.incubator.concurrent.ScopedValue;

public class App {
    static final ScopedValue<String> USER = ScopedValue.newInstance();

    public static void main(String[] args) {
        ScopedValue.where(USER, "TayJava")
                   .run(() -> {
                       System.out.println("Hello " + USER.get());
                   });
    }
}
```

### **3\. Flexible Constructor Bodies (JEP 492 – Preview)**

Constructor giờ có thể viết logic trước khi gọi `super(...)` hoặc `this(...)`. Giúp constructor linh hoạt, code rõ ràng hơn.

📌 Ví dụ:

```java
class User {
    String name;
    int age;

    User(String name, int age) {
        System.out.println("Chuẩn bị khởi tạo user...");
        this.name = name.trim(); // xử lý logic trước
        this.age = Math.max(age, 0);
    }
}
```

```java
public class App {
    public static void main(String[] args) {
        User u = new User("  Tây  ", -5);
        System.out.println(u.name + " - " + u.age); // Tây - 0
    }
}
```

### **4\. Primitive Patterns (JEP 488 – Preview)**

Cho phép pattern matching với **kiểu nguyên thủy** trong `switch` và `instanceof`. → Không cần boxing/unboxing, code ngắn gọn hơn.

📌 Ví dụ:

```java
public class App {
    
    static void main(String[] args) {
        System.out.println(check(42));     // Integer: 42
        System.out.println(check(3.14));   // Double: 3.14
    }

    public static String check(Object obj) {
        return switch (obj) {
            case int i -> "Integer: " + i;
            case double d -> "Double: " + d;
            default -> "Khác";
        };
    }
}
```

### **5\. Simple Source Files & Instance Main (JEP 495 – Preview)**

Giờ bạn có thể viết chương trình Java **mà không cần class bao ngoài**. Rất hữu ích cho học tập, demo nhanh, hoặc script nhỏ.

📌 Ví dụ: File `HelloWorld.java`

```java
void main() {
    System.out.println("Xin chào Java 24!");
}
```

– Chạy trực tiếp:

```java
java HelloWorld.java
```

### **6\. Key Derivation Function API (JEP 478 – Preview)**

Java 24 giới thiệu API chuẩn để xử lý **Key Derivation Function** (PBKDF2, Argon2). Giúp tăng cường bảo mật, dễ dàng sử dụng API thay vì tự implement.

📌 Ví dụ:

```java
import java.security.*;
import java.security.spec.*;

public class App {
    public static void main(String[] args) throws Exception {
        var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        var spec = new PBEKeySpec("mypassword".toCharArray(), "mysalt".getBytes(), 65536, 256);
        var key = factory.generateSecret(spec).getEncoded();

        System.out.println("Derived key length: " + key.length);
    }
}
```

### **7\. Vector API (JEP 489 – Incubator)**

Tiếp tục mở rộng, hỗ trợ SIMD cho tính toán hiệu năng cao. Giúp tăng tốc xử lý dữ liệu, ML, hình ảnh, khoa học.

📌 Ví dụ: Tính tổng vector

```java
import jdk.incubator.vector.*;

public class App {
    public static void main(String[] args) {
        var species = IntVector.SPECIES_PREFERRED;
        int[] a = {1, 2, 3, 4};
        int[] b = {10, 20, 30, 40};
        int[] result = new int[4];

        var va = IntVector.fromArray(species, a, 0);
        var vb = IntVector.fromArray(species, b, 0);
        var vc = va.add(vb);
        vc.intoArray(result, 0);

        System.out.println(java.util.Arrays.toString(result)); // [11, 22, 33, 44]
    }
}
```

### **8\. Quantum-Resistant Cryptography (JEP 496 & 497)**

Hỗ trợ thuật toán mã hóa khóa và chữ ký số chống lại tấn công **máy tính lượng tử**.  
👉 Đây là bước quan trọng cho **cryptography tương lai** (ML-KEM, ML-DSA).

### **9\. Compact Object Headers (JEP 450 – Experimental)**

Giảm kích thước header của object → tiết kiệm bộ nhớ heap, cải thiện hiệu năng.  
👉 Đặc biệt hữu ích cho ứng dụng chạy nhiều đối tượng nhỏ.

### **10\. Các cải tiến khác**

*   **AOT Class Loading (JEP 483):** Tăng tốc khởi động JVM.
    
*   **Loại bỏ Security Manager (JEP 486):** Dọn sạch API lỗi thời.
    
*   **ZGC chỉ còn generational mode (JEP 490).**
    
*   **Loại bỏ Windows 32-bit (JEP 479).**
    

#### 🎯 Kết luận

**Java 24** mang đến nhiều cải tiến từ hiệu năng, bảo mật, đến cú pháp ngôn ngữ:

*   Stream Gatherers & Vector API giúp xử lý dữ liệu mạnh mẽ.
    
*   Scoped Values, Flexible Constructor, Primitive Patterns làm code dễ viết hơn.
    
*   Bảo mật nâng cao với Key Derivation API & Quantum-Resistant Crypto.
    
*   JVM tối ưu hơn với Compact Object Headers, AOT Loading, GC improvements.
