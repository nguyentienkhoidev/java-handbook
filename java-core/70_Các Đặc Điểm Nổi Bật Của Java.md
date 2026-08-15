# Các Đặc Điểm Nổi Bật Của Java 14

**Java 14** (phát hành 3/2020) tiếp tục là một bản **non-LTS**, nhưng mang đến nhiều tính năng quan trọng, đặt nền móng cho các thay đổi lớn trong ngôn ngữ Java. 

Dưới đây là các **đặc điểm nổi bật của Java 14**:

### **1\. JEP 305 – Pattern Matching cho** `instanceof` **(Preview)**

Giúp code gọn hơn khi kiểm tra và ép kiểu với `instanceof`.

**– Trước đây:**

```java
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.toLowerCase());
}
```

**– Java 14:**

```java
if (obj instanceof String s) {
    System.out.println(s.toLowerCase());
}
```

→ Giảm boilerplate code, rõ ràng hơn.

### **2\. JEP 359 – Records (Preview)**

*   Cung cấp cú pháp mới để định nghĩa **class bất biến (immutable data carrier)**.
    
*   Tự động sinh constructor, getters, `equals()`, `hashCode()`, `toString()`.
    

```java
public record Person(String name, int age) {}

public class RecordExample {
    public static void main(String[] args) {
        Person p = new Person("Alice", 25);
        System.out.println(p.name()); // Alice
        System.out.println(p);       // Person[name=Alice, age=25]
    }
}
```

→ Giúp mô hình hóa **data transfer objects (DTOs)** dễ dàng hơn.

### **3\. JEP 358 – Helpful NullPointerExceptions**

Khi gặp `NullPointerException`, JVM cung cấp **thông tin chi tiết hơn** thay vì chỉ báo dòng lỗi.

📌 Ví dụ:

```java
String str = null;
System.out.println(str.toUpperCase());
```

– Kết quả:

```plaintext
Exception: Cannot invoke "String.toUpperCase()" because "str" is null
```

→ Dễ debug hơn rất nhiều.

### **4\. JEP 368 – Text Blocks (Preview 2)**

*   Cải tiến thêm cho **Text Blocks (**`"""`**)** đã giới thiệu ở Java 13.
    
*   Hỗ trợ tốt hơn cho xử lý chuỗi nhiều dòng.
    

```java
String json = """
              {
                  "name": "Alice",
                  "age": 25
              }
              """;
```

### **5\. JEP 361 – Switch Expressions (Standard)**

Sau hai lần Preview (Java 12 & 13), **Switch Expressions** chính thức trở thành **chuẩn** trong Java 14.

```java
int numLetters = switch ("MONDAY") {
    case "MONDAY", "FRIDAY", "SUNDAY" -> 6;
    case "TUESDAY" -> 7;
    case "THURSDAY", "SATURDAY" -> 8;
    case "WEDNESDAY" -> 9;
    default -> throw new IllegalStateException("Invalid day");
};
```

### **6\. JEP 365 – ZGC trên Windows**

*   **Z Garbage Collector (ZGC)** có thể chạy trên **Windows** (trước đó chỉ Linux).
    
*   Hỗ trợ low-latency GC trên nhiều nền tảng hơn.
    

### **7\. JEP 366 – Deprecate ParallelScavenge + SerialOld GC**

*   Bắt đầu **loại bỏ** sự kết hợp `ParallelScavenge + SerialOld`.
    
*   Khuyến khích dùng G1 hoặc ZGC.
    

### **8\. JEP 364 – Packaging Tool (Incubator)**

*   Giới thiệu `jpackage` – công cụ đóng gói ứng dụng Java thành file cài đặt gốc (Windows `.msi`, macOS `.pkg`, Linux `.deb`/`.rpm`).
    

### **📌 Tóm Tắt Java 14**

#Tính năngMô tả ngắn1Pattern Matching (Preview)Gọn hơn khi dùng `instanceof`2Records (Preview)Class bất biến, auto constructor + getters3Helpful NPEsNullPointerException chi tiết hơn4Text Blocks (Preview 2)Chuỗi nhiều dòng `"""` cải tiến5Switch ExpressionsChính thức thành chuẩn6ZGC trên WindowsHỗ trợ GC low-latency cho Windows7GC DeprecationLoại bỏ dần `ParallelScavenge + SerialOld`8jpackage (Incubator)Công cụ đóng gói app Java

