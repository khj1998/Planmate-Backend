package com.planmate.server.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

/**
 * 게시물 태그 테이블입니다. 게시물 외래키를 관리합니다.
 * @author kimhojin98@naver.com
 */
@Entity
@Table(name = "post_tag")
@ApiModel(value = "게시물 태그 테이블")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostTag {
    @Id
    @Column(name = "tag_id",columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "게시물 태그 식별자")
    private Long tagId;

    @Column(name = "tag_name",columnDefinition = "varchar(15)")
    @ApiModelProperty(example = "태그 이름")
    private String tagName;

    @JoinColumn(name = "post_id",columnDefinition = "int")
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @ApiModelProperty(example = "게시물 참조 외래키")
    private Post post;

    public static PostTag of(String tagName,Post post) {
        return PostTag.builder()
                .tagName(tagName)
                .post(post)
                .build();
    }
}
