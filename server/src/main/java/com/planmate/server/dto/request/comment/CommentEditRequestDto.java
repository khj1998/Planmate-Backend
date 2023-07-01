package com.planmate.server.dto.request.comment;

import lombok.Getter;

@Getter
public class CommentEditRequestDto {
    Long commentId;
    String content;
}
