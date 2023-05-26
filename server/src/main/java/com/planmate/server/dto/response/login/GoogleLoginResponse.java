package com.planmate.server.dto.response.login;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class GoogleLoginResponse {
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;
    private String id_token;
}
