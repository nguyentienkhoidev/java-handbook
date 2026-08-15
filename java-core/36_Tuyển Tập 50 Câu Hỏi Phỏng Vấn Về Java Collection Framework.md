# Tuyển Tập 50 Câu Hỏi Phỏng Vấn Về Java Collection Framework

![Java Collection Interview.jpeg](../images/ebf0f617-fb8c-4630-acc8-3c99071cd957.jpeg)

### **1\. Java Collection**

**1\. Collection Framework trong Java là gì? Nó khác gì so với Arrays?**

**📌 Trả lời:**  
_👉 Collection Framework là tập hợp các class & interface hỗ trợ thao tác với tập dữ liệu động (List, Set, Map, Queue)._

*   _Arrays có kích thước cố định, Collections có thể co giãn._
    
*   _Collections cung cấp nhiều thuật toán (sort, search, shuffle)._
    

**2\. Sự khác nhau giữa** `Collection` **và** `Collections`**?**

**📌 Trả lời:**

*   `Collection` _là_ **_interface gốc_** _của List, Set, Queue._
    
*   `Collections` _là_ **_class tiện ích_** _cung cấp các phương thức static như_ `sort()`_,_ `reverse()`_._
    

**3\. Iterator vs ListIterator khác nhau thế nào?**

**📌 Trả lời:**

*   `Iterator`_: duyệt 1 chiều, chỉ_ `next()`_,_ `remove()`_._
    
*   `ListIterator`_: duyệt 2 chiều, hỗ trợ thêm/sửa phần tử khi duyệt._
    

**4\. Tại sao** `Collection` **là interface gốc thay vì class?**

**📌 Trả lời:**  
_Để cho phép nhiều cấu trúc dữ liệu khác nhau (ArrayList, HashSet, LinkedList) implement linh hoạt._

**5\. Khi nào nên dùng** `fail-fast iterator` **và** `fail-safe iterator`**?**

**📌 Trả lời:**

*   `fail-fast` _(ArrayList, HashMap): ném_ `ConcurrentModificationException` _nếu cấu trúc thay đổi trong khi duyệt._
    
*   `fail-safe` _(ConcurrentHashMap, CopyOnWriteArrayList): cho phép duyệt mà không lỗi, nhưng có thể không thấy cập nhật mới._
    

### **2\. Java ArrayList**

**6\. ArrayList khác gì so với mảng thông thường (**`Array`**)?**

**📌 Trả lời:**  
_👉 Array có kích thước cố định, ArrayList tự động co giãn, hỗ trợ nhiều method tiện ích (_`add`_,_ `remove`_,_ `contains`_)._

**7\. Sự khác nhau giữa** `ensureCapacity()` **và** `trimToSize()` **trong ArrayList?**

**📌 Trả lời:**

*   `ensureCapacity(n)`_: tăng dung lượng nếu cần._
    
*   `trimToSize()`_: thu gọn dung lượng = số phần tử thực tế._
    

**8\. Tại sao ArrayList cho phép** `null` **phần tử nhiều lần?**

**📌 Trả lời:**  
_👉 Vì ArrayList không áp đặt ràng buộc về uniqueness như Set._

**9\. Độ phức tạp trung bình của** `get()`**,** `add()`**,** `remove()` **trong ArrayList?**

**📌 Trả lời:**

*   `get(index)` _= O(1)._
    
*   `add()` _cuối danh sách = O(1) trung bình._
    
*   `remove(index)` _= O(n) (phải dịch chuyển phần tử)._
    

**10\. Điều gì xảy ra nếu nhiều thread cùng truy cập 1 ArrayList mà không đồng bộ?**

**📌 Trả lời:**  
_👉 Có thể gây ra_ `ConcurrentModificationException` _hoặc dữ liệu sai lệch._

### **3\. Java LinkedList**

**11\. LinkedList trong Java là doubly-linked list hay singly-linked list?**

**📌 Trả lời:**  
_👉 Là_ **_doubly-linked list_** _(liên kết kép 2 chiều)._

**12\. Tại sao** `get(index)` **trong LinkedList chậm hơn ArrayList?**

**📌 Trả lời:**  
_👉 Vì phải duyệt tuần tự từ đầu/cuối → O(n)._

**13\. Khi nào nên dùng LinkedList thay vì ArrayList?**

**📌 Trả lời:**  
_👉 Khi thao tác thêm/xóa nhiều ở giữa hoặc đầu danh sách._

