# Multi-modal Vector Search: Tìm Kiếm Bằng Hình Ảnh Với CLIP

![Multi-modal Vector Search- Tìm Kiếm Bằng Hình Ảnh Với CLIP.jpeg](../images/e141edf7-1184-49eb-8847-47ea8ca73d02.jpeg)

Cho đến bây giờ chúng ta mới chỉ embed **text** — title, description, content. Nhưng [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev) còn có hàng nghìn **thumbnail khóa học**, ảnh bài viết, và ảnh profile instructor. **Multi-modal search** cho phép người dùng tìm kiếm bằng hình ảnh: upload thumbnail → tìm khóa học tương tự về chủ đề. Đây là nền tảng của các tính năng AI tiên tiến nhất hiện nay — và CLIP model làm điều này một cách elegant.

## 1\. CLIP Là Gì?

**CLIP (Contrastive Language-Image Pretraining)** của OpenAI là model được train để hiểu mối quan hệ giữa text và image — nó đặt cả hai vào **cùng một vector space**.

```java
Thế giới Text:               Thế giới Image:
  "Java programming"    ←→   [thumbnail Spring Boot]
  "backend development" ←→   [ảnh code Java]
  "cooking recipe"      ←→   [ảnh nồi phở]

Kết quả: "Java programming" gần với [thumbnail Spring Boot]
         hơn là gần với [ảnh nồi phở]
```

**Ứng dụng thực tế:**

*   Upload thumbnail → tìm khóa học cùng chủ đề
    
*   Tìm bài viết bằng hình ảnh minh họa
    
*   "Search by example": chụp ảnh code → tìm tutorial liên quan
    

## 2\. Cài Đặt

```bash
pip install transformers torch pillow requests
pip install open-clip-torch  # OpenCLIP — open source, nhiều model hơn
```

## 3\. CLIP Embedding — Text và Image

```python
import torch
import numpy as np
from PIL import Image
from transformers import CLIPProcessor, CLIPModel
import requests
from io import BytesIO
from typing import Union, List

class CLIPEmbedder:
    """
    Tạo embedding cho cả text lẫn image dùng CLIP.
    Text embedding và image embedding nằm trong cùng vector space
    → có thể so sánh trực tiếp với nhau.
    """

    def __init__(self, model_name: str = "openai/clip-vit-base-patch32"):
        print(f"Loading CLIP model: {model_name}...")
        self.model     = CLIPModel.from_pretrained(model_name)
        self.processor = CLIPProcessor.from_pretrained(model_name)
        self.device    = "cuda" if torch.cuda.is_available() else "cpu"
        self.model.to(self.device)
        self.model.eval()
        self.embedding_dim = 512  # clip-vit-base-patch32 = 512 dims
        print(f"✅ CLIP loaded on {self.device}")

    def embed_text(self, texts: Union[str, List[str]],
                   normalize: bool = True) -> np.ndarray:
        """
        Embed text thành vector 512 chiều.
        Có thể so sánh trực tiếp với image embeddings.
        """
        if isinstance(texts, str):
            texts = [texts]

        inputs = self.processor(
            text=texts,
            return_tensors="pt",
            padding=True,
            truncation=True,
            max_length=77  # CLIP text limit
        ).to(self.device)

        with torch.no_grad():
            features = self.model.get_text_features(**inputs)

        embeddings = features.cpu().numpy()

        if normalize:
            norms      = np.linalg.norm(embeddings, axis=1, keepdims=True)
            embeddings = embeddings / np.maximum(norms, 1e-8)

        return embeddings[0] if len(texts) == 1 else embeddings

    def embed_image(self, images: Union[Image.Image, List[Image.Image], str],
                    normalize: bool = True) -> np.ndarray:
        """
        Embed image thành vector 512 chiều.
        Chấp nhận PIL Image, list of PIL Images, hoặc URL string.
        """
        # Load image nếu là URL
        if isinstance(images, str):
            response = requests.get(images, timeout=10)
            images   = Image.open(BytesIO(response.content)).convert("RGB")

        if isinstance(images, Image.Image):
            images = [images]

        # Convert sang RGB nếu cần
        images = [img.convert("RGB") for img in images]

        inputs = self.processor(
            images=images,
            return_tensors="pt"
        ).to(self.device)

        with torch.no_grad():
            features = self.model.get_image_features(**inputs)

        embeddings = features.cpu().numpy()

        if normalize:
            norms      = np.linalg.norm(embeddings, axis=1, keepdims=True)
            embeddings = embeddings / np.maximum(norms, 1e-8)

        return embeddings[0] if len(images) == 1 else embeddings

    def embed_image_from_url(self, url: str) -> np.ndarray:
        """Convenience method — embed từ URL"""
        try:
            response = requests.get(url, timeout=10)
            image    = Image.open(BytesIO(response.content)).convert("RGB")
            return self.embed_image(image)
        except Exception as e:
            print(f"Failed to load image from {url}: {e}")
            return None

    def similarity(self, embedding1: np.ndarray,
                   embedding2: np.ndarray) -> float:
        """Cosine similarity giữa 2 embeddings đã normalize"""
        return float(np.dot(embedding1, embedding2))


# ──────────────────────────────────────────
# Demo cơ bản
# ──────────────────────────────────────────
clipper = CLIPEmbedder()

# So sánh text với text
java_emb   = clipper.embed_text("Java Spring Boot backend programming")
docker_emb = clipper.embed_text("Docker container deployment")
cooking_emb = clipper.embed_text("cooking Vietnamese food recipe")

print(f"Java vs Docker: {clipper.similarity(java_emb, docker_emb):.4f}")
print(f"Java vs Cooking: {clipper.similarity(java_emb, cooking_emb):.4f}")
# Java vs Docker: 0.7823  ← gần nhau (đều là tech)
# Java vs Cooking: 0.1234 ← xa nhau
```

