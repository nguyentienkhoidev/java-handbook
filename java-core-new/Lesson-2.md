Dưới đây là phần lý thuyết tổng hợp chi tiết cho các chủ đề bạn yêu cầu, được trình bày một cách logic và dễ hiểu nhất.

---

### 1. Giới thiệu về Java
Java là một ngôn ngữ lập trình hướng đối tượng (OOP), đa mục đích, được thiết kế với triết lý **"Write Once, Run Anywhere"** (Viết một lần, chạy mọi nơi). Nghĩa là code Java sau khi viết trên một hệ điều hành (như Windows) có thể chạy trên bất kỳ hệ điều hành nào khác (như macOS, Linux) mà không cần phải viết lại, miễn là máy đó có cài đặt JVM.

### 2. JDK, JRE, JVM là gì?
Đây là bộ 3 khái niệm cốt lõi của môi trường Java:

*   **JVM (Java Virtual Machine - Máy ảo Java):** Là một môi trường ảo để thực thi mã trung gian bytecode. Mỗi hệ điều hành sẽ có một phiên bản JVM riêng để dịch bytecode thành mã máy phù hợp với hệ điều hành đó.
*   **JRE (Java Runtime Environment - Môi trường thực thi Java):** Cung cấp môi trường để **chạy** các chương trình Java. Nó bao gồm **JVM** và các thư viện cốt lõi (Core Libraries) cần thiết. Nếu bạn chỉ muốn *chạy* app Java, bạn chỉ cần cài JRE.
*   **JDK (Java Development Kit - Bộ công cụ phát triển Java):** Là bộ công cụ dành cho lập trình viên để **phát triển** các ứng dụng Java. Nó bao gồm **JRE** (để chạy code) và các công cụ phát triển như trình biên dịch (`javac`), trình gỡ lỗi (`jdb`), tài liệu, v.v.

**Tóm tắt mối quan hệ:** `JDK = JRE + Development Tools` | `JRE = JVM + Libraries`

### 3. Cơ chế từ code Java ra mã máy
Không giống như C/C++ biên dịch trực tiếp ra mã máy (Machine Code) phụ thuộc vào phần cứng, Java sử dụng quy trình 2 bước để đảm bảo tính đa nền tảng:

```text
 Mã nguồn Java (.java)
          │
          ▼  (1) Trình biên dịch javac (Compiler)
          │
 Bytecode (.class)   <-- Đây là mã trung gian, con người không đọc được, máy tính chưa chạy được.
          │
          ▼  (2) JVM (Máy ảo Java) đọc và thông dịch/biên dịch JIT
          │
    Machine Code     <-- Mã máy 0 và 1 cụ thể cho từng loại CPU (Windows/Mac/Linux)
          │
          ▼  (3) Đưa vào CPU xử lý
         CPU
```

### 4. Cơ chế hoạt động của Phần cứng (Disk, RAM, Cache, CPU) khi chạy 1 chương trình
Khi bạn click đúp để chạy một ứng dụng (VD: Word, Game, hay App Java):

1.  **Disk (Ổ cứng - HDD/SSD):** App đang nằm ngủ yên ở đây. Tốc độ ổ cứng rất chậm nhưng dung lượng rất lớn.
2.  **RAM (Bộ nhớ trong):** Hệ điều hành sẽ *copy* mã lệnh và dữ liệu của App từ Ổ cứng đẩy lên RAM. RAM có tốc độ nhanh hơn Disk hàng ngàn lần.
3.  **CPU Cache (L3, L2, L1):** CPU quá nhanh, nếu nó cứ chờ lấy dữ liệu trực tiếp từ RAM thì sẽ bị "nghẽn cổ chai". Do đó, CPU có bộ nhớ đệm (Cache) nằm ngay bên trong nó:
    *   **L3 Cache:** Chia sẻ chung cho tất cả các Core của CPU. Kéo dữ liệu từ RAM vào đây.
    *   **L2 Cache:** Nằm riêng biệt cho từng Core, nhanh hơn L3. Lấy dữ liệu từ L3.
    *   **L1 Cache:** Nằm sâu nhất, nhỏ nhất, cực kỳ nhanh, chia làm L1 Instruction (chứa lệnh) và L1 Data (chứa dữ liệu).
4.  **CPU (Central Processing Unit):** Core của CPU sẽ lấy lệnh trực tiếp từ L1 Cache, giải mã và thực thi tính toán ngay lập tức.

### 5. Tại sao CPU chỉ có 2 Core mà có thể chạy đồng thời hàng chục App? Thread hoạt động ra sao?
*   **Thread (Luồng):** Là đơn vị thực thi nhỏ nhất của một chương trình. Một App khi chạy (gọi là 1 Process/Tiến trình) có thể có một hoặc nhiều luồng chạy song song (Multithreading) để làm nhiều việc cùng lúc (ví dụ tải file và cập nhật giao diện).
*   **Context Switching (Chuyển ngữ cảnh) & Time Slicing (Chia sẻ thời gian):**
    *   Thực tế, tại một thời điểm chính xác (nano giây), 1 Core của CPU **chỉ xử lý được duy nhất 1 Thread**. Một CPU 2 Core thực chất chỉ chạy được 2 Thread cùng lúc.
    *   Tuy nhiên, Hệ điều hành sử dụng thuật toán **Time Slicing**. Nó băm nhỏ thời gian (ví dụ 10 mili-giây) và phân phát cho hàng chục Thread.
    *   CPU Core sẽ thực thi App A trong 10ms -> Tạm dừng (lưu trạng thái - Context Switch) -> Chuyển sang thực thi App B 10ms -> Chuyển sang App C 10ms... Việc chuyển đổi này xảy ra **hàng ngàn lần mỗi giây**, tạo ra *ảo giác* đối với mắt người là tất cả các App đang chạy đồng thời cùng một lúc (Concurrency).

