FROM flyway/flyway:8.5.13-alpine
COPY src/main/resources /flyway/sql

ENV FLYWAY_URL=jdbc:postgresql://db:5432/share
ENV FLYWAY_USER=postgres
ENV FLYWAY_PASSWORD=postgres

ENTRYPOINT ["flyway", "migrate"]