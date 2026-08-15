# Git Là Gì? Tại Sao Mọi Developer Đều Cần Version Control?

![Tại Sao Mọi Developer Đều Cần Version Control?.jpeg](../images/e0175cf5-2093-47eb-a0cc-6ef5b7b351bf.jpeg)

Hãy tưởng tượng bạn đang code dự án [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev) một mình. Mọi thứ chạy tốt. Rồi một ngày bạn quyết định refactor module thanh toán — xóa file cũ, viết lại từ đầu. Hai ngày sau bạn nhận ra cách cũ lại tốt hơn. Không có cách nào quay lại. File đã mất. Code đã mất. Đây chính xác là vấn đề mà **Version Control** giải quyết — và **Git** là công cụ làm điều đó tốt nhất hiện nay.

## 1\. Version Control Là Gì?

**Version Control System (VCS)** là hệ thống theo dõi mọi thay đổi của code theo thời gian. Nó giống như một cỗ máy thời gian cho code:

```java
Không có VCS:
  code_v1.java
  code_v2.java
  code_v2_final.java
  code_v2_final_FINAL.java
  code_v2_final_FINAL_fix.java   ← developer humor tự nhiên

Với VCS (Git):
  payment.java  ← chỉ 1 file
  + toàn bộ lịch sử mọi thay đổi
  + có thể xem/phục hồi bất kỳ version nào
  + biết ai thay đổi gì, khi nào, tại sao
```

**Những vấn đề VCS giải quyết:**

```java
1. "Tôi lỡ xóa file quan trọng"
   → Git: git checkout HEAD~1 -- file.java

2. "Code mới bị lỗi, muốn quay lại bản cũ"
   → Git: git revert abc1234

3. "Hai người cùng sửa cùng file"
   → Git: merge, resolve conflict

4. "Không biết ai viết đoạn code này"
   → Git: git blame file.java

5. "Bug này xuất hiện từ commit nào?"
   → Git: git bisect
```

## 2\. Git Là Gì?

**Git** là Distributed Version Control System (DVCS) — hệ thống quản lý version **phân tán**.

Tạo ra năm **2005** bởi **Linus Torvalds** (người tạo ra Linux kernel) — khi hệ thống VCS cũ (BitKeeper) bị thu hồi license, Linus tự viết một hệ thống mới trong vòng 2 tuần.

**"Distributed" nghĩa là gì?**

```java
Centralized VCS (SVN):
  Developer A ──┐
  Developer B ──┼──► Central Server (source of truth)
  Developer C ──┘
  → Không có internet = không làm việc được
  → Server down = tất cả bị block

Distributed VCS (Git):
  Developer A [full copy]
  Developer B [full copy] ──► GitHub/GitLab (remote)
  Developer C [full copy]
  → Mỗi người có full copy của toàn bộ lịch sử
  → Offline vẫn commit, branch, merge được
  → Server down = vẫn làm việc local
```

## 3\. Git vs SVN — Khi Nào Dùng Cái Nào?


|  | Git | SVN |
|---|---|---|
| Architecture | Distributed | Centralized |
| Offline work | ✅ Full | ❌ Giới hạn |
| Branching | Nhanh, lightweight | Chậm, tốn storage |
| Learning curve | Cao hơn | Thấp hơn |
| Ecosystem | GitHub, GitLab, Bitbucket | Apache SVN server |
| Industry standard | ✅ Gần như 100% | Dần bị thay thế |
| Binary files | Kém hơn | Tốt hơn |



**Thực tế 2025:** Git là **industry standard**. 93%+ developer dùng Git theo khảo sát Stack Overflow. SVN vẫn tồn tại ở một số doanh nghiệp lớn (ngân hàng, tổ chức cũ) nhưng xu hướng đều đang migrate sang Git.

## 4\. Cài Đặt Git

### Windows

**Cách 1: Git for Windows (khuyến nghị)**

