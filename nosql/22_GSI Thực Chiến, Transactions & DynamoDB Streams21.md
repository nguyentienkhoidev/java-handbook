# GSI Thực Chiến, Transactions & DynamoDB Streams

![GSI Thực Chiến, Transactions & DynamoDB Streams.jpeg](../images/e6bf74e1-3cc5-44bc-924f-bdbda3663fc0.jpeg)

Bài trước chúng ta đã cover các operation cơ bản. Bài này đi sâu vào 3 features nâng cao mà production app không thể thiếu: **GSI thực chiến** với overloading patterns, **Transactions** cho business-critical operations và **DynamoDB Streams** để trigger events khi data thay đổi.

## 1\. GSI Thực Chiến — Global Secondary Index

### 1.1 Khi Nào Cần GSI?

```java
Main table Primary Key (PK + SK) → phục vụ access patterns theo PK
GSI                              → phục vụ access patterns khác

Ví dụ:
  Main: PK=USER#1, SK=PROFILE → get user by userId ✅
  GSI1: GSI1PK=USER_EMAIL, GSI1SK=email → get user by email ✅

  Nếu không có GSI:
  → get user by email phải Scan toàn table → chậm + tốn tiền
```

### 1.2 GSI Overloading — 1 GSI Nhiều Purposes

Tao đã giới thiệu ở Bài 17. Đây là phân tích chi tiết:

```python
import boto3
from boto3.dynamodb.conditions import Key, Attr
from decimal import Decimal
from datetime import datetime

dynamodb = boto3.resource(
    "dynamodb",
    endpoint_url          = "http://localhost:8000",
    region_name           = "us-east-1",
    aws_access_key_id     = "fake",
    aws_secret_access_key = "fake"
)
table = dynamodb.Table("FoxDev")

# ─── GSI1 phục vụ nhiều patterns ───

# Pattern 1: Get user by email
# GSI1PK = "USER_EMAIL"
# GSI1SK = "nam@gmail.com"
def get_user_by_email(email: str):
    resp = table.query(
        IndexName = "GSI1",
        KeyConditionExpression = (
            Key("GSI1PK").eq("USER_EMAIL") &
            Key("GSI1SK").eq(email)
        )
    )
    items = resp["Items"]
    return items[0] if items else None


# Pattern 2: List courses by category sorted by rating
# GSI1PK = "COURSE_CAT#java"
# GSI1SK = "RATING#04.80"
def list_courses_by_category(category: str, min_rating: float = 0):
    sk_condition = Key("GSI1SK").begins_with("RATING#")
    if min_rating > 0:
        # Rating được pad: "04.80" → sort string đúng
        min_padded = f"RATING#{min_rating:06.2f}"
        sk_condition = Key("GSI1SK").gte(min_padded)

    resp = table.query(
        IndexName = "GSI1",
        KeyConditionExpression = (
            Key("GSI1PK").eq(f"COURSE_CAT#{category}") &
            sk_condition
        ),
        ScanIndexForward = False  # high rating first
    )
    return resp["Items"]


# Pattern 3: Get all students of a course
# GSI1PK = "COURSE#1"
# GSI1SK = "ENROLLMENT#USER#..."
def get_course_students(course_id: str):
    resp = table.query(
        IndexName = "GSI1",
        KeyConditionExpression = (
            Key("GSI1PK").eq(f"COURSE#{course_id}") &
            Key("GSI1SK").begins_with("ENROLLMENT#")
        )
    )
    return resp["Items"]


# Pattern 4: Get order by orderId
# GSI1PK = "ORDER#ORD001"
# GSI1SK = "METADATA"
def get_order_by_id(order_id: str):
    resp = table.query(
        IndexName = "GSI1",
        KeyConditionExpression = (
            Key("GSI1PK").eq(f"ORDER#{order_id}") &
            Key("GSI1SK").eq("METADATA")
        )
    )
    items = resp["Items"]
    return items[0] if items else None
```

### 1.3 Sparse GSI — Index Chỉ Subset Items

