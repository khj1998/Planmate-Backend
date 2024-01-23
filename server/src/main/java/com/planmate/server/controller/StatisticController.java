package com.planmate.server.controller;

import com.planmate.server.dto.request.statistic.StatisticDateRequestDto;
import com.planmate.server.dto.request.statistic.StatisticMonthRequestDto;
import com.planmate.server.dto.response.statistic.StatisticResponse;
import com.planmate.server.service.statistic.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    })
    @GetMapping
    public ResponseEntity<StatisticResponse> getStatistic() {
        StatisticResponse responseDto = statisticService.getStatisticData();
        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation("M월 N일 통계 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "공부 통계 데이터 조회 api"),
    })
    @PostMapping("/day")
    public ResponseEntity<StatisticResponse> getDayStatistic(@RequestBody StatisticDateRequestDto statisticDateRequestDto) {
        StatisticResponse responseDto = statisticService.getDayStatisticData(statisticDateRequestDto.getStudyDate());
        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation("연 월로 통계 데이터 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "연 월로 통계 데이터 요청 성공")
    })
    @PostMapping("/month")
    public ResponseEntity<List<StatisticResponse>> getMonthStatistic(@RequestBody StatisticMonthRequestDto requestDto) {
        return ResponseEntity.ok(statisticService.getMonthStatisticData(requestDto.getYearMonth()));
    }
}
