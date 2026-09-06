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
- [📝 KHO 10 BÀI TẬP THỰC HÀNH (CÓ LỜI GIẢI MẪU & BÀI TẬP TƯƠNG TỰ)](#-kho-10-bài-tập-thực-hành-có-lời-giải-mẫu--bài-tập-tương-tự)

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

## 📝 KHO 10 BÀI TẬP THỰC HÀNH (CÓ LỜI GIẢI MẪU & BÀI TẬP TƯƠNG TỰ)

> **🎯 Phương pháp học tập hiệu quả:**  
> 1. **Đọc kỹ đề bài & yêu cầu kỹ thuật** của từng bài tập.
> 2. **Tự lập trình trên IDE** trước khi xem đáp án.
> 3. Lời giải mẫu đã được **ẩn mặc định**. Bạn chỉ cần bấm vào nút **"💡 Xem lời giải mẫu hoàn chỉnh"** để đối chiếu mã nguồn và tư duy giải.
> 4. Sau khi hiểu bài mẫu, hãy **tự tay giải bài tập tương tự** ngay bên dưới để khắc sâu kiến thức!

---

### 🟢 CẤP ĐỘ 1: CƠ BẢN & NẮM VỮNG CÚ PHÁP (Bài 1 - Bài 3)

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

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
public class Circle {
    // 1. Thuộc tính (State)
    private double radius;

    // 2. Constructor không tham số
    public Circle() {
        this.radius = 1.0;
    }

    // 3. Constructor có tham số (kèm validation)
    public Circle(double radius) {
        if (radius <= 0) {
            System.out.println("Cảnh báo: Bán kính phải > 0. Gán mặc định về 1.0!");
            this.radius = 1.0;
        } else {
            this.radius = radius;
        }
    }

    // 4. Các phương thức tính toán (Behaviors)
    public double calculateArea() {
        return Math.PI * this.radius * this.radius;
    }

    public double calculatePerimeter() {
        return 2 * Math.PI * this.radius;
    }

    public void displayInfo(String label) {
        System.out.printf("[%s] Bán kính: %.2f | Chu vi: %.2f | Diện tích: %.2f\n",
                label, this.radius, this.calculatePerimeter(), this.calculateArea());
    }

    public static void main(String[] args) {
        Circle c1 = new Circle();
        c1.displayInfo("Hình tròn 1");

        Circle c2 = new Circle(4.2);
        c2.displayInfo("Hình tròn 2");
    }
}
```
</details>

##### 🎯 Bài tập tương tự tự luyện: Lớp Hình Vuông (`Square`)
- **Yêu cầu:** Xây dựng Class `Square` với thuộc tính `side` (`double`).
  - Constructor không tham số: mặc định gán cạnh $= 1.0$.
  - Constructor có tham số `side` (nếu $\le 0$ thì gán mặc định $1.0$).
  - Viết method `calculateArea()` ($S = \text{side}^2$), `calculatePerimeter()` ($P = \text{side} \times 4$), `displayInfo()`.
  - Viết hàm `main` kiểm thử với hình vuông mặc định và hình vuông có cạnh $6.5$.
- **Test Case kỳ vọng:**
  ```text
  [Hình vuông 1] Cạnh: 1.00 | Chu vi: 4.00 | Diện tích: 1.00
  [Hình vuông 2] Cạnh: 6.50 | Chu vi: 26.00 | Diện tích: 42.25
  ```

---

#### 📌 Bài 2: Quản lý Điểm Sinh Viên (`Student`)
- **Mục tiêu:** Khởi tạo đối tượng từ nhiều tham số kiểu dữ liệu khác nhau, viết method đánh giá logic rẽ nhánh `if-else`.
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

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
public class Student {
    private String studentId;
    private String fullName;
    private double theoryScore;
    private double practiceScore;

    // Constructor không tham số
    public Student() {
        this.studentId = "Chưa có";
        this.fullName = "Chưa có";
        this.theoryScore = 0.0;
        this.practiceScore = 0.0;
    }

    // Constructor đầy đủ 4 tham số
    public Student(String studentId, String fullName, double theoryScore, double practiceScore) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.theoryScore = theoryScore;
        this.practiceScore = practiceScore;
    }

    // Tính điểm trung bình
    public double calculateAverage() {
        return (this.theoryScore + this.practiceScore) / 2.0;
    }

    // Xếp loại học lực
    public String getRank() {
        double avg = this.calculateAverage();
        if (avg >= 8.0) return "Giỏi";
        if (avg >= 6.5) return "Khá";
        if (avg >= 5.0) return "Trung Bình";
        return "Yếu";
    }

    // In thông tin chi tiết
    public void printDetails() {
        System.out.printf("Mã SV: %s | Họ tên: %-15s | Điểm TB: %.2f | Xếp loại: %s\n",
                this.studentId, this.fullName, this.calculateAverage(), this.getRank());
    }

    public static void main(String[] args) {
        Student s1 = new Student("SV01", "Nguyễn Văn An", 8.0, 8.5);
        Student s2 = new Student("SV02", "Trần Thị Mai", 6.0, 6.5);

        s1.printDetails();
        s2.printDetails();
    }
}
```
</details>

##### 🎯 Bài tập tương tự tự luyện: Lớp Sản Phẩm (`Product`)
- **Yêu cầu:** Xây dựng Class `Product` với thuộc tính: `productId` (`String`), `productName` (`String`), `importPrice` (`double` - Giá nhập), `sellingPrice` (`double` - Giá bán).
  - Constructor không tham số và Constructor 4 tham số.
  - Method `calculateProfit()`: Lợi nhuận = $\text{sellingPrice} - \text{importPrice}$.
  - Method `evaluateMargin()`: Trả về đánh giá:
    - Nếu Lợi nhuận $> 500,000$ VND $\rightarrow$ `"Siêu Lợi Nhuận"`
    - Nếu Lợi nhuận từ $100,000$ đến $500,000$ $\rightarrow$ `"Lợi Nhuận Tốt"`
    - Nếu Lợi nhuận $> 0$ và $< 100,000$ $\rightarrow$ `"Lợi Nhuận Thấp"`
    - Ngược lại $\rightarrow$ `"Hòa vốn hoặc Lỗ"`
  - Method `printProduct()`: In mã hàng, tên, giá nhập, giá bán, lãi và đánh giá.
- **Test Case kỳ vọng:**
  ```text
  Mã: SP01 | Tên: Tai nghe Bluetooth | Lãi: 200,000 VND | Đánh giá: Lợi Nhuận Tốt
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
     - `applyDiscount(double discountPercentage)`: Giảm trực tiếp thuộc tính `price` theo phần trăm truyền vào (ví dụ truyền 20 tức là giảm 20%). Nếu phần trăm giảm không nằm trong khoảng $[0, 100]$ thì không áp dụng.
     - `display()`: In thông tin sách và giá hiện tại.
- **Test Case mẫu:**
  ```text
  Sách: Lập trình Java Core | Tác giả: Taylor | Năm XB: 2026 | Giá gốc: 150,000 VND
  Áp dụng giảm giá 20%...
  Giá sau giảm: 120,000 VND
  ```

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
public class Book {
    private String title;
    private String author;
    private double price;
    private int publishYear;

    // 1. Constructor nòng cốt 4 tham số
    public Book(String title, String author, double price, int publishYear) {
        this.title = title;
        this.author = author;
        this.price = (price >= 0) ? price : 0.0;
        this.publishYear = publishYear;
    }

    // 2. Constructor 3 tham số (mặc định xuất bản 2026)
    public Book(String title, String author, double price) {
        this(title, author, price, 2026); // Gọi Constructor 4 tham số
    }

    // 3. Constructor không tham số
    public Book() {
        this("Chưa rõ", "Vô danh", 0.0, 2026); // Tái sử dụng Constructor 4 tham số
    }

    // Giảm giá sách
    public void applyDiscount(double discountPercentage) {
        if (discountPercentage >= 0 && discountPercentage <= 100) {
            double discountAmount = this.price * (discountPercentage / 100.0);
            this.price -= discountAmount;
            System.out.printf("Áp dụng giảm giá %.0f%% thành công!\n", discountPercentage);
        } else {
            System.out.println("Phần trăm giảm giá không hợp lệ (phải từ 0 đến 100)!");
        }
    }

    public void display() {
        System.out.printf("Sách: %s | Tác giả: %s | Năm XB: %d | Giá: %,.0f VND\n",
                this.title, this.author, this.publishYear, this.price);
    }

    public static void main(String[] args) {
        Book b = new Book("Lập trình Java Core", "Taylor", 150000);
        b.display();

        b.applyDiscount(20);
        b.display();
    }
}
```
</details>

##### 🎯 Bài tập tương tự tự luyện: Lớp Điện Thoại Thông Minh (`Smartphone`)
- **Yêu cầu:** Class `Smartphone` gồm: `brand` (`String`), `model` (`String`), `price` (`double`), `storageGB` (`int`).
  - Xây dựng Constructor 4 tham số.
  - Xây dựng Constructor 3 tham số nhận `brand`, `model`, `price` (mặc định `storageGB = 128`) gọi chéo qua `this(...)`.
  - Xây dựng Constructor không tham số gán mặc định qua `this(...)`.
  - Method `upgradeStorage(int extraGB, double upgradeFee)`: Tăng dung lượng bộ nhớ thêm `extraGB` và cộng thêm `upgradeFee` vào giá tiền `price`.
  - Method `display()`: In thông tin điện thoại.
- **Test Case kỳ vọng:**
  ```text
  Khởi tạo: iPhone 15 | Apple | 128 GB | 20,000,000 VND
  Nâng cấp thêm 128 GB (+ 3,000,000 VND)...
  Sau nâng cấp: iPhone 15 | Apple | 256 GB | 23,000,000 VND
  ```

---

### 🟡 CẤP ĐỘ 2: KHÁ & THAO TÁC ĐỐI TƯỢNG (Bài 4 - Bài 6)

#### 📌 Bài 4: Mô Phỏng Tài Khoản Ngân Hàng (`BankAccount`) & Chuyển Tiền
- **Mục tiêu:** Xử lý tương tác giữa 2 đối tượng độc lập qua tham số hàm (Pass-by-value tham chiếu).
- **Yêu cầu kỹ thuật:**
  1. Class `BankAccount` gồm: `accountNumber` (`String`), `ownerName` (`String`), `balance` (`double`).
  2. Constructor:
     - Không tham số: khởi tạo số dư bằng `0.0`.
     - Có 3 tham số: số dư khởi tạo tối thiểu phải $\ge 50,000$ VND.
  3. Viết các method:
     - `deposit(double amount)`: Nạp thêm tiền (số tiền nạp phải $> 0$).
     - `withdraw(double amount)`: Rút tiền. Điều kiện: `amount > 0` và `(balance - amount) >= 50000` (duy trì số dư tối thiểu). Trả về `true` nếu thành công, `false` nếu thất bại.
     - `transferTo(BankAccount targetAccount, double amount)`: Chuyển tiền sang tài khoản `targetAccount`. Nếu rút tiền từ tài khoản hiện tại thành công thì gọi tiếp `targetAccount.deposit(amount)` và trả về `true`.
- **Test Case mẫu:**
  ```text
  Tài khoản A (Số dư: 200,000 VND) chuyển 100,000 VND sang Tài khoản B (Số dư: 50,000 VND).
  Kết quả chuyển tiền: Thành công!
  Số dư mới tài khoản A: 100,000 VND
  Số dư mới tài khoản B: 150,000 VND
  ```

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
public class BankAccount {
    private String accountNumber;
    private String ownerName;
    private double balance;

    public BankAccount() {
        this.accountNumber = "000000";
        this.ownerName = "Khách vãng lai";
        this.balance = 0.0;
    }

    public BankAccount(String accountNumber, String ownerName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = (initialBalance >= 50000) ? initialBalance : 50000;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        } else {
            System.out.println("Số tiền nạp phải lớn hơn 0!");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && (this.balance - amount) >= 50000) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    // Phương thức tương tác giữa hai đối tượng độc lập
    public boolean transferTo(BankAccount targetAccount, double amount) {
        if (targetAccount == null) {
            System.out.println("Tài khoản đích không tồn tại!");
            return false;
        }
        if (this.withdraw(amount)) {
            targetAccount.deposit(amount);
            System.out.printf("Chuyển thành công %,.0f VND sang tài khoản %s!\n",
                    amount, targetAccount.accountNumber);
            return true;
        } else {
            System.out.println("Giao dịch chuyển tiền thất bại: Số dư không đủ!");
            return false;
        }
    }

    public void printBalance() {
        System.out.printf("Tài khoản %s (%s) - Số dư: %,.0f VND\n",
                this.accountNumber, this.ownerName, this.balance);
    }

    public static void main(String[] args) {
        BankAccount accA = new BankAccount("1001", "Nguyễn Văn A", 200000);
        BankAccount accB = new BankAccount("1002", "Trần Thị B", 50000);

        accA.printBalance();
        accB.printBalance();

        System.out.println("\n--- Thực hiện chuyển tiền ---");
        accA.transferTo(accB, 100000);

        System.out.println("\n--- Số dư sau giao dịch ---");
        accA.printBalance();
        accB.printBalance();
    }
}
```
</details>

##### 🎯 Bài tập tương tự tự luyện: Lớp Thẻ Điện Thoại SIM (`MobileSim`)
- **Yêu cầu:** Class `MobileSim` gồm: `phoneNumber` (`String`), `carrier` (`String` - Viettel/Vina), `balance` (`double` - Tiền trong tài khoản).
  - Constructor 3 tham số (số dư ban đầu $\ge 0$).
  - Method `topUp(double amount)`: Nạp tiền thẻ cào vào tài khoản.
  - Method `makeCall(int minutes)`: Trừ cước cuộc gọi với giá $1,500$ VND/phút. Nếu đủ tiền thì trừ và trả về `true`, ngược lại thông báo không đủ tiền và trả về `false`.
  - Method `transferCredit(MobileSim receiverSim, double amount)`: Bắn tiền sang SIM khác (phí dịch vụ $1,000$ VND/lần chuyển do người gửi chịu). Trừ tiền người gửi và cộng cho người nhận nếu đủ số dư.
- **Test Case kỳ vọng:**
  ```text
  Sim A (50,000 VND) bắn 20,000 VND cho Sim B (10,000 VND)
  Phí chuyển: 1,000 VND
  Số dư mới Sim A: 29,000 VND
  Số dư mới Sim B: 30,000 VND
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

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
public class Employee {
    private String id;
    private String name;
    private double basicSalary;
    private char rating;

    public Employee(String id, String name, double basicSalary, char rating) {
        this.id = id;
        this.name = name;
        this.basicSalary = (basicSalary >= 4500000) ? basicSalary : 4500000;
        this.rating = rating;
    }

    public double calculateTotalSalary() {
        switch (Character.toUpperCase(this.rating)) {
            case 'A': return this.basicSalary * 1.2;
            case 'B': return this.basicSalary * 1.1;
            case 'C': return this.basicSalary * 1.0;
            default:  return this.basicSalary;
        }
    }

    // So sánh thu nhập giữa đối tượng hiện tại và đối tượng other
    public void compareSalary(Employee other) {
        if (other == null) return;
        double salary1 = this.calculateTotalSalary();
        double salary2 = other.calculateTotalSalary();

        if (salary1 > salary2) {
            System.out.printf("%s (%,.0f VND) có thu nhập cao hơn %s (%,.0f VND)\n",
                    this.name, salary1, other.name, salary2);
        } else if (salary1 < salary2) {
            System.out.printf("%s (%,.0f VND) có thu nhập cao hơn %s (%,.0f VND)\n",
                    other.name, salary2, this.name, salary1);
        } else {
            System.out.printf("Cả hai nhân viên %s và %s có thu nhập bằng nhau (%,.0f VND)\n",
                    this.name, other.name, salary1);
        }
    }

    public static void main(String[] args) {
        Employee e1 = new Employee("NV01", "Lê Văn Minh", 10000000, 'A');
        Employee e2 = new Employee("NV02", "Phạm Thu Hà", 10000000, 'B');

        e1.compareSalary(e2);
    }
}
```
</details>

##### 🎯 Bài tập tương tự tự luyện: Lớp Ô Tô (`Car`) & So Sánh Công Suất
- **Yêu cầu:** Class `Car` gồm: `brand` (`String`), `modelName` (`String`), `horsepower` (`int` - Mã lực), `weight` (`double` - Tấn).
  - Constructor 4 tham số có validation (nếu `horsepower <= 0` thì gán `100`).
  - Method `calculatePowerToWeightRatio()`: Tính tỷ số công suất/trọng lượng = `horsepower / weight`.
  - Method `comparePerformance(Car other)`: So sánh xem chiếc xe nào có tỷ số công suất/trọng lượng lớn hơn (chiếc xe đó có gia tốc tốt hơn) và in kết luận ra màn hình.
- **Test Case kỳ vọng:**
  ```text
  Xe 1: Honda Civic (180 HP, 1.3 tấn) -> Tỷ số: 138.46 HP/tấn
  Xe 2: Toyota Fortuner (200 HP, 2.1 tấn) -> Tỷ số: 95.24 HP/tấn
  Kết luận: Honda Civic có khả năng tăng tốc vượt trội hơn Toyota Fortuner!
  ```

---

#### 📌 Bài 6: Thí Nghiệm Bộ Nhớ Stack - Heap & Pass-By-Value
- **Mục tiêu:** Kiểm tra tư duy bộ nhớ và giải thích cơ chế truyền giá trị thông qua mã nguồn thực tế.
- **Yêu cầu kỹ thuật:**
  1. Sử dụng lại Class `Rectangle` (gồm 2 thuộc tính `length`, `width`).
  2. Tạo class `MemoryTest` với 3 method tĩnh (`static`):
     - `actionOne(int number)`: Cộng thêm 10 vào biến `number`.
     - `actionTwo(Rectangle rect)`: Nhân đôi chiều dài `rect.length = rect.length * 2`.
     - `actionThree(Rectangle rect)`: Gán `rect = new Rectangle(50.0, 50.0);` và sau đó đổi `rect.length = 999.0;`.
  3. Trong hàm `main`:
     - Khai báo `int n = 5;` và `Rectangle r = new Rectangle(10.0, 5.0);`.
     - Gọi lần lượt 3 action và quan sát sự thay đổi giá trị.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh & Giải phẫu bộ nhớ (Click để mở)</b></summary>

```java
public class MemoryTest {
    // Thí nghiệm 1: Kiểu nguyên thủy
    public static void actionOne(int number) {
        number = number + 10;
        // number là bản sao cục bộ trên Stack Frame của actionOne
    }

    // Thí nghiệm 2: Thay đổi thuộc tính đối tượng
    public static void actionTwo(Rectangle rect) {
        rect.length = rect.length * 2;
        // rect giữ bản sao địa chỉ của r, thay đổi trực tiếp thuộc tính trên Heap
    }

    // Thí nghiệm 3: Gán lại tham chiếu
    public static void actionThree(Rectangle rect) {
        rect = new Rectangle(50.0, 50.0); // rect trỏ sang ô nhớ hoàn toàn mới
        rect.length = 999.0;              // Chỉ thay đổi thuộc tính của ô nhớ mới
    }

    public static void main(String[] args) {
        int n = 5;
        Rectangle r = new Rectangle(10.0, 5.0);

        System.out.println("=== THÍ NGHIỆM 1: KIỂU NGUYÊN THỦY ===");
        actionOne(n);
        System.out.println("Giá trị n sau actionOne: " + n); 
        // GIẢI THÍCH: n vẫn = 5. Do Java truyền tham trị, biến number nhận bản sao của 5.

        System.out.println("\n=== THÍ NGHIỆM 2: SỬA THUỘC TÍNH OBJECT ===");
        actionTwo(r);
        System.out.println("Giá trị r.length sau actionTwo: " + r.length); 
        // GIẢI THÍCH: r.length = 20.0. rect và r cùng giữ địa chỉ trỏ vào Object trên Heap.

        System.out.println("\n=== THÍ NGHIỆM 3: GÁN NEW TRONG HÀM ===");
        actionThree(r);
        System.out.println("Giá trị r.length sau actionThree: " + r.length); 
        // GIẢI THÍCH: r.length VẪN = 20.0. Biến r ở main vẫn trỏ vào Object cũ, 
        // phép gán new chỉ làm đổi địa chỉ của biến cục bộ rect trong actionThree.
    }
}
```
</details>

##### 🎯 Bài tập tương tự tự luyện: Lớp Tọa Độ (`Point`) & Thử Thách Hoán Đổi
- **Yêu cầu:** Tạo class `Point` gồm 2 thuộc tính `public int x, y;` và Constructor nhận `(int x, int y)`.
  - Trong class kiểm thử, viết 2 hàm:
    - `public static void swap(Point p1, Point p2)`: Dùng biến tạm đổi `Point temp = p1; p1 = p2; p2 = temp;`.
    - `public static void reset(Point p)`: Gán `p.x = 0; p.y = 0;`.
  - Trong `main`, khởi tạo $P_1(3, 4)$ và $P_2(7, 8)$.
  - Gọi `swap(p1, p2)` $\rightarrow$ Kiểm tra xem $P_1$ và $P_2$ có thực sự bị đổi chỗ cho nhau không? Giải thích vì sao.
  - Gọi `reset(p1)` $\rightarrow$ Kiểm tra tọa độ của $P_1$ và giải thích vì sao.

---

### 🟠 CẤP ĐỘ 3: NÂNG CAO & TƯ DUY TOÁN HỌC / THỜI GIAN (Bài 7 - Bài 8)

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

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
public class Fraction {
    private int numerator;
    private int denominator;

    // Tìm ước số chung lớn nhất (GCD)
    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Tối giản phân số
    private void simplify() {
        int ucln = gcd(this.numerator, this.denominator);
        this.numerator /= ucln;
        this.denominator /= ucln;

        // Chuẩn hóa dấu: mẫu luôn dương
        if (this.denominator < 0) {
            this.numerator = -this.numerator;
            this.denominator = -this.denominator;
        }
    }

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            System.out.println("Lỗi: Mẫu số không được bằng 0! Tự động gán mẫu số = 1.");
            denominator = 1;
        }
        this.numerator = numerator;
        this.denominator = denominator;
        this.simplify(); // Tự động tối giản ngay khi sinh ra
    }

    public Fraction add(Fraction other) {
        int newNum = this.numerator * other.denominator + other.numerator * this.denominator;
        int newDenom = this.denominator * other.denominator;
        return new Fraction(newNum, newDenom); // Trả về đối tượng mới
    }

    public Fraction subtract(Fraction other) {
        int newNum = this.numerator * other.denominator - other.numerator * this.denominator;
        int newDenom = this.denominator * other.denominator;
        return new Fraction(newNum, newDenom);
    }

    public Fraction multiply(Fraction other) {
        return new Fraction(this.numerator * other.numerator, this.denominator * other.denominator);
    }

    public Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            System.out.println("Lỗi chia cho phân số 0! Trả về phân số mặc định 0/1.");
            return new Fraction(0, 1);
        }
        return new Fraction(this.numerator * other.denominator, this.denominator * other.numerator);
    }

    public void print(String message) {
        if (this.denominator == 1) {
            System.out.printf("%s: %d\n", message, this.numerator);
        } else {
            System.out.printf("%s: %d/%d\n", message, this.numerator, this.denominator);
        }
    }

    public static void main(String[] args) {
        Fraction f1 = new Fraction(4, 8); // Tự rút gọn thành 1/2
        Fraction f2 = new Fraction(2, 3);

        f1.print("Phân số 1");
        f2.print("Phân số 2");

        Fraction sum = f1.add(f2);
        sum.print("f1 + f2");

        Fraction prod = f1.multiply(f2);
        prod.print("f1 * f2");
    }
}
```
</details>

##### 🎯 Bài tập tương tự tự luyện: Lớp Số Phức (`ComplexNumber`)
- **Yêu cầu:** Một số phức có dạng $z = a + bi$ ($a$: phần thực, $b$: phần ảo).
  - Tạo class `ComplexNumber` gồm: `real` (`double`), `imaginary` (`double`).
  - Constructor không tham số ($0 + 0i$) và Constructor 2 tham số.
  - Method `add(ComplexNumber other)`: Cộng 2 số phức: $(a_1 + a_2) + (b_1 + b_2)i \rightarrow$ trả về một `ComplexNumber` mới.
  - Method `subtract(ComplexNumber other)`: Trừ 2 số phức $\rightarrow$ trả về một `ComplexNumber` mới.
  - Method `multiply(ComplexNumber other)`: $(a_1a_2 - b_1b_2) + (a_1b_2 + a_2b_1)i \rightarrow$ trả về một `ComplexNumber` mới.
  - Method `print()`: In định dạng `a + bi` (hoặc `a - bi`).
- **Test Case kỳ vọng:**
  ```text
  z1 = 3.0 + 2.0i, z2 = 1.0 + 4.0i
  z1 + z2 = 4.0 + 6.0i
  z1 * z2 = -5.0 + 14.0i
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

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
public class MyTime {
    private int hour;
    private int minute;
    private int second;

    public MyTime() {
        this(0, 0, 0);
    }

    public MyTime(int hour, int minute, int second) {
        this.hour = (hour >= 0 && hour < 24) ? hour : 0;
        this.minute = (minute >= 0 && minute < 60) ? minute : 0;
        this.second = (second >= 0 && second < 60) ? second : 0;
    }

    // Constructor quy đổi từ tổng số giây trong ngày
    public MyTime(int totalSeconds) {
        totalSeconds = totalSeconds % (24 * 3600); // Đảm bảo trong 1 ngày
        if (totalSeconds < 0) totalSeconds += 24 * 3600;

        this.hour = totalSeconds / 3600;
        int remainder = totalSeconds % 3600;
        this.minute = remainder / 60;
        this.second = remainder % 60;
    }

    public void nextSecond() {
        this.second++;
        if (this.second >= 60) {
            this.second = 0;
            this.minute++;
            if (this.minute >= 60) {
                this.minute = 0;
                this.hour++;
                if (this.hour >= 24) {
                    this.hour = 0;
                }
            }
        }
    }

    public void previousSecond() {
        this.second--;
        if (this.second < 0) {
            this.second = 59;
            this.minute--;
            if (this.minute < 0) {
                this.minute = 59;
                this.hour--;
                if (this.hour < 0) {
                    this.hour = 23;
                }
            }
        }
    }

    public void display() {
        System.out.printf("%02d:%02d:%02d\n", this.hour, this.minute, this.second);
    }

    public static void main(String[] args) {
        MyTime t1 = new MyTime(23, 59, 58);
        System.out.print("Khởi tạo: "); t1.display();

        t1.nextSecond();
        System.out.print("Tăng 1s  : "); t1.display();

        t1.nextSecond();
        System.out.print("Tăng 1s  : "); t1.display(); // Qua ngày mới 00:00:00

        t1.previousSecond();
        System.out.print("Lùi 1s   : "); t1.display(); // 23:59:59
    }
}
```
</details>

##### 🎯 Bài tập tương tự tự luyện: Lớp Lịch Ngày Tháng Năm (`MyDate`)
- **Yêu cầu:** Class `MyDate` gồm: `day`, `month`, `year`.
  - Constructor nhận `(day, month, year)`.
  - Hàm phụ trợ kiểm tra năm nhuận: `isLeapYear(int year)`.
  - Hàm lấy số ngày tối đa trong tháng: tháng 1, 3, 5, 7, 8, 10, 12 có 31 ngày; tháng 4, 6, 9, 11 có 30 ngày; tháng 2 có 28 hoặc 29 ngày.
  - Method `nextDay()`: Tăng lên 1 ngày tiếp theo (tự động điều chỉnh sang tháng mới hoặc sang năm mới).
  - Method `display()`: In dạng `dd/MM/yyyy` (ví dụ: `31/12/2026` sang ngày mới thành `01/01/2027`).
- **Test Case kỳ vọng:**
  ```text
  Ngày: 28/02/2024 (Năm nhuận) -> nextDay() -> 29/02/2024
  Ngày: 31/12/2026 -> nextDay() -> 01/01/2027
  ```

---

### 🔴 CẤP ĐỘ 4: THỰC TẾ & KẾT HỢP NHIỀU ĐỐI TƯỢNG (Bài 9 - Bài 10)

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

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
public class ShoppingCartDemo {

    // 1. Lớp đại diện cho một món hàng trong giỏ
    public static class CartItem {
        private String itemName;
        private double unitPrice;
        private int quantity;

        public CartItem(String itemName, double unitPrice, int quantity) {
            this.itemName = itemName;
            this.unitPrice = (unitPrice > 0) ? unitPrice : 0.0;
            this.quantity = (quantity > 0) ? quantity : 1;
        }

        public double getSubtotal() {
            return this.unitPrice * this.quantity;
        }

        public void printItem(int index) {
            System.out.printf("%d. %-18s x %d = %,10.0f VND\n",
                    index, this.itemName, this.quantity, this.getSubtotal());
        }
    }

    // 2. Lớp giỏ hàng quản lý danh sách món hàng
    public static class ShoppingCart {
        private CartItem[] items;
        private int count;

        public ShoppingCart(int capacity) {
            this.items = new CartItem[capacity];
            this.count = 0;
        }

        public boolean addItem(CartItem item) {
            if (this.count < this.items.length) {
                this.items[this.count] = item;
                this.count++;
                return true;
            }
            System.out.println("Giỏ hàng đã đầy, không thể thêm!");
            return false;
        }

        public double calculateTotal() {
            double total = 0;
            for (int i = 0; i < this.count; i++) {
                total += this.items[i].getSubtotal();
            }
            return total;
        }

        public double calculateDiscount(String couponCode) {
            double total = this.calculateTotal();
            if ("SALE10".equalsIgnoreCase(couponCode)) {
                double discount = total * 0.10;
                return Math.min(discount, 50000); // Tối đa giảm 50,000 VND
            } else if ("FREESHIP".equalsIgnoreCase(couponCode)) {
                return 30000;
            }
            return 0.0;
        }

        public void printReceipt(String couponCode) {
            System.out.println("================ HÓA ĐƠN MUA HÀNG ================");
            for (int i = 0; i < this.count; i++) {
                this.items[i].printItem(i + 1);
            }
            System.out.println("--------------------------------------------------");
            double total = this.calculateTotal();
            double discount = this.calculateDiscount(couponCode);
            double finalPayment = Math.max(0, total - discount);

            System.out.printf("Tổng tiền hàng  : %,12.0f VND\n", total);
            if (discount > 0) {
                System.out.printf("Mã giảm (%s): -%,11.0f VND\n", couponCode, discount);
            }
            System.out.printf("THANH TOÁN      : %,12.0f VND\n", finalPayment);
            System.out.println("==================================================");
        }
    }

    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart(5);
        cart.addItem(new CartItem("Bánh mì bơ tỏi", 15000, 2));
        cart.addItem(new CartItem("Cà phê sữa đá", 25000, 1));
        cart.addItem(new CartItem("Snack khoai tây", 20000, 3));

        cart.printReceipt("SALE10");
    }
}
```
</details>

##### 🎯 Bài tập tương tự tự luyện: Quản Lý Lớp Học (`Classroom` & `Student`)
- **Yêu cầu:** Kết hợp Class `Student` (đã làm ở Bài 2) và xây dựng Class `Classroom`:
  - Fields của `Classroom`: `className` (`String`), mảng `Student[] students`, số sinh viên hiện có `size`.
  - Constructor `Classroom(String className, int maxStudents)`.
  - Method `addStudent(Student s)`: Thêm sinh viên vào lớp.
  - Method `calculateClassAverage()`: Tính điểm trung bình của toàn thể sinh viên trong lớp.
  - Method `findValedictorian()`: Tìm và in ra thông tin của Thủ khoa (sinh viên có điểm trung bình cao nhất lớp).
- **Test Case kỳ vọng:**
  ```text
  Lớp: Java 01 (Sĩ số: 3 sinh viên)
  Điểm trung bình lớp: 7.80
  Thủ khoa của lớp: Nguyễn Văn An (Điểm TB: 9.20 - Xếp loại: Giỏi)
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

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
public class MovieTicket {
    private String movieTitle;
    private String seatCode;
    private boolean isVip;
    private boolean isWeekend;
    private boolean isBooked;

