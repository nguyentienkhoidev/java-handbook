# Stream API

`Stream API` là một tính năng quan trọng được giới thiệu trong **Java 8** (nằm trong gói `java.util.stream`).  
Nó giúp xử lý tập hợp dữ liệu (**Collection, Array**) theo **phong cách lập trình hàm (functional programming)**: ngắn gọn, rõ ràng và hiệu quả hơn.

👉 Thay vì phải viết vòng lặp lồng nhau và nhiều dòng code phức tạp, ta có thể dùng **Stream API** để biểu diễn các thao tác trên dữ liệu một cách **tự nhiên và mạch lạc**.

### **1\. Stream là gì?**

*   `Stream` đại diện cho một **chuỗi các phần tử dữ liệu** có thể được xử lý.
*   `Stream` khác với `Collection`:
    *   `Collection` **lưu trữ dữ liệu**.
    *   `Stream` **không lưu trữ dữ liệu**, mà chỉ **xử lý luồng dữ liệu**.

– Ví dụ:

```java
List<String> names = Arrays.asList("Java", "Spring", "Hibernate");

// Tạo stream và xử lý dữ liệu
names.stream()
     .filter(s -> s.startsWith("J"))
     .forEach(System.out::println); // Output: Java
```

### **2\. Tính chất của Stream API**

*   **Không thay đổi dữ liệu gốc** → Các thao tác trên `Stream` không làm thay đổi Collection ban đầu.
*   **Lazy Execution** → Các thao tác trung gian (intermediate) **chỉ chạy khi có terminal operation**.
*   **Không lưu trữ dữ liệu** → Chỉ xử lý khi cần.
*   **Hỗ trợ xử lý song song** → Dễ dàng tận dụng CPU đa luồng với `parallelStream()`.

### **3\. Các loại thao tác trong Stream API**

**🔹 Intermediate Operations (Thao tác trung gian)**

Trả về một `Stream` mới, có thể xâu chuỗi nhiều thao tác. Dưới đây là một số phương thức thao tác trung gian phổ biến:

*   `filter()` – lọc phần tử
*   `map()` – chuyển đổi dữ liệu
*   `sorted()` – sắp xếp
*   `distinct()` – loại bỏ trùng lặp
*   `limit()` – giới hạn số phần tử

– Ví dụ:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
numbers.stream()
       .filter(n -> n % 2 == 0)  // lọc số chẵn
       .map(n -> n * n)          // bình phương
       .forEach(System.out::println); // Output: 4, 16, 36
```

**🔹 Terminal Operations (Thao tác kết thúc)**

Kết thúc luồng xử lý, trả về kết quả (giá trị hoặc collection). Dưới đây là một số phương thức thao tác kết thúc phổ biến:

*   `forEach()` – duyệt và in
*   `collect()` – gom kết quả thành Collection
*   `reduce()` – gộp các phần tử thành một giá trị
*   `count()` – đếm phần tử
*   `findFirst()` – lấy phần tử đầu tiên

```java
long count = numbers.stream()
                    .filter(n -> n > 3)
                    .count();
System.out.println(count); // Output: 3
```

### **4\. Quy trình sử dụng Stream API**

Một chuỗi thao tác với Stream gồm 3 bước chính:

**1\. Tạo Stream**

*   Từ Collection: `list.stream()` hoặc `list.parallelStream()`
*   Từ mảng: `Arrays.stream(array)`
*   Từ giá trị: `Stream.of(…)`

**2\. Intermediate Operations** (xử lý trung gian)

*   Lọc: `filter()`
*   Biến đổi: `map()`
*   sắp xếp: `sorter()`
*   Loại bỏ trùng lặp: `distinct()`
*   …

**3\. Terminal Operation** (thao tác kết thúc)

Thu kết quả cuối cùng như: `forEach()`, `collect()`, `reduce()`,…

– Ví dụ:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
numbers.stream()
       .filter(n -> n % 2 == 0)  // lọc số chẵn
       .map(n -> n * n)          // bình phương
       .forEach(System.out::println); // Output: 4, 16, 36
```

### **5\. Parallel Stream**

Nếu muốn tận dụng **đa luồng CPU** để tăng tốc, ta có thể dùng `parallelStream()` thay vì `stream()`.

```java
List<Integer> bigList = IntStream.rangeClosed(1, 1_000_000)
                                 .boxed()
                                 .toList();

long count = bigList.parallelStream()
                    .filter(n -> n % 2 == 0)
                    .count();

System.out.println(count); // Đếm số chẵn nhanh hơn nhờ đa luồng
```

### **6\. Ưu điểm của Stream API**

