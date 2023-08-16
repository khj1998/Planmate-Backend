package com.planmate.server.controller;

import com.planmate.server.service.subject.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/time")
@Slf4j
@Api(tags = {"시간 초기화 관련 API"})
@RequiredArgsConstructor
public class TimeInitController {
    private final SubjectService subjectService;

    @GetMapping("/reset")
    @ApiOperation("공부/운동 시간 리셋")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "공부/운동 시간 업데이트 성공"),
            @ApiResponse(responseCode = "404",description = "공부/운동 시간 업데이트에 실패함")
    })
    public ResponseEntity<Boolean> resetTime() {
        subjectService.backUpAndInit();
        return ResponseEntity.ok(true);
    }
}
