# Các câu lệnh điều khiển trong Java IF/ELSE, SWITCH

Trình biên dịch Java thực thi mã từ trên xuống dưới. Các câu lệnh trong mã được thực thi theo thứ tự xuất hiện của chúng. Tuy nhiên, Java cung cấp các câu lệnh có thể được sử dụng để kiểm soát luồng `code` Java. Các câu lệnh như vậy được gọi là câu lệnh kiểm soát luồng (**Control Statements**). Đây là một trong những tính năng cơ bản của Java, cung cấp luồng chương trình mượt mà.

### **1\. Câu lệnh If/Else**

![](https://cdn.tayjava.com/production/image/20250905_145914_pasted-1757059154116.png)

– Cú pháp:

```java
if(condition){
    //code to be executed  
}
```

– Bối cảnh áp dụng:

Kiểm tra điều kiện để thực hiện 1 hành động nào đó liên quan Giả sử khi user gửi thông tin đăng ký tới hệ thống thì các thông tin đó cần phải được xác nhận là hợp lệ nếu họ nhập không đúng thì chúng ta sẽ không cho họ đăng ký và yêu cầu nhập lại

– Ví dụ:

```java
int age = 25;

if (age >= 18) {
    System.out.println("Đã đủ tuổi cưới vợ rồi");
}
```

### **2\. Câu lệnh Switch**

![](https://cdn.tayjava.com/production/image/20250905_145914_pasted-1757059154118.png)

  
– Cú pháp:

```java
switch (expression) {
    case value1:
         //code to be executed;    
         break;  //optional  
    case value2:
         //code to be executed;    
         break;  //optional  
    default:
        // code to be executed if all cases are not matched;
}
```

– Bối cảnh áp dụng: Khi có nhiều điều kiện phức tạp

– Ví dụ:

```java
String expression = "874";
switch(expression){
    case "123":
        System.out.println("Bạn đã nhận được khoản tiền thưởng 100k");
        break;
    case "874":
        System.out.println("Bạn đã nhận được khoản tiền thưởng 200k");
        break;
    case "692":
        System.out.println("Bạn đã nhận được khoản tiền thưởng 500k");
        break;
    default:
        System.out.println("Chúc bạn lần sau may mắn");
}
```
