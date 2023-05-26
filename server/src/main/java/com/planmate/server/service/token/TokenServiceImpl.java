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

import java.time.LocalDate;

@Slf4j
public class TokenServiceImpl implements TokenService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    public TokenServiceImpl(final TokenRepository tokenRepository, final MemberRepository memberRepository) {
        this.tokenRepository = tokenRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * @author 지승언
     * @param refreshTokenDto (access token, refresh token, member id)
     * @return 재갱신된 access token, refresh token, member id
     * */
    @Override
    public Token reissueAccessToken(RefreshTokenDto refreshTokenDto) {
        Member member = memberRepository.findById(refreshTokenDto.getId()).orElseThrow((() -> new MemberNotFoundException(refreshTokenDto.getId())));

        Token token = tokenRepository.findById(refreshTokenDto.getId()).orElseThrow(() -> new MemberNotFoundException(refreshTokenDto.getId()));

        tokenRepository.findByAccessTokenAndRefreshToken(refreshTokenDto.getAccessToken(), refreshTokenDto.getRefreshToken()).orElseThrow(() -> new TokenNotFoundException(refreshTokenDto.getId()));

        token.setAccessToken(JwtUtil.createJwt(member));
        token.setAccessTokenExpiredAt(LocalDate.now().plusDays(JwtUtil.ACCESS_TOKEN_EXPIRE_TIME));

        return tokenRepository.save(token);
    }
}
