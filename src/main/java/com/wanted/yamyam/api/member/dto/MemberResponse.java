package com.wanted.yamyam.api.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberResponse {
    private String email;
    private Double lat;
    private Double lon;
    private boolean recommend;
}
