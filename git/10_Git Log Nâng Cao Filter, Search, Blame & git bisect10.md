# Git Log Nâng Cao: Filter, Search, Blame & git bisect

![Git Log Nâng Cao - Filter, Search, Blame & git bisect.jpeg](../images/730cfe6b-e61a-4ee8-80ff-6b16200c3f5a.jpeg)

`git log` không chỉ là xem lịch sử commits. Khi dự án lớn lên với hàng nghìn commits, bạn cần tìm kiếm chính xác: "Commit nào đã thêm dòng code này?", "File này thay đổi gì trong 2 tuần qua?", "Bug xuất hiện từ commit nào?". Bài này đi sâu vào các kỹ thuật tìm kiếm và phân tích lịch sử Git như một detective thực thụ.

## 1\. git log — Options Đầy Đủ

### Formatting Output

```bash
# ─── Built-in formats ───
git log --oneline           # hash + message, 1 dòng
git log --short             # compact
git log --medium            # default
git log --full              # đầy đủ
git log --fuller            # đầy đủ + author date vs commit date
git log --raw               # raw diff info

# ─── Custom format ───
git log --pretty=format:"%h %ad | %s%d [%an]" --date=short

# Format placeholders:
# %H   commit hash đầy đủ
# %h   commit hash rút gọn
# %T   tree hash
# %an  author name
# %ae  author email
# %ad  author date
# %ar  author date relative ("2 hours ago")
# %cn  committer name
# %cd  committer date
# %s   subject (first line of message)
# %b   body (rest of message)
# %d   ref names (branches, tags)
# %n   newline

# Ví dụ thực tế
git log --pretty=format:"%C(yellow)%h%Creset %C(blue)%ad%Creset | %C(green)%s%Creset %C(red)%d%Creset [%an]" --date=short

# ─── Alias hữu ích (thêm vào ~/.gitconfig) ───
git config --global alias.lg "log --color --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit"
git config --global alias.lga "log --color --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit --all"

git lg   # beautiful one-liner log
git lga  # all branches
```

## 2\. Filter Commits

### Theo Thời Gian

```bash
# Commits trong khoảng thời gian
git log --since="2025-01-01"
git log --after="2025-01-01"   # tương đương
git log --until="2025-03-31"
git log --before="2025-03-31"  # tương đương

# Kết hợp
git log --since="2025-01-01" --until="2025-03-31"

# Relative time
git log --since="2 weeks ago"
git log --since="3 days ago"
git log --since="1 month ago"
git log --since="yesterday"

# Commits trong ngày hôm nay
git log --since="midnight"
git log --since="$(date +%Y-%m-%d)"

# Commits của tuần này
git log --since="1 week ago" --oneline
```

### Theo Author

```bash
# Commits của một người
git log --author="Nam"              # partial match
git log --author="nam@nguyentienkhoi.hashnode.dev"  # by email

# Commits không phải của một người
git log --author="^(?!Nam)" --perl-regexp

# Case insensitive
git log --author="nam" -i

# Kết hợp với thời gian
git log --author="Nam" --since="2025-01-01" --oneline
```

### Theo Commit Message

```bash
# Tìm theo message
git log --grep="payment"             # chứa "payment"
git log --grep="fix" --grep="auth"   # chứa "fix" OR "auth"

# AND condition (cả hai)
git log --grep="fix" --grep="auth" --all-match

# Case insensitive
git log --grep="PAYMENT" -i

# Regex
git log --grep="feat(payment|auth)" --perl-regexp
git log --grep="^feat:" --perl-regexp

# Kết hợp tất cả
git log --author="Nam" --since="2025-01-01" --grep="feat" --oneline
```

### Theo Files và Paths

```bash
# Commits có thay đổi 1 file cụ thể
git log -- src/main/java/com/foxdev/UserService.java

# Commits có thay đổi trong thư mục
git log -- src/main/java/com/foxdev/payment/

# Commits có thay đổi file khớp pattern
git log -- "*.java"
git log -- "src/**/*.properties"

# Commits rename file
git log --follow src/UserService.java
# --follow: theo dõi qua renames (không chỉ path hiện tại)

# Xem diff của file trong commits
git log -p -- src/UserService.java
git log -p --follow -- src/UserService.java
```

### Theo Số Lượng

```bash
# N commits gần nhất
git log -5
git log --max-count=5
git log -n 5   # tương đương

# Bỏ qua N commits đầu
git log --skip=10    # bỏ 10 commits đầu

# Kết hợp (commits 11-20)
git log --skip=10 -10 --oneline
```

