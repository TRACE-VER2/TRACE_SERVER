package com.trace.traceproject.controller;

import com.trace.traceproject.domain.Member;
import com.trace.traceproject.dto.request.MemberJoinDto;
import com.trace.traceproject.security.jwt.JwtTokenProvider;
import com.trace.traceproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class MemberController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @PostMapping("/api/v1/members/join")
    public Long join(@RequestBody MemberJoinDto memberJoinDto){
        //비밀번호 암호화
        memberJoinDto.setPassword(passwordEncoder.encode(memberJoinDto.getPassword()));
        return memberService.join(memberJoinDto);
    }

    @PostMapping("/api/v1/members/login")
    public String login(@RequestBody Map<String, String> user) {
        Member member = memberService.findByUserId(user.get("userId"));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUserId(), member.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet()));
    }
}
