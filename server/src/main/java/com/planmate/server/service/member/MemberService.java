package com.planmate.server.service.member;

import com.planmate.server.domain.Authority;
import com.planmate.server.domain.Member;
import com.planmate.server.dto.response.login.LoginResponseDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public interface MemberService {
    Optional<Member> checkDuplicated(String email);
    public Optional<Member> findMemberById(Long id);
    Member signUp(String idToken);
    void signIn(HttpServletResponse response,Member member);
    public LoginResponseDto registerMember(HttpServletResponse response,Member member);
    public List<Authority> getAuthorities();
    public Member getInfo();
    public Member getInfo(Long id);
    public void signOut();
    public Member modifyName(String name);
    public Member modifyImg(String img);
    LoginResponseDto getUserInfo(HttpServletRequest request);
}
