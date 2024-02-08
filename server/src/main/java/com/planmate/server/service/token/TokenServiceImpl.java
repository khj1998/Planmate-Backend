package com.planmate.server.service.token;

import com.planmate.server.domain.Member;
import com.planmate.server.domain.Token;
import com.planmate.server.dto.request.token.ReissueTokenRequestDto;
import com.planmate.server.dto.response.token.ReissueTokenResponseDto;
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
import static com.planmate.server.config.ModelMapperConfig.modelMapper;

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
     * @param dto (access token, refresh token, member id)
     * @return 재갱신된 access token, refresh token, member id
     * */
    @Override
    @Transactional
    public ReissueTokenResponseDto reissueAccessToken(ReissueTokenRequestDto dto) {
        Member member = memberRepository.findById(dto.getId()).orElseThrow((() -> new MemberNotFoundException(dto.getId())));

        Token token = tokenRepository.findByAccessTokenAndRefreshToken(member.getMemberId(),dto.getAccessToken(),dto.getRefreshToken())
                .orElseThrow(() -> new TokenNotFoundException(dto.getId()));

        token.updateAccessToken(JwtUtil.generateAccessToken(member));
        token.updateAccessTokenExpiredAt(LocalDate.now().plusDays(JwtUtil.ACCESS_DURATION_DAYS));

        token.updateRefreshToken(JwtUtil.getExpiredRefreshToken(member));
        token.updateRefreshTokenExpiredAt(LocalDate.now().plusDays(JwtUtil.REFRESH_DURATION_DAYS));
        tokenRepository.save(token);

        return modelMapper.map(token, ReissueTokenResponseDto.class);
    }

    @Override
    @Transactional
    public ReissueTokenResponseDto reissueTokenByAdmin(ReissueTokenRequestDto dto) {
        Member member = memberRepository.findById(dto.getId())
                .orElseThrow((() -> new MemberNotFoundException(dto.getId())));

        Token token = tokenRepository.findByAccessTokenAndRefreshToken(member.getMemberId(),dto.getAccessToken(),dto.getRefreshToken())
                .orElseThrow(() -> new TokenNotFoundException(dto.getId()));

        token.updateAccessToken(JwtUtil.generateAccessToken(member));
        token.updateAccessTokenExpiredAt(LocalDate.now().plusDays(JwtUtil.ACCESS_DURATION_DAYS));

        token.updateRefreshToken(JwtUtil.getExpiredRefreshToken(member));
        token.updateRefreshTokenExpiredAt(LocalDate.now().plusDays(JwtUtil.REFRESH_DURATION_DAYS));
        tokenRepository.save(token);

        return modelMapper.map(token, ReissueTokenResponseDto.class);
    }

    @Override
    @Transactional
    public ReissueTokenResponseDto createAdminToken(ReissueTokenRequestDto dto) {
        Member member = memberRepository.findById(dto.getId())
                .orElseThrow(() -> new MemberNotFoundException(dto.getId()));

        Token token = tokenRepository.findByAccessTokenAndRefreshToken(member.getMemberId(),dto.getAccessToken(),dto.getRefreshToken())
                .orElseThrow(() -> new TokenNotFoundException(dto.getId()));

        token.updateAccessToken(JwtUtil.generateAdminAccessToken(member));
        token.updateAccessTokenExpiredAt(LocalDate.now().plusDays(JwtUtil.ACCESS_DURATION_DAYS));

        token.updateRefreshToken(JwtUtil.generateAdminRefreshToken(member));
        token.updateRefreshTokenExpiredAt(LocalDate.now().plusDays(JwtUtil.REFRESH_DURATION_DAYS));
        tokenRepository.save(token);

        return modelMapper.map(token, ReissueTokenResponseDto.class);
    }
}
