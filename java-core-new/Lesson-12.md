# 🚀 Bài 12: Đóng Gói, Kế Thừa, Đa Hình và Bài Tập Thực Hành

Trong bài học này, chúng ta sẽ đi sâu vào chi tiết 3 tính chất cực kỳ quan trọng của Lập trình Hướng đối tượng (OOP): **Đóng gói, Kế thừa và Đa hình** (Tính Trừu tượng sẽ được học ở bài sau). 

> [!NOTE]
> Đi kèm với mỗi tính chất là mã nguồn minh họa (demo) cụ thể. Cuối bài sẽ có phần bài tập lý thuyết (chuẩn bị phỏng vấn) và thực hành để bạn củng cố kiến thức.

---

## 1. Lý Thuyết Chi Tiết & Code Demo (Phần 1)

### 1.1 Tính Đóng Gói (Encapsulation) 💊

> [!IMPORTANT]
> **Đóng gói (Encapsulation)** là cơ chế **che giấu thông tin** (trạng thái, dữ liệu/thuộc tính) bên trong của một đối tượng. Bên ngoài không thể trực tiếp xem hay sửa các thuộc tính này một cách tùy tiện.
> 
> *Tưởng tượng:* Giống như một viên thuốc nhộng, các thành phần hóa học bên trong được che giấu và bảo vệ bởi lớp vỏ.

> [!TIP]
> **Mục đích của Đóng gói:**
> - **Bảo vệ tính toàn vẹn của dữ liệu:** Ngăn chặn việc gán dữ liệu không hợp lệ (ví dụ: tuổi bị gán số âm).
> - **Kiểm soát quyền truy cập:** Lớp có thể cấp quyền chỉ đọc (read-only) hoặc chỉ ghi (write-only) bằng cách cung cấp `getter` hoặc `setter`.
> - **Dễ bảo trì:** Bạn có thể thay đổi logic bên trong `setter/getter` mà không làm hỏng code của các hệ thống đang sử dụng lớp của bạn.

**💻 Demo Đóng gói:**
Ví dụ quản lý tài khoản ngân hàng. Thuộc tính `soDu` (số dư) phải được bảo vệ nghiêm ngặt.

```java
public class TaiKhoanNganHang {
    // Thuộc tính bị "đóng gói", không thể truy cập trực tiếp từ bên ngoài
    private double soDu; 
    private String chuTaiKhoan;

    public TaiKhoanNganHang(String chuTaiKhoan, double soDuBanDau) {
        this.chuTaiKhoan = chuTaiKhoan;
        this.soDu = soDuBanDau;
    }

    // Getter cho phép XEM số dư (read-only)
    public double getSoDu() {
        return soDu;
    }

    // Setter bị giới hạn, thay vào đó cung cấp phương thức nạp/rút tiền an toàn
    public void napTien(double soTien) {
        if (soTien > 0) {
            soDu += soTien; // Dữ liệu chỉ được thay đổi khi thỏa mãn điều kiện
            System.out.println("Nạp thành công: " + soTien);
        } else {
            System.out.println("Số tiền nạp không hợp lệ!");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        TaiKhoanNganHang tk = new TaiKhoanNganHang("Khoi", 50000);
        // tk.soDu = -100000; // LỖI COMPILER: Không thể truy cập trực tiếp biến private
        
        tk.napTien(20000); // Hợp lệ
        System.out.println("Số dư hiện tại: " + tk.getSoDu());
    }
}
```

---

### 1.2 Tính Kế Thừa (Inheritance) 🧬

> [!IMPORTANT]
> **Kế thừa (Inheritance)** cho phép một lớp mới (Lớp con - Subclass) được thừa hưởng các thuộc tính và phương thức từ một lớp đã tồn tại (Lớp cha - Superclass).
> 
> *Mối quan hệ:* Thể hiện mối quan hệ **IS-A** (Là một). Ví dụ: Chó *là một* Động vật.

> [!NOTE]
> **Đặc điểm trong Java:**
> - Sử dụng từ khóa `extends`.
> - Java chỉ hỗ trợ **đơn kế thừa** đối với Class (Mỗi class chỉ có đúng 1 class cha).
> - Lớp con **KHÔNG** kế thừa các thành phần `private` của lớp cha. Nó chỉ kế thừa các thành phần `public`, `protected`, và `default` (nếu cùng package).
> - Từ khóa `super` được dùng để gọi hàm khởi tạo (Constructor) hoặc các phương thức/thuộc tính của lớp cha.

**💻 Demo Kế thừa:**

