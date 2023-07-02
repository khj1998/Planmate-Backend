package com.planmate.server.service.post;

import com.planmate.server.domain.Member;
import com.planmate.server.domain.MemberScrap;
import com.planmate.server.domain.Post;
import com.planmate.server.domain.PostTag;
import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.dto.request.post.ScrapDto;
import com.planmate.server.dto.response.post.PostResponseDto;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.post.PostNotFoundException;
import com.planmate.server.exception.post.ScrapNotFoundException;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.MemberScrapRepository;
import com.planmate.server.repository.PostRepository;
import com.planmate.server.repository.PostTagRepository;
import com.planmate.server.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberScrapRepository memberScrapRepository;
    private final PostTagRepository postTagRepository;

    public PostServiceImpl(PostRepository postRepository, PostTagRepository postTagRepository
            ,MemberRepository memberRepository
            ,MemberScrapRepository memberScrapRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.memberScrapRepository = memberScrapRepository;
        this.postTagRepository = postTagRepository;
    }

    /**
     * 게시물을 생성하고 데이터베이스에 저장합니다.
     * @author kimhojin98@naver.com
     * @param postDto 요청으로 받은 게시물 Dto 입니다.
     * @return PostResponseDto - 게시물이 성공적으로 데이터베이스에 저장되면 반환되는 게시물 응답 Dto 입니다.
     */
    @Override
    public PostResponseDto createPost(PostDto postDto) {
        Post post = createPostEntity(postDto);
        Long memberId = JwtUtil.getMemberId();
        Member owner = findOwnerById(memberId);
        post.setOwner(owner);
        postRepository.save(post);

        createPostTags(postDto.getTagList(),post);
        return convertToPostDto(post);
    }

    /**
     * 게시물을 생성하고 데이터베이스에 저장합니다.
     * @author kimhojin98@naver.com
     * @param postId 쿼리 파라미터로 전달된 게시물의 Id 값입니다.
     * @return PostResponseDto - 게시물이 성공적으로 조회되면 반환되는 게시물 응답 Dto 입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public PostResponseDto  findByPostId(Long postId) {
        Post post = findPostById(postId);
        return convertToPostDto(post);
    }

    /**
     * 게시물을 수정하고 데이터베이스에 반영합니다.
     * @author kimhojin98@naver.com
     * @param postDto 요청으로 받은 게시물 Dto 입니다.
     * @return PostResponseDto - 게시물이 성공적으로 수정되어 데이터베이스에 반영되면 반환되는 게시물 응답 Dto 입니다.
     */
    @Override
    public PostResponseDto editPost(PostDto postDto) {
        Post post = findPostById(postDto.getId());
        post.updateTitle(postDto.getTitle());
        post.updateContent(postDto.getContent());
        postRepository.save(post);
        return convertToPostDto(post);
    }

    /**
     * 게시물을 삭제합니다.
     * @author kimhojin98@naver.com
     * @param postId 쿼리 파라미터로 전달받은 게시물의 Id 값입니다.
     */
    @Override
    public void deletePost(Long postId) {
        Long memberId = JwtUtil.getMemberId();
        Post post = postRepository.findByPostIdAndOwnerMemberId(postId,memberId)
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
        Long memberId = JwtUtil.getMemberId();
        Member member = findMemberById(memberId);
        List<Post> postList = postRepository.findByOwnerMemberId(member.getMemberId());
        return convertToPostDtoList(postList);
    }

    /**
     * 게시물을 스크랩합니다.
     * @author kimhojin98@naver.com
     * @param scrapDto 요청으로 전달받은 스크랩된 게시물 Dto 입니다.
     * @return PostResponseDto - 게시물이 성공적으로 스크랩되어 데이터베이스에 반영되면 반환되는 게시물 응답 Dto 입니다.
     */
    @Override
    public PostResponseDto scrapPost(ScrapDto scrapDto) {
        Long memberId = JwtUtil.getMemberId();
        Member member = findMemberById(memberId);
        Post post = findPostById(scrapDto.getPostId());
        MemberScrap memberScrap = createMemberScrap(member,post);
        memberScrapRepository.save(memberScrap);
        return convertToPostDto(post);
    }

    /**
     * 유저 자신이 스크랩한 게시물을 조회합니다.
     * @author kimhojin98@naver.com
     * @return PostResponseDto 유저 자신이 스크랩한 게시물 조회에 성공하면 반환되는 게시물 응답 Dto 리스트입니다.
     */
    @Override
    public List<PostResponseDto> findScrapPost() {
        Long memberId = JwtUtil.getMemberId();
        List<MemberScrap> scrapList = memberScrapRepository.findByOwnerMemberId(memberId);
        List<Post> postList = covertScrapToPostList(scrapList);
        return convertToPostDtoList(postList);
    }

    /**
     * 게시물 스크랩을 취소합니다.
     * @author kimhojin98@naver.com
     * @param postId 쿼리 파라미터로 받은 게시물의 Id 값입니다.
     * @throws ScrapNotFoundException memberId와 postId에 해당하는 스크랩을 찾지 못했을 때 발생하는 예외입니다.
     */
    @Override
    public void deleteScrapById(Long postId) {
        Long memberId = JwtUtil.getMemberId();
        MemberScrap scrap = memberScrapRepository.findByOwnerMemberIdAndPostPostId(memberId,postId);

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
    public List<PostResponseDto> findPostByTagName(String tagName) {
        List<PostTag> postTagList = postTagRepository.findByTagName(tagName);
        List<Post> postList = postTagList.stream()
                .map(PostTag::getPost)
                .collect(Collectors.toList());
        return convertToPostDtoList(postList);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(JwtUtil.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    private Post createPostEntity(PostDto postDto) {
        return Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .build();
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    private void createPostTags(List<String> postTagList,Post post) {
        List<PostTag> postTags = postTagList.stream()
                .map(tagName -> PostTag.of(tagName,post))
                .collect(Collectors.toList());
        postTagRepository.saveAll(postTags);
    }

    private MemberScrap createMemberScrap(Member member,Post post) {
        return MemberScrap.of(member,post);
    }

    private Member findOwnerById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    private PostResponseDto convertToPostDto(Post post) {
        PostResponseDto postResponseDto = PostResponseDto.of(post);
        postResponseDto.setPostTagList(convertPostTagList(post.getPostTagList()));
        return postResponseDto;
    }

    private List<PostResponseDto> convertToPostDtoList(List<Post> postList) {
        return postList.stream()
                .map(this::convertToPostDto)
                .collect(Collectors.toList());
    }

    private List<String> convertPostTagList(List<PostTag> postTagList) {
        return postTagList.stream()
                .map(PostTag::getTagName)
                .collect(Collectors.toList());
    }

    private List<Post> covertScrapToPostList(List<MemberScrap> scrapList) {
        return scrapList.stream()
                .map(MemberScrap::getPost)
                .collect(Collectors.toList());
    }
}