✅ **Code ngắn gọn, dễ hiểu** → thay thế nhiều vòng lặp phức tạp.

✅ **Dễ mở rộng** → dễ dàng thêm thao tác mới mà không cần sửa nhiều code.

✅ **Hiệu suất cao** → hỗ trợ xử lý song song (`parallelStream`) để tăng tốc với dữ liệu lớn.

### **7\. Danh sách các phương thức của Stream API**

#

Method

Mô tả

1

`filter(Predicate<? super T> predicate)`

Lọc các phần tử trong stream thỏa mãn điều kiện.

2

`map(Function<? super T,? extends R> mapper)`

Biến đổi mỗi phần tử trong stream thành một phần tử khác.

3

`mapToInt(ToIntFunction<? super T> mapper)`

Biến đổi phần tử thành `IntStream`.

4

`mapToLong(ToLongFunction<? super T> mapper)`

Biến đổi phần tử thành `LongStream`.

5

`mapToDouble(ToDoubleFunction<? super T> mapper)`

Biến đổi phần tử thành `DoubleStream`.

6

`flatMap(Function<? super T,? extends Stream<? extends R>> mapper)`

Chuyển đổi và làm phẳng stream con thành một stream.

7

`flatMapToInt(Function<? super T,? extends IntStream> mapper)`

Làm phẳng thành `IntStream`.

8

`flatMapToLong(Function<? super T,? extends LongStream> mapper)`

Làm phẳng thành `LongStream`.

9

`flatMapToDouble(Function<? super T,? extends DoubleStream> mapper)`

Làm phẳng thành `DoubleStream`.

10

`distinct()`

Loại bỏ các phần tử trùng lặp (dựa trên equals).

11

`sorted()`

Sắp xếp các phần tử trong stream (tự nhiên).

12

`sorted(Comparator<? super T> comparator)`

Sắp xếp theo Comparator tùy chỉnh.

13

`peek(Consumer<? super T> action)`

Thực hiện hành động với từng phần tử (dùng debug).

14

`limit(long maxSize)`

Giới hạn số phần tử trong stream.

15

`skip(long n)`

Bỏ qua n phần tử đầu tiên.

16

`forEach(Consumer<? super T> action)`

Thực hiện hành động với từng phần tử (không đảm bảo thứ tự).

17

`forEachOrdered(Consumer<? super T> action)`

Thực hiện hành động với từng phần tử (theo thứ tự).

18

`toArray()`

Trả về mảng Object\[\].

19

`toArray(IntFunction<A[]> generator)`

Trả về mảng kiểu cụ thể.

20

`reduce(T identity, BinaryOperator<T> accumulator)`

Giảm dần với giá trị khởi tạo và phép gộp.

21

`reduce(BinaryOperator<T> accumulator)`

Giảm dần không có giá trị khởi tạo.

22

`reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner)`

Giảm dần với 3 tham số (song song).

23

`collect(Collector<? super T,A,R> collector)`

Thu thập kết quả thành collection hoặc giá trị khác.

24

`collect(Supplier<R> supplier, BiConsumer<R,? super T> accumulator, BiConsumer<R,R> combiner)`

Thu thập bằng cách tự định nghĩa.

25

`min(Comparator<? super T> comparator)`

Tìm phần tử nhỏ nhất.

26

`max(Comparator<? super T> comparator)`

Tìm phần tử lớn nhất.

27

`count()`

Đếm số phần tử trong stream.

28

`anyMatch(Predicate<? super T> predicate)`

Kiểm tra có phần tử nào thỏa điều kiện không.

29

`allMatch(Predicate<? super T> predicate)`

Kiểm tra tất cả phần tử có thỏa điều kiện không.

30

`noneMatch(Predicate<? super T> predicate)`

Kiểm tra không có phần tử nào thỏa điều kiện.

31

`findFirst()`

Trả về phần tử đầu tiên (Optional).

32

`findAny()`

Trả về một phần tử bất kỳ (Optional).

33

`iterator()`

Trả về Iterator để duyệt stream.

34

`spliterator()`

Trả về Spliterator (hỗ trợ duyệt song song).

35

`isParallel()`

Kiểm tra stream có đang ở chế độ song song không.

36

`sequential()`

Trả về stream tuần tự.

37

`parallel()`

Trả về stream song song.

38

`unordered()`

Trả về stream không quan tâm đến thứ tự.

39

`onClose(Runnable closeHandler)`

Gắn handler khi stream đóng.

40

`close()`

Đóng stream.

– Ví dụ chi tiết cách sử dụng Stream API:

