FROM flyway/flyway:8.5.13-alpine

USER root
RUN apk --no-cache add curl

COPY src/main/resources /flyway/sql
COPY migration-script.sh /flyway/migration-script.sh
RUN chmod +x /flyway/migration-script.sh

ENV FLYWAY_URL=jdbc:postgresql://db:5432/share
ENV FLYWAY_USER=postgres
ENV FLYWAY_PASSWORD=postgres
ENV PROMETHEUS_PUSHGATEWAY=http://prometheus-pushgateway:9091

USER flyway
ENTRYPOINT ["/flyway/migration-script.sh"]