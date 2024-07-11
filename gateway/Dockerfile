FROM amazoncorretto:11.0.23-alpine
EXPOSE 8080
COPY target/*.jar gateway.jar
ENTRYPOINT ["java","-jar","gateway.jar"]