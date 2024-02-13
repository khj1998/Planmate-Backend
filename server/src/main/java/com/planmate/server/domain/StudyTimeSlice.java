package com.planmate.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;

@Entity
@Table(name = "study_time_slice")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyTimeSlice {
    @Id
    @Column(name = "id",columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "total_time",nullable = false,columnDefinition = "time")
    private Time totalTime;

    @Column(name = "hour",nullable = false,columnDefinition = "int")
    private Integer hour;

    @CreationTimestamp
    @Column(name = "created_at",columnDefinition = "date")
    private LocalDate createdAt;
}
