package com.planmate.server.dto.response.subject;

import lombok.*;

import java.sql.Time;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectTimeSliceResponse {
    private Integer nowGraphHour;
    private Integer yesterdayHour;
    private Integer yesterdayMinute;
    private Integer yesterdaySecond;
    private Integer todayHour;
    private Integer todayMinute;
    private Integer todaySecond;

    public static SubjectTimeSliceResponse of(Time yesterdayTotalTime,Time nowTotalTime,Integer nowGraphHour) {
        return SubjectTimeSliceResponse.builder()
                .nowGraphHour(nowGraphHour)
                .yesterdayHour(yesterdayTotalTime.getHours())
                .yesterdayMinute(yesterdayTotalTime.getMinutes())
                .yesterdaySecond(yesterdayTotalTime.getSeconds())
                .todayHour(nowTotalTime.getHours())
                .todayMinute(nowTotalTime.getMinutes())
                .todaySecond(nowTotalTime.getSeconds())
                .build();
    }
}
