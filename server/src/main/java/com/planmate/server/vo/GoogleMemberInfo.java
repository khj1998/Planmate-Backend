package com.planmate.server.vo;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GoogleMemberInfo {
    private String name;
    private String email;
    private String picture;
}
