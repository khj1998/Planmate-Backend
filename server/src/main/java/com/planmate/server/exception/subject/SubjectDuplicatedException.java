package com.planmate.server.exception.subject;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SubjectDuplicatedException extends RuntimeException {
    private String message;

    public SubjectDuplicatedException(String name) {
        super(name);
        this.message = name;
    }
}
