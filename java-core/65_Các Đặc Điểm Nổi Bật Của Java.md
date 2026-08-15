# Các Đặc Điểm Nổi Bật Của Java 9

Java 9 phát hành vào tháng 9 năm 2017 đã mang đến nhiều tính năng và cải tiến mạnh mẽ, đặc biệt trong việc tổ chức mã nguồn và tối ưu hóa hiệu suất hệ thống.

Dưới đây là **những đặc điểm nổi bật của Java 9:**

### **1\. Java Platform Module System (JPMS) – Project Jigsaw**

Tính năng quan trọng nhất của Java 9, cung cấp **hệ thống module** giúp chia ứng dụng thành các module nhỏ, dễ quản lý hơn.

*   Cải thiện khả năng bảo trì.
    
*   Tối ưu kích thước ứng dụng.
    
*   Giảm thời gian khởi động.
    

**Khái niệm:**

*   **Module**: Tập hợp các package và tài nguyên liên quan.
    
*   **Module Descriptor**: File `module-info.java` định nghĩa dependency và các package được export.
    

### **2\. REPL – JShell**

Java 9 giới thiệu **JShell** – một công cụ REPL (**Read-Eval-Print-Loop**) cho phép chạy lệnh Java trực tiếp, không cần tạo file hay class.  
👉 Hữu ích cho việc học, thử nghiệm code nhỏ, test nhanh tính năng.

– Ví dụ:

```java
jshell> System.out.println("Hello, JShell!");
```

### **3\. Factory Methods cho Collections**

Java 9 bổ sung các phương thức factory như `List.of()`, `Set.of()`, `Map.of()` để tạo **collection bất biến (immutable)** nhanh gọn.

– Ví dụ:

```java
public class App {
    public static void main(String[] args) {
        List<String> list = List.of("Java", "PHP", "Python");
        list.forEach(System.out::println);

        Map<Integer, String> map = Map.of(1, "Vietnam", 2, "Lao", 3, "Cambodia");
        map.forEach((k, v) -> System.out.println(k + "=" + v));

        Set<String> set = Set.of("Java", "Spring", "Hibernate", "JSP");
        set.forEach(System.out::println);
    }
}
```

### **4\. Anonymous Inner Class**

Dùng để **triển khai nhanh một interface hoặc abstract class** mà không cần tạo class riêng.

– Ví dụ:

```java
public abstract class AnonymousInnerClasses<T> {
    abstract T calculate(int a, int b);
}
```

```java
public class App {
    public static void main(String[] args) {
        AnonymousInnerClasses<Integer> add = new AnonymousInnerClasses<>() {
            @Override
            Integer calculate(int x, int y) { return x + y; }
        };
        System.out.println("a + b = " + add.calculate(10, 3));

        AnonymousInnerClasses<Long> sub = new AnonymousInnerClasses<>() {
            @Override
            Long calculate(int x, int y) { return (long) x - y; }
        };
        System.out.println("a - b = " + sub.calculate(15, 5));
    }
}
```

### **5\. Java Runtime Version API**

Java 9 giới thiệu `Runtime.Version` giúp truy vấn thông tin phiên bản chính xác hơn.

```java
public class JavaRuntimeVersion {
    public static void main(String[] args) {
        Runtime.Version version = Runtime.version();
        System.out.println("Version: " + version.version());
        System.out.println("Build: " + version.build());
        System.out.println("Major: " + version.major());
        System.out.println("Minor: " + version.minor());
        System.out.println("Patch: " + version.patch());
        System.out.println("Security: " + version.security());
    }
}
```

### **6\. Cải Tiến Stream API**

Các phương thức mới:

*   `takeWhile()` → Lấy phần tử đến khi điều kiện sai.
    
*   `dropWhile()` → Bỏ phần tử đến khi điều kiện sai.
    
*   `ofNullable()` → Tạo stream có thể chứa null.
    
*   `iterate()` → Hỗ trợ điều kiện dừng.
    

– Ví dụ `iterate()`:

```java
Stream<Integer> stream = Stream.iterate(2, i -> i <= 100, i -> i * 2);
stream.forEach(System.out::println);
```

### **7\. Private Methods trong Interface**

Java 9 cho phép định nghĩa **private methods** trong interface để tái sử dụng code giữa `default` và `static methods`.

```java
public interface PrivateInterfaceMethods {
    default void sayHello() {
        speak();
    }
    private void speak() {
        System.out.println("Welcome to Java 9");
    }
    private static void goodbye() {
        System.out.println("Good bye!");
    }
    void printMessage();
}
```

### **8\. HTTP/2 Client API**

Java 9 giới thiệu **HTTP/2 Client API** thay thế `HttpURLConnection`, hỗ trợ **HTTP/2** và **WebSocket**.

```java
public class App {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://nguyentienkhoi.hashnode.dev"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
```

### **9\. Multi-Release JAR Files**

Một JAR có thể chứa **nhiều phiên bản class** cho các phiên bản Java khác nhau, giúp duy trì **tương thích ngược** mà không cần tạo nhiều file JAR riêng.

### **10\. Enhanced Process API**

Quản lý và giám sát process dễ dàng hơn.

```java
public class App {
    public static void main(String[] args) {
        ProcessHandle handle = ProcessHandle.current();
        System.out.println("PID: " + handle.pid());
        System.out.println("Info: " + handle.info());
        System.out.println("Alive: " + handle.isAlive());
    }
}
```

### **11\. Improved Deprecation**

Annotation `@Deprecated` nay có thêm:

*   `since` → Bắt đầu deprecated từ phiên bản nào.
    
*   `forRemoval` → Có định xóa trong tương lai không.
    

```java
@Deprecated(since = "9", forRemoval = true)
public void deprecatedMethodName() {
    // logic
}
```

### **12\. Miscellaneous Changes (Khác)**

*   **Unified JVM Logging**: Hợp nhất logging API của JVM.
    
*   **Compact Strings**: Dùng `byte[]` để tiết kiệm bộ nhớ cho chuỗi Latin-1.
    
*   **Variable Handles**: API mạnh mẽ hơn `Atomic` để thao tác biến.
    

