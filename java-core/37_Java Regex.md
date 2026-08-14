# Java Regex

### Java Regex (Biểu thức chính quy)

**Regex** (Regular Expression) là một API mạnh mẽ trong Java dùng để **xác định mẫu tìm kiếm** và **thao tác với chuỗi ký tự**.  
Trong Java, regex được hỗ trợ thông qua **package** `**java.util.regex**`, gồm các thành phần chính sau:

1.  **MatchResult interface**
2.  **Matcher class**
3.  **Pattern class**
4.  **PatternSyntaxException class**

### **1\. Matcher Class**

`Matcher` class implements `MatchResult` và được dùng để **thực hiện so sánh, đối chiếu chuỗi với biểu thức chính quy**.

**Một số phương thức quan trọng:**

#

Method

Mô tả

1

`boolean matches()`

Kiểm tra xem toàn bộ chuỗi có khớp với regex không

2

`boolean find()`

Tìm lần xuất hiện tiếp theo khớp với regex

3

`boolean find(int start)`

Tìm từ vị trí bắt đầu chỉ định

4

`String group()`

Trả về chuỗi con khớp với regex

5

`int start()`

Trả về vị trí bắt đầu của chuỗi con khớp

6

`int end()`

Trả về vị trí kết thúc của chuỗi con khớp

7

`int groupCount()`

Trả về số nhóm (groups) trong regex

### **2\. Pattern Class**

`Pattern` class là **phiên bản biên dịch của biểu thức chính quy**, dùng để **định nghĩa mẫu (pattern)** và kết hợp với `Matcher` để xử lý chuỗi.

Một số phương thức quan trọng:

#

Method

Mô tả

1

`static Pattern compile(String regex)`

Biên dịch regex và trả về một đối tượng `Pattern`

2

`Matcher matcher(CharSequence input)`

Tạo `Matcher` để so khớp chuỗi đầu vào với pattern

3

`static boolean matches(String regex, CharSequence input)`

Biên dịch regex và kiểm tra dữ liệu đầu vào có khớp không

4

`String[] split(CharSequence input)`

Tách chuỗi theo mẫu regex đã cho

5

`String pattern()`

Trả về chuỗi regex gốc (pattern)

### **3\. Các phương pháp viết Regex**

Có 3 phương pháp tạo regex

*   **Sử dụng** `**Pattern**` **và** `**Matcher**`

**→** Dùng khi cần **xử lý phức tạp** (dùng nhiều lần, thao tác với group, start/end, lặp lại find...).

```java
import java.util.regex.*;

public class App {
    public static void main(String[] args) {
        String text = "Xin chào 2025!";
        String regex = "\\d+"; // tìm số

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            System.out.println("Tìm thấy: " + matcher.group());
        }
    }
}
```

– Kết quả:

```java
Tìm thấy: 2025
```

*   **Sử dụng** `**Pattern.matches()**`

**→** Dùng khi cần **validate dữ liệu** (email, số điện thoại, password...).

```java
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {
        String email = "someone@tayjava.com";
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,6}$";

        boolean isValid = Pattern.matches(regex, email);
        System.out.println("Email hợp lệ? " + isValid);
    }
}
```

– Kết quả:

```java
Email hợp lệ? true
```

*   **Sử dụng** `**String.matches()**`

**→** Dùng khi chỉ cần **check một lần**.

```java
public class App {
    public static void main(String[] args) {
        String phone = "0901234567";
        boolean isValid = phone.matches("\\d{10}"); // đúng 10 số
        System.out.println("Số điện thoại hợp lệ? " + isValid);
    }
}
```

– Kết quả:

```java
Số điện thoại hợp lệ? true
```

*   **Sử dụng** `**String.split()**`

Có thể dùng regex để **tách chuỗi →** cắt chuỗi theo dấu phân cách.

```java
public class App {
    public static void main(String[] args) {
        String text = "Java,Python,C++,Go";
        String[] parts = text.split(",");

        for (String lang : parts) {
            System.out.println(lang);
        }
    }
```

