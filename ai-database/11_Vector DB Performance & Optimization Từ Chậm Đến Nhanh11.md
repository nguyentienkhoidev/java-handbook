# Vector DB Performance & Optimization: Từ Chậm Đến Nhanh

![Vector DB Performance & Optimization- Từ Chậm Đến Nhanh.jpeg](../images/962851e7-09f7-4bee-805b-01f0be2f3e4d.jpeg)

Bạn đã xây dựng được search, RAG và recommendation. Bây giờ câu hỏi thực tế là: **hệ thống có đủ nhanh cho production không?** 100ms search latency có thể chấp nhận được, nhưng 2 giây thì không. Bài này sẽ đi qua benchmark thực tế, HNSW/IVF tuning, quantization trade-offs, caching strategies và monitoring — tất cả những gì cần để Vector DB chạy mượt mà ở quy mô lớn.

## 1\. Benchmark — Đo Lường Trước Khi Tối Ưu

```python
import time
import statistics
import numpy as np
from typing import List, Callable, Dict, Any
from sentence_transformers import SentenceTransformer
from qdrant_client import QdrantClient
import psycopg2

def benchmark_function(
    func: Callable,
    args: tuple = (),
    kwargs: dict = None,
    n_runs: int = 100,
    warmup: int = 10
) -> Dict[str, float]:
    """
    Benchmark một function với n_runs lần.
    Bỏ qua warmup runs đầu tiên.
    """
    kwargs   = kwargs or {}
    latencies = []

    # Warmup
    for _ in range(warmup):
        func(*args, **kwargs)

    # Actual benchmark
    for _ in range(n_runs):
        start = time.perf_counter()
        func(*args, **kwargs)
        end   = time.perf_counter()
        latencies.append((end - start) * 1000)  # convert to ms

    return {
        "min_ms":    round(min(latencies), 2),
        "max_ms":    round(max(latencies), 2),
        "mean_ms":   round(statistics.mean(latencies), 2),
        "median_ms": round(statistics.median(latencies), 2),
        "p95_ms":    round(np.percentile(latencies, 95), 2),
        "p99_ms":    round(np.percentile(latencies, 99), 2),
        "stdev_ms":  round(statistics.stdev(latencies), 2),
    }


class VectorDBBenchmark:

    def __init__(self):
        self.model  = SentenceTransformer("paraphrase-multilingual-MiniLM-L12-v2")
        self.qdrant = QdrantClient(host="localhost", port=6333)
        self.conn   = psycopg2.connect(
            host="localhost", port=5432,
            user="postgres", password="postgres",
            dbname="foxdev_ai"
        )

        # Pre-encode queries để loại bỏ embedding time khỏi search benchmark
        self.test_queries = [
            "học lập trình Java backend",
            "docker kubernetes deployment",
            "SQL database optimization",
            "ReactJS frontend development",
            "microservices architecture",
        ]
        self.query_vectors = self.model.encode(
            self.test_queries, normalize_embeddings=True
        )

    def benchmark_qdrant_search(self,
                                 collection: str,
                                 n_runs: int = 200) -> Dict:
        """Benchmark Qdrant search"""
        import random

        def search():
            vec = self.query_vectors[random.randint(0, len(self.query_vectors)-1)]
            return self.qdrant.search(
                collection_name=collection,
                query_vector=vec.tolist(),
                limit=10
            )

        return benchmark_function(search, n_runs=n_runs)

    def benchmark_pgvector_search(self,
                                   index_type: str,
                                   n_runs: int = 200) -> Dict:
        """Benchmark pgvector search — so sánh HNSW vs IVF vs No Index"""
        import random
        cursor = self.conn.cursor()

        # Set index type
        if index_type == "hnsw":
            cursor.execute("SET hnsw.ef_search = 100")
        elif index_type == "ivf":
            cursor.execute("SET ivfflat.probes = 10")
        elif index_type == "none":
            cursor.execute("SET enable_indexscan = off")

        def search():
            vec = self.query_vectors[random.randint(0, len(self.query_vectors)-1)]
            cursor.execute("""
                SELECT course_id, 1 - (embedding <=> %s::vector) AS score
                FROM course_vectors
                ORDER BY embedding <=> %s::vector
                LIMIT 10
            """, (vec.tolist(), vec.tolist()))
            return cursor.fetchall()

        result = benchmark_function(search, n_runs=n_runs)

        # Reset
        cursor.execute("SET enable_indexscan = on")
        cursor.close()

        return result

    def run_full_benchmark(self):
        print("=" * 65)
        print("VECTOR DB BENCHMARK")
        print("=" * 65)

        # pgvector comparison
        print("\n📊 pgvector — So sánh các Index Types")
        print(f"{'Method':<20} {'Median':>8} {'P95':>8} {'P99':>8}")
        print("-" * 50)

        for idx_type in ["none", "hnsw", "ivf"]:
            result = self.benchmark_pgvector_search(idx_type)
            print(f"{idx_type:<20} {result['median_ms']:>7.1f}ms "
                  f"{result['p95_ms']:>7.1f}ms "
                  f"{result['p99_ms']:>7.1f}ms")

        # Qdrant comparison
        print("\n📊 Qdrant — So sánh các Collection configs")
        print(f"{'Collection':<25} {'Median':>8} {'P95':>8} {'P99':>8}")
        print("-" * 55)

        for collection in ["foxdev_courses", "foxdev_courses_sq",
                           "foxdev_courses_bq"]:
            try:
                result = self.benchmark_qdrant_search(collection)
                print(f"{collection:<25} {result['median_ms']:>7.1f}ms "
                      f"{result['p95_ms']:>7.1f}ms "
                      f"{result['p99_ms']:>7.1f}ms")
            except Exception as e:
                print(f"{collection:<25} ERROR: {e}")


# Chạy benchmark
benchmark = VectorDBBenchmark()
benchmark.run_full_benchmark()
```

