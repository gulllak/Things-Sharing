FROM amazoncorretto:11.0.23-alpine
EXPOSE 9091
COPY target/*.jar server.jar

ENTRYPOINT ["java","-jar","server.jar"]