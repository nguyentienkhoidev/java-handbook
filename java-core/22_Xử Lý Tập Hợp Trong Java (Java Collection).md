# Xử Lý Tập Hợp Trong Java (Java Collection)

### **1\. Java Collection là gì?**

**Java Collection** là một framework trong Java dùng để quản lý và thao tác với các nhóm đối tượng. Nó cung cấp một tập hợp các cấu trúc dữ liệu và các thuật toán để xử lý dữ liệu như thêm, xóa, duyệt qua, và sắp xếp các phần tử. Java Collection bao gồm `interfaces` và `classes` giúp lập trình viên dễ dàng quản lý dữ liệu theo cách hiệu quả hơn.

![](https://cdn.tayjava.com/production/image/20250908_152148_pasted-1757319705720.png)

### **2\. Interfaces**

*   **Collection**: Interface cơ sở cho tất cả các tập hợp. Nó định nghĩa các phương thức cơ bản như `add()`, `remove()`, `size()`, `iterator()`,…
    
*   **List**: Interface mở rộng từ `Collection`, đại diện cho một tập hợp có thứ tự và cho phép các phần tử trùng lặp. Ví dụ: `ArrayList`, `LinkedList`.
    
*   **Set**: Interface không cho phép các phần tử trùng lặp. Ví dụ: `HashSet`, `LinkedHashSet`, `TreeSet`.
    
*   **Map**: Interface cho phép lưu trữ các cặp khóa-giá trị, trong đó mỗi khóa là duy nhất. Ví dụ: `HashMap`, `TreeMap`, `LinkedHashMap`.
    
*   **Queue**: Interface đại diện cho một tập hợp cho phép xử lý các phần tử theo thứ tự, thường theo nguyên tắc FIFO (First In First Out). Ví dụ: `LinkedList`, `PriorityQueue`.
    

### **3\. Classes**

*   **ArrayList**: Một danh sách có thể thay đổi kích thước được triển khai dưới dạng mảng động.
    
*   **LinkedList**: Một danh sách sử dụng cấu trúc dữ liệu danh sách liên kết cho phép thêm và xóa nhanh chóng.
    
*   **HashSet**: Một tập hợp không cho phép các phần tử trùng lặp và không duy trì thứ tự.
    
*   **LinkedHashSet**: Giống như `HashSet` nhưng duy trì thứ tự chèn các phần tử.
    
*   **TreeSet**: Một tập hợp cho phép tự động sắp xếp các phần tử.
    
*   **HashMap**: Một ánh xạ không duy trì thứ tự cho phép các cặp khóa-giá trị.
    
*   **LinkedHashMap**: Giống như `HashMap` nhưng duy trì thứ tự chèn các mục.
    
*   **TreeMap**: Một ánh xạ sắp xếp các khóa theo thứ tự tự nhiên hoặc theo trình soạn thảo của một Comparator.
    

### **4\. Algorithms**

JCF cũng cung cấp một số thuật toán hữu ích để thao tác với các cấu trúc dữ liệu như:

*   **sort()**: Sắp xếp các phần tử trong một danh sách.
    
*   **shuffle()**: Xáo trộn các phần tử trong một danh sách.
    
*   **reverse()**: Đảo ngược thứ tự của các phần tử trong một danh sách.
    
*   **binarySearch()**: Tìm kiếm nhị phân trong một danh sách đã được sắp xếp.
    

### **5\. Generics**

Java Collections Framework hỗ trợ `generics` cho phép bạn chỉ định loại dữ liệu mà tập hợp sẽ chứa. Điều này giúp tăng cường tính an toàn kiểu (type safety) và giảm thiểu lỗi runtime.

### **6\. Sự khác biệt giữa các Collections**

*   **List**: Có thứ tự cho phép trùng lặp.
    
*   **Set**: Không có thứ tự không cho phép trùng lặp.
    
*   **Map**: Không có thứ tự (trong trường hợp của `HashMap`) cho phép khóa duy nhất và giá trị có thể trùng lặp.
    

```java
import java.util.ArrayList;
import java.util.HashMap;

public class App {
    public static void main(String[] args) {
        // Sử dụng ArrayList
        List<String> fruits = new ArrayList<>();
        fruits.add("Táo");
        fruits.add("Chuối");
        fruits.add("Xoài");
        System.out.println("Fruits: " + fruits);

        // Sử dụng HashMap
        HashMap<String, Integer> map = new HashMap<>();
        ageMap.put("Dâu", 10);
        ageMap.put("Tây", 20);
        System.out.println("Tây: " + map.get("Tây") + " tuổi");
    }
}
```
