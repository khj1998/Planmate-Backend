package com.planmate.server.dto.response.post;

import com.planmate.server.domain.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEditResponseDto {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime updatedAt;

    public static PostEditResponseDto of(Post post) {
        return PostEditResponseDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
