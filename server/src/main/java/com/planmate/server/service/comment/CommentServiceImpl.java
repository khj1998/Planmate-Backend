package com.planmate.server.service.comment;

import com.planmate.server.domain.Comment;
import com.planmate.server.domain.CommentLike;
import com.planmate.server.domain.Member;
import com.planmate.server.dto.request.comment.CommentCreateRequestDto;
import com.planmate.server.dto.request.comment.CommentEditRequestDto;
import com.planmate.server.dto.response.comment.CommentResponseDto;
import com.planmate.server.exception.comment.CommentNotFoundException;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.repository.CommentLikeRepository;
import com.planmate.server.repository.CommentRepository;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentServiceImpl(CommentRepository commentRepository,MemberRepository memberRepository
            ,CommentLikeRepository commentLikeRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    @Override
    @Transactional
    public List<CommentResponseDto> findMyComment() {
        List<CommentResponseDto> responseDtoList = new ArrayList<>();
        Long memberId = JwtUtil.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        List<Comment> commentList = commentRepository.findAllByMemberId(memberId);

        for (Comment comment : commentList) {
            List<CommentLike> commentLikeList = commentLikeRepository.findAllByCommentId(comment.getCommentId());
            CommentResponseDto responseDto = CommentResponseDto.of(comment,member.getMemberName(),(long) commentLikeList.size());
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Override
    @Transactional
    public Comment createComment(CommentCreateRequestDto commentCreateRequestDto) {
        Long memberId = JwtUtil.getMemberId();
        Comment comment = Comment.of(commentCreateRequestDto,memberId);
        commentRepository.save(comment);

        return comment;
    }

    @Override
    @Transactional
    public Comment editComment(CommentEditRequestDto commentEditRequestDto) {
        Long memberId = JwtUtil.getMemberId();
        Comment comment = commentRepository.findComment(memberId,commentEditRequestDto.getCommentId())
              .orElseThrow(() -> new CommentNotFoundException(commentEditRequestDto.getCommentId()));

        comment.setContent(commentEditRequestDto.getContent());
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public Boolean setCommentLike(Long commentId) {
        Long userId = JwtUtil.getMemberId();

        CommentLike commentLike = commentLikeRepository.findCommentLike(userId,commentId);

        if (commentLike == null) {
            commentLike = CommentLike.builder()
                    .userId(userId)
                    .commentId(commentId)
                    .build();
            commentLikeRepository.save(commentLike);
        } else {
            commentLikeRepository.delete(commentLike);
        }

        return true;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Long memberId = JwtUtil.getMemberId();
        Comment comment = commentRepository.findComment(memberId,commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        commentRepository.delete(comment);
    }
}
