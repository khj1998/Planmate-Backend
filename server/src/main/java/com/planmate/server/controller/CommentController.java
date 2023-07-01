package com.planmate.server.controller;

import com.planmate.server.domain.Comment;
import com.planmate.server.dto.request.comment.CommentCreateRequestDto;
import com.planmate.server.dto.request.comment.CommentEditRequestDto;
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
            @ApiResponse(responseCode = "200",description = "자신의 댓글 조회 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "자신의 댓글 조회에 실패함")
    })
    public ResponseEntity<List<CommentResponseDto>> findComment() {
        List<CommentResponseDto> responseDtoList = commentService.findMyComment();
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping("/create")
    @ApiOperation("새 댓글을 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "새 댓글 생성 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "새 댓글을 생성하는데 실패함")
    })
    public ResponseEntity<Comment> addComment(@RequestBody CommentCreateRequestDto commentCreateRequestDto) {
        Comment comment = commentService.createComment(commentCreateRequestDto);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/like")
    @ApiOperation("댓글에 좋아요 생성/취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "댓글 좋아요 생성/취소 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "댓글 좋아요 생성/취소 하는데 실패함")
    })
    public ResponseEntity<Boolean> addLike(@RequestParam Long commendId) {
        return ResponseEntity.ok(commentService.addCommentLike(commendId));
    }
    
    @PostMapping("/modify")
    @ApiOperation("댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "댓글 수정에 실패함")
    })
    public ResponseEntity<Comment> editComment(@RequestBody CommentEditRequestDto commentEditRequestDto) {
        Comment comment = commentService.editComment(commentEditRequestDto);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/remove")
    @ApiOperation("댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "댓글 삭제에 실패함")
    })
    public ResponseEntity<Boolean> deleteComment(@RequestParam Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(true);
    }
}
