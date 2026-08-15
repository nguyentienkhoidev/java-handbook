# Submodule & Subtree: Quản Lý Code Chia Sẻ Giữa Nhiều Repos

![Submodule & Subtree- Quản Lý Code Chia Sẻ Giữa Nhiều Repos.jpeg](../images/3c6ac022-703b-4ffd-a04f-afde59b54477.jpeg)

Khi dự án lớn lên, bạn sẽ gặp bài toán: "Làm sao share một thư viện dùng chung giữa nhiều projects?" Hoặc: "Backend và Frontend nên là 1 repo hay 2 repo?" Bài này giải quyết cả hai bài toán với **Git Submodule** (tham chiếu đến repo khác) và **Git Subtree** (nhúng repo khác vào như một thư mục), cùng với so sánh các chiến lược **monorepo vs multi-repo** thực tế.

## 1\. Bài Toán Cần Giải Quyết

```java
nguyentienkhoi.hashnode.dev có 3 projects:
  foxdev-backend   (Spring Boot API)
  foxdev-frontend  (React/Next.js)
  foxdev-shared    (DTOs, constants, utilities dùng chung)

Vấn đề:
  Backend và Frontend đều cần foxdev-shared
  Làm sao sync code shared giữa 2 projects?

Các cách giải quyết:
  1. Copy-paste → manual, inconsistent, nightmare
  2. NPM/Maven package → overhead, chậm khi iterate
  3. Git Submodule → reference đến repo khác
  4. Git Subtree → nhúng repo khác vào
  5. Monorepo → tất cả trong 1 repo
```

## 2\. Git Submodule

**Submodule** = tham chiếu đến một commit cụ thể của repo khác, nhúng vào như một "sub-directory" trong repo cha.

```java
foxdev-backend/
  src/
  pom.xml
  shared/        ← đây là submodule trỏ đến foxdev-shared repo
    dto/
    constants/
  .gitmodules    ← file cấu hình submodules
```

### 2.1 Thêm Submodule

```bash
# Cấu trúc: git submodule add <url> <path>
cd foxdev-backend

git submodule add git@github.com:tayjava/tayjava-shared.git shared
# Cloning into 'foxdev-backend/shared'...
# → Tạo thư mục shared/
# → Tạo file .gitmodules
# → Thêm entry trong .git/config

# Xem .gitmodules được tạo ra
cat .gitmodules
# [submodule "shared"]
#     path = shared
#     url = git@github.com:tayjava/tayjava-shared.git
#     branch = main   ← branch mặc định (nếu thêm --branch)

# Commit
git add .gitmodules shared
git commit -m "chore: add foxdev-shared as submodule"
```

### 2.2 Clone Repo Có Submodule

```bash
# Clone bình thường → submodule directory rỗng!
git clone git@github.com:tayjava/tayjava-backend.git
cd foxdev-backend
ls shared/   # → empty!

# Cách 1: Clone và init submodules cùng lúc
git clone --recurse-submodules git@github.com:tayjava/tayjava-backend.git

# Cách 2: Init sau khi clone
git clone git@github.com:tayjava/tayjava-backend.git
cd foxdev-backend
git submodule init      # đọc .gitmodules, register submodules
git submodule update    # clone submodule repos

# Cách 3: Cả 2 bước một lúc
git submodule update --init
git submodule update --init --recursive  # nested submodules
```

### 2.3 Update Submodule

```bash
# ─── Cập nhật submodule lên commit mới nhất ───

# Vào thư mục submodule và pull
cd shared
git pull origin main
cd ..

# Hoặc từ repo cha (update tất cả submodules)
git submodule update --remote
git submodule update --remote shared   # chỉ 1 submodule

# ─── Xem trạng thái submodule ───
git submodule status
# +abc1234 shared (v1.2.0-3-gabc1234)
# → + = có thay đổi chưa commit
# → abc1234 = commit hash submodule đang trỏ đến
# → v1.2.0-3 = tag và số commits sau tag

git submodule foreach 'git status'
# → Chạy command trong từng submodule

# ─── Commit sau khi update submodule ───
# Sau khi update, repo cha cần commit để lưu "submodule đang trỏ commit nào"
git add shared
git status
# modified: shared  ← ghi nhận commit mới của submodule

git commit -m "chore: update foxdev-shared to v1.3.0"
```