## 3\. Tìm Kiếm Trong Code

### Pickaxe — Tìm Commit Thêm/Xóa Code

```bash
# Tìm commits THÊM hoặc XÓA text cụ thể
# (-S: tìm theo số lần xuất hiện thay đổi)
git log -S "VNPayGateway"            # thêm hoặc xóa string này
git log -S "VNPayGateway" --oneline  # compact

# Tìm theo regex (-G: tìm theo regex match trong diff)
git log -G "public.*Payment.*Service" --oneline
git log -G "SELECT.*FROM.*users" --oneline   # tìm SQL query bị thêm

# Xem diff của những commits tìm được
git log -S "VNPayGateway" -p

# Ví dụ thực tế:
# Tìm xem ai đã thêm API key vào code
git log -S "sk_live_" -p   # stripe key
git log -S "password123" -p
```

### Tìm Trong Working Directory và History

```bash
# Tìm text trong tất cả files hiện tại
git grep "VNPayGateway"
git grep -n "VNPayGateway"     # hiển thị line number
git grep -i "vnpaygateway"     # case insensitive
git grep -l "VNPayGateway"     # chỉ tên file

# Tìm text ở commit cụ thể
git grep "VNPayGateway" HEAD
git grep "VNPayGateway" v1.0.0
git grep "VNPayGateway" abc1234

# Tìm trong tất cả branches
git grep "VNPayGateway" $(git rev-list --all)
# ⚠️ Chậm với history lớn

# Tìm text trong tất cả commits (kết hợp log và grep)
git log -S "VNPayGateway" --all --oneline
```

## 4\. git diff — Nâng Cao

```bash
# ─── So sánh giữa branches ───
git diff main feature/payment               # diff working tree
git diff main..feature/payment             # commits có trong feature không có trong main
git diff main...feature/payment            # commits từ khi 2 branches tách nhau

# ─── So sánh commits ───
git diff HEAD~3 HEAD                       # 3 commits gần nhất
git diff abc1234 def5678                   # 2 commits cụ thể
git diff v1.0.0 v1.1.0                    # 2 tags

# ─── Chỉ xem file names ───
git diff --name-only main feature/payment
git diff --name-status main feature/payment
# M  src/UserService.java     ← Modified
# A  src/PaymentService.java  ← Added
# D  src/OldFile.java         ← Deleted
# R  src/Renamed.java         ← Renamed

# ─── Stats ───
git diff --stat main feature/payment
# src/UserService.java    | 15 +++++---
# src/PaymentService.java | 45 +++++++++++++++
# 2 files changed, 52 insertions(+), 8 deletions(-)

# ─── Diff specific file giữa branches ───
git diff main..feature/payment -- src/UserService.java

# ─── Diff với word-level (không phải line-level) ───
git diff --word-diff
git diff --word-diff=color

# ─── Ignore whitespace ───
git diff -w                     # ignore tất cả whitespace
git diff --ignore-blank-lines   # ignore blank line changes
```

## 5\. git blame — Ai Đã Viết Dòng Code Này?

```bash
# Xem ai viết từng dòng của file
git blame src/main/java/com/foxdev/UserService.java
# a1b2c3d (Nam Nguyen  2025-01-15 10:30:00 +0700  1) package com.foxdev;
# a1b2c3d (Nam Nguyen  2025-01-15 10:30:00 +0700  2)
# e4f5g6h (Linh Tran   2025-02-20 14:00:00 +0700  3) public class UserService {
# b2c3d4e (Nam Nguyen  2025-03-01 09:15:00 +0700  4)     public User findById(Long id) {

# Xem chỉ một range dòng
git blame -L 10,25 src/UserService.java
git blame -L 10,+15 src/UserService.java  # từ dòng 10, 15 dòng tiếp

# Blame theo function name
git blame -L ":findById" src/UserService.java
# → Tự detect boundary của function findById

# Compact output
git blame --line-porcelain src/UserService.java   # machine-readable
git blame -s src/UserService.java                  # chỉ hash, không tên

# Bỏ qua whitespace changes
git blame -w src/UserService.java

# Bỏ qua moves (file được copy từ file khác)
git blame -C src/UserService.java
git blame -CCC src/UserService.java  # track qua nhiều cấp copy

# Blame ở commit cụ thể (xem lúc đó file trông như thế nào)
git blame v1.0.0 -- src/UserService.java
git blame abc1234 -- src/UserService.java
```

### Blame Trong VSCode và IntelliJ

**VSCode với GitLens:**

