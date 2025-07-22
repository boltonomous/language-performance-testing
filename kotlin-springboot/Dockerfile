# Multi-stage build for Kotlin + SpringBoot
FROM eclipse-temurin:21-jdk AS builder

# Set working directory
WORKDIR /app

# Copy Gradle files
COPY gradle/ gradle/
COPY gradlew build.gradle.kts gradle.properties ./

# Copy source code
COPY src/ src/

# Build the application
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Create non-root user for security
RUN groupadd -r appuser -g 1001 && \
    useradd -u 1001 -r -g appuser -m -d /app -s /sbin/nologin -c "App user" appuser

# Change ownership of the app directory
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8085

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8085/health || exit 1

# Run the application
CMD ["java", "-jar", "app.jar"]