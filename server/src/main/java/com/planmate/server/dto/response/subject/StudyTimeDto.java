package com.planmate.server.dto.response.subject;

import lombok.*;

import java.sql.Time;;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyTimeDto {
    private Integer hour;
    private Integer minute;
    private Integer second;

    public static StudyTimeDto of(Time totalTime) {
        return StudyTimeDto.builder()
                .hour(totalTime.getHours())
                .minute(totalTime.getMinutes())
                .second(totalTime.getSeconds())
                .build();
    }
}
