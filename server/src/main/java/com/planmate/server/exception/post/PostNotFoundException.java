package com.planmate.server.exception.post;

import com.planmate.server.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 게시물을 찾지 못했을 때 발생하는 예외입니다.
 * @author kimhojin98@naver.com
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class PostNotFoundException extends RuntimeException {
    private String message;
    private ErrorCode code;

    public PostNotFoundException(Long id) {
        super(id.toString());
        this.message = id.toString();
    }
}
