package com.planmate.server.dto.response.token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReissueTokenResponseDto {
    private Long tokenId;
    private String accessToken;
    private String refreshToken;
}
