# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container
COPY target/user-service.jar /app/user-service.jar

# Expose the port on which the app will run
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]

#test
