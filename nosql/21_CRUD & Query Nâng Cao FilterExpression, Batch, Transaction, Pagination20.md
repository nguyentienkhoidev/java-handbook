# CRUD & Query Nâng Cao: FilterExpression, Batch, Transaction, Pagination

![CRUD & Query Nâng Cao- FilterExpression, Batch, Transaction, Pagination.jpeg](../images/db95f588-76da-4b6f-aeff-3fd49784a989.jpeg)

Bài trước chúng ta đã thiết kế xong schema. Bài này đi sâu vào **operations nâng cao** mà mọi production app đều cần: FilterExpression để lọc sau khi read, BatchWrite để ghi hàng loạt, TransactWrite để đảm bảo atomic operations, và Pagination để xử lý kết quả lớn không bị timeout.

## 1\. Expression System — Nền Tảng Của Mọi Operation

DynamoDB dùng **Expression** thay vì SQL — hiểu rõ hệ thống này giúp viết query hiệu quả hơn.

```python
from boto3.dynamodb.conditions import Key, Attr
from decimal import Decimal

dynamodb = boto3.resource(
    "dynamodb",
    endpoint_url          = "http://localhost:8000",
    region_name           = "us-east-1",
    aws_access_key_id     = "fake",
    aws_secret_access_key = "fake"
)
table = dynamodb.Table("FoxDev")

# Expression types:
# KeyConditionExpression → chỉ cho Query, dùng Key()
# FilterExpression       → filter SAU khi đọc, dùng Attr()
# ConditionExpression    → điều kiện cho Put/Update/Delete
# UpdateExpression       → chỉ định field nào update
# ProjectionExpression   → chọn fields trả về
```

## 2\. FilterExpression — Lọc Sau Khi Đọc

```python
# ⚠️ Quan trọng: FilterExpression KHÔNG giảm RCU consumed
# DynamoDB đọc tất cả items thỏa KeyCondition TRƯỚC,
# rồi mới apply FilterExpression → vẫn tốn RCU cho items bị filter out

# ─── Filter trên enrollment ───
# Lấy enrollments của user đã complete
resp = table.query(
    KeyConditionExpression = (
        Key("PK").eq("USER#1") &
        Key("SK").begins_with("ENROLLMENT#")
    ),
    FilterExpression = Attr("completed").eq(True)
)
completed = resp["Items"]
print(f"Completed courses: {len(completed)}")


# ─── Nhiều conditions ───
resp = table.query(
    KeyConditionExpression = (
        Key("PK").eq("USER#1") &
        Key("SK").begins_with("ENROLLMENT#")
    ),
    FilterExpression = (
        Attr("completed").eq(False) &
        Attr("progress").gt(Decimal("0.5"))
    )
)
in_progress = resp["Items"]
print(f"In progress (>50%): {len(in_progress)}")


# ─── Filter operators ───
from boto3.dynamodb.conditions import Attr

Attr("price").eq(Decimal("799000"))          # ==
Attr("price").ne(Decimal("0"))               # !=
Attr("price").lt(Decimal("800000"))          # <
Attr("price").lte(Decimal("800000"))         # <=
Attr("price").gt(Decimal("500000"))          # >
Attr("price").gte(Decimal("500000"))         # >=
Attr("price").between(Decimal("500000"), Decimal("900000"))  # BETWEEN
Attr("status").is_in(["ACTIVE", "PUBLISHED"])  # IN
Attr("email").begins_with("nam")             # begins_with
Attr("title").contains("Spring")            # contains
Attr("phone").exists()                      # attribute_exists
Attr("phone").not_exists()                  # attribute_not_exists
```

## 3\. ProjectionExpression — Chỉ Lấy Fields Cần

```python
# ─── Lấy chỉ title và price ───
resp = table.query(
    IndexName = "GSI1",
    KeyConditionExpression = (
        Key("GSI1PK").eq("COURSE_CAT#java") &
        Key("GSI1SK").begins_with("RATING#")
    ),
    ProjectionExpression = "courseId, title, price, rating",
    ScanIndexForward     = False
)
courses = resp["Items"]

# ─── Tránh reserved words ───
# "status", "name", "data" là reserved words trong DynamoDB
# → Phải dùng ExpressionAttributeNames để alias

resp = table.query(
    KeyConditionExpression = Key("PK").eq("USER#1"),
    FilterExpression       = Attr("#s").eq("ACTIVE"),
    ProjectionExpression   = "userId, email, #s",
    ExpressionAttributeNames = {
        "#s": "status"    # alias cho reserved word
    }
)

# Tip: Luôn dùng ExpressionAttributeNames nếu không chắc
resp = table.query(
    KeyConditionExpression = Key("PK").eq("USER#1") & Key("SK").eq("PROFILE"),
    ProjectionExpression   = "#n, #e, #s",
    ExpressionAttributeNames = {
        "#n": "firstName",
        "#e": "email",
        "#s": "status"
    }
)
```

