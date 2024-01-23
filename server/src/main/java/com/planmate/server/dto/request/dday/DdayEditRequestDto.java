package com.planmate.server.dto.request.dday;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DdayEditRequestDto {
    private Long dDayId;
    private String targetDate;
    private String title;
}
