# Optional

### **1\.** `Optional` **là gì?**

*   `Optional<T>` (Java 8) là **một container object** dùng để chứa giá trị **có thể tồn tại hoặc không** (nullable).
    
*   Thay vì trả về `null` và dễ gây ra `NullPointerException (NPE)`, ta có thể trả về `Optional.empty()` hoặc `Optional.of(value)`.
    

### **2\. Mục đích của** `Optional`

*   **Tránh NullPointerException:** Giúp lập trình viên xử lý trường hợp **giá trị null an toàn**.
    
*   **Viết code gọn gàng, rõ ràng:** Không cần nhiều `if (value != null)` rườm rà.
    
*   **Khuyến khích lập trình hàm:** Dùng với `map()`, `filter()`, `orElse()`...
    

### **3\. Cú pháp**

```java
// 1. Optional rỗng
Optional<String> empty = Optional.empty();

// 2. Optional với giá trị chắc chắn (không null)
Optional<String> name = Optional.of("FoxDev");

// 3. Optional có thể null
String text = null;
Optional<String> maybeText = Optional.ofNullable(text);
```

### **4\. Các phương thức quan trọng của Optional**

#MethodMô tả1`isPresent()`Trả về `true` nếu có giá trị.2`isEmpty()` (Java 11+)Trả về `true` nếu không có giá trị.3`get()`Lấy giá trị, nếu null sẽ `NoSuchElementException`.4`orElse(value)`Trả về giá trị nếu có, nếu không thì trả về `value`.5`orElseGet(Supplier)`Trả về giá trị nếu có, nếu không thì chạy `Supplier`.6`orElseThrow()`Trả về giá trị nếu có, nếu không thì ném exception.7`ifPresent(Consumer)`Nếu có giá trị thì thực hiện Consumer.8`filter(Predicate)`Lọc giá trị nếu thỏa điều kiện.9`map(Function)`Biến đổi giá trị bên trong.10`flatMap(Function)`Giống `map` nhưng tránh Optional lồng nhau.

📌 Ví dụ: Tránh `NullPointerException`

```java
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        String name = null;

        // Optional thay vì null-check
        Optional<String> optionalName = Optional.ofNullable(name);

        System.out.println(optionalName.orElse("No Name"));
    }
}
```

– Kết quả:

```plaintext
No Name
```

📌 Ví dụ: Dùng với `ifPresent`

```java
Optional<String> email = Optional.of("hello@nguyentienkhoi.hashnode.dev");

email.ifPresent(e -> System.out.println("Email: " + e));
```

📌 Ví dụ: Dùng `map()` để xử lý

```java
Optional<String> name = Optional.of("foxdev");

String upper = name.map(String::toUpperCase).orElse("UNKNOWN");
System.out.println(upper);
```

– Kết quả:

```java
FOXDEV
```

📌 Ví dụ: Dùng `orElseGet` để tính toán giá trị mặc định

```java
Optional<String> data = Optional.empty();

String result = data.orElseGet(() -> "Generated Value");
System.out.println(result);
```

## **5\. Ứng dụng thực tế**

*   Trả về từ repository (JPA/Hibernate) → tránh null khi tìm kiếm.
    
*   Tránh check null nhiều lần trong service.
    
*   Dùng trong API để trả về dữ liệu không chắc chắn.
    

