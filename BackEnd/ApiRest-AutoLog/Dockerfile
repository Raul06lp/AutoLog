FROM openjdk:17-jdk-slim

WORKDIR /api

COPY target/*.jar app.jar

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]