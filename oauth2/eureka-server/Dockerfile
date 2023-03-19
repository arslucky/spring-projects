# syntax=docker/dockerfile:1.4
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY ./target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar
EXPOSE ${port}
CMD ["java", "-jar", "eureka-server.jar"]