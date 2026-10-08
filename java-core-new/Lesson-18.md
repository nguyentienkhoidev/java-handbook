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
- [PHẦN 10: Bài tập thực hành tổng hợp](#phần-10-bài-tập-thực-hành-tổng-hợp)

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

### 📌 Bài 1: Tính số ngày còn lại đến sinh nhật
- **Mục tiêu:** Sử dụng `LocalDate`, `ChronoUnit.DAYS.between()`, xử lý cộng/đổi năm và trường hợp đặc biệt (năm nhuận ngày 29/02).
- **Yêu cầu kỹ thuật:**
  - Nhập vào ngày sinh của bạn (`LocalDate birthDate`).
  - Lấy ngày hôm nay qua `LocalDate.now()`.
  - Xác định ngày sinh nhật kế tiếp:
    - Nếu sinh nhật trong năm nay chưa diễn ra, lấy sinh nhật năm nay.
    - Nếu sinh nhật năm nay đã trôi qua hoặc là hôm nay, chuyển sang sinh nhật năm tiếp theo (`plusYears(1)`).
  - Tính và in ra số ngày còn lại đến sinh nhật tiếp theo.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BirthdayCountdownDemo {
    public static void main(String[] args) {
        // Giả sử ngày sinh: 20/11/2000
        LocalDate birthDate = LocalDate.of(2000, 11, 20);
        LocalDate today = LocalDate.now();

        System.out.println("Hôm nay là: " + today);
        System.out.println("Ngày sinh : " + birthDate);

        // 1. Gán năm hiện tại vào ngày sinh
        LocalDate nextBirthday = birthDate.withYear(today.getYear());

        // 2. Nếu sinh nhật năm nay đã qua hoặc là hôm nay -> tính sinh nhật năm sau
        if (nextBirthday.isBefore(today) || nextBirthday.isEqual(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }

        // 3. Tính khoảng cách số ngày
        long daysRemaining = ChronoUnit.DAYS.between(today, nextBirthday);

        System.out.println("Sinh nhật kế tiếp rơi vào: " + nextBirthday);
        System.out.println("Số ngày còn lại đến sinh nhật: " + daysRemaining + " ngày");
    }
}
```

**Output mẫu:**
```text
Hôm nay là: 2026-10-08
Ngày sinh : 2000-11-20
Sinh nhật kế tiếp rơi vào: 2026-11-20
Số ngày còn lại đến sinh nhật: 43 ngày
```

**Giải thích chi tiết:**
- `birthDate.withYear(today.getYear())`: Tạo ra đối tượng `LocalDate` mới giữ nguyên ngày/tháng và thay bằng năm hiện tại.
- **Xử lý năm nhuận 29/02:** Nếu một người sinh vào `2004-02-29`, khi sang năm không nhuận (như 2026), `withYear()` trong Java tự động lùi về ngày `2026-02-28` hợp lệ mà không quăng lỗi `DateTimeException`.
- `ChronoUnit.DAYS.between(today, nextBirthday)`: Đo lường chính xác số ngày giữa 2 mốc thời gian.
</details>

---

### 📌 Bài 2: Máy tính hạn sử dụng thẻ tín dụng
- **Mục tiêu:** Thao tác với `YearMonth`, `DateTimeFormatter`, và bộ điều chỉnh thời gian `TemporalAdjusters.lastDayOfMonth()`.
- **Yêu cầu kỹ thuật:**
  - Nhận vào chuỗi tháng/năm phát hành thẻ (VD: `"10/2026"`).
  - Parse chuỗi thành `YearMonth`.
  - Thẻ có hạn 3 năm (`plusYears(3)`).
  - In ra ngày cuối cùng của tháng hết hạn theo định dạng `dd/MM/yyyy`.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class CreditCardExpiryDemo {
    public static void main(String[] args) {
        String issuedDateStr = "10/2026";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/yyyy");

        // 1. Parse chuỗi thành YearMonth
        YearMonth issuedYearMonth = YearMonth.parse(issuedDateStr, inputFormatter);

        // 2. Thẻ có thời hạn 3 năm
        YearMonth expiryYearMonth = issuedYearMonth.plusYears(3);

        // 3. Cách 1: Sử dụng trực tiếp atEndOfMonth() của YearMonth
        LocalDate expiryDate = expiryYearMonth.atEndOfMonth();

        // Hoặc Cách 2: Thông qua TemporalAdjusters
        // LocalDate expiryDate = expiryYearMonth.atDay(1).with(TemporalAdjusters.lastDayOfMonth());

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Tháng phát hành    : " + issuedDateStr);
        System.out.println("Tháng hết hạn thẻ  : " + expiryYearMonth.format(inputFormatter));
        System.out.println("Ngày hết hạn cuối  : " + expiryDate.format(outputFormatter));
    }
}
```

**Output mẫu:**
```text
Tháng phát hành    : 10/2026
Tháng hết hạn thẻ  : 10/2029
Ngày hết hạn cuối  : 31/10/2029
```

**Giải thích chi tiết:**
- `YearMonth`: Class hoàn hảo cho dữ liệu chỉ gồm tháng và năm như thời hạn thẻ, kỳ công, chu kỳ hóa đơn.
- `expiryYearMonth.atEndOfMonth()`: Trả về ngày cuối cùng tương ứng của tháng đó (ngày 28, 29, 30, hoặc 31), tự động tính đúng cho năm nhuận.
</details>

---

### 📌 Bài 3: Chuyển đổi múi giờ cho hệ thống Meeting
- **Mục tiêu:** Hiểu sâu `ZonedDateTime`, `ZoneId`, và phương thức chuyển múi giờ cùng thời khắc `withZoneSameInstant()`.
- **Yêu cầu kỹ thuật:**
  - Khởi tạo lịch họp lúc `15:00` ngày `15/10/2026` theo giờ Việt Nam (`Asia/Ho_Chi_Minh` - UTC+7).
  - Chuyển đổi lịch họp sang múi giờ của đối tác:
    - Tokyo (`Asia/Tokyo` - UTC+9)
    - New York (`America/New_York` - UTC-4 Daylight Saving Time)
  - In ra giờ họp tương ứng với từng địa phương kèm thông tin múi giờ.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class GlobalMeetingSchedulerDemo {
    public static void main(String[] args) {
        // 1. Tạo lịch họp tại Việt Nam: 15:00 ngày 15/10/2026
        LocalDateTime meetingLocal = LocalDateTime.of(2026, 10, 15, 15, 0);
        ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime vnMeeting = ZonedDateTime.of(meetingLocal, vnZone);

        // 2. Chuyển đổi sang múi giờ Tokyo và New York cùng một thời khắc thực tế
        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");
        ZonedDateTime tokyoMeeting = vnMeeting.withZoneSameInstant(tokyoZone);

        ZoneId nyZone = ZoneId.of("America/New_York");
        ZonedDateTime nyMeeting = vnMeeting.withZoneSameInstant(nyZone);

        // 3. Format hiển thị
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss (z, Z)");

        System.out.println("=== LỊCH HỌP TOÀN CẦU ===");
        System.out.println("Việt Nam  : " + vnMeeting.format(formatter));
        System.out.println("Tokyo     : " + tokyoMeeting.format(formatter));
        System.out.println("New York  : " + nyMeeting.format(formatter));
    }
}
```

**Output mẫu:**
```text
=== LỊCH HỌP TOÀN CẦU ===
Việt Nam  : 15/10/2026 15:00:00 (ICT, +0700)
Tokyo     : 15/10/2026 17:00:00 (JST, +0900)
New York  : 15/10/2026 04:00:00 (EDT, -0400)
```

**Giải thích chi tiết:**
- `withZoneSameInstant(ZoneId)`: Giữ nguyên cùng một thời khắc thực tế (Instant) trên toàn cầu và chuyển đổi giờ hiển thị theo giờ địa phương của vùng đích.
- Phân biệt với `withZoneSameLocal(ZoneId)`: hàm này giữ nguyên số `15:00` nhưng đổi mác múi giờ (nghĩa là cuộc họp bị dời sang 15:00 giờ New York, sai lệch thời gian họp chung).
</details>

---

### 📌 Bài 4: Tính số ngày làm việc (Trừ Thứ 7 & Chủ Nhật)
- **Mục tiêu:** Thao tác kiểm tra thứ trong tuần với `DayOfWeek`, duyệt khoảng ngày bằng vòng lặp (Java 8) hoặc luồng Stream `datesUntil` (Java 9+).
- **Yêu cầu kỹ thuật:**
  - Viết hàm `countWorkingDays(LocalDate start, LocalDate end)` tính số ngày làm việc (bỏ qua thứ 7 và chủ nhật).
  - Ném `IllegalArgumentException` nếu `start.isAfter(end)`.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.time.DayOfWeek;
import java.time.LocalDate;

public class WorkingDaysCalculatorDemo {

    // Cách 1: Vòng lặp cơ bản (Tương thích chuẩn Java 8+)
    public static long countWorkingDaysJava8(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc!");
        }

        long count = 0;
        LocalDate current = start;
        while (!current.isAfter(end)) {
            DayOfWeek dow = current.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                count++;
            }
            current = current.plusDays(1);
        }
        return count;
    }

    // Cách 2: Dùng Stream datesUntil (Từ Java 9+)
    public static long countWorkingDaysJava9(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc!");
        }

        return start.datesUntil(end.plusDays(1)) // Duyệt đến hết ngày end
                .filter(d -> d.getDayOfWeek() != DayOfWeek.SATURDAY 
                          && d.getDayOfWeek() != DayOfWeek.SUNDAY)
                .count();
    }

    public static void main(String[] args) {
        LocalDate start = LocalDate.of(2026, 10, 1); // Thứ 5
        LocalDate end = LocalDate.of(2026, 10, 15);  // Thứ 5

        System.out.println("Từ ngày: " + start + " đến ngày: " + end);
        System.out.println("Số ngày làm việc (Java 8): " + countWorkingDaysJava8(start, end) + " ngày");
        System.out.println("Số ngày làm việc (Java 9+ Stream): " + countWorkingDaysJava9(start, end) + " ngày");
    }
}
```

**Output mẫu:**
```text
Từ ngày: 2026-10-01 đến ngày: 2026-10-15
Số ngày làm việc (Java 8): 11 ngày
Số ngày làm việc (Java 9+ Stream): 11 ngày
```

**Giải thích chi tiết:**
- Trong 15 ngày từ 01/10 đến 15/10/2026 có 2 ngày Thứ Bảy (ngày 03, 10) và 2 ngày Chủ Nhật (ngày 04, 11). Tổng cộng 4 ngày nghỉ, số ngày làm việc là $15 - 4 = 11$ ngày.
- `start.datesUntil(end.plusDays(1))` sinh ra `Stream<LocalDate>` liên tục từ `start` đến trước cận trên, do đó ta cộng thêm 1 ngày vào `end` để tính cả ngày kết thúc.
</details>

---

### 📌 Bài 5: Log Parser Timestamp
- **Mục tiêu:** Parse chuỗi chuẩn ISO-8601 UTC (`Instant`), chuyển đổi sang `LocalDateTime` theo múi giờ hệ thống máy khách và định dạng lại hiển thị.
- **Yêu cầu kỹ thuật:**
  - Cho dòng log: `"2026-10-06T14:30:45.123Z [ERROR] Database timeout"`.
  - Tách lấy chuỗi timestamp ISO.
  - Parse thành `Instant`.
  - Chuyển `Instant` thành `LocalDateTime` theo `ZoneId.systemDefault()`.
  - Hiển thị theo định dạng `dd/MM/yyyy HH:mm:ss`.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LogTimestampParserDemo {
    public static void main(String[] args) {
        String logLine = "2026-10-06T14:30:45.123Z [ERROR] Database timeout";

        // 1. Tách chuỗi thời gian ISO trước dấu cách đầu tiên
        int spaceIndex = logLine.indexOf(" ");
        String isoTimestamp = logLine.substring(0, spaceIndex);
        String logMessage = logLine.substring(spaceIndex + 1);

        // 2. Parse thành Instant (chuẩn ISO-8601 có đuôi 'Z')
        Instant instant = Instant.parse(isoTimestamp);

        // 3. Chuyển thành LocalDateTime theo múi giờ hệ thống
        ZoneId systemZone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, systemZone);

        // 4. Định dạng hiển thị dd/MM/yyyy HH:mm:ss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedTime = localDateTime.format(formatter);

        System.out.println("Chuỗi log ban đầu : " + logLine);
        System.out.println("Múi giờ hệ thống  : " + systemZone);
        System.out.println("Thời gian chuẩn hóa: " + formattedTime);
        System.out.println("Nội dung thông báo: " + logMessage);
    }
}
```

**Output mẫu (Tại Việt Nam GMT+7):**
```text
Chuỗi log ban đầu : 2026-10-06T14:30:45.123Z [ERROR] Database timeout
Múi giờ hệ thống  : Asia/Ho_Chi_Minh
Thời gian chuẩn hóa: 06/10/2026 21:30:45
Nội dung thông báo: [ERROR] Database timeout
```

**Giải thích chi tiết:**
- Ký tự `Z` ở cuối chuỗi ISO đại diện cho UTC (Zulu time - độ lệch 0).
- Khi đổi sang múi giờ `Asia/Ho_Chi_Minh` (UTC+7), thời gian tự động được cộng thêm 7 tiếng ($14:30 + 7h = 21:30$).
- `Instant` là kiểu dữ liệu chuẩn mực nhất để lưu trữ timestamp trong cơ sở dữ liệu hoặc hệ thống log phân tán.
</details>

---

## 10.3. Bài tập Lambda, Functional Interface & Method Reference

### 📌 Bài 1: Filter Custom bằng Predicate
- **Mục tiêu:** Tự xây dựng phương thức tổng quát (Generic) nhận `Predicate<T>` để hiểu cách Lambda trừu tượng hóa điều kiện lọc.
- **Yêu cầu kỹ thuật:**
  - Định nghĩa `public static <T> List<T> myFilter(List<T> list, Predicate<T> predicate)`.
  - Dùng hàm trên lọc ra:
    - Các số nguyên tố từ `List<Integer>`.
    - Các sinh viên có điểm > 8.0 từ `List<Student>`.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CustomFilterPredicateDemo {

    // 1. Tự định nghĩa hàm filter tổng quát nhận Predicate
    public static <T> List<T> myFilter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (predicate.test(item)) { // Gọi hàm kiểm tra trừu tượng
                result.add(item);
            }
        }
        return result;
    }

    // Hàm kiểm tra số nguyên tố
    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // Class Student
    static class Student {
        private String name;
        private double score;

        public Student(String name, double score) {
            this.name = name;
            this.score = score;
        }

        public double getScore() { return score; }

        @Override
        public String toString() {
            return name + " (" + score + ")";
        }
    }

    public static void main(String[] args) {
        // Test 1: Lọc số nguyên tố
        List<Integer> numbers = Arrays.asList(2, 4, 7, 9, 11, 15, 17, 20);
        // Dùng Method Reference CustomFilterPredicateDemo::isPrime
        List<Integer> primes = myFilter(numbers, CustomFilterPredicateDemo::isPrime);
        System.out.println("Các số nguyên tố: " + primes);

        // Test 2: Lọc sinh viên điểm > 8.0
        List<Student> students = Arrays.asList(
                new Student("An", 8.5),
                new Student("Bình", 7.0),
                new Student("Cường", 9.2),
                new Student("Dung", 6.8)
        );
        // Dùng Lambda Expression
        List<Student> topStudents = myFilter(students, s -> s.getScore() > 8.0);
        System.out.println("Sinh viên giỏi (> 8.0): " + topStudents);
    }
}
```

**Output mẫu:**
```text
Các số nguyên tố: [2, 7, 11, 17]
Sinh viên giỏi (> 8.0): [An (8.5), Cường (9.2)]
```

**Giải thích chi tiết:**
- `Predicate<T>` là Functional Interface nhận vào 1 đối tượng kiểu `T` và trả về `boolean` thông qua hàm `test(T t)`.
- Nhờ Generic `<T>`, cùng một hàm `myFilter` có thể tái sử dụng cho mọi loại dữ liệu (`Integer`, `Student`, `Order`, ...), loại bỏ hoàn toàn việc lặp code duyệt mảng.
</details>

---

### 📌 Bài 2: Tính toán động bằng Function & BiFunction
- **Mục tiêu:** Áp dụng `Function<T, R>` và `BiFunction<T, U, R>` để xây dựng cơ chế tính lương linh hoạt, có thể thay đổi thuế động.
- **Yêu cầu kỹ thuật:**
  - Dùng `Function<Double, Double>` tính thuế thu nhập (VD: 10% nếu tổng thu nhập $\le$ 20 triệu, 20% nếu trên 20 triệu).
  - Dùng `BiFunction<Double, Integer, Double>` nhận vào Lương cơ bản và Hệ số lương để tính Lương thực lĩnh = (Lương cơ bản * Hệ số) - Thuế.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.function.BiFunction;
import java.util.function.Function;

public class SalaryCalculationDemo {
    public static void main(String[] args) {
        // 1. Function tính thuế: Thu nhập > 20tr chịu thuế 20%, ngược lại 10%
        Function<Double, Double> taxCalculator = grossSalary -> {
            if (grossSalary > 20_000_000) {
                return grossSalary * 0.20;
            }
            return grossSalary * 0.10;
        };

        // 2. BiFunction tính lương thực lĩnh: (Lương cơ bản, Hệ số) -> Lương sau thuế
        BiFunction<Double, Integer, Double> netSalaryCalculator = (baseSalary, coefficient) -> {
            double grossSalary = baseSalary * coefficient;
            double tax = taxCalculator.apply(grossSalary); // Sử dụng Function thuế
            return grossSalary - tax;
        };

        // Test tính lương
        double base1 = 5_000_000;
        int coef1 = 3; // Gross = 15 triệu -> Thuế 10% = 1.5 triệu -> Net = 13.5 triệu
        System.out.printf("Nhân viên 1: Lương thực lĩnh = %, .0f VNĐ\n", netSalaryCalculator.apply(base1, coef1));

        double base2 = 10_000_000;
        int coef2 = 3; // Gross = 30 triệu -> Thuế 20% = 6 triệu -> Net = 24 triệu
        System.out.printf("Nhân viên 2: Lương thực lĩnh = %, .0f VNĐ\n", netSalaryCalculator.apply(base2, coef2));
    }
}
```

**Output mẫu:**
```text
Nhân viên 1: Lương thực lĩnh =  13,500,000 VNĐ
Nhân viên 2: Lương thực lĩnh =  24,000,000 VNĐ
```

**Giải thích chi tiết:**
- `Function<T, R>`: Nhận 1 đối số kiểu `T`, trả về kết quả kiểu `R`.
- `BiFunction<T, U, R>`: Nhận 2 đối số kiểu `T`, `U` và trả về kết quả kiểu `R`.
- Logic tính thuế được đóng gói độc lập; nếu công ty thay đổi chính sách thuế (thuế lũy tiến từng phần), ta chỉ cần truyền một `Function` mới mà không phải sửa logic tính lương tổng quát.
</details>

---

### 📌 Bài 3: Pipeline xử lý String với UnaryOperator
- **Mục tiêu:** Áp dụng `UnaryOperator<String>` và chuỗi xử lý (Pipeline / Chain of Operations).
- **Yêu cầu kỹ thuật:**
  - Cho `List<UnaryOperator<String>>` gồm các bước:
    1. Cắt khoảng trắng 2 đầu (`String::trim`).
    2. Viết hoa toàn bộ chuỗi (`String::toUpperCase`).
    3. Thay thế `"JAVA"` thành `"JAVA 8+"`.
  - Viết chương trình duyệt qua danh sách và áp dụng lần lượt các thao tác lên chuỗi đầu vào.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class StringPipelineDemo {
    public static void main(String[] args) {
        // 1. Định nghĩa Pipeline gồm các bước biến đổi dữ liệu
        List<UnaryOperator<String>> pipeline = Arrays.asList(
                String::trim,                             // Bước 1: Trim khoảng trắng
                String::toUpperCase,                      // Bước 2: In hoa
                s -> s.replace("JAVA", "JAVA 8+")        // Bước 3: Thay thế từ khóa
        );

        String rawInput = "   học lập trình java cùng chuyên gia!   ";
        System.out.println("Input ban đầu: \"" + rawInput + "\"");

        // Cách 1: Vòng lặp duyệt áp dụng tuần tự
        String processed = rawInput;
        for (UnaryOperator<String> step : pipeline) {
            processed = step.apply(processed);
        }
        System.out.println("Kết quả sau pipeline (Vòng lặp) : \"" + processed + "\"");

        // Cách 2: Kết hợp toán tử bằng andThen / Stream reduce
        UnaryOperator<String> combinedPipeline = pipeline.stream()
                .reduce(UnaryOperator.identity(), (f1, f2) -> s -> f2.apply(f1.apply(s)));
        System.out.println("Kết quả sau pipeline (Combined)  : \"" + combinedPipeline.apply(rawInput) + "\"");
    }
}
```

**Output mẫu:**
```text
Input ban đầu: "   học lập trình java cùng chuyên gia!   "
Kết quả sau pipeline (Vòng lặp) : "HỌC LẬP TRÌNH JAVA 8+ CÙNG CHUYÊN GIA!"
Kết quả sau pipeline (Combined)  : "HỌC LẬP TRÌNH JAVA 8+ CÙNG CHUYÊN GIA!"
```

**Giải thích chi tiết:**
- `UnaryOperator<T>` là trường hợp đặc biệt của `Function<T, T>`, khi kiểu dữ liệu đầu vào và kết quả đầu ra là **cùng một kiểu**.
- Mô hình này là nền tảng của Middleware trong Web Server hoặc Data Sanitization Pipeline trong Clean Architecture.
</details>

---

### 📌 Bài 4: Chuyển đổi Collection bằng Method Reference
- **Mục tiêu:** Thành thạo cú pháp Static Method Reference `Integer::valueOf` / `Integer::parseInt` trong Stream API.
- **Yêu cầu kỹ thuật:**
  - Cho `List<String>` chuỗi số: `["1", "2", "3", "42", "99"]`.
  - Biến đổi thành `List<Integer>` bằng Method Reference.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MethodReferenceCollectionDemo {
    public static void main(String[] args) {
        List<String> rawNumbers = Arrays.asList("1", "2", "3", "42", "99");

        // Biến đổi List<String> sang List<Integer> bằng Method Reference
        List<Integer> integers = rawNumbers.stream()
                .map(Integer::valueOf) // Tương đương: s -> Integer.valueOf(s)
                .collect(Collectors.toList());

        System.out.println("Danh sách gốc (String) : " + rawNumbers);
        System.out.println("Danh sách số (Integer) : " + integers);

        // Tính tổng các số đã chuyển đổi
        int sum = integers.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Tổng các số: " + sum);
    }
}
```

**Output mẫu:**
```text
Danh sách gốc (String) : [1, 2, 3, 42, 99]
Danh sách số (Integer) : [1, 2, 3, 42, 99]
Tổng các số: 147
```

**Giải thích chi tiết:**
- `Integer::valueOf` là **Static Method Reference** (`ClassName::staticMethodName`). Trình biên dịch ngầm hiểu chuỗi `s` truyền vào sẽ đóng vai trò đối số của hàm `valueOf(s)`.
- Ưu điểm so với `Integer::parseInt`: `valueOf()` tận dụng Integer Cache (từ -128 đến 127), giúp tiết kiệm cấp phát bộ nhớ Heap.
</details>

---

### 📌 Bài 5: Consumer và Supplier kết hợp
- **Mục tiêu:** Phối hợp cơ chế cấp phát dữ liệu lười (Lazy Evaluation) của `Supplier<T>` và cơ chế tiêu thụ của `Consumer<T>`.
- **Yêu cầu kỹ thuật:**
  - Viết hàm `processWithLog(Supplier<String> dataSupplier, Consumer<String> dataConsumer)`.
  - Hàm sẽ lấy dữ liệu từ `Supplier`, in log trung gian "Đang xử lý dữ liệu...", sau đó giao cho `Consumer` tiêu thụ.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SupplierConsumerLoggerDemo {

    public static void processWithLog(Supplier<String> dataSupplier, Consumer<String> dataConsumer) {
        System.out.println("[LOG] Bắt đầu quy trình nạp dữ liệu...");
        // 1. Chỉ khi gọi get(), logic sinh dữ liệu mới thực sự chạy (Lazy Evaluation)
        String data = dataSupplier.get();

        System.out.println("[LOG] Đang xử lý dữ liệu...");
        // 2. Chuyển giao dữ liệu cho Consumer
        dataConsumer.accept(data);

        System.out.println("[LOG] Hoàn tất quy trình!\n");
    }

    public static void main(String[] args) {
        // Trường hợp 1: Supplier sinh mã Token ngẫu nhiên, Consumer in hoa
        Supplier<String> tokenSupplier = () -> "token_" + UUID.randomUUID();
        Consumer<String> consolePrinter = token -> System.out.println(">>> Đã nhận mã bảo mật: " + token.toUpperCase());

        processWithLog(tokenSupplier, consolePrinter);

        // Trường hợp 2: Supplier đọc dữ liệu giả lập từ Database
        Supplier<String> dbSupplier = () -> {
            // Giả lập query DB
            return "User Profile: {id: 101, username: 'nguyentienkhoi'}";
        };
        Consumer<String> auditConsumer = record -> System.out.println(">>> Lưu vào file Audit Log: " + record);

        processWithLog(dbSupplier, auditConsumer);
    }
}
```

**Output mẫu:**
```text
[LOG] Bắt đầu quy trình nạp dữ liệu...
[LOG] Đang xử lý dữ liệu...
>>> Đã nhận mã bảo mật: TOKEN_4B6349FA-6C4B-4E38-B702-DF7E2C9FA84D
[LOG] Hoàn tất quy trình!

[LOG] Bắt đầu quy trình nạp dữ liệu...
[LOG] Đang xử lý dữ liệu...
>>> Lưu vào file Audit Log: User Profile: {id: 101, username: 'nguyentienkhoi'}
[LOG] Hoàn tất quy trình!
```

**Giải thích chi tiết:**
- `Supplier<T>`: Không nhận tham số đầu vào, cung cấp kết quả kiểu `T` (`T get()`). Dùng để tạo giá trị khi cần thiết (Lazy Loading, Object Factory).
- `Consumer<T>`: Nhận vào tham số kiểu `T` nhưng không trả về gì (`void accept(T t)`). Dùng để in ấn, lưu database, gửi thông báo.
</details>

---

## 10.4. Bài tập Stream API

### 📌 Bài 1: Xử lý danh sách Sản phẩm (Product)
- **Mục tiêu:** Áp dụng liên hoàn các thao tác `filter()`, `sorted()`, `limit()`, `map()`, và gom chuỗi `Collectors.joining()`.
- **Yêu cầu kỹ thuật:**
  - Cho `List<Product>` (id, name, price, category).
  - Lọc sản phẩm thuộc danh mục "Laptop" có giá > 15 triệu VNĐ.
  - Sắp xếp giảm dần theo giá.
  - Lấy tên của 3 sản phẩm đắt nhất và nối thành chuỗi cách nhau bởi dấu phẩy.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductStreamProcessingDemo {

    static class Product {
        private int id;
        private String name;
        private double price;
        private String category;

        public Product(int id, String name, double price, String category) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.category = category;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
        public String getCategory() { return category; }
    }

    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
                new Product(1, "MacBook Pro M3", 45_000_000, "Laptop"),
                new Product(2, "Chuột Logitech MX Master", 2_200_000, "Phụ kiện"),
                new Product(3, "Dell XPS 13", 28_000_000, "Laptop"),
                new Product(4, "Asus ROG Zephyrus", 35_000_000, "Laptop"),
                new Product(5, "HP Pavilion 14", 12_000_000, "Laptop"),
                new Product(6, "Bàn phím cơ Keychron", 1_800_000, "Phụ kiện"),
                new Product(7, "Lenovo ThinkPad X1", 32_000_000, "Laptop")
        );

        String result = products.stream()
                // 1. Lọc: category "Laptop" và giá > 15 triệu
                .filter(p -> "Laptop".equalsIgnoreCase(p.getCategory()) && p.getPrice() > 15_000_000)
                // 2. Sắp xếp giảm dần theo giá
                .sorted(Comparator.comparingDouble(Product::getPrice).reversed())
                // 3. Lấy 3 sản phẩm hàng đầu
                .limit(3)
                // 4. Lấy ra tên sản phẩm
                .map(Product::getName)
                // 5. Nối chuỗi bằng dấu phẩy
                .collect(Collectors.joining(", "));

        System.out.println("Top 3 Laptop đắt nhất (> 15tr):");
        System.out.println(result);
    }
}
```

**Output mẫu:**
```text
Top 3 Laptop đắt nhất (> 15tr):
MacBook Pro M3, Asus ROG Zephyrus, Lenovo ThinkPad X1
```

**Giải thích chi tiết:**
- `Comparator.comparingDouble(Product::getPrice).reversed()`: So sánh số thực và đảo chiều thứ tự để đạt sắp xếp giảm dần.
- `Collectors.joining(", ")`: Bộ thu gom tối ưu của Stream giúp nối danh sách chuỗi kèm ký tự phân cách mà không bị thừa dấu phẩy ở phần tử cuối cùng.
</details>

---

### 📌 Bài 2: Gom nhóm (GroupingBy) Đơn hàng
- **Mục tiêu:** Nắm vững `Collectors.groupingBy()` và tính toán thống kê theo từng nhóm bằng Downstream Collector.
- **Yêu cầu kỹ thuật:**
  - Cho `List<Order>` (orderId, customerName, totalAmount, status).
  - Gom nhóm các đơn hàng theo `status` (`PENDING`, `COMPLETED`, `CANCELLED`).
  - Tính tổng tiền của tất cả các đơn hàng có trạng thái `COMPLETED`.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderGroupingDemo {

    static class Order {
        private String orderId;
        private String customerName;
        private double totalAmount;
        private String status;

        public Order(String orderId, String customerName, double totalAmount, String status) {
            this.orderId = orderId;
            this.customerName = customerName;
            this.totalAmount = totalAmount;
            this.status = status;
        }

        public String getOrderId() { return orderId; }
        public double getTotalAmount() { return totalAmount; }
        public String getStatus() { return status; }

        @Override
        public String toString() {
            return String.format("[%s: %, .0f VNĐ]", orderId, totalAmount);
        }
    }

    public static void main(String[] args) {
        List<Order> orders = Arrays.asList(
                new Order("ORD01", "Khoi", 2_500_000, "COMPLETED"),
                new Order("ORD02", "Hoa", 1_200_000, "PENDING"),
                new Order("ORD03", "Nam", 4_800_000, "COMPLETED"),
                new Order("ORD04", "Lan", 850_000, "CANCELLED"),
                new Order("ORD05", "Minh", 3_100_000, "COMPLETED")
        );

        // 1. Gom nhóm danh sách đơn hàng theo trạng thái
        Map<String, List<Order>> ordersByStatus = orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus));

        System.out.println("=== DANH SÁCH ĐƠN HÀNG THEO TRẠNG THÁI ===");
        ordersByStatus.forEach((status, orderList) -> {
            System.out.println(status + " (" + orderList.size() + " đơn): " + orderList);
        });

        // 2. Tính tổng doanh thu các đơn hàng COMPLETED
        double totalCompleted = orders.stream()
                .filter(o -> "COMPLETED".equalsIgnoreCase(o.getStatus()))
                .mapToDouble(Order::getTotalAmount)
                .sum();

        System.out.printf("\nTổng tiền các đơn hàng COMPLETED: %, .0f VNĐ\n", totalCompleted);

        // Cách nâng cao: Tính tổng tiền từng nhóm trạng thái chỉ trong 1 dòng bằng Downstream Collector
        Map<String, Double> revenueByStatus = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getStatus,
                        Collectors.summingDouble(Order::getTotalAmount)
                ));
        System.out.println("\nTổng doanh thu theo từng trạng thái: " + revenueByStatus);
    }
}
```

