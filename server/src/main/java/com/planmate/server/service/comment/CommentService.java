package com.planmate.server.service.comment;

import com.planmate.server.domain.Comment;
import com.planmate.server.dto.request.comment.*;
import com.planmate.server.dto.response.comment.CommentPageResponseDto;

public interface CommentService {
    CommentPageResponseDto findRecentComment(CommentRequestDto commentRequestDto);
    CommentPageResponseDto findRecentComment(ChildRecentRequestDto commentRequestDto);
    CommentPageResponseDto findMyComment(Integer pages);
    void createComment(CommentCreateRequestDto commentCreateRequestDto);
    void createChildComment(ChildCommentRequestDto childCommentRequestDto);
    void editComment(CommentEditRequestDto commentEditRequestDto);
    void setCommentLike(Long commentId);
    void deleteComment(Long commentId);
}
