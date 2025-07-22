# Load Testing Scripts

This directory contains scripts for testing and benchmarking the performance bake-off applications.

## Available Scripts

### `test-health.sh`
Tests the health endpoints of all services to verify they're running correctly.

```bash
./test-health.sh
```

### `test-compute.sh` 
Tests the compute endpoints of all services with a simple math operation.

```bash
./test-compute.sh
```

### `benchmark.sh`
Comprehensive performance benchmark using Apache Bench (ab).

```bash
# Default: 1000 requests, 10 concurrent
./benchmark.sh

# Custom parameters
./benchmark.sh 5000 50
```

## Using with Docker Compose

1. Start all services:
```bash
docker-compose up -d
```

2. Run tests from the load-tester container:
```bash
docker-compose exec load-tester ./test-health.sh
docker-compose exec load-tester ./test-compute.sh
docker-compose exec load-tester ./benchmark.sh
```

## Manual Testing

You can also test individual endpoints manually:

```bash
# Health checks
curl http://localhost:8080/health  # Java + Micronaut
curl http://localhost:8081/health  # Kotlin + Micronaut
curl http://localhost:8082/health  # Kotlin + Ktor
curl http://localhost:8083/health  # Scala + ZIO

# Compute endpoints
curl -X POST http://localhost:8080/api/compute \
  -H "Content-Type: application/json" \
  -d '{"numbers": [1,2,3,4,5], "operation": "sum"}'
```

## Performance Metrics

The benchmark script reports:
- **Requests per second** - Throughput measure
- **Time per request** - Average latency 
- **Transfer rate** - Data throughput

These metrics help compare the relative performance characteristics of each JVM framework implementation.