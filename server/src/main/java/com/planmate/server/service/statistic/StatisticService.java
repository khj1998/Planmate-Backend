package com.planmate.server.service.statistic;

import com.planmate.server.dto.response.statistic.StatisticResponse;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface StatisticService {
    StatisticResponse getStatisticData();
    List<StatisticResponse> getMonthStatisticData(LocalDate yearMonth);
    StatisticResponse getDayStatisticData(LocalDate studyDate);
}
