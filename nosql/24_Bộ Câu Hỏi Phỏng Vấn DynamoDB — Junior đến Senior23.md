# Bộ Câu Hỏi Phỏng Vấn DynamoDB — Junior đến Senior

![Bộ Câu Hỏi Phỏng Vấn DynamoDB — Junior đến Senior.jpeg](../images/575b5777-42c1-47b1-9370-47042110ab05.jpeg)

* * *

## 🟢 JUNIOR (0–2 năm)

Mục tiêu: Kiểm tra hiểu biết cơ bản về DynamoDB, Primary Key, CRUD và khi nào nên dùng.

* * *

### Khái Niệm Cơ Bản

**Q1. DynamoDB là gì? Điểm khác biệt lớn nhất so với MongoDB và Cassandra?**

Đáp án mong đợi:

*   DynamoDB = **fully managed** Key-Value + Document database trên AWS
    
*   AWS lo toàn bộ: hardware, patching, replication, backup, scaling
    
*   Developer chỉ cần design schema và viết code — không ops
    

**Khác biệt với MongoDB/Cassandra:**


|  | DynamoDB | MongoDB | Cassandra |
|---|---|---|---|
| Managed | ✅ Fully | ❌ Self-hosted | ❌ Self-hosted |
| Setup | 0 phút | 30 phút | 60+ phút |
| Scale | Tự động | Manual | Manual node |
| Pricing | Pay per request | Server cost | Server cost |
| Vendor | AWS only | Portable | Portable |



**Khi nào chọn DynamoDB:**

*   Đang build trên AWS ecosystem (Lambda, ECS)
    
*   Không muốn quản lý infrastructure
    
*   Traffic không đoán trước được
    
*   Cần multi-region global replication
    

🚩 Red flag: Không biết DynamoDB là AWS-only, không biết vendor lock-in trade-off

  
**Q2. Partition Key và Sort Key trong DynamoDB là gì? Cho ví dụ cụ thể.**

Đáp án mong đợi:

**Simple Primary Key (chỉ Partition Key):**

```java
Table: Courses
PK: courseId = "COURSE#1"
→ Mỗi item unique theo PK
→ Chỉ exact match lookup
```

**Composite Primary Key (Partition Key + Sort Key):**

```java
Table: FoxDev
PK: "USER#1",  SK: "PROFILE"           → user profile
PK: "USER#1",  SK: "ENROLLMENT#COURSE#1" → enrollment
PK: "USER#1",  SK: "ENROLLMENT#COURSE#2"
PK: "USER#1",  SK: "ORDER#2025-03-15#001"
PK: "COURSE#1", SK: "METADATA"         → course info
PK: "COURSE#1", SK: "ENROLLMENT#USER#1" → who enrolled

→ Nhiều items cùng PK → cùng partition → 1 request lấy tất cả
→ Range query trên SK: begins_with, between, >, <
```

✅ Điểm cộng: Giải thích được tại sao composite key cho phép lấy nhiều loại data trong 1 request

  
**Q3. Giải thích sự khác biệt giữa Query và Scan trong DynamoDB.**

Đáp án mong đợi:

**Query:**

*   Đọc items trong **1 partition** (cùng PK)
    
*   Nhanh, hiệu quả, chỉ tốn RCU cho items đọc được
    
*   Bắt buộc có Partition Key
    

```python
table.query(KeyConditionExpression=Key("PK").eq("USER#1"))
```

**Scan:**

*   Đọc **toàn bộ table** rồi filter
    
*   Chậm, tốn RCU cho tất cả items dù bị filter
    
*   Không cần biết PK
    

```python
table.scan(FilterExpression=Attr("status").eq("ACTIVE"))
```

**Nguyên tắc:** Luôn dùng Query. Scan chỉ dùng cho admin tasks, migration, analytics batch — KHÔNG bao giờ dùng Scan trong production user-facing API.

🚩 Red flag: Không biết Scan tốn RCU của tất cả items dù bị filter out

  
**Q4. RCU và WCU là gì? Tính toán cơ bản như thế nào?**

Đáp án mong đợi:

**WCU (Write Capacity Unit):**

*   1 WCU = 1 write của item ≤ 1KB/giây
    
*   Item 2.5KB → cần ceil(2.5) = 3 WCU
    

**RCU (Read Capacity Unit):**

*   1 RCU = 1 **strongly consistent** read của item ≤ 4KB/giây
    
*   1 RCU = 2 **eventually consistent** reads của item ≤ 4KB/giây
    
*   Item 6KB strongly consistent → ceil(6/4) = 2 RCU
    

**Ví dụ:**

