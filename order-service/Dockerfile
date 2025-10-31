FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Instalar Maven
RUN apk add --no-cache maven
#apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Compilar
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/order-service-0.0.1-SNAPSHOT.jar app_order.jar

# Railway asigna el puerto dinámicamente
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app_order.jar"]