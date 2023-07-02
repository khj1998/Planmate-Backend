package com.planmate.server.service.logout;

import com.planmate.server.domain.Member;
import com.planmate.server.domain.Token;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.token.TokenNotFoundException;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.TokenRepository;
import com.planmate.server.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@RequiredArgsConstructor
@Slf4j
public class LogOutServiceImpl implements LogOutService{
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public String logout() {
        Member member = memberRepository.findById(JwtUtil.getMemberId()).orElseThrow(() -> new MemberNotFoundException(JwtUtil.getMemberId()));

        Token token = tokenRepository.findByMemberId(JwtUtil.getMemberId()).orElseThrow(
                () -> new TokenNotFoundException(JwtUtil.getMemberId())
        );

        token.setAccessToken(JwtUtil.logout(member));
        token.setAccessTokenExpiredAt(LocalDate.now().minusDays(2));

        return tokenRepository.save(token).getAccessToken();
    }
}
