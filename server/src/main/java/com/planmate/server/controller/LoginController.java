package com.planmate.server.controller;

import com.planmate.server.domain.Member;
import com.planmate.server.dto.request.login.GoogleLoginRequestDto;
import com.planmate.server.dto.response.login.GoogleLoginResponse;
import com.planmate.server.dto.response.login.LoginResponseDto;
import com.planmate.server.enums.SocialLoginType;
import com.planmate.server.service.login.OauthService;
import com.planmate.server.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000",allowedHeaders = {"Content-Type"})
@RequiredArgsConstructor
@RequestMapping(value = "/login")
@Slf4j
public class LoginController {
    private final OauthService oauthService;
    private final MemberService memberService;

    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @body accessToken (GOOGLE)
     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
     */
    @PostMapping(value = "/{socialLoginType}/token")
    @ApiOperation("회원가입 & token 발급 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답")
    })
    public LoginResponseDto callback(@RequestBody GoogleLoginRequestDto requestDto) throws IOException {
        LoginResponseDto loginResponseDto;
        String email = requestDto.getEmail();

        Optional<Member> member = memberService.checkDuplicated(email);

        if (member.isPresent()) {
            loginResponseDto = memberService.signIn(member.get());
        }   else {
            loginResponseDto = memberService.signUp(requestDto);
        }

        return loginResponseDto;
    }
}