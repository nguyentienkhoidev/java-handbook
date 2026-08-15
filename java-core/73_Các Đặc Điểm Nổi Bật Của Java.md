# Các Đặc Điểm Nổi Bật Của Java 17

**Java 17** được phát hành vào tháng 9 năm 2021 là một phiên bản hỗ trợ dài hạn (LTS), nghĩa là nó sẽ nhận được hỗ trợ rộng rãi từ nhà phát triển và cộng đồng Java. Phiên bản này mang lại nhiều cải tiến và tính năng mới cho ngôn ngữ và nền tảng, giúp tăng năng suất, hiệu suất và bảo mật cho lập trình viên.  
 

Những tính năng và thay đổi trên giúp Java 17 trở nên mạnh mẽ hơn, dễ bảo trì và an toàn hơn. Nếu bạn đang làm việc với Spring Boot hoặc các framework khác thì các lớp sealed, pattern matching và các cải tiến về quản lý bộ nhớ có thể cải thiện đáng kể hiệu suất và cấu trúc mã của bạn.

  
 

### **1\. Lớp Sealed (JEP 409)**

*   **Mục đích:** Lớp `sealed` hạn chế những lớp nào có thể mở rộng hoặc triển khai chúng, hữu ích khi xác định một tập hợp lớp con được phép.
    
*   **Lợi ích:** Cung cấp nhiều kiểm soát hơn cho hệ thống phân cấp lớp, giúp mã dễ bảo trì hơn và an toàn hơn.
    

```java
public abstract sealed class Shape
    permits Circle, Square { }

public final class Circle extends Shape { }
public final class Square extends Shape { }
```

### **2\. Pattern Matching cho switch (Preview) (JEP 406)**

Mục đích: Nâng cao câu lệnh switch để hoạt động với các mẫu, cho phép so khớp mẫu trong các nhãn case.

Lợi ích: Đơn giản hóa việc sử dụng câu lệnh switch, làm cho nó mạnh mẽ và dễ đọc hơn.

```java
switch (object) {
    case String s -> System.out.println("Đây là chuỗi");
    case Integer i -> System.out.println("Đây là số nguyên");
    default -> System.out.println("Không rõ");
}
```

### **3\. Strong Encapsulation for JDK Internals (JEP 403)**

*   **Mục đích:** Đóng gói chặt chẽ hơn các thành phần nội bộ của JDK theo mặc định, cải thiện bảo mật và tính modular.
    
*   **Chi tiết:** Theo mặc định việc truy cập phản chiếu bất hợp pháp vào các API nội bộ sẽ bị cấm trừ khi được phép rõ ràng.
    

### **4\. Context-Specific Deserialization Filters (JEP 415)**

*   **Mục đích:** Cung cấp cơ chế để lọc dữ liệu phân giải đến nhằm ngăn chặn các lỗ hổng bảo mật do phân giải dữ liệu không an toàn.
    
*   **Chi tiết:** Bộ lọc cho phép chỉ định các loại đối tượng có thể được phân giải.
    

### **5\. Foreign Function & Memory API (Incubator) (JEP 412)**

*   **Mục đích:** Giới thiệu API để gọi các hàm ngoại lai (không phải Java) và truy cập bộ nhớ ngoại lai ngoài heap JVM. Tính năng thử nghiệm này đơn giản hóa việc làm việc với mã native.
    
*   **Lợi ích:** Cung cấp một cách an toàn và hiệu quả hơn để làm việc với bộ nhớ native và giao diện hàm ngoại lai (FFI).
    

### **6\. Vector API (Second Incubator) (JEP 414)**

*   **Mục đích:** Thử nghiệm lần hai của API Vector cho phép biểu diễn các tính toán vector và biên dịch ra các lệnh vector tối ưu trên phần cứng hỗ trợ (ví dụ: lệnh SIMD).
    
*   **Lợi ích:** Cải thiện hiệu suất của các tác vụ xử lý dữ liệu song song.
    

### **7\. Loại bỏ trình biên dịch AOT và JIT thử nghiệm (JEP 410)**

Loại bỏ các tính năng biên dịch Ahead-of-Time (AOT) và Just-in-Time (JIT),vốn là các tính năng thử nghiệm và không được sử dụng rộng rãi.

### **8\. Ngưng sử dụng API Applet để loại bỏ (JEP 398)**

API Applet đã bị ngưng sử dụng và sẽ bị loại bỏ trong tương lai do các applet là công nghệ lỗi thời trong phát triển web hiện đại.

### **9\. Cải tiến Bộ tạo Số Ngẫu nhiên Pseudo (JEP 356)**

*   **Mục đích:** Thêm các giao diện và triển khai mới cho các bộ tạo số ngẫu nhiên (RNG), bao gồm các phương pháp dựa trên luồng và hỗ trợ các bộ tạo nhảy.
    
*   **Lợi ích:** Các tùy chọn RNG linh hoạt và có khả năng mở rộng hơn cho các trường hợp sử dụng nâng cao.
    

```java
RandomGenerator generator = RandomGeneratorFactory.of("L64X128MixRandom").create();
```

### **10\. Ngưng sử dụng Finalization (JEP 421)**

*   **Mục đích:** Finalization là một cơ chế dễ gặp lỗi khi dọn dẹp tài nguyên và việc sử dụng nó đang bị ngưng để ưu tiên các phương pháp đáng tin cậy hơn như `try-with-resources`.
    
*   **Chi tiết:** Khuyến khích chuyển từ việc sử dụng `finalize()` sang các phương án thay thế như `Cleaner`.
    

### **11\. Hỗ trợ MacOS/AArch64 (JEP 391)**

Giới thiệu hỗ trợ cho kiến trúc Apple Silicon (AArch64) trên macOS, đảm bảo Java có thể chạy hiệu quả trên chip M1 của Apple.

### **12\. Các Cải Tiến Nhỏ Khác**

*   **Khối văn bản (từ Java 13 và 14):** Java 17 củng cố hỗ trợ cho các khối văn bản (chuỗi nhiều dòng), giúp cải thiện khả năng đọc của các văn bản định dạng như JSON hoặc SQL.
    
*   **Records:** Java 17 cải thiện thêm tính năng record, được giới thiệu trong Java 16, giúp tạo các lớp dữ liệu bất biến với ít mã thừa hơn.
    

```java
String json = """
              {
                "name": "Fox Dev",
                "age": 18
              }
              """;
```

