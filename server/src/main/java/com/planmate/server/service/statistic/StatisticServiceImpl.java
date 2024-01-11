package com.planmate.server.service.statistic;

import com.planmate.server.domain.StudyBackUp;
import com.planmate.server.domain.Subject;
import com.planmate.server.dto.response.statistic.StatisticResponse;
import com.planmate.server.repository.StudyBackUpRepository;
import com.planmate.server.repository.SubjectRepository;
import com.planmate.server.util.JwtUtil;
import com.planmate.server.vo.StatisticData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class StatisticServiceImpl implements StatisticService {

    private final SubjectRepository subjectRepository;
    private final StudyBackUpRepository studyBackUpRepository;

    public StatisticServiceImpl(SubjectRepository subjectRepository,
                                StudyBackUpRepository studyBackUpRepository) {
        this.subjectRepository = subjectRepository;
        this.studyBackUpRepository = studyBackUpRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public StatisticResponse getStatisticData() {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        Sort sort = Sort.by(Sort.Direction.DESC,"studyTime");
        Pageable pageable = PageRequest.of(0,4,sort);
        List<Subject> subjectList = subjectRepository.findAllSubject(memberId,pageable);
        StatisticData statisticData = StatisticData.of(subjectList);
        return StatisticResponse.of(statisticData);
    }

    @Override
    @Transactional(readOnly = true)
    public StatisticResponse getDayStatisticData(LocalDate studyDate) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        Sort sort = Sort.by(Sort.Direction.DESC,"studyTime");
        Pageable pageable = PageRequest.of(0,4,sort);
        List<StudyBackUp> studyBackUpList = studyBackUpRepository.findStudyBackUp(studyDate,memberId,pageable);

        StatisticData statisticData = StatisticData.backUp(studyBackUpList);

        return StatisticResponse.of(statisticData);
    }
}
