# StringJoiner

`StringJoiner` là một **class mới được giới thiệu trong Java 8** (thuộc gói `java.util`) dùng để **nối (join) nhiều chuỗi** lại với nhau, với khả năng định nghĩa **delimiter (ký tự phân cách)**, **prefix (tiền tố)** và **suffix (hậu tố)**.

### **1\. Đặc điểm chính của** `**StringJoiner**`

*   **Delimiter**: Chuỗi phân cách giữa các phần tử (ví dụ: `,` hoặc `-`).
*   **Prefix và Suffix**: Có thể thêm tiền tố (prefix) ở đầu và hậu tố (suffix) ở cuối chuỗi kết quả.
*   **Tự động thêm delimiter**: Không cần phải kiểm tra thủ công để bỏ ký tự phân cách ở cuối như khi dùng vòng lặp `for`.
*   **Hữu ích trong việc build chuỗi**: Khi cần tạo chuỗi từ nhiều phần tử mà không muốn lo việc thêm dấu phân cách thủ công.

### **2\. Cú pháp khởi tạo**

```java
StringJoiner joiner = new StringJoiner(delimiter);
StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
```

*   `delimiter`: ký tự/phần tử dùng để phân cách.
*   `prefix`: chuỗi thêm ở đầu.
*   `suffix`: chuỗi thêm ở cuối.

– Ví dụ cơ bản:

```java
import java.util.StringJoiner;

public class App {
    public static void main(String[] args) {
        // Tạo StringJoiner với dấu phẩy làm delimiter
        StringJoiner joiner = new StringJoiner(", ");
        joiner.add("Java");
        joiner.add("Python");
        joiner.add("C++");

        System.out.println(joiner); 
        // Output: Java, Python, C++
    }
}
```

– Ví dụ với prefix và suffix

```java
import java.util.StringJoiner;

public class APP {
    public static void main(String[] args) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        joiner.add("One");
        joiner.add("Two");
        joiner.add("Three");

        System.out.println(joiner);
        // Output: [One, Two, Three]
    }
}
```

– Ví dụ kết hợp 2 StringJoiner

```java
public class App {
    public static void main(String[] args) {
        StringJoiner sj1 = new StringJoiner(", ");
        sj1.add("Apple").add("Banana");

        StringJoiner sj2 = new StringJoiner(", ");
        sj2.add("Orange").add("Grapes");

        sj1.merge(sj2);
        System.out.println(sj1);
        // Output: Apple, Banana, Orange, Grapes
    }
}
```

👉 Tóm lại: `StringJoiner` giúp việc nối chuỗi trở nên **gọn gàng, rõ ràng và tránh lỗi** hơn so với việc tự xử lý delimiter bằng tay.

### **3\. So sánh 3 cách nối chuỗi trong Java 8**

| Công cụ | Đặc điểm | Khi nào nên dùng | Ví dụ |
| --- | --- | --- | --- |
| `**StringJoiner**` | \- Là class trong `java.util`. - Hỗ trợ delimiter, prefix, suffix. - Có thể `merge()` nhiều `StringJoiner`. - Nối chuỗi bằng cách `.add()`. | Khi cần **xây dựng chuỗi phức tạp**, có tiền tố/hậu tố, hoặc gộp nhiều nhóm chuỗi. | `java StringJoiner sj = new StringJoiner(", ", "[", "]"); sj.add("A").add("B"); System.out.println(sj); // [A, B]` |
| `**String.join()**` | \- Là **static method** của `String`. - Hỗ trợ delimiter nhưng **không có prefix/suffix**. - Thường dùng để nối `array` hoặc `Iterable`. | Khi cần nối **một danh sách chuỗi nhanh chóng** mà không quan tâm prefix/suffix. | `java String result = String.join(" - ", "Java", "Python", "C++"); System.out.println(result); // Java - Python - C++` |
| `**Collectors.joining()**` | \- Dùng trong **Stream API**. - Có thể chỉ định delimiter, prefix, suffix. - Tích hợp với `map()` và `filter()`. | Khi làm việc với **Stream** và muốn nối kết quả xử lý dữ liệu thành chuỗi. | `java import java.util.*; import java.util.stream.*; List<String> langs = Arrays.asList("Java","Python","C++"); String result = langs.stream() .map(String::toUpperCase) .collect(Collectors.joining(", ", "[", "]")); System.out.println(result); // [JAVA, PYTHON, C++]` |

