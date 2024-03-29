package com.planmate.server.service.comment;

import com.planmate.server.domain.Comment;
import com.planmate.server.domain.CommentLike;
import com.planmate.server.domain.Member;
import com.planmate.server.domain.Post;
import com.planmate.server.dto.request.comment.*;
import com.planmate.server.dto.response.comment.CommentLikeRequestDto;
import com.planmate.server.dto.response.comment.CommentPageResponseDto;
import com.planmate.server.dto.response.comment.CommentResponseDto;
import com.planmate.server.exception.comment.CommentNotFoundException;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.post.PostNotFoundException;
import com.planmate.server.repository.CommentLikeRepository;
import com.planmate.server.repository.CommentRepository;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.PostRepository;
import com.planmate.server.util.JwtUtil;
import static com.planmate.server.config.ModelMapperConfig.modelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    @Transactional(readOnly = true)
    public CommentPageResponseDto findMyComment(Integer pages) {
        List<CommentResponseDto> responseDtoList = new ArrayList<>();
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(pages,5,sort);
        Page<Comment> commentList = commentRepository.findAllByMemberId(memberId,pageable);

        for (Comment comment : commentList) {
            List<CommentLike> commentLikeList = commentLikeRepository.findAllByCommentId(comment.getCommentId());
            CommentResponseDto responseDto = CommentResponseDto.of(comment,commentLikeList,memberId);
            responseDtoList.add(responseDto);
        }

        return CommentPageResponseDto.of(commentList.getTotalPages(),commentList.getTotalElements(),responseDtoList);
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(CommentCreateRequestDto commentCreateRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Post post = postRepository.findById(commentCreateRequestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentCreateRequestDto.getPostId()));

        Comment comment = Comment.of(commentCreateRequestDto,member,post);

        return modelMapper.map(commentRepository.save(comment), CommentResponseDto.class);
    }

    @Override
    @Transactional
    public CommentResponseDto createChildComment(ChildCommentRequestDto childCommentRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Post post = postRepository.findById(childCommentRequestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(childCommentRequestDto.getPostId()));

        Comment childComment = Comment.of(childCommentRequestDto,member,post);

        return modelMapper.map(commentRepository.save(childComment), CommentResponseDto.class);
    }

    @Override
    @Transactional
    public CommentResponseDto editComment(CommentEditRequestDto commentEditRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Comment comment = commentRepository.findComment(memberId,commentEditRequestDto.getCommentId())
              .orElseThrow(() -> new CommentNotFoundException(commentEditRequestDto.getCommentId()));

        comment.updateContent(commentEditRequestDto.getContent());
        commentRepository.save(comment);

        return modelMapper.map(comment, CommentResponseDto.class);
    }

    @Override
    @Transactional
    public void setCommentLike(CommentLikeRequestDto dto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        Comment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new CommentNotFoundException(dto.getCommentId()));
        Optional<CommentLike> commentLike = commentLikeRepository.findCommentLike(memberId,dto.getCommentId());

        if (commentLike.isEmpty()) {
            CommentLike newLike = CommentLike.builder()
                    .member(member)
                    .comment(comment)
                    .build();
            commentLikeRepository.save(newLike);
        } else {
            commentLikeRepository.delete(commentLike.get());
        }
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Comment comment = commentRepository.findComment(memberId,commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (comment.getParentCommentId() == null) {
            List<Comment> childComments = commentRepository.findAllChildComment(comment.getCommentId());
            commentRepository.deleteAll(childComments);
        }

        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentPageResponseDto findRecentComment(CommentRequestDto commentRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(commentRequestDto.getPages(),5,sort);
        Page<Comment> comments = commentRepository.findRecentComment(commentRequestDto.getPostId(), pageable);

        for (Comment comment : comments) {
            List<CommentLike> commentLikeList = commentLikeRepository.findAllByCommentId(comment.getCommentId());

            CommentResponseDto responseDto = CommentResponseDto.of(comment,commentLikeList,memberId);
            responseDtoList.add(responseDto);
        }

        return CommentPageResponseDto.of(comments.getTotalPages(),comments.getTotalElements(),responseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findRecentChildComment(ChildRecentRequestDto commentRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        List<Comment> commentList = commentRepository.findChildRecent(commentRequestDto.getPostId(),
                commentRequestDto.getParentCommentId());

        for (Comment comment : commentList) {
            List<CommentLike> commentLikeList = commentLikeRepository.findAllByCommentId(comment.getCommentId());

            CommentResponseDto responseDto = CommentResponseDto.of(comment,commentLikeList,memberId);
            responseDtoList.add(responseDto);
        }

        if (responseDtoList.size() > 0) {
            responseDtoList.sort(
                    Comparator.comparing(CommentResponseDto::getUpdatedAt).reversed()
            );
        }

        return responseDtoList;
    }
}
