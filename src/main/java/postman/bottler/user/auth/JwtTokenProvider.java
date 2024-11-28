package postman.bottler.user.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import postman.bottler.user.auth.service.CustomUserDetailsService;

@Component
public class JwtTokenProvider implements InitializingBean {

    private final String secretKey; // JWT 비밀키
    private Key key; // JWT 서명에 사용할 키
    private final long accessTokenExpirationTime; // 액세스 토큰 유효 시간
    private final long refreshTokenExpirationTime; // 리프레시 토큰 유효 시간

    private static final String AUTHORITIES_KEY = "auth"; // 권한 키

    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.token.expiration}") long accessTokenExpirationTime,
            @Value("${jwt.refresh.token.expiration}") long refreshTokenExpirationTime,
            CustomUserDetailsService customUserDetailsService) {
        this.secretKey = secretKey;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {
        // 사용자 이름 (주로 username 또는 userId)
        String username = authentication.getName();

        // 권한 정보를 문자열로 변환
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority
                        ::getAuthority)
                .collect(Collectors.joining(","));

        // 현재 시간과 만료 시간 설정
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationTime);

        // JWT 생성 및 서명
        return Jwts.builder()
                .setSubject(username) // 사용자 정보 설정
                .claim(AUTHORITIES_KEY, authorities) // 권한 정보 추가
                .setIssuedAt(now) // 발급 시간
                .setExpiration(expiryDate) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS512) // 서명 알고리즘 및 키 설정
                .compact(); // 최종 JWT 문자열 생성
    }


    public Authentication getAuthentication(String token) {
        // JWT 토큰에서 클레임(Claims) 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // 서명 키 설정
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 사용자 이름 또는 ID 추출
        String username = claims.getSubject();

        // 권한 정보 추출 및 변환
        List<SimpleGrantedAuthority> authorities =
                ((List<?>) claims.get(AUTHORITIES_KEY)) // 권한 키에서 리스트 추출
                        .stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.toString())) // SimpleGrantedAuthority로 변환
                        .collect(Collectors.toList());

        // UserDetailsService에서 사용자 정보를 로드하거나, 간단히 UserDetails 생성
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Authentication 객체 반환 (UsernamePasswordAuthenticationToken 사용)
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            // JWT 토큰 파싱 및 유효성 검증
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 키 설정
                    .build()
                    .parseClaimsJws(token);

            // 추가 검증 로직 (선택적)
            // claimsJws.getBody()를 사용하여 페이로드 내용을 확인할 수 있음

            return true; // 유효한 토큰
        } catch (SignatureException e) {
            // 서명 불일치 예외 처리
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            // 구조가 잘못된 JWT 처리
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            // 만료된 JWT 처리
            System.out.println("Expired JWT token");
        } catch (IllegalArgumentException e) {
            // 클레임이 비어있는 경우 처리
            System.out.println("JWT claims string is empty");
        }

        return false; // 유효하지 않은 토큰
    }
}
