package com.planmate.server.dto.request.comment;

import lombok.Getter;

@Getter
public class ChildCommentRequestDto {
    private Long postId;
    private Long parentCommentId;
    private String content;
}
