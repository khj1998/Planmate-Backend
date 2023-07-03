package com.planmate.server.dto.request.comment;

import lombok.Getter;

@Getter
public class CommentCreateRequestDto {
    private Long postId;
    private String content;
}
