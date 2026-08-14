# Java ArrayList

### **1\. ArrayList là gì?**

**ArrayList** là một lớp thuộc gói `java.util`, ArrayList là một trong những triển khai phổ biến nhất của `interface List.` `ArrayList` là một mảng động, có khả năng thay đổi kích thước khi các phần tử được thêm vào hoặc xóa khỏi danh sách. Đây là một lớp rất hữu ích khi bạn cần lưu trữ một danh sách các phần tử và cần thao tác với chúng một cách linh hoạt.

![](https://cdn.tayjava.com/production/image/20250909_110929_pasted-1757390967808.png)

### **2\. Các đặc điểm chính của** `ArrayList`

*   **Mảng động**: Kích thước của `ArrayList` có thể thay đổi linh hoạt khi thêm hoặc xóa phần tử, không giống như mảng tĩnh (array) có kích thước cố định.
    
*   **Truy cập ngẫu nhiên**: `ArrayList` cung cấp thời gian truy cập phần tử theo chỉ số (index) rất nhanh vì nó dựa trên mảng, với độ phức tạp O(1).
    
*   **Không đồng bộ (non-synchronized)**: `ArrayList` không an toàn cho các thao tác đồng thời (multithreading). Nếu sử dụng trong môi trường đa luồng, cần đồng bộ hóa thủ công hoặc sử dụng các lớp đồng bộ hóa như `Collections.synchronizedList()`.
    
*   **Thứ tự chèn**: `ArrayList` duy trì thứ tự các phần tử dựa trên thứ tự chúng được thêm vào.
    
*   **Cho phép phần tử trùng lặp và giá trị null**: `ArrayList` có thể chứa các giá trị trùng lặp và một hoặc nhiều giá trị `null`.
    

### **3\. Các phương thức phổ biến của** `ArrayList`

*   `add(E e)`: Thêm phần tử vào cuối danh sách.
    
*   `add(int index, E element)`: Thêm phần tử vào vị trí chỉ định.
    
*   `remove(Object o)`: Xóa phần tử đầu tiên có giá trị tương ứng trong danh sách.
    
*   `remove(int index)`: Xóa phần tử tại vị trí chỉ định.
    
*   `get(int index)`: Trả về phần tử tại vị trí chỉ định.
    
*   `set(int index, E element)`: Cập nhật phần tử tại vị trí chỉ định với giá trị mới.
    
*   `size()`: Trả về số lượng phần tử trong danh sách.
    
*   `clear()`: Xóa toàn bộ các phần tử trong danh sách.
    
*   `isEmpty()`: Kiểm tra xem danh sách có rỗng không.
    
*   `contains(Object o)`: Kiểm tra xem danh sách có chứa phần tử cụ thể không.
    

### **4\. Cách sử dụng** `ArrayList`

*   **Khởi tạo và thêm phần tử**
    

```java
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        // Khởi tạo ArrayList
        ArrayList fruits = new ArrayList<>();

        // Thêm phần tử vào ArrayList
        fruits.add("Cam"); // index = 0
        fruits.add("Quýt"); // index = 1
        fruits.add("Mít"); // index = 2
        fruits.add("Dừa"); // index = 3

        // In ArrayList
        System.out.println("Quả: " + fruits);
    }
}
```

*   **Truy cập phần tử**
    

Bạn có thể truy cập phần tử bằng phương thức `get(index)`

```java
String fruit = fruits.get(1);  // Lấy phần tử tại vị trí 1 (Quýt)
System.out.println("Quả: " + fruit);
```

*   **Cập nhật phần tử**
    

Bạn có thể cập nhật phần tử bằng phương thức `set(index, element)`

```java
fruits.set(1, "Táo");  // Cập nhật phần tử tại vị trí 1 thành Táo 
System.out.println("Cập nhật quả: " + fruits);
```

*   **Xóa phần tử**
    

Bạn có thể xóa phần tử bằng remove(index) hoặc remove(Object)

```java
fruits.remove(0);  // Xóa phần tử tại vị trí 0 (Cam)
fruits.remove("Mít");  // Xóa phần tử có giá trị "Mít"
System.out.println("Sau khi xoá: " + fruits);
```

*   **Duyệt qua các phần tử**
    

Bạn có thể duyệt qua `ArrayList` bằng vòng lặp `for-each` hoặc vòng lặp thông thường

```java
// Sử dụng for-each 
for (String fruit : fruits) {    
	System.out.println(fruit); 
}

// Sử dụng vòng lặp thông thường 
for (int i = 0; i < fruits.size(); i++) {    
	System.out.println(fruits.get(i)); 
}
```

*   **Kiểm tra kích thước và rỗng**
    
    *   Sử dụng `size()` để lấy số lượng phần tử trong ArrayList.
        
    *   Sử dụng `isEmpty()` để kiểm tra xem danh sách có rỗng không.
        

```java
System.out.println("Kích thước danh sách: " + fruits.size()); 
System.out.println("Danh sách có bị trống không? " + fruits.isEmpty());
```

*   **Kiểm tra sự tồn tại của phần tử**
    

Sử dụng `contains(Object)` để kiểm tra xem **ArrayList** có chứa phần tử nào đó không.

```java
boolean isContain = fruits.contains("Xoài");
System.out.println("Có quả xoài không? " + isContain);
```

*   **Một số điểm cần lưu ý khi sử dụng ArrayList**
    
    *   **Hiệu suất**: `ArrayList` có thời gian truy cập theo chỉ số là O(1) nhưng thời gian thêm, xóa phần tử ở giữa danh sách có thể là O(n) do việc phải dịch chuyển các phần tử còn lại.
        
    *   **Dung lượng**: `ArrayList` có khả năng tự động tăng dung lượng khi cần nhưng điều này có thể tiêu tốn tài nguyên nếu việc mở rộng xảy ra quá nhiều.
        

### **5\. Khi nào nên sử dụng** `ArrayList`**?**

*   Sử dụng `ArrayList` khi bạn cần một danh sách mà việc truy cập phần tử theo chỉ số là nhanh chóng và không cần phải thêm hoặc xóa phần tử quá nhiều.
    
*   Nếu cần thao tác thêm/xóa phần tử thường xuyên ở đầu hoặc giữa danh sách, có thể cân nhắc sử dụng **LinkedList** thay thế.
