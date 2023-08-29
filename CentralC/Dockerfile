FROM eclipse-temurin:17

RUN mkdir /app
WORKDIR /app

COPY CentralC/target/CentralC-*.jar app.jar

# Go back to the base directory for our server
WORKDIR /app
CMD ["java", "-jar", "/app/app.jar"]