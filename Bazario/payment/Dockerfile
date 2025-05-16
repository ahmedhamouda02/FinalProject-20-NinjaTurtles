FROM openjdk:17-slim

WORKDIR /app

# copy the fat-jar you just built
COPY target/Paymentgp-0.0.1-SNAPSHOT.jar /app/paymentgp.jar

EXPOSE 8080

CMD ["java","-jar","/app/paymentgp.jar"]
