package com.planmate.server.service.subject;

import com.planmate.server.dto.request.subject.SubjectCreateRequestDto;
import com.planmate.server.dto.request.subject.SubjectEditRequestDto;
import com.planmate.server.dto.request.subject.SubjectTimeRequest;
import com.planmate.server.dto.response.subject.SubjectResponse;
import com.planmate.server.dto.response.subject.SubjectStudyTimeResponse;

import java.util.List;

public interface SubjectService {
    List<SubjectResponse> findSubject();
    List<SubjectStudyTimeResponse> findSubjectTime();
    SubjectResponse createSubject(SubjectCreateRequestDto subjectCreateRequestDto);
    SubjectResponse updateSubjectTime(SubjectTimeRequest subjectTimeRequest);
    SubjectResponse editSubject(SubjectEditRequestDto subjectEditRequestDto);
    void deleteSubject(Long subjectId);
    void backUpAndInit();
    Integer checkBackUpData();
}
