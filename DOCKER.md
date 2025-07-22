# Docker Deployment Guide

This document provides comprehensive Docker deployment instructions for the performance bake-off project.

## Quick Start

```bash
# Start all services
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs

# Run load tests
docker-compose exec load-tester ./benchmark.sh
```

## Individual Service Deployment

### Java + Micronaut
```bash
cd java-micronaut
docker build -t java-micronaut-bakeoff .
docker run -d -p 8080:8080 --name java-micronaut java-micronaut-bakeoff
```

### Kotlin + Micronaut
```bash
cd kotlin-micronaut
docker build -t kotlin-micronaut-bakeoff .
docker run -d -p 8081:8081 --name kotlin-micronaut kotlin-micronaut-bakeoff
```

### Kotlin + Ktor
```bash
cd kotlin-ktor
docker build -t kotlin-ktor-bakeoff .
docker run -d -p 8082:8082 --name kotlin-ktor kotlin-ktor-bakeoff
```

### Scala + ZIO
```bash
cd scala-zio
docker build -t scala-zio-bakeoff .
docker run -d -p 8083:8083 --name scala-zio scala-zio-bakeoff
```

## Docker Architecture

### Multi-stage Builds
All Dockerfiles use multi-stage builds for optimal image size:
- **Builder stage**: Contains JDK and builds the application
- **Runtime stage**: Contains only JRE and the compiled JAR

### Security Features
- Non-root user execution
- Minimal base images (Ubuntu JRE)
- Health checks for all services
- Resource constraints via docker-compose

### Health Monitoring
Each service includes health checks:
```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
  interval: 30s
  timeout: 10s
  retries: 3
  start_period: 40s
```

## Load Testing Container

The `load-tester` service provides a complete testing environment:

```bash
# Access the load testing container
docker-compose exec load-tester sh

# Available tools:
ab --help        # Apache Bench
wrk --help       # Modern load testing
curl --help      # HTTP client

# Pre-built test scripts:
./test-health.sh    # Health check tests
./test-compute.sh   # Compute endpoint tests  
./benchmark.sh      # Full performance benchmark
```

## Performance Optimization

### JVM Tuning
Each service is configured with optimal JVM settings:
```yaml
environment:
  - JAVA_OPTS=-Xmx512m -Xms256m
```

### Resource Limits
Consider adding resource limits for fair comparison:
```yaml
deploy:
  resources:
    limits:
      cpus: '1.0'
      memory: 512M
    reservations:
      cpus: '0.5'
      memory: 256M
```

## Troubleshooting

### Common Issues

**Build Failures on ARM64 (Apple Silicon):**
- Fixed by using Ubuntu-based images instead of Alpine
- Gradle daemon issues resolved with `--no-daemon` flag

**Memory Issues:**
```bash
# Increase Docker memory limit if needed
# Docker Desktop → Settings → Resources → Memory
```

**Network Issues:**
```bash
# Recreate network
docker-compose down
docker-compose up -d
```

**Service Dependencies:**
```bash
# Check startup order
docker-compose logs | grep -E "(started|ready|listening)"
```

### Debugging

```bash
# View service logs
docker-compose logs java-micronaut
docker-compose logs kotlin-micronaut
docker-compose logs kotlin-ktor
docker-compose logs scala-zio

# Enter container for debugging
docker-compose exec java-micronaut bash

# Check health endpoints manually
curl http://localhost:8080/health
curl http://localhost:8081/health
curl http://localhost:8082/health
curl http://localhost:8083/health
```

## Production Considerations

### Scaling
```yaml
# Add to docker-compose.yml for horizontal scaling
deploy:
  replicas: 3
```

### Monitoring
Consider adding monitoring stack:
```yaml
services:
  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
  
  grafana:
    image: grafana/grafana
    ports:
      - "3000:3000"
```

### Reverse Proxy
Add nginx or Traefik for load balancing:
```yaml
services:
  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
```

## Cleanup

```bash
# Stop and remove containers
docker-compose down

# Remove images
docker rmi java-micronaut-bakeoff
docker rmi kotlin-micronaut-bakeoff  
docker rmi kotlin-ktor-bakeoff
docker rmi scala-zio-bakeoff

# Clean up unused Docker resources
docker system prune -a
```