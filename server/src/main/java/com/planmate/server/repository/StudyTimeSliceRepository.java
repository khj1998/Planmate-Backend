package com.planmate.server.repository;

import com.planmate.server.domain.StudyTimeSlice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyTimeSliceRepository extends JpaRepository<StudyTimeSlice,Long> {

    @Query("select s from StudyTimeSlice s " +
            "where (s.member.memberId = :memberId " +
            "and s.createdAt = :yesterdayDate) " +
            "or (s.member.memberId = :memberId and s.createdAt = :todayDate and s.hour = 24)")
    List<StudyTimeSlice> findYesterdayTimeSlice(@Param("memberId") Long memberId
            , @Param("yesterdayDate") LocalDate yesterdayDate, @Param("todayDate") LocalDate todayDate);

    @Query("select s from StudyTimeSlice s " +
            "where s.member.memberId = :memberId " +
            "and s.createdAt = :todayDate and s.hour != 24")
    List<StudyTimeSlice> findTodayTimeSlice(@Param("memberId") Long memberId,@Param("todayDate") LocalDate todayDate);
}
