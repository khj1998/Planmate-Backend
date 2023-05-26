package com.planmate.server.config;

import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.TokenRepository;
import com.planmate.server.service.member.MemberService;
import com.planmate.server.service.member.MemberServiceImpl;
import com.planmate.server.service.token.TokenService;
import com.planmate.server.service.token.TokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 지승언
 * service 로직에 대한 bean을 직접 등록하는 파일이다
 * 직접 등록하는 이유는 나중에 서비스 교체할 일이 있을 경우와 유지보수를 위해서이다
 * */
@Configuration
public class SpringConfig {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    public SpringConfig(final MemberRepository memberRepository,
                        final TokenRepository tokenRepository) {
        this.memberRepository = memberRepository;
        this.tokenRepository = tokenRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository, tokenRepository);
    }

    @Bean
    public TokenService tokenService() {
        return new TokenServiceImpl(tokenRepository, memberRepository);
    }
}