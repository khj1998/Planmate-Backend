package com.planmate.server.dto.request.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildCommentRequestDto {
    private Long postId;
    private Long parentCommentId;
    private String content;
}
