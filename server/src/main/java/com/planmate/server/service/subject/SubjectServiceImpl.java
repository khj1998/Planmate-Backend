package com.planmate.server.service.subject;

import com.planmate.server.domain.MemberSubject;
import com.planmate.server.domain.Subject;
import com.planmate.server.dto.request.subject.SubjectCreateRequestDto;
import com.planmate.server.dto.request.subject.SubjectEditRequestDto;
import com.planmate.server.exception.subject.SubjectNotFoundException;
import com.planmate.server.repository.MemberSubjectRepository;
import com.planmate.server.repository.SubjectRepository;
import com.planmate.server.util.JwtUtil;
import org.springframework.transaction.annotation.Transactional;

public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final MemberSubjectRepository memberSubjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository,MemberSubjectRepository memberSubjectRepository) {
        this.subjectRepository = subjectRepository;
        this.memberSubjectRepository = memberSubjectRepository;
    }

    @Override
    @Transactional
    public Subject createSubject(SubjectCreateRequestDto subjectCreateRequestDto) {
        Long memberId = JwtUtil.getMemberId();

        Subject subject = Subject.of(subjectCreateRequestDto);
        subjectRepository.save(subject);

        MemberSubject memberSubject = MemberSubject.of(memberId, subject.getId());
        memberSubjectRepository.save(memberSubject);

        return subject;
    }

    /**
     * TODO
     * Query 어노테이션으로 수정 예정 (alter table)
     */
    @Override
    @Transactional
    public Subject editSubject(SubjectEditRequestDto subjectEditRequestDto) {
        Long memberId = JwtUtil.getMemberId();
        Long subjectId = subjectEditRequestDto.getSubjectId();

        MemberSubject memberSubject = memberSubjectRepository.findMemberSubject(memberId, subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));

        Subject subject = subjectRepository.findById(memberSubject.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));

        subject.setName(subjectEditRequestDto.getName());
        subjectRepository.save(subject);

        return subject;
    }

    @Override
    @Transactional
    public void deleteSubject(Long subjectId) {
        Long memberId = JwtUtil.getMemberId();

        MemberSubject memberSubject = memberSubjectRepository.findMemberSubject(memberId, subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));

        Subject subject = subjectRepository.findById(memberSubject.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));

        subjectRepository.delete(subject);
    }
}
