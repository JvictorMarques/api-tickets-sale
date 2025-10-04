FROM maven:3.9-eclipse-temurin-25 AS builder

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jdk-jammy AS final

RUN adduser --disabled-password --gecos "" spring

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN chown spring:spring app.jar
USER spring

HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 CMD curl -f http://localhost:8080/api/actuator/health

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
