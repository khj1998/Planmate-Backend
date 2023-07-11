package com.planmate.server.dto.request.planner;

import lombok.Getter;

@Getter
public class PlannerRequestDto {
    private String type;
    private String scheduleName;
    private String colorHex;
    private String day;
    private String startAt;
    private String endAt;
}
