package com.planmate.server.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.planmate.server.domain.Comment;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {
    private Long commentId;
    private String memberName;
    private LocalDateTime updatedAt;
    private String content;
    private Long likeCount;
    private Boolean isAuthor;

    public static CommentResponseDto of(Comment comment,String memberName,Long likeCount,Boolean isAuthor) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .memberName(memberName)
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount(likeCount)
                .isAuthor(isAuthor)
                .build();
    }

    public static CommentResponseDto of(Comment comment,String memberName,Long likeCount) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .memberName(memberName)
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount(likeCount)
                .build();
    }

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount(0L)
                .build();
    }
}
