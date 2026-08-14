# Functional Interface

### **1\. Định nghĩa**

**Functional Interface** là một khái niệm quan trọng trong Java 8, đóng vai trò **nền tảng** cho việc triển khai **biểu thức Lambda** và **method reference**.

Functional Interface là một interface trong Java **chỉ có duy nhất một phương thức trừu tượng (Single Abstract Method – SAM)**. Nhờ đặc điểm này, chúng giúp code ngắn gọn, dễ đọc và linh hoạt hơn trong xử lý logic.

```java
@FunctionalInterface
public interface SampleFunctionalInterface {
    void doSomething(); // Single Abstract Method
}
```

– Giải thích:

*   `**@FunctionalInterface**`
    *   Annotation này dùng để đảm bảo một interface tuân thủ quy tắc của functional interface.
    *   Nếu interface được đánh dấu `@FunctionalInterface` nhưng chứa **nhiều hơn một phương thức trừu tượng**, trình biên dịch sẽ báo lỗi.
    *   Annotation này **không bắt buộc**, nhưng khuyến khích dùng để giúp code rõ ràng và an toàn hơn.
*   **Phương thức** `**void doSomething()**`
    *   Vì interface chỉ có duy nhất một **abstract method**, nên `SampleFunctionalInterface` được coi là một **Functional Interface hợp lệ**.  
         

### **2\. Các Functional Interface phổ biến**

Java 8 cung cấp sẵn nhiều **Functional Interface** trong package `java.util.function`. Những interface này được sử dụng rất thường xuyên khi kết hợp với **Lambda** và **Stream API**.

Dưới đây là các Functional Interface quan trọng:

2.1. `**Predicate<T>**`

*   **Mục đích:** Kiểm tra một điều kiện trên đối tượng kiểu `T`, trả về `boolean`.
*   **Phương thức chính:** `boolean test(T t)`

– Ví dụ:

```java
Predicate<String> isLonger = str -> str.length() > 8;
System.out.println(isLonger.test("Tây Java")); // false
```

2.2. `**Function<T, R>**`

*   **Mục đích:** Ánh xạ (transform) một đối tượng từ kiểu `T` sang kiểu `R`.
*   **Phương thức chính:** `R apply(T t)`

– Ví dụ:

```java
Function<String, Integer> stringToLength = str -> str.length();
System.out.println(stringToLength.apply("Tây Java")); // 8
```

2.3. `**Consumer<T>**`

*   **Mục đích:** Thực hiện một hành động nào đó trên đối tượng kiểu `T` mà **không trả về kết quả**.
*   **Phương thức chính:** `void accept(T t)`

– Ví dụ:

```java
Consumer<String> printString = str -> System.out.println(str);
printString.accept("Tây Java"); // In ra "Tây Java"
```

2.4. `**Supplier<T>**`

*   **Mục đích:** Cung cấp một giá trị kiểu `T` mà **không cần tham số đầu vào**.
*   **Phương thức chính:** `T get()`

– Ví dụ:

```java
Supplier<String> supplier = () -> "Tây Java";
System.out.println(supplier.get()); // Tây Java
```

2.5. `**BiFunction<T, U, R>**`

*   **Mục đích:** Nhận hai tham số kiểu `T` và `U`, trả về một giá trị kiểu `R`.
*   **Phương thức chính:** `R apply(T t, U u)`

– Ví dụ:

```plaintext
BiFunction<Integer, Integer, Integer> sum = (a, b) -> a + b;
System.out.println(sum.apply(3, 5)); // 8
```

2.6. `**UnaryOperator<T>**`

*   **Mục đích:** Nhận một tham số kiểu `T` và trả về **cùng kiểu T**.
*   **Đặc biệt:** Là trường hợp cụ thể của `Function<T, R>` khi `T` và `R` giống nhau.
*   **Phương thức chính:** `T apply(T t)`

– Ví dụ:

```java
UnaryOperator<Integer> square = x -> x * x;
System.out.println(square.apply(13)); // 169
```

2.7. `**BinaryOperator<T>**`

*   **Mục đích:** Nhận hai tham số cùng kiểu `T` và trả về một giá trị kiểu `T`.
*   **Đặc biệt:** Là trường hợp cụ thể của `BiFunction<T, U, R>` khi `T`, `U`, và `R` đều giống nhau.
*   **Phương thức chính:** `T apply(T t1, T t2)`

– Ví dụ:

```java
BinaryOperator<Integer> multiply = (a, b) -> a * b;
System.out.println(multiply.apply(5, 4)); // 20
```

📌 **Tóm lại:**

