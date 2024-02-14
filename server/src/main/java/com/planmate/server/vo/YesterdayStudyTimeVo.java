package com.planmate.server.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YesterdayStudyTimeVo {
    private Integer yesterdayHour;
    private Integer yesterdayMinute;
    private Integer yesterdaySecond;
}