## 4\. UpdateExpression — Update Chính Xác

```python
# ─── SET — đặt giá trị ───
table.update_item(
    Key = {"PK": "USER#1", "SK": "ENROLLMENT#COURSE#1"},
    UpdateExpression = "SET progress = :p, updatedAt = :t",
    ExpressionAttributeValues = {
        ":p": Decimal("0.9"),
        ":t": datetime.now().isoformat()
    }
)

# ─── ADD — tăng số hoặc thêm vào Set ───
# ADD cho Number: tương tự INCR trong Redis
table.update_item(
    Key = {"PK": "COURSE#1", "SK": "METADATA"},
    UpdateExpression = "ADD enrolledCount :one",
    ExpressionAttributeValues = {":one": 1}
)

# ADD cho Set: thêm phần tử vào SS/NS/BS
table.update_item(
    Key = {"PK": "COURSE#1", "SK": "METADATA"},
    UpdateExpression = "ADD tags :new_tags",
    ExpressionAttributeValues = {
        ":new_tags": {"microservices", "cloud"}  # String Set
    }
)

# ─── REMOVE — xóa attribute hoặc phần tử khỏi List ───
table.update_item(
    Key = {"PK": "USER#1", "SK": "PROFILE"},
    UpdateExpression = "REMOVE oldField, anotherField"
)

# Xóa phần tử khỏi List theo index
table.update_item(
    Key = {"PK": "ORDER#ORD001", "SK": "METADATA"},
    UpdateExpression = "REMOVE #items[0]",  # xóa phần tử đầu
    ExpressionAttributeNames = {"#items": "items"}
)

# ─── DELETE — xóa phần tử khỏi Set ───
table.update_item(
    Key = {"PK": "COURSE#1", "SK": "METADATA"},
    UpdateExpression = "DELETE tags :old_tag",
    ExpressionAttributeValues = {":old_tag": {"old_tag"}}
)

# ─── Kết hợp nhiều expressions ───
table.update_item(
    Key = {"PK": "USER#1", "SK": "ENROLLMENT#COURSE#1"},
    UpdateExpression = (
        "SET progress = :p, completed = :c, completedAt = :t "
        "ADD #wc :one"
    ),
    ExpressionAttributeNames = {"#wc": "watchCount"},
    ExpressionAttributeValues = {
        ":p":  Decimal("1.0"),
        ":c":  True,
        ":t":  datetime.now().isoformat(),
        ":one": 1
    }
)
```

## 5\. Conditional Writes — Atomic Check + Write

```python
from botocore.exceptions import ClientError

# ─── Put only if not exists ───
def create_user_if_not_exists(table, user: dict) -> bool:
    try:
        table.put_item(
            Item = {
                "PK": f"USER#{user['userId']}",
                "SK": "PROFILE",
                **user
            },
            ConditionExpression = Attr("PK").not_exists()
            # Chỉ insert nếu PK chưa tồn tại
        )
        return True
    except ClientError as e:
        if e.response["Error"]["Code"] == "ConditionalCheckFailedException":
            return False  # User đã tồn tại
        raise


# ─── Update chỉ khi version khớp (Optimistic Locking) ───
def update_course_optimistic(table, course_id: str,
                              new_price: Decimal,
                              current_version: int) -> bool:
    try:
        table.update_item(
            Key = {
                "PK": f"COURSE#{course_id}",
                "SK": "METADATA"
            },
            UpdateExpression = "SET price = :p, #v = :new_v",
            ConditionExpression = "#v = :cur_v",  # phải đúng version
            ExpressionAttributeNames = {"#v": "version"},
            ExpressionAttributeValues = {
                ":p":     new_price,
                ":new_v": current_version + 1,
                ":cur_v": current_version
            }
        )
        return True
    except ClientError as e:
        if e.response["Error"]["Code"] == "ConditionalCheckFailedException":
            return False  # Version conflict
        raise


# ─── Delete chỉ khi status = CANCELLED ───
def delete_cancelled_order(table, user_id: str,
                             order_sk: str) -> bool:
    try:
        table.delete_item(
            Key = {"PK": f"USER#{user_id}", "SK": order_sk},
            ConditionExpression = Attr("#s").eq("CANCELLED"),
            ExpressionAttributeNames = {"#s": "status"}
        )
        return True
    except ClientError as e:
        if e.response["Error"]["Code"] == "ConditionalCheckFailedException":
            return False
        raise
```

