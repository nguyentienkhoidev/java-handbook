# Base64 Encoder/Decoder

### **1\. Base64 trong Java là gì?**

**Base64** là một cơ chế **mã hóa dữ liệu nhị phân (binary data) thành chuỗi ký tự ASCII**.

Nó thường dùng để **truyền dữ liệu nhị phân qua mạng** (ví dụ: hình ảnh, file, token JWT).

Base64 **không phải là mã hóa bảo mật** → chỉ là **mã hóa biểu diễn dữ liệu** (encoding).

Từ **Java 8**, package `java.util.Base64` đã cung cấp sẵn API để encode/decode.

### **2\. Các loại Base64 Encoder/Decoder trong Java**

Lớp `Base64` cung cấp 3 loại encoder/decoder chính:

*   **Basic**
    *   Chuẩn cơ bản (theo RFC 4648).
    *   Dùng ký tự `A–Z, a–z, 0–9, +, /` và `=` để padding.
*   **URL and Filename Safe**
    *   Dùng cho URL, tránh ký tự đặc biệt.
    *   Thay `+` → `-` và `/` → `_`.
*   **MIME**
    *   Dùng cho email (MIME).
    *   Tự động chèn dòng mới `\r\n` sau mỗi 76 ký tự.

📌 Ví dụ: Encode và Decode cơ bản

```java
import java.util.Base64;

public class App {
    public static void main(String[] args) {
        String text = "Hello Java Base64!";

        // Encode
        String encoded = Base64.getEncoder().encodeToString(text.getBytes());
        System.out.println("Encoded: " + encoded);

        // Decode
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);
        String decoded = new String(decodedBytes);
        System.out.println("Decoded: " + decoded);
    }
}
```

– Kết quả:

```plaintext
Encoded: SGVsbG8gSmF2YSBCYXNlNjQh
Decoded: Hello Java Base64!
```

📌 Ví dụ: Base64 URL Safe

```java
import java.util.Base64;

public class App {
    public static void main(String[] args) {
        String text = "https://tayjava.com/video?id=123";

        // URL Safe Encode
        String encoded = Base64.getUrlEncoder().encodeToString(text.getBytes());
        System.out.println("URL Encoded: " + encoded);

        // Decode
        String decoded = new String(Base64.getUrlDecoder().decode(encoded));
        System.out.println("URL Decoded: " + decoded);
    }
}
```

📌 Ví dụ: Base64 MIME (thường dùng cho email / file nhúng)

```java
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        String longText = "Java Base64 MIME Encoding Example " +
                          "with line breaks after 76 characters.";

        String encoded = Base64.getMimeEncoder().encodeToString(longText.getBytes());
        System.out.println("MIME Encoded:\n" + encoded);

        String decoded = new String(Base64.getMimeDecoder().decode(encoded));
        System.out.println("\nMIME Decoded:\n" + decoded);
    }
}
```

### **3\. Ứng dụng thực tế của Base64 trong Java**

*   **JWT (JSON Web Token)** → phần Header và Payload được encode bằng Base64URL.
*   **Lưu trữ / truyền file nhị phân** (ảnh, PDF, video) dưới dạng chuỗi.
*   **Email MIME** (nhúng ảnh, file đính kèm).
*   **Truyền dữ liệu qua HTTP/JSON** mà không lo lỗi ký tự đặc biệt.
