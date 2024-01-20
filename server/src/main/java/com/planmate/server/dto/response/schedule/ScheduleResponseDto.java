package com.planmate.server.dto.response.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.planmate.server.domain.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Builder
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleResponseDto {
    private Long scheduleId;
    private String title;
    private LocalDate targetDate;
    private Long remainingDays;
    private Boolean isFixed;

    public static ScheduleResponseDto of(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .scheduleId(schedule.getScheduleId())
                .title(schedule.getTitle())
                .targetDate(schedule.getTargetDate())
                .remainingDays((long) Period.between(LocalDate.now(), schedule.getTargetDate()).getDays())
                .build();
    }

    public static ScheduleResponseDto of(Schedule schedule,Boolean isFixed) {
        return ScheduleResponseDto.builder()
                .scheduleId(schedule.getScheduleId())
                .title(schedule.getTitle())
                .targetDate(schedule.getTargetDate())
                .remainingDays((long) Period.between(LocalDate.now(), schedule.getTargetDate()).getDays())
                .isFixed(isFixed)
                .build();
    }
}
