package com.planmate.server.repository;

import com.planmate.server.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    public Optional<Token> findByAccessTokenAndRefreshToken(String accessToken, String refreshToken);
}
