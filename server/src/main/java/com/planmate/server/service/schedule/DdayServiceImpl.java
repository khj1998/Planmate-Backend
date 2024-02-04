package com.planmate.server.service.schedule;

import com.planmate.server.domain.Member;
import com.planmate.server.domain.Dday;
import com.planmate.server.dto.request.dday.AddDdayRequestDto;
import com.planmate.server.dto.request.dday.DdayEditRequestDto;
import com.planmate.server.dto.response.dday.DdayResponseDto;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.dday.MemberDdayNotFoundException;
import com.planmate.server.exception.dday.DdayNotFoundException;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.DdayRepository;
import com.planmate.server.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DdayServiceImpl implements DdayService {
    private final MemberRepository memberRepository;
    private final DdayRepository ddayRepository;

    @Override
    @Transactional
    public DdayResponseDto addDDay(final AddDdayRequestDto addDdayRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        return DdayResponseDto.of(ddayRepository.save(
            Dday.builder()
                    .title(addDdayRequestDto.getTitle())
                    .member(member)
                    .targetDate(LocalDate.parse(addDdayRequestDto.getTargetDate(), DateTimeFormatter.ISO_DATE))
                    .isFixed(false)
                    .build()
        ));
    }

    @Override
    @Transactional
    public void deleteDDay(final Long id) {
        Dday dday = ddayRepository.findById(id).orElseThrow(
                () -> new DdayNotFoundException(id)
        );

        ddayRepository.delete(dday);
    }

    @Override
    @Transactional
    public DdayResponseDto editSchedule(final DdayEditRequestDto editRequestDto) {
        Dday dday = ddayRepository.findById(editRequestDto.getDDayId()).orElseThrow(
                () -> new DdayNotFoundException(editRequestDto.getDDayId())
        );

        dday.updateTitle(editRequestDto.getTitle());
        dday.updateTargetDate(LocalDate.parse(editRequestDto.getTargetDate(), DateTimeFormatter.ISO_DATE));

        return DdayResponseDto.of(ddayRepository.save(dday));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DdayResponseDto> findAll() {
        List<DdayResponseDto> responseDtoList = new ArrayList<>();
        List<Dday> ddayList = ddayRepository.findAllByMemberMemberId(JwtUtil.getUserIdByAccessToken()).orElseThrow(
                () -> new MemberDdayNotFoundException(JwtUtil.getUserIdByAccessToken())
        );

        for (Dday dday : ddayList) {
            DdayResponseDto responseDto = DdayResponseDto.of(dday);
            responseDtoList.add(responseDto);
        }

        responseDtoList.sort(
                Comparator.comparing(DdayResponseDto::getRemainingDays)
        );

        return responseDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public DdayResponseDto findMin() {
        return DdayResponseDto.of(ddayRepository.findMinSchedule());
    }

    @Override
    @Transactional(readOnly = true)
    public DdayResponseDto findFixedDDay() {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Optional<Dday> schedule = ddayRepository.findFixedSchedule(memberId);

        if (schedule.isEmpty()) {
            return null;
        }

        return DdayResponseDto.of(schedule.get(),schedule.get().getIsFixed());
    }

    @Override
    @Transactional
    public void fixDDay(Long id) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Optional<Dday> schedule = ddayRepository.findFixedSchedule(memberId);

        if (schedule.isPresent()) {
            Dday s = schedule.get();
            s.updateIsFixed(false);
            ddayRepository.save(s);
        }

        Dday fixedDday = ddayRepository.findByUserIdAndId(memberId,id)
                .orElseThrow(() -> new DdayNotFoundException(id));
        fixedDday.updateIsFixed(true);

        ddayRepository.save(fixedDday);
    }
}
