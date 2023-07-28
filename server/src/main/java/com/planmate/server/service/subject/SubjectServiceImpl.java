package com.planmate.server.service.subject;

import com.planmate.server.domain.Subject;
import com.planmate.server.dto.request.subject.SubjectCreateRequestDto;
import com.planmate.server.dto.request.subject.SubjectEditRequestDto;
import com.planmate.server.dto.request.subject.SubjectTimeRequest;
import com.planmate.server.dto.response.subject.SubjectCreateResponse;
import com.planmate.server.dto.response.subject.SubjectResponse;
import com.planmate.server.dto.response.subject.SubjectStudyTimeResponse;
import com.planmate.server.dto.response.subject.SubjectTimeResponse;
import com.planmate.server.exception.subject.SubjectDuplicatedException;
import com.planmate.server.exception.subject.SubjectNotFoundException;
import com.planmate.server.repository.SubjectRepository;
import com.planmate.server.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectStudyTimeResponse> findSubjectTime() {
        List<SubjectStudyTimeResponse> responseDtoList = new ArrayList<>();
        Long memberId = JwtUtil.getMemberId();
        List<Subject> subjectList = subjectRepository.findByMemberId(memberId);

        for (Subject subject : subjectList) {
            SubjectStudyTimeResponse responseDto = SubjectStudyTimeResponse.of(subject);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectResponse> findSubject() {
        List<SubjectResponse> responseList = new ArrayList<>();
        Long memberId = JwtUtil.getMemberId();
        List<Subject> subjectList = subjectRepository.findByMemberId(memberId);

        for (Subject subject : subjectList) {
            SubjectResponse response = SubjectResponse.of(subject);
            responseList.add(response);
        }

        return responseList;
    }

    @Override
    @Transactional
    public void createSubject(SubjectCreateRequestDto subjectCreateRequestDto) {
        Long memberId = JwtUtil.getMemberId();
        String subjectName = subjectCreateRequestDto.getName().replace(" ","");

        Boolean isExistSubject = subjectRepository.findSubject(memberId,subjectName).isPresent();

        if (isExistSubject) {
            throw new SubjectDuplicatedException(subjectName);
        }

        Subject subject = Subject.of(subjectCreateRequestDto,memberId);
        subjectRepository.save(subject);
    }

    @Override
    @Transactional
    public void initTime() {
        Long memberId = JwtUtil.getMemberId();
        List<Subject> subjectList = subjectRepository.findByMemberId(memberId);

        for (Subject subject : subjectList) {
            subject.initTime();
        }
        subjectRepository.saveAll(subjectList);
    }

    @Override
    @Transactional
    public void updateSubjectTime(SubjectTimeRequest subjectTimeRequest) {
        Long memberId = JwtUtil.getMemberId();
        Subject subject = subjectRepository.findSubject(memberId, subjectTimeRequest.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(subjectTimeRequest.getSubjectId()));

        subject.updateStudyTime(subjectTimeRequest.getStartAt(),subjectTimeRequest.getEndAt());
        subject.updateRestTime(subjectTimeRequest.getStartAt());
        subject.updateStartEndTime(subjectTimeRequest);
        subjectRepository.save(subject);
    }

    /**
     * TODO
     * Query 어노테이션으로 수정 예정 (alter table)
     */
    @Override
    @Transactional
    public void editSubject(SubjectEditRequestDto subjectEditRequestDto) {
        Long memberId = JwtUtil.getMemberId();
        Long subjectId = subjectEditRequestDto.getSubjectId();

        Subject subject = subjectRepository.findSubject(memberId,subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));
        subject.setName(subjectEditRequestDto.getName());
        subject.setColorHex(subjectEditRequestDto.getColorHex());
        subjectRepository.save(subject);
    }

    @Override
    @Transactional
    public void deleteSubject(Long subjectId) {
        Long memberId = JwtUtil.getMemberId();

        Subject subject = subjectRepository.findSubject(memberId,subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));

        subjectRepository.delete(subject);
    }
}
