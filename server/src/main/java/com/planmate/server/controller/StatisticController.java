package com.planmate.server.controller;

import com.planmate.server.dto.response.statistic.StatisticResponse;
import com.planmate.server.service.statistic.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
@Api(tags = {"통계 api"})
public class StatisticController {
    private final StatisticService statisticService;

    @ApiOperation("통계 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "공부 통계 데이터 조회 api"),
            @ApiResponse(responseCode = "401",description = "유저가 인증되지 않음"),
            @ApiResponse(responseCode = "403",description = "유저가 맴버 | ADMIN 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "공부 통계 데이터 조회 실패")
    })
    @GetMapping
    public ResponseEntity<StatisticResponse> getStatistic() {
        StatisticResponse responseDto = statisticService.getStatisticData();
        return ResponseEntity.ok(responseDto);
    }
}
