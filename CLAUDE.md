# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Structure

This is a collection of performance-oriented JVM projects, including both legacy implementations and a new Claude-created performance bake-off comparison.

### Main Projects

1. **claude-performance-bakeoff/** - New performance comparison suite
   - **java-micronaut/** - Java 21 + Micronaut (port 8080)
   - **kotlin-micronaut/** - Kotlin + Micronaut (port 8081) 
   - **kotlin-ktor/** - Kotlin + Ktor (port 8082)
   - **scala-zio/** - Scala + ZIO (port 8083)
   - **kotlin-springboot/** - Kotlin + Spring Boot (port 8085)

## Build Commands

### Claude Performance Bake-off
Each implementation in `claude-performance-bakeoff/` uses standard build tools:

#### Docker (Recommended)
```bash
cd claude-performance-bakeoff

# Start all services
docker-compose up -d

# Run benchmarks  
docker-compose exec load-tester ./benchmark.sh

# Individual service builds
docker build -t java-micronaut ./java-micronaut
docker build -t kotlin-micronaut ./kotlin-micronaut
docker build -t kotlin-ktor ./kotlin-ktor
docker build -t scala-zio ./scala-zio
docker build -t kotlin-springboot ./kotlin-springboot
```

#### Local Development
```bash
# Java + Micronaut
cd claude-performance-bakeoff/java-micronaut
./gradlew build
./gradlew run          # or shadowJar + java -jar

# Kotlin + Micronaut  
cd claude-performance-bakeoff/kotlin-micronaut
./gradlew build
./gradlew run

# Kotlin + Ktor
cd claude-performance-bakeoff/kotlin-ktor
./gradlew build
./gradlew run

# Scala + ZIO (may require non-corporate environment)
cd claude-performance-bakeoff/scala-zio
sbt compile
sbt run

# Kotlin + SpringBoot
cd claude-performance-bakeoff/kotlin-springboot
./gradlew build
./gradlew bootRun
```

### Legacy Projects

```bash
# Micronaut projects (amp-kotlin, bakeoff-ktor-performance)
./gradlew build
./gradlew run

# Scala projects (poweramp-*-service)
sbt compile
sbt run
```

## Architecture Notes

### Claude Performance Bake-off
- All implementations provide identical REST APIs for fair comparison
- Each runs on different ports (8080-8083, 8085) for concurrent testing
- Endpoints: `/health` and `/api/compute` (math operations)
- Built for performance testing with tools like Apache Bench, wrk, JMeter

### Micronaut Projects
- Use AOT compilation and GraalVM optimizations
- Require `open` classes/methods in Kotlin for AOP advice
- Include test-resources service for Docker containers
- YAML configuration in `src/main/resources/application.yml`

### Scala Projects
- Multi-module SBT builds with separate api/service/config modules
- Use Play Framework with custom deployment plugins
- Configuration in `services.conf` and `*.sbt` files

### SpringBoot Projects
- Uses Spring Boot's embedded Tomcat server
- Auto-configuration and dependency injection with annotations
- Properties configuration in `src/main/resources/application.properties`
- Built-in actuator endpoints for health monitoring

## Common Development Patterns

### Micronaut (Java/Kotlin)
- Controllers: `@Controller`, methods with `@Get`/`@Post`
- Services: `@Singleton` for dependency injection  
- Models: `@Serdeable` for JSON serialization
- Validation: Jakarta validation annotations (`@NotNull`, `@Valid`)

### Important Kotlin Micronaut Requirement
Classes and methods that use Micronaut annotations must be `open` to support AOP:
```kotlin
@Controller
open class MyController {
    @Get("/endpoint")
    open fun endpoint(): String = "response"
}
```

### SpringBoot (Kotlin)
- Controllers: `@RestController`, methods with `@GetMapping`/`@PostMapping`
- Services: `@Service` for dependency injection
- Models: Standard data classes with kotlinx.serialization
- Validation: Jakarta validation annotations (`@Valid`, `@RequestBody`)
- Unlike Micronaut, classes do NOT need to be `open` for Spring's proxy-based AOP

## Testing

### Performance Testing
The bake-off projects are specifically designed for load testing:

```bash
# Health check
curl http://localhost:808X/health

# Math computation
curl -X POST http://localhost:808X/api/compute \
  -H "Content-Type: application/json" \
  -d '{"numbers": [1,2,3,4,5], "operation": "sum"}'

# Load testing
ab -n 1000 -c 10 http://localhost:808X/health
```

### Build Testing
Always verify builds succeed incrementally when implementing new features:
```bash
./gradlew build  # Gradle projects
sbt compile      # SBT projects
```

## Dependencies and Technologies

### JVM Frameworks
- **Micronaut 4.9.1** - Reactive framework with AOT compilation
- **Ktor 3.2.2** - Lightweight Kotlin coroutine-based framework  
- **Spring Boot 3.4.1** - Full-featured framework with embedded server and auto-configuration
- **Play Framework** - Scala web framework (legacy projects)
- **ZIO** - Scala functional effects library

### Key Libraries
- Jackson for JSON serialization (`jackson-module-kotlin`)
- Netty for HTTP server implementation
- Logback for logging
- Testcontainers for integration testing (auto-managed)

## Deployment Considerations

- **Kubernetes configs** in `amp-kube-configs/` for legacy services
- **Docker** support via Gradle plugins (shadow jars preferred for JVM apps)
- **Ports**: Each service uses different ports to avoid conflicts
- **Java 21** is the target JVM version for new implementations