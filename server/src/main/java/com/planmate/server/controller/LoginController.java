package com.planmate.server.controller;

import com.planmate.server.domain.Member;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/login")
@Slf4j
public class LoginController {
    private final OauthService oauthService;
    private final MemberService memberService;

    /**
     * @author 지승언
     * 사용자로부터 SNS 로그인 요청을 Social Login Type 을 받아 처리
     * @param socialLoginType (GOOGLE, GITHUB, NAVER, KAKAO)
     */
    @GetMapping(value = "/{socialLoginType}")
    @ApiOperation("sns login api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답")
    })
    public void socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
        oauthService.request(socialLoginType);
    }

    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginType (GOOGLE, FACEBOOK, NAVER, KAKAO)
     * @param code API Server 로부터 넘어노는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
     */
    @GetMapping(value = "/{socialLoginType}/token")
    @ApiOperation("회원가입 & token 발급 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답")
    })
    public ResponseEntity<LoginResponseDto> callback(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            @RequestParam(name = "code") String code) {
        GoogleLoginResponse googleLoginResponse = oauthService.requestAccessToken(socialLoginType, code);

        Member member = memberService.signUp(googleLoginResponse.getId_token());

        return ResponseEntity.ok(memberService.registerMember(member));
    }
}