**Output mẫu:**
```text
=== DANH SÁCH ĐƠN HÀNG THEO TRẠNG THÁI ===
COMPLETED (3 đơn): [[ORD01:  2,500,000 VNĐ], [ORD03:  4,800,000 VNĐ], [ORD05:  3,100,000 VNĐ]]
CANCELLED (1 đơn): [[ORD04:  850,000 VNĐ]]
PENDING (1 đơn): [[ORD02:  1,200,000 VNĐ]]

Tổng tiền các đơn hàng COMPLETED:  10,400,000 VNĐ

Tổng doanh thu theo từng trạng thái: {COMPLETED=1.04E7, CANCELLED=850000.0, PENDING=1200000.0}
```

**Giải thích chi tiết:**
- `Collectors.groupingBy(Order::getStatus)`: Tương đương mệnh đề `GROUP BY status` trong SQL, trả về `Map<K, List<V>>`.
- `mapToDouble(Order::getTotalAmount).sum()`: Chuyển sang `DoubleStream` nguyên thủy để thực hiện phép cộng hiệu năng cao, tránh chi phí Autoboxing/Unboxing của kiểu `Double`.
</details>

---

### 📌 Bài 3: Thống kê Điểm Sinh Viên (IntSummaryStatistics)
- **Mục tiêu:** Sử dụng `summaryStatistics()` để lấy mọi thông số thống kê (Min, Max, Avg, Sum, Count) chỉ trong đúng 1 lượt duyệt duy nhất (Single Pass).
- **Yêu cầu kỹ thuật:**
  - Cho mảng `int[] scores = {5, 7, 8, 9, 4, 10, 6}`.
  - Tìm Điểm cao nhất, Điểm thấp nhất và Điểm trung bình chỉ trong một lần chạy Stream.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class StudentScoreStatisticsDemo {
    public static void main(String[] args) {
        int[] scores = {5, 7, 8, 9, 4, 10, 6};

        // Thu thập toàn bộ thống kê trong 1 lần duyệt Stream duy nhất
        IntSummaryStatistics stats = Arrays.stream(scores)
                .summaryStatistics();

        System.out.println("Mảng điểm gốc: " + Arrays.toString(scores));
        System.out.println("Số lượng sinh viên : " + stats.getCount());
        System.out.println("Điểm cao nhất (Max): " + stats.getMax());
        System.out.println("Điểm thấp nhất (Min): " + stats.getMin());
        System.out.printf("Điểm trung bình (Avg): %.2f\n", stats.getAverage());
        System.out.println("Tổng điểm          : " + stats.getSum());
    }
}
```

**Output mẫu:**
```text
Mảng điểm gốc: [5, 7, 8, 9, 4, 10, 6]
Số lượng sinh viên : 7
Điểm cao nhất (Max): 10
Điểm thấp nhất (Min): 4
Điểm trung bình (Avg): 7.00
Tổng điểm          : 49
```

**Giải thích chi tiết:**
- Thay vì phải duyệt mảng 3 lần (1 lần tính max, 1 lần tính min, 1 lần tính average), `IntSummaryStatistics` chỉ cần độ phức tạp thời gian $O(N)$ đúng 1 lần chạy, tiết kiệm CPU đáng kể với tập dữ liệu lớn.
</details>

---

### 📌 Bài 4: Tìm hiểu flatMap
- **Mục tiêu:** Phân biệt ranh giới giữa `map()` và `flatMap()`, làm phẳng cấu trúc dữ liệu lồng nhau (1-to-N).
- **Yêu cầu kỹ thuật:**
  - Cho `List<Department>`, mỗi `Department` chứa `List<Employee>`.
  - Dùng `flatMap` trích xuất danh sách tất cả các `Employee` của toàn công ty.
  - Lọc ra những nhân viên có số năm kinh nghiệm > 5 năm.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlatMapDepartmentDemo {

    static class Employee {
        private String name;
        private int experienceYears;

        public Employee(String name, int experienceYears) {
            this.name = name;
            this.experienceYears = experienceYears;
        }

        public String getName() { return name; }
        public int getExperienceYears() { return experienceYears; }

        @Override
        public String toString() {
            return name + " (" + experienceYears + " năm KN)";
        }
    }

    static class Department {
        private String name;
        private List<Employee> employees;

        public Department(String name, List<Employee> employees) {
            this.name = name;
            this.employees = employees;
        }

        public List<Employee> getEmployees() { return employees; }
    }

    public static void main(String[] args) {
        Department itDept = new Department("IT", Arrays.asList(
                new Employee("Tuấn", 6),
                new Employee("Hải", 3),
                new Employee("Phong", 8)
        ));

        Department hrDept = new Department("HR", Arrays.asList(
                new Employee("Mai", 2),
                new Employee("Trang", 7)
        ));

        List<Department> departments = Arrays.asList(itDept, hrDept);

        // Sử dụng flatMap để gộp phẳng List<Employee> của các phòng ban
        List<Employee> seniorEmployees = departments.stream()
                // dept.getEmployees().stream() trả về Stream<Employee>
                // flatMap gộp các Stream<Employee> con thành một Stream<Employee> duy nhất
                .flatMap(dept -> dept.getEmployees().stream())
                // Lọc nhân sự có > 5 năm kinh nghiệm
                .filter(emp -> emp.getExperienceYears() > 5)
                .collect(Collectors.toList());

        System.out.println("Nhân sự cấp cao (> 5 năm kinh nghiệm) toàn công ty:");
        seniorEmployees.forEach(emp -> System.out.println("- " + emp));
    }
}
```

