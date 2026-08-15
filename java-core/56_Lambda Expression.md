# Lambda Expression

### **1\. Lambda Expression là gì?**

**Lambda Expression** (biểu thức lambda) là một **tính năng mới trong Java 8** giúp viết code **ngắn gọn và dễ đọc hơn** bằng cách biểu diễn một **hàm (function)** như là một **đối tượng**.

👉 Hiểu đơn giản: Lambda là **cách viết rút gọn của Anonymous Class** (lớp ẩn danh) khi implement một **Functional Interface** (interface chỉ có 1 phương thức trừu tượng).

### **2\. Cú pháp của Lambda Expression**

```java
(parameters) -> expression
(parameters) -> { statements; }
```

*   `parameters`: danh sách tham số (có thể bỏ kiểu dữ liệu, Java sẽ suy luận).
*   `->`: toán tử lambda (lambda operator).
*   `expression` hoặc `{ statements; }`: phần thân (body) của lambda.

– Ví dụ: Không dùng Lambda (cách cũ với Anonymous Class)

```java
 interface SampleFunctionalInterface {
    void sayHello();
}
```

```java
public class App {
    public static void main(String[] args) {
    	// Cách định nghĩa cũ
        SampleFunctionalInterface oldWay = new SampleFunctionalInterface() {
            @Override
            public void sayHello() {
                System.out.println("Hello Java!");
            }
        };
        oldWay.sayHello();
    }
}
```

– Ví dụ: Dùng Lambda Expression (cách mới, gọn hơn)

```java
public class App {
    public static void main(String[] args) {
        SampleFunctionalInterface newWay = () -> System.out.println("Hello Lambda!");
        newWay.sayHello();
    }
}
```

– Ví dụ: Lambda với tham số

```java
interface Calculator {
    int add(int a, int b);
}
```

```java
public class Main {
    public static void main(String[] args) {
        // Dùng lambda để định nghĩa phương thức add
        Calculator calc = (a, b) -> a + b;
        System.out.println(calc.add(5, 3)); // 8
    }
}
```

– Ví dụ: Lambda trong Java Collections

```java
import java.util.*;

public class App {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java", "Python", "C++");

        // Dùng lambda với forEach
        list.forEach(s -> System.out.println(s));

        // Dùng method reference (ngắn hơn nữa)
        list.forEach(System.out::println);
    }
}
```

### **3\. Ưu điểm & Nhược điểm của Lambda Expression**

*   **Ưu điểm**
    *   **Ngắn gọn, dễ đọc** → loại bỏ boilerplate code.
    *   **Hỗ trợ lập trình hàm** trong Java (functional programming).
    *   **Tích hợp tốt với Stream API** và Collections API.
    *   **Dễ dùng với biểu thức inline** (không cần tạo lớp riêng).
*   Nhược điểm
    *   ❌ Chỉ áp dụng cho **Functional Interface** (interface có đúng 1 phương thức trừu tượng).
    *   ❌ Quá lạm dụng có thể khiến code khó debug hơn.

