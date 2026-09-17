# Bài 11: 4 Tính Chất Cốt Lõi Của Lập Trình Hướng Đối Tượng (OOP)

Chào mừng bạn đến với bài học cực kỳ quan trọng trong chuỗi kiến thức về Lập trình hướng đối tượng (OOP)! 

Hãy tưởng tượng OOP giống như cách chúng ta xây dựng và tương tác với thế giới thực. Để quản lý sự phức tạp của các phần mềm lớn, các nhà sáng lập ra OOP đã định nghĩa **4 tính chất cốt lõi**. Nắm vững 4 nguyên lý này, bạn không chỉ biết gõ code mà còn biết cách "thiết kế" ra những chương trình thông minh, dễ bảo trì và dễ mở rộng.

Dưới đây là lời giải thích chi tiết, đi từ đời thực vào trong code Java một cách dễ hiểu nhất!

---

## 1. Tính Đóng Gói (Encapsulation) 💊

**Định nghĩa dân dã:** 
Giống như một **viên thuốc nhộng**. Bạn chỉ cần uống viên thuốc để chữa bệnh mà không cần biết bên trong chứa những hóa chất gì, tỷ lệ bao nhiêu. Mọi thứ bên trong đã được "đóng gói" và che giấu đi để:
1. Bảo vệ các hóa chất bên trong khỏi bị biến đổi bởi môi trường ngoài.
2. Bảo vệ người dùng khỏi việc dùng sai liều lượng nếu tự pha chế.

**Trong lập trình:**
Tính đóng gói là kỹ thuật **che giấu thông tin** (trạng thái, dữ liệu/thuộc tính) bên trong của một đối tượng. Các thuộc tính này thường được đặt là `private`. Bên ngoài không thể trực tiếp xem hay sửa các thuộc tính này. Thay vào đó, lớp sẽ cung cấp các "cửa ngõ" an toàn là các phương thức công khai (thường là `public getter/setter`) để tương tác với dữ liệu đó.

**Tại sao phải đóng gói? (Mục đích)**
- **Kiểm soát tính hợp lệ của dữ liệu:** Bạn có thể đặt các câu lệnh `if/else` trong hàm `setter` để cấm người khác gán dữ liệu bậy bạ (ví dụ: cấm gán tuổi = số âm).
- **Bảo mật và Read-only:** Bạn có thể chỉ tạo hàm `getter` mà không tạo `setter`, biến thuộc tính đó thành chỉ đọc.
- **Che giấu cấu trúc bên trong:** Bạn có thể thay đổi cách tính toán bên trong lớp mà không làm hỏng code của những người đang sử dụng lớp của bạn.

**Ví dụ Java chi tiết:**
```java
public class TaiKhoanNganHang {
    // 1. Dữ liệu bị "đóng gói" (private)
    // Người ngoài không thể gọi `tk.soDu = 1000000;` một cách tự do
    private double soDu; 
    private String tenChuTaiKhoan;

    public TaiKhoanNganHang(String ten, double soDuBanDau) {
        this.tenChuTaiKhoan = ten;
        this.soDu = soDuBanDau;
    }

    // 2. Cung cấp hàm getter để "xem" số dư (chỉ đọc)
    public double getSoDu() {
        return this.soDu;
    }

    // 3. Không có setSoDu(), mà thay vào đó là nạp và rút có kiểm soát
    public void napTien(double soTien) {
        if (soTien > 0) {
            soDu += soTien; // Cho phép tăng số dư nếu số tiền hợp lệ
            System.out.println("Nạp thành công: " + soTien);
        } else {
            System.out.println("Lỗi: Số tiền nạp phải lớn hơn 0!");
        }
    }

    public void rutTien(double soTien) {
        if (soTien > 0 && soTien <= soDu) {
            soDu -= soTien;
            System.out.println("Rút thành công: " + soTien);
        } else {
            System.out.println("Lỗi: Số dư không đủ hoặc số tiền rút không hợp lệ!");
        }
    }
}
```

---

## 2. Tính Kế Thừa (Inheritance) 🧬

**Định nghĩa dân dã:**
Giống như việc bạn thừa hưởng gen, tài sản và danh tiếng từ cha mẹ mình. Cha mẹ bạn đã xây dựng sẵn một căn nhà, mua một chiếc xe hơi. Khi bạn lớn lên, bạn được phép **sử dụng lại** ngôi nhà và chiếc xe đó mà không cần phải tự xây lại từ đầu. Thậm chí, bạn có thể đi làm và tự sắm thêm chiếc xe máy cho riêng mình (mở rộng thêm).

**Trong lập trình:**
Tính kế thừa cho phép một lớp mới (Lớp con - Subclass / Child class) được thừa hưởng các thuộc tính và phương thức từ một lớp đã tồn tại (Lớp cha - Superclass / Parent class). Trong Java, chúng ta sử dụng từ khóa `extends`.

