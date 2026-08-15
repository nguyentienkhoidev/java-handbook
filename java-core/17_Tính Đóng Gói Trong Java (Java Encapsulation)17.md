# Tính Đóng Gói Trong Java (Java Encapsulation)

### **1\. Java Encapsulation là gì?**

Đóng gói trong Java (Java Encapsulation) là một nguyên tắc cơ bản của lập trình hướng đối tượng (OOP), nó được sử dụng để bảo vệ dữ liệu bên trong một đối tượng và chỉ cho phép truy cập thông qua các phương thức được định nghĩa (getters và setters). Điều này giúp duy trì sự toàn vẹn của dữ liệu và ngăn chặn sự truy cập hoặc thay đổi trực tiếp từ bên ngoài.

### **2\. Access modifiers**

Các thành phần của lớp (biến, phương thức) có thể được khai báo với các quyền truy cập như `private`, `protected`, `public`, nhằm kiểm soát quyền truy cập từ các lớp khác. 

Chúng ta có các từ khoá để chỉ định truy suất như sau:

*   `private`: Chỉ có thể truy cập trong cùng lớp.
    
*   `public`: Có thể truy cập từ bất cứ đâu.
    
*   `protected`: Có thể truy cập từ các lớp cùng package hoặc từ các lớp con (subclass).
    
*   Không có modifier (`default`): Có thể truy cập từ các lớp cùng package.
    

### **3\. Getters & Setters**

Cung cấp các phương thức truy xuất và cập nhật giá trị của các thuộc tính (fields) mà không cho phép truy cập trực tiếp vào chúng. Điều này đảm bảo rằng mọi thay đổi đối với dữ liệu chỉ được thực hiện theo cách có kiểm soát.

```java
public class User {
    // Thuộc tính private chỉ có thể truy cập từ bên trong lớp này
    private String name;
    private int age;

    // Phương thức getter cho thuộc tính name
    public String getName() {
        return name;
    }

    // Phương thức setter cho thuộc tính name
    public void setName(String name) {
        this.name = name;
    }

    // Phương thức getter cho thuộc tính age
    public int getAge() {
        return age;
    }

    // Phương thức setter cho thuộc tính age
    public void setAge(int age) {
        if (age > 18) {
            this.age = age;
        }
    }
}
```

Ở ví dụ trên, biến `name` và age được khai báo là `private`, vì vậy chúng chỉ có thể truy cập thông qua các phương thức `getName()`, `setName()`, `getAge()`, và `setAge()`. Điều này ngăn chặn việc thay đổi trực tiếp các giá trị từ bên ngoài lớp, giúp bảo vệ dữ liệu và áp dụng các ràng buộc như kiểm tra điều kiện trong `setter` (ví dụ, chỉ chấp nhận giá trị age lớn hơn 18).

