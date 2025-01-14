# Stage 1: Build the application
FROM gradle:8.11.1-jdk17 AS build
WORKDIR /home/gradle/project
COPY . .
RUN gradle build --no-daemon

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]