### 2.4 Làm Việc Với Submodule

```bash
# Vào submodule để code
cd shared
git switch -c feature/add-payment-dto
# ... code ...
git add . && git commit -m "feat: add PaymentDTO"
git push origin feature/add-payment-dto

# Quay về repo cha
cd ..
git add shared   # ghi nhận commit mới của submodule
git commit -m "chore: update shared to include PaymentDTO"

# ─── Push cả submodule và parent ───
# Luôn push submodule TRƯỚC parent
cd shared && git push && cd ..
git push

# Tự động kiểm tra submodule đã được push chưa
git push --recurse-submodules=check
# → Fail nếu submodule có commits chưa push

git push --recurse-submodules=on-demand
# → Tự động push submodule trước
```

### 2.5 Xóa Submodule

```bash
# Xóa submodule (nhiều bước hơn thêm)
git submodule deinit shared        # xóa config trong .git/config
git rm shared                      # xóa thư mục và .gitmodules entry
rm -rf .git/modules/shared         # xóa cached data
git commit -m "chore: remove foxdev-shared submodule"
```

## 3\. Git Subtree

**Subtree** = nhúng nội dung của repo khác trực tiếp vào thư mục trong repo cha. Không có tracking file riêng — chỉ là code bình thường.

```java
Khác biệt cơ bản:
  Submodule: repo cha chứa THAM CHIẾU đến repo khác (commit hash)
  Subtree:   repo cha chứa TOÀN BỘ CODE của repo khác
```

### 3.1 Thêm Subtree

```bash
cd foxdev-backend

# Thêm remote cho shared repo
git remote add shared-remote git@github.com:tayjava/tayjava-shared.git

# Fetch
git fetch shared-remote

# Thêm subtree
git subtree add --prefix=shared shared-remote main --squash
# --prefix: thư mục đích
# --squash: gộp tất cả commits của shared thành 1 commit

# Output:
# git fetch shared-remote main
# From github.com:tayjava/tayjava-shared
#  * branch            main       -> FETCH_HEAD
# Added dir 'shared'

# Không cần --squash nếu muốn giữ toàn bộ history
git subtree add --prefix=shared shared-remote main
```

### 3.2 Update Subtree

```bash
# Pull changes từ shared repo vào
git subtree pull --prefix=shared shared-remote main --squash
# → Tạo merge commit: "Merge commit 'abc...' into main"

# Fetch rồi mới pull
git fetch shared-remote
git subtree pull --prefix=shared shared-remote main
```

### 3.3 Push Changes Ngược Lại

```bash
# Nếu sửa code trong thư mục shared/
# Muốn push thay đổi ngược lại về shared repo
git subtree push --prefix=shared shared-remote feature/payment-dto

# → Tạo branch feature/payment-dto trên shared-remote
# → Chứa các commits liên quan đến thư mục shared/
```

## 4\. Submodule vs Subtree — So Sánh


| Tiêu chí | Submodule | Subtree |
|---|---|---|
| Storage | Tham chiếu (hash) | Copy toàn bộ code |
| Clone | Cần --recurse-submodules hoặc submodule update | Bình thường |
| Update | submodule update --remote | subtree pull |
| Push ngược | Vào thư mục submodule, push trực tiếp | subtree push |
| Complexity | Cao hơn, dễ nhầm | Thấp hơn |
| History | Tách biệt | Hợp nhất (hoặc squash) |
| Contributor | Cần quyền push submodule repo | Không cần |
| Dependency tracking | Explicit, rõ ràng | Ẩn trong commits |
| Best for | Team riêng quản lý shared lib | Simple code sharing |



**Khi nào dùng cái nào:**

