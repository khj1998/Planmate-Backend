package com.planmate.server.exception.token;

import com.planmate.server.exception.ErrorCode;
import lombok.Generated;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class AuthorizationHeaderException extends RuntimeException {
    private String message;

    public AuthorizationHeaderException() {
        super();
        this.message = "유효한 접근이 아닙니다.";
    }
}
