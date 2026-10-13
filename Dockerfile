# Build stage: compile the Java source and create the application JAR.
FROM maven:3.9-eclipse-temurin-24 AS build

WORKDIR /build

# Copy the Maven file first so Docker can reuse downloaded dependencies.
COPY pom.xml .
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline

COPY src ./src
RUN mvn --batch-mode --no-transfer-progress clean package -DskipTests

# Runtime stage: keep only Java and the compiled application JAR.
FROM eclipse-temurin:24-jre AS runtime

LABEL org.opencontainers.image.source="https://github.com/osamorju/GROUP19-PREDEVELOPMENT"

WORKDIR /app

RUN useradd --create-home --uid 10001 appuser \
    && mkdir -p /app/reports \
    && chown -R appuser:appuser /app

COPY --from=build --chown=appuser:appuser /build/target/gp19CourseWork.jar /app/app.jar

USER appuser

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
