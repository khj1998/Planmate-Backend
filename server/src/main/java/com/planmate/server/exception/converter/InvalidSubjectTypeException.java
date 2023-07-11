package com.planmate.server.exception.converter;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSubjectTypeException extends RuntimeException {
    private String message;

    public InvalidSubjectTypeException(Long value) {
        super(value.toString());
        this.message = value.toString();
    }

    public InvalidSubjectTypeException(String name) {
        super(name);
        this.message = name;
    }
}
