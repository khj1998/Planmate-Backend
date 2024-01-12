package com.planmate.server.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.planmate.server.domain.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시물 응답시 사용되는 Dto 객체입니다.
 * @author kimhojin98@naver.com
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {
    private Long postId;
    private String nickname;
    private String title;
    private String content;
    private List<String> postTagList;
    private LocalDateTime createdAt;
    private Long likeCount;
    private Long scrapCount;
    private Long commentCount;
    private Boolean isMyHearted;
    private Boolean isMyScraped;

    public static PostResponseDto of(Post post, Member member, List<PostLike> postLikeList,
                                     List<MemberScrap> scrapList, List<Comment> commentList,
                                     List<PostTag> postTagList) {
        Boolean isMyHearted = false;
        Boolean isMyScraped = false;
        List<String> tagList = new ArrayList<>();

        for (PostTag tag : postTagList) {
            tagList.add(tag.getTagName());
        }

        for (PostLike postLike : postLikeList) {
            if (postLike.getMemberId() == member.getMemberId()) {
                isMyHearted = true;
                break;
            }
        }

        for (MemberScrap memberScrap : scrapList) {
            if (memberScrap.getMemberId() == member.getMemberId()) {
                isMyScraped = true;
                break;
            }
        }

        return PostResponseDto.builder()
                .postId(post.getPostId())
                .nickname(member.getMemberName())
                .title(post.getTitle())
                .content(post.getContent())
                .postTagList(tagList)
                .likeCount((long) postLikeList.size())
                .scrapCount((long) scrapList.size())
                .commentCount((long) commentList.size())
                .createdAt(post.getCreatedAt())
                .isMyHearted(isMyHearted)
                .isMyScraped(isMyScraped)
                .build();
    }

    public static PostResponseDto of(Post post,String nickname,List<PostLike> postLikeList,
                                     List<MemberScrap> scrapList, List<Comment> commentList,Long memberId) {
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

        return PostResponseDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .nickname(nickname)
                .likeCount((long) postLikeList.size())
                .scrapCount((long) scrapList.size())
                .commentCount((long) commentList.size())
                .createdAt(post.getCreatedAt())
                .isMyHearted(isMyHearted)
                .isMyScraped(isMyScraped)
                .build();
    }
}
