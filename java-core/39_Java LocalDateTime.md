# Java LocalDateTime

![Java LocalDateTime.jpeg](https://cdn.tayjava.com/production/image/2026/07/17/4a649e23-de81-4338-b9a6-553dc6b8472b.jpeg)

**LocalDateTime** là một class trong **Java Time API (java.time)**, được giới thiệu từ **Java 8**.  
Nó đại diện cho **ngày + giờ**, nhưng **không có thông tin múi giờ (timezone)**.

– Ví dụ:

`2025-09-10T12:30:15` → Đây chỉ là “ngày giờ địa phương” (local), chưa biết thuộc múi giờ nào.

### **1\. Cách tạo** `LocalDateTime`

#### **🔹 a. Lấy thời gian hiện tại**

```plaintext
import java.time.LocalDateTime;

public class App {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Thời gian hiện tại: " + now);
    }
}
```

– Kết quả:

```plaintext
Thời gian hiện tại: 2025-09-10T03:45:12.123
```

#### **🔹 b. Tạo thủ công**

```plaintext
import java.time.LocalDateTime;

public class App {
    static void main() throws IOException, InterruptedException {
        LocalDateTime birthday = LocalDateTime.of(1999, 12, 31, 23, 59, 59);
        System.out.println("Ngày giờ tạo thủ công: " + birthday);

    }
}
```

– Kết quả:

```plaintext
Ngày giờ tạo thủ công: 1999-12-31T23:59:59
```

### **2\. Các phương thức của** `LocalDateTime`

#### **🔹 a. Khởi tạo (Factory Methods)**

MethodMô tả`now()`Lấy thời gian hiện tại theo hệ thống`now(ZoneId zone)`Lấy thời gian hiện tại theo múi giờ`of(int y, int m, int d, int h, int min)`Tạo LocalDateTime với năm, tháng, ngày, giờ, phút`of(int y, int m, int d, int h, int min, int s)`Tạo với năm, tháng, ngày, giờ, phút, giây`of(LocalDate date, LocalTime time)`Tạo từ LocalDate + LocalTime`parse(CharSequence text)`Parse chuỗi ISO-8601 thành LocalDateTime`parse(CharSequence text, DateTimeFormatter f)`Parse chuỗi với định dạng custom

#### **🔹 b. Lấy thông tin (Getters)**

MethodMô tả`getYear()`Lấy năm`getMonth()`Lấy tháng (enum Month)`getMonthValue()`Lấy tháng (1–12)`getDayOfMonth()`Ngày trong tháng (1–31)`getDayOfWeek()`Ngày trong tuần (enum DayOfWeek)`getDayOfYear()`Ngày trong năm (1–365/366)`getHour()`Lấy giờ (0–23)`getMinute()`Lấy phút (0–59)`getSecond()`Lấy giây (0–59)`getNano()`Lấy nano giây

#### **🔹 c. Chỉnh sửa thời gian (Manipulation)**

MethodMô tả`plusYears(long y)`Cộng thêm năm`plusMonths(long m)`Cộng thêm tháng`plusWeeks(long w)`Cộng thêm tuần`plusDays(long d)`Cộng thêm ngày`plusHours(long h)`Cộng thêm giờ`plusMinutes(long m)`Cộng thêm phút`plusSeconds(long s)`Cộng thêm giây`plusNanos(long n)`Cộng thêm nano giây`minusYears(long y)`Trừ năm`minusMonths(long m)`Trừ tháng`minusWeeks(long w)`Trừ tuần`minusDays(long d)`Trừ ngày`minusHours(long h)`Trừ giờ`minusMinutes(long m)`Trừ phút`minusSeconds(long s)`Trừ giây`minusNanos(long n)`Trừ nano giây`withYear(int y)`Đặt lại năm`withMonth(int m)`Đặt lại tháng`withDayOfMonth(int d)`Đặt lại ngày trong tháng`withDayOfYear(int d)`Đặt lại ngày trong năm`withHour(int h)`Đặt lại giờ`withMinute(int m)`Đặt lại phút`withSecond(int s)`Đặt lại giây`withNano(int n)`Đặt lại nano giây

#### **🔹 d. So sánh**

MethodMô tả`isBefore(LocalDateTime other)`Kiểm tra nhỏ hơn thời gian khác`isAfter(LocalDateTime other)`Kiểm tra lớn hơn thời gian khác`isEqual(LocalDateTime other)`Kiểm tra bằng nhau

#### **🔹 e. Chuyển đổi (Conversion)**

MethodMô tả`toLocalDate()`Trích phần ngày (`LocalDate`)`toLocalTime()`Trích phần giờ (`LocalTime`)`toInstant(ZoneOffset offset)`Chuyển thành `InstantatZone(ZoneId zone)`Chuyển thành `ZonedDateTime`

#### **🔹 f. Định dạng (Formatting)**

MethodMô tả`format(DateTimeFormatter f)`Định dạng thành chuỗi theo formatter

### **3\. Các phương thức hay dùng**

#### **🔹 a. Lấy thông tin ngày, tháng, năm, giờ**

```plaintext
LocalDateTime now = LocalDateTime.now();

System.out.println("Năm: " + now.getYear());
System.out.println("Tháng: " + now.getMonth());
System.out.println("Ngày: " + now.getDayOfMonth());
System.out.println("Giờ: " + now.getHour());
System.out.println("Phút: " + now.getMinute());
```

#### **🔹 b. Cộng/trừ thời gian**

```plaintext
LocalDateTime now = LocalDateTime.now();

LocalDateTime plusDays = now.plusDays(5);
LocalDateTime minusHours = now.minusHours(3);

System.out.println("Sau 5 ngày: " + plusDays);
System.out.println("Trước 3 giờ: " + minusHours);
```

#### **🔹 c. So sánh thời gian**

```plaintext
LocalDateTime today = LocalDateTime.now();
LocalDateTime tomorrow = today.plusDays(1);

System.out.println(today.isBefore(tomorrow)); // true
System.out.println(today.isAfter(tomorrow));  // false
```

### **4\. Định dạng (Formatting)**

🔹 Sử dụng `DateTimeFormatter`

```plaintext
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App {
    static void main() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formatted = now.format(formatter);

        System.out.println("Định dạng: " + formatted);

    }
}
```

– Kết quả:

```plaintext
Định dạng: 10/09/2025 05:23:51
```

### **5\. Chuyển đổi LocalDateTime → Kiểu khác**

**🔹 LocalDateTime → LocalDate / LocalTime**

```plaintext
import java.time.LocalDateTime;

public class App {
    static void main() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Chỉ ngày: " + now.toLocalDate());
        System.out.println("Chỉ giờ: " + now.toLocalTime());
    }
}
```

– Kết quả:

```plaintext
Chỉ ngày: 2025-09-10
Chỉ giờ: 05:26:18.721814
```

**🔹 LocalDateTime → Instant (cần ZoneOffset)**

```plaintext
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class App {
    static void main() {
        LocalDateTime now = LocalDateTime.now();
        Instant instant = now.toInstant(ZoneOffset.UTC);
        System.out.println("Instant (UTC): " + instant);
    }
}
```

– Kết quả:

```plaintext
Instant (UTC): 2025-09-10T05:26:59.065699Z
```

### **6\. Khi nào dùng LocalDateTime?**

*   Khi làm việc với **ngày + giờ**, nhưng **không cần múi giờ**.
    
*   Phù hợp cho dữ liệu “local” như:
    
    *   Ngày sinh nhật, lịch hẹn.
        
    *   Deadline trong hệ thống.
        

❌ Nếu cần chính xác múi giờ (ví dụ: event toàn cầu, đặt vé máy bay), hãy dùng `ZonedDateTime` hoặc `OffsetDateTime`.
