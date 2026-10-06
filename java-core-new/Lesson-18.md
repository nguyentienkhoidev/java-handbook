# Lesson 18: Hệ sinh thái Java 8+, Xử lý Thời gian và Regex

Bài học này là một trong những bài học **đồ sộ và quan trọng bậc nhất** trong lộ trình Java Core. Chúng ta sẽ trang bị những vũ khí sắc bén nhất của lập trình viên Java hiện đại, từ xử lý chuỗi phức tạp (Regex), quản lý thời gian chuẩn mực (Date/Time API), cho đến bước ngoặt vĩ đại của Java 8: **Lập trình hàm (Functional Programming)** với Lambda, Stream API và Optional.

## Mục lục
- [PHẦN 1: Regex](#phần-1-regex---biểu-thức-chính-quy)
- [PHẦN 2: Date Time API](#phần-2-date-time-api)
- [PHẦN 3: Default Method](#phần-3-default-method-trong-interface)
- [PHẦN 4: Functional Interface](#phần-4-functional-interface)
- [PHẦN 5: Lambda Expression & Type Inference](#phần-5-lambda-expression--type-inference)
- [PHẦN 6: Method Reference](#phần-6-method-reference)
- [PHẦN 7: Stream API](#phần-7-stream-api)
- [PHẦN 8: Optional](#phần-8-optional)
- [PHẦN 9: Generics trong Functional Programming](#phần-9-generics-trong-functional-programming)

---
---

# PHẦN 1: REGEX - Biểu Thức Chính Quy

## 1.1. Regex là gì?

**Regex (Regular Expression)** là một chuỗi ký tự đặc biệt dùng để mô tả một **"khuôn mẫu" (pattern)** tìm kiếm. Hãy tưởng tượng bạn muốn kiểm tra xem một chuỗi có đúng là email hay không, hoặc muốn tìm tất cả số điện thoại trong một đoạn văn bản dài — Regex chính là công cụ để làm điều đó.

**Ứng dụng thực tế:**
- Validate form đăng ký (email, số điện thoại, mật khẩu).
- Tìm kiếm và thay thế văn bản phức tạp.
- Trích xuất dữ liệu từ file log, HTML, CSV.

## 1.2. Các ký hiệu cơ bản (Metacharacters)

Regex sử dụng các ký hiệu đặc biệt để mô tả khuôn mẫu:

| Ký hiệu | Ý nghĩa | Ví dụ |
|:---|:---|:---|
| `.` | Bất kỳ ký tự nào (trừ xuống dòng) | `a.c` khớp `abc`, `a1c`, `a c` |
| `^` | Bắt đầu chuỗi | `^Hello` khớp chuỗi bắt đầu bằng "Hello" |
| `$` | Kết thúc chuỗi | `end$` khớp chuỗi kết thúc bằng "end" |
| `\|` | HOẶC (OR) | `cat\|dog` khớp "cat" hoặc "dog" |
| `()` | Nhóm (Grouping) | `(ab)+` khớp "ab", "abab", "ababab" |
| `[]` | Lớp ký tự (Character class) | `[aeiou]` khớp một nguyên âm |
| `\` | Escape ký tự đặc biệt | `\.` khớp đúng dấu chấm `.` |

## 1.3. Lớp ký tự (Character Classes)

```
[abc]      → Khớp a HOẶC b HOẶC c
[^abc]     → Khớp bất kỳ ký tự nào KHÔNG phải a, b, c
[a-z]      → Khớp từ a đến z (chữ thường)
[A-Z]      → Khớp từ A đến Z (chữ hoa)
[0-9]      → Khớp một chữ số
[a-zA-Z0-9] → Khớp chữ cái hoặc chữ số
```

**Các lớp ký tự viết tắt (dùng rất nhiều):**

| Viết tắt | Tương đương | Ý nghĩa |
|:---|:---|:---|
| `\d` | `[0-9]` | Một chữ số |
| `\D` | `[^0-9]` | KHÔNG phải chữ số |
| `\w` | `[a-zA-Z0-9_]` | Chữ cái, số hoặc gạch dưới |
| `\W` | `[^a-zA-Z0-9_]` | KHÔNG phải word character |
| `\s` | `[ \t\n\r\f]` | Khoảng trắng (space, tab, xuống dòng) |
| `\S` | `[^ \t\n\r\f]` | KHÔNG phải khoảng trắng |

## 1.4. Bộ định lượng (Quantifiers)

Dùng để chỉ định **số lần lặp lại** của ký tự hoặc nhóm phía trước nó:

| Quantifier | Ý nghĩa | Ví dụ |
|:---|:---|:---|
| `*` | 0 hoặc nhiều lần | `ab*c` khớp `ac`, `abc`, `abbc` |
| `+` | 1 hoặc nhiều lần | `ab+c` khớp `abc`, `abbc` (KHÔNG khớp `ac`) |
| `?` | 0 hoặc 1 lần | `colou?r` khớp `color` và `colour` |
| `{n}` | Chính xác n lần | `\d{4}` khớp đúng 4 chữ số |
| `{n,}` | Ít nhất n lần | `\d{2,}` khớp 2 chữ số trở lên |
| `{n,m}` | Từ n đến m lần | `\d{6,8}` khớp từ 6 đến 8 chữ số |

## 1.5. Biên giới từ (Boundary Matchers)

```
\b  → Biên giới từ (Word Boundary)
\B  → Không phải biên giới từ
```

```java
// \b giúp tìm ĐÚNG từ "cat", không khớp "category" hay "concatenate"
String regex = "\\bcat\\b";
"I have a cat".matches(".*\\bcat\\b.*"); // true
"category".matches(".*\\bcat\\b.*");     // false
```

## 1.6. Nhóm bắt giữ (Groups & Capturing)

Dấu ngoặc `()` không chỉ để nhóm, mà còn **"bắt giữ"** (capture) chuỗi con khớp được để bạn dùng lại:

```java
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupExample {
    public static void main(String[] args) {
        String input = "Ngày 06/10/2026 là hôm nay";
        
        // Group 1: ngày, Group 2: tháng, Group 3: năm
        Pattern pattern = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4})");
        Matcher matcher = pattern.matcher(input);
        
        if (matcher.find()) {
            System.out.println("Toàn bộ: " + matcher.group(0)); // 06/10/2026
            System.out.println("Ngày: " + matcher.group(1));    // 06
            System.out.println("Tháng: " + matcher.group(2));   // 10
            System.out.println("Năm: " + matcher.group(3));     // 2026
        }
    }
}
```

**Named Group (Nhóm có tên — từ Java 7):**
```java
// Đặt tên cho nhóm bằng (?<tên>...)
Pattern pattern = Pattern.compile("(?<day>\\d{2})/(?<month>\\d{2})/(?<year>\\d{4})");
Matcher matcher = pattern.matcher("06/10/2026");
if (matcher.find()) {
    System.out.println("Năm: " + matcher.group("year")); // 2026
}
```

## 1.7. Các class Java để làm việc với Regex

Java cung cấp 2 class chính trong gói `java.util.regex`:

### Pattern — Biên dịch regex thành đối tượng (Thread-safe)
```java
// Nên lưu static final vì Pattern.compile() tốn chi phí
private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
```

### Matcher — Thực hiện so khớp (KHÔNG Thread-safe)
```java
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexDemo {
    // Biên dịch 1 lần, tái sử dụng nhiều lần → Tiết kiệm hiệu năng
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
    
    public static void main(String[] args) {
        // matches() — Kiểm tra TOÀN BỘ chuỗi có khớp pattern không
        Matcher m1 = PHONE_PATTERN.matcher("0987654321");
        System.out.println(m1.matches()); // true
        
        // find() — Tìm match TIẾP THEO trong chuỗi (dùng trong vòng lặp)
        Pattern wordPattern = Pattern.compile("\\b\\w+\\b");
        Matcher m2 = wordPattern.matcher("Hello Java World");
        while (m2.find()) {
            System.out.println("Tìm thấy từ: " + m2.group());
        }
        // Output: Hello, Java, World
    }
}
```

### Các hàm của String hỗ trợ Regex (Cách dùng nhanh):
```java
// 1. matches() — Kiểm tra toàn bộ chuỗi
"0987654321".matches("^0\\d{9}$"); // true

// 2. split() — Tách chuỗi theo pattern
String[] parts = "apple,banana,,orange".split(",");
// ["apple", "banana", "", "orange"]

// split() với limit
String[] parts2 = "a-b-c-d".split("-", 3);
// ["a", "b", "c-d"] → Tách tối đa thành 3 phần

// 3. replaceAll() — Thay thế tất cả match
"Hello   Java   World".replaceAll("\\s+", " "); // "Hello Java World"

// 4. replaceFirst() — Thay thế match đầu tiên
"abc123def456".replaceFirst("\\d+", "***"); // "abc***def456"
```

## 1.8. Flags (Cờ điều khiển)

Flags thay đổi cách Regex hoạt động:

```java
// CASE_INSENSITIVE — Không phân biệt hoa/thường
Pattern p1 = Pattern.compile("hello", Pattern.CASE_INSENSITIVE);
p1.matcher("HELLO").matches(); // true

// MULTILINE — ^ và $ khớp đầu/cuối MỖI DÒNG (không chỉ đầu/cuối chuỗi)
Pattern p2 = Pattern.compile("^Start", Pattern.MULTILINE);

// Kết hợp nhiều flags
Pattern p3 = Pattern.compile("hello", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

// Hoặc viết trực tiếp trong regex
String regex = "(?i)hello"; // (?i) = CASE_INSENSITIVE
```

## 1.9. Cạm bẫy quan trọng

**1. Escape trong Java String:**
Trong Java, dấu `\` phải escape 2 lần: 1 lần cho Java String, 1 lần cho Regex.
```java
// Muốn viết regex \d trong Java code → phải viết "\\d"
// Muốn khớp ký tự \ → regex là \\  → Java code là "\\\\"
```

**2. Hiệu năng — Tuyệt đối KHÔNG compile Pattern trong vòng lặp:**
```java
// ❌ SAI (Tốn hiệu năng khủng khiếp)
for (String email : emails) {
    if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) { ... } 
    // matches() biên dịch Pattern mới MỖI LẦN lặp!
}

// ✅ ĐÚNG (Biên dịch 1 lần)
private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
for (String email : emails) {
    if (EMAIL_PATTERN.matcher(email).matches()) { ... }
}
```

**3. `Pattern.quote()` — Tự động escape chuỗi:**
```java
// Nếu cần tìm chuỗi literal có chứa ký tự đặc biệt
String userInput = "price is $10.00"; // $ và . là metacharacters
String safeRegex = Pattern.quote("$10.00"); // Tự động escape hết
```

---
---

# PHẦN 2: DATE TIME API

## 2.1. Tại sao không dùng `java.util.Date` nữa?

Trước Java 8, chúng ta dùng `java.util.Date` và `Calendar`. Nhưng chúng cực kỳ tệ:
- **Mutable (Thay đổi được):** Ai cũng có thể gọi `date.setTime()` để thay đổi, gây lỗi ngầm.
- **Không Thread-safe:** `SimpleDateFormat` dùng trong đa luồng sẽ gây lỗi.
- **Tháng đánh số từ 0:** Tháng 1 = 0, Tháng 12 = 11, gây nhầm lẫn kinh khủng.

Từ Java 8, API mới **`java.time`** ra đời — **Immutable**, **Thread-safe**, cú pháp đẹp.

## 2.2. LocalDate — Chỉ lưu Ngày

`LocalDate` lưu ngày tháng năm, **không có giờ phút**, **không có timezone**.

```java
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.Month;

public class LocalDateDemo {
    public static void main(String[] args) {
        // 1. Lấy ngày hôm nay
        LocalDate today = LocalDate.now(); // 2026-10-06
        
        // 2. Tạo một ngày cụ thể
        LocalDate birthday = LocalDate.of(1998, 5, 20);       // 1998-05-20
        LocalDate christmas = LocalDate.of(2026, Month.DECEMBER, 25); // Dùng Enum Month
        
        // 3. Parse từ chuỗi (Chuỗi phải đúng format ISO: yyyy-MM-dd)
        LocalDate parsed = LocalDate.parse("2026-10-06");
        
        // 4. Lấy thông tin
        int year = today.getYear();           // 2026
        Month month = today.getMonth();       // OCTOBER
        int monthValue = today.getMonthValue(); // 10 (ĐÚNG, không phải 9 như Date cũ!)
        int day = today.getDayOfMonth();      // 6
        DayOfWeek dow = today.getDayOfWeek(); // TUESDAY
        int dayOfYear = today.getDayOfYear(); // 279
        
        // 5. Tính toán (Cộng/Trừ) — Trả về đối tượng MỚI (Immutable)
        LocalDate nextWeek = today.plusDays(7);
        LocalDate lastMonth = today.minusMonths(1);
        LocalDate nextYear = today.plusYears(1);
        
        // 6. So sánh
        boolean isBefore = birthday.isBefore(today); // true
        boolean isAfter = birthday.isAfter(today);   // false
        boolean isEqual = today.isEqual(LocalDate.now()); // true
        
        // 7. Kiểm tra năm nhuận
        boolean isLeap = LocalDate.of(2024, 1, 1).isLeapYear(); // true
        
        // 8. Số ngày trong tháng/năm
        int daysInMonth = today.lengthOfMonth(); // 31 (Tháng 10)
        int daysInYear = today.lengthOfYear();   // 365
    }
}
```

## 2.3. LocalTime — Chỉ lưu Giờ

```java
import java.time.LocalTime;

public class LocalTimeDemo {
    public static void main(String[] args) {
        LocalTime now = LocalTime.now();          // 06:56:13.123456789
        LocalTime lunch = LocalTime.of(12, 30);   // 12:30
        LocalTime exact = LocalTime.of(14, 30, 45); // 14:30:45
        
        // Các hằng số tiện ích
        LocalTime midnight = LocalTime.MIDNIGHT;   // 00:00
        LocalTime noon = LocalTime.NOON;           // 12:00
        
        // Lấy thông tin
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        
        // Tính toán
        LocalTime later = now.plusHours(2).plusMinutes(30);
        
        // So sánh
        boolean isAfterNoon = now.isAfter(LocalTime.NOON);
    }
}
```

## 2.4. LocalDateTime — Ngày + Giờ

```java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateTimeDemo {
    public static void main(String[] args) {
        // Kết hợp cả ngày và giờ
        LocalDateTime now = LocalDateTime.now(); // 2026-10-06T06:56:13
        
        LocalDateTime specific = LocalDateTime.of(2026, 10, 6, 14, 30, 0);
        
        // Ghép từ LocalDate + LocalTime
        LocalDate date = LocalDate.of(2026, 10, 6);
        LocalTime time = LocalTime.of(14, 30);
        LocalDateTime combined = LocalDateTime.of(date, time);
        
        // Tách ra
        LocalDate datePart = now.toLocalDate();
        LocalTime timePart = now.toLocalTime();
        
        // Tất cả hàm của LocalDate + LocalTime đều dùng được
        LocalDateTime nextDay = now.plusDays(1).plusHours(3);
    }
}
```

## 2.5. Xử lý Timezone — ZonedDateTime, Instant

Khi ứng dụng phục vụ người dùng ở nhiều múi giờ khác nhau (như đặt vé máy bay, họp online quốc tế), bạn cần `ZonedDateTime`.

```java
import java.time.*;

public class TimezoneDemo {
    public static void main(String[] args) {
        // 1. ZoneId — Đại diện cho múi giờ
        ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");
        ZoneId systemZone = ZoneId.systemDefault(); // Múi giờ máy tính
        
        // 2. ZonedDateTime — Ngày + Giờ + Timezone
        ZonedDateTime vnNow = ZonedDateTime.now(vnZone);
        ZonedDateTime tokyoNow = ZonedDateTime.now(tokyoZone);
        
        System.out.println("Giờ VN: " + vnNow);     // 2026-10-06T06:56+07:00[Asia/Ho_Chi_Minh]
        System.out.println("Giờ Tokyo: " + tokyoNow); // 2026-10-06T08:56+09:00[Asia/Tokyo]
        
        // 3. Chuyển đổi múi giờ (Giữ nguyên thời điểm, đổi số hiển thị)
        ZonedDateTime tokyoFromVn = vnNow.withZoneSameInstant(tokyoZone);
        // 06:56 VN → 08:56 Tokyo (cùng một thời điểm, chỉ khác cách hiển thị)
        
        // 4. Instant — Một điểm thời gian trên trục (Machine Time, UTC)
        // Dùng cho: logging, timestamp, lưu database
        Instant now = Instant.now(); // Thời điểm hiện tại (nanosecond precision)
        long epochMilli = now.toEpochMilli(); // Unix timestamp (milliseconds)
        
        // Chuyển Instant sang ZonedDateTime
        ZonedDateTime fromInstant = now.atZone(vnZone);
    }
}
```

## 2.6. Khoảng thời gian — Duration và Period

```java
import java.time.*;

public class DurationPeriodDemo {
    public static void main(String[] args) {
        // --- DURATION: Khoảng cách dựa trên giờ/phút/giây (Time-based) ---
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 30);
        Duration workDuration = Duration.between(start, end);
        System.out.println("Làm việc: " + workDuration.toHours() + " giờ " 
                           + (workDuration.toMinutes() % 60) + " phút"); 
        // Làm việc: 8 giờ 30 phút
        
        // Tạo Duration
        Duration twoHours = Duration.ofHours(2);
        Duration fiveMinutes = Duration.ofMinutes(5);
        
        // --- PERIOD: Khoảng cách dựa trên năm/tháng/ngày (Date-based) ---
        LocalDate birthday = LocalDate.of(1998, 5, 20);
        LocalDate today = LocalDate.now();
        Period age = Period.between(birthday, today);
        System.out.println("Tuổi: " + age.getYears() + " năm, " 
                           + age.getMonths() + " tháng, " 
                           + age.getDays() + " ngày");
        // Tuổi: 28 năm, 4 tháng, 16 ngày
        
        // --- CHRONOUNIT: Tính khoảng cách TUYỆT ĐỐI theo đơn vị cụ thể ---
        long totalDays = ChronoUnit.DAYS.between(birthday, today);
        System.out.println("Tổng số ngày đã sống: " + totalDays); // ~10366
    }
}
```

> **Phân biệt Period vs ChronoUnit:**
> - `Period.between(date1, date2)` trả về: "28 năm, 4 tháng, 16 ngày"
> - `ChronoUnit.DAYS.between(date1, date2)` trả về: tổng số ngày tuyệt đối (vd: 10366)

## 2.7. Định dạng và Parse — DateTimeFormatter

```java
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatterDemo {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        
        // 1. Format: Đối tượng → Chuỗi hiển thị
        DateTimeFormatter f1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formatted = now.format(f1); // "06/10/2026 06:56:13"
        
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
        String formatted2 = now.format(f2); // "06-Oct-2026"
        
        // 2. Parse: Chuỗi → Đối tượng
        String dateStr = "25/12/2026";
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate christmas = LocalDate.parse(dateStr, parser);
        
        // 3. Các Formatter có sẵn (ISO chuẩn)
        String iso = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        // "2026-10-06T06:56:13"
    }
}
```

**Các ký hiệu Format quan trọng:**

| Ký hiệu | Ý nghĩa | Ví dụ |
|:---|:---|:---|
| `yyyy` | Năm 4 chữ số | 2026 |
| `MM` | Tháng (2 chữ số) | 10 |
| `dd` | Ngày (2 chữ số) | 06 |
| `HH` | Giờ 24h (0-23) | 14 |
| `hh` | Giờ 12h (1-12) | 02 |
| `mm` | Phút | 30 |
| `ss` | Giây | 45 |
| `a` | AM/PM | PM |
| `E` | Thứ trong tuần | Tue |
| `EEEE` | Thứ đầy đủ | Tuesday |

## 2.8. TemporalAdjusters — Bộ điều chỉnh thời gian

Khi bạn cần tìm "Ngày cuối tháng", "Thứ Hai tiếp theo", "Thứ Sáu đầu tiên trong tháng":

```java
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

public class AdjusterDemo {
    public static void main(String[] args) {
        LocalDate today = LocalDate.of(2026, 10, 6); // Thứ Ba
        
        LocalDate firstDay = today.with(TemporalAdjusters.firstDayOfMonth());    // 2026-10-01
        LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth());      // 2026-10-31
        LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY)); // 2026-10-12
        LocalDate prevFriday = today.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY)); // 2026-10-02
        
        // Thứ Sáu đầu tiên trong tháng
        LocalDate firstFriday = today.with(TemporalAdjusters.firstInMonth(DayOfWeek.FRIDAY));
        // 2026-10-02
    }
}
```

## 2.9. Các class phụ trợ hữu ích

```java
import java.time.Year;
import java.time.YearMonth;
import java.time.MonthDay;

