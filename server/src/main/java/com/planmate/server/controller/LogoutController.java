package com.planmate.server.controller;

import com.planmate.server.service.logout.LogOutService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
@Slf4j
@RequiredArgsConstructor
public class LogoutController {
    private final LogOutService logOutService;

    @GetMapping("sign-out")
    @ApiOperation(value = "로그 아웃")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 환료"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 멤버 없음"),
    })
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok(logOutService.logout());
    }
}
