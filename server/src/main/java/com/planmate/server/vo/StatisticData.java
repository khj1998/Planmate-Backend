package com.planmate.server.vo;

import com.planmate.server.domain.Subject;
import lombok.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticData {
    private Time totalStudyTime;
    private Time restTime;
    private Time maxStudyTime;
    private Time startAt;
    private Time endAt;
    private List<StudyTime> studyTimeList;

    public static StatisticData of(List<Subject> subjectList) {
        List<StudyTime> studyTimeList = new ArrayList<>();
        Integer totalStudySecond = 0;
        Integer restSecond = 0;
        Integer maxStudySecond = 0;
        Integer startAtSecond = Integer.MAX_VALUE;
        Integer endAtSecond = 0;

        for (Subject subject : subjectList) {
            StudyTime studyTime = StudyTime.builder()
                    .name(subject.getName())
                    .studyTimeHours((long) subject.getStudyTime().getHours())
                    .studyTimeMinutes((long) subject.getStudyTime().getMinutes())
                    .studyTimeSeconds((long) subject.getStudyTime().getSeconds())
                    .build();
            studyTimeList.add(studyTime);

            totalStudySecond += getSecond(subject.getStudyTime());
            restSecond += getSecond(subject.getRestTime());
            maxStudySecond = getMaxSecond(subject.getMaxStudyTime(),maxStudySecond);
            startAtSecond = getMinSecond(subject.getStartAt(),subject.getEndAt(),startAtSecond);
            endAtSecond = getMaxSecond(subject.getEndAt(),endAtSecond);
        }

        if (startAtSecond == Integer.MAX_VALUE) {
            startAtSecond = 3600*5;
        }

        return StatisticData.builder()
                .studyTimeList(studyTimeList)
                .totalStudyTime(getTime(totalStudySecond))
                .restTime(getTime(restSecond))
                .maxStudyTime(getTime(maxStudySecond))
                .startAt(getTime(startAtSecond))
                .endAt(getTime(endAtSecond))
                .build();
    }

    private static Time getTime(Integer inputSecond) {
        Integer hours = inputSecond/3600;
        Integer minutes = (inputSecond - hours*3600)/60;
        Integer seconds = inputSecond - hours*3600 - minutes*60;

        return new Time(hours, minutes, seconds);
    }

    private static Integer getSecond(Time time) {
        Integer hours = time.getHours();
        Integer minutes = time.getMinutes();
        Integer seconds = time.getSeconds();

        return hours*3600 + minutes*60 + seconds;
    }

    private static Integer getMinSecond(Time startAt,Time endAt,Integer compareSecond) {
        Time endTime = new Time(5,0,0);

        if (endTime.equals(endAt)) {
            return compareSecond;
        }

        return Math.min(compareSecond,getSecond(startAt));
    }

    private static Integer getMaxSecond(Time endAt,Integer compareSecond) {
        return Math.max(compareSecond,getSecond(endAt));
    }
}
