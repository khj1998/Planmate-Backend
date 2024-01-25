package com.planmate.server.service.post;

import com.planmate.server.domain.*;
import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.dto.request.post.ScrapDto;
import com.planmate.server.dto.response.post.PostCreateResponseDto;
import com.planmate.server.dto.response.post.PostEditResponseDto;
import com.planmate.server.dto.response.post.PostPageResponseDto;
import com.planmate.server.dto.response.post.PostResponseDto;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.post.PostNotFoundException;
import com.planmate.server.repository.*;
import com.planmate.server.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberScrapRepository memberScrapRepository;
    private final PostTagRepository postTagRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    /**
     * 페이지 별 게시물 정보를 조회합니다.
     * @author kimhojin98@naver.com
     * @param pages 게시물을 조회할 페이지 입니다.
     * @return PostPageResponseDto - 각 페이지 별로 게시물 응답 dto입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public PostPageResponseDto findRecentPost(Integer pages) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        List<PostResponseDto> responseDtoList = new ArrayList<>();
        Page<Post> postList = postRepository.findPostByPage(0L,getPostPageable(pages));

        for (Post post : postList) {
            PostResponseDto responseDto = PostResponseDto.of(post);
            responseDto.setIsMyPost(post.getMember().getMemberId().equals(memberId));
            responseDtoList.add(responseDto);
        }

        return PostPageResponseDto.of(postList.getTotalPages(),responseDtoList);
    }

    /**
     * 게시물을 파라미터로 조회합니다.
     * @author kimhojin98@naver.com
     * @param postId 쿼리 파라미터로 전달된 게시물의 Id 값입니다.
     * @return PostResponseDto - 게시물이 성공적으로 조회되면 반환되는 게시물 응답 Dto 입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public PostResponseDto findByPostId(Long postId) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        PostResponseDto responseDto = PostResponseDto.of(post);
        responseDto.setIsMyPost(post.getMember().getMemberId().equals(memberId));

        return responseDto;
    }

    /**
     * 게시물을 생성하고 데이터베이스에 저장합니다.
     * @author kimhojin98@naver.com
     * @param postDto 요청으로 받은 게시물 Dto 입니다.
     * @return PostResponseDto - 게시물이 성공적으로 데이터베이스에 저장되면 반환되는 게시물 응답 Dto 입니다.
     */
    @Override
    @Transactional
    public PostCreateResponseDto createPost(PostDto postDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        List<PostTag> postTagList = new ArrayList<>();

        Member owner = memberRepository.findById(memberId)
                        .orElseThrow(() -> new MemberNotFoundException(memberId));

        Post post = Post.of(postDto, owner);

        for (String tagName : postDto.getTagList()) {
            PostTag postTag = PostTag.of(tagName,post);
            postTagList.add(postTag);
        }
        post.addPostTag(postTagList);
        postRepository.save(post);

        return PostCreateResponseDto.of(post,owner.getMemberName(), postTagList);
    }

    /**
     * 게시물을 수정하고 데이터베이스에 반영합니다.
     * @author kimhojin98@naver.com
     * @param postDto 요청으로 받은 게시물 Dto 입니다.
     * @return PostResponseDto - 게시물이 성공적으로 수정되어 데이터베이스에 반영되면 반환되는 게시물 응답 Dto 입니다.
     */
    @Override
    @Transactional
    public PostEditResponseDto editPost(PostDto postDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Post post = postRepository.findMemberPost(postDto.getPostId(),memberId)
                .orElseThrow(() -> new PostNotFoundException(postDto.getPostId()));

        post.updateTitle(postDto.getTitle());
        post.updateContent(postDto.getContent());
        postRepository.save(post);

        return PostEditResponseDto.of();
    }

    /**
     * 게시물을 삭제합니다.
     * @author kimhojin98@naver.com
     * @param postId 쿼리 파라미터로 전달받은 게시물의 Id 값입니다.
     */
    @Override
    @Transactional
    public void deletePost(Long postId) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Post post = postRepository.findMemberPost(postId,memberId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        postRepository.delete(post);
    }

    /**
     * 유저 자신이 등록한 게시물 모두를 조회합니다.
     * @author kimhojin98@naver.com
     * @return PostResponseDto - 유저 자신이 게시물들을 성공적으로 조회하면 반환되는 게시물 응답 Dto 리스트입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public PostPageResponseDto findMyPost(Integer pages) {
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        Page<Post> postList = postRepository.findByMemberMemberId(member.getMemberId(),getPostPageable(pages));

        for (Post post : postList) {
            PostResponseDto responseDto = PostResponseDto.of(post);
            responseDto.setIsMyPost(post.getMember().getMemberId().equals(memberId));
            responseDtoList.add(responseDto);
        }

        return PostPageResponseDto.of(postList.getTotalPages(),responseDtoList);
    }

    /**
     * 게시물을 스크랩합니다.
     * @author kimhojin98@naver.com
     * @param scrapDto 요청으로 전달받은 스크랩된 게시물 Dto 입니다.
     * @return PostResponseDto - 게시물이 성공적으로 스크랩되어 데이터베이스에 반영되면 반환되는 게시물 응답 Dto 입니다.
     */
    @Override
    @Transactional
    public Boolean scrapPost(ScrapDto scrapDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Boolean isScraped = memberScrapRepository.findMemberScrap(memberId,scrapDto.getPostId()).isPresent();

        if (isScraped) {
            MemberScrap memberScrap = memberScrapRepository.findMemberScrap(memberId,scrapDto.getPostId()).get();
            memberScrapRepository.delete(memberScrap);
        } else {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberNotFoundException(memberId));
            Post post = postRepository.findById(scrapDto.getPostId())
                    .orElseThrow(() -> new PostNotFoundException(scrapDto.getPostId()));

            MemberScrap memberScrap = MemberScrap.of(member,post);
            memberScrapRepository.save(memberScrap);
        }

        return true;
    }

    /**
     * 유저 자신이 스크랩한 게시물을 조회합니다.
     * @author kimhojin98@naver.com
     * @return PostResponseDto 유저 자신이 스크랩한 게시물 조회에 성공하면 반환되는 게시물 응답 Dto 리스트입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public PostPageResponseDto findScrapPost(Integer pages) {
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Page<MemberScrap> scrapList = memberScrapRepository.findByMemberMemberId(memberId,getPostPageable(pages));

        for (MemberScrap memberScrap : scrapList) {
            Post post = postRepository.findByPostId(memberScrap.getPost().getPostId())
                    .orElseThrow(() -> new PostNotFoundException(memberScrap.getMember().getMemberId()));

            PostResponseDto responseDto = PostResponseDto.of(post);
            responseDto.setIsMyPost(post.getMember().getMemberId().equals(memberId));
            responseDtoList.add(responseDto);
        }

        return PostPageResponseDto.of(scrapList.getTotalPages(),responseDtoList);
    }

    /**
     * 태그를 통해 게시물을 조회합니다.
     * @author kimhojin98@naver.com
     * @param tagName 쿼리 파라미터로 전달받은 태그 입니다.
     * @return List<PostResponseDto> 태그를 가지는 게시물들 조회에 성공하면 반한되는 게시물 응답 Dto 리스트입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public PostPageResponseDto findPostByTagName(String tagName,Integer pages) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        List<PostResponseDto> responseDtoList = new ArrayList<>();

        Page<PostTag> postTagList = postTagRepository.findByTagName(tagName,getPostPageable(pages));

        for (PostTag postTag : postTagList) {
            Post post = postTag.getPost();
            PostResponseDto responseDto = PostResponseDto.of(post);
            responseDto.setIsMyPost(post.getMember().getMemberId().equals(memberId));
            responseDtoList.add(responseDto);
        }

        return PostPageResponseDto.of(postTagList.getTotalPages(),responseDtoList);
    }

    @Override
    @Transactional
    public Boolean setPostLike(Long postId) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Optional<PostLike> postLike = postLikeRepository.findByPost(memberId,postId);

        if (postLike.isPresent()) {
            postLikeRepository.delete(postLike.get());
        } else {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberNotFoundException(memberId));
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException(postId));

            PostLike newLike = PostLike.of(member,post);
            postLikeRepository.save(newLike);
        }

        return true;
    }

    private Pageable getPostPageable(Integer pages) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        return PageRequest.of(pages,10,sort);
    }
}