**Kết quả mong đợi (10k vectors):**

```java
📊 pgvector — So sánh các Index Types
Method               Median      P95      P99
--------------------------------------------------
none (brute force)   45.2ms   52.3ms   58.1ms
hnsw                  1.2ms    1.8ms    2.4ms
ivf                   2.1ms    2.9ms    3.5ms

📊 Qdrant — So sánh các Collection configs
Collection                Median      P95      P99
-------------------------------------------------------
foxdev_courses (float32)  0.8ms    1.2ms    1.6ms
foxdev_courses_sq (SQ8)   0.5ms    0.8ms    1.1ms  ← 4x nhỏ hơn RAM
foxdev_courses_bq (BQ)    0.3ms    0.5ms    0.7ms  ← 32x nhỏ hơn RAM
```

## 2\. HNSW Tuning

### 2.1 Ảnh Hưởng Của Tham Số ef\_search

```python
def benchmark_ef_search(pgconn, query_vectors: np.ndarray) -> None:
    """
    Đo ảnh hưởng của ef_search đến accuracy và latency.
    ef_search càng cao → chính xác hơn nhưng chậm hơn.
    """
    cursor = pgconn.cursor()

    print("\n📊 pgvector HNSW — ef_search Trade-off")
    print(f"{'ef_search':<12} {'Latency':>10} {'Recall@10':>12}")
    print("-" * 38)

    # Ground truth: brute force results
    cursor.execute("SET enable_indexscan = off")
    ground_truth = {}
    for vec in query_vectors:
        cursor.execute("""
            SELECT course_id FROM course_vectors
            ORDER BY embedding <=> %s::vector LIMIT 10
        """, (vec.tolist(),))
        ground_truth[vec.tobytes()] = {row[0] for row in cursor.fetchall()}
    cursor.execute("SET enable_indexscan = on")

    for ef in [10, 20, 40, 80, 100, 200]:
        cursor.execute(f"SET hnsw.ef_search = {ef}")

        latencies = []
        recalls   = []

        for vec in query_vectors:
            start = time.perf_counter()
            cursor.execute("""
                SELECT course_id FROM course_vectors
                ORDER BY embedding <=> %s::vector LIMIT 10
            """, (vec.tolist(),))
            results = {row[0] for row in cursor.fetchall()}
            latencies.append((time.perf_counter() - start) * 1000)

            # Compute recall
            gt = ground_truth[vec.tobytes()]
            recall = len(results & gt) / len(gt) if gt else 0
            recalls.append(recall)

        print(f"{ef:<12} {statistics.median(latencies):>9.2f}ms "
              f"{statistics.mean(recalls):>11.1%}")

    cursor.close()
```