```java
Submodule phù hợp:
  ✅ Shared library có team riêng
  ✅ Muốn explicit versioning (biết đang dùng version nào)
  ✅ Cần track changes của shared lib riêng biệt
  ✅ Nhiều repos đều dùng chung (3+ repos)

Subtree phù hợp:
  ✅ Chỉ 1-2 repos dùng shared code
  ✅ Team nhỏ, không muốn phức tạp
  ✅ Contributor không có quyền push shared repo
  ✅ Muốn simple workflow (không cần submodule commands)
```

## 5\. Monorepo vs Multi-repo

### Multi-repo (Polyrepo)

```java
Mỗi service/project là 1 repo riêng biệt

foxdev-backend/   (repo 1)
foxdev-frontend/  (repo 2)
foxdev-shared/    (repo 3)
foxdev-devops/    (repo 4)

Ưu điểm:
  ✅ Mỗi team tự quản lý repo của mình
  ✅ Permissions độc lập (dev frontend không cần đọc backend)
  ✅ CI/CD nhẹ hơn (chỉ build repo thay đổi)
  ✅ History sạch sẽ, tập trung

Nhược điểm:
  ❌ Khó sync khi shared code thay đổi
  ❌ Cross-repo refactoring rất khó
  ❌ Khó tìm "full picture" của một feature
  ❌ Dependency management phức tạp
```

### Monorepo

```java
Tất cả trong 1 repo

foxdev/
  backend/         (Spring Boot)
  frontend/        (Next.js)
  shared/          (shared code)
  devops/          (infra, docker-compose)
  docs/            (documentation)

Ưu điểm:
  ✅ Atomic commits across services
  ✅ Single source of truth
  ✅ Cross-repo refactoring dễ dàng
  ✅ Dependency luôn sync (shared thay đổi = tất cả thấy ngay)
  ✅ Dễ tìm "full picture" của feature

Nhược điểm:
  ❌ Repo lớn → clone, fetch chậm hơn
  ❌ CI/CD phải thông minh (chỉ build phần thay đổi)
  ❌ Cần tools riêng: Nx, Turborepo, Bazel...
  ❌ Khó granular permissions
```

### Cấu Trúc Monorepo Thực Tế

```bash
# Tạo monorepo cho nguyentienkhoi.hashnode.dev
mkdir foxdev && cd foxdev
git init

# Cấu trúc thư mục
mkdir -p backend/src/main/java/com/foxdev
mkdir -p frontend/src
mkdir -p shared/src
mkdir -p devops/docker
mkdir -p docs

# Root-level files
cat > .gitignore << 'EOF'
# Backend
backend/target/
backend/.idea/

# Frontend
frontend/node_modules/
frontend/.next/
frontend/.env.local

# Shared
shared/target/

# Common
*.class
.DS_Store
*.log
EOF

cat > README.md << 'EOF'
# FoxDev Monorepo

## Projects
- `backend/`   - Spring Boot API
- `frontend/`  - Next.js web app
- `shared/`    - Shared DTOs and utilities
- `devops/`    - Infrastructure and deployment
- `docs/`      - Documentation
EOF

# Root pom.xml cho Maven multi-module (backend)
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.foxdev</groupId>
    <artifactId>foxdev-root</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>shared</module>
        <module>backend</module>
    </modules>
</project>
EOF

git add .
git commit -m "chore: initialize foxdev monorepo structure"
```

### Monorepo — Branch Strategy

```bash
# Branches trong monorepo vẫn dùng convention + prefix project

feature/backend/payment-vnpay
feature/frontend/checkout-page
feature/shared/add-payment-dto
fix/backend/auth-token-expiry
chore/devops/update-docker-compose

# Hoặc thêm scope vào Conventional Commits
git commit -m "feat(backend/payment): add VNPay integration"
git commit -m "feat(frontend/checkout): add payment UI"
git commit -m "feat(shared): add PaymentRequestDTO"
```

## 6\. Sparse Checkout — Clone Chỉ Một Phần Monorepo

Khi monorepo quá lớn, developer frontend không cần clone toàn bộ backend code:

