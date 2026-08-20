# Bài 3: Các loại Toán tử (Operators) và Cấu trúc rẽ nhánh

Trong lập trình Java, toán tử (Operator) là những ký hiệu đặc biệt dùng để thực hiện các phép toán trên các biến và giá trị. Dưới đây là chi tiết các loại toán tử kèm ví dụ dễ hiểu.

---

## 1. Arithmetic Operators (Toán tử số học)
Dùng để thực hiện các phép toán cơ bản như trong toán học.

*   `+` (Cộng): Tính tổng 2 số.
*   `-` (Trừ): Tính hiệu 2 số.
*   `*` (Nhân): Tính tích 2 số.
*   `/` (Chia): Tính thương 2 số. **Lưu ý:** Nếu cả 2 toán hạng là số nguyên (int), kết quả sẽ là số nguyên (bỏ phần thập phân, không làm tròn). Ví dụ `5 / 2 = 2`. Muốn ra `2.5` thì phải ép kiểu: `5.0 / 2`.
*   `%` (Chia lấy dư): Trả về phần dư của phép chia. Rất hay dùng để kiểm tra tính chẵn lẻ (chia dư cho 2).

**Ví dụ:**
```java
int a = 10, b = 3;
System.out.println("a + b = " + (a + b)); // 13
System.out.println("a - b = " + (a - b)); // 7
System.out.println("a * b = " + (a * b)); // 30
System.out.println("a / b = " + (a / b)); // 3 (phần nguyên)
System.out.println("a % b = " + (a % b)); // 1 (phần dư của 10 chia 3)
```

### Phân biệt `i++` (Post-increment) và `++i` (Pre-increment)
*   **`i++` (Hậu tố):** "Lấy giá trị hiện tại đi dùng trước, tính toán xong rồi mới tăng bản thân nó lên 1".
    ```java
    int i = 5;
    int a = i++; 
    System.out.println(a); // In ra 5. Vì i đã gán cho a xong mới tăng
    System.out.println(i); // In ra 6
    ```
*   **`++i` (Tiền tố):** "Tăng bản thân nó lên 1 trước, sau đó mới mang đi sử dụng".
    ```java
    int x = 5;
    int y = ++x; 
    System.out.println(y); // In ra 6. Vì x đã tăng lên 6 trước khi gán cho y
    System.out.println(x); // In ra 6
    ```

---

## 2. Relational Operators (Toán tử quan hệ)
Dùng để so sánh 2 giá trị. Kết quả của phép so sánh **luôn luôn là `boolean`** (`true` hoặc `false`). Thường dùng làm điều kiện cho cấu trúc `if`.

*   `==` (Bằng): Kiểm tra 2 giá trị có bằng nhau không.
*   `!=` (Khác): Kiểm tra 2 giá trị có khác nhau không.
*   `>` (Lớn hơn), `<` (Nhỏ hơn)
*   `>=` (Lớn hơn hoặc bằng), `<=` (Nhỏ hơn hoặc bằng)

**Ví dụ:**
```java
int myAge = 20;
int limitAge = 18;

System.out.println("Tôi đủ 18 tuổi? " + (myAge >= limitAge)); // true
System.out.println("Tôi đúng bằng 18 tuổi? " + (myAge == limitAge)); // false
System.out.println("Tôi khác 18 tuổi? " + (myAge != limitAge)); // true
```

---

## 3. Logical Operators (Toán tử logic)
Dùng để kết hợp nhiều điều kiện logic lại với nhau.

*   **`&&` (Logical AND - VÀ):** Chỉ trả về `true` khi **TẤT CẢ** các điều kiện đều đúng.
    *   *Short-circuit:* Nếu điều kiện 1 sai (`false`), máy tính sẽ chốt luôn kết quả là `false` mà không thèm kiểm tra điều kiện 2.
*   **`||` (Logical OR - HOẶC):** Chỉ cần **MỘT TRONG CÁC** điều kiện đúng thì sẽ trả về `true`.
    *   *Short-circuit:* Nếu điều kiện 1 đúng (`true`), máy tính sẽ chốt kết quả là `true` luôn mà không cần kiểm tra điều kiện 2.
*   **`!` (Logical NOT - PHỦ ĐỊNH):** Đảo ngược `true` thành `false` và ngược lại.

**Ví dụ:**
```java
double gpa = 8.5;
boolean hasGoodConduct = true;

// Để được học bổng cần GPA > 8.0 VÀ Hạnh kiểm tốt
boolean getScholarship = (gpa > 8.0) && hasGoodConduct; 
System.out.println("Đạt học bổng: " + getScholarship); // true

boolean isSunday = false;
boolean isHoliday = true;

// Được đi chơi nếu nay là Chủ nhật HOẶC là Ngày lễ
boolean canPlay = isSunday || isHoliday;
System.out.println("Được đi chơi: " + canPlay); // true

// Bị kẹt xe (false) -> Phủ định lại là Đường thông thoáng (true)
boolean isTrafficJam = false;
System.out.println("Đường thông thoáng: " + !isTrafficJam); // true
```

