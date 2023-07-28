package com.planmate.server.domain;

import com.planmate.server.dto.request.subject.SubjectCreateRequestDto;
import com.planmate.server.dto.request.subject.SubjectTimeRequest;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.*;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;

@Entity
@Table(name = "subject")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @Column(name = "id",columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "과목 태그 식별자")
    private Long id;

    @Column(name = "member_id",nullable = false,columnDefinition = "int")
    private Long memberId;

    @Column(name = "name",nullable = false,length = 50,columnDefinition = "varchar")
    private String name;

    @Column(name = "color_hex",nullable = false,length = 12,columnDefinition = "varchar")
    private String colorHex;

    @Column(name = "max_study_time",columnDefinition = "time")
    private Time maxStudyTime;

    @Column(name = "study_time",columnDefinition = "time")
    private Time studyTime;

    @Column(name = "rest_time",columnDefinition = "time")
    private Time restTime;

    @Column(name = "start_at",columnDefinition = "datetime")
    private Time startAt;

    @Column(name = "end_at",columnDefinition = "datetime")
    private Time endAt;

    public static Subject of(SubjectCreateRequestDto subjectCreateRequestDto,Long memberId) {
        String colorHex;

        if (subjectCreateRequestDto.getColorHex() == null) {
            colorHex = "#009900";
        } else {
            colorHex = subjectCreateRequestDto.getColorHex();
        }

        return Subject.builder()
                .memberId(memberId)
                .name(subjectCreateRequestDto.getName())
                .maxStudyTime(new Time(0,0,0))
                .studyTime(new Time(0,0,0))
                .restTime(new Time(0,0,0))
                .startAt(new Time(5,0,0))
                .endAt(new Time(5,0,0))
                .colorHex(colorHex)
                .build();
    }

    public void initTime() {
        // 시작 시간, 끝 시간 초기 설정
        this.startAt = new Time(5,0,0);
        this.endAt = new Time(5,0,0);

        // maxStudyTime 초기 설정
        this.maxStudyTime = new Time(0,0,0);

        // studyTime 초기 설정
        this.studyTime = new Time(0,0,0);

        // restTime 초기 설정
        this.restTime = new Time(0,0,0);
    }

    public void updateStudyTime(String startAt,String endAt) {
        String[] startSplit = startAt.split(":");
        String[] endSplit = endAt.split(":");

        Integer startTimeSecond = Integer.parseInt(startSplit[0])*3600 + Integer.parseInt(startSplit[1])*60 + Integer.parseInt(startSplit[2]);
        Integer endTimeSecond = Integer.parseInt(endSplit[0])*3600 + Integer.parseInt(endSplit[1])*60 + Integer.parseInt(endSplit[2]);

        Integer newStudySecond = endTimeSecond - startTimeSecond;
        Integer lastTimeSecond = this.getStudyTime().getHours()*3600 + this.getStudyTime().getMinutes()*60 + this.getStudyTime().getSeconds();

        Integer newStudyTimeSecond = newStudySecond + lastTimeSecond;
        Integer hours = newStudyTimeSecond/3600;
        Integer minutes = (newStudyTimeSecond - 3600*hours)/60;
        Integer seconds = newStudyTimeSecond - 3600*hours - 60*minutes;

        this.setStudyTime(new Time(hours,minutes,seconds));

        Integer maxStudyTime = this.getMaxStudyTime().getHours()*3600 + this.getMaxStudyTime().getMinutes()*60 + this.getMaxStudyTime().getSeconds();
        maxStudyTime = Math.max(maxStudyTime,newStudySecond);

        hours = maxStudyTime/3600;
        minutes = (maxStudyTime - 3600*hours)/60;
        seconds = maxStudyTime - 3600*hours - 60*minutes;

        this.setMaxStudyTime(new Time(hours,minutes,seconds));
    }

    public void updateStartEndTime(SubjectTimeRequest subjectTimeRequest) {
        Time firstTime = new Time(5,0,0);

        String[] startSplit = subjectTimeRequest.getStartAt().split(":");
        String[] endSplit = subjectTimeRequest.getEndAt().split(":");

        Integer startTimeSecond = Integer.parseInt(startSplit[0])*3600 + Integer.parseInt(startSplit[1])*60 + Integer.parseInt(startSplit[2]);
        Integer endTimeSecond = Integer.parseInt(endSplit[0])*3600 + Integer.parseInt(endSplit[1])*60 + Integer.parseInt(endSplit[2]);
        Integer hours;
        Integer minutes;
        Integer seconds;

        if (this.endAt.getTime() == firstTime.getTime()) {
            hours = startTimeSecond/3600;
            minutes = (startTimeSecond - hours*3600)/60;
            seconds = startTimeSecond - hours*3600 - minutes*60;
            this.setStartAt(new Time(hours,minutes,seconds));

            hours = endTimeSecond/3600;
            minutes = (endTimeSecond - hours*3600)/60;
            seconds = endTimeSecond - hours*3600 - minutes*60;
            this.setEndAt(new Time(hours,minutes,seconds));
        } else {
            hours = endTimeSecond/3600;
            minutes = (endTimeSecond - hours*3600)/60;
            seconds = endTimeSecond - hours*3600 - hours*60;
            this.setEndAt(new Time(hours,minutes,seconds));
        }
     }

    public void updateRestTime(String startAt) {
        String[] startSplit = startAt.split(":");

        Integer nowStartSecond = Integer.parseInt(startSplit[0])*3600 + Integer.parseInt(startSplit[1])*60 + Integer.parseInt(startSplit[2]);
        Integer lastEndSecond = this.getEndAt().getHours()*3600 + this.getEndAt().getMinutes()*60 + this.getEndAt().getSeconds();
        Integer restSecond = this.getRestTime().getHours()*3600 + this.getRestTime().getMinutes()*60 + this.getRestTime().getSeconds();
        Integer newRestSecond = restSecond + (nowStartSecond - lastEndSecond);

        Integer hours = newRestSecond/3600;
        Integer minutes = (newRestSecond-hours*3600)/60;
        Integer seconds = newRestSecond - 3600*hours - 60*minutes;

        this.setRestTime(new Time(hours,minutes,seconds));
    }
}
