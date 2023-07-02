package com.planmate.server.controller;

import com.planmate.server.dto.request.tendinous.AlertRequestDto;
import com.planmate.server.service.tendinous.AlertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tendinous")
@Slf4j
@Api(tags = {"건의 관련 API"})
@RequiredArgsConstructor
public class TendinousController {
    private final AlertService alertService;

    @PostMapping("alert")
    @ApiOperation(value = "신고 내용을 슬랙으로 전송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 환료"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 멤버 없음"),
    })
    public ResponseEntity signOut(@RequestBody AlertRequestDto alertRequestDto) {
        alertService.alert(alertRequestDto);
        return ResponseEntity.ok().build();
    }
}
