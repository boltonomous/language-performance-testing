# Claude Performance Bake-off

A comprehensive performance comparison between JVM-based web frameworks and Go for math computation, health, and database endpoints.

## Technologies Compared

1. **Java 21 + Micronaut** - Modern Java with reactive framework (Port 8080)
2. **Kotlin + Micronaut** - Kotlin with same reactive framework (Port 8081)
3. **Kotlin + Ktor** - Kotlin with lightweight coroutine-based framework (Port 8082)
4. **Scala 3 + ZIO 2** - Modern Scala with functional effect system (Port 8083)
5. **Go + Gin** - Lightweight Go web framework (Port 8084)
6. **Kotlin + Spring Boot** - Kotlin with mature Spring framework (Port 8085)
7. **Scala 2.13 + Play Framework** - Traditional Scala web framework (Port 8086)

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
- Kotlin + Spring Boot: `http://localhost:8085`
- Scala 2.13 + Play: `http://localhost:8086`

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

# Kotlin + Spring Boot (port 8085)
cd kotlin-springboot && ./gradlew bootRun

# Scala 2.13 + Play Framework (port 8086) - requires SBT environment
cd scala-play && sbt run
```

### Build Status
- ✅ **Java + Micronaut** - Builds and runs successfully
- ✅ **Kotlin + Micronaut** - Builds and runs successfully (requires `open` classes)
- ✅ **Kotlin + Ktor** - Builds and runs successfully 
- ✅ **Scala 3 + ZIO 2** - Builds and runs successfully with Docker
- ✅ **Go + Gin** - Builds and runs successfully
- ✅ **Kotlin + Spring Boot** - Builds and runs successfully
- ✅ **Scala 2.13 + Play Framework** - Builds and runs successfully with Docker

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

📊 **[View Comprehensive Performance Analysis →](Performance_Results.md)**

The complete performance benchmarking results, including tests with varying load levels (1K, 10K, 100K requests) and concurrency patterns (10, 200 concurrent connections), are available in the dedicated performance results document.

## Implementation Status

### All Services Working ✅
- **Go + Gin**: Full implementation with all endpoints
- **Kotlin + Ktor**: Full implementation with all endpoints  
- **Scala 3 + ZIO 2**: Full implementation with all endpoints
- **Java + Micronaut**: Full implementation with all endpoints
- **Kotlin + Micronaut**: Full implementation with all endpoints
- **Kotlin + Spring Boot**: Full implementation with all endpoints
- **Scala 2.13 + Play Framework**: Full implementation with all endpoints

### Resolved Issues ✅
1. **~~Micronaut Health Endpoints~~**: ✅ Fixed - All services respond properly at `/health`
2. **~~Java Micronaut MongoDB~~**: ✅ Fixed - Type casting error resolved
3. **~~Kotlin Micronaut MongoDB~~**: ✅ Fixed - Now returns structured response object

All 7 frameworks now provide complete implementations with consistent API endpoints, enabling comprehensive performance comparison across health checks, compute operations, and database interactions.

