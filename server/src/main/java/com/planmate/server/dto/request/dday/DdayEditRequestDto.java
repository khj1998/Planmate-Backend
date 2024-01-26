package com.planmate.server.dto.request.dday;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DdayEditRequestDto {
    @JsonProperty("dDayId")
    private Long dDayId;
    private String targetDate;
    private String title;
}
