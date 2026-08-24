# 📘 Bài 3: Các loại Toán tử (Operators) và Cấu trúc rẽ nhánh

Trong lập trình Java, toán tử (Operator) là những ký hiệu đặc biệt dùng để thực hiện các phép toán trên các biến và giá trị. 

---

## 📑 Mục Lục
- [1️⃣ Toán tử số học (Arithmetic)](#1️⃣-toán-tử-số-học-arithmetic)
- [2️⃣ Toán tử quan hệ (Relational)](#2️⃣-toán-tử-quan-hệ-relational)
- [3️⃣ Toán tử logic (Logical)](#3️⃣-toán-tử-logic-logical)
- [4️⃣ Toán tử gán (Assignment)](#4️⃣-toán-tử-gán-assignment)
- [5️⃣ Toán tử 3 ngôi (Ternary Operator)](#5️⃣-toán-tử-3-ngôi-ternary-operator)
- [6️⃣ Toán tử Bitwise (Thao tác bit)](#6️⃣-toán-tử-bitwise-thao-tác-bit)
- [7️⃣ Toán tử instanceof](#7️⃣-toán-tử-instanceof)
- [8️⃣ Cấu trúc rẽ nhánh (if-else & switch-case)](#8️⃣-cấu-trúc-rẽ-nhánh-if-else--switch-case)
- [📝 KHO BÀI TẬP THỰC HÀNH](#-kho-bài-tập-thực-hành)
  - [I. Bài Tập Cơ Bản](#i-bài-tập-cơ-bản)
  - [II. Thử thách Master](#ii-thử-thách-master)
  - [III. Bài Tập Cuối Tuần (Tổng Hợp)](#iii-bài-tập-cuối-tuần-tổng-hợp)

---

## 1️⃣ Toán tử số học (Arithmetic)
Dùng để thực hiện các phép toán cơ bản như trong toán học.

*   `+` (Cộng): Tính tổng 2 số.
*   `-` (Trừ): Tính hiệu 2 số.
*   `*` (Nhân): Tính tích 2 số.
*   `/` (Chia): Tính thương 2 số. 
*   `%` (Chia lấy dư): Trả về phần dư của phép chia. Rất hay dùng để kiểm tra tính chẵn lẻ.

> **⚠️ CẢNH BÁO (Phép chia số nguyên):**
> Nếu cả 2 toán hạng là số nguyên (`int`), kết quả sẽ là số nguyên (bỏ phần thập phân, không làm tròn). Ví dụ `5 / 2 = 2`. Muốn ra `2.5` thì phải ép kiểu: `5.0 / 2`.

```java
int a = 10, b = 3;
System.out.println("a / b = " + (a / b)); // 3 (phần nguyên)
System.out.println("a % b = " + (a % b)); // 1 (phần dư của 10 chia 3)
```

> **💡 TIP (Phân biệt `i++` và `++i`):**
> *   **`i++` (Hậu tố):** Lấy giá trị hiện tại đi dùng trước, tính toán xong rồi mới tăng bản thân nó lên 1.
> *   **`++i` (Tiền tố):** Tăng bản thân nó lên 1 trước, sau đó mới mang đi sử dụng.

## 2️⃣ Toán tử quan hệ (Relational)
Dùng để so sánh 2 giá trị. Kết quả của phép so sánh **luôn luôn là `boolean`** (`true` hoặc `false`).

*   `==` (Bằng): Kiểm tra 2 giá trị có bằng nhau không.
*   `!=` (Khác): Kiểm tra 2 giá trị có khác nhau không.
*   `>`, `<`, `>=`, `<=`

```java
int myAge = 20;
int limitAge = 18;
System.out.println("Đủ tuổi? " + (myAge >= limitAge)); // true
```

## 3️⃣ Toán tử logic (Logical)
Dùng để kết hợp nhiều điều kiện logic lại với nhau.

*   **`&&` (AND - VÀ):** Chỉ trả về `true` khi **TẤT CẢ** các điều kiện đều đúng.
*   **`||` (OR - HOẶC):** Chỉ cần **MỘT TRONG CÁC** điều kiện đúng thì sẽ trả về `true`.
*   **`!` (NOT - PHỦ ĐỊNH):** Đảo ngược `true` thành `false` và ngược lại.

> **📝 LƯU Ý (Cơ chế Short-circuit - Đoản mạch):**
> * Với `&&`: Nếu điều kiện đầu tiên sai (`false`), Java sẽ bỏ qua điều kiện sau vì kết quả chắc chắn là `false`.
> * Với `||`: Nếu điều kiện đầu tiên đúng (`true`), Java sẽ bỏ qua điều kiện sau vì kết quả chắc chắn là `true`.

## 4️⃣ Toán tử gán (Assignment)
Dùng để gán hoặc cập nhật giá trị cho biến.
*   `=` : Lấy giá trị bên phải đổ vào biến bên trái.
*   `+=` : Cộng thêm rồi gán (`x += 3` giống như `x = x + 3`).
*   `-=` , `*=` , `/=` , `%=`

## 5️⃣ Toán tử 3 ngôi (Ternary Operator)
Cách viết rút gọn rất hay của `if-else`.
*   **Cú pháp:** `(Điều_kiện) ? [Giá_trị_nếu_Đúng] : [Giá_trị_nếu_Sai];`

```java
int score = 7;
String result = (score >= 5) ? "Đậu" : "Rớt";
System.out.println("Kết quả thi: " + result); // Đậu
```

## 6️⃣ Toán tử Bitwise (Thao tác bit)
Dùng để thao tác trực tiếp trên các bit (số 0 và 1) của dữ liệu. Rất hữu ích khi tối ưu hoá hiệu năng cực hạn hoặc làm việc với phần cứng.
*   `&` (AND bit), `|` (OR bit), `^` (XOR bit), `~` (NOT bit).
*   `<<` (Dịch trái): Giống như nhân cho $2^n$.
*   `>>` (Dịch phải): Giống như chia cho $2^n$.

## 7️⃣ Toán tử instanceof
Dùng để kiểm tra xem một đối tượng có phải là con cháu hay thể hiện của một Class cụ thể nào đó hay không. (Dùng nhiều trong OOP).
```java
String name = "Khoi Nguyen";
boolean isString = name instanceof String; // true
```

## 8️⃣ Cấu trúc rẽ nhánh (if-else & switch-case)

### 🔀 Cấu trúc `if-else`
Dùng khi bạn có các khoảng điều kiện phức tạp (lớn hơn, nhỏ hơn, kết hợp AND/OR).

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

### 🎛️ Cấu trúc `switch-case`
Dùng khi bạn muốn kiểm tra giá trị của biến với các giá trị **cụ thể**, **rời rạc**. Gọn gàng hơn `if-else`. Lệnh `break` ngăn chặn hiện tượng chạy tuột xuống các case bên dưới (fall-through).

---
---

## 📝 KHO BÀI TẬP THỰC HÀNH

### I. Bài Tập Cơ Bản

1. **Bài 1 (Tính năm nhuận):**
   Năm nhuận là năm thoả mãn: (Chia hết cho 4 **VÀ** không chia hết cho 100) **HOẶC** (chia hết cho 400). Viết code nhập vào 1 năm và dùng `if-else` kiểm tra.
2. **Bài 2 (Hệ thống phân loại sinh viên):**
   Sinh viên nhận học bổng khi đạt đủ: `GPA >= 8.0` **VÀ** `điểm rèn luyện >= 80` **VÀ** (`Không vi phạm nội quy` **HOẶC** `Có giấy khen khoa`). Viết 1 câu lệnh `if` duy nhất chứa tất cả các điều kiện này.
3. **Bài 3 (Đố vui tiền tố/hậu tố):**
   Tính nhẩm: `int a = 5; int b = 8; int c = ++a + b-- - a++ + --b;`. Giải thích chi tiết.
4. **Bài 4 (Tính chỉ số BMI):**
   Nhập `weight` (kg) và `height` (m). Tính `BMI = weight / (height * height)`. 
   Phân loại: `<18.5` (Thiếu cân), `18.5 - 25.0` (Bình thường), `25.0 - 30.0` (Thừa cân), `>=30.0` (Béo phì).

### 🔥 II. Thử thách Master

5. **Bài 5 (Tính tiền cước xe công nghệ):**
   - Giá cước cơ bản: 15.000đ/km.
   - Giờ cao điểm (7h-9h **HOẶC** 17h-19h): Cộng thêm 5.000đ mỗi km.
   - Giờ đêm (22h đêm - 5h sáng): Phụ phí cố định 20.000đ.
   Nhập số km và giờ đi, tính tổng tiền. (Gợi ý: chú ý logic khung giờ đêm).
6. **Bài 6 (Xét duyệt vay vốn ngân hàng):**
   Tuổi từ 18-60 **VÀ** (Thu nhập >= 15 triệu **HOẶC** Có tài sản thế chấp). In ra kết quả duyệt. Cố gắng sử dụng **Toán tử 3 ngôi** thay vì `if-else`.

### 🏆 III. Bài Tập Cuối Tuần (Tổng Hợp)

7. **Bài 7 (Mini Game - Rock Paper Scissors):**
   Quy ước `1: Búa, 2: Bao, 3: Kéo`. Máy tính random 1 số, người chơi nhập 1 số. Viết cấu trúc `if-else` lồng nhau (hoặc `switch-case`) để phân định thắng thua.
8. **Bài 8 (Máy tính tiền Tạp hoá):**
   Khách mua 3 món hàng. Nếu tổng bill > 500k, giảm 10%. Nếu khách có thẻ thành viên (VIP), giảm thêm 5% trên số tiền ĐÃ GIẢM. Hãy tính toán bằng các toán tử gán rút gọn (`*=`, `-=`).