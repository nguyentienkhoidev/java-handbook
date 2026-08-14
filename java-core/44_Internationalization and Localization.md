# Internationalization and Localization

*   **Internationalization (i18n)**: Quốc tế hóa – quá trình thiết kế ứng dụng để có thể hỗ trợ nhiều ngôn ngữ/khu vực.
*   **Localization (l10n)**: Bản địa hóa – quá trình dịch thuật và tùy chỉnh ứng dụng theo ngôn ngữ và văn hóa cụ thể.

### **1\. Internationalization (i18n) – Quốc tế hóa**

Trước khi áp dụng I18N, cần hiểu rằng nhiều dữ liệu phụ thuộc vào khu vực địa lý và văn hóa, ví dụ:

*   Messages (Thông điệp)
*   Dates (Ngày tháng)
*   Times (Thời gian)
*   Numbers (Số)
*   Currencies (Tiền tệ)
*   Measurements (Đo lường)
*   Phone Numbers (Số điện thoại)
*   Postal Addresses (Địa chỉ bưu điện)
*   Labels trên GUI components

#### **🔹 a. Lớp** `**Locale**`

`Locale` đại diện cho một khu vực hoặc văn hóa. Nó cung cấp thông tin như: ngôn ngữ, quốc gia, biến thể...

*   **Một số hằng số trong** `**Locale**`

```java
public static final Locale ENGLISH;
public static final Locale FRENCH;
public static final Locale GERMAN;
public static final Locale ITALIAN;
public static final Locale JAPANESE;
public static final Locale KOREAN;
public static final Locale CHINESE;
public static final Locale SIMPLIFIED_CHINESE;
public static final Locale TRADITIONAL_CHINESE;

public static final Locale FRANCE;
public static final Locale GERMANY;
public static final Locale ITALY;
public static final Locale JAPAN;
public static final Locale KOREA;
public static final Locale CHINA;
public static final Locale TAIWAN;
public static final Locale UK;
public static final Locale US;
public static final Locale CANADA;
public static final Locale CANADA_FRENCH;

public static final Locale ROOT;
```

*   **Các constructor của** `**Locale**`

```java
Locale(String language);
Locale(String language, String country);
Locale(String language, String country, String variant);
```

*   **Các phương thức quan trọng của** `**Locale**`

Method

Mô tả

`static Locale getDefault()`

Trả về `Locale` mặc định của hệ thống

`static Locale[] getAvailableLocales()`

Trả về danh sách các locale có sẵn

`String getDisplayCountry()`

Tên quốc gia theo locale hiện tại

`String getDisplayLanguage()`

Tên ngôn ngữ theo locale hiện tại

`String getDisplayVariant()`

Mã biến thể theo locale hiện tại

`String getISO3Country()`

Mã quốc gia 3 ký tự

`String getISO3Language()`

Mã ngôn ngữ 3 ký tự

– Ví dụ: Lấy thông tin Locale mặc định

```java
import java.util.Locale;

public class App {
    public static void main(String[] args) {
        Locale locale = Locale.getDefault();

        System.out.println("getCountry(): " + locale.getCountry());
        System.out.println("getDisplayCountry(): " + locale.getDisplayCountry());
        System.out.println("getLanguage(): " + locale.getLanguage());
        System.out.println("getDisplayLanguage(): " + locale.getDisplayLanguage());
        System.out.println("getDisplayName(): " + locale.getDisplayName());
        System.out.println("getISO3Country(): " + locale.getISO3Country());
        System.out.println("getISO3Language(): " + locale.getISO3Language());
    }
}
```

– Ví dụ: Chuyển từ tiếng Việt sang ngôn ngữ khác

```java
import java.util.Locale;

public class App {
    public static void main(String[] args) {
        Locale vnLocale = new Locale("vi", "VN");
        Locale chinaLocale = Locale.CHINA;
        Locale japanLocale = Locale.JAPAN;

        System.out.println("VN: " + vnLocale.getDisplayLanguage(vnLocale));
        System.out.println("CHINA: " + chinaLocale.getDisplayLanguage(vnLocale));  // Tiếng Trung
        System.out.println("JAPAN: " + japanLocale.getDisplayLanguage(vnLocale));  // Tiếng Nhật
    }
}
```

– Ví dụ: Hiển thị ngôn ngữ từ nhiều Locale

```java
import java.util.Locale;

public class App {
    public static void main(String[] args) {
        Locale[] locales = {
            new Locale("en", "GB"),
            new Locale("fr", "FR"),
            new Locale("es", "ES")
        };

        for (Locale loc : locales) {
            String displayLanguage = loc.getDisplayLanguage(loc);
            System.out.println(loc + ": " + displayLanguage);
        }
    }
}
```

