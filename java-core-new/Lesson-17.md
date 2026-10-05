# Lesson 17: HashMap và HashSet trong Java (Lý thuyết và Thực hành)

Trong các bài học trước về Java Collection, chúng ta đã tìm hiểu về `List` (điển hình là `ArrayList`) giúp lưu trữ dữ liệu theo thứ tự và cho phép trùng lặp. Tuy nhiên, trong thực tế có rất nhiều bài toán yêu cầu:
1. Lưu trữ dữ liệu **không trùng lặp** (Ví dụ: danh sách biển số xe, số chứng minh nhân dân).
2. Lưu trữ dữ liệu theo cặp **Khóa - Giá trị (Key - Value)** để tra cứu thông tin cực kỳ nhanh (Ví dụ: tra cứu điểm thi bằng số báo danh, tra cứu thông tin sinh viên qua mã sinh viên).

Để giải quyết 2 bài toán trên, Java cung cấp cho chúng ta `Set` (với triển khai phổ biến nhất là `HashSet`) và `Map` (với triển khai phổ biến nhất là `HashMap`).

---

## 1. HashSet trong Java

### 1.1. Khái niệm
`HashSet` là một lớp implement interface `Set`. Nó kế thừa đầy đủ tính chất của cấu trúc dữ liệu tập hợp trong toán học.

**Đặc điểm cốt lõi của HashSet:**
- **Không cho phép các phần tử trùng lặp (No duplicate elements):** Nếu bạn cố tình thêm một phần tử đã tồn tại vào `HashSet`, nó sẽ tự động bỏ qua (không báo lỗi nhưng hàm `add()` sẽ trả về `false`).
- **Không đảm bảo thứ tự:** Các phần tử trong `HashSet` không được lưu theo thứ tự thêm vào. Trình tự khi bạn in ra có thể hoàn toàn khác với trình tự lúc bạn `add`.
- **Chấp nhận giá trị `null`:** Nhưng chỉ được phép có duy nhất MỘT giá trị `null`.
- **Hiệu năng cực cao:** Thao tác thêm (add), xóa (remove), kiểm tra tồn tại (contains) có độ phức tạp trung bình là O(1) (Nhanh hơn rất nhiều so với ArrayList khi tìm kiếm).

### 1.2. Cách HashSet hoạt động phía sau
Thực chất, bên dưới `HashSet` sử dụng một `HashMap` để lưu trữ dữ liệu. Khi bạn thêm một phần tử vào `HashSet`, Java sẽ lấy phần tử đó làm `Key` trong `HashMap` (còn `Value` là một dummy object cố định). Do `Key` trong `HashMap` là duy nhất, nên `HashSet` tự động có tính chất không trùng lặp.

Để kiểm tra hai đối tượng có trùng nhau hay không, `HashSet` sử dụng cơ chế Hashing kết hợp 2 hàm:
1. `hashCode()`: Băm đối tượng thành một số nguyên (mã băm).
2. `equals()`: So sánh giá trị thực sự của 2 đối tượng nếu chúng có chung mã băm.

> **Lưu ý:** Khi tạo một Class tự định nghĩa (Ví dụ `Student`) và muốn đưa vào `HashSet`, bạn **bắt buộc** phải override (ghi đè) 2 hàm `equals()` và `hashCode()`, nếu không `HashSet` sẽ coi 2 object có cùng nội dung là 2 object khác nhau do chúng nằm ở 2 ô nhớ khác nhau.

### 1.3. Các phương thức phổ biến của HashSet

```java
import java.util.HashSet;
import java.util.Set;

public class HashSetExample {
    public static void main(String[] args) {
        Set<String> vehicles = new HashSet<>();

        // 1. Thêm phần tử
        vehicles.add("Car");
        vehicles.add("Bike");
        vehicles.add("Train");
        
        // Thử thêm phần tử trùng lặp
        boolean isAdded = vehicles.add("Car"); // false, vì "Car" đã tồn tại
        
        // 2. Kích thước
        System.out.println("Số lượng xe: " + vehicles.size()); // 3
        
        // 3. Kiểm tra phần tử có tồn tại không (Rất nhanh O(1))
        if (vehicles.contains("Bike")) {
            System.out.println("Có chứa Bike");
        }
        
        // 4. Xóa phần tử
        vehicles.remove("Train");
        
        // 5. Duyệt HashSet
        // Cách 1: Dùng for-each
        for (String v : vehicles) {
            System.out.println(v); 
        }
        
        // CÁCH 2: Dùng Iterator (Nếu vừa duyệt vừa muốn xóa)
        // ... (Giống bài 15)
        
        // 6. Xóa toàn bộ
        vehicles.clear();
    }
}
```

---

## 2. HashMap trong Java

### 2.1. Khái niệm
`HashMap` là một lớp implement interface `Map`. Nó lưu trữ dữ liệu dưới dạng các cặp **Key - Value** (Khóa - Giá trị).

**Đặc điểm cốt lõi của HashMap:**
- **Key (Khóa) là duy nhất:** Không thể có 2 Key trùng nhau. Nếu bạn `put` một Key đã tồn tại với một Value mới, Value cũ sẽ bị đè (overwrite).
- **Value (Giá trị) có thể trùng lặp:** Nhiều Key khác nhau có thể cùng trỏ tới một Value giống nhau.
- **Có thể chứa `null`:** Cho phép tối đa MỘT Key là `null` và có thể có nhiều Value là `null`.
- **Không đảm bảo thứ tự:** Tương tự `HashSet`, các cặp Key-Value không được lưu theo thứ tự bạn thêm vào.
- **Tốc độ tra cứu siêu phàm:** Tra cứu Value thông qua Key có độ phức tạp trung bình là O(1).

