# syntax=docker/dockerfile:1.4
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY ./target/config-server-0.0.1-SNAPSHOT.jar config-server.jar
#EXPOSE ${port}
CMD ["java", "-jar", "config-server.jar"]