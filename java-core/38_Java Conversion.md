# Java Conversion

**Java Conversion** là quá trình **chuyển đổi dữ liệu từ kiểu này sang kiểu khác** trong Java. Đây là một phần quan trọng vì khi lập trình, ta thường phải:

*   Ép kiểu số từ nhỏ sang lớn (int → long) hoặc ngược lại (double → int).
*   Chuyển đổi giữa **kiểu dữ liệu nguyên thủy (primitive)** và **đối tượng (wrapper class)**.
*   Chuyển đổi giữa **String** và các kiểu dữ liệu khác.

### **1\. Type Casting (Ép kiểu dữ liệu)**

*   **Widening (Implicit Conversion)**:  
    Tự động chuyển kiểu nhỏ sang kiểu lớn.

```java
int a = 10;
double b = a; // int → double (tự động)
```

*   **Narrowing (Explicit Conversion)**:  
    Cần ép kiểu thủ công khi chuyển kiểu lớn sang nhỏ.

```java
double x = 9.78;
int y = (int) x; // double → int (mất phần thập phân)
```

### **2\. Autoboxing & Unboxing**

*   **Autoboxing**: chuyển primitive thành wrapper.
*   **Unboxing**: chuyển wrapper về primitive.

```java
int a = 5;
Integer obj = a;   // Autoboxing (int → Integer)
int b = obj;       // Unboxing (Integer → int)
```

### **3\. String Conversion**

*   **Chuyển kiểu dữ liệu → String:**

```java
int num = 100;
String str1 = String.valueOf(num);
String str2 = Integer.toString(num);
```

*   **Chuyển String → kiểu dữ liệu:**

```java
String s = "200";
int i = Integer.parseInt(s);   // "200" → 200
double d = Double.parseDouble("3.14");
```

### **4\. Object Conversion**

Trong OOP, có thể chuyển đổi giữa **kiểu cha (superclass)** và **kiểu con (subclass)**:

*   **Upcasting** (ngầm định): Subclass → Superclass.
*   **Downcasting** (tường minh): Superclass → Subclass.

```java
class Animal {}
class Dog extends Animal {}
Animal a = new Dog();     // Upcasting
Dog d = (Dog) a;          // Downcasting
```
