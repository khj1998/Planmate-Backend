package com.planmate.server.service.statistic;

import com.planmate.server.domain.Member;
import com.planmate.server.domain.StudyBackUp;
import com.planmate.server.exception.member.MemberNotFoundException;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.StudyBackUpRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
@RequiredArgsConstructor
class StatisticServiceImplTest {
    @Autowired
    private StudyBackUpRepository studyBackUpRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void addMockBackUpData() {
        Random random = new Random();
        Integer subjectNum = 1;
        LocalDate startDate = LocalDate.of(2024,1,1);
        LocalDate januaryMaxDate = LocalDate.of(2024,1,31);
        List<StudyBackUp> studyBackUpList = new ArrayList<>();

        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new MemberNotFoundException(1L));

        for (LocalDate date = startDate; !date.isAfter(januaryMaxDate); date = date.plusDays(1)) {
            Integer hours = random.nextInt(11);
            Integer minutes = random.nextInt(60);
            Integer seconds = random.nextInt(60);

            StudyBackUp studyBackUp = StudyBackUp.builder()
                    .member(member)
                    .name("과목"+subjectNum)
                    .colorHex("#2196F3")
                    .maxStudyTime(new Time(hours,minutes,seconds))
                    .studyTime(new Time(hours,minutes,seconds))
                    .restTime(new Time(24 - hours,minutes,seconds))
                    .startAt(new Time(7,0,0))
                    .endAt(new Time(23,59,59))
                    .studyDate(date)
                    .build();
            studyBackUpList.add(studyBackUp);

            subjectNum += 1;
        }

        studyBackUpRepository.saveAll(studyBackUpList);
    }
}