package com.planmate.server.service.token;

import com.planmate.server.domain.ExpiredToken;
import com.planmate.server.domain.Member;
import com.planmate.server.domain.Token;
import com.planmate.server.dto.request.token.ReissueTokenRequestDto;
import com.planmate.server.dto.response.token.ReissueTokenResponseDto;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.token.TokenExpiredException;
import com.planmate.server.exception.token.TokenNotFoundException;
import com.planmate.server.repository.ExpiredTokenRepository;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.TokenRepository;
import com.planmate.server.util.JwtUtil;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.planmate.server.config.ModelMapperConfig.modelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    @PersistenceContext
    private EntityManager entityManager;
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final ExpiredTokenRepository expiredTokenRepository;

    /**
     * @author 지승언
     * @param dto (access token, refresh token, member id)
     * 엑세스 토큰을 리프레시 토큰이 아직 유효하다면 갱신. 리프레시 토큰 만료시 예외 응답 (로그아웃)
     * @return 재갱신된 access token, refresh token, member id
     * */
    @Override
    @Transactional
    public ReissueTokenResponseDto reissueAccessToken(ReissueTokenRequestDto dto) {
        Member member = memberRepository.findById(dto.getId()).orElseThrow((() -> new MemberNotFoundException(dto.getId())));

        Token token = tokenRepository.findByAccessTokenAndRefreshToken(member.getMemberId(),dto.getAccessToken(),dto.getRefreshToken())
                .orElseThrow(() -> new TokenNotFoundException(dto.getId()));

        token.updateAccessToken(JwtUtil.generateAccessToken(member));
        token.updateRefreshToken(JwtUtil.getExpiredRefreshToken(member));
        tokenRepository.save(token);

        return modelMapper.map(token, ReissueTokenResponseDto.class);
    }

    /**
     * @author 김호진
     * 관리자 권한을 가지는 토큰을 발급. 프로덕션 환경에서는 제거
     * @param dto
     * @return 재발행한 엑세스/리프레시 토큰,id 반환
     */
    @Override
    @Transactional
    public ReissueTokenResponseDto createAdminToken(ReissueTokenRequestDto dto) {
        Member member = memberRepository.findById(dto.getId())
                .orElseThrow(() -> new MemberNotFoundException(dto.getId()));

        Token token = tokenRepository.findByAccessTokenAndRefreshToken(member.getMemberId(),dto.getAccessToken(),dto.getRefreshToken())
                .orElseThrow(() -> new TokenNotFoundException(dto.getId()));

        token.updateAccessToken(JwtUtil.generateAdminAccessToken(member));
        token.updateRefreshToken(JwtUtil.generateAdminRefreshToken(member));
        tokenRepository.save(token);

        return modelMapper.map(token, ReissueTokenResponseDto.class);
    }

    @Override
    @Transactional
    public void findExpiredToken(String token) throws TokenExpiredException {
        Optional<ExpiredToken> accessToken = expiredTokenRepository.findByAccessToken(token);

        if (accessToken.isPresent()) {
            if (accessToken.get().getAccessTokenExpiredAt().getSecond() <= LocalDateTime.now().getSecond()) {
                expiredTokenRepository.delete(accessToken.get());
                entityManager.flush();
            }

            throw new TokenExpiredException(token);
        }
    }
}
