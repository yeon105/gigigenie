FROM postgres:17

COPY vector/vector.so /usr/lib/postgresql/17/lib/
COPY vector/vector.control /usr/share/postgresql/17/extension/
COPY vector/vector--0.8.0.sql /usr/share/postgresql/17/extension/

COPY gigigenie.sql /docker-entrypoint-initdb.d/