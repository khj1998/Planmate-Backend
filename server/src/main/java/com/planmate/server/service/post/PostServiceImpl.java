package com.planmate.server.service.post;

import com.planmate.server.domain.*;
import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.dto.request.post.ScrapDto;
import com.planmate.server.dto.response.post.PostResponseDto;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.post.PostNotFoundException;
import com.planmate.server.exception.post.ScrapNotFoundException;
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
import java.util.stream.Collectors;

@Slf4j
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberScrapRepository memberScrapRepository;
    private final PostTagRepository postTagRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    public PostServiceImpl(PostRepository postRepository, PostTagRepository postTagRepository
            ,MemberRepository memberRepository
            ,MemberScrapRepository memberScrapRepository
            ,PostLikeRepository postLikeRepository
            ,CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.memberScrapRepository = memberScrapRepository;
        this.postTagRepository = postTagRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public List<PostResponseDto> findRecentPost(Integer pages) {
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC,"updatedAt");
        Pageable pageable = PageRequest.of(pages,10,sort);
        Page<Post> postList = postRepository.findAll(pageable);

        for (Post post : postList) {
            Member member = memberRepository.findById(post.getMemberId())
                    .orElseThrow(() -> new MemberNotFoundException(post.getMemberId()));

            List<PostLike> postLikeList = postLikeRepository.findAllByPostId(post.getPostId());
            List<MemberScrap> scrapList = memberScrapRepository.findByPostId(post.getPostId());
            List<PostTag> postTagList = postTagRepository.findByPostId(post.getPostId());
            List<Comment> commentList = commentRepository.findByPostId(post.getPostId());

            PostResponseDto responseDto = PostResponseDto.of(post,member.getMemberName(),
                    (long) postLikeList.size(),(long) scrapList.size(),(long) commentList.size(),postTagList);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    /**
     * 게시물을 생성하고 데이터베이스에 저장합니다.
     * @author kimhojin98@naver.com
     * @param postDto 요청으로 받은 게시물 Dto 입니다.
     * @return PostResponseDto - 게시물이 성공적으로 데이터베이스에 저장되면 반환되는 게시물 응답 Dto 입니다.
     */
    @Override
    @Transactional
    public PostResponseDto createPost(PostDto postDto) {
        Long memberId = JwtUtil.getMemberId();
        List<PostTag> postTagList = new ArrayList<>();

        Member owner = memberRepository.findById(memberId)
                        .orElseThrow(() -> new MemberNotFoundException(memberId));

        Post post = Post.of(postDto, owner.getMemberId());
        postRepository.save(post);

        for (String tagName : postDto.getTagList()) {
            PostTag postTag = PostTag.of(tagName,post.getPostId());
            postTagList.add(postTag);
        }

        postTagRepository.saveAll(postTagList);

        return PostResponseDto.of(post,owner.getMemberName(),
                0L,0L,0L,postTagList);
    }

    /**
     * 게시물을 파라미터로 조회합니다.
     * @author kimhojin98@naver.com
     * @param postId 쿼리 파라미터로 전달된 게시물의 Id 값입니다.
     * @return PostResponseDto - 게시물이 성공적으로 조회되면 반환되는 게시물 응답 Dto 입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public PostResponseDto  findByPostId(Long postId) {
        Long memberId = JwtUtil.getMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        List<PostTag> postTagList = postTagRepository.findByPostId(postId);
        List<MemberScrap> memberScrapList = memberScrapRepository.findByPostId(postId);
        List<PostLike> postLikeList = postLikeRepository.findAllByPostId(postId);
        List<Comment> commentList = commentRepository.findByPostId(post.getPostId());

        Long likeCount = (long) postLikeList.size();
        Long scrapCount = (long) memberScrapList.size();
        Long commentCount = (long) commentList.size();

        return PostResponseDto.of(post,member.getMemberName()
                ,likeCount ,scrapCount,commentCount,postTagList);
    }

    /**
     * 게시물을 수정하고 데이터베이스에 반영합니다.
     * @author kimhojin98@naver.com
     * @param postDto 요청으로 받은 게시물 Dto 입니다.
     * @return PostResponseDto - 게시물이 성공적으로 수정되어 데이터베이스에 반영되면 반환되는 게시물 응답 Dto 입니다.
     */
    @Override
    @Transactional
    public PostResponseDto editPost(PostDto postDto) {
        Long memberId = JwtUtil.getMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Post post = postRepository.findMemberPost(postDto.getId(),memberId)
                .orElseThrow(() -> new PostNotFoundException(postDto.getId()));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        postRepository.save(post);

        List<PostTag> postTagList = postTagRepository.findByPostId(postDto.getId());
        List<MemberScrap> memberScrapList = memberScrapRepository.findByPostId(post.getPostId());
        List<PostLike> postLikeList = postLikeRepository.findAllByPostId(post.getPostId());
        List<Comment> commentList = commentRepository.findByPostId(post.getPostId());

        Long likeCount = (long) postLikeList.size();
        Long scrapCount = (long) memberScrapList.size();
        Long commentCount = (long) commentList.size();

        return PostResponseDto.of(post,member.getMemberName(),
                likeCount,scrapCount,commentCount,postTagList);
    }

    /**
     * 게시물을 삭제합니다.
     * @author kimhojin98@naver.com
     * @param postId 쿼리 파라미터로 전달받은 게시물의 Id 값입니다.
     */
    @Override
    @Transactional
    public void deletePost(Long postId) {
        Long memberId = JwtUtil.getMemberId();
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
    public List<PostResponseDto> findMyPost() {
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        Long memberId = JwtUtil.getMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        List<Post> postList = postRepository.findByMemberId(member.getMemberId());

        for (Post post : postList) {
            List<PostTag> postTagList = postTagRepository.findByPostId(post.getPostId());
            List<MemberScrap> memberScrapList = memberScrapRepository.findByPostId(post.getPostId());
            List<PostLike> postLikeList = postLikeRepository.findAllByPostId(post.getPostId());
            List<Comment> commentList = commentRepository.findByPostId(post.getPostId());

            Long likeCount = (long) postLikeList.size();
            Long scrapCount = (long) memberScrapList.size();
            Long commentCount = (long) commentList.size();

            PostResponseDto responseDto = PostResponseDto.of(post, member.getMemberName(),
                    likeCount,scrapCount,commentCount,postTagList);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
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
        Long memberId = JwtUtil.getMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Post post = postRepository.findById(scrapDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(scrapDto.getPostId()));

        MemberScrap memberScrap = MemberScrap.of(member.getMemberId(),post.getPostId());
        memberScrapRepository.save(memberScrap);

        return true;
    }

    /**
     * 유저 자신이 스크랩한 게시물을 조회합니다.
     * @author kimhojin98@naver.com
     * @return PostResponseDto 유저 자신이 스크랩한 게시물 조회에 성공하면 반환되는 게시물 응답 Dto 리스트입니다.
     */
    @Override
    @Transactional
    public List<PostResponseDto> findScrapPost() {
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        Long memberId = JwtUtil.getMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        List<MemberScrap> scrapList = memberScrapRepository.findByMemberId(memberId);

        for (MemberScrap memberScrap : scrapList) {
            Post post = postRepository.findById(memberScrap.getPostId())
                    .orElseThrow(() -> new PostNotFoundException(memberScrap.getPostId()));

            List<PostTag> postTagList = postTagRepository.findByPostId(post.getPostId());
            List<PostLike> postLikeList = postLikeRepository.findAllByPostId(post.getPostId());
            List<MemberScrap> memberScrapList =  memberScrapRepository.findByPostId(post.getPostId());
            List<Comment> commentList = commentRepository.findByPostId(post.getPostId());

            Long likeCount = (long) postLikeList.size();
            Long scrapCount = (long) memberScrapList.size();
            Long commentCount = (long) commentList.size();

            PostResponseDto responseDto = PostResponseDto.of(post,member.getMemberName(),
                    likeCount,scrapCount,commentCount,postTagList);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    /**
     * 게시물 스크랩을 취소합니다.
     * @author kimhojin98@naver.com
     * @param postId 쿼리 파라미터로 받은 게시물의 Id 값입니다.
     * @throws ScrapNotFoundException memberId와 postId에 해당하는 스크랩을 찾지 못했을 때 발생하는 예외입니다.
     */
    @Override
    @Transactional
    public void deleteScrapById(Long postId) {
        Long memberId = JwtUtil.getMemberId();
        MemberScrap scrap = memberScrapRepository.findMemberScrap(memberId,postId);

        if (scrap == null) {
            throw new ScrapNotFoundException(postId);
        }

        memberScrapRepository.delete(scrap);
    }

    /**
     * 태그를 통해 게시물을 조회합니다.
     * @author kimhojin98@naver.com
     * @param tagName 쿼리 파라미터로 전달받은 태그 입니다.
     * @return List<PostResponseDto> 태그를 가지는 게시물들 조회에 성공하면 반한되는 게시물 응답 Dto 리스트입니다.
     */
    @Override
    @Transactional
    public List<PostResponseDto> findPostByTagName(String tagName) {
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        List<PostTag> postTagList = postTagRepository.findByTagName(tagName);

        for (PostTag postTag : postTagList) {
            Post post = postRepository.findById(postTag.getPostId())
                    .orElseThrow(() -> new PostNotFoundException(postTag.getPostId()));

            Member member = memberRepository.findById(post.getMemberId())
                    .orElseThrow(() -> new MemberNotFoundException(post.getMemberId()));

            List<PostLike> postLikeList = postLikeRepository.findAllByPostId(post.getPostId());
            List<MemberScrap> scrapList = memberScrapRepository.findByPostId(post.getPostId());
            List<PostTag> postTags = postTagRepository.findByPostId(post.getPostId());
            List<Comment> commentList = commentRepository.findByPostId(post.getPostId());

            PostResponseDto responseDto = PostResponseDto.of(post,member.getMemberName(),
                                        (long) postLikeList.size(),(long) scrapList.size(),(long) commentList.size(),postTags);

            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Override
    @Transactional
    public Boolean setPostLike(Long postId) {
        Long memberId = JwtUtil.getMemberId();
        PostLike postLike = postLikeRepository.findByPost(memberId,postId);

        if (postLike == null) {
            postLike = PostLike.of(memberId,postId);
            postLikeRepository.save(postLike);
        } else {
            postLikeRepository.delete(postLike);
        }

        return true;
    }
}