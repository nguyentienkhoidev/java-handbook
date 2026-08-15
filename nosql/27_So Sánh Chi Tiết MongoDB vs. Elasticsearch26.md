# So Sánh Chi Tiết: MongoDB vs. Elasticsearch

### **1\. Giới thiệu**

**MongoDB**: Cơ sở dữ liệu NoSQL **hướng tài liệu (document-oriented)**, lưu trữ dữ liệu dưới dạng BSON (Binary JSON). Được thiết kế để quản lý dữ liệu **phi cấu trúc hoặc bán cấu trúc**, hỗ trợ sharding, replication và giao dịch.

**Elasticsearch**: Công cụ tìm kiếm và phân tích dữ liệu **phân tán**, xây dựng trên **Apache Lucene**. Dữ liệu được lưu dưới dạng JSON, tối ưu cho **tìm kiếm toàn văn (full-text search), phân tích log, real-time analytics**.

👉 Cả hai đều làm việc với dữ liệu document-based, nhưng **mục tiêu thiết kế khác nhau**: MongoDB là database tổng quát, còn Elasticsearch là search engine + analytics.

### **2\. So sánh theo tiêu chí**

#### **2.1 Mục đích chính**

*   **MongoDB**: Lưu trữ và quản lý dữ liệu (database).
    
*   **Elasticsearch**: Tìm kiếm và phân tích dữ liệu (search/analytics engine).
    

#### **2.2 Kiểu dữ liệu & lưu trữ**

*   **MongoDB**:
    
    *   Lưu trữ document BSON, có thể chứa dữ liệu phức tạp, object lồng nhau, mảng.
        
    *   Hỗ trợ thay đổi schema linh hoạt.
        
*   **Elasticsearch**:
    
    *   Lưu document JSON, nhưng dữ liệu được **đánh chỉ mục (indexed)** ngay khi lưu.
        
    *   Mapping định nghĩa kiểu dữ liệu (string, keyword, date, geo…).
        

#### **2.3 Khả năng tìm kiếm**

*   **MongoDB**:
    
    *   Hỗ trợ query cơ bản (filter, sort, aggregation).
        
    *   Có full-text search nhưng còn hạn chế.
        
*   **Elasticsearch**:
    
    *   Tìm kiếm toàn văn cực mạnh (full-text, fuzzy, regex, relevance score, highlight…).
        
    *   Phân tích dữ liệu với **aggregation framework** gần real-time.
        

👉 Nếu yêu cầu **search nâng cao**, Elasticsearch vượt trội.

#### **2.4 Hiệu suất đọc/ghi**

*   **MongoDB**:
    
    *   Tối ưu cho **ghi dữ liệu (write-heavy apps)**.
        
    *   Khả năng mở rộng ngang tốt (sharding).
        
*   **Elasticsearch**:
    
    *   Tối ưu cho **đọc/tìm kiếm nhanh**.
        
    *   Indexing ngốn tài nguyên hơn ghi dữ liệu thông thường.
        

#### **2.5 Khả năng mở rộng**

*   Cả hai đều hỗ trợ **phân tán, sharding, replication**.
    
*   MongoDB: phù hợp hệ thống OLTP (transactional).
    
*   Elasticsearch: phù hợp hệ thống OLAP (analytics, search).
    

#### **2.6 Giao dịch (Transactions)**

*   **MongoDB**: Hỗ trợ **multi-document ACID transactions** từ v4.0.
    
*   **Elasticsearch**: Không hỗ trợ ACID transactions, chỉ đảm bảo tính **eventual consistency**.
    

#### **2.7 Khả năng phân tích dữ liệu**

*   **MongoDB**: Aggregation pipeline mạnh mẽ, nhưng thiên về xử lý dữ liệu ứng dụng.
    
*   **Elasticsearch**: Aggregation + search kết hợp, cực mạnh cho **log analysis, metrics, dashboards** (kết hợp với Kibana).
    

#### **2.8 Tích hợp hệ sinh thái**

*   **MongoDB**: Kết hợp tốt với ứng dụng web, mobile, microservices. Có MongoDB Atlas (DBaaS).
    
*   **Elasticsearch**: Thường dùng trong **ELK Stack (Elasticsearch, Logstash, Kibana)** để phân tích log, monitoring, SIEM.
    

#### **3\. Khi nào nên dùng MongoDB?**

*   Ứng dụng **web/mobile** cần database lưu trữ chính.
    
*   Hệ thống cần **giao dịch, cập nhật dữ liệu thường xuyên**.
    
*   Dữ liệu phi cấu trúc thay đổi thường xuyên, cần schema linh hoạt.
    
*   Cần mở rộng ngang, quản lý dữ liệu quy mô lớn (big data storage).
    

Ví dụ: e-commerce, social media, IoT data storage.

#### **4\. Khi nào nên dùng Elasticsearch?**

*   Hệ thống cần **search engine mạnh mẽ** (tìm kiếm full-text, gợi ý, autocomplete).
    
*   Phân tích dữ liệu **gần real-time** (log, metrics, monitoring).
    
*   Xây dựng **dashboard** với Kibana để theo dõi dữ liệu.
    
*   Khi hiệu suất tìm kiếm trong MongoDB không còn đủ.
    

Ví dụ: công cụ tìm kiếm website, phân tích log hệ thống, SIEM, search trong e-commerce.

#### **5\. Kết hợp MongoDB + Elasticsearch**

Trong nhiều hệ thống, hai công nghệ thường **kết hợp với nhau**:

*   MongoDB làm **primary database** để lưu trữ dữ liệu chính.
    
*   Elasticsearch dùng để **index dữ liệu** từ MongoDB, phục vụ **search & analytics**.
    

👉 Ví dụ:

*   **E-commerce**: MongoDB lưu thông tin sản phẩm, đơn hàng. Elasticsearch xử lý tìm kiếm sản phẩm và phân tích hành vi người dùng.
    
*   **Logging system:** MongoDB lưu dữ liệu hoạt động, Elasticsearch phục vụ dashboard phân tích real-time.
    

#### **6\. Kết luận**

*   **MongoDB**: Database tổng quát, mạnh về lưu trữ và giao dịch.
    
*   **Elasticsearch**: Search/analytics engine, mạnh về tìm kiếm và phân tích real-time.
    
*   **Không thay thế cho nhau**, mà **bổ sung cho nhau** trong nhiều hệ thống hiện đại.
    

