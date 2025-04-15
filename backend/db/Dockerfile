FROM pgvector/pgvector:latest

# 초기화 SQL 스크립트 복사
COPY gigigenie.sql /docker-entrypoint-initdb.d/

# pgvector 확장 활성화
RUN echo "shared_preload_libraries = 'vector'" >> /var/lib/postgresql/postgresql.conf