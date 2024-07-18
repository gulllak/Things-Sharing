FROM flyway/flyway:8.5.13-alpine
COPY src/main/resources /flyway/sql

ENV FLYWAY_URL=jdbc:postgresql://db:5432/share
ENV FLYWAY_USER=evgenii
ENV FLYWAY_PASSWORD=12345678

ENTRYPOINT ["flyway", "migrate"]