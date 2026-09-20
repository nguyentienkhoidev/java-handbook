# Lesson 14: Tính Trừu Tượng, Độ Phức Tạp Thuật Toán và Tổng Quan Collection (ArrayList)

Trong bài học này, chúng ta sẽ đi sâu vào tính chất quan trọng cuối cùng của Lập trình hướng đối tượng: **Tính trừu tượng (Abstraction)** thông qua **Abstract Class** và **Interface**. Sau đó, chúng ta sẽ tìm hiểu về khái niệm nền tảng trong khoa học máy tính: **Độ phức tạp thuật toán (Big O)**, trước khi bước vào thế giới của **Java Collection Framework** với đại diện đầu tiên là **ArrayList**.

---

## 1. Tính Trừu Tượng (Abstraction) trong OOP

**Tính trừu tượng** là quá trình ẩn đi các chi tiết triển khai bên trong (implementation details) phức tạp và chỉ hiển thị ra ngoài những tính năng, giao diện cần thiết cho người sử dụng.

*   **Ví dụ thực tế:** Khi bạn sử dụng một chiếc tivi, bạn chỉ cần dùng remote để bật, tắt, chuyển kênh (giao diện sử dụng) mà không cần phải hiểu bên trong tivi các bo mạch điện tử hoạt động như thế nào (chi tiết triển khai bị ẩn đi).
*   **Mục đích:** Giảm sự phức tạp, tăng tính bảo mật, và tập trung vào "ĐỐI TƯỢNG LÀM GÌ" thay vì "ĐỐI TƯỢNG LÀM ĐIỀU ĐÓ NHƯ THẾ NÀO".

Trong Java, tính trừu tượng được thực hiện bằng 2 cách:
1.  **Abstract Class** (Lớp trừu tượng) - Trừu tượng một phần (0% - 100%)
2.  **Interface** (Giao diện) - Trừu tượng hoàn toàn (100% trước Java 8)

---

## 2. Abstract Class (Lớp Trừu Tượng)

**Abstract Class** là một lớp được khai báo với từ khóa `abstract`. Nó mang tính chất của một bản thiết kế "chưa hoàn thiện".

### Đặc điểm:
-   **Không thể khởi tạo đối tượng:** Bạn không thể dùng từ khóa `new` để tạo instance của một abstract class (vd: `new Animal()` là lỗi nếu Animal là abstract).
-   **Có thể chứa Abstract Method (phương thức trừu tượng):** Là phương thức chỉ có khai báo mà không có thân hàm (body). Các lớp con *bắt buộc* phải ghi đè (override) và triển khai thân hàm cho các phương thức này.
-   **Có thể chứa Non-abstract Method:** Có thể chứa các phương thức bình thường (có thân hàm), thuộc tính, và constructor như class bình thường.

### Ví dụ:

```java
abstract class Animal {
    // Thuộc tính
    String name;

    // Constructor của abstract class
    public Animal(String name) {
        this.name = name;
    }

    // Abstract method (không có body)
    public abstract void makeSound();

    // Regular method
    public void sleep() {
        System.out.println(name + " is sleeping: Zzz...");
    }
}

class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }

    // Bắt buộc phải triển khai (implement) abstract method
    @Override
    public void makeSound() {
        System.out.println(name + " barks: Woof woof!");
    }
}

public class Main {
    public static void main(String[] args) {
        // Animal a = new Animal("Lulu"); // LỖI COMPILER
        
        Animal dog = new Dog("Milu"); // Đa hình
        dog.makeSound(); // Output: Milu barks: Woof woof!
        dog.sleep();     // Output: Milu is sleeping: Zzz...
    }
}
```

---

## 3. Interface (Giao Diện)

**Interface** trong Java là một bản thiết kế (blueprint) của một class. Nó giống như một bản hợp đồng: bất kỳ class nào "ký hợp đồng" (implements) với interface đều phải tuân thủ việc thực hiện các hành động mà interface quy định.

### Đặc điểm:
-   Sử dụng từ khóa `interface`.
-   **Trước Java 8:** Interface chỉ chứa các hằng số (mặc định là `public static final`) và các phương thức trừu tượng (mặc định là `public abstract`).
-   **Từ Java 8 trở đi:** Interface có thể chứa `default methods` (phương thức có body mặc định) và `static methods`.
-   Một class có thể **implements (triển khai) nhiều Interface**, đây là cách Java khắc phục việc không hỗ trợ đa kế thừa đối với class.

### Ví dụ:

