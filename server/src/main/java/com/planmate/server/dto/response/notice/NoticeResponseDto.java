package com.planmate.server.dto.response.notice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.planmate.server.domain.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticeResponseDto {
    private Long noticeId;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Long likeCount;
    private Long scrapCount;
    private Long commentCount;
    private Boolean isMyHearted;
    private Boolean isMyScraped;

    public static NoticeResponseDto of(Post post, String nickname, List<PostLike> postLikeList,
                                     List<MemberScrap> scrapList, List<Comment> commentList, Long memberId) {
        Boolean isMyHearted = false;
        Boolean isMyScraped = false;

        for (PostLike postLike : postLikeList) {
            if (postLike.getMemberId() == memberId) {
                isMyHearted = true;
                break;
            }
        }

        for (MemberScrap memberScrap : scrapList) {
            if (memberScrap.getMemberId() == memberId) {
                isMyScraped = true;
                break;
            }
        }

        return NoticeResponseDto.builder()
                .noticeId(post.getPostId())
                .createdAt(post.getCreatedAt())
                .nickname(nickname)
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount((long) postLikeList.size())
                .scrapCount((long) scrapList.size())
                .commentCount((long) commentList.size())
                .isMyHearted(isMyHearted)
                .isMyScraped(isMyScraped)
                .build();
    }
}