### 2.2. Khi nào nên sử dụng HashMap?
Bạn nên sử dụng HashMap khi bài toán có tính chất "tra từ điển".
- Danh bạ điện thoại: Tra Số điện thoại (Value) theo Tên (Key).
- Tra điểm thi: Tra Điểm (Value) theo Số báo danh (Key).
- Đếm số lần xuất hiện của một từ trong câu: Từ (Key) và Số lần (Value).

### 2.3. Các phương thức phổ biến của HashMap

```java
import java.util.HashMap;
import java.util.Map;

public class HashMapExample {
    public static void main(String[] args) {
        // Khởi tạo HashMap với Key là String, Value là Integer
        Map<String, Integer> studentGrades = new HashMap<>();

        // 1. Thêm cặp Key-Value (Dùng put() thay vì add())
        studentGrades.put("Alice", 9);
        studentGrades.put("Bob", 7);
        studentGrades.put("Charlie", 10);
        
        // Nếu put Key đã tồn tại -> Value cũ sẽ bị đè
        studentGrades.put("Bob", 8); // Bob cập nhật điểm lên 8
        
        // 2. Lấy Value dựa vào Key
        int aliceGrade = studentGrades.get("Alice"); // Trả về 9
        
        // Nếu get Key không tồn tại -> Trả về null
        Integer davidGrade = studentGrades.get("David"); // null
        
        // 3. getOrDefault() - Nếu không có Key thì trả về giá trị mặc định
        int score = studentGrades.getOrDefault("Tom", 0); // Trả về 0 do Tom không tồn tại
        
        // 4. Kiểm tra Key hoặc Value có tồn tại không
        boolean hasAlice = studentGrades.containsKey("Alice"); // true
        boolean hasScore10 = studentGrades.containsValue(10); // true
        
        // 5. Xóa phần tử theo Key
        studentGrades.remove("Charlie"); 
        
        // 6. Duyệt HashMap (Có nhiều cách)
        // Cách thông dụng nhất: Duyệt qua entrySet (Tập hợp các cặp Key-Value)
        for (Map.Entry<String, Integer> entry : studentGrades.entrySet()) {
            System.out.println("Học sinh: " + entry.getKey() + " - Điểm: " + entry.getValue());
        }
        
        // Duyệt riêng Key
        for (String name : studentGrades.keySet()) { 
            // logic
        }
        
        // Duyệt riêng Value
        for (Integer grade : studentGrades.values()) { 
            // logic
        }
    }
}
```

---
---

## 📝 BÀI TẬP THỰC HÀNH

Trong thực tế, người ta rất hay sử dụng kết hợp cả `Map` và `Set` trong cùng một cấu trúc dữ liệu (`Map<Key, Set<Value>>`) để giải quyết bài toán: Một đối tượng (Key) có thể chứa nhiều giá trị duy nhất (Set of Values).

**Yêu cầu:** 
Viết chương trình `CourseManager` mô phỏng hệ thống đăng ký môn học.
- Mỗi Môn học (Ví dụ: `Toan`, `Ly`) sẽ có nhiều Sinh viên đăng ký.
- Yêu cầu mã sinh viên đăng ký vào một môn không được phép trùng lặp (nếu sinh viên đăng ký 2 lần môn Toán thì cũng chỉ ghi nhận 1 lần).

Dưới đây là bộ khung mã nguồn, hãy hoàn thiện các hàm bên trong:

```java
package com.khoinguyen.hash_set;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CourseManager {
    // Key: Tên môn học, Value: Danh sách (Set) các mã sinh viên đăng ký
    private Map<String, Set<String>> courseRegistrations = new HashMap<>();

    /**
     * 1. Sinh viên đăng ký môn học
     */
    public void registerCourse(String courseName, String studentId) {
        // TODO:
        // Bước 1: Lấy Set chứa danh sách sinh viên của môn học courseName từ Map.
        // Bước 2: Nếu Set trả về là null (nghĩa là môn này chưa có ai đăng ký),
        //         hãy tạo một HashSet mới tinh và put vào Map.
        // Bước 3: Add studentId vào Set.
        
    }

    /**
     * 2. Sinh viên hủy đăng ký môn học
     */
    public void dropCourse(String courseName, String studentId) {
        // TODO:
        // Nếu môn học có tồn tại trong Map, hãy lấy Set danh sách sinh viên ra.
        // Sau đó xóa studentId khỏi Set đó.
        
    }

    /**
     * 3. Lấy danh sách tất cả mã sinh viên đang học một môn cụ thể
     */
    public Set<String> getStudents(String courseName) {
        // TODO: 
        // Trả về Set sinh viên của môn học. Nếu môn không tồn tại, trả về một HashSet rỗng để tránh lỗi NullPointerException.
        return null;
    }

    /**
     * 4. In ra toàn bộ hệ thống
     */
    public void printSystem() {
        for (Map.Entry<String, Set<String>> entry : courseRegistrations.entrySet()) {
            System.out.println("Môn: " + entry.getKey() + " - Sĩ số: " + entry.getValue().size());
            System.out.println("   => Danh sách: " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        CourseManager manager = new CourseManager();
        
        manager.registerCourse("Java Core", "SV001");
        manager.registerCourse("Java Core", "SV002");
        manager.registerCourse("Java Core", "SV001"); // Cố tình đăng ký trùng

        manager.registerCourse("Spring Boot", "SV001");
        manager.registerCourse("Spring Boot", "SV003");

        System.out.println("--- TRƯỚC KHI HỦY MÔN ---");
        manager.printSystem();

        manager.dropCourse("Spring Boot", "SV001");

        System.out.println("\n--- SAU KHI SV001 HỦY MÔN SPRING BOOT ---");
        manager.printSystem();
    }
}
```
