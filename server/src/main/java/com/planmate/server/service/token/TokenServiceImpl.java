package com.planmate.server.service.token;

import com.planmate.server.domain.Member;
import com.planmate.server.domain.Token;
import com.planmate.server.dto.request.token.RefreshTokenDto;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.token.TokenNotFoundException;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.TokenRepository;
import com.planmate.server.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class TokenServiceImpl implements TokenService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    public TokenServiceImpl(final TokenRepository tokenRepository, final MemberRepository memberRepository) {
        this.tokenRepository = tokenRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Token reissueAccessToken(RefreshTokenDto refreshTokenDto) {
        Long memberId = JwtUtil.getMemberId();

        Member member = memberRepository.findById(memberId).orElseThrow((() -> new MemberNotFoundException(memberId)));

        Token token = tokenRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        tokenRepository.findByAccessTokenAndRefreshToken(JwtUtil.getAccessToken(), refreshTokenDto.getRefreshToken()).orElseThrow(() -> new TokenNotFoundException(memberId));

        token.setAccessToken(JwtUtil.createJwt(member));
        token.setAccessTokenExpiredAt(LocalDateTime.now().plusDays(3));

        return tokenRepository.save(token);
    }
}
