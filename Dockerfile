FROM gradle:8.6.0-jdk21 as gradle-build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:21-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=gradle-build /home/gradle/src/build/libs/*.jar /app/thundera.jar

ENTRYPOINT ["java", "-jar", "/app/thundera.jar"]