# Xử Lý Ngoại Lệ Trong Java (Exception Handling)

Exception Handling trong Java là cơ chế xử lý các lỗi xảy ra trong ứng dụng để đảm bảo ứng dụng hoạt động ổn định và tránh bị gián đoạn đột ngột. Java cung cấp các từ khóa để xử lý ngoại lệ bao gồm: `try`, `catch`, `finally`, `throw`, và `throws`.

### **1\. Cấu trúc của lớp Java Exception**

Lớp `java.lang.Throwable` là lớp gốc của hệ thống phân cấp Java Exception được kế thừa bởi hai lớp con: **Exception** và **Error**.

![](https://cdn.tayjava.com/production/image/20250910_084807_pasted-1757468884373.png)

### **2\. Các loại Exception trong Java**

Trong Java, exceptions được chia làm hai loại chính:

● **Checked Exceptions**: Đây là những ngoại lệ mà trình biên dịch bắt buộc phải xử lý (thường là ngoại lệ có thể xảy ra trong quá trình đọc file, kết nối cơ sở dữ liệu,...). Ví dụ: _IOException_, _SQLException_.

● **Unchecked Exceptions**: Là những ngoại lệ xảy ra khi có lỗi lập trình và không bị trình biên dịch kiểm tra. Thường là các lỗi logic trong chương trình, Ví dụ: _ArithmeticException_, _NullPointerException_, _ArrayIndexOutOfBoundsException_.

### **3\. Các từ khóa chính:**

*   `try`: Được sử dụng để bao bọc đoạn mã có thể phát sinh ngoại lệ.
    
*   `catch`: Dùng để xử lý các ngoại lệ phát sinh trong khối try. Khi ngoại lệ xảy ra, Java sẽ tìm khối catch tương ứng để xử lý.
    
*   `finally`: Khối lệnh này luôn được thực thi dù ngoại lệ có xảy ra hay không. Nó thường được dùng để dọn dẹp tài nguyên như đóng tệp hoặc kết nối cơ sở dữ liệu.
    
*   `throw`: Được dùng để ném ra một ngoại lệ một cách rõ ràng trong chương trình.
    
*   `throws`: Khai báo ngoại lệ có thể phát sinh trong phương thức, để phương thức gọi biết rằng ngoại lệ có thể xảy ra và phải được xử lý.
    

### **4\. Cú pháp Exception Handling trong Java**

#### 🔹 `try` / `catch` / `finally`

```java
try {
    // Mã có thể gây ra ngoại lệ
} catch (ExceptionType1 e1) {
    // Xử lý ngoại lệ loại ExceptionType1
} catch (ExceptionType2 e2) {
    // Xử lý ngoại lệ loại ExceptionType2
} finally {
    // Khối lệnh này sẽ luôn được thực thi (dù có ngoại lệ hay không)
}
```

– Ví dụ:

```java
public class App {
    public static void main(String[] args) {
        try {
            int result = 10 / 0; // Sẽ phát sinh ArithmeticException
        } catch (ArithmeticException e) {
            System.out.println("Không thể chia cho 0");
        } finally {
            System.out.println("Đoạn code trong finally luôn được thực thi");
        }
    }
}
```

– Kết quả:

```java
Không thể chia cho 0
Đoạn code trong finally luôn được thực thi
```

#### 🔹 Sử dụng `throw` và `throws`

*   `throw`: dùng để ném ra một ngoại lệ cụ thể.
    
*   `throws`: dùng trong khai báo method, cho biết method đó có thể phát sinh ngoại lệ nào.
    

– Ví dụ:

```java
public class App {
    public static void main(String[] args) {
        // handle exception bằng try/catch
        try {
            validateAge(16);
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }
    }

    // 'throws' khai báo loại ngoại lệ có thể phát sinh
    static void validateAge(int age) throws ArithmeticException {
        if (age < 18) {
            // 'throw' ném ra ngoại lệ khi điều kiện không thỏa mãn
            throw new ArithmeticException("Không đủ tuổi cưới vợ");
        } else {
            System.out.println("Bác sĩ bảo cưới thì cưới thôi 😁");
        }
    }
}
```

– Kết quả:

```java
Không đủ tuổi cưới vợ
```

### 5\. Lợi ích của Exception Handling

✅ **Bảo vệ chương trình khỏi dừng đột ngột**  
→ Khi ngoại lệ xảy ra, chương trình không bị crash mà tìm cách xử lý.

✅ **Tách biệt logic bình thường và xử lý lỗi**  
→ Giúp chương trình rõ ràng, dễ đọc hơn.

✅ **Giảm thiểu rủi ro**  
→ Có thể xử lý trước những tình huống bất ngờ.

### 6\. Lưu ý khi xử lý Exception Handling

⚠️ **Không xử lý ngoại lệ chung chung**  
→ Tránh `catch (Exception e)` mà không phân biệt loại ngoại lệ.

⚠️ **Không bỏ qua khối catch**  
→ Nên log hoặc xử lý, tránh để trống.

⚠️ **Luôn dùng finally để giải phóng tài nguyên**  
→ Ví dụ: đóng kết nối CSDL, đóng file.

⚠️ **Tạo Custom Exception khi cần**  
→ Cho các trường hợp đặc biệt của ứng dụng.

– Ví dụ:

```java
// Tạo custom exception
class CustomException extends Exception {
    CustomException(String message) {
        super(message);
    }
}

// Sử dụng custom exception
public class Test {
    static void checkScore(int score) throws CustomException {
        if (score < 50) {
            throw new CustomException("Điểm quá thấp, không đạt yêu cầu!");
        }
        System.out.println("Điểm hợp lệ: " + score);
    }

    public static void main(String[] args) {
        try {
            checkScore(30);
        } catch (CustomException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
}
```

– Kết quả:

```java
Lỗi: Điểm quá thấp, không đạt yêu cầu!
```

### 📌 So sánh Checked Exception và Unchecked Exception trong Java

Tiêu chí**Checked ExceptionUnchecked ExceptionĐịnh nghĩa**Ngoại lệ **được kiểm tra tại compile-time**. Trình biên dịch yêu cầu xử lý bằng `try/catch` hoặc khai báo `throws`.Ngoại lệ **xảy ra tại runtime** (thời gian chạy). Trình biên dịch không bắt buộc phải xử lý.**Ví dụ thường gặp**`IOException`, `SQLException`, `ClassNotFoundExceptionNullPointerException`, `ArithmeticException`, `ArrayIndexOutOfBoundsException`**Khi nào xảy ra**Khi làm việc với **tác vụ bên ngoài** (file, database, network).Khi có **lỗi logic** trong chương trình.**Bắt buộc xử lý?**✅ Có (phải dùng `try/catch` hoặc `throws`)❌ Không bắt buộc**Package chứa**`java.lang.Exception` (ngoại trừ `RuntimeException`)`java.lang.RuntimeException` và các lớp con**Ví dụ code**`java\ntry {\n FileReader fr = new FileReader(\"file.txt\");\n} catch (IOException e) {\n e.printStackTrace();\n}\njava\nint result = 10 / 0; // ArithmeticException\nString s = null;\ns.length(); // NullPointerException\n`

### ✅ Ghi nhớ nhanh

*   **Checked Exception** → Lỗi có thể **dự đoán trước** (bắt buộc xử lý).
    
*   **Unchecked Exception** → Lỗi thường do **lập trình sai** (không bắt buộc xử lý).
