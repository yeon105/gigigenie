FROM node:18-alpine as build

WORKDIR /app

# 의존성 파일 복사 및 설치
COPY package*.json ./
RUN npm install

# 소스 코드 복사 및 빌드
COPY . .
RUN npm run build

# 프로덕션 환경
FROM nginx:alpine

# 빌드 결과물 복사
COPY --from=build /app/build /usr/share/nginx/html

# nginx 설정
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]