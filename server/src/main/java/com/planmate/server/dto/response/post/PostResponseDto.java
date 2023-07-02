package com.planmate.server.dto.response.post;

import com.planmate.server.domain.Post;
import com.planmate.server.dto.request.post.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 게시물 응답시 사용되는 Dto 객체입니다.
 * @author kimhojin98@naver.com
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private List<String> postTagList;
    private Date updatedAt;

    public static PostResponseDto of(Post post) {
        return PostResponseDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
