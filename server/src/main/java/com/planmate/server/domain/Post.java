package com.planmate.server.domain;

import com.planmate.server.dto.request.post.PostDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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
@NoArgsConstructor
public class Post {
    @Id
    @Column(name = "post_id",columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "게시물 고유 식별자")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "owner_id")
    @ApiModelProperty(example = "게시물 소유 맴버와 매핑")
    private Member owner;

    @Column(name = "title",columnDefinition = "varchar(100)")
    @ApiModelProperty(example = "게시물 제목")
    private String title;

    @Column(name = "content",columnDefinition = "longtext")
    @ApiModelProperty(example = "본문 내용")
    private String content;

    @UpdateTimestamp
    @Column(name = "updated_at",columnDefinition = "datetime")
    @ApiModelProperty(example = "게시물 업데이트 날짜")
    private Date updatedAt;

    @OneToMany(mappedBy = "post",orphanRemoval = true)
    final private List<PostTag> postTagList = new ArrayList<>();

    @Builder
    public Post(String title,String content) {
        this.title = title;
        this.content = content;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