## 4\. Schema — Lưu CLIP Embeddings

```sql
-- Bảng lưu image embeddings cho course thumbnails
CREATE TABLE course_image_vectors (
    id              BIGSERIAL PRIMARY KEY,
    course_id       BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    image_url       TEXT NOT NULL,
    embedding       VECTOR(512) NOT NULL,   -- CLIP = 512 dims
    model_name      VARCHAR(100) NOT NULL DEFAULT 'clip-vit-base-patch32',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_course_image_vectors UNIQUE (course_id, model_name)
);

-- HNSW index cho cosine similarity
CREATE INDEX idx_course_image_vectors_hnsw
    ON course_image_vectors
    USING hnsw (embedding vector_cosine_ops)
    WITH (m = 16, ef_construction = 64);

-- Bảng lưu cả text embedding (384 dims) và image embedding (512 dims)
-- Dùng cho multi-modal fusion search
CREATE TABLE course_multimodal_vectors (
    id              BIGSERIAL PRIMARY KEY,
    course_id       BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,

    -- Text embedding (từ title + description)
    text_embedding  VECTOR(512),   -- CLIP text = 512 dims
    text_model      VARCHAR(100),

    -- Image embedding (từ thumbnail)
    image_embedding VECTOR(512),   -- CLIP image = 512 dims
    image_model     VARCHAR(100),
    image_url       TEXT,

    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_course_multimodal UNIQUE (course_id)
);

CREATE INDEX idx_course_multimodal_text
    ON course_multimodal_vectors
    USING hnsw (text_embedding vector_cosine_ops)
    WITH (m = 16, ef_construction = 64);

CREATE INDEX idx_course_multimodal_image
    ON course_multimodal_vectors
    USING hnsw (image_embedding vector_cosine_ops)
    WITH (m = 16, ef_construction = 64);
```

## 5\. Multi-modal Embedding Pipeline