**Output mẫu:**
```text
Nhân sự cấp cao (> 5 năm kinh nghiệm) toàn công ty:
- Tuấn (6 năm KN)
- Phong (8 năm KN)
- Trang (7 năm KN)
```

**Giải thích chi tiết:**
- `map(dept -> dept.getEmployees())` sẽ trả về `Stream<List<Employee>>` (dạng lồng nhau, không xử lý tiếp từng phần tử được).
- `flatMap(dept -> dept.getEmployees().stream())` sẽ "làm phẳng" (flatten) các stream con thành một luồng `Stream<Employee>` liên tục.
</details>

---

### 📌 Bài 5: Chữ cái xuất hiện nhiều nhất
- **Mục tiêu:** Ứng dụng Stream API kết hợp gom nhóm, đếm tần suất (`Collectors.counting()`) và tìm giá trị lớn nhất theo thuộc tính Map (`Map.Entry.comparingByValue()`).
- **Yêu cầu kỹ thuật:**
  - Cho một chuỗi văn bản dài.
  - Lọc bỏ khoảng trắng, chuẩn hóa chữ thường, đếm tần suất từng chữ cái.
  - Tìm ký tự xuất hiện nhiều nhất và số lần xuất hiện.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MostFrequentCharStreamDemo {
    public static void main(String[] args) {
        String text = "Java Stream API is elegant, declarative and extremely powerful!";

        // 1. Chuyển đổi text thành Map tần suất các chữ cái (bỏ khoảng trắng và dấu câu)
        Map<Character, Long> frequencyMap = text.chars()
                .filter(Character::isLetter)                         // Chỉ giữ lại chữ cái
                .mapToObj(c -> (char) Character.toLowerCase(c))     // Đưa về chữ thường
                .collect(Collectors.groupingBy(
                        Function.identity(),                         // Key: chính ký tự đó
                        Collectors.counting()                        // Value: số lần xuất hiện
                ));

        System.out.println("Bảng thống kê tần suất ký tự: " + frequencyMap);

        // 2. Tìm Entry có số lần xuất hiện (Value) lớn nhất
        Map.Entry<Character, Long> mostFrequent = frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (mostFrequent != null) {
            System.out.printf("\nKý tự xuất hiện nhiều nhất: '%c' với %d lần xuất hiện.\n",
                    mostFrequent.getKey(), mostFrequent.getValue());
        }
    }
}
```

**Output mẫu:**
```text
Bảng thống kê tần suất ký tự: {a=7, c=1, d=1, e=6, g=1, i=3, j=1, l=3, m=1, n=2, o=1, p=2, r=4, s=2, t=3, u=1, v=2, w=1, x=1, y=1}

