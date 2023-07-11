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
    private Time studyTime;
}
