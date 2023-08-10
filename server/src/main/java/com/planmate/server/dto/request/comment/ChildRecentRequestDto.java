package com.planmate.server.dto.request.comment;

import lombok.Getter;

@Getter
public class ChildRecentRequestDto {
    private Long postId;
    private Long parentCommentId;
}