Ký tự xuất hiện nhiều nhất: 'a' với 7 lần xuất hiện.
```

**Giải thích chi tiết:**
- `text.chars()`: Trả về `IntStream` các mã ASCII/Unicode của từng ký tự trong chuỗi.
- `Character::isLetter`: Loại bỏ dấu cách ` `, dấu phẩy `,`, dấu chấm than `!`.
- `Map.Entry.comparingByValue()`: Comparator tiện ích dùng để so sánh các Entry theo giá trị Value (ở đây là số lần đếm `Long`).
</details>

---

## 10.5. Bài tập Optional

### 📌 Bài 1: Tránh NPE cơ bản
- **Mục tiêu:** Thay thế hoàn toàn thói quen kiểm tra `if (obj != null)` bằng tư duy Functional của Optional: `map()`, `orElse()`, `ifPresentOrElse()`.
- **Yêu cầu kỹ thuật:**
  - Viết hàm `public Optional<User> findUserById(int id)` giả lập tìm user trong DB (id = 1 trả về `User`, id khác trả về rỗng).
  - Trong `main`, gọi hàm và in ra tên user nếu tìm thấy, hoặc `"User not found"` nếu không thấy. **Tuyệt đối không dùng câu lệnh `if-else`**.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Optional;

public class AvoidNpeOptionalDemo {

    static class User {
        private int id;
        private String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() { return name; }
    }

    // Giả lập tìm kiếm dữ liệu từ Database
    public static Optional<User> findUserById(int id) {
        if (id == 1) {
            return Optional.of(new User(1, "Khoi"));
        }
        return Optional.empty(); // Không tìm thấy
    }

    public static void main(String[] args) {
        // Cách 1: Dùng map() kết hợp orElse() (Trả về chuỗi mặc định)
        String userName1 = findUserById(1)
                .map(User::getName)
                .orElse("User not found");
        System.out.println("Kết quả ID = 1: " + userName1);

        String userName2 = findUserById(99)
                .map(User::getName)
                .orElse("User not found");
        System.out.println("Kết quả ID = 99: " + userName2);

        // Cách 2: Dùng ifPresentOrElse() (Có từ Java 9+) - Thực thi Action không trả về
        System.out.print("Kiểm tra ID = 1 qua Action: ");
        findUserById(1).map(User::getName).ifPresentOrElse(
                name -> System.out.println("Xin chào, " + name + "!"),
                () -> System.out.println("User not found")
        );
    }
}
```

