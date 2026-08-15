# Workflow Cơ Bản: init, add, commit, log & Thực Hành Với VSCode

![Workflow Cơ Bản- init, add, commit, log & Thực Hành Với VSCode.jpeg](../images/ae4b8f23-81ae-4e3f-ae89-e18e53c59f70.jpeg)

Bài trước FoxDev đã giải thích lý thuyết. Bài này thực hành — bạn sẽ tạo một project Spring Boot thực tế, setup Git từ đầu và đi qua toàn bộ workflow hàng ngày. Mỗi command đều được giải thích kỹ lý do tại sao dùng, không chỉ dùng như thế nào.

## 1\. Khởi Tạo Repository

### git init — Bắt Đầu Từ Đầu

```bash
# Tạo project mới
mkdir foxdev-backend
cd foxdev-backend

# Khởi tạo Git repository
git init
# Initialized empty Git repository in /path/to/foxdev-backend/.git/

# Kiểm tra: thư mục .git/ được tạo
ls -la
# .git/   ← Git database ở đây

# Xem branch hiện tại
git status
# On branch main
# No commits yet
# nothing to commit (create/copy files and use "git add" to track)
```

**Trong VSCode:**

```java
1. Mở thư mục: File → Open Folder → chọn foxdev-backend
2. Source Control panel (Ctrl+Shift+G) hiển thị "Initialize Repository"
3. Click "Initialize Repository" → tương đương git init
```

**Trong IntelliJ:**

```java
VCS → Create Git Repository → chọn thư mục project
```

## 2\. Tạo Project Spring Boot Mẫu

```bash
# Tạo cấu trúc project
mkdir -p src/main/java/com/foxdev
mkdir -p src/main/resources
mkdir -p src/test/java/com/foxdev

# pom.xml
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.foxdev</groupId>
    <artifactId>backend</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
</project>
EOF

# Main application class
cat > src/main/java/com/foxdev/FoxDevApplication.java << 'EOF'
package com.foxdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoxDevApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoxDevApplication.class, args);
    }
}
EOF

# application.properties
cat > src/main/resources/application.properties << 'EOF'
spring.application.name=foxdev-backend
server.port=8080
EOF

# .gitignore
cat > .gitignore << 'EOF'
# Maven
target/
*.class
*.jar
*.war

# IntelliJ IDEA
.idea/
*.iml
*.iws

# VSCode
.vscode/
!.vscode/extensions.json

# OS
.DS_Store
Thumbs.db

# Logs
*.log
logs/

# Environment
.env
*.properties.local
application-prod.properties
EOF
```

## 3\. git status — Luôn Chạy Trước Mọi Thứ

```bash
git status
# On branch main
# No commits yet
#
# Untracked files:
#   .gitignore
#   pom.xml
#   src/
#
# nothing added to commit but untracked files present
# (use "git add" to track)
```

`git status` cho biết:

*   Branch đang đứng
    
*   Files chưa track (Untracked)
    
*   Files đã thay đổi (Modified)
    
*   Files đã stage, chờ commit (Staged)
    

**Short status:**

```bash
git status -s
# ?? .gitignore    ← ?? = untracked
# ?? pom.xml
# ?? src/
# M  UserService.java   ← M = modified (staged)
#  M OtherFile.java     ← M (right) = modified (not staged)
# A  NewFile.java       ← A = added (staged)
```

## 4\. git add — Stage Thay Đổi

```bash
# Stage từng file
git add .gitignore
git add pom.xml

# Stage toàn bộ thư mục
git add src/

# Stage tất cả (phổ biến nhất)
git add .

# Xem kết quả
git status
# Changes to be committed:
#   new file:   .gitignore
#   new file:   pom.xml
#   new file:   src/main/java/com/foxdev/FoxDevApplication.java
#   new file:   src/main/resources/application.properties
```

### git add -p — Stage Theo Từng Phần (Hunk)

Đây là kỹ năng quan trọng — cho phép stage chỉ một phần thay đổi trong file:

