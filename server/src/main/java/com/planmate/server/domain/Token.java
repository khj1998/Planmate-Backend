package com.planmate.server.domain;

import com.planmate.server.util.JwtUtil;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "token")
@ApiModel(value = "token들을 저장하는 테이블")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @Column(name = "token_id", columnDefinition = "int")
    private Long tokenId;

    @Column(name = "access_token", nullable = false,columnDefinition = "mediumtext")
    private String accessToken;

    @Column(name = "access_token_expired_at",nullable = false, columnDefinition = "datetime")
    private LocalDateTime accessTokenExpiredAt;

    @Column(name = "refresh_token",nullable = false, columnDefinition = "mediumtext")
    private String refreshToken;

    @Column(name = "refresh_token_expired_at",nullable = false, columnDefinition = "datetime")
    private LocalDateTime refreshTokenExpiredAt;

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
        this.accessTokenExpiredAt = LocalDateTime.now().plusDays(JwtUtil.ACCESS_DURATION_DAYS);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredAt = LocalDateTime.now().plusDays(JwtUtil.REFRESH_DURATION_DAYS);
    }
}