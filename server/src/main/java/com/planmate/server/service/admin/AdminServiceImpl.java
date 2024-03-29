package com.planmate.server.service.admin;

import com.planmate.server.domain.BannedEmail;
import com.planmate.server.domain.ExpiredToken;
import com.planmate.server.domain.Member;
import com.planmate.server.dto.request.member.MemberBanRequestDto;
import com.planmate.server.dto.request.member.MemberUnBanRequestDto;
import com.planmate.server.dto.response.member.MemberResponseDto;
import com.planmate.server.exception.member.MemberBanNotFoundException;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.repository.BannedEmailRepository;
import com.planmate.server.repository.ExpiredTokenRepository;
import com.planmate.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.planmate.server.config.ModelMapperConfig.modelMapper;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final BannedEmailRepository bannedEmailRepository;
    private final ExpiredTokenRepository expiredTokenRepository;

    @Override
    @Transactional
    public MemberResponseDto banMember(MemberBanRequestDto dto) {
        Member member = memberRepository.findByMemberName(dto.getNickname())
                .orElseThrow(() -> new MemberNotFoundException(dto.getNickname()));

        BannedEmail bannedEmail = BannedEmail.of(member);
        bannedEmailRepository.save(bannedEmail);

        return modelMapper.map(member, MemberResponseDto.class);
    }

    @Override
    @Transactional
    public void removeUserBan(String email) {
        BannedEmail bannedEmail = bannedEmailRepository.findByEmail(email)
                .orElseThrow(() -> new MemberBanNotFoundException(email));

        bannedEmailRepository.delete(bannedEmail);
    }

    @Override
    @Transactional
    public Boolean removeExpiredToken() {
        List<ExpiredToken> tokenList = expiredTokenRepository.findAll();
        List<ExpiredToken> expiredTokenList = new ArrayList<>();

        for (ExpiredToken token : tokenList) {
            if (token.getAccessTokenExpiredAt().isBefore(LocalDateTime.now())) {
                expiredTokenList.add(token);
            }
        }

        expiredTokenRepository.deleteAll(expiredTokenList);
        return true;
    }
}
