package com.planmate.server.service.statistic;

import com.planmate.server.dto.response.statistic.StatisticResponse;

import java.time.LocalDate;

public interface StatisticService {
    StatisticResponse getStatisticData();
    StatisticResponse getDayStatisticData(LocalDate studyDate);
}
