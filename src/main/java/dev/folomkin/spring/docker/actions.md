# Шаги для запуска приложения:
___1. Сборка jar:___
```shell
mvn clean package
```
___2. Сборка образа:___
```shell
docker build -t springboot-docker-demo .
```
___3. Запуск контейнера на основе обаза:___
```shell
docker container run -i -t -p 8080:8080 springboot-docker-demo --name springboot-docker
```

# Сборка образов

### Dockerfile(maven)

#FROM openjdk:17-slim-buster
#EXPOSE 8080
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#
FROM eclipse-temurin:17.0.5_8-jre-focal as builder
WORKDIR extracted
ADD ./target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract
#
FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR application
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]


##### with plugin configs
#FROM eclipse-temurin:17.0.5_8-jre-focal as builder
#WORKDIR application
#ADD maven/springboot-docker-demo-0.0.1-SNAPSHOT.jar ./
#RUN java -Djarmode=layertools -jar springboot-docker-demo-0.0.1-SNAPSHOT.jar extract
#
#FROM eclipse-temurin:17.0.5_8-jre-focal
#WORKDIR application
#COPY --from=builder application/dependencies/ ./
#COPY --from=builder application/spring-boot-loader/ ./
#COPY --from=builder application/snapshot-dependencies/ ./
#COPY --from=builder application/application/ ./
#EXPOSE 8080
#ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

#### actions:
##### Шаги для запуска приложения:
___1. Сборка jar:___
```shell
mvn clean package
```
___2. Сборка образа:___
```shell
docker build -t springboot-docker-demo .
```
___3. Запуск контейнера на основе обаза:___
```shell
docker container run -i -t -p 8080:8080 springboot-docker-demo --name springboot-docker
```
========== DockerFile instruction from AI Docker ===========

Creating a Dockerfile for a Spring Boot application involves defining the steps to build and run your
application within a Docker container. Below is a basic example of a multi-stage Dockerfile for a Spring Boot
application using Maven. This approach helps create a lean and efficient Docker image by separating the build and
runtime environments.

```dockerfile
# Use the official Eclipse Temurin image as the base for building the application
FROM eclipse-temurin:21-jdk-jammy as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and the pom.xml file to the container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download the project dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code to the container
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Use a smaller JRE base image for the final image
FROM eclipse-temurin:21-jre-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Explanation:

1. **Builder Stage**:
    - **Base Image**: Uses `eclipse-temurin:21-jdk-jammy` for building the application.
    - **Dependencies**: Copies the Maven wrapper and `pom.xml` to download dependencies.
    - **Source Code**: Copies the source code and builds the application using Maven.

2. **Final Stage**:
    - **Base Image**: Uses a smaller JRE image `eclipse-temurin:21-jre-jammy` for running the application.
    - **Copy JAR**: Copies the built JAR file from the builder stage.
    - **Expose Port**: Exposes port 8080 for the application.
    - **Run Application**: Uses `java -jar` to run the Spring Boot application.

This Dockerfile is a good starting point for containerizing a Spring Boot application. You can further
customize it based on your specific requirements, such as adding environment variables or configuring the
application for different environments.

For more detailed information, you can refer to the
[official Docker documentation on multi-stage builds](https://docs.docker.com/build/building/multi-stage/) and
[Spring Boot Docker guide](https://spring.io/guides/topicals/spring-boot-docker).



### Dockerfile(gradle)

###### v1:
#FROM gradle:8.5-jdk17-alpine as build
#COPY --chown=gradle:gradle . /home/gradle/src
#WORKDIR /home/gradle/src
#RUN gradle build --no-daemon
#FROM openjdk:17-jdk-slim
#EXPOSE 8080
#RUN mkdir /app
#COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
#ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]
#

###### v2:
#FROM eclipse-temurin:17.0.5_8-jre-focal as builder
#WORKDIR extracted
#ADD ./build/libs/*.jar app.jar
#RUN java -Djarmode=layertools -jar app.jar extractava", "-jar", "/app.jar"]
#
#FROM eclipse-temurin:17.0.5_8-jre-focal
#WORKDIR application
#COPY --from=builder extracted/dependencies/ ./
#COPY --from=builder extracted/spring-boot-loader/ ./
#COPY --from=builder extracted/snapshot-dependencies/ ./
#COPY --from=builder extracted/application/ ./
#EXPOSE 8080
#ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

FROM eclipse-temurin:17-jre
WORKDIR /opt/app
EXPOSE 8080
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar", "app.jar"]
#

#### actions:

```shell
gradle build
```

```shell
docker build -t docker-gradle-image .
```
```shell
docker run -i -t -p 8080:8080 --name docker-gradle-cont docker-gradle-image
```
