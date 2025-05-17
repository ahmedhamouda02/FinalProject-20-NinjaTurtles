# Use OpenJDK 25 base image
FROM openjdk:25-ea-4-jdk-oraclelinux9

WORKDIR /app

# Copy the JAR file (adjust the name here if it's different)
COPY target/order-0.0.1-SNAPSHOT.jar order.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "order.jar"]
