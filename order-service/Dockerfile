FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/order-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_order.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app_order.jar"]