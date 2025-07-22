# Java 21 + Micronaut Implementation

## Running the Application

```bash
# Build the application
./gradlew build

# Run the application
./gradlew run

# Or run with shadow jar
./gradlew shadowJar
java -jar build/libs/java-micronaut-bakeoff-0.1-all.jar
```

The application will start on http://localhost:8080

## Testing the Endpoints

```bash
# Health check
curl http://localhost:8080/health

# Math computation
curl -X POST http://localhost:8080/api/compute \
  -H "Content-Type: application/json" \
  -d '{"numbers": [1, 2, 3, 4, 5], "operation": "sum"}'

# Fibonacci test
curl -X POST http://localhost:8080/api/compute \
  -H "Content-Type: application/json" \
  -d '{"numbers": [30], "operation": "fibonacci"}'
```