```python
# Sparse GSI: chỉ items có attribute đó mới được index
# Ví dụ: chỉ index courses được "featured" (không phải tất cả)

# Khi tạo course thường: KHÔNG set featuredGSIPK → không vào GSI
# Khi tạo featured course: SET featuredGSIPK → vào GSI

def feature_course(course_id: str, feature_order: int):
    """Đánh dấu course là featured — tự động vào Sparse GSI"""
    table.update_item(
        Key = {"PK": f"COURSE#{course_id}", "SK": "METADATA"},
        UpdateExpression = "SET GSI2PK = :gpk, GSI2SK = :gsk",
        ExpressionAttributeValues = {
            ":gpk": "FEATURED_COURSE",
            ":gsk": f"ORDER#{feature_order:04d}"
        }
    )


def unfeature_course(course_id: str):
    """Bỏ featured — tự động ra khỏi Sparse GSI"""
    table.update_item(
        Key = {"PK": f"COURSE#{course_id}", "SK": "METADATA"},
        UpdateExpression = "REMOVE GSI2PK, GSI2SK"
        # Không có GSI2PK → item không còn trong GSI2
    )


def get_featured_courses() -> list:
    """Query GSI2 — chỉ trả về featured courses"""
    resp = table.query(
        IndexName = "GSI2",
        KeyConditionExpression = (
            Key("GSI2PK").eq("FEATURED_COURSE") &
            Key("GSI2SK").begins_with("ORDER#")
        ),
        ScanIndexForward = True  # theo thứ tự feature_order
    )
    return resp["Items"]


# Setup featured courses
feature_course("1", feature_order=1)
feature_course("3", feature_order=2)

featured = get_featured_courses()
print(f"Featured courses: {[c['title'] for c in featured]}")
```

## 2\. DynamoDB Transactions

### 2.1 TransactGet — Đọc Atomic

```python
def get_user_with_course(user_id: str, course_id: str) -> dict:
    """
    Đọc user và course trong cùng 1 transaction.
    Đảm bảo consistent snapshot của cả 2.
    """
    client = table.meta.client

    resp = client.transact_get_items(
        TransactItems = [
            {
                "Get": {
                    "TableName": "FoxDev",
                    "Key": {
                        "PK": {"S": f"USER#{user_id}"},
                        "SK": {"S": "PROFILE"}
                    }
                }
            },
            {
                "Get": {
                    "TableName": "FoxDev",
                    "Key": {
                        "PK": {"S": f"COURSE#{course_id}"},
                        "SK": {"S": "METADATA"}
                    }
                }
            }
        ]
    )

    responses = resp["Responses"]
    return {
        "user":   responses[0].get("Item"),
        "course": responses[1].get("Item")
    }
```

### 2.2 TransactWrite — Real-world Scenarios

