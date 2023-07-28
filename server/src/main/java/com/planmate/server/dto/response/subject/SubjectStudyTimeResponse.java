package com.planmate.server.dto.response.subject;

import com.planmate.server.domain.Subject;
import lombok.*;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectStudyTimeResponse {
    private Long subjectId;
    private String name;
    private String colorHex;
    private Long studyTimeHours;
    private Long studyTimeMinutes;
    private Long studyTimeSeconds;

    public static SubjectStudyTimeResponse of(Subject subject) {
        return SubjectStudyTimeResponse.builder()
                .subjectId(subject.getId())
                .name(subject.getName())
                .colorHex(subject.getColorHex())
                .studyTimeHours((long) subject.getStudyTime().getHours())
                .studyTimeMinutes((long) subject.getStudyTime().getMinutes())
                .studyTimeSeconds((long) subject.getStudyTime().getSeconds())
                .build();
    }
}
