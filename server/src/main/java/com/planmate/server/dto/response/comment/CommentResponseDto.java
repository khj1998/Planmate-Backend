package com.planmate.server.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.planmate.server.domain.Comment;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {
    private String memberName;
    private LocalDate updatedAt;
    private String content;
    private Long likeCount;
    private Boolean isAuthor;

    public static CommentResponseDto of(Comment comment,String memberName,Long likeCount,Boolean isAuthor) {
        return CommentResponseDto.builder()
                .memberName(memberName)
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount(likeCount)
                .isAuthor(isAuthor)
                .build();
    }

    public static CommentResponseDto of(Comment comment,String memberName,Long likeCount) {
        return CommentResponseDto.builder()
                .memberName(memberName)
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount(likeCount)
                .build();
    }

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount(0L)
                .build();
    }
}