```python
import os
import psycopg2
import psycopg2.extras
from PIL import Image
import requests
from io import BytesIO
import logging

logger = logging.getLogger(__name__)

class MultimodalPipeline:
    """
    Pipeline embed cả text và image cho courses.
    """

    def __init__(self):
        self.clipper = CLIPEmbedder("openai/clip-vit-base-patch32")
        self.conn    = psycopg2.connect(
            host=os.getenv("POSTGRES_HOST"),
            port=os.getenv("POSTGRES_PORT"),
            user=os.getenv("POSTGRES_USER"),
            password=os.getenv("POSTGRES_PASSWORD"),
            dbname=os.getenv("POSTGRES_DB")
        )

    def _load_image(self, url: str) -> Image.Image:
        """Load image từ URL với error handling"""
        response = requests.get(url, timeout=10)
        response.raise_for_status()
        image = Image.open(BytesIO(response.content)).convert("RGB")
        return image

    def embed_all_courses(self, batch_size: int = 16) -> int:
        """
        Embed cả text và image cho tất cả published courses.
        """
        cursor = self.conn.cursor(cursor_factory=psycopg2.extras.DictCursor)

        # Lấy courses chưa có multimodal vector
        cursor.execute("""
            SELECT c.id, c.title, c.description, c.thumbnail_url
            FROM courses c
            LEFT JOIN course_multimodal_vectors cmv ON cmv.course_id = c.id
            WHERE c.course_status = 'PUBLISHED'
              AND cmv.id IS NULL
            ORDER BY c.id
        """)

        courses = cursor.fetchall()
        cursor.close()

        if not courses:
            logger.info("All courses already embedded")
            return 0

        logger.info(f"Embedding {len(courses)} courses...")

        embedded = 0
        for i in range(0, len(courses), batch_size):
            batch = courses[i:i + batch_size]

            # Batch text embedding
            texts = [
                f"{c['title']}. {c['title']}. {c['description'] or ''}"
                for c in batch
            ]
            text_embeddings = self.clipper.embed_text(texts)
            if text_embeddings.ndim == 1:
                text_embeddings = text_embeddings.reshape(1, -1)

            # Individual image embedding (có thể fail nếu URL lỗi)
            image_embeddings = []
            image_urls       = []
            for course in batch:
                if course['thumbnail_url']:
                    try:
                        image = self._load_image(course['thumbnail_url'])
                        emb   = self.clipper.embed_image(image)
                        image_embeddings.append(emb)
                        image_urls.append(course['thumbnail_url'])
                    except Exception as e:
                        logger.warning(f"Failed image for course {course['id']}: {e}")
                        image_embeddings.append(None)
                        image_urls.append(None)
                else:
                    image_embeddings.append(None)
                    image_urls.append(None)

            # Upsert vào database
            cursor = self.conn.cursor()
            for j, course in enumerate(batch):
                text_emb  = text_embeddings[j].tolist()
                image_emb = image_embeddings[j].tolist() if image_embeddings[j] is not None else None
                image_url = image_urls[j]

                cursor.execute("""
                    INSERT INTO course_multimodal_vectors
                        (course_id, text_embedding, text_model,
                         image_embedding, image_model, image_url)
                    VALUES (%s, %s, %s, %s, %s, %s)
                    ON CONFLICT (course_id) DO UPDATE SET
                        text_embedding  = EXCLUDED.text_embedding,
                        image_embedding = EXCLUDED.image_embedding,
                        image_url       = EXCLUDED.image_url,
                        updated_at      = NOW()
                """, (
                    course['id'],
                    text_emb,
                    'clip-vit-base-patch32',
                    image_emb,
                    'clip-vit-base-patch32' if image_emb else None,
                    image_url
                ))

            self.conn.commit()
            cursor.close()

            embedded += len(batch)
            logger.info(f"Embedded {embedded}/{len(courses)} courses")

        return embedded
```

## 6\. Multi-modal Search Service

