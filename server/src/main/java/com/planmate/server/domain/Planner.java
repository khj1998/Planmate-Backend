package com.planmate.server.domain;

import com.planmate.server.converter.SubjectTypeConverter;
import com.planmate.server.dto.request.planner.PlannerRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@ApiModel(description = "시간표 엔티티")
@Entity
@Table(name = "planner")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planner {
    @Id
    @Column(name = "planner_id",columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plannerId;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "schedule_name",nullable = false,length = 64,columnDefinition = "varchar")
    private String scheduleName;

    @Column(name = "color_hex",nullable = false,length = 8,columnDefinition = "varchar")
    private String colorHex;

    @Column(name = "day",nullable = false,length = 32,columnDefinition = "varchar")
    private String day;

    @Column(name = "start_time",nullable = false,length = 12,columnDefinition = "varchar")
    private String startTime;

    @Column(name = "end_time",nullable = false,length = 12,columnDefinition = "varchar")
    private String endTime;

    public static Planner of(PlannerRequestDto plannerRequestDto,Member member) {
        return Planner.builder()
                .member(member)
                .scheduleName(plannerRequestDto.getScheduleName())
                .colorHex(plannerRequestDto.getColorHex())
                .day(plannerRequestDto.getDay())
                .startTime(plannerRequestDto.getStartAt())
                .endTime(plannerRequestDto.getEndAt())
                .build();
    }

    public void updatePlanner(PlannerRequestDto plannerRequestDto) {
        if (StringUtils.hasText(plannerRequestDto.getScheduleName())) {
            this.scheduleName = plannerRequestDto.getScheduleName();
        }

        if (StringUtils.hasText(plannerRequestDto.getStartAt())) {
            this.startTime = plannerRequestDto.getStartAt();
        }

        if (StringUtils.hasText(plannerRequestDto.getEndAt())) {
            this.endTime = plannerRequestDto.getEndAt();
        }

        if (StringUtils.hasText(plannerRequestDto.getColorHex())) {
            this.colorHex = plannerRequestDto.getColorHex();
        }
    }
}