```java
Hover vào dòng code → tooltip hiển thị:
  "Nam Nguyen, 2 days ago • feat(user): add findById"

Click tooltip → mở full commit info

Alt+B → toggle inline blame annotations
  → Mỗi dòng hiển thị: "Nam Nguyen, Mar 15 • feat(user)..."

Source Control → GitLens → File Annotations → Toggle Git Blame
```

**IntelliJ:**

```java
Right-click gutter (số dòng) → Annotate with Git Blame
→ Mỗi dòng hiển thị author + date

Click annotation → mở commit trong Git Log

VCS → Git → Annotate (shortcut: tùy cấu hình)
```

## 6\. git bisect — Tìm Commit Gây Ra Bug

**bisect** dùng binary search để tìm commit nào gây ra bug — thay vì check từng commit một.

```java
Không dùng bisect:
  1000 commits, bug đâu đó trong này
  → Check từng commit: 1000 lần ← impossible

Dùng bisect (binary search):
  Lần 1: check commit giữa (500) → bug có không?
  Lần 2: check commit giữa nửa đó (250 hoặc 750)
  ...
  → Sau ~10 lần: tìm ra commit gây bug!
  log2(1000) ≈ 10 lần check
```

### Bisect Thủ Công

```bash
# ─── Bước 1: Start bisect ───
git bisect start

# ─── Bước 2: Đánh dấu điểm tốt và xấu ───
# Current commit có bug
git bisect bad                    # HEAD là bad

# Commit 3 tuần trước không có bug
git bisect good v1.0.0            # hoặc dùng hash
git bisect good abc1234

# Git tự checkout commit giữa:
# Bisecting: 231 revisions left to test after this
# [midcommit] feat: add email feature

# ─── Bước 3: Test và mark ───
# ... chạy code, test xem bug có không ...

# Nếu bug có:
git bisect bad

# Nếu bug không có:
git bisect good

# → Git checkout commit tiếp theo cần test
# Lặp lại đến khi Git tìm ra commit gây bug:

# abc1234 is the first bad commit
# commit abc1234
# Author: Nam Nguyen <nam@nguyentienkhoi.hashnode.dev>
# Date:   Mon Mar 15 10:00:00 2025
#
#     feat(payment): add VNPay integration
#
# :040000 040000 abc def src
# M  src/PaymentService.java

# ─── Bước 4: Kết thúc bisect ───
git bisect reset
# → Quay về HEAD ban đầu
```

### Bisect Tự Động — Chạy Script

```bash
# Viết script test (trả về 0 nếu OK, non-zero nếu bug)
cat > test_script.sh << 'EOF'
#!/bin/bash
# Build project
mvn test -q 2>/dev/null

# Hoặc test specific
curl -s http://localhost:8080/api/payment/status | grep -q "success"
exit $?
EOF
chmod +x test_script.sh

# Chạy bisect tự động
git bisect start
git bisect bad HEAD
git bisect good v1.0.0
git bisect run ./test_script.sh

# Git tự động:
# → Checkout commit giữa
# → Chạy script
# → Đánh dấu good/bad dựa trên exit code
# → Tìm ra commit gây bug
# → In kết quả
# → Tự động reset!
```

### Bisect — Bỏ Qua Commits Không Build Được

```bash
# Nếu một commit không thể build/test
git bisect skip               # skip current commit
git bisect skip abc1234       # skip commit cụ thể
git bisect skip abc..def      # skip range of commits
```

## 7\. git shortlog — Thống Kê Commits

```bash
# Đếm commits theo author
git shortlog -sn
# 127  Nam Nguyen
#  84  Linh Tran
#  45  Minh Le

# Với email
git shortlog -sne
# 127  Nam Nguyen <nam@nguyentienkhoi.hashnode.dev>

# Nhóm theo author với messages
git shortlog -n

# Trong khoảng thời gian
git shortlog -sn --since="2025-01-01"

# Thống kê cho release notes
git shortlog v1.0.0..v1.1.0 -s
# → Xem ai contribute gì giữa 2 releases
```

## 8\. Xem Lịch Sử File

```bash
# Lịch sử thay đổi của 1 file
git log --follow -p src/PaymentService.java
# --follow: theo dõi qua renames
# -p: hiển thị diff

# Số lần thay đổi file
git log --follow --oneline src/PaymentService.java

# Ai thay đổi nhiều nhất
git log --follow --pretty=format:"%an" src/PaymentService.java | sort | uniq -c | sort -rn

# Xem file ở thời điểm commit cụ thể
git show abc1234:src/PaymentService.java

# So sánh file qua 2 commits
git diff abc1234 def5678 -- src/PaymentService.java

# Khi file được tạo/deleted
git log --diff-filter=A --oneline -- src/PaymentService.java  # Added
git log --diff-filter=D --oneline -- src/DeletedFile.java     # Deleted

# --diff-filter options:
# A = Added, C = Copied, D = Deleted, M = Modified
# R = Renamed, T = Type changed, U = Unmerged
# X = Unknown, B = Broken pairing
```

