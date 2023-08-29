FROM eclipse-temurin:17

RUN mkdir /app
WORKDIR /app

COPY build/libs/*-all.jar app.jar
# Go back to the base directory for our server
WORKDIR /app
CMD ["java", "-jar", "/app/app.jar"]