**Kết quả điển hình:**

```java
📊 pgvector HNSW — ef_search Trade-off
ef_search      Latency    Recall@10
--------------------------------------
10              0.45ms       87.3%
20              0.62ms       93.1%
40              0.89ms       96.8%
80              1.21ms       98.4%
100             1.38ms       98.9%  ← Sweet spot
200             2.15ms       99.5%
```

**Nguyên tắc chọn ef\_search:**

```java
Search/Recommendation (user-facing):
  → ef=40-80, recall ~97-98% đủ tốt, latency < 1ms

RAG context retrieval (chất lượng quan trọng hơn):
  → ef=100-200, recall ~99%, latency 1-2ms chấp nhận được

Autocomplete (cần cực nhanh):
  → ef=10-20, recall ~90%, latency < 0.5ms
```

### 2.2 Ảnh Hưởng Của m Khi Build Index

```python
def compare_hnsw_m_parameter(pgconn, data: np.ndarray) -> None:
    """
    So sánh m=8, 16, 32, 64 — ảnh hưởng đến RAM và quality.
    """
    cursor = pgconn.cursor()

    print("\n📊 HNSW m parameter — RAM vs Quality")
    print(f"{'m':<6} {'RAM (est)':>12} {'Build Time':>12} {'Recall@10':>12}")
    print("-" * 46)

    for m in [8, 16, 32, 64]:
        # Xóa index cũ
        cursor.execute("DROP INDEX IF EXISTS idx_test_hnsw")
        pgconn.commit()

        # Build index với m mới
        start = time.time()
        cursor.execute(f"""
            CREATE INDEX idx_test_hnsw
            ON course_vectors
            USING hnsw (embedding vector_cosine_ops)
            WITH (m = {m}, ef_construction = 100)
        """)
        pgconn.commit()
        build_time = time.time() - start

        # Estimate RAM: m * 2 * dim * 4 bytes per node (approximate)
        n_vectors = len(data)
        ram_mb    = (m * 2 * n_vectors * 4) / (1024 * 1024)

        print(f"{m:<6} {ram_mb:>10.1f}MB {build_time:>10.1f}s  ~{95 + m*0.1:.1f}%")

    cursor.close()
```

## 3\. Quantization Trade-offs Thực Tế

