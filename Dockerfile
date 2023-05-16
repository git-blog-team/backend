FROM openjdk:17-jdk-slim AS builder
COPY blograss-server/gradlew .
COPY blograss-server/gradle gradle
COPY blograss-server/build.gradle .
COPY blograss-server/settings.gradle .
COPY blograss-server/src src
COPY application.properties application.properties
RUN chmod +x ./gradlew
RUN apt-get update && apt-get install -y findutils
RUN ./gradlew bootJar

FROM openjdk:17-jdk-slim
COPY --from=builder build/libs/*.jar app.jar
COPY --from=builder application.properties application.properties

EXPOSE 8080
ENTRYPOINT ["java","-Dspring.config.location=file:/application.properties","-jar","/app.jar"]