```java
interface Flyable {
    // Thuộc tính ngầm định là public static final
    int MAX_SPEED = 1000; 

    // Phương thức ngầm định là public abstract
    void fly(); 
}

interface Swimmable {
    void swim();
}

// Một class có thể implement nhiều interface
class Duck implements Flyable, Swimmable {
    @Override
    public void fly() {
        System.out.println("Duck is flying at low altitude.");
    }

    @Override
    public void swim() {
        System.out.println("Duck is swimming in the pond.");
    }
}
```

---

## 4. So sánh Abstract Class và Interface

| Tiêu chí | Abstract Class | Interface |
| :--- | :--- | :--- |
| **Bản chất** | Biểu diễn mối quan hệ **"IS-A"** (Là một). Vd: Chó *là một* Động Vật. | Biểu diễn mối quan hệ **"HAS-A"** hoặc khả năng (Can-do). Vd: Vịt *có khả năng* Bay. |
| **Đa kế thừa** | Một class chỉ có thể `extends` **MỘT** class (kể cả abstract). | Một class có thể `implements` **NHIỀU** interface. |
| **Phương thức** | Chứa cả phương thức abstract và non-abstract. | Chỉ chứa abstract (trừ default/static từ Java 8). |
| **Biến (Variables)** | Có thể chứa mọi loại biến (private, protected, non-final...). | Biến mặc định luôn là `public static final` (hằng số). |
| **Constructor** | **Có** constructor (để lớp con gọi qua `super()`). | **Không** có constructor. |

> **Khi nào dùng gì?**
> *   Dùng **Abstract Class** khi các class có cùng chung bản chất, muốn chia sẻ chung code (thuộc tính, phương thức đã được viết sẵn).
> *   Dùng **Interface** khi muốn định nghĩa một tập hợp các hành động (hành vi) mà nhiều class không liên quan đến nhau đều có thể thực hiện (vd: class MáyBay và class Chim không liên quan, nhưng đều có thể `implements Flyable`).

---

## 5. Độ Phức Tạp Thuật Toán (Big O Notation)

Khi giải quyết một bài toán, có thể có nhiều cách viết code (thuật toán) khác nhau. Để so sánh cách nào tốt hơn, người ta đánh giá 2 yếu tố:
1.  **Time Complexity (Độ phức tạp thời gian):** Thuật toán mất bao lâu để chạy khi kích thước dữ liệu đầu vào ($n$) tăng lên.
2.  **Space Complexity (Độ phức tạp không gian):** Thuật toán tốn bao nhiêu bộ nhớ (RAM) khi dữ liệu đầu vào tăng lên.

Ký hiệu **Big O ($O$)** được dùng để mô tả giới hạn trên (trường hợp xấu nhất - worst case) của thuật toán.

### Các độ phức tạp phổ biến (Từ tốt đến xấu):

1.  **$O(1)$ - Constant Time (Hằng số):**
    *   Thời gian chạy luôn không đổi dù dữ liệu có 1 hay 1 triệu phần tử.
    *   *Ví dụ:* Truy cập phần tử mảng bằng index (`arr[5]`), thêm vào cuối ArrayList.
2.  **$O(\log n)$ - Logarithmic Time (Logarit):**
    *   Rất nhanh. Dữ liệu tăng gấp đôi nhưng số bước chạy chỉ tăng 1. (Thường chia đôi dữ liệu sau mỗi bước).
    *   *Ví dụ:* Tìm kiếm nhị phân (Binary Search).
3.  **$O(n)$ - Linear Time (Tuyến tính):**
    *   Thời gian tăng tỉ lệ thuận với đầu vào (Dữ liệu tăng 10 lần, thời gian tăng 10 lần).
    *   *Ví dụ:* Duyệt vòng lặp `for` qua mảng để tìm một số.
4.  **$O(n \log n)$ - Linearithmic Time:**
    *   *Ví dụ:* Các thuật toán sắp xếp tối ưu như Merge Sort, Quick Sort.
5.  **$O(n^2)$ - Quadratic Time (Bậc 2):**
    *   Chậm. Thường xuất hiện khi có 2 vòng lặp lồng nhau duyệt qua dữ liệu.
    *   *Ví dụ:* Bubble Sort, duyệt mảng 2 chiều.

---

## 6. Tổng Quan Java Collection Framework

**Java Collection Framework (JCF)** là một thư viện chuẩn trong Java cung cấp cấu trúc kiến trúc để **lưu trữ và thao tác** với nhóm các đối tượng (như một mảng nhưng linh hoạt và mạnh mẽ hơn rất nhiều).