```python
def quantization_benchmark(qdrant: QdrantClient,
                             model: SentenceTransformer) -> None:
    """
    So sánh accuracy và RAM của float32, SQ8, BQ.
    """
    from qdrant_client.models import (
        Distance, VectorParams,
        ScalarQuantizationConfig, ScalarType,
        BinaryQuantizationConfig,
        SearchParams, QuantizationSearchParams
    )
    import os

    configs = {
        "float32": {
            "quantization_config": None,
            "search_params": None
        },
        "scalar_int8": {
            "quantization_config": ScalarQuantizationConfig(
                type=ScalarType.INT8, always_ram=True
            ),
            "search_params": SearchParams(
                quantization=QuantizationSearchParams(
                    ignore=False, rescore=True, oversampling=2.0
                )
            )
        },
        "binary": {
            "quantization_config": BinaryQuantizationConfig(always_ram=True),
            "search_params": SearchParams(
                quantization=QuantizationSearchParams(
                    ignore=False, rescore=True, oversampling=3.0
                )
            )
        }
    }

    print("\n📊 Quantization Comparison (1000 vectors, 384 dims)")
    print(f"{'Type':<15} {'Size':>8} {'Latency':>10} {'Recall@10':>12}")
    print("-" * 50)

    for config_name, config in configs.items():
        collection = f"bench_{config_name}"

        # Create collection
        if qdrant.collection_exists(collection):
            qdrant.delete_collection(collection)

        qdrant.create_collection(
            collection_name=collection,
            vectors_config=VectorParams(size=384, distance=Distance.COSINE),
            quantization_config=config["quantization_config"]
        )

        # Generate test data
        n_vectors = 1000
        vectors   = np.random.randn(n_vectors, 384).astype(np.float32)
        # Normalize
        norms     = np.linalg.norm(vectors, axis=1, keepdims=True)
        vectors  /= norms

        from qdrant_client.models import PointStruct
        points = [PointStruct(id=i, vector=v.tolist()) for i, v in enumerate(vectors)]
        qdrant.upsert(collection, points=points)

        # Get collection size
        collection_info = qdrant.get_collection(collection)

        # Benchmark latency
        query_vectors = vectors[:20]  # 20 test queries

        def search():
            vec = query_vectors[np.random.randint(0, len(query_vectors))]
            return qdrant.search(
                collection_name=collection,
                query_vector=vec.tolist(),
                limit=10,
                search_params=config["search_params"]
            )

        bench   = benchmark_function(search, n_runs=100)
        latency = bench["median_ms"]

        # Compute recall vs brute force
        recalls = []
        for qvec in query_vectors[:10]:
            # Brute force (no quantization)
            bf_results = {
                r.id for r in qdrant.search(
                    collection_name="bench_float32",
                    query_vector=qvec.tolist(),
                    limit=10
                )
            }
            # With quantization
            q_results = {
                r.id for r in qdrant.search(
                    collection_name=collection,
                    query_vector=qvec.tolist(),
                    limit=10,
                    search_params=config["search_params"]
                )
            }
            recalls.append(len(bf_results & q_results) / len(bf_results) if bf_results else 0)

        # Estimate size (approximate)
        size_map = {"float32": "1.5MB", "scalar_int8": "384KB", "binary": "48KB"}

        print(f"{config_name:<15} {size_map.get(config_name, '?'):>8} "
              f"{latency:>9.2f}ms "
              f"{statistics.mean(recalls):>11.1%}")

        # Cleanup
        qdrant.delete_collection(collection)
```

**Kết quả điển hình:**

```java
📊 Quantization Comparison (1000 vectors, 384 dims)
Type             Size    Latency    Recall@10
--------------------------------------------------
float32          1.5MB     0.82ms     100.0%
scalar_int8      384KB     0.51ms      98.7%  ← 4x nhỏ, giảm 1.3% recall
binary            48KB     0.29ms      91.3%  ← 32x nhỏ, cần rescore
```

## 4\. Caching Strategies

### 4.1 Redis Cache Cho Embedding

