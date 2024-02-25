package com.planmate.server.service.admin;

import com.planmate.server.dto.request.member.MemberBanRequestDto;
import com.planmate.server.dto.request.member.MemberUnBanRequestDto;
import com.planmate.server.dto.response.member.MemberResponseDto;

public interface AdminService {
    MemberResponseDto banMember(MemberBanRequestDto dto);
    void removeUserBan(String email);
    Boolean removeExpiredToken();
}
