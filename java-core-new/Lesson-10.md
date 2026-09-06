# 📘 Bài 10: Khởi Tạo Đối Tượng Với `new`, Constructor, Method, Bộ Nhớ Stack - Heap & Cơ Chế Pass-By-Value Trong Java

---

## 📑 Mục Lục

- [1️⃣ Khởi Tạo Đối Tượng Bằng Từ Khóa `new`](#1-khởi-tạo-đối-tượng-bằng-từ-khóa-new)
  - [1.1 Cú pháp tổng quát](#11-cú-pháp-tổng-quát)
  - [1.2 Bản chất các bước thực thi của `new`](#12-bản-chất-các-bước-thực-thi-của-new)
  - [1.3 Khai báo biến tham chiếu vs Khởi tạo đối tượng](#13-khai-báo-biến-tham-chiếu-vs-khởi-tạo-đối-tượng)
- [2️⃣ Constructor (Hàm Khởi Tạo)](#2-constructor-hàm-khởi-tạo)
  - [2.1 Khái niệm và nguyên tắc bắt buộc](#21-khái-niệm-và-nguyên-tắc-bắt-buộc)
  - [2.2 Constructor không tham số & Default Constructor](#22-constructor-không-tham-số--default-constructor)
  - [2.3 Constructor có tham số](#23-constructor-có-tham-số)
  - [2.4 Constructor Overloading và gọi chéo bằng `this(...)`](#24-constructor-overloading-và-gọi-chéo-bằng-this)
  - [2.5 Phân biệt thuộc tính và tham số bằng từ khóa `this`](#25-phân-biệt-thuộc-tính-và-tham-số-bằng-từ-khóa-this)
- [3️⃣ Thiết Kế Method (Phương Thức)](#3-thiết-kế-method-phương-thức)
  - [3.1 Cấu trúc chuẩn của một Method](#31-cấu-trúc-chuẩn-của-một-method)
  - [3.2 Phân biệt Method `void` và Method có giá trị trả về](#32-phân-biệt-method-void-và-method-có-giá-trị-trả-về)
  - [3.3 Cách Method thao tác trên State (Fields) của Object](#33-cách-method-thao-tác-trên-state-fields-của-object)
- [4️⃣ Ví Dụ Thực Hành: Lớp Hình Chữ Nhật (`Rectangle`)](#4-ví-dụ-thực-hành-lớp-hình-chữ-nhật-rectangle)
  - [4.1 Xây dựng Class `Rectangle`](#41-xây-dựng-class-rectangle)
  - [4.2 Khởi tạo và kiểm thử trong hàm `main`](#42-khởi-tạo-và-kiểm-thử-trong-hàm-main)
- [5️⃣ Giải Phẫu Bộ Nhớ: Stack vs Heap](#5-giải-phẫu-bộ-nhớ-stack-vs-heap)
  - [5.1 Vùng nhớ Stack](#51-vùng-nhớ-stack)
  - [5.2 Vùng nhớ Heap](#52-vùng-nhớ-heap)
  - [5.3 Truy vết luồng thực thi trên Stack và Heap](#53-truy-vết-luồng-thực-thi-trên-stack-và-heap)
  - [5.4 Hiện tượng gán biến đối tượng: Hai tham chiếu, một thực thể](#54-hiện-tượng-gán-biến-đối-tượng-hai-tham-chiếu-một-thực-thể)
- [6️⃣ Cơ Chế Pass-By-Value Trong Java](#6-cơ-chế-pass-by-value-trong-java)
  - [6.1 Khẳng định cốt lõi: Java chỉ có Pass-By-Value](#61-khẳng-định-cốt-lõi-java-chỉ-có-pass-by-value)
  - [6.2 Truyền biến nguyên thủy (Primitive Type)](#62-truyền-biến-nguyên-thủy-primitive-type)
  - [6.3 Truyền biến đối tượng (Reference Type)](#63-truyền-biến-đối-tượng-reference-type)
  - [6.4 Hai thí nghiệm làm sáng tỏ bản chất](#64-hai-thí-nghiệm-làm-sáng-tỏ-bản-chất)
- [📝 KHO 10 BÀI TẬP THỰC HÀNH (KHÔNG CÓ LỜI GIẢI)](#-kho-10-bài-tập-thực-hành-không-có-lời-giải)

---

## 1️⃣ Khởi Tạo Đối Tượng Bằng Từ Khóa `new`

Class chỉ là bản thiết kế trên lý thuyết. Để chuyển bản thiết kế đó thành một đối tượng thực tế tồn tại trong bộ nhớ và có thể hoạt động được, ta dùng từ khóa **`new`**.

### 1.1 Cú pháp tổng quát

```java
TênLớp tênBiến = new TênLớp([danh_sách_đối_số]);
```

Ví dụ:
```java
Rectangle hcn = new Rectangle(12.0, 5.0);
```

Câu lệnh trên bao gồm 2 phần độc lập:
- **Vế trái (`Rectangle hcn`):** Khai báo một biến tham chiếu có tên `hcn` nằm trên vùng nhớ **Stack**.
- **Vế phải (`new Rectangle(12.0, 5.0)`):** Cấp phát một đối tượng thực tế trên vùng nhớ **Heap** và trả về địa chỉ ô nhớ đó cho biến `hcn` nắm giữ.

---

### 1.2 Bản chất các bước thực thi của `new`

Khi gặp từ khóa `new`, JVM sẽ thực hiện lần lượt các công việc sau:
1. **Cấp phát bộ nhớ (Allocation):** Tìm và dành ra một vùng không gian trống trên **Heap** đủ để chứa tất cả các thuộc tính của đối tượng.
2. **Khởi tạo giá trị ngầm định (Zero-initialization):** Tạm thời nạp giá trị mặc định cho các thuộc tính:
   - Kiểu số (`byte`, `short`, `int`, `long`, `float`, `double`): `0` hoặc `0.0`
   - Kiểu `boolean`: `false`
   - Kiểu tham chiếu (Object, String): `null`
3. **Gọi Constructor:** Kích hoạt Constructor tương ứng để khởi tạo dữ liệu nghiệp vụ do lập trình viên chỉ định.
4. **Trả về địa chỉ tham chiếu:** Toán tử `new` trả về địa chỉ ô nhớ trên Heap (ví dụ `0x10A4`). Địa chỉ này được lưu vào biến tham chiếu bên vế trái thông qua phép gán `=`.

---

### 1.3 Khai báo biến tham chiếu vs Khởi tạo đối tượng

Cần phân biệt rõ giữa việc khai báo biến và việc tạo đối tượng:

```java
// 1. Chỉ khai báo: Chưa có đối tượng nào được sinh ra trên Heap
Rectangle r; 

// r đang chứa giá trị mặc định là null. 
// Nếu gọi method lúc này:
// r.calculateArea(); // 💥 Gây lỗi java.lang.NullPointerException

// 2. Khởi tạo đối tượng thật sự:
r = new Rectangle(6.0, 4.0); // Lúc này mới có thực thể trên Heap để thao tác
```

---

## 2️⃣ Constructor (Hàm Khởi Tạo)

### 2.1 Khái niệm và nguyên tắc bắt buộc

Constructor là một khối lệnh đặc biệt được gọi tự động khi một đối tượng được tạo ra thông qua từ khóa `new`. Nhiệm vụ chính của Constructor là nạp giá trị ban đầu cho các thuộc tính của đối tượng.

**3 quy tắc bắt buộc của Constructor:**
1. Tên Constructor **bắt buộc phải trùng hoàn toàn** với tên của Class (kể cả chữ hoa/thường).
2. Constructor **tuyệt đối không có kiểu trả về**, kể cả `void`.
3. Constructor không thể được gọi trực tiếp như hàm thông thường (như `obj.Constructor()`), mà chỉ được kích hoạt đi kèm với từ khóa `new`.

> ⚠️ **Lưu ý quan trọng:** Nếu bạn vô tình thêm `void` vào trước tên Constructor (ví dụ: `public void Rectangle(...)`), Java sẽ coi đó là một method thông thường chứ không còn là Constructor nữa.

---

### 2.2 Constructor không tham số & Default Constructor

- **Default Constructor (Mặc định):** Nếu trong class bạn **không viết bất kỳ constructor nào**, trình biên dịch Java sẽ tự động tạo một constructor rỗng không có tham số:
  ```java
  public class Blank {
      // Trình biên dịch tự ngầm bổ sung: public Blank() {}
  }
  ```
- **Constructor không tham số tự viết (No-Arg Constructor):** Dùng để thiết lập các giá trị mặc định theo quy tắc nghiệp vụ khi người dùng không truyền dữ liệu:
  ```java
  public class Rectangle {
      double length;
      double width;

      public Rectangle() {
          this.length = 1.0;
          this.width = 1.0;
      }
  }
  ```

> 🚨 **Quy tắc mất Default Constructor:** Ngay khi bạn tự định nghĩa **ít nhất một Constructor có tham số**, Java sẽ **không còn tự động sinh ra** Default Constructor rỗng nữa. Nếu muốn sử dụng khởi tạo không tham số `new Rectangle()`, bạn bắt buộc phải tự tay viết nó.

---

### 2.3 Constructor có tham số

Dùng để nhận các giá trị từ bên ngoài truyền vào khi khởi tạo đối tượng:

```java
public class Rectangle {
    double length;
    double width;

    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }
}
```

---

### 2.4 Constructor Overloading và gọi chéo bằng `this(...)`

Một Class có thể chứa nhiều Constructor với danh sách tham số khác nhau (khác số lượng hoặc khác kiểu dữ liệu). Đây là kỹ thuật **nạp chồng Constructor (Constructor Overloading)**.

Để tránh trùng lặp mã nguồn, ta có thể dùng cú pháp `this(...)` để Constructor này tái sử dụng Constructor khác:

```java
public class Rectangle {
    double length;
    double width;

    // Constructor 1: Đầy đủ 2 tham số (Constructor trung tâm)
    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    // Constructor 2: Dành cho hình vuông (1 tham số)
    public Rectangle(double side) {
        this(side, side); // Gọi Constructor 1
    }

    // Constructor 3: Không tham số
    public Rectangle() {
        this(1.0, 1.0);   // Gọi Constructor 1 với giá trị mặc định 1.0
    }
}
```

> **Quy định với `this(...)`:** Lệnh gọi `this(...)` **phải nằm ở dòng lệnh đầu tiên** bên trong thân Constructor.

---

### 2.5 Phân biệt thuộc tính và tham số bằng từ khóa `this`

Khi tên của tham số đầu vào trùng khớp với tên thuộc tính của Class:

```java
public Rectangle(double length, double width) {
    // length = length; // ❌ Sai: biến tham số tự gán lại cho chính nó (Shadowing)
    
    this.length = length; // ✔️ Đúng: this.length là thuộc tính của đối tượng trên Heap
    this.width = width;
}
```

- **`this`**: Đại diện cho chính đối tượng hiện tại đang thực thi trong bộ nhớ.
- **`this.fieldName`**: Chỉ đích danh thuộc tính (field) thuộc về đối tượng trên Heap, phân biệt hoàn toàn với biến tham số cục bộ trên Stack.

---

## 3️⃣ Thiết Kế Method (Phương Thức)

### 3.1 Cấu trúc chuẩn của một Method

Method thể hiện cho **hành vi (Behavior)** của đối tượng.

```
[Phạm vi truy cập] [Kiểu trả về] [Tên method]([Danh sách tham số]) {
    // Khối mã xử lý (Thân method)
    // return [giá trị]; (bắt buộc nếu kiểu trả về khác void)
}
```

Ví dụ:
```java
public double calculateArea() {
    return this.length * this.width;
}
```

- **Phạm vi truy cập (`public`, `private`,...):** Quyết định nơi nào có quyền gọi method này.
- **Kiểu trả về (`return type`):** Định nghĩa kiểu dữ liệu của kết quả sau khi xử lý (`int`, `double`, `String`, `boolean` hoặc `void` nếu không trả về gì).
- **Tên method:** Đặt theo chuẩn `camelCase`, nên dùng động từ (ví dụ: `calculateArea`, `displayInfo`, `deposit`).
- **Danh sách tham số:** Dữ liệu đầu vào cần thiết cho việc xử lý. Nếu phương thức đã dùng các thuộc tính sẵn có của đối tượng thì danh sách này có thể để trống.

---

### 3.2 Phân biệt Method `void` và Method có giá trị trả về

| Đặc điểm | Method kiểu `void` | Method có kiểu trả về cụ thể |
| :--- | :--- | :--- |
| **Mục đích** | Thực hiện một hành động (in ấn, cập nhật dữ liệu) và không cần trả về kết quả. | Thực hiện tính toán và trả kết quả về cho nơi gọi sử dụng tiếp. |
| **Từ khóa `return`** | Không bắt buộc (chỉ dùng `return;` khi muốn thoát sớm). | **Bắt buộc** phải có `return <giá_trị>;` phù hợp với kiểu đã khai báo. |
| **Cách sử dụng** | Đứng độc lập: `rect.displayInfo();` | Gán vào biến hoặc đưa vào biểu thức: `double s = rect.calculateArea();` |

---

### 3.3 Cách Method thao tác trên State (Fields) của Object

Trong lập trình hướng đối tượng, các method của một class có toàn quyền truy cập và thao tác trực tiếp trên các thuộc tính của chính class đó mà không cần phải truyền lại các thuộc tính đó qua tham số hàm:

```java
public class Rectangle {
    double length;
    double width;

    // Không cần viết: calculateArea(double length, double width)
    public double calculateArea() {
        return this.length * this.width; // Đọc trực tiếp từ thuộc tính hiện tại
    }
}
```

---

## 4️⃣ Ví Dụ Thực Hành: Lớp Hình Chữ Nhật (`Rectangle`)

Để liên kết từ khóa `new`, Constructor và Method, chúng ta xây dựng lớp `Rectangle` giải quyết bài toán tính chu vi và diện tích hình chữ nhật.

### 4.1 Xây dựng Class `Rectangle`

```java
public class Rectangle {
    // 1. Fields (Thuộc tính)
    private double length;
    private double width;

    // 2. Constructor đầy đủ tham số (có kiểm tra dữ liệu hợp lệ)
    public Rectangle(double length, double width) {
        if (length > 0 && width > 0) {
            this.length = length;
            this.width = width;
        } else {
            System.out.println("Kích thước không hợp lệ! Gán mặc định về 1.0 x 1.0.");
            this.length = 1.0;
            this.width = 1.0;
        }
    }

    // 3. Constructor 1 tham số (Dành cho hình vuông)
    public Rectangle(double side) {
        this(side, side);
    }

    // 4. Constructor không tham số
    public Rectangle() {
        this(1.0, 1.0);
    }

    // 5. Method tính diện tích
    public double calculateArea() {
        return this.length * this.width;
    }

    // 6. Method tính chu vi
    public double calculatePerimeter() {
        return (this.length + this.width) * 2;
    }

    // 7. Method hiển thị thông tin
    public void displayInfo() {
        System.out.printf("Hình chữ nhật [%.2f x %.2f] | Chu vi: %.2f | Diện tích: %.2f\n",
                this.length, this.width, this.calculatePerimeter(), this.calculateArea());
    }
}
```

---

### 4.2 Khởi tạo và kiểm thử trong hàm `main`

```java
public class Main {
    public static void main(String[] args) {
        // Dùng constructor không tham số
        Rectangle r1 = new Rectangle();
        r1.displayInfo();

        // Dùng constructor 1 tham số (hình vuông)
        Rectangle r2 = new Rectangle(5.0);
        r2.displayInfo();

        // Dùng constructor 2 tham số
        Rectangle r3 = new Rectangle(8.0, 4.0);
        r3.displayInfo();
    }
}
```

**Kết quả khi chạy:**
```text
Hình chữ nhật [1.00 x 1.00] | Chu vi: 4.00 | Diện tích: 1.00
Hình chữ nhật [5.00 x 5.00] | Chu vi: 20.00 | Diện tích: 25.00
Hình chữ nhật [8.00 x 4.00] | Chu vi: 24.00 | Diện tích: 32.00
```

---

## 5️⃣ Giải Phẫu Bộ Nhớ: Stack vs Heap

Java quản lý bộ nhớ lúc thực thi thông qua 2 vùng nhớ chính là **Stack** và **Heap**.

```
       VÙNG NHỚ STACK                               VÙNG NHỚ HEAP
+----------------------------+             +-------------------------------+
| main() Frame:              |             |                               |
|  - Biến r1 = 0x12AB        |------------>| Đối tượng Rectangle (0x12AB)  |
|  - Biến r2 = 0x12AB        |-----/       |  - length: 8.0                |
|                            |             |  - width : 4.0                |
| (Lưu biến cục bộ, tham     |             +-------------------------------+
|  chiếu, giải phóng ngay)   |             | (Chứa thực thể Object thật)   |
+----------------------------+             +-------------------------------+
```

### 5.1 Vùng nhớ Stack
- Hoạt động theo cơ chế **LIFO (Last In, First Out)**.
- Chứa các **Stack Frame**: Mỗi khi một method được gọi, một frame mới được đẩy vào Stack; khi method kết thúc, frame này được giải phóng ngay lập tức.
- Lưu trữ các **biến cục bộ (local variables)** có kiểu nguyên thủy và các **biến tham chiếu (reference variables)** lưu địa chỉ trỏ sang Heap.
- Có tốc độ truy cập rất nhanh nhưng dung lượng giới hạn.

### 5.2 Vùng nhớ Heap
- Là vùng nhớ dùng chung (shared memory) cho toàn bộ ứng dụng.
- Lưu trữ **toàn bộ các Object thực sự** được tạo ra bằng từ khóa `new` cùng các biến instance bên trong đối tượng đó.
- Đối tượng trên Heap tồn tại độc lập với thời gian sống của các hàm, cho đến khi không còn biến tham chiếu nào trỏ tới nó. Lúc này bộ thu dọn rác **Garbage Collector (GC)** sẽ tự động thu hồi bộ nhớ.

---

### 5.3 Truy vết luồng thực thi trên Stack và Heap

Quan sát đoạn mã sau:
```java
public static void main(String[] args) {
    Rectangle r1 = new Rectangle(8.0, 4.0);
    double area = r1.calculateArea();
}
```

1. Khi vào hàm `main()`, một Stack Frame cho `main` được tạo ra.
2. Gặp `new Rectangle(8.0, 4.0)`: Một khối nhớ được cấp phát trên **Heap** tại địa chỉ giả định `0x12AB` để chứa `length = 8.0` và `width = 4.0`.
3. Biến `r1` trên **Stack** được gán giá trị là địa chỉ `0x12AB`.
4. Gọi `r1.calculateArea()`: Một Stack Frame mới cho `calculateArea` được mở trên đỉnh Stack, nhận ngầm định tham chiếu `this = 0x12AB`.
5. Khi tính toán xong ($8.0 \times 4.0 = 32.0$), giá trị $32.0$ trả về cho biến `area` trên Frame của `main`, sau đó Stack Frame của `calculateArea` lập tức bị hủy bỏ.

---

### 5.4 Hiện tượng gán biến đối tượng: Hai tham chiếu, một thực thể

```java
Rectangle r1 = new Rectangle(10.0, 5.0);
Rectangle r2 = r1; // Phép gán giữa hai biến đối tượng
```

**Bản chất:**
- Phép gán `r2 = r1` **không tạo ra đối tượng Rectangle mới trên Heap**.
- Phép gán này chỉ sao chép con số địa chỉ ô nhớ từ `r1` sang `r2`.
- Kết quả: Cả `r1` và `r2` cùng trỏ đến **duy nhất một đối tượng** trên Heap.

Nếu ta thay đổi qua `r2`:
```java
r2.length = 50.0;
System.out.println(r1.length); // Kết quả in ra là 50.0
```

---

## 6️⃣ Cơ Chế Pass-By-Value Trong Java

### 6.1 Khẳng định cốt lõi: Java chỉ có Pass-By-Value

Trong ngôn ngữ lập trình Java:
> **"Java is strictly PASS-BY-VALUE." (Java hoàn toàn là truyền tham trị, không có Pass-by-reference).**

Mọi đối số khi truyền vào một phương thức đều được **sao chép giá trị (copy value)** sang một biến tham số mới trên Stack Frame của phương thức đó.

---

### 6.2 Truyền biến nguyên thủy (Primitive Type)

Với các kiểu dữ liệu nguyên thủy (`int`, `double`, `boolean`...): Giá trị của biến chính là dữ liệu thực. Khi truyền vào hàm, hàm nhận được một bản sao độc lập.

```java
public static void changeNumber(int a) {
    a = 100; // Chỉ thay đổi bản sao a trong Stack Frame của hàm changeNumber
}

public static void main(String[] args) {
    int x = 10;
    changeNumber(x);
    System.out.println(x); // Vẫn in ra 10 (không thay đổi)
}
```

---

### 6.3 Truyền biến đối tượng (Reference Type)

Với các kiểu đối tượng: Giá trị của biến tham chiếu chính là **địa chỉ ô nhớ** của đối tượng trên Heap (ví dụ: `0x12AB`).

Khi truyền đối tượng vào hàm, Java sao chép con số địa chỉ đó đưa cho tham số của hàm. Do đó, cả biến bên ngoài và tham số bên trong hàm đều đang cùng trỏ tới cùng một đối tượng trên Heap.

---

### 6.4 Hai thí nghiệm làm sáng tỏ bản chất

#### Thí nghiệm 1: Thay đổi thuộc tính bên trong Object (Giá trị bên ngoài CÓ thay đổi)
```java
public static void modifyObject(Rectangle r) {
    r.length = 99.0; // Thao tác trực tiếp vào thuộc tính của Object tại ô nhớ chung
}

public static void main(String[] args) {
    Rectangle box = new Rectangle(10.0, 5.0);
    modifyObject(box);
    System.out.println(box.length); // In ra 99.0 (Đã bị thay đổi)
}
```
*Giải thích:* Do tham số `r` giữ bản sao địa chỉ của `box`, lệnh `r.length = 99.0` tìm đến đúng địa chỉ đó trên Heap để ghi đè dữ liệu.

#### Thí nghiệm 2: Gán `new` đối tượng mới bên trong hàm (Giá trị bên ngoài KHÔNG thay đổi)
```java
public static void reassignObject(Rectangle r) {
    r = new Rectangle(200.0, 100.0); // r nhận địa chỉ của một đối tượng hoàn toàn mới
    r.length = 300.0;
}

public static void main(String[] args) {
    Rectangle box = new Rectangle(10.0, 5.0);
    reassignObject(box);
    System.out.println(box.length); // Vẫn in ra 10.0 (Không hề đổi!)
}
```
*Giải thích:* Lệnh `r = new Rectangle(...)` chỉ gán địa chỉ ô nhớ mới cho biến tham số cục bộ `r`. Biến `box` ở hàm `main` vẫn giữ nguyên địa chỉ ô nhớ ban đầu. Điều này khẳng định Java không hề truyền tham chiếu gốc (Pass-by-reference).

---

## 📝 KHO 10 BÀI TẬP THỰC HÀNH (KHÔNG CÓ LỜI GIẢI)

> **🎯 Hướng dẫn tự luyện:**  
> Đọc kỹ các yêu cầu, tự thiết kế cấu trúc Class, Constructor và Method trên IDE. Tuyệt đối không xem đáp án trước khi tự viết code hoàn chỉnh và chạy thử đúng các Test Case mẫu.

---

### 🟢 CẤP ĐỘ 1: CƠ BẢN & NẮM VỮNG CÚ PHÁP

#### 📌 Bài 1: Xây dựng Lớp Hình Tròn (`Circle`)
- **Mục tiêu:** Thực hành viết Constructor có/không tham số, phương thức tính toán với số thực và hằng số `Math.PI`.
- **Yêu cầu kỹ thuật:**
  1. Tạo class `Circle` với thuộc tính riêng `radius` (`double`).
  2. Tạo Constructor không tham số: mặc định gán `radius = 1.0`.
  3. Tạo Constructor có tham số: nhận vào `radius`. Nếu giá trị truyền vào $\le 0$ thì gán mặc định `1.0`.
  4. Viết các method:
     - `calculateArea()`: Trả về diện tích $S = \pi \times r^2$.
     - `calculatePerimeter()`: Trả về chu vi $C = 2 \times \pi \times r$.
     - `displayInfo()`: In ra bán kính, chu vi và diện tích với định dạng 2 chữ số thập phân.
  5. Trong hàm `main`: Tạo một hình tròn bán kính mặc định và một hình tròn bán kính `4.2`, sau đó in thông tin kiểm tra.
- **Test Case mẫu:**
  ```text
  [Hình tròn 1] Bán kính: 1.00 | Chu vi: 6.28 | Diện tích: 3.14
  [Hình tròn 2] Bán kính: 4.20 | Chu vi: 26.39 | Diện tích: 55.42
  ```

---

#### 📌 Bài 2: Quản lý Điểm Sinh Viên (`Student`)
- **Mục tiêu:** Khởi tạo đối tượng từ nhiều tham số kiểu dữ liệu khác nhau, viết method đánh giá logic rẽ nhánh.
- **Yêu cầu kỹ thuật:**
  1. Class `Student` gồm các thuộc tính: `studentId` (`String`), `fullName` (`String`), `theoryScore` (`double`), `practiceScore` (`double`).
  2. Constructor:
     - Constructor không tham số: `studentId = "Chưa có"`, `fullName = "Chưa có"`, các điểm bằng `0.0`.
     - Constructor đầy đủ 4 tham số để nạp dữ liệu cụ thể.
  3. Viết các method:
     - `calculateAverage()`: Tính và trả về điểm trung bình cộng của 2 đầu điểm.
     - `getRank()`: Trả về xếp loại học lực:
       - Điểm TB $\ge 8.0 \rightarrow$ `"Giỏi"`
       - $6.5 \le$ Điểm TB $< 8.0 \rightarrow$ `"Khá"`
       - $5.0 \le$ Điểm TB $< 6.5 \rightarrow$ `"Trung Bình"`
       - Điểm TB $< 5.0 \rightarrow$ `"Yếu"`
     - `printDetails()`: In ra Mã sinh viên, Họ tên, Điểm TB và Xếp loại.
- **Test Case mẫu:**
  ```text
  Mã SV: SV01 | Họ tên: Nguyễn Văn An | Điểm TB: 8.25 | Xếp loại: Giỏi
  Mã SV: SV02 | Họ tên: Trần Thị Mai  | Điểm TB: 6.25 | Xếp loại: Trung Bình
  ```

---

#### 📌 Bài 3: Quản lý Thông Tin Sách (`Book`) & Khuyến Mãi
- **Mục tiêu:** Rèn luyện Constructor Overloading với lệnh gọi `this(...)` và method thay đổi trạng thái thuộc tính.
- **Yêu cầu kỹ thuật:**
  1. Class `Book` gồm: `title` (`String`), `author` (`String`), `price` (`double`), `publishYear` (`int`).
  2. Xây dựng 3 constructors:
     - Constructor 1: Đầy đủ 4 tham số.
     - Constructor 2: Nhận `title`, `author`, `price`. Gán ngầm định `publishYear = 2026` bằng cách gọi `this(...)`.
     - Constructor 3: Không tham số. Gán tiêu đề `"Chưa rõ"`, giá bằng `0.0`, năm xuất bản `2026` bằng `this(...)`.
  3. Viết các method:
     - `applyDiscount(double discountPercentage)`: Giảm trực tiếp thuộc tính `price` theo phần trăm truyền vào (ví dụ truyền 10 tức là giảm 10%). Nếu phần trăm giảm không nằm trong khoảng $[0, 100]$ thì không áp dụng.
     - `display()`: In thông tin sách và giá hiện tại.
- **Test Case mẫu:**
  ```text
  Sách: Lập trình Java Core | Tác giả: Taylor | Năm XB: 2026 | Giá gốc: 150,000 VND
  Áp dụng giảm giá 20%...
  Giá sau giảm: 120,000 VND
  ```

---

### 🟡 CẤP ĐỘ 2: KHÁ & THAO TÁC ĐỐI TƯỢNG

#### 📌 Bài 4: Mô Phỏng Tài Khoản Ngân Hàng (`BankAccount`)
- **Mục tiêu:** Xử lý tương tác giữa 2 đối tượng độc lập qua tham số hàm (Pass-by-value tham chiếu).
- **Yêu cầu kỹ thuật:**
  1. Class `BankAccount` gồm: `accountNumber` (`String`), `ownerName` (`String`), `balance` (`double`).
  2. Constructor:
     - Không tham số: khởi tạo số dư bằng `0.0`.
     - Có 3 tham số: số dư khởi tạo tối thiểu phải $\ge 50,000$ VND.
  3. Viết các method:
     - `deposit(double amount)`: Nạp thêm tiền (số tiền nạp phải $> 0$).
     - `withdraw(double amount)`: Rút tiền. Điều kiện rút: `amount > 0` và `(balance - amount) >= 50000` (giữ lại số dư tối thiểu). Trả về `true` nếu thành công, `false` nếu thất bại.
     - `transferTo(BankAccount targetAccount, double amount)`: Chuyển tiền sang tài khoản `targetAccount`. Nếu rút tiền từ tài khoản hiện tại thành công thì nạp số tiền đó vào `targetAccount` và trả về `true`.
- **Test Case mẫu:**
  ```text
  Tài khoản A (Số dư: 200,000 VND) chuyển 100,000 VND sang Tài khoản B (Số dư: 50,000 VND).
  Kết quả chuyển tiền: Thành công!
  Số dư mới tài khoản A: 100,000 VND
  Số dư mới tài khoản B: 150,000 VND
  ```

---

#### 📌 Bài 5: Quản lý Nhân Viên (`Employee`) & Xét Thưởng
- **Mục tiêu:** Kiểm tra ràng buộc hợp lệ dữ liệu trong Constructor và viết phương thức so sánh giữa 2 đối tượng.
- **Yêu cầu kỹ thuật:**
  1. Class `Employee` gồm: `id` (`String`), `name` (`String`), `basicSalary` (`double`), `rating` (`char` - xếp loại: 'A', 'B', 'C').
  2. Constructor nhận 4 tham số: Nếu `basicSalary < 4500000` thì gán lương cơ bản về `4500000`.
  3. Viết các method:
     - `calculateTotalSalary()`: Tính tổng thu nhập thực lĩnh:
       - Loại 'A': Thưởng 20% lương cơ bản (Hệ số 1.2).
       - Loại 'B': Thưởng 10% lương cơ bản (Hệ số 1.1).
       - Loại 'C': Giữ nguyên lương cơ bản (Hệ số 1.0).
     - `compareSalary(Employee other)`: So sánh lương thực lĩnh của đối tượng hiện tại (`this`) với một đối tượng nhân viên `other` được truyền vào. In ra màn hình tên nhân viên có thu nhập cao hơn.
- **Test Case mẫu:**
  ```text
  Nhân viên 1: Lê Văn Minh (Loại A) -> Thu nhập: 12,000,000 VND
  Nhân viên 2: Phạm Thu Hà (Loại B) -> Thu nhập: 11,000,000 VND
  Kết quả so sánh: Lê Văn Minh có thu nhập cao hơn Phạm Thu Hà.
  ```

---

#### 📌 Bài 6: Bài Thí Nghiệm Bộ Nhớ Stack - Heap & Pass-By-Value
- **Mục tiêu:** Kiểm tra tư duy bộ nhớ và giải thích cơ chế truyền giá trị thông qua mã nguồn thực tế.
- **Yêu cầu kỹ thuật:**
  1. Sử dụng lại Class `Rectangle` (gồm 2 thuộc tính `length`, `width`).
  2. Tạo class `MemoryTest` với 3 method tĩnh (`static`):
     - `actionOne(int number)`: Cộng thêm 10 vào biến `number`.
     - `actionTwo(Rectangle rect)`: Nhân đôi chiều dài `rect.length = rect.length * 2`.
     - `actionThree(Rectangle rect)`: Gán `rect = new Rectangle(50.0, 50.0);` và sau đó đổi `rect.length = 999.0;`.
  3. Trong hàm `main`:
     - Khai báo `int n = 5;` và `Rectangle r = new Rectangle(10.0, 5.0);`.
     - Gọi `actionOne(n);` $\rightarrow$ In lại giá trị của `n`.
     - Gọi `actionTwo(r);` $\rightarrow$ In lại giá trị của `r.length`.
     - Gọi `actionThree(r);` $\rightarrow$ In lại giá trị của `r.length`.
  4. **Yêu cầu tự trả lời:** Viết ghi chú (comment) giải thích chính xác tại sao `n` không đổi, tại sao sau `actionTwo` thì `length` đổi, và tại sao sau `actionThree` thì `length` không bị đổi thành 999.0.

---

### 🟠 CẤP ĐỘ 3: NÂNG CAO & TƯ DUY TOÁN HỌC / THỜI GIAN

#### 📌 Bài 7: Xây dựng Lớp Phân Số (`Fraction`)
- **Mục tiêu:** Thiết kế đối tượng có phương thức trả về một đối tượng mới (`return new ...`), giải thuật tìm ước chung lớn nhất (UCLN).
- **Yêu cầu kỹ thuật:**
  1. Class `Fraction` gồm: `numerator` (tử số - `int`), `denominator` (mẫu số - `int`).
  2. Constructor:
     - Nhận vào tử số và mẫu số. Nếu mẫu số $= 0$, thông báo lỗi và tự gán mẫu số thành `1`.
     - Tự động chuẩn hóa dấu: Nếu mẫu số âm, đổi dấu cả tử và mẫu để mẫu luôn dương.
     - Tự động tối giản phân số ngay khi khởi tạo.
  3. Viết các method:
     - `add(Fraction other)`: Trả về một đối tượng `Fraction` mới là kết quả phép cộng.
     - `subtract(Fraction other)`: Trả về đối tượng `Fraction` mới là kết quả phép trừ.
     - `multiply(Fraction other)`: Trả về đối tượng `Fraction` mới là kết quả phép nhân.
     - `divide(Fraction other)`: Trả về đối tượng `Fraction` mới là kết quả phép chia.
     - `print()`: In phân số dạng `tu/mau` (nếu mẫu số $= 1$ thì chỉ in tử số).
- **Test Case mẫu:**
  ```text
  Phân số 1: 4/8  -> Tự động tối giản thành: 1/2
  Phân số 2: 2/3
  Cộng: 1/2 + 2/3 = 7/6
  Nhân: 1/2 * 2/3 = 1/3
  ```

---

#### 📌 Bài 8: Mô phỏng Đồng Hồ Thời Gian (`MyTime`)
- **Mục tiêu:** Xử lý sự thay đổi trạng thái theo chu kỳ và liên kết giữa các Constructor.
- **Yêu cầu kỹ thuật:**
  1. Class `MyTime` gồm 3 thuộc tính: `hour` ($0 \le \text{hour} < 24$), `minute` ($0 \le \text{minute} < 60$), `second` ($0 \le \text{second} < 60$).
  2. Constructor:
     - Không tham số: Khởi tạo thời gian `00:00:00`.
     - 3 tham số `(hour, minute, second)`: Kiểm tra tính hợp lệ, nếu không hợp lệ thì gán `0`.
     - 1 tham số `(int totalSeconds)`: Nhận tổng số giây trôi qua trong ngày và tự quy đổi về giờ, phút, giây tương ứng.
  3. Viết các method:
     - `nextSecond()`: Tăng thời gian thêm đúng 1 giây. Nếu vượt qua `23:59:59` thì chuyển thành `00:00:00`.
     - `previousSecond()`: Giảm thời gian đi đúng 1 giây. Nếu đang là `00:00:00` thì lùi về `23:59:59`.
     - `display()`: In theo định dạng chuẩn 2 chữ số `HH:mm:ss` (ví dụ `08:05:09`).
- **Test Case mẫu:**
  ```text
  Thời gian ban đầu: 23:59:59
  Gọi nextSecond()  -> 00:00:00
  Gọi previousSecond() -> 23:59:59
  ```

---

### 🔴 CẤP ĐỘ 4: THỰC TẾ & KẾT HỢP NHIỀU ĐỐI TƯỢNG

#### 📌 Bài 9: Hệ Thống Giỏ Hàng Siêu Thị Mini (`CartItem` & `ShoppingCart`)
- **Mục tiêu:** Tổ chức mối quan hệ giữa các đối tượng (Class chứa mảng các Class khác) và tính toán chiết khấu.
- **Yêu cầu kỹ thuật:**
  1. Class `CartItem` (Món hàng trong giỏ):
     - Thuộc tính: `itemName` (`String`), `unitPrice` (`double`), `quantity` (`int`).
     - Constructor đầy đủ tham số (kiểm tra `quantity > 0` và `unitPrice > 0`).
     - Method `getSubtotal()`: Trả về thành tiền = `unitPrice * quantity`.
  2. Class `ShoppingCart`:
     - Thuộc tính: Mảng `CartItem[] items`, số lượng món hiện có trong giỏ `count`.
     - Constructor `ShoppingCart(int maxItems)`: Tạo giỏ hàng có sức chứa tối đa `maxItems`.
     - Method `addItem(CartItem item)`: Thêm một món vào giỏ (nếu giỏ chưa đầy).
     - Method `calculateTotal()`: Tính tổng tiền các món trong giỏ.
     - Method `applyCoupon(String code)`: Trả về số tiền được giảm giá:
       - Mã `"SALE10"`: Giảm 10% tổng hóa đơn (tối đa 50,000 VND).
       - Mã `"FREESHIP"`: Giảm trực tiếp 30,000 VND.
     - Method `printReceipt()`: In danh sách từng mặt hàng, tổng tiền trước giảm giá, tiền giảm giá và số tiền thanh toán cuối cùng.
- **Test Case mẫu:**
  ```text
  Giỏ hàng gồm:
  - Bánh mì: 2 cái x 15,000 = 30,000 VND
  - Sữa chua: 4 hộp x 10,000 = 40,000 VND
  Tổng phụ: 70,000 VND
  Áp mã SALE10: Giảm 7,000 VND
  Phải thanh toán: 63,000 VND
  ```

---

#### 📌 Bài 10: Quản Lý Đặt Vé Xem Phim (`MovieTicket`)
- **Mục tiêu:** Bài toán tổng hợp toàn diện: Ràng buộc nghiệp vụ chặt chẽ, nhiều Constructor nạp chồng, kiểm soát trạng thái đặt vé.
- **Yêu cầu kỹ thuật:**
  1. Class `MovieTicket` gồm các thuộc tính:
     - `movieTitle` (`String`): Tên phim.
     - `seatCode` (`String`): Mã ghế (ví dụ: `"A05"`, `"VIP01"`).
     - `isVip` (`boolean`): Ghế VIP hay thường.
     - `isWeekend` (`boolean`): Suất chiếu ngày cuối tuần hay không.
     - `isBooked` (`boolean`): Đã được đặt hay chưa (mặc định ban đầu `false`).
  2. Constructor:
     - Constructor 1: Nhận `movieTitle` và `seatCode`. Tự động kiểm tra: Nếu `seatCode` bắt đầu bằng chữ `"VIP"` (dùng `seatCode.startsWith("VIP")`) thì tự gán `isVip = true`, ngược lại `false`. Mặc định `isWeekend = false`.
     - Constructor 2: Nhận `movieTitle`, `seatCode`, `isWeekend`. Tự động phân loại `isVip` tương tự như trên.
  3. Viết các method:
     - `calculatePrice()`: Trả về giá vé dựa trên quy tắc:
       - Giá vé chuẩn: $80,000$ VND.
       - Nếu là cuối tuần: phụ thu thêm $20,000$ VND.
       - Nếu là ghế VIP: phụ thu thêm $30,000$ VND.
     - `book()`: Đặt vé. Nếu vé chưa được đặt (`isBooked == false`), chuyển trạng thái thành `true` và in `"Đặt vé thành công!"`. Nếu vé đã bị đặt trước đó, in thông báo lỗi `"Lỗi: Ghế đã có người đặt!"`.
     - `cancel()`: Hủy đặt vé, đưa `isBooked` về lại `false`.
     - `printTicketInfo()`: In ra tên phim, mã ghế, loại ghế (VIP/Thường), loại ngày (Cuối tuần/Ngày thường), giá vé và trạng thái đặt.
- **Test Case mẫu:**
  ```text
  Vé 1: Phim: Nhà Bà Nữ | Ghế: VIP02 | Loại: Ghế VIP | Ngày: Cuối tuần
  -> Giá vé: 130,000 VND
  -> Gọi book(): Đặt vé thành công!
  -> Gọi book() lần nữa: Lỗi: Ghế đã có người đặt!
  ```

---
