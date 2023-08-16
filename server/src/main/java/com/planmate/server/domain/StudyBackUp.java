package com.planmate.server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Table(name = "study_back_up")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyBackUp {
    @Id
    @Column(name = "id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "고유 식별자")
    private Long id;

    @Column(name = "member_id",nullable = false,columnDefinition = "int")
    private Long memberId;

    @Column(name = "name",nullable = false,length = 50,columnDefinition = "varchar")
    private String name;

    @Column(name = "color_hex",nullable = false,length = 12,columnDefinition = "varchar")
    private String colorHex;

    @Column(name = "max_study_time",nullable = false,columnDefinition = "time")
    private Time maxStudyTime;

    @Column(name = "study_time",nullable = false,columnDefinition = "time")
    private Time studyTime;

    @Column(name = "rest_time",nullable = false,columnDefinition = "time")
    private Time restTime;

    @Column(name = "start_at",nullable = false,columnDefinition = "time")
    private Time startAt;

    @Column(name = "end_at",nullable = false,columnDefinition = "time")
    private Time endAt;

    @Column(name = "study_date",nullable = false,columnDefinition = "date")
    private LocalDate studyDate;

    public static StudyBackUp of(Subject subject) {
        Integer year = LocalDateTime.now().getYear();
        Integer month = LocalDateTime.now().getMonth().getValue();
        Integer day = LocalDateTime.now().getDayOfMonth();

        return StudyBackUp.builder()
                .memberId(subject.getMemberId())
                .name(subject.getName())
                .colorHex(subject.getColorHex())
                .maxStudyTime(subject.getMaxStudyTime())
                .studyTime(subject.getStudyTime())
                .restTime(subject.getRestTime())
                .startAt(subject.getStartAt())
                .endAt(subject.getEndAt())
                .studyDate(LocalDate.of(year,month,day))
                .build();
    }
}
