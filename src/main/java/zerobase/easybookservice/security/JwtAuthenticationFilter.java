package zerobase.easybookservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 필터 <- 권한이 필요한 api 에 접근할 때 동작
    // 요청이 서버의 컨트롤러에 도달하기 전에 jwt 토큰을 검사하고 검증
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer "; // 이 뒤에 토큰이 붙어서 요청이 들어옴

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveTokenFromRequest(request);
        log.info("Token from request: {}", token);

        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            //<- 토큰 유효성 검증
            log.info("Token is valid");

            Authentication auth = tokenProvider.getAuthentication(token);
            log.info("Authentication: {}", auth);

            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            log.warn("Invalid token");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        log.info("Token from request header: {}", token);
        // http 요청 헤더에서 TOKEN_HEADER 에 해당하는 값을 가져온다

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_BEARER_PREFIX)) {
            return token.substring(TOKEN_BEARER_PREFIX.length());
        }
        // 토큰이 비어있지 않은지 확인
        // + 토큰이 TOKEN_BEARER_PREFIX로 시작하는지 확인
        // 조건 만족시 -> token.substring(TOKEN_BEARER_PREFIX.length())를 통해 Bearer 접두사를 제거한 실제 토큰 문자열을 반환합니다.
        return null;
    }
}