**Output mẫu:**
```text
Kết quả ID = 1: Khoi
Kết quả ID = 99: User not found
Kiểm tra ID = 1 qua Action: Xin chào, Khoi!
```

**Giải thích chi tiết:**
- `Optional.of(obj)`: Bọc đối tượng chắc chắn không null (nếu null sẽ quăng NPE ngay lập tức).
- `Optional.empty()`: Đại diện cho giá trị rỗng thay vì dùng từ khóa `null`.
- `map(User::getName)`: Tự động bỏ qua nếu Optional rỗng mà không bị NullPointerException.
</details>

---

### 📌 Bài 2: Optional trong Object lồng nhau
- **Mục tiêu:** Giải quyết bài toán "Kim tự tháp địa ngục" (Null-check Hell) trong các chuỗi đối tượng lồng nhau: `Company` $\rightarrow$ `Department` $\rightarrow$ `Manager` $\rightarrow$ `name`.
- **Yêu cầu kỹ thuật:**
  - Viết hàm lấy tên Manager của một Company truyền vào.
  - Sử dụng Optional `map()` và `orElse()` để xử lý chuỗi truy cập, nếu bất kỳ mắt xích nào là null thì trả về `"No Manager"`.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Optional;

public class NestedObjectOptionalDemo {

    static class Manager {
        private String name;
        public Manager(String name) { this.name = name; }
        public String getName() { return name; }
    }

