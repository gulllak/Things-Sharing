FROM maven:3.8.4-openjdk-11 AS build

WORKDIR /build

COPY ./pom.xml ./server/pom.xml
COPY ./src ./server/src

WORKDIR /build/server
RUN mvn --fail-never verify -Pskip-unpack
RUN mvn -T 10C -Dmaven.test.skip -DskipTests package -Pskip-unpack

FROM amazoncorretto:11.0.23-alpine
EXPOSE 8090
ARG JAR_FILE
COPY --from=build /build/server/target/${JAR_FILE} /server.jar

ENTRYPOINT ["java","-jar","/server.jar"]