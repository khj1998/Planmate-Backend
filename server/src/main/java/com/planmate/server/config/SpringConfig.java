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