```python
from botocore.exceptions import ClientError

# ─── Scenario 1: Complete course (atomic update nhiều items) ───
def complete_course(user_id: str, course_id: str) -> bool:
    """
    Atomic:
    1. Mark enrollment as completed
    2. Create certificate item
    3. Increment course completion count
    """
    client     = table.meta.client
    now        = datetime.now().isoformat()
    cert_id    = f"CERT#{user_id}#{course_id}"

    try:
        client.transact_write_items(
            TransactItems = [
                # 1. Update enrollment
                {
                    "Update": {
                        "TableName": "FoxDev",
                        "Key": {
                            "PK": {"S": f"USER#{user_id}"},
                            "SK": {"S": f"ENROLLMENT#COURSE#{course_id}"}
                        },
                        "UpdateExpression": (
                            "SET completed = :t, progress = :one, "
                            "completedAt = :now"
                        ),
                        "ConditionExpression": "completed = :f",  # chưa complete
                        "ExpressionAttributeValues": {
                            ":t":   {"BOOL": True},
                            ":one": {"N": "1"},
                            ":now": {"S": now},
                            ":f":   {"BOOL": False}
                        }
                    }
                },
                # 2. Create certificate
                {
                    "Put": {
                        "TableName": "FoxDev",
                        "Item": {
                            "PK":          {"S": f"USER#{user_id}"},
                            "SK":          {"S": f"CERT#COURSE#{course_id}"},
                            "GSI1PK":     {"S": f"CERT#{cert_id}"},
                            "GSI1SK":     {"S": "METADATA"},
                            "entityType": {"S": "CERTIFICATE"},
                            "userId":     {"S": user_id},
                            "courseId":   {"S": course_id},
                            "certId":     {"S": cert_id},
                            "issuedAt":   {"S": now},
                        },
                        "ConditionExpression": "attribute_not_exists(PK)"
                    }
                },
                # 3. Increment completion count
                {
                    "Update": {
                        "TableName": "FoxDev",
                        "Key": {
                            "PK": {"S": f"COURSE#{course_id}"},
                            "SK": {"S": "METADATA"}
                        },
                        "UpdateExpression": "ADD completionCount :one",
                        "ExpressionAttributeValues": {
                            ":one": {"N": "1"}
                        }
                    }
                }
            ]
        )
        print(f"✅ User {user_id} completed Course {course_id}")
        return True

    except ClientError as e:
        code = e.response["Error"]["Code"]
        if code == "TransactionCanceledException":
            reasons = e.response.get("CancellationReasons", [])
            for i, r in enumerate(reasons):
                if r["Code"] != "None":
                    print(f"  Step {i+1} failed: {r['Code']}")
            return False
        raise


# Test
complete_course("1", "1")


# ─── Scenario 2: Transfer enrollment (cancel + re-enroll) ───
def transfer_enrollment(user_id: str,
                         from_course: str,
                         to_course:   str) -> bool:
    """Chuyển enrollment từ course này sang course khác (atomic)"""
    client = table.meta.client
    now    = datetime.now().isoformat()

    try:
        client.transact_write_items(
            TransactItems = [
                # Delete old enrollment
                {
                    "Delete": {
                        "TableName": "FoxDev",
                        "Key": {
                            "PK": {"S": f"USER#{user_id}"},
                            "SK": {"S": f"ENROLLMENT#COURSE#{from_course}"}
                        },
                        "ConditionExpression": "attribute_exists(PK)"
                    }
                },
                # Create new enrollment
                {
                    "Put": {
                        "TableName": "FoxDev",
                        "Item": {
                            "PK":       {"S": f"USER#{user_id}"},
                            "SK":       {"S": f"ENROLLMENT#COURSE#{to_course}"},
                            "GSI1PK":  {"S": f"COURSE#{to_course}"},
                            "GSI1SK":  {"S": f"ENROLLMENT#USER#{user_id}"},
                            "entityType": {"S": "ENROLLMENT"},
                            "userId":   {"S": user_id},
                            "courseId": {"S": to_course},
                            "progress": {"N": "0"},
                            "completed": {"BOOL": False},
                            "enrolledAt": {"S": now},
                            "transferredFrom": {"S": from_course},
                        },
                        "ConditionExpression": "attribute_not_exists(PK)"
                    }
                }
            ]
        )
        return True
    except ClientError:
        return False
```

## 3\. DynamoDB Streams

### 3.1 Streams Là Gì?

```java
DynamoDB Streams = Change Data Capture (CDC) cho DynamoDB
→ Mỗi khi item được PUT/UPDATE/DELETE → tạo stream record
→ Stream records được giữ 24 giờ
→ Lambda có thể consume stream → trigger actions

Use cases:
  ✅ Send email khi order được tạo
  ✅ Sync DynamoDB sang Elasticsearch
  ✅ Invalidate cache khi data thay đổi
  ✅ Analytics pipeline
  ✅ Cross-region replication (Global Tables)
```

### 3.2 Bật Streams

