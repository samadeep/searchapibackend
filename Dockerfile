# Use OpenJDK 21 as the base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose the port your app runs on
ENV PORT=8080
EXPOSE $PORT

# Command to run the application
CMD ["sh", "-c", "java -jar target/searchapi-0.0.1-SNAPSHOT.jar --server.port=$PORT"] 