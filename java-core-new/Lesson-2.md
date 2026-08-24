# 📘 Bài 2: Môi trường Java, Hoạt động phần cứng & Biến (Variables)

---

## 📑 Mục Lục
- [1️⃣ Giới thiệu về Java](#1️⃣-giới-thiệu-về-java)
- [2️⃣ JDK, JRE, JVM là gì?](#2️⃣-jdk-jre-jvm-là-gì)
- [3️⃣ Cơ chế biên dịch (Từ Code ra Mã máy)](#3️⃣-cơ-chế-biên-dịch-từ-code-ra-mã-máy)
- [4️⃣ Hoạt động của Phần cứng (Disk, RAM, CPU)](#4️⃣-hoạt-động-của-phần-cứng-disk-ram-cpu)
- [5️⃣ Thread và Đa luồng cơ bản](#5️⃣-thread-và-đa-luồng-cơ-bản)
- [6️⃣ Khai báo biến và Phạm vi (Scope)](#6️⃣-khai-báo-biến-và-phạm-vi-scope)
- [7️⃣ Các kiểu dữ liệu nguyên thuỷ (Primitive Types)](#7️⃣-các-kiểu-dữ-liệu-nguyên-thuỷ)
- [8️⃣ Cơ chế ép kiểu (Type Casting)](#8️⃣-cơ-chế-ép-kiểu-type-casting)
- [📝 KHO BÀI TẬP THỰC HÀNH](#-kho-bài-tập-thực-hành)
  - [I. Bài Tập Cơ Bản](#i-bài-tập-cơ-bản)
  - [II. Thử thách Master](#ii-thử-thách-master)

---

## 1️⃣ Giới thiệu về Java
Java là một ngôn ngữ lập trình hướng đối tượng (OOP), đa mục đích, được thiết kế với triết lý **"Write Once, Run Anywhere"** (Viết một lần, chạy mọi nơi). 
Nghĩa là code Java sau khi viết trên một hệ điều hành (như Windows) có thể chạy trên bất kỳ hệ điều hành nào khác (như macOS, Linux) mà không cần phải viết lại, miễn là máy đó có cài đặt JVM.

## 2️⃣ JDK, JRE, JVM là gì?
Đây là bộ 3 khái niệm cốt lõi của môi trường Java:

*   **JVM (Java Virtual Machine - Máy ảo Java):** Là một môi trường ảo để thực thi mã trung gian bytecode. Mỗi hệ điều hành sẽ có một phiên bản JVM riêng để dịch bytecode thành mã máy phù hợp với hệ điều hành đó.
*   **JRE (Java Runtime Environment - Môi trường thực thi Java):** Cung cấp môi trường để **chạy** các chương trình Java. Nó bao gồm **JVM** và các thư viện cốt lõi (Core Libraries) cần thiết.
*   **JDK (Java Development Kit - Bộ công cụ phát triển Java):** Là bộ công cụ dành cho lập trình viên để **phát triển** các ứng dụng Java. Nó bao gồm **JRE** (để chạy code) và các công cụ phát triển như trình biên dịch (`javac`), trình gỡ lỗi (`jdb`), tài liệu, v.v.

> **💡 TIP (Mẹo ghi nhớ):**
> *   Chỉ muốn chơi Game Java? Cài **JRE**.
> *   Muốn tự viết ra Game Java? Cài **JDK**.
> *   `JDK = JRE + Development Tools` | `JRE = JVM + Libraries`

## 3️⃣ Cơ chế biên dịch (Từ Code ra Mã máy)
Không giống như C/C++ biên dịch trực tiếp ra mã máy (Machine Code) phụ thuộc vào phần cứng, Java sử dụng quy trình 2 bước để đảm bảo tính đa nền tảng:

```text
 Mã nguồn Java (.java)
          │
          ▼  (1) Trình biên dịch javac (Compiler)
          │
 Bytecode (.class)   <-- Đây là mã trung gian, con người không đọc được, máy chưa chạy được.
          │
          ▼  (2) JVM (Máy ảo Java) đọc và thông dịch/biên dịch JIT
          │
    Machine Code     <-- Mã máy 0 và 1 cụ thể cho từng loại CPU (Windows/Mac/Linux)
          │
          ▼  (3) Đưa vào CPU xử lý
         CPU
```

## 4️⃣ Hoạt động của Phần cứng (Disk, RAM, CPU)
Khi bạn click đúp để chạy một ứng dụng (VD: Word, Game, hay App Java):

1.  **Disk (Ổ cứng - HDD/SSD):** App đang nằm ngủ yên ở đây. Tốc độ ổ cứng rất chậm nhưng dung lượng rất lớn.
2.  **RAM (Bộ nhớ trong):** Hệ điều hành sẽ *copy* mã lệnh và dữ liệu của App từ Ổ cứng đẩy lên RAM. RAM có tốc độ nhanh hơn Disk hàng ngàn lần.
3.  **CPU Cache (L3, L2, L1):** CPU quá nhanh, nếu nó cứ chờ lấy dữ liệu trực tiếp từ RAM thì sẽ bị "nghẽn cổ chai". Do đó, CPU có bộ nhớ đệm (Cache) nằm ngay bên trong nó:
    *   **L3 Cache:** Chia sẻ chung cho tất cả các Core. Kéo dữ liệu từ RAM vào đây.
    *   **L2 Cache:** Nằm riêng biệt cho từng Core, nhanh hơn L3. Lấy dữ liệu từ L3.
    *   **L1 Cache:** Nằm sâu nhất, nhỏ nhất, cực kỳ nhanh.
4.  **CPU (Central Processing Unit):** Core của CPU lấy lệnh từ L1 Cache và tính toán ngay lập tức.

## 5️⃣ Thread và Đa luồng cơ bản
*   **Thread (Luồng):** Là đơn vị thực thi nhỏ nhất của một chương trình. Một App khi chạy có thể có nhiều luồng (Multithreading) để làm nhiều việc cùng lúc.
*   **Context Switching (Chuyển ngữ cảnh) & Time Slicing (Chia sẻ thời gian):**
    *   Tại một thời điểm chính xác (nano giây), 1 Core của CPU **chỉ xử lý được duy nhất 1 Thread**.
    *   Hệ điều hành băm nhỏ thời gian (ví dụ 10 mili-giây) và phân phát cho hàng chục Thread.
    *   CPU Core sẽ đổi qua lại cực nhanh giữa các App, tạo ra *ảo giác* đối với mắt người là tất cả các App đang chạy đồng thời cùng một lúc.

## 6️⃣ Khai báo biến và Phạm vi (Scope)
Biến (Variable) là một vùng nhớ trong RAM được đặt tên, dùng để lưu trữ dữ liệu. 
Cú pháp: `Kiểu_dữ_liệu Tên_biến = Giá_trị;`

Các loại biến theo vị trí khai báo:
1.  **Local Variable (Biến cục bộ):** Khai báo *bên trong* một phương thức (method) hoặc khối lệnh `{}`. Chết khi ra khỏi khối lệnh đó. *Bắt buộc khởi tạo trước khi dùng*.
2.  **Instance Variable (Biến thể hiện):** Khai báo *bên trong* Class nhưng ngoài phương thức. Mỗi đối tượng tạo ra sẽ có bản sao riêng.
3.  **Static Variable (Biến tĩnh):** Dùng từ khóa `static`. Thuộc về **Class**, tất cả đối tượng dùng chung 1 biến này.
4.  **Reference Variable (Biến tham chiếu):** Lưu **địa chỉ bộ nhớ** chỉ tới nơi đối tượng được lưu trên Heap (VD: String, Object).
5.  **Constant Variable (Hằng số):** Sử dụng từ khóa `final`. Không thể thay đổi giá trị.

## 7️⃣ Các kiểu dữ liệu nguyên thuỷ (Primitive Types)
Java có 8 kiểu nguyên thủy, lưu trữ giá trị trực tiếp trên bộ nhớ Stack:
*   **Số nguyên:** `byte` (1 byte), `short` (2 bytes), `int` (4 bytes - mặc định), `long` (8 bytes).
*   **Số thực:** `float` (4 bytes), `double` (8 bytes - mặc định).
*   **Ký tự:** `char` (2 bytes - lưu bảng mã Unicode).
*   **Logic:** `boolean` (chỉ có true / false).

## 8️⃣ Cơ chế ép kiểu (Type Casting)
Ép kiểu là chuyển đổi giá trị từ kiểu dữ liệu này sang kiểu dữ liệu khác.
*   **Widening Casting (Ép kiểu ngầm định - Tự động):** Chuyển từ kiểu nhỏ sang kiểu lớn (an toàn).
    *   `byte -> short -> char -> int -> long -> float -> double`
    *   *Ví dụ:* `int a = 10; double b = a; // b sẽ là 10.0`
*   **Narrowing Casting (Ép kiểu tường minh - Thủ công):** Chuyển từ kiểu lớn sang kiểu nhỏ (nguy cơ mất dữ liệu). Bắt buộc phải đặt tên kiểu dữ liệu trong dấu `()`.
    *   *Ví dụ:* `double c = 9.78; int d = (int) c; // d sẽ là 9 (phần thập phân bị cắt)`

> **⚠️ CẢNH BÁO:**
> Khi ép kiểu hẹp (Narrowing) từ số thực về số nguyên, phần thập phân sẽ bị vứt bỏ hoàn toàn chứ KHÔNG bị làm tròn. (9.99 ép về int sẽ ra 9).

---
---

## 📝 KHO BÀI TẬP THỰC HÀNH

### I. Bài Tập Cơ Bản

1. **Bài 1 (Khai báo biến và Ép kiểu):**
   - Khai báo 1 biến `int` chứa năm sinh của bạn.
   - Khai báo 1 hằng số (`final double`) tỷ giá USD sang VND (ví dụ 25400.5).
   - Khai báo 1 số tiền USD kiểu `int` (ví dụ 50).
   - Tính ra số tiền VND (USD * tỷ giá). Lúc này kết quả sẽ là kiểu `double`. Hãy thực hiện **ép kiểu tường minh (narrowing)** kết quả đó về kiểu `long` để hiển thị tiền Việt không có phần thập phân.

2. **Bài 2 (Phân biệt Local Variable và Instance Variable):**
   - Viết một class tên là `Car`. Bên trong class, khai báo một *instance variable* là `String brand`.
   - Viết một hàm `startEngine()`, bên trong hàm này khai báo một *local variable* `boolean isRunning = true`.
   - Cố gắng in biến `brand` ở trong hàm `startEngine()`, và cố gắng in biến `isRunning` ở bên ngoài hàm đó. Ghi lại kết quả lỗi để hiểu rõ Scope.

3. **Bài 3 (Toán học với kích thước bộ nhớ):**
   - Khai báo 1 biến `byte` có giá trị là 100.
   - Nhân biến đó lên 3 lần và thử gán ngược lại vào biến `byte`. Bạn có nhận được lỗi biên dịch không? Nếu có, hãy dùng ép kiểu tường minh để sửa lỗi và phân tích xem kết quả in ra là gì (Hiện tượng tràn bộ nhớ - Overflow).

### 🔥 II. Thử thách Master

4. **Bài 4 (Hệ thống tính tiền vé máy bay):**
   Xây dựng hệ thống tính tiền vé máy bay. 
   - Hằng số: Phí sân bay `final int AIRPORT_FEE = 150000;`.
   - Giá vé gốc là một biến `double basePrice = 2500500.75;`.
   - Viết code cộng giá vé gốc với phí sân bay. Sau đó, ép kiểu tổng tiền về kiểu `long` để hệ thống ngân hàng có thể trừ tiền chính xác (ngân hàng không làm việc với tiền lẻ thập phân). In ra cả giá chưa ép kiểu và giá đã ép kiểu.

5. **Bài 5 (Mô phỏng bộ đệm RAM):**
   Hãy tạo ra 1 đoạn mã (mô phỏng) giải thích lại quy trình mà biến của bạn đi từ Ổ cứng -> RAM -> L3/L2/L1 Cache -> CPU bằng cách sử dụng các dòng lệnh `System.out.println()`. Bài tập này giúp bạn thuộc lòng quy trình làm việc của máy tính.