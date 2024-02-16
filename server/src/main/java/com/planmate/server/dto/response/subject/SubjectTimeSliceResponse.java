package com.planmate.server.dto.response.subject;

import lombok.*;

import java.sql.Time;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectTimeSliceResponse {
    private Integer nowGraphHour;
    private Integer todayHour;
    private Integer todayMinute;
    private Integer todaySecond;

    private List<StudyTimeDto> todayStudyTimeList;
    private List<StudyTimeDto> yesterdayStudyTimeList;

    public static SubjectTimeSliceResponse of(List<StudyTimeDto> yesterdayStudyTimeList, List<StudyTimeDto> todayStudyTimeList, Time nowTotalTime, Integer nowGraphHour) {
        return SubjectTimeSliceResponse.builder()
                .nowGraphHour(nowGraphHour)
                .yesterdayStudyTimeList(yesterdayStudyTimeList)
                .todayStudyTimeList(todayStudyTimeList)
                .todayHour(nowTotalTime.getHours())
                .todayMinute(nowTotalTime.getMinutes())
                .todaySecond(nowTotalTime.getSeconds())
                .build();
    }
}
