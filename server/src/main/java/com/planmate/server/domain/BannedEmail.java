package com.planmate.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "banned_email")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannedEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",columnDefinition = "int")
    private Long id;

    @Column(name = "email",nullable = false,columnDefinition = "varchar",length = 40)
    private String eMail;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static BannedEmail of(Member member) {
        return BannedEmail.builder()
                .eMail(member.getEMail())
                .build();
    }
}
