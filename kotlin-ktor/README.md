# Kotlin + Ktor Implementation

## Running the Application

```bash
# Build the application
./gradlew build

# Run the application
./gradlew run

# Or create a fat JAR
./gradlew buildFatJar
java -jar build/libs/kotlin-ktor-bakeoff-0.1-all.jar
```

The application will start on http://localhost:8082

## Testing the Endpoints

```bash
# Health check
curl http://localhost:8082/health

# Math computation
curl -X POST http://localhost:8082/api/compute \
  -H "Content-Type: application/json" \
  -d '{"numbers": [1, 2, 3, 4, 5], "operation": "sum"}'

# Fibonacci test
curl -X POST http://localhost:8082/api/compute \
  -H "Content-Type: application/json" \
  -d '{"numbers": [30], "operation": "fibonacci"}'
```

## Performance Characteristics

- **Lightweight**: Minimal overhead with coroutine-based concurrency
- **Async**: Non-blocking I/O with Kotlin coroutines
- **Fast startup**: Quick application boot time
- **Memory efficient**: Lower memory footprint compared to Spring Boot