```java
import java.util.Objects;

public class Order {
    private int id;
    private String productName;
    private String productType;
    private String vendor;
    private int quantity;
    private double price;

    public Order() {
    }

    public Order(int id, String productName, String productType, String vendor, int quantity, double price) {
        this.id = id;
        this.productName = productName;
        this.productType = productType;
        this.vendor = vendor;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productType='" + productType + '\'' +
                ", vendor='" + vendor + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && quantity == order.quantity && Double.compare(price, order.price) == 0 && Objects.equals(productName, order.productName) && Objects.equals(productType, order.productType) && Objects.equals(vendor, order.vendor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, productType, vendor, quantity, price);
    }
}
```

```java
import java.util.ArrayList;
import java.util.List;

public class MockUp {

    public static List<Order> orderList() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1, "Iphone 14", "Mobile", "Apple", 3, 580.25));
        orders.add(new Order(2, "Iphone 14 Pro", "Mobile", "Apple", 5, 699.99));
        orders.add(new Order(3, "Samsung Galaxy", "Mobile", "Samsung", 7, 32.45));
        orders.add(new Order(4, "Samsung Watch5", "Watch", "Samsung", 2, 230.21));
        orders.add(new Order(5, "MacBook pro", "Laptop", "Apple", 4, 233.25));
        orders.add(new Order(6, "Nokia 2630", "Mobile", "S", 6, 100));
        return orders;
    }

}
```

