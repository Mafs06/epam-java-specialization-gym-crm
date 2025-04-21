# Usa una imagen oficial de Maven para compilar la aplicación
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Usa una imagen de Java para ejecutar la aplicación
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/gym-crm-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