```python
import math

def calc_wcu(item_size_kb: float) -> int:
    return math.ceil(item_size_kb)

def calc_rcu(item_size_kb: float, consistent: bool = False) -> float:
    units = math.ceil(item_size_kb / 4)
    return units if consistent else units / 2

# Item 1KB: 1 WCU để ghi, 0.5 RCU eventually consistent
# Item 5KB: 5 WCU để ghi, 2 RCU strongly consistent
```

  
**Q5. On-demand và Provisioned Capacity là gì? Nên chọn cái nào?**

Đáp án mong đợi:

**On-demand Mode:**

*   Trả tiền theo số requests thực tế
    
*   $1.25 per million WRU, $0.25 per million RRU
    
*   Tự scale ngay lập tức, không cần estimate
    
*   Tốt cho: traffic không đoán được, new app, low traffic, flash sales
    

**Provisioned Mode:**

*   Đặt trước RCU/WCU
    
*   ~$0.00065 per WCU/giờ, ~$0.00013 per RCU/giờ
    
*   Rẻ hơn On-demand ~70% nếu traffic predictable
    
*   Auto Scaling: tự điều chỉnh trong min/max range
    

**Khi nào chọn:**

```java
Traffic mới, không chắc → On-demand (an toàn)
Traffic ổn định, đã biết pattern → Provisioned + Auto Scaling (tiết kiệm)
Flash sale, viral → On-demand (tránh throttle)
```

  
**Q6. FilterExpression và KeyConditionExpression khác nhau thế nào?**

Đáp án mong đợi:

**KeyConditionExpression:**

*   Dùng để tìm items theo Primary Key (PK + SK)
    
*   Chạy **trước** khi đọc → chỉ đọc items khớp
    
*   Giảm RCU consumed
    
*   Chỉ dùng với Query
    

```python
# Chỉ đọc items của USER#1 có SK bắt đầu bằng ENROLLMENT#
Key("PK").eq("USER#1") & Key("SK").begins_with("ENROLLMENT#")
```

**FilterExpression:**

*   Filter items **sau khi đọc** từ DB
    
*   Không giảm RCU — đã tốn RCU rồi mới filter
    
*   Dùng được với cả Query và Scan
    

```python
# Đọc tất cả enrollment của USER#1 trước, rồi filter completed=True
# → Vẫn tốn RCU cho tất cả enrollments dù không completed
Attr("completed").eq(True)
```

🚩 Red flag: Nghĩ FilterExpression giảm RCU — sai hoàn toàn

  
**Q7. DynamoDB item size limit là bao nhiêu? Ảnh hưởng đến thiết kế như thế nào?**

Đáp án mong đợi:

*   **Maximum item size: 400KB** — bao gồm cả attribute names và values
    
*   Khác với MongoDB (16MB) và Cassandra (không giới hạn cứng)
    

**Ảnh hưởng thiết kế:**

```python
# ❌ Không lưu file/image trong DynamoDB item
item = {
    "PK": "COURSE#1",
    "SK": "THUMBNAIL",
    "imageData": base64_encoded_image  # có thể > 400KB!
}

# ✅ Lưu reference đến S3, không lưu binary
item = {
    "PK":          "COURSE#1",
    "SK":          "METADATA",
    "thumbnailUrl": "s3://foxdev-assets/courses/1/thumb.jpg"
}

# ✅ Nếu cần lưu list lớn → tách ra items riêng
# Thay vì 1 item chứa list 500 entries
# → 500 items riêng lẻ với SK khác nhau
```

  
**Q8. GSI (Global Secondary Index) là gì? Cho ví dụ khi nào cần GSI.**

Đáp án mong đợi:

*   **GSI** = Global Secondary Index — index với Partition Key và Sort Key khác với main table
    
*   Cho phép query theo những attributes không phải main PK
    

```python
# Main table: PK=USER#{userId}, SK=PROFILE
# Query: "get user by email" → email không phải PK → không query được!

# Giải pháp: GSI
# GSI1PK = "USER_EMAIL"
# GSI1SK = email value

# Khi insert user:
item = {
    "PK":     "USER#1",
    "SK":     "PROFILE",
    "email":  "nam@gmail.com",
    # GSI attributes
    "GSI1PK": "USER_EMAIL",
    "GSI1SK": "nam@gmail.com",
}

# Query by email qua GSI
table.query(
    IndexName="GSI1",
    KeyConditionExpression=(
        Key("GSI1PK").eq("USER_EMAIL") &
        Key("GSI1SK").eq("nam@gmail.com")
    )
)
```

✅ Điểm cộng: Biết GSI có cost (writes vào main table đồng thời write vào GSI → RCU/WCU gấp đôi)

###   
Coding Question Junior

**Q9. Viết code Python dùng boto3 để: tạo user, lấy user theo userId, cập nhật email.**

