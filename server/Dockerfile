FROM amazoncorretto:11.0.23-alpine
EXPOSE 9090
COPY target/*.jar server.jar
ENTRYPOINT ["java","-jar","server.jar"]