package com.planmate.server.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planmate.server.domain.Authority;
import com.planmate.server.domain.BannedEmail;
import com.planmate.server.domain.Member;
import com.planmate.server.domain.Token;
import com.planmate.server.dto.request.login.GoogleLoginRequestDto;
import com.planmate.server.dto.response.login.LoginResponseDto;
import com.planmate.server.dto.response.member.MemberResponseDto;
import com.planmate.server.exception.member.MemberBannedException;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.token.TokenNotFoundException;
import com.planmate.server.repository.BannedEmailRepository;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.TokenRepository;
import com.planmate.server.util.JwtUtil;
import com.planmate.server.vo.GoogleIdTokenVo;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.planmate.server.config.ModelMapperConfig.modelMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Generated
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final BannedEmailRepository bannedEmailRepository;

    @Override
    @Transactional
    public Optional<Member> checkDuplicated(String email) {
        return memberRepository.findByEmail(email);
    }

    /**
     * @author 지승언
     * @param id
     * @return id와 일치하는 Member 객체를 반환한다.
     * */
    @Override
    @Transactional
    public Member findMemberById(final Long id) throws MemberNotFoundException {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Member checkMember(Long id) throws MemberBannedException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));

        Optional<BannedEmail> bannedEmail = bannedEmailRepository.findByEmail(member.getEMail());

        if (bannedEmail.isPresent()) {
            throw new MemberBannedException(member.getEMail());
        }

        return member;
    }

    /**
     * @author 김호진
     * sns login시 받은 id token을 사용해 사용자를 DB에 저장한다
     * @body Google에서 전달받은 유저 정보 dto 객체
     * @return 저장된 member 객체
     * */
    @Override
    @Transactional
    public LoginResponseDto signUp(GoogleLoginRequestDto requestDto) {
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .profile(requestDto.getPicture())
                .memberName(requestDto.getName())
                .eMail(requestDto.getEmail())
                .authorities(Arrays.asList(authority))
                .loginType(0L)
                .build();

        memberRepository.save(member);

        Token token = Token.builder()
                .tokenId(member.getMemberId())
                .accessToken(JwtUtil.generateAccessToken(member))
                .accessTokenExpiredAt(LocalDateTime.now().plusDays(JwtUtil.ACCESS_DURATION_DAYS))
                .refreshToken(JwtUtil.generateRefreshToken(member))
                .refreshTokenExpiredAt(LocalDateTime.now().plusDays(JwtUtil.REFRESH_DURATION_DAYS))
                .build();

        tokenRepository.save(token);

        return LoginResponseDto.of(member,token);
    }

    @Override
    @Transactional
    public LoginResponseDto signIn(Member member) {
        Token token = tokenRepository.findByTokenId(member.getMemberId())
                .orElseThrow(() -> new TokenNotFoundException(member.getMemberId()));

        token.updateAccessToken(JwtUtil.generateAccessToken(member));
        token.updateRefreshToken(JwtUtil.generateRefreshToken(member));
        tokenRepository.save(token);

        return LoginResponseDto.of(member,token);
    }

    @Override
    @Transactional
    public List<Authority> getAuthorities() {
        Long id = JwtUtil.getUserIdByAccessToken();

        return memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException(id)
        ).getAuthorities();
    }

    @Override
    @Transactional
    public MemberResponseDto getInfo() {
        Long id = JwtUtil.getUserIdByAccessToken();

        Member member =  memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException(id)
        );

        return modelMapper.map(member, MemberResponseDto.class);
    }

    @Override
    @Transactional
    public MemberResponseDto getInfo(final Long id) {
        Member member =  memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException(id)
        );

        return modelMapper.map(member, MemberResponseDto.class);
    }

    @Override
    @Transactional
    public void signOut() {
        final Long memberId = JwtUtil.getUserIdByAccessToken();
        memberRepository.deleteById(memberId);
        tokenRepository.deleteByTokenId(memberId);
    }
    
    @Override
    @Transactional
    public MemberResponseDto modifyName(final String name) {
        Long id = JwtUtil.getUserIdByAccessToken();

        Member member = memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException(id)
        );

        member.updateMemberName(name);

        return modelMapper.map(member, MemberResponseDto.class);
    }

    @Override
    @Transactional
    public MemberResponseDto modifyImg(final String img) {
        Long id = JwtUtil.getUserIdByAccessToken();

        Member member = memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException(id)
        );

        member.updateProfile(img);

        return modelMapper.map(member, MemberResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto getUserInfo(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));

        Token token = tokenRepository.findByTokenId(member.getMemberId())
                .orElseThrow(() -> new TokenNotFoundException(member.getMemberId()));

        return LoginResponseDto.of(member,token);
    }
}
