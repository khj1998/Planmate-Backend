package com.planmate.server.controller;

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
            @ApiResponse(responseCode = "200", description = "추가 환료"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 멤버 없음"),
    })
    public ResponseEntity<ScheduleResponseDto> addDDay(@RequestBody AddScheduleRequestDto dto) {
        return ResponseEntity.ok(scheduleService.addDDay(dto));
    }

    @DeleteMapping("/remove")
    @ApiOperation(value = "d-day 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 환료"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 멤버 없음"),
    })
    public ResponseEntity removeDDay(@RequestParam(value = "id") Long id) {
        scheduleService.deleteDDay(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "d-day 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 환료"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 멤버 없음"),
    })
    public ResponseEntity<ScheduleResponseDto> modifyDDay(@RequestBody ScheduleEditRequestDto dto) {
        return ResponseEntity.ok(scheduleService.modifySchedule(dto));
    }

    @GetMapping("/all")
    @ApiOperation(value = "d-day 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 환료"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 멤버 없음"),
    })
    public ResponseEntity<List<Schedule>> findAll() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    @GetMapping("/min")
    @ApiOperation(value = "d-day 가장 적은거 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 환료"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 멤버 없음"),
    })
    public ResponseEntity<ScheduleResponseDto> findMin() {
        return ResponseEntity.ok(scheduleService.findMin());
    }
}
