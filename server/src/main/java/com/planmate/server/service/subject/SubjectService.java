package com.planmate.server.service.subject;

import com.planmate.server.domain.Subject;
import com.planmate.server.dto.request.subject.SubjectCreateRequestDto;
import com.planmate.server.dto.request.subject.SubjectEditRequestDto;
import com.planmate.server.dto.request.subject.SubjectTimeRequest;
import com.planmate.server.dto.response.subject.SubjectCreateResponse;
import com.planmate.server.dto.response.subject.SubjectResponse;
import com.planmate.server.dto.response.subject.SubjectStudyTimeResponse;
import com.planmate.server.dto.response.subject.SubjectTimeResponse;

import java.util.List;

public interface SubjectService {
    List<SubjectResponse> findSubject();
    List<SubjectStudyTimeResponse> findSubjectTime();
    void createSubject(SubjectCreateRequestDto subjectCreateRequestDto);
    void updateSubjectTime(SubjectTimeRequest subjectTimeRequest);
    void editSubject(SubjectEditRequestDto subjectEditRequestDto);
    void deleteSubject(Long subjectId);
    void initTime();
}