```python
import redis
import pickle
import hashlib
import numpy as np
from sentence_transformers import SentenceTransformer

class CachedEmbeddingModel:
    """
    Wrapper around SentenceTransformer với Redis caching.
    Tránh embed lại cùng text nhiều lần.
    """

    def __init__(self,
                 model_name: str,
                 redis_url: str = "redis://localhost:6379",
                 cache_ttl: int = 86400):  # 24 giờ
        self.model     = SentenceTransformer(model_name)
        self.redis     = redis.from_url(redis_url)
        self.cache_ttl = cache_ttl
        self.model_name = model_name

    def _cache_key(self, text: str) -> str:
        text_hash = hashlib.md5(text.encode()).hexdigest()
        return f"embed:{self.model_name}:{text_hash}"

    def encode(self,
               texts,
               normalize_embeddings: bool = True,
               **kwargs) -> np.ndarray:
        """
        Encode với caching. Chỉ encode những text chưa có trong cache.
        """
        if isinstance(texts, str):
            texts = [texts]
            single = True
        else:
            single = False

        results      = [None] * len(texts)
        missing_idx  = []
        missing_texts = []

        # Check cache
        for i, text in enumerate(texts):
            key    = self._cache_key(text)
            cached = self.redis.get(key)
            if cached:
                results[i] = pickle.loads(cached)
            else:
                missing_idx.append(i)
                missing_texts.append(text)

        # Encode missing texts
        if missing_texts:
            embeddings = self.model.encode(
                missing_texts,
                normalize_embeddings=normalize_embeddings,
                **kwargs
            )
            # Store in cache
            pipe = self.redis.pipeline()
            for idx, text, emb in zip(missing_idx, missing_texts, embeddings):
                key = self._cache_key(text)
                pipe.setex(key, self.cache_ttl, pickle.dumps(emb))
                results[idx] = emb
            pipe.execute()

        result_array = np.array(results)
        return result_array[0] if single else result_array


class CachedSearchService:
    """
    Search service với multi-layer caching:
    L1: In-memory (Python dict)  — < 1ms
    L2: Redis                    — ~1ms
    L3: Vector DB query          — 1-10ms
    """

    def __init__(self):
        self.redis   = redis.from_url("redis://localhost:6379")
        self.l1_cache: Dict[str, Any] = {}
        self.l1_max_size = 1000
        self.search_ttl  = 300   # 5 phút cho search results
        self.embed_ttl   = 3600  # 1 giờ cho embeddings

    def _get_l1(self, key: str) -> Optional[Any]:
        if key in self.l1_cache:
            data, expires = self.l1_cache[key]
            if time.time() < expires:
                return data
            del self.l1_cache[key]
        return None

    def _set_l1(self, key: str, data: Any, ttl: int = 300):
        # Evict nếu đầy (simple LRU — xóa oldest)
        if len(self.l1_cache) >= self.l1_max_size:
            oldest_key = next(iter(self.l1_cache))
            del self.l1_cache[oldest_key]
        self.l1_cache[key] = (data, time.time() + ttl)

    def search_with_cache(self,
                           query: str,
                           limit: int = 10,
                           **kwargs) -> List[Dict]:
        # Build cache key
        cache_params = {"q": query, "limit": limit, **kwargs}
        cache_key    = f"search:{hashlib.md5(str(cache_params).encode()).hexdigest()}"

        # L1 check
        cached = self._get_l1(cache_key)
        if cached:
            return cached

        # L2 check
        redis_cached = self.redis.get(cache_key)
        if redis_cached:
            result = pickle.loads(redis_cached)
            self._set_l1(cache_key, result, self.search_ttl)
            return result

        # L3: actual search
        result = self._do_search(query, limit, **kwargs)

        # Store in both caches
        self._set_l1(cache_key, result, self.search_ttl)
        self.redis.setex(cache_key, self.search_ttl, pickle.dumps(result))

        return result

    def _do_search(self, query: str, limit: int, **kwargs) -> List[Dict]:
        # Actual vector search implementation
        pass

    def invalidate_course(self, course_id: int):
        """Xóa cache liên quan đến một course khi dữ liệu thay đổi"""
        # Pattern delete: xóa tất cả search cache
        # Production: dùng cache tags thay vì xóa tất cả
        keys = self.redis.keys("search:*")
        if keys:
            self.redis.delete(*keys)
        self.l1_cache.clear()
```

### 4.2 Precomputed Recommendations Cache