**14\. LinkedList có thể được dùng như Stack và Queue không?**

**📌 Trả lời:**  
_👉 Có, vì nó hỗ trợ_ `addFirst()`_,_ `addLast()`_,_ `removeFirst()`_,_ `removeLast()`_._

**15\. Điều gì xảy ra khi gọi** `removeFirst()` **trên LinkedList rỗng?**

**📌 Trả lời:**  
_👉 Ném_ `NoSuchElementException`_._

### **4\. Java HashSet**

**16\. HashSet dựa trên cấu trúc dữ liệu nào?**

**📌 Trả lời:**  
_👉 Dựa trên_ **_HashMap_** _(chỉ lưu key, value là dummy object)._

**17\. Tại sao HashSet không cho phép phần tử trùng lặp?**

**📌 Trả lời:**  
_👉 Vì key trong HashMap là duy nhất._

**18\. Làm sao HashSet kiểm tra hai phần tử bằng nhau?**

**📌 Trả lời:**  
_👉 So sánh_ `hashCode()` _trước, sau đó_ `equals()`_._

### **5\. Java LinkedHashSet**

**19\. LinkedHashSet khác HashSet ở điểm nào?**

**📌 Trả lời:**  
_👉 LinkedHashSet duy trì thứ tự_ **_chèn phần tử_**_, HashSet thì không._

**20\. Khi nào nên dùng LinkedHashSet thay cho HashSet?**

**📌 Trả lời:**  
_👉 Khi cần loại bỏ trùng lặp nhưng vẫn giữ thứ tự phần tử._

### **6\. Java TreeSet**

**21\. TreeSet dựa trên cấu trúc dữ liệu nào?**

**📌 Trả lời:**  
_👉_ **_Red-Black Tree_** _(cây nhị phân cân bằng)._

**22\. Tại sao TreeSet không cho phép** `null`**?**

**📌 Trả lời:**  
_👉 Vì cần so sánh phần tử (_`Comparable` _hoặc_ `Comparator`_),_ `null` _không so sánh được._

**23\. TreeSet có duy trì thứ tự chèn không?**

**📌 Trả lời:**  
_👉 Không, TreeSet sắp xếp theo thứ tự tự nhiên hoặc Comparator._

### **7\. Java PriorityQueue**

**24\. PriorityQueue khác Queue thông thường thế nào?**

**📌 Trả lời:**  
_👉 Phần tử luôn được sắp theo_ **_độ ưu tiên_** _(min-heap hoặc max-heap)._

**25\. Nếu hai phần tử có cùng độ ưu tiên thì PriorityQueue xử lý ra sao?**

**📌 Trả lời:**  
_👉 Không đảm bảo thứ tự ổn định._

**26\. PriorityQueue có cho phép** `null` **không?**

**📌 Trả lời:**  
_👉 Không, sẽ ném_ `NullPointerException`_._

### **8\. Java ArrayDeque**

**27\. ArrayDeque khác LinkedList khi dùng làm Queue thế nào?**

**📌 Trả lời:**  
_👉 ArrayDeque nhanh hơn, ít overhead hơn (không cần node object)._

**28\. Tại sao ArrayDeque nhanh hơn Stack?**

**📌 Trả lời:**  
_👉 Stack dựa trên_ `Vector` _(đồng bộ, chậm hơn)._

**29\. ArrayDeque có cho phép** `null` **không?**

**📌 Trả lời:**  
_👉 Không,_ `NullPointerException` _nếu thêm_ `null`_._

### **9\. Java HashMap**

**30\. HashMap lưu trữ key-value như thế nào?**

**📌 Trả lời:**  
_👉 Dựa trên mảng bucket, mỗi bucket chứa linked list hoặc red-black tree._

**31\. Tại sao nên override cả** `hashCode()` **và** `equals()` **khi dùng làm key trong HashMap?**

**📌 Trả lời:**  
_👉 Vì HashMap so sánh bằng_ `hashCode()` _trước, sau đó_ `equals()`_. Nếu không override đúng → trùng key._

**32\. Sự khác nhau giữa** `HashMap` **và** `Hashtable`**?**

**📌 Trả lời:**

*   HashMap: không đồng bộ, cho phép `null` key/value.
    
*   Hashtable: đồng bộ, không cho phép `null`.
    

**33\. Khi load factor vượt quá ngưỡng, HashMap xử lý thế nào?**

