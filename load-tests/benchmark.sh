#!/bin/sh
# Performance benchmark script for all services

echo "Performance Benchmark - JVM Framework Comparison"
echo "================================================"

services="java-micronaut:8080 kotlin-micronaut:8081 kotlin-ktor:8082 scala-zio:8083 golang:8084"

# Parameters
REQUESTS=${1:-1000}
CONCURRENCY=${2:-10}

echo "Parameters: $REQUESTS requests, $CONCURRENCY concurrent"
echo ""

# Test health endpoints
echo "1. Health Endpoint Performance"
echo "------------------------------"

for service in $services; do
    echo "Testing $service/health"
    ab -n "$REQUESTS" -c "$CONCURRENCY" -q "http://$service/health" | \
        grep -E "(Requests per second|Time per request|Transfer rate)" | \
        sed 's/^/  /'
    echo ""
done

# Test compute endpoints
echo "2. Compute Endpoint Performance"  
echo "-------------------------------"

# Create temporary file for POST data
cat > /tmp/compute_data.json << EOF
{"numbers": [1, 2, 3, 4, 5], "operation": "sum"}
EOF

for service in $services; do
    echo "Testing $service/api/compute"
    ab -n "$REQUESTS" -c "$CONCURRENCY" -q \
        -p /tmp/compute_data.json \
        -T "application/json" \
        "http://$service/api/compute" | \
        grep -E "(Requests per second|Time per request|Transfer rate)" | \
        sed 's/^/  /'
    echo ""
done

# Test MongoDB users endpoints
echo "3. MongoDB Users Endpoint Performance"
echo "-------------------------------------"

for service in $services; do
    echo "Testing $service/api/users"
    ab -n "$REQUESTS" -c "$CONCURRENCY" -q "http://$service/api/users" | \
        grep -E "(Requests per second|Time per request|Transfer rate)" | \
        sed 's/^/  /'
    echo ""
done

echo "4. MongoDB Users with Department Filter Performance"
echo "--------------------------------------------------"

for service in $services; do
    echo "Testing $service/api/users?department=Engineering"
    ab -n "$REQUESTS" -c "$CONCURRENCY" -q "http://$service/api/users?department=Engineering" | \
        grep -E "(Requests per second|Time per request|Transfer rate)" | \
        sed 's/^/  /'
    echo ""
done

# Cleanup
rm -f /tmp/compute_data.json

echo "Benchmark complete!"
echo ""
echo "To run with different parameters:"
echo "  ./benchmark.sh [requests] [concurrency]"
echo "  Example: ./benchmark.sh 5000 50"