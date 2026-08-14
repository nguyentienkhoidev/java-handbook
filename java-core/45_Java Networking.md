# Java Networking

**Java Networking** là một phần quan trọng trong lập trình Java, cho phép các ứng dụng giao tiếp với nhau thông qua mạng máy tính. Java cung cấp sẵn các **packages** và **API** để hỗ trợ xây dựng ứng dụng mạng như **client-server**, **peer-to-peer** hay **phân tán**.

Dưới đây là các khái niệm và thành phần quan trọng trong Java Networking:

### **1\. Gói** `java.net`

Gói `java.net` chứa các **class** và **interface** hỗ trợ lập trình mạng. Một số lớp tiêu biểu:

*   **InetAddress**: Đại diện cho một địa chỉ IP.
    
*   **URL**: Đại diện cho **Uniform Resource Locator**, cho phép truy xuất dữ liệu từ tài nguyên qua mạng.
    
*   **URLConnection**: Đại diện cho kết nối đến một tài nguyên được chỉ định bởi URL.
    
*   **Socket**: Dùng để tạo kết nối TCP giữa client và server.
    
*   **ServerSocket**: Dùng để tạo server và lắng nghe các kết nối TCP từ client.
    
*   **DatagramSocket** và **DatagramPacket**: Dùng cho kết nối UDP, hỗ trợ gửi/nhận các packet không yêu cầu kết nối.
    

### **2\. Các giao thức mạng phổ biến**

*   **TCP (Transmission Control Protocol)**: Giao thức hướng kết nối, đảm bảo dữ liệu truyền tải **an toàn**, **đầy đủ** và **theo đúng thứ tự**.
    
*   **UDP (User Datagram Protocol)**: Giao thức không kết nối, **nhanh hơn TCP**, nhưng không đảm bảo dữ liệu toàn vẹn (có thể mất gói hoặc sai thứ tự).
    

### **3\. Lập trình Socket trong Java**

Java hỗ trợ lập trình socket cho cả **TCP** và **UDP**, giúp xây dựng các ứng dụng client-server.

![](https://cdn.tayjava.com/production/image/20250910_095948_pasted-1757473185965.png)

#### **🔹 TCP Sockets**

TCP là giao thức phổ biến trong mô hình **client-server**.

*   **Server**: Tạo một `ServerSocket` để lắng nghe kết nối trên một cổng cụ thể. Khi có client kết nối, server sẽ tạo ra một `Socket` để giao tiếp với client.
    

```java
import java.io.*;
import java.net.*;

public class SampleServer {
    public static void main(String[] args) throws IOException {
        // Tạo ServerSocket tại cổng 4953
        ServerSocket serverSocket = new ServerSocket(4953);
        System.out.println("Server đang chờ kết nối...");

        // Chờ client kết nối
        Socket socket = serverSocket.accept();
        System.out.println("Client đã kết nối.");

        // Nhận dữ liệu từ client
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String message = reader.readLine();
        System.out.println("Message từ client: " + message);

        // Gửi phản hồi về client
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println("Xin chào từ Server!");

        socket.close();
        serverSocket.close();
    }
}
```

*   **Client**: Tạo `Socket` để kết nối đến server.
    

```java
import java.io.*;
import java.net.*;

public class SampleClient {
    public static void main(String[] args) throws IOException {
        // Kết nối đến server tại localhost, cổng 4953
        Socket socket = new Socket("localhost", 4953);

        // Gửi dữ liệu đến server
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println("Xin chào từ Client!");

        // Nhận phản hồi từ server
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String response = reader.readLine();
        System.out.println("Phản hồi từ server: " + response);

        socket.close();
    }
}
```

#### **🔹 UDP Sockets**

Khác với TCP, UDP **không yêu cầu kết nối** trước khi gửi dữ liệu. Các gói tin được gửi độc lập và không đảm bảo toàn vẹn.

*   **UDP Server**:
    

```java
import java.io.IOException;
import java.net.*;

public class SampleUDPServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(7749);
        byte[] receiveData = new byte[1024];

        System.out.println("Server UDP đang chờ nhận dữ liệu...");

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("Nhận từ client: " + message);

        socket.close();
    }
}
```

*   **UDP Client:**
    

```java
import java.net.*;

public class SampleUDPClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        byte[] sendData = "Xin chào Tây Java".getBytes();

        InetAddress IPAddress = InetAddress.getByName("localhost");
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 7749);

        socket.send(sendPacket);
        socket.close();
    }
}
```

### **4\. Giao tiếp qua URL**

Java hỗ trợ giao tiếp với tài nguyên qua **URL** bằng các lớp `URL` và `URLConnection`.

```java
import java.net.*;
import java.io.*;

public class URLReader {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://tayjava.vn");

        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }

        in.close();
    }
}
```

### **5\. Lợi ích và Ứng dụng của Java Networking**

*   **Tính độc lập nền tảng**: Java chạy trên nhiều hệ điều hành, giúp ứng dụng mạng dễ dàng triển khai đa nền tảng.
    
*   **Hỗ trợ giao thức chuẩn**: Hỗ trợ mạnh mẽ các giao thức như HTTP, FTP, TCP, UDP.
    
*   **Tính bảo mật cao**: Java cung cấp API bảo mật mạnh mẽ (SSL/TLS, mã hóa, xác thực).
    

👉 Java Networking được ứng dụng trong **Web Services**, **ứng dụng phân tán**, **chat app**, **game online**, và nhiều hệ thống hiện đại khác.
