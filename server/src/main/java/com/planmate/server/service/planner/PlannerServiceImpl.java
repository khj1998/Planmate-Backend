package com.planmate.server.service.planner;

import com.planmate.server.domain.Planner;
import com.planmate.server.dto.request.planner.PlannerRequestDto;
import com.planmate.server.dto.response.planner.PlannerResponseDto;
import com.planmate.server.exception.planner.PlannerNotFoundException;
import com.planmate.server.repository.PlannerRepository;
import com.planmate.server.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PlannerServiceImpl implements PlannerService {
    private final PlannerRepository plannerRepository;

    public PlannerServiceImpl(PlannerRepository plannerRepository) {
        this.plannerRepository = plannerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlannerResponseDto> findPlan() {
        List<PlannerResponseDto> responseDtoList = new ArrayList<>();
        Long memberId = JwtUtil.getMemberId();
        List<Planner> plannerList = plannerRepository.findAllPlanner(memberId);

        for (Planner planner : plannerList) {
            PlannerResponseDto responseDto = PlannerResponseDto.of(planner);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Override
    @Transactional
    public void createPlan(PlannerRequestDto plannerRequestDto) {
        Long memberId = JwtUtil.getMemberId();
        Planner planner = Planner.of(plannerRequestDto,memberId);
        plannerRepository.save(planner);
    }

    @Override
    @Transactional
    public void editPlan(PlannerRequestDto plannerRequestDto) {}

    @Override
    @Transactional
    public void deletePlan(Long plannerId) {
        Long memberId = JwtUtil.getMemberId();
        Planner planner = plannerRepository.findPlanner(memberId,plannerId)
                .orElseThrow(() -> new PlannerNotFoundException(plannerId));
        plannerRepository.delete(planner);
    }
}