– Kết quả:

```java
Java
Python
C++
Go
```

*   **Dùng** `**String.replaceAll()**` **hoặc** `**replaceFirst()**`

→ Dùng khi cần **xử lý chuỗi đầu vào** (xóa số, bỏ ký tự đặc biệt, chuẩn hóa dữ liệu...).

Thay thế chuỗi dựa trên regex: 

```java
public class App {
    public static void main(String[] args) {
        String text = "Hello 123 World 456";
        String result = text.replaceAll("\\d+", "#");
        System.out.println(result);
    }
}
```

– Kết quả:

```java
Hello # World #
```

*   **Dùng** `**Scanner**` **với** `**Regex**`

`Scanner` cũng hỗ trợ regex để tách dữ liệu từ chuỗi hoặc file.

→ Dùng khi cần **đọc dữ liệu theo mẫu**

```java
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String input = "One 1 Two 2 Three 3";
        Scanner scanner = new Scanner(input);

        while (scanner.hasNext()) {
            if (scanner.hasNext("\\d+")) { // kiểm tra token có phải số không
                System.out.println("Số: " + scanner.next("\\d+")); // lấy đúng token số
            } else {
                scanner.next(); // bỏ qua token không phải số
            }
        }
        scanner.close();
    }
}
```

– Kết quả:

```java
Số: 1
Số: 2
Số: 3
```

#### **🎯 Tổng kết**

Có 6 cách viết và sử dụng Regex trong Java:

1.  **Pattern + Matcher** → mạnh mẽ, linh hoạt.
2.  **Pattern.matches()** → kiểm tra nhanh.
3.  **String.matches()** → đơn giản, gọn.
4.  **String.split()** → tách chuỗi.
5.  **String.replaceAll()/replaceFirst()** → thay thế chuỗi.
6.  **Scanner với regex** → đọc dữ liệu theo mẫu.

👉 Với **Java Regex**, bạn có thể dễ dàng xử lý các tác vụ như:

*   Kiểm tra định dạng email, số điện thoại.
*   Tách chuỗi theo dấu phẩy, khoảng trắng.
*   Trích xuất thông tin từ văn bản.
*   Thay thế, làm sạch dữ liệu đầu vào.

### **4\. Các Loại Pattern trong Java Regex**

Trong Java, `Pattern` (thuộc `java.util.regex`) cho phép mô tả nhiều **mẫu ký tự (pattern)** khác nhau. Nắm vững các loại pattern này sẽ giúp bạn xử lý chuỗi linh hoạt hơn.

**🔹 1. Pattern ký tự cơ bản**

Pattern

Ý nghĩa

Ví dụ khớp

`.`

Bất kỳ ký tự nào (trừ newline `\n`)

`a.b` khớp `acb`, `a1b`

`\d`

Chữ số (0–9)

`\d{3}` khớp `123`

`\D`

Không phải chữ số

`\D+` khớp `abc`

`\w`

Ký tự chữ hoặc số, dấu gạch dưới (`[a-zA-Z0-9_]`)

`\w+` khớp `Java123`

`\W`

Ký tự không phải chữ hoặc số

`\W+` khớp `@#!`

`\s`

Khoảng trắng (space, tab, newline)

`\s+` khớp `" "`

`\S`

Không phải khoảng trắng

`\S+` khớp `Hello`

**🔹 2. Pattern số lượng (Quantifiers)**

Pattern

Ý nghĩa

Ví dụ khớp

`*`

0 hoặc nhiều lần

`a*` khớp `""`, `a`, `aaa`

`+`

1 hoặc nhiều lần

`a+` khớp `a`, `aaa`

`?`

0 hoặc 1 lần

`a?` khớp `""`, `a`

`{n}`

Đúng n lần

`\d{3}` khớp `123`

`{n,}`

Ít nhất n lần

