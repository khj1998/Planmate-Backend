package com.planmate.server.service.member;

import com.planmate.server.domain.Member;
import com.planmate.server.dto.response.login.LoginResponseDto;

import java.util.Optional;

public interface MemberService {
    public Optional<Member> findMemberById(Long id);
    public Member signUp(String idToken);
    public LoginResponseDto registerMember(Member member);
}
