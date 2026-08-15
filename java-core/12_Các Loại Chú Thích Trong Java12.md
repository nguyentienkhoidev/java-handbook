# Các Loại Chú Thích Trong Java

Chú thích được sử dụng để ghi chú, giải thích mã nguồn mà không ảnh hưởng đến quá trình biên dịch và chạy chương trình. Có ba loại chú thích chính trong Java như sau:

### **1\. Chú thích dòng đơn (**`//`**)**

Dùng để chú thích một dòng, mọi thứ sau `//` trên cùng dòng đó sẽ bị bỏ qua bởi trình biên dịch.

```java
// Đây là chú thích dòng đơn
System.out.println("Xin chào Fox Dev"); // In ra dòng chữ Xin chào Fox Dev
```

### **2\. Chú thích nhiều dòng (**`/* ... */`**)**

Dùng để chú thích trên nhiều dòng, bắt đầu bằng `/*` và kết thúc bằng `*/`. Thích hợp cho việc chú thích khối lớn hoặc giải thích chi tiết hơn.

```java
/*
   Đây là chú thích nhiều dòng.
   Nó có thể bao gồm nhiều dòng văn bản.
*/
System.out.println("Xin chào Fox Dev");
```

### **3\. Chú thích kiểu Javadoc (**`/** ... */`**)**

Dùng để tạo tài liệu tự động cho mã nguồn bằng công cụ `Javadoc`. Các chú thích này thường được sử dụng để mô tả lớp, phương thức hoặc biến và có thể bao gồm các thẻ như `@param`, `@return`, và `@author`.

```java
public class App {
    public static void main(String[] args) {
        printMessage("Fox Dev");
    }

    /**
     * In ra ten
     * @param name
     * @return
     */
    public static void printMessage(String name) {
        System.out.println(name);
    }
}
```

