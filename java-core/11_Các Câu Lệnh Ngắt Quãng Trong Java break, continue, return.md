# Các Câu Lệnh Ngắt Quãng Trong Java: break, continue, return

**Câu Lệnh Ngắt Quãng** (Jump statements) được dùng để thay đổi luồng thực thi bình thường của chương trình. Chúng cho phép bạn **nhảy** ra khỏi vòng lặp, bỏ qua một phần mã, hoặc quay lại đầu vòng lặp.

### **1\. Câu lệnh** `break`

Khi gặp câu lệnh `break` bên trong vòng lặp, vòng lặp sẽ ngay lập tức kết thúc và chương trình sẽ tiếp tục điều khiển ở câu lệnh tiếp theo sau vòng lặp.  
Câu lệnh `break` của Java được sử dụng để ngắt vòng lặp hoặc câu lệnh chuyển đổi. Nó ngắt luồng hiện tại của chương trình ở điều kiện được chỉ định. Trong trường hợp vòng lặp bên trong, nó chỉ ngắt vòng lặp bên trong.  
Chúng ta có thể sử dụng câu lệnh `break` của Java trong tất cả các loại vòng lặp như vòng lặp `for`, vòng lặp `while` và vòng lặp `do-while`.

![](https://cdn.tayjava.com/production/image/20250908_083426_pasted-1757295263353.png)

*   Bối cảnh áp dụng: Dùng khi ta muốn dừng vòng lặp ngay lập tức
    
*   Cú pháp:
    

```java
jump-statement;
break;
```

*   Ví dụ:
    

```java
public class App {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                break; // ngắt vòng lặp ở đây
            }
            System.out.println("So: " + i);
        }
    }
}
```

### **2\.** `continue`

Câu lệnh `continue` được sử dụng trong cấu trúc điều khiển vòng lặp khi bạn cần nhảy đến vòng lặp tiếp theo của vòng lặp ngay lập tức. Nó có thể được sử dụng với vòng lặp `for`, `while` và `do-while`  
Câu lệnh `continue` của Java được sử dụng để tiếp tục vòng lặp. Nó tiếp tục luồng hiện tại của chương trình và bỏ qua mã còn lại ở điều kiện đã chỉ định. Trong trường hợp vòng lặp bên trong, nó chỉ tiếp tục vòng lặp bên trong.

![](https://cdn.tayjava.com/production/image/20250908_083426_pasted-1757295263354.png)

*   Bối cảnh áp dụng: Sử dụng khi ta muốn bỏ qua điều kiện nào đó khi duyệt mảng
    
*   Cú pháp:
    

```java
jump-statement;
continue;
```

*   Ví dụ:
    

```java
public class App {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                continue; // tiếp tục vòng lặp tiếp theo
            }
            System.out.println("So: " + i);
        }
    }
}
```

### **3\.** `return`

Dùng để **thoát khỏi phương thức** và có thể trả về một giá trị (nếu phương thức không phải `void`).

![](https://cdn.tayjava.com/production/image/20250908_085513_pasted-1757296510255.png)

```java
public class App {
   public static void main(String[] args) {
       System.out.println(checkNumber(5));
   }
   public static String checkNumber(int n) {
       if (n % 2 == 0) {
           return "Even"; // kết thúc hàm và trả về "Even"
       }
       return "Odd"; // kết thúc hàm và trả về "Odd"
   }
}
```
