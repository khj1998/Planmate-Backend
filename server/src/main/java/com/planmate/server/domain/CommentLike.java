package com.planmate.server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity
@Table(name = "comment_like",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id","comment_id"})
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentLike {
    @Id
    @Column(name = "id", columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "고유 식별자")
    private Long id;

    @Column(name = "member_id",nullable = false,columnDefinition = "int")
    private Long memberId;

    @Column(name = "comment_id",nullable = false,columnDefinition = "int")
    private Long commentId;

    public static CommentLike of(Long userId, Long commentId) {
        return CommentLike.builder()
                .memberId(userId)
                .commentId(commentId)
                .build();
    }
}
