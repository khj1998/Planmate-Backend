package com.planmate.server.repository;

import com.planmate.server.domain.Subject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long> {

    List<Subject> findByMemberMemberId(Long memberId);

    @Query("select s from Subject s where s.member.memberId = :memberId and "
          +"s.name = :name")
    Optional<Subject> findSubject(@Param("memberId") Long memberId, @Param("name") String name);

    @Query("select s from Subject s where s.member.memberId = :memberId and "+
            "s.id = :subjectId")
    Optional<Subject> findSubject(@Param("memberId") Long memberId, @Param("subjectId") Long subjectId);

    @Query("select s from Subject s where s.member.memberId = :memberId")
    List<Subject> findAllSubject(@Param("memberId") Long memberId, Pageable pageable);
}