```java
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        allMatch();
        anyMatch();
        noneMatch();
        streamBuilder();
        collect();
        streamConcat();
        distinct();
        findAny();
        findFirst();
        flatMap();
        generate();
        common();
        sort();
        skip();
        peek();
    }


    /**
     * boolean anyMatch(Predicate<? super T> predicate)
     * It returns all elements of this stream which match the provided predicate.
     * If the stream is empty then true is returned and the predicate is not evaluated.
     */
    private static void allMatch() {
        // get all product by type=Mobile
        List<Order> mobile = MockUp.orderList().stream().filter(order -> Objects.equals(order.getProductType(), "Mobile")).collect(Collectors.toList());

        System.out.println("Check Mobile");
        boolean isMobile = mobile.stream().allMatch(order -> Objects.equals(order.getProductType(), "Mobile"));
        if (isMobile) {
            System.out.println("All products are Mobile");
        }

    }

    /**
     * boolean allMatch(Predicate<? super T> predicate)
     * It returns any element of this stream that matches the provided predicate.
     * If the stream is empty then false is returned and the predicate is not evaluated.
     */
    private static void anyMatch() {
        boolean flag = MockUp.orderList().stream().anyMatch(product -> product.getProductName().equals("MacBook pro"));
        System.out.println(flag);
    }

    private static void noneMatch() {
        boolean check = MockUp.orderList().stream().noneMatch(order -> order.getPrice() > 800);
        if (check) {
            System.out.println("There is no order with price greater than 800");
        } else {
            System.out.println("There is order with price greater than 800");
        }
    }

    /**
     * static <T> Stream.Builder<T> builder()
     * It returns a builder for a Stream.
     */
    private static void streamBuilder() {
        Stream.Builder<String> builder = Stream.builder();
        Stream<String> stream = builder.add("Java").add("PHP").add("Python").build();
        stream.forEach(System.out::println);
    }

    private static void collect() {
        System.out.println("Collectors.toList(): All Samsung");
        List<Order> samsung = MockUp.orderList().stream().filter(order -> Objects.equals(order.getVendor(), "Samsung")).collect(Collectors.toList());
        System.out.println(samsung);

        System.out.println("\nCollectors.toSet(): All Vendors");
        Set<String> vendors = MockUp.orderList().stream().map(Order::getVendor).collect(Collectors.toSet());
        System.out.println(vendors);
    }

    /**
     * static <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b)
     * It creates a lazily concatenated stream whose elements are all the elements of the first stream followed by all the elements of the second stream.
     * The resulting stream is ordered if both of the input streams are ordered, and parallel if either of the input streams is parallel.
     * When the resulting stream is closed, the close handlers for both input streams are invoked.
     */
    private static void streamConcat() {
        Stream<String> stream1 = Stream.of("Geeks");
        Stream<String> stream2 = Stream.of("GeeksQuiz");
        Stream<String> stream3 = Stream.of("GeeksforGeeks");
        Stream<String> stream4 = Stream.of("GFG");

        // concatenating all the Streams
        // with Stream.concat() function
        // and displaying the result
        Stream.concat(Stream.concat(Stream.concat(stream1, stream2), stream3), stream4)
                .forEach(System.out::println);
    }

    /**
     * Stream<T> distinct()
     * It returns a stream consisting of the distinct elements (according to Object.equals(Object)) of this stream.
     */
    private static void distinct() {
        List<String> productType = MockUp.orderList().stream().map(Order::getProductType).distinct().collect(Collectors.toList());
        System.out.println(productType);
    }

    /**
     * Optional<T> findAny()
     * It returns an Optional describing some element of the stream, or an empty Optional if the stream is empty.
     */
    private static void findAny() {
        // instance new stream empty
        List<String> productType = MockUp.orderList().stream().map(Order::getProductType).distinct().collect(Collectors.toList());

        Optional<String> check = productType.stream().findAny();

        if (check.isPresent()) {
            productType.forEach(System.out::println);
        } else {
            System.out.println("ProductType not exist");
        }
    }

    /**
     * Optional<T> findFirst()
     * It returns an Optional describing the first element of this stream, or an empty Optional if the stream is empty.
     * If the stream has no encounter order, then any element may be returned.
     */
    private static void findFirst() {
        Optional<Order> first = MockUp.orderList().stream().findFirst();
        first.ifPresent(System.out::println);
    }

    /**
     * <R> Stream<R> flatMap(Function<? super T,? extends Stream<? extends R>> mapper)
     * It returns a stream consisting of the results of replacing each element of this stream with the contents of a mapped stream produced by applying
     * the provided mapping function to each element. Each mapped stream is closed after its contents have been placed into this stream.
     * (If a mapped stream is null an empty stream is used, instead.)
     */
    private static void flatMap() {
        List<Order> orders = MockUp.orderList();
        List<String> productName = orders.stream().map(Order::getProductName).collect(Collectors.toList());
        List<String> productType = orders.stream().map(Order::getProductType).collect(Collectors.toList());

        List<List<String>> product = new ArrayList<>();
        product.add(productName);
        product.add(productType);

        System.out.println("Distinct all product type");
        product.stream().flatMap(list -> list.stream().distinct()).forEach(System.out::println);

        System.out.println("\nTotal price");
        double total = MockUp.orderList().stream()
                .map(Order::getPrice)
                .flatMapToDouble(DoubleStream::of).sum();
        System.out.println(total);

        System.out.println("Print first character");
        productName.stream().flatMap(s -> Stream.of(s.toUpperCase().charAt(0))).forEach(System.out::println);
    }

    /**
     * static <T> Stream<T> generate(Supplier<T> s)
     * It returns an infinite sequential unordered stream where each element is generated by the provided Supplier.
     * This is suitable for generating constant streams, streams of random elements, etc.
     */
    private static void generate() {
        Stream.generate(new Random()::nextInt).limit(10).forEach(System.out::println);
        Stream.generate(new Random()::nextDouble).limit(3).forEach(System.out::println);
    }

    private static void common() {
        List<Order> personList = MockUp.orderList();
        //        System.out.println("\n-- Sum salary --");
        double totalSalary1 = personList.stream().mapToDouble(Order::getPrice).sum();
        System.out.println("Sum by Lambda: " + totalSalary1);
        Double totalSalary2 = personList.stream().map(Order::getPrice)
                .reduce(0.0d, Double::sum);
        System.out.println("Sum by reduce, Double::sum: " + totalSalary2);

        Double totalSalary3 = personList.stream().map(Order::getPrice).reduce(0.0d, (sum, salary) -> sum + salary);
        System.out.println("Sum by reduce, Lambda " + totalSalary3);

        Double totalSalary4 = personList.stream().collect(Collectors.summingDouble(Order::getPrice));
        System.out.println("Sum by Collectors.summingDouble: " + totalSalary4);


        System.out.println("\n-- Max salary --");
        System.out.println(personList.stream().mapToDouble(Order::getPrice).max().getAsDouble());

        System.out.println("\n-- Min salary --");
        System.out.println(personList.stream().mapToDouble(Order::getPrice).min().getAsDouble());

        System.out.println("\n-- Average salary --");
        System.out.println(personList.stream().mapToDouble(Order::getPrice).average().getAsDouble());

    }

    /**
     * Sort all element follow alphabet
     */
    private static void sort() {
        MockUp.orderList().stream().map(Order::getProductName).sorted().forEach(System.out::println);
    }

    /**
     * Stream<T> skip(long n)
     * It returns a stream consisting of the remaining elements of this stream after discarding the first n elements of the stream.
     * If this stream contains fewer than n elements then an empty stream will be returned.
     */
    private static void skip() {
        MockUp.orderList().stream().skip(1).forEach(System.out::println);
    }

    private static void peek() {
        MockUp.orderList().stream()
                .peek(order -> order.setProductName(order.getProductName().toUpperCase())).forEach(System.out::println);
    }
}
```
