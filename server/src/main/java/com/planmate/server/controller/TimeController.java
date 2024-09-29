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

    @GetMapping("/check")
    @ApiOperation("백업 데이터 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "확인 성공")
    })
    public ResponseEntity<Integer> checkData() {
        return ResponseEntity.ok(subjectService.checkBackUpData());
    }
}
