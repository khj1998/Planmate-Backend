package com.planmate.server.controller;

import com.planmate.server.domain.Comment;
import com.planmate.server.dto.request.comment.ChildCommentRequestDto;
import com.planmate.server.dto.request.comment.CommentCreateRequestDto;
import com.planmate.server.dto.request.comment.CommentEditRequestDto;
import com.planmate.server.dto.request.comment.CommentRequestDto;
import com.planmate.server.dto.response.comment.CommentPageResponseDto;
import com.planmate.server.dto.response.comment.CommentResponseDto;
import com.planmate.server.service.comment.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/comment")
@Slf4j
@Api(tags = {"댓글 API"})
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/find")
    @ApiOperation("자신이 작성한 댓글 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "자신의 댓글 조회 성공"),
            @ApiResponse(responseCode = "401",description = "인증되지 않은 유저 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "유저가 맴버 | admin 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "자신의 댓글 조회 실패")
    })
    public ResponseEntity<CommentPageResponseDto> findComment(@RequestParam("pages") Integer pages) {
        CommentPageResponseDto responseDto = commentService.findMyComment(pages);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/find/all")
    @ApiOperation("게시물에 해당하는 최근 댓글 N개 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "최근 댓글 N개 조회 성공"),
            @ApiResponse(responseCode = "401",description = "인증되지 않은 유저 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "유저가 맴버 | admin 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "최근 댓글 N개 조회 실패")
    })
    public ResponseEntity<CommentPageResponseDto> findRecentComment(@RequestBody CommentRequestDto commentRequestDto) {
        CommentPageResponseDto responseDto = commentService.findRecentComment(commentRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/create")
    @ApiOperation("새 댓글을 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "새 댓글 생성 성공"),
            @ApiResponse(responseCode = "401",description = "인증되지 않은 유저 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "유저가 맴버 | admin 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "새 댓글 생성 실패")
    })
    public ResponseEntity<Boolean> addComment(@RequestBody CommentCreateRequestDto commentCreateRequestDto) {
        commentService.createComment(commentCreateRequestDto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/child/create")
    @ApiOperation(value = "대댓글 생성 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "새 대댓글 생성 성공"),
            @ApiResponse(responseCode = "401",description = "인증되지 않은 유저 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "유저가 맴버 | admin 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "새 대댓글 생성 실패")
    })
    public ResponseEntity<Boolean> addChildComment(@RequestBody ChildCommentRequestDto commentRequestDto) {
        commentService.createChildComment(commentRequestDto);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/like")
    @ApiOperation("댓글에 좋아요 생성/취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 좋아요 생성/취소 성공"),
            @ApiResponse(responseCode = "401",description = "인증되지 않은 유저 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "유저가 맴버 | admin 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "댓글 좋아요 생성/취소 실패")
    })
    public ResponseEntity<Boolean> addLike(@RequestParam Long commentId) {
        commentService.setCommentLike(commentId);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/modify")
    @ApiOperation("댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "401",description = "인증되지 않은 유저 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "유저가 맴버 | admin 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "댓글 수정 실패")
    })
    public ResponseEntity<Boolean> editComment(@RequestBody CommentEditRequestDto commentEditRequestDto) {
        commentService.editComment(commentEditRequestDto);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/remove")
    @ApiOperation("댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "401",description = "인증되지 않은 유저 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "유저가 맴버 | admin 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "댓글 삭제 실패")
    })
    public ResponseEntity<Boolean> deleteComment(@RequestParam Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(true);
    }
}