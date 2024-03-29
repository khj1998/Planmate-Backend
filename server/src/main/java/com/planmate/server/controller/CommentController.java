package com.planmate.server.controller;

import com.planmate.server.domain.Comment;
import com.planmate.server.dto.request.comment.*;
import com.planmate.server.dto.response.comment.CommentLikeRequestDto;
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
    })
    public ResponseEntity<CommentPageResponseDto> findComment(@RequestParam("pages") Integer pages) {
        CommentPageResponseDto responseDto = commentService.findMyComment(pages);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/find/all")
    @ApiOperation("게시물에 해당하는 최근 댓글 N개 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "최근 댓글 N개 조회 성공"),
    })
    public ResponseEntity<CommentPageResponseDto> findRecentComment(@RequestBody CommentRequestDto commentRequestDto) {
        CommentPageResponseDto responseDto = commentService.findRecentComment(commentRequestDto);
        return ResponseEntity.ok(responseDto);
    }
    
    @PostMapping("/child/recent")
    @ApiOperation("자식 댓글 N개 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "자식 댓글 N개 조회 성공"),
    })
    public ResponseEntity<List<CommentResponseDto>> findChildComment(@RequestBody ChildRecentRequestDto commentRequestDto) {
        List<CommentResponseDto> responseDtoList = commentService.findRecentChildComment(commentRequestDto);
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping("/create")
    @ApiOperation("새 댓글을 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "새 댓글 생성 성공"),
    })
    public ResponseEntity<CommentResponseDto> addComment(@RequestBody CommentCreateRequestDto commentCreateRequestDto) {
        return ResponseEntity.ok(commentService.createComment(commentCreateRequestDto));
    }

    @PostMapping("/child/create")
    @ApiOperation(value = "대댓글 생성 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "새 대댓글 생성 성공"),
    })
    public ResponseEntity<CommentResponseDto> addChildComment(@RequestBody ChildCommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.createChildComment(commentRequestDto));
    }

    @PostMapping("/like")
    @ApiOperation("댓글에 좋아요 생성/취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 좋아요 생성/취소 성공"),
    })
    public ResponseEntity<Boolean> addLike(@RequestBody CommentLikeRequestDto dto) {
        commentService.setCommentLike(dto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/edit")
    @ApiOperation("댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
    })
    public ResponseEntity<CommentResponseDto> editComment(@RequestBody CommentEditRequestDto commentEditRequestDto) {
        return ResponseEntity.ok(commentService.editComment(commentEditRequestDto));
    }

    @DeleteMapping
    @ApiOperation("댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
    })
    public ResponseEntity<Boolean> deleteComment(@RequestParam Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(true);
    }
}