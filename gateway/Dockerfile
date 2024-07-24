FROM amazoncorretto:11.0.23-alpine
EXPOSE 8090
COPY target/*.jar gateway.jar

ENTRYPOINT ["java","-jar","gateway.jar"]