package com.planmate.server.dto.request.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequestDto {
    private Long postId;
    private String content;
}
