package com.trace.traceproject.controller;

import com.trace.traceproject.advice.exception.InvalidAuthenticationTokenException;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.dto.Token;
import com.trace.traceproject.dto.request.MemberJoinDto;
import com.trace.traceproject.dto.response.LoginResponseDto;
import com.trace.traceproject.dto.response.model.SingleResult;
import com.trace.traceproject.repository.TokenRedisRepository;
import com.trace.traceproject.security.jwt.JwtUtil;
import com.trace.traceproject.service.MemberService;
import com.trace.traceproject.service.ResponseService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final TokenRedisRepository tokenRedisRepository;
    private final ResponseService responseService;

    @PostMapping("/api/v1/members/join")
    public Long join(@RequestBody MemberJoinDto memberJoinDto){
        //비밀번호 암호화
        memberJoinDto.setPassword(passwordEncoder.encode(memberJoinDto.getPassword()));
        return memberService.join(memberJoinDto);
    }

    @PostMapping("/api/v1/members/login")
    public SingleResult login(@RequestBody Map<String, String> loginRequest) {
        Member member = memberService.findByUserId(loginRequest.get("userId"));
        
        //비밀번호 불일치
        if (!passwordEncoder.matches(loginRequest.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        //로그인 성공
        //access + refresh 토큰 생성
        String access = jwtUtil.generateAccessToken(member.getUserId(), member.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet()));
        String refresh = jwtUtil.generateRefreshToken(member.getUserId());

        //refresh토큰은 redis에 저장 (만료시간 설정)
        Token token = new Token(member.getUserId(), refresh);
        tokenRedisRepository.save(token);

        //access + refresh 토큰 응답
        return responseService.getSingleResult(new LoginResponseDto(access, refresh));
    }

    //access token 재발급 요청
    @PostMapping("/api/v1/members/refreshToken")
    public SingleResult refresh(@RequestBody Map<String, String> refreshRequest) {
        String refreshToken = refreshRequest.get("refreshToken");
        String username = null;
        Map<String, String> result = new HashMap<>();

        try {
            username = jwtUtil.getUsernameFromToken(refreshToken);
        } catch (IllegalArgumentException e) {
        } catch (ExpiredJwtException e) {
            throw new InvalidAuthenticationTokenException("refreshToken이 만료되었습니다.");
        }

        Token token = tokenRedisRepository.findById(username).orElseThrow(InvalidAuthenticationTokenException::new);
        String refreshTokenfromDb = token.getRefreshToken();
        Member member = memberService.findByUserId(username);

        //refreshToken 유효성 검사
        if (refreshToken.equals(refreshTokenfromDb)) {
            String newAccessToken = jwtUtil.generateAccessToken(username, member.getRoles().stream()
                    .map(Enum::name)
                    .collect(Collectors.toSet()));

            //한번 사용한 refresh토큰은 폐기하고 새로운 refresh 토큰 발급
            String newRefreshToken = jwtUtil.generateRefreshToken(username);
            Token newToken = new Token(username, newRefreshToken);
            tokenRedisRepository.save(newToken);
            
            result.put("accessToken", newAccessToken);
            result.put("refreshToken", newRefreshToken);
            return responseService.getSingleResult(result);
        } else {
            throw new InvalidAuthenticationTokenException();
        }
    }


}
