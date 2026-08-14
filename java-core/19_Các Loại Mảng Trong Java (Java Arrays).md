# Các Loại Mảng Trong Java (Java Arrays)

### **1\. Java Arrays là gì?**

**Java Arrays** là một đối tượng chứa các phần tử có cùng kiểu dữ liệu. Các phần tử của một mảng được lưu trữ trong một vị trí bộ nhớ liền kề. Đây là một cấu trúc dữ liệu để lưu trữ các phần tử giống nhau. Chúng ta chỉ có thể lưu trữ một tập hợp các phần tử cố định trong một mảng Java.

![](https://cdn.tayjava.com/production/image/20250908_143350_pasted-1757316829694.png)

*   Ưu điểm:
    
    *   **Tối ưu code**: Nó làm cho code được tối ưu hoá vì chúng ta dễ dàng sắp xếp và truy xuất dữ liệu.
        
    *   **Truy xuất ngẫu nhiên**: Chúng ta có thể lấy bất kỳ dữ liệu nào theo chỉ mục.
        
*   Nhược điểm:
    
    *   **Giới hạn kích thước:** Chúng ta chỉ có thể lưu trữ kích thước cố định của các phần tử trong mảng. Nó không tăng kích thước khi chạy chương trình. Để giải quyết vấn đề này Java collection có thể tự động gia tăng kích thước của array.
        

### **2\. Các loại Arrays**

Có 2 loại array:

*   Mảng một chiều (Single Dimensional Array)
    
*   Mảng đa chiều (Multidimensional Array)
    

#### **a. Mảng một chiều (Single Dimensional Array)**

– Cú pháp

```java
// Định nghĩa array
dataType[] arr; (or)
dataType []arr; (or)
dataType arr[];
// Khởi tạo
arrayRefVa r= new datatype[size];
```

– Ví dụ 1:

```java
public class App {
    public static void main(String[] args) {

        int arr[] = new int[3];// định nghĩa và tạo array

        arr[0] = 13; // gán phần từ vào mảng
        arr[1] = 49;
        arr[2] = 77;

        // Duyệt mảng
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        // Mảng string
        String arrS[] = {"A", "B", "C", "D"};
        for (String s : arrS) {
            System.out.println(s);
        }
    }
}
```

– Ví dụ 2: Định nghĩa, khai báo và khởi tạo array

```java
public static void main(String[] args) {
    //declaration, instantiation and initialization
    int xArr[] = {1, 3, 7};

    // in mảng bằng foreach
    for (int i : xArr) {
        System.out.println(i);
    }
}
```

– **ArrayIndexOutOfBoundsException**: Ngoại lệ `ArrayIndexOutOfBoundsException` xảy ra khi chúng ta cố gắng truy xuất phần tử ngoài độ dài của mảng.

```java
public class App {
    public static void main(String[] args) {

        int yArr[] = {2, 4, 6};
        System.out.println("Độ dài của yArr = " + yArr.length);
        System.out.println(yArr[0]);
        System.out.println(yArr[1]);
        System.out.println(yArr[2]);
        // dòng này bị lỗi ArrayIndexOutOfBoundsException do mảng chỉ có 3 phần tử và index = 3 là không tồn tại.
        System.out.println(yArr[3]);
    }
}
```

Lỗi in ra như sau:

```java
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3
	at src.main.java.App.main(App.java:12)
Độ dài của yArr = 3
2
4
6
```

#### **b. Mảng đa chiều (Multidimensional Array)**

– Cú pháp

```java
dataType[][] arr; (or)  
dataType [][]arr; (or)  
dataType arr[][]; (or)  
dataType []arr[];
```

– Ví dụ

```java
public class App {
    public static void main(String[] args) {

        int[][] xArr = new int[3][3]; // 3 = row and 3 = column

        xArr[0][0] = 1;
        xArr[0][1] = 2;
        xArr[0][2] = 3;

        xArr[1][0] = 4;
        xArr[1][1] = 5;
        xArr[1][2] = 6;

        xArr[2][0] = 7;
        xArr[2][1] = 8;
        xArr[2][2] = 9;

        System.out.println("--- xArr ---");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(xArr[i][j]);
            }
            System.out.println();
        }

        int[][] yArr = {{10, 20, 30}, {40, 50, 60}, {70, 80, 90}};
        System.out.println("--- yArr ---");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(yArr[i][j]);
            }
            System.out.println();
        }
    }
}
```

### **c. Mảng răng cưa (Jagged Array)**

*   **Mảng răng cưa là gì?**
    

Mảng răng cưa (Jagged Array) trong Java là một loại mảng nhiều chiều nhưng các hàng của nó có thể có số lượng cột khác nhau. Điều này khác với mảng hai chiều thông thường, trong đó mỗi hàng có cùng số lượng cột. Mảng răng cưa cho phép mỗi hàng của mảng có kích thước khác nhau, tạo thành một cấu trúc không đều (giống như hình răng cưa).

![](https://cdn.tayjava.com/production/image/20250908_144427_pasted-1757317465959.png)

– Cú pháp:

```java
int[][] jaggedArray = new int[3][];  // Khai báo một mảng 2 chiều với 3 hàng nhưng không xác định số cột
jaggedArray[0] = new int[2];  // Hàng đầu tiên có 2 cột
jaggedArray[1] = new int[4];  // Hàng thứ hai có 4 cột
jaggedArray[2] = new int[3];  // Hàng thứ ba có 3 cột
```

– Ví dụ:

```java
public class App {
    public static void main(String[] args) {
        // Mảng 2 chiều
        int arr[][] = new int[3][];
        arr[0] = new int[3];
        arr[1] = new int[5];
        arr[2] = new int[2];

        // Tạo mảng răng cưa
        int count = 0;
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[i].length; j++)
                arr[i][j] = count++;

        // in ra mảng
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();//new line
        }
    }
}
```

– Kết quả in ra như sau:

```java
0 1 2 
3 4 5 6 7 
8 9
```

*   **Đặc điểm của mảng răng cưa**
    
    *   **Kích thước không đều**: Các hàng có thể có số lượng phần tử khác nhau, cho phép cấu trúc linh hoạt hơn so với mảng hai chiều thông thường.
        
    *   **Truy cập các phần tử**: Bạn có thể truy cập các phần tử trong mảng bằng cách sử dụng chỉ mục giống như mảng hai chiều.
        
    *   **Ứng dụng**: Mảng răng cưa hữu ích khi bạn cần quản lý dữ liệu với các nhóm có kích thước khác nhau (ví dụ: dữ liệu không đồng nhất, ma trận không đều).
        

### **3\. Clone Array**

**Clone Array trong Java** là quá trình tạo một bản sao của một mảng. Khi bạn clone một mảng thì một mảng mới với các phần tử giống như mảng ban đầu sẽ được tạo ra. Java hỗ trợ phương thức `clone()` để tạo bản sao của mảng nhưng có một số điều cần lưu ý tùy thuộc vào loại mảng bạn đang làm việc đó là **mảng một chiều** hay **mảng nhiều chiều**.

#### **a. Clone mảng một chiều**

Khi clone một mảng một chiều, Java sẽ tạo ra một bản sao nông (shallow copy) của mảng. Điều này có nghĩa là tất cả các giá trị trong mảng mới sẽ giống như mảng cũ, và mảng mới sẽ độc lập với mảng cũ (thay đổi trong mảng cũ sẽ không ảnh hưởng đến mảng mới).

```java
public class App {
    public static void main(String[] args) {
        int[] originalArray = {1, 2, 3, 4, 5};

        // Clone mảng
        int[] clonedArray = originalArray.clone();

        // In cả hai mảng
        System.out.println("Mảng gốc: ");
        for (int i : originalArray) {
            System.out.print(i + " ");
        }

        System.out.println("\nMảng clone: ");
        for (int i : clonedArray) {
            System.out.print(i + " ");
        }

        // Thay đổi giá trị trong mảng gốc và kiểm tra mảng clone
        originalArray[0] = 100;

        System.out.println("\n\nSau khi thay đổi mảng gốc:");
        System.out.println("Mảng gốc: ");
        for (int i : originalArray) {
            System.out.print(i + " ");
        }

        System.out.println("\nMảng clone: ");
        for (int i : clonedArray) {
            System.out.print(i + " ");
        }
    }
}
```

– Kết quả in ra như sau:

```java
Mảng gốc: 
1 2 3 4 5 
Mảng clone: 
1 2 3 4 5 

Sau khi thay đổi mảng gốc:
Mảng gốc: 
100 2 3 4 5 
Mảng clone: 
1 2 3 4 5
```

#### **b. Clone mảng nhiều chiều (mảng hai chiều)**

Khi clone một mảng nhiều chiều (mảng 2 chiều), Java chỉ thực hiện sao chép nông (shallow copy) đối với hàng đầu tiên của mảng. Điều này có nghĩa là các phần tử của mảng chính được sao chép nhưng các mảng con bên trong không được sao chép. Nếu bạn thay đổi một phần tử bên trong mảng con thì nó sẽ ảnh hưởng đến cả mảng clone và mảng gốc.

```java
public class App {
    public static void main(String[] args) {
        int[][] originalArray = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        // Clone mảng
        int[][] clonedArray = originalArray.clone();

        // In mảng gốc
        System.out.println("Mảng gốc: ");
        for (int[] row : originalArray) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }

        // Thay đổi giá trị trong mảng gốc
        originalArray[0][0] = 100;

        System.out.println("\nSau khi thay đổi mảng gốc:");
        System.out.println("Mảng gốc: ");
        for (int[] row : originalArray) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }

        System.out.println("Mảng clone: ");
        for (int[] row : clonedArray) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }
}
```

– Kết quả in ra như sau:

```java
Mảng gốc: 
1 2 3 
4 5 6 
7 8 9 

Sau khi thay đổi mảng gốc:
Mảng gốc: 
100 2 3 
4 5 6 
7 8 9 
Mảng clone: 
100 2 3 
4 5 6 
7 8 9
```

– **Giải thích**:  
Ở mảng hai chiều, khi bạn clone mảng `originalArray` thì bản thân mảng `clonedArray` được tạo ra nhưng các mảng con (các hàng) vẫn chỉ trỏ tới cùng một vùng nhớ. Điều này có nghĩa là khi bạn thay đổi `originalArray[0][0]` thì `clonedArray[0][0]` cũng sẽ thay đổi theo.

– **Giải pháp cho deep copy (sao chép sâu)**  
Nếu bạn muốn sao chép sâu (deep copy) toàn bộ mảng hai chiều thì bạn cần tự lặp qua từng hàng và sao chép từng mảng con.

```java
int[][] clonedArray = new int[originalArray.length][];
for (int i = 0; i < originalArray.length; i++) {
    clonedArray[i] = originalArray[i].clone();
}
```
