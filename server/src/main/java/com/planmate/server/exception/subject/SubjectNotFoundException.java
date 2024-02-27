package com.planmate.server.exception.subject;

import com.planmate.server.exception.ErrorCode;
import lombok.Generated;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubjectNotFoundException extends RuntimeException {
    private String message;

    public SubjectNotFoundException(Long id) {
        super(id.toString());

        this.message = id.toString();
    }
}
