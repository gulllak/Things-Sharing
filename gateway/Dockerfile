FROM amazoncorretto:11.0.23-alpine
COPY target/*.jar gateway.jar
ENTRYPOINT ["java","-jar","gateway.jar"]