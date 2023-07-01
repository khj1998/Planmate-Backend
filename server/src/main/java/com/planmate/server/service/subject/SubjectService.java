package com.planmate.server.service.subject;

import com.planmate.server.domain.Subject;
import com.planmate.server.dto.request.subject.SubjectCreateRequestDto;
import com.planmate.server.dto.request.subject.SubjectEditRequestDto;

public interface SubjectService {
    Subject createSubject(SubjectCreateRequestDto subjectCreateRequestDto);
    Subject editSubject(SubjectEditRequestDto subjectEditRequestDto);
    void deleteSubject(Long subjectId);
}
