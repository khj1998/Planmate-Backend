package com.planmate.server.dto.response.comment;

import com.planmate.server.domain.Comment;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private String memberName;
    private LocalDate updatedAt;
    private String content;
    private Long likeCount;

    public static CommentResponseDto of(Comment comment,String memberName,Long likeCount) {
        return CommentResponseDto.builder()
                .memberName(memberName)
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount(likeCount)
                .build();
    }
}
