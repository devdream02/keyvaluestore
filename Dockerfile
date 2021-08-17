FROM maven:3.6.2-jdk-8 as base
ARG APP_PATH=/usr/src/app/
WORKDIR ${APP_PATH}
COPY ./src ./src
COPY ./pom.xml ./pom.xml

FROM base as build
RUN mvn -q compile -DskipTests

FROM build as test
RUN mvn -q test

FROM build as package
RUN mvn -q package -DskipTests

FROM openjdk:8-jre-alpine as release
RUN apk --no-cache add curl
RUN /usr/sbin/adduser -D -s /bin/sh docker_user
ARG APP_NAME
ARG APP_PATH=/usr/src/app
WORKDIR ${APP_PATH}
COPY --from=package ${APP_PATH}/target/mcadocdownload*.jar app.jar
RUN chown -R docker_user:root /usr/src/app
RUN chmod 775 /usr -R
EXPOSE 8080
USER docker_user
ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom","-jar", "./app.jar" ]