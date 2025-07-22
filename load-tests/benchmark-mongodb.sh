#!/bin/sh
# Performance benchmark script for MongoDB users endpoints (working services only)

echo "MongoDB Performance Benchmark - All Services"
echo "==============================================="

# All services now working: Go, Kotlin Ktor, Scala ZIO, Java & Kotlin Micronaut, Kotlin Spring Boot
services="golang:8084 kotlin-ktor:8082 scala-zio:8083 java-micronaut:8080 kotlin-micronaut:8081 kotlin-springboot:8085"

# Parameters
REQUESTS=${1:-1000}
CONCURRENCY=${2:-10}

echo "Parameters: $REQUESTS requests, $CONCURRENCY concurrent"
echo ""

# Test MongoDB users endpoints
echo "1. MongoDB Users Endpoint Performance"
echo "------------------------------------"

for service in $services; do
    echo "Testing $service/api/users"
    ab -n "$REQUESTS" -c "$CONCURRENCY" -q "http://localhost:$(echo $service | cut -d: -f2)/api/users" | \
        grep -E "(Requests per second|Time per request|Transfer rate)" | \
        sed 's/^/  /'
    echo ""
done

echo "2. MongoDB Users with Department Filter Performance"
echo "-------------------------------------------------"

for service in $services; do
    echo "Testing $service/api/users?department=Engineering"
    ab -n "$REQUESTS" -c "$CONCURRENCY" -q "http://localhost:$(echo $service | cut -d: -f2)/api/users?department=Engineering" | \
        grep -E "(Requests per second|Time per request|Transfer rate)" | \
        sed 's/^/  /'
    echo ""
done

echo "MongoDB benchmark complete!"
echo ""
echo "To run with different parameters:"
echo "  ./benchmark-mongodb.sh [requests] [concurrency]"
echo "  Example: ./benchmark-mongodb.sh 5000 50"