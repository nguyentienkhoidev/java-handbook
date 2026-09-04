# 📘 Bài 9: Lập Trình Hướng Đối Tượng (OOP) - Nhập Môn Tư Duy Đối Tượng, Class & Object Toàn Diện

---

## 📑 Mục Lục

- [1️⃣ Khởi Nguồn Tư Duy: Từ Đời Thực Đến Lập Trình Hướng Đối Tượng (OOP)](#1-khởi-nguồn-tư-duy-từ-đời-thực-đến-lập-trình-hướng-đối-tượng-oop)
  - [1.1 Quan sát thế giới: Mọi thứ quanh ta đều là Đối tượng (Object)](#11-quan-sát-thế-giới-mọi-thứ-quanh-ta-đều-là-đối-tượng-object)
  - [1.2 Ba trụ cột của một Đối tượng: State (Trạng thái) - Behavior (Hành vi) - Identity (Định danh)](#12-ba-trụ-cột-của-một-đối-tượng-state-trạng-thái---behavior-hành-vi---identity-định-danh)
  - [1.3 Tại sao lại cần OOP? (Sự bế tắc của lập trình hướng thủ tục POP và giải pháp từ OOP)](#13-tại-sao-lại-cần-oop-sự-bế-tắc-của-lập-trình-hướng-thủ-tục-pop-và-giải-pháp-từ-oop)
- [2️⃣ Lớp (Class) & Đối Tượng (Object): Từ Bản Thiết Kế Đến Thực Thể](#2-lớp-class--đối-tượng-object-từ-bản-thiết-kế-đến-thực-thể)
  - [2.1 Phép ẩn dụ kinh điển: Khuôn đúc tượng (Class) - Phễu rót (Constructor) - Bức tượng đồng (Object)](#21-phép-ẩn-dụ-kinh-điển-khuôn-đúc-tượng-class---phễu-rót-constructor---bức-tượng-đồng-object)
  - [2.2 Cấu trúc giải phẫu của một Class: Fields, Methods, Constructor](#22-cấu-trúc-giải-phẫu-của-một-class-fields-methods-constructor)
  - [2.3 Mối liên hệ mật thiết: Fields lưu trữ State, Methods thao tác trên Fields](#23-mối-liên-hệ-mật-thiết-fields-lưu-trữ-state-methods-thao-tác-trên-fields)
  - [2.4 Bảng so sánh trực diện: Class (Bản thiết kế) vs Object (Thực thể)](#24-bảng-so-sánh-trực-diện-class-bản-thiết-kế-vs-object-thực-thể)
- [3️⃣ Thực Hành Khởi Tạo & Vận Hành Đối Tượng (Hands-on Coding)](#3-thực-hành-khởi-tạo--vận-hành-đối-tượng-hands-on-coding)
  - [3.1 Cú pháp khởi tạo và từ khóa `new`](#31-cú-pháp-khởi-tạo-và-từ-khóa-new)
  - [3.2 Ví dụ thực chiến 1: Lái chiếc xe máy (`Motorbike`)](#32-ví-dụ-thực-chiến-1-lái-chiếc-xe-máy-motorbike)
  - [3.3 Ví dụ thực chiến 2: Quản lý tài khoản ngân hàng (`BankAccount`) - Dữ liệu được bảo vệ qua hành vi](#33-ví-dụ-thực-chiến-2-quản-lý-tài-khoản-ngân-hàng-bankaccount---dữ-liệu-được-bảo-vệ-qua-hành-vi)
  - [3.4 Tính độc lập giữa các Instance (Bản sao riêng biệt của mỗi thực thể)](#34-tính-độc-lập-giữa-các-instance-bản-sao-riêng-biệt-của-mỗi-thực-thể)
- [4️⃣ Toàn Cảnh Bộ Nhớ JVM & Giải Phẫu 5 Vùng Nhớ (Runtime Data Areas)](#4-toàn-cảnh-bộ-nhớ-jvm--giải-phẫu-5-vùng-nhớ-runtime-data-areas)
  - [4.1 JVM có mấy vùng nhớ? (Bức tranh 5 Vùng nhớ Runtime Data Areas)](#41-jvm-có-mấy-vùng-nhớ-bức-tranh-5-vùng-nhớ-runtime-data-areas)
  - [4.2 Bộ Ba Tam Giác Vàng trong OOP: Metaspace - Heap - Stack](#42-bộ-ba-tam-giác-vàng-trong-oop-metaspace---heap---stack)
  - [4.3 Giải phẫu Vùng nhớ 1: Method Area / Metaspace (Nơi Class và Bytecode cư ngụ)](#43-giải-phẫu-vùng-nhớ-1-method-area--metaspace-nơi-class-và-bytecode-cư-ngụ)
  - [4.4 Giải phẫu Vùng nhớ 2: Heap Area (Nhà kho chứa toàn bộ Object)](#44-giải-phẫu-vùng-nhớ-2-heap-area-nhà-kho-chứa-toàn-bộ-object)
  - [4.5 Giải phẫu Vùng nhớ 3: JVM Stack Area & Cấu trúc Stack Frame (Lời gọi hàm)](#45-giải-phẫu-vùng-nhớ-3-jvm-stack-area--cấu-trúc-stack-frame-lời-gọi-hàm)
  - [4.6 Hai vùng nhớ phụ trợ: PC Register (Đếm chỉ lệnh) & Native Method Stack](#46-hai-vùng-nhớ-phụ-trợ-pc-register-đếm-chỉ-lệnh--native-method-stack)
  - [4.7 Chụp X-Quang RAM: Trace từng dòng code qua Metaspace, Heap và Stack](#47-chụp-x-quang-ram-trace-từng-dòng-code-qua-metaspace-heap-và-stack)
  - [4.8 Hiện tượng hai biến trỏ chung một Object trên Heap (`b2 = b1`)](#48-hiện-tượng-hai-biến-trỏ-chung-một-object-trên-heap-b2--b1)
  - [4.9 Cơ chế dọn rác của Garbage Collector (GC): Khi nào Object biến thành "Rác"?](#49-cơ-chế-dọn-rác-của-garbage-collector-gc-khi-nào-object-biến-thành-rác)
  - [4.10 Hai cơn ác mộng bộ nhớ kinh điển: StackOverflowError vs OutOfMemoryError](#410-hai-cơn-ác-mộng-bộ-nhớ-kinh-điển-stackoverflowerror-vs-outofmemoryerror)
- [5️⃣ Nâng Cấp Tư Duy: "Trong Đối Tượng Lại Có Đối Tượng" (Composition)](#5-nâng-cấp-tư-duy-trong-đối-tượng-lại-có-đối-tượng-composition)
  - [5.1 Kết nối các kiểu dữ liệu đã học: Class cũng chính là một kiểu dữ liệu mới!](#51-kết-nối-các-kiểu-dữ-liệu-đã-học-class-cũng-chính-là-một-kiểu-dữ-liệu-mới)
  - [5.2 Quan sát thực tế: Bảng thông số kỹ thuật Smartphone tại CellphoneS](#52-quan-sát-thực-tế-bảng-thông-số-kỹ-thuật-smartphone-tại-cellphones)
  - [5.3 Mô hình hóa bằng Java: Lắp ráp các linh kiện (`Screen`, `Battery`) vào `CellphoneSProduct`](#53-mô-hình-hóa-bằng-java-lắp-ráp-các-linh-kiện-screen-battery-vào-cellphonesproduct)
  - [5.4 Hậu trường RAM Heap lồng Heap: Giải mã cú pháp gọi dấu chấm liên tiếp](#54-hậu-trường-ram-heap-lồng-heap-giải-mã-cú-pháp-gọi-dấu-chấm-liên-tiếp)
- [6️⃣ Tổng Kết Cốt Lõi & Top 5 "Cạm Bẫy" Cần Tránh Khi Nhập Môn OOP](#6-tổng-kết-cốt-lõi--top-5-cạm-bẫy-cần-tránh-khi-nhập-môn-oop)
  - [6.1 Tóm tắt các khái niệm then chốt](#61-tóm-tắt-các-khái-niệm-then-chốt)
  - [6.2 Top 5 cạm bẫy kinh điển người mới hay mắc phải](#62-top-5-cạm-bẫy-kinh-điển-người-mới-hay-mắc-phải)

---

## 1️⃣ Khởi Nguồn Tư Duy: Từ Đời Thực Đến Lập Trình Hướng Đối Tượng (OOP)

### 1.1 Quan sát thế giới: Mọi thứ quanh ta đều là Đối tượng (Object)

Hãy tạm gác lại màn hình code và nhìn ra xung quanh căn phòng của bạn:
- Bạn thấy **chiếc laptop** bạn đang gõ.
- Bạn thấy **ly trà sữa** đang tan đá trên bàn.
- Bạn thấy **chú mèo** đang nằm ngủ trên ghế.
- Xa hơn ngoài đường, bạn thấy **những chiếc xe máy, ô tô** đang lưu thông.

Tất cả những sự vật, hiện tượng cụ thể đó trong đời sống được gọi là các **ĐỐI TƯỢNG (OBJECTS)**. Con người nhận thức và giao tiếp với thế giới thông qua các đối tượng, chứ không nhìn đời bằng những biến số hay dòng lệnh rời rạc.

> 💡 **Đối tượng (Object) trong lập trình:** Là một thực thể phần mềm cụ thể mô phỏng lại một sự vật trong đời thực, chứa đầy đủ **thông tin mô tả (dữ liệu)** và **các hành động (chức năng)** mà nó có thể làm được.

---

### 1.2 Ba trụ cột của một Đối tượng: State (Trạng thái) - Behavior (Hành vi) - Identity (Định danh)

Bất kỳ đối tượng nào bạn chạm vào ngoài đời hay tạo ra trong code cũng đều có đủ **3 yếu tố không thể tách rời**:

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

| Yếu tố | Ý nghĩa đời thực | Ánh xạ vào Java | Ví dụ: Chiếc xe máy |
| :--- | :--- | :--- | :--- |
| **State (Trạng thái)** | Là những đặc điểm, tính chất, thông số để nhận biết đối tượng tại một thời điểm. | **Fields / Thuộc tính / Biến thực thể** | Màu đỏ, hãng Honda, vận tốc hiện tại đang là 40 km/h, bình xăng còn 2 lít. |
| **Behavior (Hành vi)** | Là những việc, chức năng, phản ứng mà đối tượng có thể làm hoặc người khác tác động lên nó. | **Methods / Phương thức** | Nổ máy, tăng ga (làm vận tốc tăng lên), bóp phanh (làm xe dừng lại), bấm còi. |
| **Identity (Định danh)** | Là đặc điểm duy nhất giúp phân biệt đối tượng này với mọi đối tượng khác, dù chúng giống hệt nhau về trạng thái. | **Địa chỉ ô nhớ (Memory Address trên RAM Heap)** | Hai chiếc xe Honda cùng mua một ngày, cùng màu đỏ nhưng có **số khung, biển số khác nhau**. Trong RAM, chúng nằm ở hai ô nhớ riêng biệt. |

---

### 1.3 Tại sao lại cần OOP? (Sự bế tắc của lập trình hướng thủ tục POP và giải pháp từ OOP)

Trước khi OOP ra đời, thế giới lập trình bị thống trị bởi **Lập trình hướng thủ tục (Procedure-Oriented Programming - POP)** như ngôn ngữ C hay Pascal. 

Trong hướng thủ tục:
- **Dữ liệu (Data)** và **Hàm xử lý (Functions)** bị tách rời nhau hoàn toàn.
- Dữ liệu thường được để ở dạng các biến toàn cục (global) nằm trơ trọi, và bất kỳ hàm nào trong toàn bộ chương trình cũng có quyền tự do can thiệp và chỉnh sửa.

```
       [ Dữ liệu toàn cục trơ trọi: Tên, Tuổi, Số dư tài khoản... ]
            /                   |                     \
    Hàm Rút Tiền()         Hàm Đổi Tên()          Hàm Đổi Mật Khẩu()
```

> 🏦 **Ẩn dụ dễ hiểu nhất:** 
> - **Ngân hàng kiểu Hướng thủ tục (POP):** Tiền của khách hàng được đổ đống giữa sân ga. Ai đi ngang qua cũng có thể thò tay bốc vài cọc tiền hoặc nhét giấy lộn vào mà không có bất kỳ ai gác cổng hay kiểm tra!
> - **Ngân hàng kiểu Hướng đối tượng (OOP):** Mỗi khách hàng có một chiếc két sắt riêng biệt. Bạn không thể tự thò tay vào trong két, mà bắt buộc phải giao dịch qua nhân viên ngân hàng (gọi Method). Nhân viên sẽ kiểm tra: *"Mã PIN có đúng không? Số dư có đủ không?"* rồi mới xuất tiền an toàn cho bạn.

---

#### 📌 Dẫn chứng thực tế: Bài toán Quản lý Rút tiền Ngân hàng

Hãy cùng nhìn vào một ví dụ thực tế bằng code để thấy rõ sự sụp đổ của tư duy hướng thủ tục khi dự án lớn lên:

##### 🔴 Cách làm theo Hướng thủ tục (POP) & 3 Giới hạn chí mạng:

```java
public class PopBankDemo {
    // 1. Dữ liệu toàn cục nằm trơ trọi giữa trời
    static String account1_STK = "1001";
    static double account1_SoDu = 5000000; // 5 triệu VND

    static String account2_STK = "1002";
    static double account2_SoDu = 2000000; // 2 triệu VND

    // 2. Hàm rút tiền độc lập
    public static void rutTien(double soTien) {
        if (soTien > 0 && soTien <= account1_SoDu) {
            account1_SoDu -= soTien;
            System.out.println("Rút thành công: " + soTien + "đ");
        }
    }

    public static void main(String[] args) {
        // THẢM HỌA 1: Dữ liệu bị phá hỏng tùy tiện (Không có vỏ bọc bảo vệ)
        // Bất kỳ ai ở đâu trong code cũng có thể tự tiện gán số âm hoặc sửa bậy:
        account1_SoDu = -999999999; // Gán số âm vô lý mà không hề có cơ chế nào ngăn cản!
        account1_SoDu = account1_SoDu - 10000000; // Rút lố 10 triệu mà không hề kiểm tra!

        // THẢM HỌA 2: Bùng nổ biến khi mở rộng (Poor Scalability)
        // Nếu có 10.000 khách hàng thì sao?
        // Khai báo account1_SoDu, account2_SoDu... đến account10000_SoDu?
        // Hay dùng 2 mảng song song: String[] stks và double[] soDus?
        // Rất nguy hiểm: Xóa 1 phần tử ở mảng này mà quên xóa ở mảng kia -> Lệch index hàng loạt (tiền ông A gán nhầm cho ông B)!

        // THẢM HỌA 3: Hàm và dữ liệu không gắn liền
        // Hàm rutTien() phải nhận cả đống tham số rời rạc. Cần thêm loại tiền (USD, VND) -> Phải sửa lại toàn bộ các hàm!
    }
}
```

---

##### 🟢 Giải pháp triệt để từ Lập trình hướng đối tượng (OOP):

OOP giải quyết bài toán này bằng cơ chế **Đóng gói (Encapsulation)**: Nhốt **Dữ liệu (State)** và **Hành vi (Behavior)** vào chung một chiếc hộp đối tượng, đặt ổ khóa bảo vệ dữ liệu.

```java
public class BankAccount {
    // 1. DỮ LIỆU ĐƯỢC BẢO VỆ TUYỆT ĐỐI (Private)
    // Người ngoài KHÔNG THỂ can thiệp trực tiếp vào số dư!
    private String accountNumber;
    private double balance;

    // 2. CONSTRUCTOR: Kiểm soát tính hợp lệ ngay từ lúc sinh ra
    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = (initialBalance >= 0) ? initialBalance : 0;
    }

    // 3. NGƯỜI GÁC CỔNG: Muốn thay đổi số dư, BẮT BUỘC phải đi qua hành vi này!
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Lỗi: Số tiền rút phải lớn hơn 0đ!");
            return;
        }
        if (amount > this.balance) {
            System.out.println("❌ Giao dịch từ chối! Số dư không đủ để rút " + amount + "đ.");
            return;
        }
        this.balance -= amount; // HỢP LỆ mới được trừ tiền!
        System.out.println("✅ Rút thành công " + amount + "đ. Số dư còn lại: " + this.balance + "đ");
    }

    // Chỉ cho xem số dư chứ không cho sửa bậy
    public double getBalance() {
        return this.balance;
    }
}
```

**Thực thi thực tế với OOP:**
```java
public class OopBankDemo {
    public static void main(String[] args) {
        // Cần bao nhiêu khách hàng chỉ việc "đúc" bấy nhiêu đối tượng độc lập
        BankAccount acc1 = new BankAccount("1001", 5000000);
        BankAccount acc2 = new BankAccount("1002", 2000000);

        // 1. Cố tình gán số âm từ bên ngoài?
        // acc1.balance = -999999; 
        // -> TRÌNH BIÊN DỊCH BÁO LỖI NGAY: balance has private access in BankAccount!

        // 2. Cố tình rút quá số dư?
        acc1.withdraw(7000000); // ❌ Bị chặn lại an toàn bởi logic bên trong Object!

        // 3. Rút tiền hợp lệ:
        acc1.withdraw(2000000); // ✅ Rút thành công 2000000.0đ. Số dư còn lại: 3000000.0đ
    }
}
```

---

##### 📊 Bảng so sánh trực diện: Vì sao OOP vượt trội hơn POP?

| Tiêu chí | Hướng thủ tục (POP) | Hướng đối tượng (OOP) |
| :--- | :--- | :--- |
| **Mối quan hệ Dữ liệu & Hàm** | Tách rời, dữ liệu nằm trơ trọi ở ngoài. | **Gắn liền mật thiết**, dữ liệu được bao bọc bên trong Object. |
| **Bảo vệ dữ liệu** | **Không có.** Bất kỳ hàm nào cũng có thể sửa bừa bãi. | **Bảo vệ tuyệt đối.** Chỉ có các Method của chính đối tượng mới được phép thay đổi State. |
| **Khả năng mở rộng** | Rất tệ. Càng nhiều dữ liệu, code càng rối và dễ lệch thông tin. | **Vô hạn.** Cần bao nhiêu khách chỉ việc `new BankAccount()`, mỗi đối tượng tự quản lý độc lập. |
| **Tư duy lập trình** | Máy móc, tập trung vào *"Làm việc gì trước, việc gì sau"*. | Tự nhiên như đời thực, tập trung vào *"Ai sở hữu dữ liệu gì và ai làm được hành vi nào"*. |

> 💡 **Mẹo nhớ trong 3 giây:** 
> - **POP:** Dữ liệu để ngoài đường, ai đi qua cũng đá được một cái.
> - **OOP:** Dữ liệu cất trong két, muốn đụng vào bắt buộc phải bấm chuông gọi Method mở cửa!

---

## 2️⃣ Lớp (Class) & Đối Tượng (Object): Từ Bản Thiết Kế Đến Thực Thể

### 2.1 Phép ẩn dụ kinh điển: Khuôn đúc tượng (Class) - Phễu rót (Constructor) - Bức tượng đồng (Object)

Để không bao giờ nhầm lẫn giữa **Class**, **Constructor** và **Object**, hãy ghi nhớ hình ảnh trực quan này:

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
   - Bản thân cái khuôn chỉ là khung rỗng, là bản vẽ kỹ thuật. Nó quy định: *"Mọi bức tượng đúc ra từ đây đều sẽ có chiều cao, hình dáng và chất liệu"*.
   - Bạn **không thể** mang cái khuôn đi trưng bày phòng khách như một bức tượng, vì nó chỉ là cái khuôn!
2. **Constructor (Hàm khởi tạo) = Chiếc phễu rót nguyên liệu:**
   - Là cửa ngõ tiếp nhận các giá trị cụ thể khi đúc tượng (rót vào đồng đỏ nặng 10kg, hoặc đồng vàng nặng 5kg).
3. **Object (Đối tượng) = Bức tượng đồng thành phẩm:**
   - Là sản phẩm thực tế cầm nắm được, có màu sắc, cân nặng cụ thể và **chiếm không gian trong căn phòng (chiếm bộ nhớ RAM)**.

> ❓ **Câu hỏi đánh thức tư duy:** 
> *Nếu tôi đập vỡ cái khuôn hoặc sửa lại khuôn thêm một cái rãnh mới, những bức tượng đã đúc từ tuần trước đang đặt ở phòng khách có tự thay đổi theo không?*
> 
> 👉 **Trả lời: TUYỆT ĐỐI KHÔNG!**
> Cái khuôn (Class) chỉ là quy chuẩn đúc. Bức tượng (Object) một khi đã đúc xong và đặt lên bàn (cấp phát trên Heap), nó tồn tại hoàn toàn độc lập với cái khuôn. Việc bạn sửa Class trong code chỉ có tác dụng đối với những Object được tạo ra **sau thời điểm sửa**!

---

### 2.2 Cấu trúc giải phẫu của một Class: Fields, Methods, Constructor

Một Class chuẩn trong Java gồm 3 thành phần cấu thành:

```java
public class Motorbike {
    
    // ==========================================
    // 1. FIELDS (Thuộc tính / Instance Variables)
    // -> Đại diện cho STATE (Trạng thái của đối tượng)
    // ==========================================
    String brand;        // Thương hiệu xe (Honda, Yamaha...)
    String color;        // Màu sơn (Đỏ, Xanh...)
    int speed;           // Tốc độ hiện tại (km/h)
    boolean isEngineOn;  // Trạng thái máy (đang nổ hay tắt?)

    // ==========================================
    // 2. CONSTRUCTOR (Hàm khởi tạo - Phễu rót dữ liệu)
    // -> Đặt tên trùng 100% với tên Class, KHÔNG CÓ kiểu trả về
    // ==========================================
    public Motorbike(String brand, String color) {
        this.brand = brand;      // "this.brand" là biến của Class, "brand" là tham số truyền vào
        this.color = color;
        this.speed = 0;          // Xe mới đúc ra mặc định tốc độ = 0
        this.isEngineOn = false; // Động cơ mặc định tắt
    }

    // ==========================================
    // 3. METHODS (Phương thức)
    // -> Đại diện cho BEHAVIOR (Hành vi của đối tượng)
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
        this.speed += amount; // Method làm thay đổi giá trị của Field speed!
        System.out.println(brand + ": Vừa tăng ga! Tốc độ hiện tại là: " + this.speed + " km/h");
    }

    public void brake() {
        this.speed = 0; // Method đưa Field speed về 0
        System.out.println(brand + ": Đã phanh lại. Xe dừng hẳn!");
    }
}
```

---

### 2.3 Mối liên hệ mật thiết: Fields lưu trữ State, Methods thao tác trên Fields

Một sai lầm rất lớn của người mới học là coi Fields và Methods như hai thứ rời rạc bị nhét chung vào một file. **Thực tế, chúng sinh ra là dành cho nhau:**

> 🔑 **Nguyên lý cốt tử:** **Hành vi (Method) sinh ra là để kiểm tra, thao tác hoặc làm thay đổi Trạng thái (Field) của chính đối tượng đó.**
> - Nếu một Class có Fields mà không có Method nào ➡️ Đối tượng sẽ là một "vật thể chết" trơ trọi.
> - Nếu một Class có Methods mà không cần chạm đến bất kỳ Field nào của đối tượng ➡️ Method đó không thuộc về đối tượng đó (nó nên là một hàm tiện ích tĩnh `static`).

---

### 2.4 Bảng so sánh trực diện: Class (Bản thiết kế) vs Object (Thực thể)

| Tiêu chí | Class (Lớp) | Object (Đối tượng / Thực thể) |
| :--- | :--- | :--- |
| **Bản chất** | Bản thiết kế trừu tượng, khuôn mẫu lý thuyết. | Thực thể có thực, sở hữu dữ liệu cụ thể. |
| **Vị trí trong RAM** | Được nạp vào vùng nhớ **Metaspace** một lần khi chạy app. | Được cấp phát động trên vùng nhớ **Heap**. |
| **Dung lượng bộ nhớ** | Không tốn dung lượng để chứa dữ liệu của thực thể. | Chiếm không gian RAM thật tương ứng với các Fields. |
| **Số lượng** | Chỉ cần 1 Class duy nhất (1 bản thiết kế). | Có thể tạo ra hàng triệu Objects từ cùng 1 Class. |
| **Hình dung thực tế** | Bản vẽ ngôi nhà trên giấy. | Ngôi nhà số 10, ngôi nhà số 12 xây trên đất thật. |

> 💡 **Mẹo nhớ trong 3 giây:** 
> - **Class:** Là tờ giấy khai sinh mẫu (chỉ in sẵn các dòng chấm trống).
> - **Object:** Là con người cụ thể bằng xương bằng thịt sau khi đã khai báo tên tuổi vào giấy khai sinh!

---

## 3️⃣ Thực Hành Khởi Tạo & Vận Hành Đối Tượng (Hands-on Coding)

### 3.1 Cú pháp khởi tạo và từ khóa `new`

Để tạo ra một đối tượng thực tế từ Class, ta dùng cú pháp:

```java
TênClass tênBiến = new TênConstructor(các_tham_số);
```

**Ví dụ:** Dùng khuôn `Motorbike` để đúc ra 2 chiếc xe cụ thể:
```java
Motorbike waveAlpha = new Motorbike("Honda Wave", "Đỏ đô");
Motorbike exciter   = new Motorbike("Yamaha Exciter", "Xanh GP");
```

---

### 3.2 Ví dụ thực chiến 1: Lái chiếc xe máy (`Motorbike`)

Tạo file `Main.java` để xem các đối tượng vận hành trong thực tế:

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. ĐÚC RA CÁC CHIẾC XE CỤ THỂ ===");
        Motorbike wave = new Motorbike("Honda Wave", "Đỏ");
        Motorbike exciter = new Motorbike("Yamaha Exciter", "Xanh");

        System.out.println("\n=== 2. THAO TÁC TRÊN XE WAVE ===");
        // Thử tăng ga khi chưa nổ máy -> Bị chặn lại bởi logic trong method!
        wave.accelerate(30); 

        // Nổ máy và tăng ga
        wave.startEngine();
        wave.accelerate(40);
        wave.accelerate(20); // Vận tốc tăng tiếp lên 60 km/h

        System.out.println("\n=== 3. PHANH XE WAVE ===");
        wave.brake();
    }
}
```

**Output in ra màn hình:**
```text
=== 1. ĐÚC RA CÁC CHIẾC XE CỤ THỂ ===

=== 2. THAO TÁC TRÊN XE WAVE ===
Honda Wave: Xe chưa nổ máy, không thể tăng ga!
Honda Wave: Động cơ đã nổ! Brừm brừm...
Honda Wave: Vừa tăng ga! Tốc độ hiện tại là: 40 km/h
Honda Wave: Vừa tăng ga! Tốc độ hiện tại là: 60 km/h

=== 3. PHANH XE WAVE ===
Honda Wave: Đã phanh lại. Xe dừng hẳn!
```

---

### 3.3 Ví dụ thực chiến 2: Quản lý tài khoản ngân hàng (`BankAccount`) - Dữ liệu được bảo vệ qua hành vi

Ví dụ này chứng minh vì sao OOP vượt trội: **Không ai có thể tùy tiện can thiệp số dư, mọi thay đổi phải đi qua cửa ngõ Method với logic chặt chẽ.**

```java
public class BankAccount {
    // 1. STATE (Dữ liệu quan trọng cần bảo vệ)
    String accountNumber;
    String ownerName;
    double balance; // Số dư tài khoản

    // 2. CONSTRUCTOR (Phễu khởi tạo)
    public BankAccount(String accountNumber, String ownerName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        // Kiểm tra tính hợp lệ ngay từ lúc sinh ra
        if (initialBalance < 0) {
            this.balance = 0;
            System.out.println("Số dư ban đầu không được âm! Gán mặc định = 0đ.");
        } else {
            this.balance = initialBalance;
        }
    }

    // 3. BEHAVIOR: Nạp tiền
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Số tiền nạp vào phải lớn hơn 0!");
            return;
        }
        this.balance += amount; // Biến đổi State
        System.out.println("Nạp thành công: +" + amount + "đ vào tài khoản " + accountNumber);
        displayBalance();
    }

    // 3. BEHAVIOR: Rút tiền (Có kiểm tra bảo vệ số dư)
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Số tiền rút phải lớn hơn 0!");
            return;
        }
        if (amount > this.balance) {
            System.out.println("❌ Giao dịch thất bại! Số dư không đủ để rút " + amount + "đ.");
            return;
        }
        this.balance -= amount; // Biến đổi State
        System.out.println("Rút thành công: -" + amount + "đ từ tài khoản " + accountNumber);
        displayBalance();
    }

    public void displayBalance() {
        System.out.println("-> Chủ tài khoản: " + ownerName + " | Số dư hiện tại: " + this.balance + "đ");
    }
}
```

```java
public class BankTest {
    public static void main(String[] args) {
        BankAccount myAccount = new BankAccount("19001008", "Nguyễn Văn A", 1000000);

        myAccount.deposit(500000);  // Nạp 500k -> Số dư: 1tr5
        myAccount.withdraw(800000); // Rút 800k -> Số dư: 700k
        myAccount.withdraw(1000000);// Cố tình rút 1 triệu -> Bị từ chối ngay!
    }
}
```

---

### 3.4 Tính độc lập giữa các Instance (Bản sao riêng biệt của mỗi thực thể)

Các thuộc tính (`brand`, `speed`, `balance`...) được gọi là **Biến thực thể (Instance Variables)**.
- Chữ **"Instance"** có nghĩa là một **"thực thể cụ thể"**.
- Mỗi khi từ khóa `new` được chạy, Java tạo ra **một bản sao thuộc tính hoàn toàn độc lập** trên bộ nhớ RAM.
- Xe Wave của bạn chạy với tốc độ $60\text{ km/h}$ **tuyệt đối không làm xe Exciter của người khác chạy theo**. Chiếc Exciter vẫn đứng yên ở tốc độ $0\text{ km/h}$.

---

## 4️⃣ Toàn Cảnh Bộ Nhớ JVM & Giải Phẫu 5 Vùng Nhớ (Runtime Data Areas)

Sau khi bạn đã viết code và thấy các đối tượng chạy được, câu hỏi lớn mang tính bản chất kỹ thuật phần mềm là: **"Bên dưới phần cứng máy tính, Máy ảo Java (JVM) phân chia bộ nhớ RAM thành mấy vùng, và chiếc xe máy cùng các hàm của bạn được cất giữ ở đâu?"**.

---

### 4.1 JVM có mấy vùng nhớ? (Bức tranh 5 Vùng nhớ Runtime Data Areas)

Theo đặc tả chuẩn của Máy ảo Java (**JVM Specification**), khi một chương trình Java khởi chạy, hệ điều hành sẽ cấp phát cho JVM một vùng nhớ RAM. JVM chia không gian này thành **5 VÙNG NHỚ RIÊNG BIỆT (Runtime Data Areas)**, được gom thành 2 nhóm lớn:

```
+---------------------------------------------------------------------------------------------+
|                             TỔNG QUAN BỘ NHỚ RUNTIME CỦA JVM                                |
+---------------------------------------------------------------------------------------------+
|                                                                                             |
|   [ NHÓM 1: DÙNG CHUNG TOÀN ỨNG DỤNG (Thread-Shared) ]                                     |
|   (Sinh ra khi JVM khởi động - Tồn tại suốt vòng đời ứng dụng - Chết khi JVM tắt)           |
|                                                                                             |
|   1. HEAP AREA (Vùng nhớ đống)            2. METHOD AREA / METASPACE (Vùng nhớ phương thức) |
|      - Lưu trữ TẤT CẢ các Objects            - Lưu Bản thiết kế Class (Metadata)            |
|      - Lưu các Mảng (Arrays)                 - Lưu mã Bytecode của các method               |
|      - Nơi Garbage Collector (GC) dọn dẹp    - Lưu các biến tĩnh (static variables)         |
|                                              - Run-Time Constant Pool                       |
|                                                                                             |
|---------------------------------------------------------------------------------------------|
|                                                                                             |
|   [ NHÓM 2: DÙNG RIÊNG CHO TỪNG LUỒNG (Thread-Private) ]                                    |
|   (Mỗi Thread có 1 bản riêng - Sinh ra khi Thread tạo - Chết ngay khi Thread kết thúc)      |
|                                                                                             |
|   3. JVM STACK AREA            4. PC REGISTER              5. NATIVE METHOD STACK           |
|      - Lưu các Stack Frame        - Lưu địa chỉ chỉ lệnh      - Dành riêng cho các hàm      |
|      - Lưu biến cục bộ,             Bytecode tiếp theo mà       viết bằng C/C++             |
|        tham số, con trỏ this        CPU cần thi hành cho        (thư viện hệ điều hành JNI) |
|      - Cơ chế LIFO (PUSH/POP)       luồng đó                                                |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
```

| Tên vùng nhớ | Thuộc nhóm | Dữ liệu lưu trữ chính | Lỗi tràn bộ nhớ |
| :--- | :--- | :--- | :--- |
| **1. Heap Area** | Thread-Shared | **Tất cả các Object**, Mảng, biến thực thể (Instance variables) | `OutOfMemoryError: Java heap space` |
| **2. Method Area (Metaspace)** | Thread-Shared | Bản thiết kế Class, mã Bytecode, biến `static`, Constant Pool | `OutOfMemoryError: Metaspace` |
| **3. JVM Stack Area** | Thread-Private | Các **Stack Frame** (biến cục bộ, tham số, con trỏ `this`, địa chỉ quay về) | `StackOverflowError` |
| **4. PC Register** | Thread-Private | Địa chỉ lệnh Bytecode hiện tại đang thực thi | *Không bao giờ tràn* |
| **5. Native Method Stack** | Thread-Private | Các frame thực thi mã máy C / C++ (JNI) | `StackOverflowError` |

> 🏭 **Ẩn dụ "Nhà máy sản xuất xe máy" - Hiểu trọn vẹn 5 vùng nhớ JVM trong 1 phút:**
> 1. **Metaspace (Method Area):** *Phòng Thiết Kế & Lưu Trữ Bản Vẽ.* Nơi cất giữ bản thiết kế xe Wave, xe Lead... và cuốn cẩm nang quy trình sản xuất (Bytecode). Toàn bộ công nhân trong nhà máy đều dùng chung phòng thiết kế này (`Thread-Shared`).
> 2. **Heap Area:** *Bãi Tập Kết & Nhà Kho Khổng Lồ.* Mỗi khi có lệnh đúc xe (`new`), robot sẽ tạo ra một chiếc xe thật và đem để ở bãi xe này (`Object`). Cả nhà máy dùng chung bãi xe này (`Thread-Shared`).
> 3. **JVM Stack Area:** *Bàn Làm Việc Cá Nhân Của Từng Kỹ Sư.* Mỗi kỹ sư (`Thread`) có một bàn làm việc riêng (`Thread-Private`). Khi bắt đầu làm một công việc (gọi hàm), kỹ sư đặt một chiếc khay hồ sơ (**Stack Frame**) lên bàn. Làm xong việc thì dẹp khay đi (`POP`).
> 4. **PC Register:** *Ngón Tay Trỏ Của Kỹ Sư Trên Sổ Quy Trình.* Chỉ chính xác kỹ sư đang đọc và làm tới dòng số mấy. Khi kỹ sư tạm dừng việc để làm việc khác (*Context Switching*), ngón tay vẫn giữ đúng dòng đó để lúc quay lại làm tiếp ngay lập tức!
> 5. **Native Method Stack:** *Chiếc Bộ Đàm Gọi Chuyên Gia C/C++ Bên Ngoài.* Khi cần can thiệp sâu vào hệ thống phần cứng mà quy chuẩn Java nội bộ không làm được, kỹ sư dùng bộ đàm gọi chuyên gia C/C++ của hệ điều hành hỗ trợ.

---

### 4.2 Bộ Ba Tam Giác Vàng trong OOP: Metaspace - Heap - Stack

Mặc dù JVM quản lý 5 vùng nhớ, nhưng trong **95% công việc lập trình hướng đối tượng (OOP) hàng ngày**, bạn chỉ cần nắm vững sự phối hợp nhịp nhàng giữa **Bộ Ba Tam Giác Vàng**:

```
 [ 1. METASPACE (Method Area) ]  ---------> [ 2. HEAP MEMORY ]  <--------- [ 3. STACK MEMORY ]
    Nơi nạp Bản thiết kế                       Nơi Đúc Bức tượng              Nơi cầm Chìa khóa
    public class Motorbike                     Object Motorbike               Motorbike bike = 0x99
    (Class Metadata + Bytecode)                (brand="Honda", speed=0)       (Biến tham chiếu & Frame)
```

1. **Metaspace (Khuôn vẽ):** Lưu trữ Class `Motorbike` và mã chỉ thị Bytecode của các method.
2. **Heap (Kho tượng):** Nơi đúc ra chiếc xe `Motorbike` thật sự từ lệnh `new`.
3. **Stack (Bàn làm việc):** Nơi biến `bike` cầm địa chỉ ô nhớ và nơi các lệnh gọi hàm được thực thi.

---

### 4.3 Giải phẫu Vùng nhớ 1: Method Area / Metaspace (Nơi Class và Bytecode cư ngụ)

- **Lịch sử tên gọi:**
  - Từ Java 7 trở về trước: Vùng này có tên là **PermGen (Permanent Generation)** và nằm bên trong bộ nhớ Heap. Do giới hạn dung lượng cố định, PermGen rất hay gây lỗi sập server `OutOfMemoryError: PermGen space`.
  - Từ **Java 8 trở đi**: Java đã loại bỏ hoàn toàn PermGen và thay thế bằng **Metaspace**. Metaspace không nằm trong Heap nữa mà nằm trực tiếp ở **Native Memory** (bộ nhớ RAM của hệ điều hành), có khả năng tự động mở rộng linh hoạt theo dung lượng RAM vật lý của máy tính.
- **Metaspace chứa những gì?**
  1. **Class Metadata:** Toàn bộ thông tin mô tả Class (tên class, class cha, interface thực thi, danh sách fields, danh sách methods).
  2. **Mã Bytecode:** Các chỉ lệnh máy ảo của các hàm sau khi được biên dịch từ file `.java`.
  3. **Biến tĩnh (`static` variables):** Các biến có từ khóa `static` thuộc về sở hữu chung của cả Class, tồn tại xuyên suốt vòng đời ứng dụng.
  4. **Run-Time Constant Pool:** Vùng chứa các hằng số cố định và String Literal Pool.

---

### 4.4 Giải phẫu Vùng nhớ 2: Heap Area (Nhà kho chứa toàn bộ Object)

- **Bản chất đời sống:** Là **Nhà kho tổng** của công ty, rộng thênh thang, bao la.
- **Heap chứa những gì?**
  - **Mọi thực thể Object** sinh ra từ từ khóa `new`.
  - Toàn bộ các mảng dữ liệu (`int[]`, `String[]`...).
  - **Biến thực thể (Instance Variables):** Bất kể thuộc tính của đối tượng là kiểu nguyên thủy (`int speed = 0`) hay kiểu tham chiếu (`String brand = "Honda"`), hễ nó là thành phần của Object thì **bắt buộc phải sống trên Heap bên trong thân thể của Object đó**!
- **Đặc điểm sống còn:**
  - Không tự mất đi khi hàm kết thúc.
  - Phải có bộ phận riêng là **Garbage Collector (GC)** đi quét dọn định kỳ các Object vô chủ.

---

### 4.5 Giải phẫu Vùng nhớ 3: JVM Stack Area & Cấu trúc Stack Frame (Lời gọi hàm)

- **Bản chất đời sống:** Là **Bàn làm việc cá nhân** với khay tài liệu LIFO (Last In First Out - Vào sau ra trước).
- Khi chạy tới một lời gọi hàm (ví dụ: `myBike.accelerate(40);`), JVM sẽ tạo ra một chiếc hộp gọi là **Stack Frame (Khung ngăn xếp)** và đẩy (PUSH) lên đỉnh Stack.

#### Cấu trúc bên trong một chiếc hộp Stack Frame:
```
+---------------------------------------------------------------+
|                      MỘT STACK FRAME                          |
+---------------------------------------------------------------+
| 1. Local Variable Table (Bảng biến cục bộ & tham số)          |
|    - slot 0: biến tham chiếu "this" (nếu là instance method)  |
|    - slot 1, 2...: các tham số truyền vào hàm                 |
|    - các biến cục bộ khai báo bên trong hàm (int a, double b) |
+---------------------------------------------------------------+
| 2. Operand Stack (Ngăn xếp toán hạng - Bàn tính nháp CPU)     |
|    - Nơi CPU nạp số, thực hiện phép toán cộng, trừ, nhân, chia|
+---------------------------------------------------------------+
| 3. Frame Data (Dữ liệu khung & Bảng ngoại lệ Exception Table) |
|    - Tham chiếu hằng số (Constant Pool Reference) ở Metaspace |
|    - Bảng bắt lỗi try-catch để điều hướng khi xảy ra Exception|
+---------------------------------------------------------------+
| 4. Return Address (Địa chỉ dòng lệnh quay về)                 |
|    - Ghi nhớ: "Chạy xong hàm này thì nhảy về dòng code số     |
|      mấy của hàm gọi (caller) để tiếp tục thực thi?"          |
+---------------------------------------------------------------+
```

- ⭐ **Bí mật thú vị của OOP (Vì sao hàm biết chiếc xe nào đang gọi nó?):**
  - Khi bạn viết `wave.accelerate(40);`, bạn có bao giờ tự hỏi: *"Trong Class Motorbike chỉ có hàm `accelerate(int amount)`, làm sao Java biết phải tăng tốc đúng cho xe Wave mà không tăng nhầm cho xe Exciter?"*
  - **Bí mật hậu trường:** Khi biên dịch ra Bytecode, JVM âm thầm biến lời gọi đó thành: `Motorbike.accelerate(wave, 40)`. JVM tự động đưa địa chỉ của chiếc Wave vào **`slot 0`** của bảng Local Variable Table dưới cái tên là con trỏ **`this`**!
  - Khi method chạy tới dòng `this.speed += amount;`, CPU chỉ việc nhìn vào `slot 0`, lấy địa chỉ chiếc Wave trên Heap và tăng tốc cho nó!
  - 👉 Đó cũng chính là lý do vì sao **trong các hàm `static` không tồn tại từ khóa `this`**, vì hàm static thuộc về Class nói chung chứ không phục vụ cho bất kỳ chiếc xe cụ thể nào cả!

- **Chu trình PUSH/POP (Vào sau ra trước):** 
  - Khi gọi hàm con ➡️ PUSH Frame mới lên đỉnh (Active Frame); Các frame bên dưới tạm dừng chờ.
  - Khi hàm con chạy xong (`return` hoặc `}`) ➡️ POP Frame ra và tiêu hủy tức thì ($0$ giây), con trỏ lệnh nhảy về đúng **Return Address**.

> 💡 **Mẹo nhớ trong 3 giây:** 
> - Gọi hàm = Đặt thêm khay hồ sơ lên bàn làm việc (`PUSH Stack Frame`).
> - Hàm chạy xong = Vứt khay hồ sơ vào sọt rác tức thì (`POP Stack Frame`).
> - Chiếc ghế VIP số 0 (`slot 0`) trong khay luôn dành riêng cho con trỏ `this`!

---

### 4.6 Hai vùng nhớ phụ trợ: PC Register (Đếm chỉ lệnh) & Native Method Stack

Mặc dù ít khi trực tiếp thao tác, nhưng hiểu về 2 vùng này giúp bạn có cái nhìn hoàn chỉnh về máy tính:

1. **PC Register (Program Counter Register - Thanh ghi đếm chương trình):**
   - Mỗi Thread có 1 PC Register riêng biệt.
   - **Nhiệm vụ:** Lưu địa chỉ của dòng lệnh Bytecode tiếp theo trong Metaspace mà CPU cần thi hành cho Thread đó. Khi máy tính chuyển đổi qua lại giữa hàng trăm luồng (*Context Switching*), CPU nhờ PC Register mà biết được luồng này đang chạy dở ở dòng nào để tiếp tục mà không bị chạy sai.
2. **Native Method Stack (Ngăn xếp hàm bản địa):**
   - Tương tự như JVM Stack, nhưng dành riêng cho các hàm viết bằng ngôn ngữ C hoặc C++ gốc của hệ điều hành, được gọi vào Java thông qua cơ chế **JNI (Java Native Interface)** (ví dụ: các hàm đọc file cấp thấp, giao tiếp phần cứng...).

---

### 4.7 Chụp X-Quang RAM: Trace từng dòng code qua Metaspace, Heap và Stack

Hãy theo dõi một kịch bản hoàn chỉnh:

```java
public class MemoryTrace {
    public static void main(String[] args) {
        int age = 20;
        Motorbike myBike = new Motorbike("Honda Wave", "Đỏ");
        ride(myBike);
        System.out.println("Tốc độ sau cùng: " + myBike.speed);
    }

    public static void ride(Motorbike bikeParam) {
        int bonusSpeed = 40;
        bikeParam.accelerate(bonusSpeed);
    }
}
```

```
TÌNH HUỐNG 1: Khi hàm main đang chạy (trước khi gọi ride):

     STACK MEMORY (Đang chạy main)                       HEAP MEMORY
  +--------------------------------+          +-------------------------------+
  | [Khay main]                    |          | Object [Motorbike] (Tại 0x99) |
  |   - age: 20                    |          |   - brand: "Honda Wave"       |
  |   - myBike: [ 0x99 ] ----------+--------->|   - color: "Đỏ"               |
  +--------------------------------+          |   - speed: 0                  |
                                              |   - isEngineOn: false         |
                                              +-------------------------------+
-----------------------------------------------------------------------------------
TÌNH HUỐNG 2: Khi bước vào hàm ride(myBike):
-> Khay ride() được ĐÈ LÊN TRÊN khay main(). Cả 2 biến cùng trỏ tới 1 Object trên Heap!

     STACK MEMORY                                        HEAP MEMORY
  +--------------------------------+ (Đỉnh Stack)
  | [Khay ride]                    |          +-------------------------------+
  |   - bonusSpeed: 40             |          | Object [Motorbike] (Tại 0x99) |
  |   - bikeParam: [ 0x99 ] -------+-----+--->|   - brand: "Honda Wave"       |
  +--------------------------------+     |    |   - color: "Đỏ"               |
  | [Khay main]                    |     |    |   - speed: 40 (Đã tăng ga!)   |
  |   - age: 20                    |     |    |   - isEngineOn: true          |
  |   - myBike: [ 0x99 ] ----------+-----+    +-------------------------------+
  +--------------------------------+ (Đáy Stack)
-----------------------------------------------------------------------------------
TÌNH HUỐNG 3: Khi hàm ride() chạy xong (Gặp dấu `}` của ride):
-> Khay ride() bị VỨT VÀO SỌT RÁC ngay lập tức! Nhưng Object trên Heap vẫn nguyên vẹn!

     STACK MEMORY                                        HEAP MEMORY
  +--------------------------------+          +-------------------------------+
  | [Khay main]                    |          | Object [Motorbike] (Tại 0x99) |
  |   - age: 20                    |          |   - brand: "Honda Wave"       |
  |   - myBike: [ 0x99 ] ----------+--------->|   - color: "Đỏ"               |
  +--------------------------------+          |   - speed: 40 (Vẫn là 40!)    |
                                              |   - isEngineOn: true          |
                                              +-------------------------------+
```

👉 **Kết luận:** Biến `bikeParam` trên Stack chết đi theo hàm `ride()`, nhưng chiếc xe tại `0x99` trên **Heap** vẫn sống và giữ tốc độ mới là $40\text{ km/h}$.

---

### 4.8 Hiện tượng hai biến trỏ chung một Object trên Heap (`b2 = b1`)

```java
Motorbike b1 = new Motorbike("Honda", "Đỏ");
Motorbike b2 = b1; // Gán tham chiếu: b2 copy địa chỉ từ b1

b2.color = "Xanh"; // Đổi màu thông qua biến b2
System.out.println(b1.color); // In ra gì? -> KẾT QUẢ LÀ "Xanh"!
```

```
           STACK MEMORY                                     HEAP MEMORY
    +-------------------------+                     +-------------------------------+
    |                         |                     |   Object duy nhất trên Heap   |
    |  b1: [ 0x1111 ] --------+-------------------->|   Địa chỉ: 0x1111             |
    |                         |                     |   - brand: "Honda"            |
    |                         |                     |   - color: "Xanh" (Đã bị đổi) |
    |  b2: [ 0x1111 ] --------+-------------------->|   - speed: 0                  |
    +-------------------------+                     +-------------------------------+
```

> ⚠️ **Ghi nhớ:** Lệnh `b2 = b1` **không tạo ra chiếc xe thứ 2**! Nó chỉ tạo ra chiếc chìa khóa thứ 2 trỏ chung vào cùng một chiếc xe trên Heap. Người này sơn lại xe thì người kia mở khóa cũng sẽ thấy xe đổi màu!

---

### 4.9 Cơ chế dọn rác của Garbage Collector (GC): Khi nào Object biến thành "Rác"?

Trong các ngôn ngữ bậc thấp như C/C++, lập trình viên phải tự tay cấp phát (`malloc()`) và giải phóng bộ nhớ (`free()`). Nếu quên, bộ nhớ sẽ bị rò rỉ (**Memory Leak**) làm sập máy.

Java giải quyết triệt để vấn đề này bằng **Garbage Collector (GC - Bộ thu gom rác tự động)**, chạy ngầm để dọn dẹp các ô nhớ không còn sử dụng trên **Heap**.

#### 4.9.1 Khái niệm "Rác" trong Java: Thế nào là một đối tượng Unreachable?

- **Biến tham chiếu trên Stack** = Sợi dây cầm trong tay.
- **Thực thể Object trên Heap** = Quả bóng bay trên trời.

```
   BÀN LÀM VIỆC (STACK)                            KHO HEAP
+-------------------------+              +----------------------------+
|  Biến bike: [ 0x1111 ] -+-(Sợi dây)--->| Object xe máy (Tại 0x1111) |
+-------------------------+              +----------------------------+
```

> 🗑️ **Quy tắc vàng của GC:** 
> Một Object chỉ bị coi là **"RÁC"** khi nó rơi vào trạng thái **Unreachable (Không thể chạm tới được)** — tức là **không còn bất kỳ sợi dây tham chiếu nào từ Stack trỏ tới nó nữa**.

#### 4.9.2 Bốn tình huống kinh điển biến một Object thành "RÁC":
1. **Gán biến về `null`:** `bike = null;` (Cắt đứt sợi dây, xe trôi nổi vô chủ).
2. **Gán tham chiếu sang Object khác:** `b1 = b2;` (`b1` buông tay khỏi xe cũ để cầm chìa khóa xe mới, xe cũ biến thành rác).
3. **Đối tượng sinh ra cục bộ trong hàm:** Sau khi hàm chạy xong, Stack Frame bị hủy, Object trên Heap trở thành vô chủ.
4. **Đối tượng vô danh (Anonymous Object):** `new Motorbike("Ducati", "Đỏ").startEngine();` (Chạy xong không ai giữ địa chỉ ➡️ thành rác ngay lập tức).

#### 4.9.3 Thuật toán cốt lõi: Mark & Sweep & Compact

```
[ CỌC SẮT GC ROOTS ] (Biến trên Stack / Biến static)
        |
    (Sợi dây 1)
        |
        v
   [ Object A ] (Alive - Đánh dấu sống)
        |
    (Sợi dây 2)
        |
        v
   [ Object B ] (Alive - Đánh dấu sống)

-----------------------------------------------------------
   [ Object C ] <========> [ Object D ] (Mồ côi GC Roots!)
   (Hai quả bóng tự buộc dây vào nhau nhưng không ai cầm)
   ===> GC COI LÀ RÁC VÀ TIÊU HỦY CẢ HAI!
```

1. **Mark (Đánh dấu từ cọc đất GC Roots):** Xuất phát từ các **GC Roots** (biến cục bộ trên Stack, biến static trong Metaspace), lần theo các sợi dây tham chiếu. Bất kỳ đối tượng nào còn chạm tới được sẽ được dán nhãn "Alive" (Còn sống).
2. **Sweep (Quét sạch):** GC càn quét qua toàn bộ Heap, tiêu hủy ngay lập tức mọi đối tượng không được dán nhãn "Alive".
3. **Compact (Dồn nén):** Sau khi dọn dẹp, bộ nhớ sẽ bị lồi lõm lỗ chỗ (phân mảnh). GC gom tất cả các Object còn sống dồn về một góc Heap để tạo ra một khoảng trống liền mạch rộng lớn cho các Object tương lai.

> 🌟 **Vũ khí tối thượng của Java: Xử lý Tham Chiếu Vòng Tròn (Circular Reference):**
> Trong các hệ thống cũ, nếu Object A giữ Object B và Object B lại giữ ngược lại Object A, hệ thống sẽ tưởng chúng còn dùng nên không dám xóa. Nhưng với Java, vì **không có sợi dây nào từ GC Roots (Stack) chạm tới được cụm A-B này**, GC coi cả hai là mồ côi và **quét sạch cả hai** không thương tiếc!

#### 4.9.4 Phân chia thế hệ trong Heap (Generational Collection):
- **Young Generation (Eden Space & Survivor S0/S1):** Nơi Object mới sinh ra từ lệnh `new`. Phần lớn Object "chết trẻ" ngay tại đây (ví dụ: biến tạm trong hàm). Được dọn bằng **Minor GC** với tốc độ cực nhanh (vài mili-giây).
- **Old Generation (Tenured Space):** Chứa các Object "sống lâu năm" (vượt qua 15 lần dọn rác ở Young Gen mà vẫn còn sống, ví dụ: Connection pool, Caching). Được dọn bằng **Major GC / Full GC** (tốn nhiều tài nguyên hơn).
- **Sự thật về `System.gc()`:** Lệnh này chỉ mang tính chất *"gợi ý/thỉnh cầu"* JVM: *"Bác GC ơi rảnh thì ghé qua dọn giùm con"*, chứ **không thể ép buộc** GC chạy ngay lập tức. Đừng bao giờ lạm dụng lệnh này trong code thực tế!

> 💡 **Mẹo nhớ trong 3 giây về GC:** 
> - Object chỉ là RÁC khi **đứt toàn bộ dây liên lạc** với Stack (`Unreachable`).
> - Dọn rác là việc tự động của JVM, lập trình viên Java không cần bận tâm `malloc` hay `free` như C/C++!

---

### 4.10 Hai cơn ác mộng bộ nhớ kinh điển: StackOverflowError vs OutOfMemoryError

| Cơn ác mộng | Bản chất đời thực | Nguyên nhân thường gặp trong code | Cách khắc phục |
| :--- | :--- | :--- | :--- |
| **`StackOverflowError`** | Khay hồ sơ trên bàn bị xếp quá cao làm sập bàn làm việc (**Tràn Stack**). | Hàm đệ quy gọi chính nó liên tục mà **quên viết điều kiện dừng**. | Kiểm tra lại điều kiện dừng của đệ quy (`base case`), chuyển sang dùng vòng lặp `for`/`while`. |
| **`OutOfMemoryError: Java heap space`** | Nhà kho rộng mênh mông nhưng bị nhét chật cứng không còn 1 chỗ trống (**Tràn Heap**). | Dùng `new` tạo hàng triệu Object trong vòng lặp vô hạn và nhét vào một `List` (GC không thể dọn vì vẫn còn tham chiếu). | Tối ưu thuật toán, phân trang khi truy vấn dữ liệu, giải phóng biến tham chiếu khi dùng xong (`list.clear()`). |
| **`OutOfMemoryError: Metaspace`** | Phòng thiết kế bị chất đầy bởi hàng trăm ngàn bản vẽ Class (**Tràn Metaspace**). | Ứng dụng nạp quá nhiều Class động do các thư viện sinh bytecode liên tục mà không giải phóng. | Tăng dung lượng Metaspace (`-XX:MaxMetaspaceSize`) hoặc kiểm tra rò rỉ ClassLoader. |

> 💡 **Mẹo nhớ trong 3 giây:** 
> - **Cháy Stack (`StackOverflow`):** Do gọi hàm đệ quy không lối thoát.
> - **Cháy Heap (`OutOfMemory`):** Do `new` vô tội vạ mà vẫn giữ chặt tham chiếu không buông!

---

## 5️⃣ Nâng Cấp Tư Duy: "Trong Đối Tượng Lại Có Đối Tượng" (Composition)

### 5.1 Kết nối các kiểu dữ liệu đã học: Class cũng chính là một kiểu dữ liệu mới!

Ở các bài trước, chúng ta đã học:
- **Kiểu nguyên thủy:** `int`, `double`, `boolean`, `char`...
- **Kiểu tham chiếu cơ bản:** `String`, mảng (`int[]`, `String[]`...).

Và bây giờ, một chân trời kiến trúc mở ra:
> 💡 **Bản thân mỗi Class bạn tạo ra CHÍNH LÀ MỘT KIỂU DỮ LIỆU MỚI (User-Defined Type)!**
> Điều này có nghĩa là: **Bên trong một Class, các Fields không chỉ là `int`, `String`... mà hoàn toàn có thể là MỘT CLASS KHÁC!**
> 
> Thuật ngữ chuyên ngành gọi đây là quan hệ **HAS-A (Có một)** hoặc **Object Composition (Tổng hợp đối tượng)**.

> 🧩 **Ẩn dụ "Bộ lắp ghép LEGO":** 
> Bạn không thể đúc một chiếc lâu đài bằng một khối nhựa đúc liền duy nhất. Bạn lắp ghép lâu đài từ những khối gạch nhỏ: khối làm tường, khối làm mái ngói, khối làm cửa sổ. 
> Trong lập trình cũng y hệt: Thay vì nhồi nhét 50 thuộc tính vào một Class `Smartphone` khổng lồ, ta tách nhỏ thành các Class linh kiện độc lập (`Screen`, `Battery`, `Camera`) rồi lắp ghép chúng lại!

> 💡 **Mẹo nhớ trong 3 giây:** 
> Điện thoại **CÓ MỘT (HAS-A)** Màn hình, xe máy **CÓ MỘT (HAS-A)** Động cơ ➡️ Đó chính là Composition (Đối tượng chứa đối tượng)!

---

### 5.2 Quan sát thực tế: Bảng thông số kỹ thuật Smartphone tại CellphoneS

Khi bạn lướt website **CellphoneS** để xem sản phẩm **iPhone 16 Pro Max**:

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
+-----------------------------------------------------------------------+
```

Hãy nhìn bản chất thế giới thực:
- Chiếc điện thoại **không phải là một khối sắt đúc liền một mảnh**.
- Chiếc điện thoại là một đối tượng lớn **được lắp ghép từ nhiều linh kiện (nhiều đối tượng con)**: Màn hình (`Screen`), Pin (`Battery`), Cụm Camera (`Camera`)...

---

### 5.3 Mô hình hóa bằng Java: Lắp ráp các linh kiện (`Screen`, `Battery`) vào `CellphoneSProduct`

#### Bước 1: Tạo các Class linh kiện con
```java
// Linh kiện 1: Màn hình
public class Screen {
    double sizeInches;      // 6.9 inch
    String panelType;       // "Super Retina XDR OLED"
    int refreshRateHz;      // 120 Hz

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
// Linh kiện 2: Viên Pin
public class Battery {
    int capacityMah;        // 4685 mAh
    int fastChargeWatt;     // 30 W
    int currentPercent;     // Phần trăm pin hiện tại (100%)

    public Battery(int capacityMah, int fastChargeWatt) {
        this.capacityMah = capacityMah;
        this.fastChargeWatt = fastChargeWatt;
        this.currentPercent = 100;
    }

    public void consume(int percent) {
        this.currentPercent -= percent;
        if (this.currentPercent < 0) this.currentPercent = 0;
        System.out.println("  ⚡ Pin đã tiêu hao " + percent + "%. Còn lại: " + this.currentPercent + "%");
    }
}
```

---

#### Bước 2: Tạo Class cha `CellphoneSProduct` chứa các đối tượng con làm Field

```java
public class CellphoneSProduct {
    // 1. CÁC THUỘC TÍNH ĐƠN GIẢN ĐÃ HỌC
    String productName;     // "iPhone 16 Pro Max"
    String brand;           // "Apple"
    double price;           // 34990000.0
    int storageGB;          // 256
    boolean isAvailable;    // true

    // 2. CÁC THUỘC TÍNH LÀ ĐỐI TƯỢNG (OBJECT TRONG ĐỐI TƯỢNG)
    Screen screen;          // Kiểu dữ liệu là Class Screen!
    Battery battery;        // Kiểu dữ liệu là Class Battery!

    public CellphoneSProduct(String productName, String brand, double price, int storageGB, 
                            boolean isAvailable, Screen screen, Battery battery) {
        this.productName = productName;
        this.brand = brand;
        this.price = price;
        this.storageGB = storageGB;
        this.isAvailable = isAvailable;
        this.screen = screen;
        this.battery = battery;
    }

    public void printSpecSheet() {
        System.out.println("=================================================");
        System.out.println("📱 CHI TIẾT SẢN PHẨM TẠI CELLPHONES");
        System.out.println("Tên: " + productName + " (" + storageGB + "GB) | Hãng: " + brand);
        System.out.println("Giá bán: " + String.format("%,.0f", price) + " VNĐ | Trạng thái: " + (isAvailable ? "Còn hàng" : "Hết hàng"));
        
        System.out.println("\n--- THÔNG SỐ PHẦN CỨNG CHI TIẾT ---");
        screen.displayScreenInfo(); // Gọi method của đối tượng Screen con
        System.out.println("  + Pin: " + battery.capacityMah + " mAh (Sạc nhanh " + battery.fastChargeWatt + "W)");
        System.out.println("=================================================");
    }

    public void playGame(String gameName) {
        System.out.println("\n🎮 Đang chơi game '" + gameName + "' trên " + productName + "...");
        // Tác động trực tiếp vào đối tượng Pin bên trong
        this.battery.consume(20);
    }
}
```

---

#### Bước 3: Lắp ráp và chạy thử tại quầy CellphoneS

```java
public class CellphoneSDemo {
    public static void main(String[] args) {
        // 1. Chế tạo linh kiện rời
        Screen oledScreen = new Screen(6.9, "Super Retina XDR OLED", 120);
        Battery appleBattery = new Battery(4685, 30);

        // 2. Lắp ráp thành chiếc điện thoại hoàn chỉnh
        CellphoneSProduct iphone16 = new CellphoneSProduct(
            "iPhone 16 Pro Max", "Apple", 34990000.0, 256, true, oledScreen, appleBattery
        );

        // 3. Xem thông số
        iphone16.printSpecSheet();

        // 4. Chơi game làm tụt pin bên trong
        iphone16.playGame("Liên Quân Mobile");
    }
}
```

---

### 5.4 Hậu trường RAM Heap lồng Heap: Giải mã cú pháp gọi dấu chấm liên tiếp

Khi bạn viết code: `iphone16.battery.currentPercent`, JVM làm gì trong thanh RAM?

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
                                    |   - currentPercent: 80                  |
                                    +-----------------------------------------+
```

> 🎯 **Bản chất giải mã:**
> - `iphone16`: Nhảy đến địa chỉ `0xAAAA` trên Heap.
> - `.battery`: Lấy ra địa chỉ `0xCCCC` đang được cất trong thuộc tính `battery`.
> - `.currentPercent`: Nhảy tiếp sang ô nhớ `0xCCCC` để đọc con số `80`.
> 
> Mọi đối tượng con đều nằm tự do trên **Heap**, đối tượng cha chỉ nắm giữ **địa chỉ tham chiếu** của chúng!

---

## 6️⃣ Tổng Kết Cốt Lõi & Top 5 "Cạm Bẫy" Cần Tránh Khi Nhập Môn OOP

### 6.1 Tóm tắt các khái niệm then chốt

```
+---------------------------------------------------------------------------------------+
|  KHÁI NIỆM     |  BẢN CHẤT ĐỜI THỰC                  |  ÁNH XẠ VÀO JAVA CODE           |
|----------------|-------------------------------------|--------------------------------|
|  Class         |  Khuôn đúc tượng / Bản thiết kế     |  public class MyClass { ... }  |
|  Constructor   |  Chiếc phễu rót nguyên liệu         |  public MyClass(...) { ... }   |
|  Object        |  Bức tượng thành phẩm cụ thể        |  MyClass obj = new MyClass()   |
|  State         |  Đặc điểm, thông số của đối tượng   |  Fields / Biến thực thể        |
|  Behavior      |  Hành động, chức năng đối tượng     |  Methods / Phương thức         |
|  Identity      |  Số khung xe / Biển số phân biệt    |  Địa chỉ ô nhớ RAM trên Heap   |
|  Stack         |  Bàn làm việc cá nhân (dọn ngay)    |  Lưu biến cục bộ, con trỏ ref  |
|  Heap          |  Nhà kho tổng (GC dọn định kỳ)      |  Lưu toàn bộ các Object thật   |
+---------------------------------------------------------------------------------------+
```

```
                   🗺️ BẢN ĐỒ TƯ DUY 30 GIÂY: CHINH PHỤC BÀI 9
  
  [ CLASS (Metaspace) ] ====================> [ CONSTRUCTOR ]
   (Bản vẽ / Khuôn mẫu)                        (Phễu rót dữ liệu)
            |                                          |
            | (Đúc ra bằng từ khóa "new")              v
            +--------------------------------> [ OBJECT (Heap) ]
                                                - State: Fields
                                                - Behavior: Methods
                                                - Identity: Địa chỉ RAM (0x...)
                                                       ^
                                                       | (Sợi dây tham chiếu)
                                              [ BIẾN REF (Stack) ]
                                              (Cắt dây = Thành rác GC dọn)
```

---

### 6.2 Top 5 cạm bẫy kinh điển người mới hay mắc phải

> 🚩 **Cạm bẫy 1: Quên từ khóa `new` dẫn đến `NullPointerException`**
> ```java
> Motorbike bike; // Mới chỉ tạo ra tấm thẻ trên Stack, đang trỏ vào null!
> bike.startEngine(); // SẬP APP: java.lang.NullPointerException (Xe chưa hề tồn tại trên Heap!)
> ```
> 👉 **Khắc phục:** Luôn nhớ tạo đối tượng bằng `new Motorbike(...)` trước khi gọi phương thức.

---

> 🚩 **Cạm bẫy 2: Nhầm lẫn giữa biến tham chiếu và bản thân đối tượng (So sánh `==`)**
> ```java
> Motorbike b1 = new Motorbike("Honda", "Đỏ");
> Motorbike b2 = new Motorbike("Honda", "Đỏ");
> System.out.println(b1 == b2); // KẾT QUẢ: false!
> ```
> 👉 **Giải thích:** Dù hai chiếc xe cùng màu, cùng hãng, toán tử `==` so sánh **địa chỉ ô nhớ** trên Heap (`0x1111 == 0x2222`). Đây là hai chiếc xe độc lập tại hai ô nhớ khác nhau nên kết quả là `false`.

---

> 🚩 **Cạm bẫy 3: Đặt kiểu trả về cho Constructor**
> ```java
> public void Motorbike(String brand, String color) { ... } // SAI NGUY HIỂM!
> ```
> 👉 **Giải thích:** Khi thêm `void` hay bất kỳ kiểu trả về nào, Java sẽ biến nó thành một **method thông thường**, nó **không còn là Constructor nữa**!

---

> 🚩 **Cạm bẫy 4: Quên từ khóa `this` khi tên tham số trùng tên field**
> ```java
> public Motorbike(String brand, String color) {
>     brand = brand; // Gán tham số brand cho chính tham số brand! Field của đối tượng vẫn bị null!
> }
> ```
> 👉 **Khắc phục:** Bắt buộc dùng `this.brand = brand;`. Từ khóa `this` đại diện cho "thực thể hiện tại đang được xử lý".

---

> 🚩 **Cạm bẫy 5: Đối tượng lồng nhau chưa được khởi tạo (Nested NullPointerException)**
> ```java
> CellphoneSProduct phone = new CellphoneSProduct("iPhone 16", "Apple", 30000000, 128, true, null, null);
> phone.screen.displayScreenInfo(); // SẬP APP: java.lang.NullPointerException!
> ```
> 👉 **Giải thích:** Linh kiện `screen` bên trong `phone` đang nhận giá trị `null` (chưa được lắp ráp màn hình thật). Khi cố tình chấm tiếp `.displayScreenInfo()`, chương trình sẽ sập ngay!