## 6\. BatchWrite — Ghi Hàng Loạt

```python
# BatchWriteItem: tối đa 25 items per batch
# Không phải atomic — một số items có thể fail

def batch_write_items(table, items: list):
    """
    Batch write với auto-retry cho unprocessed items.
    DynamoDB có thể trả về UnprocessedItems khi throttled.
    """
    # Chia thành batches 25 items
    batch_size = 25
    total_written = 0

    for i in range(0, len(items), batch_size):
        batch = items[i:i + batch_size]

        with table.batch_writer() as writer:
            for item in batch:
                writer.put_item(Item=item)

        total_written += len(batch)
        print(f"Written {total_written}/{len(items)}")

    return total_written


# Batch write nhiều courses cùng lúc
courses_to_seed = [
    {
        "PK": f"COURSE#{i}", "SK": "METADATA",
        "courseId": str(i),
        "title": f"Course {i}",
        "category": "java",
        "price": Decimal("599000"),
        "rating": Decimal("4.5"),
        "status": "PUBLISHED",
        "GSI1PK": "COURSE_CAT#java",
        "GSI1SK": f"RATING#04.50",
    }
    for i in range(5, 20)
]

batch_write_items(table, courses_to_seed)


# BatchGet: lấy nhiều items cùng lúc (tối đa 100)
def batch_get_users(table, user_ids: list) -> list:
    """Lấy nhiều users trong 1 request"""
    keys = [
        {"PK": {"S": f"USER#{uid}"}, "SK": {"S": "PROFILE"}}
        for uid in user_ids
    ]

    client = table.meta.client
    resp   = client.batch_get_item(
        RequestItems = {
            "FoxDev": {"Keys": keys}
        }
    )

    return resp["Responses"].get("FoxDev", [])


users = batch_get_users(table, ["1", "2", "3"])
print(f"Got {len(users)} users")
```

## 7\. TransactWrite — Atomic Multi-item Operations

```python
# TransactWrite: tối đa 100 items, ALL or NOTHING
# Dùng khi: tạo order phải đồng thời tạo enrollment

def create_order_and_enrollment(
    table,
    user_id:   str,
    course_id: str,
    order_id:  str,
    amount:    Decimal
) -> bool:
    """
    Atomic: tạo Order + Enrollment cùng lúc.
    Nếu 1 trong 2 fail → cả 2 rollback.
    """
    client     = table.meta.client
    created_at = datetime.now().isoformat()

    try:
        client.transact_write_items(
            TransactItems = [
                # 1. Tạo Order
                {
                    "Put": {
                        "TableName": "FoxDev",
                        "Item": {
                            "PK":        {"S": f"USER#{user_id}"},
                            "SK":        {"S": f"ORDER#{created_at}#{order_id}"},
                            "GSI1PK":   {"S": f"ORDER#{order_id}"},
                            "GSI1SK":   {"S": "METADATA"},
                            "GSI2PK":   {"S": "ORDER_STATUS#PAID"},
                            "GSI2SK":   {"S": created_at},
                            "entityType": {"S": "ORDER"},
                            "orderId":  {"S": order_id},
                            "userId":   {"S": user_id},
                            "courseId": {"S": course_id},
                            "status":   {"S": "PAID"},
                            "amount":   {"N": str(amount)},
                            "createdAt": {"S": created_at},
                        },
                        # Điều kiện: order chưa tồn tại
                        "ConditionExpression": "attribute_not_exists(PK)"
                    }
                },
                # 2. Tạo Enrollment
                {
                    "Put": {
                        "TableName": "FoxDev",
                        "Item": {
                            "PK":       {"S": f"USER#{user_id}"},
                            "SK":       {"S": f"ENROLLMENT#COURSE#{course_id}"},
                            "GSI1PK":  {"S": f"COURSE#{course_id}"},
                            "GSI1SK":  {"S": f"ENROLLMENT#USER#{user_id}"},
                            "entityType": {"S": "ENROLLMENT"},
                            "userId":   {"S": user_id},
                            "courseId": {"S": course_id},
                            "progress": {"N": "0"},
                            "completed": {"BOOL": False},
                            "enrolledAt": {"S": created_at},
                        },
                        # Điều kiện: chưa enrolled
                        "ConditionExpression": "attribute_not_exists(PK)"
                    }
                },
                # 3. Increment enrolledCount của Course
                {
                    "Update": {
                        "TableName": "FoxDev",
                        "Key": {
                            "PK": {"S": f"COURSE#{course_id}"},
                            "SK": {"S": "METADATA"}
                        },
                        "UpdateExpression": "ADD enrolledCount :one",
                        "ExpressionAttributeValues": {
                            ":one": {"N": "1"}
                        }
                    }
                }
            ]
        )
        print(f"✅ Order {order_id} created + User {user_id} enrolled in Course {course_id}")
        return True

    except ClientError as e:
        if e.response["Error"]["Code"] == "TransactionCanceledException":
            reasons = e.response.get("CancellationReasons", [])
            for i, reason in enumerate(reasons):
                if reason["Code"] != "None":
                    print(f"  Item {i} failed: {reason['Code']} — {reason.get('Message')}")
        return False


# Test
create_order_and_enrollment(
    table, user_id="3", course_id="2",
    order_id="ORD004", amount=Decimal("599000")
)
```

