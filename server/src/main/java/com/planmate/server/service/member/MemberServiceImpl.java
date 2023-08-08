package com.planmate.server.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planmate.server.domain.Authority;
import com.planmate.server.domain.Member;
import com.planmate.server.domain.Token;
import com.planmate.server.dto.response.login.LoginResponseDto;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.token.TokenNotFoundException;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.TokenRepository;
import com.planmate.server.util.JwtUtil;
import com.planmate.server.vo.GoogleIdTokenVo;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Generated
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    public MemberServiceImpl(final MemberRepository memberRepository, final TokenRepository tokenRepository) {
        this.memberRepository = memberRepository;
        this.tokenRepository = tokenRepository;
    }

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
    public Optional<Member> findMemberById(final Long id) {
        log.info("called member service");
        return memberRepository.findById(id);
    }

    /**
     * @author 지승언
     * sns login시 받은 id token을 사용해 사용자를 DB에 저장한다
     * @param idToken id_token
     * @return 저장된 member 객체
     * */
    @Override
    @Transactional
    public Member signUp(final String idToken) {
        final GoogleIdTokenVo googleIdTokenVo = convertToGoogleIdTokenVo(decryptIdToken(idToken.split("\\.")[1]));

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        final Member build = Member.builder()
                .profile(googleIdTokenVo.getPicture())
                .memberName(googleIdTokenVo.getName())
                .eMail(googleIdTokenVo.getEmail())
                .authorities(Arrays.asList(authority))
                .loginType(0L)
                .build();

        memberRepository.save(build);

        return build;
    }

    @Override
    @Transactional
    public void signIn(HttpServletResponse response,Member member) {
        Token token = tokenRepository.findByMemberId(member.getMemberId())
                .orElseThrow(() -> new TokenNotFoundException(member.getMemberId()));

        token.setAccessToken(JwtUtil.createJwt(member));
        token.setRefreshToken(JwtUtil.createRefreshToken());
        tokenRepository.save(token);

        setHttpOnlyCookie(response,token);
    }

    /**
     * @author 지승언
     * token 정보를 저장한다
     * @param member 회원가입한 member
     * @return 회원가입한 멤버에 대한 access token, refresh token 각 토큰의 만료일, member info를 반환
     * */
    @Override
    @Transactional
    public LoginResponseDto registerMember(HttpServletResponse response,Member member) {
        Token token = Token.builder()
                .memberId(member.getMemberId())
                .accessToken(JwtUtil.createJwt(member))
                .accessTokenExpiredAt(LocalDate.now().plusDays(JwtUtil.ACCESS_TOKEN_EXPIRE_TIME))
                .refreshToken(JwtUtil.createRefreshToken())
                .refreshTokenExpiredAt(LocalDate.now().plusYears(1))
                .build();

        tokenRepository.save(token);

        setHttpOnlyCookie(response,token);

        return LoginResponseDto.of(member, token);
    }

    @Override
    @Transactional
    public List<Authority> getAuthorities() {
        log.info("called");
        return memberRepository.findById(JwtUtil.getMemberId()).orElseThrow(
                () -> new MemberNotFoundException(JwtUtil.getMemberId())
        ).getAuthorities();
    }

    @Override
    @Transactional
    public Member getInfo() {
        return memberRepository.findById(JwtUtil.getMemberId()).orElseThrow(
                () -> new MemberNotFoundException(JwtUtil.getMemberId())
        );
    }

    @Override
    @Transactional
    public Member getInfo(final Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException(JwtUtil.getMemberId())
        );
    }

    @Override
    @Transactional
    public void signOut() {
        final Long memberId = JwtUtil.getMemberId();
        memberRepository.deleteById(memberId);
        tokenRepository.deleteByMemberId(memberId);
    }

    /**
     * TODO: query annotation 써서 alter
     * */
    @Override
    @Transactional
    public Member modifyName(final String name) {
        Member member = memberRepository.findById(JwtUtil.getMemberId()).orElseThrow(
                () -> new MemberNotFoundException(JwtUtil.getMemberId())
        );

        member.setMemberName(name);

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member modifyImg(final String img) {
        Member member = memberRepository.findById(JwtUtil.getMemberId()).orElseThrow(
                () -> new MemberNotFoundException(JwtUtil.getMemberId())
        );

        member.setProfile(img);

        return memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto getUserInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;

        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals("access_token")) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        Token token = tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenNotFoundException());

        Member member = memberRepository.findById(token.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(token.getMemberId()));

        return LoginResponseDto.of(member,token);
    }

    /**
     * @author 지승언
     * sns login에서 받은 Id token을 한글로 인코딩하는 함수
     * */
    private String decryptIdToken(String idToken) {
        byte[] decode = Base64.decodeBase64(idToken);

        return new String(decode, StandardCharsets.UTF_8);
    }

    /**
     * @author  지승언
     * id token을 원하는 객체 형태로 변환 하는 함
     * */
    private GoogleIdTokenVo convertToGoogleIdTokenVo(String idToken) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(idToken, GoogleIdTokenVo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json failed");
        }
    }

    private void setHttpOnlyCookie(HttpServletResponse response,Token token) {
        Cookie accessCookie = new Cookie("access_token",token.getAccessToken());
        accessCookie.setMaxAge(1800);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false);
        accessCookie.setPath("/"); //쿠키가 브라우저에 허용되는 URL 설정

        Cookie refreshCookie = new Cookie("refresh_token",token.getRefreshToken());
        refreshCookie.setMaxAge(1800);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }
}
