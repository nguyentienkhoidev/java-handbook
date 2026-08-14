# Tuyển Tập 10 Câu Hỏi Thực Tế Về Cách Sử Dụng Multithread

![Java MultiThread  Interview.jpeg](https://cdn.tayjava.com/production/image/2026/07/17/d6b02a1e-2aa2-448d-9f9a-c2c96e0001f8.jpeg)

### 1\. Xử lý nhiều file log cùng lúc

**🔥** Bạn có một ứng dụng đọc dữ liệu log từ nhiều file khác nhau, làm sao để tận dụng multithread để xử lý nhanh hơn?  
 

**📌 Trả lời:**

_Sử dụng_ **_ExecutorService_** _(thread pool). Mỗi thread đọc và phân tích một file, sau đó kết quả được gom lại bằng_ `Future` _hoặc_ `CompletableFuture`_. Như vậy tổng thời gian xử lý = thời gian file lớn nhất thay vì cộng dồn tất cả._

### 2\. Thread pool trong web server

**🔥** Trong hệ thống web server, tại sao thường dùng thread pool thay vì tạo thread mới cho mỗi request?

**📌 Trả lời:**  
_Vì việc tạo và hủy thread tốn chi phí (CPU, memory). Thread pool giữ sẵn một số thread và tái sử dụng, giúp_ **_hiệu suất cao hơn và kiểm soát số lượng thread tối đa_** _để không làm cạn kiệt tài nguyên._

### 3\. Gọi nhiều API song song

**🔥** Bạn cần tải dữ liệu từ 5 API khác nhau rồi gộp kết quả. Làm sao dùng multithreading để tối ưu thời gian?  
 

**📌 Trả lời:**

_Dùng_ `CompletableFuture.supplyAsync()` _hoặc_ `ExecutorService.invokeAll()`_. Các API chạy song song, rồi kết quả được gộp lại bằng_ `CompletableFuture.allOf()`_. Tổng thời gian ≈ thời gian API lâu nhất._

### 4\. Tránh race condition khi rút tiền

**🔥** Trong ứng dụng ngân hàng, khi 2 người cùng lúc rút tiền từ 1 tài khoản, cần xử lý thế nào để tránh sai lệch số dư?  
 

**📌 Trả lời:**

_Dùng_ **_synchronization_** _(_`synchronized` _block hoặc_ `ReentrantLock`_) để đảm bảo chỉ 1 thread được truy cập và cập nhật số dư tại một thời điểm → tránh_ **_race condition_**_._

### 5\. Chạy tác vụ định kỳ

**🔥** Khi bạn muốn thực hiện công việc định kỳ (ví dụ: quét database mỗi 10 giây), bạn sẽ chọn cơ chế nào trong multithreading?  
 

**📌 Trả lời:**

_Dùng_ `ScheduledExecutorService` _với_ `scheduleAtFixedRate()` _(chạy định kỳ với khoảng cách cố định) hoặc_ `scheduleWithFixedDelay()` _(chạy sau khi tác vụ trước kết thúc)._

### 6\. Xử lý batch ảnh song song

**🔥** Trong ứng dụng xử lý ảnh, bạn có 1000 ảnh cần resize. Làm sao dùng multithreading để tối ưu xử lý?  
 

**_📌 Trả lời:_**

_Chia thành các batch (ví dụ 10 ảnh/thread), dùng_ **_thread pool_** _để xử lý song song. Cuối cùng merge kết quả → giảm mạnh thời gian so với chạy tuần tự._

### 7\. Server chat nhiều client

**🔥** Trong chat server, nhiều client gửi tin nhắn cùng lúc. Tại sao nên dùng non-blocking I/O thay vì mỗi client một thread?  
 

**📌 Trả lời:**

_Vì mỗi kết nối một thread →_ **_thread bùng nổ_** _khi có nhiều client. Non-blocking I/O (_`NIO`_,_ `Netty`_) dùng ít thread để phục vụ nhiều kết nối nhờ_ **_event-driven model_** _→ tiết kiệm tài nguyên, xử lý tốt hàng nghìn client._

### 8\. Xử lý video lâu mà không treo UI

**🔥** Bạn có một tác vụ lâu (ví dụ: xử lý video 10 phút). Làm sao để người dùng không bị treo UI khi chạy?

  
**📌 Trả lời:**

_Chạy trong_ **_background thread_** _(_`ExecutorService`_,_ `CompletableFuture`_,_ `SwingWorker` _hoặc_ `JavaFX Task`_). UI thread chỉ hiển thị progress bar → UI không bị freeze._

### 9\. Crawl dữ liệu từ hàng nghìn website

**🔥** Bạn đang crawl dữ liệu từ hàng nghìn website. Làm sao tránh việc tạo quá nhiều thread gây quá tải hệ thống?

  
**_📌 Trả lời:_**

_Dùng_ **_ThreadPoolExecutor với queue_** _để giới hạn số thread hoạt động cùng lúc. Hoặc dùng_ **_Semaphore_** _để hạn chế số request song song. Điều này giúp tránh quá tải CPU và network._

### 10\. Khi nào dùng CompletableFuture

**🔥** Trong Java, khi nào bạn nên dùng `CompletableFuture` thay cho `Thread` hay `ExecutorService`?  
 

**_📌 Trả lời:_**

_Khi cần_ **_xử lý bất đồng bộ + chaining kết quả_** _(ví dụ: gọi API → parse JSON → lưu DB)._ `CompletableFuture` _cho phép viết code ngắn gọn, dễ đọc hơn_ `Thread` _hoặc_ `ExecutorService`_, và hỗ trợ kết hợp nhiều tác vụ song song._

#### **_👉 Đăng ký ngay khoá học_** [**_Java Core Nâng Cao Thực Chiến - Full Version_**](https://vi.tayjava.com/courses/java-core-nang-cao-thuc-chien-full-version) **_để nắm vững Java Core và bứt phá sự nghiệp Lập Trình Java_**
