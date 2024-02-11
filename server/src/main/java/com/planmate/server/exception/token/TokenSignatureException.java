package com.planmate.server.exception.token;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenSignatureException extends RuntimeException {
    public TokenSignatureException() {
        super();
    }
}
