# Use Java 8 to Build the Image
FROM maven:3.8.4-openjdk-8 AS build

# Set the Spring Profile to test for the build process (Integration Test)
ENV SPRING_PROFILES_ACTIVE=test

# Copy the source code into the container
WORKDIR /app
COPY . /app

# Build the application
RUN mvn clean package

# Generate the final image
FROM openjdk:8-jdk-alpine

# Copy the built JAR file into the final image
COPY --from=build /app/target/order-service.jar /app/order-service.jar

# Set the working directory
WORKDIR /app

# Run the JAR file with the correct profile (product env)
ENTRYPOINT ["java", "-jar", "/app/order-service.jar", "--spring.profiles.active=prd"]

# Expose the application port
EXPOSE 8080
