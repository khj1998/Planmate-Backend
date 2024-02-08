package com.planmate.server.service.token;

import com.planmate.server.dto.request.token.ReissueTokenRequestDto;
import com.planmate.server.dto.response.token.ReissueTokenResponseDto;

public interface TokenService {
    ReissueTokenResponseDto reissueAccessToken(ReissueTokenRequestDto reissueTokenRequestDto);
    ReissueTokenResponseDto reissueTokenByAdmin(ReissueTokenRequestDto dto);
    ReissueTokenResponseDto createAdminToken(ReissueTokenRequestDto dto);
}
