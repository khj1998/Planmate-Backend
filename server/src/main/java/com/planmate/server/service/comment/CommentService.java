package com.planmate.server.service.comment;

import com.planmate.server.domain.Comment;
import com.planmate.server.dto.request.comment.*;
import com.planmate.server.dto.response.comment.CommentLikeRequestDto;
import com.planmate.server.dto.response.comment.CommentPageResponseDto;
import com.planmate.server.dto.response.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentPageResponseDto findRecentComment(CommentRequestDto commentRequestDto);
    List<CommentResponseDto> findRecentChildComment(ChildRecentRequestDto commentRequestDto);
    CommentPageResponseDto findMyComment(Integer pages);
    CommentResponseDto createComment(CommentCreateRequestDto commentCreateRequestDto);
    CommentResponseDto createChildComment(ChildCommentRequestDto childCommentRequestDto);
    CommentResponseDto editComment(CommentEditRequestDto commentEditRequestDto);
    void setCommentLike(CommentLikeRequestDto dto);
    void deleteComment(Long commentId);
}