---

### 6. Khai báo biến, Các loại biến trong Java
Biến (Variable) là một vùng nhớ trong RAM được đặt tên, dùng để lưu trữ dữ liệu. Cú pháp: `Kiểu_dữ_liệu Tên_biến = Giá_trị;`

Các loại biến theo vị trí khai báo (Phạm vi):
1.  **Local Variable (Biến cục bộ):** Được khai báo *bên trong* một phương thức (method), block hoặc hàm tạo. Nó chỉ tồn tại khi phương thức được gọi và bị hủy khi phương thức kết thúc. *Bắt buộc phải khởi tạo giá trị trước khi dùng*.
2.  **Instance Variable (Biến thể hiện/Biến đối tượng):** Được khai báo *bên trong* Class nhưng *bên ngoài* mọi phương thức. Mỗi đối tượng (Object) tạo ra sẽ có một bản sao riêng của biến này.
3.  **Static Variable (Biến tĩnh):** Được khai báo với từ khóa `static`. Nó thuộc về **Class** chứ không thuộc về Object. Tất cả các đối tượng của Class sẽ dùng chung một vùng nhớ của biến static này.
4.  **Reference Variable (Biến tham chiếu):** Không lưu trực tiếp giá trị thực, mà lưu **địa chỉ bộ nhớ** (thẻ trỏ) chỉ tới nơi đối tượng thực sự được lưu trên bộ nhớ Heap (VD: String, Object, Array).
5.  **Constant Variable (Hằng số):** Sử dụng từ khóa `final`. Giá trị của biến này không thể thay đổi sau khi đã được khởi tạo (VD: `final double PI = 3.14;`).

### 7. Các kiểu biến nguyên thuỷ (Primitive Types)
Java có 8 kiểu nguyên thủy, lưu trữ giá trị trực tiếp trên bộ nhớ Stack:
*   **Số nguyên:** `byte` (1 byte), `short` (2 bytes), `int` (4 bytes - mặc định), `long` (8 bytes).
*   **Số thực:** `float` (4 bytes), `double` (8 bytes - mặc định).
*   **Ký tự:** `char` (2 bytes - lưu bảng mã Unicode).
*   **Logic:** `boolean` (chỉ có true / false).

### 8. Cơ chế ép kiểu (Type Casting)
Ép kiểu là chuyển đổi giá trị từ kiểu dữ liệu này sang kiểu dữ liệu khác.
*   **Widening Casting (Ép kiểu ngầm định - Tự động):** Chuyển từ kiểu có kích thước nhỏ sang kiểu có kích thước lớn hơn (không mất dữ liệu).
    *   `byte -> short -> char -> int -> long -> float -> double`
    *   *Ví dụ:* `int a = 10; double b = a; // b sẽ là 10.0`
*   **Narrowing Casting (Ép kiểu tường minh - Thủ công):** Chuyển từ kiểu có kích thước lớn sang kiểu nhỏ hơn (có nguy cơ mất mát dữ liệu). Bắt buộc phải đặt tên kiểu dữ liệu trong dấu `()`.
    *   *Ví dụ:* `double c = 9.78; int d = (int) c; // d sẽ là 9 (phần thập phân bị cắt bỏ)`

### 9. Bài Tập Thực Hành

**Bài 1: Khai báo biến và Ép kiểu**
Viết chương trình Java, thực hiện các yêu cầu sau:
1. Khai báo 1 biến `int` chứa năm sinh của bạn.
2. Khai báo 1 hằng số (`final double`) tỷ giá USD sang VND (ví dụ 25400.5).
3. Khai báo 1 số tiền USD kiểu `int` (ví dụ 50).
4. Tính ra số tiền VND (USD * tỷ giá). Lúc này kết quả sẽ là kiểu `double`. Hãy thực hiện **ép kiểu tường minh (narrowing)** kết quả đó về kiểu `long` để hiển thị tiền Việt không có phần thập phân.

**Bài 2: Phân biệt Local Variable và Instance Variable**
Hãy viết một class `Car`.
1. Bên trong class, khai báo một *instance variable* là `String brand`.
2. Viết một hàm `startEngine()`, bên trong hàm này khai báo một *local variable* `boolean isRunning = true`.
3. Cố gắng in biến `brand` ở trong hàm `startEngine()`, và cố gắng in biến `isRunning` ở một hàm khác. Ghi lại kết quả lỗi của trình biên dịch (để hiểu scope/phạm vi của local biến).