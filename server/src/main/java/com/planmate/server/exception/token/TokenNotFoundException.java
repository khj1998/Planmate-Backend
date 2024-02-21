package com.planmate.server.exception.token;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TokenNotFoundException extends RuntimeException {
    private String message;

    public TokenNotFoundException(Long id) {
        super(id.toString());
        this.message = id.toString();
    }

    public TokenNotFoundException(String token) {
        super(token);
        this.message = token;
    }
}
