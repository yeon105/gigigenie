FROM postgres:17

# 빌드 도구 설치 & pgvector 설치
RUN apt-get update && apt-get install -y \
    git make gcc postgresql-server-dev-17 && \
    git clone https://github.com/pgvector/pgvector.git && \
    cd pgvector && make && make install && \
    cd .. && rm -rf pgvector

# 초기화 SQL 복사
COPY gigigenie.sql /docker-entrypoint-initdb.d/