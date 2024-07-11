FROM amazoncorretto:11.0.23-alpine
COPY target/*.jar server.jar
ENTRYPOINT ["java","-jar","server.jar"]