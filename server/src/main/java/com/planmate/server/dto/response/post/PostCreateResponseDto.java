package com.planmate.server.dto.response.post;

import com.planmate.server.domain.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateResponseDto {
    private Long postId;
    private String nickname;
    private String title;
    private String content;
    private List<String> postTagList;
    private LocalDateTime updatedAt;

    public static PostCreateResponseDto of(Post post, String nickname, List<PostTag> postTagList) {
        List<String> tagList = new ArrayList<>();

        for (PostTag tag : postTagList) {
            tagList.add(tag.getTagName());
        }

        return PostCreateResponseDto.builder()
                .postId(post.getPostId())
                .nickname(nickname)
                .title(post.getTitle())
                .content(post.getContent())
                .postTagList(tagList)
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
