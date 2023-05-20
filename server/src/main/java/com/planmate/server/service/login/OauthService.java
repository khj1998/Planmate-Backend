package com.planmate.server.service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planmate.server.dto.response.login.GoogleLoginResponse;
import com.planmate.server.enums.SocialLoginType;
import com.planmate.server.service.oauth.SocialOauth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {
    private final List<SocialOauth> socialOauthList;
    private final HttpServletResponse response;

    public void request(SocialLoginType socialLoginType) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        String redirectURL = socialOauth.getOauthRedirectURL();
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GoogleLoginResponse requestAccessToken(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        final String response = socialOauth.requestAccessToken(code);

        return convertStringToResponse(response);
    }

    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

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