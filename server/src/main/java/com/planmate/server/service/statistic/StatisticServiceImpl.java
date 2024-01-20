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
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

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
        StatisticData statisticData = StatisticData.backUp(studyBackUpList,studyDate);

        return StatisticResponse.of(statisticData);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatisticResponse> getMonthStatisticData(LocalDate yearMonth) {
        Long memberId = JwtUtil.getUserIdByAccessToken();
        List<StatisticResponse> responseList = new ArrayList<>();

        Integer year = yearMonth.getYear();
        Integer month = yearMonth.getMonth().getValue();
        List<StudyBackUp> studyBackUpList = studyBackUpRepository.findMonthlyStudyBackUp(year,month,memberId);
        Map<LocalDate,StatisticData> studyBackUpGroup = groupStudyBackUpByLocalDate(studyBackUpList);

        for (LocalDate studyDate : studyBackUpGroup.keySet()) {
            StatisticData statisticData = studyBackUpGroup.get(studyDate);
            responseList.add(StatisticResponse.of(statisticData));
        }

        return responseList;
    }

    private Map<LocalDate,StatisticData> groupStudyBackUpByLocalDate(List<StudyBackUp> studyBackUpList) {
        Map<LocalDate,StatisticData> statisticDataMap = new HashMap<>();

        Map<LocalDate,List<StudyBackUp>> studyBackUpGroup = studyBackUpList.stream()
                .collect(Collectors.groupingBy(StudyBackUp::getStudyDate));

        studyBackUpGroup.forEach((date,backUpList) -> {
            backUpList.sort(Comparator.comparing(StudyBackUp::getStudyTime).reversed());
            Integer maxSize = Math.min(backUpList.size(),4);
            List<StudyBackUp> slicedStudyBackUpList = new ArrayList<>(backUpList.subList(0,maxSize));

            StatisticData statisticData = StatisticData.backUp(slicedStudyBackUpList,date);
            statisticDataMap.put(backUpList.get(0).getStudyDate(),statisticData);
        });

        return new TreeMap<>(statisticDataMap);
    }

    private Pageable getStudyTimePageable() {
        Sort sort = Sort.by(Sort.Direction.DESC,"studyTime");
        return PageRequest.of(0,4,sort);
    }
}
