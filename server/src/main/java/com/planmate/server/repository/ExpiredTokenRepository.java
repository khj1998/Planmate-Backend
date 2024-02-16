package com.planmate.server.repository;

import com.planmate.server.domain.ExpiredToken;
import com.planmate.server.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpiredTokenRepository extends JpaRepository<ExpiredToken,Long> {
    Optional<ExpiredToken> findByAccessToken(String accessToken);
}
