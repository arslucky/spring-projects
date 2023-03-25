# syntax=docker/dockerfile:1.4
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY ./target/log-server-0.0.1-SNAPSHOT.jar log-server.jar
#EXPOSE ${port}
CMD ["java", "-jar", "log-server.jar"]