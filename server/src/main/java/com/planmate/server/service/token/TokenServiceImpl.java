package com.planmate.server.service.token;

import com.planmate.server.domain.Member;
import com.planmate.server.domain.Token;
import com.planmate.server.dto.request.token.RefreshTokenDto;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.token.TokenNotFoundException;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.TokenRepository;
import com.planmate.server.util.JwtUtil;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@Generated
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    /**
     * @author 지승언
     * @param refreshTokenDto (access token, refresh token, member id)
     * @return 재갱신된 access token, refresh token, member id
     * */
    @Override
    @Transactional
    public Token reissueAccessToken(RefreshTokenDto refreshTokenDto) {
        Member member = memberRepository.findById(refreshTokenDto.getId()).orElseThrow((() -> new MemberNotFoundException(refreshTokenDto.getId())));

        Token token = tokenRepository.findById(refreshTokenDto.getId()).orElseThrow(() -> new MemberNotFoundException(refreshTokenDto.getId()));

        tokenRepository.findByAccessTokenAndRefreshToken(refreshTokenDto.getAccessToken(), refreshTokenDto.getRefreshToken()).orElseThrow(() -> new TokenNotFoundException(refreshTokenDto.getId()));

        token.updateAccessToken(JwtUtil.generateAccessToken(member));
        token.updateAccessTokenExpiredAt(LocalDate.now().plusDays(JwtUtil.ACCESS_DURATION_DAYS));

        return tokenRepository.save(token);
    }
}
