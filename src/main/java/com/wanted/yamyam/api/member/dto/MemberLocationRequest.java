package com.wanted.yamyam.api.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLocationRequest {
    private Double lat;
    private Double lon;
    private boolean recommend;
}
