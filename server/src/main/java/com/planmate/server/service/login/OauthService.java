package com.planmate.server.service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planmate.server.dto.response.login.GoogleLoginResponse;
import com.planmate.server.enums.SocialLoginType;
import com.planmate.server.service.oauth.SocialOauth;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@Generated
@RequiredArgsConstructor
public class OauthService {
    private final List<SocialOauth> socialOauthList;
    private final HttpServletResponse response;

    /**
     * @author 지승언
     * 구글에 로그인 요청 api
     * @param socialLoginType 각 login type에 맞는 url이 호출이 된다
     * */
    public void request(SocialLoginType socialLoginType) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        String redirectURL = socialOauth.getOauthRedirectURL();
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author 지승언
     * @param socialLoginType (github, google, naver, kakao)
     * @param code (auhtentication code)
     * @return 구글 로그인에 결과를 json 형태로 반환한다
     * */
    public GoogleLoginResponse requestAccessToken(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        final String response = socialOauth.requestAccessToken(code);

        return convertStringToResponse(response);
    }

    /**
     * @author 지승언
     * 어떤 sns 로그인 요청인지를 판단
     * */
    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

    /**
     * @author 지승언
     * @param response 구글의 access token 요청 시 반환 되는 값
     * @return 객체 형태로 돌려주기 위해 objectMapper를 사용해 객체로 변환
     * */
    private GoogleLoginResponse convertStringToResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            final GoogleLoginResponse r = objectMapper.readValue(response, GoogleLoginResponse.class);

            return r;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json failed");
        }
    }
}