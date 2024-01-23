package com.planmate.server.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 게시물 태그 테이블입니다. 게시물 외래키를 관리합니다.
 * @author kimhojin98@naver.com
 */
@Entity
@Table(name = "post_tag")
@NamedEntityGraph(name= "tag_paging",attributeNodes = {
        @NamedAttributeNode("post")
})
@ApiModel(value = "게시물 태그 테이블")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostTag {
    @Id
    @Column(name = "tag_id",columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "게시물 태그 식별자")
    private Long tagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "tag_name",nullable = false,length = 15,columnDefinition = "varchar")
    @ApiModelProperty(example = "태그 이름")
    private String tagName;

    @CreationTimestamp
    @Column(name = "created_at",columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false,columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    public static PostTag of(String tagName,Post post) {
        return PostTag.builder()
                .tagName(tagName)
                .post(post)
                .build();
    }
}
