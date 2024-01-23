package com.planmate.server.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.planmate.server.domain.Comment;
import com.planmate.server.domain.CommentLike;
import com.planmate.server.domain.Member;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {
    private Long commentId;
    private Long postId;
    private String memberName;
    private LocalDateTime updatedAt;
    private String content;
    private Long likeCount;
    private Boolean isAuthor;
    private Boolean isPostAuthor;
    private Boolean isMyHearted;

    public static CommentResponseDto of(Comment comment, List<CommentLike> commentLikeList,Long memberId) {
        Boolean isMyHearted = false;
        Member member = comment.getMember();

        for (CommentLike commentLike : commentLikeList) {
            if (commentLike.getMember().getMemberId().equals(memberId)) {
                isMyHearted = true;
                break;
            }
        }

        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .memberName(member.getMemberName())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount((long) commentLikeList.size())
                .isMyHearted(isMyHearted)
                .isAuthor(member.getMemberId().equals(memberId))
                .isPostAuthor(memberId.equals(comment.getPost().getMember().getMemberId()))
                .build();
    }

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount(0L)
                .build();
    }
}