// Year — Chỉ lưu năm
Year y = Year.of(2024);
boolean isLeap = y.isLeap(); // true (năm nhuận)

// YearMonth — Năm + Tháng (VD: Hạn thẻ tín dụng)
YearMonth cardExpiry = YearMonth.of(2026, 10);
int daysInMonth = cardExpiry.lengthOfMonth(); // 31

// MonthDay — Tháng + Ngày (VD: Sinh nhật, lặp lại mỗi năm)
MonthDay birthday = MonthDay.of(5, 20); // 20 tháng 5
```

## 2.10. Chuyển đổi giữa API cũ và mới

Khi làm dự án thực tế, bạn sẽ gặp code cũ dùng `java.util.Date`. Đây là cách chuyển đổi:

```java
import java.time.*;
import java.util.Date;

// Date cũ → LocalDateTime mới
Date oldDate = new Date();
LocalDateTime newLdt = oldDate.toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime();

// LocalDateTime mới → Date cũ
LocalDateTime ldt = LocalDateTime.now();
Date backToOld = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

// Instant ↔ Date (Dễ nhất)
Instant instant = oldDate.toInstant();    // Date → Instant
Date fromInstant = Date.from(instant);    // Instant → Date
```

---
---

# PHẦN 3: DEFAULT METHOD TRONG INTERFACE

## 3.1. Vấn đề trước Java 8

Trước Java 8, mọi phương thức trong Interface **bắt buộc** phải là trừu tượng (không có thân hàm). Điều này dẫn đến vấn đề: Nếu muốn thêm 1 hàm mới vào Interface đã có hàng triệu class implement, **tất cả class đó đều phải sửa code**. Nếu không sửa → Lỗi biên dịch.

## 3.2. Giải pháp: Từ khóa `default`

Từ Java 8, bạn có thể viết hàm có sẵn logic trong Interface bằng từ khóa `default`:

```java
interface Animal {
    void makeSound(); // Hàm trừu tượng (bắt buộc phải implement)
    
