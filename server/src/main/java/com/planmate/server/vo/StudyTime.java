package com.planmate.server.vo;

import lombok.*;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyTime {
    private String name;
    private Long studyTimeHours;
    private Long studyTimeMinutes;
    private Long studyTimeSeconds;
}