```python
class MultimodalSearchService:
    """
    Search service hỗ trợ:
    1. Text → Courses (semantic search)
    2. Image → Courses (visual similarity)
    3. Text → Courses (CLIP text space)
    4. Fused text + image query
    """

    def __init__(self):
        self.clipper = CLIPEmbedder()
        self.conn    = psycopg2.connect(
            host=os.getenv("POSTGRES_HOST"),
            port=os.getenv("POSTGRES_PORT"),
            user=os.getenv("POSTGRES_USER"),
            password=os.getenv("POSTGRES_PASSWORD"),
            dbname=os.getenv("POSTGRES_DB")
        )

    def search_by_text_clip(self,
                             query: str,
                             limit: int = 10) -> list:
        """
        Tìm khóa học bằng text dùng CLIP text embedding.
        Khác với sentence-transformers: CLIP text và image trong cùng space.
        """
        query_embedding = self.clipper.embed_text(query)
        return self._vector_search("text_embedding", query_embedding, limit)

    def search_by_image(self,
                         image: Union[Image.Image, str],
                         limit: int = 10) -> list:
        """
        Tìm khóa học bằng hình ảnh.
        image: PIL Image hoặc URL string
        """
        if isinstance(image, str):
            if image.startswith("http"):
                image_embedding = self.clipper.embed_image_from_url(image)
            else:
                image_embedding = self.clipper.embed_image(Image.open(image))
        else:
            image_embedding = self.clipper.embed_image(image)

        if image_embedding is None:
            return []

        # Tìm trong image embeddings TRƯỚC
        image_results = self._vector_search(
            "image_embedding", image_embedding, limit
        )

        # Nếu ít kết quả → fallback sang text embedding
        if len(image_results) < limit // 2:
            text_results = self._vector_search(
                "text_embedding", image_embedding, limit
            )
            # Merge và deduplicate
            seen = {r['course_id'] for r in image_results}
            for r in text_results:
                if r['course_id'] not in seen:
                    image_results.append(r)
                    seen.add(r['course_id'])

        return image_results[:limit]

    def search_multimodal_fusion(self,
                                  text_query: str,
                                  image: Optional[Union[Image.Image, str]] = None,
                                  text_weight: float = 0.6,
                                  image_weight: float = 0.4,
                                  limit: int = 10) -> list:
        """
        Fused search: kết hợp text query và image query.
        Dùng khi user cung cấp cả mô tả text VÀ hình ảnh mẫu.
        """
        # Text embedding
        text_emb = self.clipper.embed_text(text_query)

        if image is not None:
            # Image embedding
            if isinstance(image, str):
                image_emb = (self.clipper.embed_image_from_url(image)
                             if image.startswith("http")
                             else self.clipper.embed_image(Image.open(image)))
            else:
                image_emb = self.clipper.embed_image(image)

            if image_emb is not None:
                # Fused embedding: weighted average
                fused = text_emb * text_weight + image_emb * image_weight
                # Normalize
                norm  = np.linalg.norm(fused)
                if norm > 0:
                    fused /= norm
                query_embedding = fused
            else:
                query_embedding = text_emb
        else:
            query_embedding = text_emb

        # Search trong text embeddings (text space thường tốt hơn cho fusion)
        text_results  = self._vector_search("text_embedding", query_embedding, limit)

        # Search trong image embeddings
        image_results = self._vector_search("image_embedding", query_embedding, limit)

        # RRF merge
        return self._rrf_merge(text_results, image_results, limit)

    def _vector_search(self, embedding_column: str,
                        query_embedding: np.ndarray,
                        limit: int) -> list:
        """Generic vector search trên course_multimodal_vectors"""
        cursor = self.conn.cursor(cursor_factory=psycopg2.extras.DictCursor)

        # Skip nếu column là NULL cho tất cả rows
        cursor.execute(f"""
            SELECT
                c.id                                            AS course_id,
                c.title,
                c.slug,
                cat.name                                        AS category,
                COALESCE(cp.price, 0)                          AS price,
                COALESCE(c.rating, 0)                          AS rating,
                (c.course_type = 'FREE')                       AS is_free,
                cmv.image_url,
                1 - (cmv.{embedding_column} <=> %s::vector)   AS similarity
            FROM course_multimodal_vectors cmv
            JOIN courses    c   ON c.id   = cmv.course_id
            JOIN categories cat ON cat.id = c.category_id
            LEFT JOIN course_pricing cp
                   ON cp.course_id = c.id
                  AND cp.currency = 'VND'
                  AND cp.is_active = TRUE
            WHERE c.course_status = 'PUBLISHED'
              AND cmv.{embedding_column} IS NOT NULL
            ORDER BY cmv.{embedding_column} <=> %s::vector
            LIMIT %s
        """, (query_embedding.tolist(), query_embedding.tolist(), limit))

        rows   = cursor.fetchall()
        cursor.close()
        return [dict(row) for row in rows]

    def _rrf_merge(self, list1: list, list2: list,
                    limit: int, k: int = 60) -> list:
        """RRF merge hai result lists"""
        scores: dict = {}
        content: dict = {}

        for rank, item in enumerate(list1, 1):
            cid = item['course_id']
            scores[cid]  = scores.get(cid, 0) + 1.0 / (k + rank)
            content[cid] = item

        for rank, item in enumerate(list2, 1):
            cid = item['course_id']
            scores[cid]  = scores.get(cid, 0) + 1.0 / (k + rank)
            if cid not in content:
                content[cid] = item

        sorted_ids = sorted(scores, key=lambda x: scores[x], reverse=True)
        return [
            {**content[cid], "score": scores[cid]}
            for cid in sorted_ids[:limit]
        ]
```

