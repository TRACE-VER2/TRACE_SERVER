package com.trace.traceproject.controller;

import com.trace.traceproject.advice.exception.PasswordMismatchException;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.dto.Token;
import com.trace.traceproject.dto.request.ChangePasswordDto;
import com.trace.traceproject.dto.request.MemberJoinDto;
import com.trace.traceproject.dto.request.MemberUpdateDto;
import com.trace.traceproject.dto.response.LoginResponseDto;
import com.trace.traceproject.dto.response.model.CommonResult;
import com.trace.traceproject.dto.response.model.SingleResult;
import com.trace.traceproject.repository.TokenRedisRepository;
import com.trace.traceproject.security.jwt.JwtUtil;
import com.trace.traceproject.service.MemberService;
import com.trace.traceproject.service.ResponseService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("/api/v1/members")
public class MemberController {
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final TokenRedisRepository tokenRedisRepository;
    private final ResponseService responseService;
    private final RedisTemplate redisTemplate;

    @PostMapping("/join")
    public CommonResult join(@RequestBody MemberJoinDto memberJoinDto){
        //비밀번호 암호화
        memberJoinDto.setPassword(passwordEncoder.encode(memberJoinDto.getPassword()));
        memberService.join(memberJoinDto);
        return responseService.getSuccessResult(200, "회원가입 성공");
    }

    @CrossOrigin(allowCredentials = "true")
    @PostMapping("/login")
    public SingleResult login(@RequestBody Map<String, String> loginRequest,
                              HttpServletResponse response) {
        Member member = memberService.findByUserId(loginRequest.get("userId"));

        //비밀번호 불일치
        if (!passwordEncoder.matches(loginRequest.get("password"), member.getPassword())) {
            throw new PasswordMismatchException("잘못된 비밀번호입니다.");
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

        //refresh토큰을 response cookie로 넣어줌
        Cookie cookie = new Cookie("refreshToken", refresh);
        cookie.setMaxAge(60*60*24*7);
        cookie.setHttpOnly(true);
        cookie.setDomain("jaggutrace.com");
        cookie.setPath("/api/v1/auth/token");
        response.addCookie(cookie);
        
        //access + refresh 토큰 응답
        return responseService.getSingleResult(new LoginResponseDto(access, refresh));
    }

    @GetMapping("/logout")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommonResult logout(@RequestHeader("Authorization") String token) {//헤더에서 accessToken 가져옴
        //Bearer 제거
        String accessToken = token.substring(7);
        String username;

        try {
            username = jwtUtil.getUsernameFromToken(accessToken);
        } catch (ExpiredJwtException e) {
            username = e.getClaims().getSubject();
            log.info("username from expired access token : " + username);
        }

        //redis에서 refresh 토큰 삭제
        if (tokenRedisRepository.existsById(username)) {
            log.info("delete refresh Token from redis");
            tokenRedisRepository.deleteById(username);
        } else {
            log.warn("로그인 되어있지 않음");
        }

        //logout 토큰 캐싱 (만료시간만큼)
        log.info("logout ing : " + accessToken);
//        redisTemplate.opsForValue().set(accessToken, true);
//        redisTemplate.expire(accessToken, 2, TimeUnit.HOURS);

        Token logoutToken = new Token(accessToken, null, 2);
        tokenRedisRepository.save(logoutToken);

        return responseService.getSuccessResult(200, "로그아웃 성공");
    }

    /**
     * 비밀번호 변경 요청 (로그인 된 상태에서만 가능 == 인증 토큰 같이 보내야함)
     * controller에서는 Principal을 인자로 받아서 현재 로그인된 사용자 정보에 접근 가능
     * Authentication도 인자로 받을 수 있음
     * @AuthenticationPrincipal 어노테이션으로 어디에서든 인증된 사용자 정보 받을 수 있음
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/password")
    public CommonResult changePassword(Principal principal, @RequestBody ChangePasswordDto changePasswordDto) {

        String prevPw = changePasswordDto.getPrevPassword();
        String changedPw = changePasswordDto.getChangedPassword();

//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String userId = userDetails.getUsername();

        String userId = principal.getName();
        Member member = memberService.findByUserId(userId);

        if (!passwordEncoder.matches(prevPw, member.getPassword())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        log.info("member Id : " + member.getId());
        memberService.changePassword(member.getId(), passwordEncoder.encode(changedPw));

        return responseService.getSuccessResult(200, "비밀번호 변경 성공");
    }

    //회원 정보 수정
    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping
    public CommonResult update(Principal principal, @RequestBody MemberUpdateDto memberUpdateDto) {
        String userId = principal.getName();
        log.info("수정 시작");
        memberService.update(userId, memberUpdateDto);
        log.info("수정 끝");
        return responseService.getSuccessResult(200, "회원 정보 수정 성공");
    }

    // 회원 탈퇴
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping
    public CommonResult withdrawal(Principal principal) {
        String userId = principal.getName();

        memberService.deleteByUserId(userId);

        return responseService.getSuccessResult(200, "회원 탈퇴 성공");
    }

}
