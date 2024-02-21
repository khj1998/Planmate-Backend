package com.planmate.server.service.logout;

import com.planmate.server.domain.ExpiredToken;
import com.planmate.server.domain.Member;
import com.planmate.server.domain.Token;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.token.TokenNotFoundException;
import com.planmate.server.repository.ExpiredTokenRepository;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.TokenRepository;
import com.planmate.server.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogOutServiceImpl implements LogOutService{
    private final TokenRepository tokenRepository;
    private final ExpiredTokenRepository expiredTokenRepository;

    @Override
    @Transactional
    public String logout() {
        String accessToken = JwtUtil.getAccessTokenByRequest();

        Token token = tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenNotFoundException(accessToken));

        if (token.getAccessTokenExpiredAt().isAfter(LocalDateTime.now())) {
            ExpiredToken expiredToken = ExpiredToken.builder()
                    .accessToken(token.getAccessToken())
                    .accessTokenExpiredAt(token.getAccessTokenExpiredAt())
                    .build();

            expiredTokenRepository.save(expiredToken);
        }

        return tokenRepository.save(token).getAccessToken();
    }
}
