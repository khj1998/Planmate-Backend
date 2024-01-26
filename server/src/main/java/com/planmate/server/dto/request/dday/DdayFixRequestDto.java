package com.planmate.server.dto.request.dday;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DdayFixRequestDto {
    @JsonProperty("dDayId")
    private Long dDayId;
}
