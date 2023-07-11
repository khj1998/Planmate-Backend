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
    private String type;
    private String name;
    private Time maxStudyTime;
    private Time studyTime;
    private Time restTime;

    public static SubjectTimeResponse of(Subject subject) {
        Long inputValue = subject.getType() ? 0L:1L;
        String type = SubjectTypeConverter.getTypeName(inputValue);

        return SubjectTimeResponse.builder()
                .type(type)
                .name(subject.getName())
                .maxStudyTime(subject.getMaxStudyTime())
                .studyTime(subject.getStudyTime())
                .restTime(subject.getRestTime())
                .build();
    }
}