    static class Department {
        private Manager manager;
        public Department(Manager manager) { this.manager = manager; }
        public Manager getManager() { return manager; }
    }

    static class Company {
        private Department department;
        public Company(Department department) { this.department = department; }
        public Department getDepartment() { return department; }
    }

    // Hàm lấy tên Manager an toàn tuyệt đối với Optional
    public static String getManagerName(Company company) {
        return Optional.ofNullable(company)
                .map(Company::getDepartment)  // Nếu company null -> Optional rỗng
                .map(Department::getManager)  // Nếu department null -> Optional rỗng
                .map(Manager::getName)        // Nếu manager null -> Optional rỗng
                .orElse("No Manager");        // Giá trị dự phòng an toàn
    }

    public static void main(String[] args) {
        // Case 1: Đầy đủ thông tin
        Company c1 = new Company(new Department(new Manager("Nguyễn Tiến Khôi")));
        System.out.println("Company 1 Manager: " + getManagerName(c1));

        // Case 2: Department không có Manager (manager = null)
        Company c2 = new Company(new Department(null));
        System.out.println("Company 2 Manager: " + getManagerName(c2));

        // Case 3: Company không có Department (department = null)
        Company c3 = new Company(null);
        System.out.println("Company 3 Manager: " + getManagerName(c3));

        // Case 4: Bản thân Company là null
        System.out.println("Company 4 Manager: " + getManagerName(null));
    }
}
```

**Output mẫu:**
```text
Company 1 Manager: Nguyễn Tiến Khôi
Company 2 Manager: No Manager
Company 3 Manager: No Manager
Company 4 Manager: No Manager
```

**Giải thích chi tiết:**
- Truyền thống: Bạn phải viết 4 tầng `if (company != null && company.getDepartment() != null && ...)`.
- Với `Optional.ofNullable().map().map()`: Mã nguồn trở thành một dòng biểu thức thanh lịch, dễ bảo trì và hoàn toàn miễn nhiễm với NPE.
</details>

---

### 📌 Bài 3: Xử lý giá trị Default bằng orElseGet
- **Mục tiêu:** Hiểu rõ sự khác biệt chí mạng giữa `orElse()` (Eager Evaluation) và `orElseGet()` (Lazy Evaluation).
- **Yêu cầu kỹ thuật:**
  - Giả lập hàm lấy cấu hình từ Database `getConfigFromDb()` rất chậm và tốn tài nguyên.
  - Viết logic thử lấy cấu hình từ Biến môi trường.
  - Chứng minh `orElseGet()` chỉ gọi hàm Database khi biến môi trường thực sự không tồn tại.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Optional;

public class OrElseVsOrElseGetDemo {

    // Giả lập hàm truy vấn CSDL tốn kém tài nguyên
    public static String getConfigFromDb() {
        System.out.println("   --> [DATABASE QUERY] Đang kết nối CSDL và đọc config (RẤT CHẬM)...");
        return "DATABASE_DEFAULT_CONFIG";
    }

    public static void main(String[] args) {
        String existingEnvConfig = "PRODUCTION_ENV_CONFIG";

        System.out.println("=== THỬ NGHIỆM VỚI orElse() ===");
        // orElse() LUÔN LUÔN thực thi biểu thức bên trong ngoặc bất kể Optional có giá trị hay không!
        String res1 = Optional.ofNullable(existingEnvConfig)
                .orElse(getConfigFromDb());
        System.out.println("Kết quả: " + res1);

        System.out.println("\n=== THỬ NGHIỆM VỚI orElseGet() ===");
        // orElseGet() nhận Supplier và CHỈ GỌI khi Optional thực sự rỗng (Lazy Evaluation)
        String res2 = Optional.ofNullable(existingEnvConfig)
                .orElseGet(() -> getConfigFromDb());
        System.out.println("Kết quả: " + res2);

        System.out.println("\n=== THỬ NGHIỆM KHI BIẾN MÔI TRƯỜNG LÀ NULL ===");
        String nullEnvConfig = null;
        String res3 = Optional.ofNullable(nullEnvConfig)
                .orElseGet(() -> getConfigFromDb());
        System.out.println("Kết quả: " + res3);
    }
}
```

