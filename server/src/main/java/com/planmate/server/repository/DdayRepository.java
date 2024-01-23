package com.planmate.server.repository;

import com.planmate.server.domain.Dday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DdayRepository extends JpaRepository<Dday, Long> {
    Optional<List<Dday>> findAllByMemberMemberId(Long id);
    @Query(value = "select d from Dday d where d.targetDate =  (select min(dday.targetDate) from Dday dday)")
    Dday findMinSchedule();

    @Query("select d from Dday d where d.member.memberId = :memberId and "+
            "d.isFixed = true")
    Optional<Dday> findFixedSchedule(@Param("memberId") Long memberId);

    @Query("select d from Dday d where d.member.memberId = :memberId and d.dDayId = :id")
    Optional<Dday> findByUserIdAndId(@Param("memberId") Long memberId, @Param("id") Long id);
}