```python
# Bật stream khi tạo table
client = dynamodb.meta.client

client.create_table(
    TableName = "FoxDevWithStream",
    KeySchema = [
        {"AttributeName": "PK", "KeyType": "HASH"},
        {"AttributeName": "SK", "KeyType": "RANGE"},
    ],
    AttributeDefinitions = [
        {"AttributeName": "PK", "AttributeType": "S"},
        {"AttributeName": "SK", "AttributeType": "S"},
    ],
    BillingMode = "PAY_PER_REQUEST",
    StreamSpecification = {
        "StreamEnabled":  True,
        "StreamViewType": "NEW_AND_OLD_IMAGES"
        # NEW_IMAGE:         chỉ item sau khi thay đổi
        # OLD_IMAGE:         chỉ item trước khi thay đổi
        # NEW_AND_OLD_IMAGES: cả trước và sau
        # KEYS_ONLY:         chỉ PK + SK
    }
)

# Bật stream cho table đã có
client.update_table(
    TableName = "FoxDev",
    StreamSpecification = {
        "StreamEnabled":  True,
        "StreamViewType": "NEW_AND_OLD_IMAGES"
    }
)
```

### 3.3 Đọc Stream Thủ Công (không Lambda)

```python
def read_stream_records(table_name: str, limit: int = 10) -> list:
    """
    Đọc stream records thủ công (thường dùng Lambda trong production).
    """
    client = boto3.client(
        "dynamodbstreams",
        endpoint_url          = "http://localhost:8000",
        region_name           = "us-east-1",
        aws_access_key_id     = "fake",
        aws_secret_access_key = "fake"
    )

    # Lấy stream ARN
    table_info  = dynamodb.meta.client.describe_table(TableName=table_name)
    stream_arn  = table_info["Table"].get("LatestStreamArn")
    if not stream_arn:
        print("Stream not enabled")
        return []

    # Lấy shards
    desc_stream = client.describe_stream(StreamArn=stream_arn)
    shards      = desc_stream["StreamDescription"]["Shards"]

    records = []
    for shard in shards:
        shard_id = shard["ShardId"]

        # Lấy iterator
        iterator_resp = client.get_shard_iterator(
            StreamArn         = stream_arn,
            ShardId           = shard_id,
            ShardIteratorType = "TRIM_HORIZON"  # từ đầu
        )
        iterator = iterator_resp["ShardIterator"]

        # Đọc records
        while iterator:
            resp     = client.get_records(ShardIterator=iterator, Limit=limit)
            records.extend(resp["Records"])
            iterator = resp.get("NextShardIterator")
            if not resp["Records"]:
                break

    return records


# ─── Lambda Handler (production pattern) ───
def lambda_handler(event, context):
    """
    AWS Lambda function xử lý DynamoDB Stream events.
    Trigger: DynamoDB Streams → Lambda
    """
    for record in event["Records"]:
        event_name = record["eventName"]  # INSERT, MODIFY, REMOVE

        if event_name == "INSERT":
            new_item = record["dynamodb"].get("NewImage", {})
            entity_type = new_item.get("entityType", {}).get("S")

            if entity_type == "ORDER":
                order_id = new_item.get("orderId", {}).get("S")
                user_id  = new_item.get("userId",  {}).get("S")
                amount   = new_item.get("amount",  {}).get("N")
                print(f"New order: {order_id} by user {user_id} — {amount}đ")
                # send_confirmation_email(user_id, order_id)

            elif entity_type == "ENROLLMENT":
                user_id   = new_item.get("userId",   {}).get("S")
                course_id = new_item.get("courseId", {}).get("S")
                print(f"New enrollment: user {user_id} → course {course_id}")
                # send_welcome_email(user_id, course_id)

        elif event_name == "MODIFY":
            new_item  = record["dynamodb"].get("NewImage", {})
            old_item  = record["dynamodb"].get("OldImage", {})
            entity_type = new_item.get("entityType", {}).get("S")

            if entity_type == "ENROLLMENT":
                new_completed = new_item.get("completed", {}).get("BOOL", False)
                old_completed = old_item.get("completed", {}).get("BOOL", False)

                # Chỉ xử lý khi vừa complete
                if new_completed and not old_completed:
                    user_id   = new_item.get("userId",   {}).get("S")
                    course_id = new_item.get("courseId", {}).get("S")
                    print(f"Course completed! User {user_id} finished {course_id}")
                    # send_completion_certificate(user_id, course_id)

        elif event_name == "REMOVE":
            old_item    = record["dynamodb"].get("OldImage", {})
            entity_type = old_item.get("entityType", {}).get("S")
            print(f"Deleted {entity_type}")
```

