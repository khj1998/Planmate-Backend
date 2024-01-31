package com.planmate.server.service.schedule;

import com.planmate.server.dto.request.dday.AddDdayRequestDto;
import com.planmate.server.dto.request.dday.DdayEditRequestDto;
import com.planmate.server.dto.response.dday.DdayResponseDto;

import java.util.List;

public interface DdayService {
    DdayResponseDto addDDay(AddDdayRequestDto addDdayRequestDto);
    void deleteDDay(Long id);
    DdayResponseDto editSchedule(DdayEditRequestDto editRequestDto);
    List<DdayResponseDto> findAll();
    DdayResponseDto findMin();
    void fixDDay(Long id);
    DdayResponseDto findFixedDDay();
}
