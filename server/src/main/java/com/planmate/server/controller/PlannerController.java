package com.planmate.server.controller;

import com.planmate.server.dto.request.planner.PlannerRequestDto;
import com.planmate.server.dto.response.planner.PlannerResponseDto;
import com.planmate.server.service.planner.PlannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@Api(tags = {"플래너 api"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/planner")
public class PlannerController {
    private final PlannerService plannerService;

    @ApiOperation("자신의 시간표 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "자신의 시간표 조회 성공"),
    })
    @GetMapping("/find")
    public ResponseEntity<List<PlannerResponseDto>> findPlan() {
        List<PlannerResponseDto> responseDtoList = plannerService.findPlan();
        return ResponseEntity.ok(responseDtoList);
    }

    @ApiOperation("시간표 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "시간표 추가 성공"),
    })
    @PostMapping("/add")
    public ResponseEntity<Boolean> addPlan(@RequestBody PlannerRequestDto plannerRequestDto) {
        plannerService.createPlan(plannerRequestDto);
        return ResponseEntity.ok(true);
    }

    @ApiOperation("시간표 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "시간표 수정 성공"),
    })
    @PostMapping("/edit")
    public ResponseEntity<Boolean> editPlan(@RequestBody PlannerRequestDto plannerRequestDto) {
        plannerService.editPlan(plannerRequestDto);
        return ResponseEntity.ok(true);
    }

    @ApiOperation("시간표 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "시간표 삭제 성공"),
    })
    @DeleteMapping
    public ResponseEntity<Boolean> removePlan(@RequestParam Long plannerId) {
        plannerService.deletePlan(plannerId);
        return ResponseEntity.ok(true);
    }
}
