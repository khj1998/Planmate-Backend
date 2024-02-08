package com.planmate.server.controller;

import com.planmate.server.dto.request.token.ReissueTokenRequestDto;
import com.planmate.server.dto.response.token.ReissueTokenResponseDto;
import com.planmate.server.service.token.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 지승언
 * */

@CrossOrigin
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
@Api(tags = {"토큰 관련 API"})
public class TokenController {
    private final TokenService tokenService;

    /**
     * access token 재발급 API
     * @param reissueTokenRequestDto (access_token, refresh_token)
     * @return access_token
     * */
    @PostMapping("/refresh")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답")
    })
    public ResponseEntity<ReissueTokenResponseDto> reissueAccessToken(@RequestBody ReissueTokenRequestDto reissueTokenRequestDto){
        return ResponseEntity.ok(tokenService.reissueAccessToken(reissueTokenRequestDto));
    }

    /**
     * Todo
     * 프로덕션에서는 해당 엔드포인트 제거
     */
    @PostMapping(value = "/admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자 토큰 생성 응답")
    })
    public ResponseEntity<ReissueTokenResponseDto> createAdminToken(@RequestBody ReissueTokenRequestDto dto) {
        return ResponseEntity.ok(tokenService.createAdminToken(dto));
    }
}
