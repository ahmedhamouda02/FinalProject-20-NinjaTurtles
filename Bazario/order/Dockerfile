FROM openjdk:23-jdk-slim

WORKDIR /app

# copy the fat-jar you just built
COPY target/order-0.0.1-SNAPSHOT.jar /app/order.jar

EXPOSE 8080

CMD ["java","-jar","/app/order.jar"]