    // Constructor 1: 2 tham số
    public MovieTicket(String movieTitle, String seatCode) {
        this(movieTitle, seatCode, false);
    }

    // Constructor 2: 3 tham số
    public MovieTicket(String movieTitle, String seatCode, boolean isWeekend) {
        this.movieTitle = movieTitle;
        this.seatCode = seatCode;
        // Tự động nhận diện ghế VIP nếu mã ghế bắt đầu bằng VIP
        this.isVip = (seatCode != null && seatCode.toUpperCase().startsWith("VIP"));
        this.isWeekend = isWeekend;
        this.isBooked = false; // Mặc định ban đầu vé chưa ai đặt
    }

    public double calculatePrice() {
        double price = 80000; // Giá vé gốc
        if (this.isWeekend) {
            price += 20000;   // Phụ thu cuối tuần
        }
        if (this.isVip) {
            price += 30000;   // Phụ thu ghế VIP
        }
        return price;
    }

    public boolean book() {
        if (!this.isBooked) {
            this.isBooked = true;
            System.out.println("✅ Đặt vé thành công cho ghế: " + this.seatCode);
            return true;
        } else {
            System.out.println("❌ Lỗi: Ghế " + this.seatCode + " đã có người đặt trước!");
            return false;
        }
    }

    public void cancel() {
        if (this.isBooked) {
            this.isBooked = false;
            System.out.println("Đã hủy vé ghế: " + this.seatCode);
        } else {
            System.out.println("Vé chưa được đặt, không thể hủy!");
        }
    }

