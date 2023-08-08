package com.planmate.server.service.token;

import com.planmate.server.domain.Token;
import com.planmate.server.dto.request.token.RefreshTokenDto;

import java.util.Optional;

public interface TokenService {
    public Token reissueAccessToken(RefreshTokenDto refreshTokenDto);
}
