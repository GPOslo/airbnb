package com.GPOslo.airbnbclone.global.jwt;

import com.GPOslo.airbnbclone.domain.auth.entity.Authority;
import com.GPOslo.airbnbclone.global.exception.handler.BizException;
import com.GPOslo.airbnbclone.global.exception.type.AuthorityExceptionType;
import com.GPOslo.airbnbclone.global.jwt.dto.TokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;
    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-expire-time}") long accessTime,
                         @Value("${jwt.refresh-token-expire-time}") long refreshTime) {
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTime;

        // 서버만 알고있는 개인키인 secretKey를 전달받아 BASE64로 디코딩
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    protected String createToken(String email, Set<Authority> auth, long tokenValid) {
        Claims claims = Jwts.claims().setSubject(email);

        claims.put(AUTHORITIES_KEY,
                auth.stream()
                    .map(Authority::getAuthorityName)
                    .collect(Collectors.joining(","))
        );

        // 현재 시간
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        // 토큰 만료 시간
        Date expire = new Date(now.getTime() + tokenValid);

        return Jwts.builder()
                .setClaims(claims)      // 토큰 발행 유저 정보
                .setIssuedAt(now)       // 토큰 발행 시간
                .setExpiration(expire)  // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS512)        // 키, 알고리즘 설정
                .compact();
    }

    /**
     *
     * @param email
     * @param auth
     * @return 엑세스 토큰 생성하여 반환
     */
    public String createAccessToken(String email, Set<Authority> auth) {
        return this.createToken(email, auth, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /**
     *
     * @param email
     * @param auth
     * @return 리프레시 토큰 생성하여 반환
     */
    public String createRefreshToken(String email, Set<Authority> auth) {
        return this.createToken(email, auth, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /**
     *
     * @param token
     * @return 토큰 값을 파싱하여 클레임에 담긴 이메일 값을 반환한다
     */
    public String getMemberEmailByToken(String token) {
        return this.parseClaims(token).getSubject();
    }

    /**
     *
     * @param accessToken
     * @param refreshToken
     * @return TOKEN DTO를 생성한다.
     */
    public TokenDTO createTokenDTO(String accessToken, String refreshToken) {
        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType(BEARER_TYPE)
                .build();
    }

    public Authentication getAuthentication(String accessToken) throws BizException {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null || !StringUtils.hasText(claims.get(AUTHORITIES_KEY).toString())) {
            // 유저에게 아무런 권한이 없음
             throw new BizException(AuthorityExceptionType.NOT_FOUND_AUTHORITY);
        }

        log.debug("cliams.getAuth = {}", claims.get(AUTHORITIES_KEY));
        log.debug("cliams.getEmail = {}", claims.getSubject());

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        authorities.forEach(o -> {
            log.debug("getAuthentication -> authorities = {}", o.getAuthority());
        });

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new CustomEmailPasswordAuthToken(principal, "", authorities);
    }

    public int validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return 1;
        } catch(ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return 2;
        } catch(Exception e) {
            log.info("잘못된 토큰입니다.");
            return -1;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰이더라도 일단 파싱 한다
            return e.getClaims();
        }
    }
}
