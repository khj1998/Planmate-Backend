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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NoticeServiceImpl implements NoticeService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberScrapRepository memberScrapRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    public NoticeServiceImpl(final PostRepository postRepository,
                             final MemberRepository memberRepository,
                             final MemberScrapRepository memberScrapRepository,
                             final PostLikeRepository postLikeRepository,
                             final CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.memberScrapRepository = memberScrapRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public Boolean createNotice(NoticeRequestDto noticeRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Member owner = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Post post = Post.of(noticeRequestDto,memberId);
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

        post.setTitle(noticeEditRequestDto.getTitle());
        post.setContent(noticeEditRequestDto.getContent());
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
            Member member = memberRepository.findById(post.getMemberId())
                    .orElseThrow(() -> new MemberNotFoundException(post.getMemberId()));

            List<PostLike> postLikeList = postLikeRepository.findAllByPostId(post.getPostId());
            List<MemberScrap> scrapList = memberScrapRepository.findByPostId(post.getPostId());
            List<Comment> commentList = commentRepository.findByPostId(post.getPostId());

            NoticeResponseDto responseDto = NoticeResponseDto.of(post,member.getMemberName(),postLikeList,scrapList,commentList,memberId);

            responseDtoList.add(responseDto);
        }

        return NoticePageResponseDto.of(postList.getTotalPages(),responseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeResponseDto findByNoticeId(Long noticeId) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Post post = postRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeNotFoundException(noticeId));

        Member member = memberRepository.findById(post.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(post.getMemberId()));

        List<MemberScrap> memberScrapList = memberScrapRepository.findByPostId(noticeId);
        List<PostLike> postLikeList = postLikeRepository.findAllByPostId(noticeId);
        List<Comment> commentList = commentRepository.findByPostId(noticeId);

        return NoticeResponseDto.of(post,member.getMemberName()
                ,postLikeList,memberScrapList,commentList,memberId);
    }
}
