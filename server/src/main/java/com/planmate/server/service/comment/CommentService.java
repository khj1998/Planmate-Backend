package com.planmate.server.service.comment;

import com.planmate.server.domain.Comment;
import com.planmate.server.dto.request.comment.ChildCommentRequestDto;
import com.planmate.server.dto.request.comment.CommentCreateRequestDto;
import com.planmate.server.dto.request.comment.CommentEditRequestDto;
import com.planmate.server.dto.request.comment.CommentRequestDto;
import com.planmate.server.dto.response.comment.CommentCreateResponseDto;
import com.planmate.server.dto.response.comment.CommentEditResponseDto;
import com.planmate.server.dto.response.comment.CommentPageResponseDto;
import com.planmate.server.dto.response.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentPageResponseDto findRecentComment(CommentRequestDto commentRequestDto);
    CommentPageResponseDto findMyComment(Integer pages);
    CommentCreateResponseDto createComment(CommentCreateRequestDto commentCreateRequestDto);
    CommentCreateResponseDto createChildComment(ChildCommentRequestDto childCommentRequestDto);
    CommentEditResponseDto editComment(CommentEditRequestDto commentEditRequestDto);
    Boolean setCommentLike(Long commentId);
    void deleteComment(Long commentId);
}
