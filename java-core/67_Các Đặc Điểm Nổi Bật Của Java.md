# Các Đặc Điểm Nổi Bật Của Java 11

**Java 11** (phát hành tháng 9/2018) là một phiên bản **LTS (Long-Term Support)**, được nhiều doanh nghiệp sử dụng vì được hỗ trợ lâu dài. Đây là bản kế nhiệm quan trọng sau Java 8 (LTS) và mang lại nhiều cải tiến lớn.

### **1\. Java LTS (Long-Term Support)**

*   Java 11 là bản **LTS đầu tiên sau Java 8**, được hỗ trợ dài hạn (Oracle hỗ trợ đến 2026 cho bản thương mại).
    
*   Doanh nghiệp ưu tiên dùng Java 11 vì sự ổn định và bảo mật.
    

### **2\.** `var` **trong Lambda Parameters**

*   Java 10 cho phép dùng `var` cho biến cục bộ.
    
*   Java 11 mở rộng, cho phép dùng `var` trong **lambda parameters**.
    

```java
BiFunction<Integer, Integer, Integer> add = (var x, var y) -> x + y;
System.out.println(add.apply(10, 5)); // 15
```

### **3\. Chuỗi (String API) Mới**

Java 11 bổ sung nhiều phương thức tiện lợi cho `String`:

*    `isBlank()`
    

Kiểm tra xem chuỗi có rỗng hoặc chỉ chứa ký tự trắng (whitespace) không. Khác với `isEmpty()`, vốn chỉ kiểm tra độ dài.

```java
System.out.println("".isBlank());        // true
System.out.println("   ".isBlank());     // true
System.out.println("abc".isBlank());     // false
```

*   `lines()`
    

Trả về một `Stream<String>` gồm các dòng trong chuỗi, tách bởi ký tự xuống dòng. → Hữu ích khi xử lý văn bản nhiều dòng.

```java
String text = "Java\nPython\nC++";
text.lines().forEach(System.out::println);
```

– Kết quả:

```java
Java
Python
C++
```

*   `strip()`**,** `stripLeading()`**,** `stripTrailing()`
    

Loại bỏ ký tự trắng (bao gồm cả Unicode whitespace) ở đầu/cuối chuỗi. Khác với `trim()`, vốn chỉ xử lý một số whitespace trong bảng ASCII.

```java
System.out.println("  Fox Dev  ".strip());        // "Fox Dev"
System.out.println("  Fox Dev  ".stripLeading()); // "Fox Dev  "
System.out.println("  Fox Dev  ".stripTrailing());// "  Fox Dev"
```

*   `repeat(int count)`
    

Lặp lại chuỗi nhiều lần.

```java
System.out.println("Java ".repeat(3)); // Java Java Java 
```

*   `indent(int n)`
    

Thêm/thụt lề vào từng dòng trong chuỗi. (Thực tế chính thức từ Java 12, nhưng nhiều tài liệu nhắc chung khi học String API mới).

```java
System.out.println("Hello\nWorld".indent(4));
```

### **4\. HTTP Client API (Chuẩn hóa từ Java 9/10)**

*   API HTTP/2 Client chính thức được chuẩn hóa (không còn ở chế độ incubator).
    
*   Hỗ trợ **HTTP/2** và **WebSocket**.
    

```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://nguyentienkhoi.hashnode.dev"))
        .build();
HttpResponse<String> response =
        client.send(request, HttpResponse.BodyHandlers.ofString());

System.out.println(response.body());
```

### **5\. API Files:** `readString()` **và** `writeString()`

Đọc/ghi file đơn giản hơn với `Files.readString()` và `Files.writeString()`.

```java
Path path = Path.of("test.txt");

// Ghi chuỗi vào file
Files.writeString(path, "Hello Java 11");

// Đọc chuỗi từ file
String content = Files.readString(path);
System.out.println(content);
```

### **6\. Collection API**

Bổ sung phương thức `toArray(IntFunction<T[]>)` giúp chuyển `Collection` sang mảng tiện lợi.

```java
List<String> list = List.of("Java", "Python", "C++");
String[] array = list.toArray(String[]::new);
System.out.println(Arrays.toString(array));
```

### **7\. Optional API**

Thêm các phương thức mới cho `Optional`:

```java
Optional<String> opt = Optional.of("Java 11");

System.out.println(opt.isEmpty());         // false
opt.ifPresentOrElse(
    v -> System.out.println("Value: " + v),
    () -> System.out.println("No value")
);
```

### **8\. Nashorn JavaScript Engine Removed**

*   **Nashorn**, engine JavaScript tích hợp từ Java 8, đã bị **loại bỏ** vì ít được sử dụng và đã lỗi thời.
    

### **9\. JEP 333 – Flight Recorder**

*   Công cụ giám sát hiệu năng và phân tích lỗi (giống như “black box” cho JVM).
    
*   Giúp dễ dàng theo dõi hiệu suất trong môi trường production.
    

### **10\. JEP 318 – Epsilon GC (Garbage Collector “No-Op”)**

*   Một **GC tối giản** chỉ phân bổ bộ nhớ mà không thu gom rác.
    
*   Dùng cho benchmark/test, không dùng trong production.
    

### **11\. JEP 335 – Deprecate Nashorn**

Chính thức deprecated `Nashorn` APIs (JS engine).

### **12\. Miscellaneous Changes**

**Single-File Source Code Execution**: Chạy file `.java` trực tiếp mà không cần biên dịch thủ công.

```java
$ java Hello.java
```

*   **Standardized Launching of Nested JARs** (hữu ích cho Spring Boot).
    
*   Nhiều cải tiến về bảo mật & hiệu năng JVM.
    

### **📌 Tóm Tắt Java 11**

#Tính năngMô tả ngắn1LTSBản Long-Term Support, ổn định cho doanh nghiệp2`var` trong lambdaHỗ trợ `var` cho tham số lambda3String APIThêm `isBlank()`, `lines()`, `repeat()`, `strip()`4HTTP ClientHTTP/2 Client chính thức5Files API`readString()`, `writeString()` đơn giản hơn6Collection API`toArray()` cải tiến7Optional APIThêm `isEmpty()`, `ifPresentOrElse()`8Nashorn RemovedLoại bỏ Nashorn JavaScript Engine9Flight RecorderCông cụ giám sát JVM10Epsilon GCGC không thu gom rác (benchmark)11Single-file ExecutionChạy trực tiếp file `.java`12KhácCải thiện hiệu năng, bảo mật JVM

