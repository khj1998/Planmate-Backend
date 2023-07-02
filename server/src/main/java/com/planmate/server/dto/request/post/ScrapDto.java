package com.planmate.server.dto.request.post;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시물 스크랩 요청을 통해 전달되는 Dto 입니다.
 * @author kimhojin98@naver.com
 */
@Getter
@Setter
public class ScrapDto {
    private Long postId;
}
