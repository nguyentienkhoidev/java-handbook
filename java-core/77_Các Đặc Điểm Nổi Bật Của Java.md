# Các Đặc Điểm Nổi Bật Của Java 21

### **1\. Giới thiệu**

Java 21 là bản phát hành **Hỗ trợ Dài hạn (LTS – Long Term Support)** được công bố vào tháng 9 năm 2023. Phiên bản này mang đến nhiều tính năng mới và cải tiến trong các lĩnh vực **ngôn ngữ**, **thư viện** và **JVM**.

Java 21 đặc biệt nổi bật với:

*   Quản lý luồng hiện đại nhờ **Virtual Threads** và **Structured Concurrency**.
    
*   Hỗ trợ mạnh mẽ cho **pattern matching**.
    
*   Cải tiến về **chuỗi (string templates)**.
    
*   Nâng cao hiệu suất với **Generational ZGC**.
    

👉 Những tính năng này giúp phát triển các ứng dụng **lớn, phức tạp** nhưng vẫn giữ được **an toàn, hiệu quả và dễ đọc**.

### **2\. Pattern Matching for Switch (Final Release)**

*   Tính năng **Pattern Matching for Switch** đã được hoàn thiện trong Java 21. Nó giúp viết mã ngắn gọn và dễ đọc hơn khi xử lý nhiều trường hợp của một giá trị.
    
*   Hỗ trợ pattern matching cho **class**, **record**, **sealed class** và **guarded patterns**.
    

📌 Ví dụ:

```java
static String formatterPattern(Object obj) {
    return switch (obj) {
        case Integer i -> String.format("int %d", i);
        case Long l    -> String.format("long %d", l);
        case Double d  -> String.format("double %f", d);
        case String s  -> String.format("String %s", s);
        default        -> obj.toString();
    };
}
```

### **3\. Record Patterns (Final Release)**

**Record Patterns** cho phép giải nén trực tiếp các thành phần của record khi sử dụng pattern matching, giúp code dễ hiểu và ngắn gọn hơn.

📌 Ví dụ:

```java
record Point(int x, int y) {}

void printSum(Object obj) {
    if (obj instanceof Point(int x, int y)) {
        System.out.println(x + y);
    }
}
```

### **4\. Unnamed Patterns and Variables (Preview)**

Java 21 giới thiệu **Unnamed Patterns** và **Unnamed Variables** để bỏ qua các giá trị không cần thiết trong pattern matching. Giúp mã ngắn gọn hơn khi chỉ quan tâm đến một phần dữ liệu.

📌 Ví dụ:

```java
record Point(int x, int y) {}

static void process(Point p) {
    if (p instanceof Point(int x, _)) { // Bỏ qua giá trị y
        System.out.println("X coordinate is: " + x);
    }
```

### **5\. Sequenced Collections (Final Release)**

*   Bổ sung API cho các collection có thứ tự, bao gồm: `SequencedCollection`, `SequencedSet`, `SequencedMap`.
    
*   Điểm nổi bật: Truy cập, thêm, xóa phần tử **đầu tiên** và **cuối cùng** dễ dàng.
    

📌 Ví dụ:

```java
SequencedCollection<String> sc = new ArrayList<>();
sc.addFirst("First");
sc.addLast("Last");
```

### **6\. Virtual Threads (Final Release)**

**Virtual Threads** (thuộc dự án Loom) cho phép tạo hàng triệu luồng nhẹ trong JVM mà không ảnh hưởng lớn đến hiệu năng. Rất hữu ích cho ứng dụng **I/O bound** như web server, microservices.

📌 Ví dụ:

```java
Thread.startVirtualThread(() -> {
    System.out.println("Running in a virtual thread");
});
```

### **7\. Structured Concurrency (Preview)**

Cung cấp API để quản lý các tác vụ đồng thời theo cách có cấu trúc. Dễ dàng hủy, theo dõi, và xử lý lỗi trong nhóm tác vụ.

📌 Ví dụ:

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<String> future1 = scope.fork(() -> fetchDataFromAPI1());
    Future<String> future2 = scope.fork(() -> fetchDataFromAPI2());
    scope.join();              // Chờ cả hai tác vụ
    scope.throwIfFailed();     // Nếu có lỗi thì ném ra
}
```

### **8\. String Templates (Preview)**

**String Templates** giúp chèn biến trực tiếp vào chuỗi một cách an toàn, ngắn gọn.

📌 Ví dụ:

```java
String name = "Fox Dev";
int age = 18;
String message = STR."My name is \{name} and I am \{age} years old.";
System.out.println(message);
```

### **9\. Generational ZGC (Final Release)**

**ZGC** được nâng cấp thành **Generational ZGC**, cho phép phân biệt đối tượng **ngắn hạn** và **dài hạn** trong heap. Cải thiện hiệu năng và giảm pause time.

### **10\. Preview và Incubator Features**

*   Ngoài các tính năng chính thức, Java 21 còn giới thiệu:
    
*   **Scoped Values (Preview):** Quản lý dữ liệu bất biến giữa các thread/virtual thread.
    
*   **Foreign Function & Memory API (Preview):** Giao tiếp với native code mà không cần JNI.
    
*   **Vector API (Incubator):** Tận dụng SIMD để tăng hiệu suất xử lý dữ liệu.
    

### **11\. Deprecation và Removal**

*   Một số API và tính năng cũ đã được:
    
*   Đánh dấu **@Deprecated**.
    
*   Hoặc **loại bỏ** để cải thiện hiệu năng, bảo mật, và giữ Java gọn gàng hơn.
    

👉 **Tóm lại:**  
Java 21 (LTS) là phiên bản **cực kỳ quan trọng** với các tính năng thay đổi lớn về **concurrency (Virtual Threads, Structured Concurrency)**, **pattern matching**, **string templates**, và **ZGC**. Đây sẽ là nền tảng ổn định cho phát triển ứng dụng trong nhiều năm tới.

