FROM gradle:7.3.0-jdk11 AS build

ENV LANG en_US.UTF-8

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar

FROM openjdk:11
RUN mkdir /usr/src/myapp
COPY --from=build /home/gradle/src/build/libs/wildberries-0.0.1-SNAPSHOT.jar /usr/src/myapp/wildberries.jar
WORKDIR /usr/src/myapp

EXPOSE 8080
ENTRYPOINT exec java -Dfile.encoding=UTF-8 -jar /usr/src/myapp/wildberries.jar