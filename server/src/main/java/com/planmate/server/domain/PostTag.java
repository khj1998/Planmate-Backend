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

    @Column(name = "tag_name",nullable = false,length = 15,columnDefinition = "varchar")
    @ApiModelProperty(example = "태그 이름")
    private String tagName;

    @Column(name = "post_id",nullable = false,columnDefinition = "bigint")
    @ApiModelProperty(example = "게시물 참조 외래키")
    private Long postId;

    @CreationTimestamp
    @Column(name = "created_at",columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false,columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    public static PostTag of(String tagName,Long postId) {
        return PostTag.builder()
                .tagName(tagName)
                .postId(postId)
                .build();
    }
}
