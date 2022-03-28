# syntax=docker/dockerfile:1

FROM openjdk:8-alpine3.9

WORKDIR /app

COPY target/JokesETL.jar .

ENTRYPOINT ["java","-jar","JokesETL.jar"]