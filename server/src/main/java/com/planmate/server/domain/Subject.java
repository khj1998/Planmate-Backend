package com.planmate.server.domain;

import com.planmate.server.dto.request.subject.SubjectCreateRequestDto;
import com.planmate.server.dto.request.subject.SubjectTimeRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

import javax.persistence.*;
import java.sql.Time;
@Entity
@Table(name = "subject")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @Column(name = "subject_id",columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "과목 태그 식별자")
    private Long subjectId;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public static Subject of(SubjectCreateRequestDto subjectCreateRequestDto,Member member) {
        String colorHex;

        if (subjectCreateRequestDto.getColorHex() == null) {
            colorHex = "#009900";
        } else {
            colorHex = subjectCreateRequestDto.getColorHex();
        }

        return Subject.builder()
                .member(member)
                .name(subjectCreateRequestDto.getName())
                .maxStudyTime(new Time(0,0,0))
                .studyTime(new Time(0,0,0))
                .restTime(new Time(0,0,0))
                .startAt(new Time(0,0,0))
                .endAt(new Time(0,0,0))
                .colorHex(colorHex)
                .build();
    }

    public void initTime() {
        // 시작 시간, 끝 시간 초기 설정
        this.startAt = new Time(0,0,0);
        this.endAt = new Time(0,0,0);

        // maxStudyTime 초기 설정
        this.maxStudyTime = new Time(0,0,0);

        // studyTime 초기 설정
        this.studyTime = new Time(0,0,0);

        // restTime 초기 설정
        this.restTime = new Time(0,0,0);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public void updateStudyTime(String startAt,String endAt) {
        String[] startSplit = startAt.split(":");
        String[] endSplit = endAt.split(":");
        Integer startTimeSecond;
        Integer endTimeSecond = Integer.parseInt(endSplit[0])*3600 + Integer.parseInt(endSplit[1])*60 + Integer.parseInt(endSplit[2]);

        if (Integer.parseInt(startSplit[0]) > Integer.parseInt(endSplit[0])) {
            startTimeSecond = 0;
        } else {
            startTimeSecond = Integer.parseInt(startSplit[0])*3600 + Integer.parseInt(startSplit[1])*60 + Integer.parseInt(startSplit[2]);
        }

        Integer newStudySecond = endTimeSecond - startTimeSecond;
        Integer lastTimeSecond = this.getStudyTime().getHours()*3600 + this.getStudyTime().getMinutes()*60 + this.getStudyTime().getSeconds();

        this.studyTime = getNewStudyTime(newStudySecond,lastTimeSecond);

        Integer maxStudySecond = this.getMaxStudyTime().getHours()*3600 + this.getMaxStudyTime().getMinutes()*60 + this.getMaxStudyTime().getSeconds();
        maxStudySecond = Math.max(maxStudySecond,newStudySecond);

        this.maxStudyTime = getNewMaxStudyTime(maxStudySecond);
    }

    private Time getNewStudyTime(Integer newStudySecond,Integer lastTimeSecond) {
        Integer newStudyTimeSecond = newStudySecond + lastTimeSecond;
        Integer hours = newStudyTimeSecond/3600;
        Integer minutes = (newStudyTimeSecond - 3600*hours)/60;
        Integer seconds = newStudyTimeSecond - 3600*hours - 60*minutes;

        return new Time(hours,minutes,seconds);
    }

    private Time getNewMaxStudyTime(Integer maxStudySecond) {
        Integer hours = maxStudySecond/3600;
        Integer minutes = (maxStudySecond - 3600*hours)/60;
        Integer seconds = maxStudySecond - 3600*hours - 60*minutes;

        return new Time(hours,minutes,seconds);
    }

    public void updateStartEndTime(SubjectTimeRequest subjectTimeRequest) {
        String[] startSplit = subjectTimeRequest.getStartAt().split(":");
        String[] endSplit = subjectTimeRequest.getEndAt().split(":");

        Integer startTimeSecond = Integer.parseInt(startSplit[0])*3600 + Integer.parseInt(startSplit[1])*60 + Integer.parseInt(startSplit[2]);
        Integer endTimeSecond = Integer.parseInt(endSplit[0])*3600 + Integer.parseInt(endSplit[1])*60 + Integer.parseInt(endSplit[2]);
        Integer hours;
        Integer minutes;
        Integer seconds;

        // DB에 시작시간과 끝 시간이 같으면 유저는 공부한 적이 없음. 시작시간과 끝시간 모두 업데이트 해야함.
        if (this.endAt.getTime() == this.getStartAt().getTime()) {
            hours = startTimeSecond/3600;
            minutes = (startTimeSecond - hours*3600)/60;
            seconds = startTimeSecond - hours*3600 - minutes*60;
            this.startAt = new Time(hours,minutes,seconds);

            hours = endTimeSecond/3600;
            minutes = (endTimeSecond - hours*3600)/60;
            seconds = endTimeSecond - hours*3600 - minutes*60;
            this.endAt = new Time(hours,minutes,seconds);
        } else { // 공부 시작한 기록이 있음. 공부 최종 종료 시간만 업데이트 진행.
            hours = endTimeSecond/3600;
            minutes = (endTimeSecond - hours*3600)/60;
            seconds = endTimeSecond - hours*3600 - minutes*60;
            this.endAt = new Time(hours,minutes,seconds);
        }
     }

    public void updateRestTime() {
        Integer startTimeSecond = this.getStartAt().getHours()*3600 + this.getStartAt().getMinutes()*60 + this.getStartAt().getSeconds();
        Integer endTimeSecond = this.getEndAt().getHours()*3600 + this.getEndAt().getMinutes()*60 + this.getEndAt().getSeconds();
        Integer studyTimeSecond = this.getStudyTime().getHours()*3600 + this.getStudyTime().getMinutes()*60 + this.getStudyTime().getSeconds();

        Integer totalRestTimeSecond = endTimeSecond - startTimeSecond - studyTimeSecond;
        Integer restTimeHour = totalRestTimeSecond/3600;
        Integer restTimeMinute = (totalRestTimeSecond - restTimeHour*3600) / 60;
        Integer restTimeSecond = totalRestTimeSecond - restTimeHour*3600 - restTimeMinute*60;

        this.restTime = new Time(restTimeHour,restTimeMinute,restTimeSecond);
    }
}
