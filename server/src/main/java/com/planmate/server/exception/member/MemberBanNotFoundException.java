package com.planmate.server.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemberBanNotFoundException extends RuntimeException {
    private String message;

    public MemberBanNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
