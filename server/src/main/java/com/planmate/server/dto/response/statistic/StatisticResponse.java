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
    private Long totalStudyTimeHours;
    private Long totalStudyTimeMinutes;
    private Long totalStudyTimeSeconds;

    private Long restTimeHours;
    private Long restTimeMinutes;
    private Long restTimeSeconds;

    private Long maxStudyTimeHours;
    private Long maxStudyTimeMinutes;
    private Long maxStudyTimeSeconds;

    private Long startAtHours;
    private Long startAtMinutes;

    private Long endAtHours;
    private Long endAtMinutes;

    private List<StudyTime> studyTimeList;

    public static StatisticResponse of(StatisticData statisticData) {
        Time totalStudyTime = statisticData.getTotalStudyTime();
        Time restTime = statisticData.getRestTime();
        Time maxStudyTime = statisticData.getMaxStudyTime();
        Time startAt = statisticData.getStartAt();
        Time endAt = statisticData.getEndAt();

        return StatisticResponse.builder()
                .totalStudyTimeHours((long) totalStudyTime.getHours())
                .totalStudyTimeMinutes((long) totalStudyTime.getMinutes())
                .totalStudyTimeSeconds((long) totalStudyTime.getSeconds())

                .restTimeHours((long) restTime.getHours())
                .restTimeMinutes((long) restTime.getMinutes())
                .restTimeSeconds((long) restTime.getSeconds())

                .maxStudyTimeHours((long) maxStudyTime.getHours())
                .maxStudyTimeMinutes((long) maxStudyTime.getMinutes())
                .maxStudyTimeSeconds((long) maxStudyTime.getSeconds())

                .startAtHours((long) startAt.getHours())
                .startAtMinutes((long) startAt.getMinutes())

                .endAtHours((long) endAt.getHours())
                .endAtMinutes((long) endAt.getMinutes())
                .studyTimeList(statisticData.getStudyTimeList())
                .build();
    }
}
