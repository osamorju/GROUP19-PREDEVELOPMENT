# Database image: start with the MySQL long-term support release.
FROM mysql:8.4

LABEL org.opencontainers.image.source="https://github.com/osamorju/GROUP19-PREDEVELOPMENT"

# MySQL runs this file once when it creates a new database volume.
COPY --chown=mysql:mysql --chmod=0444 db/world.sql /docker-entrypoint-initdb.d/001-world.sql