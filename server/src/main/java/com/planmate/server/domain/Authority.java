package com.planmate.server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authority")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {
    @Id
    @Column(name = "authority_name", columnDefinition = "varchar(512)")
    @ApiModelProperty(example = "ROLE_USER | ROLE_ADMIN")
    private String authorityName;

    @Generated
    public String getRole() {
        if(authorityName.equals("ROLE_USER")) {
            return "USER";
        }
        else if(authorityName.equals("ROLE_ADMIN")) {
            return "ADMIN";
        }
        else
        {
            return "error";
        }
    }
}