– Kết quả:

```java
en_GB: English
fr_FR: français
es_ES: español
```

#### **🔹 b. ResourceBundle**

`ResourceBundle` được dùng để quốc tế hóa message (text hiển thị).

Method

Mô tả

`static ResourceBundle getBundle(String basename)`

Lấy bundle theo locale mặc định

`static ResourceBundle getBundle(String basename, Locale locale)`

Lấy bundle theo locale chỉ định

`String getString(String key)`

Lấy giá trị theo key trong bundle

– Ví dụ: Giả sử chúng ta có một ứng dụng cần hiển thị lời chào bằng nhiều ngôn ngữ.

**→ Bước 1:** Tạo các file `properties`

 - `messages_en.properties`

```java
greeting=Hello
farewell=Goodbye
inquiry=How are you?
```

\- `messages_vi.properties`

```java
greeting=Xin chào
farewell=Tạm biệt
inquiry=Bạn khỏe không?
```

\- `messages_fr.properties`

```java
greeting=Bonjour
farewell=Au revoir
inquiry=Comment ça va ?
```

**→ Bước 2:** Sử dụng `ResourceBundle` trong Java

```plaintext
import java.util.Locale;
import java.util.ResourceBundle;

public class App {
    public static void main(String[] args) {
        // Locale mặc định của hệ thống
        ResourceBundle bundleDefault = ResourceBundle.getBundle("messages");
        System.out.println("Default greeting: " + bundleDefault.getString("greeting"));

        // Locale tiếng Việt
        Locale vnLocale = new Locale("vi", "VN");
        ResourceBundle bundleVi = ResourceBundle.getBundle("messages", vnLocale);
        System.out.println("Tiếng Việt: " + bundleVi.getString("greeting"));
        System.out.println("Tiếng Việt (farewell): " + bundleVi.getString("farewell"));

        // Locale tiếng Pháp
        Locale frLocale = new Locale("fr", "FR");
        ResourceBundle bundleFr = ResourceBundle.getBundle("messages", frLocale);
        System.out.println("Français: " + bundleFr.getString("greeting"));
        System.out.println("Français (farewell): " + bundleFr.getString("farewell"));
    }
}
```

– Kết quả:

```plaintext
Default greeting: Hello
Tiếng Việt: Xin chào
Tiếng Việt (farewell): Tạm biệt
Français: Bonjour
Français (farewell): Au revoir
```

📌 **Giải thích**:

`ResourceBundle.getBundle("messages")` → Java sẽ tự động tìm file `messages_{ngôn ngữ}_{quốc gia}.properties` phù hợp với `Locale`.

Nếu không tìm thấy, nó sẽ fallback về `messages.properties` (nếu có).

Điều này giúp ứng dụng hiển thị đúng ngôn ngữ theo khu vực mà không cần viết code if-else phức tạp.

### **2\. Internationalizing Currency (Tiền tệ)**

– Ví dụ: Hiển thị tiền tệ theo từng khu vực.

```java
import java.text.NumberFormat;
import java.util.Locale;

public class App {
    public static void main(String[] args) {
        NumberFormat vnFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        NumberFormat jpyFormatter = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        NumberFormat usdFormatter = NumberFormat.getCurrencyInstance(Locale.US);

        String vnd = vnFormatter.format(10500.3245);
        String jpy = jpyFormatter.format(10500.3245);
        String usd = usdFormatter.format(10500.3245);

        System.out.println("VNĐ: " + vnd);
        System.out.println("JPY: " + jpy);
        System.out.println("USD: " + usd);
    }
}
```

– Kết quả:

```java
VNĐ: ₫10,500
JPY: ￥10,500
USD: $10,500.32
```

### **3\. Internationalizing Date (Ngày tháng)**

– Ví dụ: Lấy ngày tháng năm theo local mặc định.

```java
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
        Date currentDate = new Date();
        String date = formatter.format(currentDate);

        System.out.println(date);
    }
}
```

– Kết quả:

```java
Sep 10, 2025
```

### **4\. Internationalizing Time (Thời gian)**

– Ví dụ: Lấy thời gian theo local mặc định.

```java
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        DateFormat formatter = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault());
        Date currentDate = new Date();
        String time = formatter.format(currentDate);

        System.out.println(time);
    }
}
```

– Kết quả:

```java
6:44:57 AM
```
