FROM amazoncorretto:11.0.23-alpine
EXPOSE 9091
COPY target/*.jar server.jar

COPY target/*.jar gateway.jar
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENV OTEL_SERVICE_NAME="Server"
ENV OTEL_TRACES_EXPORTER=logging
ENV OTEL_METRICS_EXPORTER=none
ENV OTEL_LOGS_EXPORTER=none
ENV OTEL_EXPORTER_OTLP_ENDPOINT="http://jaeger:4318"
ENV JAVA_TOOL_OPTIONS="-javaagent:opentelemetry-javaagent.jar"

ENTRYPOINT ["java","-jar","server.jar"]