package com.planmate.server.domain;

import com.planmate.server.dto.request.comment.CommentCreateRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(name = "updatedAt",nullable = false,columnDefinition = "datetime")
    private LocalDate updatedAt;

    public static Comment of(CommentCreateRequestDto commentCreateRequestDto,Long memberId){
        LocalDate now = LocalDate.now();

        return Comment.builder()
                .memberId(memberId)
                .postId(commentCreateRequestDto.getPostId())
                .content(commentCreateRequestDto.getContent())
                .updatedAt(now)
                .build();
    }
}
