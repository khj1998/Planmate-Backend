package com.planmate.server.repository;

import com.planmate.server.domain.StudyBackUp;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudyBackUpRepository extends JpaRepository<StudyBackUp,Long> {

    @Query("select ds from StudyBackUp ds " +
            "where ds.studyDate = :studyDate " +
            "and ds.memberId = :memberId")
    List<StudyBackUp> findStudyBackUp(@Param("studyDate")LocalDate studyDate, @Param("memberId") Long memberId, Pageable pageable);

    @Query("select ds from StudyBackUp ds " +
            "where ds.memberId = :memberId and " +
            "YEAR(ds.studyDate) = :year and MONTH(ds.studyDate) = :month")
    List<StudyBackUp> findMonthlyStudyBackUp(@Param("year") Integer year,@Param("month") Integer month
            , @Param("memberId") Long memberId);
}
