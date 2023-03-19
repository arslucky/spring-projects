# syntax=docker/dockerfile:1.4
FROM eclipse-temurin:8-jdk-jammy
WORKDIR /app
COPY ./target/authorization-server-0.0.1-SNAPSHOT.jar authorization-server.jar
EXPOSE ${port}
CMD ["java", "-jar", "authorization-server.jar"]