    // Hàm default — Có sẵn logic, class con KHÔNG BẮT BUỘC phải override
    default void sleep() {
        System.out.println("Zzz...");
    }
}

class Dog implements Animal {
    @Override
    public void makeSound() {
        System.out.println("Gâu gâu!");
    }
    // Không cần override sleep() vì nó đã có sẵn logic
}

class Cat implements Animal {
    @Override
    public void makeSound() {
        System.out.println("Meo meo!");
    }
    
    @Override
    public void sleep() {
        System.out.println("Mèo ngủ cuộn tròn..."); // Override lại nếu muốn
    }
}
```

## 3.3. Static Method trong Interface

```java
interface MathUtils {
    // Static method — Gọi bằng InterfaceName.method(), KHÔNG THỂ override
    static int add(int a, int b) {
        return a + b;
    }
}

// Gọi: MathUtils.add(3, 5);
```

## 3.4. Diamond Problem — Xung đột đa kế thừa

Khi class implement 2 Interface có cùng `default method`:

```java
interface InterfaceA {
    default void hello() { System.out.println("Hello from A"); }
}

interface InterfaceB {
    default void hello() { System.out.println("Hello from B"); }
}

// Lỗi biên dịch! Phải override và chọn gọi interface nào
class MyClass implements InterfaceA, InterfaceB {
    @Override
    public void hello() {
        InterfaceA.super.hello(); // Chọn gọi hàm của InterfaceA
    }
}
```

**Quy tắc phân giải:**
1. **Class luôn thắng Interface:** Nếu superclass có method cùng tên → bỏ qua default method.
2. **Sub-interface thắng super-interface:** Interface cụ thể hơn (con) thắng.
3. **Nếu vẫn mơ hồ** → Lỗi biên dịch, bắt buộc override.

---
---

# PHẦN 4: FUNCTIONAL INTERFACE

## 4.1. Định nghĩa

**Functional Interface** là interface có **DUY NHẤT 1 phương thức trừu tượng** (SAM — Single Abstract Method). Nó có thể có bao nhiêu `default` hay `static` method tùy ý, nhưng **chỉ được 1 abstract method**.

```java
@FunctionalInterface // Annotation giúp compiler kiểm tra
interface Greeting {
    void sayHello(String name); // Chỉ 1 abstract method → ✅ Functional Interface
    
