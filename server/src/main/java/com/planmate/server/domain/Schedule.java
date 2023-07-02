package com.planmate.server.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "schedule")
@ApiModel(value = "d-day 테이블")
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @Column(name = "id",columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "디데이 고유 식별자")
    private Long id;

    @Column(name = "member_id", columnDefinition = "int")
    private Long memberId;

    @Column(name = "title", columnDefinition = "varchar(30)")
    private String title;

    @Column(name = "target_date", columnDefinition = "date")
    private LocalDate targetDate;
}