## 7\. Zero-shot Image Classification

CLIP có thể phân loại hình ảnh mà không cần train thêm — chỉ cần list nhãn:

```python
def classify_thumbnail(clipper: CLIPEmbedder,
                        image: Image.Image,
                        categories: List[str]) -> dict:
    """
    Zero-shot classification: phân loại thumbnail vào category.
    Không cần train thêm — chỉ cần cung cấp tên category.
    """
    # Embed image
    image_emb = clipper.embed_image(image)

    # Embed tất cả category labels
    label_texts = [f"A thumbnail for a {cat} programming course"
                   for cat in categories]
    label_embs  = clipper.embed_text(label_texts)

    # Tính similarity với từng label
    similarities = {
        cat: clipper.similarity(image_emb, label_embs[i])
        for i, cat in enumerate(categories)
    }

    # Sort theo similarity
    ranked = sorted(similarities.items(), key=lambda x: x[1], reverse=True)

    return {
        "top_category":  ranked[0][0],
        "confidence":    round(ranked[0][1], 4),
        "all_scores":    dict(ranked)
    }


# Demo
categories = ["java", "python", "frontend", "devops", "database", "mobile"]

# Test với thumbnail URL
thumbnail = Image.open(requests.get(
    "https://example.com/spring-boot-thumbnail.jpg",
    stream=True
).raw).convert("RGB")

result = classify_thumbnail(clipper, thumbnail, categories)
print(f"Category: {result['top_category']} (confidence: {result['confidence']:.4f})")
print(f"All scores: {result['all_scores']}")
```

## 8\. FastAPI Multi-modal Search API

```python
from fastapi import FastAPI, File, UploadFile, Form, Query
from fastapi.responses import JSONResponse
from PIL import Image
from io import BytesIO
import base64

mm_app = FastAPI(title="Multi-modal Search API")
service = MultimodalSearchService()

@mm_app.get("/search/by-text")
async def search_by_text(
    q:     str = Query(..., min_length=1),
    limit: int = Query(10, ge=1, le=50)
):
    """Text search dùng CLIP text embedding"""
    results = service.search_by_text_clip(q, limit)
    return {"query": q, "results": results}

@mm_app.post("/search/by-image")
async def search_by_image(
    file:  UploadFile = File(...),
    limit: int = Form(10)
):
    """
    Upload ảnh thumbnail → tìm khóa học tương tự về chủ đề.
    """
    # Validate file type
    if not file.content_type.startswith("image/"):
        return JSONResponse(
            status_code=400,
            content={"error": "File phải là hình ảnh"}
        )

    # Load image
    contents = await file.read()
    image    = Image.open(BytesIO(contents)).convert("RGB")

    results = service.search_by_image(image, limit)
    return {
        "filename": file.filename,
        "results":  results
    }

@mm_app.post("/search/multimodal")
async def multimodal_search(
    query:        str        = Form(...),
    file:         UploadFile = File(None),
    text_weight:  float      = Form(0.6),
    image_weight: float      = Form(0.4),
    limit:        int        = Form(10)
):
    """
    Kết hợp text query và image để search.
    """
    image = None
    if file and file.content_type.startswith("image/"):
        contents = await file.read()
        image    = Image.open(BytesIO(contents)).convert("RGB")

    results = service.search_multimodal_fusion(
        text_query   = query,
        image        = image,
        text_weight  = text_weight,
        image_weight = image_weight,
        limit        = limit
    )
    return {"query": query, "has_image": image is not None, "results": results}

@mm_app.post("/classify/thumbnail")
async def classify_thumbnail_endpoint(
    file: UploadFile = File(...)
):
    """
    Zero-shot: phân loại thumbnail vào category tự động.
    Dùng để auto-tag khi instructor upload khóa học mới.
    """
    contents = await file.read()
    image    = Image.open(BytesIO(contents)).convert("RGB")

    categories = ["java", "python", "frontend", "devops",
                  "database", "mobile", "ai", "security"]

    result = classify_thumbnail(service.clipper, image, categories)
    return {
        "suggested_category": result['top_category'],
        "confidence":         result['confidence'],
        "all_scores":         result['all_scores']
    }
```