```bash
# Giả sử UserService.java có 2 thay đổi độc lập:
# 1. Fix bug ở method findUser()
# 2. Add feature ở method createUser()
# Muốn commit fix bug riêng, feature riêng

git add -p src/UserService.java
# diff --git a/src/UserService.java b/src/UserService.java
# ...
# @@ -10,7 +10,9 @@ public class UserService {
#  
# +    // Fix: handle null user_id
#      public User findUser(String userId) {
# +        if (userId == null) return null;
#          return userRepository.findById(userId);
#      }
# Stage this hunk [y,n,q,a,d,s,e,?]?
# y → stage hunk này
# n → bỏ qua
# s → split hunk nhỏ hơn
# e → edit thủ công
```

## 5\. git commit — Tạo Snapshot

```bash
# Commit cơ bản
git commit -m "chore: initial project setup"
# [main (root-commit) abc1234] chore: initial project setup
# 5 files changed, 45 insertions(+)
# create mode 100644 .gitignore
# create mode 100644 pom.xml
# create mode 100644 src/main/java/com/foxdev/FoxDevApplication.java

# Commit với multi-line message
git commit
# → Mở editor (VSCode/vim)
# Viết:
#   feat(user): add UserService with CRUD operations
#   
#   - Add UserService class with findById, save, delete
#   - Integrate with UserRepository
#   - Add input validation
# Lưu và đóng editor → commit được tạo

# Shortcut: add + commit cùng lúc (chỉ cho tracked files)
git commit -am "fix: resolve null pointer in UserService"
# -a = stage tất cả modified files
# Không stage untracked files mới
```

### Sửa Commit Vừa Tạo

```bash
# Quên add file, hoặc commit message sai
# → amend (chỉ dùng khi CHƯA push lên remote!)

git add ForgottenFile.java
git commit --amend --no-edit        # giữ nguyên message
git commit --amend -m "new message" # đổi message

# ⚠️ amend tạo commit mới hoàn toàn (SHA hash mới)
# → Nếu đã push, amend sẽ gây conflict cho người khác
```

## 6\. git log — Đọc Lịch Sử

```bash
# Full log
git log

# Compact log (dùng nhiều nhất)
git log --oneline
# a1b2c3d feat(user): add UserService
# e4f5g6h chore: initial project setup

# Log với graph (rất hữu ích khi có nhiều branches)
git log --oneline --graph --all
# * a1b2c3d (HEAD → main) feat(user): add UserService
# * e4f5g6h chore: initial project setup

# Log verbose: xem files thay đổi trong mỗi commit
git log --stat
# commit a1b2c3d
# feat(user): add UserService
#  src/UserService.java | 45 +++++++++++
#  1 file changed, 45 insertions(+)

# Log với diff content
git log -p -2   # patch mode, 2 commits gần nhất

# Custom format
git log --pretty=format:"%h - %an, %ar : %s"
# a1b2c3d - Nam Nguyen, 2 hours ago : feat(user): add UserService
# e4f5g6h - Nam Nguyen, 1 day ago : chore: initial project setup
```

### Xem Chi Tiết Một Commit

```bash
git show a1b2c3d
# commit a1b2c3d...
# Author: Nam Nguyen <nam@nguyentienkhoi.hashnode.dev>
# Date:   Mon Mar 15 10:00:00 2025
#
#     feat(user): add UserService with CRUD operations
#
# diff --git a/src/UserService.java b/src/UserService.java
# ...

# Xem chỉ files thay đổi
git show a1b2c3d --name-only
```

## 7\. Workflow Hoàn Chỉnh — Thực Hành

Hãy thực hành đúng quy trình developer hàng ngày:

```bash
# ─── ITERATION 1: Thêm UserController ───

# 1. Xem trạng thái trước khi làm gì
git status
# On branch main
# nothing to commit, working tree clean

# 2. Tạo file mới
cat > src/main/java/com/foxdev/UserController.java << 'EOF'
package com.foxdev;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public List<String> getAllUsers() {
        return List.of("user1", "user2", "user3");
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable String id) {
        return "user:" + id;
    }
}
EOF

# 3. Xem thay đổi
git status
# Untracked files: src/main/java/com/foxdev/UserController.java

# 4. Stage
git add src/main/java/com/foxdev/UserController.java

# 5. Verify staging area
git diff --staged

# 6. Commit
git commit -m "feat(user): add UserController with GET endpoints"

# ─── ITERATION 2: Sửa code ───

# 7. Sửa UserController để thêm endpoint mới
# ... edit file ...

# 8. Xem diff
git diff

# 9. Stage và commit
git add src/main/java/com/foxdev/UserController.java
git commit -m "feat(user): add POST /api/users endpoint"

# ─── Xem kết quả ───
git log --oneline
# b2c3d4e feat(user): add POST /api/users endpoint
# a1b2c3d feat(user): add UserController with GET endpoints
# e4f5g6h chore: initial project setup
```

