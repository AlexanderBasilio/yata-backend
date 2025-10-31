FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY order-service/pom.xml .
COPY order-service/src ./srcgit

# Instalar Maven
RUN apk update && apk add --no-cache maven
#apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Compilar
RUN mvn clean package -DskipTests
#agregado 2
# Encuentra cualquier JAR que termine en .jar en target/ y muévelo a un nombre fijo
RUN find target/ -name "*.jar" -exec mv {} target/app-prod.jar \;

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
#COPY --from=build /app/target/order-service-0.0.1-SNAPSHOT.jar app_order.jar
COPY --from=build /app/target/app.jar app_order.jar

# Railway asigna el puerto dinámicamente
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app_order.jar"]