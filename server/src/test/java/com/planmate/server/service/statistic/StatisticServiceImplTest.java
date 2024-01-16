package com.planmate.server.service.statistic;

import com.planmate.server.domain.StudyBackUp;
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

    @Test
    void addMockBackUpData() {
        Random random = new Random();
        Integer subjectNum = 1;
        LocalDate startDate = LocalDate.of(2023,12,1);
        LocalDate currentDate = LocalDate.now();
        List<StudyBackUp> studyBackUpList = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(currentDate); date = date.plusDays(1)) {
            Integer hours = random.nextInt(11);
            Integer minutes = random.nextInt(60);
            Integer seconds = random.nextInt(60);

            StudyBackUp studyBackUp = StudyBackUp.builder()
                    .memberId(1L)
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