## 8\. Pagination

```python
# DynamoDB trả về tối đa 1MB data per request
# Nếu có nhiều hơn → trả về LastEvaluatedKey để fetch tiếp

def query_all_pages(table, key_condition, **kwargs) -> list:
    """
    Tự động paginate cho đến khi lấy hết data.
    """
    items          = []
    last_key       = None

    while True:
        params = {
            "KeyConditionExpression": key_condition,
            **kwargs
        }
        if last_key:
            params["ExclusiveStartKey"] = last_key

        resp     = table.query(**params)
        items.extend(resp["Items"])
        last_key = resp.get("LastEvaluatedKey")

        if not last_key:
            break

    return items


# Lấy tất cả enrollments của user
all_enrollments = query_all_pages(
    table,
    Key("PK").eq("USER#1") & Key("SK").begins_with("ENROLLMENT#")
)

# ─── API Pagination (cursor-based) ───
def query_page(table, user_id: str,
               page_size: int = 10,
               cursor: str = None) -> dict:
    """
    Trả về 1 page và cursor cho page tiếp theo.
    cursor là base64 encoded LastEvaluatedKey.
    """
    import base64, json

    params = {
        "KeyConditionExpression": (
            Key("PK").eq(f"USER#{user_id}") &
            Key("SK").begins_with("ENROLLMENT#")
        ),
        "Limit":         page_size,
        "ScanIndexForward": False
    }

    if cursor:
        last_key       = json.loads(base64.b64decode(cursor).decode())
        params["ExclusiveStartKey"] = last_key

    resp  = table.query(**params)
    items = resp["Items"]

    next_cursor = None
    if "LastEvaluatedKey" in resp:
        next_cursor = base64.b64encode(
            json.dumps(resp["LastEvaluatedKey"]).encode()
        ).decode()

    return {
        "items":      items,
        "count":      len(items),
        "nextCursor": next_cursor,
        "hasMore":    next_cursor is not None
    }


# Page 1
page1 = query_page(table, "1", page_size=2)
print(f"Page 1: {len(page1['items'])} items, hasMore={page1['hasMore']}")

# Page 2 (dùng cursor từ page 1)
if page1["nextCursor"]:
    page2 = query_page(table, "1", page_size=2, cursor=page1["nextCursor"])
    print(f"Page 2: {len(page2['items'])} items")
```

## 9\. Parallel Scan — Tăng Tốc Scan

```python
import threading

def parallel_scan(table, total_segments: int = 4) -> list:
    """
    Chia scan thành N segments chạy song song.
    Mỗi segment là 1 thread độc lập.
    Tăng throughput khi cần scan toàn bộ table.
    """
    results       = [[] for _ in range(total_segments)]
    errors        = []

    def scan_segment(segment_idx: int):
        try:
            last_key = None
            while True:
                params = {
                    "Segment":      segment_idx,
                    "TotalSegments": total_segments,
                }
                if last_key:
                    params["ExclusiveStartKey"] = last_key

                resp     = table.scan(**params)
                results[segment_idx].extend(resp["Items"])
                last_key = resp.get("LastEvaluatedKey")
                if not last_key:
                    break
        except Exception as e:
            errors.append(e)

    # Chạy parallel
    threads = [
        threading.Thread(target=scan_segment, args=(i,))
        for i in range(total_segments)
    ]
    for t in threads:
        t.start()
    for t in threads:
        t.join()

    if errors:
        raise errors[0]

    # Merge tất cả results
    all_items = []
    for segment_results in results:
        all_items.extend(segment_results)

    return all_items


all_items = parallel_scan(table, total_segments=4)
print(f"Total items in table: {len(all_items)}")
```

