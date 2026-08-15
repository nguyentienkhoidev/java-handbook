# Các Đặc Điểm Nổi Bật Của Java 15

**Java 15** (phát hành tháng 9/2020, **non-LTS**) mang đến nhiều thay đổi quan trọng về **ngôn ngữ**, **bảo mật** và **GC**, đồng thời chuẩn hóa một số tính năng đã ở trạng thái Preview từ các phiên bản trước.

### **1\. JEP 378 – Text Blocks (Standard)**

Sau khi thử nghiệm ở Java 13 và Java 14, **Text Blocks (**`"""`**)** chính thức trở thành chuẩn trong Java 15.

Giúp làm việc với chuỗi nhiều dòng dễ dàng, đặc biệt với JSON, SQL, HTML.

```java
String html = """
              <html>
                  <body>
                      <h1>Hello Java 15</h1>
                  </body>
              </html>
              """;
System.out.println(html);
```

→ Kết thúc việc nối chuỗi phức tạp bằng `\n` hay `+`.

### **2\. JEP 360 – Sealed Classes (Preview)**

*   Giúp **giới hạn** tập hợp class có thể kế thừa một class hoặc implement một interface.
    
*   Tăng tính **an toàn**, **kiểm soát** trong thiết kế API.
    

```java
public sealed class Shape permits Circle, Rectangle {}

final class Circle extends Shape {}
final class Rectangle extends Shape {}
```

→ Không cho phép các class khác ngoài `Circle` và `Rectangle` kế thừa `Shape`.

### **3\. JEP 384 – Records (Second Preview)**

*   Records tiếp tục được cải tiến sau lần đầu ra mắt ở Java 14.
    
*   Hữu ích khi tạo các **data carrier class** (DTO).
    

```java
public record Person(String name, int age) {}

Person p = new Person("Alice", 30);
System.out.println(p.name());  // Alice
System.out.println(p);         // Person[name=Alice, age=30]
```

### **4\. JEP 375 – Pattern Matching cho** `instanceof` **(Second Preview)**

Tối ưu tiếp cú pháp `instanceof`.

```java
if (obj instanceof String s) {
    System.out.println(s.toLowerCase());
}
```

### **5\. JEP 339 – Edwards-Curve Digital Signature Algorithm (EdDSA)**

*   Thêm hỗ trợ EdDSA (Edwards-Curve Digital Signature Algorithm).
    
*   Thuật toán chữ ký số mới, nhanh hơn và an toàn hơn so với ECDSA/RSA.
    

### **6\. JEP 377 – ZGC trở thành Production**

*   Trước đây ZGC chỉ là **experimental**, nay chính thức thành **production**.
    
*   ZGC là **low-latency GC** (thời gian pause chỉ vài ms), hỗ trợ heap size từ **8MB đến 16TB**.
    

### **7\. JEP 371 – Hidden Classes**

*   Cho phép tạo ra **class ẩn**, chỉ dùng nội bộ bởi JVM hoặc frameworks.
    
*   Hữu ích cho các thư viện **dynamic proxy** hoặc **bytecode frameworks** như Spring, Hibernate.
    

```java
MethodHandles.Lookup lookup = MethodHandles.lookup();
Class<?> hidden = lookup.defineHiddenClass(
        bytecode, true).lookupClass();
```

### **8\. JEP 381 – Loại bỏ RMI Activation**

*   Module `java.rmi.activation` bị **xóa bỏ**.
    
*   RMI (Remote Method Invocation) vẫn còn, nhưng Activation system bị loại bỏ do ít được sử dụng.
    

### **9\. JEP 385 – Deprecate RMI**

*   Một số API RMI được đánh dấu **deprecated**.
    

### **10\. JEP 373 & 374 – Reimplement Legacy Datagrams/Socket API**

Thay thế code cũ trong **DatagramSocket** và **Socket API** bằng **modern implementation** dễ bảo trì hơn.

### **📌 Tóm Tắt Java 15**

#Tính năngMô tả1Text Blocks (Standard)Chuỗi nhiều dòng `"""` chính thức2Sealed Classes (Preview)Giới hạn class được phép kế thừa3Records (Preview 2)Class bất biến, tối ưu cho DTO4Pattern Matching (Preview 2)`instanceof` gọn hơn5EdDSAThuật toán chữ ký số mới, an toàn & nhanh6ZGC ProductionGarbage Collector low-latency chính thức7Hidden ClassesHỗ trợ frameworks runtime/proxy8Remove RMI ActivationXóa module RMI Activation9Deprecate RMI APIMột số phần RMI bị bỏ dần10Reimplement Socket APIsAPI Socket cũ được viết lại

