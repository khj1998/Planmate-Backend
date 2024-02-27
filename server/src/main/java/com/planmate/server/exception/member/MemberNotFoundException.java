package com.planmate.server.exception.member;

import com.planmate.server.exception.ErrorCode;
import lombok.Generated;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class MemberNotFoundException extends RuntimeException {
    private String message;

    public MemberNotFoundException(Long id) {
        super(id.toString());

        this.message = id.toString();
    }

    public MemberNotFoundException(String memberName) {
        super(memberName);
        this.message = memberName;
    }
}
