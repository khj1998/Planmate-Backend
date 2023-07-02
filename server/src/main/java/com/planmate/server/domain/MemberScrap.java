package com.planmate.server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id",columnDefinition = "int")
    @ApiModelProperty(example = "맴버 참조 외래키")
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_id",columnDefinition = "int")
    @ApiModelProperty(example = "게시물 참조 외래키")
    private Post post;

    public static MemberScrap of(Member owner,Post post) {
        return MemberScrap.builder()
                .owner(owner)
                .post(post)
                .build();
    }
}
