FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/harmoniq-0.0.1-SNAPSHOT.jar harmoniq.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","harmoniq.jar"]
