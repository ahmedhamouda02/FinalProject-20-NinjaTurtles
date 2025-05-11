FROM openjdk:25-ea-4-jdk-oraclelinux9

WORKDIR /app

# Copy the built .jar into the container directly
COPY ./target/productcart.jar .

ENV instance_name="Main Instance"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "productcart.jar"]
