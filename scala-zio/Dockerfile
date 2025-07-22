# Multi-stage build for Scala 3 + ZIO
FROM sbtscala/scala-sbt:eclipse-temurin-jammy-21.0.1_12_1.9.7_3.3.1 AS builder

# Set working directory
WORKDIR /app

# Copy SBT files
COPY project/ project/
COPY build.sbt ./

# Copy source code
COPY src/ src/

# Build the application
RUN sbt assembly

# Runtime stage
FROM eclipse-temurin:21-jre

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/scala-3.3.4/scala-zio-bakeoff.jar app.jar

# Create non-root user for security
RUN groupadd -r appuser -g 1001 && \
    useradd -u 1001 -r -g appuser -m -d /app -s /sbin/nologin -c "App user" appuser

# Change ownership of the app directory
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8083

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8083/health || exit 1

# Run the application
CMD ["java", "-jar", "app.jar"]