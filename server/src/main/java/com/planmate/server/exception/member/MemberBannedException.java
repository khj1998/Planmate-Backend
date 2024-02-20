package com.planmate.server.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class MemberBannedException extends RuntimeException {
    private String message;

    public MemberBannedException(String message) {
        super(message);
        this.message = message;
    }
}
