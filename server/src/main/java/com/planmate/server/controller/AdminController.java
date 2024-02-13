package com.planmate.server.controller;

import com.planmate.server.domain.Member;
import com.planmate.server.dto.request.notice.NoticeEditRequestDto;
import com.planmate.server.dto.request.notice.NoticeRequestDto;
import com.planmate.server.dto.request.token.ReissueTokenRequestDto;
import com.planmate.server.dto.response.member.MemberResponseDto;
import com.planmate.server.dto.response.token.ReissueTokenResponseDto;
import com.planmate.server.service.member.MemberService;
import com.planmate.server.service.notice.NoticeService;
import com.planmate.server.service.token.TokenService;
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
    public ResponseEntity<MemberResponseDto> getInfo(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(memberService.getInfo(id));
    }

    @PostMapping("/notice")
    @ApiOperation("새 공지사항을 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "새 공지사항 생성 성공"),
    })
    public ResponseEntity<Boolean> createPost(@RequestBody NoticeRequestDto noticeRequestDto) {
        return ResponseEntity.ok(noticeService.createNotice(noticeRequestDto));
    }

    @PostMapping("/notice/edit")
    @ApiOperation("공지사항 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "공지사항 수정 성공"),
    })
    public ResponseEntity<Boolean> editPost(@RequestBody NoticeEditRequestDto noticeEditRequestDto) {
        return ResponseEntity.ok(noticeService.editNotice(noticeEditRequestDto));
    }

    @DeleteMapping("/notice")
    @ApiOperation("Id로 공지사항 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "공지사항 정상 삭제"),
    })
    public ResponseEntity deletePost(@RequestParam("noticeId") Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok().build();
    }
}
