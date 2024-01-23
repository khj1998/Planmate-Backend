package com.planmate.server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "comment_like",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id","comment_id"})
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentLike {
    @Id
    @Column(name = "id", columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "고유 식별자")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    public static CommentLike of(Member member, Comment comment) {
        return CommentLike.builder()
                .member(member)
                .comment(comment)
                .build();
    }
}
