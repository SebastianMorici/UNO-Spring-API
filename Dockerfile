FROM eclipse-temurin:17-alpine
COPY target/uno-backend-0.0.1-SNAPSHOT.jar UNO-app.jar
ENTRYPOINT ["java", "-jar", "UNO-app.jar"]