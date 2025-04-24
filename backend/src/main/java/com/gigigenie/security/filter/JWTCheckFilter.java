package com.gigigenie.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigigenie.domain.member.enums.MemberRole;
import com.gigigenie.security.MemberDTO;
import com.gigigenie.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        log.info("check uri: " + path);

        // Pre-flight 요청은 필터를 타지 않도록 설정
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        // SecurityConfig와 일치하는 경로 패턴 - permitAll() 설정된 경로들
        if (path.equals("/api/member/login") ||
                path.equals("/api/member/join") ||
                path.equals("/api/member/check-email") ||
                path.equals("/api/member/me") ||
                path.equals("/api/product/search") ||
                path.equals("/api/product/list") ||
                (path.startsWith("/api/chat"))) {
            return true;
        }

        // 관리자 경로
        if (path.startsWith("/api/admin/member/login") ||
                path.startsWith("/api/admin/member/join") ||
                path.startsWith("/api/admin/member/refresh") ||
                path.startsWith("/api/admin/member/logout")) {
            return true;
        }

        // Swagger 및 기타 리소스 관련 경로 제외
        if (path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/h2-console") ||
                path.startsWith("/favicon.ico")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("------------------JWTCheckFilter 시작------------------");
        log.info("요청 경로: {}", request.getServletPath());

        String autHeaderStr = request.getHeader("Authorization");
        log.info("Authorization 헤더: {}", autHeaderStr);

        if (autHeaderStr == null) {
            log.info("Authorization 헤더 없음, 필터 통과");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = autHeaderStr.substring(7).trim();
            log.info("추출된 토큰: {}", accessToken.substring(0, 20) + "...");

            Map<String, Object> claims = jwtUtil.validateToken(accessToken);
            log.info("JWT claims: {}", claims);

            Integer id = (Integer) claims.get("id");
            String email = (String) claims.get("email");
            String password = (String) claims.get("password");
            String name = (String) claims.get("name");
            String role = (String) claims.get("role");
            log.info("추출된 role: {}", role);

            MemberRole memberRole = MemberRole.valueOf(role);
            MemberDTO memberDTO = new MemberDTO(id, email, password, name, memberRole);

            log.info("생성된 memberDTO: {}", memberDTO);
            log.info("memberDTO의 권한: {}", memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(memberDTO, password, memberDTO.getAuthorities());
            log.info("생성된 인증 토큰: {}", authenticationToken);
            log.info("인증 토큰의 권한: {}", authenticationToken.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("SecurityContext에 인증 정보 설정 완료");

            filterChain.doFilter(request, response);
            log.info("------------------JWTCheckFilter 종료------------------");
        } catch (Exception e) {
            log.error("JWT 체크 오류: {}", e.getMessage(), e);
            log.error("e.getMessage(): {}", e.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            String msg = objectMapper.writeValueAsString(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
        }
    }

}
