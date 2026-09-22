# Lesson 15: Xử lý Chuỗi, Wrapper Class, Iterable và Xử lý Ngoại Lệ (Exception)

Bài học này tổng hợp các kiến thức cực kỳ quan trọng và thường xuyên xuất hiện khi đi phỏng vấn Java Core. Chúng ta sẽ đi sâu vào bản chất của xử lý chuỗi (`String`, `StringBuilder`), cách cấu trúc Collection làm việc với `Wrapper Class` và `Iterable`, cũng như làm chủ hoàn toàn cơ chế Xử lý ngoại lệ (`Exception Handling`).

## Mục lục
- [1. String trong Java](#1-string-trong-java)
- [2. StringBuilder và StringBuffer](#2-stringbuilder-và-stringbuffer)
- [3. Iterable Interface và Iterator](#3-iterable-interface-và-iterator)
- [4. StringJoiner (Từ Java 8)](#4-stringjoiner-từ-java-8)
- [5. Wrapper Class, Boxing và Unboxing](#5-wrapper-class-boxing-và-unboxing)
- [6. Xử lý Ngoại lệ (Exception Handling): try-catch-finally](#6-xử-lý-ngoại-lệ-exception-handling-try-catch-finally)
  - [6.1. Exception (Ngoại lệ) là gì? (Checked vs Unchecked)](#61-exception-ngoại-lệ-là-gì)
  - [6.2. Cấu trúc try-catch-finally](#62-cấu-trúc-try-catch-finally)
  - [6.3. Chủ động ném ngoại lệ với throw và throws](#63-chủ-động-ném-ngoại-lệ-với-throw-và-throws)
  - [6.4. Câu hỏi phỏng vấn "Kinh Điển": Phân biệt final, finally, và finalize](#64-câu-hỏi-phỏng-vấn-kinh-điển-phân-biệt-final-finally-và-finalize)
  - [6.5. Nâng cao: Khối lệnh Try-with-resources (Từ Java 7)](#65-nâng-cao-khối-lệnh-try-with-resources-từ-java-7)
  - [6.6. Nâng cao: Custom Exception (Tự định nghĩa Ngoại lệ)](#66-nâng-cao-custom-exception-tự-định-nghĩa-ngoại-lệ)

---

## 1. String trong Java

Trong Java, `String` không phải là kiểu dữ liệu nguyên thủy (primitive type) như `int` hay `char`, mà nó là một **Đối tượng (Object)** được định nghĩa bởi class `java.lang.String`.

### Đặc điểm quan trọng nhất: Tính Bất Biến (Immutable)
- Khi một đối tượng String được tạo ra, nội dung của nó **không thể bị thay đổi**.
- Bất kỳ thao tác nào tưởng chừng như thay đổi chuỗi (nối chuỗi, cắt chuỗi, viết hoa...) thực chất đều **tạo ra một đối tượng String hoàn toàn mới** trong bộ nhớ.

```java
String s = "Hello";
s.concat(" World"); // Tạo ra đối tượng mới "Hello World" nhưng không ai hứng lấy nó
System.out.println(s); // Vẫn in ra "Hello"

// Cách dùng đúng:
s = s.concat(" World"); // Biến s trỏ tới đối tượng mới "Hello World"
```

### Cơ chế String Pool (Hồ chứa chuỗi)
Để tiết kiệm bộ nhớ, Java có một khu vực đặc biệt trong Heap Memory gọi là **String Pool**.
- **Tạo bằng literal (dấu ngoặc kép):** `String s1 = "Java";`
  Java sẽ kiểm tra trong String Pool xem chữ "Java" đã có chưa. Nếu chưa, nó tạo mới. Nếu có rồi, nó trả về tham chiếu trỏ tới đối tượng cũ.
- **Tạo bằng từ khóa `new`:** `String s2 = new String("Java");`
  Java sẽ BẮT BUỘC tạo một đối tượng hoàn toàn mới nằm ở vùng nhớ Heap (bên ngoài String Pool), bất chấp trong Pool đã có hay chưa.

*(Lời khuyên: Luôn khởi tạo String bằng dấu `""`, hạn chế dùng từ khóa `new`)*

### Các phương thức String phổ biến (với Code Example):

Dưới đây là các hàm cực kỳ hay dùng khi làm việc với String trong Java:

```java
String text = "  Hello Java World!  ";

// 1. length(): Lấy độ dài chuỗi
int len = text.length(); // 21

// 2. trim(): Xóa khoảng trắng thừa ở 2 đầu chuỗi
String trimmed = text.trim(); // "Hello Java World!"

// 3. charAt(int index): Lấy ký tự tại một vị trí
char c = trimmed.charAt(0); // 'H'

// 4. substring(int beginIndex, int endIndex): Cắt chuỗi con
// (Lấy từ beginIndex đến sát endIndex - 1)
String sub = trimmed.substring(6, 10); // "Java"

// 5. toLowerCase() / toUpperCase(): Chuyển đổi chữ hoa/thường
String upper = trimmed.toUpperCase(); // "HELLO JAVA WORLD!"

// 6. contains(CharSequence s): Kiểm tra chuỗi có chứa chuỗi con không
boolean hasJava = trimmed.contains("Java"); // true

// 7. equals(Object o) & equalsIgnoreCase(String s): So sánh chuỗi
// LƯU Ý QUAN TRỌNG: Tuyệt đối dùng equals() để so sánh nội dung, KHÔNG DÙNG "=="
boolean isEqual = trimmed.equals("hello java world!"); // false
boolean isEqualIgnore = trimmed.equalsIgnoreCase("hello java world!"); // true

// 8. startsWith(String prefix) / endsWith(String suffix): Kiểm tra bắt đầu/kết thúc
boolean starts = trimmed.startsWith("Hello"); // true

// 9. replace(CharSequence target, CharSequence replacement): Thay thế chuỗi
String replaced = trimmed.replace("Java", "Python"); // "Hello Python World!"

// 10. split(String regex): Tách chuỗi thành mảng dựa trên dấu phân cách
String data = "apple,banana,orange";
String[] fruits = data.split(","); // ["apple", "banana", "orange"]

// 11. indexOf(String str): Tìm vị trí xuất hiện đầu tiên của chuỗi con
int index = trimmed.indexOf("Java"); // 6 (nếu không thấy trả về -1)

// 12. isEmpty() / isBlank() (từ Java 11): Kiểm tra chuỗi rỗng
boolean empty = "".isEmpty(); // true (độ dài = 0)
boolean blank = "   ".isBlank(); // true (chỉ chứa khoảng trắng)
```

---

## 2. StringBuilder và StringBuffer

### Tại sao KHÔNG NÊN dùng toán tử cộng `+` để nối chuỗi?

Theo thói quen, chúng ta thường nối chuỗi bằng toán tử `+`:
```java
String s = "Java";
s = s + " Core"; 
```
Tuy nhiên, vì **String là bất biến (Immutable)**, ở dòng code thứ 2, Java không thực sự cộng thêm chữ " Core" vào vùng nhớ của chữ "Java". Thay vào đó, tiến trình phía sau diễn ra như sau:
1. Java tạo một đối tượng String mới hoàn toàn có giá trị là `"Java Core"`.
2. Gán biến `s` trỏ vào đối tượng mới này.
3. Bỏ rơi đối tượng `"Java"` ban đầu (trở thành rác chờ Garbage Collector dọn dẹp).

**Thảm họa hiệu năng khi dùng `+` trong vòng lặp:**
```java
String result = "";
for (int i = 0; i < 10000; i++) {
    result = result + i; // CỰC KỲ TỆ!
}
```
Đoạn code trên sẽ tạo ra **10,000 đối tượng String mới** trong bộ nhớ và vứt bỏ đi 9,999 đối tượng cũ. Việc liên tục cấp phát và dọn dẹp bộ nhớ như vậy sẽ làm ứng dụng bị **ngốn RAM, chạy chậm và giảm hiệu suất trầm trọng**.

Để giải quyết bài toán cần thay đổi/nối chuỗi liên tục một cách tối ưu, Java cung cấp hai class là `StringBuilder` và `StringBuffer`.

### Đặc điểm chung:
- Cả 2 đều là **Mutable (Có thể thay đổi)**.
- Khi gọi các hàm thêm, sửa, xóa, chúng sẽ thao tác **trực tiếp trên chính vùng nhớ của đối tượng đó** mà không tạo ra đối tượng mới.

### Sự khác biệt giữa StringBuilder và StringBuffer:

1.  **StringBuffer (Ra đời từ Java 1.0):**
    - Là **Thread-safe (An toàn luồng)**: Các phương thức của nó được đánh dấu `synchronized`.
    - Nghĩa là trong môi trường đa luồng (multi-threading), nhiều luồng không thể thao tác trên cùng một đối tượng StringBuffer cùng lúc (luồng này phải đợi luồng kia làm xong).
    - **Nhược điểm:** Do cơ chế khóa (lock) này nên nó chạy **rất chậm**.

2.  **StringBuilder (Ra đời từ Java 1.5):**
    - Là **Không Thread-safe (Không an toàn luồng)**: Không có cơ chế đồng bộ hóa.
    - **Ưu điểm:** Chạy **cực kỳ nhanh**. 

> **Khi nào dùng cái gì?**
> - Dùng **`String`**: Khi dữ liệu chuỗi ít thay đổi (ví dụ: tên, email, hằng số).
> - Dùng **`StringBuilder`**: Khi cần thao tác nối/cắt chuỗi nhiều và làm việc trong môi trường đơn luồng (Single-thread). Đây là class được **sử dụng nhiều nhất** để thay thế String khi thao tác chuỗi.
> - Dùng **`StringBuffer`**: Chỉ dùng khi ứng dụng là đa luồng (Multi-thread) và nhiều luồng cùng chọc vào thay đổi chung một biến chuỗi.

### Các phương thức phổ biến (có ở cả Builder và Buffer):
```java
StringBuilder sb = new StringBuilder("Hello");

// 1. Nối thêm vào cuối (Rất nhanh)
sb.append(" World"); // "Hello World"
sb.append(2024);     // "Hello World2024"

// 2. Chèn vào vị trí bất kỳ
sb.insert(5, " Java"); // "Hello Java World2024"

// 3. Xóa đoạn chuỗi
sb.delete(5, 10); 

// 4. Đảo ngược chuỗi (Thường dùng trong thuật toán)
sb.reverse();

// 5. Chuyển ngược lại thành String (Bắt buộc phải làm khi muốn gán vào biến String)
String finalString = sb.toString();
```

---

## 3. Iterable Interface và Iterator

Trong bài 14, chúng ta đã học về `ArrayList`. Làm thế nào mà chúng ta có thể dùng vòng lặp `for-each` để duyệt qua từng phần tử của ArrayList một cách dễ dàng như vậy? Câu trả lời nằm ở `Iterable`.

### Khái niệm:
- **`Iterable`** là interface nằm ở trên cùng (Root Interface) của cây phả hệ Java Collection Framework (Nó là cha của `Collection`, do đó là ông nội của `List`, `Set`, `Queue`).
- **Luật:** Bất kỳ class nào implements interface `Iterable`, class đó đều **cung cấp khả năng được duyệt qua (iterate)** và có thể **được sử dụng trực tiếp trong vòng lặp `for-each`**.

### Iterator là gì?
Interface `Iterable` chỉ bắt buộc class con triển khai một phương thức duy nhất:
```java
public Iterator<T> iterator();
```
Nó trả về một đối tượng **`Iterator`**. `Iterator` giống như một "con trỏ" (cursor) giúp bạn đi qua từng phần tử của cấu trúc dữ liệu từ đầu đến cuối một chiều.

Các hàm cốt lõi của `Iterator`:
1.  `hasNext()`: Trả về `true` nếu vẫn còn phần tử tiếp theo phía trước.
2.  `next()`: Trả về phần tử tiếp theo và dịch con trỏ tiến lên một bước.
3.  `remove()`: Xóa phần tử vừa được trả về bởi `next()`.

### Các cách duyệt một Collection (như ArrayList):

```java
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");

        // CÁCH 1: Dùng vòng lặp for truyền thống (Dùng Index)
        // Nhược điểm: Chỉ dùng được cho cấu trúc có Index như List, không dùng được cho Set.
        for (int i = 0; i < fruits.size(); i++) {
            System.out.println(fruits.get(i));
        }

        // CÁCH 2: Dùng for-each (Cú pháp rút gọn, bản chất bên dưới nó dùng CÁCH 3)
        // Ưu điểm: Cú pháp ngắn gọn, dễ đọc.
        // Nhược điểm: KHÔNG THỂ XÓA phần tử trong lúc đang duyệt (Sẽ bị lỗi ConcurrentModificationException).
        for (String fruit : fruits) {
            System.out.println(fruit);
        }

        // CÁCH 3: Dùng Iterator trực tiếp
        // Ưu điểm: LÀ CÁCH DUY NHẤT AN TOÀN NẾU MUỐN VỪA DUYỆT VỪA XÓA PHẦN TỬ.
        Iterator<String> iterator = fruits.iterator();
        while (iterator.hasNext()) {
            String fruit = iterator.next();
            if (fruit.equals("Banana")) {
                iterator.remove(); // Xóa an toàn trong lúc duyệt
            } else {
                System.out.println(fruit);
            }
        }
    }
}
```

---

## 4. StringJoiner (Từ Java 8)

Khi bạn muốn nối một danh sách các chuỗi lại với nhau và ngăn cách chúng bởi một ký tự (ví dụ: dấu phẩy `,`), nếu dùng vòng lặp với `StringBuilder` bạn sẽ phải tự viết logic xử lý việc không in dấu phẩy ở phần tử cuối cùng. `StringJoiner` ra đời (từ Java 8) để giải quyết việc này cực kỳ gọn gàng.

```java
import java.util.StringJoiner;

public class Main {
    public static void main(String[] args) {
        // Khởi tạo StringJoiner
        // Cú pháp: StringJoiner(delimiter, prefix, suffix)
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        
        sj.add("Apple");
        sj.add("Banana");
        sj.add("Orange");
        
        System.out.println(sj.toString()); 
        // Kết quả: [Apple, Banana, Orange]
        // (Rất tiện lợi khi định dạng chuỗi JSON hoặc mảng!)
    }
}
```

---

## 5. Wrapper Class, Boxing và Unboxing

Trong Java, bên cạnh 8 kiểu dữ liệu nguyên thủy (Primitive type: `int`, `double`, `boolean`, `char`...), chúng ta có các cấu trúc dữ liệu thuộc Java Collection (như `ArrayList`) **CHỈ chấp nhận làm việc với Đối tượng (Object)**.

Bạn KHÔNG THỂ viết `ArrayList<int>`. Để giải quyết điều này, Java cung cấp các **Wrapper Class** (Lớp bao bọc) tương ứng để "bọc" các kiểu nguyên thủy thành đối tượng:
- `int` -> `Integer`
- `double` -> `Double`
- `char` -> `Character`
- `boolean` -> `Boolean`
- ...

### Boxing và Unboxing là gì?

- **Boxing (Đóng hộp):** Quá trình chuyển đổi từ kiểu nguyên thủy sang Wrapper Class tương ứng.
- **Unboxing (Mở hộp):** Quá trình chuyển đổi ngược lại từ Wrapper Class sang kiểu nguyên thủy.
- **Autoboxing / Auto-unboxing:** Từ Java 5 trở đi, trình biên dịch tự động làm việc đóng/mở hộp này mà bạn không cần phải gọi hàm gán thủ công.

```java
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // 1. Autoboxing (Tự động đóng hộp)
        // int nguyên thủy 10 tự động biến thành đối tượng Integer
        Integer a = 10; 
        
        // (Hoạt động ngầm phía sau: Integer a = Integer.valueOf(10);)
        
        // 2. Auto-unboxing (Tự động mở hộp)
        // Đối tượng Integer 'a' tự động lấy giá trị lõi ra gán cho biến int 'b'
        int b = a; 
        
        // (Hoạt động ngầm phía sau: int b = a.intValue();)

        // 3. Ứng dụng phổ biến trong ArrayList:
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(5); // Autoboxing: int 5 -> Integer
        int firstNum = numbers.get(0); // Auto-unboxing: Integer -> int
    }
}
```

> **Lưu ý hiệu năng:** Mặc dù Autoboxing/Unboxing diễn ra tự động và rất tiện lợi, nhưng việc biến đổi qua lại giữa kiểu nguyên thủy và Object liên tục trong các vòng lặp hàng triệu phần tử sẽ gây chậm chương trình và sinh ra nhiều rác bộ nhớ. Hãy ưu tiên kiểu nguyên thủy khi phải tính toán toán học số lượng cực lớn.

### So sánh Wrapper Class và Kiểu nguyên thủy (Primitive Type)

Khi nào nên dùng kiểu nguyên thủy, khi nào nên dùng Wrapper Class? Dưới đây là bảng so sánh chi tiết:

| Tiêu chí | Kiểu nguyên thủy (int, double, boolean...) | Wrapper Class (Integer, Double, Boolean...) |
| :--- | :--- | :--- |
| **Bản chất** | Là giá trị đơn thuần, lưu trực tiếp trên bộ nhớ Stack. | Là một Đối tượng (Object), lưu trên bộ nhớ Heap (tham chiếu lưu ở Stack). |
| **Tốc độ & Hiệu năng** | **Cực kỳ nhanh** và tốn rất ít bộ nhớ. | **Chậm hơn** do tốn chi phí khởi tạo object và dọn rác (Garbage Collector). |
| **Giá trị `null`** | **Không thể** chứa giá trị `null`. Trả về giá trị mặc định (vd `int` mặc định là `0`). | **Có thể** chứa giá trị `null` (Rất quan trọng khi làm việc với Database để phân biệt số 0 và việc người dùng chưa nhập gì). |
| **Collection (ArrayList..)**| **Không** sử dụng được trong Collection Framework. | **Bắt buộc** dùng trong Collection Framework (`ArrayList<Integer>`). |
| **Phương thức tiện ích** | Không có phương thức nào đi kèm. | Cung cấp rất nhiều hàm hỗ trợ (ví dụ: `Integer.parseInt("123")`, `Integer.max(a, b)`). |

---

## 6. Xử lý Ngoại lệ (Exception Handling): try-catch-finally

Trong quá trình chạy chương trình, đôi khi sẽ xảy ra những lỗi bất ngờ (ví dụ: chia cho 0, mở một file không tồn tại, kết nối database bị đứt). Nếu không xử lý, chương trình sẽ bị "chết" (crash) ngay lập tức. Cơ chế **Exception Handling** giúp chúng ta bắt các lỗi này và cho phép chương trình tiếp tục chạy.

### 6.1. Exception (Ngoại lệ) là gì?
- **Error (Lỗi hệ thống):** Các lỗi nghiêm trọng từ môi trường máy ảo Java (JVM) như tràn bộ nhớ (`OutOfMemoryError`). Chúng ta **không thể** và không nên cố gắng bắt các lỗi này bằng code.
- **Exception (Ngoại lệ):** Lỗi xảy ra do logic code hoặc do tác động bên ngoài mà ứng dụng **có thể bắt và xử lý được**. 

**Phân biệt 2 loại Exception (Checked vs Unchecked):**
Đây là kiến thức cực kỳ quan trọng thường xuyên xuất hiện khi đi làm và phỏng vấn:

| Tiêu chí | Checked Exception | Unchecked Exception (Runtime Exception) |
| :--- | :--- | :--- |
| **Bản chất** | Ngoại lệ mà **Trình biên dịch ép buộc bạn phải xử lý** ngay lúc viết code. Nếu không bọc `try-catch` hoặc `throws`, code sẽ báo lỗi đỏ không cho biên dịch. | Ngoại lệ xảy ra trong lúc **ứng dụng đang chạy (Runtime)**. Trình biên dịch không ép buộc bạn phải xử lý. |
| **Kế thừa từ** | Kế thừa trực tiếp từ class `Exception` (nhưng KHÔNG kế thừa từ `RuntimeException`). | Kế thừa từ class `RuntimeException` (Một class con của Exception). |
| **Nguyên nhân**| Thường do khách quan bên ngoài tầm kiểm soát: đứt cáp mạng, file bị xóa, database sập... | Thường do **LỖI CẨU THẢ của lập trình viên**: quên check null, chia cho 0, gọi index ngoài mảng... |
| **Ví dụ** | `IOException` (Lỗi đọc/ghi file)<br>`SQLException` (Lỗi kết nối database)<br>`ClassNotFoundException` | `NullPointerException` (Lỗi gây crash nhiều nhất Java)<br>`ArithmeticException` (Chia cho 0)<br>`ArrayIndexOutOfBoundsException` |
| **Cách xử lý** | **BẮT BUỘC** phải dùng `try-catch` để khắc phục, hoặc dùng `throws` để đẩy lỗi đi. | Tốt nhất là **SỬA LẠI LOGIC CODE** (vd: thêm `if (obj != null)`) thay vì nhắm mắt bọc `try-catch`. |

### 6.2. Cấu trúc try-catch-finally

- **`try`**: Chứa đoạn code có *nguy cơ* sinh ra lỗi.
- **`catch`**: Nếu code trong `try` bị lỗi, luồng chạy sẽ nhảy ngay vào `catch` tương ứng để xử lý. Chương trình KHÔNG bị crash.
- **`finally`**: Khối lệnh **luôn luôn được chạy**, bất kể có xảy ra lỗi hay không (hoặc thậm chí có lệnh `return` ở trên). Thường dùng để dọn dẹp tài nguyên (đóng file, đóng kết nối database).

```java
public class Main {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        
        try {
            // Cố tình truy cập phần tử ngoài mảng để gây lỗi
            System.out.println("Đang xử lý...");
            int x = arr[5]; // Lỗi ArrayIndexOutOfBoundsException xảy ra tại đây
            System.out.println("Dòng này sẽ KHÔNG bao giờ được in ra");
            
        } catch (ArrayIndexOutOfBoundsException e) {
            // Bắt đúng loại lỗi và xử lý
            System.out.println("Lỗi: Bạn đã truy cập quá giới hạn của mảng!");
            System.out.println("Chi tiết lỗi: " + e.getMessage());
            
        } catch (Exception e) {
            // Catch Exception chung chung luôn để ở cuối cùng (như một mẻ lưới dự phòng)
            System.out.println("Lỗi không xác định: " + e.toString());
            
        } finally {
            // Luôn luôn chạy
            System.out.println("Khối finally luôn được thực thi. Giải phóng tài nguyên...");
        }
        
        System.out.println("Chương trình vẫn chạy tiếp tục xuống đây...");
    }
}
```

> **QUY TẮC QUAN TRỌNG VỀ THỨ TỰ BẮT LỖI (Catch Order):**
> Khi bạn có nhiều khối `catch` liên tiếp nhau, bạn **BẮT BUỘC** phải đặt các lỗi cụ thể (Lớp con - Subclass) lên trên, và lỗi chung chung nhất (`Exception` - Lớp cha) ở dưới cùng.
> - **Lý do:** Các khối `catch` hoạt động như một cái phễu lọc từ trên xuống dưới. Nếu bạn đặt `catch (Exception e)` lên trên cùng, nó sẽ "hứng" trọn toàn bộ mọi loại lỗi, khiến cho các khối `catch` cụ thể bên dưới nó vĩnh viễn không bao giờ có cơ hội được chạy (Unreachable code). Trình biên dịch Java (Compiler) rất thông minh và sẽ báo lỗi đỏ ngay lập tức nếu bạn xếp sai thứ tự này.

### 6.3. Chủ động ném ngoại lệ với `throw` và `throws`

Ngoài việc thụ động bắt lỗi bằng `try-catch`, đôi khi trong logic nghiệp vụ (business logic), chúng ta muốn **chủ động tạo ra một lỗi** (ví dụ: tuổi nhập vào không được là số âm) và "đẩy" (ném) trách nhiệm xử lý lỗi đó ra bên ngoài cho nơi khác gọi hàm.

1. **Từ khóa `throw` (Ném một ngoại lệ cụ thể):**
   - Được dùng **bên trong** thân hàm.
   - Theo sau nó BẮT BUỘC phải là một **đối tượng Exception** (thường khởi tạo bằng từ khóa `new`).
   - Ngay khi chương trình chạy đến lệnh `throw`, hàm sẽ lập tức dừng lại (giống như lệnh `return`).

2. **Từ khóa `throws` (Khai báo ngoại lệ):**
   - Được dùng ở **chữ ký của hàm** (nằm sau danh sách tham số).
   - Có tác dụng cảnh báo cho bất kỳ hàm nào gọi hàm này rằng: *"Hàm này có khả năng ném ra lỗi đấy, nếu bạn gọi nó thì hãy tự chuẩn bị sẵn `try-catch` để hứng đi nhé!"*.

**Ví dụ kết hợp:**
```java
public class Main {
    // Dùng 'throws' để cảnh báo hàm này có nguy cơ văng ra lỗi Exception
    public static void checkAge(int age) throws Exception {
        if (age < 0) {
            // Dùng 'throw' để chủ động bóp cò, ném ra một đối tượng lỗi
            throw new Exception("Tuổi không hợp lệ! Tuổi phải >= 0.");
        }
        System.out.println("Tuổi hợp lệ: " + age);
    }

    public static void main(String[] args) {
        // Nơi gọi hàm checkAge bắt buộc phải bọc nó trong try-catch (vì hàm kia có 'throws')
        try {
            checkAge(-5); 
        } catch (Exception e) {
            System.out.println("Bắt được lỗi do hàm checkAge ném ra: " + e.getMessage());
        }
    }
}
```

### 6.4. Câu hỏi phỏng vấn "Kinh Điển": Phân biệt final, finally, và finalize

Đây là 3 khái niệm trông tên thì giống nhau nhưng mục đích hoàn toàn khác biệt:

1. **`final` (Từ khóa - Keyword):** Dùng để đánh dấu sự "bất biến".
   - Biến `final`: Trở thành hằng số (không thể đổi giá trị).
   - Phương thức `final`: Lớp con không thể Ghi đè (Override).
   - Lớp `final`: Không cho phép lớp khác kế thừa (extends).
2. **`finally` (Khối lệnh - Block):** Đi kèm với `try-catch`, chứa đoạn code luôn luôn phải chạy để dọn dẹp tài nguyên (đóng kết nối mạng, đóng file).
3. **`finalize()` (Phương thức - Method):** (Đã bị Deprecated từ Java 9) Là một hàm thuộc lớp `Object`. Được Garbage Collector gọi ngay trước khi nó tiêu hủy một object để thu hồi bộ nhớ. Trong code thực tế hiện nay chúng ta không bao giờ nên lạm dụng hàm này.

### 6.5. Nâng cao: Khối lệnh Try-with-resources (Từ Java 7)

Trước Java 7, để đảm bảo việc đọc/ghi file hoặc kết nối database không bị rò rỉ tài nguyên, chúng ta BẮT BUỘC phải đóng chúng lại (gọi hàm `close()`) bên trong khối `finally`. Điều này làm code trở nên rất dài dòng vì bản thân hàm `close()` đôi khi cũng văng ra Exception, dẫn đến việc phải viết `try-catch` lồng nhau vô cùng rối mắt.

**Try-with-resources** ra đời để giải quyết triệt để vấn đề này. Mọi tài nguyên khởi tạo trong cặp ngoặc tròn `try (...)` sẽ được **tự động đóng lại (auto-closed)** ngay sau khi khối `try` kết thúc, bất kể có xảy ra lỗi hay không. Bạn sẽ giải thoát hoàn toàn khỏi khối `finally`.

*(Điều kiện: Class của tài nguyên đó phải `implements AutoCloseable`).*

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Tài nguyên BufferedReader được khai báo ngay bên trong try(...)
        // Hệ thống sẽ tự động gọi br.close() khi vòng try kết thúc. Không cần viết finally!
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            
            String line = br.readLine();
            System.out.println(line);
            
        } catch (IOException e) {
            System.out.println("Lỗi đọc file: " + e.getMessage());
        }
    }
}
```

### 6.6. Nâng cao: Custom Exception (Tự định nghĩa Ngoại lệ)

Trong các dự án đi làm thực tế, người ta rất hạn chế việc ném ra các Exception chung chung có sẵn của Java (như `RuntimeException`, `Exception`). Thay vào đó, để code rõ ràng và mang đậm tính nghiệp vụ (Business Logic), các team sẽ tự thiết kế ra các class Exception của riêng mình (ví dụ `UserNotFoundException`, `EmailAlreadyExistsException`).

Cách làm cực kỳ đơn giản: Chỉ cần tạo một class mới và cho nó **`extends Exception`** (nếu muốn tạo Checked Exception) hoặc **`extends RuntimeException`** (nếu muốn tạo Unchecked Exception).

```java
// 1. Tự định nghĩa một Ngoại lệ riêng cho nghiệp vụ Ngân hàng
class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message); // Gọi constructor của lớp cha để lưu câu báo lỗi
    }
}

// 2. Sử dụng class lỗi vừa tạo
public class BankAccount {
    private double balance = 1000;

    public void withdraw(double amount) {
        if (amount > balance) {
            // Chủ động ném ra lỗi nghiệp vụ mang tên cực kỳ dễ hiểu
            throw new InsufficientBalanceException("Số dư không đủ để rút " + amount);
        }
        balance -= amount;
        System.out.println("Rút tiền thành công!");
    }

    public static void main(String[] args) {
        BankAccount myAcc = new BankAccount();
        try {
            myAcc.withdraw(5000); // Cố tình rút số tiền lớn hơn số dư
        } catch (InsufficientBalanceException e) {
            System.out.println("Giao dịch thất bại: " + e.getMessage());
        }
    }
}
```

### Tổng kết:
- Xử lý chuỗi đơn giản, ít thay đổi -> Dùng `String`.
- Nối, cắt, sửa chuỗi liên tục -> Dùng `StringBuilder`.
- Nối mảng chuỗi với dấu phân cách -> Dùng `StringJoiner`.
- Muốn tự tạo Class có thể dùng vòng lặp `for-each` để duyệt -> `implements Iterable`.
- Muốn vừa lặp vừa xóa phần tử -> Dùng `Iterator`.
- Collection chỉ chơi với Object, nên phải dùng **Wrapper Class** (Autoboxing/Unboxing xử lý việc chuyển đổi ngầm).
