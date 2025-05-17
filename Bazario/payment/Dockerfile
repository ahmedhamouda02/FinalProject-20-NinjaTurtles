FROM openjdk:23-jdk-slim

WORKDIR /app

# copy the fat-jar you just built
COPY target/payment-0.0.1-SNAPSHOT.jar /app/payment.jar

EXPOSE 8080

CMD ["java","-jar","/app/payment.jar"]