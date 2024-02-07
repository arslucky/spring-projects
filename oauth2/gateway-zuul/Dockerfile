# syntax=docker/dockerfile:1.4
FROM eclipse-temurin:8-jdk-jammy
WORKDIR /app
COPY ./target/gateway-0.0.1-SNAPSHOT-exec.jar gateway.jar
EXPOSE ${port}
CMD ["java", "-jar", "gateway.jar"]