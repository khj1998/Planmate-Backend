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

    public static NoticeResponseDto of(Post post, List<PostLike> postLikeList,List<MemberScrap> scrapList,  Long memberId) {
        Boolean isMyHearted = false;
        Boolean isMyScraped = false;
        Member member = post.getMember();

        for (PostLike postLike : postLikeList) {
            if (postLike.getMember().getMemberId() == memberId) {
                isMyHearted = true;
                break;
            }
        }

        for (MemberScrap memberScrap : scrapList) {
            if (memberScrap.getMember().getMemberId() == memberId) {
                isMyScraped = true;
                break;
            }
        }

        return NoticeResponseDto.builder()
                .noticeId(post.getPostId())
                .createdAt(post.getCreatedAt())
                .nickname(member.getMemberName())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount((long) postLikeList.size())
                .scrapCount((long) scrapList.size())
                .commentCount((long) post.getCommentList().size())
                .isMyHearted(isMyHearted)
                .isMyScraped(isMyScraped)
                .build();
    }
}
