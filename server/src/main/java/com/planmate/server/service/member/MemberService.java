package com.planmate.server.service.member;

import com.planmate.server.domain.Authority;
import com.planmate.server.domain.Member;
import com.planmate.server.dto.request.login.GoogleLoginRequestDto;
import com.planmate.server.dto.response.login.LoginResponseDto;
import com.planmate.server.dto.response.member.MemberResponseDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public interface MemberService {
    Optional<Member> checkDuplicated(String email);
    Optional<Member> findMemberById(Long id);
    LoginResponseDto signUp(GoogleLoginRequestDto requestDto);
    LoginResponseDto signIn(Member member);
    List<Authority> getAuthorities();
    MemberResponseDto getInfo();
    MemberResponseDto getInfo(Long id);
    void signOut();
    MemberResponseDto modifyName(String name);
    MemberResponseDto modifyImg(String img);
    LoginResponseDto getUserInfo(Long id);
}
