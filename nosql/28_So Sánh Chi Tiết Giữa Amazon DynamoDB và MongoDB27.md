# So Sánh Chi Tiết Giữa Amazon DynamoDB và MongoDB

### **1\. Kiến trúc và triển khai**

Tiêu chí**DynamoDBMongoDBKiểu triển khai**Managed service 100% (serverless) do AWS vận hành. Người dùng không quản lý hạ tầng.Open-source, có thể tự cài đặt on-premise hoặc dùng MongoDB Atlas (managed service).**Mô hình dữ liệu**Bảng (table) → Items → Attributes. Item giống document nhưng đơn giản hơn (dữ liệu key-value).Collections → Documents (JSON/BSON). Document linh hoạt, có thể lồng nhiều cấp.**Hạ tầng**Chạy trong AWS, scale tự động. Không có quyền truy cập server trực tiếp.Có thể chạy trên bất kỳ hạ tầng nào: local, on-premise, cloud (AWS, Azure, GCP…).**Ngôn ngữ truy vấn**PartiQL (SQL-like) + API (GetItem, Query, Scan, UpdateItem).MongoDB Query Language (MQL), rất giống JSON, mạnh mẽ và linh hoạt.

### **2\. Khả năng mở rộng & Hiệu năng**

Tiêu chí**DynamoDBMongoDBScale**Tự động scale dựa trên dung lượng đọc/ghi (RCU/WCU). Không cần quản lý cluster.Sharding thủ công để scale-out. Người dùng quản lý cluster hoặc để Atlas làm.**Độ trễ**Rất thấp (single-digit milliseconds), ổn định nhờ hạ tầng AWS.Có thể thấp nhưng phụ thuộc cấu hình cluster, network và workload.**Replication**Multi-AZ replication tự động. Tích hợp global table để nhân bản đa vùng (multi-region).Replica Set (Primary–Secondary–Arbiter). Multi-region replication phức tạp hơn.**Transactions**Hỗ trợ giao dịch ACID multi-item (nhưng giới hạn 25 items/lần).Từ MongoDB 4.0 hỗ trợ multi-document ACID transactions đầy đủ.

### **3\. Mô hình dữ liệu**

*   **DynamoDB**: Tối ưu cho **key-value và access patterns cụ thể**. Mỗi bảng cần xác định **partition key** (và có thể có sort key). Nếu thiết kế sai schema, hiệu năng dễ bị nghẽn.
    
*   **MongoDB**: Tối ưu cho **document linh hoạt**, dữ liệu có thể thay đổi schema dễ dàng. Thích hợp cho các ứng dụng cần cấu trúc phức tạp, lồng nhiều cấp.
    

👉 Tóm lại:

*   DynamoDB → Phù hợp khi **biết rõ access pattern ngay từ đầu**.
    
*   MongoDB → Phù hợp khi **dữ liệu và schema thay đổi linh hoạt**.
    

### **4\. Chi phí**

Tiêu chí**DynamoDBMongoDBChi phí**Pay-per-request (on-demand) hoặc provisioned capacity. Không tốn chi phí quản trị.Nếu tự triển khai: chỉ tốn hạ tầng (EC2, VPS…). Nếu dùng Atlas: trả phí theo cluster size.**Predictability**Dễ kiểm soát nếu workload ổn định. Nhưng nếu query/scan toàn bảng → rất tốn kém.Linh hoạt hơn, nhưng nếu tự quản lý cluster thì tốn công DevOps.**Free Tier**Có free tier (25 GB lưu trữ, 25 RCU/WCU mỗi tháng).MongoDB Atlas có free tier (512 MB).

### **5\. Tính năng đặc biệt**

Tính năng**DynamoDBMongoDBTTL (Time-to-Live)**Có sẵn, tự động xóa dữ liệu hết hạn.Có thể dùng TTL Index.**Streams/Change Data Capture**DynamoDB Streams (event-driven, tích hợp Lambda).Change Streams (theo dõi thay đổi document).**Secondary Index**GSI (Global Secondary Index), LSI (Local Secondary Index).Bất kỳ trường nào cũng có thể tạo index, hỗ trợ compound index.**Search**Không hỗ trợ trực tiếp → thường kết hợp với OpenSearch/Elasticsearch.Có aggregation framework mạnh mẽ, hỗ trợ tìm kiếm text và tích hợp Atlas Search.**Backup/Restore**Managed, chỉ vài click.Atlas hỗ trợ backup, còn self-hosted phải tự setup.

## 6\. **Khi nào nên dùng?**

✅ **Chọn DynamoDB khi:**

*   Bạn chạy **hệ thống trong AWS** và muốn **serverless – không quản lý hạ tầng**.
    
*   Dữ liệu có access pattern rõ ràng (ví dụ: e-commerce order, IoT telemetry, session store).
    
*   Cần **tốc độ truy xuất thấp và ổn định** trên quy mô lớn (hàng triệu request/s).
    

✅ **Chọn MongoDB khi:**

*   Bạn cần **schema linh hoạt** cho dữ liệu phức tạp.
    
*   Hệ thống không phụ thuộc AWS, cần chạy **đa nền tảng hoặc on-premise**.
    
*   Ứng dụng yêu cầu **query phức tạp, aggregation, transactions nhiều document** (ví dụ: CRM, CMS, phân tích dữ liệu).
    

## 7\. **Tóm tắt so sánh**

Tiêu chí**DynamoDBMongoDB**Triển khaiAWS managed, serverlessOpen-source, self-hosted hoặc AtlasMô hình dữ liệuKey-Value, Table-Item-AttributeDocument (JSON/BSON)ScaleTự độngSharding thủ công (Atlas hỗ trợ)TransactionsCó, nhưng giới hạnĐầy đủ (multi-document)Chi phíPay-per-requestTùy cluster sizeQueryPartiQL, APIMQL (JSON-like, mạnh mẽ)Global replicationDễ dàng (Global Table)Khó hơn, phải cấu hìnhPhù hợp nhấtWorkload đơn giản, scale cực lớnDữ liệu linh hoạt, query phức tạp

## ✅ Kết luận

**DynamoDB**: lựa chọn hàng đầu cho hệ thống chạy trên AWS cần hiệu năng cao, không muốn quản lý hạ tầng và có access pattern rõ ràng.

**MongoDB**: phù hợp cho ứng dụng phức tạp, cần linh hoạt trong thiết kế dữ liệu, có thể chạy trên nhiều môi trường.

👉 Tóm gọn: **DynamoDB tối ưu cho scale-out ổn định**, còn **MongoDB tối ưu cho sự linh hoạt và giàu tính năng**.

