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
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogOutServiceImpl implements LogOutService{
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public String logout() {
        Member member = memberRepository.findById(JwtUtil.getUserIdByAccessToken()).orElseThrow(() -> new MemberNotFoundException(JwtUtil.getUserIdByAccessToken()));

        Token token = tokenRepository.findByMemberId(JwtUtil.getUserIdByAccessToken()).orElseThrow(
                () -> new TokenNotFoundException(JwtUtil.getUserIdByAccessToken())
        );

        token.setAccessToken(JwtUtil.logout(member));
        token.setAccessTokenExpiredAt(LocalDate.now().minusDays(JwtUtil.ACCESS_DURATION));

        return tokenRepository.save(token).getAccessToken();
    }
}