    public void printTicketInfo() {
        System.out.println("----------------------------------------------");
        System.out.println("🎬 VÉ XEM PHIM: " + this.movieTitle);
        System.out.printf("- Số ghế   : %s (%s)\n", this.seatCode, this.isVip ? "Ghế VIP" : "Ghế Thường");
        System.out.printf("- Suất chiếu: %s\n", this.isWeekend ? "Cuối Tuần" : "Ngày Thường");
        System.out.printf("- Giá vé   : %,.0f VND\n", this.calculatePrice());
        System.out.printf("- Trạng thái: %s\n", this.isBooked ? "ĐÃ ĐƯỢC ĐẶT" : "CÒN TRỐNG");
        System.out.println("----------------------------------------------");
    }

    public static void main(String[] args) {
        MovieTicket t1 = new MovieTicket("Dune: Hành Tinh Cát", "VIP08", true);
        t1.printTicketInfo();

        // Thử đặt vé lần 1
        t1.book();

        // Cố tình đặt vé lần 2 trên cùng ghế
        t1.book();

        t1.printTicketInfo();
    }
}
```
</details>

##### 🎯 Bài tập tương tự tự luyện: Quản Lý Đặt Phòng Khách Sạn (`HotelRoom`)
- **Yêu cầu:** Class `HotelRoom` gồm:
  - Fields: `roomNumber` (`String`), `roomType` (`String` - "STANDARD", "DELUXE", "VIP"), `isBooked` (`boolean`).
  - Constructor nhận `roomNumber` và `roomType`. Mặc định `isBooked = false`.
  - Method `getRoomRate()`:
    - STANDARD: $400,000$ VND/đêm.
    - DELUXE: $700,000$ VND/đêm.
    - VIP: $1,200,000$ VND/đêm.
  - Method `checkIn()`: Đổi `isBooked = true` (nếu phòng trống thì báo thành công, nếu đã có khách báo lỗi).
  - Method `checkOut(int nights)`: Tính tiền phòng = `nights * getRoomRate()`, sau đó trả phòng (đổi `isBooked = false`).
- **Test Case kỳ vọng:**
  ```text
  Phòng VIP 301:
  - Khách check-in: Thành công!
  - Khách ở 2 đêm, check-out: Tổng tiền = 2,400,000 VND
  - Phòng trả về trạng thái TRỐNG.
  ```

---
