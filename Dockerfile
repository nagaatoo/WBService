FROM openjdk:11

ENV LANG en_US.UTF-8

USER root
RUN mkdir /usr/src/myapp
COPY ./build/libs/wildberries-0.0.1-SNAPSHOT.jar /usr/src/myapp/wildberries.jar
WORKDIR /usr/src/myapp

EXPOSE 8080
ENTRYPOINT exec java -Dfile.encoding=UTF-8 -jar /usr/src/myapp/wildberries.jar