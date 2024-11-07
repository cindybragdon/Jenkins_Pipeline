FROM eclipse-temurin:21
EXPOSE 8080
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} /target/
ENTRYPOINT ["java","-jar","/app.jar"]