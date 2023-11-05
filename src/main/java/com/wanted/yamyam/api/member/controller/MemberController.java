package com.wanted.yamyam.api.member.controller;

import com.wanted.yamyam.api.member.dto.MemberRequest;
import com.wanted.yamyam.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public String signup(@RequestBody MemberRequest memberRequest) {
        memberService.signup(memberRequest);
        return "완료";
    }
}
