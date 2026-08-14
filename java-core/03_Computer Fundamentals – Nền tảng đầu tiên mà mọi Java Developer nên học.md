# Computer Fundamentals – Nền tảng đầu tiên mà mọi Java Developer nên học

![](https://cdn.tayjava.com/production/image/2026/07/12/78cb852d-1be6-49fb-915a-16eede36e416.png)

> **"Muốn xây một tòa nhà cao tầng, trước tiên phải có một nền móng vững chắc."**

Rất nhiều người khi bắt đầu học Java thường mở ngay IntelliJ IDEA, viết `Hello World`, học biến, vòng lặp hay lập trình hướng đối tượng (OOP). Điều đó không sai, nhưng nếu thiếu kiến thức nền tảng về máy tính, bạn sẽ rất dễ gặp khó khăn khi học đến JVM, Memory, Multithreading hay tối ưu hiệu năng.

Chính vì vậy, **Computer Fundamentals** là bài học đầu tiên trong lộ trình Java dành cho người mới bắt đầu tại TayJava.  
Tại sao Java Developer cần học Computer Fundamentals?

Bạn không cần trở thành kỹ sư phần cứng.

Nhưng bạn cần hiểu:

*   Máy tính hoạt động như thế nào.
    
*   Chương trình Java được thực thi ra sao.
    
*   CPU xử lý công việc gì.
    
*   RAM dùng để làm gì.
    
*   Ổ cứng khác RAM ở điểm nào.
    
*   Vai trò của hệ điều hành.
    
*   Vì sao Java có thể chạy trên nhiều hệ điều hành khác nhau.
    

Khi hiểu được những điều này, bạn sẽ học Java nhanh hơn và ít bị "học thuộc lòng" mà không hiểu bản chất.

##   
Máy tính gồm những thành phần nào?

Một chiếc máy tính thông thường bao gồm bốn thành phần quan trọng:

*   CPU
    
*   RAM
    
*   Ổ cứng (SSD/HDD)
    
*   Hệ điều hành
    

Mỗi thành phần đều có vai trò riêng trong quá trình chạy một chương trình Java.

##   
CPU – Bộ não của máy tính

CPU (Central Processing Unit) là nơi thực hiện mọi phép tính và xử lý các lệnh của chương trình.

Ví dụ:

```java
int a = 10;
int b = 20;

System.out.println(a + b);
```

Đoạn mã trên trông rất đơn giản, nhưng thực tế Java sẽ biên dịch mã nguồn thành Bytecode, JVM sẽ chuyển Bytecode thành Machine Code, sau đó **CPU mới là thành phần trực tiếp thực hiện phép cộng**.

Nói cách khác, mọi câu lệnh Java cuối cùng đều được CPU xử lý.

##   
RAM – Bộ nhớ tạm thời

RAM (Random Access Memory) là nơi lưu trữ dữ liệu khi chương trình đang chạy.

Ví dụ:

```java
String name = "TayJava";
```

Biến `name` sẽ được lưu trong RAM.

Nếu chương trình kết thúc hoặc máy tính bị tắt, dữ liệu trong RAM sẽ biến mất.

Đó là lý do RAM được gọi là **bộ nhớ tạm thời**.

##   
SSD/HDD – Bộ nhớ lưu trữ

Khác với RAM, ổ cứng dùng để lưu dữ liệu lâu dài.

Ví dụ:

*   File Java
    
*   Hình ảnh
    
*   Video
    
*   Database
    
*   Tài liệu
    

Khi bạn lưu file:

```java
HelloWorld.java
```

File này sẽ nằm trên SSD hoặc HDD và vẫn còn sau khi khởi động lại máy tính.

##   
Hệ điều hành (Operating System)

Các hệ điều hành phổ biến hiện nay gồm:

*   Windows
    
*   Linux
    
*   macOS
    

Hệ điều hành đóng vai trò trung gian giữa phần mềm và phần cứng.

Ví dụ, khi chương trình Java muốn đọc một file, JVM không truy cập ổ cứng trực tiếp mà sẽ gửi yêu cầu cho hệ điều hành. Sau đó, hệ điều hành mới thực hiện việc đọc dữ liệu từ ổ cứng và trả kết quả về cho chương trình.

##   
Java chạy như thế nào?

Đây là kiến thức quan trọng nhất mà bất kỳ người mới nào cũng nên hiểu.

```java
Mã nguồn Java (.java)
          │
          ▼
       javac
          │
          ▼
 Bytecode (.class)
          │
          ▼
         JVM
          │
          ▼
    Machine Code
          │
          ▼
         CPU
```

Quá trình này giúp Java có thể chạy trên nhiều hệ điều hành khác nhau mà không cần viết lại chương trình.

Đó cũng chính là lý do Java nổi tiếng với khẩu hiệu:

> **Write Once, Run Anywhere.**

##   
JDK, JRE và JVM khác nhau như thế nào?

Đây là ba khái niệm rất dễ bị nhầm lẫn.

### JDK (Java Development Kit)

Là bộ công cụ dành cho lập trình viên.

Bao gồm:

*   javac
    
*   java
    
*   jar
    
*   javadoc
    
*   JRE
    

Nếu muốn lập trình Java, bạn cần cài đặt JDK.

### JRE (Java Runtime Environment)

Là môi trường dùng để chạy chương trình Java.

Nếu chỉ muốn chạy ứng dụng Java mà không cần lập trình, về lý thuyết chỉ cần JRE. Tuy nhiên, từ Java 11 trở đi, Oracle không còn phát hành JRE riêng lẻ, vì vậy hầu hết chúng ta sẽ cài JDK.

### JVM (Java Virtual Machine)

JVM là máy ảo Java.

Nhiệm vụ của JVM là:

*   Đọc Bytecode.
    
*   Chuyển Bytecode thành mã máy.
    
*   Quản lý bộ nhớ.
    
*   Thu gom rác (Garbage Collection).
    
*   Thực thi chương trình Java.
    

Có thể xem JVM là "trái tim" của hệ sinh thái Java.

##   
Vì sao Java có thể chạy trên Windows, Linux và macOS?

Khi biên dịch, Java không tạo ra mã máy như C/C++.

Thay vào đó, Java tạo ra **Bytecode**.

Mỗi hệ điều hành sẽ có một JVM riêng để chuyển Bytecode thành Machine Code phù hợp với hệ điều hành đó.

Nhờ vậy, cùng một file `.class` có thể chạy trên nhiều nền tảng mà không cần biên dịch lại.

##   
Người mới nên làm gì sau bài học này?

Sau khi hiểu Computer Fundamentals, bạn nên:

*   Cài đặt JDK mới nhất.
    
*   Cài IntelliJ IDEA Community Edition.
    
*   Thiết lập biến môi trường `JAVA_HOME`.
    
*   Kiểm tra bằng lệnh:
    

```bash
java -version
javac -version
```

*   Viết chương trình `HelloWorld.java`.
    
*   Biên dịch bằng `javac`.
    
*   Chạy chương trình bằng lệnh `java`.
    

Đây là bước khởi đầu quan trọng trước khi học Java Core.

##   
Kết luận

Computer Fundamentals không dạy bạn cách viết code, nhưng lại giúp bạn hiểu **vì sao code hoạt động**.

Khi nắm được CPU, RAM, hệ điều hành, JDK, JVM và quy trình thực thi của Java, bạn sẽ có nền tảng vững chắc để học các chủ đề tiếp theo như Java Core, OOP, Collections, Spring Boot hay Microservices.

Đừng vội lao vào những công nghệ phức tạp khi nền móng còn chưa vững. Một Java Developer giỏi không chỉ biết viết code, mà còn hiểu cách máy tính thực sự vận hành.

  
**Trong bài tiếp theo của lộ trình Java dành cho người mới bắt đầu, chúng ta sẽ cùng tìm hiểu về "Java Development Environment" – cách cài đặt JDK, IntelliJ IDEA và viết chương trình Java đầu tiên.**
