FROM gradle:5.1.0-jdk11-slim as build-env
RUN mkdir -p /home/gradle/code
COPY --chown=gradle:gradle ./ /home/gradle/code
WORKDIR /home/gradle/code
RUN gradle build

FROM openjdk:11-jre-slim
RUN mkdir -p /app
COPY --from=build-env /home/gradle/code/build/libs/smartstore-*.jar /app/app.jar
EXPOSE 9456
ENTRYPOINT ["java","-jar","/app/app.jar"]