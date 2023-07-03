package com.planmate.server.service.comment;

import com.planmate.server.domain.Comment;
import com.planmate.server.dto.request.comment.CommentCreateRequestDto;
import com.planmate.server.dto.request.comment.CommentEditRequestDto;
import com.planmate.server.dto.response.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {
    List<CommentResponseDto> findMyComment();
    Comment createComment(CommentCreateRequestDto commentCreateRequestDto);
    Comment editComment(CommentEditRequestDto commentEditRequestDto);
    Boolean setCommentLike(Long commentId);
    void deleteComment(Long commentId);
}
