# Tính Trừu Tượng Trong Java (Java Abstraction)

### **1\. Abstraction là gì?**

Abstraction là quá trình ẩn đi các chi tiết thực hiện chỉ hiển thị chức năng với những thông tin cần thiết cho người dùng. Người dùng chỉ có thể nhận được kết quả mà không thể biết được quá trình, các bước tạo ra kết quả đó.

Các cách để đạt được Abstraction

*   class abstract (0 đến 100%)
    
*   interface (100%) 
    

### **2\. Abstract class trong Java**

Một class được khai báo bằng từ khóa abstract được gọi là lớp trừu tượng trong Java. Nó có thể có các abstract method(phương thức trừu tượng) và non-abstract(phương thức không trừu tượng). Nó cần được extends và các method của nó cần implements để thực hiện các logic. Abstract class thì không thể khởi tạo.

**Những điểm cần nhớ:**

*   Một lớp trừu tượng phải được khai báo bằng từ khóa abstract.
    
*   Nó có thể có các phương thức trừu tượng(abstract method) và không trừu tượng(none-abstract method).
    
*   Nó cũng có thể có các constructor và phương thức tĩnh(static method).
    
*   Nó có thể có các final method.
    
*   Nó không thể được khởi tạo.
    

![](https://cdn.tayjava.com/production/image/20250908_133311_pasted-1757313189518.png)

– Ví dụ:

```java
// class abstract 
public abstract class SampleAbstractClass {

  // abstract method
  public abstract void sayHello(); // abstract method has not body

  // no abstract method has body
  public int calculate(int a, int b) {
    return a + b;
  }

  // static method
  public static void sayGoodBye() {
    System.out.println("Bye!");
  }

  // constructor
  protected SampleAbstractClass() {
  }

  // final method
  public final void noChangeBody(){
    System.out.println("Lớp con không được thay đổi nội dung của phương thức này vì nó là final method");
  }
}

// class extends từ abstract class
public class SampleExtendClass extends SampleAbstractClass {
  @Override // Ghi đè phương thức abstract method
  public void sayHello() {
    System.out.println("Hello, Tay Java");
  }

  @Override // Ghi đè phương thức non-abstract method
  public int calculate(int a, int b) {
    return a * b;
  }
}
```

### **3\. Sự khác nhau giữa** `abstract class` **và** `interface`

Cả `interface` và `abstract class` đều được sử dụng để thực hiện trừu tượng hóa trong Java, chúng ta có thể định nghĩa các phương thức trừu tượng (abstract method) nhưng không thể khởi tạo interface và abstract class.

**Abstract classInterface**Abstract class có thể có abstract and non-abstract methods.Interface chỉ có abstract method. Từ Java 8 nó có thêm default and static methodsAbstract class không hỗ trợ đa kế thừa.Interface hỗ trợ đa kế thừaAbstract class có các biến final, non-final, static and non-static variables.Interface chỉ có biến static and final variables.Abstract class có thể cung cấp các thực thi của interfaceInterface không thể cung cấp các thực thi abstract class.Từ khoá abstract được dùng để định nghĩa abstract class.Từ khóa interface được dùng để định nghĩa interfaceMôt abstract class có thể extends từ Java class khác và implements nhiều interface khác.Một interface chỉ có thể extends từ các interface khácMột abstract class có thể được mở rộng và thực thi với từ khoá extendsMột interface chỉ có thể được thực thi và triển khai khi sử dụng từ khoá implementsMột abstract class cho phép sử dụng tất cả các từ khóa truy xuất như private, protected và publicMột interface chỉ cho phép sử dụng từ khóa truy xuất là public
