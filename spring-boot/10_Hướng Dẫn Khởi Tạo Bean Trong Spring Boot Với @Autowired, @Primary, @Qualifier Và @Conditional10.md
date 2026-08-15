# Hướng Dẫn Khởi Tạo Bean Trong Spring Boot Với @Autowired, @Primary, @Qualifier Và @Conditional

Trong Spring Boot, **Bean** là các đối tượng được quản lý bởi Spring IoC Container. Việc khởi tạo và quản lý Bean là một trong những khái niệm quan trọng giúp ứng dụng có khả năng mở rộng, tái sử dụng và dễ bảo trì.

Bài viết này sẽ hướng dẫn bạn cách **khởi tạo Bean trong Spring Boot** thông qua:

*   `@Autowired`
    
*   `@Primary`
    
*   `@Qualifier`
    

Các annotation điều kiện (`@ConditionalOnBean`, `@ConditionalOnMissingBean`, `@ConditionalOnClass`, `@ConditionalOnProperty`, …)

### 1\. Khởi Tạo Bean Với `@Autowired`, `@Primary`, `@Qualifier`

**📌** `@Autowired` **và vấn đề gặp phải**

Annotation `@Autowired` được Spring Boot sử dụng để **tự động inject Bean** vào nơi cần thiết. Tuy nhiên, khi tồn tại nhiều Bean cùng implement một interface, Spring Boot sẽ không biết chọn Bean nào để inject, dẫn đến lỗi.

– Ví dụ:

```java
public interface Animal {
    void shouting();
}
```

```java
@Component
public class Dog implements Animal {
    @Override
    public void shouting() {
        System.out.println("Con chó sủa gâu gâu!");
    }
}
```

```java
@Component
public class Cat implements Animal {
    @Override
    public void shouting() {
        System.out.println("Con mèo kêu meo meo!");
    }
}
```

→ Inject Bean `Animal` vào `FoxDevApplication`:

```java
@SpringBootApplication
public class FoxDevApplication implements CommandLineRunner {

    @Autowired
    private Animal animal;

    public static void main(String[] args) {
        SpringApplication.run(FoxDevApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        animal.shouting();
    }
}
```

👉 Kết quả: lỗi **Multiple Beans** vì Spring Boot tìm thấy cả `Dog` và `Cat`.

📌 **Sử dụng** `@Primary`

`@Primary` được dùng khi bạn muốn **chỉ định Bean mặc định** sẽ được inject khi có nhiều Bean cùng loại.

```java
@Component
@Primary
public class Dog implements Animal {
    @Override
    public void shouting() {
        System.out.println("Con chó sủa gâu gâu!");
    }
}
```

👉 Kết quả in ra:

```plaintext
Con chó sủa gâu gâu!
```

**📌 Sử dụng** `@Qualifier`

`@Qualifier` được dùng để **chỉ định chính xác Bean nào cần inject**.

```java
@SpringBootApplication
public class FoxDevApplication implements CommandLineRunner {

    @Autowired
    private Animal animal;

    @Autowired
    @Qualifier("cat")
    private Animal cat;

    public static void main(String[] args) {
        SpringApplication.run(FoxDevApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        animal.shouting();
        cat.shouting();
    }
}
```

👉 Kết quả in ra:

```plaintext
Con chó sủa gâu gâu!
Con mèo kêu meo meo!
```

✅ **Kết luận**:

*   Dùng `@Primary` khi muốn chỉ định **Bean mặc định**.
    
*   Dùng `@Qualifier` khi muốn **inject chính xác Bean** cần thiết.
    

### **2\. Khởi Tạo Bean Theo Các Điều Kiện**

Ngoài `@Component` hay `@Bean`, Spring Boot còn cung cấp nhiều annotation điều kiện (`@Conditional...`) để khởi tạo Bean linh hoạt hơn.

📌 `@ConditionalOnBean`

Khởi tạo Bean khi **một Bean khác đã tồn tại**.

```java
@Configuration
public class AppConfig {
    @Bean
    @ConditionalOnBean(DBConnectionBean.class)
    DBConnectionService dbConnectionService() {
        System.out.println("===> Init DBConnectionService successfully");
        return new DBConnectionService();
    }
}
```

📌 `@ConditionalOnMissingBean`

Khởi tạo Bean khi **một Bean khác chưa tồn tại**.

```java
@Configuration
public class AppConfig {
    @Bean
    @ConditionalOnMissingBean(KafkaConnectionBean.class)
    KafkaConnectionService kafkaConnectionService() {
        System.out.println("===> Init kafkaConnectionService");
        return new KafkaConnectionService();
    }
}
```

📌 `@ConditionalOnClass`

Khởi tạo Bean khi **một class tồn tại trong classpath**.

```java
@Configuration
@ConditionalOnClass(name = "vn.foxdev.model.SomeOne")
public class InitBeanByConditionalOnClass {
}
```

📌 `@ConditionalOnMissingClass`

Khởi tạo Bean khi **một class không tồn tại trong classpath**.

```java
@Configuration
@ConditionalOnMissingClass(value = "vn.foxdev.model.SomeOne")
public class InitBeanByConditionalOnMissingClass {
}
```

📌 `@ConditionalOnProperty`

Khởi tạo Bean khi **có key-value phù hợp trong application.properties**.

```java
@Configuration
public class AppConfig {
    @Bean
    @ConditionalOnProperty(
        value = "vn.foxdev.allowed",
        havingValue = "true",
        matchIfMissing = false
    )
    InitBeanByConditionalOnProperty initBeanByConditionalOnProperty() {
        return new InitBeanByConditionalOnProperty();
    }
}
```

📌 `@ConditionalOnExpression`

Khởi tạo Bean theo **biểu thức điều kiện**.

```java
@Configuration
@ConditionalOnExpression(
    "${vn.foxdev.allowed:true} and ${vn.foxdev.enabled:true}"
)
public class InitBeanByConditionalOnExpression {
}
```

📌 `@ConditionalOnResource`

Khởi tạo Bean khi **file resource tồn tại**.

```java
@Configuration
@ConditionalOnResource(resources = "/application.properties")
public class InitBeanByConditionalOnResource {
}
```

**📌** `@ConditionalOnJava`

Khởi tạo Bean theo **version Java**.

```java
@Configuration
@ConditionalOnJava(JavaVersion.SEVENTEEN)
public class InitBeanByConditionalOnJava {
}
```

#### ✅ Kết Luận

*   `@Autowired` giúp tự động inject Bean nhưng có thể gây lỗi khi tồn tại nhiều Bean.
    
*   `@Primary` chỉ định Bean mặc định.
    
*   `@Qualifier` chọn chính xác Bean cần dùng.
    

👉 Nhóm annotation `@Conditional...` giúp **khởi tạo Bean theo điều kiện**, làm ứng dụng Spring Boot trở nên **linh hoạt, dễ mở rộng và tùy biến**.

