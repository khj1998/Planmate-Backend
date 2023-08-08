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
    private Boolean isMyHearted;

    public static CommentResponseDto of(Comment comment, Member member, List<CommentLike> commentLikeList, Boolean isAuthor,Long memberId) {
        Boolean isMyHearted = false;

        for (CommentLike commentLike : commentLikeList) {
            if (commentLike.getUserId() == memberId) {
                isMyHearted = true;
                break;
            }
        }

        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPostId())
                .memberName(member.getMemberName())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount((long) commentLikeList.size())
                .isAuthor(isAuthor)
                .isMyHearted(isMyHearted)
                .build();
    }

    public static CommentResponseDto of(Comment comment,Member member,List<CommentLike> commentLikeList,Long memberId) {
        Boolean isMyHearted = false;

        for (CommentLike commentLike : commentLikeList) {
            if (commentLike.getUserId() == memberId) {
                isMyHearted = true;
                break;
            }
        }

        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPostId())
                .memberName(member.getMemberName())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount((long) commentLikeList.size())
                .isMyHearted(isMyHearted)
                .build();
    }

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .likeCount(0L)
                .build();
    }
}
