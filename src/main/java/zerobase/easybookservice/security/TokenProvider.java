package zerobase.easybookservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zerobase.easybookservice.service.UserService;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final String KEY_ROLES = "roles";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 5;
    private final UserService userService;

    @Value("${jwt.secret}")
    private String secretKey;

    // jwt 의 세 부분 : header, payload, signature
    // header : 토큰의 타입 (jwt) 과 사용된 서명 알고리즘 (HMAC SHA256) 지정
    // payload : 토큰의 실제 데이터 포함 <- 여기에 claims 포함
    // signature : 토큰의 무결성 검증 위해 사용. 헤더와 페이로드를 합쳐서 비밀 키로 서명
    // 토큰 생성 메서드
    public String generateToken(String email, List<String> authorities) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(KEY_ROLES, authorities);

        var now = new Date();
        var expiration = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(getEmail(token));
        log.info("Authentication user: {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            log.warn("Token is empty or null");
            return false;
        }

        try {
            var claims = parseClaims(token);
            boolean isValid = claims.getExpiration().after(new Date());
            log.info("Token is valid: {}", isValid);
            return isValid;
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return false;
        }
        // 토큰의 만료 시간이 현재의 이전인지 아닌지로 만료 여부 체크
    }

    // 토큰 유효성 검사
    // 가져온 토큰으로부터 claims 추출
    public Claims parseClaims(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build() // 파서 빌드
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // TODO
            log.error("Expired JWT token {}", e.getMessage());
            return e.getClaims(); // 만료된 토큰이어도 클레임 객체 반환할 수 있도록 함
        } catch (Exception e) {
            log.error("Invalid token: {}", e.getMessage());
            throw new RuntimeException("Invalid token");
        }
    }
}
