# Claude Performance Bake-off

A performance comparison between different JVM-based web frameworks and languages for math computation endpoints.

## Technologies Compared

1. **Java 21 + Micronaut** - Modern Java with reactive framework
2. **Kotlin + Micronaut** - Kotlin with same reactive framework  
3. **Kotlin + Ktor** - Kotlin with lightweight coroutine-based framework
4. **Scala 3 + ZIO 2** - Modern Scala with functional effect system
5. **Go + Gin** - Lightweight Go web framework

## API Endpoints

All implementations provide the same REST API:

- `GET /health` - Health check endpoint
- `POST /api/compute` - Math computation endpoint
- `GET /api/users` - MongoDB users endpoint (with optional `?department=X` filter)

### Compute Endpoint

**Request:**
```json
{
  "numbers": [1, 2, 3, 4, 5],
  "operation": "sum"
}
```

**Supported operations:** `sum`, `average`, `max`, `min`, `fibonacci`

**Response:**
```json
{
  "result": 15.0,
  "operation": "sum",
  "inputSize": 5,
  "processingTimeMs": 2
}
```

### MongoDB Users Endpoint

**Request:**
```bash
GET /api/users
GET /api/users?department=Engineering
```

**Response:**
```json
{
  "users": [
    {
      "id": "507f1f77bcf86cd799439011",
      "name": "Alice Johnson",
      "email": "alice@example.com",
      "age": 28,
      "department": "Engineering",
      "salary": 75000.0,
      "skills": ["Java", "Python", "Docker"],
      "createdAt": "2025-07-22T01:05:44.756Z"
    }
  ],
  "count": 22,
  "totalUsers": 100,
  "processingTimeMs": 4
}
```

## Database Integration

### MongoDB Setup
The application includes a MongoDB instance with sample data:

- **Container**: `mongodb:27017`
- **Database**: `bakeoff`
- **Collection**: `users` (100 sample records)
- **Departments**: Engineering, Data Science, DevOps, Marketing, Sales
- **Authentication**: `admin:password`

The MongoDB service is automatically started with Docker Compose and includes initialization scripts.

## Running Applications

Each application runs on a different port to allow concurrent testing:

- Java + Micronaut: `http://localhost:8080`
- Kotlin + Micronaut: `http://localhost:8081` 
- Kotlin + Ktor: `http://localhost:8082`
- Scala 3 + ZIO 2: `http://localhost:8083`
- Go + Gin: `http://localhost:8084`

## Building and Running

### Docker (Recommended)
```bash
# Start all services with Docker Compose
docker-compose up -d

# Or build and run individual services
docker build -t java-micronaut ./java-micronaut
docker run -p 8080:8080 java-micronaut
```

### Local Development
```bash
# Java + Micronaut (port 8080)
cd java-micronaut && ./gradlew run

# Kotlin + Micronaut (port 8081) 
cd kotlin-micronaut && ./gradlew run

# Kotlin + Ktor (port 8082)
cd kotlin-ktor && ./gradlew run

# Scala 3 + ZIO 2 (port 8083) - requires SBT environment
cd scala-zio && sbt run

# Go + Gin (port 8084)
cd golang && go run cmd/main.go
```

### Build Status
- ✅ **Java + Micronaut** - Builds and runs successfully
- ✅ **Kotlin + Micronaut** - Builds and runs successfully (requires `open` classes)
- ✅ **Kotlin + Ktor** - Builds and runs successfully 
- ✅ **Scala 3 + ZIO 2** - Builds and runs successfully with Docker
- ✅ **Go + Gin** - Builds and runs successfully

See individual README files in each subdirectory for detailed build instructions.

## Performance Testing

