package com.planmate.server.dto.response.subject;

import com.planmate.server.converter.SubjectTypeConverter;
import com.planmate.server.domain.Subject;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectCreateResponse {
    private Long subjectId;
    private String name;
    private String colorHex;

    public static SubjectCreateResponse of(Subject subject) {

        return SubjectCreateResponse.builder()
                .subjectId(subject.getId())
                .name(subject.getName())
                .colorHex(subject.getColorHex())
                .build();
    }
}
