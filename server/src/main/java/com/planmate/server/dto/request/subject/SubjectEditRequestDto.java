package com.planmate.server.dto.request.subject;

import lombok.Getter;

@Getter
public class SubjectEditRequestDto {
    private Long subjectId;
    private String name;
    private String colorHex;
}
