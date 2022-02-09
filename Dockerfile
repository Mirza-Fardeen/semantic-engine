FROM adoptopenjdk/openjdk11:alpine-jre
LABEL maintainer="chi-hung.le@insight-centre.org"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app.jar"]