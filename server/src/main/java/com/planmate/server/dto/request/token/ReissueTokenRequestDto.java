package com.planmate.server.dto.request.token;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReissueTokenRequestDto {
    private Long id;
    private String accessToken;
    private String refreshToken;
}
