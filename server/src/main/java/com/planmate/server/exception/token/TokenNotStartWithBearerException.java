package com.planmate.server.exception.token;

import com.planmate.server.exception.ErrorCode;
import lombok.Getter;
import org.aspectj.bridge.IMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class TokenNotStartWithBearerException extends RuntimeException {
    private String message;

    public TokenNotStartWithBearerException(String message) {
        super(message);
    }
}
