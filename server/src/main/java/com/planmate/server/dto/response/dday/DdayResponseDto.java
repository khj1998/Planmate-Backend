package com.planmate.server.dto.response.dday;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.planmate.server.domain.Dday;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Builder
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DdayResponseDto {
    @JsonProperty("dDayId")
    private Long dDayId;
    private String title;
    private LocalDate targetDate;
    private Long remainingDays;
    private Boolean isFixed;

    public static DdayResponseDto of(Dday dday) {
        return DdayResponseDto.builder()
                .dDayId(dday.getDDayId())
                .title(dday.getTitle())
                .targetDate(dday.getTargetDate())
                .remainingDays((long) Period.between(LocalDate.now(), dday.getTargetDate()).getDays())
                .isFixed(dday.getIsFixed())
                .build();
    }

    public static DdayResponseDto of(Dday dday, Boolean isFixed) {
        return DdayResponseDto.builder()
                .dDayId(dday.getDDayId())
                .title(dday.getTitle())
                .targetDate(dday.getTargetDate())
                .remainingDays((long) Period.between(LocalDate.now(), dday.getTargetDate()).getDays())
                .isFixed(isFixed)
                .build();
    }
}
