# Default Methods

### **1\. Default Methods là gì ?**

*   `default method` (hay còn gọi là **defender method** hoặc **virtual extension method**) là một phương thức trong **interface** có phần **cài đặt sẵn (implementation)** bằng từ khóa `default`.
*   Trước Java 8, **interface chỉ chứa abstract methods** (không có thân hàm). Từ Java 8 trở đi, interface có thể chứa:
*   `abstract methods` (bắt buộc lớp implement phải override),
*   `default methods` (có thân hàm, lớp implement có thể dùng luôn hoặc override nếu muốn),
*   `static methods` (gọi trực tiếp từ interface).

– Cú pháp:

```java
public interface SampleInterface {
    // abstract method
    void abstractMethod();

    // default method
    default void defaultMethod() {
        System.out.println("This is a default method in interface");
    }
}
```

📌 Ví dụ:

```java
interface Vehicle {
    void start(); // abstract method

    // default method
    default void stop() {
        System.out.println("Vehicle is stopping...");
    }
}
```

```java
class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("Car is starting...");
    }
}
```

```java
public class App {
    public static void main(String[] args) {
        Car car = new Car();
        car.start(); // Car is starting...
        car.stop();  // Vehicle is stopping...
    }
}
```

### **2\. Khi nào dùng** `**default methods**`**?**

*   Khi muốn **mở rộng** một interface mà **không làm hỏng code cũ** đã implement interface đó.
*   Dùng để cung cấp **hành vi mặc định** nhưng vẫn cho phép override.