**Tại sao phải kế thừa? (Mục đích)**
- **Tái sử dụng mã nguồn (Code Reuse):** Tránh việc phải copy-paste các đoạn code giống nhau ở nhiều class khác nhau.
- **Tạo ra cấu trúc phân cấp:** Thể hiện mối quan hệ **"IS-A" (Là một)** trong thực tế. Ví dụ: "Chó *là một* Động vật", "Xe máy *là một* Phương tiện".

**Những điều cần lưu ý:**
- Lớp con thừa hưởng mọi thứ public/protected từ lớp cha, nhưng **KHÔNG thừa hưởng** các thuộc tính/phương thức `private` của cha.
- Java chỉ hỗ trợ **Đơn kế thừa** (Mỗi lớp con chỉ có đúng 1 cha).

**Ví dụ Java chi tiết:**
```java
// LỚP CHA: Nơi chứa những đặc điểm chung nhất
public class NhanVien {
    protected String ten; // Dùng protected để lớp con có thể truy cập
    protected double luongCoBan;

    public NhanVien(String ten, double luongCoBan) {
        this.ten = ten;
        this.luongCoBan = luongCoBan;
    }

    public void chamCong() {
        System.out.println(ten + " đang chấm công lúc 8h sáng.");
    }
}

// LỚP CON: Kế thừa (extends) từ NhanVien
public class LapTrinhVien extends NhanVien {
    private String ngonNguLapTrinh; // Thuộc tính mở rộng riêng của con

    public LapTrinhVien(String ten, double luongCoBan, String ngonNguLapTrinh) {
        super(ten, luongCoBan); // Gọi hàm khởi tạo của lớp cha
        this.ngonNguLapTrinh = ngonNguLapTrinh;
    }

    public void vietCode() {
        // Phương thức riêng của Lập trình viên
        System.out.println(ten + " đang gõ code " + ngonNguLapTrinh);
    }
}

// KHI SỬ DỤNG:
LapTrinhVien dev = new LapTrinhVien("Khôi", 1000, "Java");
dev.chamCong(); // Dùng lại hành động của lớp cha
dev.vietCode(); // Dùng hành động của riêng mình
```

---

## 3. Tính Đa Hình (Polymorphism) 🎭

**Định nghĩa dân dã:**
"Đa" là nhiều, "Hình" là hình thái. Đa hình là khả năng cùng một hành động nhưng lại có cách thực hiện khác nhau tùy thuộc vào đối tượng đang thực hiện hành động đó. 
Ví dụ với mệnh lệnh **"Hãy Kêu Lên!"**: 
- Nếu bạn ra lệnh cho con chó -> nó sủa "Gâu gâu".
- Nếu bạn ra lệnh cho con mèo -> nó kêu "Meo meo".
- Nếu bạn ra lệnh cho con vịt -> nó kêu "Cạp cạp".
Cùng một lời gọi "kêu", nhưng mỗi sinh vật lại có cách phản hồi (hình thái) khác nhau.

**Trong lập trình:**
Đa hình cho phép một biến (có kiểu là lớp cha) tham chiếu đến đối tượng của lớp con, và tùy thuộc vào đối tượng con thực sự là gì, nó sẽ gọi đúng phương thức tương ứng của đối tượng đó.
Có 2 loại đa hình phổ biến:
1. **Đa hình lúc biên dịch (Compile-time):** Thể hiện qua **Method Overloading** (Nạp chồng phương thức) - Nhiều hàm cùng tên trong 1 class nhưng khác tham số.
2. **Đa hình lúc chạy (Run-time):** Thể hiện qua **Method Overriding** (Ghi đè phương thức) kết hợp với **Upcasting** (Ép kiểu lên). Đây là dạng đa hình quan trọng nhất của OOP.

**Tại sao phải dùng đa hình? (Mục đích)**
- Khả năng mở rộng vô bờ bến (Extensibility). Bạn có thể viết các đoạn code chung chung làm việc với "Lớp cha", sau này nếu có thêm nhiều "Lớp con" mới, code cũ của bạn vẫn chạy đúng mà không cần sửa đổi (Nguyên lý Open/Closed).

**Ví dụ Java chi tiết (Run-time Polymorphism):**
```java
class DongVat {
    public void keu() {
        System.out.println("Tiếng kêu chung chung...");
    }
}

class Cho extends DongVat {
    @Override // Ghi đè phương thức của cha
    public void keu() {
        System.out.println("Gâu gâu!"); 
    }
}

class Meo extends DongVat {
    @Override // Ghi đè phương thức của cha
    public void keu() {
        System.out.println("Meo meo!"); 
    }
}

public class Main {
    public static void main(String[] args) {
        // Upcasting: Biến kiểu DongVat nhưng trỏ vào đối tượng Cho/Meo
        DongVat dv1 = new Cho();
        DongVat dv2 = new Meo();

        // Cùng gọi hàm keu(), nhưng kết quả ra khác nhau
        dv1.keu(); // Kết quả: "Gâu gâu!"
        dv2.keu(); // Kết quả: "Meo meo!"
    }
}
```
*Lưu ý:* Trình biên dịch chỉ biết `dv1` là `DongVat`, nhưng khi chạy (run-time), máy ảo Java nhận ra bản chất của `dv1` là `Cho`, nên nó gọi hành động kêu của `Cho`.

