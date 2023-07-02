package com.planmate.server.domain;

import com.planmate.server.dto.request.subject.SubjectCreateRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;

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

    @Column(name = "name",nullable = false,length = 50,columnDefinition = "varchar")
    private String name;

    @Column(name = "type",nullable = false,columnDefinition = "bit")
    private Boolean type;

    @Column(name = "max_study_time",columnDefinition = "time")
    private Time maxStudyTime;

    @Column(name = "study_time",columnDefinition = "time")
    private Time studyTime;

    @Column(name = "rest_time",columnDefinition = "time")
    private Time restTime;

    @Column(name = "start_at",columnDefinition = "datetime")
    private LocalDate startAt;

    @Column(name = "end_at",columnDefinition = "datetime")
    private LocalDate endAt;

    public static Subject of(SubjectCreateRequestDto subjectCreateRequestDto) {
        return Subject.builder()
                .name(subjectCreateRequestDto.getName())
                .type(subjectCreateRequestDto.getType())
                .build();
    }
}
