# 📘 Bài 4: Toán tử, Cách Đặt Tên Biến và Các Cấu Trúc Điều Khiển (Switch-case, Loops)

---

## 📑 Mục Lục
- [1️⃣ Cách Đặt Tên Biến Chuẩn & Hằng Số trong Java](#part1)
- [2️⃣ Toán tử Tăng/Giảm phức tạp (++a, a++, --a, a--)](#part2)
- [3️⃣ Cấu trúc rẽ nhánh `switch-case`](#part3)
- [4️⃣ Các Cấu Trúc Vòng Lặp (Loops)](#part4)
- [5️⃣ Các Từ Khóa Điều Khiển Luồng: `break`, `continue`, `return`](#part5)
- [6️⃣ Cách Viết Hàm (Phương Thức) Trong Java](#part6)
- [7️⃣ Phạm Vi Của Biến (Variable Scope)](#part7)
- [📝 KHO BÀI TẬP THỰC HÀNH (62 Bài)](#part8)
- [🏆 VII. Bài Tập Cuối Tuần (Tổng Hợp)](#part9)
- [🎖️ VIII. FINAL BOSS - 3 Bài Tập Lớn (Mini-Projects)](#part10)

---

<a id="part1"></a>
## 1️⃣ Cách Đặt Tên Biến Chuẩn & Hằng Số trong Java

### 🏷️ 1.1 Quy tắc đặt tên biến chuẩn (Naming Convention)
Trong Java, chúng ta sử dụng **`camelCase`** (kiểu con lạc đà) để đặt tên biến và tên phương thức (method).
- Bắt đầu bằng một chữ cái **viết thường**.
- Viết hoa chữ cái đầu tiên của mỗi từ tiếp theo.
- Tên biến nên mang ý nghĩa rõ ràng, tránh đặt tên vô nghĩa như `a`, `b`, `c` (ngoại trừ trong vòng lặp hoặc thuật toán ngắn).

```java
int studentAge = 20;
String firstName = "Nguyen";
double accountBalance = 1500.50;
```

### 🔒 1.2 Hằng số (Constants)
Hằng số là những biến mà giá trị của nó **không bao giờ thay đổi** sau khi được khởi tạo. 

> **💡 TIP (Quy tắc):**
> - Dùng từ khóa `final` để khai báo.
> - Đặt tên theo chuẩn **`UPPER_SNAKE_CASE`** (Tất cả viết hoa và phân cách bằng dấu gạch ngang dưới).

```java
final double PI = 3.14159;
final int MAX_USERS_PER_ROOM = 50;
```

---

<a id="part2"></a>
## 2️⃣ Toán tử Tăng/Giảm phức tạp (++a, a++, --a, a--)

Việc giải các bài toán dạng `int c = ++a + b-- - a++ + --b;` đòi hỏi chúng ta phải hiểu rõ sự khác biệt giữa **Tiền tố (Prefix)** và **Hậu tố (Postfix)**.

> **📌 QUAN TRỌNG:**
> * **Tiền tố (`++a` hoặc `--a`)**: Tăng/giảm giá trị của biến **TRƯỚC**, sau đó mới sử dụng giá trị mới để tính toán biểu thức.
> * **Hậu tố (`a++` hoặc `a--`)**: Sử dụng giá trị hiện tại của biến để tính toán biểu thức **TRƯỚC**, sau đó mới tăng/giảm giá trị của biến đó.

**🔍 Ví dụ phân tích chi tiết:**
```java
int a = 5;
int b = 8;
int c = ++a + b-- - a++ + --b;
```

**Cách giải quyết từng bước từ trái sang phải:**
1. `++a`: `a` tăng lên `6` trước. Giá trị đưa vào biểu thức là **6**. *(Lúc này a = 6, b = 8)*
2. `b--`: Sử dụng giá trị hiện tại của `b` là **8** cho biểu thức. Sau đó `b` giảm xuống `7`. *(Lúc này a = 6, b = 7)*
3. `a++`: Sử dụng giá trị hiện tại của `a` là **6** cho biểu thức. Sau đó `a` tăng lên `7`. *(Lúc này a = 7, b = 7)*
4. `--b`: `b` giảm xuống `6` trước. Giá trị đưa vào biểu thức là **6**. *(Lúc này a = 7, b = 6)*

**Ráp lại vào biểu thức:**
`c = 6 + 8 - 6 + 6 = 14`

**✅ Kết quả cuối cùng:** `a = 7`, `b = 6`, `c = 14`.

### 🔀 2.1 Toán tử 3 ngôi (Ternary Operator)
Toán tử 3 ngôi là cách viết tắt cực kỳ ngắn gọn của cấu trúc `if-else`. Rất hay được dùng để gán giá trị cho biến dựa trên một điều kiện.

**Cú pháp:**
`Kiểu_dữ_liệu tên_biến = (Điều_kiện) ? Giá_trị_nếu_đúng : Giá_trị_nếu_sai;`

```java
int diem = 8;
// Thay vì viết if (diem >= 5) kq = "Đậu"; else kq = "Rớt";
String ketQua = (diem >= 5) ? "Đậu" : "Rớt";
System.out.println(ketQua); // In ra: Đậu
```

---

<a id="part3"></a>
## 3️⃣ Cấu trúc rẽ nhánh `switch-case`

`switch-case` được dùng để thay thế chuỗi `if-else if` quá dài khi cần so sánh giá trị của 1 biến với nhiều trường hợp cụ thể bằng nhau.

> **📝 LƯU Ý (Đặc điểm):**
> - Hỗ trợ các kiểu dữ liệu: `byte`, `short`, `char`, `int`, `String` (từ Java 7), `enum`.
> - Từ khóa `break`: Giúp thoát khỏi khối `switch`. Nếu thiếu `break`, chương trình sẽ chạy tuột xuống các `case` bên dưới (hiện tượng *"fall-through"*) cho đến khi gặp `break` hoặc hết khối lệnh.

```java
int dayOfWeek = 3;
switch (dayOfWeek) {
    case 2:
        System.out.println("Thứ Hai");
        break;
    case 3:
        System.out.println("Thứ Ba");
        break;
    case 8:
        System.out.println("Chủ Nhật");
        break;
    default:
        System.out.println("Ngày không hợp lệ");
}
```

---

<a id="part4"></a>
## 4️⃣ Các Cấu Trúc Vòng Lặp (Loops)

Vòng lặp giúp chúng ta thực thi một đoạn code lặp đi lặp lại nhiều lần mà không phải viết lại code đó, giúp tiết kiệm thời gian và công sức. 

### 🔄 4.1 Vòng lặp `for` (Biết trước số lần lặp)
**Khái niệm:** Dùng khi bạn đã **biết chính xác số lần** mình muốn lặp lại một công việc.

**Cú pháp:**
```java
for (Khởi_tạo_biến_đếm; Điều_kiện_lặp; Cập_nhật_biến_đếm) {
    // Khối lệnh thực thi
}
```
**Quy trình hoạt động:**
1. Khởi tạo biến đếm *(chỉ chạy đúng 1 lần đầu tiên)*.
2. Kiểm tra điều kiện *(Nếu `true` -> Chạy lệnh, Nếu `false` -> Dừng vòng lặp)*.
3. Cập nhật biến đếm.
4. Lặp lại bước 2 và 3.

```java
// Ví dụ: In ra 5 lần chữ "Hello"
for (int i = 1; i <= 5; i++) {
    System.out.println("Hello " + i);
}
// Giải thích: i bắt đầu từ 1. Miễn là i <= 5, in ra "Hello". Sau mỗi lần in, i tăng thêm 1 (i++).
```

### 🔁 4.2 Vòng lặp `while` (Chưa biết trước số lần lặp)
**Khái niệm:** Dùng khi bạn **chưa biết sẽ phải lặp bao nhiêu lần**, nhưng bạn biết **điều kiện để dừng lại**. Vòng lặp này sẽ kiểm tra điều kiện **TRƯỚC**, nếu đúng thì mới chạy khối lệnh.

**Cú pháp:**
```java
while (Điều_kiện_lặp) {
    // Khối lệnh thực thi
    // (Bắt buộc phải có đoạn code để làm thay đổi Điều_kiện_lặp, nếu không sẽ bị lặp vô tận)
}
```

```java
// Ví dụ: Đếm số lượng chữ số của một số nguyên
int n = 1234;
int count = 0;
while (n > 0) { // Chừng nào n còn lớn hơn 0
    count++;    // Tăng biến đếm lên 1
    n = n / 10; // Cắt bỏ chữ số cuối cùng của n (1234 -> 123 -> 12 -> 1 -> 0)
}
System.out.println("Số chữ số là: " + count); 
```

### 🔂 4.3 Vòng lặp `do-while` (Chạy trước, kiểm tra sau)
**Khái niệm:** Hoàn toàn giống `while`, nhưng điểm khác biệt mấu chốt là nó kiểm tra điều kiện **SAU** khi đã chạy khối lệnh. 

> **⚠️ CẢNH BÁO:** Khối lệnh bên trong `do-while` **luôn luôn được chạy ít nhất 1 lần** bất chấp điều kiện ban đầu đúng hay sai.

```java
// Ví dụ: Bắt người dùng nhập một số dương. Nếu họ cố tình nhập số âm, bắt nhập lại.
import java.util.Scanner;

Scanner sc = new Scanner(System.in);
int x;
do {
    System.out.print("Nhập một số dương: ");
    x = sc.nextInt(); 
    // Người dùng bắt buộc phải nhập dữ liệu ở đây ít nhất 1 lần
} while (x <= 0); // Kiểm tra lại: nếu họ nhập số âm hoặc bằng 0, lặp lại việc bắt nhập!

System.out.println("Bạn đã nhập số hợp lệ: " + x);
```

### 🔄 4.4 Vòng lặp lồng nhau (Nested Loops)
**Khái niệm:** Bạn hoàn toàn có thể đặt một vòng lặp bên trong một vòng lặp khác. Khi đó, với **mỗi 1 lần** vòng lặp ngoài chạy, vòng lặp bên trong sẽ phải **chạy hết toàn bộ vòng đời** của nó.

```java
// Ví dụ: In ra một ma trận 3x3
for (int i = 1; i <= 3; i++) { // Vòng lặp ngoài (quản lý số Dòng)
    for (int j = 1; j <= 3; j++) { // Vòng lặp trong (quản lý số Cột)
        System.out.print("(" + i + "," + j + ") ");
    }
    System.out.println(); // Xuống dòng sau khi in hết 1 dòng
}
/* Kết quả:
(1,1) (1,2) (1,3) 
(2,1) (2,2) (2,3) 
(3,1) (3,2) (3,3) 
*/
```

---

<a id="part5"></a>
## 5️⃣ Các Từ Khóa Điều Khiển Luồng: `break`, `continue`, `return`

Trong quá trình sử dụng vòng lặp hoặc hàm, bạn sẽ cần các từ khóa này để can thiệp vào luồng chạy của chương trình.

### 🛑 5.1 Từ khóa `break`
**Tác dụng:** Dừng ngay lập tức toàn bộ vòng lặp (hoặc khối `switch-case`) chứa nó. Chương trình sẽ tiếp tục chạy các lệnh nằm sau vòng lặp đó.

```java
for (int i = 1; i <= 10; i++) {
    if (i == 5) {
        break; // Khi i = 5, thoát vòng lặp ngay lập tức
    }
    System.out.print(i + " ");
}
// Kết quả in ra: 1 2 3 4
```

### ⏭️ 5.2 Từ khóa `continue`
**Tác dụng:** Bỏ qua các câu lệnh còn lại trong **lần lặp hiện tại**, và nhảy thẳng sang lần lặp tiếp theo.

```java
for (int i = 1; i <= 5; i++) {
    if (i == 3) {
        continue; // Bỏ qua lượt i = 3, nhảy sang lượt i = 4
    }
    System.out.print(i + " ");
}
// Kết quả in ra: 1 2 4 5 (Không in ra 3)
```

### 🔙 5.3 Từ khóa `return`
**Tác dụng:** Dừng ngay lập tức **toàn bộ hàm/phương thức** (method) hiện tại và trả về giá trị cho nơi đã gọi nó. Nếu dùng trong hàm `main`, nó sẽ kết thúc luôn chương trình.

```java
public void checkAge(int age) {
    if (age < 18) {
        System.out.println("Bạn chưa đủ tuổi.");
        return; // Thoát khỏi hàm ngay lập tức
    }
    System.out.println("Chào mừng bạn đăng nhập!");
}
```

### 🏷️ 5.4 Labeled `break` và Labeled `continue` (Có nhãn)
Khi làm việc với **Vòng lặp lồng nhau**, lệnh `break` hoặc `continue` bình thường chỉ có tác dụng với **vòng lặp gần nó nhất**. Nếu bạn muốn thoát thẳng ra khỏi vòng lặp cha (vòng lặp bên ngoài), bạn phải đặt "nhãn" (Label) cho vòng lặp cha.

```java
outerLoop: // Đặt nhãn cho vòng lặp ngoài
for (int i = 1; i <= 3; i++) {
    for (int j = 1; j <= 3; j++) {
        if (i == 2 && j == 2) {
            break outerLoop; // Thoát hẳn ra khỏi vòng lặp ngoài!
        }
        System.out.println("i=" + i + ", j=" + j);
    }
}
// Khi i=2, j=2, chương trình lập tức văng ra khỏi toàn bộ 2 vòng lặp.
```

---

<a id="part6"></a>
## 6️⃣ Cách Viết Hàm (Phương Thức) Trong Java

Hàm (trong Java gọi là Method) là một khối code dùng để thực hiện một tác vụ cụ thể. Việc dùng hàm giúp tái sử dụng code, làm code gọn gàng và dễ bảo trì hơn.

### 🛠️ Cú pháp cơ bản của một hàm:
```java
[Phạm_vi_truy_cập] [Kiểu_trả_về] tên_hàm(Danh_sách_tham_số) {
    // Các câu lệnh thực thi
    return [Giá_trị_trả_về]; // (Chỉ cần thiết nếu Kiểu_trả_về không phải là void)
}
```

- **Phạm_vi_truy_cập:** Thường dùng `public`, `private`, hoặc `protected` (Quyết định ai có quyền gọi hàm này). Bắt đầu học ta có thể thêm chữ `static` để gọi trực tiếp trong hàm `main`.
- **Kiểu_trả_về:** Hàm tính toán xong sẽ trả ra dữ liệu kiểu gì? (`int`, `String`, `double`...). Nếu hàm chỉ thực hiện hành động (như in ra màn hình) mà không trả về gì, dùng `void`.
- **Danh_sách_tham_số:** Đầu vào của hàm (nguyên liệu để hàm xử lý).

### 🔍 Các ví dụ:

**Ví dụ 1: Hàm KHÔNG có tham số đầu vào và KHÔNG trả về giá trị (dùng `void`)**
```java
public static void sayHello() {
    System.out.println("Xin chào! Chúc một ngày tốt lành.");
}

// Cách gọi hàm: sayHello();
```

**Ví dụ 2: Hàm CÓ tham số đầu vào nhưng KHÔNG trả về giá trị**
```java
public static void printSum(int a, int b) {
    int sum = a + b;
    System.out.println("Tổng là: " + sum);
}

// Cách gọi hàm: printSum(5, 10);
```

**Ví dụ 3: Hàm CÓ tham số đầu vào và CÓ kiểu trả về**
```java
public static int calculateArea(int width, int height) {
    int area = width * height;
    return area; // Bắt buộc phải có lệnh return trả về 1 số nguyên (int)
}

// Cách gọi hàm: 
// int result = calculateArea(4, 5); 
// System.out.println(result); // In ra 20
```

> **💡 TIP:** Luôn đặt tên hàm bằng một Động Từ viết theo chuẩn `camelCase` (ví dụ: `calculateArea`, `getUserName`, `printReport`).

---

<a id="part7"></a>
## 7️⃣ Phạm Vi Của Biến (Variable Scope)

Một quy tắc sống còn trong Java: **Biến được khai báo bên trong cặp ngoặc nhọn `{ }` nào thì chỉ có thể được sử dụng (sống) bên trong cặp ngoặc nhọn đó.**

```java
public static void main(String[] args) {
    int outsideVar = 10;
    
    if (outsideVar > 5) {
        int insideVar = 20; // Khai báo trong block if
        System.out.println(outsideVar); // Hợp lệ
        System.out.println(insideVar);  // Hợp lệ
    }
    
    // System.out.println(insideVar); // LỖI BÁO ĐỎ! insideVar đã "chết" khi ra khỏi block if
    
    for (int i = 0; i < 3; i++) {
        System.out.println(i);
    }
    
    // System.out.println(i); // LỖI BÁO ĐỎ! Biến i chỉ sống bên trong vòng for
}
```

---
---

<a id="part8"></a>
## 📝 KHO BÀI TẬP THỰC HÀNH

### I. Bài Tập Tính Biểu Thức (Toán tử Tăng/Giảm)
*Yêu cầu: Hãy tính nhẩm ra giấy giá trị cuối cùng của tất cả các biến trước khi viết code chạy thử.*

1. **Bài 1:**
   ```java
   int x = 10, y = 5;
   int z = x++ + ++y - --x + y--;
   ```
2. **Bài 2:**
   ```java
   int a = 3, b = 4;
   int c = ++a * b-- + a-- * ++b;
   ```
3. **Bài 3:**
   ```java
   int m = 7, n = 2;
   int p = m-- % ++n + ++m * n--;
   ```
4. **Bài 4:**
   ```java
   int i = 5;
   int result = i++ + ++i + i-- - --i;
   ```
5. **Bài 5:**
   ```java
   int q = 8, r = 3;
   int s = --q - r++ + ++q + --r - q--;
   ```

#### 🔥 Thử thách Master Toán Tử:
6. **Bài 6 (Hack não):**
   ```java
   int a = 2, b = 5, c = 1;
   int res = a++ + ++b * --c - c++ + b--;
   // Hãy tính giá trị của a, b, c và res?
   ```
7. **Bài 7 (Kết hợp logic):**
   ```java
   int x = 10, y = 10;
   boolean check = (x++ == 10) && (++y == 11);
   // Hỏi giá trị cuối cùng của x, y và check?
   ```

---

### II. Bài Tập `switch-case`
1. **Bài 1:** Nhập vào tháng (1-12), in ra số ngày của tháng đó (Tháng 2 mặc định 28 ngày, không xét năm nhuận).
2. **Bài 2:** Xây dựng một máy tính cơ bản. Nhập vào 2 số `a`, `b` và một ký tự toán tử (`+`, `-`, `*`, `/`). Dùng `switch` để tính và in ra kết quả tương ứng.
3. **Bài 3:** Nhập vào một chữ cái (a-z). Dùng `switch` kiểm tra xem nó là nguyên âm (`a, e, i, o, u`) hay phụ âm.
4. **Bài 4:** Menu đánh giá ứng dụng: Nhập một số nguyên từ 1 đến 5 (1: Rất tệ, 2: Tệ, 3: Bình thường, 4: Tốt, 5: Rất tốt). In ra câu phản hồi tương ứng.
5. **Bài 5:** Menu quán cafe. Hiển thị menu: `1. Cafe đen`, `2. Cafe sữa`, `3. Sinh tố`, `4. Nước ép`. Nhập vào lựa chọn (1-4) và in ra giá tiền tương ứng.

#### 🔥 Thử thách Master `switch-case`:
6. **Bài 6 (Quản lý trạng thái đơn hàng):** Nhập vào 1 chuỗi (String) trạng thái (`"PENDING"`, `"PROCESSING"`, `"SHIPPED"`, `"DELIVERED"`, `"CANCELED"`). Dùng `switch` in ra thông báo tiếng Việt tương ứng.
7. **Bài 7 (Gộp case nâng cao):** Nhập vào một tháng (1-12) và năm. Dùng `switch-case` gom nhóm tính số ngày của tháng đó *(Cần kiểm tra năm nhuận cho tháng 2, dùng chung khối lệnh `case` cho các tháng có 31 ngày và 30 ngày)*.

---

### III. Bài Tập Vòng Lặp (`for`, `while`, `do-while`)
1. **Bài 1:** In ra các số từ 1 đến 100, nhưng các số chia hết cho 3 thì in "Fizz", chia hết cho 5 thì in "Buzz", chia hết cho cả 3 và 5 thì in "FizzBuzz".
2. **Bài 2:** Nhập vào số nguyên dương `n`. Tính giai thừa của `n` (`n!`).
3. **Bài 3:** In ra bảng cửu chương của 1 số `n` nhập từ bàn phím.
4. **Bài 4:** Nhập vào 1 số `n`. In ra số đảo ngược của `n` (Ví dụ: `123 -> 321`) dùng `while`.
5. **Bài 5:** Nhập 1 số, kiểm tra xem nó có phải là số nguyên tố hay không.
6. **Bài 6:** Viết chương trình in ra `n` số Fibonacci đầu tiên.
7. **Bài 7:** Viết menu tương tác bằng `do-while`, bắt người dùng nhập mật khẩu, chỉ dừng lại khi nhập đúng `"admin123"`.
8. **Bài 8:** Tính tổng các số lẻ trong khoảng từ 1 đến 100.
9. **Bài 9:** In ra một hình tam giác vuông bằng dấu `*` với chiều cao `h`.
10. **Bài 10:** Nhập 2 số `a`, `b`. Tìm Ước chung lớn nhất (UCLN) của 2 số bằng vòng lặp.

#### 🔥 Thử thách Master Vòng Lặp (Nested Loops & Thuật toán):
11. **Bài 11 (Vẽ hình rỗng):** Nhập 2 kích thước `m`, `n`. In ra hình chữ nhật rỗng `m x n` bằng dấu `*`. (Chỉ viền là `*`, bên trong là khoảng trắng).
12. **Bài 12 (Vẽ tam giác cân):** Vẽ một tam giác cân bằng dấu `*` có chiều cao `h` nhập từ bàn phím.
13. **Bài 13 (Số hoàn hảo):** Số hoàn hảo là số có tổng các ước số thực sự của nó bằng chính nó (Ví dụ `6 = 1 + 2 + 3`). Hãy in ra tất cả các số hoàn hảo trong khoảng từ 1 đến 10.000.
14. **Bài 14 (Số lượng số nguyên tố):** Viết chương trình in ra chính xác `n` số nguyên tố đầu tiên (với `n` nhập từ bàn phím).
15. **Bài 15 (Số Armstrong):** Số Armstrong có `k` chữ số là số mà tổng lũy thừa bậc `k` của các chữ số của nó bằng chính nó (VD: $153 = 1^3 + 5^3 + 3^3$). In ra các số Armstrong có 3 chữ số.

---

### IV. Bài Toán Thực Tế
1. **Bài 1 (Tính tiền taxi):** 
   - 1 km đầu tiên: `15,000` VND.
   - Từ km 2 đến km 20: `12,000` VND/km.
   - Từ km 21 trở đi: `10,000` VND/km.
   - *Yêu cầu:* Nhập vào số km quãng đường đi, tính tổng tiền phải trả.

2. **Bài 2 (Lãi suất ngân hàng):**
   Gửi tiết kiệm `X` triệu VNĐ với lãi suất kép `8%/năm`. Hãy dùng vòng lặp để tính xem sau `Y` năm, tổng số tiền (cả gốc lẫn lãi) là bao nhiêu. Nhập `X` và `Y` từ bàn phím.

3. **Bài 3 (Trả góp):**
   Bạn mua một chiếc xe máy giá 50 triệu. Trả trước 20%. Số tiền còn lại bạn vay ngân hàng với lãi suất `1.5%/tháng`, trả góp trong vòng 12 tháng. Dùng vòng lặp in ra số tiền nợ và lãi phải trả ở mỗi tháng.

4. **Bài 4 (Trò chơi đoán số - Guessing Game):**
   Chương trình tạo một số ngẫu nhiên `secretNumber` từ 1 đến 100. Người dùng có tối đa 7 lượt đoán. Mỗi lần đoán, chương trình sẽ gợi ý "Quá lớn" hoặc "Quá nhỏ". Trò chơi kết thúc khi đoán trúng hoặc hết lượt.

5. **Bài 5 (Hệ thống đăng nhập chống Spam):**
   Mô phỏng chức năng đăng nhập. Yêu cầu nhập `username` và `password`. Nếu nhập sai 3 lần liên tiếp, khóa tài khoản (in ra "Tài khoản của bạn đã bị khóa") và kết thúc chương trình. Nếu đúng, in ra "Đăng nhập thành công". Dùng vòng lặp để theo dõi số lần sai.

#### 🔥 Thử thách Master Ứng Dụng Thực Tế:
6. **Bài 6 (Mô phỏng máy ATM mini):**
   Khởi tạo số dư tài khoản là 10.000.000 VNĐ. Hiển thị menu vòng lặp `do-while`: 
   `1. Xem số dư` | `2. Rút tiền` | `3. Nạp tiền` | `4. Thoát`. 
   - *Rút tiền:* Kiểm tra số tiền rút có `<= số dư` hiện tại không, nếu thỏa mãn thì trừ tiền.
   - *Nạp tiền:* Kiểm tra số tiền nạp `> 0`, cộng vào số dư.
7. **Bài 7 (Mô phỏng đếm ngược tên lửa):**
   Viết chương trình vòng lặp đếm ngược từ 10 về 0 để phóng tên lửa (in ra 10, 9, 8...). 
   Tại giây số 3, in ra `"Ignition!"`. Tại giây 0 in ra `"Liftoff!"`. Có thể sử dụng hàm `Thread.sleep(1000)` để làm cho vòng lặp thực sự dừng lại 1 giây ở mỗi lần đếm.

---

### V. Bài Tập Về Hàm & Từ Khóa (`break`, `continue`, `return`)

1. **Bài 1 (Dùng `break`):** Viết chương trình nhập liên tục các số nguyên từ bàn phím bằng vòng lặp vô hạn `while(true)`. Vòng lặp sẽ **dừng lại ngay lập tức** (dùng `break`) khi người dùng nhập vào số `0`. Cuối cùng in ra tổng các số đã nhập.
2. **Bài 2 (Dùng `continue`):** Viết chương trình in ra các số từ 1 đến 50. Tuy nhiên, nếu gặp số chia hết cho 7 thì **bỏ qua không in số đó** (dùng `continue` để nhảy sang số kế tiếp).
3. **Bài 3 (Viết Hàm cơ bản):** Viết một hàm `public static boolean isEven(int n)` để kiểm tra số chẵn. Dùng `return true` nếu chẵn, `return false` nếu lẻ. Trong hàm `main`, gọi hàm này để kiểm tra và in kết quả.
4. **Bài 4 (Kết hợp Hàm và `return` sớm):** Viết hàm `public static void printDivision(int a, int b)`. Nếu `b == 0`, in ra `"Lỗi: Không thể chia cho 0!"` và dùng lệnh `return;` để thoát hàm ngay lập tức (không chạy phần bên dưới). Nếu hợp lệ, in ra kết quả `a / b`.
5. **Bài 5 (Refactoring code):** Đem bài toán kiểm tra **Số Nguyên Tố** (ở mục III) bọc lại thành một hàm `public static boolean isPrime(int n)`. Hàm sẽ dùng vòng lặp, nếu phát hiện chia hết thì `return false` (thoát sớm). Nếu vòng lặp chạy xong an toàn thì `return true`. Gọi hàm trong `main` để test thử với số `29` và `30`.

#### 🔥 Thử thách Master Ứng Dụng Thực Tế (Hàm & Điều Khiển Luồng):
6. **Bài 6 (Hệ thống thanh toán Giỏ hàng):** 
   Viết hàm `public static double calculateDiscount(double price, String discountCode)`. 
   - Nếu mã là `"VIP"`, giảm 20%. Nếu mã `"MEMBER"`, giảm 10%. Nếu mã sai, in ra `"Mã không hợp lệ!"` và trả về `0` (dùng `return` sớm). 
   - Trong `main`, viết vòng lặp `while(true)` cho phép thu ngân nhập giá tiền các món hàng. Nếu nhập giá tiền bằng `0`, dùng `break` để chốt đơn và in ra tổng tiền phải trả.
7. **Bài 7 (Xử lý danh sách dữ liệu có nhiễu):** 
   Cho một vòng lặp `for` mô phỏng việc đọc dữ liệu từ ID 1 đến 20. 
   - Nếu ID là 13 (số xui xẻo), bỏ qua không xử lý và nhảy sang ID 14 (dùng `continue`).
   - Nếu ID là 18, in ra `"Đã tìm thấy dữ liệu mật!"` và dừng toàn bộ quá trình đọc (dùng `break`).
   - Các ID bình thường in ra `"Đang xử lý dữ liệu ID: ..."`.
8. **Bài 8 (Game RPG - Đánh quái vật):** 
   Viết hàm `public static boolean attack(int monsterHp, int damage)`. Hàm tính toán máu quái vật còn lại, nếu `< 0` thì `return true` (quái chết), ngược lại `return false`.
   - Trong `main`, quái vật có `HP = 100`. Dùng vòng lặp cho phép người chơi nhập sát thương (damage) đánh liên tục.
   - Nếu người chơi nhập sát thương âm (đánh hụt), in ra `"Đánh hụt!"` và dùng `continue` để bắt đầu lượt mới.
   - Nếu hàm `attack` trả về `true`, in ra `"Chiến thắng!"` và dùng `break` kết thúc trò chơi.

---

### VI. Bài Tập Nâng Cao (Toán tử 3 ngôi, Scope, Labeled Loop)

1. **Bài 1 (Toán tử 3 ngôi):** Viết hàm `public static String checkPass(double score)` trả về `"Pass"` nếu `score >= 5.0` và `"Fail"` nếu `score < 5.0`. **Yêu cầu:** Chỉ dùng đúng 1 dòng code bên trong hàm bằng toán tử 3 ngôi, tuyệt đối không dùng `if-else`.
2. **Bài 2 (Phạm vi biến - Sửa lỗi):** Đoạn code sau bị lỗi biên dịch. Hãy giải thích tại sao và sửa lại cho đúng:
   ```java
   public static void main(String[] args) {
       for(int i = 1; i <= 5; i++) {
           int total = 0;
           total += i;
       }
       System.out.println("Tổng là: " + total);
   }
   ```
3. **Bài 3 (Nested Loops - Vẽ bàn cờ):** Dùng 2 vòng lặp `for` lồng nhau để in ra một bàn cờ Vua $8 \times 8$. (Gợi ý: Dùng `if` kết hợp kiểm tra tổng `(i + j)` chẵn lẻ để in ra ô Đen `[B]` và ô Trắng `[W]`).
4. **Bài 4 (Labeled Break - Tìm kho báu):** Cho một bản đồ ma trận tọa độ $5 \times 5$. Kho báu được giấu ở tọa độ `(3, 4)`. Hãy dùng 2 vòng lặp lồng nhau duyệt qua từng tọa độ `(i, j)`. Mỗi lần duyệt in ra `"Đang tìm ở (i, j)"`. Khi đến đúng `(3, 4)`, in ra `"Bingo! Đã tìm thấy!"` và dùng **Labeled break** để dừng toàn bộ quá trình tìm kiếm ngay lập tức.

#### 🔥 Thử thách Master Ứng Dụng Thực Tế (Phần Nâng Cao):
5. **Bài 5 (Hệ thống phân tích Log máy chủ - Labeled Continue):** 
   Giả sử bạn có 3 tệp log, mỗi tệp có 100 dòng. Ta dùng vòng lặp ngoài `for(int file = 1; file <= 3; file++)` và vòng lặp trong `for(int line = 1; line <= 100; line++)`. 
   - **Tình huống:** Tệp số 2 bị hỏng nên cứ hễ `file == 2` thì bạn phải bỏ qua toàn bộ phần còn lại của tệp này và chuyển ngay sang đọc tệp số 3. (Gợi ý: Dùng `continue` có nhãn đặt ở vòng lặp ngoài).
   - Tiến trình bình thường thì in ra `"Đang đọc tệp " + file + " dòng " + line`.

---
---

<a id="part9"></a>
## 🏆 VII. Bài Tập Cuối Tuần (Tổng Hợp Toàn Bộ Kiến Thức)

Đây là 10 bài tập kết hợp **Toán tử, Switch-case, Vòng lặp, Nhãn (Label), Phạm vi biến, và Hàm**. Hãy cố gắng chia nhỏ logic ra thành các Hàm thay vì viết tất cả vào `main`.

1. **Bài 1 (Máy tính đa năng):** Viết chương trình hiển thị Menu: `1. Cộng`, `2. Trừ`, `3. Nhân`, `4. Chia`, `0. Thoát`. Dùng vòng lặp `while(true)` và `switch-case`. Nhập lựa chọn, sau đó gọi các Hàm tương ứng (VD: `public static double sum(double a, double b)`) để tính toán và in kết quả.
2. **Bài 2 (Oẳn tù tì - Kéo Búa Bao):** Máy tính random ngẫu nhiên 1 số từ 1 đến 3 (1: Kéo, 2: Búa, 3: Bao). Người chơi nhập lựa chọn của mình. So sánh và in kết quả (Thắng/Thua/Hòa). Dùng vòng lặp để chơi liên tục đến khi người chơi nhập số `0` thì dừng (`break`) và in ra tổng số ván đã thắng.
3. **Bài 3 (Máy bán nước tự động):** Khởi tạo ví tiền của bạn là 50.000đ. Menu: `1. Coca (10k)`, `2. Pepsi (10k)`, `3. Bò húc (15k)`. Khi chọn mua, viết Hàm kiểm tra xem số dư có đủ không. Nếu đủ thì trừ tiền, nếu không đủ thì báo lỗi. Dùng toán tử 3 ngôi in thông báo: `"Mua thành công, số dư: ..."` hoặc `"Không đủ tiền!"`.
4. **Bài 4 (Phân tích số):** Viết một Hàm nhận vào số nguyên dương `n`. Hàm sẽ in ra màn hình: số lượng chữ số của `n`, tổng các chữ số của `n`, và chữ số lớn nhất của `n`. (Ví dụ: `n = 492`, in ra: `3 chữ số`, `tổng = 15`, `lớn nhất = 9`).
5. **Bài 5 (Vẽ hình thoi rỗng):** Một thử thách kinh điển về Nested Loops. Nhập vào số dòng `n` (lẻ). Dùng vòng lặp `for` lồng nhau để in ra một hình thoi rỗng ruột bằng dấu `*`.
6. **Bài 6 (Tính tiền điện lũy tiến):** Viết một Hàm tính tiền điện `public static double calculateElectricityBill(int kwh)`. Bậc 1 (0-50 kWh): 1.678đ, Bậc 2 (51-100 kWh): 1.734đ, Bậc 3 (101-200 kWh): 2.014đ. Trả về tổng tiền. (Gợi ý: Dùng `if-else` lồng nhau kết hợp `return` sớm).
7. **Bài 7 (Xác thực mật khẩu mạnh):** Viết Hàm `public static boolean isValidPassword(String password)`. (Tạm mô phỏng việc kiểm tra độ dài bằng hàm `password.length()`). Điều kiện: mật khẩu phải dài hơn 8 ký tự. Dùng `while` bắt người dùng nhập mật khẩu, nếu chưa đạt chuẩn thì bắt nhập lại. (Dùng `continue` để bỏ qua lượt kiểm tra nếu độ dài $< 8$).
8. **Bài 8 (Dãy Fibonacci tùy biến):** Viết Hàm in ra `n` số Fibonacci đầu tiên. Nhưng thêm điều kiện: Nếu trong dãy sinh ra có số nào lớn hơn `1000` thì **dừng việc in ngay lập tức** (dùng `return` hoặc `break`).
9. **Bài 9 (Truy tìm cặp số - Labeled Break):** Cho 1 vòng lặp `x` chạy từ 1 đến 100, vòng lặp `y` chạy từ 1 đến 100. Hãy tìm **cặp số (x, y) đầu tiên** thỏa mãn phương trình: $3x + 5y = 125$. Khi tìm thấy, in ra kết quả và dùng **nhãn (label)** để thoát hoàn toàn khỏi cả 2 vòng lặp.
10. **Bài 10 (Ứng dụng Quản lý Ngân hàng Console):** 
    Tích hợp tất cả kỹ năng: Khai báo hằng số `final String ADMIN_PIN = "1234";`. 
    - Bắt người dùng nhập PIN, quá 3 lần sai -> Khóa thẻ (`return` kết thúc chương trình).
    - Nếu đúng, hiển thị Menu giao dịch: `Rút tiền`, `Chuyển khoản`, `Xem lịch sử`. (Dùng `switch`).
    - Viết các Hàm xử lý `withdraw(amount)`, `transfer(target, amount)`. Lưu ý biến số dư tài khoản phải được đặt ở **Scope** (phạm vi) nào để có thể vừa giữ được giá trị qua nhiều vòng lặp, vừa có thể truyền vào các Hàm.

---
---

<a id="part10"></a>
## 🎖️ VIII. FINAL BOSS - 3 Bài Tập Lớn (Mini-Projects)

Đây là 3 bài tập lớn (Mini-Projects). Mỗi bài yêu cầu bạn phải xây dựng một **Menu điều hướng** bằng `do-while` và `switch-case`, đồng thời tách toàn bộ logic tính toán ra thành các **Hàm (Methods)** riêng biệt. Cố gắng vận dụng mọi kiến thức từ Toán tử, Vòng lặp, Nhãn, Phạm vi biến cho đến `return` sớm.

### 🎮 Final Boss 1: Hệ thống Quản lý Sinh viên cơ bản
Viết chương trình quản lý điểm sinh viên.
**Menu chính:**
1. **Nhập điểm:** Toán, Lý, Hóa. (Bắt buộc điểm phải từ $0 \to 10$, nếu nhập sai dùng `continue` bắt nhập lại).
2. **Tính điểm trung bình:** Gọi hàm `public static double calculateAverage(double t, double l, double h)`.
3. **Xếp loại học lực:** Gọi hàm `public static String classifyStudent(double avg)`. (Giỏi: $\ge 8.0$, Khá: $\ge 6.5$, Trung bình: $\ge 5.0$, Yếu: $< 5.0$).
4. **Thoát chương trình.**
*(Yêu cầu: Dùng các biến cục bộ khai báo ngay trong hàm `main` trước vòng lặp để lưu điểm, giúp các menu khác nhau có chung dữ liệu).*

### 🛒 Final Boss 2: Ứng dụng Đặt đồ ăn online (Food Delivery)
Viết chương trình mô phỏng một app đặt đồ ăn đơn giản.
**Menu chính:**
1. **Xem Menu quán ăn:** In ra danh sách: `1. Phở (40k)`, `2. Bún chả (35k)`, `3. Cơm rang (30k)`.
2. **Đặt món:** Cho phép chọn món và số lượng. Tính tiền và cộng dồn vào biến `totalBill`.
3. **Áp dụng mã giảm giá:** Viết hàm `applyVoucher`. Nhập chuỗi: `"FREESHIP"` giảm 15k, `"DISCOUNT20"` giảm 20% tổng bill. 
4. **Thanh toán:** In ra số tiền cần trả. Yêu cầu nhập số tiền khách đưa. 
   - Nếu khách đưa thiếu tiền, thông báo lỗi. 
   - Nếu đủ, in ra tiền thừa và kết thúc chương trình (dùng `return` thoát hàm `main`).
5. **Thoát.**

### ⚔️ Final Boss 3: Trò chơi Nhập vai "Anh Hùng Diệt Rồng" (RPG Text-based)
Viết một game đánh theo lượt qua màn hình Console.
**Khởi tạo:**
- Anh hùng: $HP = 100, Mana = 50$.
- Rồng: $HP = 200$.

**Vòng lặp trận chiến (Hiển thị Menu mỗi lượt):**
1. **Tấn công thường:** Trừ 15 HP rồng.
2. **Dùng kỹ năng đặc biệt:** Tốn 20 Mana, trừ 40 HP rồng. Nếu không đủ Mana, thông báo và bắt chọn lại (dùng `continue`).
3. **Uống bình máu:** Hồi 30 HP, tốn 1 lượt (HP tối đa là 100).
4. **Bỏ chạy:** Kết thúc game ngay lập tức (`break`).

**Cơ chế tự động:** 
- Sau khi bạn chọn xong (1, 2 hoặc 3), nếu rồng chưa chết ($HP > 0$), Rồng sẽ phản công trừ của bạn ngẫu nhiên từ $10 \to 20$ HP. 
- *Điều kiện kết thúc:* Trò chơi dùng vòng lặp vô hạn, chỉ thoát khi Anh hùng chết ($HP \le 0$) hoặc Rồng chết ($HP \le 0$). 
- Khi kết thúc, viết hàm `printResult()` để hiển thị thông điệp vinh danh hoặc Game Over!
