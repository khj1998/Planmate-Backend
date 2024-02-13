package com.planmate.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planmate.server.domain.Member;
import com.planmate.server.exception.ApiErrorResponse;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.service.member.MemberService;
import com.planmate.server.service.member.MemberServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
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

public class JwtCustomFilter extends OncePerRequestFilter {
    private final MemberService memberService;

    public JwtCustomFilter(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);

        List<String> roleList = JwtUtil.getRolesByAccessToken(accessToken);
        String userRole = getUserRole(roleList);

        try {
            Member member = memberService.findMemberById(JwtUtil.getUserIdByAccessToken());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getMemberId(),null,
                            Collections.singleton(new SimpleGrantedAuthority(userRole)));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (MemberNotFoundException ex) {
            setExceptionResponse(response,HttpStatus.NOT_FOUND.value(), JwtUtil.getUserIdByAccessToken()+"member not found");
        }
    }

    private void setExceptionResponse(HttpServletResponse response,Integer statusCode,String message) {
        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(statusCode.toString(),message);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header.split(" ")[1];
    }

    private String getUserRole(List<String> roleList) {
        if (roleList.contains("ROLE_ADMIN")) {
            return "ROLE_ADMIN";
        }

        return "ROLE_USER";
    }
}