Đáp án mong đợi:

```python
import boto3
from datetime import datetime

dynamodb = boto3.resource(
    "dynamodb",
    endpoint_url="http://localhost:8000",
    region_name="us-east-1",
    aws_access_key_id="fake",
    aws_secret_access_key="fake"
)
table = dynamodb.Table("FoxDev")

# CREATE
def create_user(user_id: str, email: str, name: str):
    table.put_item(Item={
        "PK":       f"USER#{user_id}",
        "SK":       "PROFILE",
        "userId":   user_id,
        "email":    email,
        "name":     name,
        "status":   "ACTIVE",
        "createdAt": datetime.now().isoformat(),
        # GSI for email lookup
        "GSI1PK":  "USER_EMAIL",
        "GSI1SK":  email,
    })

# READ
def get_user(user_id: str) -> dict | None:
    resp = table.get_item(Key={
        "PK": f"USER#{user_id}",
        "SK": "PROFILE"
    })
    return resp.get("Item")

# UPDATE
def update_email(user_id: str, new_email: str):
    table.update_item(
        Key={"PK": f"USER#{user_id}", "SK": "PROFILE"},
        UpdateExpression="SET email = :e, GSI1SK = :e, updatedAt = :t",
        ExpressionAttributeValues={
            ":e": new_email,
            ":t": datetime.now().isoformat()
        }
    )
```

* * *

## 🟡 INTERMEDIATE (2–4 năm)

Mục tiêu: Single Table Design, GSI patterns, Conditional Writes, BatchWrite, Transactions.

  
**Q10. Single Table Design là gì? Tại sao là concept quan trọng nhất của DynamoDB?**

Đáp án mong đợi:

**Multi-table (giống SQL):**

```java
Users table + Courses table + Enrollments table
→ Render dashboard = 3 separate requests
→ 3 network round trips = 3x latency
```

**Single Table Design:**

```java
1 table FoxDev — tất cả entities
PK=USER#1 → Query trả về: profile + enrollments + orders cùng lúc
→ 1 request, 1 network round trip
```

**Tại sao quan trọng:**

*   DynamoDB charge theo request
    
*   1 request lấy N items trong cùng partition = cùng chi phí với lấy 1 item
    
*   Query trong 1 partition = 1 node lookup = cực nhanh
    
*   Tránh N+1 problem hoàn toàn
    

**Nguyên tắc:**