## 9\. Thực Hành: Detective Mode

```bash
# Kịch bản: Production bị lỗi sau deploy, không biết commit nào gây ra

# ─── Bước 1: Xác định "good" và "bad" ───
git log --oneline --since="1 week ago"
# h8i9j0k (HEAD → main) feat: add performance optimizations
# g7h8i9j feat: update payment flow
# f6g7h8i fix: resolve login timeout
# e5f6a7b feat: add email notifications
# d4e5f6a fix: database connection pool
# c3d4e5f feat: add course enrollment v2   ← lần cuối deploy OK

git bisect start
git bisect bad HEAD
git bisect good c3d4e5f

# ─── Bước 2: Test từng commit ───
# Git checkout d4e5f6a...
# Chạy test, bug có không?
git bisect good  # không có bug ở đây

# Git checkout e5f6a7b...
git bisect good  # không có bug

# Git checkout f6g7h8i...
git bisect bad   # bug xuất hiện!

# Git checkout e5f6a7b (giữa d và f)...
git bisect good  # không có bug

# ─── Kết quả ───
# f6g7h8i is the first bad commit
# Author: Nam Nguyen <nam@nguyentienkhoi.hashnode.dev>
# fix: resolve login timeout

# ─── Bước 3: Điều tra commit gây lỗi ───
git bisect reset    # về HEAD

git show f6g7h8i
# Xem diff của commit này

git diff f6g7h8i~1 f6g7h8i -- src/UserService.java
# Tìm chính xác dòng nào gây lỗi

git blame src/UserService.java
# Xem context xung quanh dòng lỗi

# ─── Bước 4: Tìm thêm context ───
# Ai else sửa file này gần đây?
git log --oneline -5 -- src/UserService.java

# Commit này thay đổi gì?
git show f6g7h8i --stat
# Modified: src/UserService.java, src/AuthService.java
# → Bug có thể ở AuthService.java, không phải UserService.java!

git log -S "sessionTimeout" --oneline
# → Tìm commit thêm/sửa sessionTimeout value
```

## 10\. VSCode và IntelliJ — Log Nâng Cao

**VSCode với Git Graph:**

```java
Ctrl+Shift+P → "Git Graph: View Git Graph"

Tính năng:
→ Filter commits: by branch, author, date range, message
→ Search: type vào search box
→ Click commit → xem changed files bên phải
→ Right-click file → "View File Diff" tại commit đó
→ "Compare" 2 commits: select commit 1, Ctrl+click commit 2
→ Blame: hover line trong file view
```

**IntelliJ Git Log:**

```java
View → Tool Windows → Git (Alt+9) → Log

Filter bar phía trên:
  → Branch dropdown: chọn branch
  → User: filter theo author
  → Date: từ/đến
  → Text: search trong message

History của 1 file:
  Right-click file → Git → Show History
  → Timeline của file với diffs
  
Blame + History kết hợp:
  Git → Annotate → hover dòng → click → xem commit
  → Trong commit view: "Show Full History" → xem file history
```

## Tổng Kết

```java
Git Log toolbox:

Tìm ai làm gì:
  git log --author="Nam"
  git blame -L 10,20 file.java

Tìm khi nào:
  git log --since="1 week ago"
  git log -- path/to/file

Tìm gì thay đổi:
  git log -S "keyword"         # code thêm/xóa
  git log -G "regex"           # regex trong diff
  git grep "text"              # text trong current state

Tìm commit gây bug:
  git bisect start
  git bisect bad/good
  git bisect run script.sh

Thống kê:
  git shortlog -sn
  git diff --stat
```


| Command | Dùng khi |
|---|---|
| git log --author | Tìm commits của người cụ thể |
| git log --grep | Tìm theo message |
| git log -S "text" | Tìm commit thêm/xóa text này |
| git log -- file | Xem history của file |
| git blame -L | Ai viết dòng nào |
| git bisect | Binary search tìm commit gây bug |
| git diff --stat | Thống kê thay đổi |
| git shortlog -sn | Đếm commits theo author |



Bài tiếp theo chuyển sang **Advanced section**: `git cherry-pick`, `git reflog` nâng cao và cách recover mọi tình huống tưởng chừng không thể.

