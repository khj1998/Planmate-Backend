package com.planmate.server.controller;

import com.planmate.server.domain.Subject;
import com.planmate.server.dto.request.subject.SubjectCreateRequestDto;
import com.planmate.server.dto.request.subject.SubjectEditRequestDto;
import com.planmate.server.service.subject.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/subject")
@Slf4j
@Api(tags = {"과목 관련 API"})
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping("/create")
    @ApiOperation("새 과목을 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "새 과목 생성 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "새 과목을 생성하는데 실패함")
    })
    public ResponseEntity<Subject> addSubject(@RequestBody SubjectCreateRequestDto subjectCreateRequestDto) {
        Subject subject = subjectService.createSubject(subjectCreateRequestDto);
        return ResponseEntity.ok(subject);
    }

    @PostMapping("/edit")
    @ApiOperation("과목 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "과목 수정 성공")
    })
    public ResponseEntity<Subject> editSubject(@RequestBody SubjectEditRequestDto subjectEditRequestDto) {
        Subject subject = subjectService.editSubject(subjectEditRequestDto);
        return ResponseEntity.ok(subject);
    }
    
    @DeleteMapping("/remove")
    @ApiOperation("과목 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "과목 삭제 성공")
    })
    public ResponseEntity<Boolean> deleteSubject(@RequestParam Long subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok(true);
    }
}
