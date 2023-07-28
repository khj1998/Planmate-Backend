package com.planmate.server.service.planner;

import com.planmate.server.dto.request.planner.PlannerRequestDto;
import com.planmate.server.dto.response.planner.PlannerResponseDto;

import java.util.List;

public interface PlannerService {
    List<PlannerResponseDto> findPlan();
    void createPlan(PlannerRequestDto plannerRequestDto);
    void editPlan(PlannerRequestDto plannerRequestDto);
    void deletePlan(Long plannerId);
}
