package com.planmate.server.exception.notice;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoticeNotFoundException extends RuntimeException {
    private String message;

    public NoticeNotFoundException(Long id) {
        super(id.toString());
        this.message = id.toString();
    }
}
