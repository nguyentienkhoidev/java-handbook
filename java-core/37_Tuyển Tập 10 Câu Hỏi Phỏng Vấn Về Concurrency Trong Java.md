# Tuyển Tập 10 Câu Hỏi Phỏng Vấn Về Concurrency Trong Java

![concurrency interview.jpeg](../images/1b9f4e36-cb80-4a9d-b4f4-2c4d8d26d5ec.jpeg)

**1\. Thread và Process khác nhau như thế nào?**

**📌 Trả lời:**

*   _Process là chương trình đang chạy, có không gian nhớ riêng._
    
*   _Thread là luồng thực thi nhỏ hơn trong một process, các thread cùng chia sẻ bộ nhớ._
    

### **2\. Sự khác biệt giữa Runnable và Thread class?**

**📌 Trả lời:**

*   `Thread` _là class,_ `Runnable` _là interface._
    
*   _Nếu extend_ `Thread` _thì không thể extend class khác._
    
*   _Dùng_ `Runnable` _linh hoạt hơn, tách riêng logic chạy và cơ chế quản lý thread._
    

### **3\. synchronized trong Java hoạt động thế nào?**

**📌 Trả lời:**

`synchronized` _khóa đối tượng hoặc method, đảm bảo chỉ một thread được truy cập vào vùng code đó tại một thời điểm._

### **4\. Khác biệt giữa synchronized block và synchronized method?**  
**📌 Trả lời:**

*   `synchronized method` _khóa toàn bộ method._
    
*   `synchronized block` _chỉ khóa một đoạn code nhỏ → hiệu năng tốt hơn._
    

### **5\. volatile khác synchronized thế nào?**

**📌 Trả lời:**

*   `volatile` _đảm bảo visibility (mọi thread thấy giá trị mới nhất)._
    
*   `synchronized` _đảm bảo cả visibility và mutual exclusion (không có race condition)._
    

### **6\. Deadlock là gì? Ví dụ?**

**📌 Trả lời:**

*   Deadlock xảy ra khi 2 thread chờ nhau giải phóng resource → kẹt vĩnh viễn.– Ví dụ:
*   Thread A giữ lock1, chờ lock2;

_Thread B giữ lock2, chờ lock1._

### **7\. Difference giữa ExecutorService và Thread?**

**📌 Trả lời:**

*   `Thread`_: quản lý thủ công, tốn chi phí tạo thread mới._
    
*   `ExecutorService`_: quản lý pool thread, tái sử dụng, dễ mở rộng (submit, shutdown)._
    

### **8\. CountDownLatch khác CyclicBarrier thế nào?**

**📌 Trả lời:**

*   `CountDownLatch`_: đếm ngược, khi về 0 thì các thread mới tiếp tục. Không reset được._
    
*   `CyclicBarrier`_: đồng bộ nhiều thread tại một điểm, có thể reset, tái sử dụng._
    

### **9\. ReentrantLock khác synchronized thế nào?**

**📌 Trả lời:**

*   `ReentrantLock`_: linh hoạt hơn (công bằng, lock interruptible, tryLock)._
    
*   `synchronized`_: đơn giản hơn, tự động release lock._
    

### **10\. ForkJoinPool dùng để làm gì?**

**📌 Trả lời:**

_ForkJoinPool dùng cho lập trình_ **_parallelism_**_: chia task lớn thành các task nhỏ (fork), sau đó ghép kết quả lại (join). Tối ưu trên CPU đa nhân._

