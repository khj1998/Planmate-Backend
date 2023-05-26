package com.planmate.server.exception.token;

import com.planmate.server.exception.ErrorCode;
import lombok.Generated;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@Generated
public class TokenNotFoundException extends RuntimeException {
    private String message;
    private ErrorCode code;

    public TokenNotFoundException(Long id) {
        super(id.toString());

        this.message = id.toString();
    }
}
