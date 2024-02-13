package com.planmate.server.exception.subject;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubjectTimeSliceNotFoundException extends RuntimeException {
    private String message;

    public SubjectTimeSliceNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