    default void sayBye() { System.out.println("Bye!"); }   // OK, default method
    static void info() { System.out.println("Greeting v1"); } // OK, static method
}
```

## 4.2. Bốn Functional Interface cốt lõi

Java cung cấp sẵn 4 interface trong gói `java.util.function` mà bạn sẽ dùng **hàng ngày**:

### Predicate — Kiểm tra điều kiện
```java
import java.util.function.Predicate;

// Predicate<T>: Nhận T → Trả về boolean
Predicate<Integer> isEven = n -> n % 2 == 0;
System.out.println(isEven.test(4)); // true
System.out.println(isEven.test(7)); // false
```

### Consumer — Tiêu thụ dữ liệu (không trả về)
```java
import java.util.function.Consumer;

// Consumer<T>: Nhận T → Không trả về gì (void)
Consumer<String> printer = s -> System.out.println("Xin chào " + s);
printer.accept("Khôi"); // "Xin chào Khôi"
```

### Function — Biến đổi dữ liệu
```java
import java.util.function.Function;

// Function<T, R>: Nhận T → Trả về R
Function<String, Integer> getLength = s -> s.length();
System.out.println(getLength.apply("Java")); // 4
```

### Supplier — Cung cấp dữ liệu (không nhận tham số)
```java
import java.util.function.Supplier;

// Supplier<T>: Không nhận gì → Trả về T
Supplier<Double> randomNumber = () -> Math.random();
System.out.println(randomNumber.get()); // 0.7856...
```

## 4.3. Các biến thể mở rộng

### Bi- (Hai tham số):
```java
import java.util.function.*;

// BiPredicate: 2 tham số → boolean
BiPredicate<String, Integer> isLongerThan = (s, len) -> s.length() > len;
isLongerThan.test("Hello", 3); // true

// BiFunction: 2 tham số → kết quả
BiFunction<Integer, Integer, String> sumToString = (a, b) -> "Tổng: " + (a + b);
sumToString.apply(3, 5); // "Tổng: 8"

// BiConsumer: 2 tham số → void
BiConsumer<String, Integer> printInfo = (name, age) -> 
    System.out.println(name + " - " + age + " tuổi");
printInfo.accept("Khôi", 25); // "Khôi - 25 tuổi"
```

### Operator (Input/Output cùng kiểu):
```java
import java.util.function.*;

// UnaryOperator<T> = Function<T, T>
UnaryOperator<String> toUpper = s -> s.toUpperCase();
toUpper.apply("hello"); // "HELLO"

// BinaryOperator<T> = BiFunction<T, T, T>
BinaryOperator<Integer> multiply = (a, b) -> a * b;
multiply.apply(3, 5); // 15
```

## 4.4. Tổ hợp hàm (Composition) — Nối chuỗi Predicate, Function

Đây là tính năng cực kỳ mạnh mẽ cho phép **nối nhiều điều kiện** hoặc **nhiều phép biến đổi** lại với nhau:

```java
import java.util.function.*;

// --- Tổ hợp Predicate ---
Predicate<Integer> isPositive = n -> n > 0;
Predicate<Integer> isEven = n -> n % 2 == 0;

Predicate<Integer> isPositiveAndEven = isPositive.and(isEven);    // VÀ
Predicate<Integer> isPositiveOrEven = isPositive.or(isEven);      // HOẶC
Predicate<Integer> isNotPositive = isPositive.negate();             // PHỦI ĐỊNH

System.out.println(isPositiveAndEven.test(4));  // true  (4 > 0 VÀ 4 chẵn)
System.out.println(isPositiveAndEven.test(-2)); // false (-2 không > 0)

// --- Tổ hợp Function ---
Function<Integer, Integer> doubleIt = n -> n * 2;
Function<Integer, Integer> addTen = n -> n + 10;

// andThen: Chạy doubleIt TRƯỚC, rồi addTen SAU → (5 * 2) + 10 = 20
Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);
System.out.println(doubleThenAdd.apply(5)); // 20

// compose: Chạy addTen TRƯỚC, rồi doubleIt SAU → (5 + 10) * 2 = 30
Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);
System.out.println(addThenDouble.apply(5)); // 30
```

---
---

# PHẦN 5: LAMBDA EXPRESSION & TYPE INFERENCE

## 5.1. Lambda Expression là gì?

**Lambda** là cú pháp viết tắt siêu gọn để tạo đối tượng từ **Functional Interface** mà không cần viết cả một class con dài dòng.

**Cú pháp:** `(tham_số) -> { thân_hàm }`

```java
// ❌ CÁCH CŨ: Anonymous Class (Dài dòng 6 dòng)
Runnable oldWay = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running...");
    }
};

// ✅ CÁCH MỚI: Lambda (Gọn 1 dòng)
Runnable newWay = () -> System.out.println("Running...");
```

## 5.2. Các dạng cú pháp Lambda

```java
// 1. Không tham số
Runnable r = () -> System.out.println("Hello");

// 2. Một tham số (BỎ được ngoặc tròn)
Consumer<String> c = s -> System.out.println(s);

// 3. Nhiều tham số (KHÔNG bỏ được ngoặc tròn)
BinaryOperator<Integer> add = (a, b) -> a + b;

// 4. Block body (Nhiều dòng → PHẢI có dấu ngoặc nhọn {} và return nếu cần)
Function<Integer, String> classify = n -> {
    if (n > 0) return "Dương";
    else if (n < 0) return "Âm";
    else return "Không";
};
```

## 5.3. Type Inference (Suy luận kiểu) trong Lambda

Java Compiler tự suy luận kiểu dữ liệu của tham số Lambda dựa trên Functional Interface:

```java
// Compiler biết MathOperation.calculate nhận (int, int) → tự hiểu a, b là int
MathOperation add = (a, b) -> a + b; // Không cần viết (int a, int b)

