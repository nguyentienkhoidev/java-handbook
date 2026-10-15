# 🎓 BÀI TỔNG KẾT TOÀN DIỆN KIẾN THỨC JAVA CORE (LESSON 2 - 18)
> **Cẩm nang chuyên sâu: Hệ thống hóa toàn bộ kiến thức, giải mã bản chất cơ chế máy ảo JVM, kèm kỹ thuật "Văn mẫu phỏng vấn" giúp bạn diễn đạt tự tin, gãy gọn và 40 câu hỏi phỏng vấn hay gặp nhất.**

---

## 📑 MỤC LỤC TỔNG QUAN

- [HƯỚNG DẪN: CÁCH SỬ DỤNG TÀI LIỆU ĐỂ ÔN TẬP & LUYỆN NÓI](#hướng-dẫn-cách-sử-dụng-tài-liệu-để-ôn-tập--luyện-nói)
- [CHƯƠNG 1: NỀN TẢNG MÁY TÍNH & CÚ PHÁP CƠ SỞ (LESSON 2 - 6)](#chương-1-nền-tảng-máy-tính--cú-pháp-cơ-sở-lesson-2---6)
  - [1.1 Triết lý Java & Bộ ba JDK - JRE - JVM](#11-triết-lý-java--bộ-ba-jdk---jre---jvm)
  - [1.2 Quy trình biên dịch và thực thi 2 bước (Bytecode & JIT)](#12-quy-trình-biên-dịch-và-thực-thi-2-bước-bytecode--jit)
  - [1.3 Hoạt động phần cứng: Disk, RAM, CPU Cache & Registers](#13-hoạt-động-phần-cứng-disk-ram-cpu-cache--registers)
  - [1.4 Hệ thống Kiểu dữ liệu nguyên thủy (Primitive Types) & Ép kiểu (Casting)](#14-hệ-thống-kiểu-dữ-liệu-nguyên-thủy-primitive-types--ép-kiểu-casting)
  - [1.5 Quy tắc đặt tên chuẩn & Hằng số (final)](#15-quy-tắc-đặt-tên-chuẩn--hằng-số-final)
  - [1.6 Hệ thống Toán tử & Bản chất Tiền tố / Hậu tố (++a vs a++)](#16-hệ-thống-toán-tử--bản-chất-tiền-tố--hậu-tố-a-vs-a)
  - [1.7 Cấu trúc rẽ nhánh (if-else & switch-case cải tiến)](#17-cấu-trúc-rẽ-nhánh-if-else--switch-case-cải-tiến)
  - [1.8 Cấu trúc vòng lặp & Từ khóa điều khiển luồng (break, continue, return)](#18-cấu-trúc-vòng-lặp--từ-khóa-điều-khiển-luồng-break-continue-return)
  - [1.9 Phương thức (Method) & Nạp chồng phương thức (Overloading)](#19-phương-thức-method--nạp-chồng-phương-thức-overloading)
- [CHƯƠNG 2: CẤU TRÚC DỮ LIỆU MẢNG (ARRAY) (LESSON 7)](#chương-2-cấu-trúc-dữ-liệu-mảng-array-lesson-7)
  - [2.1 Bản chất mảng trong Java: Luôn là Object trên Heap](#21-bản-chất-mảng-trong-java-luôn-là-object-trên-heap)
  - [2.2 Mảng 1 chiều & Cạm bẫy ArrayIndexOutOfBoundsException](#22-mảng-1-chiều--cạm-bẫy-arrayindexoutofboundsexception)
  - [2.3 Thư viện java.util.Arrays & Phân biệt Shallow Copy vs Deep Copy](#23-thư-viện-javautilarrays--phân-biệt-shallow-copy-vs-deep-copy)
  - [2.4 Mảng 2 chiều & Mảng răng cưa (Jagged Array)](#24-mảng-2-chiều--mảng-răng-cưa-jagged-array)
- [CHƯƠNG 3: LẬP TRÌNH HƯỚNG ĐỐI TƯỢNG (OOP) TOÀN DIỆN (LESSON 9, 10, 11, 12, 14)](#chương-3-lập-trình-hướng-đối-tượng-oop-toàn-diện-lesson-9-10-11-12-14)
  - [3.1 Tư duy OOP: State, Behavior, Identity & Class vs Object](#31-tư-duy-oop-state-behavior-identity--class-vs-object)
  - [3.2 Giải phẫu Bộ nhớ JVM: Metaspace, Heap, Stack Frame & GC](#32-giải-phẫu-bộ-nhớ-jvm-metaspace-heap-stack-frame--gc)
  - [3.3 Từ khóa new, Constructor & Từ khóa this](#33-từ-khóa-new-constructor--từ-khóa-this)
  - [3.4 Từ khóa static: Biến static, Hàm static & Khối static block](#34-từ-khóa-static-biến-static-hàm-static--khối-static-block)
  - [3.5 Cơ chế Pass-By-Value tuyệt đối trong Java (Chứng minh cốt lõi)](#35-cơ-chế-pass-by-value-tuyệt-đối-trong-java-chứng-minh-cốt-lõi)
  - [3.6 Quan hệ Composition ("Has-A")](#36-quan-hệ-composition-has-a)
  - [3.7 Trụ cột 1: Tính Đóng Gói (Encapsulation) & Access Modifiers](#37-trụ-cột-1-tính-đóng-gói-encapsulation--access-modifiers)
  - [3.8 Trụ cột 2: Tính Kế Thừa (Inheritance) & Từ khóa super](#38-trụ-cột-2-tính-kế-thừa-inheritance--từ-khóa-super)
  - [3.9 Trụ cột 3: Tính Đa Hình (Polymorphism): Overloading vs Overriding & Upcasting/Downcasting](#39-trụ-cột-3-tính-đa-hình-polymorphism-overloading-vs-overriding--upcastingdowncasting)
  - [3.10 Trụ cột 4: Tính Trừu Tượng (Abstraction): Abstract Class vs Interface](#310-trụ-cột-4-tính-trừu-tượng-abstraction-abstract-class-vs-interface)
  - [3.11 Hợp đồng (Contract) giữa equals() và hashCode() trong Object](#311-hợp-đồng-contract-giữa-equals-và-hashcode-trong-object)
  - [3.12 Độ phức tạp thuật toán (Big O) & Bản chất ArrayList](#312-độ-phức-tạp-thuật-toán-big-o--bản-chất-arraylist)
- [CHƯƠNG 4: XỬ LÝ CHUỖI, NGOẠI LỆ & GENERICS (LESSON 15 - 16)](#chương-4-xử-lý-chuỗi-ngoại-lệ--generics-lesson-15---16)
  - [4.1 String: Tính Bất Biến (Immutable) & String Pool](#41-string-tính-bất-biến-immutable--string-pool)
  - [4.2 StringBuilder vs StringBuffer vs StringJoiner](#42-stringbuilder-vs-stringbuffer-vs-stringjoiner)
  - [4.3 Wrapper Class, Autoboxing/Unboxing & Cạm bẫy Integer Cache](#43-wrapper-class-autoboxingunboxing--cạm-bẫy-integer-cache)
  - [4.4 Iterable, Iterator & Lỗi ConcurrentModificationException (Fail-Fast)](#44-iterable-iterator--lỗi-concurrentmodificationexception-fail-fast)
  - [4.5 Xử lý Ngoại lệ: Checked vs Unchecked, try-catch-finally, try-with-resources](#45-xử-lý-ngoại-lệ-checked-vs-unchecked-try-catch-finally-try-with-resources)
  - [4.6 Phân biệt bộ ba kinh điển: final - finally - finalize](#46-phân-biệt-bộ-ba-kinh-điển-final---finally---finalize)
  - [4.7 Generics: Tham số hóa kiểu, Wildcards (?, extends, super) & Nguyên lý PECS](#47-generics-tham-số-hóa-kiểu-wildcards--extends-super--nguyên-lý-pecs)
  - [4.8 Sắp xếp đối tượng: Comparable vs Comparator](#48-sắp-xếp-đối-tượng-comparable-vs-comparator)
- [CHƯƠNG 5: HỆ SINH THÁI JAVA 8+ HIỆN ĐẠI (LESSON 18)](#chương-5-hệ-sinh-thái-java-8-hiện-đại-lesson-18)
  - [5.1 Biểu thức chính quy (Regex - Regular Expressions)](#51-biểu-thức-chính-quy-regex---regular-expressions)
  - [5.2 Modern Date/Time API (java.time)](#52-modern-datetime-api-javatime)
  - [5.3 Default Method & Static Method trong Interface](#53-default-method--static-method-trong-interface)
  - [5.4 Functional Interface & Bộ tứ cốt lõi: Predicate, Function, Consumer, Supplier](#54-functional-interface--bộ-tứ-cốt-lõi-predicate-function-consumer-supplier)
  - [5.5 Lambda Expression & Method Reference (::)](#55-lambda-expression--method-reference-)
  - [5.6 Stream API: Đường ống xử lý dữ liệu khai báo & Lazy Evaluation](#56-stream-api-đường-ống-xử-lý-dữ-liệu-khai-báo--lazy-evaluation)
  - [5.7 Optional: Khai tử NullPointerException](#57-optional-khai-tử-nullpointerexception)
- [CHƯƠNG 6: BẢNG TRA CỨU NHANH & TOP CẠM BẪY SÁT THỦ](#chương-6-bảng-tra-cứu-nhanh--top-cạm-bẫy-sát-thủ)
  - [6.1 Bảng ma trận so sánh các khái niệm cốt lõi](#61-bảng-ma-trận-so-sánh-các-khái-niệm-cốt-lõi)
  - [6.2 Top 10 cạm bẫy Java Core người mới hay mắc phải](#62-top-10-cạm-bẫy-java-core-người-mới-hay-mắc-phải)
- [CHƯƠNG 7: KHO 40 CÂU HỎI PHỎNG VẤN JAVA HAY GẶP NHẤT (KÈM LỜI THOẠI MẪU GHI ĐIỂM)](#chương-7-kho-40-câu-hỏi-phỏng-vấn-java-hay-gặp-nhất-kèm-lời-thoại-mẫu-ghi-điểm)
  - [Nhóm 1: Cú pháp cơ sở, Môi trường & Bộ nhớ (Câu 1 - 8)](#nhóm-1-cú-pháp-cơ-sở-môi-trường--bộ-nhớ-câu-1---8)
  - [Nhóm 2: Mảng & Cấu trúc Dữ liệu cơ sở (Câu 9 - 12)](#nhóm-2-mảng--cấu-trúc-dữ-liệu-cơ-sở-câu-9---12)
  - [Nhóm 3: Lập trình Hướng đối tượng OOP & Quản lý Bộ nhớ (Câu 13 - 23)](#nhóm-3-lập-trình-hướng-đối-tượng-oop--quản-lý-bộ-nhớ-câu-13---23)
  - [Nhóm 4: Xử lý Chuỗi, Wrapper Class & Ngoại lệ (Câu 24 - 32)](#nhóm-4-xử-lý-chuỗi-wrapper-class--ngoại-lệ-câu-24---32)
  - [Nhóm 5: Generics, Java 8+ & Sắp xếp Collection (Câu 33 - 40)](#nhóm-5-generics-java-8--sắp-xếp-collection-câu-33---40)

---

# HƯỚNG DẪN: CÁCH SỬ DỤNG TÀI LIỆU ĐỂ ÔN TẬP & LUYỆN NÓI

Tài liệu này được biên soạn nhằm giải quyết 2 khó khăn lớn nhất của lập trình viên: **"Hiểu code nhưng không biết giải thích"** và **"Bị nhà tuyển dụng hỏi xoáy vào bản chất JVM thì ấp úng"**.

1.  **Đọc hiểu bản chất:** Xem các mô hình ASCII và bảng so sánh trực quan để hiểu rõ cơ chế bên dưới RAM/JVM.
2.  **Luyện nói theo mục `🗣️ Văn mẫu phỏng vấn`:** Mỗi chủ đề đều có khung câu trả lời ngắn gọn (30s - 1 phút). Hãy đọc to bằng lời, tập nói trước gương hoặc ghi âm lại giọng nói của mình để tạo phản xạ giao tiếp tự tin.
3.  **Thực chiến ở Chương 7:** Ôn tập toàn bộ **40 câu hỏi phỏng vấn hay gặp nhất**. Mỗi câu hỏi đã được chắt lọc gồm **Ý chính**, **Lời thoại tự tin**, và **Cạm bẫy cần né**.

---

# CHƯƠNG 1: NỀN TẢNG MÁY TÍNH & CÚ PHÁP CƠ SỞ (LESSON 2 - 6)

## 1.1 Triết lý Java & Bộ ba JDK - JRE - JVM

Java ra đời với tôn chỉ nổi tiếng: **"Write Once, Run Anywhere" (WORA)** (Viết một lần, chạy mọi nơi). Để hiện thực hóa triết lý này trên mọi hệ điều hành (Windows, macOS, Linux), hệ sinh thái Java phân tách thành 3 tầng:

```text
+-------------------------------------------------------------------------------+
| JDK (Java Development Kit) - Bộ công cụ dành cho Lập trình viên                |
|  - Trình biên dịch: javac (biên dịch .java thành .class)                      |
|  - Công cụ gỡ lỗi & giám sát: jdb, jconsole, jps, javadoc...                  |
|  +-------------------------------------------------------------------------+  |
|  | JRE (Java Runtime Environment) - Môi trường chỉ để Chạy chương trình     |  |
|  |  - Core Libraries: java.lang, java.util, java.io...                     |  |
|  |  +-------------------------------------------------------------------+  |  |
|  |  | JVM (Java Virtual Machine) - Máy ảo thực thi mã Bytecode          |  |  |
|  |  |  - ClassLoader, Execution Engine (JIT + Interpreter), GC          |  |  |
|  |  +-------------------------------------------------------------------+  |  |
|  +-------------------------------------------------------------------------+  |
+-------------------------------------------------------------------------------+
```

*   **JVM (Java Virtual Machine):** Là trái tim của Java. Nó là một cỗ máy ảo giả lập chạy trên hệ điều hành thật. Nhiệm vụ của nó là đọc mã nhị phân trung gian (**Bytecode**) và dịch thành mã máy thực thi trực tiếp trên CPU.
*   **JRE (Java Runtime Environment):** Là gói môi trường tối thiểu để một người dùng thông thường chạy được ứng dụng Java (`JRE = JVM + Core Libraries`).
*   **JDK (Java Development Kit):** Là bộ đồ nghề đầy đủ cho Developer để viết, biên dịch, chạy và debug mã nguồn (`JDK = JRE + Development Tools`).

> [!TIP]
> 🗣️ **Văn mẫu phỏng vấn khi được hỏi "Phân biệt JDK, JRE và JVM?":**
> *"Dạ thưa anh/chị, JVM là máy ảo trực tiếp thực thi Bytecode và quản lý bộ nhớ thông qua Garbage Collector. JRE bao gồm JVM cộng thêm các thư viện chuẩn để chạy ứng dụng Java. Còn JDK là bộ công cụ hoàn chỉnh nhất dành cho lập trình viên, nó chứa JRE cùng các công cụ như trình biên dịch `javac` và công cụ gỡ lỗi `jdb`. Tóm lại: Người dùng chỉ cần JRE để chạy, còn lập trình viên bắt buộc phải có JDK để phát triển."*

---

## 1.2 Quy trình biên dịch và thực thi 2 bước (Bytecode & JIT)

Khác biệt cốt lõi giữa Java và các ngôn ngữ như C/C++ nằm ở quy trình 2 bước:

```text
[Mã nguồn .java] 
       │ 
       ▼ (1) Biên dịch bởi `javac` (Compile-time)
[Bytecode .class]    <--- Độc lập hệ điều hành, không phụ thuộc phần cứng
       │ 
       ▼ (2) Máy ảo JVM đọc và xử lý (Runtime)
  ┌────┴───────────────────────────┐
  ▼                                ▼
[Interpreter (Thông dịch)]      [JIT Compiler (Biên dịch tức thời)]
(Dịch từng dòng, chạy ngay)      (Biên dịch các đoạn mã "Hot spot" sang mã máy và cache lại)
  └────┬───────────────────────────┘
       ▼
[Mã máy Machine Code (0101...)] ──> [CPU xử lý]
```

1.  **Bước 1 - Compile-time:** Lập trình viên chạy lệnh `javac Program.java`. Trình biên dịch kiểm tra cú pháp và sinh ra file `Program.class` chứa mã Bytecode.
2.  **Bước 2 - Runtime:** Lệnh `java Program` kích hoạt JVM. JVM dùng **ClassLoader** nạp file `.class` vào RAM, sau đó **Execution Engine** phối hợp giữa:
    *   **Interpreter:** Dịch ngay từng dòng Bytecode để app khởi động nhanh.
    *   **JIT (Just-In-Time) Compiler:** Theo dõi những đoạn code chạy đi chạy lại nhiều lần ("Hot spot"), biên dịch thẳng chúng sang mã máy gốc và lưu cache lại, giúp các lần chạy sau đạt tốc độ tương đương C/C++.

---

## 1.3 Hoạt động phần cứng: Disk, RAM, CPU Cache & Registers

Khi bạn click đúp chạy một phần mềm Java:

| Thành phần | Đặc điểm tốc độ | Vai trò đối với Java Application |
| :--- | :--- | :--- |
| **Disk (SSD/HDD)** | Cực chậm (~vài ms) | Nơi lưu trữ mã nguồn `.java` và file `.class` khi chưa chạy. |
| **RAM** | Nhanh (~10-100 ns) | Nơi JVM được hệ điều hành cấp phát bộ nhớ để nạp code và tạo đối tượng (Heap, Stack, Metaspace). |
| **L3 / L2 / L1 Cache** | Cực nhanh (~1-10 ns) | Nằm bên trong CPU. CPU nạp các biến đang tính toán dở từ RAM vào Cache để tránh nghẽn cổ chai. |
| **Registers** | Nhanh nhất (~0.5 ns) | Nằm ngay trên lõi CPU để thực hiện trực tiếp các phép tính cộng trừ bit. |

---

## 1.4 Hệ thống Kiểu dữ liệu nguyên thủy (Primitive Types) & Ép kiểu (Casting)

Java có **8 kiểu dữ liệu nguyên thủy** được lưu trực tiếp trên vùng nhớ **Stack**:

| Kiểu | Kích thước | Giá trị mặc định | Miền giá trị | Ghi nhớ nhanh |
| :---: | :---: | :---: | :--- | :--- |
| `byte` | 1 byte (8 bit) | `0` | $-128$ đến $127$ | Tiết kiệm RAM cho mảng dữ liệu thô lớn |
| `short` | 2 bytes (16 bit) | `0` | $-32,768$ đến $32,767$ | Ít dùng |
| `int` | 4 bytes (32 bit) | `0` | $\approx -2.1$ tỷ đến $+2.1$ tỷ | **Kiểu số nguyên mặc định** |
| `long` | 8 bytes (64 bit) | `0L` | $\approx -9 \times 10^{18}$ đến $+9 \times 10^{18}$ | Thêm hậu tố `L`: `long id = 1000L;` |
| `float` | 4 bytes (32 bit) | `0.0f` | Độ chính xác ~6-7 chữ số | Thêm hậu tố `f`: `float pi = 3.14f;` |
| `double`| 8 bytes (64 bit) | `0.0d` | Độ chính xác ~15-16 chữ số | **Kiểu số thực mặc định** |
| `char` | 2 bytes (16 bit) | `'\u0000'` | Ký tự Unicode ($0$ đến $65535$) | Đặt trong nháy đơn: `char c = 'A';` |
| `boolean`| 1 bit / 1 byte | `false` | Chỉ có `true` hoặc `false` | Không thể ép sang số (khác C/C++) |

### Cơ chế Ép kiểu (Type Casting)

```java
// 1. Widening (Ép kiểu nới rộng / ngầm định): Nhỏ -> Lớn
// byte -> short -> int -> long -> float -> double
int i = 50;
double d = i; // Tự động, an toàn 100%, không bao giờ mất mát dữ liệu

// 2. Narrowing (Ép kiểu thu hẹp / tường minh): Lớn -> Nhỏ
double pi = 3.14159;
int truncated = (int) pi; // truncated = 3 (Mất hẳn phần thập phân)

int over = 130;
byte b = (byte) over; // b = -126 (Bị tràn số chu kỳ: 127 -> -128 -> -127 -> -126)
```

> [!WARNING]
> 🗣️ **Văn mẫu phỏng vấn:**
> *"Ép kiểu nới rộng diễn ra tự động vì kiểu đích có kích thước lớn hơn kiểu nguồn, đảm bảo an toàn dữ liệu. Ngược lại, ép kiểu thu hẹp bắt buộc phải chỉ định thủ công bằng cú pháp `(targetType)` vì tiềm ẩn 2 rủi ro lớn: mất độ chính xác thập phân hoặc tràn số (overflow)."*

---

## 1.5 Quy tắc đặt tên chuẩn & Hằng số (final)

- **Biến & Hàm:** `lowerCamelCase` (ví dụ: `accountBalance`, `calculateInterestRate()`).
- **Class & Interface:** `UpperCamelCase` / `PascalCase` (ví dụ: `PaymentService`, `UserDto`).
- **Package:** Toàn bộ chữ thường, đảo ngược tên miền (ví dụ: `com.company.project.service`).
- **Hằng số (Constant):** Khai báo kết hợp `public static final` và dùng `SCREAMING_SNAKE_CASE` (ví dụ: `MAX_POOL_SIZE`, `DEFAULT_PAGE_LIMIT`).

---

## 1.6 Hệ thống Toán tử & Bản chất Tiền tố / Hậu tố (++a vs a++)

### Toán tử logic ngắn mạch (Short-Circuit)
- `&&` (AND ngắn mạch): Nếu vế trái là `false`, JVM lập tức kết luận cả biểu thức là `false` và **không thèm tính vế phải**.
- `||` (OR ngắn mạch): Nếu vế trái là `true`, JVM lập tức kết luận biểu thức là `true` và **bỏ qua vế phải**.
- *Ứng dụng thực tế:* Ngăn chặn lỗi NullPointerException: `if (user != null && user.isActive())`.

### Giải mã dứt điểm Tiền tố (++a) vs Hậu tố (a++)

*   `++a` (Tiền tố - Pre-increment): **Tăng giá trị biến lên 1 trước**, sau đó mới lấy giá trị mới đưa vào biểu thức.
*   `a++` (Hậu tố - Post-increment): **Lấy giá trị hiện tại của biến** đưa vào biểu thức tính toán trước, sau đó mới **tăng giá trị biến lên 1**.

```java
// Ví dụ cơ bản:
int a = 5;
int b = ++a; // a tăng lên 6 -> b nhận giá trị 6 (a=6, b=6)

int x = 5;
int y = x++; // y nhận giá trị x cũ là 5 -> x tăng lên 6 (y=5, x=6)

// Giải mã bài toán phỏng vấn:
int k = 1;
int result = ++k + k++ + ++k;
// Bước 1: ++k -> k tăng từ 1 lên 2, giá trị trả về = 2
// Bước 2: k++ -> giá trị trả về = 2, sau đó k tăng từ 2 lên 3
// Bước 3: ++k -> k tăng từ 3 lên 4, giá trị trả về = 4
// Tổng: result = 2 + 2 + 4 = 8 (và giá trị cuối cùng của k là 4)
```

> [!TIP]
> 🗣️ **Cách nói ngắn gọn dễ nhớ:**
> *"Tiền tố là Tăng trước - Dùng sau. Hậu tố là Dùng trước - Tăng sau."*

---

## 1.7 Cấu trúc rẽ nhánh (if-else & switch-case cải tiến)

- `if-else`: Dùng cho các điều kiện logic phức tạp, so sánh khoảng giá trị (`score >= 8.0 && score <= 10.0`).
- `switch-case`: Phù hợp khi so sánh một biến với các giá trị cố định rời rạc (`byte`, `short`, `char`, `int`, `String`, `enum`).
  - **Cạm bẫy Fall-through:** Nếu thiếu từ khóa `break`, code sẽ trôi tuột xuống thực thi tất cả các case bên dưới.
  - **Cải tiến Java 14+ (Switch Expression):** Dùng cú pháp mũi tên `->`, hỗ trợ gom nhiều giá trị trên 1 dòng, không bị fall-through và có thể trả về giá trị trực tiếp:

```java
String dayName = switch (day) {
    case 1, 7 -> "Cuối tuần";
    case 2, 3, 4, 5, 6 -> "Ngày trong tuần";
    default -> throw new IllegalArgumentException("Ngày không hợp lệ: " + day);
};
```

---

## 1.8 Cấu trúc vòng lặp & Từ khóa điều khiển luồng (break, continue, return)

| Vòng lặp | Kiểm tra điều kiện | Số lần chạy tối thiểu | Khi nào nên dùng? |
| :--- | :--- | :---: | :--- |
| **`for`** | Trước mỗi vòng | 0 lần | Khi biết trước số lần lặp cụ thể. |
| **`while`** | Trước mỗi vòng | 0 lần | Khi chưa biết trước số lần lặp, phụ thuộc điều kiện dừng. |
| **`do-while`** | **Sau mỗi vòng** | **1 lần** | Khi cần thực thi thân hàm ít nhất 1 lần (hiển thị menu, nhập dữ liệu lần đầu). |

### Phân biệt 3 từ khóa điều khiển
- `break`: Đập vỡ và thoát ngay lập tức khỏi cấu trúc lặp hoặc `switch` gần nhất.
- `continue`: Bỏ qua các dòng lệnh còn lại của vòng lặp hiện tại, lập tức nhảy tới lần lặp tiếp theo.
- `return`: Thoát hoàn toàn khỏi **phương thức (method)** đang thực thi, trả về giá trị (nếu có).

---

## 1.9 Phương thức (Method) & Nạp chồng phương thức (Overloading)

### Nạp chồng phương thức (Method Overloading)
Là kỹ thuật định nghĩa nhiều phương thức **trong cùng một Class có CÙNG TÊN** nhưng phải **KHÁC NHAU VỀ DANH SÁCH THAM SỐ** (khác số lượng tham số, hoặc khác kiểu dữ liệu, hoặc khác thứ tự kiểu dữ liệu).

> [!CAUTION]
> 🗣️ **Văn mẫu phỏng vấn - Bẫy Overloading:**
> *"Lưu ý rất quan trọng: Việc chỉ thay đổi kiểu trả về (return type) hoặc thay đổi access modifier mà giữ nguyên danh sách tham số thì KHÔNG ĐƯỢC coi là Overloading. Trình biên dịch sẽ báo lỗi trùng lặp phương thức ngay lập tức."*

---

# CHƯƠNG 2: CẤU TRÚC DỮ LIỆU MẢNG (ARRAY) (LESSON 7)

## 2.1 Bản chất mảng trong Java: Luôn là Object trên Heap

Mảng là tập hợp các phần tử có **cùng kiểu dữ liệu**, có **kích thước cố định (fixed-size)** được khai báo trước và các phần tử nằm ở các ô nhớ **liên tiếp nhau** trong bộ nhớ RAM.

```text
       STACK MEMORY                         HEAP MEMORY
+------------------------+         +-------------------------------------------+
|  arr (Biến tham chiếu) | ======> | [0]: 10 | [1]: 20 | [2]: 30 | length = 3  |
+------------------------+         +-------------------------------------------+
 (Lưu địa chỉ: @0x99a)              (@0x99a - Đối tượng Mảng thực tế trên Heap)
```

- Trong ngôn ngữ C/C++, mảng cục bộ có thể nằm trên Stack. **Nhưng trong Java, MẢNG LUÔN LUÔN LÀ MỘT OBJECT NẰM TRÊN BỘ NHỚ HEAP!**
- Biến mảng mà chúng ta khai báo thực chất chỉ là một **con trỏ tham chiếu (Reference)** nằm trên Stack lưu giữ địa chỉ trỏ sang Heap.

---

## 2.2 Mảng 1 chiều & Cạm bẫy ArrayIndexOutOfBoundsException

```java
int[] arr = new int[5]; // Cấp phát 5 ô nhớ trên Heap, giá trị mặc định là 0
arr[0] = 10;
// Lấy độ dài mảng: arr.length (là thuộc tính final, không có dấu ngoặc tròn ())
```

> [!CAUTION]
> **Cạm bẫy:** Chỉ số mảng chạy từ `0` đến `arr.length - 1`. Bất kỳ truy xuất nào tới chỉ số âm hoặc `>= arr.length` đều bắn lỗi ngoại lệ thời gian chạy: `java.lang.ArrayIndexOutOfBoundsException`.

---

## 2.3 Thư viện java.util.Arrays & Phân biệt Shallow Copy vs Deep Copy

Lớp tiện ích `java.util.Arrays` cung cấp các thuật toán tối ưu sẵn có:
- `Arrays.toString(arr)`: In nội dung mảng dạng chuỗi đẹp mắt `[1, 2, 3]`.
- `Arrays.sort(arr)`: Sắp xếp mảng tăng dần (sử dụng Dual-Pivot Quicksort với primitive, TimSort với Object - độ phức tạp $O(N \log N)$).
- `Arrays.binarySearch(arr, key)`: Tìm kiếm nhị phân $O(\log N)$ (bắt buộc mảng phải được sort trước).

### Phân biệt Shallow Copy vs Deep Copy

```java
int[] original = {1, 2, 3};

// 1. Phép gán con trỏ (Reference Assignment): Cùng trỏ chung 1 mảng trên Heap
int[] alias = original;
alias[0] = 999; // original[0] cũng biến thành 999!

// 2. Shallow Copy (Với mảng primitive):
int[] copy = Arrays.copyOf(original, original.length);
copy[0] = 111; // original[0] KHÔNG bị ảnh hưởng

// ⚠️ CẢNH BÁO với mảng Đối tượng (Student[]):
// Arrays.copyOf() chỉ copy danh sách các địa chỉ tham chiếu, các đối tượng Student
// bên trong vẫn dùng chung. Muốn Deep Copy, phải duyệt for và new từng đối tượng mới!
```

---

## 2.4 Mảng 2 chiều & Mảng răng cưa (Jagged Array)

Trong Java, mảng 2 chiều không phải là một khối chữ nhật đồng nhất mà là **"Mảng của các mảng" (Array of Arrays)**.
Chính vì vậy, Java hỗ trợ **Mảng răng cưa (Jagged Array)** - nơi mỗi hàng có thể có số cột hoàn toàn khác nhau:

```java
int[][] jagged = new int[3][];
jagged[0] = new int[2]; // Hàng 0 có 2 cột
jagged[1] = new int[5]; // Hàng 1 có 5 cột
jagged[2] = new int[3]; // Hàng 2 có 3 cột

// In mảng 2 chiều chuẩn chỉnh:
System.out.println(Arrays.deepToString(jagged));
```

---

# CHƯƠNG 3: LẬP TRÌNH HƯỚNG ĐỐI TƯỢNG (OOP) TOÀN DIỆN (LESSON 9, 10, 11, 12, 14)

## 3.1 Tư duy OOP: State, Behavior, Identity & Class vs Object

Thế giới thực được phản ánh vào lập trình hướng đối tượng qua 3 yếu tố:
1.  **Trạng thái (State):** Dữ liệu đặc trưng của đối tượng $\to$ Biến thuộc tính (**Fields**).
2.  **Hành vi (Behavior):** Những việc đối tượng có thể làm $\to$ Phương thức (**Methods**).
3.  **Định danh (Identity):** Tính độc nhất phân biệt đối tượng này với đối tượng khác $\to$ **Địa chỉ ô nhớ trên Heap**.

```text
+------------------------------------+          +------------------------------------+
| CLASS (Lớp / Bản thiết kế)         |          | OBJECT (Đối tượng / Thực thể)      |
+------------------------------------+          +------------------------------------+
| - Khuôn đúc mẫu trên lý thuyết.     |  ==new==>| - Bức tượng cụ thể được đúc ra.    |
| - Nạp vào Metaspace / Method Area. |          | - Chiếm bộ nhớ RAM thực tế trên Heap|
| - Chỉ có 1 định nghĩa duy nhất.    |          | - Có thể tạo ra vô số đối tượng.   |
+------------------------------------+          +------------------------------------+
```

---

## 3.2 Giải phẫu Bộ nhớ JVM: Metaspace, Heap, Stack Frame & GC

```text
+---------------------------------------------------------------------------------+
|                                 BỘ NHỚ JVM RUNTIME                              |
|                                                                                 |
|  [ METASPACE / METHOD AREA ] (Dùng chung cho toàn ứng dụng)                     |
|  - Chứa cấu trúc Class, Bytecode các hàm, hằng số Constant Pool, biến static.    |
|                                                                                 |
|  [ HEAP AREA ] (Dùng chung cho mọi Thread - Garbage Collector hoạt động ở đây)  |
|  - Chứa TẤT CẢ các Object và Array được tạo bởi từ khóa `new`.                  |
|  - String Constant Pool (Hồ chứa chuỗi).                                        |
|                                                                                 |
|  [ JVM STACK ] (Mỗi Thread có một Stack riêng biệt)                             |
|  - Mỗi lời gọi hàm đẩy vào 1 Stack Frame (chứa biến cục bộ, biến tham chiếu).   |
|  - Hàm kết thúc -> Stack Frame tự động bị Pop (hủy) ngay lập tức.               |
+---------------------------------------------------------------------------------+
```

### So sánh 2 thảm họa bộ nhớ kinh điển

*   **`StackOverflowError`:** Xảy ra trên vùng nhớ **Stack**. Nguyên nhân điển hình là do gọi hàm **đệ quy vô tận** (thiếu điều kiện dừng), khiến các Stack Frame chồng chất lên nhau vượt quá giới hạn bộ nhớ Stack.
*   **`OutOfMemoryError (OOM)`:** Xảy ra trên vùng nhớ **Heap**. Nguyên nhân do ứng dụng liên tục tạo mới Object nhưng vẫn giữ tham chiếu tới chúng (Memory Leak), khiến **Garbage Collector (Bộ dọn rác)** không thể thu gom, dẫn đến cạn kiệt bộ nhớ Heap.

---

## 3.3 Từ khóa new, Constructor & Từ khóa this

Khi thực hiện lệnh: `Student s = new Student("Nam", 20);`:
1.  **Nạp Class:** JVM kiểm tra Class `Student` trong Metaspace, nếu chưa có thì ClassLoader sẽ nạp vào.
2.  **Cấp phát bộ nhớ Heap:** Từ khóa `new` xin hệ điều hành một vùng nhớ trống trên Heap đủ chứa các trường dữ liệu của `Student`.
3.  **Khởi tạo mặc định:** Mọi field được gán giá trị mặc định (`null`, `0`, `false`).
4.  **Gọi Constructor:** Constructor được kích hoạt để gán giá trị thực tế theo tham số truyền vào.
5.  **Trả về tham chiếu:** Địa chỉ của vùng nhớ Heap được trả về và gán cho biến `s` trên Stack.

### Từ khóa `this`
- `this.fieldName`: Dùng để chỉ định thuộc tính của đối tượng hiện tại, tránh bị che khuất bởi tham số hàm khi trùng tên (Variable Shadowing).
- `this(...)`: Gọi chéo một constructor khác trong cùng một Class (Constructor Chaining). **Bắt buộc phải nằm ở dòng lệnh đầu tiên trong thân constructor!**

---

## 3.4 Từ khóa static: Biến static, Hàm static & Khối static block

Từ khóa `static` chỉ ra rằng thành phần đó thuộc về **toàn bộ Class**, chứ không thuộc về một đối tượng (instance) cụ thể nào:
- **Biến static (Static variable):** Được cấp phát một lần duy nhất khi Class được nạp vào Metaspace. Mọi đối tượng tạo ra đều dùng chung biến này (rất hay dùng làm bộ đếm ID, hằng số cấu hình).
- **Phương thức static (Static method):** Có thể gọi trực tiếp thông qua tên Class: `Math.max(a, b)`.
  - **Quy tắc bất di bất dịch:** Phương thức `static` **KHÔNG THỂ truy cập trực tiếp biến hoặc phương thức `non-static`**, và tuyệt đối không được dùng từ khóa `this` hay `super` (vì lúc này chưa có đối tượng cụ thể nào được tạo ra trên Heap).
- **Khối static block (`static { ... }`):** Chạy duy nhất một lần khi Class được ClassLoader nạp vào bộ nhớ, dùng để khởi tạo các cấu hình tĩnh phức tạp.

---

## 3.5 Cơ chế Pass-By-Value tuyệt đối trong Java (Chứng minh cốt lõi)

> [!IMPORTANT]
> 🗣️ **Khẳng định nguyên lý:** **JAVA 100% LUÔN LUÔN LÀ PASS-BY-VALUE (Truyền giá trị). JAVA HOÀN TOÀN KHÔNG CÓ PASS-BY-REFERENCE.**

Rất nhiều người bị nhầm lẫn khi truyền đối tượng vào hàm. Bản chất diễn ra như sau:
- **Với biến nguyên thủy:** Giá trị được copy là **con số thực tế**. Hàm sửa thì biến gốc bên ngoài không đổi.
- **Với biến tham chiếu (Object):** Giá trị được copy là **địa chỉ con trỏ tham chiếu** (Memory Address).
  - Nếu dùng con trỏ copy để **sửa thuộc tính bên trong đối tượng** (`p.setName("Mới")`): Đối tượng trên Heap **BỊ THAY ĐỔI**.
  - Nếu **gán con trỏ copy sang một đối tượng mới** (`p = new Person(...)`): Ta chỉ thay đổi con trỏ bản sao cục bộ trong Stack Frame của hàm đó, **con trỏ gốc bên ngoài hàm hoàn toàn KHÔNG THAY ĐỔI**!

```java
public class PassByValueProof {
    public static void test(Person p) {
        p.setName("Bob"); // Đổi dữ liệu trên ô nhớ Heap chung -> Bên ngoài bị đổi!
        p = new Person("Charlie"); // Gán p trỏ sang đối tượng mới trên Heap
        p.setName("David"); // Chỉ đổi trên đối tượng mới Charlie
    }

    public static void main(String[] args) {
        Person person = new Person("Alice");
        test(person);
        System.out.println(person.getName()); // In ra: "Bob" (chứ KHÔNG PHẢI Charlie hay David!)
    }
}
```

---

## 3.6 Quan hệ Composition ("Has-A")

- Đại diện cho mối quan hệ sở hữu: **"Có một" (Has-A)**. Ví dụ: Xe hơi *có một* Động cơ (`Car has an Engine`), Điện thoại *có một* Pin (`Phone has a Battery`).
- **Triết lý thiết kế chuẩn:** *"Ưu tiên sử dụng Composition hơn Kế thừa (Favor Composition over Inheritance)"*. Kế thừa làm các lớp gắn kết quá chặt chẽ (tight-coupling), trong khi Composition giúp code linh hoạt, dễ thay thế và dễ viết unit test bằng Mock.

---

## 3.7 Trụ cột 1: Tính Đóng Gói (Encapsulation) & Access Modifiers

- **Định nghĩa:** Đóng gói là kỹ thuật che giấu dữ liệu nội bộ (đặt thuộc tính là `private`), chỉ cho phép truy cập và sửa đổi thông qua các phương thức công khai được kiểm soát (`public getter/setter`).
- **Mục đích:** Bảo vệ tính toàn vẹn của dữ liệu (chặn gán tuổi âm, chặn rút tiền quá số dư), tạo thuộc tính chỉ đọc (read-only), và che giấu giải thuật nội bộ.

### Bảng Ma trận Access Modifiers (Phạm vi truy cập)

| Access Modifier | Trong cùng Class | Cùng Package | Lớp con ở Package khác (`extends`) | Toàn bộ Project |
| :--- | :---: | :---: | :---: | :---: |
| **`private`** | ✅ Có | ❌ Không | ❌ Không | ❌ Không |
| **Default** (Không ghi gì) | ✅ Có | ✅ Có | ❌ Không | ❌ Không |
| **`protected`** | ✅ Có | ✅ Có | ✅ Có | ❌ Không |
| **`public`** | ✅ Có | ✅ Có | ✅ Có | ✅ Có |

---

## 3.8 Trụ cột 2: Tính Kế Thừa (Inheritance) & Từ khóa super

- Đại diện cho mối quan hệ **"Là một" (IS-A)**: `Dog is an Animal`. Lớp con tái sử dụng các thuộc tính và phương thức non-private của lớp cha thông qua từ khóa `extends`.
- **Đơn kế thừa:** Trong Java, một Class chỉ được kế thừa trực tiếp từ **duy nhất 1 Class cha** (tránh xung đột Diamond Problem).
- Mọi Class trong Java đều là con cháu của lớp tổ tiên tối cao: `java.lang.Object`.
- **Từ khóa `super`:**
  - `super.method()`: Gọi phương thức của lớp cha khi bị lớp con ghi đè.
  - `super(...)`: Gọi constructor của lớp cha. **Bắt buộc phải nằm ở dòng đầu tiên của constructor lớp con!**

---

## 3.9 Trụ cột 3: Tính Đa Hình (Polymorphism): Overloading vs Overriding & Upcasting/Downcasting

**Đa hình** nghĩa là "một hành vi có thể thể hiện dưới nhiều hình thái khác nhau".

| Tiêu chí so sánh | Method Overloading (Đa hình Tĩnh) | Method Overriding (Đa hình Động) |
| :--- | :--- | :--- |
| **Thời điểm quyết định** | **Compile-time** (Lúc biên dịch code). | **Runtime** (Lúc chương trình đang chạy). |
| **Vị trí** | Trong **cùng một Class**. | Giữa **Lớp con và Lớp cha**. |
| **Tên hàm** | Giống nhau. | Giống nhau. |
| **Danh sách tham số** | **Bắt buộc phải KHÁC NHAU**. | **Bắt buộc phải GIỐNG HỆT NHAU**. |
| **Cơ chế JVM** | Biên dịch dựa vào kiểu tham số truyền vào. | **Dynamic Method Dispatch** (gọi hàm theo đối tượng thực tế trên Heap). |

### Upcasting và Downcasting

```java
// Upcasting: Ép kiểu ngược lên lớp cha (Luôn an toàn, tự động)
Animal a = new Dog(); // Dog IS-A Animal
a.makeSound(); // JVM gọi phương thức makeSound() của Dog tại Runtime!

// Downcasting: Ép kiểu xuôi xuống lớp con (Tiềm ẩn lỗi ClassCastException)
if (a instanceof Dog) { // Bắt buộc phải kiểm tra bằng instanceof trước khi ép kiểu
    Dog d = (Dog) a;
    d.wagTail(); // Gọi phương thức riêng của lớp Dog
}
```

---

## 3.10 Trụ cột 4: Tính Trừu Tượng (Abstraction): Abstract Class vs Interface

Tính trừu tượng tập trung vào câu hỏi **"Làm cái gì" (What to do)** thay vì "Làm như thế nào" (How to do).

| Tiêu chí | Lớp trừu tượng (Abstract Class) | Giao diện (Interface) |
| :--- | :--- | :--- |
| **Bản chất quan hệ** | **IS-A** (Bản sắc gốc rễ cốt lõi). | **CAN-DO** (Năng lực, hành vi, hợp đồng giao tiếp). |
| **Khởi tạo trực tiếp (`new`)** | ❌ Không thể `new`. | ❌ Không thể `new`. |
| **Khả năng kế thừa** | Đơn kế thừa (`extends 1 Class`). | Đa triển khai (`implements nhiều Interfaces`). |
| **Thuộc tính (Fields)** | Chứa mọi loại biến (private, protected, instance, static). | Chỉ chứa hằng số: mặc định là `public static final`. |
| **Phương thức** | Chứa cả Abstract method và hàm bình thường có body. | Mặc định là `public abstract`. Từ Java 8 có thêm `default` và `static`. Từ Java 9 có `private`. |
| **Constructor** | ✅ **Có constructor** (để lớp con gọi `super()`). | ❌ **Không có constructor**. |

---

## 3.11 Hợp đồng (Contract) giữa equals() và hashCode() trong Object

Trong lớp tối cao `java.lang.Object`, có 2 phương thức luôn đi đôi với nhau:
- `boolean equals(Object obj)`: So sánh logic 2 đối tượng có bằng nhau không.
- `int hashCode()`: Trả về một con số nguyên đại diện cho vị trí băm của đối tượng trong bộ nhớ (dùng cho `HashSet`, `HashMap`).

> [!IMPORTANT]
> **Hợp đồng bắt buộc (Contract Rules):**
> 1. Nếu hai đối tượng bằng nhau theo `equals()` (`obj1.equals(obj2) == true`), thì `hashCode()` của chúng **BẮT BUỘC PHẢI BẰNG NHAU** (`obj1.hashCode() == obj2.hashCode()`).
> 2. Nếu hai đối tượng có `hashCode()` bằng nhau, chúng **CHƯA CHẮC đã bằng nhau theo `equals()`** (hiện tượng đụng độ mã băm - Hash Collision).
> 
> **Hậu quả nếu vi phạm:** Nếu bạn ghi đè `equals()` mà **quên ghi đè `hashCode()`**, khi đưa đối tượng vào `HashSet` hoặc làm Key trong `HashMap`, cấu trúc dữ liệu sẽ không thể tìm thấy phần tử đó hoặc bị lưu trùng lặp dữ liệu!

---

## 3.12 Độ phức tạp thuật toán (Big O) & Bản chất ArrayList

### Các cấp độ Big O thường gặp
$$O(1) < O(\log N) < O(N) < O(N \log N) < O(N^2)$$

- $O(1)$: Truy cập phần tử mảng qua chỉ số `arr[i]`.
- $O(\log N)$: Tìm kiếm nhị phân (Binary Search).
- $O(N)$: Duyệt toàn bộ mảng hoặc danh sách.
- $O(N \log N)$: Sắp xếp MergeSort, QuickSort (trung bình).
- $O(N^2)$: Hai vòng for lồng nhau (Bubble Sort).

### Bản chất của `ArrayList<E>`
- `ArrayList` được hiện thực bên dưới bằng một **Mảng động (Dynamic Array)**.
- Khi khởi tạo rỗng, dung lượng mặc định là **10 phần tử**.
- Khi mảng đầy, ArrayList tự tạo một mảng mới có dung lượng gấp khoảng **1.5 lần** mảng cũ ($new = old + old >> 1$), sau đó sao chép toàn bộ phần tử sang mảng mới bằng `Arrays.copyOf()`.
- **Hiệu năng:** Lấy phần tử theo index cực nhanh $O(1)$. Nhưng chèn/xóa ở đầu hoặc giữa mảng thì tốn $O(N)$ do phải dịch chuyển các phần tử phía sau.

---

# CHƯƠNG 4: XỬ LÝ CHUỖI, NGOẠI LỆ & GENERICS (LESSON 15 - 16)

## 4.1 String: Tính Bất Biến (Immutable) & String Pool

Trong Java, `String` là một **Đối tượng Bất biến (Immutable)**. Khi đã được khởi tạo, nội dung mảng ký tự bên trong String vĩnh viễn không thể thay đổi.

```text
               HEAP MEMORY
+-------------------------------------------------+
|  [ STRING CONSTANT POOL ]                       |
|      "Hello" <-------- s1 (Dùng literal)        |
|              <-------- s2 (Tái sử dụng "Hello") |
+-------------------------------------------------+
|  [ NON-POOL HEAP ]                              |
|      Object "Hello" <- s3 (Tạo bằng từ khóa new)|
+-------------------------------------------------+
```

```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = new String("Hello");

System.out.println(s1 == s2);      // TRUE (Trỏ chung 1 ô nhớ trong String Pool)
System.out.println(s1 == s3);      // FALSE (s3 nằm ở vùng Heap ngoài Pool)
System.out.println(s1.equals(s3)); // TRUE (So sánh nội dung ký tự bên trong)
```

> [!TIP]
> 🗣️ **Văn mẫu phỏng vấn: "Tại sao String trong Java lại bất biến?":**
> *"Dạ, String được thiết kế bất biến vì 3 lý do sống còn: Thứ nhất là để tiết kiệm bộ nhớ nhờ cơ chế String Pool (nhiều biến cùng trỏ vào 1 chuỗi). Thứ hai là an toàn bảo mật (Security), vì String thường được dùng lưu password, URL database, tên đăng nhập mạng. Thứ ba là an toàn đa luồng (Thread-Safety), vì dữ liệu bất biến thì không bao giờ bị xung đột khi nhiều luồng cùng truy cập."*

---

## 4.2 StringBuilder vs StringBuffer vs StringJoiner

Khi cần nối chuỗi nhiều lần trong vòng lặp, việc dùng toán tử `+` sẽ liên tục sinh ra các đối tượng String rác trong String Pool, gây nghẽn bộ nhớ. Ta bắt buộc phải dùng các bộ đệm khả biến (Mutable):

| Tiêu chí | `String` | `StringBuilder` | `StringBuffer` |
| :--- | :--- | :--- | :--- |
| **Tính khả biến** | Bất biến (Immutable) | Khả biến (Mutable) | Khả biến (Mutable) |
| **Thread-Safe** | Có (Do bất biến) | ❌ **Không an toàn đa luồng** | ✅ **An toàn đa luồng** (`synchronized`) |
| **Tốc độ** | Rất chậm khi nối chuỗi lớn | **Nhanh nhất** (Khuyên dùng cho đơn luồng) | Chậm hơn StringBuilder do chi phí khóa lock |

*   **`StringJoiner` (Java 8):** Chuyên dùng ghép danh sách chuỗi với ký tự phân tách:
    `new StringJoiner(", ", "[", "]").add("A").add("B").toString(); // "[A, B]"`

---

## 4.3 Wrapper Class, Autoboxing/Unboxing & Cạm bẫy Integer Cache

Java cung cấp 8 **Wrapper Class** tương ứng với 8 kiểu nguyên thủy (`int` $\to$ `Integer`, `double` $\to$ `Double`...) nhằm mục đích:
1.  Đưa vào các Collection (`List<Integer>`, `Map<K, V>`) vốn chỉ chấp nhận Object.
2.  Chứa được giá trị `null` (đại diện cho dữ liệu rỗng trong Database).

- **Autoboxing:** Java tự động đóng gói Primitive thành Wrapper: `Integer a = 10;` $\to$ `Integer.valueOf(10)`.
- **Unboxing:** Java tự động mở gói Wrapper thành Primitive: `int b = a;` $\to$ `a.intValue()`.

> [!CAUTION]
> 🗣️ **Cạm bẫy phỏng vấn cực hiểm - Integer Cache (-128 đến 127):**
> Java có một vùng nhớ đệm lưu sẵn các đối tượng Integer có giá trị từ $-128$ đến $127$.
> ```java
> Integer a = 100, b = 100;
> System.out.println(a == b); // TRUE (Nằm trong cache, trỏ chung object)
> 
> Integer x = 200, y = 200;
> System.out.println(x == y); // FALSE (Vượt quá cache, tạo 2 object mới riêng biệt!)
> System.out.println(x.equals(y)); // TRUE (Muốn so sánh giá trị Wrapper BẮT BUỘC dùng .equals())
> ```
> *Cạm bẫy thứ hai:* Unboxing một biến Wrapper đang mang giá trị `null` sẽ lập tức ném ra lỗi `NullPointerException`!

---

## 4.4 Iterable, Iterator & Lỗi ConcurrentModificationException (Fail-Fast)

- Mọi Collection đều kế thừa từ `Iterable`, cung cấp con trỏ `Iterator` để duyệt phần tử gồm 3 phương thức: `hasNext()`, `next()`, `remove()`.
- **Lỗi `ConcurrentModificationException`:** Xảy ra khi bạn đang duyệt danh sách bằng vòng lặp `for-each` mà lại gọi trực tiếp `list.remove(item)`. Lúc này, chỉ số biến đổi `modCount` của danh sách bị lệch với dự kiến của Iterator, kích hoạt cơ chế tự bảo vệ **Fail-Fast**.
- **Cách sửa đúng:** Sử dụng `iterator.remove()` hoặc dùng `list.removeIf(condition)`.

---

## 4.5 Xử lý Ngoại lệ: Checked vs Unchecked, try-catch-finally, try-with-resources

```text
                     Throwable
                     /       \
               Exception      Error (Lỗi JVM nghiêm trọng, không nên catch: OOM, StackOverflow)
               /       \
    Checked Exception   RuntimeException (Unchecked Exception)
    (Bắt buộc try-catch  (Lỗi logic lập trình viên: NullPointerException,
     hoặc throws:        ArithmeticException, ArrayIndexOutOfBoundsException...)
     IOException,
     SQLException...)
```

- **Checked Exception:** Kế thừa từ `Exception` (trừ `RuntimeException`). Trình biên dịch bắt buộc bạn phải xử lý ngay khi viết code.
- **Unchecked Exception:** Kế thừa từ `RuntimeException`. Là các lỗi do sơ suất logic của lập trình viên, trình biên dịch không ép phải khai báo `throws`.

### Try-catch-finally & Try-with-resources
- Khối `finally` **LUÔN LUÔN CHẠY**, bất kể có exception hay không, kể cả khi khối `try` có lệnh `return`.
- **Try-with-resources (Java 7+):** Tự động đóng các tài nguyên (file stream, socket, connection) triển khai interface `AutoCloseable` mà không cần viết khối `finally` cồng kềnh:

```java
try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
    System.out.println(br.readLine());
} catch (IOException e) {
    e.printStackTrace();
} // BufferedReader tự động được đóng an toàn tại đây!
```

---

## 4.6 Phân biệt bộ ba kinh điển: final - finally - finalize

| Từ khóa | Bản chất | Tác dụng |
| :--- | :--- | :--- |
| **`final`** | Modifier | - Biến `final`: Trở thành hằng số, không thể gán lại.<br>- Phương thức `final`: Không cho phép lớp con ghi đè (`@Override`).<br>- Lớp `final`: Không cho phép lớp khác kế thừa (`class String`, `class Math`). |
| **`finally`** | Khối lệnh | Đi kèm với `try-catch`, luôn luôn được thực thi để dọn dẹp tài nguyên. |
| **`finalize()`** | Phương thức | Thuộc lớp `Object`. Được GC gọi trước khi dọn rác đối tượng. **Đã bị Deprecated từ Java 9** vì gây rò rỉ bộ nhớ và không dự đoán được thời điểm chạy. |

---

## 4.7 Generics: Tham số hóa kiểu, Wildcards (?, extends, super) & Nguyên lý PECS

Generics giúp kiểm tra an toàn kiểu dữ liệu ngay tại **Compile-time** và loại bỏ việc ép kiểu thủ công `(Type)`.

### Ký tự Wildcard (`?`) & Nguyên lý PECS
- `<?>`: Chấp nhận bất kỳ kiểu dữ liệu nào.
- `<? extends Number>` (Upper Bound): Nhận `Number` hoặc các lớp con của `Number`.
- `<? super Integer>` (Lower Bound): Nhận `Integer` hoặc các lớp cha của `Integer`.

> [!IMPORTANT]
> 🗣️ **Nguyên lý vàng PECS: Producer Extends, Consumer Super**
> - **Producer Extends:** Khi cấu trúc dữ liệu đóng vai trò là nơi **cung cấp/đọc dữ liệu** (bạn chỉ lấy phần tử ra đọc): Hãy dùng `<? extends T>`.
> - **Consumer Super:** Khi cấu trúc dữ liệu đóng vai trò là nơi **tiêu thụ/ghi dữ liệu** (bạn thêm phần tử mới vào danh sách): Hãy dùng `<? super T>`.

### Cơ chế Type Erasure (Xóa kiểu)
Để tương thích ngược với các phiên bản Java cũ, Java chỉ dùng Generics ở bước biên dịch. Khi dịch sang Bytecode `.class`, mọi tham số Generic `<T>` đều bị xóa bỏ và thay thế bằng `Object`. Do đó, lúc runtime không hề có khái niệm `ArrayList<String>` trong bộ nhớ JVM.

---

## 4.8 Sắp xếp đối tượng: Comparable vs Comparator

Khi sắp xếp danh sách đối tượng tùy biến (ví dụ `List<Student>`), Java cung cấp 2 giải pháp:

| Tiêu chí | `Comparable<T>` | `Comparator<T>` |
| :--- | :--- | :--- |
| **Giao diện** | Nằm trong `java.lang` | Nằm trong `java.util` |
| **Phương thức** | `int compareTo(T other)` | `int compare(T o1, T o2)` |
| **Vị trí cài đặt** | Cài đặt **trực tiếp bên trong** Class cần sắp xếp. | Viết thành một **Class riêng biệt** hoặc dùng Lambda bên ngoài. |
| **Mục đích** | Định nghĩa thứ tự sắp xếp tự nhiên, mặc định của đối tượng. | Cung cấp nhiều tiêu chí sắp xếp linh hoạt khác nhau (theo tên, theo tuổi, theo điểm...). |
| **Cú pháp gọi** | `Collections.sort(list);` | `list.sort(Comparator.comparing(Student::getGpa));` |

---

# CHƯƠNG 5: HỆ SINH THÁI JAVA 8+ HIỆN ĐẠI (LESSON 18)

## 5.1 Biểu thức chính quy (Regex - Regular Expressions)

Regex là chuỗi mẫu quy định cấu trúc văn bản dùng để validate form, tìm kiếm hoặc trích xuất dữ liệu.

```java
// Validate Email chuẩn:
String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
boolean isEmail = "dev@company.com".matches(emailRegex); // true

// Tìm kiếm và trích xuất với Pattern & Matcher:
Pattern pattern = Pattern.compile("\\d{10}"); // Tìm số điện thoại 10 số
Matcher matcher = pattern.matcher("Hotline: 0912345678 hoặc 0987654321");
while (matcher.find()) {
    System.out.println("SĐT tìm thấy: " + matcher.group());
}
```

---

## 5.2 Modern Date/Time API (java.time)

Thay thế hoàn toàn bộ thư viện cũ `java.util.Date` và `Calendar` (vốn dĩ Mutable, không an toàn đa luồng). Toàn bộ các lớp trong `java.time` đều **Immutable và Thread-safe**:
- `LocalDate`: Chỉ ngày (ví dụ: `2026-10-15`).
- `LocalTime`: Chỉ giờ-phút-giây (ví dụ: `14:30:00`).
- `LocalDateTime`: Kết hợp ngày và giờ (không múi giờ).
- `ZonedDateTime`: Ngày giờ có múi giờ (`ZoneId.of("Asia/Ho_Chi_Minh")`).
- `Instant`: Mốc thời gian chính xác trên trục timestamp UTC.
- `Duration` (đo khoảng cách theo giây/phút/giờ) vs `Period` (đo khoảng cách theo ngày/tháng/năm).
- `DateTimeFormatter`: Định dạng chuỗi ngày giờ (`ofPattern("dd/MM/yyyy HH:mm:ss")`).

---

## 5.3 Default Method & Static Method trong Interface

Từ Java 8, Interface được phép có thân hàm cụ thể thông qua từ khóa `default` hoặc `static`.
- **Mục đích:** Giúp đội ngũ phát triển Java bổ sung thêm phương thức mới vào các Interface có sẵn (như phương thức `.forEach()`, `.stream()` trong `Collection`) mà không làm gãy vỡ hàng triệu lớp con đang triển khai interface đó trước đây.
- **Xử lý xung đột Đa kế thừa giao diện (Diamond Problem):** Nếu một lớp implements hai interface có cùng tên hàm `default`, lớp con bắt buộc phải ghi đè `@Override` lại và chỉ định rõ muốn gọi hàm của interface nào: `InterfaceA.super.methodName()`.

---

## 5.4 Functional Interface & Bộ tứ cốt lõi: Predicate, Function, Consumer, Supplier

**Functional Interface** là interface chỉ chứa **DUY NHẤT MỘT phương thức trừu tượng** (Single Abstract Method - SAM). Được gắn nhãn `@FunctionalInterface`.

```text
1. Predicate<T>    ──> boolean test(T t)     : Kiểm tra điều kiện (Lọc dữ liệu)
2. Function<T, R>  ──> R apply(T t)          : Chuyển đổi đối tượng T thành R
3. Consumer<T>     ──> void accept(T t)      : Tiêu thụ/In ấn dữ liệu (không trả về gì)
4. Supplier<T>     ──> T get()               : Cung cấp/Khởi tạo đối tượng mới lười biếng
```

---

## 5.5 Lambda Expression & Method Reference (::)

- **Lambda Expression:** Cú pháp viết tắt cực gọn cho Functional Interface: `(params) -> { body }`. Java tự động suy luận kiểu dữ liệu (**Type Inference**).
- **Method Reference (`::`):** Cú pháp rút gọn tối đa khi Lambda chỉ đơn thuần gọi tiếp một phương thức đã có sẵn:
  - `Math::max` thay thế cho `(a, b) -> Math.max(a, b)` (Static method)
  - `System.out::println` thay thế cho `x -> System.out.println(x)` (Instance method cụ thể)
  - `String::toUpperCase` thay thế cho `s -> s.toUpperCase()` (Instance method tùy ý)
  - `ArrayList::new` thay thế cho `() -> new ArrayList<>()` (Constructor)

---

## 5.6 Stream API: Đường ống xử lý dữ liệu khai báo & Lazy Evaluation

**Stream API** mang tư duy lập trình hàm (Declarative) vào xử lý tập hợp dữ liệu.

```text
[ Nguồn Dữ Liệu ] ──> [ Thao tác Trung gian ] ──> [ Thao tác Trung gian ] ──> [ Thao tác Kết thúc ]
 (List, Set, Mảng)     .filter(...)               .map(...)                   .collect(...)
                      (Trả về Stream mới)       (Trả về Stream mới)         (KÍCH HOẠT CHẠY)
```

- **Tính chất cốt lõi:**
  1. **Không lưu trữ:** Stream chỉ là đường ống trung chuyển dữ liệu.
  2. **Không làm đổi dữ liệu gốc:** Danh sách ban đầu được giữ nguyên vẹn.
  3. **Cơ chế Lazy Evaluation (Tính toán lười):** Các hàm trung gian (`filter`, `map`, `sorted`) chỉ được đăng ký trên giấy chứ **CHƯA HỀ CHẠY**. Toàn bộ đường ống chỉ thực sự bừng tỉnh và chạy một lượt khi gặp một **Terminal Operation** (`collect`, `count`, `forEach`).

```java
List<Student> students = getStudents();

// Tìm danh sách tên sinh viên giỏi (GPA >= 8.0) viết hoa, sắp xếp A-Z
List<String> topStudents = students.stream()
    .filter(s -> s.getGpa() >= 8.0)          // Intermediate: Lọc
    .map(Student::getName)                  // Intermediate: Lấy tên
    .map(String::toUpperCase)               // Intermediate: Viết hoa
    .sorted()                               // Intermediate: Sắp xếp
    .collect(Collectors.toList());          // Terminal: Thu hoạch kết quả
```

---

## 5.7 Optional: Khai tử NullPointerException

`Optional<T>` là một chiếc hộp bao bọc lấy đối tượng, có thể chứa giá trị hoặc rỗng (`Optional.empty()`), buộc lập trình viên phải chủ động kiểm soát trường hợp dữ liệu bị `null`.

```java
// 1. Khởi tạo an toàn
Optional<User> userOpt = Optional.ofNullable(findUserById(id));

// 2. Thao tác Functional mượt mà (Không cần if-else null thủ công)
String email = userOpt
    .filter(User::isActive)
    .map(User::getEmail)
    .orElse("default@system.com"); // Nếu null thì lấy giá trị mặc định

// 3. Ném ngoại lệ chuẩn chỉnh nếu không tìm thấy
User user = userOpt.orElseThrow(() -> new NotFoundException("Không tìm thấy User!"));
```

> [!CAUTION]
> **2 Điều cấm kỵ với Optional:**
> 1. Tuyệt đối không gọi thẳng `.get()` khi chưa kiểm tra `.isPresent()` (sẽ ném lỗi `NoSuchElementException`).
> 2. Không dùng `Optional` làm thuộc tính của Class hay tham số truyền vào hàm.

---

# CHƯƠNG 6: BẢNG TRA CỨU NHANH & TOP CẠM BẪY SÁT THỦ

## 6.1 Bảng ma trận so sánh các khái niệm cốt lõi

### 1. `==` vs `.equals()`
- `==`: So sánh **địa chỉ ô nhớ** (với Object) hoặc **giá trị bit thô** (với primitive).
- `.equals()`: Phương thức của lớp `Object` (mặc định là `==`), nhưng được các lớp như `String`, `Integer` ghi đè để so sánh **nội dung dữ liệu bên trong**.

### 2. Abstract Class vs Interface
- Dùng **Abstract Class** khi các đối tượng có quan hệ huyết thống mật thiết (**IS-A**: Chó là Động vật) và muốn tái sử dụng code chung, lưu trạng thái biến instance.
- Dùng **Interface** khi muốn đặt ra một bản hợp đồng hành vi (**CAN-DO**: Chim biết Bay, Máy bay biết Bay) cho các đối tượng hoàn toàn không cùng họ hàng.

---

## 6.2 Top 10 cạm bẫy Java Core người mới hay mắc phải

1.  **Chia số nguyên:** `int x = 5 / 2;` ra `2`. Muốn ra `2.5` phải viết `5.0 / 2`.
2.  **So sánh chuỗi bằng `==`:** Gây bug chết người khi chuỗi được tạo bằng `new String()` hoặc nhận từ bên ngoài. Luôn dùng `.equals()`.
3.  **Bẫy Integer Cache:** Hai số `Integer` bằng `128` so sánh bằng `==` ra `false` vì nằm ngoài khoảng cache $[-128, 127]$.
4.  **Bẫy NullPointerException khi Unboxing:** Gán `Integer a = null; int b = a;` sẽ nổ lỗi `NullPointerException` ngay lập tức!
5.  **Quên `break` trong switch-case:** Làm code trôi tự do (Fall-through) thực thi các case không mong muốn.
6.  **Xóa phần tử khi đang duyệt for-each:** Gây ra lỗi `ConcurrentModificationException`. Khắc phục bằng `Iterator.remove()` hoặc `removeIf()`.
7.  **Nhầm lẫn Pass-by-value:** Tưởng rằng gán lại `p = new Person()` trong hàm sẽ đổi được con trỏ bên ngoài.
8.  **Nối chuỗi trong vòng lặp bằng dấu `+`:** Tạo ra hàng nghìn object rác trong String Pool, gây quá tải bộ dọn rác GC. Luôn dùng `StringBuilder`.
9.  **Lạm dụng `Optional.get()`:** Gọi trực tiếp `.get()` mà không kiểm tra làm mất đi toàn bộ ý nghĩa an toàn của `Optional`.
10. **Quên gọi `super()` hoặc đặt sai vị trí:** Lệnh gọi `super(...)` hay `this(...)` trong constructor bắt buộc phải nằm ở **dòng lệnh đầu tiên**.

---

# CHƯƠNG 7: KHO 40 CÂU HỎI PHỎNG VẤN JAVA HAY GẶP NHẤT (KÈM LỜI THOẠI MẪU GHI ĐIỂM)

---

## Nhóm 1: Cú pháp cơ sở, Môi trường & Bộ nhớ (Câu 1 - 8)

### Câu 1: Trình bày cơ chế "Write Once, Run Anywhere" của Java?
- **Bản chất cốt lõi:** Mã nguồn `.java` được biên dịch thành mã trung gian Bytecode `.class` độc lập với phần cứng. JVM trên từng hệ điều hành sẽ dịch Bytecode đó thành mã máy tương ứng.
- 🗣️ **Văn mẫu trả lời:**
  > *"Java đạt được tính đa nền tảng nhờ cơ chế biên dịch 2 bước. Đầu tiên, trình biên dịch `javac` dịch mã nguồn `.java` thành mã nhị phân trung gian gọi là Bytecode `.class`. Bytecode này không phụ thuộc vào bất kỳ hệ điều hành nào. Khi chạy trên môi trường thực tế, mỗi hệ điều hành như Windows, Mac hay Linux sẽ có một máy ảo JVM riêng biệt để thông dịch và biên dịch mã Bytecode đó thành mã máy gốc của CPU. Nhờ đó ta chỉ cần viết code một lần là có thể chạy được ở mọi nơi."*

### Câu 2: Phân biệt JDK, JRE và JVM?
- **Bản chất cốt lõi:** Quan hệ bao hàm: `JDK > JRE > JVM`.
- 🗣️ **Văn mẫu trả lời:**
  > *"Dạ, JVM là máy ảo Java chịu trách nhiệm thực thi Bytecode và quản lý bộ nhớ thông qua Garbage Collector. JRE là môi trường chạy, gồm có JVM cùng với các thư viện chuẩn Core Libraries. Còn JDK là bộ công cụ hoàn chỉnh dành cho lập trình viên, bao gồm toàn bộ JRE cộng thêm trình biên dịch `javac` và các công cụ gỡ lỗi, profiling. Người dùng chỉ cần JRE để chạy app, còn developer bắt buộc phải cài JDK."*

### Câu 3: JIT Compiler là gì và đóng vai trò gì trong JVM?
- **Bản chất cốt lõi:** Just-In-Time Compiler nằm trong Execution Engine của JVM.
- 🗣️ **Văn mẫu trả lời:**
  > *"JIT Compiler là trình biên dịch tức thời. Ban đầu, JVM dùng Interpreter để thông dịch từng dòng bytecode giúp ứng dụng khởi động ngay. Tuy nhiên trong quá trình chạy, JIT sẽ theo dõi và phát hiện những đoạn mã chạy lặp lại nhiều lần - gọi là 'Hot Spot'. JIT sẽ biên dịch trực tiếp các đoạn mã đó sang mã máy gốc và lưu vào bộ nhớ cache. Nhờ vậy ở các lần thực thi tiếp theo, CPU xử lý trực tiếp mã máy mà không cần thông dịch lại, giúp tốc độ của Java tiệm cận với C++."*

### Câu 4: Phân biệt ép kiểu Widening và Narrowing trong Java?
- **Bản chất cốt lõi:** Widening (Nới rộng) tự động, an toàn; Narrowing (Thu hẹp) thủ công, tiềm ẩn mất mát dữ liệu hoặc tràn số.
- 🗣️ **Văn mẫu trả lời:**
  > *"Ép kiểu nới rộng là chuyển từ kiểu dữ liệu nhỏ sang lớn hơn, ví dụ từ `int` sang `double`. Quá trình này diễn ra hoàn toàn tự động và an toàn tuyệt đối. Ngược lại, ép kiểu thu hẹp là chuyển từ kiểu lớn về kiểu nhỏ hơn, ví dụ từ `double` về `int`. Quá trình này bắt buộc phải ép kiểu tường minh bằng dấu ngoặc đơn vì nó có nguy cơ làm mất phần thập phân hoặc gây tràn số nếu giá trị vượt quá miền của kiểu đích."*

### Câu 5: Phân biệt toán tử logic `&` vs `&&` và `|` vs `||`?
- **Bản chất cốt lõi:** Ngắn mạch (Short-circuit) vs Không ngắn mạch.
- 🗣️ **Văn mẫu trả lời:**
  > *"Toán tử `&&` và `||` là toán tử ngắn mạch (short-circuit). Đối với `&&`, nếu vế trái sai thì JVM lập tức dừng lại và trả về false mà không thèm tính vế phải. Tương tự với `||`, vế trái đúng là dừng ngay. Còn toán tử đơn `&` và `|` luôn luôn bắt buộc tính toán cả hai vế bất kể kết quả vế trái là gì. Trong thực tế, ta luôn ưu tiên dùng `&&` để tránh lỗi NullPointerException, ví dụ như kiểm tra `user != null && user.isValid()`."*

### Câu 6: Phân biệt `++i` và `i++`?
- **Bản chất cốt lõi:** Thứ tự ưu tiên tăng giá trị và trả về giá trị biểu thức.
- 🗣️ **Văn mẫu trả lời:**
  > *"Điểm khác biệt nằm ở thời điểm tăng giá trị. Với `++i` (tiền tố), biến sẽ được tăng lên 1 đơn vị trước, sau đó giá trị mới mới được đưa vào biểu thức tính toán. Với `i++` (hậu tố), giá trị hiện tại của biến được đưa vào tính toán trước, sau khi hoàn thành câu lệnh thì giá trị của biến mới được tăng lên 1. Nói ngắn gọn: Tiền tố là Tăng trước - Dùng sau, Hậu tố là Dùng trước - Tăng sau."*

### Câu 7: Switch-case trong Java hỗ trợ những kiểu dữ liệu nào? Lỗi Fall-through là gì?
- **Bản chất cốt lõi:** Kiểu số nguyên nhỏ (`byte`, `short`, `char`, `int`), `String`, `enum`, Wrapper classes. Lỗi Fall-through do thiếu `break`.
- 🗣️ **Văn mẫu trả lời:**
  > *"Switch-case trong Java hỗ trợ các kiểu số nguyên từ `int` trở xuống gồm byte, short, char, int, cùng với các Wrapper class tương ứng, kiểu Enum, và từ Java 7 trở đi đã hỗ trợ thêm kiểu String. Lưu ý là không hỗ trợ kiểu số thực `float`, `double` và `boolean`. Còn lỗi Fall-through xảy ra khi chúng ta quên từ khóa `break` ở cuối mỗi case, khiến luồng thực thi sẽ tự động 'rơi' xuống và chạy tiếp toàn bộ các case bên dưới cho đến khi gặp break tiếp theo hoặc hết switch."*

### Câu 8: Phân biệt 3 từ khóa `break`, `continue` và `return`?
- **Bản chất cốt lõi:** Phạm vi ảnh hưởng: vòng lặp/switch vs lần lặp hiện tại vs toàn bộ hàm.
- 🗣️ **Văn mẫu trả lời:**
  > *"`break` dùng để thoát ngay lập tức khỏi cấu trúc vòng lặp hoặc switch gần nhất. `continue` thì không thoát vòng lặp mà chỉ bỏ qua các câu lệnh còn lại của lần lặp hiện tại để nhảy sang lần lặp kế tiếp. Còn `return` là lệnh kết thúc hoàn toàn phương thức hiện tại và có thể trả về một giá trị cho nơi gọi nó."*

---

## Nhóm 2: Mảng & Cấu trúc Dữ liệu cơ sở (Câu 9 - 12)

### Câu 9: Mảng trong Java được lưu ở đâu trong bộ nhớ RAM?
- **Bản chất cốt lõi:** Luôn là Object trên **Heap**.
- 🗣️ **Văn mẫu trả lời:**
  > *"Trong Java, khác hoàn toàn với C/C++, mảng LUÔN LUÔN được coi là một Đối tượng (Object) và được cấp phát trên bộ nhớ HEAP. Kể cả khi bạn khai báo mảng các số nguyên nguyên thủy như `int[] arr = new int[5]`, thì toàn bộ 5 phần tử đó vẫn nằm trên Heap. Biến `arr` mà ta khai báo trong hàm thực chất chỉ là một biến tham chiếu nằm trên Stack, lưu địa chỉ trỏ sang đối tượng mảng trên Heap."*

### Câu 10: Phân biệt Shallow Copy và Deep Copy đối với mảng trong Java?
- **Bản chất cốt lõi:** Copy tham chiếu vs Nhân bản toàn bộ đối tượng con bên trong.
- 🗣️ **Văn mẫu trả lời:**
  > *"Shallow Copy (sao chép nông) là chỉ sao chép cấu trúc mảng và các giá trị ô nhớ. Nếu mảng chứa các đối tượng Object, thì mảng mới vẫn trỏ chung tới các đối tượng cũ; sửa thuộc tính của một phần tử ở mảng mới sẽ làm mảng cũ bị đổi theo. Ngược lại, Deep Copy (sao chép sâu) là tạo ra một mảng hoàn toàn mới và nhân bản độc lập từng đối tượng con bên trong nó, đảm bảo hai mảng tách biệt 100% trong bộ nhớ."*

### Câu 11: Tại sao kích thước của mảng trong Java lại cố định (Fixed-size)?
- **Bản chất cốt lõi:** Yêu cầu các ô nhớ liên tiếp nhau trong RAM để đạt tốc độ truy xuất $O(1)$.
- 🗣️ **Văn mẫu trả lời:**
  > *"Mảng được thiết kế có kích thước cố định vì nó cần một dải các ô nhớ liên tiếp nhau trên Heap. Nhờ các ô nhớ nằm sát nhau và có cùng kích thước byte, JVM có thể tính toán vị trí của phần tử thứ $i$ trong tích tắc qua công thức: `Địa chỉ phần tử = Địa chỉ gốc + i * Kích thước kiểu dữ liệu`, đạt độ phức tạp $O(1)$. Nếu cho phép mảng tự động phình to, các ô nhớ kế bên có thể đã bị đối tượng khác chiếm dụng, buộc phải copy sang vùng nhớ khác, làm mất đi tính chất tối ưu nguyên bản của mảng."*

### Câu 12: Mảng răng cưa (Jagged Array) trong Java là gì?
- **Bản chất cốt lõi:** Mảng của các mảng, các hàng có kích thước cột khác nhau.
- 🗣️ **Văn mẫu trả lời:**
  > *"Trong Java, mảng 2 chiều bản chất là 'mảng chứa các mảng con'. Do đó, Java cho phép các mảng con này có độ dài không bằng nhau, tạo thành mảng răng cưa (Jagged Array). Ví dụ hàng thứ nhất có thể có 2 cột, nhưng hàng thứ hai lại có 5 cột. Điều này giúp tối ưu hóa bộ nhớ khi lưu trữ dữ liệu dạng tam giác hoặc dữ liệu có độ dài linh hoạt."*

---

## Nhóm 3: Lập trình Hướng đối tượng OOP & Quản lý Bộ nhớ (Câu 13 - 23)

### Câu 13: Phân biệt Class và Object?
- **Bản chất cốt lõi:** Bản thiết kế (Blueprint) trên Metaspace vs Thực thể (Instance) trên Heap.
- 🗣️ **Văn mẫu trả lời:**
  > *"Class là một bản thiết kế, khuôn mẫu định nghĩa các thuộc tính và hành vi chung. Nó được nạp vào vùng nhớ Metaspace. Còn Object là một thực thể cụ thể được sinh ra từ khuôn mẫu Class đó thông qua từ khóa `new` và chiếm bộ nhớ thực tế trên Heap. Ví dụ: Class là bản vẽ thiết kế chiếc xe hơi, còn Object là chiếc xe VinFast cụ thể màu đỏ đang lăn bánh trên đường."*

### Câu 14: Java là Pass-By-Value hay Pass-By-Reference? Hãy chứng minh?
- **Bản chất cốt lõi:** **100% LUÔN LÀ PASS-BY-VALUE**. Tham chiếu truyền vào thực chất là copy giá trị con trỏ địa chỉ.
- 🗣️ **Văn mẫu trả lời ghi điểm tuyệt đối:**
  > *"Em khẳng định chắc chắn 100% rằng Java CHỈ CÓ Pass-by-Value, hoàn toàn không có Pass-by-reference. Khi ta truyền một đối tượng vào phương thức, Java sẽ copy giá trị của biến tham chiếu đó — tức là copy địa chỉ ô nhớ. Vì biến copy và biến gốc cùng trỏ tới một vùng nhớ trên Heap, nên nếu ta dùng biến copy để gọi setter sửa đổi thuộc tính đối tượng thì dữ liệu trên Heap sẽ bị thay đổi. Tuy nhiên, nếu ta gán biến copy đó cho một đối tượng mới (`p = new Person()`), thì ta chỉ đang đổi con trỏ bản sao cục bộ trong Stack Frame của hàm đó, con trỏ gốc bên ngoài hàm hoàn toàn không bị ảnh hưởng. Đây chính là bằng chứng xác thực chứng minh Java chỉ truyền bản sao giá trị."*

### Câu 15: Phân biệt vùng nhớ Stack và Heap trong JVM?
- **Bản chất cốt lõi:** Stack lưu Frame lời gọi hàm và biến cục bộ (Thread riêng biệt); Heap lưu mọi Object và Array (Dùng chung).
- 🗣️ **Văn mẫu trả lời:**
  > *"Vùng nhớ Stack gắn liền với vòng đời của từng Thread. Mỗi khi một hàm được gọi, một Stack Frame được tạo ra để chứa các biến nguyên thủy cục bộ và các biến con trỏ tham chiếu. Khi hàm chạy xong, Stack Frame tự động bị giải phóng tức thì, tốc độ cực nhanh. Ngược lại, Heap là vùng nhớ dùng chung cho toàn bộ các Thread trong ứng dụng, là nơi chứa toàn bộ các Object và Mảng được tạo bằng từ khóa `new`. Bộ nhớ Heap được quản lý và thu gom rác tự động bởi Garbage Collector."*

### Câu 16: Từ khóa `static` trong Java hoạt động như thế nào? Biến static được lưu ở đâu?
- **Bản chất cốt lõi:** Thuộc về Class chứ không thuộc về instance, lưu trong Metaspace, nạp khi Class nạp vào JVM.
- 🗣️ **Văn mẫu trả lời:**
  > *"Từ khóa `static` quy định rằng biến hoặc phương thức đó thuộc về toàn bộ Class chứ không phụ thuộc vào bất kỳ đối tượng (instance) cụ thể nào. Biến static được cấp phát bộ nhớ một lần duy nhất trong Metaspace khi Class được nạp vào và được chia sẻ chung cho mọi instance. Do thuộc tính static tồn tại độc lập với đối tượng, nên trong phương thức static ta tuyệt đối không thể truy cập trực tiếp các biến hay hàm non-static, và cũng không thể sử dụng từ khóa `this` hay `super`."*

### Câu 17: Khi nào thì một Object bị Garbage Collector (GC) thu gom?
- **Bản chất cốt lõi:** Khi đối tượng trở thành "Unreachable" (không còn bất kỳ tham chiếu gốc nào trỏ tới).
- 🗣️ **Văn mẫu trả lời:**
  > *"Một đối tượng trên Heap sẽ được GC đánh dấu để thu gom khi nó trở thành 'Unreachable', nghĩa là không còn bất kỳ đường dẫn tham chiếu nào từ các rễ GC Roots (như biến cục bộ trên Stack, biến static, hoặc luồng đang chạy) trỏ tới nó nữa. Điển hình là khi ta gán biến tham chiếu bằng `null`, hoặc khi một biến tham chiếu cục bộ ra khỏi phạm vi kết thúc hàm."*

### Câu 18: Phân biệt `StackOverflowError` và `OutOfMemoryError`?
- **Bản chất cốt lõi:** Tràn bộ nhớ Stack (do đệ quy sâu) vs Cạn kiệt bộ nhớ Heap (do rò rỉ bộ nhớ/tạo quá nhiều object).
- 🗣️ **Văn mẫu trả lời:**
  > *"`StackOverflowError` xảy ra trên vùng nhớ Stack, thường do lỗi đệ quy vô tận làm các Stack Frame xếp chồng lên nhau quá giới hạn kích thước Stack. Còn `OutOfMemoryError` xảy ra trên vùng nhớ Heap khi ứng dụng liên tục tạo mới các đối tượng nhưng chúng vẫn bị giữ tham chiếu (Memory Leak), khiến GC không thể giải phóng được, dẫn đến việc JVM hết sạch bộ nhớ Heap để cấp phát cho đối tượng mới."*

### Câu 19: Trình bày ngắn gọn 4 tính chất cốt lõi của OOP?
- **Bản chất cốt lõi:** Đóng gói, Kế thừa, Đa hình, Trừu tượng.
- 🗣️ **Văn mẫu trả lời trôi chảy:**
  > *"Dạ, 4 tính chất cốt lõi của OOP bao gồm:
  > 1. **Đóng gói (Encapsulation):** Che giấu dữ liệu nội bộ qua biến `private` và cung cấp cổng giao tiếp an toàn qua `getter/setter`.
  > 2. **Kế thừa (Inheritance):** Cho phép lớp con tái sử dụng mã nguồn của lớp cha qua quan hệ IS-A (`extends`).
  > 3. **Đa hình (Polymorphism):** Cùng một hành vi nhưng thực thi khác nhau tùy đối tượng thực tế, gồm Đa hình tĩnh (Overloading) và Đa hình động (Overriding).
  > 4. **Trừu tượng (Abstraction):** Tập trung vào việc đối tượng 'làm cái gì' thay vì 'làm như thế nào', được triển khai qua Abstract Class và Interface."*

### Câu 20: Phân biệt Method Overloading và Method Overriding?
- **Bản chất cốt lõi:** Compile-time vs Runtime; Cùng class (khác tham số) vs Lớp con ghi đè lớp cha (giống tham số).
- 🗣️ **Văn mẫu trả lời:**
  > *"Overloading là đa hình lúc biên dịch (Compile-time), xảy ra trong cùng một Class khi các hàm có cùng tên nhưng khác nhau về danh sách tham số. Còn Overriding là đa hình lúc chạy (Runtime), xảy ra giữa lớp con và lớp cha khi lớp con viết lại phương thức của lớp cha với tên hàm, danh sách tham số và kiểu trả về tương thích, dùng annotation `@Override`."*

### Câu 21: Phân biệt Upcasting và Downcasting? Cạm bẫy là gì?
- **Bản chất cốt lõi:** Ép lên cha (an toàn) vs Ép xuống con (tiềm ẩn `ClassCastException`).
- 🗣️ **Văn mẫu trả lời:**
  > *"Upcasting là ép kiểu biến tham chiếu từ lớp con lên lớp cha, ví dụ `Animal a = new Dog()`. Thao tác này luôn luôn an toàn và diễn ra tự động. Ngược lại, Downcasting là ép kiểu ngược từ lớp cha xuống lớp con, ví dụ `Dog d = (Dog) a`. Thao tác này tiềm ẩn rủi ro ném lỗi `ClassCastException` nếu đối tượng thực tế trên Heap không phải là lớp con đó. Vì vậy khi Downcasting, nguyên tắc bắt buộc là phải kiểm tra trước bằng toán tử `instanceof`."*

### Câu 22: Phân biệt Abstract Class và Interface? Khi nào dùng cái nào?
- **Bản chất cốt lõi:** Quan hệ IS-A (Bản sắc chung) vs Quan hệ CAN-DO (Hợp đồng hành vi).
- 🗣️ **Văn mẫu trả lời chuẩn chuyên gia:**
  > *"Về bản chất thiết kế: Abstract Class đại diện cho mối quan hệ IS-A, dùng khi các lớp con có chung bản sắc cốt lõi và muốn chia sẻ lại mã nguồn cũng như biến trạng thái. Trong khi đó, Interface đại diện cho quan hệ CAN-DO, đóng vai trò như một bản hợp đồng hành vi cho các đối tượng hoàn toàn khác nhau cùng tuân thủ.
  > Về mặt cú pháp: Java chỉ cho phép đơn kế thừa một Abstract Class nhưng cho phép đa triển khai nhiều Interface. Abstract Class có thể chứa constructor và biến instance bình thường, còn Interface chỉ chứa hằng số `public static final` và không có constructor."*

### Câu 23: Tại sao Java không hỗ trợ Đa kế thừa (Multiple Inheritance) đối với Class?
- **Bản chất cốt lõi:** Tránh vấn đề xung đột hình thoi (Diamond Problem).
- 🗣️ **Văn mẫu trả lời:**
  > *"Java cấm đa kế thừa class để tránh 'Bài toán hình thoi' (Diamond Problem). Hãy tưởng tượng nếu Class A có phương thức `display()`, hai class con B và C cùng kế thừa A và ghi đè phương thức này. Nếu class D được phép kế thừa cùng lúc cả B và C, thì khi D gọi `display()`, JVM sẽ không thể biết nên thực thi mã của B hay của C, gây ra mơ hồ. Tuy nhiên với Interface, do Interface ban đầu không chứa code triển khai nên việc một lớp implement nhiều interface là hoàn toàn không bị xung đột."*

---

## Nhóm 4: Xử lý Chuỗi, Wrapper Class & Ngoại lệ (Câu 24 - 32)

### Câu 24: Tại sao String trong Java lại được thiết kế Bất biến (Immutable)?
- **Bản chất cốt lõi:** Tiết kiệm bộ nhớ (String Pool), An toàn đa luồng (Thread-safe), Bảo mật hệ thống (Security).
- 🗣️ **Văn mẫu trả lời:**
  > *"String được thiết kế bất biến vì 3 lý do tối quan trọng:
  > 1. **Tiết kiệm bộ nhớ:** Cho phép cơ chế String Pool hoạt động, nhiều biến có thể trỏ chung vào một chuỗi mà không sợ bị biến khác làm thay đổi.
  > 2. **Bảo mật:** String được dùng làm tham số mở kết nối database, mở file, socket mạng. Nếu chuỗi bị thay đổi lén lút thì sẽ gây ra lỗ hổng bảo mật nghiêm trọng.
  > 3. **An toàn đa luồng (Thread-Safety):** Một đối tượng không thể thay đổi trạng thái thì các thread có thể đọc đồng thời mà không bao giờ gặp xung đột dữ liệu."*

### Câu 25: String Pool là gì? Phân biệt `String s = "abc"` và `String s = new String("abc")`?
- **Bản chất cốt lõi:** Tái sử dụng trong Constant Pool vs Cấp phát Object mới trên Heap.
- 🗣️ **Văn mẫu trả lời:**
  > *"String Pool là một vùng nhớ đặc biệt nằm trong Heap dùng để lưu trữ và tái sử dụng các chuỗi ký tự. Khi ta khai báo bằng literal `String s = \"abc\"`, JVM sẽ kiểm tra trong String Pool trước: nếu đã có chuỗi này rồi thì trả về tham chiếu có sẵn, nếu chưa có mới tạo mới. Còn khi dùng `new String(\"abc\")`, JVM bắt buộc phải cấp phát một đối tượng String hoàn toàn mới ở vùng Heap thông thường nằm ngoài Pool, gây lãng phí bộ nhớ."*

### Câu 26: Phân biệt `String`, `StringBuilder` và `StringBuffer`?
- **Bản chất cốt lõi:** Immutable vs Mutable; Single-thread (nhanh) vs Thread-safe (đồng bộ hóa).
- 🗣️ **Văn mẫu trả lời:**
  > *"`String` là bất biến, mỗi lần nối chuỗi sẽ tạo ra đối tượng mới, chỉ nên dùng khi dữ liệu ít biến động. Khi cần thao tác nối chuỗi nhiều lần trong vòng lặp, ta phải dùng `StringBuilder` hoặc `StringBuffer` vì chúng là khả biến (Mutable), có thể sửa đổi trực tiếp trên mảng ký tự bên trong mà không tạo rác. Điểm khác nhau giữa hai bộ đệm này là: `StringBuilder` không có đồng bộ hóa nên tốc độ chạy nhanh nhất, tối ưu cho môi trường đơn luồng. Còn `StringBuffer` có các phương thức gắn `synchronized` nên an toàn đa luồng (Thread-Safe) nhưng tốc độ chậm hơn."*

### Câu 27: Phân biệt toán tử `==` và phương thức `.equals()`?
- **Bản chất cốt lõi:** So sánh địa chỉ ô nhớ vs So sánh nội dung giá trị.
- 🗣️ **Văn mẫu trả lời:**
  > *"Toán tử `==` so sánh địa chỉ ô nhớ giữa hai biến đối tượng xem chúng có trỏ chung vào một thực thể trên Heap hay không. Còn phương thức `.equals()` là phương thức của lớp `Object` dùng để so sánh nội dung logic bên trong. Mặc định phương thức `.equals()` của `Object` cũng so sánh địa chỉ bằng `==`, nhưng các lớp như `String`, `Integer`, `Date` đã ghi đè lại phương thức này để so sánh giá trị nội dung ký tự hoặc con số."*

### Câu 28: Hợp đồng (Contract) giữa `equals()` và `hashCode()` là gì? Tại sao khi override `equals()` BẮT BUỘC phải override `hashCode()`?
- **Bản chất cốt lõi:** Hai object `equals == true` thì `hashCode` PHẢI bằng nhau. Nếu không, `HashMap`/`HashSet` sẽ bị lỗi hỏng logic.
- 🗣️ **Văn mẫu trả lời ghi điểm tuyệt đối:**
  > *"Hợp đồng giữa `equals()` và `hashCode()` quy định rằng: nếu hai đối tượng bằng nhau theo phương thức `equals()`, thì giá trị mã băm `hashCode()` của chúng BẮT BUỘC phải bằng nhau.
  > Ta phải override cả hai vì các cấu trúc dữ liệu bảng băm như `HashMap` hay `HashSet` luôn dùng `hashCode()` trước để xác định thùng (Bucket) chứa phần tử. Nếu ta chỉ override `equals()` mà quên override `hashCode()`, hai đối tượng có cùng nội dung logic sẽ sinh ra hai mã hash khác nhau và bị đẩy vào hai bucket khác nhau. Khi đó, `map.get(key)` hoặc `set.contains(item)` sẽ không thể tìm thấy dữ liệu, hoặc tệ hơn là cho phép lưu 2 phần tử trùng nhau vào Set."*

### Câu 29: Autoboxing và Unboxing là gì? Giải thích cạm bẫy Integer Cache?
- **Bản chất cốt lõi:** Tự động chuyển đổi giữa Primitive và Wrapper. Cache số nguyên từ $-128$ đến $127$.
- 🗣️ **Văn mẫu trả lời:**
  > *"Autoboxing là cơ chế trình biên dịch tự động chuyển kiểu nguyên thủy sang Wrapper Object (ví dụ `int` sang `Integer`), còn Unboxing là chiều ngược lại.
  > Cạm bẫy lớn nhất là Integer Cache: Java tự động lưu sẵn trong bộ nhớ đệm các đối tượng Integer từ -128 đến 127. Vì vậy, nếu ta khai báo hai biến Integer cùng bằng 100 rồi so sánh bằng `==` thì kết quả ra `true` do trỏ chung object trong cache. Nhưng nếu khai báo bằng 200, vượt quá giới hạn 127, JVM sẽ tạo 2 object mới riêng biệt khiến phép so sánh `==` trả về `false`. Do đó, nguyên tắc vàng là luôn dùng `.equals()` khi so sánh các Wrapper Class."*

### Câu 30: Lỗi `ConcurrentModificationException` là gì? Cách khắc phục?
- **Bản chất cốt lõi:** Xảy ra do cơ chế Fail-Fast khi xóa phần tử Collection trong lúc đang duyệt for-each.
- 🗣️ **Văn mẫu trả lời:**
  > *"Lỗi này xảy ra khi ta đang duyệt một Collection bằng vòng lặp `for-each` mà lại gọi trực tiếp phương thức `list.remove()`. Danh sách có biến đếm `modCount` ghi nhận số lần thay đổi cấu trúc, khi Iterator phát hiện `modCount` bị thay đổi ngoài ý muốn thì nó kích hoạt cơ chế Fail-Fast ném lỗi ngay để tránh sai lệch dữ liệu. Để khắc phục, ta có hai cách chuẩn: một là dùng phương thức `iterator.remove()` của chính Iterator, hai là dùng cú pháp ngắn gọn của Java 8: `list.removeIf(condition)`."*

### Câu 31: Phân biệt Checked Exception và Unchecked Exception?
- **Bản chất cốt lõi:** Bắt buộc xử lý lúc compile-time vs Lỗi logic runtime không ép buộc.
- 🗣️ **Văn mẫu trả lời:**
  > *"Checked Exception là các ngoại lệ kế thừa trực tiếp từ `Exception` (ngoại trừ RuntimeException), ví dụ như `IOException`, `SQLException`. Trình biên dịch bắt buộc lập trình viên phải xử lý ngay khi viết code bằng cách bọc `try-catch` hoặc khai báo `throws` ở chữ ký hàm. Còn Unchecked Exception kế thừa từ `RuntimeException`, thường là các lỗi do sơ suất logic của lập trình viên như `NullPointerException`, `ArrayIndexOutOfBoundsException`. Trình biên dịch không bắt buộc phải khai báo những lỗi này."*

### Câu 32: Phân biệt bộ ba `final`, `finally` và `finalize()`?
- **Bản chất cốt lõi:** Modifier vs Block dọn dẹp vs Method của Object dọn rác (deprecated).
- 🗣️ **Văn mẫu trả lời:**
  > *"`final` là một từ khóa hạn chế: gắn với biến biến thành hằng số, gắn với hàm thì cấm ghi đè (@Override), gắn với class thì cấm kế thừa. `finally` là khối lệnh đi kèm sau khối `try-catch`, luôn luôn được thực thi dù có ngoại lệ hay không để giải phóng tài nguyên. Còn `finalize()` là một phương thức của lớp `Object`, được GC gọi trước khi thu gom đối tượng, nhưng phương thức này đã bị khai tử (deprecated) từ Java 9 vì gây rò rỉ bộ nhớ và không thể kiểm soát thời gian chạy."*

---

## Nhóm 5: Generics, Java 8+ & Sắp xếp Collection (Câu 33 - 40)

### Câu 33: Khối lệnh `try-with-resources` hoạt động như thế nào?
- **Bản chất cốt lõi:** Tự động gọi `.close()` của interface `AutoCloseable`.
- 🗣️ **Văn mẫu trả lời:**
  > *"`try-with-resources` được giới thiệu từ Java 7, cho phép khai báo các tài nguyên như file stream, database connection ngay trong dấu ngoặc tròn sau từ khóa `try`. Điểm tuyệt vời của nó là khi kết thúc khối try (dù chạy thành công hay có exception), JVM sẽ tự động gọi phương thức `.close()` để giải phóng tài nguyên đó. Điều kiện duy nhất là các tài nguyên này bắt buộc phải cài đặt (implement) interface `AutoCloseable`."*

### Câu 34: Generics là gì? Trình bày nguyên lý PECS trong Wildcards?
- **Bản chất cốt lõi:** Kiểm tra kiểu lúc biên dịch (Compile-time Type Safety). PECS = Producer Extends, Consumer Super.
- 🗣️ **Văn mẫu trả lời:**
  > *"Generics cho phép tham số hóa kiểu dữ liệu, giúp bắt lỗi kiểu ngay tại thời điểm biên dịch và loại bỏ việc ép kiểu thủ công.
  > Khi làm việc với ký tự đại diện Wildcard `?`, ta có nguyên lý vàng **PECS: Producer Extends, Consumer Super**. Nếu cấu trúc dữ liệu đóng vai trò là nơi 'sản xuất' hay cung cấp dữ liệu ra để ta chỉ đọc, hãy dùng `<? extends T>`. Còn nếu cấu trúc dữ liệu đóng vai trò là nơi 'tiêu thụ' để ta thêm dữ liệu vào, hãy dùng `<? super T>`."*

### Câu 35: Cơ chế Type Erasure (Xóa kiểu) trong Generics là gì?
- **Bản chất cốt lõi:** Xóa bỏ tham số kiểu lúc dịch ra bytecode để tương thích ngược.
- 🗣️ **Văn mẫu trả lời:**
  > *"Type Erasure là cơ chế mà trình biên dịch Java chỉ sử dụng thông tin Generics để kiểm tra tính an toàn kiểu lúc compile-time. Khi dịch sang file mã máy nhị phân Bytecode `.class`, JVM sẽ xóa toàn bộ các định nghĩa kiểu như `<String>`, `<Integer>` và thay thế chúng bằng kiểu thô `Object`. Điều này được Java thiết kế nhằm đảm bảo khả năng tương thích ngược hoàn toàn với các phiên bản Java 1.4 trở về trước."*

### Câu 36: Phân biệt `Comparable` và `Comparator` khi sắp xếp đối tượng trong Java?
- **Bản chất cốt lõi:** Sắp xếp tự nhiên nội tại (`compareTo`) vs Chiến lược sắp xếp tùy biến bên ngoài (`compare`).
- 🗣️ **Văn mẫu trả lời:**
  > *"`Comparable` nằm trong gói `java.lang`, dùng phương thức `compareTo()` được cài đặt trực tiếp bên trong Class đối tượng để định nghĩa thứ tự sắp xếp tự nhiên mặc định (ví dụ Student sắp xếp mặc định theo ID). Còn `Comparator` nằm trong gói `java.util`, dùng phương thức `compare()` để tạo ra các tiêu chí sắp xếp linh hoạt từ bên ngoài (ví dụ sắp xếp theo tên, theo điểm GPA). Khi dùng Java 8, `Comparator` có thể kết hợp cực kỳ mạnh mẽ với Lambda như `Comparator.comparing(Student::getGpa)`."*

### Câu 37: Tại sao từ Java 8 Interface lại được bổ sung `default method`?
- **Bản chất cốt lõi:** Mở rộng tính năng Interface có sẵn mà không làm vỡ code các lớp kế thừa cũ.
- 🗣️ **Văn mẫu trả lời:**
  > *"Trước Java 8, nếu ta thêm một phương thức mới vào một Interface có sẵn, tất cả các lớp con đang implement interface đó đều bị lỗi biên dịch vì chưa ghi đè hàm mới. Do đó, Java 8 bổ sung `default method` (phương thức có sẵn thân hàm) để các thư viện nền tảng có thể bổ sung các hàm mới như `.forEach()`, `.stream()` vào interface `Collection` mà không làm hỏng hàng triệu dòng code cũ của các dự án trên thế giới."*

### Câu 38: Functional Interface là gì? Kể tên 4 Functional Interface cốt lõi?
- **Bản chất cốt lõi:** Interface có đúng 1 abstract method (SAM). Predicate, Function, Consumer, Supplier.
- 🗣️ **Văn mẫu trả lời:**
  > *"Functional Interface là interface chỉ có duy nhất một phương thức trừu tượng, được đánh dấu bằng `@FunctionalInterface`. Có 4 interface cốt lõi trong gói `java.util.function`:
  > 1. `Predicate<T>`: nhận vào T, trả về boolean $\to$ dùng kiểm tra điều kiện lọc.
  > 2. `Function<T, R>`: nhận vào T, trả về R $\to$ dùng biến đổi đối tượng.
  > 3. `Consumer<T>`: nhận vào T, không trả về gì $\to$ dùng in ấn hoặc lưu trữ.
  > 4. `Supplier<T>`: không nhận gì, trả về T $\to$ dùng cấp phát hoặc khởi tạo lười đối tượng mới."*

### Câu 39: Stream API là gì? Phân biệt Intermediate Operation và Terminal Operation? Lazy Evaluation là gì?
- **Bản chất cốt lõi:** Xử lý dữ liệu kiểu khai báo. Intermediate trả về Stream mới (chưa chạy); Terminal kích hoạt đường ống chạy thật (Lazy evaluation).
- 🗣️ **Văn mẫu trả lời ghi điểm tuyệt đối:**
  > *"Stream API mang tư duy lập trình hàm vào việc xử lý tập hợp dữ liệu. Thao tác trong Stream chia làm 2 loại:
  > - **Intermediate Operations** (như `filter`, `map`, `sorted`): Luôn trả về một Stream mới.
  > - **Terminal Operations** (như `collect`, `forEach`, `count`): Đóng đường ống và trả về kết quả cụ thể.
  > Điểm đặc biệt nhất của Stream là cơ chế **Lazy Evaluation (Tính toán lười)**: các thao tác trung gian sẽ KHÔNG HỀ THỰC THI khi được khai báo. Chúng chỉ thực sự chạy khi một Terminal Operation được gọi đến để kích hoạt toàn bộ đường ống, giúp tối ưu hóa hiệu năng tối đa."*

### Câu 40: `Optional` trong Java là gì? Tại sao không nên lạm dụng phương thức `.get()`?
- **Bản chất cốt lõi:** Khai tử `NullPointerException`. Gọi `.get()` khi rỗng sẽ ném lỗi `NoSuchElementException`.
- 🗣️ **Văn mẫu trả lời:**
  > *"`Optional<T>` là chiếc hộp bọc lấy đối tượng, có thể chứa giá trị hoặc rỗng nhằm buộc lập trình viên phải chủ động xử lý trường hợp giá trị bị `null`.
  > Ta tuyệt đối không nên gọi trực tiếp phương thức `.get()` một cách mù quáng, vì nếu đối tượng bên trong là rỗng (`Optional.empty()`), nó sẽ ném ra lỗi `NoSuchElementException`, khiến mục tiêu ban đầu của Optional bị phá sản hoàn toàn. Thay vào đó, ta nên dùng các hàm an toàn theo phong cách functional như `.map()`, `.filter()`, `.orElse(defaultValue)` hoặc `.orElseThrow()`."*

---

> [!NOTE]
> 🎯 **Lời khuyên thực chiến:** Hãy đọc đi đọc lại mục **🗣️ Văn mẫu trả lời** của 40 câu hỏi trên, luyện phản xạ nói to thành tiếng. Sự kết hợp giữa **hiểu sâu bản chất kỹ thuật** và **diễn đạt tự tin, gãy gọn** chính là chìa khóa vàng giúp bạn chinh phục mọi vòng phỏng vấn kỹ thuật Java Core!
