package com.planmate.server.domain;

import com.planmate.server.dto.request.post.PostDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 게시물 테이블입니다. 맴버 참조를 위한 외래키를 가집니다.
 * @author kimhojin98@naver.com
 */
@Entity
@Table(name = "post")
@ApiModel(value = "게시물 테이블")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @Column(name = "post_id",columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "게시물 고유 식별자")
    private Long postId;

    @Column(name = "member_id",nullable = false,columnDefinition = "int")
    @ApiModelProperty(example = "게시물 소유 맴버와 매핑")
    private Long memberId;

    @Column(name = "title",columnDefinition = "varchar(100)")
    @ApiModelProperty(example = "게시물 제목")
    private String title;

    @Column(name = "content",columnDefinition = "longtext")
    @ApiModelProperty(example = "본문 내용")
    private String content;

    @CreationTimestamp
    @Column(name = "started_at",columnDefinition = "datetime")
    private LocalDateTime startedAt;

    @UpdateTimestamp
    @Column(name = "updated_at",columnDefinition = "datetime")
    @ApiModelProperty(example = "게시물 업데이트 날짜")
    private LocalDateTime updatedAt;

    public static Post of(PostDto postDto,Long memberId)  {
        return Post.builder()
                .memberId(memberId)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .build();
    }
}
