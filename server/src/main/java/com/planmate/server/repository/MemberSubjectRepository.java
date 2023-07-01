package com.planmate.server.repository;

import com.planmate.server.domain.MemberSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberSubjectRepository extends JpaRepository<MemberSubject,Long> {
    @Query("select ms from MemberSubject ms where ms.memberId = :memberId and "+"ms.subjectId = :subjectId")
    Optional<MemberSubject> findMemberSubject(@Param("memberId") Long memberId,@Param("subjectId") Long subjectId);
}
