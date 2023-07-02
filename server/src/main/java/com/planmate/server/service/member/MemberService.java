package com.planmate.server.service.member;

import com.planmate.server.domain.Authority;
import com.planmate.server.domain.Member;
import com.planmate.server.dto.response.login.LoginResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    public Optional<Member> findMemberById(Long id);
    public Member signUp(String idToken);
    public LoginResponseDto registerMember(Member member);
    public List<Authority> getAuthorities();
    public Member getInfo();
    public Member getInfo(Long id);
    public void signOut();
    public Member modifyName(String name);
    public Member modifyImg(String img);
}
