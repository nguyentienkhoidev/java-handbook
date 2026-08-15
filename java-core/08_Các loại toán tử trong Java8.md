# Các loại toán tử trong Java

**Java operators** là các toán tử  trong Java bao gồm các ký hiệu đặc biệt dùng để thực hiện các phép tính trên biến hoặc các giá trị. Các toán tử này giúp bạn thực hiện các phép tính và logic trong lập trình Java một cách hiệu quả và linh hoạt.

### **1\. Arithmetic Operators (Toán tử số học)**

Được sử dụng để thực hiện các phép toán số học cơ bản.


| Toán tử | Mô tả | Ví dụ |
|---|---|---|
| + | Phép cộng | a + b |
| - | Phép trừ | a - b |
| * | Phép nhân | a * b |
| / | Phép chia | a / b |
| % | Phép chia lấy dư | a % b |
| ++ | Tăng giá trị lên 1 | a++ hoặc ++a |
| -- | Giảm giá trị đi 1 | a-- hoặc --a |



### **2\. Relational Operators (Toán tử quan hệ)**

Được sử dụng để so sánh hai giá trị và trả về kết quả boolean (`true` hoặc `false`).


| Toán tử | Mô tả | Ví dụ |
|---|---|---|
| == | So sánh bằng nhau | a == b |
| != | So sánh không bằng | a != b |
| > | Lớn hơn | a > b |
| < | Nhỏ hơn | a < b |
| >= | Lớn hơn hoặc bằng | a >= b |
| <= | Nhỏ hơn hoặc bằng | a <= b |



### **3\. Logical Operators (Toán tử logic)**

Được sử dụng để thực hiện các phép toán logic, kết hợp nhiều điều kiện.


| Toán tử | Mô tả | Ví dụ |
|---|---|---|
| && | Toán tử AND (và) | a && b |
| ` |  | ` |
| ! | Toán tử NOT (phủ định) | !a |



### **4\. Assignment Operators (Toán tử gán)**

Được sử dụng để gán giá trị cho biến.


| Toán tử | Mô tả | Ví dụ |
|---|---|---|
| = | Gán giá trị | a = 5 |
| += | Cộng và gán giá trị | a += 5 |
| -= | Trừ và gán giá trị | a -= 5 |
| *= | Nhân và gán giá trị | a *= 5 |
| /= | Chia và gán giá trị | a /= 5 |
| %= | Chia lấy dư và gán giá trị | a %= 5 |



### **5\. Ternary Operator (Toán tử điều kiện)**

Được sử dụng để viết các biểu thức điều kiện gọn hơn.


| Toán tử | Mô tả | Ví dụ |
|---|---|---|
| ? : | Biểu thức điều kiện | (condition) ? value1 : value2 |



```java
int a = 10, b = 20;
int result = (a > b) ? a : b;  // Trả về giá trị lớn hơn
```

### **6\. Bitwise Operators (Toán tử bitwise)**

Hoạt động trên các giá trị nhị phân (bit).


| Toán tử | Mô tả | Ví dụ |
|---|---|---|
| & | AND theo bit | a & b |
| ` | ` | OR theo bit |
| ^ | XOR theo bit | a ^ b |
| ~ | NOT theo bit | ~a |
| << | Dịch trái | a << 2 |
| >> | Dịch phải | a >> 2 |
| >>> | Dịch phải không dấu | a >>> 2 |



### **7\. Toán tử** `instanceof`

Kiểm tra xem một đối tượng có phải là thể hiện của một lớp cụ thể không.

```java
String str = "Hello Fox Dev";
boolean result = str instanceof String; // Kết quả là true
```