`\d{2,}` khớp `12`, `12345`

`{n,m}`

Từ n đến m lần

`\d{2,4}` khớp `12`, `1234`

**🔹 3. Pattern nhóm và phạm vi**

Pattern

Ý nghĩa

Ví dụ khớp

`[abc]`

Một trong các ký tự a, b hoặc c

`a` hoặc `c`

`[^abc]`

Bất kỳ ký tự nào **không phải** a, b, c

`x`, `z`

`[a-z]`

Phạm vi chữ thường từ a đến z

`j`

`[A-Z]`

Phạm vi chữ hoa từ A đến Z

`K`

`[0-9]`

Phạm vi số

`5`

`(abc)`

Nhóm ký tự

`(ab)+` khớp `abab`

`(?:abc)`

Nhóm nhưng không capture (non-capturing group)

 

**🔹 4. Pattern lựa chọn (Alternation)**

Pattern

Ý nghĩa

Ví dụ khớp

\`a

b\`

Khớp `a` hoặc `b`

\`(cat

dog)\`

Khớp `cat` hoặc `dog`

**🔹 5. Pattern neo (Anchors)**

Pattern

Ý nghĩa

Ví dụ

`^`

Bắt đầu chuỗi

`^Hello` khớp `"Hello world"`

`$`

Kết thúc chuỗi

`world$` khớp `"Hello world"`

`\b`

Biên giới từ (word boundary)

`\bJava\b` khớp `"Java is fun"`

`\B`

Không phải biên giới từ

`\Bend` khớp `"weekend"`

**🔹 6. Pattern nâng cao**

Pattern

Ý nghĩa

`(?=...)`

Lookahead dương (điều kiện phía sau phải đúng)

`(?!...)`

Lookahead âm (điều kiện phía sau sai)

`(?<=...)`

Lookbehind dương (điều kiện phía trước đúng)

`(?<!...)`

Lookbehind âm (điều kiện phía trước sai)

**👉 Ví dụ mẫu:**

```plaintext
import java.util.regex.*;

public class App {
    public static void main(String[] args) {
        System.out.println("metacharacters = \\d"); // \d nghĩa là 1 ký tự số (digit)

        System.out.println(Pattern.matches("\\d", "abc"));   // false -> "abc" không phải là 1 ký tự số
        System.out.println(Pattern.matches("\\d", "1"));     // true  -> "1" là đúng 1 ký tự số
        System.out.println(Pattern.matches("\\d", "4443"));  // false -> nhiều ký tự số (hơn 1)
        System.out.println(Pattern.matches("\\d", "323abc"));// false -> vừa số vừa chữ

        System.out.println("metacharacters = \\D"); // \D nghĩa là 1 ký tự không phải số (non-digit)

        System.out.println(Pattern.matches("\\D", "abc"));   // false -> "abc" có nhiều ký tự
        System.out.println(Pattern.matches("\\D", "1"));     // false -> "1" là ký tự số
        System.out.println(Pattern.matches("\\D", "4443"));  // false -> toàn số
        System.out.println(Pattern.matches("\\D", "323abc"));// false -> lẫn số và chữ
        System.out.println(Pattern.matches("\\D", "m"));     // true  -> "m" là đúng 1 ký tự không phải số

        System.out.println("metacharacters = \\D*"); // \D* nghĩa là 0 hoặc nhiều ký tự không phải số

        System.out.println(Pattern.matches("\\D*", ""));       // true  -> rỗng (0 lần non-digit)
        System.out.println(Pattern.matches("\\D*", "abc"));    // true  -> toàn non-digit
        System.out.println(Pattern.matches("\\D*", "abcXYZ")); // true  -> toàn non-digit
        System.out.println(Pattern.matches("\\D*", "abc123")); // false -> có chứa số
    }
}
```

– Kết quả:

```java
metacharacters = \d
false
true
false
false
metacharacters = \D
false
false
false
false
true
metacharacters = \D*
true
true
true
false
```