1.  Tải tại: [https://git-scm.com/download/win](https://git-scm.com/download/win)
    
2.  Chạy installer, chọn các options:
    
    *   **Default editor**: Visual Studio Code (nếu đã cài)
        
    *   **Git from command line**: "Git from the command line and also from 3rd-party software"
        
    *   **Line ending**: "Checkout Windows-style, commit Unix-style"
        

**Cách 2: Winget**

### macOS

```bash
# Cách 1: Homebrew (khuyến nghị)
brew install git

# Cách 2: Xcode Command Line Tools
xcode-select --install
# → Tự động cài Git cùng với dev tools
```

### Linux (Ubuntu/Debian)

```bash
sudo apt update && sudo apt install git -y
```

### Kiểm Tra

```bash
git --version
# git version 2.43.0
```

## 5\. Cấu Hình Lần Đầu

Trước khi dùng Git, cần cấu hình **tên và email** — thông tin này gắn vào mọi commit bạn tạo.

```bash
# Cấu hình global (áp dụng cho tất cả repos trên máy)
git config --global user.name "Nguyen Van Nam"
git config --global user.email "nam@nguyentienkhoi.hashnode.dev"

# Cấu hình editor mặc định
git config --global core.editor "code --wait"      # VSCode
# git config --global core.editor "idea --wait"    # IntelliJ
# git config --global core.editor "vim"            # Vim

# Cấu hình default branch name (khuyến nghị "main" thay vì "master")
git config --global init.defaultBranch main

# Cấu hình line endings (quan trọng khi làm việc cross-platform)
# Windows:
git config --global core.autocrlf true
# macOS/Linux:
git config --global core.autocrlf input

# Xem tất cả config
git config --list

# Xem config của từng key
git config user.name
# → Nguyen Van Nam
```

**File config nằm ở đâu:**

```bash
# Global config: ~/.gitconfig
cat ~/.gitconfig
# [user]
#     name = Nguyen Van Nam
#     email = nam@nguyentienkhoi.hashnode.dev
# [core]
#     editor = code --wait

# Local config (per-repo): .git/config
# Ghi đè global config cho repo này
git config --local user.email "work@company.com"
```

## 6\. Tích Hợp VSCode với Git

VSCode có **Source Control** panel tích hợp sẵn — không cần cài thêm gì.

**Mở Source Control:**

*   Click icon Source Control ở sidebar (Ctrl+Shift+G)
    
*   Hoặc: View → Source Control
    

**Extensions hữu ích:**

```java
GitLens (bắt buộc cài):
  → Xem ai viết từng dòng code (blame inline)
  → History của từng file/line
  → So sánh branches

Git Graph:
  → Visualize branch tree đẹp hơn
  → Click để xem commit details

GitHub Pull Requests:
  → Review PR ngay trong VSCode
```

**Cài extensions:**

```java
Ctrl+Shift+X → tìm "GitLens" → Install
```

## 7\. Tích Hợp IntelliJ với Git

IntelliJ IDEA có Git integration rất mạnh — **không cần cài thêm plugin**.

**Setup:**

1.  File → Settings → Version Control → Git
    
2.  Path to Git executable: tự detect hoặc set manual
    
3.  Test: click "Test" button
    

**Giao diện chính:**

```java
View → Tool Windows → Git  (hoặc Alt+9)
  → Log tab: xem toàn bộ commit history với graph
  → Console tab: xem git commands đang chạy

View → Tool Windows → Commit  (Ctrl+K)
  → Staging area visual
  → Diff viewer
```

## 8\. Quy Trình Làm Việc Cơ Bản

Đây là flow mà mọi developer đều làm hàng ngày:

```java
1. Clone repo về (lần đầu)
   git clone https://github.com/tayjava/backend.git

2. Tạo branch mới cho feature
   git checkout -b feature/payment-module

3. Code, code, code...

4. Xem những gì đã thay đổi
   git status
   git diff

5. Stage các thay đổi muốn commit
   git add src/payment/PaymentService.java
   git add .  ← tất cả

6. Commit với message có ý nghĩa
   git commit -m "feat: add VNPay payment integration"

7. Push lên remote
   git push origin feature/payment-module

8. Tạo Pull Request trên GitHub/GitLab
   → Team review code
   → Merge vào main branch
```

## 9\. Tại Sao Git Quan Trọng Với Mọi Developer?

**Không có Git:**

```java
❌ Không thể làm việc nhóm hiệu quả
❌ Code review = gửi file qua email/Slack
❌ Deploy = copy file thủ công
❌ Bug fix = không biết code nào bị thay đổi
❌ "Works on my machine" syndrome không có cách debug
```

**Có Git:**

```java
✅ Làm việc nhóm song song không conflict
✅ Code review qua Pull Request có context đầy đủ
✅ CI/CD trigger tự động từ git push
✅ Audit trail: biết ai thay đổi gì, khi nào, tại sao
✅ Rollback production trong < 5 phút
✅ Feature flags qua feature branches
```

**Git trong career developer:**

*   Phỏng vấn: hầu hết công ty hỏi về Git workflow
    
*   Daily work: dùng Git ít nhất 5-10 lần mỗi ngày
    
*   Senior indicator: biết dùng Git nâng cao (rebase, bisect, hooks)
    

## 10\. Thực Hành: Repository Đầu Tiên

```bash
# Tạo thư mục project
mkdir foxdev-demo
cd foxdev-demo

# Khởi tạo Git repo
git init
# Initialized empty Git repository in /path/to/foxdev-demo/.git/

# Tạo file đầu tiên
echo "# FoxDev Demo" > README.md

# Xem trạng thái
git status
# On branch main
# No commits yet
# Untracked files:
#   README.md

# Stage file
git add README.md

# Commit đầu tiên
git commit -m "initial commit: add README"
# [main (root-commit) abc1234] initial commit: add README
# 1 file changed, 1 insertion(+)
# create mode 100644 README.md

# Xem lịch sử
git log
# commit abc1234...
# Author: Nguyen Van Nam <nam@nguyentienkhoi.hashnode.dev>
# Date:   Mon Mar 15 2025
#
#     initial commit: add README
```

## Tổng Kết


| Khái niệm | Ý nghĩa |
|---|---|
| Version Control | Theo dõi lịch sử thay đổi của code |
| Git | Distributed VCS — phổ biến nhất hiện nay |
| Distributed | Mỗi developer có full copy, offline vẫn làm được |
| Repository (repo) | Thư mục chứa code + toàn bộ lịch sử Git |
| Commit | Một snapshot của code tại một thời điểm |



```java
Git lifecycle cơ bản:
  Working Directory → Staging Area → Repository
  (edit files)         (git add)      (git commit)
```

Bài tiếp theo chúng ta sẽ tìm hiểu sâu hơn về **3 trạng thái của Git**: Working Tree, Staging Area và Repository — cùng với các khái niệm cốt lõi như HEAD, blob, tree và cách Git thực sự lưu trữ data.

