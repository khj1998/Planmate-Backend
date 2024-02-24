package com.planmate.server.controller;

import com.planmate.server.dto.request.member.MemberBanRequestDto;
import com.planmate.server.dto.request.notice.NoticeEditRequestDto;
import com.planmate.server.dto.request.notice.NoticeRequestDto;
import com.planmate.server.dto.response.member.MemberResponseDto;
import com.planmate.server.service.admin.AdminService;
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
    private final AdminService adminService;

    @GetMapping("/user/info")
    @ApiOperation(value = "사용자 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 완료")
    })
    public ResponseEntity<MemberResponseDto> getInfo(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(memberService.getInfo(id));
    }

    @PostMapping("/user/ban")
    @ApiOperation(value = "사용자 닉네임으로 벤")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "사용자 닉네임으로 벤 성공")
    })
    public ResponseEntity<MemberResponseDto> banMember(@RequestBody MemberBanRequestDto dto) {
        return ResponseEntity.ok(adminService.banMember(dto));
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

    @DeleteMapping("/user/unban")
    @ApiOperation("이메일로 요청받아 사용자 벤 해제")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "사용자 벤 해제 성공")
    })
    public ResponseEntity unbanMember(@RequestParam("email") String email) {
        adminService.removeUserBan(email);
        return ResponseEntity.ok().build();
    }
}