---

## 4. Assignment Operators (Toán tử gán)
Dùng để gán hoặc cập nhật giá trị cho biến.

*   `=` : Lấy giá trị bên phải đổ vào biến bên trái.
*   Toán tử gán kết hợp số học (giúp viết code ngắn gọn hơn):
    *   `+=` : Cộng thêm rồi gán. `x += 3` giống như `x = x + 3`
    *   `-=` : Trừ đi rồi gán. `x -= 2` giống như `x = x - 2`
    *   `*=` : Nhân thêm rồi gán. `x *= 4` giống như `x = x * 4`
    *   `/=` : Chia rồi gán.
    *   `%=` : Chia lấy dư rồi gán.

**Ví dụ:**
```java
int money = 1000; // Đang có 1000
money += 500;     // Mẹ cho thêm 500 -> money = 1500
money -= 200;     // Ăn phở hết 200 -> money = 1300
money *= 2;       // Chơi chứng khoán x2 tài khoản -> money = 2600
System.out.println("Tiền còn lại: " + money);
```

---

## 5. Ternary Operator (Toán tử điều kiện / Toán tử 3 ngôi)
Được gọi là 3 ngôi vì nó có 3 thành phần. Đây là cách viết rút gọn rất hay của `if-else`.

*   **Cú pháp:** `(Điều_kiện) ? [Giá_trị_nếu_Đúng] : [Giá_trị_nếu_Sai];`

**Ví dụ:**
```java
int score = 7;
// Nếu score >= 5 thì gán "Đậu", ngược lại gán "Rớt"
String result = (score >= 5) ? "Đậu" : "Rớt";
System.out.println("Kết quả thi: " + result); // Đậu

// Tìm số lớn nhất giữa a và b
int a = 10, b = 25;
int max = (a > b) ? a : b; 
System.out.println("Số lớn nhất là: " + max); // 25
```

---

## 6. Bitwise Operators (Toán tử bitwise)
Dùng để thao tác trực tiếp trên các bit (số 0 và 1) của dữ liệu. Rất hữu ích khi tối ưu hoá hiệu năng cực hạn hoặc làm việc với phần cứng.

*   `&` (AND bit): Bit kết quả là 1 nếu cả 2 bit cùng là 1.
*   `|` (OR bit): Bit kết quả là 1 nếu 1 trong 2 bit là 1.
*   `^` (XOR bit): Bit kết quả là 1 nếu 2 bit khác nhau.
*   `~` (NOT bit): Đảo bit, 0 thành 1 và ngược lại.
*   `<<` (Dịch trái): Dịch toàn bộ bit sang trái n vị trí (Giống như x * 2^n).
*   `>>` (Dịch phải): Dịch toàn bộ bit sang phải n vị trí (Giống như x / 2^n).

**Ví dụ:**
```java
int p = 5;  // Nhị phân là 0101
int q = 3;  // Nhị phân là 0011

System.out.println(p & q); // 0001 -> In ra 1
System.out.println(p | q); // 0111 -> In ra 7
System.out.println(p ^ q); // 0110 -> In ra 6
System.out.println(p << 1); // 1010 -> In ra 10 (tương đương 5 * 2^1)
```

---

## 7. Toán tử `instanceof`
Dùng để kiểm tra xem một đối tượng (Object) có phải là con cháu hay thể hiện (instance) của một Class cụ thể nào đó hay không. Rất hay dùng trong tính Đa hình (Polymorphism) của OOP.

**Ví dụ:**
```java
String name = "Khoi Nguyen";

// Kiểm tra xem biến name có phải được tạo ra từ class String không?
boolean isString = name instanceof String; 
System.out.println("name là chuỗi? " + isString); // true

// Object là class cha của mọi class trong Java
boolean isObject = name instanceof Object;
System.out.println("name là Object? " + isObject); // true
```

---

## 8. Lý thuyết `if-else` và `switch-case`

### Cấu trúc `if-else`
Dùng khi bạn có các khoảng điều kiện phức tạp.
```java
int temp = 28;

if (temp > 35) {
    System.out.println("Trời rất nóng");
} else if (temp >= 25 && temp <= 35) {
    System.out.println("Trời mát mẻ");
} else {
    System.out.println("Trời lạnh");
}
```

