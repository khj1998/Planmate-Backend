package com.planmate.server.dto.response.subject;

import com.planmate.server.converter.SubjectTypeConverter;
import com.planmate.server.domain.Subject;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectEditResponseDto {
    private Long subjectId;
    private String name;
    private String type;
    private String colorHex;

    public static SubjectEditResponseDto of(Subject subject) {
        Long typeValue = subject.getType() ? 0L : 1L;
        String type = SubjectTypeConverter.getTypeName(typeValue);

        return SubjectEditResponseDto.builder()
                .subjectId(subject.getId())
                .name(subject.getName())
                .type(type)
                .colorHex(subject.getColorHex())
                .build();
    }
}
