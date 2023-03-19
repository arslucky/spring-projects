# syntax=docker/dockerfile:1.4
FROM eclipse-temurin:8-jdk-jammy
WORKDIR /app
COPY ./target/resource-0.0.1-SNAPSHOT.jar resource.jar
#EXPOSE ${port} random port
CMD ["java", "-jar", "resource.jar"]+