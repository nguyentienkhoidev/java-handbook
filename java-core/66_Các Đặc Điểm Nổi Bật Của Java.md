# Các Đặc Điểm Nổi Bật Của Java

**Java 12** (phát hành tháng 3/2019) là bản phát hành **non-LTS**, nhưng mang nhiều tính năng mới cho developer và cải tiến hiệu năng JVM. 

Đây là các **đặc điểm nổi bật của Java 12**:

### **1\. JEP 325 – Switch Expressions (Preview)**

*   Nâng cấp cú pháp `switch` thành **expression**, giúp code gọn hơn.
    
*   Có thể dùng `yield` để trả về giá trị.
    

```java
public class App {
    public static void main(String[] args) {
        String day = "MONDAY";
        int result = switch (day) {
            case "MONDAY", "FRIDAY", "SUNDAY" -> 6;
            case "TUESDAY" -> 7;
            case "THURSDAY", "SATURDAY" -> 8;
            case "WEDNESDAY" -> 9;
            default -> throw new IllegalStateException("Invalid day: " + day);
        };
        System.out.println(result);
    }
}
```

### **2\. JEP 189 – Shenandoah GC (Experimental)**

*   Một **low-pause GC** từ RedHat, giảm đáng kể thời gian pause khi thu gom rác.
    
*   Phù hợp cho ứng dụng thời gian thực (real-time systems).
    

### **3\. JEP 344 – Abortable Mixed Collections for G1 GC**

*   G1 GC có thể **dừng sớm** quá trình Mixed Collection nếu vượt quá thời gian cho phép.
    
*   Giúp giảm độ trễ (latency).
    

### **4\. JEP 346 – Promptly Return Unused Committed Memory from G1**

*   G1 GC có thể **trả lại bộ nhớ không dùng** cho hệ điều hành nhanh hơn.
    
*   Hữu ích khi ứng dụng có workload thay đổi liên tục.
    

### **5\. JEP 230 – Microbenchmark Suite**

*   Bộ framework microbenchmark tích hợp trong JDK (dựa trên JMH).
    
*   Giúp developer dễ dàng viết benchmark kiểm tra hiệu năng.
    

### **6\. JEP 334 – JVM Constants API**

*   API mới để xử lý **constant pool** của JVM.
    
*   Hữu ích cho công cụ phân tích `bytecode`, IDE, và frameworks.
    

### **7\. JEP 305 – Pattern Matching for** `instanceof` **(Preview groundwork)**

*   Đặt nền móng cho **pattern matching**.
    
*   Hỗ trợ viết code gọn hơn khi dùng `instanceof`. (Tính năng đầy đủ xuất hiện ở Java 14+).
    

### **8\. JEP 341 – Default CDS Archives**

*   JDK phát hành kèm với **default class-data sharing (CDS) archives**, giảm thời gian startup JVM.
    

### **📌 Tóm Tắt Java 12**

#Tính năngMô tả ngắn1Switch Expressions (Preview)`switch` trả về giá trị, code ngắn gọn2Shenandoah GCGC low-pause, giảm latency3G1 Abortable Mixed CollectionsDừng Mixed GC sớm nếu tốn nhiều thời gian4G1 Return Unused MemoryTrả bộ nhớ rảnh về OS nhanh hơn5Microbenchmark SuiteBộ công cụ benchmark tích hợp JDK6JVM Constants APIAPI xử lý constant pool7Pattern Matching groundworkBước đầu cho pattern matching `instanceof`8Default CDS ArchivesGiảm startup time JVM với CDS mặc định
