package com.planmate.server.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planmate.server.domain.Authority;
import com.planmate.server.domain.Member;
import com.planmate.server.domain.Token;
import com.planmate.server.dto.response.login.LoginResponseDto;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.TokenRepository;
import com.planmate.server.util.JwtUtil;
import com.planmate.server.vo.GoogleIdTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    public MemberServiceImpl(final MemberRepository memberRepository, final TokenRepository tokenRepository) {
        this.memberRepository = memberRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Optional<Member> findMemberById(final Long id) {
        log.info("called member service");
        return memberRepository.findById(id);
    }

    @Override
    public Member signUp(final String idToken) {
        final GoogleIdTokenVo googleIdTokenVo = convertToGoogleIdTokenVo(decryptIdToken(idToken.split("\\.")[1]));

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        return Member.builder()
                .profile(googleIdTokenVo.getPicture())
                .memberName(googleIdTokenVo.getName())
                .eMail(googleIdTokenVo.getEmail())
                .authorities(Arrays.asList(authority))
                .loginType(0L)
                .build();
    }

    @Override
    public LoginResponseDto registerMember(Member member) {
        Token token = Token.builder()
                .memberId(member.getMemberId())
                .accessToken(JwtUtil.createJwt(member))
                .accessTokenExpiredAt(LocalDateTime.now().plusDays(JwtUtil.ACCESS_TOKEN_EXPIRE_TIME))
                .refreshToken(JwtUtil.createRefreshToken())
                .refreshTokenExpiredAt(LocalDateTime.now().plusYears(1))
                .build();

        memberRepository.save(member);

        tokenRepository.save(token);

        return LoginResponseDto.of(member, token);
    }

    private String decryptIdToken(String idToken) {
        byte[] decode = Base64.decodeBase64(idToken);

        return new String(decode, StandardCharsets.UTF_8);
    }

    private GoogleIdTokenVo convertToGoogleIdTokenVo(String idToken) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(idToken, GoogleIdTokenVo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json failed");
        }
    }
}
