package com.wanted.yamyam.api.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberLoginResponse {
    private String accessToken;
    private String refreshToken;
}