**Output mẫu:**
```text
=== THỬ NGHIỆM VỚI orElse() ===
   --> [DATABASE QUERY] Đang kết nối CSDL và đọc config (RẤT CHẬM)...
Kết quả: PRODUCTION_ENV_CONFIG

=== THỬ NGHIỆM VỚI orElseGet() ===
Kết quả: PRODUCTION_ENV_CONFIG

=== THỬ NGHIỆM KHI BIẾN MÔI TRƯỜNG LÀ NULL ===
   --> [DATABASE QUERY] Đang kết nối CSDL và đọc config (RẤT CHẬM)...
Kết quả: DATABASE_DEFAULT_CONFIG
```

**Giải thích chi tiết:**
- Nhìn vào Output: Khi `existingEnvConfig` đã có dữ liệu, `orElse()` vẫn chạy dòng log `[DATABASE QUERY]`! Đây là bẫy hiệu năng kinh điển khiến ứng dụng chậm chạp mà nhiều lập trình viên không nhận ra.
- **Quy tắc vàng:**
  - Dùng `orElse("default string")` khi giá trị mặc định là một hằng số có sẵn, không tốn chi phí tính toán.
  - Luôn dùng `orElseGet(() -> computeDefault())` khi giá trị mặc định cần gọi hàm, tạo object mới (`new Object()`), hoặc truy vấn I/O.
