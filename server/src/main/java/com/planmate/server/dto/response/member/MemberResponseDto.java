package com.planmate.server.dto.response.member;

import com.planmate.server.domain.Authority;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberResponseDto {
    private Long memberId;
    private String memberName;
    private String profile;
    private String eMail;
    private List<Authority> authorities;
}
