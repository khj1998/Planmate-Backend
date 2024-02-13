package com.planmate.server.exception.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planmate.server.exception.ApiErrorResponse;
import com.planmate.server.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilterChainExceptionHandler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);

        if (!isBearerHeader(request)) {
            setExceptionResponse(response,HttpStatus.BAD_REQUEST.value(), "Authorization Header is not started with Bearer");
            return;
        }

        try {
            JwtUtil.validateToken(accessToken);
            filterChain.doFilter(request,response);
        } catch (ExpiredJwtException ex) {
            setExceptionResponse(response,HttpStatus.UNAUTHORIZED.value(),"token expired");
        } catch (MalformedJwtException ex) {
            setExceptionResponse(response,HttpStatus.UNAUTHORIZED.value(),"malformed token");
        } catch (UnsupportedJwtException ex) {
            setExceptionResponse(response,HttpStatus.UNAUTHORIZED.value(),"unsupported token");
        } catch (SignatureException ex) {
            setExceptionResponse(response,HttpStatus.UNAUTHORIZED.value(),"signature is not valid");
        }
    }

    private Boolean isBearerHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer ");
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header.split(" ")[1];
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
}