### Using Docker (Recommended)
```bash
# Start all services
docker-compose up -d

# Run health tests
docker-compose exec load-tester ./test-health.sh

# Run compute tests  
docker-compose exec load-tester ./test-compute.sh

# Run performance benchmarks
docker-compose exec load-tester ./benchmark.sh
docker-compose exec load-tester ./benchmark.sh 5000 50  # Custom params

# Run MongoDB endpoint tests
docker-compose exec load-tester ./test-all-endpoints.sh

# Run MongoDB performance benchmarks
docker-compose exec load-tester ./benchmark-mongodb.sh
docker-compose exec load-tester ./benchmark-mongodb.sh 2000 20  # Custom params
```

### Manual Testing
```bash
# Basic load test
ab -n 1000 -c 10 http://localhost:8080/health

# Math computation test
curl -X POST http://localhost:8080/api/compute \
  -H "Content-Type: application/json" \
  -d '{"numbers": [30], "operation": "fibonacci"}'

# MongoDB users test
curl http://localhost:8080/api/users
curl http://localhost:8080/api/users?department=Engineering
```

### Load Testing Tools Included
- **Apache Bench (ab)** - Traditional load testing
- **wrk** - Modern load testing tool  
- **curl** - Manual API testing
- **Custom scripts** - Automated benchmark suite

## Performance Results

Performance benchmark conducted using Docker containers with 1000 requests and 10 concurrent connections:

### Health Endpoint Performance

| Framework | Requests/sec | Avg Time (ms) | Rank |
|-----------|-------------|---------------|------|
| **Go + Gin** | 24,093 | 0.42 | 🥇 1st |
| **Kotlin + Ktor** | 12,383 | 0.81 | 🥈 2nd |
| **Scala 3 + ZIO 2** | 7,361 | 1.36 | 🥉 3rd |
| **Kotlin + Micronaut** | 6,800 | 1.47 | 4th |
| **Java + Micronaut** | 5,952 | 1.68 | 5th |

### Compute Endpoint Performance

| Framework | Requests/sec | Avg Time (ms) | Rank |
|-----------|-------------|---------------|------|
| **Go + Gin** | 29,246 | 0.34 | 🥇 1st |
| **Kotlin + Micronaut** | 12,075 | 0.83 | 🥈 2nd |
| **Java + Micronaut** | 11,350 | 0.88 | 🥉 3rd |
| **Kotlin + Ktor** | 9,955 | 1.01 | 4th |
| **Scala 3 + ZIO 2** | 7,228 | 1.38 | 5th |

### MongoDB Users Endpoint Performance

Performance benchmark for database operations with 2000 requests and 20 concurrent connections:

#### All Users Query (100 records)
| Framework | Requests/sec | Avg Time (ms) | Rank |
|-----------|-------------|---------------|------|
| **Go + Gin** | 2,273 | 8.8 | 🥇 1st |
| **Kotlin + Ktor** | 1,786 | 11.2 | 🥈 2nd |
| **Scala 3 + ZIO 2** | 1,276 | 15.7 | 🥉 3rd |
| **Java + Micronaut** | 759 | 26.3 | 4th |
| **Kotlin + Micronaut** | 695 | 28.8 | 5th |

#### Filtered Query (22 Engineering records)
| Framework | Requests/sec | Avg Time (ms) | Rank |
|-----------|-------------|---------------|------|
| **Go + Gin** | 3,126 | 6.4 | 🥇 1st |
| **Scala 3 + ZIO 2** | 3,096 | 6.5 | 🥈 2nd |
| **Kotlin + Ktor** | 3,068 | 6.5 | 🥉 3rd |
| **Kotlin + Micronaut** | 771 | 25.9 | 4th |
| **Java + Micronaut** | 311 | 64.3 | 5th |

### Key Findings

#### Overall Performance
- **Go dominates** with 2-4x better performance than JVM solutions
- **Kotlin + Ktor** leads among JVM frameworks for simple endpoints
- **Micronaut frameworks** excel at compute-heavy workloads  
- **Scala 3 + ZIO 2** shows respectable performance with functional programming benefits
- **Docker-based testing** ensures consistent, reproducible results

