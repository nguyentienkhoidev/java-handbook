# Java LinkedHashSet

### **1\. LinkedHashSet là gì?**

**LinkedHashSet** trong Java là một cấu trúc dữ liệu thuộc thư viện Java Collections Framework, được sử dụng để lưu trữ các phần tử không trùng lặp theo thứ tự thêm vào. Nó kết hợp các đặc điểm của **HashSet** và **LinkedList**:

![](https://cdn.tayjava.com/production/image/20250909_113448_pasted-1757392488563.png)

*   **Không cho phép các phần tử trùng lặp**: Giống như HashSet, LinkedHashSet không cho phép phần tử trùng lặp.
    
*   **Duy trì thứ tự chèn**: LinkedHashSet lưu trữ các phần tử theo thứ tự mà chúng được thêm vào. Điều này khác với HashSet, trong đó các phần tử không có thứ tự xác định.
    
*   **Sử dụng bảng băm và danh sách liên kết kép**: LinkedHashSet sử dụng một bảng băm (Hash Table) để lưu trữ các phần tử và một danh sách liên kết kép để duy trì thứ tự chèn.
    

### **2\. Đặc điểm chính của LinkedHashSet**

*   **Không cho phép trùng lặp**: Các phần tử trùng lặp sẽ không được thêm vào.
    
*   **Duy trì thứ tự chèn**: Giúp đảm bảo thứ tự các phần tử theo đúng thứ tự chúng được thêm vào.
    
*   **Hiệu suất ổn định**: LinkedHashSet có thời gian truy cập trung bình là _O(1)_ cho các thao tác thêm, xóa và kiểm tra sự tồn tại của phần tử.
    
*   **Cho phép phần tử null**: Chỉ cho phép một phần tử `null` duy nhất, giống như **HashSet**.
    

### **3\. Khi nào nên dùng LinkedHashSet?**

LinkedHashSet thường được dùng khi:

*   Bạn cần một tập hợp không trùng lặp nhưng vẫn duy trì thứ tự thêm vào của các phần tử.
    
*   Muốn đảm bảo hiệu suất nhanh cho các thao tác thêm, xóa, và kiểm tra sự tồn tại (gần tương đương `HashSet`).
    
*   Không cần sắp xếp các phần tử mà chỉ cần giữ thứ tự chèn.
    

### **4\. Các phương thức quan trọng của LinkedHashSet**

**LinkedHashSet** thừa hưởng hầu hết các phương thức từ **HashSet**, bao gồm:

*   `add(E e)`: Thêm một phần tử vào LinkedHashSet.
    
*   `remove(Object o)`: Xóa một phần tử khỏi LinkedHashSet.
    
*   `contains(Object o)`: Kiểm tra sự tồn tại của một phần tử trong LinkedHashSet.
    
*   `clear()`: Xóa tất cả các phần tử khỏi LinkedHashSet.
    
*   `isEmpty()`: Kiểm tra xem LinkedHashSet có trống hay không.
    
*   `size()`: Trả về số lượng phần tử trong LinkedHashSet.
    

```java
import java.util.LinkedHashSet;

public class App {

    public static void main(String[] args) {
        // Khởi tạo LinkedHashSet
        LinkedHashSet linkedHashSet = new LinkedHashSet<>();

        // Thêm phần tử vào LinkedHashSet
        linkedHashSet.add("Cam");
        linkedHashSet.add("Quýt");
        linkedHashSet.add("Mít");
        linkedHashSet.add("Dừa");

        // LinkedHashSet duy trì thứ tự chèn
        System.out.println("LinkedHashSet: " + linkedHashSet); // [Cam, Quýt, Mít, Dừa]

        // Thêm phần tử trùng lặp (sẽ bị bỏ qua)
        linkedHashSet.add("Cam");
        System.out.println("Sau khi thêm phần tử trùng lặp: " + linkedHashSet); // [Cam, Quýt, Mít, Dừa, Java]

        // Kiểm tra sự tồn tại của một phần tử
        System.out.println("LinkedHashSet có chứa 'Mít' không? " + linkedHashSet.contains("Mít")); // true

        // Xóa phần tử
        linkedHashSet.remove("Dừa");
        System.out.println("LinkedHashSet sau khi xóa 'Dừa': " + linkedHashSet); // [Cam, Quýt, Mít]
    }
}
```

### **5\. Ưu và nhược điểm của LinkedHashSet**

*   **Ưu điểm:**
    
    *   Duy trì thứ tự chèn, giúp dễ dàng duyệt qua các phần tử theo thứ tự thêm vào.
        
    *   Hiệu suất tốt với thời gian trung bình O(1) cho các thao tác thêm, xóa và tìm kiếm.
        
*   **Nhược điểm:**
    
    *   Tốn bộ nhớ hơn so với **HashSet** vì phải duy trì danh sách liên kết kép để lưu thứ tự chèn.
        
    *   Không có sắp xếp tự động các phần tử như **TreeSet**.
