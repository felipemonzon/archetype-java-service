# --- Stage 1: Build the application ---
# Use a Gradle image with JDK 26 for building the application
FROM gradle:8.8-jdk26-jammy AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy only necessary files for Gradle to determine dependencies
# This leverages Docker's build cache: if these files don't change,
# subsequent layers can be reused, speeding up builds.
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

# Copy the application source code
COPY src src

# Build the application
# --no-daemon is recommended for CI/CD environments
# -x test skips running tests during the build, as tests are typically run in a separate CI step
RUN ./gradlew clean build -x test

# --- Stage 2: Create the final runtime image ---
# Use a lightweight OpenJDK JRE 26 image for the final application
FROM openjdk:26-jre-slim-buster

# Set timezone (optional, but good practice for consistent logging and date handling)
ENV TZ=America/Mexico_City
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Expose the port the Spring Boot application runs on
EXPOSE 8080

# Set the working directory for the final application
WORKDIR /app

# Copy the built JAR file from the 'builder' stage to the final image
# The JAR file is typically found in build/libs/ and named like 'your-app-name-version.jar'
# Using '*.jar' ensures it works even if the version changes.
COPY --from=builder /app/build/libs/*.jar app.jar

# Define the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
