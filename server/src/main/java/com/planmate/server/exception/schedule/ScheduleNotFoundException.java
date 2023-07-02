package com.planmate.server.exception.schedule;

import com.planmate.server.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class ScheduleNotFoundException extends RuntimeException {
    private String message;
    private ErrorCode errorCode;

    public ScheduleNotFoundException(Long id) {
        super(id.toString());
        message = id.toString();
    }
}
