package com.planmate.server.repository;

import com.planmate.server.domain.BannedEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BannedEmailRepository extends JpaRepository<BannedEmail,Long> {
    @Query("select be from BannedEmail be where be.eMail = :eMail")
    Optional<BannedEmail> findByEmail(@Param("eMail") String eMail);
}
