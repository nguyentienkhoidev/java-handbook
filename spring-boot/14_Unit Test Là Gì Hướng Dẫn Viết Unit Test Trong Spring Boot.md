# Unit Test Là Gì? Hướng Dẫn Viết Unit Test Trong Spring Boot

### 1\. Unit Test là gì?

**– Khái niệm Unit Test**

Unit test là một **phương pháp kiểm thử phần mềm** nhằm xác minh tính đúng đắn của **đơn vị nhỏ nhất trong mã nguồn** – thường là hàm hoặc phương thức.  
Trong unit test, mỗi đơn vị được kiểm tra độc lập với các phần khác để đảm bảo hoạt động đúng như yêu cầu.

**– Đặc điểm của Unit Test**

*   **Độc lập**: Mỗi test chỉ kiểm tra một thành phần, không phụ thuộc vào phần khác.
    
*   **Tự động hóa**: Unit test được viết dưới dạng mã và chạy tự động.
    
*   **Nhanh**: Vì chỉ kiểm tra từng đơn vị nhỏ, nên test chạy rất nhanh.
    

**– Lợi ích của Unit Test**

*   **Phát hiện lỗi sớm**: Giúp phát hiện bug ngay từ giai đoạn phát triển.
    
*   **Dễ bảo trì**: Khi thay đổi code, unit test đảm bảo không phá vỡ chức năng cũ.
    
*   **Cải thiện thiết kế**: Thúc đẩy lập trình viên viết code rõ ràng, dễ kiểm tra.
    
*   **Tài liệu hóa**: Test case đóng vai trò như tài liệu mô tả cách sử dụng hàm/lớp.
    

### 2\. Các bước viết Unit Test với Spring Boot

**2.1 Chuẩn bị môi trường**

Trong Spring Boot, việc viết unit test rất thuận tiện vì đã có **Spring Boot Starter Test** tích hợp sẵn:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

– Spring Boot Starter Test bao gồm:

*   **JUnit 5** – framework unit test phổ biến nhất.
    
*   **Mockito** – dùng để mock dependencies.
    
*   **AssertJ** – thư viện assertion mạnh mẽ.
    
*   **Hamcrest** – hỗ trợ matcher khi so khớp điều kiện.
    

– Một số annotation quan trọng trong Unit Test

*   `@Test`: Đánh dấu một phương thức là test case.
    
*   `@BeforeEach`: Chạy trước mỗi test (khởi tạo dữ liệu).
    
*   `@AfterEach`: Chạy sau mỗi test (giải phóng tài nguyên).
    
*   `@SpringBootTest`: Khởi chạy Spring context (cho test tích hợp).
    
*   `@Mock`: Tạo mock object.
    
*   `@InjectMocks`: Tự động inject mock vào đối tượng cần test.
    
*   `@DisplayName`: Đặt tên dễ đọc cho test case.
    

**2.2 Xác định đối tượng cần test**

Ví dụ: kiểm tra `UserService` trong ứng dụng Spring Boot.

```java
@Service
public class UserService {
    public UserResponse findById(Long id) {
        log.info("Find user by id: {}", id);
        UserEntity userEntity = getUserEntity(id);
        return UserResponse.builder()
                .id(id)
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .gender(userEntity.getGender())
                .birthday(userEntity.getBirthday())
                .username(userEntity.getUsername())
                .phone(userEntity.getPhone())
                .email(userEntity.getEmail())
                .build();
    }
}
```

**2.3 Viết Unit Test cho UserService**

Tạo file `UserServiceTest` trong thư mục `src/test/java`

```java
@ExtendWith(MockitoExtension.class) // Sử dụng Mockito
class UserServiceTest {

    private UserService userService;

    private @Mock UserRepository userRepository;

    private static UserEntity FoxDev;

    @BeforeAll
    static void beforeAll() {
        // Dữ liệu giả lập
        FoxDev = new UserEntity();
        FoxDev.setId(1L);
        FoxDev.setFirstName("Tay");
        FoxDev.setLastName("Java");
        FoxDev.setUsername("foxdev");
    }

    @BeforeEach
    void beforeEach() {
        // Khởi tạo UserService với repository mock
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(FoxDev));

        UserResponse result = userService.findById(1L);

        Assertions.assertNotNull(result);
        assertEquals("foxdev", result.getUsername());
    }

    @Test
    void testGetUserById_Failure() {
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, 
            () -> userService.findById(10L));

        assertEquals("User not found", thrown.getMessage());
    }
}
```

**2.4 Chạy Unit Test**

– Chạy lệnh Maven:

```java
mvn test
```

– Kết quả:

```plaintext
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### 3\. Kết luận

*   **Unit test là gì?** → Là công cụ mạnh mẽ để kiểm tra từng đơn vị code.
    
*   **Unit test trong Spring Boot** → Được hỗ trợ sẵn qua Spring Boot Starter Test.
    
*   **Lợi ích** → Giúp phát hiện lỗi sớm, cải thiện thiết kế, dễ bảo trì, nâng cao chất lượng code.
    

👉 Với sinh viên, việc **học cách viết Unit Test ngay từ đầu** sẽ rèn luyện thói quen code tốt, chuyên nghiệp, và sẵn sàng cho môi trường làm việc thực tế.