## 8\. Workflow Với VSCode Source Control

**Thực hành thao tác trong VSCode:**

```java
1. Mở Source Control (Ctrl+Shift+G)

2. Xem file changes:
   → Files xuất hiện dưới "Changes"
   → U = Untracked, M = Modified, D = Deleted, A = Added

3. Stage file:
   → Hover vào file → click "+" icon
   → Hoặc click "Stage All Changes" (+) cạnh "Changes"
   → File chuyển sang "Staged Changes"

4. Xem diff:
   → Click vào file trong "Changes" → mở diff view
   → Left: bản cũ, Right: bản mới

5. Viết commit message:
   → Gõ vào ô "Message" ở trên cùng
   → Ctrl+Enter để commit

6. Unstage:
   → Hover file trong "Staged Changes" → click "-"

7. Discard changes:
   → Hover file trong "Changes" → click "↺" (discard)
   → ⚠️ Không thể undo!
```

**GitLens extension:**

```java
Sau khi cài GitLens:
→ Mỗi dòng code có annotation: "John Doe, 3 days ago: fix login"
→ Hover để xem chi tiết commit
→ Click để xem full commit
→ Alt+B để mở full blame cho file
```

## 9\. Workflow Với IntelliJ

**Commit dialog (Ctrl+K):**

```java
1. Checkbox files muốn commit
2. Xem diff: click file → diff viewer mở bên phải
3. Viết message ở ô lớn phía trên
4. "Commit" hoặc "Commit and Push"
5. "Amend Commit": sửa commit vừa tạo
```

**Git Log (View → Git → Log):**

```java
→ Visual branch graph ở trên
→ Click commit → xem changed files bên phải
→ Double-click file → xem diff của file đó trong commit
→ Right-click commit → nhiều options hữu ích
```

**VCS menu:**

```java
VCS → Git → Compare With Branch
VCS → Git → Annotate (blame)
VCS → Git → Show History for Selection
```

## 10\. Một Số Lệnh Hay Dùng Hàng Ngày

```bash
# Xem những gì mình đã commit hôm nay
git log --since="8 hours ago" --author="$(git config user.name)"

# Xem tóm tắt thay đổi
git log --stat --oneline -5

# Diff giữa 2 commits
git diff HEAD~2 HEAD   # so sánh 2 commits trước với hiện tại

# Tìm commit chứa text cụ thể
git log --grep="payment" --oneline

# Tìm file nào đã bị xóa
git log --diff-filter=D --name-only

# Xem ai đã thay đổi dòng X của file
git blame -L 10,20 UserService.java
# → xem dòng 10-20
# e4f5g6h (Nam Nguyen 2025-03-15 10:00:00) public User findUser...

# Xem nội dung file ở commit cụ thể
git show HEAD~2:src/UserService.java
```

## Tổng Kết

```java
Daily Git workflow:

git status              → bước 1: luôn kiểm tra trước
git pull               → lấy code mới nhất từ remote
... code ...
git status              → xem những gì thay đổi
git diff               → xem chi tiết thay đổi
git add .              → stage thay đổi
git diff --staged      → verify trước khi commit
git commit -m "..."    → commit với message rõ ràng
git push               → đẩy lên remote
```


| Command | Shortcut VSCode | Shortcut IntelliJ |
|---|---|---|
| git status | Source Control panel | Ctrl+K (commit dialog) |
| git add | Click "+" trên file | Check file trong commit dialog |
| git commit | Ctrl+Enter | Ctrl+K → Commit |
| git log | GitLens history | Alt+9 → Log tab |
| git diff | Click file | Click file trong commit dialog |



Bài tiếp theo chúng ta sẽ học **Remote Repository** — kết nối GitHub/GitLab, setup SSH key, push, pull, fetch và cách làm việc với remote branches.

