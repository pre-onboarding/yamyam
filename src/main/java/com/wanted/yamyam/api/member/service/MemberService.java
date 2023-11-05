package com.wanted.yamyam.api.member.service;

import com.wanted.yamyam.api.member.dto.MemberRequest;
import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.member.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(MemberRequest memberRequest) {
        Member member = Member.builder()
                .email(memberRequest.getEmail())
                .password(passwordEncoder.encode(memberRequest.getPassword()))
                .build();
        memberRepository.save(member);
    }
}
