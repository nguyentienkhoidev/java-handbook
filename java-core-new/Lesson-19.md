# Lesson 19: Lập trình Đa luồng (Multithreading) và Concurrency trong Java

Bài học này sẽ đưa bạn từ những khái niệm cơ bản nhất cho đến các công cụ xử lý đa luồng (Concurrency) phức tạp. Đa luồng là một chủ đề "khó nhằn" trong Java, nhưng đừng lo, chúng ta sẽ tiếp cận nó bằng những **ví dụ đời thực dễ hiểu nhất** dành cho người chưa biết gì!

---

## 1. Khái niệm cơ bản (Hiểu theo Đời thực)

Để dễ hình dung, bạn đừng vội nghĩ đến code. Hãy tưởng tượng hệ điều hành máy tính là một khu công nghiệp, trong đó có các **Nhà máy** và các **Công nhân**.

### 1.1. Tiến trình (Process) và Luồng (Thread)
- **Tiến trình (Process) = 1 Nhà máy:** Mỗi phần mềm bạn mở trên máy tính (như trình duyệt Chrome, Zalo, Word) giống như một nhà máy độc lập. Các nhà máy này nằm cách xa nhau, **tuyệt đối không dùng chung** kho hàng của nhau (mỗi ứng dụng có một vùng bộ nhớ RAM riêng).
- **Luồng (Thread) = 1 Công nhân:** Để nhà máy hoạt động, cần phải có công nhân. Một nhà máy có thể có 1 công nhân (Single-thread) hoặc nhiều công nhân (Multi-thread).
  > **Quy tắc cốt lõi:** Tất cả công nhân trong cùng 1 nhà máy đều **dùng chung một cái kho hàng** (dùng chung bộ nhớ Heap của Java).

### 1.2. Tại sao phải dùng Đa luồng (Multi-threading)?
Tưởng tượng bạn đang code một phần mềm nghe nhạc:
- **Nếu chỉ có 1 công nhân (Single-thread):** Anh ta vừa phải đi đọc dữ liệu file nhạc nặng 1GB, vừa phải phục vụ người dùng bấm nút Play/Pause. Lúc anh ta đang bận đọc file ở trong kho, giao diện bên ngoài sẽ bị **đơ hoàn toàn** (người dùng bấm nút mà phần mềm không phản hồi).
- **Nếu dùng Đa luồng (2 công nhân):** Công nhân 1 chỉ tập trung vào việc đọc file. Công nhân 2 chuyên túc trực ở giao diện để phản hồi các nút bấm của người dùng. Phần mềm trở nên cực kỳ mượt mà!

---

## 2. Cách tạo Thread (Tuyển công nhân mới)

Trong Java, có 3 cách phổ biến để "tạo ra một công nhân mới":

### Cách 1: Kế thừa class `Thread` (Cách cổ điển)
```java
// Tạo ra một mẫu Công nhân
class CongNhan extends Thread {
    @Override
    public void run() { // Hàm run() định nghĩa công việc công nhân sẽ làm
        System.out.println("Công nhân đang làm việc: " + Thread.currentThread().getName());
    }
}

public class Main {
    public static void main(String[] args) {
        CongNhan cn1 = new CongNhan();
        // Gọi start() để yêu cầu hệ điều hành chính thức cho công nhân đi làm
        // (TUYỆT ĐỐI KHÔNG gọi trực tiếp hàm run())
        cn1.start(); 
    }
}
```

### Cách 2: Implement interface `Runnable` (Cách khuyên dùng)
Vì Java chỉ cho phép kế thừa 1 class duy nhất, nếu bạn dùng cách 1 thì class của bạn không thể kế thừa class nào khác nữa. Implement `Runnable` giúp mã linh hoạt hơn.

```java
// Chỉ định nghĩa "Công việc" (Task)
class CongViec implements Runnable {
    @Override
    public void run() {
        System.out.println("Đang làm công việc: " + Thread.currentThread().getName());
    }
}

public class Main {
    public static void main(String[] args) {
        // Tạo công nhân và giao công việc cho họ
        Thread cn2 = new Thread(new CongViec());
        cn2.start();
        
        // Cực ngắn gọn nếu dùng Lambda (Java 8+)
        Thread cn3 = new Thread(() -> System.out.println("Công việc Lambda"));
        cn3.start();
    }
}
```

