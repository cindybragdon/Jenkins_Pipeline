#cls515-labmaven-eq19/pom.xml

FROM eclipse-temurin:21
EXPOSE 8080
ARG JAR_FILE=/target/cls515-labmaven-eq19-1.0.14.jar
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]