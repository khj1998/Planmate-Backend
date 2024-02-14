package com.planmate.server.dto.response.subject;

import com.planmate.server.vo.YesterdayStudyTimeVo;
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

    private List<YesterdayStudyTimeVo> yesterdayStudyTimeList;

    public static SubjectTimeSliceResponse of(List<YesterdayStudyTimeVo> yesterdayStudyTimeList,Time nowTotalTime,Integer nowGraphHour) {
        return SubjectTimeSliceResponse.builder()
                .nowGraphHour(nowGraphHour)
                .yesterdayStudyTimeList(yesterdayStudyTimeList)
                .todayHour(nowTotalTime.getHours())
                .todayMinute(nowTotalTime.getMinutes())
                .todaySecond(nowTotalTime.getSeconds())
                .build();
    }
}
