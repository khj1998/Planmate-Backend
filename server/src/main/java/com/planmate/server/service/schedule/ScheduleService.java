package com.planmate.server.service.schedule;

import com.planmate.server.domain.Schedule;
import com.planmate.server.dto.request.schedule.AddScheduleRequestDto;
import com.planmate.server.dto.request.schedule.ScheduleEditRequestDto;
import com.planmate.server.dto.response.schedule.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto addDDay(AddScheduleRequestDto addScheduleRequestDto);
    void deleteDDay(Long id);
    ScheduleResponseDto modifySchedule(ScheduleEditRequestDto editRequestDto);
    List<ScheduleResponseDto> findAll();
    ScheduleResponseDto findMin();
    void fixDDay(Long id);
    ScheduleResponseDto findFixedDDay();
}
