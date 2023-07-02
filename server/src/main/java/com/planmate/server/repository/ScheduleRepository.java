package com.planmate.server.repository;

import com.planmate.server.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    public Optional<List<Schedule>> findAllByMemberId(Long id);
    @Query(value = "select s from Schedule s where s.targetDate =  (select min(schedule.targetDate) from Schedule schedule)")
    public Schedule findMinSchedule();
}
