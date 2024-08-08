FROM maven:3.8.4-openjdk-11 as build
WORKDIR /build
COPY pom.xml .
RUN mvn --fail-never verify
COPY src src
RUN mvn -T 10C -Dmaven.test.skip -DskipTests -o package

FROM amazoncorretto:11.0.23-alpine
EXPOSE 8090
ARG JAR_FILE
COPY --from=build /build/target/${JAR_FILE} /gateway.jar
#COPY target/*.jar gateway.jar

ENTRYPOINT ["java","-jar","gateway.jar"]