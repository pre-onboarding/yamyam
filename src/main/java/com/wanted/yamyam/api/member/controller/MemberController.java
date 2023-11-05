package com.wanted.yamyam.api.member.controller;

import com.wanted.yamyam.api.member.dto.MemberRequest;
import com.wanted.yamyam.api.member.dto.MemberLoginResponse;
import com.wanted.yamyam.api.member.service.MemberService;
import com.wanted.yamyam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/members")
    public String signup(@RequestBody MemberRequest memberRequest) {
        memberService.signup(memberRequest);
        return "완료";
    }

    @PostMapping("/members/signin")
    public MemberLoginResponse login(@RequestBody MemberRequest memberRequest) {
        return memberService.signinReturnToken(memberRequest);
    }
}