### Cách 3: Implement `Callable` (Khi công nhân cần báo cáo kết quả)
Hàm `run()` của `Runnable` là hàm `void` (không trả về gì cả). Nếu bạn muốn giao việc cho công nhân và bắt họ đem kết quả (ví dụ con số tính toán) về báo cáo, hãy dùng `Callable` kết hợp với `Future`.

```java
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

class TinhToan implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return 10 + 20; // Báo cáo kết quả 30
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        FutureTask<Integer> phieuGiaoViec = new FutureTask<>(new TinhToan());
        Thread cn4 = new Thread(phieuGiaoViec);
        cn4.start();
        
        // Lấy báo cáo. Lệnh get() sẽ bắt luồng chính ĐỨNG CHỜ cho đến khi công nhân làm xong!
        Integer ketQua = phieuGiaoViec.get(); 
        System.out.println("Kết quả là: " + ketQua);
    }
}
```

---

## 3. Vòng đời của Thread (Một ngày làm việc)

Một công nhân trải qua các trạng thái sau:
1. **NEW (Mới sinh):** Vừa gõ `new Thread()` nhưng chưa gọi `start()`. (Đang ký hợp đồng, chưa đi làm).
2. **RUNNABLE (Đang làm):** Đã gọi `start()`. Đang thực sự chạy code hoặc đang chờ hệ điều hành xếp lịch chạy.
3. **BLOCKED (Bị chặn):** Cửa kho hàng bị khóa, công nhân phải đứng chờ ở ngoài.
4. **WAITING (Chờ đợi vô tận):** Đang chờ một công nhân khác ra hiệu (bằng hàm `notify()`).
5. **TIMED_WAITING (Chờ có hẹn giờ):** Đang ngủ trưa (`Thread.sleep(1000)`).
6. **TERMINATED (Chết/Nghỉ việc):** Chạy xong hàm `run()` hoặc code bị lỗi văng ra ngoài. Lúc này không thể bắt công nhân `start()` lại được nữa.

Các hàm hay dùng để điều khiển:
- `Thread.sleep(1000)`: Tạm dừng (ngủ) 1 giây.
- `cn1.join()`: Ép luồng hiện tại phải "đứng yên chờ" công nhân `cn1` làm xong hẳn thì mới đi tiếp.

---

## 4. Vấn đề Đạp chân nhau (Race Condition) và Đồng bộ (Synchronized)

### 4.1. Race Condition là gì?
Vì các công nhân dùng chung kho hàng, sẽ rất dễ xảy ra thảm họa nếu họ không giao tiếp với nhau.
**Ví dụ:** 
- Trong kho còn đúng **1 cái bánh** (`count = 1`). 
- Công nhân A và B cùng lúc bước vào. Cả hai cùng nhìn thấy 1 cái bánh và ghi nhớ vào đầu: "Trong kho có 1 cái". 
- A thò tay lấy bánh ăn (lúc này kho còn 0 cái). 
- Do B chưa kịp cập nhật thông tin, B cũng thò tay vào lấy bánh theo trí nhớ của mình, nhưng vồ vào hư không -> **Chương trình bị lỗi hoặc sai lệch dữ liệu!**

### 4.2. Khắc phục bằng Ổ Khóa (Synchronized)
Để giải quyết, ông chủ ra luật: **Lắp ổ khóa ở cửa kho (Từ khóa `synchronized`).**

```java
class KhoHang {
    int banh = 1;
    
    // Khóa cửa kho: Ai vào hàm này thì cầm chìa khóa, người khác phải đứng chờ ở ngoài
    public synchronized void layBanh() {
        if (banh > 0) {
            System.out.println(Thread.currentThread().getName() + " đang lấy bánh");
            banh--;
        } else {
            System.out.println("Hết bánh rồi!");
        }
    }
}
```
**Quy trình chạy:**
1. A vào hàm `layBanh()`, A lập tức chốt cửa lại (Chiếm Lock).
2. B đến cửa thấy khóa, đành đứng đợi ở ngoài (Trạng thái **BLOCKED**).
3. Khi A lấy xong bánh và bước ra (hết hàm), A trả lại chìa khóa (Nhả Lock). 
4. B chui vào kho, lúc này B sẽ thấy số bánh = 0 một cách chính xác. Mọi thứ an toàn!

