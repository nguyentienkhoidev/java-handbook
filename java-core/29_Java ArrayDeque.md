# Java ArrayDeque

## **1\. ArrayDeque là gì?**

`ArrayDeque` là một lớp trong Java thuộc gói `java.util`. Nó cung cấp một **hàng đợi hai đầu (double-ended queue)** dựa trên một mảng động. Điều này có nghĩa là bạn có thể thêm và xóa phần tử từ cả hai đầu (đầu và đuôi) của hàng đợi một cách hiệu quả.

![](https://cdn.tayjava.com/production/image/20250909_115403_pasted-1757393643444.png)

## **2\. Các đặc điểm chính của** `ArrayDeque`

1.  **Không giới hạn kích thước**: Kích thước của mảng bên trong sẽ được tự động mở rộng nếu cần.
    
2.  **Hiệu suất cao**:
    
    *   Nhanh hơn `Stack` khi được sử dụng như một ngăn xếp.
        
    *   Nhanh hơn `LinkedList` khi được sử dụng như một hàng đợi.
        
3.  **Không cho phép phần tử null**: `ArrayDeque` không cho phép lưu trữ giá trị `null`.
    
4.  **Không đồng bộ**: Nó không đồng bộ hóa, vì vậy không an toàn khi sử dụng trong môi trường đa luồng trừ khi bạn tự quản lý việc đồng bộ hóa.
    

## **3\. Một số phương thức thường dùng**

*   **Thêm/xóa ở đầu**:
    
    *   `addFirst(E e)` / `offerFirst(E e)`: Thêm phần tử vào đầu hàng đợi.
        
    *   `pollFirst()` / `removeFirst()`: Lấy và xóa phần tử ở đầu hàng đợi.
        
    *   `peekFirst()` / `getFirst()`: Lấy nhưng không xóa phần tử ở đầu hàng đợi.
        
*   **Thêm/xóa ở cuối**:
    
    *   `addLast(E e)` / `offerLast(E e)`: Thêm phần tử vào cuối hàng đợi.
        
    *   `pollLast()` / `removeLast()`: Lấy và xóa phần tử ở cuối hàng đợi.
        
    *   `peekLast()` / `getLast()`: Lấy nhưng không xóa phần tử ở cuối hàng đợi.
        
*   **Hoạt động như một ngăn xếp**:
    
    *   `push(E e)`: Thêm phần tử vào đầu hàng đợi (giống như `addFirst`).
        
    *   `pop()`: Lấy và xóa phần tử từ đầu hàng đợi (giống như `removeFirst`).
        

## **4\. Cách sử dụng** `ArrayDeque`

Dưới đây là một ví dụ minh họa việc sử dụng `ArrayDeque`:

```java
import java.util.ArrayDeque;

public class App {
    public static void main(String[] args) {
        ArrayDeque deque = new ArrayDeque<>();

        // Thêm phần tử vào cuối
        deque.addLast("A");
        deque.addLast("B");

        // Thêm phần tử vào đầu
        deque.addFirst("C");

        System.out.println("Deque ban đầu: " + deque);

        // Lấy và xóa phần tử ở đầu
        Object first = deque.pollFirst();
        System.out.println("Phần tử ở đầu: " + first);
        System.out.println("Deque sau khi xóa phần tử ở đầu: " + deque);

        // Lấy nhưng không xóa phần tử ở cuối
        Object last = deque.peekLast();
        System.out.println("Phần tử ở cuối: " + last);

        // Hoạt động như một ngăn xếp
        deque.push("XYZ");
        System.out.println("Deque sau khi push: " + deque);

        Object popped = deque.pop();
        System.out.println("Phần tử pop ra: " + popped);
        System.out.println("Deque cuối cùng: " + deque);
    }
}
```

– Kết quả in ra:

```java
Deque ban đầu: [C, A, B]
Phần tử ở đầu: C
Deque sau khi xóa phần tử ở đầu: [A, B]
Phần tử ở cuối: B
Deque sau khi push: [XYZ, A, B]
Phần tử pop ra: XYZ
Deque cuối cùng: [A, B]
```

### 5\. Khi nào nên dùng `ArrayDeque`?

*   **Cần cài đặt Queue hoặc Stack hiệu quả**
    

Hàng đợi (FIFO): `offer()`, `poll()`, `peek()`.

Ngăn xếp (LIFO): `push()`, `pop()`, `peek()`.  
→ Nhanh hơn `LinkedList` trong hầu hết các trường hợp.

*   **Khi muốn thêm/xóa ở đầu/cuối liên tục**
    

`addFirst()`, `removeFirst()`, `addLast()`, `removeLast()` đều **O(1)**.

Không bị tốn nhiều bộ nhớ như `LinkedList`.

*   **Khi không cần truy cập theo chỉ số**
    

Giống `LinkedList`, `ArrayDeque` **không hỗ trợ** `get(index)` như `ArrayList`.

*   **Khi muốn hiệu năng cao hơn** `LinkedList` **cho Queue/Stack**
    

Vì `ArrayDeque` lưu trên mảng liên tục, cache-friendly hơn so với danh sách liên kết (LinkedList có overhead `prev/next`).

#### Khi KHÔNG nên dùng `ArrayDeque`

*   Khi cần truy cập ngẫu nhiên theo chỉ số (`get(i)`) → hãy dùng `ArrayList`.
    
*   Khi kích thước quá lớn và bạn thường xuyên thêm ở giữa → `LinkedList` linh hoạt hơn.
    
*   Khi bạn cần **thread-safe queue** → dùng `ConcurrentLinkedDeque` hoặc `BlockingQueue`.
    

#### So sánh nhanh

Tính năng`ArrayDequeLinkedListArrayList`Truy cập theo chỉ số❌O(n)✅ O(1)Thêm/xóa đầu/cuối✅ O(1)✅ O(1)❌ O(n)Bộ nhớÍt tốnTốn (prev/next)Trung bìnhQueue/Stack✅ Rất tốt✅ Được⚠️ Không tối ưu
