package com.planmate.server.service.statistic;

import com.planmate.server.domain.StudyBackUp;
import com.planmate.server.domain.Subject;
import com.planmate.server.dto.response.statistic.StatisticResponse;
import com.planmate.server.repository.StudyBackUpRepository;
import com.planmate.server.repository.SubjectRepository;
import com.planmate.server.util.JwtUtil;
import com.planmate.server.vo.StatisticData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final SubjectRepository subjectRepository;
    private final StudyBackUpRepository studyBackUpRepository;

    @Override
    @Transactional(readOnly = true)
    public StatisticResponse getStatisticData() {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        List<Subject> subjectList = subjectRepository.findAllSubject(memberId,getStudyTimePageable());
        StatisticData statisticData = StatisticData.of(subjectList);

        return StatisticResponse.of(statisticData);
    }

    @Override
    @Transactional(readOnly = true)
    public StatisticResponse getDayStatisticData(LocalDate studyDate) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        List<StudyBackUp> studyBackUpList = studyBackUpRepository.findStudyBackUp(studyDate,memberId,getStudyTimePageable());
        StatisticData statisticData = StatisticData.backUp(studyBackUpList);

        return StatisticResponse.of(statisticData);
    }

    @Override
    @Transactional(readOnly = true)
    public StatisticResponse getMonthStatisticData(YearMonth yearMonth) {
        Long memberId = JwtUtil.getUserIdByAccessToken();

        LocalDate studyDate = yearMonth.atDay(1);
        List<StudyBackUp> studyBackUpList = studyBackUpRepository.findMonthlyStudyBackUp(studyDate,memberId,getStudyTimePageable());
        StatisticData statisticData = StatisticData.backUp(studyBackUpList);

        return StatisticResponse.of(statisticData);
    }

    private Pageable getStudyTimePageable() {
        Sort sort = Sort.by(Sort.Direction.DESC,"studyTime");
        return PageRequest.of(0,4,sort);
    }
}
