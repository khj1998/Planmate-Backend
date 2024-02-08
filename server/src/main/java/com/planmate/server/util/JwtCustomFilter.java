package com.planmate.server.util;

import com.planmate.server.domain.Member;
import com.planmate.server.service.member.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Generated
public class JwtCustomFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        if (!isBearerHeader(request)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("Authorization 헤더가 Bearer로 시작하지 않습니다.");
            return;
        }

        String accessToken = getAccessToken(request);

        try {
            JwtUtil.validateToken(accessToken);
        } catch (ExpiredJwtException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("엑세스 토큰이 만료됨");
            return;
        } catch (MalformedJwtException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("엑세스 토큰이 유효한 형태가 아님");
            return;
        } catch (UnsupportedJwtException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("엑세스 토큰이 서비스의 형태와 맞지 않음");
            return;
        } catch (SignatureException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("엑세스 토큰 서명이 유효하지 않음");
            return;
        }

        List<String> roleList = JwtUtil.getRolesByAccessToken(accessToken);
        String userRole = getUserRole(roleList);

        if (userRole.equals("INVALID_ROLE")) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("유효하지 않은 권한");
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(JwtUtil.getUserIdByAccessToken(),null,
                        Collections.singleton(new SimpleGrantedAuthority(userRole)));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    private Boolean isBearerHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer ");
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header.split(" ")[1];
    }

    private String getUserRole(List<String> roleList) {
        if (roleList.contains("ROLE_ADMIN")) {
            return "ROLE_ADMIN";
        }

        if (roleList.contains("ROLE_USER")) {
            return "ROLE_USER";
        }

        return "INVALID_ROLE";
    }
}
