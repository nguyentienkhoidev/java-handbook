# 📘 Bài 7: Mảng trong Java (Array) - Mảng 1 Chiều và Mảng 2 Chiều Toàn Diện

---

## 📑 Mục Lục
- [1️⃣ Tổng Quan Về Mảng Trong Java](#1-tổng-quan-về-mảng-trong-java)
  - [1.1 Mảng là gì? Tại sao cần dùng mảng?](#11-mảng-là-gì-tại-sao-cần-dùng-mảng)
  - [1.2 Đặc điểm cốt lõi của mảng trong Java](#12-đặc-điểm-cốt-lõi-của-mảng-trong-java)
  - [1.3 Cơ chế phân bổ bộ nhớ (Stack & Heap)](#13-cơ-chế-phân-bổ-bộ-nhớ-stack--heap)
- [2️⃣ Mảng 1 Chiều (Single-Dimensional Array)](#2-mảng-1-chiều-single-dimensional-array)
  - [2.1 Khai báo và Khởi tạo mảng](#21-khai-báo-và-khởi-tạo-mảng)
  - [2.2 Giá trị mặc định của mảng khi khởi tạo](#22-giá-trị-mặc-định-của-mảng-khi-khởi-tạo)
  - [2.3 Truy xuất & Cập nhật phần tử](#23-truy-xuất--cập-nhật-phần-tử)
  - [2.4 Lỗi kinh điển: ArrayIndexOutOfBoundsException](#24-lỗi-kinh-điển-arrayindexoutofboundsexception)
  - [2.5 Các cách duyệt mảng 1 chiều](#25-các-cách-duyệt-mảng-1-chiều)
  - [2.6 Thư viện tiện ích `java.util.Arrays`](#26-thư-viện-tiện-ích-javautilarrays)
  - [2.7 Sao chép mảng: Shallow Copy vs Deep Copy](#27-sao-chép-mảng-shallow-copy-vs-deep-copy)
- [3️⃣ Mảng 2 Chiều & Đa Chiều (Multi-Dimensional Array)](#3-mảng-2-chiều--đa-chiều-multi-dimensional-array)
  - [3.1 Bản chất: Mảng của các mảng (Array of Arrays)](#31-bản-chất-mảng-của-các-mảng-array-of-arrays)
  - [3.2 Khai báo & Khởi tạo mảng 2 chiều](#32-khai-báo--khởi-tạo-mảng-2-chiều)
  - [3.3 Mảng răng cưa (Jagged Array)](#33-mảng-răng-cưa-jagged-array)
  - [3.4 Truy xuất, xác định kích thước & Duyệt mảng 2 chiều](#34-truy-xuất-xác-định-kích-thước--duyệt-mảng-2-chiều)
  - [3.5 In ma trận 2 chiều chuẩn chỉnh](#35-in-ma-trận-2-chiều-chuẩn-chỉnh)
- [4️⃣ Tổng Kết Các "Cạm Bẫy" Cần Tránh](#4-tổng-kết-các-cạm-bẫy-cần-tránh)
- [📝 KHO BÀI TẬP THỰC HÀNH MẢNG 1 CHIỀU & 2 CHIỀU](#-kho-bài-tập-thực-hành-mảng-1-chiều--2-chiều)
  - [I. PHẦN CƠ BẢN & KHỞI ĐỘNG (Mảng 1 Chiều - Dễ/Khá)](#i-phần-cơ-bản--khởi-động-mảng-1-chiều---dễkhá)
    - [Bài 1 (Dễ): Tính tổng số chẵn và đếm số lượng số lẻ trong mảng](#bài-1-dễ-tính-tổng-số-chẵn-và-đếm-số-lượng-số-lẻ-trong-mảng)
    - [Bài 2 (Dễ): Tính trung bình cộng các phần tử tại vị trí index chẵn](#bài-2-dễ-tính-trung-bình-cộng-các-phần-tử-tại-vị-trí-index-chẵn)
    - [Bài 3 (Dễ - Khá): Tìm giá trị lớn nhất (Max), nhỏ nhất (Min) và vị trí của chúng](#bài-3-dễ---khá-tìm-giá-trị-lớn-nhất-max-nhỏ-nhất-min-và-vị-trí-của-chúng)
    - [Bài 4 (Dễ - Khá): Tìm kiếm phần tử X và đếm số lần xuất hiện](#bài-4-dễ---khá-tìm-kiếm-phần-tử-x-và-đếm-số-lần-xuất-hiện)
    - [Bài 5 (Khá): Kiểm tra mảng đã được sắp xếp tăng dần hay chưa](#bài-5-khá-kiểm-tra-mảng-đã-được-sắp-xếp-tăng-dần-hay-chưa)
  - [II. PHẦN RÈN LUYỆN TƯ DUY & THUẬT TOÁN (Mảng 1 Chiều - Trung bình/Nâng cao)](#ii-phần-rèn-luyện-tư-duy--thuật-toán-mảng-1-chiều---trung-bìnhnâng-cao)
    - [Bài 6: Tính tổng và trung bình cộng các số dương](#bài-6-tính-tổng-và-trung-bình-cộng-các-số-dương)
    - [Bài 7: Tìm phần tử lớn thứ nhì (Second Largest) không dùng hàm sort](#bài-7-tìm-phần-tử-lớn-thứ-nhì-second-largest-không-dùng-hàm-sort)
    - [Bài 8: Đếm và liệt kê các số nguyên tố trong mảng](#bài-8-đếm-và-liệt-kê-các-số-nguyên-tố-trong-mảng)
    - [Bài 9: Đảo ngược mảng tại chỗ (In-place Array Reversal)](#bài-9-đảo-ngược-mảng-tại-chỗ-in-place-array-reversal)
    - [Bài 10: Phân tách mảng chẵn và mảng lẻ](#bài-10-phân-tách-mảng-chẵn-và-mảng-lẻ)
    - [Bài 11: Kiểm tra mảng đối xứng (Palindrome Array)](#bài-11-kiểm-tra-mảng-đối-xứng-palindrome-array)
    - [Bài 12: Đếm tần suất xuất hiện của từng phần tử](#bài-12-đếm-tần-suất-xuất-hiện-của-từng-phần-tử)
    - [Bài 13: Đếm số lượng phần tử lớn hơn giá trị trung bình của toàn mảng](#bài-13-đếm-số-lượng-phần-tử-lớn-hơn-giá-trị-trung-bình-của-toàn-mảng)
    - [Bài 14: Xoay vòng mảng sang trái K vị trí (Left Rotate by K)](#bài-14-xoay-vòng-mảng-sang-trái-k-vị-trí-left-rotate-by-k)
    - [Bài 15: Loại bỏ các phần tử trùng lặp (Giữ mảng duy nhất)](#bài-15-loại-bỏ-các-phần-tử-trùng-lặp-giữ-mảng-duy-nhất)
  - [III. PHẦN MẢNG 2 CHIỀU CƠ BẢN (Nắm Vững Bản Chất Ma Trận)](#iii-phần-mảng-2-chiều-cơ-bản-nắm-vững-bản-chất-ma-trận)
    - [Bài 16 (Cơ bản): Nhập, in ma trận chữ nhật M x N và tính tổng các phần tử](#bài-16-cơ-bản-nhập-in-ma-trận-chữ-nhật-m-x-n-và-tính-tổng-các-phần-tử)
    - [Bài 17 (Cơ bản - Khá): Tính tổng từng dòng và tìm phần tử lớn nhất của ma trận](#bài-17-cơ-bản---khá-tính-tổng-từng-dòng-và-tìm-phần-tử-lớn-nhất-của-ma-trận)
    - [Bài 18 (Trọng tâm): Tính tổng đường chéo chính và đường chéo phụ của ma trận vuông](#bài-18-trọng-tâm-tính-tổng-đường-chéo-chính-và-đường-chéo-phụ-của-ma-trận-vuông)

---

## 1️⃣ Tổng Quan Về Mảng Trong Java

### 1.1 Mảng là gì? Tại sao cần dùng mảng?
Hãy tưởng tượng bạn cần lưu trữ điểm số của **50 học sinh** trong một lớp:
- **Nếu không có mảng:** Bạn phải khai báo 50 biến riêng biệt: `score1`, `score2`, ..., `score50`. Việc viết code để tính điểm trung bình hay tìm người điểm cao nhất sẽ là một "cơn ác mộng" với hàng trăm dòng `if-else`.
- **Khi có mảng:** Bạn chỉ cần tạo **1 biến duy nhất** chứa toàn bộ 50 điểm số đó: `double[] scores = new double[50];`.

> **Định nghĩa:** **Mảng (Array)** là một tập hợp các phần tử có **cùng kiểu dữ liệu**, được lưu trữ tại các **vùng nhớ liên tiếp nhau** trong bộ nhớ và được truy xuất thông qua **chỉ số (index)**.

---

### 1.2 Đặc điểm cốt lõi của mảng trong Java
1. **Kiểu dữ liệu đồng nhất (Homogeneous):** Mọi phần tử trong mảng bắt buộc phải cùng một kiểu (ví dụ: toàn bộ là `int`, toàn bộ là `String`...).
2. **Kích thước cố định (Fixed Size):** Sau khi đã khởi tạo mảng với độ dài $N$, bạn **không thể** tăng thêm hoặc giảm bớt số lượng phần tử của mảng đó.
3. **Đánh chỉ số từ 0 (0-indexed):** 
   - Phần tử đầu tiên có vị trí (index) là `0`.
   - Phần tử cuối cùng có index là `length - 1`.
4. **Mảng là một Đối tượng (Object):** Trong Java, mảng không phải là kiểu nguyên thủy (primitive) mà là một Object được cấp phát trên bộ nhớ **Heap**.

---

### 1.3 Cơ chế phân bổ bộ nhớ (Stack & Heap)

Khi bạn khai báo: `int[] numbers = new int[4];`

```
           STACK MEMORY                            HEAP MEMORY
     +-----------------------+              +-----------------------+
     |                       |              |     int[] Object      |
     |   numbers (tham chiếu)|              |  Index: [0] [1] [2] [3]|
     |     [ 0x1A2B3C ]  ----+------------->|  Value: [ 0 | 0 | 0 | 0]|
     |                       |   Địa chỉ    |  length: 4            |
     +-----------------------+   0x1A2B3C   +-----------------------+
```

- **Biến `numbers`:** Nằm trên vùng nhớ **Stack**, lưu giữ địa chỉ tham chiếu (con trỏ) trỏ tới vùng nhớ trên Heap.
- **Thực thể mảng:** Nằm trên vùng nhớ **Heap**, là một khối ô nhớ liên tiếp nhau chứa các giá trị thực tế.

---

## 2️⃣ Mảng 1 Chiều (Single-Dimensional Array)

### 2.1 Khai báo và Khởi tạo mảng

Có 2 cách thông dụng để khai báo và khởi tạo mảng:

#### Cách 1: Khai báo kích thước trước, gán giá trị sau (Dùng từ khóa `new`)
Thích hợp khi chưa biết trước các giá trị cụ thể hoặc cần nhận dữ liệu từ người dùng.
```java
// Cú pháp chuẩn (Khuyên dùng):
kiểu_dữ_liệu[] tên_mảng = new kiểu_dữ_liệu[kích_thước];

int[] numbers = new int[5]; // Mảng gồm 5 số nguyên (index từ 0 đến 4)
String[] names = new String[10]; // Mảng gồm 10 chuỗi
```

> **💡 Lưu ý:** Bạn cũng có thể viết `int numbers[] = new int[5];` (kiểu C/C++), nhưng cách `int[] numbers` chuẩn và dễ đọc hơn trong Java.

#### Cách 2: Khởi tạo và gán sẵn giá trị (Array Literals)
Thích hợp khi đã biết rõ toàn bộ giá trị ban đầu.
```java
// Cách rút gọn (chỉ dùng khi khai báo trực tiếp)
int[] scores = {9, 8, 10, 7, 6};

// Cách đầy đủ (có thể dùng để gán lại hoặc truyền vào hàm)
int[] primes = new int[]{2, 3, 5, 7, 11};
```

---

### 2.2 Giá trị mặc định của mảng khi khởi tạo
Khi bạn dùng từ khóa `new DataType[N]`, Java sẽ tự động điền các giá trị mặc định cho từng ô nhớ:

| Kiểu dữ liệu | Giá trị mặc định (Default Value) |
| :--- | :--- |
| `byte`, `short`, `int`, `long` | `0` |
| `float`, `double` | `0.0` |
| `boolean` | `false` |
| `char` | `'\u0000'` (ký tự Null / trống) |
| Kiểu tham chiếu (`String`, `Object`, `Array`...) | `null` |

---

### 2.3 Truy xuất & Cập nhật phần tử

```
 Mảng: numbers = { 10,  20,  30,  40,  50 }
 Index (Chỉ số):   0    1    2    3    4
 Kích thước (length) = 5
```

- **Đọc giá trị:** `tên_mảng[index]`
- **Gán/Cập nhật giá trị:** `tên_mảng[index] = giá_trị_mới;`
- **Lấy độ dài mảng:** `tên_mảng.length` (thuộc tính `length`, không có dấu ngoặc tròn `()`).

```java
int[] numbers = {10, 20, 30, 40, 50};

System.out.println("Phần tử đầu tiên: " + numbers[0]); // 10
System.out.println("Phần tử cuối cùng: " + numbers[numbers.length - 1]); // 50

// Cập nhật giá trị ô thứ 2 (index 1)
numbers[1] = 99;
System.out.println("Giá trị mới ở index 1: " + numbers[1]); // 99
```

---

### 2.4 Lỗi kinh điển: `ArrayIndexOutOfBoundsException`
Đây là ngoại lệ phổ biến nhất khi mới học mảng. Ngoại lệ này ném ra khi bạn truy xuất vào một index **âm** hoặc **lớn hơn hoặc bằng `length`**.

```java
int[] arr = new int[5]; // Index hợp lệ: 0, 1, 2, 3, 4

// CÁC LỖI THƯỜNG GẶP:
// arr[5] = 100;   // ❌ LỖI: Index 5 vượt quá (length = 5)
// arr[-1] = 50;   // ❌ LỖI: Index âm không tồn tại trong Java
```

> **⚠️ BẢO HIỂM LỖI:** Trước khi truy xuất `arr[i]`, luôn đảm bảo: `0 <= i < arr.length`.

---

### 2.5 Các cách duyệt mảng 1 chiều

#### 1. Duyệt bằng vòng lặp `for` truyền thống
Dùng khi bạn cần **biết chỉ số `i`**, duyệt ngược hoặc thay đổi giá trị trong mảng.
```java
int[] arr = {10, 20, 30, 40, 50};

// Duyệt xuôi từ đầu đến cuối:
for (int i = 0; i < arr.length; i++) {
    System.out.println("arr[" + i + "] = " + arr[i]);
}

// Duyệt ngược từ cuối về đầu:
for (int i = arr.length - 1; i >= 0; i--) {
    System.out.print(arr[i] + " ");
}
```

#### 2. Duyệt bằng vòng lặp `for-each` (Enhanced for loop)
Cú pháp cực kỳ ngắn gọn, an toàn (không lo lỗi tràn index). Dùng khi bạn chỉ muốn **đọc toàn bộ giá trị** mà không cần quan tâm đến vị trí index.
```java
int[] arr = {10, 20, 30, 40, 50};

for (int value : arr) {
    System.out.print(value + " ");
}
```

> **⚠️ Lưu ý:** Không thể dùng `for-each` để thay đổi giá trị của các phần tử kiểu nguyên thủy trong mảng vì biến lặp `value` chỉ là một bản sao tạm thời.

---

### 2.6 Thư viện tiện ích `java.util.Arrays`
Java cung cấp sẵn class `Arrays` với rất nhiều hàm tiện ích cực mạnh:

```java
import java.util.Arrays;

public class ArraysDemo {
    public static void main(String[] args) {
        int[] arr = {5, 2, 9, 1, 6};

        // 1. In mảng thành chuỗi đẹp mắt: Arrays.toString()
        System.out.println("Mảng ban đầu: " + Arrays.toString(arr)); // [5, 2, 9, 1, 6]

        // 2. Sắp xếp mảng tăng dần: Arrays.sort()
        Arrays.sort(arr);
        System.out.println("Sau khi sort: " + Arrays.toString(arr)); // [1, 2, 5, 6, 9]

        // 3. Tìm kiếm nhị phân (Yêu cầu mảng đã sort): Arrays.binarySearch()
        int index = Arrays.binarySearch(arr, 5);
        System.out.println("Vị trí của số 5 là: " + index); // 2

        // 4. Điền toàn bộ mảng với một giá trị: Arrays.fill()
        int[] emptyArr = new int[5];
        Arrays.fill(emptyArr, -1);
        System.out.println("Sau fill: " + Arrays.toString(emptyArr)); // [-1, -1, -1, -1, -1]

        // 5. So sánh 2 mảng theo nội dung: Arrays.equals()
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        System.out.println("a == b: " + (a == b)); // false (vì so sánh 2 địa chỉ ô nhớ khác nhau)
        System.out.println("Arrays.equals(a, b): " + Arrays.equals(a, b)); // true (so sánh nội dung)
    }
}
```

---

### 2.7 Sao chép mảng: Shallow Copy vs Deep Copy

```
1. Gán biến (arr2 = arr1) - Shallow Copy (Tham chiếu cùng 1 vùng nhớ):
   arr1 ---------\
                  +-----> [ Heap Memory: 10, 20, 30 ]
   arr2 ---------/
   (Sửa arr2 làm arr1 thay đổi theo!)

2. Sao chép thực sự - Deep Copy (Tạo mảng mới độc lập):
   arr1 ---------------> [ Heap 1: 10, 20, 30 ]
   arr2 ---------------> [ Heap 2: 10, 20, 30 ]
```

```java
int[] original = {10, 20, 30};

// ❌ SAI LẦM PHỔ BIẾN:
int[] wrongCopy = original; // Chỉ copy địa chỉ ô nhớ
wrongCopy[0] = 999;
System.out.println(original[0]); // In ra 999 (Bị thay đổi theo!)

// ✅ CÁCH COPY ĐÚNG:
// Cách 1: Dùng Arrays.copyOf()
int[] copy1 = Arrays.copyOf(original, original.length);

// Cách 2: Dùng clone()
int[] copy2 = original.clone();

// Cách 3: Dùng System.arraycopy() (Tốc độ cao nhất)
int[] copy3 = new int[original.length];
System.arraycopy(original, 0, copy3, 0, original.length);
```

---

## 3️⃣ Mảng 2 Chiều & Đa Chiều (Multi-Dimensional Array)

### 3.1 Bản chất: Mảng của các mảng (Array of Arrays)
Trong Java, không có mảng đa chiều thực sự ở mức phần cứng. **Mảng 2 chiều thực chất là một mảng 1 chiều mà mỗi phần tử của nó lại trỏ tới một mảng 1 chiều khác.**

```
 Ma trận kích thước 3 hàng x 4 cột (3 rows, 4 columns):

                Cột 0   Cột 1   Cột 2   Cột 3
    Hàng 0:    [ 10,     20,     30,     40 ]   --> a[0] (độ dài 4)
    Hàng 1:    [ 50,     60,     70,     80 ]   --> a[1] (độ dài 4)
    Hàng 2:    [ 90,    100,    110,    120 ]   --> a[2] (độ dài 4)

                Mô hình bộ nhớ (Stack & Heap):
    Stack:
    [ matrix ] ----> Heap: Mảng chính chứa 3 tham chiếu:
                     [0] ----> [ 10 | 20 | 30 | 40 ]
                     [1] ----> [ 50 | 60 | 70 | 80 ]
                     [2] ----> [ 90 | 100 | 110 | 120 ]
```

---

### 3.2 Khai báo & Khởi tạo mảng 2 chiều

#### Cách 1: Cấp phát kích thước ma trận hình chữ nhật
```java
// Cú pháp: kiểu_dữ_liệu[][] tên_mảng = new kiểu_dữ_liệu[số_hàng][số_cột];
int[][] matrix = new int[3][4]; // 3 hàng, 4 cột (tổng cộng 12 phần tử)
```

#### Cách 2: Khởi tạo trực tiếp kèm giá trị
```java
int[][] matrix = {
    {1, 2, 3},      // Hàng 0 (index 0)
    {4, 5, 6},      // Hàng 1 (index 1)
    {7, 8, 9}       // Hàng 2 (index 2)
};
```

---

### 3.3 Mảng răng cưa (Jagged Array)
Vì là "mảng của các mảng", các hàng trong Java **không nhất thiết phải có cùng số lượng cột**! Mảng có các dòng có độ dài khác nhau gọi là **Jagged Array** (Mảng răng cưa / zíc zắc).

```java
// Khai báo mảng có 3 hàng nhưng chưa định nghĩa số cột:
int[][] jagged = new int[3][];

jagged[0] = new int[2]; // Hàng 0 có 2 cột
jagged[1] = new int[5]; // Hàng 1 có 5 cột
jagged[2] = new int[3]; // Hàng 2 có 3 cột
```

---

### 3.4 Truy xuất, xác định kích thước & Duyệt mảng 2 chiều

- **Lấy số lượng hàng:** `matrix.length`
- **Lấy số lượng cột của hàng `i`:** `matrix[i].length`
- **Truy xuất phần tử tại hàng `r`, cột `c`:** `matrix[r][c]`

#### Duyệt bằng 2 vòng lặp `for` lồng nhau:
```java
int[][] matrix = {
    {10, 20, 30},
    {40, 50, 60}
};

for (int i = 0; i < matrix.length; i++) { // Duyệt từng hàng
    for (int j = 0; j < matrix[i].length; j++) { // Duyệt từng cột trên hàng i
        System.out.print(matrix[i][j] + "\t");
    }
    System.out.println(); // Hết 1 hàng thì xuống dòng
}
```

#### Duyệt bằng `for-each` lồng nhau:
```java
for (int[] row : matrix) {
    for (int cell : row) {
        System.out.print(cell + "\t");
    }
    System.out.println();
}
```

---

### 3.5 In ma trận 2 chiều chuẩn chỉnh
- Muốn in nhanh mảng 2 chiều ra console mà không cần viết vòng lặp, hãy dùng: **`Arrays.deepToString()`**

```java
int[][] matrix = {{1, 2}, {3, 4}};
System.out.println(Arrays.deepToString(matrix)); 
// Output: [[1, 2], [3, 4]]
```

---

## 4️⃣ Tổng Kết Các "Cạm Bẫy" Cần Tránh

| Cạm bẫy (Gotchas) | Hậu quả | Cách xử lý đúng |
| :--- | :--- | :--- |
| **Truy cập index `>= length`** | Ném `ArrayIndexOutOfBoundsException` | Luôn kiểm tra `0 <= index < arr.length` |
| **Gán `arr2 = arr1` để copy** | Sửa `arr2` làm thay đổi luôn `arr1` | Dùng `Arrays.copyOf()` hoặc `arr.clone()` |
| **Dùng `==` để so sánh 2 mảng** | Luôn ra `false` dù nội dung giống nhau | Dùng `Arrays.equals(a, b)` hoặc `Arrays.deepEquals()` |
| **Muốn thêm/xóa phần tử** | Không thể resize mảng cố định | Tạo mảng mới có kích thước mới rồi copy sang (hoặc dùng `ArrayList`) |
| **Quên khởi tạo phần tử mảng con** | `NullPointerException` (đặc biệt với Jagged array / mảng Object) | Luôn `new` mảng con trước khi gán `jagged[i][j]` |

---

## 📝 KHO BÀI TẬP THỰC HÀNH MẢNG 1 CHIỀU & 2 CHIỀU

> **🎯 Hướng dẫn luyện tập:**  
> Hệ thống bài tập được thiết kế phân cấp độ từ **Mảng 1 Chiều Cơ Bản & Nâng Cao** đến **Mảng 2 Chiều Cốt Lõi**.  
> Các bài tập đều **không có code giải sẵn** để bạn tự lập trình và rèn luyện tư duy thực tế. Hãy dùng các test case mẫu để đối chiếu kết quả!

---

### I. PHẦN CƠ BẢN & KHỞI ĐỘNG (Mảng 1 Chiều - Dễ/Khá)

#### Bài 1 (Dễ): Tính tổng số chẵn và đếm số lượng số lẻ trong mảng
- **Độ khó:** ⭐ (Dễ)
- **Mục tiêu:** Nắm vững cách duyệt mảng cơ bản với vòng lặp và câu lệnh rẽ nhánh `if-else`.
- **Yêu cầu:** Nhập vào số nguyên $N$ ($N > 0$) và một mảng gồm $N$ số nguyên. Hãy:
  1. Tính tổng tất cả các số chẵn trong mảng.
  2. Đếm xem trong mảng có bao nhiêu số lẻ.
- **Ví dụ:**
  - **Input:** $N = 6$, Mảng = `[4, 7, 2, 9, 10, 5]`
  - **Output:**
    ```text
    Tổng các số chẵn: 16 (4 + 2 + 10)
    Số lượng số lẻ: 3 (gồm các số 7, 9, 5)
    ```
- **Gợi ý tư duy:**
  - Khai báo 2 biến: `sumEven = 0` và `countOdd = 0`.
  - Duyệt mảng: nếu `x % 2 == 0` thì `sumEven += x`, ngược lại `countOdd++`.

---

#### Bài 2 (Dễ): Tính trung bình cộng các phần tử tại vị trí index chẵn
- **Độ khó:** ⭐ (Dễ)
- **Mục tiêu:** Hiểu rõ khái niệm chỉ số (index) so với giá trị (value) của phần tử.
- **Yêu cầu:** Nhập vào mảng $N$ số nguyên. Hãy tính trung bình cộng của các phần tử nằm ở **vị trí chỉ số chẵn** (index $0, 2, 4, 6, \dots$).
- **Ví dụ:**
  - **Input:** Mảng = `[10, 5, 20, 8, 30, 12]` (Index chẵn là `0, 2, 4` với giá trị tương ứng `10, 20, 30`)
  - **Output:**
    ```text
    Trung bình cộng các phần tử ở vị trí chẵn: 20.00
    ```
- **Gợi ý tư duy:**
  - Chạy vòng lặp với bước nhảy `i += 2`: `for (int i = 0; i < arr.length; i += 2)`.
  - Cộng dồn `sum += arr[i]` và đếm số lượng phần tử được cộng để tính trung bình.

---

#### Bài 3 (Dễ - Khá): Tìm giá trị lớn nhất (Max), nhỏ nhất (Min) và vị trí của chúng
- **Độ khó:** ⭐⭐ (Dễ - Khá)
- **Mục tiêu:** Hiểu và áp dụng kỹ thuật "Đặt lính canh" (Sentinel Value) để tìm cực trị.
- **Yêu cầu:** Nhập vào mảng gồm $N$ số thực. Hãy tìm giá trị lớn nhất ($Max$) và nhỏ nhất ($Min$) trong mảng cùng với **chỉ số (index)** đầu tiên mà chúng xuất hiện.
- **Ví dụ:**
  - **Input:** Mảng = `[12.5, 4.2, 99.0, -3.5, 15.8]`
  - **Output:**
    ```text
    Giá trị lớn nhất (Max): 99.0 tại vị trí index: 2
    Giá trị nhỏ nhất (Min): -3.5 tại vị trí index: 3
    ```
- **Gợi ý tư duy:**
  - Giả sử phần tử đầu tiên là lớn nhất và nhỏ nhất: `max = arr[0]`, `min = arr[0]`, `maxIndex = 0`, `minIndex = 0`.
  - Duyệt từ `i = 1` đến `arr.length - 1`: nếu `arr[i] > max` thì cập nhật lại `max` và `maxIndex = i`; tương tự với `min`.

---

#### Bài 4 (Dễ - Khá): Tìm kiếm phần tử X và đếm số lần xuất hiện
- **Độ khó:** ⭐⭐ (Dễ - Khá)
- **Mục tiêu:** Rèn luyện giải thuật Tìm kiếm tuyến tính (Linear Search).
- **Yêu cầu:** Nhập một mảng $N$ số nguyên và một số nguyên $X$.
  1. Kiểm tra xem $X$ có xuất hiện trong mảng hay không.
  2. Nếu có: In ra tổng số lần $X$ xuất hiện và tất cả các vị trí (index) của $X$.
  3. Nếu không: In ra thông báo `"Không tìm thấy X trong mảng!"`.
- **Ví dụ:**
  - **Input 1:** Mảng = `[3, 8, 5, 8, 2, 8, 1]`, $X = 8$  
    **Output:** `Số 8 xuất hiện 3 lần tại các vị trí index: 1, 3, 5`
  - **Input 2:** Mảng = `[1, 2, 3, 4]`, $X = 9$  
    **Output:** `Không tìm thấy 9 trong mảng!`
- **Gợi ý tư duy:**
  - Khởi tạo biến đếm `count = 0`.
  - Duyệt qua từng phần tử bằng vòng lặp `for (int i = 0; i < arr.length; i++)`.
  - Nếu `arr[i] == X` thì tăng `count` và in index `i`. Kết thúc vòng lặp, nếu `count == 0` thì thông báo không tìm thấy.

---

#### Bài 5 (Khá): Kiểm tra mảng đã được sắp xếp tăng dần hay chưa
- **Độ khó:** ⭐⭐ (Khá)
- **Mục tiêu:** Rèn luyện kỹ thuật cờ hiệu (Flag / Boolean) và so sánh các cặp phần tử liền kề.
- **Yêu cầu:** Nhập một mảng số nguyên. Hãy kiểm tra xem các phần tử trong mảng có đang được sắp xếp theo thứ tự **tăng dần** (nghĩa là phần tử đứng sau luôn $\ge$ phần tử đứng trước) hay không.
- **Ví dụ:**
  - **Input 1:** Mảng = `[1, 3, 5, 8, 12]` $\rightarrow$ **Output:** `Mảng ĐÃ được sắp xếp tăng dần.`
  - **Input 2:** Mảng = `[2, 5, 1, 9, 10]` $\rightarrow$ **Output:** `Mảng CHƯA được sắp xếp tăng dần.`
- **Gợi ý tư duy:**
  - Giả định mảng ban đầu đã được sắp xếp: `boolean isSorted = true;`.
  - Chạy vòng lặp từ `i = 0` đến `arr.length - 2`.
  - Nếu phát hiện bất kỳ cặp nào mà `arr[i] > arr[i + 1]`, gán `isSorted = false` và lập tức dừng vòng lặp (`break`).
  - Kiểm tra giá trị của `isSorted` để in kết luận.

---

### II. PHẦN RÈN LUYỆN TƯ DUY & THUẬT TOÁN (Mảng 1 Chiều - Trung bình/Nâng cao)

#### Bài 6: Tính tổng và trung bình cộng các số dương
- **Yêu cầu:** Nhập vào số nguyên $N$ ($N > 0$) và mảng gồm $N$ số nguyên. Tính tổng và giá trị trung bình cộng (in định dạng 2 chữ số thập phân) của tất cả các số dương ($> 0$) có trong mảng. Nếu mảng không có số dương nào, hãy thông báo tương ứng.
- **Ví dụ:**
  - **Input:** `N = 6`, Mảng = `[3, -2, 7, 0, 5, -8]`
  - **Output:** 
    ```text
    Tổng các số dương: 15
    Trung bình cộng các số dương: 5.00
    ```
- **Gợi ý tư duy:**
  - Dùng 1 biến `sum` tích luỹ tổng và 1 biến `count` đếm số lượng số dương.
  - Ép kiểu `(double) sum / count` để tránh phép chia nguyên.

---

#### Bài 7: Tìm phần tử lớn thứ nhì (Second Largest) không dùng hàm sort
- **Yêu cầu:** Cho mảng số nguyên gồm ít nhất 2 phần tử. Tìm giá trị lớn thứ nhì trong mảng chỉ bằng **1 lần duyệt** ($O(n)$) mà **không được sử dụng** hàm `Arrays.sort()`.
- **Ví dụ:**
  - **Input 1:** Mảng = `[10, 5, 20, 20, 8, 15]`  
    **Output:** `Số lớn nhất = 20, Số lớn thứ nhì = 15`
  - **Input 2:** Mảng = `[7, 7, 7]`  
    **Output:** `Không tồn tại số lớn thứ nhì`
- **Gợi ý tư duy:**
  - Khởi tạo `max = Integer.MIN_VALUE` và `secondMax = Integer.MIN_VALUE`.
  - Khi gặp `x > max`: gán `secondMax = max` rồi cập nhật `max = x`.
  - Khi gặp `x > secondMax` và `x < max`: chỉ cập nhật `secondMax = x`.

---

#### Bài 8: Đếm và liệt kê các số nguyên tố trong mảng
- **Yêu cầu:** Nhập một mảng số nguyên. Viết hàm riêng `boolean isPrime(int n)` để kiểm tra số nguyên tố, sau đó đếm và liệt kê toàn bộ các số nguyên tố xuất hiện trong mảng.
- **Ví dụ:**
  - **Input:** Mảng = `[2, 9, 11, 15, 17, 18, 23, 1, 0]`
  - **Output:**
    ```text
    Có 4 số nguyên tố trong mảng: 2, 11, 17, 23
    ```
- **Gợi ý tư duy:**
  - Số nguyên tố là số $> 1$ và chỉ chia hết cho 1 và chính nó (kiểm tra ước từ $2$ đến $\sqrt{n}$).
  - Duyệt qua từng phần tử mảng, nếu `isPrime(x)` trả về `true` thì in ra và tăng biến đếm.

---

#### Bài 9: Đảo ngược mảng tại chỗ (In-place Array Reversal)
- **Yêu cầu:** Đảo ngược toàn bộ thứ tự các phần tử của một mảng mà **không được tạo mảng phụ** (thao tác trực tiếp trên mảng ban đầu).
- **Ví dụ:**
  - **Input:** Mảng ban đầu = `[10, 20, 30, 40, 50, 60]`
  - **Output:** Mảng sau khi đảo = `[60, 50, 40, 30, 20, 10]`
- **Gợi ý tư duy:**
  - Sử dụng kỹ thuật **Hai con trỏ (Two Pointers)**: con trỏ `left = 0` và `right = arr.length - 1`.
  - Dùng vòng lặp `while (left < right)`, hoán đổi (swap) giá trị `arr[left]` và `arr[right]`, sau đó `left++` và `right--`.

---

#### Bài 10: Phân tách mảng chẵn và mảng lẻ
- **Yêu cầu:** Cho một mảng các số nguyên. Hãy phân tách mảng ban đầu thành 2 mảng mới:
  1. Mảng `evenArr` chứa tất cả các số chẵn.
  2. Mảng `oddArr` chứa tất cả các số lẻ.
  *(Kích thước của 2 mảng mới phải vừa khít với số lượng phần tử cần chứa, không tạo thừa ô nhớ)*.
- **Ví dụ:**
  - **Input:** Mảng gốc = `[12, 7, 9, 20, 15, 8, 4]`
  - **Output:**
    ```text
    Mảng chẵn (4 phần tử): [12, 20, 8, 4]
    Mảng lẻ   (3 phần tử): [7, 9, 15]
    ```
- **Gợi ý tư duy:**
  - Lần duyệt 1: Đếm số lượng phần tử chẵn `countEven`, suy ra `countOdd = arr.length - countEven`.
  - Khởi tạo 2 mảng: `new int[countEven]` và `new int[countOdd]`.
  - Lần duyệt 2: Điền các giá trị vào mảng tương ứng với 2 biến index riêng biệt.

---

#### Bài 11: Kiểm tra mảng đối xứng (Palindrome Array)
- **Yêu cầu:** Một mảng được gọi là đối xứng (Palindrome) nếu đọc từ trái sang phải cũng giống hệt như đọc từ phải sang trái. Hãy viết chương trình kiểm tra xem mảng nhập vào có phải mảng đối xứng hay không.
- **Ví dụ:**
  - **Input 1:** `[1, 2, 3, 2, 1]` $\rightarrow$ **Output:** `Mảng đối xứng`
  - **Input 2:** `[1, 2, 3, 4, 5]` $\rightarrow$ **Output:** `Mảng KHÔNG đối xứng`
- **Gợi ý tư duy:**
  - Chạy vòng lặp từ `i = 0` đến `arr.length / 2`.
  - So sánh `arr[i]` với phần tử đối xứng ở cuối `arr[arr.length - 1 - i]`. Nếu chỉ cần có 1 cặp khác nhau $\rightarrow$ kết luận ngay là không đối xứng (`break`).

---

#### Bài 12: Đếm tần suất xuất hiện của từng phần tử
- **Yêu cầu:** Nhập một mảng số nguyên. Thống kê xem mỗi giá trị khác nhau trong mảng xuất hiện bao nhiêu lần. Đảm bảo mỗi giá trị chỉ được in kết quả thống kê đúng 1 lần duy nhất.
- **Ví dụ:**
  - **Input:** Mảng = `[2, 3, 2, 5, 3, 2, 8]`
  - **Output:**
    ```text
    Giá trị 2 xuất hiện: 3 lần
    Giá trị 3 xuất hiện: 2 lần
    Giá trị 5 xuất hiện: 1 lần
    Giá trị 8 xuất hiện: 1 lần
    ```
- **Gợi ý tư duy:**
  - Tạo một mảng `boolean[] visited = new boolean[arr.length]` để đánh dấu các phần tử đã được đếm.
  - Với mỗi phần tử tại index `i`, nếu `visited[i] == true` thì bỏ qua (`continue`). Nếu chưa, đếm số lần xuất hiện ở các vị trí `j` phía sau và đánh dấu `visited[j] = true`.

---

#### Bài 13: Đếm số lượng phần tử lớn hơn giá trị trung bình của toàn mảng
- **Yêu cầu:** Nhập mảng gồm $N$ số nguyên. Hãy:
  1. Tính giá trị trung bình cộng ($Avg$) của toàn bộ mảng.
  2. Đếm và in ra tất cả các phần tử trong mảng có giá trị lớn hơn $Avg$.
- **Ví dụ:**
  - **Input:** Mảng = `[10, 20, 30, 40, 50]` (Giá trị trung bình = 30.0)
  - **Output:**
    ```text
    Giá trị trung bình: 30.00
    Có 2 phần tử lớn hơn trung bình: 40, 50
    ```
- **Gợi ý tư duy:**
  - Lần duyệt 1: Tính tổng toàn bộ mảng $\rightarrow$ tính `avg = (double) sum / arr.length`.
  - Lần duyệt 2: Duyệt lại mảng, so sánh `arr[i] > avg` để in ra và đếm số lượng.

---

#### Bài 14: Xoay vòng mảng sang trái K vị trí (Left Rotate by K)
- **Yêu cầu:** Cho một mảng $N$ phần tử và số nguyên dương $K$. Hãy xoay vòng các phần tử của mảng sang bên trái $K$ lần.
- **Ví dụ:**
  - **Input:** Mảng = `[1, 2, 3, 4, 5, 6, 7]`, $K = 2$
  - **Output:** Mảng sau khi xoay trái 2 vị trí: `[3, 4, 5, 6, 7, 1, 2]`
  - **Giải thích:** Xoay 1 lần: `[2, 3, 4, 5, 6, 7, 1]` $\rightarrow$ Xoay 2 lần: `[3, 4, 5, 6, 7, 1, 2]`.
- **Gợi ý tư duy:**
  - Chuẩn hóa $K = K \% N$.
  - Có thể tạo mảng mới và gán `newArr[i] = arr[(i + K) % N]` hoặc xoay mảng tại chỗ bằng cách đảo 3 đoạn (đảo `0 .. K-1`, đảo `K .. N-1`, đảo `0 .. N-1`).

---

#### Bài 15: Loại bỏ các phần tử trùng lặp (Giữ mảng duy nhất)
- **Yêu cầu:** Cho một mảng các số nguyên có thể chứa nhiều giá trị trùng lặp. Hãy tạo ra một mảng mới chỉ chứa các phần tử duy nhất xuất hiện lần đầu tiên, giữ nguyên thứ tự xuất hiện ban đầu.
- **Ví dụ:**
  - **Input:** Mảng ban đầu = `[4, 2, 4, 5, 2, 3, 1, 5, 9]`
  - **Output:** Mảng duy nhất = `[4, 2, 5, 3, 1, 9]`
- **Gợi ý tư duy:**
  - Dùng một mảng tạm `temp` cùng kích thước và biến `uniqueCount = 0`.
  - Với mỗi phần tử `arr[i]`, kiểm tra xem nó đã có trong `temp` (từ 0 đến `uniqueCount - 1`) chưa.
  - Nếu chưa có, thêm vào `temp[uniqueCount++]`. Cuối cùng dùng `Arrays.copyOf(temp, uniqueCount)` để lấy mảng kết quả vừa khít.

---

### III. PHẦN MẢNG 2 CHIỀU CƠ BẢN (Nắm Vững Bản Chất Ma Trận)

#### Bài 16 (Cơ bản): Nhập, in ma trận chữ nhật M x N và tính tổng các phần tử
- **Độ khó:** ⭐⭐ (Dễ - Khá)
- **Mục tiêu:** Nắm vững cú pháp khai báo mảng 2 chiều, 2 vòng lặp `for` lồng nhau để nhập và in dữ liệu dạng lưới ma trận.
- **Yêu cầu:** 
  1. Nhập vào số hàng $M$ và số cột $N$ ($M, N > 0$).
  2. Nhập các phần tử cho ma trận kích thước $M \times N$.
  3. In ma trận ra màn hình theo đúng định dạng dòng/cột (các số cách nhau bằng dấu tab `\t`, hết mỗi dòng thì xuống dòng).
  4. Tính và in ra tổng của tất cả các phần tử trong ma trận.
- **Ví dụ:**
  - **Input:** $M = 2, N = 3$, Các giá trị: `1 2 3 4 5 6`
  - **Output:**
    ```text
    --- Ma trận vừa nhập ---
    1	2	3	
    4	5	6	
    Tổng tất cả phần tử: 21
    ```
- **Gợi ý tư duy:**
  - Khai báo `int[][] matrix = new int[m][n];`.
  - Dùng 2 vòng lặp lồng nhau: vòng ngoài `for (int i = 0; i < m; i++)` duyệt dòng, vòng trong `for (int j = 0; j < n; j++)` duyệt cột.
  - Tích luỹ `sum += matrix[i][j]` khi duyệt qua từng ô.

---

#### Bài 17 (Cơ bản - Khá): Tính tổng từng dòng và tìm phần tử lớn nhất của ma trận
- **Độ khó:** ⭐⭐ (Dễ - Khá)
- **Mục tiêu:** Rèn luyện tư duy xử lý dữ liệu theo từng hàng độc lập và tìm cực trị trên không gian 2 chiều.
- **Yêu cầu:** Cho một ma trận số nguyên $M \times N$. Hãy:
  1. Tính và in ra tổng giá trị của các phần tử trên **từng dòng** (hàng $0, 1, \dots$).
  2. Tìm giá trị lớn nhất ($Max$) trong toàn bộ ma trận và in ra toạ độ `(hàng, cột)` nơi $Max$ xuất hiện.
- **Ví dụ:**
  - **Input:** Ma trận kích thước $3 \times 3$:
    ```text
    3   8   1
    12  5   19
    7   14  6
    ```
  - **Output:**
    ```text
    Tổng dòng 0: 12
    Tổng dòng 1: 36
    Tổng dòng 2: 27
    Phần tử lớn nhất là: 19 tại hàng 1, cột 2
    ```
- **Gợi ý tư duy:**
  - Với mỗi hàng `i`, đặt biến `sumRow = 0` ở đầu vòng lặp dòng ngoài, sau đó duyệt vòng lặp cột trong để cộng dồn.
  - Đặt `max = matrix[0][0]`, `maxRow = 0`, `maxCol = 0`. Duyệt qua từng ô `(i, j)`, nếu `matrix[i][j] > max` thì cập nhật `max`, `maxRow = i`, `maxCol = j`.

---

#### Bài 18 (Trọng tâm): Tính tổng đường chéo chính và đường chéo phụ của ma trận vuông
- **Độ khó:** ⭐⭐⭐ (Trung bình)
- **Mục tiêu:** Hiểu rõ mối liên hệ giữa các chỉ số index `(i, j)` trong ma trận vuông $N \times N$.
- **Yêu cầu:** Nhập ma trận vuông kích thước $N \times N$. Hãy:
  1. Tính tổng các phần tử nằm trên **đường chéo chính** (từ góc trên bên trái xuống góc dưới bên phải).
  2. Tính tổng các phần tử nằm trên **đường chéo phụ** (từ góc trên bên phải xuống góc dưới bên trái).
- **Ví dụ:**
  - **Input:** Ma trận vuông $3 \times 3$:
    ```text
    1   2   3
    4   5   6
    7   8   9
    ```
  - **Output:**
    ```text
    Tổng đường chéo chính: 15 (1 + 5 + 9)
    Tổng đường chéo phụ:   15 (3 + 5 + 7)
    ```
- **Gợi ý tư duy:**
  - **Đường chéo chính:** Các phần tử có chỉ số dòng bằng chỉ số cột $\rightarrow$ `matrix[i][i]`.
  - **Đường chéo phụ:** Các phần tử thỏa mãn chỉ số cột $j = N - 1 - i \rightarrow$ `matrix[i][n - 1 - i]`.
  - Chỉ cần **1 vòng lặp duy nhất** `for (int i = 0; i < n; i++)` để tính cả hai đường chéo cùng lúc!
