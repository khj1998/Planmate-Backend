package com.planmate.server.exception.member;

import com.planmate.server.exception.ErrorCode;
import lombok.Generated;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@Generated
public class MemberNotFoundException extends RuntimeException {
    private String message;

    public MemberNotFoundException(Long id) {
        super(id.toString());

        this.message = id.toString();
    }
}
