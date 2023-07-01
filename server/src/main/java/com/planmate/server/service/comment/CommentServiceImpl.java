package com.planmate.server.service.comment;

import com.planmate.server.domain.Comment;
import com.planmate.server.domain.Member;
import com.planmate.server.dto.request.comment.CommentCreateRequestDto;
import com.planmate.server.dto.request.comment.CommentEditRequestDto;
import com.planmate.server.dto.response.comment.CommentResponseDto;
import com.planmate.server.exception.comment.CommentNotFoundException;
import com.planmate.server.exception.member.MemberNotFoundException;
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

    public CommentServiceImpl(CommentRepository commentRepository,MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
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
            CommentResponseDto responseDto = CommentResponseDto.of(comment,member.getMemberName());
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
    public Boolean addCommentLike(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        commentRepository.save(comment);

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