### Cấu trúc `switch-case`
Dùng khi bạn muốn kiểm tra giá trị của biến với các giá trị **cụ thể**, **rời rạc** (menu, thứ trong tuần, tháng trong năm). Gọn gàng hơn `if-else`.
```java
int day = 3;

switch (day) {
    case 2:
        System.out.println("Thứ Hai");
        break; // Lệnh break ngăn chặn code chạy tuột xuống các case bên dưới
    case 3:
        System.out.println("Thứ Ba");
        break;
    default:
        System.out.println("Không xác định");
}
```

---

## 9. Bài Tập Thực Hành

**Bài 1: Xét duyệt vay vốn ngân hàng**
**Yêu cầu:** Viết chương trình nhập vào tuổi, mức thu nhập hàng tháng (triệu VNĐ) và cờ xác nhận có tài sản thế chấp hay không (true/false) của một người. 
Ngân hàng sẽ duyệt cho vay nếu đáp ứng **tất cả** các điều kiện sau:
*   Độ tuổi từ 18 đến 60 (bao gồm cả 18 và 60).
*   **VÀ** thoả mãn **một trong hai** điều kiện về tài chính: Mức thu nhập >= 15 triệu, **HOẶC** có tài sản thế chấp.
In ra "Được duyệt vay" hoặc "Không được duyệt".

**Bài 2: Tính năm nhuận**
**Yêu cầu:** Năm nhuận là năm thoả mãn điều kiện sau:
*   Năm đó phải chia hết cho 4 **VÀ** không chia hết cho 100.
*   **HOẶC** năm đó chia hết cho 400.
Viết chương trình nhập vào một năm (ví dụ: 2024, 1900, 2000) và in ra màn hình đó có phải năm nhuận hay không bằng cách dùng `if-else` và gộp điều kiện bằng `&&`, `||`.

**Bài 3: Hệ thống phân loại sinh viên đạt Học Bổng**
**Yêu cầu:** Sinh viên được nhận học bổng khi đạt đủ các tiêu chí:
1.  Điểm trung bình (GPA) >= 8.0.
2.  **VÀ** điểm rèn luyện >= 80.
3.  **VÀ** sinh viên đó KHÔNG BỊ vi phạm nội quy (biến `isViPham` = false) **HOẶC** có giấy khen của khoa (biến `hasGiayKhen` = true).
Hãy tạo các biến tương ứng, gán giá trị và viết một câu lệnh `if` duy nhất chứa tất cả các điều kiện để kiểm tra xem sinh viên có đạt học bổng không.

**Bài 4: Tính tiền cước xe công nghệ có phụ phí ban đêm**
**Yêu cầu:** Nhập vào khoảng cách đi (km), và giờ bắt đầu đi (chỉ lấy giờ từ 0 - 23).
*   Giá cước cơ bản: 15.000 VNĐ / km.
*   Tuy nhiên, nếu khách đi vào **giờ cao điểm** (từ 7h đến 9h) **HOẶC** (từ 17h đến 19h), giá cước mỗi km sẽ tăng thêm 5.000 VNĐ.
*   Ngoài ra, nếu khách đi vào **khung giờ đêm** (từ 22h đêm đến 5h sáng hôm sau), sẽ bị cộng thêm 20.000 VNĐ phụ phí cố định (cộng vào tổng tiền, không phải cộng vào mỗi km).
Hãy viết chương trình tính tổng tiền cước khách phải trả. *(Gợi ý: chú ý cách biểu diễn khung giờ từ 22h đến 5h, nó là giờ >= 22 HOẶC giờ <= 5)*.

**Bài 5: Đố vui ++i và i++**
**Yêu cầu:** Cho đoạn code sau:
```java
int a = 5;
int b = 8;
int c = ++a + b-- - a++ + --b;
```
Hãy tính toán **thủ công ra giấy** (không dùng máy tính/chạy code) xem giá trị cuối cùng của biến `c`, `a`, và `b` là bao nhiêu. Phân tích từng bước thực hiện dựa trên quy tắc tiền tố (thực hiện ngay) và hậu tố (sử dụng xong mới thay đổi).

**Bài 6: Phân loại sức khoẻ dựa trên chỉ số BMI**
**Yêu cầu:** Khai báo và gán cứng (hoặc nhập từ bàn phím) cân nặng `weight` (đơn vị: kg) và chiều cao `height` (đơn vị: mét) của một người. 
1. Sử dụng toán tử số học để tính chỉ số BMI theo công thức: `BMI = weight / (height * height)`.
2. Sử dụng cấu trúc `if-else if` kết hợp với toán tử quan hệ (`>=`, `<`) và logic (`&&`) để phân loại và in ra màn hình:
*   BMI < 18.5: "Thiếu cân"
*   Từ 18.5 đến dưới 25.0: "Bình thường"
*   Từ 25.0 đến dưới 30.0: "Thừa cân"
*   BMI >= 30.0: "Béo phì"