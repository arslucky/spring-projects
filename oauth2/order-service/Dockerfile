# syntax=docker/dockerfile:1.4
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY ./target/order-service-0.0.1-SNAPSHOT.jar order-service.jar
#EXPOSE ${port} random port
CMD ["java", "-jar", "order-service.jar"]+