// Nếu muốn khai báo kiểu rõ ràng → PHẢI khai báo HẾT, không được trộn
MathOperation sub = (int a, int b) -> a - b; // OK
// MathOperation sub = (int a, b) -> a - b;  // ❌ LỖI — Không được trộn
```

**`var` trong Lambda (Từ Java 11):**
```java
// Cho phép dùng var (Hữu ích khi muốn gắn Annotation)
BiFunction<String, String, String> concat = (var a, var b) -> a + b;
```

## 5.4. Variable Capture — Bắt giữ biến bên ngoài

Lambda có thể truy cập biến local bên ngoài, nhưng biến đó phải là **effectively final** (giá trị không bao giờ thay đổi sau khi gán):

```java
int factor = 5; // Effectively final (không ai thay đổi nó)

Function<Integer, Integer> multiply = n -> n * factor; // ✅ OK

System.out.println(multiply.apply(3)); // 15

// factor = 10; // ❌ Nếu uncomment dòng này → Lambda ở trên sẽ báo lỗi biên dịch!
// Vì factor không còn là "effectively final" nữa
```

**Tại sao phải effectively final?** Vì Lambda **copy giá trị** của biến local vào bên trong (không phải tham chiếu). Nếu cho phép thay đổi sẽ gây ra kết quả không nhất quán.

## 5.5. Từ khóa `this` trong Lambda

```java
class MyClass {
    String name = "MyClass";
    
    void demo() {
        // Trong Lambda: this trỏ đến MyClass (class bao bọc)
        Runnable lambda = () -> System.out.println(this.name); // "MyClass"
        
        // Trong Anonymous Class: this trỏ đến chính Anonymous Class
        Runnable anon = new Runnable() {
            String name = "Anonymous";
            @Override
            public void run() {
                System.out.println(this.name); // "Anonymous"
            }
        };
    }
}
```

---
---

# PHẦN 6: METHOD REFERENCE

## 6.1. Khái niệm

Khi Lambda chỉ làm mỗi 1 việc là **gọi một hàm đã có sẵn**, bạn có thể rút gọn hơn nữa bằng cú pháp `::` (Method Reference):

```java
// Lambda:          name -> System.out.println(name)
// Method Reference: System.out::println
```

## 6.2. Bốn loại Method Reference

### Loại 1: Tham chiếu đến Static Method
```java
// Lambda:           s -> Integer.parseInt(s)
// Method Reference: Integer::parseInt
Function<String, Integer> parser = Integer::parseInt;
System.out.println(parser.apply("123")); // 123
```

### Loại 2: Tham chiếu đến Instance Method của một đối tượng CỤ THỂ
```java
// Lambda:           x -> System.out.println(x)
// Method Reference: System.out::println
Consumer<String> printer = System.out::println;
printer.accept("Hello"); // Hello
```

### Loại 3: Tham chiếu đến Instance Method của một kiểu BẤT KỲ
```java
// Lambda:           (String s) -> s.toUpperCase()
// Method Reference: String::toUpperCase
// Tham số đầu tiên (s) trở thành receiver (đối tượng gọi hàm)
Function<String, String> upper = String::toUpperCase;
System.out.println(upper.apply("hello")); // HELLO
```

### Loại 4: Tham chiếu đến Constructor
```java
// Lambda:           () -> new ArrayList<>()
// Method Reference: ArrayList::new
Supplier<List<String>> listFactory = ArrayList::new;
List<String> newList = listFactory.get(); // Tạo ArrayList mới

// Array Constructor Reference
Function<Integer, String[]> arrayFactory = String[]::new;
String[] arr = arrayFactory.apply(5); // Tạo mảng String có 5 phần tử
```

## 6.3. Ví dụ so sánh tổng hợp

```java
List<String> names = Arrays.asList("khoi", "java", "spring");

// Lambda
names.stream().map(s -> s.toUpperCase()).forEach(s -> System.out.println(s));

// Method Reference (Ngắn gọn hơn)
names.stream().map(String::toUpperCase).forEach(System.out::println);
```

---
---

# PHẦN 7: STREAM API

Đây là tính năng **vĩ đại nhất** của Java 8. Stream API cho phép xử lý Collection theo kiểu "đường ống" (pipeline) — dữ liệu chảy qua từng bước xử lý một cách mạch lạc và súc tích.

## 7.1. Stream là gì?

- Stream **KHÔNG PHẢI** là Collection — Nó không lưu trữ dữ liệu.
- Stream là một **pipeline xử lý dữ liệu** tuần tự.
- Stream **chỉ dùng MỘT LẦN** — Sau khi đóng, gọi lại sẽ bị lỗi `IllegalStateException`.
- Stream có tính **Lazy** (lười biếng) — Chỉ xử lý khi có Terminal Operation.

## 7.2. Tạo Stream

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;

// 1. Từ Collection
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> s1 = list.stream();

// 2. Từ mảng
String[] arr = {"x", "y", "z"};
Stream<String> s2 = Arrays.stream(arr);

// 3. Từ giá trị rời
Stream<String> s3 = Stream.of("hello", "world");

// 4. Stream rỗng
Stream<String> s4 = Stream.empty();

// 5. Stream số nguyên (IntStream)
IntStream range = IntStream.range(1, 10);       // 1, 2, ..., 9 (không gồm 10)
IntStream rangeClosed = IntStream.rangeClosed(1, 10); // 1, 2, ..., 10 (gồm 10)
```

## 7.3. Intermediate Operations — Thao tác trung gian

Các thao tác này **trả về Stream mới**, có thể nối chuỗi (chaining). Chúng chưa chạy cho đến khi gặp Terminal Operation.

### filter — Lọc phần tử
```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);

// Lọc ra các số chẵn
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
// [2, 4, 6, 8]
```

### map — Biến đổi từng phần tử
```java
List<String> names = List.of("khoi", "java", "spring");

// Chuyển tất cả thành chữ hoa
List<String> upperNames = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
// [KHOI, JAVA, SPRING]
```

### flatMap — Biến đổi + Làm phẳng
```java
// Mỗi phần tử → nhiều phần tử → gộp hết lại thành 1 stream phẳng
List<List<Integer>> nested = List.of(
    List.of(1, 2, 3),
    List.of(4, 5),
    List.of(6, 7, 8, 9)
);

List<Integer> flat = nested.stream()
    .flatMap(innerList -> innerList.stream())  // Mở từng list con ra
    .collect(Collectors.toList());
// [1, 2, 3, 4, 5, 6, 7, 8, 9]
```

### sorted — Sắp xếp
```java
List<String> fruits = List.of("Banana", "Apple", "Cherry");

// Sắp xếp mặc định (alphabetical)
List<String> sorted = fruits.stream()
    .sorted()
    .collect(Collectors.toList());
// [Apple, Banana, Cherry]

// Sắp xếp theo tiêu chí tùy chọn (theo độ dài)
List<String> byLength = fruits.stream()
    .sorted((a, b) -> a.length() - b.length())
    .collect(Collectors.toList());
// [Apple, Cherry, Banana]
```

### distinct, limit, skip
```java
List<Integer> nums = List.of(1, 2, 2, 3, 3, 3, 4, 5);

nums.stream().distinct().toList();          // [1, 2, 3, 4, 5] — Loại trùng
nums.stream().limit(3).toList();            // [1, 2, 2] — Lấy 3 phần tử đầu
nums.stream().skip(5).toList();             // [3, 4, 5] — Bỏ qua 5 phần tử đầu
```

### peek — Nhìn trộm (Debug)
```java
List<String> result = List.of("a", "b", "c").stream()
    .peek(s -> System.out.println("Trước map: " + s))
    .map(String::toUpperCase)
    .peek(s -> System.out.println("Sau map: " + s))
    .collect(Collectors.toList());
// Trước map: a → Sau map: A → Trước map: b → Sau map: B → ...
```

## 7.4. Terminal Operations — Thao tác kết thúc

