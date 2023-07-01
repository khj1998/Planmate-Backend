package com.planmate.server.exception;

import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.post.PostNotFoundException;
import com.planmate.server.exception.post.ScrapNotFoundException;
import com.planmate.server.exception.token.TokenNotFoundException;
import com.planmate.server.exception.token.TokenNotStartWithBearerException;
import org.apache.coyote.Response;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Generated
@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(MemberNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0001","Member is not found : " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(TokenNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0002","Member has invalid token : member " + ex.getMessage() + "번");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenNotStartWithBearerException.class)
    public ResponseEntity<ApiErrorResponse> handleException(TokenNotStartWithBearerException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0003","Token in header not start with Bearer");
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(PostNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0004","Post is not found : "+ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ScrapNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(ScrapNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0005","Scrap is not found : "+ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
}