</details>

---

### 📌 Bài 4: Ném Exception với Optional
- **Mục tiêu:** Sử dụng `orElseThrow()` để biến việc kiểm tra dữ liệu không hợp lệ thành ngoại lệ nghiệp vụ một cách tường minh.
- **Yêu cầu kỹ thuật:**
  - Nhận chuỗi số lượng từ API (có thể là `null`, rỗng, hoặc không phải số).
  - Bọc vào `Optional`, biến đổi thành `Integer`, kiểm tra $\ge 0$.
  - Nếu không hợp lệ, ném ra ngoại lệ `IllegalArgumentException("Invalid quantity")`.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Optional;

public class ThrowExceptionOptionalDemo {

    public static int parseQuantity(String quantityStr) {
        return Optional.ofNullable(quantityStr)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    try {
                        return Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        return null; // Trả về null nếu không phải số để filter lọc bỏ
                    }
                })
                .filter(q -> q != null && q >= 0) // Số lượng phải >= 0
                .orElseThrow(() -> new IllegalArgumentException("Invalid quantity: \"" + quantityStr + "\""));
    }

    public static void main(String[] args) {
        // Test case hợp lệ
        System.out.println("Parsed valid: " + parseQuantity(" 15 "));

        // Test cases không hợp lệ
        testException(null);
        testException("   ");
        testException("abc");
        testException("-5");
    }

    private static void testException(String input) {
        try {
            parseQuantity(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Bắt ngoại lệ thành công: " + e.getMessage());
        }
    }
}
```

**Output mẫu:**
```text
Parsed valid: 15
Bắt ngoại lệ thành công: Invalid quantity: "null"
Bắt ngoại lệ thành công: Invalid quantity: "   "
Bắt ngoại lệ thành công: Invalid quantity: "abc"
Bắt ngoại lệ thành công: Invalid quantity: "-5"
```

**Giải thích chi tiết:**
- `filter(Predicate)`: Nếu điều kiện đúng, giữ nguyên Optional; nếu sai, biến Optional thành `Optional.empty()`.
- `orElseThrow(Supplier<? extends X>)`: Ném ra ngoại lệ do bạn định nghĩa khi Optional rỗng, giúp loại bỏ hoàn toàn các khối `if (data == null) throw new ...` rườm rà.
</details>

---

### 📌 Bài 5: Kết hợp Stream và Optional
- **Mục tiêu:** Nắm vững kỹ thuật giải phóng giá trị từ danh sách `List<Optional<T>>` bằng `flatMap(Optional::stream)` trong Java 9+ và kỹ thuật tương đương trên Java 8.
- **Yêu cầu kỹ thuật:**
  - Cho `List<Optional<String>>` chứa nhiều email (bao gồm cả các phần tử rỗng).
  - Tách lấy toàn bộ các email hợp lệ và nối thành một chuỗi duy nhất phân cách bởi dấu chấm phẩy `; `.

<details>
<summary><b>💡 Xem lời giải mẫu hoàn chỉnh (Click để mở)</b></summary>

```java
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamOptionalIntegrationDemo {
    public static void main(String[] args) {
        List<Optional<String>> emailList = Arrays.asList(
                Optional.of("khoi@tayjava.vn"),
                Optional.empty(),
                Optional.of("admin@google.com"),
                Optional.empty(),
                Optional.of("contact@openai.com")
        );

        // Cách 1: Sử dụng flatMap(Optional::stream) - Chuẩn hiện đại từ Java 9+
        String resultJava9 = emailList.stream()
                .flatMap(Optional::stream) // Tự động bung giá trị nếu có, bỏ qua nếu empty
                .collect(Collectors.joining("; "));

        System.out.println("Kết quả (Java 9+ flatMap Optional::stream):");
        System.out.println(resultJava9);

        // Cách 2: Chuẩn tương thích Java 8
        String resultJava8 = emailList.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining("; "));

        System.out.println("\nKết quả (Java 8 filter + map):");
        System.out.println(resultJava8);
    }
}
```

**Output mẫu:**
```text
Kết quả (Java 9+ flatMap Optional::stream):
khoi@tayjava.vn; admin@google.com; contact@openai.com

Kết quả (Java 8 filter + map):
khoi@tayjava.vn; admin@google.com; contact@openai.com
```

**Giải thích chi tiết:**
- Trong Java 9, class `Optional` được bổ sung phương thức `stream()`:
  - Nếu `Optional` chứa giá trị `x`, `optional.stream()` trả về `Stream.of(x)`.
  - Nếu `Optional` rỗng, `optional.stream()` trả về `Stream.empty()`.
- Khi kết hợp với `flatMap()`, toàn bộ các `Stream` con được nối phẳng lại, tự động loại bỏ các `Optional.empty()` một cách cực kỳ tinh tế và an toàn, không cần gọi `filter(Optional::isPresent)` thủ công.
</details>