### Lợi ích:
-   Không cần phải tự viết lại các cấu trúc dữ liệu cơ bản (Danh sách liên kết, Cây, Bảng băm...).
-   Cung cấp sẵn các thuật toán tìm kiếm, sắp xếp.

### Kiến trúc cơ bản:
Collection Framework phân nhánh thành 2 gốc chính:
1.  **`Collection` Interface:** Cha của các List, Set, Queue.
    *   **`List`:** Danh sách có thứ tự, **cho phép** phần tử trùng lặp.
    *   **`Set`:** Tập hợp không có thứ tự, **không cho phép** phần tử trùng lặp.
    *   **`Queue`:** Hàng đợi (Vào trước ra trước - FIFO).
2.  **`Map` Interface:** Lưu trữ dữ liệu theo cặp **Key - Value** (Khóa - Giá trị). Chú ý: Map *không* kế thừa từ Collection nhưng nằm trong JCF.

---

## 7. Cấu Trúc Dữ Liệu ArrayList

**ArrayList** là lớp (class) phổ biến nhất triển khai (implements) `List` interface. Nó hoạt động như một **Mảng động (Dynamic Array)**.

### Bản chất của ArrayList:
*   Mảng thông thường (vd `int[] arr = new int[5]`) có kích thước cố định. Khi đầy, bạn không thể thêm phần tử.
*   **ArrayList** khắc phục điều này. Bên dưới ArrayList thực chất là một mảng `Object[]`. 
*   **Cơ chế tự động tăng (Resizing):** Khi mảng bên dưới bị đầy, ArrayList tự động tạo một mảng mới lớn hơn (thường bằng 1.5 lần kích thước mảng cũ), copy dữ liệu cũ sang, và trỏ tới mảng mới.

### Đặc điểm:
-   Truy xuất ngẫu nhiên (get/set theo index) rất nhanh: $O(1)$.
-   Giữ nguyên thứ tự chèn dữ liệu.
-   Cho phép chứa phần tử trùng lặp và giá trị `null`.
-   **Chỉ lưu trữ được Object (đối tượng), không lưu được kiểu nguyên thủy.** Phải dùng Wrapper Class (vd: `Integer` thay cho `int`, `Double` thay cho `double`).

### Các phương thức thường dùng (Time Complexity):

```java
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Khởi tạo ArrayList lưu String
        ArrayList<String> names = new ArrayList<>();

        // 1. add(E element): Thêm vào cuối - O(1) (Thường xuyên)
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");

        // 2. add(int index, E element): Chèn vào vị trí - O(n) (Do phải dịch các phần tử sau)
        names.add(1, "David"); // [Alice, David, Bob, Charlie]

        // 3. get(int index): Lấy phần tử - O(1)
        String name = names.get(2); // Trả về "Bob"

        // 4. set(int index, E element): Cập nhật phần tử - O(1)
        names.set(0, "Alex"); // Đổi "Alice" thành "Alex"

        // 5. remove(int index) / remove(Object o): Xóa phần tử - O(n) (Phải dồn các phần tử sau lên)
        names.remove("Charlie");

        // 6. size(): Lấy số lượng phần tử - O(1)
        int size = names.size();

        // 7. Duyệt ArrayList
        for (String n : names) {
            System.out.println(n);
        }
    }
}
```

---

## 8. Bài Tập Thực Hành

Để củng cố kiến thức về OOP (Tính trừu tượng, Đa hình, Kế thừa) kết hợp với **ArrayList**, hãy làm 2 bài tập sau:

### Bài 1: Hệ Thống Quản Lý Cửa Hàng Bán Lẻ (Retail Store)
Xây dựng hệ thống quản lý sản phẩm trong cửa hàng.

**Yêu cầu:**
1. Tạo một abstract class `Product` có:
   - Thuộc tính: `id` (String), `name` (String), `price` (double).
   - Constructor khởi tạo 3 thuộc tính.
   - Abstract method: `double calculateDiscount()`.
   - Method `displayInfo()` in ra thông tin id, name, price.
2. Tạo interface `Taxable` có method `double calculateTax()`.
3. Tạo 2 class con kế thừa `Product` và implements `Taxable`:
   - `Electronics` (Đồ điện tử): Thêm thuộc tính `warrantyPeriod` (tháng).
     - Ghi đè `calculateDiscount()`: Giảm 10% giá tiền.
     - Ghi đè `calculateTax()`: Thuế VAT 10%.
     - Ghi đè `displayInfo()` gọi `super` và in thêm warranty.
   - `Clothing` (Quần áo): Thêm thuộc tính `size` (String - S, M, L).
     - Ghi đè `calculateDiscount()`: Giảm 20% giá tiền.
     - Ghi đè `calculateTax()`: Thuế VAT 5%.
