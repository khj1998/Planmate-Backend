package com.planmate.server.dto.request.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 게시물 관련 요청을 통해 전달되는 Dto 입니다.
 * @author kimhojin98@naver.com
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private List<String> tagList;
}