Các thao tác này **sinh kết quả cuối cùng** và **đóng Stream lại**.

### forEach — Duyệt từng phần tử
```java
List.of("A", "B", "C").stream()
    .forEach(System.out::println);
// A
// B
// C
```

### collect — Gom kết quả
```java
// Gom vào List
List<String> list = stream.collect(Collectors.toList());

// Gom vào Set (tự động loại trùng)
Set<String> set = stream.collect(Collectors.toSet());

// Gom vào String (nối chuỗi)
String joined = List.of("Java", "is", "fun").stream()
    .collect(Collectors.joining(" "));
// "Java is fun"

String joinedWithComma = List.of("A", "B", "C").stream()
    .collect(Collectors.joining(", ", "[", "]"));
// "[A, B, C]"
```

### count, min, max
```java
List<Integer> nums = List.of(3, 1, 4, 1, 5, 9);

long count = nums.stream().count(); // 6
Optional<Integer> min = nums.stream().min(Integer::compareTo); // Optional[1]
Optional<Integer> max = nums.stream().max(Integer::compareTo); // Optional[9]
```

### reduce — Rút gọn toàn bộ stream thành 1 giá trị
```java
List<Integer> nums = List.of(1, 2, 3, 4, 5);

// Tính tổng
int sum = nums.stream().reduce(0, (a, b) -> a + b); // 15
// Giải thích: Bắt đầu từ 0, lấy 0+1=1, rồi 1+2=3, rồi 3+3=6, rồi 6+4=10, rồi 10+5=15

// Tìm tích
int product = nums.stream().reduce(1, (a, b) -> a * b); // 120
```

### findFirst, findAny, anyMatch, allMatch, noneMatch
```java
List<String> names = List.of("Khoi", "Java", "Spring", "Boot");

Optional<String> first = names.stream()
    .filter(s -> s.length() > 4)
    .findFirst(); // Optional["Spring"]

boolean anyStartsWithJ = names.stream().anyMatch(s -> s.startsWith("J")); // true
boolean allLong = names.stream().allMatch(s -> s.length() > 2); // true
boolean noneEmpty = names.stream().noneMatch(String::isEmpty); // true
```

## 7.5. Collectors nâng cao

### groupingBy — Nhóm dữ liệu
```java
List<String> words = List.of("hello", "hi", "world", "we", "hope", "win");

// Nhóm theo chữ cái đầu tiên
Map<Character, List<String>> grouped = words.stream()
    .collect(Collectors.groupingBy(w -> w.charAt(0)));
// {h=[hello, hi, hope], w=[world, we, win]}

// Nhóm theo độ dài
Map<Integer, List<String>> byLength = words.stream()
    .collect(Collectors.groupingBy(String::length));
// {2=[hi, we], 3=[win], 4=[hope], 5=[hello, world]}

// Nhóm + Đếm số lượng
Map<Character, Long> countByLetter = words.stream()
    .collect(Collectors.groupingBy(w -> w.charAt(0), Collectors.counting()));
// {h=3, w=3}
```

### partitioningBy — Chia 2 nhóm (true/false)
```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

Map<Boolean, List<Integer>> partitioned = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
// {false=[1, 3, 5], true=[2, 4, 6]}
```

### toMap — Gom vào Map
```java
List<String> names = List.of("Khoi", "Java", "Spring");

// Key: tên, Value: độ dài
Map<String, Integer> nameLengths = names.stream()
    .collect(Collectors.toMap(
        name -> name,           // keyMapper
        name -> name.length()   // valueMapper
    ));
// {Khoi=4, Java=4, Spring=6}
```

### summarizingInt — Thống kê tổng hợp
```java
List<Integer> scores = List.of(85, 92, 78, 95, 88);

IntSummaryStatistics stats = scores.stream()
    .collect(Collectors.summarizingInt(Integer::intValue));

System.out.println("Số lượng: " + stats.getCount());   // 5
System.out.println("Tổng: " + stats.getSum());          // 438
System.out.println("Min: " + stats.getMin());            // 78
System.out.println("Max: " + stats.getMax());            // 95
System.out.println("Trung bình: " + stats.getAverage()); // 87.6
```

## 7.6. Primitive Streams — IntStream, LongStream, DoubleStream

Dùng để tránh chi phí Boxing/Unboxing khi xử lý số:

```java
import java.util.stream.IntStream;

// Tạo dãy số
IntStream.range(1, 6).forEach(System.out::println); // 1, 2, 3, 4, 5

// Các hàm thống kê riêng (không cần Collectors)
int sum = IntStream.of(1, 2, 3, 4, 5).sum(); // 15
double avg = IntStream.of(1, 2, 3, 4, 5).average().orElse(0); // 3.0
int max = IntStream.of(1, 2, 3, 4, 5).max().orElse(0); // 5

// Chuyển từ Stream<Integer> sang IntStream
List<Integer> nums = List.of(1, 2, 3);
int total = nums.stream().mapToInt(Integer::intValue).sum(); // 6

// Chuyển từ IntStream sang Stream<Integer>
Stream<Integer> boxed = IntStream.of(1, 2, 3).boxed();
```

## 7.7. Cạm bẫy quan trọng

```java
// ❌ Stream chỉ dùng MỘT LẦN
Stream<String> s = List.of("a", "b").stream();
s.forEach(System.out::println); // OK
s.forEach(System.out::println); // ❌ IllegalStateException!

// ❌ Thiếu Terminal Operation → Stream không chạy gì cả (Lazy)
List.of("a", "b", "c").stream()
    .filter(s -> {
        System.out.println("Filtering: " + s); // KHÔNG BAO GIỜ IN RA
        return true;
    }); // Thiếu .collect() hoặc .forEach() → Không chạy!

// ✅ Đặt filter TRƯỚC sorted để giảm số phần tử cần sort
stream.filter(...).sorted(...); // Tốt
stream.sorted(...).filter(...); // Tệ hơn (sort hết rồi mới lọc)
```

---
---

# PHẦN 8: OPTIONAL

## 8.1. Vấn đề: NullPointerException

Lỗi `NullPointerException` (NPE) là lỗi gây crash app nhiều nhất trong Java. **Optional** là chiếc "hộp" bọc lấy một giá trị, buộc bạn phải kiểm tra trước khi lấy ra.

## 8.2. Khởi tạo Optional

```java
import java.util.Optional;

// 1. Optional chứa giá trị (BẮT BUỘC không null)
Optional<String> opt1 = Optional.of("Hello"); 
// Optional.of(null) → Ném NullPointerException ngay!

// 2. Optional có thể null hoặc không
Optional<String> opt2 = Optional.ofNullable("Hello"); // Optional["Hello"]
Optional<String> opt3 = Optional.ofNullable(null);    // Optional.empty

// 3. Optional rỗng
Optional<String> opt4 = Optional.empty();
```

## 8.3. Lấy giá trị an toàn

```java
Optional<String> name = Optional.ofNullable(getUserName()); // Có thể null

// ❌ KHÔNG NÊN: get() ném NoSuchElementException nếu rỗng
// String value = name.get(); 

// ✅ orElse — Giá trị mặc định (LUÔN được đánh giá dù có hay không)
String value1 = name.orElse("Khách");

// ✅ orElseGet — Giá trị mặc định LAZY (CHỈ đánh giá khi rỗng)
String value2 = name.orElseGet(() -> generateDefaultName());

// ✅ orElseThrow — Ném Exception nếu rỗng
String value3 = name.orElseThrow(() -> new RuntimeException("Tên không được trống!"));
```

> **QUAN TRỌNG — orElse vs orElseGet:**
> ```java
> // orElse: Hàm queryDatabase() LUÔN LUÔN bị gọi (dù Optional có giá trị)
> String r1 = opt.orElse(queryDatabase()); // ❌ Lãng phí nếu opt có giá trị
> 
> // orElseGet: Hàm queryDatabase() CHỈ bị gọi khi Optional rỗng
> String r2 = opt.orElseGet(() -> queryDatabase()); // ✅ Tiết kiệm
> ```

