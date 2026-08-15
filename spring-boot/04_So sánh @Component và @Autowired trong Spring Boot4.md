# So sánh @Component và @Autowired trong Spring Boot

Trong Spring Framework nói chung và Spring Boot nói riêng, hai annotation quan trọng mà lập trình viên thường gặp là `@Component` và `@Autowired`. Tuy nhiên, nhiều bạn mới học thường nhầm lẫn vai trò của chúng. Bài viết này sẽ phân tích, so sánh và đưa ví dụ thực tế để bạn hiểu rõ cách sử dụng.

### **1\.** `@Component` **là gì?**

*   **Mục đích**: Đánh dấu một class là **Spring Bean**, để Spring quản lý vòng đời của nó trong **ApplicationContext**.
    
*   **Cách hoạt động**: Khi Spring Boot khởi động, nó quét các package được chỉ định (component scan). Class nào có `@Component` sẽ được Spring tạo ra instance và đưa vào container.
    

– Ví dụ cơ bản:

```java
@Component 
public class SampleService {
    public void doSomething() {
        System.out.println("Doing something...");
    }
}
```

→ Ở đây `SampleService` đã được Spring quản lý như một bean.

### **2\.** `@Autowired` **là gì?**

*   **Mục đích**: Tự động **inject (tiêm)** một bean đã có trong ApplicationContext vào nơi cần sử dụng.
    
*   **Cách hoạt động**: Spring tìm bean có type phù hợp và gán nó vào **field**, **constructor** hoặc **setter**.
    

– Ví dụ cơ bản:

```java
@Component 
public class SampleController {

    private final SampleController sampleService;

    @Autowired  // Constructor Injection (khuyến khích)
    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    public void run() {
        sampleService.doSomething();
    }
}
```

### **3\. Ví dụ thực tế với AWS S3**

Để hiểu rõ hơn, ta xây dựng một ứng dụng Spring Boot kết nối với **AWS S3** và áp dụng cả `@Component` và `@Autowired`.

#### 3.1. Cấu hình AWS bằng `@Configuration` và `@Bean`

```java
@Configuration public class AmazonS3Config {

    @Value("${amazon.s3.region}")
    String region;
    @Value("${amazon.s3.accessKey}")
    String accessKey;
    @Value("${amazon.s3.secretKey}")
    String secretKey;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .withRegion(Regions.fromName(region))
                .build();
    }

    public AWSCredentials credentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }
}
```

👉 Ở đây, `amazonS3()` được đăng ký thành bean trong Spring Container.

#### 3.2. Định nghĩa `S3Client` với `@Component` và inject `AmazonS3`

```java
@Component @Slf4j(topic = "S3-SERVICE") public class S3Client {

    @Autowired
    private AmazonS3 amazonS3Client;

    public void createS3Bucket(String bucketName, boolean publicBucket) {
        if (amazonS3Client.doesBucketExistV2(bucketName)) {
            log.info("Bucket name already in use. Try another name.");
            return;
        }
        if (publicBucket) {
            amazonS3Client.createBucket(bucketName);
        } else {
            amazonS3Client.createBucket(
                new CreateBucketRequest(bucketName)
                .withCannedAcl(CannedAccessControlList.Private)
            );
        }
    }

    public List<Bucket> listBuckets() {
        return amazonS3Client.listBuckets();
    }
}
```

👉 Ở đây, `S3Client` được đánh dấu `@Component`, Spring sẽ quản lý nó.  
👉 `@Autowired` tự động inject `amazonS3` bean vào trong `S3Client`.

#### 3.3. RestController sử dụng `S3Client`

```java
@RestController @RequestMapping("/s3") 
public class S3Controller {

    @Autowired
    private S3Client s3Client;

    @PostMapping("/{bucketName}")
    public void createBucket(
            @PathVariable String bucketName,
            @RequestParam boolean publicBucket) {
        s3Client.createS3Bucket(bucketName, publicBucket);
    }

    @GetMapping("/buckets")
    public List<String> listBuckets() {
        return s3Client.listBuckets().stream()
                .map(Bucket::getName)
                .toList();
    }
}
```

👉 `S3Controller` dùng `@Autowired` để tiêm `S3Client` vào. Khi gọi API `/s3/buckets`, danh sách bucket từ AWS sẽ được trả về.

### **4\. So sánh** `@Component` **và** `@Autowired`

Đặc điểm`@Component@Autowired`**Chức năng**Đăng ký class thành beanInject bean đã có vào class khác**Vị trí áp dụng**Trên **class**Trên **field**, **constructor**, **setterÝ nghĩa**“Tạo bean cho Spring quản lý”“Dùng bean này trong class hiện tại”**Ví dụ**`@Component public class S3Client {}@Autowired private S3Client s3Client;`

### **5\. Kết luận & Lưu ý**

*   `@Component`: để Spring biết class nào cần quản lý.
    
*   `@Autowired`: để Spring tự động tiêm bean vào nơi cần dùng (Dependency Injection).
    

👉 **Dependency Injection (DI)** là nguyên tắc giảm phụ thuộc giữa các thành phần trong ứng dụng, giúp code dễ bảo trì và test hơn.

⚠️ **Lưu ý quan trọng**:

Việc lạm dụng `@Autowired` trên field có thể gây khó debug vì Spring tự động liên kết mơ hồ, dễ sinh lỗi nếu có nhiều bean cùng type.

Trong thực tế, developer thường **ưu tiên Constructor Injection** thay vì field injection để dễ test và rõ ràng hơn.

Tìm hiểu thêm: _Limitations and Disadvantages of Autowiring_.

📌 Như vậy, `@Component` và `@Autowired` tuy khác nhau về vai trò, nhưng thường được dùng chung:

*   `@Component` để Spring quản lý bean.
    
*   `@Autowired` để đưa bean đó vào nơi cần thiết.
    

