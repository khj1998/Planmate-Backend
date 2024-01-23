package com.planmate.server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member_scrap",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id","post_id"})
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberScrap {
    @Id
    @Column(name = "scrap_id",columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "스크랩 식별자")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @CreationTimestamp
    @Column(name = "created_at",columnDefinition = "datetime")
    private LocalDateTime createdAt;

    public static MemberScrap of(Member member,Post post) {
        return MemberScrap.builder()
                .member(member)
                .post(post)
                .build();
    }
}
