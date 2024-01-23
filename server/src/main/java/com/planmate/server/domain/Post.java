package com.planmate.server.domain;

import com.planmate.server.dto.request.notice.NoticeRequestDto;
import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.dto.response.notice.NoticeResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.sql.Array;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 게시물 테이블입니다. 맴버 참조를 위한 외래키를 가집니다.
 * @author kimhojin98@naver.com
 */
@Entity
@NamedEntityGraph(name = "post_paging",attributeNodes = {
        @NamedAttributeNode("member"),
})
@Table(name = "post")
@ApiModel(value = "게시물 테이블")
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @Column(name = "post_id",columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "게시물 고유 식별자")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostTag> postTagList = new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post" ,cascade = CascadeType.ALL)
    private List<PostLike> postLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<MemberScrap> memberScrapList = new ArrayList<>();

    @Column(name = "type",nullable = false,columnDefinition = "int")
    private Long type;

    @Column(name = "title",columnDefinition = "varchar(100)")
    @ApiModelProperty(example = "게시물 제목")
    private String title;

    @Column(name = "content",columnDefinition = "longtext")
    @ApiModelProperty(example = "본문 내용")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at",columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",columnDefinition = "datetime")
    @ApiModelProperty(example = "게시물 업데이트 날짜")
    private LocalDateTime updatedAt;

    @Builder
    public Post(Member member,Long type,String title,String content) {
        this.member = member;
        this.type = type;
        this.title = title;
        this.content = content;
    }

    public static Post of(PostDto postDto,Member member)  {
        return Post.builder()
                .member(member)
                .type(0L)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .build();
    }

    public static Post of(NoticeRequestDto noticeRequestDto, Member member)  {
        return Post.builder()
                .member(member)
                .type(1L)
                .title(noticeRequestDto.getTitle())
                .content(noticeRequestDto.getContent())
                .build();
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void addPostTag(List<PostTag> postTagList) {
        this.postTagList = postTagList;
    }
}
