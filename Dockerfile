FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -Dmaven.test.skip=true


FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/ELearningPlatform-0.0.1-SNAPSHOT.jar /e_learning.jar
EXPOSE 8080
CMD ["java", "-jar", "/e_learning.jar"]
