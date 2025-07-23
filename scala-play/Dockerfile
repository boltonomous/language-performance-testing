# Multi-stage build for Scala 2.13 + Play Framework
FROM sbtscala/scala-sbt:eclipse-temurin-jammy-21.0.1_12_1.9.7_2.13.12 AS builder

# Set working directory
WORKDIR /app

# Copy SBT files
COPY project/ project/
COPY build.sbt ./

# Copy configuration and source code
COPY conf/ conf/
COPY app/ app/

# Install unzip and build the application (Play universal dist)
RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*
RUN sbt dist

# Extract the built distribution
RUN unzip target/universal/scala-play-bakeoff-1.0.zip -d target/universal/

# Runtime stage
FROM eclipse-temurin:21-jre

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the built application from builder stage
COPY --from=builder /app/target/universal/scala-play-bakeoff-1.0/ ./

# Create non-root user for security
RUN groupadd -r appuser -g 1001 && \
    useradd -u 1001 -r -g appuser -m -d /app -s /sbin/nologin -c "App user" appuser

# Change ownership of the app directory
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8086

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8086/health || exit 1

# Run the application
CMD ["./bin/scala-play-bakeoff", "-Dhttp.port=8086", "-Dplay.http.secret.key=performance-bakeoff-secret-key-for-testing-only-not-for-production-use-very-long-random-key-here"]