package com.planmate.server.service.statistic;

import com.planmate.server.domain.Subject;
import com.planmate.server.dto.response.statistic.StatisticResponse;
import com.planmate.server.repository.SubjectRepository;
import com.planmate.server.util.JwtUtil;
import com.planmate.server.vo.StatisticData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;

@Slf4j
public class StatisticServiceImpl implements StatisticService {

    private final SubjectRepository subjectRepository;

    public StatisticServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public StatisticResponse getStatisticData() {
        Long memberId = JwtUtil.getMemberId();
        Sort sort = Sort.by(Sort.Direction.DESC,"studyTime");
        Pageable pageable = PageRequest.of(0,4,sort);
        List<Subject> subjectList = subjectRepository.findAllSubject(memberId,pageable);
        StatisticData statisticData = StatisticData.of(subjectList);
        return StatisticResponse.of(statisticData);
    }
}
