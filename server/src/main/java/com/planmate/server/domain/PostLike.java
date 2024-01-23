package com.planmate.server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity
@Table(name = "post_like",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id","post_id"})
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostLike {
    @Id
    @Column(name = "id",columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "고유 식별자")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public static PostLike of(Member member,Post post) {
        return PostLike.builder()
                .member(member)
                .post(post)
                .build();
    }
}
