package com.planmate.server.controller;

import com.planmate.server.dto.response.notice.NoticePageResponseDto;
import com.planmate.server.dto.response.notice.NoticeResponseDto;
import com.planmate.server.service.notice.NoticeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    @ApiOperation("공지사항 최신순 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "공지사항 최신순 조회 성공"),
    })
    public ResponseEntity<NoticePageResponseDto> findRecentNotice(@RequestParam Integer pages) {
        NoticePageResponseDto responseDto = noticeService.findRecentNotice(pages);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/detail")
    @ApiOperation("공지사항 Id 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "공지사항 Id 조회 성공"),
    })
    public ResponseEntity<NoticeResponseDto> findNoticeDetail(@RequestParam Long noticeId) {
        NoticeResponseDto responseDto = noticeService.findByNoticeId(noticeId);
        return ResponseEntity.ok(responseDto);
    }
}