## 8.4. Xử lý có điều kiện

```java
Optional<String> name = Optional.of("Khoi");

// ifPresent — Chạy code nếu CÓ giá trị
name.ifPresent(n -> System.out.println("Xin chào " + n)); // "Xin chào Khoi"

// ifPresentOrElse (Java 9) — Chạy code cho cả 2 trường hợp
name.ifPresentOrElse(
    n -> System.out.println("Xin chào " + n),  // Nếu CÓ
    () -> System.out.println("Không có tên")     // Nếu RỖNG
);
```

## 8.5. Biến đổi (Transformation)

```java
Optional<String> name = Optional.of("khoi nguyen");

// map — Biến đổi giá trị bên trong Optional
Optional<String> upperName = name.map(String::toUpperCase);
// Optional["KHOI NGUYEN"]

// filter — Lọc giá trị
Optional<String> longName = name.filter(n -> n.length() > 5);
// Optional["khoi nguyen"] (vì length = 12 > 5)

Optional<String> shortName = name.filter(n -> n.length() > 20);
// Optional.empty (vì length = 12 < 20)

// flatMap — Khi hàm biến đổi trả về Optional (tránh Optional<Optional<T>>)
Optional<String> result = name.flatMap(n -> findNickname(n)); 
// findNickname trả về Optional<String> → flatMap mở hộp ra luôn
```

## 8.6. Anti-patterns — Cách dùng SAI (Tránh xa!)

```java
// ❌ SAI: Dùng isPresent() + get() → Chẳng khác gì check null
if (opt.isPresent()) {
    String value = opt.get();
}
// ✅ ĐÚNG: Dùng orElse hoặc ifPresent

// ❌ SAI: Dùng Optional làm tham số hàm
public void doSomething(Optional<String> name) { ... }
// ✅ ĐÚNG: Dùng overloading hoặc @Nullable

// ❌ SAI: Dùng Optional làm field/thuộc tính class (Không Serializable)
class User {
    Optional<String> nickname; // ❌
}
// ✅ ĐÚNG: Field dùng kiểu bình thường, getter trả về Optional
class User {
    private String nickname; // Có thể null
    public Optional<String> getNickname() { return Optional.ofNullable(nickname); }
}

// ❌ SAI: Optional<List<T>> → Dư thừa
Optional<List<String>> opt = Optional.of(Collections.emptyList());
// ✅ ĐÚNG: Trả về List rỗng thay vì null
List<String> result = Collections.emptyList();
```

---
---

# PHẦN 9: GENERICS TRONG FUNCTIONAL PROGRAMMING

## 9.1. Ôn tập nhanh Generics

Generics (`<T>`) cho phép tạo class/method hoạt động với **nhiều kiểu dữ liệu** mà vẫn an toàn kiểu:

```java
// Generic Class
class Box<T> {
    private T item;
    public void set(T item) { this.item = item; }
    public T get() { return item; }
}

Box<String> stringBox = new Box<>();
stringBox.set("Hello");

Box<Integer> intBox = new Box<>();
intBox.set(42);
```

## 9.2. Wildcard — PECS (Producer Extends, Consumer Super)

Đây là nguyên tắc vàng khi thiết kế API với Generics:

```java
// <? extends T> — Dùng khi ĐỌC dữ liệu (Producer Extends)
public static double sumOfList(List<? extends Number> list) {
    double sum = 0;
    for (Number n : list) {
        sum += n.doubleValue();
    }
    return sum;
}
// Có thể truyền List<Integer>, List<Double>, List<Number>

// <? super T> — Dùng khi GHI dữ liệu (Consumer Super)
public static void addIntegers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
}
// Có thể truyền List<Integer>, List<Number>, List<Object>
```

## 9.3. Kết hợp Generics + Lambda + Stream

Đây là sức mạnh tổng hợp — tạo ra các hàm tiện ích **đa năng** dùng cho mọi kiểu dữ liệu:

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class GenericUtils {

    // Hàm lọc đa năng: Dùng cho List<Integer>, List<String>, List<Student>,...
    public static <T> List<T> filterBy(List<T> list, Predicate<T> condition) {
        return list.stream()
            .filter(condition)
            .collect(Collectors.toList());
    }

    // Hàm biến đổi đa năng: Biến List<T> thành List<R>
    public static <T, R> List<R> transformList(List<T> list, Function<T, R> transformer) {
        return list.stream()
            .map(transformer)
            .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        // Lọc số chẵn
        List<Integer> evens = filterBy(List.of(1, 2, 3, 4, 5), n -> n % 2 == 0);
        System.out.println(evens); // [2, 4]

        // Lọc chuỗi dài hơn 4 ký tự
        List<String> longWords = filterBy(
            List.of("Java", "Spring", "Boot", "Microservices"),
            s -> s.length() > 4
        );
        System.out.println(longWords); // [Spring, Microservices]

        // Biến đổi: List tên → List độ dài
        List<Integer> lengths = transformList(
            List.of("Java", "Spring", "Boot"),
            String::length
        );
        System.out.println(lengths); // [4, 6, 4]
    }
}
```

---
---

## Tổng kết Lesson 18

| Chủ đề | Khi nào dùng? |
|:---|:---|
| **Regex** | Validate form, tìm kiếm/thay thế chuỗi phức tạp |
| **Date Time API** | Mọi thao tác liên quan đến ngày giờ (dùng `java.time`, KHÔNG dùng `java.util.Date`) |
| **Default Method** | Mở rộng Interface mà không phá vỡ code cũ |
| **Functional Interface** | Nền tảng cho Lambda, Stream, Optional |
| **Lambda Expression** | Viết code ngắn gọn thay cho Anonymous Class |
| **Method Reference** | Rút gọn Lambda khi chỉ gọi 1 hàm có sẵn |
| **Stream API** | Lọc, biến đổi, gom nhóm dữ liệu từ Collection |
| **Optional** | Tránh NullPointerException, xử lý giá trị có thể null |
| **Generics + FP** | Tạo hàm/class đa năng, tái sử dụng cho mọi kiểu dữ liệu |

---
---

# PHẦN 10: BÀI TẬP THỰC HÀNH TỔNG HỢP

Để master các kiến thức đồ sộ trong bài học này, bạn cần thực hành rất nhiều. Dưới đây là danh sách bài tập cho từng chủ đề, từ cơ bản đến nâng cao.

## 10.1. Bài tập Regex

**Bài 1: Validate biển số xe máy Việt Nam**
Viết chương trình kiểm tra biển số xe máy hợp lệ. 
Ví dụ hợp lệ: `29-A1 123.45`, `30-K9 1234`.
*Gợi ý: Bắt đầu bằng 2 chữ số, tiếp theo là 1 chữ cái và 1 chữ số, dấu cách, sau đó là 4 hoặc 5 chữ số (có thể có dấu chấm).*

**Bài 2: Trích xuất các URL từ một đoạn văn bản HTML**
Cho một chuỗi HTML chứa nhiều thẻ `<a>`. Hãy viết Regex để trích xuất toàn bộ các đường link (giá trị trong thuộc tính `href="..."`).
*Ví dụ input:* `<a href="https://tayjava.vn">Trang chủ</a>` -> *Output:* `https://tayjava.vn`

**Bài 3: Chuẩn hóa khoảng trắng trong chuỗi**
Viết một hàm nhận vào một chuỗi, loại bỏ các khoảng trắng thừa (2 khoảng trắng liền nhau trở lên) và thay bằng 1 khoảng trắng duy nhất, đồng thời xóa khoảng trắng ở đầu và cuối chuỗi. Không dùng hàm `trim()`.

