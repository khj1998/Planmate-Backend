package com.planmate.server.repository;

import com.planmate.server.domain.StudyTimeSlice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyTimeSliceRepository extends JpaRepository<StudyTimeSlice,Long> {
}
