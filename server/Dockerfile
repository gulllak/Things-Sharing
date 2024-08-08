FROM maven:3.8.4-openjdk-11 as build
WORKDIR /build
COPY pom.xml .
RUN mvn --fail-never verify
COPY src src
RUN mvn -T 10C -Dmaven.test.skip -DskipTests -o package

FROM amazoncorretto:11.0.23-alpine
EXPOSE 9091
ARG JAR_FILE
COPY --from=build /build/target/${JAR_FILE} /server.jar
COPY target/*.jar server.jar

ENTRYPOINT ["java","-jar","server.jar"]