**Bài 4: Tìm từ lặp lại trong câu**
Dùng Backreference (Tham chiếu ngược) để tìm và in ra các từ bị gõ lặp liên tiếp trong câu.
*Ví dụ:* "Tôi đi học học bài và làm làm bài tập" -> Bắt được "học học", "làm làm".

**Bài 5: Validate Password Mạnh (Lookahead)**
Viết Regex kiểm tra password thỏa mãn:
- Ít nhất 8 ký tự.
- Chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số, 1 ký tự đặc biệt (`@, $, !, %, *, ?, &`).

---

## 10.2. Bài tập Date Time API

**Bài 1: Tính số ngày còn lại đến sinh nhật**
Nhập vào ngày sinh của bạn. Tính xem từ hôm nay (`LocalDate.now()`) đến sinh nhật tiếp theo của bạn còn bao nhiêu ngày.

**Bài 2: Máy tính hạn sử dụng thẻ tín dụng**
Viết chương trình nhận vào ngày phát hành thẻ (VD: "10/2026"). Thẻ có hạn 3 năm. Hãy in ra ngày cuối cùng của tháng hết hạn. (Gợi ý: Dùng `YearMonth` và `TemporalAdjusters.lastDayOfMonth()`).

**Bài 3: Chuyển đổi múi giờ cho hệ thống Meeting**
Bạn tạo một lịch họp lúc `15:00` giờ Việt Nam (`Asia/Ho_Chi_Minh`). Đối tác của bạn ở Tokyo (`Asia/Tokyo`) và New York (`America/New_York`). Hãy in ra giờ họp tương ứng trên máy tính của đối tác.

**Bài 4: Tính số ngày làm việc (Trừ T7, CN)**
Viết một hàm nhận vào 2 đối tượng `LocalDate` (start và end). Trả về tổng số ngày làm việc giữa 2 khoảng thời gian này (không tính thứ 7 và Chủ Nhật).

**Bài 5: Log Parser Timestamp**
Cho một file log chứa các dòng bắt đầu bằng thời gian chuẩn ISO: `2026-10-06T14:30:45.123Z [ERROR] Database timeout`. Viết chương trình đọc chuỗi thời gian trên, parse thành `Instant`, sau đó chuyển thành `LocalDateTime` hiển thị theo định dạng `dd/MM/yyyy HH:mm:ss` (múi giờ mặc định của hệ thống).

---

## 10.3. Bài tập Lambda, Functional Interface & Method Reference

**Bài 1: Filter Custom bằng Predicate**
Tự định nghĩa một phương thức `public static <T> List<T> myFilter(List<T> list, Predicate<T> predicate)`. 
Sử dụng hàm này để:
- Lọc ra các số nguyên tố từ một `List<Integer>`.
- Lọc ra các sinh viên có điểm > 8.0 từ một `List<Student>`.

**Bài 2: Tính toán động bằng Function & BiFunction**
Viết một hệ thống tính lương đơn giản.
- Dùng `Function<Double, Double>` để tính thuế thu nhập (ví dụ 10%).
- Dùng `BiFunction<Double, Integer, Double>` để tính tổng lương = Lương cơ bản * Hệ số - Thuế.

**Bài 3: Pipeline xử lý String với UnaryOperator**
Cho một `List<UnaryOperator<String>>` chứa các bước xử lý chuỗi: Xóa khoảng trắng 2 đầu -> Viết hoa toàn bộ -> Thay thế "JAVA" thành "JAVA 8+". 
Viết chương trình duyệt qua List này và áp dụng tuần tự các thao tác lên một chuỗi input.

**Bài 4: Chuyển đổi Collection bằng Method Reference**
Cho một `List<String>` chứa các chuỗi số (VD: `["1", "2", "3"]`). Dùng Method Reference của `Integer` để biến đổi List này thành `List<Integer>`.

**Bài 5: Consumer và Supplier kết hợp**
Viết hàm `processWithLog(Supplier<String> dataSupplier, Consumer<String> dataConsumer)`. Hàm này sẽ lấy dữ liệu từ Supplier, in ra dòng log "Đang xử lý dữ liệu...", sau đó đưa dữ liệu vào Consumer để xử lý.

---

## 10.4. Bài tập Stream API

**Bài 1: Xử lý danh sách Sản phẩm (Product)**
Cho `List<Product>` (id, name, price, category).
- Lọc ra các sản phẩm thuộc category "Laptop" có giá > 15 triệu.
- Sắp xếp kết quả theo giá giảm dần.
- Lấy ra tên của 3 sản phẩm đầu tiên và nối thành chuỗi, cách nhau bởi dấu phẩy.

**Bài 2: Gom nhóm (GroupingBy) Đơn hàng**
Cho `List<Order>` (orderId, customerName, totalAmount, status).
- Gom nhóm các đơn hàng theo `status` (PENDING, COMPLETED, CANCELLED).
- Tính tổng tiền của tất cả các đơn hàng COMPLETED.

**Bài 3: Thống kê Điểm Sinh Viên (IntSummaryStatistics)**
Cho mảng điểm `int[] scores = {5, 7, 8, 9, 4, 10, 6}`. Dùng Stream để tìm Điểm cao nhất, Điểm thấp nhất, và Điểm trung bình trong một lần chạy duy nhất.

**Bài 4: Tìm hiểu flatMap**
Cho một `List<Department>`, mỗi `Department` chứa một `List<Employee>`. Sử dụng `flatMap` để tạo ra một danh sách chứa TẤT CẢ các Employee trong công ty, sau đó lọc ra những người có số năm kinh nghiệm > 5.

**Bài 5: Chữ cái xuất hiện nhiều nhất**
Cho một chuỗi văn bản dài. Dùng Stream biến đổi chuỗi thành danh sách các ký tự, lọc bỏ khoảng trắng, gom nhóm để đếm tần suất mỗi ký tự, và tìm ra ký tự xuất hiện nhiều nhất.

---

## 10.5. Bài tập Optional

**Bài 1: Tránh NPE cơ bản**
Viết hàm `public Optional<User> findUserById(int id)` giả lập tìm user trong DB. Nếu id = 1 thì trả về User(id=1, name="Khoi"), ngược lại rỗng. Trong hàm `main`, gọi hàm trên, in ra tên user nếu tìm thấy, nếu không in ra "User not found" (Dùng Optional API, cấm dùng if-else).

**Bài 2: Optional trong Object lồng nhau**
Cho các class `Company` -> chứa `Department` -> chứa `Manager` -> chứa `String name`. (Tất cả đều dùng getter có thể trả về null).
Viết hàm lấy tên Manager của một Company truyền vào. Dùng Optional `map` và `flatMap` để xử lý chuỗi này, nếu bất kỳ bước nào null, trả về "No Manager".

**Bài 3: Xử lý giá trị Default bằng orElseGet**
Giả sử hàm lấy cấu hình từ database `getConfigFromDb()` rất chậm. Viết logic: Thử lấy cấu hình bằng biến môi trường (Environment Variable), bọc trong Optional. Nếu rỗng, dùng `orElseGet` để gọi hàm `getConfigFromDb()`.

**Bài 4: Ném Exception với Optional**
Lấy một chuỗi số lượng sản phẩm từ API (có thể null). Bọc vào Optional. Biến đổi (map) chuỗi đó thành Integer. Nếu chuỗi rỗng hoặc lỗi, ném ra ngoại lệ `IllegalArgumentException("Invalid quantity")` bằng `orElseThrow`.

**Bài 5: Kết hợp Stream và Optional**
Cho `List<Optional<String>>` chứa nhiều email (có cái có, có cái rỗng). Viết Stream để bóc tất cả các Optional có giá trị ra, nối chúng thành một chuỗi duy nhất cách nhau bởi dấu chấm phẩy `;`. Dùng hàm `flatMap(Optional::stream)` (có từ Java 9).
