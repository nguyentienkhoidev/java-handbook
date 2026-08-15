# Validate Enum Trong Spring Boot

Làm thế nào để **validate Enum trong Spring Boot** và trả về **message mong muốn**?

Các annotation validation mặc định (`@NotNull`, `@NotBlank`, `@Pattern`...) không hỗ trợ trực tiếp cho Enum. Trong bài viết này, Fox Dev sẽ hướng dẫn bạn nhiều cách khác nhau để **validate Enum trong Spring Boot** bằng **annotation tùy chỉnh**.

### **1\. Validate Enum Với** `@Pattern`

Mặc định, bạn **không thể** dùng `@Pattern` trực tiếp cho Enum object. Nếu thử:

```java
@Pattern(regexp = "^ACTIVE|INACTIVE|NONE$", message = "status must be one in {ACTIVE, INACTIVE, NONE}")
private UserStatus status;
```

→ Sẽ báo lỗi:

```plaintext
jakarta.validation.UnexpectedTypeException: HV000030: No validator could be found... 
```

👉 Nếu muốn dùng `@Pattern`, bạn buộc phải khai báo Enum dưới dạng **String**:

```java
@Pattern(regexp = "^ACTIVE|INACTIVE|NONE$", message = "status must be one in {ACTIVE, INACTIVE, NONE}")
private String status;
```

### **2\. Validate Enum Bằng Regex**

1\. Tạo Enum `UserStatus`

```java
public enum UserStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE,
    @JsonProperty("none")
    NONE
}
```

2\. Tạo Annotation `@EnumPattern`

```java
@Documented
@Retention(RUNTIME)
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = EnumPatternValidator.class)
public @interface EnumPattern {
    String name();
    String regexp();
    String message() default "{name} must match {regexp}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

3\. Tạo Validator `EnumPatternValidator`

```java
public class EnumPatternValidator implements ConstraintValidator<EnumPattern, Enum<?>> {
    private Pattern pattern;

    @Override
    public void initialize(EnumPattern enumPattern) {
        pattern = Pattern.compile(enumPattern.regexp());
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return pattern.matcher(value.name()).matches();
    }
}
```

4\. Áp Dụng `@EnumPattern`

```java
@EnumPattern(name = "status", regexp = "ACTIVE|INACTIVE|NONE")
private UserStatus status;
```

👉 Ưu điểm: Có thể tái sử dụng cho nhiều enum khác nhau:

```java
@EnumPattern(name = "gender", regexp = "MALE|FEMALE|OTHER")
private Gender gender;
```

### **3\. Validate Enum Với Một Số Giá Trị (Subset Validation)**

Đôi khi bạn chỉ muốn Enum nhận **một số giá trị nhất định**, không phải tất cả.

1\. Tạo Enum `Gender`

```java
public enum Gender {
    @JsonProperty("male")
    MALE,
    @JsonProperty("female")
    FEMALE,
    @JsonProperty("other")
    OTHER;
}
```

2\. Tạo Annotation `@GenderSubset`

```java
@Documented
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = GenderSubSetValidator.class)
public @interface GenderSubset {
    Gender[] anyOf();
    String message() default "must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

3\. Tạo Validator `GenderSubSetValidator`

```java
public class GenderSubSetValidator implements ConstraintValidator<GenderSubset, Gender> {
    private Gender[] genders;

    @Override
    public void initialize(GenderSubset constraint) {
        this.genders = constraint.anyOf();
    }

    @Override
    public boolean isValid(Gender value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(genders).contains(value);
    }
}
```

4\. Áp Dụng `@GenderSubset`

```java
@GenderSubset(anyOf = {MALE, FEMALE, OTHER})
private Gender gender;
```

👉 Ưu điểm: Có thể giới hạn **một subset giá trị** (ví dụ chỉ `MALE`, `FEMALE`).

### **4\. Validate String Với Enum Class (**`@EnumValue`**)**

Cách này dùng khi bạn nhận dữ liệu dạng **String**, và muốn kiểm tra giá trị có tồn tại trong Enum không.

1\. Tạo Annotation `@EnumValue`

```java
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {
    String name();
    String message() default "{name} must be any of enum {enumClass}";
    Class<? extends Enum<?>> enumClass();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

2\. Tạo Validator `EnumValueValidator`

```java
public class EnumValueValidator implements ConstraintValidator<EnumValue, CharSequence> {
    private List<String> acceptedValues;

    @Override
    public void initialize(EnumValue enumValue) {
        acceptedValues = Stream.of(enumValue.enumClass().getEnumConstants())
                               .map(Enum::name)
                               .toList();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return value == null || acceptedValues.contains(value.toString().toUpperCase());
    }
}
```

3\. Áp Dụng `@EnumValue`

```java
@NotNull(message = "type must be not null")
@EnumValue(name = "type", enumClass = UserType.class)
private String type;
```

👉 Ưu điểm: Dùng chung cho tất cả enum và dễ xử lý exception.

### 6\. Test Validate Enum Với Postman

– Ví dụ request test:

```plaintext
curl --location 'http://localhost:8080/user/' \
--header 'Content-Type: application/json' \
--data-raw '{
  "firstName": "Tay",
  "lastName": "Java",
  "email": "someone@email.com",
  "phone": "0123456789",
  "dateOfBirth": "06/05/2003",
  "gender": "other",
  "username": "foxdev",
  "password": "password",
  "type": "user",
  "status": "active"
}'
```

### 7\. Kết Luận

*   `@Pattern` chỉ dùng được khi Enum khai báo dưới dạng String.
    
*   Có thể tạo **custom validator** như `@EnumPattern`, `@GenderSubset`, `@EnumValue` để linh hoạt validate Enum.
    
*   Nên chọn cách phù hợp tùy theo yêu cầu dự án: validate tất cả enum, một subset, hoặc validate string theo enum.
    

👉 Source code đầy đủ: [GitHub FoxDev Sample Code](https://github.dev/luongquoctay87/tayjava-sample-code/tree/bai-4-validate-enum-in-spring-boot)