## 4\. TTL — Tự Động Xóa Items

```python
import time

# ─── Set TTL khi tạo item ───
def put_session(user_id: str, session_data: dict,
                ttl_hours: int = 24):
    """Session tự động xóa sau ttl_hours"""
    expires_at = int(time.time()) + (ttl_hours * 3600)

    table.put_item(Item={
        "PK":        f"SESSION#{session_data['sessionId']}",
        "SK":        "DATA",
        "GSI1PK":   f"USER#{user_id}",
        "GSI1SK":   f"SESSION#{session_data['sessionId']}",
        "entityType": "SESSION",
        "userId":    user_id,
        "sessionId": session_data["sessionId"],
        "device":    session_data.get("device", "web"),
        "createdAt": datetime.now().isoformat(),
        "expiresAt": expires_at,   # TTL attribute — phải là Unix timestamp (số)
    })


# ─── Bật TTL cho table ───
def enable_ttl(table_name: str, ttl_attribute: str = "expiresAt"):
    dynamodb.meta.client.update_time_to_live(
        TableName           = table_name,
        TimeToLiveSpecification = {
            "Enabled":       True,
            "AttributeName": ttl_attribute
        }
    )
    print(f"✅ TTL enabled on attribute '{ttl_attribute}'")


enable_ttl("FoxDev", "expiresAt")

# Test session
put_session("1", {
    "sessionId": "sess_abc123",
    "device":    "mobile"
}, ttl_hours=2)

# Kiểm tra TTL value
resp = table.get_item(Key={
    "PK": "SESSION#sess_abc123",
    "SK": "DATA"
})
if resp.get("Item"):
    ttl_ts  = resp["Item"].get("expiresAt")
    expires = datetime.fromtimestamp(ttl_ts)
    print(f"Session expires at: {expires.isoformat()}")
```

## 5\. Thực Hành Tổng Hợp

```python
def test_all_features():
    print("=" * 55)
    print("DYNAMODB ADVANCED FEATURES TEST")
    print("=" * 55)

    # 1. GSI queries
    print("\n1. GSI Queries:")
    user   = get_user_by_email("nam@gmail.com")
    print(f"   By email: {user['firstName'] if user else 'Not found'}")

    courses = list_courses_by_category("java")
    print(f"   Java courses: {[c['title'] for c in courses[:2]]}")

    students = get_course_students("1")
    print(f"   Course 1 students: {len(students)}")

    # 2. Sparse GSI
    print("\n2. Sparse GSI (Featured):")
    feature_course("1", 1)
    feature_course("3", 2)
    featured = get_featured_courses()
    print(f"   Featured: {[c.get('title') for c in featured]}")

    # 3. Transaction — complete course
    print("\n3. Transaction (Complete Course):")
    result = complete_course("2", "1")
    print(f"   Completed: {result}")

    # 4. Streams
    print("\n4. Streams:")
    print("   Stream records available via Lambda trigger")
    print("   (Local DynamoDB streams may have limitations)")

    # 5. TTL
    print("\n5. TTL:")
    enable_ttl("FoxDev")
    put_session("1", {"sessionId": "test_session"}, ttl_hours=1)
    print("   Session created with 1h TTL")


test_all_features()
```

## Tổng Kết


| Feature | Dùng khi |
|---|---|
| GSI Overloading | Phục vụ nhiều access patterns với ít GSI |
| Sparse GSI | Chỉ index subset items (featured, published...) |
| TransactGet | Đọc nhiều items consistent snapshot |
| TransactWrite | Atomic multi-item writes (order + enrollment) |
| Streams | Trigger actions khi data thay đổi (email, sync) |
| TTL | Tự động xóa items hết hạn (session, cache) |



```java
DynamoDB Advanced Patterns:
  1 GSI → nhiều purposes (overloading)
  Sparse GSI → index chỉ items cần thiết
  TransactWrite → atomic business operations
  Streams → event-driven architecture
  TTL → built-in data expiration
```

Bài tiếp theo — bài cuối của DynamoDB series — **Pricing, Capacity Planning, Anti-patterns và khi nào nên dùng DynamoDB**.

