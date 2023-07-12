package com.planmate.server.dto.response.comment;

import com.planmate.server.domain.Comment;
import com.planmate.server.domain.CommentLike;
import com.planmate.server.domain.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateResponseDto {
    private Long commentId;
    private String memberName;
    private LocalDateTime updatedAt;
    private String content;

    public static CommentCreateResponseDto of(Comment comment,String memberName) {
        return CommentCreateResponseDto.builder()
                .commentId(comment.getCommentId())
                .memberName(memberName)
                .updatedAt(comment.getUpdatedAt())
                .content(comment.getContent())
                .build();
    }
}