*   `Predicate` → Kiểm tra điều kiện (boolean).
*   `Function` → Biến đổi dữ liệu từ `T` → `R`.
*   `Consumer` → Thực hiện hành động (không trả về).
*   `Supplier` → Cung cấp giá trị (không cần input).
*   `BiFunction` → Nhận 2 input, trả về 1 output.
*   `UnaryOperator` & `BinaryOperator` → Trường hợp đặc biệt của `Function` và `BiFunction`.

## **3\. Biểu thức Lambda với Functional Interface**

**Biểu thức Lambda** giúp khởi tạo các functional interface một cách **ngắn gọn và dễ hiểu hơn**.  
Thay vì phải tạo **lớp ẩn danh (anonymous class)** để triển khai phương thức của functional interface, ta có thể dùng **lambda expression** để viết code súc tích và rõ ràng hơn.

– Ví dụ:

```java
@FunctionalInterface
interface SampleFunctionalInterface {
    void doSomething();
}
```

```java
public class App {
    public static void main(String[] args) {

        // 🔴 Cách cũ: Sử dụng lớp ẩn danh
        SampleFunctionalInterface oldWay = new SampleFunctionalInterface() {
            @Override
            public void doSomething() {
                System.out.println("Cách viết cũ");
            }
        };
        oldWay.doSomething(); // Output: Cách viết cũ


        // 🟢 Cách mới: Sử dụng biểu thức Lambda
        SampleFunctionalInterface newWay = () -> 
            System.out.println("Cách viết mới");

        newWay.doSomething(); // Output: Cách viết mới
    }
}
```

### **4\. Method References với Functional Interface**

Ngoài **Lambda Expression**, Java 8 còn cung cấp **Method Reference** – một cách viết ngắn gọn hơn để tham chiếu trực tiếp đến **phương thức có sẵn** hoặc **constructor**.

👉 **Method Reference** là một cú pháp rút gọn của Lambda, khi Lambda chỉ gọi lại một phương thức đã tồn tại.

– Ví dụ:

```java
import java.util.function.Consumer;

public class App {
    public static void main(String[] args) {
        // 🔴 Sử dụng biểu thức lambda
        Consumer<String> print = str -> System.out.println(str);
        print.accept("Hello Tây Java");  // Output: Hello Tây Java

        // 🟢 Sử dụng Method Reference
        Consumer<String> printMethodReference = System.out::println;
        printMethodReference.accept("Welcome Tây Java"); // Output: Welcome Tây Java
    }
}
```

**🔹 Các loại Method Reference trong Java 8**

*   Tham chiếu đến phương thức tĩnh (Static Method Reference)

```java
Function<Integer, String> intToString = String::valueOf;
System.out.println(intToString.apply(100)); // "100"
```

*   Tham chiếu đến phương thức của một đối tượng (Instance Method Reference)

```java
String message = "Tây Java";
Supplier<Integer> getLength = message::length;
System.out.println(getLength.get()); // 7
```

*   Tham chiếu đến phương thức của một lớp bất kỳ (Instance Method of an Arbitrary Object)

```java
List<String> names = Arrays.asList("Java", "Spring", "Hibernate");
names.forEach(System.out::println);
```

*   Tham chiếu đến Constructor (Constructor Reference)

```java
Supplier<StringBuilder> builder = StringBuilder::new;
System.out.println(builder.get().append("Hello Constructor Reference"));
```

### **5\. Functional Interface và Default Methods**

Mặc dù **Functional Interface** chỉ được phép có **một phương thức trừu tượng (abstract method)**, nhưng nó **có thể chứa nhiều** `**default method**` **và** `**static method**`.

👉 Lý do:

`default method` và `static method` **đã có sẵn phần thân (implementation)** → không được tính là abstract.

Do đó, chúng không làm thay đổi bản chất của Functional Interface.

– Ví dụ:

```java
@FunctionalInterface
public interface SampleFunctionalInterface {
    
    // 🔑 Phương thức trừu tượng duy nhất
    void doSomething();
    
    // 🔹 Static method
    static void doSomethingElse() {
        System.out.println("Do something else");
    }

    // 🔹 Default method 1
    default void printMessage() {
        System.out.println("Sample Functional Interface");
    }

    // 🔹 Default method 2
    default void welcome() {
        System.out.println("Welcome to Tây Java");
    }
}
```

```java
public class App {
    public static void main(String[] args) {
        // Sử dụng biểu thức lambda cho Functional Interface
        SampleFunctionalInterface sample = () -> 
            System.out.println("Do something (lambda)");

        // Gọi phương thức trừu tượng
        sample.doSomething();  // Output: Do something (lambda)

        // Gọi default methods
        sample.printMessage(); // Output: Sample Functional Interface
        sample.welcome();      // Output: Welcome to Tây Java

        // Gọi static method (qua tên Interface)
        SampleFunctionalInterface.doSomethingElse(); 
        // Output: Do something else
    }
}
```
