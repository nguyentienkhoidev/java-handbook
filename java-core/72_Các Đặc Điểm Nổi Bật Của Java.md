# Các Đặc Điểm Nổi Bật Của Java 16

**Java 16** (phát hành tháng 3/2021, **non-LTS**) tiếp tục hoàn thiện các tính năng từ Java 14–15, đồng thời mang đến nhiều cải tiến quan trọng cho **ngôn ngữ**, **JVM**, và **API**. 

Đây là bản phát hành chuẩn hóa một số tính năng vốn đã ở trạng thái _Preview_ trước đó.

### **1\. JEP 394 – Pattern Matching for** `instanceof` **(Standard)**

Sau 2 lần Preview (Java 14, 15), tính năng **Pattern Matching cho** `instanceof` chính thức thành chuẩn.

```java
Object obj = "Hello Java 16";

if (obj instanceof String s) {
    System.out.println(s.toUpperCase()); // HELLO JAVA 16
}
```

→ Giúp code ngắn gọn, giảm ép kiểu thủ công.

### **2\. JEP 395 – Records (Standard)**

Sau 2 lần Preview (Java 14, 15), **Records** chính thức trở thành chuẩn trong Java 16.

Dùng để định nghĩa **data carrier class bất biến**.

```java
public record Person(String name, int age) {}
```

```java
public class App {
    static void main() {
        Person p = new Person("Alice", 25);
        System.out.println(p.name()); // Alice
        System.out.println(p);        // Person[name=Alice, age=25] 
    }
}
```

→ Tự động có `constructor`, `equals()`, `hashCode()`, `toString()`.

### **3\. JEP 376 – ZGC: Concurrent Thread-Stack Processing**

*   Cải tiến **Z Garbage Collector** với khả năng xử lý **thread stack song song**.
    
*   Giúp **giảm độ trễ (latency)** hơn nữa.
    

### **4\. JEP 387 – Elastic Metaspace**

*   JVM có thể **trả lại vùng Metaspace không dùng** về hệ điều hành.
    
*   Tối ưu việc quản lý bộ nhớ, giảm dung lượng sử dụng.
    

### **5\. JEP 338 – Vector API (Incubator)**

*   API mới hỗ trợ **tính toán vector hóa** (SIMD) để xử lý dữ liệu số nhanh hơn.
    
*   Hữu ích cho **machine learning, xử lý ảnh, big data**.
    

```java
import jdk.incubator.vector.*;

public class App {
    public static void main(String[] args) {
        float[] arrA = {1, 2, 3, 4, 5, 6, 7, 8};
        float[] arrB = {2, 2, 2, 2, 2, 2, 2, 2};
        float[] result = new float[8];

        FloatVector a = FloatVector.fromArray(FloatVector.SPECIES_256, arrA, 0);
        FloatVector b = FloatVector.fromArray(FloatVector.SPECIES_256, arrB, 0);
        FloatVector c = a.mul(b);

        c.intoArray(result, 0);

        for (float f : result) {
            System.out.print(f + " ");
        }
    }
}
```

– Kết quả:

```plaintext
2.0 4.0 6.0 8.0 10.0 12.0 14.0 16.0
```

### **6\. JEP 390 – Warnings for Value-Based Classes**

*   Một số class giá trị (`value-based classes` như `Optional`, `LocalDateTime`) sẽ cảnh báo nếu bị sử dụng sai (ví dụ: đồng bộ hóa `synchronized` trên chúng).
    

### **7\. JEP 392 – Packaging Tool (**`jpackage`**)**

Công cụ `jpackage` (xuất hiện ở Java 14 dạng incubator) chính thức thành chuẩn.

Cho phép **đóng gói ứng dụng Java thành installer gốc**:

*   Windows: `.msi`
    
*   macOS: `.pkg`
    
*   Linux: `.deb` / `.rpm`
    

### **8\. JEP 393 – Foreign-Memory Access API (Third Incubator)**

*   Tiếp tục thử nghiệm API cho phép **truy cập bộ nhớ ngoài Java heap** một cách an toàn và hiệu quả.
    
*   Đây là một phần trong **Project Panama**.
    

### **9\. JEP 394 – Unix-Domain Socket Channels**

*   Bổ sung API cho **Unix-Domain Socket**, thay vì chỉ hỗ trợ TCP/IP.
    
*   Hữu ích cho **giao tiếp giữa các process trên cùng hệ thống** (IPC).
    

### **10\. JEP 347 – C++14 Language Features cho JDK Internals**

*   Bên trong mã nguồn JDK, compiler được nâng cấp để hỗ trợ **C++14** (thay vì C++98 cũ).
    
*   Không ảnh hưởng trực tiếp đến lập trình viên Java, nhưng giúp phát triển JDK dễ hơn.
    

### 📌 Tóm Tắt Java 16

#Tính năngMô tả1Pattern Matching `instanceof`Chính thức, gọn hơn khi ép kiểu2RecordsClass bất biến chuẩn hóa3ZGC cải tiếnXử lý thread stack song song4Elastic MetaspaceJVM trả lại bộ nhớ không dùng5Vector API (Incubator)Tính toán SIMD cho hiệu suất cao6Warnings for Value-Based ClassesCảnh báo dùng sai Optional, DateTime…7Packaging Tool (`jpackage`)Đóng gói app thành installer gốc8Foreign-Memory Access APIQuản lý bộ nhớ ngoài heap9Unix-Domain Socket ChannelsIPC nhanh hơn qua Unix socket10C++14 cho JDKJDK nội bộ nâng cấp compiler

