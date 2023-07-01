package com.planmate.server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "member_subject")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSubject {
    @Id
    @Column(name = "id",columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "과목 id - 맴버 id 매핑 테이블")
    private Long id;

    @Column(name = "member_id",nullable = false,columnDefinition = "int")
    private Long memberId;

    @Column(name = "subject_id",nullable = false,columnDefinition = "int")
    private Long subjectId;

    public static MemberSubject of(Long memberId,Long subjectId) {
        return MemberSubject.builder()
                .memberId(memberId)
                .subjectId(subjectId)
                .build();
    }
}
