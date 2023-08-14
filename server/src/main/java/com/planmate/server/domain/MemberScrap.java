package com.planmate.server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Member와 Post의 중간 테이블입니다. 게시물 스크랩 로직에서 사용되며, 맴버와 게시물 외래키를 가집니다.
 * @author kimhojin98@naver.com
 */
@Entity
@Table(name = "member_scrap")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberScrap {
    @Id
    @Column(name = "scrap_id",columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "스크랩 식별자")
    private Long id;

    @Column(name = "member_id",nullable = false,columnDefinition = "int")
    @ApiModelProperty(example = "맴버 참조 외래키")
    private Long memberId;

    @Column(name = "post_id",nullable = false,columnDefinition = "int")
    @ApiModelProperty(example = "게시물 참조 외래키")
    private Long postId;

    @CreationTimestamp
    @Column(name = "created_at",columnDefinition = "datetime")
    private LocalDateTime createdAt;

    public static MemberScrap of(Long memberId,Long postId) {
        return MemberScrap.builder()
                .memberId(memberId)
                .postId(postId)
                .build();
    }
}