```bash
# Sparse checkout: chỉ lấy thư mục cần thiết
git clone --filter=blob:none --sparse git@github.com:tayjava/tayjava.git
cd foxdev

# Chỉ checkout frontend và shared
git sparse-checkout init --cone
git sparse-checkout set frontend shared docs

# Xem những gì đang được checkout
git sparse-checkout list
# frontend
# shared
# docs

# Thêm thư mục sau
git sparse-checkout add devops

# Clone nhanh không lấy history (shallow)
git clone --depth=1 --filter=blob:none --sparse git@github.com:tayjava/tayjava.git
```

## 7\. Thực Hành Tổng Hợp

```bash
# ─── Kịch bản: Setup multi-repo với submodule ───

# 1. Tạo shared repo
mkdir foxdev-shared && cd foxdev-shared
git init
mkdir -p src/main/java/com/foxdev/shared/dto

cat > src/main/java/com/foxdev/shared/dto/CourseDTO.java << 'EOF'
package com.foxdev.shared.dto;

public class CourseDTO {
    private Long id;
    private String title;
    private Double price;
    // getters, setters...
}
EOF

cat > src/main/java/com/foxdev/shared/dto/UserDTO.java << 'EOF'
package com.foxdev.shared.dto;

public class UserDTO {
    private Long id;
    private String email;
    private String name;
    // getters, setters...
}
EOF

git add . && git commit -m "feat: add CourseDTO and UserDTO"
git tag -a v1.0.0 -m "Initial shared library"

# Push lên remote (giả sử đã tạo repo trên GitHub)
git remote add origin git@github.com:tayjava/tayjava-shared.git
git push -u origin main --tags

# 2. Backend dùng shared như submodule
cd ../foxdev-backend
git submodule add git@github.com:tayjava/tayjava-shared.git shared
git commit -m "chore: add foxdev-shared v1.0.0 as submodule"

# 3. Frontend cũng dùng shared (JavaScript version)
cd ../foxdev-frontend
git submodule add git@github.com:tayjava/tayjava-shared.git shared
git commit -m "chore: add foxdev-shared v1.0.0 as submodule"

# ─── Developer workflow với submodule ───

# 4. Thêm tính năng vào shared
cd ../foxdev-shared
cat > src/main/java/com/foxdev/shared/dto/PaymentDTO.java << 'EOF'
package com.foxdev.shared.dto;

public class PaymentDTO {
    private String orderId;
    private Double amount;
    private String currency;
    // getters, setters...
}
EOF

git add . && git commit -m "feat: add PaymentDTO"
git tag -a v1.1.0 -m "Add PaymentDTO"
git push origin main --tags

# 5. Backend update submodule
cd ../foxdev-backend
git submodule update --remote shared
git diff shared
# Subproject commit abc1234  (v1.0.0)
# +Subproject commit def5678  (v1.1.0)

git add shared
git commit -m "chore: update foxdev-shared to v1.1.0 (add PaymentDTO)"
git push
```

## Tổng Kết

```java
Submodule:
  → Tham chiếu đến commit của repo khác
  → Clone cần: --recurse-submodules hoặc submodule update --init
  → Update: submodule update --remote
  → Best for: shared library với team riêng, 3+ repos

Subtree:
  → Copy code của repo khác vào thư mục
  → Clone bình thường, không cần extra steps
  → Update: subtree pull
  → Best for: simple sharing, team nhỏ

Monorepo:
  → Tất cả trong 1 repo
  → Atomic commits, dễ sync
  → Cần tooling: sparse checkout cho repo lớn
  → Best for: tightly coupled projects, small-medium teams
```


| Command | Tác dụng |
|---|---|
| git submodule add <url> <path> | Thêm submodule |
| git clone --recurse-submodules | Clone kèm submodules |
| git submodule update --init | Init + clone submodules sau khi clone |
| git submodule update --remote | Update submodule lên latest |
| git submodule foreach 'cmd' | Chạy command trong tất cả submodules |
| git subtree add --prefix | Thêm subtree |
| git subtree pull --prefix | Update subtree |
| git subtree push --prefix | Push changes về repo gốc |
| git sparse-checkout set | Chỉ checkout một số thư mục |



Bài tiếp theo chúng ta sẽ học **Git Hooks** — tự động hóa kiểm tra code, format, test trước khi commit và push, tích hợp với Husky và commitlint.

