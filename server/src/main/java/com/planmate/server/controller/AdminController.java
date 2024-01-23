package com.planmate.server.controller;

import com.planmate.server.domain.Member;
import com.planmate.server.dto.request.notice.NoticeEditRequestDto;
import com.planmate.server.dto.request.notice.NoticeRequestDto;
import com.planmate.server.dto.response.post.PostCreateResponseDto;
import com.planmate.server.dto.response.post.PostEditResponseDto;
import com.planmate.server.service.member.MemberService;
import com.planmate.server.service.notice.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Api(tags = {"관리자 api"})
@Slf4j
public class AdminController {
    private final MemberService memberService;
    private final NoticeService noticeService;

    @GetMapping("/user/info")
    @ApiOperation(value = "사용자 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 환료"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음")
    })
    public ResponseEntity<Member> getInfo(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(memberService.getInfo(id));
    }

    @PostMapping("/notice")
    @ApiOperation("새 공지사항을 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "새 공지사항 생성 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 ADMIN 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "새 공지사항 생성에 실패함")
    })
    public ResponseEntity<Boolean> createPost(@RequestBody NoticeRequestDto noticeRequestDto) {
        return ResponseEntity.ok(noticeService.createNotice(noticeRequestDto));
    }

    @PostMapping("/notice/edit")
    @ApiOperation("공지사항 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "공지사항 수정 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 ADMIN 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "공지사항 수정에 실패함")
    })
    public ResponseEntity<Boolean> editPost(@RequestBody NoticeEditRequestDto noticeEditRequestDto) {
        return ResponseEntity.ok(noticeService.editNotice(noticeEditRequestDto));
    }

    @DeleteMapping("/notice")
    @ApiOperation("Id로 공지사항 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "공지사항 정상 삭제"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 ADMIN 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "공지사항 삭제에 실패함")
    })
    public ResponseEntity deletePost(@RequestParam("noticeId") Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok().build();
    }
}
