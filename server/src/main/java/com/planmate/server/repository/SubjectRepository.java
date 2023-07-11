package com.planmate.server.repository;

import com.planmate.server.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long> {

    List<Subject> findByMemberId(Long memberId);

    @Query("select s from Subject s where s.memberId = :memberId and "+
            "s.id = :subjectId")
    Optional<Subject> findSubject(@Param("memberId") Long memberId, @Param("subjectId") Long subjectId);
}
