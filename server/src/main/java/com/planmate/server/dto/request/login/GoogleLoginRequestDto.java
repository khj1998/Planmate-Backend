package com.planmate.server.dto.request.login;

import lombok.Getter;

@Getter
public class GoogleLoginRequestDto {
    private String email;
    private String picture;
    private String name;
}
