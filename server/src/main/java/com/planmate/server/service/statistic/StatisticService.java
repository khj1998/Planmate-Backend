package com.planmate.server.service.statistic;

import com.planmate.server.dto.response.statistic.StatisticResponse;

import java.time.LocalDate;
import java.time.YearMonth;

public interface StatisticService {
    StatisticResponse getStatisticData();
    StatisticResponse getMonthStatisticData(YearMonth yearMonth);
    StatisticResponse getDayStatisticData(LocalDate studyDate);
}
