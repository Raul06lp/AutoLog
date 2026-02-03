# Stage 1: build
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copiamos lo necesario
COPY pom.xml ./pom.xml
COPY src ./src

# Instalacion de Maven y compilacion
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Stage 2: run
FROM eclipse-temurin:17-jdk
WORKDIR /api

# Copia del jar
COPY --from=build /app/target/*.jar app.jar

ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