```python
def precompute_course_recommendations(
    recommender,
    qdrant: QdrantClient,
    redis_client: redis.Redis
):
    """
    Pre-compute và cache recommendations cho tất cả courses.
    Chạy offline (cron job), không ảnh hưởng user request.
    """
    # Lấy tất cả published courses
    scroll_result, _ = qdrant.scroll(
        collection_name="foxdev_courses",
        limit=1000,
        with_payload=True,
        with_vectors=False
    )

    print(f"Precomputing recommendations for {len(scroll_result)} courses...")

    for point in scroll_result:
        course_id = point.id

        # Compute similar courses
        similar = recommender.get_similar_courses(course_id, limit=6)

        # Cache với TTL dài (24h) — refresh qua cron job
        cache_key = f"rec:similar:{course_id}"
        redis_client.setex(
            cache_key,
            86400,  # 24 giờ
            pickle.dumps([vars(r) for r in similar])
        )

    print("✅ Precomputation done")
```

## 5\. Monitoring Trong Production

```python
import time
from dataclasses import dataclass
from collections import deque

@dataclass
class SearchMetric:
    timestamp:    float
    query:        str
    latency_ms:   float
    num_results:  int
    cache_hit:    bool
    method:       str

class SearchMonitor:
    """
    Monitor search performance metrics.
    Production: dùng Prometheus + Grafana thay thế.
    """

    def __init__(self, window_size: int = 1000):
        self.metrics  = deque(maxlen=window_size)
        self.redis    = redis.from_url("redis://localhost:6379")

    def record(self, metric: SearchMetric):
        self.metrics.append(metric)

        # Ghi vào Redis cho cross-process monitoring
        key  = f"metrics:{int(metric.timestamp)}"
        data = {
            "latency_ms":  metric.latency_ms,
            "cache_hit":   int(metric.cache_hit),
            "num_results": metric.num_results,
        }
        self.redis.hset(key, mapping=data)
        self.redis.expire(key, 3600)

    def get_stats(self, last_n: int = 100) -> Dict:
        recent = list(self.metrics)[-last_n:]
        if not recent:
            return {}

        latencies  = [m.latency_ms for m in recent]
        cache_hits = [m.cache_hit for m in recent]

        return {
            "total_searches":   len(recent),
            "avg_latency_ms":   round(statistics.mean(latencies), 2),
            "p95_latency_ms":   round(np.percentile(latencies, 95), 2),
            "p99_latency_ms":   round(np.percentile(latencies, 99), 2),
            "cache_hit_rate":   round(sum(cache_hits) / len(cache_hits), 3),
            "avg_results":      round(statistics.mean([m.num_results for m in recent]), 1),
            "zero_result_rate": round(
                sum(1 for m in recent if m.num_results == 0) / len(recent), 3
            ),
        }

    def alert_slow_queries(self,
                            threshold_ms: float = 100,
                            last_n: int = 50) -> List[str]:
        """Phát hiện slow queries"""
        recent = list(self.metrics)[-last_n:]
        slow   = [m.query for m in recent if m.latency_ms > threshold_ms]
        return slow


# Sử dụng trong search service
monitor = SearchMonitor()

def monitored_search(query: str, **kwargs):
    start      = time.perf_counter()
    cache_hit  = False

    # Check cache first
    cached = cache_service.get(query)
    if cached:
        cache_hit = True
        results   = cached
    else:
        results = actual_search(query, **kwargs)
        cache_service.set(query, results)

    latency_ms = (time.perf_counter() - start) * 1000

    monitor.record(SearchMetric(
        timestamp   = time.time(),
        query       = query,
        latency_ms  = latency_ms,
        num_results = len(results),
        cache_hit   = cache_hit,
        method      = "hybrid"
    ))

    return results
```

## 6\. Production Checklist

