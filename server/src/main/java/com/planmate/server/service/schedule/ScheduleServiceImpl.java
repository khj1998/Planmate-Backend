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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public ScheduleResponseDto addDDay(final AddScheduleRequestDto addScheduleRequestDto) {
        return ScheduleResponseDto.of(scheduleRepository.save(
            Schedule.builder()
                    .title(addScheduleRequestDto.getTitle())
                    .memberId(JwtUtil.getMemberId())
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
        Schedule schedule = scheduleRepository.findById(editRequestDto.getId()).orElseThrow(
                () -> new ScheduleNotFoundException(editRequestDto.getId())
        );

        schedule.setTitle(editRequestDto.getTitle());
        schedule.setTargetDate(LocalDate.parse(editRequestDto.getDate(), DateTimeFormatter.ISO_DATE));

        return ScheduleResponseDto.of(scheduleRepository.save(schedule));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> findAll() {
        return scheduleRepository.findAllByMemberId(JwtUtil.getMemberId()).orElseThrow(
                () -> new MemberScheduleNotFoundException(JwtUtil.getMemberId())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponseDto findMin() {
        return ScheduleResponseDto.of(scheduleRepository.findMinSchedule());
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponseDto findFixedDDay() {
        Long memberId = JwtUtil.getMemberId();

        Optional<Schedule> schedule = scheduleRepository.findFixedSchedule(memberId);

        if (schedule.isEmpty()) {
            return null;
        }

        return ScheduleResponseDto.of(schedule.get());
    }

    @Override
    @Transactional
    public void fixDDay(Long id) {
        Long memberId = JwtUtil.getMemberId();

        Optional<Schedule> schedule = scheduleRepository.findFixedSchedule(memberId);

        if (schedule.isPresent()) {
            Schedule s = schedule.get();
            s.setIsFixed(false);
            scheduleRepository.save(s);
        }

        Schedule fixedSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(id));
        fixedSchedule.setIsFixed(true);

        scheduleRepository.save(fixedSchedule);
    }
}
