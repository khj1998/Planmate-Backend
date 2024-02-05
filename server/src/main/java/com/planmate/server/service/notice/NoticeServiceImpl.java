package com.planmate.server.service.notice;

import com.planmate.server.domain.*;
import com.planmate.server.dto.request.notice.NoticeEditRequestDto;
import com.planmate.server.dto.request.notice.NoticeRequestDto;
import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.dto.request.post.ScrapDto;
import com.planmate.server.dto.response.notice.NoticePageResponseDto;
import com.planmate.server.dto.response.notice.NoticeResponseDto;
import com.planmate.server.dto.response.post.PostCreateResponseDto;
import com.planmate.server.dto.response.post.PostEditResponseDto;
import com.planmate.server.dto.response.post.PostPageResponseDto;
import com.planmate.server.dto.response.post.PostResponseDto;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.notice.NoticeNotFoundException;
import com.planmate.server.exception.post.PostNotFoundException;
import com.planmate.server.repository.*;
import com.planmate.server.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberScrapRepository memberScrapRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    @Transactional
    public Boolean createNotice(NoticeRequestDto noticeRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Member owner = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Post post = Post.of(noticeRequestDto,owner);
        postRepository.save(post);

        return true;
    }

    @Override
    @Transactional
    public void deleteNotice(Long noticeId) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Post post = postRepository.findMemberPost(noticeId,memberId)
                .orElseThrow(() -> new NoticeNotFoundException(noticeId));

        postRepository.delete(post);
    }

    @Override
    @Transactional
    public Boolean editNotice(NoticeEditRequestDto noticeEditRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Post post = postRepository.findMemberPost(noticeEditRequestDto.getNoticeId(), memberId)
                .orElseThrow(() -> new NoticeNotFoundException(noticeEditRequestDto.getNoticeId()));

        post.updateTitle(noticeEditRequestDto.getTitle());
        post.updateContent(noticeEditRequestDto.getContent());
        postRepository.save(post);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public NoticePageResponseDto findRecentNotice(Integer pages) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        List<NoticeResponseDto> responseDtoList = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(pages,10,sort);
        Page<Post> postList = postRepository.findPostByPage(1L,pageable);

        for (Post post : postList) {
            List<PostLike> postLikeList = postLikeRepository.findAllByPostPostId(post.getPostId());
            List<MemberScrap> scrapList = memberScrapRepository.findByPostPostId(post.getPostId());

            NoticeResponseDto responseDto = NoticeResponseDto.of(post,postLikeList,scrapList,memberId);
            responseDtoList.add(responseDto);
        }

        return NoticePageResponseDto.of(postList.getTotalPages(),responseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeResponseDto findByNoticeId(Long noticeId) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Post post = postRepository.findByPostId(noticeId)
                .orElseThrow(() -> new NoticeNotFoundException(noticeId));

        List<MemberScrap> memberScrapList = memberScrapRepository.
                findByPostPostId(noticeId);
        List<PostLike> postLikeList = postLikeRepository.findAllByPostPostId(noticeId);

        return NoticeResponseDto.of(post,postLikeList,memberScrapList,memberId);
    }
}
