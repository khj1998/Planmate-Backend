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
    Optional<Member> findMemberById(Long id);
    LoginResponseDto signUp(String idToken);
    LoginResponseDto signIn(Member member);
    List<Authority> getAuthorities();
    Member getInfo();
    Member getInfo(Long id);
    void signOut();
    Member modifyName(String name);
    Member modifyImg(String img);
    LoginResponseDto getUserInfo(Long id);
}
