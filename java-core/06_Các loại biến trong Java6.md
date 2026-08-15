# Các loại biến trong Java

Trong Java, **biến (variable)** là vùng nhớ được đặt tên, dùng để lưu trữ dữ liệu trong chương trình.  
Java là ngôn ngữ **strongly typed**, nghĩa là mọi biến phải được khai báo kiểu dữ liệu rõ ràng.

Trong Java có 5 loại biến như sau:

### **1\. Local Variable (Biến cục bộ)**

Một biến được khai báo bên trong thân phương thức(method) được gọi là local variable. Bạn chỉ có thể sử dụng biến này trong phương thức đó và các phương thức khác trong lớp thậm chí không biết rằng biến này tồn tại.

*   **Phạm vi:** Được khai báo trong một phương thức, constructor, hoặc khối lệnh.
    
*   **Vòng đời:** Tồn tại trong suốt quá trình thực thi phương thức hoặc khối lệnh đó. Khi phương thức kết thúc, biến cục bộ sẽ bị hủy.
    
*   **Khởi tạo:** Cần được khởi tạo trước khi sử dụng, vì Java không tự động khởi tạo biến cục bộ.
    

```java
public class App {

    public void printName() {
        String localVar = "Fox Dev"; // Biến cục bộ
        System.out.println(localVar);
    }
}
```

### **2\. Instance Variable (Biến instance)**

Một biến được khai báo bên trong class nhưng bên ngoài phương thức được gọi là instance variable. Biến này không được khai báo là static. Biến này được gọi là instance variable vì giá trị của biến này là cụ thể cho từng instance cụ thể và không được chia sẻ giữa các instance.

*   **Phạm vi:** Được khai báo bên trong một lớp nhưng bên ngoài bất kỳ phương thức, constructor, hoặc khối lệnh nào.
    
*   **Vòng đời:** Gắn liền với đối tượng (instance) của lớp. Khi đối tượng bị hủy (garbage collected), biến instance cũng sẽ bị hủy theo.
    
*   **Khởi tạo:** Mặc định sẽ được khởi tạo giá trị nếu không được khởi tạo (ví dụ: null cho đối tượng, 0 cho số nguyên, false cho kiểu boolean).
    

```java
public class App {
    int instanceVar;  // Biến instance (thuộc đối tượng)
    
    public void printName() {
        String localVar = "Fox Dev"; // Biến cục bộ
        System.out.println(localVar);
    }
}
```

#### **3\. Static Variable (Biến tĩnh)**

Một biến được khai báo là static được gọi là static variable. Biến này không thể là local variable. Bạn có thể tạo một bản sao duy nhất của static variable và chia sẻ biến này giữa tất cả các method của class. Việc cấp phát bộ nhớ cho các biến tĩnh chỉ diễn ra một lần khi lớp được tải trong bộ nhớ.

*   **Phạm vi:** Được khai báo với từ khóa static và tồn tại trên phạm vi lớp thay vì phạm vi đối tượng. Tức là biến này được chia sẻ giữa tất cả các đối tượng của lớp đó.
    
*   **Vòng đời:** Tồn tại trong suốt vòng đời của lớp và chỉ được khởi tạo một lần khi lớp được tải vào bộ nhớ (class loading).
    
*   **Khởi tạo:** Tương tự như biến instance, biến static được khởi tạo giá trị mặc định nếu không được khởi tạo.
    

```java
public class App {
    int instanceVar;  // Biến instance (thuộc đối tượng)

    static int staticVar = 100;  // Biến static (thuộc lớp)

    public void printName() {
        String localVar = "Fox Dev"; // Biến cục bộ
        System.out.println(localVar);
    }
}
```

### **4\. Constant Variable (Biến hằng số)**

*   **Phạm vi:** Là biến có giá trị cố định và không thể thay đổi sau khi đã khởi tạo. Được khai báo với từ khóa final.
    
*   **Vòng đời:** Giống như biến instance hoặc biến static tùy thuộc vào cách khai báo.
    
*   **Khởi tạo:** Cần được khởi tạo khi khai báo hoặc trong constructor nếu là biến instance final.
    

```java
public class App {
    int instanceVar;  // Biến instance (thuộc đối tượng)

    static int staticVar = 100;  // Biến static (thuộc lớp)

    final double CONSTANT_VAR = 3.14; // Biến hằng số

    public void printName() {
        String localVar = "Fox Dev"; // Biến cục bộ
        System.out.println(localVar);
    }
}
```

### **5\. Reference Variable (Biến tham chiếu)**

*   **Phạm vi:** Được sử dụng để tham chiếu đến các đối tượng (object) trong Java. Đây là các biến lưu trữ địa chỉ của đối tượng thay vì giá trị thực tế của nó.
    
*   **Vòng đời:** Giống như biến cục bộ, instance hoặc static tùy thuộc vào nơi khai báo biến tham chiếu.
    
*   **Khởi tạo:** Biến tham chiếu mặc định là null nếu không được khởi tạo.
    

```java
public class App {
    int instanceVar;  // Biến instance (thuộc đối tượng)

    static int staticVar = 100;  // Biến static (thuộc lớp)

    final double CONSTANT_VAR = 3.14; // Biến hằng số
    
    String welcome = "Welcome to Java Programming!"; // Biến tham chiếu tới đối tượng String

    public void printName() {
        String localVar = "Fox Dev"; // Biến cục bộ
        System.out.println(localVar);
    }

    public void display() {
        System.out.println(welcome);
    }
}
```

