package com.planmate.server.dto.request.comment;

import lombok.Getter;

@Getter
public class ChildRecentRequestDto {
    private Integer pages;
    private Long postId;
    private Long parentCommentId;
}