```java
// Lớp cha (Superclass)
public class DongVat {
    protected String ten;
    
    public DongVat(String ten) {
        this.ten = ten;
    }
    
    public void an() {
        System.out.println(ten + " đang ăn...");
    }
}

// Lớp con (Subclass) kế thừa lớp cha
public class Cho extends DongVat {
    private String giongCho; // Thuộc tính mở rộng riêng
    
    public Cho(String ten, String giongCho) {
        super(ten); // Gọi Constructor của lớp cha (DongVat) để khởi tạo 'ten'
        this.giongCho = giongCho;
    }
    
    // Phương thức mở rộng riêng của lớp con
    public void sua() {
        System.out.println(ten + " sủa: Gâu gâu!");
    }
}

public class Main {
    public static void main(String[] args) {
        Cho c = new Cho("Cậu Vàng", "Shiba");
        
        c.an();  // Sử dụng lại phương thức từ lớp cha
        c.sua(); // Sử dụng phương thức của chính nó
    }
}
```

---

### 1.3 Tính Đa Hình (Polymorphism) 🎭

> [!IMPORTANT]
> **Đa hình (Polymorphism)** là hiện tượng các đối tượng khác nhau có thể thực hiện cùng một hành động nhưng theo những cách khác nhau. Đa hình giúp code linh hoạt và vô cùng dễ mở rộng.

#### A. Đa hình lúc biên dịch (Compile-time Polymorphism) - Overloading
> [!NOTE]
> **Nạp chồng phương thức (Overloading):** Xảy ra khi trong cùng một class, chúng ta có nhiều phương thức **cùng tên** nhưng **khác tham số** (khác số lượng hoặc khác kiểu dữ liệu của tham số).

**💻 Demo Overloading:**

```java
public class TinhToan {
    // Hàm cộng 2 số nguyên
    public int cong(int a, int b) {
        return a + b;
    }
    
    // Nạp chồng: Hàm cộng 3 số nguyên (Khác số lượng tham số)
    public int cong(int a, int b, int c) {
        return a + b + c;
    }
    
    // Nạp chồng: Hàm cộng 2 số thực (Khác kiểu dữ liệu)
    public double cong(double a, double b) {
        return a + b;
    }
}

public class Main {
    public static void main(String[] args) {
        TinhToan t = new TinhToan();
        System.out.println(t.cong(5, 10));         // Gọi hàm 1
        System.out.println(t.cong(5, 10, 15));     // Gọi hàm 2
        System.out.println(t.cong(5.5, 2.0));      // Gọi hàm 3
    }
}
```

#### B. Đa hình lúc chạy (Run-time Polymorphism) - Overriding & Upcasting
> [!NOTE]
> **Ghi đè phương thức (Overriding) & Upcasting:** Xảy ra khi lớp con định nghĩa lại (ghi đè) phương thức của lớp cha.
> 
> *Quy tắc vàng:* Khi gọi một phương thức thông qua biến tham chiếu kiểu Cha (Upcasting), Java sẽ dựa vào **bản chất đối tượng thực sự** được cấp phát bộ nhớ lúc chương trình chạy (`new` đối tượng gì) để gọi phương thức của lớp đó.

**💻 Demo Overriding & Upcasting:**

```java
class NhanVien {
    public void tinhLuong() {
        System.out.println("Lương nhân viên cơ bản: 5.000.000");
    }
}

class GiamDoc extends NhanVien {
    @Override // Ghi đè phương thức của lớp cha
    public void tinhLuong() {
        System.out.println("Lương giám đốc: 50.000.000");
    }
}

class TruongPhong extends NhanVien {
    @Override // Ghi đè phương thức của lớp cha
    public void tinhLuong() {
        System.out.println("Lương trưởng phòng: 20.000.000");
    }
}

public class Main {
    public static void main(String[] args) {
        // Upcasting: Kiểu khai báo là NhanVien (Cha) 
        // Nhưng đối tượng thực tế sinh ra là GiamDoc, TruongPhong (Con)
        NhanVien nv1 = new GiamDoc();
        NhanVien nv2 = new TruongPhong();
        NhanVien nv3 = new NhanVien();
        
        // Cùng một lời gọi hàm tinhLuong(), nhưng hành vi trả về khác nhau
        // Đây chính là ĐA HÌNH LÚC CHẠY!
        nv1.tinhLuong(); // In ra: Lương giám đốc: 50.000.000
        nv2.tinhLuong(); // In ra: Lương trưởng phòng: 20.000.000
        nv3.tinhLuong(); // In ra: Lương nhân viên cơ bản: 5.000.000
    }
}
```

---

## 2. 📝 Bài Tập Lý Thuyết (Câu Hỏi Phỏng Vấn - Interview Questions)

> [!TIP]
> Dưới đây là các câu hỏi phỏng vấn thường gặp nhất về 3 tính chất Đóng gói, Kế thừa và Đa hình. Hãy tự trả lời để kiểm tra kiến thức thực tế của mình!

### Về tính Đóng gói (Encapsulation):

