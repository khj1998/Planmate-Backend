package com.planmate.server.repository;

import com.planmate.server.domain.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlannerRepository extends JpaRepository<Planner,Long> {
    @Query("select p from Planner p where p.memberId = :memberId and "
          +"p.plannerId = :plannerId")
    Optional<Planner> findPlanner(@Param("memberId") Long memberId,@Param("plannerId") Long plannerId);

    @Query("select p from Planner p where p.memberId = :memberId")
    List<Planner> findAllPlanner(@Param("memberId") Long memberId);
}
