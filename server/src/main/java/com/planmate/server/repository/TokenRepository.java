package com.planmate.server.repository;

import com.planmate.server.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("select t from Token t where t.accessToken = :accessToken")
    Optional<Token> findByAccessToken(@Param("accessToken") String accessToken);
    public Optional<Token> findByAccessTokenAndRefreshToken(String accessToken, String refreshToken);
    public Optional<Token> findByMemberId(Long id);
    public void deleteByMemberId(Long id);
}
