package com.planmate.server.service.schedule;

import com.planmate.server.domain.Schedule;
import com.planmate.server.dto.request.schedule.AddScheduleRequestDto;
import com.planmate.server.dto.request.schedule.ScheduleEditRequestDto;
import com.planmate.server.dto.response.schedule.ScheduleResponseDto;
import com.planmate.server.exception.schedule.MemberScheduleNotFoundException;
import com.planmate.server.exception.schedule.ScheduleNotFoundException;
import com.planmate.server.repository.ScheduleRepository;
import com.planmate.server.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public ScheduleResponseDto addDDay(final AddScheduleRequestDto addScheduleRequestDto) {
        return ScheduleResponseDto.of(scheduleRepository.save(
            Schedule.builder()
                    .title(addScheduleRequestDto.getTitle())
                    .memberId(JwtUtil.getUserIdByAccessToken())
                    .targetDate(LocalDate.parse(addScheduleRequestDto.getTargetDate(), DateTimeFormatter.ISO_DATE))
                    .isFixed(false)
                    .build()
        ));
    }

    @Override
    @Transactional
    public void deleteDDay(final Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new ScheduleNotFoundException(id)
        );

        scheduleRepository.delete(schedule);
    }

    @Override
    @Transactional
    public ScheduleResponseDto modifySchedule(final ScheduleEditRequestDto editRequestDto) {
        Schedule schedule = scheduleRepository.findById(editRequestDto.getScheduleId()).orElseThrow(
                () -> new ScheduleNotFoundException(editRequestDto.getScheduleId())
        );

        schedule.updateTitle(editRequestDto.getTitle());
        schedule.updateTargetDate(LocalDate.parse(editRequestDto.getDate(), DateTimeFormatter.ISO_DATE));

        return ScheduleResponseDto.of(scheduleRepository.save(schedule));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> findAll() {
        List<ScheduleResponseDto> responseDtoList = new ArrayList<>();
        List<Schedule> scheduleList = scheduleRepository.findAllByMemberId(JwtUtil.getUserIdByAccessToken()).orElseThrow(
                () -> new MemberScheduleNotFoundException(JwtUtil.getUserIdByAccessToken())
        );

        for (Schedule schedule : scheduleList) {
            ScheduleResponseDto responseDto = ScheduleResponseDto.of(schedule);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponseDto findMin() {
        return ScheduleResponseDto.of(scheduleRepository.findMinSchedule());
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponseDto findFixedDDay() {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Optional<Schedule> schedule = scheduleRepository.findFixedSchedule(memberId);

        if (schedule.isEmpty()) {
            return null;
        }

        return ScheduleResponseDto.of(schedule.get(),schedule.get().getIsFixed());
    }

    @Override
    @Transactional
    public void fixDDay(Long id) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Optional<Schedule> schedule = scheduleRepository.findFixedSchedule(memberId);

        if (schedule.isPresent()) {
            Schedule s = schedule.get();
            s.updateIsFixed(false);
            scheduleRepository.save(s);
        }

        Schedule fixedSchedule = scheduleRepository.findByUserIdAndId(memberId,id)
                .orElseThrow(() -> new ScheduleNotFoundException(id));
        fixedSchedule.updateIsFixed(true);

        scheduleRepository.save(fixedSchedule);
    }
}
