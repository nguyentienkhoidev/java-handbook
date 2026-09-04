# 📘 Bài 9: Lập Trình Hướng Đối Tượng (OOP) - Nhập Môn Tư Duy Đối Tượng, Class & Object Toàn Diện

---

## 📑 Mục Lục
- [1️⃣ Đặt Vấn Đề: Tại Sao Cần Lập Trình Hướng Đối Tượng?](#1-đặt-vấn-đề-tại-sao-cần-lập-trình-hướng-đối-tượng)
  - [1.1 Lập trình hướng thủ tục (POP) và giới hạn của nó](#11-lập-trình-hướng-thủ-tục-pop-và-giới-hạn-của-nó)
  - [1.2 Cuộc cách mạng OOP: Đưa thế giới thực vào máy tính](#12-cuộc-cách-mạng-oop-đưa-thế-giới-thực-vào-máy-tính)
- [2️⃣ Đối Tượng (Object) Là Gì?](#2-đối-tượng-object-là-gì)
  - [2.1 Định nghĩa Đối tượng trong đời sống và trong lập trình](#21-định-nghĩa-đối-tượng-trong-đời-sống-và-trong-lập-trình)
  - [2.2 Ba trụ cột của một Đối tượng: State, Behavior & Identity](#22-ba-trụ-cột-của-một-đối-tượng-state-behavior--identity)
  - [2.3 Sự kết nối mật thiết: State khớp với Fields, Behavior khớp với Methods](#23-sự-kết-nối-mật-thiết-state-khớp-với-fields-behavior-khớp-với-methods)
- [3️⃣ Lớp (Class) - Bản Thiết Kế Vĩ Đại](#3-lớp-class---bản-thiết-kế-vĩ-đại)
  - [3.1 Định nghĩa Class](#31-định-nghĩa-class)
  - [3.2 Phép ẩn dụ kinh điển: Khuôn đúc - Phễu rót - Bức tượng đồng](#32-phép-ẩn-dụ-kinh-điển-khuôn-đúc---phễu-rót---bức-tượng-đồng)
  - [3.3 Các thành phần cốt lõi của một Class (Fields, Methods, Constructor)](#33-các-thành-phần-cốt-lõi-của-một-class-fields-methods-constructor)
  - [3.4 Bản thiết kế (Class) vs Thực thể (Object)](#34-bản-thiết-kế-class-vs-thực-thể-object)
- [4️⃣ Khởi Tạo Đối Tượng & Bản Chất Từ Khóa `new`](#4-khởi-tạo-đối-tượng--bản-chất-từ-khóa-new)
  - [4.1 Cú pháp khai báo và khởi tạo](#41-cú-pháp-khai-báo-và-khởi-tạo)
  - [4.2 Hậu trường của từ khóa `new`: Chuyện gì xảy ra?](#42-hậu-trường-của-từ-khóa-new-chuyện-gì-xảy-ra)
  - [4.3 Constructor: Chiếc phễu nạp dữ liệu ban đầu](#43-constructor-chiếc-phễu-nạp-dữ-liệu-ban-đầu)
- [5️⃣ Cơ Chế Lưu Trữ Đối Tượng Trong Bộ Nhớ RAM (Stack & Heap)](#5-cơ-chế-lưu-trữ-đối-tượng-trong-bộ-nhớ-ram-stack--heap)
  - [5.1 Biến tham chiếu (Reference) trên Stack & Thực thể Object trên Heap](#51-biến-tham-chiếu-reference-trên-stack--thực-thể-object-trên-heap)
  - [5.2 Sơ đồ phân bổ bộ nhớ trực quan](#52-sơ-đồ-phân-bổ-bộ-nhớ-trực-quan)
  - [5.3 Biến thực thể (Instance Variable): Mỗi thực thể một khoảng trời riêng](#53-biến-thực-thể-instance-variable-mỗi-thực-thể-một-khoảng-trời-riêng)
- [6️⃣ Thực Chiến Code Xây Dựng Class & Object Chuẩn Chỉnh](#6-thực-chiến-code-xây-dựng-class--object-chuẩn-chỉnh)
  - [6.1 Ví dụ 1: Mô hình hóa chiếc xe máy (`Motorbike`)](#61-ví-dụ-1-mô-hình-hóa-chiếc-xe-máy-motorbike)
  - [6.2 Ví dụ 2: Mô hình hóa tài khoản ngân hàng (`BankAccount`) - Thao tác State qua Behavior](#62-ví-dụ-2-mô-hình-hóa-tài-khoản-ngân-hàng-bankaccount---thao-tác-state-qua-behavior)
- [7️⃣ Bản Chất Sâu Sắc: "Trong Đối Tượng Có Đối Tượng" (Composition) - Phân Tích Thực Tế CellphoneS](#7-bản-chất-sâu-sắc-trong-đối-tượng-có-đối-tượng-composition---phân-tích-thực-tế-cellphones)
  - [7.1 Mọi Field của Class thực chất là gì? Sự kết nối với các kiểu dữ liệu đã học](#71-mọi-field-của-class-thực-chất-là-gì-sự-kết-nối-với-các-kiểu-dữ-liệu-đã-học)
  - [7.2 Quan sát đời sống: Bảng thông số kỹ thuật điện thoại tại CellphoneS](#72-quan-sát-đời-sống-bảng-thông-số-kỹ-thuật-điện-thoại-tại-cellphones)
  - [7.3 Mô hình hóa bằng Java: Các Class linh kiện tạo nên chiếc Smartphone](#73-mô-hình-hóa-bằng-java-các-class-linh-kiện-tạo-nên-chiếc-smartphone)
  - [7.4 Hậu trường RAM: "Đối tượng chứa đối tượng" được lưu như thế nào trên Heap?](#74-hậu-trường-ram-đối-tượng-chứa-đối-tượng-được-lưu-như-thế-nào-trên-heap)
- [8️⃣ Tổng Kết Các "Cạm Bẫy" Cần Tránh Khi Nhập Môn OOP](#8-tổng-kết-các-cạm-bẫy-cần-tránh-khi-nhập-môn-oop)

---

## 1️⃣ Đặt Vấn Đề: Tại Sao Cần Lập Trình Hướng Đối Tượng?

### 1.1 Lập trình hướng thủ tục (POP) và giới hạn của nó
Trước khi OOP xuất hiện, thế giới lập trình thống trị bởi **Lập trình hướng thủ tục (Procedure-Oriented Programming - POP)**, điển hình như ngôn ngữ C hay Pascal. 

Trong hướng thủ tục:
- Chương trình được chia nhỏ thành các **hàm (functions)** hoặc thủ tục.
- Dữ liệu (data) và hàm xử lý (functions) bị **tách rời nhau**. Dữ liệu thường được đặt ở phạm vi toàn cục (global) để các hàm cùng truy cập.

```
       [ Dữ liệu toàn cục: Tên, Tuổi, Số dư... ]
           /                |                \
    Hàm Rút Tiền()     Hàm Đổi Tên()    Hàm Chuyển Khoản()
```

🚨 **Hậu quả khi dự án phình to:**
1. **Dữ liệu dễ bị xâm phạm:** Bất kỳ hàm nào cũng có thể can thiệp và sửa đổi dữ liệu tùy tiện, rất khó kiểm soát ai đã thay đổi biến số dư từ `1000$` thành `0$`.
2. **Khó bảo trì và mở rộng:** Một thay đổi nhỏ trong cấu trúc dữ liệu sẽ kéo theo việc phải sửa hàng chục hàm liên quan.
3. **Không phản ánh đúng thế giới thực:** Trong đời thực, không có chuyện "dữ liệu nằm trơ trọi một chỗ rồi các hành động từ đâu bay tới can thiệp".

---

### 1.2 Cuộc cách mạng OOP: Đưa thế giới thực vào máy tính
Con người quan sát thế giới xung quanh không phải qua các dòng lệnh rời rạc, mà qua các **sự vật, hiện tượng cụ thể**.
- Bạn nhìn thấy một **chiếc xe máy** (vừa có màu sơn, biển số, vừa biết nổ máy, tăng ga).
- Bạn nhìn thấy một **con người** (vừa có tên, tuổi, chiều cao, vừa biết ăn, ngủ, học bài).
- Bạn thấy một **tài khoản ngân hàng** (vừa có số dư, số tài khoản, vừa có hành vi nạp, rút).

> 💡 **Lập trình hướng đối tượng (Object-Oriented Programming - OOP)** là phương pháp lập trình đưa các thực thể ngoài đời thực vào trong code bằng cách **đóng gói dữ liệu (State)** và **các hàm thao tác trên dữ liệu đó (Behavior)** thành một khối thống nhất gọi là **Đối tượng (Object)**.

```
+-------------------------------------------------------+
|                 ĐỐI TƯỢNG (OBJECT)                    |
|  +-------------------------------------------------+  |
|  | DỮ LIỆU (State): Biển số, Màu xe, Vận tốc...   |  |
|  +-------------------------------------------------+  |
|  | HÀNH VI (Behavior): Nổ máy(), Chạy(), Phanh()...|  |
|  +-------------------------------------------------+  |
+-------------------------------------------------------+
```

---

## 2️⃣ Đối Tượng (Object) Là Gì?

### 2.1 Định nghĩa Đối tượng trong đời sống và trong lập trình
- **Đời sống:** Mọi thứ bạn có thể sờ, nhìn, cảm nhận, hoặc trừu tượng hóa có tính chất và hành động độc lập đều là đối tượng: chiếc laptop bạn đang dùng, ly cà phê trên bàn, con mèo tam thể đang ngủ, chú bảo vệ tòa nhà...
- **Lập trình:** **Đối tượng (Object)** là một thực thể phần mềm cụ thể, chứa các thông tin mô tả đặc điểm và các hành vi mà nó có thể thực hiện.

---

### 2.2 Ba trụ cột của một Đối tượng: State, Behavior & Identity

Bất kỳ đối tượng nào trong tự nhiên hay trong lập trình cũng đều sở hữu đủ **3 yếu tố cốt lõi**:

```
                  +--------------------------------+
                  |      TAM GIÁC ĐỐI TƯỢNG        |
                  +--------------------------------+
                                  /\
                                 /  \
                                /    \
               [ STATE ] <---------------> [ BEHAVIOR ]
              (Trạng thái)       \        (Hành vi)
                                  \
                             [ IDENTITY ]
                              (Định danh)
```

| Yếu tố | Giải thích đời sống | Ánh xạ vào Java | Ví dụ: Chiếc xe máy |
| :--- | :--- | :--- | :--- |
| **State (Trạng thái)** | Là những đặc điểm, thông số, thuộc tính để mô tả đối tượng tại một thời điểm. | **Fields / Thuộc tính / Biến thực thể** | Màu đỏ, hãng Honda, đang chạy với tốc độ 40 km/h, mức xăng còn 3 lít. |
| **Behavior (Hành vi)** | Là những việc, chức năng, phản ứng mà đối tượng đó có thể làm hoặc bị tác động. | **Methods / Phương thức** | Nổ máy, tăng tốc (làm vận tốc tăng), bóp phanh (làm vận tốc giảm), bấm còi. |
| **Identity (Định danh)** | Là đặc điểm duy nhất giúp phân biệt đối tượng này với mọi đối tượng khác, dù hai đối tượng có trạng thái giống hệt nhau. | **Địa chỉ ô nhớ (Memory Address / HashCode)** | Số khung xe / Biển kiểm soát (Hai chiếc xe cùng màu, cùng đời vẫn là 2 xe khác nhau). Trong RAM, chúng nằm ở 2 địa chỉ khác nhau. |

---

### 2.3 Sự kết nối mật thiết: State khớp với Fields, Behavior khớp với Methods

Một sai lầm của người mới học là xem "Fields" và "Methods" là hai thứ độc lập được nhét chung vào một class. **Thực tế, chúng gắn liền mật thiết:**

> 🔑 **Nguyên lý vàng:** **Hành vi (Behavior - Method) sinh ra là để đọc hoặc làm thay đổi Trạng thái (State - Field) của chính đối tượng đó.**

Hãy quan sát sự tác động qua lại trong đời sống:
- Khi xe máy thực hiện hành vi **`tăngGa(20)`** (Method) ➡️ Trạng thái **`vận tốc`** (Field) từ $40$ nhảy lên $60\text{ km/h}$.
- Khi bạn thực hiện hành vi **`rútTiền(500k)`** (Method) ➡️ Trạng thái **`số dư`** (Field) bị trừ đi $500\text{k}$.
- Nếu không có Field, Method không biết xử lý cái gì. Nếu có Field mà không có Method, đối tượng sẽ là một "vật thể chết" vô dụng.

---

## 3️⃣ Lớp (Class) - Bản Thiết Kế Vĩ Đại

### 3.1 Định nghĩa Class
Nếu bạn muốn sản xuất hàng nghìn chiếc xe máy Honda Wave Alpha, các kỹ sư không thể tự nhiên đập búa gò từng miếng sắt theo cảm hứng. Họ bắt buộc phải có một **bản vẽ kỹ thuật (bản thiết kế)** xác định rõ: xe gồm những bộ phận gì, kích thước ra sao, hoạt động thế nào.

> 📐 **Định nghĩa:** **Class (Lớp)** là một **bản thiết kế (blueprint)**, một **khuôn mẫu (template)** định nghĩa sẵn những thuộc tính (fields) và hành vi (methods) mà mọi đối tượng tạo ra từ nó sẽ sở hữu.

---

### 3.2 Phép ẩn dụ kinh điển: Khuôn đúc - Phễu rót - Bức tượng đồng

Hãy ghi nhớ hình ảnh trực quan này, bạn sẽ không bao giờ nhầm lẫn giữa Class, Constructor và Object:

```
    [ CLASS ]                         [ CONSTRUCTOR ]                       [ OBJECTS ]
+------------------+             +----------------------+            +-----------------------+
|  KHUÔN ĐÚC TƯỢNG |             |   PHỄU RÓT NGUYÊN LIỆU|            |   CÁC BỨC TƯỢNG THÀNH PHẨM|
|                  |             |                      |            |                       |
|  - Rãnh chiều cao|             |  Rót: Đồng đỏ, 10kg  | ---------> |  Tượng A: Đồng đỏ, 10kg|
|  - Rãnh hình dáng| +-------->  |                      |            +-----------------------+
|  - Rãnh trọng    |             +----------------------+            +-----------------------+
|    lượng         |             |  Rót: Đồng vàng, 5kg | ---------> |  Tượng B: Đồng vàng, 5kg|
+------------------+             +----------------------+            +-----------------------+
 (Bản thiết kế chung)              (Hàm tiếp nhận thông số             (Các thực thể cụ thể cầm,
                                      để khởi tạo đối tượng)               nắm, đặt trong phòng)
```

1. **Class (Lớp) = Khuôn đúc tượng:** 
   - Nó rỗng ruột, chỉ định hình khung: "Tượng nào đúc ra từ đây cũng sẽ có chất liệu, trọng lượng, màu sắc".
   - Bạn **không thể** đem cái khuôn đi thờ hay trưng bày phòng khách như một bức tượng, vì nó chỉ là cái khuôn!
2. **Constructor (Hàm tạo) = Chiếc phễu rót nguyên liệu:**
   - Là cửa ngõ để bạn rót dữ liệu cụ thể vào khuôn (ví dụ: rót đồng đỏ nặng 10kg, hoặc đồng vàng nặng 5kg).
3. **Object (Đối tượng) = Bức tượng thành phẩm:**
   - Là sản phẩm thực tế, có khối lượng, màu sắc rõ ràng và chiếm không gian trong căn phòng (chiếm bộ nhớ RAM).

---

### 3.3 Các thành phần cốt lõi của một Class (Fields, Methods, Constructor)

Một Class trong Java thông thường được cấu thành từ 3 thành phần chính:

```java
public class Motorbike {
    
    // ==========================================
    // 1. FIELDS (Thuộc tính / Instance Variables)
    // -> Đại diện cho STATE (Trạng thái của xe)
    // ==========================================
    String brand;        // Thương hiệu (Honda, Yamaha...)
    String color;        // Màu sơn (Đỏ, Đen, Xanh...)
    int speed;           // Tốc độ hiện tại (km/h)
    boolean isEngineOn;  // Động cơ đang bật hay tắt?

    // ==========================================
    // 2. CONSTRUCTOR (Hàm khởi tạo - Phễu rót)
    // -> Dùng để gán giá trị ban đầu khi tạo đối tượng
    // ==========================================
    public Motorbike(String brand, String color) {
        this.brand = brand;
        this.color = color;
        this.speed = 0;              // Mặc định xe mới tạo đứng yên
        this.isEngineOn = false;     // Mặc định động cơ đang tắt
    }

    // ==========================================
    // 3. METHODS (Phương thức)
    // -> Đại diện cho BEHAVIOR (Hành vi của xe)
    // ==========================================
    public void startEngine() {
        this.isEngineOn = true;
        System.out.println(brand + ": Động cơ đã nổ! Brừm brừm...");
    }

    public void accelerate(int amount) {
        if (!isEngineOn) {
            System.out.println(brand + ": Xe chưa nổ máy, không thể tăng ga!");
            return;
        }
        this.speed += amount;
        System.out.println(brand + ": Vừa tăng ga! Tốc độ hiện tại là: " + this.speed + " km/h");
    }

    public void brake() {
        this.speed = 0;
        System.out.println(brand + ": Đã phanh lại. Xe dừng hẳn!");
    }
}
```

---

### 3.4 Bản thiết kế (Class) vs Thực thể (Object)

| Đặc điểm | Class (Lớp) | Object (Đối tượng / Thực thể) |
| :--- | :--- | :--- |
| **Bản chất** | Bản thiết kế trừu tượng, khuôn mẫu lý thuyết. | Thực thể tồn tại thực tế, có dữ liệu cụ thể. |
| **Tồn tại ở đâu?** | Được nạp vào vùng nhớ **Metaspace** một lần khi chạy code. | Được cấp phát động trên vùng nhớ **Heap**. |
| **Bộ nhớ** | Không chiếm bộ nhớ để lưu dữ liệu thực thể. | Chiếm không gian RAM thật để lưu các trường dữ liệu. |
| **Số lượng** | Chỉ có 1 bản thiết kế duy nhất. | Có thể tạo ra hàng triệu đối tượng từ 1 Class. |
| **Ví dụ đời sống** | Bản vẽ ngôi nhà trên giấy. | Ngôi nhà số 12, ngôi nhà số 14 xây trên mảnh đất thực tế. |

---

## 4️⃣ Khởi Tạo Đối Tượng & Bản Chất Từ Khóa `new`

### 4.1 Cú pháp khai báo và khởi tạo

```java
// Cú pháp:
TênClass tênBiến = new TênConstructor(các_đối_số);

// Ví dụ tạo 2 chiếc xe khác nhau từ cùng 1 khuôn Motorbike:
Motorbike bike1 = new Motorbike("Honda", "Đỏ");
Motorbike bike2 = new Motorbike("Yamaha", "Xanh GP");
```

---

### 4.2 Hậu trường của từ khóa `new`: Chuyện gì xảy ra?

Khi câu lệnh `Motorbike bike1 = new Motorbike("Honda", "Đỏ");` được thực thi, JVM thực hiện **4 bước liên tiếp**:

```
 [1. Quét Class]       [2. Cấp phát Heap]       [3. Gọi Constructor]       [4. Gán Địa chỉ]
  Xem bản vẽ          Tìm khoảng trống         Bơm "Honda", "Đỏ"           Trả địa chỉ 0x99
  Motorbike           trên RAM Heap            vào các ô nhớ              cho biến bike1
      |                      |                         |                        |
      v                      v                         v                        v
+------------+     +-------------------+     +-------------------+     +------------------+
| Đọc Fields | --> | Cấp ô nhớ Heap    | --> | Chạy code bên     | --> | bike1 = 0x99     |
| & Methods  |     | Gán giá trị def   |     | trong constructor |     | (trên Stack)     |
+------------+     +-------------------+     +-------------------+     +------------------+
```

1. **Quét bộ nhớ (Class Loading):** JVM kiểm tra xem Class `Motorbike` đã được load chưa.
2. **Cấp phát bộ nhớ Heap (`new`):** JVM cấp phát một vùng nhớ trống trên **Heap** đủ lớn để chứa toàn bộ các thuộc tính của `Motorbike` (`brand`, `color`, `speed`, `isEngineOn`). Ban đầu các biến này nhận giá trị mặc định (`null`, `0`, `false`).
3. **Thực thi Constructor:** Constructor được gọi để gán các giá trị cụ thể ("Honda", "Đỏ") vào vùng nhớ vừa tạo.
4. **Trả về địa chỉ tham chiếu:** Toán tử `new` trả về **địa chỉ ô nhớ** (ví dụ: `0x99AF`) của đối tượng trên Heap, sau đó gán vào biến `bike1` trên Stack.

---

### 4.3 Constructor: Chiếc phễu nạp dữ liệu ban đầu
- **Tên Constructor:** Bắt buộc phải **trùng 100% với tên Class**.
- **Kiểu trả về:** Constructor **hoàn toàn không có kiểu trả về** (kể cả `void`).
- **Mục đích duy nhất:** Khởi tạo trạng thái ban đầu cho đối tượng ngay khi nó chào đời.
- **Nếu bạn không viết Constructor nào:** Trình biên dịch Java sẽ tự động sinh ra một **Default Constructor rỗng** (`public Motorbike() {}`). Nhưng một khi bạn đã tự viết một Constructor có tham số, Default Constructor mặc định sẽ biến mất!

---

## 5️⃣ Cơ Chế Lưu Trữ Đối Tượng Trong Bộ Nhớ RAM (Stack & Heap)

Hiểu được cơ chế này là bạn đã nắm được 80% bản chất vận hành của Java!

### 5.1 Biến tham chiếu (Reference) trên Stack & Thực thể Object trên Heap

Hãy nhớ quy tắc bất biến của Java:
1. **Biến tham chiếu (Reference Variable):** Như `bike1`, `bike2` được tạo trong hàm `main`, nó nằm tại **Stack Memory**. Nó **không chứa chiếc xe thật**, mà chỉ chứa một "tấm danh thiếp ghi địa chỉ nhà" (con trỏ địa chỉ ô nhớ) của chiếc xe.
2. **Thực thể đối tượng (Instance Object):** Toàn bộ dữ liệu thật (`brand="Honda"`, `color="Đỏ"`, `speed=0`) nằm trọn vẹn tại **Heap Memory**.

---

### 5.2 Sơ đồ phân bổ bộ nhớ trực quan

Giả sử ta chạy đoạn code:
```java
Motorbike bike1 = new Motorbike("Honda", "Đỏ");
Motorbike bike2 = new Motorbike("Yamaha", "Xanh");
Motorbike bike3 = bike1; // Gán tham chiếu!
```

Bộ nhớ RAM được tổ chức chính xác như sau:

```
           STACK MEMORY                                     HEAP MEMORY
    +-------------------------+                     +-------------------------------+
    |                         |                     |   Object 1 (Motorbike)        |
    |  bike1: [ 0x1111 ] -----+-------------------->|   - brand: "Honda"            |
    |                         |  Địa chỉ: 0x1111    |   - color: "Đỏ"               |
    |                         |                     |   - speed: 0                  |
    |                         |                     |   - isEngineOn: false         |
    |                         |                     +-------------------------------+
    |                         |                                     ^
    |  bike3: [ 0x1111 ] -----+-------------------------------------+ (Cùng trỏ Object 1)
    |                         |
    |                         |                     +-------------------------------+
    |  bike2: [ 0x2222 ] -----+-------------------->|   Object 2 (Motorbike)        |
    |                         |  Địa chỉ: 0x2222    |   - brand: "Yamaha"           |
    |                         |                     |   - color: "Xanh"             |
    |                         |                     |   - speed: 0                  |
    +-------------------------+                     |   - isEngineOn: false         |
                                                    +-------------------------------+
```

> ⚠️ **Hiện tượng 2 biến trỏ chung 1 thực thể (`bike3 = bike1`):**
> - Biến `bike3` không tạo ra chiếc xe mới nào cả! Nó chỉ sao chép địa chỉ `0x1111` từ `bike1`.
> - Nếu bạn gọi: `bike3.accelerate(50);` ➡️ Chiếc xe tại `0x1111` tăng tốc lên 50. Lúc này nếu in `bike1.speed`, kết quả cũng sẽ là **50**!

---

### 5.3 Biến thực thể (Instance Variable): Mỗi thực thể một khoảng trời riêng

Các biến được khai báo bên trong Class nhưng nằm ngoài các hàm được gọi là **Biến thực thể (Instance Variables)**.
- Từ "Instance" có nghĩa là "thực thể/bản thể".
- **Tại sao gọi là Instance Variable?** Vì mỗi khi một Object (Instance) được sinh ra từ lệnh `new`, nó sẽ được cấp **một bản sao hoàn toàn độc lập** của các biến đó trên Heap.
- Thay đổi thuộc tính của `bike1` **tuyệt đối không làm ảnh hưởng** đến thuộc tính của `bike2`. `bike1` chạy 80 km/h thì `bike2` vẫn có thể đang đứng yên 0 km/h.

---

## 6️⃣ Thực Chiến Code Xây Dựng Class & Object Chuẩn Chỉnh

### 6.1 Ví dụ 1: Mô hình hóa chiếc xe máy (`Motorbike`)

Tạo file `Main.java` để chạy thử nghiệm kịch bản lái xe:

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. TẠO CÁC XE MÁY ===");
        Motorbike waveAlpha = new Motorbike("Honda Wave", "Đỏ đô");
        Motorbike exciter = new Motorbike("Yamaha Exciter", "Xanh GP");

        System.out.println("\n=== 2. THAO TÁC TRÊN XE WAVE ALPHA ===");
        // Thử tăng ga khi chưa nổ máy
        waveAlpha.accelerate(30); 

        // Bật máy và tăng tốc
        waveAlpha.startEngine();
        waveAlpha.accelerate(40);
        waveAlpha.accelerate(20); // Tăng tiếp 20 -> vận tốc lên 60

        System.out.println("\n=== 3. KIỂM TRA TÍNH ĐỘC LẬP GIỮA CÁC INSTANCE ===");
        // Xe Exciter hoàn toàn không bị ảnh hưởng bởi xe Wave!
        System.out.println("Tốc độ xe Wave: " + waveAlpha.speed + " km/h");
        System.out.println("Tốc độ xe Exciter: " + exciter.speed + " km/h (Vẫn đứng yên)");

        System.out.println("\n=== 4. PHANH XE ===");
        waveAlpha.brake();
    }
}
```

**Output khi chạy chương trình:**
```text
=== 1. TẠO CÁC XE MÁY ===

=== 2. THAO TÁC TRÊN XE WAVE ALPHA ===
Honda Wave: Xe chưa nổ máy, không thể tăng ga!
Honda Wave: Động cơ đã nổ! Brừm brừm...
Honda Wave: Vừa tăng ga! Tốc độ hiện tại là: 40 km/h
Honda Wave: Vừa tăng ga! Tốc độ hiện tại là: 60 km/h

=== 3. KIỂM TRA TÍNH ĐỘC LẬP GIỮA CÁC INSTANCE ===
Tốc độ xe Wave: 60 km/h
Tốc độ xe Exciter: 0 km/h (Vẫn đứng yên)

=== 4. PHANH XE ===
Honda Wave: Đã phanh lại. Xe dừng hẳn!
```

---

### 6.2 Ví dụ 2: Mô hình hóa tài khoản ngân hàng (`BankAccount`) - Thao tác State qua Behavior

Ví dụ này giúp bạn thấy rõ nhất: **Methods bảo vệ và thay đổi Fields như thế nào**.

```java
public class BankAccount {
    // 1. STATE (Trạng thái được lưu trong Fields)
    String accountNumber;
    String ownerName;
    double balance; // Số dư tài khoản

    // 2. CONSTRUCTOR (Khởi tạo tài khoản với số dư ban đầu)
    public BankAccount(String accountNumber, String ownerName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        // Kiểm tra hợp lệ ngay từ phễu khởi tạo
        if (initialBalance < 0) {
            this.balance = 0;
            System.out.println("Số dư ban đầu không được âm. Gán mặc định là 0đ.");
        } else {
            this.balance = initialBalance;
        }
    }

    // 3. BEHAVIORS (Hành vi xử lý và biến đổi State)
    
    // Nạp tiền vào tài khoản
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Số tiền nạp phải lớn hơn 0!");
            return;
        }
        this.balance += amount; // Thay đổi State
        System.out.println("Nạp thành công: +" + amount + "đ vào tài khoản " + accountNumber);
        displayBalance();
    }

    // Rút tiền khỏi tài khoản
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Số tiền rút phải lớn hơn 0!");
            return;
        }
        if (amount > this.balance) {
            System.out.println("Giao dịch thất bại! Số dư không đủ để rút " + amount + "đ.");
            return;
        }
        this.balance -= amount; // Thay đổi State
        System.out.println("Rút thành công: -" + amount + "đ từ tài khoản " + accountNumber);
        displayBalance();
    }

    // Hiển thị trạng thái hiện tại
    public void displayBalance() {
        System.out.println("-> Chủ tài khoản: " + ownerName + " | Số dư hiện tại: " + this.balance + "đ");
    }
}
```

**Chạy thử nghiệm giao dịch ngân hàng:**
```java
public class BankTest {
    public static void main(String[] args) {
        BankAccount acc = new BankAccount("19001008", "Nguyễn Văn A", 1000000);

        System.out.println("--- Giao dịch 1: Nạp thêm tiền ---");
        acc.deposit(500000);

        System.out.println("\n--- Giao dịch 2: Rút tiền hợp lệ ---");
        acc.withdraw(800000);

        System.out.println("\n--- Giao dịch 3: Rút quá số dư ---");
        acc.withdraw(1000000); // Lúc này số dư chỉ còn 700k
    }
}
```

---

## 7️⃣ Bản Chất Sâu Sắc: "Trong Đối Tượng Có Đối Tượng" (Composition) - Phân Tích Thực Tế CellphoneS

### 7.1 Mọi Field của Class thực chất là gì? Sự kết nối với các kiểu dữ liệu đã học

Khi học các bài học trước, bạn đã làm quen với 2 nhóm kiểu dữ liệu:
1. **Kiểu dữ liệu nguyên thủy (Primitive Types):** `byte`, `short`, `int`, `long`, `float`, `double`, `boolean`, `char`.
2. **Kiểu dữ liệu tham chiếu cơ bản:** `String`, mảng (Array).

Và bây giờ, khi bước sang OOP, một sự thật cốt lõi được hé lộ:
> 💡 **Bản thân mỗi Class mà bạn tạo ra CHÍNH LÀ MỘT KIỂU DỮ LIỆU MỚI (User-Defined Type)!**
> Điều này đồng nghĩa: **Bên trong một Class, các Fields (thuộc tính) không chỉ là `int`, `double`, `String`... mà hoàn toàn có thể là MỘT CLASS KHÁC!**

Đây chính là nguyên lý: **"Trong một đối tượng lớn lại chứa nhiều đối tượng con bên trong"** (Thuật ngữ chuyên môn gọi là **Object Composition** hay quan hệ **HAS-A**).

---

### 7.2 Quan sát đời sống: Bảng thông số kỹ thuật điện thoại tại CellphoneS

Hãy tưởng tượng bạn truy cập vào website **CellphoneS** để xem sản phẩm **iPhone 16 Pro Max**:

```
+-----------------------------------------------------------------------+
|  [ CellphoneS.com.vn ]                                                |
|  ĐIỆN THOẠI IPHONE 16 PRO MAX 256GB - CHÍNH HÃNG VN/A                  |
|  Giá niêm yết: 34.990.000đ  | Trạng thái: Còn hàng                    |
|-----------------------------------------------------------------------|
|  THÔNG SỐ KỸ THUẬT CHI TIẾT:                                          |
|                                                                       |
|  📱 MÀN HÌNH (Screen):                                                |
|     - Kích thước: 6.9 inch                                            |
|     - Công nghệ: Super Retina XDR OLED                                |
|     - Tần số quét: 120 Hz                                             |
|                                                                       |
|  🔋 PIN & SẠC (Battery):                                              |
|     - Dung lượng: 4685 mAh                                            |
|     - Công suất sạc nhanh: 30 W                                       |
|                                                                       |
|  📷 CỤM CAMERA (Camera):                                              |
|     - Độ phân giải camera chính: 48 MP                                |
|     - Zoom quang học: 5x                                              |
+-----------------------------------------------------------------------+
```

Hãy phân tích cách các kiểu dữ liệu đã học ánh xạ vào từng thông số:
- **Thông tin tổng quan của Điện thoại:**
  - Tên máy (`"iPhone 16 Pro Max"`): Kiểu `String`.
  - Giá bán (`34990000.0`): Kiểu `double`.
  - Màu sắc (`"Titan Sa Mạc"`): Kiểu `String`.
  - Trạng thái còn hàng (`true`): Kiểu `boolean`.
  - Bộ nhớ trong (`256`): Kiểu `int`.
- **Nhưng nhìn vào Màn hình (`Screen`), Pin (`Battery`), Camera (`Camera`):**
  - Màn hình không thể chỉ là 1 con số. Nó là **một đối tượng độc lập** gồm: kích thước (`double`), công nghệ tấm nền (`String`), tần số quét (`int`).
  - Viên pin cũng là **một đối tượng độc lập** gồm: dung lượng (`int`), sạc nhanh (`int`).
  - Chiếc điện thoại hoàn chỉnh được tạo ra bằng cách **lắp ráp các linh kiện (đối tượng con) này lại với nhau!**

---

### 7.3 Mô hình hóa bằng Java: Các Class linh kiện tạo nên chiếc Smartphone

Hãy cùng viết code tách bạch từng linh kiện theo đúng tư duy hướng đối tượng:

#### Bước 1: Xây dựng các Class linh kiện con (`Screen` & `Battery`)

```java
// Linh kiện 1: Màn hình điện thoại
public class Screen {
    double sizeInches;      // Kiểu double (đã học)
    String panelType;       // Kiểu String (đã học)
    int refreshRateHz;      // Kiểu int (đã học)

    public Screen(double sizeInches, String panelType, int refreshRateHz) {
        this.sizeInches = sizeInches;
        this.panelType = panelType;
        this.refreshRateHz = refreshRateHz;
    }

    public void displayScreenInfo() {
        System.out.println("  + Màn hình: " + sizeInches + " inch, Tấm nền: " + panelType + " (" + refreshRateHz + "Hz)");
    }
}
```

```java
// Linh kiện 2: Viên Pin điện thoại
public class Battery {
    int capacityMah;        // Dung lượng pin: mAh (Kiểu int)
    int fastChargeWatt;     // Công suất sạc nhanh: Watt (Kiểu int)
    int currentPercent;     // Phần trăm pin hiện tại (0 - 100%)

    public Battery(int capacityMah, int fastChargeWatt) {
        this.capacityMah = capacityMah;
        this.fastChargeWatt = fastChargeWatt;
        this.currentPercent = 100; // Pin mới khui hộp đầy 100%
    }

    // Hành vi tiêu hao pin khi dùng
    public void consume(int percent) {
        this.currentPercent -= percent;
        if (this.currentPercent < 0) this.currentPercent = 0;
        System.out.println("  ⚡ Pin đã tiêu hao " + percent + "%. Còn lại: " + this.currentPercent + "%");
    }
}
```

---

#### Bước 2: Xây dựng Class cha `CellphoneSProduct` (Chứa các Class con làm Field)

```java
public class CellphoneSProduct {
    // 1. CÁC FIELDS NGUYÊN THỦY & STRING ĐÃ HỌC
    String productName;     // Kiểu String
    String brand;           // Kiểu String
    double price;           // Kiểu double
    int storageGB;          // Kiểu int
    boolean isAvailable;    // Kiểu boolean

    // 2. CÁC FIELDS LÀ ĐỐI TƯỢNG (OBJECT TRONG ĐỐI TƯỢNG)
    Screen screen;          // Kiểu dữ liệu là Class Screen!
    Battery battery;        // Kiểu dữ liệu là Class Battery!

    // Constructor: Phễu nhận cả giá trị cơ bản lẫn các object linh kiện
    public CellphoneSProduct(String productName, String brand, double price, int storageGB, 
                            boolean isAvailable, Screen screen, Battery battery) {
        this.productName = productName;
        this.brand = brand;
        this.price = price;
        this.storageGB = storageGB;
        this.isAvailable = isAvailable;
        this.screen = screen;       // Gán tham chiếu Screen vào Phone
        this.battery = battery;     // Gán tham chiếu Battery vào Phone
    }

    // Hành vi: Hiển thị bảng thông số chuẩn CellphoneS
    public void printSpecSheet() {
        System.out.println("=================================================");
        System.out.println("📱 CHI TIẾT SẢN PHẨM TẠI CELLPHONES");
        System.out.println("Tên: " + productName + " (" + storageGB + "GB)");
        System.out.println("Hãng: " + brand + " | Giá: " + String.format("%,.0f", price) + " VNĐ");
        System.out.println("Trạng thái: " + (isAvailable ? "Còn hàng (Giao nhanh 2h)" : "Hết hàng"));
        
        System.out.println("\n--- THÔNG SỐ PHẦN CỨNG CHI TIẾT ---");
        // Gọi hành vi của đối tượng con Screen
        screen.displayScreenInfo();
        
        // Truy xuất trực tiếp field của đối tượng con Battery
        System.out.println("  + Pin: " + battery.capacityMah + " mAh (Sạc nhanh " + battery.fastChargeWatt + "W)");
        System.out.println("=================================================");
    }

    // Hành vi: Chơi game trên điện thoại -> Làm hao pin của đối tượng Battery bên trong!
    public void playGame(String gameName) {
        System.out.println("\n🎮 Đang chơi game '" + gameName + "' trên " + productName + "...");
        // Đối tượng cha gọi phương thức của đối tượng con bên trong nó:
        this.battery.consume(20); 
    }
}
```

---

#### Bước 3: Chạy chương trình mô phỏng thực tế

```java
public class CellphoneSDemo {
    public static void main(String[] args) {
        // 1. Tạo các linh kiện con trước
        Screen iphoneScreen = new Screen(6.9, "Super Retina XDR OLED", 120);
        Battery iphoneBattery = new Battery(4685, 30);

        // 2. Lắp ráp các linh kiện vào chiếc điện thoại iPhone 16 Pro Max
        CellphoneSProduct iphone16 = new CellphoneSProduct(
            "iPhone 16 Pro Max",
            "Apple",
            34990000.0,
            256,
            true,
            iphoneScreen,
            iphoneBattery
        );

        // 3. In bảng thông số kỹ thuật CellphoneS
        iphone16.printSpecSheet();

        // 4. Thử nghiệm chơi game -> Thấy pin của đối tượng Battery bên trong bị trừ
        iphone16.playGame("Liên Quân Mobile");
        iphone16.playGame("Genshin Impact");
    }
}
```

**Output thực tế khi chạy:**
```text
=================================================
📱 CHI TIẾT SẢN PHẨM TẠI CELLPHONES
Tên: iPhone 16 Pro Max (256GB)
Hãng: Apple | Giá: 34,990,000 VNĐ
Trạng thái: Còn hàng (Giao nhanh 2h)

--- THÔNG SỐ PHẦN CỨNG CHI TIẾT ---
  + Màn hình: 6.9 inch, Tấm nền: Super Retina XDR OLED (120Hz)
  + Pin: 4685 mAh (Sạc nhanh 30W)
=================================================

🎮 Đang chơi game 'Liên Quân Mobile' trên iPhone 16 Pro Max...
  ⚡ Pin đã tiêu hao 20%. Còn lại: 80%

🎮 Đang chơi game 'Genshin Impact' trên iPhone 16 Pro Max...
  ⚡ Pin đã tiêu hao 20%. Còn lại: 60%
```

---

### 7.4 Hậu trường RAM: "Đối tượng chứa đối tượng" được lưu như thế nào trên Heap?

Khi bạn thấy cú pháp quen thuộc: `iphone16.battery.capacityMah` hay `iphone16.screen.refreshRateHz`, JVM đã làm điều gì dưới tầng bộ nhớ?

Hãy nhìn vào sơ đồ phân bổ RAM:

```
      STACK MEMORY                                          HEAP MEMORY
 +---------------------+            +-----------------------------------------------------+
 |                     |            |   Object [CellphoneSProduct] (Tại địa chỉ 0xAAAA)    |
 |  iphone16           |            |   - productName: "iPhone 16 Pro Max"                |
 |    [ 0xAAAA ]  -----+----------->|   - price: 34990000.0                               |
 |                     |  Địa chỉ   |   - storageGB: 256                                  |
 |                     |  0xAAAA    |   - isAvailable: true                               |
 |                     |            |   - screen:  [ 0xBBBB ] ------------------------+   |
 |                     |            |   - battery: [ 0xCCCC ] --------------------+   |   |
 +---------------------+            +---------------------------------------------|---|---+
                                                                                  |   |
                                    +-----------------------------------------+   |   |
                                    |   Object [Screen] (Tại địa chỉ 0xBBBB)  |<------+
                                    |   - sizeInches: 6.9                     |
                                    |   - panelType: "Super Retina XDR OLED"  |
                                    |   - refreshRateHz: 120                  |
                                    +-----------------------------------------+
                                                                                  
                                    +-----------------------------------------+
                                    |   Object [Battery] (Tại địa chỉ 0xCCCC) |<--+
                                    |   - capacityMah: 4685                   |
                                    |   - fastChargeWatt: 30                  |
                                    |   - currentPercent: 60                  |
                                    +-----------------------------------------+
```

> 🎯 **Bài học bản chất cốt tử:**
> 1. Đối tượng `CellphoneSProduct` **không nhét trọn toàn bộ thân xác** của `Screen` và `Battery` vào trong nó.
> 2. Nó chỉ lưu trữ **2 biến tham chiếu (địa chỉ `0xBBBB` và `0xCCCC`)** trỏ sang các vùng nhớ của `Screen` và `Battery` nằm độc lập trên **Heap**!
> 3. Cú pháp dấu chấm liên tiếp (`iphone16.battery.currentPercent`):
>    - `iphone16`: Nhảy đến địa chỉ `0xAAAA` trên Heap.
>    - `.battery`: Đọc lấy địa chỉ `0xCCCC` được lưu trong ô nhớ `battery`.
>    - `.currentPercent`: Nhảy tiếp sang địa chỉ `0xCCCC` và lấy ra giá trị `60`.

---

## 8️⃣ Tổng Kết Các "Cạm Bẫy" Cần Tránh Khi Nhập Môn OOP

> 🚩 **Cạm bẫy 1: Quên từ khóa `new` dẫn đến `NullPointerException`**
> ```java
> Motorbike bike; // Mới chỉ khai báo biến tham chiếu trên Stack, trỏ vào null!
> bike.startEngine(); // LỖI NGAY: java.lang.NullPointerException (Xe chưa hề tồn tại trên Heap!)
> ```
> 👉 **Khắc phục:** Luôn nhớ khởi tạo đối tượng bằng `new` trước khi chấm gọi phương thức.

---

> 🚩 **Cạm bẫy 2: Nhầm lẫn giữa Biến và Bản thân Đối tượng**
> ```java
> Motorbike b1 = new Motorbike("Honda", "Đỏ");
> Motorbike b2 = new Motorbike("Honda", "Đỏ");
> System.out.println(b1 == b2); // KẾT QUẢ: false!
> ```
> 👉 **Giải thích:** Dù hai chiếc xe cùng màu "Đỏ", cùng hãng "Honda", toán tử `==` so sánh **địa chỉ ô nhớ** trên Heap (`0x1111 == 0x2222`). Đây là 2 thực thể độc lập nên kết quả là `false`.

---

> 🚩 **Cạm bẫy 3: Đặt kiểu trả về cho Constructor**
> ```java
> public void Motorbike(String brand, String color) { ... } // SAI TÉC HỎA!
> ```
> 👉 **Giải thích:** Một khi bạn thêm `void` hay bất kỳ kiểu trả về nào, Java sẽ xem đây là một **method bình thường**, KHÔNG CÒN là Constructor nữa!

---

> 🚩 **Cạm bẫy 4: Nhầm lẫn phạm vi của từ khóa `this`**
> ```java
> public Motorbike(String brand, String color) {
>     brand = brand; // Gán tham số brand cho chính tham số brand! Instance variable vẫn giữ giá trị null!
> }
> ```
> 👉 **Khắc phục:** Phải dùng `this.brand = brand;`. Từ khóa `this` đại diện cho "thực thể hiện tại đang được xử lý".

---

> 🚩 **Cạm bẫy 5: Đối tượng lồng nhau chưa được khởi tạo (Nested NullPointerException)**
> ```java
> CellphoneSProduct phone = new CellphoneSProduct("iPhone 16", "Apple", 30000000, 128, true, null, null);
> phone.screen.displayScreenInfo(); // LỖI: java.lang.NullPointerException!
> ```
> 👉 **Giải thích:** Biến `screen` bên trong `phone` đang nhận giá trị `null` (chưa có linh kiện màn hình thật trên Heap). Khi bạn cố tình chấm tiếp `.displayScreenInfo()`, chương trình sẽ sập ngay lập tức!
