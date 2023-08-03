package com.planmate.server.dto.response.planner;

import com.planmate.server.converter.SubjectTypeConverter;
import com.planmate.server.domain.Planner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerResponseDto {
    private Long plannerId;
    private String type;
    private String scheduleName;
    private String colorHex;
    private String day;
    private String startAt;
    private String endAt;

    public static PlannerResponseDto of(Planner planner) {

        return PlannerResponseDto.builder()
                .plannerId(planner.getPlannerId())
                .scheduleName(planner.getScheduleName())
                .colorHex(planner.getColorHex())
                .day(planner.getDay())
                .startAt(planner.getStartTime())
                .endAt(planner.getEndTime())
                .build();
    }
}
