package com.planmate.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "expired_token")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpiredToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,columnDefinition = "int")
    private Long id;

    @Column(name = "access_token",nullable = false,columnDefinition = "mediumtext")
    private String accessToken;

    @Column(name = "access_token_expired_at",nullable = false,columnDefinition = "datetime")
    private LocalDateTime accessTokenExpiredAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