```java
Generic PK/SK: "USER#1", "COURSE#1", "ENROLLMENT#COURSE#1"
→ Encode entity type + id vào key
→ Query PK=USER#1 → tất cả data của user
→ Filter SK: begins_with "ENROLLMENT#" → chỉ enrollments
```

  
**Q11. Thiết kế schema Single Table cho** [**nguyentienkhoi.hashnode.dev**](http://nguyentienkhoi.hashnode.dev)**. Giải thích access patterns và key encoding.**

Đáp án mong đợi:

**Access Patterns → Key Mapping:**


| Pattern | PK | SK |
|---|---|---|
| Get user by userId | USER#{userId} | PROFILE |
| Get user by email | GSI1PK=USER_EMAIL | GSI1SK={email} |
| Get course by courseId | COURSE#{courseId} | METADATA |
| List courses by category | GSI1PK=COURSE_CAT#{cat} | GSI1SK=RATING#{rating} |
| All enrollments of user | USER#{userId} | begins_with ENROLLMENT# |
| All users in course | GSI1PK=COURSE#{courseId} | begins_with ENROLLMENT# |
| All orders of user | USER#{userId} | begins_with ORDER# |
| List orders by status | GSI2PK=ORDER_STATUS#{status} | GSI2SK={date} |



**Ví dụ thực tế:**

```python
# 1 request lấy dashboard của user
resp = table.query(
    KeyConditionExpression=Key("PK").eq("USER#1"),
    ScanIndexForward=False
)
for item in resp["Items"]:
    if item["SK"] == "PROFILE":         profile = item
    elif item["SK"].startswith("ENROLLMENT#"): enrollments.append(item)
    elif item["SK"].startswith("ORDER#"):       orders.append(item)
# Profile + enrollments + orders trong 1 round trip!
```

  
**Q12. UpdateExpression trong DynamoDB — giải thích SET, ADD, REMOVE, DELETE.**

Đáp án mong đợi:

```python
from boto3.dynamodb.conditions import Attr

# SET — đặt giá trị
table.update_item(
    Key={"PK": "USER#1", "SK": "PROFILE"},
    UpdateExpression="SET #n = :name, updatedAt = :t",
    ExpressionAttributeNames={"#n": "name"},  # "name" là reserved word
    ExpressionAttributeValues={
        ":name": "Nguyen Van Nam",
        ":t": datetime.now().isoformat()
    }
)

# ADD — tăng số hoặc thêm vào Set
table.update_item(
    Key={"PK": "COURSE#1", "SK": "METADATA"},
    UpdateExpression="ADD enrolledCount :one, tags :new_tags",
    ExpressionAttributeValues={
        ":one": 1,
        ":new_tags": {"microservices", "cloud"}  # String Set
    }
)

# REMOVE — xóa attribute
table.update_item(
    Key={"PK": "USER#1", "SK": "PROFILE"},
    UpdateExpression="REMOVE oldField, tempData"
)

# DELETE — xóa khỏi Set
table.update_item(
    Key={"PK": "COURSE#1", "SK": "METADATA"},
    UpdateExpression="DELETE tags :old",
    ExpressionAttributeValues={":old": {"deprecated_tag"}}
)
```

  
**Q13. Conditional Write là gì? Implement Optimistic Locking với DynamoDB.**

Đáp án mong đợi:

**Conditional Write:** Check condition trước khi write — nếu không thỏa → fail với `ConditionalCheckFailedException`

```python
from botocore.exceptions import ClientError

# Insert only if not exists
def create_if_not_exists(user_id: str, data: dict) -> bool:
    try:
        table.put_item(
            Item={"PK": f"USER#{user_id}", "SK": "PROFILE", **data},
            ConditionExpression=Attr("PK").not_exists()
        )
        return True
    except ClientError as e:
        if e.response["Error"]["Code"] == "ConditionalCheckFailedException":
            return False  # đã tồn tại
        raise

# Optimistic Locking — tránh lost update
def update_with_version(course_id: str,
                         new_price: Decimal,
                         current_version: int) -> bool:
    try:
        table.update_item(
            Key={"PK": f"COURSE#{course_id}", "SK": "METADATA"},
            UpdateExpression="SET price = :p, version = :new_v",
            ConditionExpression=Attr("version").eq(current_version),
            ExpressionAttributeValues={
                ":p":     new_price,
                ":new_v": current_version + 1
            }
        )
        return True
    except ClientError as e:
        if e.response["Error"]["Code"] == "ConditionalCheckFailedException":
            return False  # version conflict, retry
        raise
```

  
**Q14. TransactWrite trong DynamoDB là gì? Khi nào dùng? Giới hạn là gì?**

Đáp án mong đợi:

```python
# TransactWrite: tối đa 100 items, ALL or NOTHING
client.transact_write_items(
    TransactItems=[
        # 1. Tạo Order
        {"Put": {
            "TableName": "FoxDev",
            "Item": {"PK": {"S": "USER#1"},
                     "SK": {"S": "ORDER#2025-03-15#001"},
                     "status": {"S": "PAID"}, ...},
            "ConditionExpression": "attribute_not_exists(PK)"
        }},
        # 2. Tạo Enrollment
        {"Put": {
            "TableName": "FoxDev",
            "Item": {"PK": {"S": "USER#1"},
                     "SK": {"S": "ENROLLMENT#COURSE#1"},
                     "completed": {"BOOL": False}, ...},
            "ConditionExpression": "attribute_not_exists(PK)"
        }},
        # 3. Increment enrolledCount
        {"Update": {
            "TableName": "FoxDev",
            "Key": {"PK": {"S": "COURSE#1"}, "SK": {"S": "METADATA"}},
            "UpdateExpression": "ADD enrolledCount :one",
            "ExpressionAttributeValues": {":one": {"N": "1"}}
        }}
    ]
)
```

**Giới hạn:**

*   Tối đa 100 items per transaction
    
*   Tối đa 4MB tổng data
    
*   Tất cả items phải trong **cùng region**
    
*   Cost: 2x RCU/WCU so với thường (overhead của transaction coordination)
    

  
**Q15. GSI Overloading là gì? Cho ví dụ 1 GSI phục vụ nhiều access patterns.**

Đáp án mong đợi:

**GSI Overloading:** Dùng 1 GSI với tên generic (GSI1PK, GSI1SK) để phục vụ nhiều query patterns khác nhau — mỗi entity type gán giá trị khác nhau vào GSI1PK/GSI1SK.

```java
GSI1PK             | GSI1SK              | Access Pattern
─────────────────────┼────────────────────┼────────────────────
USER_EMAIL          | nam@gmail.com       | Get user by email
COURSE_CAT#java     | RATING#04.80        | List courses by category
COURSE#1            | ENROLLMENT#USER#1   | Users enrolled in course
ORDER#ORD001        | METADATA            | Get order by orderId
ORDER_STATUS#PAID   | 2025-03-15T10:00:00 | List paid orders

→ 1 GSI phục vụ 5 access patterns!
→ Ít GSI = tiết kiệm cost (mỗi GSI = thêm storage + WCU)
```

###   
Coding Question Intermediate

**Q16. Implement hàm** `get_user_dashboard` **lấy profile + enrollments + last 5 orders trong 1 request.**

Đáp án mong đợi:

```python
from boto3.dynamodb.conditions import Key

def get_user_dashboard(table, user_id: str) -> dict:
    resp  = table.query(
        KeyConditionExpression=Key("PK").eq(f"USER#{user_id}"),
        ScanIndexForward=False   # newest SK first
    )

    profile, enrollments, orders = None, [], []

    for item in resp["Items"]:
        sk = item.get("SK", "")
        if sk == "PROFILE":
            profile = item
        elif sk.startswith("ENROLLMENT#"):
            enrollments.append(item)
        elif sk.startswith("ORDER#"):
            orders.append(item)

    return {
        "profile":     profile,
        "enrollments": enrollments,
        "orders":      orders[:5],   # last 5
        "stats": {
            "courses_enrolled":  len(enrollments),
            "courses_completed": sum(1 for e in enrollments
                                     if e.get("completed")),
            "total_orders":      len(orders),
        }
    }

# Test
dashboard = get_user_dashboard(table, "1")
print(f"User: {dashboard['profile']['name']}")
print(f"Stats: {dashboard['stats']}")
# Tất cả trong 1 network round trip!
```

* * *

## 🟠 ADVANCED (4–7 năm)

Mục tiêu: DynamoDB Streams, performance anti-patterns, capacity planning, production operations.

  
**Q17. DynamoDB Streams là gì? Thiết kế event-driven pipeline với Lambda.**

Đáp án mong đợi:

**DynamoDB Streams:** CDC (Change Data Capture) — mỗi INSERT/UPDATE/DELETE tạo stream record, giữ 24 giờ.

**StreamViewType options:**

*   `NEW_IMAGE`: item sau khi thay đổi
    
*   `OLD_IMAGE`: item trước khi thay đổi
    
*   `NEW_AND_OLD_IMAGES`: cả hai
    
*   `KEYS_ONLY`: chỉ PK + SK
    

```python
# Lambda handler xử lý stream events
def lambda_handler(event, context):
    for record in event["Records"]:
        event_name  = record["eventName"]  # INSERT, MODIFY, REMOVE
        new_item    = record["dynamodb"].get("NewImage", {})
        old_item    = record["dynamodb"].get("OldImage", {})
        entity_type = new_item.get("entityType", {}).get("S", "")

        if event_name == "INSERT":
            if entity_type == "ORDER":
                order_id = new_item["orderId"]["S"]
                user_id  = new_item["userId"]["S"]
                # Trigger: send confirmation email
                send_confirmation_email(user_id, order_id)

        elif event_name == "MODIFY":
            if entity_type == "ENROLLMENT":
                new_completed = new_item.get("completed", {}).get("BOOL", False)
                old_completed = old_item.get("completed", {}).get("BOOL", False)
                if new_completed and not old_completed:
                    # Trigger: issue certificate
                    issue_certificate(
                        new_item["userId"]["S"],
                        new_item["courseId"]["S"]
                    )
```

**Use cases:**

*   Gửi email khi order được tạo
    
*   Sync DynamoDB → Elasticsearch cho search
    
*   Invalidate cache khi data thay đổi
    
*   Cross-region replication (Global Tables)
    
*   Audit trail
    

  
**Q18. Giải thích 5 anti-patterns phổ biến nhất trong DynamoDB.**

Đáp án mong đợi:

**Anti-pattern 1: Scan thay vì Query**

```python
# ❌ Scan toàn table → tốn tiền, chậm
table.scan(FilterExpression=Attr("email").eq("nam@gmail.com"))
# ✅ GSI cho email lookup
table.query(IndexName="GSI1", KeyConditionExpression=...)
```

**Anti-pattern 2: Hot Partition Key**

```python
# ❌ PK = "COURSES" cho tất cả course items
# → 1 partition nhận 100% writes → throttled
table.query(KeyConditionExpression=Key("PK").eq("COURSES"))
# ✅ PK = "COURSE#1", "COURSE#2" — mỗi course là partition riêng
```

**Anti-pattern 3: Item > 400KB**

```python
# ❌ Lưu image binary trong item
{"PK": "COURSE#1", "imageData": base64_image_data}  # > 400KB
# ✅ Lưu S3 URL
{"PK": "COURSE#1", "thumbnailUrl": "s3://bucket/image.jpg"}
```

**Anti-pattern 4: Nhiều tables thay vì Single Table**

```python
# ❌ 3 requests riêng biệt
user    = users_table.get_item(...)
courses = courses_table.query(...)
orders  = orders_table.query(...)
# ✅ 1 request, 1 table
dashboard = table.query(KeyConditionExpression=Key("PK").eq("USER#1"))
```

**Anti-pattern 5: Không handle throttling**

```python
# ❌ Crash khi bị throttled
table.put_item(Item=item)  # ProvisionedThroughputExceededException!

# ✅ Retry với exponential backoff
import boto3
config = botocore.config.Config(
    retries={"max_attempts": 10, "mode": "adaptive"}
)
```

  
**Q19. \[System Design\] Thiết kế schema DynamoDB cho hệ thống e-commerce: Users, Products, Orders, Reviews. Access patterns cụ thể:**

1.  Get user profile
    
2.  List orders of user (newest first)
    
3.  Get order detail
    
4.  List products by category sorted by price
    
5.  List reviews of product (newest first)
    

Đáp án mong đợi:

**Access Pattern → Key Mapping:**


| Pattern | Table/Index | PK | SK |
|---|---|---|---|
| Get user profile | Main | USER#{userId} | PROFILE |
| List orders of user | Main | USER#{userId} | begins_with ORDER# + sort by date |
| Get order detail | GSI1 | ORDER#{orderId} | METADATA |
| List products by category | GSI1 | PROD_CAT#{category} | PRICE#{price} |
| List reviews of product | Main | PRODUCT#{productId} | begins_with REVIEW# |



**Schema:**

```python
# User profile
{"PK": "USER#1", "SK": "PROFILE",
 "GSI1PK": "USER_EMAIL", "GSI1SK": "nam@gmail.com"}

# Order (SK có date để sort theo thời gian)
{"PK": "USER#1", "SK": "ORDER#2025-03-15T10:00:00#ORD001",
 "GSI1PK": "ORDER#ORD001", "GSI1SK": "METADATA",
 "GSI2PK": "ORDER_STATUS#PAID", "GSI2SK": "2025-03-15T10:00:00"}

# Product
{"PK": "PRODUCT#1", "SK": "METADATA",
 "GSI1PK": "PROD_CAT#electronics",
 "GSI1SK": f"PRICE#{Decimal('15000000'):015.2f}"}  # zero-padded để sort đúng

# Review
{"PK": "PRODUCT#1", "SK": f"REVIEW#{datetime.now().isoformat()}#USER#1",
 "userId": "1", "rating": Decimal("4.5")}
```

✅ Senior indicator: Zero-padded price để sort string đúng, SK encoding có timestamp cho ordering

  
**Q20. TTL trong DynamoDB hoạt động như thế nào? Khác gì với Cassandra TTL?**

Đáp án mong đợi:

**DynamoDB TTL:**

*   Enable TTL trên table: chỉ định attribute name chứa Unix timestamp
    
*   Items tự xóa khi `expires_at < current_time`
    
*   TTL background job chạy trong vòng 48 giờ sau khi expire (không real-time)
    
*   **Không tốn WCU** khi xóa bởi TTL
    

```python
import time

# Enable TTL
dynamodb.meta.client.update_time_to_live(
    TableName="FoxDev",
    TimeToLiveSpecification={
        "Enabled": True,
        "AttributeName": "expiresAt"   # tên attribute
    }
)

# Tạo item với TTL
table.put_item(Item={
    "PK": f"SESSION#{session_id}",
    "SK": "DATA",
    "userId": user_id,
    "expiresAt": int(time.time()) + 86400  # Unix timestamp, 24 giờ
})
```

**So sánh với Cassandra:**


|  | DynamoDB TTL | Cassandra TTL |
|---|---|---|
| Cú pháp | Attribute value (Unix timestamp) | USING TTL seconds |
| Precision | Seconds | Seconds |
| Deletion timing | Trong vòng 48h | Gần chính xác hơn |
| WCU cost | Free | N/A |
| Tombstones | Không tạo tombstones | Tạo tombstones |



  
**Q21. DynamoDB Adaptive Capacity là gì? Liên quan gì đến hot partition?**

Đáp án mong đợi:

*   **Adaptive Capacity** (DynamoDB tự động): tự phân bổ capacity cho partitions bị overloaded
    
*   Khi 1 partition nhận nhiều traffic hơn các partitions khác → DynamoDB tự tăng capacity cho partition đó
    
*   Không cần config, hoạt động trong nền
    

**Tuy nhiên có giới hạn:**

*   Adaptive capacity có trần = total table capacity
    
*   Nếu throughput vượt cả table capacity → vẫn bị throttle
    
*   On-demand mode: adaptive capacity linh hoạt hơn nhiều
    

**Vẫn cần tránh hot partition vì:**

*   Adaptive capacity có delay (không instant)
    
*   On-demand mode vẫn có burst limit
    
*   Best practice: distribute writes across partitions naturally
    

* * *

## 🔴 SENIOR / PRINCIPAL (7+ năm)

  
**Q22. \[System Design\]** [**nguyentienkhoi.hashnode.dev**](http://nguyentienkhoi.hashnode.dev) **đang dùng RDS PostgreSQL. Khi nào bạn đề xuất migrate sang DynamoDB? Làm thế nào migrate zero downtime?**

Đáp án mong đợi:

**Khi nào migrate:**

*   Traffic growth → RDS không scale đủ (vertical scale limit)
    
*   Serverless architecture → Lambda + DynamoDB natural fit
    
*   Multi-region requirement → DynamoDB Global Tables
    
*   Cost optimization với predictable read-heavy traffic
    

**Khi nào KHÔNG migrate:**

*   Complex reporting queries (GROUP BY, multi-table JOIN)
    
*   Team không quen DynamoDB — learning curve
    
*   Data model thay đổi thường xuyên — DynamoDB schema cứng hơn
    

**Kế hoạch migration zero downtime:**

```java
Phase 1: Design & Benchmark (2 tuần)
  → Access pattern analysis
  → DynamoDB schema design (Single Table)
  → Load test với production-like data

Phase 2: Dual-write (3 tuần)
  → Application writes vào cả PostgreSQL và DynamoDB
  → Reads vẫn từ PostgreSQL
  → Monitor: DynamoDB performance, data consistency

Phase 3: Backfill (1 tuần)
  → Migrate historical data
  → Rate limit: 5,000 items/giây để tránh throttle
  → Verify: row counts, sample checks

Phase 4: Gradual read switch
  → 5% → 25% → 50% → 100% reads từ DynamoDB
  → Monitor: latency, error rate
  → Feature flag để quick rollback

Phase 5: Cleanup
  → Remove PostgreSQL writes sau 2 tuần stable
  → Archive PostgreSQL data
```

✅ Senior indicator: Dual-write period, feature flags, gradual rollout, rollback strategy

  
**Q23. Giải thích DynamoDB Global Tables. Khi nào cần và conflict resolution hoạt động thế nào?**

Đáp án mong đợi:

**Global Tables:**

*   Multi-region active-active replication
    
*   Mỗi region có full copy của data
    
*   Tự động replicate writes across regions (~1 giây lag)
    
*   Read từ local region → single-digit ms latency toàn cầu
    

**Khi nào cần:**

*   App có users ở nhiều countries → latency quan trọng
    
*   Disaster recovery: region down → traffic route sang region khác
    
*   Data residency requirements
    

**Conflict Resolution:**

*   "Last writer wins" — dựa trên timestamp
    
*   DynamoDB dùng write timestamp để resolve conflicts khi cùng item được ghi ở 2 regions trong cùng thời điểm
    
*   Không có manual conflict resolution
    

**Giới hạn:**

*   Tables phải dùng On-demand hoặc Provisioned với Auto Scaling
    
*   TransactWrite không được cross-region
    
*   Có thể có ~1 giây replication lag — eventual consistency
    

  
**Q24. \[Trade-off\] Bạn review một PR: developer dùng DynamoDB Scan với FilterExpression để list tất cả PAID orders cho admin dashboard. Traffic thấp (~10 req/ngày). Bạn approve không?**

Câu hỏi open-ended — đánh giá tư duy:

**Arguments for approve (trong context này):**

*   Traffic rất thấp (10 req/ngày admin) → cost impact negligible
    
*   Implementation đơn giản, ít code hơn
    
*   Admin dashboard không cần ultra-low latency
    
*   Premature optimization là bad engineering
    

**Arguments against:**

*   Scan đọc toàn bộ table → grows linearly với data
    
*   Khi table lớn (millions items) → scan slow + expensive
    
*   Technical debt — khó fix sau khi đang dùng production
    
*   "Traffic thấp hôm nay" không đảm bảo tương lai
    

**Senior answer (balanced):**

*   Short term: có thể accept với **comment ghi rõ** đây là known issue
    
*   Medium term: create GSI `ORDER_STATUS#{status}` + `{date}` SK
    
*   Điều kiện accept: add TODO comment, create tech debt ticket, review lại khi table > 100K items
    
*   Không phải binary accept/reject — depends on context
    

✅ Senior indicator: Không dogmatic, cân nhắc pragmatism vs correctness, biết khi nào trade-off chấp nhận được

  
**Q25. Thiết kế Rate Limiting system dùng DynamoDB. Tại sao DynamoDB không phải lựa chọn tốt nhất cho use case này?**

Đáp án mong đợi:

**Implement với DynamoDB:**

```python
import time
from decimal import Decimal
from botocore.exceptions import ClientError

def check_rate_limit_dynamo(table, user_id: str,
                              limit: int = 100,
                              window_secs: int = 60) -> bool:
    window    = int(time.time() // window_secs) * window_secs
    pk        = f"RATE_LIMIT#{user_id}"
    sk        = f"WINDOW#{window}"
    expires_at = window + window_secs + 60

    try:
        resp = table.update_item(
            Key={"PK": pk, "SK": sk},
            UpdateExpression="ADD #count :one SET expiresAt = :exp",
            ConditionExpression=Attr("#count").lt(limit) | Attr("#count").not_exists(),
            ExpressionAttributeNames={"#count": "count"},
            ExpressionAttributeValues={
                ":one": 1,
                ":exp": expires_at,
                ":limit": limit
            },
            ReturnValues="UPDATED_NEW"
        )
        return True
    except ClientError as e:
        if e.response["Error"]["Code"] == "ConditionalCheckFailedException":
            return False  # rate limit exceeded
        raise
```

**Tại sao DynamoDB không phải lựa chọn tốt nhất:**

*   **Latency**: DynamoDB ~1-10ms per request. Redis ~0.1ms. Rate limiter gọi mỗi API request → thêm 1-10ms latency cho mọi request
    
*   **Cost**: Rate limit check = 1 WCU per request. 1M API requests/ngày = 1M WCU/ngày = ~$1.25/ngày chỉ cho rate limiting
    
*   **Complexity**: Conditional update với ADD + TTL phức tạp hơn Redis INCR + EXPIRE
    
*   **Atomic concern**: DynamoDB conditional write đủ atomic, nhưng không elegant như Redis single-threaded INCR
    

**Khi nào DynamoDB cho rate limiting acceptable:**

*   Đang hoàn toàn trên AWS serverless, không có Redis
    
*   Rate limit không cần sub-millisecond (admin API, webhook endpoints)
    
*   Cost không phải concern
    

**Best practice:** Redis cho rate limiting, DynamoDB cho persistent business data.

* * *

## Bảng Điểm Đánh Giá


| Level | Câu hỏi | Pass khi |
|---|---|---|
| Junior | Q1–Q9 | Pass 7/9, bắt buộc Q2 (PK/SK), Q3 (Query vs Scan), Q6 (FilterExpression không giảm RCU) |
| Intermediate | Q10–Q16 | Pass 5/7, bắt buộc Q10 (Single Table Design), Q13 (Conditional Write), Q16 (coding) |
| Advanced | Q17–Q21 | Pass 4/5, bắt buộc Q18 (anti-patterns), Q19 (system design) |
| Senior | Q22–Q25 | Pass 3/4, đặc biệt Q22 (migration planning), Q24 (trade-off thinking) |



* * *

## Câu Hỏi Bẫy Hay Dùng

**Bẫy 1:** "FilterExpression giúp giảm chi phí đọc?" → Sai. FilterExpression chạy sau khi DynamoDB đã đọc items từ disk. RCU được tính cho tất cả items đọc, dù bị filter out. Chỉ KeyConditionExpression mới giảm items đọc thực sự.

**Bẫy 2:** "DynamoDB transactions giống PostgreSQL transactions?" → Không. DynamoDB TransactWrite: tối đa 100 items, tốn 2x WCU, cùng region, không có isolation levels. PostgreSQL: full ACID với isolation, row-level locking, rollback hoàn toàn.

**Bẫy 3:** "Càng nhiều GSI càng tốt?" → Sai. Mỗi GSI tốn thêm storage và WCU (mỗi write vào main table cũng write vào tất cả GSIs). Tối đa 20 GSI per table, nhưng best practice là ít GSI + overloading pattern.

**Bẫy 4:** "Partition Key là khái niệm giống Partition Key trong Cassandra?" → Tương tự về mục đích (xác định node) nhưng khác về behavior. DynamoDB không expose Partition Key hash, không cho phép range query trên PK (chỉ equality). Cassandra composite partition key không có tương đương trực tiếp trong DynamoDB.

**Bẫy 5:** "DynamoDB TTL xóa item ngay khi expire?" → Không. DynamoDB TTL background job có thể delay lên đến 48 giờ. Items "expired" có thể vẫn xuất hiện trong reads — application phải filter ra. Cassandra TTL chính xác hơn.

**Bẫy 6:** "Single Table Design dùng được cho mọi use case?" → Không. Single Table Design tốt khi access patterns rõ ràng và ổn định. Khi access patterns thay đổi thường xuyên hoặc cần ad-hoc queries, multi-table hoặc relational DB tốt hơn.

