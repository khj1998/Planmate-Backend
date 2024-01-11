package com.planmate.server.service.comment;

import com.planmate.server.domain.Comment;
import com.planmate.server.domain.CommentLike;
import com.planmate.server.domain.Member;
import com.planmate.server.domain.Post;
import com.planmate.server.dto.request.comment.*;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentServiceImpl(PostRepository postRepository,CommentRepository commentRepository,MemberRepository memberRepository
            ,CommentLikeRepository commentLikeRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CommentPageResponseDto findMyComment(Integer pages) {
        List<CommentResponseDto> responseDtoList = new ArrayList<>();
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(pages,5,sort);
        Page<Comment> commentList = commentRepository.findAllByMemberId(memberId,pageable);

        for (Comment comment : commentList) {
            List<CommentLike> commentLikeList = commentLikeRepository.findAllByCommentId(comment.getCommentId());
            CommentResponseDto responseDto = CommentResponseDto.of(comment,member,commentLikeList,memberId);
            responseDtoList.add(responseDto);
        }

        return CommentPageResponseDto.of(commentList.getTotalPages(),commentList.getTotalElements(),responseDtoList);
    }

    @Override
    @Transactional
    public void createComment(CommentCreateRequestDto commentCreateRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Comment comment = Comment.of(commentCreateRequestDto,memberId);
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void createChildComment(ChildCommentRequestDto childCommentRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Comment parentComment = commentRepository.findById(childCommentRequestDto.getParentCommentId())
                .orElseThrow(() -> new CommentNotFoundException(childCommentRequestDto.getParentCommentId()));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Comment childComment = Comment.of(childCommentRequestDto,memberId);
        commentRepository.save(childComment);
    }

    @Override
    @Transactional
    public void editComment(CommentEditRequestDto commentEditRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Comment comment = commentRepository.findComment(memberId,commentEditRequestDto.getCommentId())
              .orElseThrow(() -> new CommentNotFoundException(commentEditRequestDto.getCommentId()));

        comment.setContent(commentEditRequestDto.getContent());
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void setCommentLike(Long commentId) {
        Long userId = JwtUtil.getUserIdByAccessToken();

        CommentLike commentLike = commentLikeRepository.findCommentLike(userId,commentId);

        if (commentLike == null) {
            commentLike = CommentLike.builder()
                    .memberId(userId)
                    .commentId(commentId)
                    .build();
            commentLikeRepository.save(commentLike);
        } else {
            commentLikeRepository.delete(commentLike);
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

        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentRequestDto.getPostId()));

        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(commentRequestDto.getPages(),5,sort);
        Page<Comment> comments = commentRepository.findRecentComment(commentRequestDto.getPostId(), pageable);

        for (Comment comment : comments) {
            Member member = memberRepository.findById(comment.getMemberId())
                    .orElseThrow(() -> new MemberNotFoundException(comment.getMemberId()));

            Boolean isAuthor = comment.getMemberId().equals(post.getMemberId());
            List<CommentLike> commentLikeList = commentLikeRepository.findAllByCommentId(comment.getCommentId());

            CommentResponseDto responseDto = CommentResponseDto.of(comment,member,commentLikeList,isAuthor,memberId);
            responseDtoList.add(responseDto);
        }

        return CommentPageResponseDto.of(comments.getTotalPages(),comments.getTotalElements(),responseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findRecentChildComment(ChildRecentRequestDto commentRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentRequestDto.getPostId()));

        List<Comment> comments = commentRepository.findChildRecent(commentRequestDto.getPostId(),
                commentRequestDto.getParentCommentId());

        for (Comment comment : comments) {
            Member member = memberRepository.findById(comment.getMemberId())
                    .orElseThrow(() -> new MemberNotFoundException(comment.getMemberId()));

            Boolean isAuthor = comment.getMemberId().equals(post.getMemberId());
            List<CommentLike> commentLikeList = commentLikeRepository.findAllByCommentId(comment.getCommentId());

            CommentResponseDto responseDto = CommentResponseDto.of(comment,member,commentLikeList,isAuthor,memberId);
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
