package postman.bottler.user.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import postman.bottler.user.exception.TokenException;

@Component
@Slf4j
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

    private String createToken(Authentication authentication, String type) {
        String email = authentication.getName();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority
                        ::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = null;

        if (type.equals("access")) {
            expiryDate = new Date(now.getTime() + accessTokenExpirationTime);
        } else if (type.equals("refresh")) {
            expiryDate = new Date(now.getTime() + accessTokenExpirationTime);
        }

        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, "access");
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, "refresh");
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();

        Object authoritiesObj = claims.get(AUTHORITIES_KEY);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (authoritiesObj instanceof List) {
            authorities = ((List<?>) authoritiesObj)
                    .stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.toString()))
                    .collect(Collectors.toList());
        } else if (authoritiesObj instanceof String) {
            authorities.add(new SimpleGrantedAuthority((String) authoritiesObj));
        }

        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new TokenException("만료된 jwt 토큰입니다.");
        } catch (Exception e) {
            throw new TokenException("유효하지 않은 jwt 토큰입니다.");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 키 설정
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("jwt signature가 불일치합니다. " + e);
        } catch (MalformedJwtException e) {
            log.error("유효하지 않은 jwt 토큰입니다. " + e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 jwt 토큰입니다. " + e);
        } catch (IllegalArgumentException e) {
            log.error("jwt claims 비어있습니다. " + e);
        }
        return false;
    }
}
