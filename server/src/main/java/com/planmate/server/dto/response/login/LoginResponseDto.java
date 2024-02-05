package com.planmate.server.dto.response.login;

import com.planmate.server.domain.Member;
import com.planmate.server.domain.Token;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {
    private Long memberId;
    private String nickname;
    private String profileImage;
    private String email;
    private String accessToken;
    private String refreshToken;

    public static LoginResponseDto of(Member member, Token token) {
        return LoginResponseDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getMemberName())
                .profileImage(member.getProfile())
                .email(member.getEMail())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}