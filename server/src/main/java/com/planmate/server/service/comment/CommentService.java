package com.planmate.server.service.comment;

import com.planmate.server.domain.Comment;
import com.planmate.server.dto.request.comment.ChildCommentRequestDto;
import com.planmate.server.dto.request.comment.CommentCreateRequestDto;
import com.planmate.server.dto.request.comment.CommentEditRequestDto;
import com.planmate.server.dto.request.comment.CommentRequestDto;
import com.planmate.server.dto.response.comment.CommentPageResponseDto;

public interface CommentService {
    CommentPageResponseDto findRecentComment(CommentRequestDto commentRequestDto);
    CommentPageResponseDto findMyComment(Integer pages);
    void createComment(CommentCreateRequestDto commentCreateRequestDto);
    void createChildComment(ChildCommentRequestDto childCommentRequestDto);
    void editComment(CommentEditRequestDto commentEditRequestDto);
    void setCommentLike(Long commentId);
    void deleteComment(Long commentId);
}
