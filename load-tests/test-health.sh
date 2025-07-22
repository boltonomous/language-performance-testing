#!/bin/sh
# Health check test for all services

echo "Testing health endpoints..."

services="java-micronaut:8080 kotlin-micronaut:8081 kotlin-ktor:8082 scala-zio:8083 golang:8084"

for service in $services; do
    echo "Testing $service/health"
    response=$(curl -s -w "%{http_code}" -o /dev/null "http://$service/health")
    if [ "$response" = "200" ]; then
        echo "✅ $service - OK"
    else
        echo "❌ $service - Failed (HTTP $response)"
    fi
done