## 10\. Thực Hành Tổng Hợp

```python
class FoxDevRepository:
    """Repository pattern cho DynamoDB Single Table Design"""

    def __init__(self, table_name: str = "FoxDev"):
        self.table = dynamodb.Table(table_name)

    # ─── User ───
    def get_user(self, user_id: str) -> dict | None:
        resp = self.table.get_item(
            Key={"PK": f"USER#{user_id}", "SK": "PROFILE"},
            ProjectionExpression="userId, email, firstName, lastName, #s",
            ExpressionAttributeNames={"#s": "status"}
        )
        return resp.get("Item")

    def get_user_dashboard(self, user_id: str) -> dict:
        """Profile + enrollments + last 5 orders trong 1 request"""
        resp  = self.table.query(
            KeyConditionExpression = Key("PK").eq(f"USER#{user_id}"),
            ScanIndexForward       = False
        )
        profile, enrollments, orders = None, [], []

        for item in resp["Items"]:
            sk = item["SK"]
            if sk == "PROFILE":
                profile = item
            elif sk.startswith("ENROLLMENT#"):
                enrollments.append(item)
            elif sk.startswith("ORDER#"):
                orders.append(item)

        return {
            "profile":     profile,
            "enrollments": enrollments,
            "orders":      orders[:5],
            "stats": {
                "courses_enrolled": len(enrollments),
                "courses_completed": sum(1 for e in enrollments if e.get("completed")),
                "total_orders":     len(orders),
            }
        }

    # ─── Course ───
    def list_courses_by_category(self, category: str,
                                  limit: int = 20) -> list:
        resp = self.table.query(
            IndexName = "GSI1",
            KeyConditionExpression = (
                Key("GSI1PK").eq(f"COURSE_CAT#{category}") &
                Key("GSI1SK").begins_with("RATING#")
            ),
            ProjectionExpression = "courseId, title, price, rating, enrolledCount",
            ScanIndexForward     = False,
            Limit                = limit
        )
        return resp["Items"]

    # ─── Enrollment ───
    def enroll_user(self, user_id: str,
                     course_id: str, order_id: str,
                     amount: Decimal) -> bool:
        return create_order_and_enrollment(
            self.table, user_id, course_id, order_id, amount
        )

    def update_progress(self, user_id: str,
                         course_id: str, progress: float):
        self.table.update_item(
            Key = {
                "PK": f"USER#{user_id}",
                "SK": f"ENROLLMENT#COURSE#{course_id}"
            },
            UpdateExpression = (
                "SET progress = :p, completed = :c, updatedAt = :t"
            ),
            ExpressionAttributeValues = {
                ":p": Decimal(str(progress)),
                ":c": progress >= 1.0,
                ":t": datetime.now().isoformat()
            }
        )


# Test
repo = FoxDevRepository()

dashboard = repo.get_user_dashboard("1")
print(f"User: {dashboard['profile']['firstName']}")
print(f"Stats: {dashboard['stats']}")

java_courses = repo.list_courses_by_category("java")
print(f"Java courses: {[c['title'] for c in java_courses]}")

repo.update_progress("1", "1", 0.95)
print("Progress updated to 95%")
```

## Tổng Kết


| Operation | Method | Notes |
|---|---|---|
| FilterExpression | Attr() operators | Tốn RCU ngay cả khi bị filter |
| ProjectionExpression | Field names | Giảm data transfer, không giảm RCU |
| ExpressionAttributeNames | #alias | Bắt buộc với reserved words |
| UpdateExpression SET | Đặt giá trị | Tương tự UPDATE SET |
| UpdateExpression ADD | Tăng số/thêm Set | Atomic increment |
| ConditionExpression | Check trước write | Optimistic locking |
| BatchWriteItem | 25 items/batch | Không atomic |
| TransactWrite | 100 items max | All or nothing |
| Pagination | LastEvaluatedKey | Always handle! |
| Parallel Scan | Segments | Tăng scan throughput |



Bài tiếp theo chúng ta sẽ học **GSI thực chiến, Conditional Write, Transactions và DynamoDB Streams** — những features nâng cao nhất của DynamoDB.

