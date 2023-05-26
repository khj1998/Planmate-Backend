package com.planmate.server.dto.request.token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDto {
    private Long id;
    private String accessToken;
    private String refreshToken;
}
