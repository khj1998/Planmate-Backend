package com.planmate.server.dto.response.statistic;

import com.planmate.server.domain.Subject;
import com.planmate.server.vo.StatisticData;
import com.planmate.server.vo.StudyTime;
import lombok.*;

import java.sql.Time;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticResponse {
    private Time totalStudyTime;
    private Time restTime;
    private Time maxStudyTime;
    private Time startAt;
    private Time endAt;
    private List<StudyTime> studyTimeList;

    public static StatisticResponse of(StatisticData statisticData) {
        return StatisticResponse.builder()
                .totalStudyTime(statisticData.getTotalStudyTime())
                .restTime(statisticData.getRestTime())
                .maxStudyTime(statisticData.getMaxStudyTime())
                .startAt(statisticData.getStartAt())
                .endAt(statisticData.getEndAt())
                .studyTimeList(statisticData.getStudyTimeList())
                .build();
    }
}
