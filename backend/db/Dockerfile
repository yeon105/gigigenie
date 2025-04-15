FROM postgres:17

RUN apt-get update && apt-get install -y \
    git make gcc postgresql-server-dev-17 && \
    git clone https://github.com/pgvector/pgvector.git && \
    cd pgvector && make && make install && \
    cd .. && rm -rf pgvector

COPY gigigenie.sql /docker-entrypoint-initdb.d/