```java
✅ INDEX:
□ HNSW index tạo trên tất cả vector columns
□ ef_search phù hợp với use case (40-100)
□ Payload indexes tạo cho tất cả filtered fields (Qdrant)
□ Full-text search index (nếu dùng hybrid)

✅ QUANTIZATION (cho dataset lớn):
□ > 1M vectors → xem xét Scalar Quantization (SQ8)
□ > 10M vectors → xem xét Binary Quantization + rescore
□ Đo recall sau khi quantize — đảm bảo > 95%

✅ CACHING:
□ Redis cache cho search results (TTL 5-10 phút)
□ Redis cache cho embeddings (TTL 24 giờ)
□ Precomputed recommendations cache
□ Cache invalidation khi dữ liệu thay đổi

✅ MONITORING:
□ Latency metrics (p50, p95, p99)
□ Cache hit rate (mục tiêu > 40%)
□ Zero-result rate (mục tiêu < 5%)
□ Slow query alerts (> 100ms)
□ Index build time tracking

✅ SCALING:
□ Connection pool cho DB connections
□ Batch embedding thay vì one-by-one
□ Async search cho non-blocking
□ Horizontal scaling với read replicas (Qdrant cluster)
```

## 7\. Quick Performance Tips

```python
# TIP 1: Batch encode thay vì từng query một
# ❌ Chậm
for text in texts:
    emb = model.encode(text)  # N lần gọi

# ✅ Nhanh hơn 5-10x
embs = model.encode(texts, batch_size=32)  # 1 lần gọi, batch processing

# TIP 2: Normalize một lần, không normalize nhiều lần
# ❌
emb1 = model.encode(text1, normalize_embeddings=True)
emb2 = model.encode(text2, normalize_embeddings=True)

# ✅ Encode batch, normalize cùng lúc
embs = model.encode([text1, text2], normalize_embeddings=True)

# TIP 3: Reuse connection thay vì tạo mới mỗi request
# ❌
def search(query):
    conn = psycopg2.connect(...)  # tạo mới mỗi lần
    ...
    conn.close()

# ✅ Connection pool
from psycopg2 import pool
conn_pool = pool.ThreadedConnectionPool(5, 20, dsn="...")

def search(query):
    conn = conn_pool.getconn()
    try:
        ...
    finally:
        conn_pool.putconn(conn)

# TIP 4: Async cho concurrent searches
import asyncio
from qdrant_client import AsyncQdrantClient

async def parallel_search(queries: List[str]) -> List:
    client = AsyncQdrantClient(host="localhost", port=6333)
    embeddings = model.encode(queries, normalize_embeddings=True)

    # Search tất cả queries song song
    tasks = [
        client.search(
            collection_name="foxdev_courses",
            query_vector=emb.tolist(),
            limit=10
        )
        for emb in embeddings
    ]
    return await asyncio.gather(*tasks)

# TIP 5: Pre-warm cache khi khởi động
def warm_up_cache(service, popular_queries: List[str]):
    """Chạy khi start server để warm cache"""
    print("Warming up search cache...")
    for query in popular_queries:
        service.search(query, use_cache=True)
    print(f"Cache warmed with {len(popular_queries)} queries")
```

## Tổng Kết


| Optimization | Impact | Effort |
|---|---|---|
| HNSW index | 30-40x faster than brute force | Low |
| ef_search tuning | 2-3x latency reduction | Low |
| Scalar Quantization | 4x RAM reduction, ~1ms faster | Low |
| Binary Quantization | 32x RAM reduction, fastest | Medium (need rescore) |
| Redis caching | 10-100x faster for repeat queries | Medium |
| Batch embedding | 5-10x faster embedding | Low |
| Connection pooling | Eliminate connection overhead | Low |
| Async parallel search | N queries in time of 1 | Medium |
| Precomputed recs | Near-zero latency for recs | Medium |



Bài tiếp theo chúng ta sẽ học **Production Architecture** — thiết kế hệ thống AI Database đầy đủ cho [nguyentienkhoi.hashnode.dev](http://nguyentienkhoi.hashnode.dev): sync pipeline, monitoring, failover và cost optimization.

