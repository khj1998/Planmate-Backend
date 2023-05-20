package com.planmate.server.controller;

import com.planmate.server.domain.Token;
import com.planmate.server.dto.request.token.RefreshTokenDto;
import com.planmate.server.service.member.MemberService;
import com.planmate.server.service.token.TokenService;
import com.planmate.server.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;
    private final MemberService memberService;

    @GetMapping("/expiredAt")
    @ApiOperation("토큰 만료 확인 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답"),
            @ApiResponse(responseCode = "404", description = "해당 정보를 가진 Member가 없음"),
            @ApiResponse(responseCode = "403", description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "401", description = "해당 사용자가 인증되지 않음 | 토큰 만료")
    })
    public ResponseEntity<Boolean> checkExpiredAt() {
        return ResponseEntity.ok(JwtUtil.isExpired(JwtUtil.getAccessToken()));
    }

    @PostMapping("/refresh")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답"),
            @ApiResponse(responseCode = "404", description = "해당 정보를 가진 Member가 없음"),
            @ApiResponse(responseCode = "403", description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "401", description = "해당 사용자가 인증되지 않음 | 토큰 만료")
    })
    public ResponseEntity<Token> reissueAccessToken(@RequestBody RefreshTokenDto refreshTokenDto){
        return ResponseEntity.ok(tokenService.reissueAccessToken(refreshTokenDto));
    }
}
