# Method References

**Method References** là một **tính năng mới trong Java 8** cho phép bạn **tham chiếu (reference) trực tiếp đến một phương thức hoặc constructor** thay vì phải viết toàn bộ biểu thức lambda.

### **1\. Đặc điểm chính:**

*   **Ngắn gọn hơn Lambda**: Không cần viết `x -> someMethod(x)` nữa, chỉ cần viết `ClassName::methodName`.
*   **Có thể tham chiếu**:
    *   Phương thức **static**.
    *   Phương thức **instance** (của đối tượng cụ thể hoặc của bất kỳ đối tượng nào kiểu đó).
    *   Constructor (`new`).
*   **Chỉ dùng khi lambda chỉ gọi lại một phương thức có sẵn**.

### **2\. Cú pháp cơ bản**

```java
ClassName::staticMethod
objectReference::instanceMethod
ClassName::instanceMethod
ClassName::new
```

– Ví dụ: Tham chiếu đến phương thức static

```java
import java.util.function.Consumer;

public class App {
    public static void printMsg(String msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) {
        // Dùng lambda
        Consumer<String> lambdaWay = s -> Main.printMsg(s);
        lambdaWay.accept("Hello Lambda");

        // Dùng method reference
        Consumer<String> refWay = Main::printMsg;
        refWay.accept("Hello Method Reference");
    }
}
```

– Ví dụ: Tham chiếu đến phương thức instance của một object

```java
import java.util.function.Supplier;

public class App {
    public static void main(String[] args) {
        String str = "Fox Dev";

        // Dùng lambda
        Supplier<Integer> lambdaWay = () -> str.length();

        // Dùng method reference
        Supplier<Integer> refWay = str::length;

        System.out.println(lambdaWay.get()); // 7
        System.out.println(refWay.get());    // 7
    }
}
```

– Ví dụ: Tham chiếu đến phương thức static

```java
import java.util.*;
import java.util.function.Function;

public class App {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java", "Python", "C++");

        // Dùng lambda
        list.forEach(s -> System.out.println(s));

        // Dùng method reference
        list.forEach(System.out::println);

        // Ví dụ khác: map tên thành độ dài
        Function<String, Integer> func = String::length;
        System.out.println(func.apply("Java")); // 4
    }
}
```

– Ví dụ: Tham chiếu đến Constructor

```java
import java.util.function.Supplier;

class Student {
    Student() {
        System.out.println("A new Student is created!");
    }
}

public class Main {
    public static void main(String[] args) {
        // Dùng lambda
        Supplier<Student> lambdaWay = () -> new Student();

        // Dùng method reference
        Supplier<Student> refWay = Student::new;

        lambdaWay.get(); // A new Student is created!
        refWay.get();    // A new Student is created!
    }
}
```

– Ví dụ: Method References với Functional Interface

```java
import java.util.function.Consumer;

public class App {
    public static void main(String[] args) {
        // 🔴 Sử dụng biểu thức lambda
        Consumer<String> print = str -> System.out.println(str);
        print.accept("Hello Fox Dev");  // Output: Hello Fox Dev

        // 🟢 Sử dụng Method Reference
        Consumer<String> printMethodReference = System.out::println;
        printMethodReference.accept("Welcome Fox Dev"); // Output: Welcome Fox Dev
    }
}
```

