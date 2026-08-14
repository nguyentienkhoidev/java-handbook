# Các Đặc Điểm Nổi Bật Của Java

**Java 13** (phát hành tháng 9/2019) là bản **non-LTS**, tiếp tục cải tiến ngôn ngữ và JVM, chủ yếu mở rộng các tính năng thử nghiệm từ Java 12. 

Dưới đây là các **đặc điểm nổi bật của Java 13**:

### **1\. JEP 354 – Switch Expressions (Preview, lần 2)**

*   Hoàn thiện hơn so với Java 12.
    
*   Giới thiệu từ khóa `yield` thay cho `break` khi muốn trả về giá trị.
    

```java
public class App {
    public static void main(String[] args) {
        int dayNumber = 3;
        String day = switch (dayNumber) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> {
                yield "Wednesday";  // dùng yield
            }
            default -> "Unknown";
        };
        System.out.println(day);
    }
}
```

→ Gọn gàng, an toàn, tránh lỗi quên `break`.

### **2\. JEP 355 – Text Blocks (Preview)**

*   Thêm cú pháp **text block (**`"""`**)** để viết chuỗi nhiều dòng dễ đọc hơn.
    
*   Giúp viết **JSON, SQL, HTML** trực tiếp trong code Java mà không cần escape.
    

```java
String html = """
              <html>
                  <body>
                      <h1>Hello Java 13</h1>
                  </body>
              </html>
              """;
System.out.println(html);
```

→ Loại bỏ việc phải nối chuỗi `" + "` hoặc thêm `\n`.

### **3\. JEP 350 – Dynamic CDS Archives**

*   Cho phép **tạo CDS archive động** khi tắt JVM mà không cần chạy thêm lệnh riêng.
    
*   Giúp cải thiện **startup time** của ứng dụng Java.
    

### **4\. JEP 351 – ZGC: Uncommit Unused Memory**

*   Z Garbage Collector (ZGC) có thể **trả lại bộ nhớ không dùng cho hệ điều hành**.
    
*   Giúp ứng dụng có workload biến động tiết kiệm RAM.
    

### **5\. JEP 353 – Reimplement the Legacy Socket API**

*   Thư viện `java.net.Socket` và `ServerSocket` được **viết lại** bằng code hiện đại, dễ bảo trì hơn.
    
*   Không thay đổi API, nhưng tăng hiệu năng và khả năng mở rộng.
    

### **📌 Tóm Tắt Java 13**

#Tính năngMô tả ngắn1Switch Expressions (Preview 2)Hoàn thiện cú pháp `switch` với `yield`2Text Blocks (Preview)Chuỗi nhiều dòng với `"""`3Dynamic CDS ArchivesTự động tạo CDS archive khi JVM tắt4ZGC Uncommit MemoryTrả RAM không dùng cho OS5Legacy Socket API RewriteViết lại thư viện Socket tăng hiệu năng
