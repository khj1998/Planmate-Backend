package com.planmate.server.domain;

import com.planmate.server.converter.SubjectTypeConverter;
import com.planmate.server.dto.request.planner.PlannerRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@ApiModel(description = "시간표 엔티티")
@Entity
@Table(name = "planner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planner {
    @Id
    @Column(name = "planner_id",columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plannerId;

    @Column(name = "member_id",nullable = false,columnDefinition = "int")
    private Long memberId;

    @Column(name = "schedule_name",nullable = false,length = 64,columnDefinition = "varchar")
    private String scheduleName;

    @Column(name = "color_hex",nullable = false,length = 8,columnDefinition = "varchar")
    private String colorHex;

    @Column(name = "day",nullable = false,length = 2,columnDefinition = "varchar")
    private String day;

    @Column(name = "start_time",nullable = false,length = 12,columnDefinition = "varchar")
    private String startTime;

    @Column(name = "end_time",nullable = false,length = 12,columnDefinition = "varchar")
    private String endTime;

    public static Planner of(PlannerRequestDto plannerRequestDto,Long memberId) {
        return Planner.builder()
                .memberId(memberId)
                .scheduleName(plannerRequestDto.getScheduleName())
                .colorHex(plannerRequestDto.getColorHex())
                .day(plannerRequestDto.getDay())
                .startTime(plannerRequestDto.getStartAt())
                .endTime(plannerRequestDto.getEndAt())
                .build();
    }
}
