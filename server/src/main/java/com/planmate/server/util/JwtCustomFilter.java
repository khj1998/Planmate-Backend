package com.planmate.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planmate.server.domain.Member;
import com.planmate.server.exception.ApiErrorResponse;
import com.planmate.server.exception.member.MemberBannedException;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.token.TokenExpiredException;
import com.planmate.server.service.member.MemberService;
import com.planmate.server.service.token.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtCustomFilter extends OncePerRequestFilter {
    private final MemberService memberService;
    private final TokenService tokenService;

    public JwtCustomFilter(MemberService memberService,TokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);

        try {
            tokenService.findExpiredToken(accessToken);
            Long memberId = JwtUtil.getUserIdByAccessToken();
            Member member = memberService.checkMember(memberId);
            String userRole = member.getRole();

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(memberId,null,
                            Collections.singleton(new SimpleGrantedAuthority(userRole)));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (MemberNotFoundException ex) {
            setExceptionResponse(response,HttpStatus.NOT_FOUND.value(), JwtUtil.getUserIdByAccessToken()+" member not found");
        } catch (TokenExpiredException ex) {
            setExceptionResponse(response,HttpStatus.UNAUTHORIZED.value(), accessToken + " already has been expired");
        } catch (MemberBannedException ex) {
            setExceptionResponse(response,HttpStatus.UNAUTHORIZED.value(), "This email has been banned!");
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
}
