package com.planmate.server.domain;

import com.planmate.server.dto.request.comment.ChildCommentRequestDto;
import com.planmate.server.dto.request.comment.CommentCreateRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Entity
@NamedEntityGraph(name = "comment_paging",attributeNodes = {
        @NamedAttributeNode("member")
})
@Table(name = "comment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @Column(name = "id", columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "고유 식별자")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "post_id")
    private Post post;

    @Column(name = "content",nullable = false,columnDefinition = "longtext")
    private String content;

    @Column(name = "parent_comment",columnDefinition = "int")
    private Long parentCommentId;

    @CreationTimestamp
    @Column(name = "created_at",columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false,columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    public static Comment of(CommentCreateRequestDto commentCreateRequestDto,Member member,Post post){
        return Comment.builder()
                .member(member)
                .post(post)
                .content(commentCreateRequestDto.getContent())
                .build();
    }

    public static Comment of(ChildCommentRequestDto childCommentRequestDto,Member member,Post post) {
        return Comment.builder()
                .member(member)
                .post(post)
                .parentCommentId(childCommentRequestDto.getParentCommentId())
                .content(childCommentRequestDto.getContent())
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
