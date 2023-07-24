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
    private Time studyTime;

    public static SubjectStudyTimeResponse of(Subject subject) {
        return SubjectStudyTimeResponse.builder()
                .subjectId(subject.getId())
                .name(subject.getName())
                .studyTime(subject.getStudyTime())
                .build();
    }
}
