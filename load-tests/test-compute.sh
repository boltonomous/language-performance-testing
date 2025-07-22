#!/bin/sh
# Compute endpoint test for all services

echo "Testing compute endpoints..."

services="java-micronaut:8080 kotlin-micronaut:8081 kotlin-ktor:8082 scala-zio:8083 golang:8084 kotlin-springboot:8085"
test_data='{"numbers": [1, 2, 3, 4, 5], "operation": "sum"}'

for service in $services; do
    echo "Testing $service/api/compute"
    response=$(curl -s -X POST \
        -H "Content-Type: application/json" \
        -d "$test_data" \
        "http://$service/api/compute")
    
    if echo "$response" | grep -q '"result"'; then
        result=$(echo "$response" | grep -o '"result":[^,]*' | cut -d':' -f2)
        echo "✅ $service - Result: $result"
    else
        echo "❌ $service - Failed: $response"
    fi
done