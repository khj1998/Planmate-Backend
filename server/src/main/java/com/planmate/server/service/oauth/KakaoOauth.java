package com.planmate.server.service.oauth;

public class KakaoOauth implements SocialOauth {

    @Override
    public String getOauthRedirectURL() {
        return null;
    }

    @Override
    public String requestAccessToken(final String code) {
        return null;
    }
}
