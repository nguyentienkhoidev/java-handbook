# Xử Lý Chuỗi Trong Java (Java String)

### **1\. Java String là gì?**

Trong Java, String về cơ bản là một đối tượng biểu diễn chuỗi các giá trị `char`. Một mảng các ký tự hoạt động giống như Java string

```java
char[] ch = {'T', 'â', 'y', 'J', 'a', 'v', 'a'};

String s = new String(ch);
```

→

```java
String s = "TâyJava";
```

Lớp Java String cung cấp nhiều phương thức để thực hiện các để xử lý String như `compare()`, `concat()`, `equals()`, `split()`, `length()`, `replace()`, `compareTo()`, `intern()`, `substring()`, v.v.

**Interface CharSequence** được sử dụng để biểu diễn chuỗi ký tự. Các lớp **String**, **StringBuffer** và **StringBuilder** đều implements nó. Điều đó có nghĩa là chúng ta có thể tạo một string trong Java bằng cách sử dụng ba lớp này.

![](https://cdn.tayjava.com/production/image/20250908_151526_pasted-1757319326111.png)

**Java String** là bất biến nghĩa là không thể thay đổi kích thước của nó. Bất cứ khi nào chúng ta thay đổi bất kỳ string nào đó thì một instance mới sẽ được tạo ra. Nếu bạn muốn sử dụng các chuỗi có thể thay đổi thì có thể dùng **StringBuffer** và **StringBuilder**.

### **2\. Làm thế nào để khởi tạo một Java String?**

#### **a. Sử dụng string literal**

String được tạo ra với dấu `“”`

```java
String s1 = "Welcome to Tay Java";
```

Mỗi lần bạn tạo ra một String, JVM sẽ kiểm tra “string constant pool” trước. Nếu String đã tồn tại trong pool thì một tham chiếu đến instance đó sẽ được tạo ra. Nếu String đó chưa tồn tại thì JVM sẽ tạo string mới.

```java
String s1 = "Welcome to Tay Java";
String s2 = "Welcome to Tay Java"; // JVM không tạo một string mới
```

![](https://cdn.tayjava.com/production/image/20250908_151526_pasted-1757319326112.png)

*   **Tại sao Java sử dụng khái niệm String literal?**
    

Để làm cho Java sử dụng bộ nhớ hiệu quả hơn, Bởi vì không có đối tượng mới nào được tạo nếu nó đã tồn tại trong **Spring constant pool.**

#### **b. Sử dụng từ khóa** `new`

```java
String s1 = new String("Welcome to Tây Java");
```

Trong trường hợp như vậy, JVM sẽ tạo một đối tượng String mới trong bộ nhớ heap bình thường (không phải pool) và giá trị `“Welcome to Tây Java”` theo nghĩa đen sẽ được đặt trong **Spring constant pool**. Biến s sẽ tham chiếu đến đối tượng trong heap (không phải pool).

### **3\. Các method trong class String**

**#MethodMô tả**1char charAt(int index)Trả về giá trị char cho index cụ thể2int length()Trả về độ dài của string3static String format(String format, Object… args)Trả về một String đã được định dạng4static String format(Locale locale, String format, Object… args)Trả về một String đã được định dạng theo ngôn ngữ đã cho5String substring(int beginIndex)Trả về substring từ vị trí index cho trước6String substring(int beginIndex, int endIndex)Trả về substring từ index bắt đầu đến index kết thúc7boolean contains(CharSequence s)Nó trả về giá trị đúng hoặc sai sau khi khớp với string giá trị char8static String join(CharSequence delimiter, CharSequence… elements)Trả về một string đã nối9static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements)Trả về một string đã nối10boolean equals(Object another)Kiểm tra xem string có giống với đối tượng đã cho hay không11boolean isEmpty()Kiểm tra string có bị rỗng không (null hoặc blank) không ?12String concat(String str)Nối string đã chỉ định13String replace(char old, char new)Thay thế tất cả các lần xuất hiện của giá trị char được chỉ định14String replace(CharSequence old, CharSequence new)Thay thế tất cả các lần xuất hiện của CharSequence đã chỉ định15static String equalsIgnoreCase(String another)So sánh với một string khác không kiểm tra chữ hoa chữ thường16String\[\] split(String regex)Trả về một string phân tách khớp với regex17String\[\] split(String regex, int limit)Trả về một string phân tách khớp với regex và limit18String intern()Trả về một string đã được đồng bộ trong String constant pool19int indexOf(int ch)Trả về giá trị char được chỉ định20int indexOf(int ch, int fromIndex)Trả về giá trị char được chỉ định bắt đầu theo index đã cho21int indexOf(String substring)Trả về chỉ mục string con được chỉ định22int indexOf(String substring, int fromIndex)Trả về chỉ mục string con được chỉ định bắt đầu theo index đã cho23String toLowerCase()Trả về một string thường24String toLowerCase(Locale locale)Trả về một string thường theo ngôn ngữ được chỉ định25String toUpperCase()Trả về một STRING IN HOA26String toUpperCase(Locale locale)Trả về một STRING IN HOA theo ngôn ngữ được chỉ định27String trim()Loại bỏ khoảng trắng ở trước và sau String28static String valueOf(int value)Chuyển đổi kiểu từ dữ liệu đã cho thành string. Đây là overloading method
