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

    @Column(name = "member_id",nullable = false,columnDefinition = "int")
    private Long memberId;

    @Column(name = "post_id",nullable = false,columnDefinition = "bigint")
    private Long postId;

    public static PostLike of(Long memberId,Long postId) {
        return PostLike.builder()
                .memberId(memberId)
                .postId(postId)
                .build();
    }
}
