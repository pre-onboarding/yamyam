package com.wanted.yamyam.api.member.controller;

import com.wanted.yamyam.api.member.dto.MemberLocationRequest;
import com.wanted.yamyam.api.member.dto.MemberRequest;
import com.wanted.yamyam.api.member.dto.MemberLoginResponse;
import com.wanted.yamyam.api.member.dto.MemberResponse;
import com.wanted.yamyam.api.member.service.MemberService;
import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/members")
    public String update(@RequestBody MemberLocationRequest memberLocationRequest) {
        String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
        memberService.update(memberLocationRequest, Long.parseLong(emailId));
        return "완료";
    }

    @GetMapping("/members")
    public MemberResponse getInfo() {
        String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberService.getInfo(Long.parseLong(emailId));
    }
}
