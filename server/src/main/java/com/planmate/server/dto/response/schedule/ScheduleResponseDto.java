package com.planmate.server.dto.response.schedule;

import com.planmate.server.domain.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Builder
@Setter
@Getter
public class ScheduleResponseDto {
    private Long id;
    private String title;
    private LocalDate date;
    private Long dDay;

    public static ScheduleResponseDto of(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .date(schedule.getTargetDate())
                .dDay(new Long(Period.between(LocalDate.now(), schedule.getTargetDate()).getDays()))
                .build();
    }
}
