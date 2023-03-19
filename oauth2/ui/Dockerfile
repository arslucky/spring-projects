# syntax=docker/dockerfile:1.4
FROM eclipse-temurin:8-jdk-jammy
WORKDIR /app
COPY ./target/ui-0.0.1-SNAPSHOT.jar ui.jar
#EXPOSE ${port} random port
CMD ["java", "-jar", "ui.jar"]+