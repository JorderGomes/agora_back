FROM maven:3.9.6-eclipse-temurin-17 AS build 
WORKDIR /app  

COPY pom.xml .
COPY src ./src 

RUN mvn clean package -DskipTests  

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app 

EXPOSE 8080

COPY --from=build /app/target/realtime-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]