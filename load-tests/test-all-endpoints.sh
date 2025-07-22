#!/bin/sh
# Test all MongoDB users endpoints for working services

echo "MongoDB Users Endpoint Test"
echo "============================"

services_health="golang:8084:/health kotlin-ktor:8082:/health scala-zio:8083:/health java-micronaut:8080:/health kotlin-micronaut:8081:/health kotlin-springboot:8085:/health"
services_users="golang:8084:/api/users kotlin-ktor:8082:/api/users scala-zio:8083:/api/users java-micronaut:8080:/api/users kotlin-micronaut:8081:/api/users kotlin-springboot:8085:/api/users"

echo "Testing health endpoints..."
echo ""

for service_endpoint in $services_health; do
    service=$(echo $service_endpoint | cut -d: -f1)
    port=$(echo $service_endpoint | cut -d: -f2)
    endpoint=$(echo $service_endpoint | cut -d: -f3)
    
    echo "--- $service:$port ---"
    echo "GET $endpoint"
    response=$(curl -s "http://$service:$port$endpoint" 2>/dev/null)
    
    if [ $? -eq 0 ] && echo "$response" | grep -q 'status'; then
        echo "✅ SUCCESS"
    else
        echo "❌ FAILED"
        echo "Raw response: $response"
    fi
    echo ""
done

echo "Testing MongoDB users endpoints..."
echo ""

for service_endpoint in $services_users; do
    service=$(echo $service_endpoint | cut -d: -f1)
    port=$(echo $service_endpoint | cut -d: -f2)
    endpoint=$(echo $service_endpoint | cut -d: -f3)
    
    echo "--- $service:$port ---"
    echo "GET $endpoint"
    response=$(curl -s "http://$service:$port$endpoint" 2>/dev/null)
    
    if [ $? -eq 0 ] && echo "$response" | grep -q '"users"'; then
        count=$(echo "$response" | jq -r '.count // empty' 2>/dev/null)
        total=$(echo "$response" | jq -r '.totalUsers // empty' 2>/dev/null)
        time_ms=$(echo "$response" | jq -r '.processingTimeMs // empty' 2>/dev/null)
        echo "Count: $count, Total: $total, Time: ${time_ms}ms"
        echo "✅ SUCCESS"
    else
        echo "❌ FAILED"
        echo "Raw response: $response"
    fi
    echo ""
done

echo "Testing department filter (Engineering)..."
echo ""

for service_endpoint in $services_users; do
    service=$(echo $service_endpoint | cut -d: -f1)
    port=$(echo $service_endpoint | cut -d: -f2)
    endpoint=$(echo $service_endpoint | cut -d: -f3)
    
    echo "--- $service:$port ---"
    echo "GET $endpoint?department=Engineering"
    response=$(curl -s "http://$service:$port$endpoint?department=Engineering" 2>/dev/null)
    
    if [ $? -eq 0 ] && echo "$response" | grep -q '"users"'; then
        count=$(echo "$response" | jq -r '.count // empty' 2>/dev/null)
        total=$(echo "$response" | jq -r '.totalUsers // empty' 2>/dev/null)
        time_ms=$(echo "$response" | jq -r '.processingTimeMs // empty' 2>/dev/null)
        echo "Engineering users: $count, Total: $total, Time: ${time_ms}ms"
        echo "✅ SUCCESS"
    else
        echo "❌ FAILED"
        echo "Raw response: $response"
    fi
    echo ""
done

echo "MongoDB endpoint testing complete!"