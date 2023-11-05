package com.wanted.yamyam.api.member.service;

import com.wanted.yamyam.api.member.dto.MemberLocationRequest;
import com.wanted.yamyam.api.member.dto.MemberLoginResponse;
import com.wanted.yamyam.api.member.dto.MemberRequest;
import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.member.repo.MemberRepository;
import com.wanted.yamyam.global.exception.ErrorCode;
import com.wanted.yamyam.global.exception.ErrorException;
import com.wanted.yamyam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(MemberRequest memberRequest) {
        Member member = Member.builder()
                .email(memberRequest.getEmail())
                .password(passwordEncoder.encode(memberRequest.getPassword()))
                .build();
        memberRepository.save(member);
    }

    @Transactional
    public MemberLoginResponse signinReturnToken(MemberRequest memberRequest) {
        Member member = memberRepository.findByEmail(memberRequest.getEmail())
                .orElseThrow(() -> new ErrorException(ErrorCode.NON_EXISTENT_MEMBER));
        validatePassword(memberRequest.getPassword(), member.getPassword());
        return createToken(member);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword))
            throw new ErrorException(ErrorCode.WRONG_PASSWORD);
    }

    private MemberLoginResponse createToken(Member member) {
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        member.setRefreshToken(refreshToken);
        return new MemberLoginResponse(accessToken, refreshToken);
    }

    @Transactional
    public void update(MemberLocationRequest memberRequest, Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NON_EXISTENT_MEMBER));
        member.setLocation(memberRequest.getLat(), memberRequest.getLon(), memberRequest.isRecommend());
    }
}
