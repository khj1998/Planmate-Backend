package com.planmate.server.dto.request.schedule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleEditRequestDto {
    private Long scheduleId;
    private String date;
    private String title;
}
