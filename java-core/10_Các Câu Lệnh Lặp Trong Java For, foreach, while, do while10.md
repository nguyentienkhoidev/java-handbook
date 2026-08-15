# Các Câu Lệnh Lặp Trong Java: For, foreach, while, do while

Trong lập trình, **câu lệnh lặp (loop statements)** cho phép thực thi một khối lệnh nhiều lần cho đến khi một điều kiện không còn đúng.  
Java cung cấp nhiều loại vòng lặp để xử lý các tình huống khác nhau.

Câu lệnh lặp được sử dụng để thực hiện lặp đi lặp lại một đoạn mã.

### **1\. Vòng lặp** `for`

Vòng lặp for của Java được sử dụng để lặp lại một phần của chương trình nhiều lần. Nếu số lần lặp cố định, nên sử dụng vòng lặp for.

*   Bối cảnh áp dụng: Đọc một danh sách theo tuần tự để tìm ra giá trị tương ứng
    
*   Cú pháp:
    

```java
for (initialization; condition; increment/decrement){
    //statement or code to be executed    
}
```

*   Ví dụ:
    

```java
public class App {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("So: " + i);
        }

        // Lặp chồng lặp (++)
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }

        // Lặp chồng lặp (--)
        for (int i = 1; i <= 10; i++) {
            for (int j = 10; j >= i; j--) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }
}
```

### **2\. Vòng lặp** `foreach`

*   Bối cảnh áp dụng: Đọc một danh sách với các kiểu dữ liệu khác nhau theo tuần tự để tìm ra giá trị tương ứng
    
*   Cú pháp:
    

```java
for (data_type variable : array_name){
    //code to be executed    
}
```

*   Ví dụ:
    

```java
public class App {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        list.add("H");
        for (String s : list) {
            System.out.println(s);
        }
    }
}
```

### **3\. Vòng lặp** `while`

Vòng lặp `while` của Java được sử dụng để lặp lại một phần của chương trình nhiều lần cho đến khi điều kiện Boolean được chỉ định là đúng. Ngay khi điều kiện Boolean trở thành false, vòng lặp sẽ tự động dừng lại.

*   Bối cảnh áp dụng: dùng để duyệt danh sách theo điều kiện chỉ định trước
    
*   Cú pháp:
    

```java
while (condition){    
   // code to be executed   
   // Increment / decrement statement  
}
```

*   Ví dụ:
    

```java
public class App {
    public static void main(String[] args) {
        int x = 5;
        while (x <= 10) {
            System.out.println(x);
            x++;
        }
    }
}
```

### **4\. Vòng lặp** `do while`

*   Bối cảnh áp dụng: dùng để duyệt danh sách theo điều kiện chỉ định
    
*   Cú pháp:
    

```java
do {    
   // code to be executed / loop body  
   // update statement   
} while (condition);
```

*   Ví dụ:
    

```java
public class App {
    public static void main(String[] args) {
        int y = 5;
        do {
            System.out.println(y);
            y++;
        } while (y <= 10);
    }
}
```

