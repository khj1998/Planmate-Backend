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
    @Column(name = "id",columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "디데이 고유 식별자")
    private Long id;

    @Column(name = "member_id", columnDefinition = "int")
    private Long memberId;

    @Column(name = "title",nullable = false ,length = 30,columnDefinition = "varchar")
    private String title;

    @Column(name = "target_date",nullable = false, columnDefinition = "date")
    private LocalDate targetDate;

    @Column(name = "is_fixed",nullable = false,columnDefinition = "bit")
    private Boolean isFixed;
}
