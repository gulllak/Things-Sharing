FROM maven:3.8.4-openjdk-11 AS build

WORKDIR /build

COPY ./pom.xml ./gateway/pom.xml
COPY ./src ./gateway/src

WORKDIR /build/gateway
RUN mvn --fail-never verify
RUN mvn -T 10C -Dmaven.test.skip -DskipTests -o package

FROM amazoncorretto:11.0.23-alpine
EXPOSE 8090
ARG JAR_FILE
COPY --from=build /build/gateway/target/${JAR_FILE} /gateway.jar

ENTRYPOINT ["java","-jar","/gateway.jar"]