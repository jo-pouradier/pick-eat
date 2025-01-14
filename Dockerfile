FROM gradle:8.11.1-jdk17 AS build

WORKDIR /app

COPY . .

RUN gradle build --no-daemon -x test

FROM openjdk:17-jdk-slim

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]