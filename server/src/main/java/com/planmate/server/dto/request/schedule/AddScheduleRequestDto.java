package com.planmate.server.dto.request.schedule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddScheduleRequestDto {
    private String title;
    private String targetDate;
}