*(Lưu ý: Bạn cũng có thể dùng `synchronized(this) { ... }` để chỉ khóa một đoạn code nhỏ thay vì khóa cả hàm để tăng tốc độ).*

---

## 5. Thread Pool (Biệt đội công nhân) - BẮT BUỘC DÙNG KHI ĐI LÀM

Trong thực tế, **TUYỆT ĐỐI KHÔNG** được tạo công nhân vô tội vạ (`new Thread().start()`). Nếu có 1000 khách hàng truy cập web của bạn, bạn tuyển ngay 1000 công nhân thì bộ nhớ RAM sẽ quá tải và sập toàn bộ Server.

**Giải pháp: Thread Pool (Hồ chứa Thread)**.
Thay vì thuê mới, ông chủ chỉ tuyển **cố định 10 công nhân tinh nhuệ**. Có 1000 khách đến thì bắt khách xếp hàng. 10 công nhân này sẽ làm việc quần quật, xong khách này thì tiếp khách khác. Rất an toàn, ổn định!

Java hỗ trợ việc này qua `ExecutorService`:

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        // Tạo một biệt đội cố định có đúng 3 công nhân
        ExecutorService bietDoi = Executors.newFixedThreadPool(3);
        
        // Ném cho họ 10 công việc (task)
        for (int i = 1; i <= 10; i++) {
            final int task = i;
            bietDoi.submit(() -> {
                System.out.println("Công nhân " + Thread.currentThread().getName() + " đang làm task " + task);
                try { Thread.sleep(1000); } catch (Exception e) {} // Giả lập mất 1s để làm
            });
        }
        
        // Đóng cửa (Không nhận thêm việc, nhưng vẫn chạy cho xong các việc đang làm dở)
        bietDoi.shutdown(); 
    }
}
```

---

## 6. Deadlock (Khóa chết)

Đây là lỗi nguy hiểm và đau đầu nhất của Đa luồng:
- Công nhân A đang giữ "Chìa khóa Tủ", và đứng đợi mượn "Chìa khóa Két sắt" để làm việc.
- Công nhân B đang giữ "Chìa khóa Két sắt", và đứng đợi mượn "Chìa khóa Tủ" để làm việc.
- Cả 2 ông không ai chịu nhường ai, đứng chờ nhau tới kiếp sau. Phần mềm của bạn sẽ **bị treo vĩnh viễn** mà không báo lỗi gì cả. Đó gọi là Deadlock.

**Cách phòng tránh:**
1. Yêu cầu mọi công nhân phải xin khóa theo đúng **một thứ tự duy nhất** (VD: Quy định ai cũng phải lấy chìa tủ xong mới được lấy chìa két).
2. Không lồng ghép nhiều khóa (`synchronized` bên trong `synchronized`) nếu không cần thiết.

---

## 7. Vũ khí Đa luồng Hạng Nặng (`java.util.concurrent`)

Sử dụng `synchronized` khá chậm và dễ dính Deadlock. Java cung cấp sẵn một kho vũ khí cực xịn:

1. **`ReentrantLock` (Ổ khóa thông minh):** Thay vì đứng chờ mù quáng như `synchronized`, bạn có thể dùng `tryLock(thời_gian)`. Nếu xin khóa 3 giây không được thì từ bỏ làm việc khác, giúp tránh Deadlock.
2. **Cấu trúc dữ liệu an toàn:** Tuyệt đối không dùng `ArrayList`, `HashMap` thông thường cho Đa luồng. Hãy dùng:
   - `ConcurrentHashMap`: Vừa an toàn, vừa chạy siêu nhanh.
   - `CopyOnWriteArrayList`: Chuyên dùng cho trường hợp "Đọc cực nhiều, Ghi cực hiếm".
   - `BlockingQueue`: Một cái ống nước. Hết nước thì người múc tự đứng chờ, đầy nước thì người đổ tự đứng chờ. Dùng cực nhiều trong thực tế.
3. **`AtomicInteger` (Biến đếm ma thuật):** Nếu chỉ đơn thuần là cộng/trừ số (Ví dụ: Tăng lượt view bài viết), dùng ổ khóa là quá cồng kềnh. `AtomicInteger` dùng lệnh phần cứng trực tiếp để cộng số siêu nhanh mà 100% không bị lệch dữ liệu.

---

## 8. Virtual Threads (Bổ sung tương lai - Java 21)

Công nhân thông thường (Platform Thread) rất "nặng cân" (~1MB RAM mỗi người). 
**Java 21 giới thiệu Virtual Threads (Luồng ảo - Project Loom):**
- Đây là những "Công nhân tàng hình", nhẹ đến mức bạn có thể tạo **1 triệu công nhân** cùng lúc mà máy tính không hề hấn gì.
- Cú pháp đơn giản: `Thread.startVirtualThread(() -> { ... });`
- Khi 1 công nhân tàng hình phải đứng chờ dữ liệu (ví dụ chờ tải file), hệ điều hành sẽ tự động bốc anh ta ra chỗ khác ngủ, và nhường đường cho công nhân khác vào chạy. Điều này giúp Web Server sau này xử lý hàng trăm ngàn lượt truy cập cùng lúc một cách dễ dàng!

---
---

# PHẦN 9: BÀI TẬP THỰC HÀNH TỔNG HỢP

**Bài 1: Khởi động Thread cơ bản**
Tạo 2 luồng: 
- Luồng 1 (Thread T1): In ra các số chẵn từ 1 đến 20, mỗi lần in ngủ (`sleep`) 500ms.
- Luồng 2 (Thread T2): In ra các số lẻ từ 1 đến 20, mỗi lần in ngủ (`sleep`) 500ms.
*Yêu cầu:* Dùng interface `Runnable`. Khởi chạy 2 luồng đồng thời ở hàm main. (Chú ý quan sát sự xen kẽ trên console).

**Bài 2: Tính tổng mảng bằng Biệt đội (ExecutorService & Callable)**
Tạo một mảng có 100.000 phần tử số nguyên ngẫu nhiên.
Thay vì tính từ đầu đến cuối, hãy dùng `ExecutorService` có 4 Thread, tạo 4 đối tượng `Callable` để chia mảng ra làm 4 khúc và tính tổng 4 khúc đó song song.
Hàm main dùng `Future.get()` lấy kết quả 4 khúc cộng lại để ra tổng cuối cùng.

**Bài 3: Bài toán "Tranh giành tiền" (Race Condition)**
Tạo 1 class `TaiKhoanNganHang` có biến `soDu = 1000`. 
Tạo 10 Thread, mỗi Thread chạy vòng lặp 100 lần, mỗi lần trừ 10 đồng khỏi `soDu`.
*Yêu cầu:*
- Lần 1: Code bình thường không khóa -> Quan sát xem kết quả cuối cùng có phải là 0 không? (Chắc chắn sẽ ra số linh tinh do đạp chân nhau).
- Lần 2: Thêm từ khóa `synchronized` vào hàm trừ tiền -> Đảm bảo kết quả luôn luôn ra đúng 0.

**Bài 4: Tăng biến đếm siêu tốc với Atomic**
Tương tự bài 3, nhưng thay vì dùng `soDu` là số nguyên bình thường và dùng khóa, hãy sử dụng `AtomicInteger`. Gọi hàm `decrementAndGet()` 1000 lần bằng 10 thread khác nhau và đo xem tốc độ (thời gian chạy) có nhanh hơn việc dùng `synchronized` ở Bài 3 không.

**Bài 5: Băng chuyền sản xuất (BlockingQueue)**
Tạo 1 `ArrayBlockingQueue<Integer>(capacity = 5)` đóng vai trò là chiếc bàn chứa được 5 cái bánh.
- Tạo 1 công nhân Nướng Bánh (Thread 1): Cứ 1 giây ném 1 số ngẫu nhiên vào Queue (dùng lệnh `put()`).
- Tạo 2 công nhân Ăn Bánh (Thread 2 & 3): Liên tục lấy bánh ra khỏi Queue (dùng lệnh `take()`), mỗi lần ăn mất 3 giây.
Quan sát cơ chế tự động tuyệt vời của Queue: Khi bàn đầy 5 cái, thợ nướng tự động đứng chơi. Khi bàn hết bánh, thợ ăn tự động đứng chờ.
