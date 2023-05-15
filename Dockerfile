FROM openjdk:17-jdk-slim AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN apt-get update && apt-get install -y findutils
RUN ./gradlew bootJar

FROM openjdk:17-jdk-slim
COPY --from=builder build/libs/*.jar app.jar
COPY application.properties application.properties

EXPOSE 8080
ENTRYPOINT ["java","-Dspring.config.location=file:/application.properties","-jar","/app.jar"]