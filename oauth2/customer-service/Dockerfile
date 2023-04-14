# syntax=docker/dockerfile:1.4
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY ./target/customer-service-0.0.1-SNAPSHOT.jar customer-service.jar
#EXPOSE ${port} random port
CMD ["java", "-jar", "customer-service.jar"]+