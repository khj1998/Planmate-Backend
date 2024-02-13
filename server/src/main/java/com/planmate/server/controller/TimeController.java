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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/time")
@Slf4j
@Api(tags = {"시간 초기화 관련 API"})
@RequiredArgsConstructor
public class TimeController {
    private final SubjectService subjectService;

    @PostMapping("/reset")
    @ApiOperation("공부/운동 시간 리셋")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "공부/운동 시간 업데이트 성공")
    })
    public ResponseEntity<Boolean> resetTime() {
        subjectService.backUpAndInit();
        return ResponseEntity.ok(true);
    }

    @GetMapping("/check")
    @ApiOperation("백업 데이터 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "확인 성공")
    })
    public ResponseEntity<Integer> checkData() {
        return ResponseEntity.ok(subjectService.checkBackUpData());
    }

    @PostMapping("/slice/backup")
    @ApiOperation("특정 시간까지의 총 공부 시간 백업")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "특정 시간까지 총 공부 시간 백업 성공")
    })
    public ResponseEntity<Boolean> backupTimeSlice() {
        return ResponseEntity.ok(subjectService.backupTimeSlice());
    }
}
