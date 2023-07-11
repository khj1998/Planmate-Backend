package com.planmate.server.dto.request.subject;

import lombok.Getter;

@Getter
public class SubjectCreateRequestDto {
    private String name;
    private Boolean type;
    private String colorHex;
}
