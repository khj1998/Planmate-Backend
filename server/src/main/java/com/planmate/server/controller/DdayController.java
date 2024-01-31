package com.planmate.server.controller;

import com.planmate.server.dto.request.dday.DdayFixRequestDto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.planmate.server.dto.request.dday.AddDdayRequestDto;
import com.planmate.server.dto.request.dday.DdayEditRequestDto;
import com.planmate.server.dto.response.dday.DdayResponseDto;
import com.planmate.server.service.schedule.DdayService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/dday")
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"스케줄 관련 API"})
public class DdayController {
    private final DdayService ddayService;

    @PostMapping("/add")
    @ApiOperation(value = "d-day 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추가 환료")
    })
    public ResponseEntity<DdayResponseDto> addDDay(@RequestBody AddDdayRequestDto dto) {
        return ResponseEntity.ok(ddayService.addDDay(dto));
    }

    @DeleteMapping
    @ApiOperation(value = "d-day 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 완료")
    })
    public ResponseEntity<Boolean> removeDDay(@RequestParam(value = "dDayId") Long dDayId) {
        ddayService.deleteDDay(dDayId);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "d-day 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 완료")
    })
    public ResponseEntity<DdayResponseDto> modifyDDay(@RequestBody DdayEditRequestDto dto) {
        return ResponseEntity.ok(ddayService.editSchedule(dto));
    }

    @GetMapping("/all")
    @ApiOperation(value = "d-day 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 완료")
    })
    public ResponseEntity<List<DdayResponseDto>> findAll() {
        return ResponseEntity.ok(ddayService.findAll());
    }

    @ApiOperation("고정된 디데이 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "고정된 디데이 조회 성공")
    })
    @GetMapping("/fix")
    public ResponseEntity<DdayResponseDto> findFixedDDay() {
        DdayResponseDto responseDto = ddayService.findFixedDDay();
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/fix")
    @ApiOperation(value = "d-day 고정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "d-day 고정 완료")
    })
    public ResponseEntity<Boolean> fixDDay(@RequestBody DdayFixRequestDto ddayFixRequestDto) {
        ddayService.fixDDay(ddayFixRequestDto.getDDayId());
        return ResponseEntity.ok(true);
    }
}
