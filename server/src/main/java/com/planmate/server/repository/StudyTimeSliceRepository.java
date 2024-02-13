package com.planmate.server.repository;

import com.planmate.server.domain.StudyTimeSlice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyTimeSliceRepository extends JpaRepository<StudyTimeSlice,Long> {

    @Query("select s from StudyTimeSlice where s.hour <= hour and s.createdAt = :yesterdayDate")
    Optional<StudyTimeSlice> findYesterdayTimeSlice(@Param("hour") Integer hour, @Param("yesterdayDate") LocalDate yesterdayDate);
}
