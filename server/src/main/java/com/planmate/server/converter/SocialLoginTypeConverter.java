package com.planmate.server.converter;

import com.planmate.server.enums.SocialLoginType;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class SocialLoginTypeConverter implements Converter<String, SocialLoginType> {
    /**
     * @author 지승언
     * API endpoint를 SocialLoginType 객체로 변환하는 config 파일
     * @param s (google, naver, github, kakao)
     * @return 각 스트링에 해당하는 SocialLoginType 객체
     * */
    @Override
    public SocialLoginType convert(String s) {
        return SocialLoginType.valueOf(s.toUpperCase());
    }
}