package com.planmate.server.dto.request.statistic;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StatisticDateRequestDto {
    LocalDate studyDate;
}
