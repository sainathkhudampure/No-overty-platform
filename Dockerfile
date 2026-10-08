# ====================================================================
# Multi-Stage Dockerfile for NO POVERTY Welfare Platform (UN SDG 1)
# ====================================================================

# Stage 1: Build the Application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy POM and download dependencies to optimize caching
COPY backend/pom.xml ./
RUN mvn dependency:go-offline -B

# Copy backend source code and frontend resources
COPY backend/src ./src
COPY frontend/ ./src/main/resources/static/

# Build optimized executable JAR
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime Container
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Set non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy compiled artifact from build stage
COPY --from=build /app/target/platform-1.0.0.jar app.jar

# Set ownership
RUN chown -R appuser:appgroup /app
USER appuser

# Expose default application port
EXPOSE 8085

# Environment defaults
ENV SPRING_PROFILES_ACTIVE=default \
    SERVER_PORT=8085 \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8085/api/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

