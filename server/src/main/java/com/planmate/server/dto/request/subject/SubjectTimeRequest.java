package com.planmate.server.dto.request.subject;

import lombok.Getter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class SubjectTimeRequest {
    private Long subjectId;
    private String startAt;
    private String endAt;
}
