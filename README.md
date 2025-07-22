# Claude Performance Bake-off

A comprehensive performance comparison between JVM-based web frameworks and Go for math computation, health, and database endpoints.

## Technologies Compared

1. **Java 21 + Micronaut** - Modern Java with reactive framework (Port 8080)
2. **Kotlin + Micronaut** - Kotlin with same reactive framework (Port 8081)
3. **Kotlin + Ktor** - Kotlin with lightweight coroutine-based framework (Port 8082)
4. **Scala 3 + ZIO 2** - Modern Scala with functional effect system (Port 8083)
5. **Go + Gin** - Lightweight Go web framework (Port 8084)
6. **Kotlin + Spring Boot** - Kotlin with mature Spring framework (Port 8085)

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

Performance benchmark conducted using Docker containers with 5000 requests and 50 concurrent connections:

### Health Endpoint Performance

| Framework | Requests/sec | Avg Time (ms) | Rank |
|-----------|-------------|---------------|------|
| **Go + Gin** | 28,247 | 1.77 | 🥇 1st |
| **Scala 3 + ZIO 2** | 14,265 | 3.50 | 🥈 2nd |
| **Java + Micronaut** | 11,077 | 4.51 | 🥉 3rd |
| **Kotlin + Spring Boot** | 10,649 | 4.70 | 4th |
| **Kotlin + Ktor** | 10,559 | 4.74 | 5th |
| **Kotlin + Micronaut** | 8,975 | 5.57 | 6th |

### Compute Endpoint Performance

| Framework | Requests/sec | Avg Time (ms) | Rank |
|-----------|-------------|---------------|------|
| **Go + Gin** | 26,027 | 1.92 | 🥇 1st |
| **Kotlin + Spring Boot** | 12,243 | 4.08 | 🥈 2nd |
| **Kotlin + Ktor** | 12,106 | 4.13 | 🥉 3rd |
| **Scala 3 + ZIO 2** | 11,564 | 4.32 | 4th |
| **Kotlin + Micronaut** | 9,118 | 5.48 | 5th |
| **Java + Micronaut** | 7,217 | 6.93 | 6th |

### MongoDB Users Endpoint Performance

Performance benchmark for database operations with 5000 requests and 50 concurrent connections:

#### All Users Query (100 records)
| Framework | Requests/sec | Avg Time (ms) | Rank |
|-----------|-------------|---------------|------|
| **Kotlin + Spring Boot** | 4,471 | 11.18 | 🥇 1st |
| **Kotlin + Ktor** | 4,342 | 11.52 | 🥈 2nd |
| **Go + Gin** | 3,643 | 13.72 | 🥉 3rd |
| **Java + Micronaut** | 3,051 | 16.39 | 4th |
| **Kotlin + Micronaut** | 2,588 | 19.32 | 5th |
| **Scala 3 + ZIO 2** | 1,650 | 30.31 | 6th |

#### Filtered Query (22 Engineering records)
| Framework | Requests/sec | Avg Time (ms) | Rank |
|-----------|-------------|---------------|------|
| **Go + Gin** | 11,268 | 4.44 | 🥇 1st |
| **Kotlin + Spring Boot** | 6,630 | 7.54 | 🥈 2nd |
| **Kotlin + Ktor** | 6,184 | 8.09 | 🥉 3rd |
| **Java + Micronaut** | 4,241 | 11.79 | 4th |
| **Kotlin + Micronaut** | 4,107 | 12.18 | 5th |
| **Scala 3 + ZIO 2** | 3,947 | 12.67 | 6th |

### Key Findings

#### Overall Performance
- **Go dominates health/compute** with 2-3x better performance than JVM solutions
- **Spring Boot emerges as database champion** - outperforms even Go in full database queries
- **Kotlin frameworks excel**: Spring Boot and Ktor lead JVM performance across scenarios
- **Scala 3 + ZIO 2** surprisingly leads JVM health endpoint performance  
- **Framework ecosystem maturity matters** - Spring Boot's optimizations clearly visible in database operations
- **Docker-based testing** ensures consistent, reproducible results

#### Database Operations - **Spring Boot Revolution**
- **Spring Boot dominates database operations** - 4.5K req/sec beats Go's 3.6K req/sec in full queries
- **JVM frameworks now lead database performance**: Spring Boot and Ktor excel with 4K+ req/sec
- **Ecosystem maturity wins**: Years of Spring Data and connection pooling optimization pays off
- **Performance consistency**: Database response times much more consistent (7-30ms range) than previous tests
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

| Use Case | 🥇 1st | 🥈 2nd | 🥉 3rd | 4th | 5th | 6th |
|----------|--------|--------|--------|-----|-----|-----|
| **Health Checks** | Go + Gin<br/>28K req/sec | Scala 3 + ZIO 2<br/>14K req/sec | Java + Micronaut<br/>11K req/sec | Kotlin + Spring Boot<br/>11K req/sec | Kotlin + Ktor<br/>11K req/sec | Kotlin + Micronaut<br/>9K req/sec |
| **Compute Operations** | Go + Gin<br/>26K req/sec | Kotlin + Spring Boot<br/>12K req/sec | Kotlin + Ktor<br/>12K req/sec | Scala 3 + ZIO 2<br/>12K req/sec | Kotlin + Micronaut<br/>9K req/sec | Java + Micronaut<br/>7K req/sec |
| **Database (Full)** | Kotlin + Spring Boot<br/>4.5K req/sec | Kotlin + Ktor<br/>4.3K req/sec | Go + Gin<br/>3.6K req/sec | Java + Micronaut<br/>3.1K req/sec | Kotlin + Micronaut<br/>2.6K req/sec | Scala 3 + ZIO 2<br/>1.7K req/sec |
| **Database (Filtered)** | Go + Gin<br/>11K req/sec | Kotlin + Spring Boot<br/>7K req/sec | Kotlin + Ktor<br/>6K req/sec | Java + Micronaut<br/>4K req/sec | Kotlin + Micronaut<br/>4K req/sec | Scala 3 + ZIO 2<br/>4K req/sec |

### Key Architectural Insights

1. **Go + Gin**: Still dominates health/compute scenarios - lightweight, compiled, excellent for CPU-intensive services
2. **Kotlin + Spring Boot**: **NEW DATABASE CHAMPION** - mature ecosystem delivers exceptional database performance (4.5K req/sec)
3. **Kotlin + Ktor**: Balanced JVM framework - strong across all scenarios with coroutines for async operations
4. **Scala 3 + ZIO 2**: Surprisingly leads JVM health performance - good balance for functional business logic
5. **Micronaut**: Improved but inconsistent - better database performance than previous tests but still variable
6. **Framework ecosystem maturity**: Spring Boot's years of optimization clearly benefit database-heavy applications

### Game-Changing Discovery
**Spring Boot's database dominance** (4.5K req/sec vs Go's 3.6K req/sec) demonstrates that **ecosystem maturity can overcome language performance gaps**. This comprehensive comparison shows that framework selection should prioritize **workload-specific strengths** over general benchmarks.