package com.planmate.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.planmate.server.domain.Member;
import com.planmate.server.dto.response.login.GoogleLoginResponse;
import com.planmate.server.dto.response.login.LoginResponseDto;
import com.planmate.server.enums.SocialLoginType;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.service.login.OauthService;
import com.planmate.server.service.member.MemberService;
import com.planmate.server.service.token.TokenService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/login")
@Slf4j
public class LoginController {

    @Value("${frontend.redirect.url}")
    private String redirectURL;
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
    public RedirectView callback(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            @RequestParam(name = "code") String code,
            HttpServletResponse response) throws JsonProcessingException {

        GoogleLoginResponse googleLoginResponse = oauthService.requestAccessToken(socialLoginType, code);
        String email = oauthService.getEmailByIdToken(googleLoginResponse.getId_token());

        Optional<Member> checkedMember = memberService.checkDuplicated(email);

        if (checkedMember.isPresent()) {
            memberService.signIn(response,checkedMember.get());
        }   else {
            Member member = memberService.signUp(googleLoginResponse.getId_token());
            memberService.registerMember(response,member);
        }

        return new RedirectView(redirectURL);
    }
}