## 9\. Demo Hoàn Chỉnh

```python
def demo_multimodal():
    service = MultimodalSearchService()
    clipper = CLIPEmbedder()

    print("=" * 60)
    print("MULTI-MODAL SEARCH DEMO")
    print("=" * 60)

    # 1. Text search với CLIP
    print("\n1. Text Search (CLIP):")
    results = service.search_by_text_clip("Java backend REST API")
    for r in results[:3]:
        print(f"  [{r['similarity']:.4f}] {r['title']}")

    # 2. Cross-modal: text vs image similarity
    print("\n2. Cross-modal Similarity:")
    text_emb = clipper.embed_text("Spring Boot Java programming tutorial")

    # Simulate: giả sử có thumbnail URL
    sample_images = {
        "java_thumbnail":   "https://via.placeholder.com/800x450.png?text=Spring+Boot",
        "cooking_thumbnail": "https://via.placeholder.com/800x450.png?text=Cooking",
    }

    for name, url in sample_images.items():
        try:
            img_emb = clipper.embed_image_from_url(url)
            if img_emb is not None:
                sim = clipper.similarity(text_emb, img_emb)
                print(f"  {name}: {sim:.4f}")
        except:
            print(f"  {name}: (skipped)")

    # 3. Zero-shot classification
    print("\n3. Zero-shot Classification:")
    categories = ["java", "python", "frontend", "devops", "database"]
    text_emb   = clipper.embed_text("Docker Kubernetes container orchestration")
    label_embs = clipper.embed_text([
        f"A course thumbnail about {cat}" for cat in categories
    ])

    scores = {
        cat: clipper.similarity(text_emb, label_embs[i])
        for i, cat in enumerate(categories)
    }
    ranked = sorted(scores.items(), key=lambda x: x[1], reverse=True)
    print(f"  Query: 'Docker Kubernetes container orchestration'")
    for cat, score in ranked:
        bar = "█" * int(score * 20)
        print(f"  {cat:<12} {score:.4f} {bar}")

asyncio.run(demo_multimodal()) if __name__ == "__main__" else None
```

## 10\. CLIP Model Comparison


| Model | Dims | Size | Speed | Quality | Best For |
|---|---|---|---|---|---|
| clip-vit-base-patch32 | 512 | 350MB | Fast | Good | General use, production |
| clip-vit-base-patch16 | 512 | 350MB | Medium | Better | Higher quality images |
| clip-vit-large-patch14 | 768 | 890MB | Slow | Best | Max quality |
| openai/clip-vit-large-patch14-336 | 768 | 890MB | Slowest | Best+ | High-res images |



**OpenCLIP alternatives (open source, nhiều model hơn):**

```python
import open_clip

# Nhiều lựa chọn model hơn
model, _, preprocess = open_clip.create_model_and_transforms(
    'ViT-B-32',
    pretrained='laion2b_s34b_b79k'  # train trên LAION-2B
)
tokenizer = open_clip.get_tokenizer('ViT-B-32')
```

## Tổng Kết


| Tính năng | Cách làm |
|---|---|
| Text → Courses | CLIP text embedding → cosine search |
| Image → Courses | CLIP image embedding → cosine search |
| Text + Image | Weighted average của 2 embeddings → search |
| Auto classify | Zero-shot: compare image với label text embeddings |
| Cross-modal | Text embedding có thể compare với image embedding trực tiếp |



```java
CLIP Magic:
  "Spring Boot Java"    → [0.82, -0.15, ...]
       ↕ comparable!
  [thumbnail Spring Boot] → [0.79, -0.18, ...]

  cos_sim = 0.95 ← text và image về cùng chủ đề → rất gần nhau
```

Bài tiếp theo chúng ta sẽ học **Time-series Database** — InfluxDB và TimescaleDB để lưu trữ AI model metrics, user behavior tracking và monitoring dữ liệu theo thời gian.

