package com.planmate.server.dto.request.token;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Generated
public class RefreshTokenDto {
    private Long id;
    private String accessToken;
    private String refreshToken;
}
