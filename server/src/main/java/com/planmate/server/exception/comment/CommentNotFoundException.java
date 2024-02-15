package com.planmate.server.exception.comment;

import com.planmate.server.exception.ErrorCode;
import lombok.Generated;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
@Generated
public class CommentNotFoundException extends RuntimeException {
    private String message;

    public CommentNotFoundException(Long id) {
        super(id.toString());
        this.message = id.toString();
    }
}
