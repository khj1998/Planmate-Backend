package com.planmate.server.exception.post;

import com.planmate.server.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 스크랩 한 글을 찾지 못했을 때 발생하는 예외입니다.
 * @author kimhojin98@naver.com
 */
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScrapNotFoundException extends RuntimeException {
    private String message;

    public ScrapNotFoundException(Long id) {
        super(id.toString());
        this.message = id.toString();
    }
}
