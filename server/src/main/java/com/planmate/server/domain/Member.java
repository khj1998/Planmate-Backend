package com.planmate.server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Slf4j
@Entity
@Table(name = "member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "member_id", columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "고유 식별자")
    private Long memberId;

    @Column(name = "member_name",nullable = false,length = 30,columnDefinition = "varchar")
    @ApiModelProperty(example = "멤버 이름")
    private String memberName;

    @Column(name = "profile", columnDefinition = "mediumtext")
    @ApiModelProperty(example = "프로필 이미지")
    private String profile;

    @Column(name = "login_type", columnDefinition = "int")
    @ApiModelProperty(example = "0: google, 1: naver, 2: kakao, 3: github")
    private Long loginType;

    @Column(name = "email", columnDefinition = "varchar(40)")
    @ApiModelProperty(example = "e-mail")
    private String eMail;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    @ApiModelProperty(example = "사용자 권한 정보들")
    private List<Authority> authorities;

    @Generated
    public boolean hasRole(String role) {
        for(int i = 0; i < authorities.size(); i++) {
            if(authorities.get(i).getRole().equals(role)) {
                return true;
            }
        }

        return false;
    }

    public void updateMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void updateProfile(String profileImg) {
        this.profile = profileImg;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        return Objects.equals(memberId,((Member) obj).getMemberId());
    }
}