1. Đóng gói (Encapsulation) là gì? Làm thế nào để đạt được tính đóng gói trong Java?
2. Sự khác biệt giữa các Access Modifier `private`, `protected`, `default` (không ghi gì) và `public` là gì?
3. Nếu khai báo tất cả thuộc tính là `private` và sau đó tự động sinh toàn bộ hàm `getter` và `setter` `public` cho chúng, thì tính Đóng gói có bị mất đi ý nghĩa không? Tại sao?

### Về tính Kế thừa (Inheritance):

1. Tại sao Java không hỗ trợ đa kế thừa (Multiple Inheritance) đối với Class? (Gợi ý: Vấn đề Diamond Problem).
2. Khi một đối tượng của lớp con được khởi tạo (bằng từ khóa `new`), Constructor của lớp cha có được tự động gọi hay không? Thứ tự gọi như thế nào?
3. Từ khóa `super` dùng để làm gì? Nêu sự khác biệt giữa `super()` và `this()`.

### Về tính Đa hình (Polymorphism):

1. Phân biệt rõ sự khác nhau giữa **Method Overloading** (Nạp chồng) và **Method Overriding** (Ghi đè) trong Java.
2. Có thể Override một phương thức `static` hoặc `private` của lớp cha được không? Tại sao?
3. Khái niệm **Upcasting** và **Downcasting** trong Java là gì? Quá trình Downcasting có an toàn không và làm sao để tránh lỗi `ClassCastException`?
4. Tại sao đa hình lúc chạy (Run-time Polymorphism) bắt buộc phải đi kèm với Kế thừa (Inheritance) và Ghi đè (Overriding)?

---

## 3. 🛠️ Bài Tập Thực Hành

### Bài tập 1: Quản Lý Thư Viện (Thực hành Đóng Gói & Kế Thừa)

> [!WARNING]
> **Yêu cầu:** Một thư viện cần quản lý các tài liệu. Hãy tạo hệ thống các lớp như sau:

1. **Tạo lớp cha `TaiLieu`:**
   - Các thuộc tính `private`: `maTaiLieu` (String), `tenNhaXuatBan` (String), `soBanPhatHanh` (int).
   - Tạo Constructor đầy đủ tham số, và các phương thức `getter/setter` hợp lệ.
   - Tạo phương thức `hienThiThongTin()` in ra các thông tin của tài liệu.
2. **Tạo lớp con `Sach` kế thừa từ `TaiLieu`:**
   - Bổ sung các thuộc tính `private`: `tenTacGia` (String), `soTrang` (int).
   - Tạo Constructor (nhớ sử dụng `super` để khởi tạo dữ liệu cho lớp cha).
   - Ghi đè (`@Override`) phương thức `hienThiThongTin()` để in thêm tên tác giả và số trang (Sử dụng `super.hienThiThongTin()` để in thông tin từ cha trước).
3. **Trong hàm `main`:** 
   - Khởi tạo 1 đối tượng `Sach`.
   - Gọi phương thức `hienThiThongTin()` để kiểm tra kết quả.

---

### Bài tập 2: Quản Lý Nhân Viên (Thực hành Đa Hình)

> [!WARNING]
> **Yêu cầu:** Công ty có nhiều loại nhân viên khác nhau. Hãy viết chương trình tính lương thể hiện tính Đa hình:

1. **Tạo lớp cha `NhanVien`:**
   - Thuộc tính `protected`: `ten` (String), `luongCoBan` (double).
   - Constructor khởi tạo 2 thuộc tính.
   - Phương thức `tinhLuong()`: Trả về `luongCoBan` (kiểu double).
   - Phương thức `hienThiThongTin()`: In ra tên và mức lương nhận được (bằng cách gọi `tinhLuong()`).
2. **Tạo lớp con `NhanVienFullTime` kế thừa `NhanVien`:**
   - Thuộc tính bổ sung: `thuong` (double).
   - Ghi đè phương thức `tinhLuong()`: Trả về `luongCoBan + thuong`.
3. **Tạo lớp con `NhanVienPartTime` kế thừa `NhanVien`:**
   - Thuộc tính bổ sung: `soGioLam` (int).
   - Ghi đè phương thức `tinhLuong()`: Trả về `luongCoBan + (soGioLam * 100000)`.
4. **Viết class `Main` chứa hàm `main`:**
   - Tạo một mảng (hoặc ArrayList) chứa danh sách các `NhanVien` (khai báo kiểu dữ liệu của mảng là `NhanVien` nhưng thêm vào các đối tượng `NhanVienFullTime` và `NhanVienPartTime` - *Đây là khái niệm Upcasting*).
   - Dùng vòng lặp `for` duyệt qua mảng đó và gọi phương thức `hienThiThongTin()` cho từng nhân viên.
   - *Gợi ý quan sát:* Cùng một câu lệnh gọi `hienThiThongTin()` (gọi ngầm tới `tinhLuong()`), nhưng mỗi đối tượng con sẽ tự động tính toán lương dựa trên công thức riêng của nó. Đây chính là **Đa hình lúc chạy (Run-time Polymorphism)**.
