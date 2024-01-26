package com.planmate.server.service.planner;

import com.planmate.server.domain.Member;
import com.planmate.server.domain.Planner;
import com.planmate.server.dto.request.planner.PlannerRequestDto;
import com.planmate.server.dto.response.planner.PlannerResponseDto;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.planner.PlannerNotFoundException;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.MemberScrapRepository;
import com.planmate.server.repository.PlannerRepository;
import com.planmate.server.util.JwtUtil;
import static com.planmate.server.config.ModelMapperConfig.modelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlannerServiceImpl implements PlannerService {
    private final MemberScrapRepository memberScrapRepository;
    private final PlannerRepository plannerRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PlannerResponseDto> findPlan() {
        List<PlannerResponseDto> responseDtoList = new ArrayList<>();
        Long memberId = JwtUtil.getUserIdByAccessToken();
        List<Planner> plannerList = plannerRepository.findAllPlanner(memberId);

        for (Planner planner : plannerList) {
            PlannerResponseDto responseDto = PlannerResponseDto.of(planner);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Override
    @Transactional
    public PlannerResponseDto createPlan(PlannerRequestDto plannerRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Planner planner = Planner.of(plannerRequestDto,member);

        return modelMapper.map(plannerRepository.save(planner), PlannerResponseDto.class);
    }

    @Override
    @Transactional
    public PlannerResponseDto editPlan(PlannerRequestDto plannerRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Planner planner = plannerRepository.findPlanner(memberId,plannerRequestDto.getPlannerId())
                .orElseThrow(() -> new PlannerNotFoundException(plannerRequestDto.getPlannerId()));
        planner.updatePlanner(plannerRequestDto);

        return modelMapper.map(plannerRepository.save(planner), PlannerResponseDto.class);
    }

    @Override
    @Transactional
    public void deletePlan(Long plannerId) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Planner planner = plannerRepository.findPlanner(memberId,plannerId)
                .orElseThrow(() -> new PlannerNotFoundException(plannerId));
        plannerRepository.delete(planner);
    }
}
