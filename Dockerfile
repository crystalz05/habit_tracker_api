# Use a lightweight OpenJDK image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the Maven/Gradle build output JAR into the container
COPY target/*.jar app.jar

# Run the app with any JVM options and environment variables
ENTRYPOINT ["java", "-jar", "app.jar"]