#### Database Operations
- **Go maintains dominance** in MongoDB operations with ~2-3K req/sec
- **Lightweight frameworks excel**: Kotlin Ktor and Scala ZIO outperform Micronaut in database scenarios
- **Micronaut frameworks struggle** with database operations despite strong compute performance
- **Filtered queries reveal performance characteristics**: Some frameworks scale better with smaller payloads
- **Java Micronaut particularly struggles** with filtered queries (311 req/sec - 10x slower than Go)
- **Database latency vs serialization**: Response times range from 6-64ms indicating varying efficiency
- **Full-stack integration** successfully demonstrates real-world database scenarios across all frameworks

### Test Environment
- **Container**: Docker with controlled resource allocation
- **JVM Settings**: `-Xmx512m -Xms256m` for all JVM applications
- **Load Pattern**: 
  - Health/Compute: 1000 requests, 10 concurrent connections
  - MongoDB: 2000 requests, 20 concurrent connections
- **Database**: MongoDB 7.0 with 100 sample user records
- **Tool**: Apache Bench (ab)
- **Date**: July 2025

## Implementation Status

### All Services Working ✅
- **Go + Gin**: Full implementation with all endpoints
- **Kotlin + Ktor**: Full implementation with all endpoints  
- **Scala 3 + ZIO 2**: Full implementation with all endpoints
- **Java + Micronaut**: Full implementation with all endpoints
- **Kotlin + Micronaut**: Full implementation with all endpoints

### Resolved Issues ✅
1. **~~Micronaut Health Endpoints~~**: ✅ Fixed - All services respond properly at `/health`
2. **~~Java Micronaut MongoDB~~**: ✅ Fixed - Type casting error resolved
3. **~~Kotlin Micronaut MongoDB~~**: ✅ Fixed - Now returns structured response object

All frameworks now provide complete implementations with consistent API endpoints, enabling comprehensive performance comparison across health checks, compute operations, and database interactions.

## Performance Summary & Insights

### Framework Rankings by Use Case

| Use Case | 🥇 1st | 🥈 2nd | 🥉 3rd | 4th | 5th |
|----------|--------|--------|--------|-----|-----|
| **Health Checks** | Go + Gin<br/>24K req/sec | Kotlin + Ktor<br/>12K req/sec | Scala 3 + ZIO 2<br/>7K req/sec | Kotlin + Micronaut<br/>7K req/sec | Java + Micronaut<br/>6K req/sec |
| **Compute Operations** | Go + Gin<br/>29K req/sec | Kotlin + Micronaut<br/>12K req/sec | Java + Micronaut<br/>11K req/sec | Kotlin + Ktor<br/>10K req/sec | Scala 3 + ZIO 2<br/>7K req/sec |
| **Database (Full)** | Go + Gin<br/>2.3K req/sec | Kotlin + Ktor<br/>1.8K req/sec | Scala 3 + ZIO 2<br/>1.3K req/sec | Java + Micronaut<br/>759 req/sec | Kotlin + Micronaut<br/>695 req/sec |
| **Database (Filtered)** | Go + Gin<br/>3.1K req/sec | Scala 3 + ZIO 2<br/>3.1K req/sec | Kotlin + Ktor<br/>3.1K req/sec | Kotlin + Micronaut<br/>771 req/sec | Java + Micronaut<br/>311 req/sec |

### Key Architectural Insights

1. **Go + Gin**: Dominates across all scenarios - lightweight, compiled, excellent for high-throughput services
2. **Kotlin + Ktor**: Best JVM framework for I/O-heavy workloads (health, database) - coroutines excel
3. **Micronaut**: Strong at CPU-intensive compute tasks but struggles with database operations - overhead from DI/AOP
4. **Scala 3 + ZIO 2**: Consistent performance with functional programming benefits - good for complex business logic
5. **Framework specialization matters**: Different frameworks excel at different workload types

This comprehensive comparison demonstrates real-world performance trade-offs across different architectural approaches and programming paradigms.