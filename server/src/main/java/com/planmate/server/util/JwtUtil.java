package com.planmate.server.util;

import com.planmate.server.domain.Member;
import com.planmate.server.exception.token.AuthorizationHeaderException;
import com.planmate.server.exception.token.TokenNotStartWithBearerException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

@Slf4j
@Component
@Generated
public class JwtUtil {
    private static String JWT_SECRET_KEY;
    public static final Long ACCESS_DURATION = 365 * 24 * 60 * 60 * 1000L;
    public static final Long REFRESH_DURATION = 365 * 24 * 60 * 60 * 1000L;

    @Value("${jwt.secret}")
    public void setKey(String key) {
        JWT_SECRET_KEY = key;
    }

    public static String generateAccessToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getMemberId().toString());

        // 유저 권한 리스트로 변경 예정
        claims.put("roles", Arrays.asList("ROLE_USER"));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_DURATION))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    public static String generateRefreshToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getMemberId().toString());

        //유저 권한 리스트로 변경 예정
        claims.put("roles", Arrays.asList("ROLE_USER"));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_DURATION))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    public static Long getUserIdByAccessToken() {
        String accessToken = getAccessTokenByRequest();

        String userId = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET_KEY)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();

        return Long.parseLong(userId);
    }

    public static List<String> getRolesByAccessToken(String accessToken) {
        return (List<String>) Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET_KEY)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .get("roles");
    }

    public static void validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(JWT_SECRET_KEY).build()
                    .parseClaimsJws(accessToken);
        } catch (ExpiredJwtException ex) {
            log.error("엑세스 토큰 만료");
        } catch (MalformedJwtException ex) {
            log.error("엑세스 토큰이 유효한 형태가 아님");
        } catch (UnsupportedJwtException ex) {
            log.error("엑세스 토큰이 서비스의 형태와 맞지 않음");
        } catch (SignatureException ex) {
            log.error("엑세스 토큰 서명이 유효하지 않음");
        }
    }

    public static String logout(Member member) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() - ACCESS_DURATION);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getMemberId());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    private static String getAccessTokenByRequest() {
        String accessToken = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
        } else {
            throw new AuthorizationHeaderException();
        }

        return accessToken;
    }
}