---

## 4. Tính Trừu Tượng (Abstraction) 🌫️

**Định nghĩa dân dã:**
Khi bạn đi xe máy, bạn chỉ cần biết cách vặn ga, bóp phanh là xe chạy chậm lại hoặc tăng tốc. Bạn hoàn toàn **không cần quan tâm** pít-tông bên trong động cơ hoạt động ra sao, bugi đánh lửa thế nào, tỷ lệ xăng - không khí trộn ra sao. 
Trừu tượng là kỹ thuật **chỉ hiển thị những chức năng cần thiết ra bên ngoài** và **che giấu đi các chi tiết thực thi phức tạp bên trong**.

*(Ghi chú để không nhầm lẫn: Tính đóng gói là giấu dữ liệu để bảo vệ. Tính trừu tượng là giấu sự phức tạp của hệ thống để dễ sử dụng).*

**Trong lập trình:**
Tính trừu tượng giúp chúng ta tập trung vào **đối tượng cần phải làm gì (What it does)** thay vì quan tâm tới **nó làm điều đó như thế nào (How it does)**. 
Trong Java, tính trừu tượng được hiện thực hóa bởi:
- **Abstract class (Lớp trừu tượng):** Trừu tượng một phần (có thể chứa cả hàm có code và hàm không có code).
- **Interface (Giao diện):** Trừu tượng hoàn toàn (chỉ chứa các khai báo hàm, giống như một bản hợp đồng).

**Tại sao phải dùng trừu tượng? (Mục đích)**
- Giảm thiểu sự phức tạp cho người sử dụng class. Họ chỉ cần nhìn vào interface là biết class có thể làm gì mà không cần đọc hàng ngàn dòng code logic bên trong.
- Bắt buộc các lớp con phải tuân thủ theo một "bản thiết kế" hay "khuôn mẫu" chung.

**Ví dụ Java chi tiết (sử dụng Abstract Class):**
```java
// Bản thiết kế trừu tượng
public abstract class HinhHoc {
    // Phương thức trừu tượng (không có thân hàm)
    // Chỉ quy định: "Mọi Hình học đều PHẢI có cách tính diện tích"
    public abstract double tinhDienTich(); 
    
    // Abstract class vẫn có thể có hàm bình thường
    public void hienThiThongTin() {
        System.out.println("Đây là một hình học.");
    }
}

public class HinhVuong extends HinhHoc {
    private double canh;

    public HinhVuong(double canh) {
        this.canh = canh;
    }

    // Bắt buộc phải hiện thực hóa (implement) cách tính diện tích riêng của nó
    @Override
    public double tinhDienTich() {
        return canh * canh;
    }
}

public class HinhTron extends HinhHoc {
    private double banKinh;

    public HinhTron(double banKinh) {
        this.banKinh = banKinh;
    }

    // Hình tròn lại có công thức tính diện tích khác
    @Override
    public double tinhDienTich() {
        return Math.PI * banKinh * banKinh;
    }
}
```
Lúc này, dù hệ thống có 100 loại hình khác nhau, bạn chỉ cần gọi `hinh.tinhDienTich()` và yên tâm rằng mỗi hình đã tự giấu đi công thức tính toán phức tạp (như pi, căn bậc 2...) bên trong nó.

---

## 🎯 Tóm Tắt Bí Kíp Cốt Lõi:
1. 💊 **Đóng gói (Encapsulation):** Giấu dữ liệu nhạy cảm (`private`), giao tiếp thông qua các cổng kiểm soát (`public getter/setter`). Giúp **BẢO VỆ**.
2. 🧬 **Kế thừa (Inheritance):** Lớp con thừa hưởng và mở rộng từ lớp cha (`extends`). Giúp **TÁI SỬ DỤNG CODE**.
3. 🎭 **Đa hình (Polymorphism):** Cùng một tên hành động, nhưng đối tượng khác nhau sẽ phản hồi khác nhau (`overriding` / `upcasting`). Giúp **LINH HOẠT, DỄ MỞ RỘNG**.
4. 🌫️ **Trừu tượng (Abstraction):** Chỉ đưa ra bề nổi "Làm được cái gì" và che giấu phần chìm "Làm như thế nào" (`abstract` / `interface`). Giúp **GIẢM PHỨC TẠP**.
