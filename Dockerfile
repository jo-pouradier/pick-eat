# Stage 1: Build the application
FROM gradle:8.11.1-jdk17 AS build
ARG TARGET_SERVICE=event
WORKDIR /home/gradle/project
COPY . .
RUN gradle $TARGET_SERVICE:build --no-daemon

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim
ARG TARGET_SERVICE=event
COPY --from=build /home/gradle/project/$TARGET_SERVICE/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]