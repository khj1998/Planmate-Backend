package com.planmate.server.repository;

import com.planmate.server.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<List<Schedule>> findAllByMemberId(Long id);
    @Query(value = "select s from Schedule s where s.targetDate =  (select min(schedule.targetDate) from Schedule schedule)")
    Schedule findMinSchedule();

    @Query("select s from Schedule s where s.memberId = :memberId and "+
            "s.isFixed = true")
    Optional<Schedule> findFixedSchedule(@Param("memberId") Long memberId);

    @Query("select s from Schedule s where s.memberId = :memberId and s.id = :id")
    Optional<Schedule> findByUserIdAndId(@Param("memberId") Long memberId, @Param("id") Long id);
}
