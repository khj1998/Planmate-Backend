package com.planmate.server.controller;

import com.planmate.server.domain.Authority;
import com.planmate.server.service.member.MemberService;
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

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@Slf4j
@Api(tags = {"권한 관련 API"})
@RequiredArgsConstructor
public class AuthorityController {
    private final MemberService memberService;

    @GetMapping("check")
    @ApiOperation(value = "사용자가 가진 권한 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 환료")
    })
    public ResponseEntity<List<Authority>> getAuthorities() {
        return ResponseEntity.ok(memberService.getAuthorities());
    }
}
