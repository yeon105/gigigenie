package com.gigigenie.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 인증 정보(쿠키, HTTP 인증, 클라이언트 측 SSL 인증서 등)를 포함할 수 있도록 허용
        config.setAllowCredentials(true);

        // React 개발 서버의 도메인 허용
        config.addAllowedOrigin("http://localhost:3000");

        // 모든 헤더 허용
        config.addAllowedHeader("*");

        // 모든 HTTP 메서드(GET, POST, PUT, DELETE 등) 허용
        config.addAllowedMethod("*");

        // 모든 경로에 대해 위 설정 적용
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
