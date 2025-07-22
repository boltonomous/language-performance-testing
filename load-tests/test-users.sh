#!/bin/sh
# Test MongoDB users endpoints for all services

echo "MongoDB Users Endpoint Test"
echo "==========================="

services="java-micronaut:8080 kotlin-micronaut:8081 kotlin-ktor:8082 scala-zio:8083 golang:8084"

echo "Testing all users endpoint..."
echo ""

for service in $services; do
    echo "--- $service ---"
    echo "GET /api/users"
    response=$(curl -s "http://$service/api/users" 2>/dev/null)
    
    if [ $? -eq 0 ] && echo "$response" | grep -q '"users"'; then
        echo "$response" | jq '.count, .totalUsers, .processingTimeMs' 2>/dev/null || echo "Response received but JSON parsing failed"
        echo "✅ SUCCESS"
    else
        echo "❌ FAILED - No response or invalid JSON"
        echo "Raw response: $response"
    fi
    echo ""
done

echo "Testing department filter (Engineering)..."
echo ""

for service in $services; do
    echo "--- $service ---"
    echo "GET /api/users?department=Engineering"
    response=$(curl -s "http://$service/api/users?department=Engineering" 2>/dev/null)
    
    if [ $? -eq 0 ] && echo "$response" | grep -q '"users"'; then
        echo "$response" | jq '.count, .totalUsers, .processingTimeMs' 2>/dev/null || echo "Response received but JSON parsing failed"
        echo "✅ SUCCESS"
    else
        echo "❌ FAILED - No response or invalid JSON"
        echo "Raw response: $response"
    fi
    echo ""
done

echo "MongoDB endpoint testing complete!"