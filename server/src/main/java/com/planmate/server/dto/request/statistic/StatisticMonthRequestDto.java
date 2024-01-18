package com.planmate.server.dto.request.statistic;

import lombok.Getter;

import java.time.LocalDate;
import java.time.YearMonth;

@Getter
public class StatisticMonthRequestDto {
    private LocalDate yearMonth;
}
