package com.planmate.server.service.comment;

import com.planmate.server.domain.Comment;
import com.planmate.server.dto.request.comment.ChildCommentRequestDto;
import com.planmate.server.dto.request.comment.CommentCreateRequestDto;
import com.planmate.server.dto.request.comment.CommentEditRequestDto;
import com.planmate.server.dto.request.comment.CommentRequestDto;
import com.planmate.server.dto.response.comment.CommentPageResponseDto;
import com.planmate.server.dto.response.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentPageResponseDto findRecentComment(CommentRequestDto commentRequestDto);
    List<CommentResponseDto> findMyComment();
    CommentResponseDto createComment(CommentCreateRequestDto commentCreateRequestDto);
    CommentResponseDto createChildComment(ChildCommentRequestDto childCommentRequestDto);
    CommentResponseDto editComment(CommentEditRequestDto commentEditRequestDto);
    Boolean setCommentLike(Long commentId);
    void deleteComment(Long commentId);
}
