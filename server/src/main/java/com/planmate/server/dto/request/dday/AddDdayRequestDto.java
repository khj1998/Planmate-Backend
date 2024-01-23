package com.planmate.server.dto.request.dday;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDdayRequestDto {
    private String title;
    private String targetDate;
}
