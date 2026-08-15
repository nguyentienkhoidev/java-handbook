# Java Input/Output

### **1\. Java I/O là gì?**

**Java I/O (Input/Output)** là một phần quan trọng trong Java, cung cấp các **API** để đọc dữ liệu từ các nguồn (input) và ghi dữ liệu ra các đích (output).

Nguồn (input) và đích (output) có thể là:

*   Bàn phím, màn hình (console).
*   File (tệp tin trên hệ thống).
*   Mạng (network socket).
*   Bộ nhớ (memory stream).

### **2\. Các gói Java I/O chính**

Java cung cấp hai gói cơ bản:

*   `**java.io**`: Hỗ trợ I/O dựa trên **stream** (luồng byte và ký tự).
*   `**java.nio**` **(New I/O)**: Hỗ trợ I/O **phi khối** (non-blocking), tối ưu cho xử lý dữ liệu lớn và ứng dụng mạng.

## **3\. Các khái niệm quan trọng**

#### a. Stream (Luồng dữ liệu)

*   **Input Stream**: Luồng dữ liệu đi vào (đọc).
*   **Output Stream**: Luồng dữ liệu đi ra (ghi).

Có 2 loại stream chính:

*   **Byte Stream** (`InputStream`, `OutputStream`) → xử lý dữ liệu nhị phân (hình ảnh, video, file nén…).
*   **Character Stream** (`Reader`, `Writer`) → xử lý dữ liệu dạng văn bản (text).

#### b. Các lớp chính trong `java.io`

*   **Byte Streams**: `FileInputStream`, `FileOutputStream` → đọc/ghi file dạng nhị phân.
*   **Character Streams**: `FileReader`, `FileWriter` → đọc/ghi file dạng text.
*   **Buffered Streams**: `BufferedReader`, `BufferedWriter` → tăng hiệu suất khi đọc/ghi.
*   **Data Streams**: `DataInputStream`, `DataOutputStream` → đọc/ghi dữ liệu nguyên thủy (int, double, boolean…).
*   **Object Streams**: `ObjectInputStream`, `ObjectOutputStream` → đọc/ghi đối tượng (serialization).

– Ví dụ: Đọc văn bản

```java
import java.io.*;

public class ReadFileExample {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

– Ví dụ: Ghi văn bản

```java
import java.io.*;

public class WriteFileExample {
    public static void main(String[] args) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            writer.write("Xin chào Fox Dev!");
            writer.newLine();
            writer.write("Đây là ví dụ về Java I/O.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### **4\. Ưu điểm của Java I/O**

*   Cung cấp API mạnh mẽ để xử lý nhiều loại dữ liệu khác nhau.
*   Có sẵn **buffering** giúp tăng hiệu suất.
*   Hỗ trợ **serialization** để lưu trữ và truyền đối tượng.
*   Có **Java NIO** cho các ứng dụng hiện đại cần hiệu năng cao.

