package com.planmate.server.dto.response.login;

import com.planmate.server.domain.Member;
import com.planmate.server.domain.Token;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {
    private Long id;
    private String name;
    private String img;
    private String email;
    private String accessToken;
    private String refreshToken;

    public static LoginResponseDto of(Member member, Token token) {
        return LoginResponseDto.builder()
                .id(member.getMemberId())
                .name(member.getMemberName())
                .img(member.getProfile())
                .email(member.getEMail())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}