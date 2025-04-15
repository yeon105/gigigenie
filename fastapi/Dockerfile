FROM python:3.9-slim

WORKDIR /app

# 시스템 의존성 설치 (psycopg2-binary 빌드에 필요한 패키지)
RUN apt-get update && apt-get install -y \
    gcc \
    python3-dev \
    libpq-dev \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# 의존성 파일 복사 및 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
RUN pip install pgvector psycopg2-binary

# 애플리케이션 코드 복사
COPY . .

# 서버 실행
CMD ["python", "main.py"]