**📌 Trả lời:**  
_👉 Resize gấp đôi mảng bucket và rehash lại toàn bộ phần tử._

**34\. Tại sao HashMap trong Java 8 trở lên dùng red-black tree thay cho linked list trong bucket?**

**📌 Trả lời:**  
_👉 Để giảm độ phức tạp tìm kiếm từ O(n) → O(log n) khi nhiều key hash vào cùng bucket._

### **10\. Java ConcurrentSkipListMap**

**35\. ConcurrentSkipListMap khác TreeMap thế nào?**

**📌 Trả lời:**  
_👉 ConcurrentSkipListMap thread-safe (khóa phân đoạn), TreeMap không._

**36\. Tại sao ConcurrentSkipListMap hữu ích trong môi trường đa luồng?**

**📌 Trả lời:**  
_👉 Cho phép nhiều thread đọc/ghi cùng lúc mà không cần synchronized toàn bộ map._

### **11\. Java NavigableMap**

**37\. NavigableMap mở rộng SortedMap thêm những phương thức nào?**

**📌 Trả lời:**  
_👉_ `lowerKey()`_,_ `floorKey()`_,_ `ceilingKey()`_,_ `higherKey()`_,_ `descendingMap()`_._

**38\.** `floorKey()` **và** `ceilingKey()` **khác nhau thế nào?**

**📌 Trả lời:**

*   `floorKey(x)`_: phần tử ≤ x gần nhất._
    
*   `ceilingKey(x)`_: phần tử ≥ x gần nhất._
    

### **12\. Java LinkedHashMap**

**39\. LinkedHashMap khác HashMap thế nào?**

**📌 Trả lời:**  
_👉 Duy trì thứ tự chèn hoặc thứ tự truy cập._

**40\. Làm sao để LinkedHashMap hoạt động như LRU Cache?**

**📌 Trả lời:**  
_👉 Override_ `removeEldestEntry()` _để tự xóa phần tử cũ._

**41\. LinkedHashMap duy trì thứ tự nào mặc định?**

**📌 Trả lời:**  
_👉 Thứ tự_ **_chèn phần tử_**_._

### **13\. Java TreeMap**

**42\. TreeMap có cho phép** `null` **key không?**

**📌 Trả lời:**  
_👉 Không, sẽ ném_ `NullPointerException`_._

**43\. TreeMap và HashMap khác nhau thế nào về hiệu năng?**

**📌 Trả lời:**

*   _HashMap: O(1) trung bình._
    
*   _TreeMap: O(log n)._
    

**44\. Khi nào nên dùng TreeMap thay HashMap?**

**📌 Trả lời:**  
_👉 Khi cần dữ liệu_ **_có thứ tự_** _(sorted map)._

### **14\. Java WeakHashMap**

**45\. WeakHashMap khác HashMap thế nào?**

**📌 Trả lời:**  
_👉 WeakHashMap sử dụng_ **_weak reference_** _cho key → GC có thể xóa entry khi không còn tham chiếu mạnh._

**46\. Tại sao WeakHashMap hữu ích khi quản lý cache?**

**📌 Trả lời:**  
_👉 Giải phóng bộ nhớ tự động khi key không còn dùng._

**47\. Nếu key không còn tham chiếu mạnh, điều gì xảy ra với entry trong WeakHashMap?**

**📌 Trả lời:**  
_👉 Entry sẽ bị GC xóa._

**48\. WeakHashMap có cho phép** `null` **key/value không?**

**📌 Trả lời:**  
_👉 Có, cho phép 1_ `null` _key và nhiều_ `null` _value._

**49\. Tại sao nên chọn cấu trúc dữ liệu phù hợp thay vì luôn dùng HashMap hoặc ArrayList?**

**📌 Trả lời:**  
_👉 Vì mỗi cấu trúc tối ưu cho tình huống khác nhau (tìm kiếm, thêm/xóa, thứ tự, thread-safe)._

**50\. Cho ví dụ về việc kết hợp nhiều collection trong thực tế.**

**📌 Trả lời:**  
_👉_ `Map<String, List<User>>` _dùng để nhóm danh sách user theo city._

#### **_👉 Đăng ký ngay khoá học_** [**_Java Core Nâng Cao Thực Chiến - Full Version_**](https://vi.nguyentienkhoi.hashnode.dev/courses/java-core-nang-cao-thuc-chien-full-version) **_để nắm vững Java Core và bứt phá sự nghiệp Lập Trình Java_**

