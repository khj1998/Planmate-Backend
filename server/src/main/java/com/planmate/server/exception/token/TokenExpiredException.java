package com.planmate.server.exception.token;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends RuntimeException {
    private String message;

    public TokenExpiredException(String message) {
        super(message);
        this.message = message;
    }
}