4. Trong lớp `Main`, tạo một `ArrayList<Product>` để lưu trữ danh sách sản phẩm.
5. Thêm một vài đối tượng Electronics và Clothing vào list. Duyệt ArrayList để in thông tin từng sản phẩm, bao gồm cả số tiền giảm giá và tiền thuế phải đóng.

### Bài 2: Quản Lý Nhân Sự Công Ty (Employee Management)
Hệ thống tính lương nhân viên.

**Yêu cầu:**
1. Tạo interface `Evaluatable`:
   - Chứa method: `String evaluatePerformance()`.
2. Tạo abstract class `Employee`:
   - Thuộc tính: `empId` (int), `name` (String), `baseSalary` (double).
   - Constructor khởi tạo.
   - Abstract method `double calculateSalary()`.
   - Method `displayEmployee()` in thông tin cơ bản.
3. Tạo class `FullTimeEmployee` kế thừa `Employee` và implements `Evaluatable`:
   - Thêm thuộc tính `bonus` (double).
   - Lương = `baseSalary` + `bonus`.
   - Đánh giá năng lực: Trả về "Good" nếu bonus > 1000, ngược lại "Average".
4. Tạo class `PartTimeEmployee` kế thừa `Employee`:
   - Thêm thuộc tính `workingHours` (int) và `hourlyRate` (double).
   - Ghi đè `calculateSalary()`: Lương = `workingHours` * `hourlyRate`. (Bỏ qua baseSalary hoặc gán = 0).
5. Trong `Main`, dùng `ArrayList<Employee>` để quản lý. 
6. Viết chức năng tìm kiếm nhân viên theo `name` hoặc tìm ra những nhân viên FullTime có đánh giá là "Good". (Gợi ý: Dùng vòng lặp và `instanceof` để kiểm tra kiểu đối tượng).

### Bài 3: Hệ Thống Quản Lý Trường Học (School Management - Full CRUD)
Xây dựng một hệ thống để quản lý học sinh và giáo viên trong trường học, cung cấp đầy đủ các chức năng Thêm, Đọc (Hiển thị), Sửa, Xóa (CRUD).

**Yêu cầu:**
1. Tạo abstract class `Person`:
   - Thuộc tính: `id` (String), `name` (String), `age` (int).
   - Constructor, getters/setters cho các thuộc tính.
   - Abstract method: `void displayDetails()`.
2. Tạo interface `Role`:
   - Chứa method: `String getRoleName()`.
3. Tạo class `Student` kế thừa `Person` và implements `Role`:
   - Thêm thuộc tính `gpa` (double).
   - Ghi đè `displayDetails()`: In ra thông tin học sinh kèm điểm GPA.
   - Ghi đè `getRoleName()`: Trả về chuỗi `"Student"`.
4. Tạo class `Teacher` kế thừa `Person` và implements `Role`:
   - Thêm thuộc tính `subject` (String).
   - Ghi đè `displayDetails()`: In ra thông tin giáo viên kèm môn học.
   - Ghi đè `getRoleName()`: Trả về chuỗi `"Teacher"`.
5. Tạo class `SchoolManager`:
   - Chứa thuộc tính `ArrayList<Person> personList = new ArrayList<>();`.
   - Viết các phương thức (CRUD):
     - **Create**: `addPerson(Person p)` - Thêm người mới vào danh sách.
     - **Read**: `displayAll()` - Duyệt và gọi `displayDetails()` cho từng người. Có thể in thêm Role của họ bằng cách gọi `getRoleName()`.
     - **Update**: `updatePerson(String id)` - Tìm người theo `id` (nếu có). Cập nhật thông tin (ví dụ `age`, hoặc dùng `instanceof` ép kiểu để cập nhật `gpa` nếu là Student, `subject` nếu là Teacher).
     - **Delete**: `deletePerson(String id)` - Tìm và xóa người khỏi danh sách theo `id`.
6. Trong `Main`, tạo menu console (dùng `Scanner` và vòng lặp `while/switch-case`) để người dùng tương tác chọn các chức năng: 1. Thêm mới, 2. Xem danh sách, 3. Cập nhật, 4. Xóa, 5. Thoát chương trình.

---
*Chúc các bạn hoàn thành tốt bài tập và nắm vững bản chất của Tính Trừu Tượng cũng như ArrayList trong Java!*
