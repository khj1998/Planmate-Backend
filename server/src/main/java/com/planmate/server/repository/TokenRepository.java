package com.planmate.server.repository;

import com.planmate.server.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("select t from Token t where t.tokenId = :tokenId and t.accessToken  = :accessToken and t.refreshToken = :refreshToken")
    Optional<Token> findByAccessTokenAndRefreshToken(@Param("tokenId") Long tokenId,@Param("accessToken") String accessToken,@Param("refreshToken") String refreshToken);
    Optional<Token> findByTokenId(Long tokenId);
    void deleteByTokenId(Long id);
}
