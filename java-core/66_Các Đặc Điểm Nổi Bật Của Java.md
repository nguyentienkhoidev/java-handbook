# Các Đặc Điểm Nổi Bật Của Java 10

**Java 10** (phát hành tháng 3/2018) là một **phiên bản ngắn hạn (non-LTS)**, nhưng vẫn mang đến nhiều cải tiến đáng chú ý cho lập trình viên. 

Dưới đây là các **đặc điểm nổi bật của Java 10**:

### **1\. Local-Variable Type Inference (**`var`**)**

Cho phép khai báo biến với từ khóa `var`, trình biên dịch tự suy luận kiểu dựa trên giá trị gán.

Giúp code ngắn gọn và dễ đọc hơn.

```java
public class App {
    public static void main(String[] args) {
        var message = "Hello Java 10";   // kiểu String
        var number  = 100;               // kiểu int
        var list    = List.of("Java", "Python", "C++"); // kiểu List<String>

        list.forEach(System.out::println);
    }
}
```

### **2\. Unmodifiable Collections Enhancements**

Java 10 mở rộng API cho `List.copyOf()`, `Set.copyOf()`, `Map.copyOf()` để tạo **collection bất biến** từ một collection khác.

```java
List<String> oldList = new ArrayList<>(List.of("Java", "Kotlin"));
List<String> newList = List.copyOf(oldList);  // bất biến
```

### **3\. Application Class-Data Sharing (AppCDS)**

*   Giúp **chia sẻ dữ liệu class giữa nhiều JVM** để giảm thời gian khởi động và sử dụng bộ nhớ hiệu quả hơn.
    
*   Không cần giấy phép thương mại (OpenJDK hỗ trợ miễn phí).
    

### **4\. Parallel Full GC for G1**

*   Java 9 giới thiệu **G1 Garbage Collector** nhưng Full GC còn chạy **single-threaded**.
    
*   Java 10 cải tiến, cho phép Full GC chạy **song song (parallel)**, tăng tốc độ dọn rác.
    

### **5\. Root Certificates**

Java 10 tích hợp sẵn **root CA certificates** trong JDK, giúp cải thiện bảo mật và loại bỏ sự phụ thuộc vào JCE.

### **6\. Thread-Local Handshakes**

*   Cho phép dừng một thread riêng lẻ thay vì toàn bộ JVM.
    
*   Tăng hiệu suất trong việc quản lý thread.
    

### **7\. Experimental Java-Based JIT Compiler (Graal)**

*   Graal được tích hợp như một **JIT compiler** thử nghiệm.
    
*   Viết bằng Java, hứa hẹn thay thế **C2 compiler** trong tương lai.
    

### **8\. Heap Allocation on Alternative Memory Devices**

*   Cho phép JVM sử dụng bộ nhớ ngoài RAM chuẩn (ví dụ: **NVDIMM**) để quản lý heap.
    
*   Hữu ích cho hệ thống lớn, cần xử lý dữ liệu trong bộ nhớ tốc độ cao.
    

### **9\. Improved Container Awareness**

*   JVM nhận biết tốt hơn khi chạy trong **Docker và container**.
    
*   Tự động tối ưu CPU và RAM theo giới hạn container, tránh chiếm toàn bộ tài nguyên host.
    

### **10\. Miscellaneous Changes**

*   API `Optional.orElseThrow()` mặc định ném `NoSuchElementException`.
    
*   Các cải tiến nhỏ trong `var` cho lambda và try-with-resources.
    

### 📌 Tóm Tắt Java 10

#Tính năngMô tả ngắn1`var`Suy luận kiểu biến cục bộ2`copyOf()`Tạo collection bất biến3AppCDSGiảm startup time, tiết kiệm bộ nhớ4Parallel Full GCTối ưu G1 GC với Full GC song song5Root CertificatesTích hợp chứng chỉ CA gốc6Thread-Local HandshakesDừng thread riêng lẻ thay vì toàn bộ JVM7Graal JITTrình biên dịch JIT bằng Java8Heap Allocation on NVMDùng bộ nhớ ngoài (NVDIMM) cho heap9Container AwarenessJVM tối ưu khi chạy trong Docker10API cải tiến`Optional.orElseThrow()`, try-with-resources

