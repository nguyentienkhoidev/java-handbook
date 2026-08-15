# Tuyển Tập 10 Câu Hỏi Phỏng Vấn Về Lập Trình Hướng Đối Tượng (OOP) Trong Java – Từ Cơ Bản Đến Nâng Cao

![OOP interview.jpeg](../images/e4b456b2-21e3-4a33-9681-26c0c70024c5.jpeg)

Lập trình Hướng Đối Tượng (OOP) là nền tảng quan trọng trong Java. Dưới đây là 10 câu hỏi thường gặp, được sắp xếp từ mức độ dễ đến khó, kèm theo phần giải thích chi tiết.

### **1\. OOP là gì? Các đặc tính của OOP trong Java?**

📌 Trả lời:

**_OOP (Object-Oriented Programming):_** _mô hình lập trình dựa trên object và class._

**_OOP bao gồm 4 đặc tính chính:_**

*   **_Encapsulation (Đóng gói):_** _Che giấu dữ liệu, dùng getter/setter._
    
*   **_Inheritance (Kế thừa):_** _Tái sử dụng code từ class cha._
    
*   **_Polymorphism (Đa hình):_** _Hành động giống nhau nhưng cách thực hiện khác nhau (overloading, overriding)._
    
*   **_Abstraction (Trừu tượng):_** _Ẩn chi tiết cài đặt, chỉ lộ interface/abstract method._
    

## **2\. Sự khác nhau giữa Abstract Class và Interface?**

**📌 Trả lời:**

*   **_Abstract Class:_**
    
    *   _Có thể chứa cả abstract method và method thường._
        
    *   _Có constructor, biến instance._
        
    *   _Chỉ kế thừa 1 class cha._
        
*   **_Interface:_**
    
    *   _Chỉ chứa khai báo hành vi (từ Java 8 có default & static method)._
        
    *   _Không có constructor._
        
    *   _Một class có thể implements nhiều interface._
        

_👉_ **_Khi nào dùng?_**

*   _Abstract class để chia sẻ_ **_logic chung_**_._
    
*   _Interface để định nghĩa_ **_hợp đồng hành vi_**_._
    

### **3\. Method Overloading vs Method Overriding khác nhau như thế nào?**

**📌 Trả lời:**

*   **_Overloading (nạp chồng):_**
    
    *   _Cùng tên method, khác tham số._
        
    *   _Xác định tại_ **_compile-time_**_._
        
*   **_Overriding (ghi đè):_**
    
    *   _Subclass định nghĩa lại method của superclass._
        
    *   _Xác định tại_ **_runtime_**_._
        

### **4\. Final, Finally, Finalize khác nhau thế nào?**

**📌 Trả lời:**

*   **_final:_** _Từ khóa → ngăn kế thừa (class), ngăn override (method), khai báo hằng số (biến)._
    
*   **_finally:_** _Block trong_ `try-catch-finally`_, luôn chạy dù có exception._
    
*   **_finalize():_** _Method gọi trước khi GC thu hồi object, không đảm bảo thời gian chạy._
    

### **5\. Static vs Non-static trong Java?**

**📌 Trả lời:**

*   **_Static:_**
    
    *   _Gắn với class, không cần tạo object._
        
    *   _Ví dụ:_ `Math.sqrt()`_._
        
*   **_Non-static:_**
    
    *   _Gắn với instance, mỗi object có bản riêng._
        
    *   _Ví dụ:_ `person.getName()`_._
        

### **6\. Composition vs Inheritance – khi nào nên dùng?**

**📌 Trả lời:**

*   **_Inheritance (IS-A):_** `Cat extends Animal`_._
    
*   **_Composition (HAS-A):_** `Car has Engine`_._
    

_👉 Nên ưu tiên_ **_composition_** _vì:_

*   _Linh hoạt hơn._
    
*   _Giảm coupling._
    
*   _Áp dụng nguyên tắc: “Favor composition over inheritance.”_
    

### **7\. Multiple Inheritance qua Interface & Diamond Problem trong Java**

**📌 Trả lời:**

*   _Java không hỗ trợ multiple inheritance qua class, nhưng hỗ trợ qua interface._
    
*   _Nếu hai interface có_ `default method` _trùng nhau → class implements phải override để giải quyết xung đột._
    

_– Ví dụ:_

```java
interface A {
	default void hello() {
		System.out.println("A");
	}
}
```

```java
interface B {
	default void hello() {
		System.out.println("B");
	}
}
```

```java
class C implements A, B {
    @Override
    public void hello() {
    	A.super.hello();
    }
}
```

### **8\. Lập trình hướng Interface thay vì Implementation**

**📌 Trả lời:**

_Giúp code_ **_dễ mở rộng_**_,_ **_giảm phụ thuộc_**_._

_– Ví dụ xấu:_

```java
ArrayList<String> list = new ArrayList<>();
```

_👉 Khó thay đổi sang LinkedList._

_– Ví dụ tốt:_

```java
List<String> list = new ArrayList<>();
```

### **9\. Overriding vs Hiding (Static Method)**

**📌 Trả lời:**

*   **_Overriding:_** _Dùng cho_ **_instance method_** _→ quyết định tại runtime._
    
*   **_Hiding:_** _Dùng cho_ **_static method_** _→ quyết định tại compile-time._
    

```java
class Parent {
    static void show() { System.out.println("Parent static"); }
    void run() { System.out.println("Parent run"); }
}
```

```java
class Child extends Parent {
	
	// hiding
    static void show() {
    	System.out.println("Child static");
    }
    
    // overriding
    @Override
    void run() {
    	System.out.println("Child run");
    }
}
```

### **10\. Dynamic Dispatch & Virtual Method Table (VMT)**

**📌 Trả lời:**

*   **_Dynamic Dispatch:_** _JVM chọn method gọi tại runtime dựa trên object thực tế._
    
*   **_Virtual Method Table (vtable):_**
    
    *   _JVM lưu bảng ánh xạ method override._
        
    *   _Khi gọi_ `obj.method()`_, JVM tra cứu vtable để xác định method đúng._
        
    *   _Đây là cách Java thực hiện_ **_runtime polymorphism_**_._
        

#### **_👉 Đăng ký ngay khoá học_** [**_Java Core Nâng Cao Thực Chiến - Full Version_**](https://vi.nguyentienkhoi.hashnode.dev/courses/java-core-nang-cao-thuc-chien-full-version) **_để nắm vững Java Core và bứt phá sự nghiệp Lập Trình Java_**

