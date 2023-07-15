package com.planmate.server.domain;

import com.planmate.server.dto.request.comment.ChildCommentRequestDto;
import com.planmate.server.dto.request.comment.CommentCreateRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Table(name = "comment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @Column(name = "id", columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "고유 식별자")
    private Long commentId;

    @Column(name = "member_id",nullable = false,columnDefinition = "int")
    private Long memberId;

    @Column(name = "post_id",nullable = false,columnDefinition = "int")
    private Long postId;

    @Column(name = "content",nullable = false,columnDefinition = "longtext")
    private String content;

    @Column(name = "parent_comment",columnDefinition = "int")
    private Long parentCommentId;

    @CreationTimestamp
    @Column(name = "started_at",columnDefinition = "datetime")
    private LocalDateTime startedAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false,columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    public static Comment of(CommentCreateRequestDto commentCreateRequestDto,Long memberId){
        return Comment.builder()
                .memberId(memberId)
                .postId(commentCreateRequestDto.getPostId())
                .content(commentCreateRequestDto.getContent())
                .build();
    }

    public static Comment of(ChildCommentRequestDto childCommentRequestDto,Long memberId) {
        return Comment.builder()
                .memberId(memberId)
                .postId(childCommentRequestDto.getPostId())
                .parentCommentId(childCommentRequestDto.getParentCommentId())
                .content(childCommentRequestDto.getContent())
                .build();
    }
}
