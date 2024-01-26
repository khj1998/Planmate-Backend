package com.planmate.server.exception;

import com.planmate.server.exception.comment.CommentNotFoundException;
import com.planmate.server.exception.converter.InvalidSubjectTypeException;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.notice.NoticeNotFoundException;
import com.planmate.server.exception.planner.PlannerNotFoundException;
import com.planmate.server.exception.post.PostNotFoundException;
import com.planmate.server.exception.post.ScrapNotFoundException;
import com.planmate.server.exception.subject.SubjectDuplicatedException;
import com.planmate.server.exception.subject.SubjectNotFoundException;
import com.planmate.server.exception.dday.MemberDdayNotFoundException;
import com.planmate.server.exception.dday.DdayNotFoundException;
import com.planmate.server.exception.token.TokenNotFoundException;
import com.planmate.server.exception.token.TokenNotStartWithBearerException;
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

    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(SubjectNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0006","Subject is not found : "+ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(CommentNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0007", "Comment is not found : " + ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DdayNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(DdayNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0008","Schedule is not found : "+ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberDdayNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(MemberDdayNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0009","Member's schedule is not exist. member id: " + ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidSubjectTypeException.class)
    public ResponseEntity<ApiErrorResponse> handleException(InvalidSubjectTypeException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0010","Subject type converting failed value/name : "+ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SubjectDuplicatedException.class)
    public ResponseEntity<ApiErrorResponse> handleException(SubjectDuplicatedException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0011",ex.getMessage()+" already exists subject");
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlannerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(PlannerNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0012","planner not found id : "+ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(NoticeNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse("ERROR-0013","notice not found id : "+ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
}
