package com.planmate.server.service.subject;

import com.planmate.server.domain.Member;
import com.planmate.server.domain.StudyBackUp;
import com.planmate.server.domain.StudyTimeSlice;
import com.planmate.server.domain.Subject;
import com.planmate.server.dto.request.subject.SubjectCreateRequestDto;
import com.planmate.server.dto.request.subject.SubjectEditRequestDto;
import com.planmate.server.dto.request.subject.SubjectTimeRequest;
import com.planmate.server.dto.response.subject.SubjectResponse;
import com.planmate.server.dto.response.subject.SubjectStudyTimeResponse;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.exception.subject.SubjectDuplicatedException;
import com.planmate.server.exception.subject.SubjectNotFoundException;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.StudyBackUpRepository;
import com.planmate.server.repository.StudyTimeSliceRepository;
import com.planmate.server.repository.SubjectRepository;
import com.planmate.server.util.JwtUtil;
import static com.planmate.server.config.ModelMapperConfig.modelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;
    private final StudyBackUpRepository studyBackUpRepository;
    private final StudyTimeSliceRepository studyTimeSliceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SubjectStudyTimeResponse> findSubjectTime() {
        List<SubjectStudyTimeResponse> responseDtoList = new ArrayList<>();
        Long memberId = JwtUtil.getUserIdByAccessToken();
        List<Subject> subjectList = subjectRepository.findByMemberMemberId(memberId);

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
        Long memberId = JwtUtil.getUserIdByAccessToken();
        List<Subject> subjectList = subjectRepository.findByMemberMemberId(memberId);

        for (Subject subject : subjectList) {
            SubjectResponse response = SubjectResponse.of(subject);
            responseList.add(response);
        }

        return responseList;
    }

    @Override
    @Transactional
    public SubjectResponse createSubject(SubjectCreateRequestDto subjectCreateRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        String subjectName = subjectCreateRequestDto.getName().replace(" ","");

        Boolean isExistSubject = subjectRepository.findSubject(memberId,subjectName).isPresent();

        if (isExistSubject) {
            throw new SubjectDuplicatedException(subjectName);
        }

        Subject subject = Subject.of(subjectCreateRequestDto,member);
        return modelMapper.map(subjectRepository.save(subject), SubjectResponse.class);
    }

    @Override
    @Transactional
    public void backUpAndInit() {
        List<Subject> subjectList = subjectRepository.findAll();
        List<StudyBackUp> studyBackUpList = new ArrayList<>();

        for (Subject subject : subjectList) {
            StudyBackUp studyBackUp = StudyBackUp.of(subject);
            studyBackUpList.add(studyBackUp);
            subject.initTime();
        }

        studyBackUpRepository.saveAll(studyBackUpList);
        subjectRepository.saveAll(subjectList);
    }

    @Override
    @Transactional
    public SubjectResponse updateSubjectTime(SubjectTimeRequest subjectTimeRequest) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Subject subject = subjectRepository.findSubject(memberId, subjectTimeRequest.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(subjectTimeRequest.getSubjectId()));

        subject.updateStudyTime(subjectTimeRequest.getStartAt(),subjectTimeRequest.getEndAt());
        subject.updateRestTime(subjectTimeRequest.getStartAt());
        subject.updateStartEndTime(subjectTimeRequest);

        return modelMapper.map(subjectRepository.save(subject),SubjectResponse.class);
    }

    @Override
    @Transactional
    public SubjectResponse editSubject(SubjectEditRequestDto subjectEditRequestDto) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Long subjectId = subjectEditRequestDto.getSubjectId();

        Subject subject = subjectRepository.findSubject(memberId,subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));
        subject.updateName(subjectEditRequestDto.getName());
        subject.updateColorHex(subjectEditRequestDto.getColorHex());

        return modelMapper.map(subjectRepository.save(subject), SubjectResponse.class);
    }

    @Override
    @Transactional
    public void deleteSubject(Long subjectId) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Subject subject = subjectRepository.findSubject(memberId,subjectId)
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));

        subjectRepository.delete(subject);
    }

    @Override
    @Transactional
    public Integer checkBackUpData() {
        return studyBackUpRepository.findAll().size();
    }

    @Override
    @Transactional
    public Boolean backupTimeSlice() {
        List<Subject> subjectList = subjectRepository.findAll();

        HashMap<Member,List<Subject>> memberSubjectGroup = makeMemberSubjectGroup(subjectList);
        List<StudyTimeSlice> studyTimeSliceList = getStudyTimeSliceList(memberSubjectGroup);

        studyTimeSliceRepository.saveAll(studyTimeSliceList);

        return true;
    }

    private HashMap<Member,List<Subject>> makeMemberSubjectGroup(List<Subject> subjectList) {
        HashMap<Member,List<Subject>> hashMap = new HashMap<>();

        for (Subject subject : subjectList) {
            Member m = subject.getMember();

            if (hashMap.containsKey(m)) {
                hashMap.get(m).add(subject);
            } else {
                hashMap.put(m,new ArrayList<>(List.of(subject)));
            }
        }

        return hashMap;
    }

    private List<StudyTimeSlice> getStudyTimeSliceList(HashMap<Member,List<Subject>> memberSubjectGroup) {
        List<StudyTimeSlice> studyTimeSliceList = new ArrayList<>();

        for (Member member : memberSubjectGroup.keySet()) {
            List<Subject> subjectList = memberSubjectGroup.get(member);

            StudyTimeSlice studyTimeSlice = StudyTimeSlice.builder()
                    .member(member)
                    .totalTime(getTotalStudyTime(subjectList))
                    .hour(LocalDateTime.now().getHour())
                    .build();

            studyTimeSliceList.add(studyTimeSlice);
        }

        return studyTimeSliceList;
    }

    private Time getTotalStudyTime(List<Subject> subjectList) {
        Long totalMillis = 0L;

        for (Subject subject : subjectList) {
            totalMillis += subject.getStudyTime().getTime();
        }

        return new Time(totalMillis);
    }
}
