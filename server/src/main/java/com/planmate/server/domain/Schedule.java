package com.planmate.server.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@ApiModel(value = "d-day 테이블")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @Column(name = "id",columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "디데이 고유 식별자")
    private Long scheduleId;

    @Column(name = "member_id", columnDefinition = "int")
    private Long memberId;

    @Column(name = "title",nullable = false ,length = 30,columnDefinition = "varchar")
    private String title;

    @Column(name = "target_date",nullable = false, columnDefinition = "date")
    private LocalDate targetDate;

    @Column(name = "is_fixed",nullable = false,columnDefinition = "bit")
    private Boolean isFixed;

    @CreationTimestamp
    @Column(name = "created_at",columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false,columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public void updateIsFixed(Boolean isFixed) {
        this.isFixed = isFixed;
    }
}
