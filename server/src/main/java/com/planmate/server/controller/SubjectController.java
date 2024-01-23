package com.planmate.server.controller;

import com.planmate.server.dto.request.subject.SubjectCreateRequestDto;
import com.planmate.server.dto.request.subject.SubjectEditRequestDto;
import com.planmate.server.dto.request.subject.SubjectTimeRequest;
import com.planmate.server.dto.response.subject.SubjectResponse;
import com.planmate.server.dto.response.subject.SubjectStudyTimeResponse;
import com.planmate.server.service.subject.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/subject")
@Slf4j
@Api(tags = {"과목 관련 API"})
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/studytime")
    @ApiOperation("자신의 각 과목 공부시간 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "자신의 각 과목 공부시간 조회 성공"),
    })
    public ResponseEntity<List<SubjectStudyTimeResponse>> findSubjectTime() {
        List<SubjectStudyTimeResponse> responseDtoList = subjectService.findSubjectTime();
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/find")
    @ApiOperation("자신의 공부/운동 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "자신의 공부/운동 목록 조회 성공"),
    })
    public ResponseEntity<List<SubjectResponse>> findSubject() {
        List<SubjectResponse> responseList = subjectService.findSubject();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/create")
    @ApiOperation("새 과목을 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "새 과목 생성 성공"),
    })
    public ResponseEntity<Boolean> addSubject(@RequestBody SubjectCreateRequestDto subjectCreateRequestDto) {
        subjectService.createSubject(subjectCreateRequestDto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/time")
    @ApiOperation("공부/운동 시간 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "공부/운동 시간 업데이트 성공"),
    })
    public ResponseEntity<Boolean> updateTime(@RequestBody SubjectTimeRequest subjectTimeRequest) {
        subjectService.updateSubjectTime(subjectTimeRequest);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/edit")
    @ApiOperation("과목 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "과목 수정 성공")
    })
    public ResponseEntity<Boolean> editSubject(@RequestBody SubjectEditRequestDto subjectEditRequestDto) {
        subjectService.editSubject(subjectEditRequestDto);
        return ResponseEntity.ok(true);
    }
    
    @DeleteMapping
    @ApiOperation("과목 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "과목 삭제 성공")
    })
    public ResponseEntity<Boolean> deleteSubject(@RequestParam Long subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok(true);
    }
}
