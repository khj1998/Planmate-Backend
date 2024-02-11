package com.planmate.server.exception.token;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class TokenMalformedException extends RuntimeException {
    public TokenMalformedException() {
        super();
    }
}
