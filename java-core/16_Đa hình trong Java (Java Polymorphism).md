# Đa hình trong Java (Java Polymorphism)

### **1\. Polymorphism là gì ?**

Đa hình trong Java (Java Polymorphism) là một khái niệm mà chúng ta có thể thực hiện một hành động duy nhất theo nhiều cách khác nhau. Đa hình bắt nguồn từ 2 từ tiếng Hy Lạp: poly và morphs. Từ “poly” có nghĩa là nhiều và “morphs” có nghĩa là hình thức. Vì vậy, đa hình có nghĩa là nhiều hình thức.

Có hai loại đa hình trong Java là đa hình trong thời gian biên dịch (**Compile-time polymorphism**) và đa hình thời gian chạy (**Runtime polymorphism**). Chúng ta có thể thực hiện đa hình trong Java bằng cách nạp phương thức nạp chồng (Overloading Method) và phương thức ghi đè (Overriding Method).

### **2\. Method Overloading (Phương thức nạp chồng)**

Nếu một class có nhiều method có cùng tên nhưng khác tham số, thì được gọi là **Method Overloading**

Có 2 cách để tạo ra Method Overloading

*   Thay đổi số lượng arguments
    
*   Thay đổi kiểu dữ liệu
    

– Ví dụ:

```java
public class SampleOverLoading {

    // Thay đổi số lượng arguments
    public int add(int a, int b) {
        return a + b;
    }

    public int add(int a, int b, int c) {
        return a + b + c;
    }

    // Thay đổi kiểu dữ liệu 
    public float add(float a, float b) {
        return a + b;
    }

    public float add(float a, float b, float c) {
        return a + b + c;
    }
}
```

### **3\. Method overriding (Phương thức ghi đè)**

Nếu **Subclass** có cùng method như được khai báo trong **Superclass** thì được gọi là method overriding trong Java.

Nói cách khác, nếu một lớp con cung cấp implements cụ thể của một method đã được định nghĩa trong Parent class thì được gọi là method overriding.

– Ví dụ 1:

```java
// Nếu `Subclass` có cùng method như được khai báo trong `Superclass` thì được gọi là `method overriding` trong Java. 
// Super class
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

// Subclass
public class SampleExtendClass extends SampleAbstractClass {
  @Override // method overriding
  public void sayHello() {
    System.out.println("Hello, Tay Java");
  }

  @Override // method overriding
  public int calculate(int a, int b) {
    return a * b;
  }
}
```

– Ví dụ 2:

```java
// Nói cách khác, nếu một lớp con cung cấp `implements` cụ thể của một method đã được định nghĩa trong `Parent class` thì được gọi là `method overriding`.

// Subclass
public interface SampleInterface {

  // constant fields
  String name = "Tây Java";

  // abstract methods
  void method1();

  int method2();

  String methodN();

  // default method
  default void sayHello() {
    System.out.println("Đây là sample interface");
  }

  // static method
  static String getCurrentTime() {
    return String.valueOf(LocalDate.now());
  }

}

// Parent class
public class SampleMethodOverriding implements SampleInterface {
  @Override
  public void method1() {
    // code here
  }

  @Override
  public int method2() {
    return 0;
  }

  @Override
  public String methodN() {
    return "";
  }

  @Override
  public void sayHello() {
    SampleInterface.super.sayHello(); // super key
  }
}
```

### **4\. Từ khoá quan trọng trọng** `super`**,** `final`

Từ khóa `super` trong Java là một biến tham chiếu được sử dụng để tham chiếu đến đối tượng lớp cha trực tiếp. Khi sử dụng từ khóa `super` ta có thể tham chiếu trực tiếp đến lớp cha và gọi các method hoặc constructor của Lớp cha.

Từ khóa `final` được sử dụng trong java để hạn chế user, nó có thể áp dụng cho các ngữ cảnh với variable, method và class.

![](https://cdn.tayjava.com/production/image/20250908_135305_pasted-1757314384980.png)

*   Một **variable** được chỉ định với từ khóa `final` điều đó có nghĩa là **variable** này không thể thay đổi giá trị.
    
*   Một **method** được chỉ định với từ khóa final điều đó có nghĩa là **method** này không thể overriding.
    
*   Một **class** được chỉ định với từ khóa `final` điều đó có nghĩa là **class** này không thể được extends.
    

– Ví dụ:

```java
// class này không thể được extends
final class FinalClass {
  // code here
}


public class FinalMethod {

  // variable này không thể thay đổi giá trị
  final String keyword = "Final keyword";

  // method này không thể overriding
  final void cannotChange() {

  }
}
```
