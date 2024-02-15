package com.planmate.server.exception.dday;

import com.planmate.server.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class DdayNotFoundException extends RuntimeException {
    private String message;

    public DdayNotFoundException(Long id) {
        super(id.toString());
        message = id.toString();
    }
}
