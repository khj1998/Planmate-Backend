package com.planmate.server.controller;

import com.planmate.server.dto.request.schedule.ScheduleFixRequestDto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.planmate.server.domain.Schedule;
import com.planmate.server.dto.request.schedule.AddScheduleRequestDto;
import com.planmate.server.dto.request.schedule.ScheduleEditRequestDto;
import com.planmate.server.dto.response.schedule.ScheduleResponseDto;
import com.planmate.server.service.schedule.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/schedule")
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"스케줄 관련 API"})
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/add")
    @ApiOperation(value = "d-day 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추가 환료")
    })
    public ResponseEntity<ScheduleResponseDto> addDDay(@RequestBody AddScheduleRequestDto dto) {
        return ResponseEntity.ok(scheduleService.addDDay(dto));
    }

    @DeleteMapping("/remove")
    @ApiOperation(value = "d-day 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 환료")
    })
    public ResponseEntity removeDDay(@RequestParam(value = "id") Long id) {
        scheduleService.deleteDDay(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "d-day 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 환료")
    })
    public ResponseEntity<ScheduleResponseDto> modifyDDay(@RequestBody ScheduleEditRequestDto dto) {
        return ResponseEntity.ok(scheduleService.modifySchedule(dto));
    }

    @GetMapping("/all")
    @ApiOperation(value = "d-day 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 환료")
    })
    public ResponseEntity<List<Schedule>> findAll() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    @ApiOperation("고정된 디데이 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "고정된 디데이 조회 성공")
    })
    @GetMapping("/fix")
    public ResponseEntity<ScheduleResponseDto> findFixedDDay() {
        ScheduleResponseDto responseDto = scheduleService.findFixedDDay();
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/fix")
    @ApiOperation(value = "d-day 고정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "d-day 고정 완료")
    })
    public ResponseEntity<Boolean> fixDDay(@RequestBody ScheduleFixRequestDto scheduleFixRequestDto) {
        scheduleService.fixDDay(scheduleFixRequestDto.getId());
        return ResponseEntity.ok(true);
    }
}
