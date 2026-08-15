# Type Inference

### **1\. Type Inference là gì?**

**Type Inference** (suy luận kiểu dữ liệu) là khả năng của **Java Compiler** tự động xác định kiểu dữ liệu của biến, tham số generic, hoặc giá trị trả về mà không cần lập trình viên khai báo tường minh.

Giúp code **ngắn gọn, dễ đọc** hơn, đặc biệt khi làm việc với **Generics, Collections, và Lambda Expressions**.

📌 Ví dụ đơn giản:

*   Không có type inference (Java cũ)

```plaintext
List<String> list = new ArrayList<String>();
```

*   Với type inference (Java 7+ - Diamond Operator `<>`)

```java
List<String> list = new ArrayList<>();
```

👉 Compiler sẽ suy luận `<>` là `String`.

### **2\. Các trường hợp phổ biến của Type Inference**

| # | Trường hợp | Ví dụ | Mô tả |
| --- | --- | --- | --- |
| 1 | **Diamond Operator (**`**<>**`**)** | `Map<String, List<Integer>> map = new HashMap<>();` | Compiler tự suy luận kiểu generic khi khởi tạo collection. |
| 2 | **Generic Methods** | `Collections.<String>emptyList();` có thể viết `Collections.emptyList();` | Compiler đoán kiểu từ ngữ cảnh. |
| 3 | **Lambda Expressions** (Java 8) | `Stream.of("Java", "Python").forEach(s -> System.out.println(s));` | Kiểu của `s` được compiler suy ra là `String`. |
| 4 | **Method References** | `list.forEach(System.out::println);` | Compiler tự suy luận kiểu tham số từ method reference. |
| 5 | **var (Java 10)** | `var list = new ArrayList<String>();` | Compiler suy ra `list` có kiểu `ArrayList<String>`. |

📌 Ví dụ: Generic Method

```java
public static <T> List<T> singletonList(T value) {
    List<T> list = new ArrayList<>();
    list.add(value);
    return list;
}

List<String> list = singletonList("Hello");
// Compiler tự hiểu T = String
```

📌 Lambda + Stream API

```java
List<String> names = Arrays.asList("Java", "Spring", "Docker");

List<String> upper = names.stream()
                          .map(s -> s.toUpperCase()) // Compiler biết s là String
                          .toList();

System.out.println(upper); // [JAVA, SPRING, DOCKER]
```

📌 `var` (Java 10+)

```java
var nums = List.of(1, 2, 3, 4); 
// nums có kiểu List<Integer>

var text = "Hello FoxDev";
// text có kiểu String
```

#### ⚠️ **Lưu ý**

*   **Type inference không có nghĩa là dynamic typing** (Java vẫn là strongly-typed, kiểu được xác định tại compile-time).
*   `var` chỉ được dùng cho **local variables** (không dùng cho fields, parameters, hay return type).
*   Dùng type inference giúp code **gọn gàng**, nhưng lạm dụng có thể làm code **khó đọc** (khó biết kiểu dữ liệu thực sự).

#### **🎯 Kết luận**

*   **Java 7**: Giới thiệu **Diamond Operator**.
*   **Java 8**: Tăng cường type inference với **Lambda & Method References**.
*   **Java 10**: Giới thiệu `**var**` để khai báo biến ngắn gọn.

