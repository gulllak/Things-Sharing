FROM amazoncorretto:11.0.23-alpine
EXPOSE 8090
COPY target/*.jar gateway.jar
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENV OTEL_SERVICE_NAME="Gateway"
ENV OTEL_EXPORTER_OTLP_ENDPOINT="http://jaeger:4318"
ENV JAVA_TOOL_OPTIONS="-javaagent:opentelemetry-javaagent.jar"

ENTRYPOINT ["java","-jar","gateway.jar"]