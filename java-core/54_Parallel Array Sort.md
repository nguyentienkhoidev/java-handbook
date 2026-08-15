# Parallel Array Sort

`Arrays.parallelSort` là một **phương thức mới trong Java 8** (thuộc class `java.util.Arrays`) dùng để **sắp xếp mảng theo kiểu song song (parallel)**, giúp tăng hiệu năng khi xử lý mảng có kích thước lớn.

### **1\. Đặc điểm chính**

*   **Dựa trên Fork/Join Framework**:
    *   Bên trong `parallelSort`, Java sử dụng **Fork/Join pool** để chia mảng thành các phần nhỏ, sắp xếp song song, sau đó gộp kết quả lại.
    *   Khác với `Arrays.sort` vốn chạy tuần tự.
*   **Tự động chọn thuật toán**:
    *   Nếu mảng **nhỏ**, `parallelSort` có thể rơi về `Arrays.sort` (tuần tự) để tránh overhead.
    *   Nếu mảng **lớn**, nó sẽ chia và xử lý song song để tận dụng nhiều CPU core.
*   **Sắp xếp ổn định**:
    *   Đảm bảo tính ổn định (stable sort) giống như `Arrays.sort`.
*   **Có nhiều overloads**:
    *   Hỗ trợ mảng **primitives** (`int[]`, `long[]`, `double[]`, …).
    *   Hỗ trợ mảng **Objects** với `Comparator`.

– Ví dụ 1: Sắp xếp mảng số nguyên

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] numbers = { 9, 3, 1, 7, 5, 4, 6, 2, 8 };
        
        // Sắp xếp song song
        Arrays.parallelSort(numbers);
        
        System.out.println(Arrays.toString(numbers));
        // Output: [1, 2, 3, 4, 5, 6, 7, 8, 9]
    }
}
```

– Ví dụ 2: Sắp xếp mảng đối tượng

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String[] langs = { "Java", "Python", "C++", "Go", "Rust" };
        
        // Sắp xếp song song theo độ dài chuỗi
        Arrays.parallelSort(langs, (a, b) -> Integer.compare(a.length(), b.length()));
        
        System.out.println(Arrays.toString(langs));
        // Output: [Go, Java, Rust, Python, C++]
    }
}
```

– Ví dụ 2: Sắp xếp một phần mảng

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] numbers = { 10, 3, 5, 7, 2, 8, 6, 1, 9, 4 };
        
        // Chỉ sắp xếp từ index 2 đến 7
        Arrays.parallelSort(numbers, 2, 8);
        
        System.out.println(Arrays.toString(numbers));
        // Output: [10, 3, 1, 2, 5, 6, 7, 8, 9, 4]
    }
}
```

### **2\. Khi nào dùng** `**parallelSort**`**?**

✅ Khi mảng **rất lớn** (ví dụ: hàng triệu phần tử).

✅ Khi chạy trên hệ thống **nhiều CPU core** → tăng tốc độ đáng kể.

❌ Không nên dùng cho mảng nhỏ (do overhead của chia tách và gộp).

