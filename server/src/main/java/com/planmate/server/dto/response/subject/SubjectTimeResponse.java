package com.planmate.server.dto.response.subject;

import com.planmate.server.converter.SubjectTypeConverter;
import com.planmate.server.domain.Subject;
import lombok.*;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectTimeResponse {
    private Long subjectId;
    private String name;
    private Time maxStudyTime;
    private Time studyTime;
    private Time restTime;

    public static SubjectTimeResponse of(Subject subject) {

        return SubjectTimeResponse.builder()
                .subjectId(subject.getId())
                .name(subject.getName())
                .maxStudyTime(subject.getMaxStudyTime())
                .studyTime(subject.getStudyTime())
                .restTime(subject.getRestTime())
                .build();
    }
}
