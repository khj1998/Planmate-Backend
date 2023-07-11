package com.planmate.server.dto.response.post;

import com.planmate.server.domain.Post;
import com.planmate.server.domain.PostTag;
import com.planmate.server.dto.request.post.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private String nickname;
    private String title;
    private String content;
    private List<String> postTagList;
    private LocalDateTime updatedAt;
    private Long likeCount;
    private Long scrapCount;
    private Long commentCount;

    public static PostResponseDto of(Post post,String nickname,Long likeCount,
                                     Long scrapCount,Long commentCount,List<PostTag> postTagList) {
        List<String> tagList = new ArrayList<>();

        for (PostTag tag : postTagList) {
            tagList.add(tag.getTagName());
        }

        return PostResponseDto.builder()
                .postId(post.getPostId())
                .nickname(nickname)
                .title(post.getTitle())
                .content(post.getContent())
                .postTagList(tagList)
                .likeCount(likeCount)
                .scrapCount(scrapCount)
                .commentCount(commentCount)
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
