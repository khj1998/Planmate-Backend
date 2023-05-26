package com.planmate.server.domain;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "token")
@ApiModel(value = "token들을 저장하는 테이블")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @Column(name = "member_id", columnDefinition = "int")
    private Long memberId;

    @Column(name = "access_token", columnDefinition = "mediumtext")
    private String accessToken;

    @Column(name = "access_token_expired_at", columnDefinition = "date")
    private LocalDate accessTokenExpiredAt;

    @Column(name = "refresh_token", columnDefinition = "mediumtext")
    private String refreshToken;

    @Column(name = "refresh_token_expired_at", columnDefinition = "date")
    private LocalDate refreshTokenExpiredAt;
}