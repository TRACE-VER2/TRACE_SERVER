package com.trace.traceproject.controller;

import com.trace.traceproject.advice.exception.InvalidAuthenticationTokenException;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.dto.Token;
import com.trace.traceproject.dto.response.model.CommonResult;
import com.trace.traceproject.dto.response.model.SingleResult;
import com.trace.traceproject.repository.TokenRedisRepository;
import com.trace.traceproject.security.jwt.JwtUtil;
import com.trace.traceproject.service.MemberService;
import com.trace.traceproject.service.ResponseService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final TokenRedisRepository tokenRedisRepository;
    private final MemberService memberService;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    //등록 여부 검사
    @GetMapping("/registered")
    public CommonResult confirmRegistered(@RequestParam(name = "userId", required = false) String userId,
                                          @RequestParam(name = "name", required = false) String name,
                                          @RequestParam(name = "phoneNum", required = false) String phoneNum) {
        if (name == null && phoneNum == null && userId != null) {
            try {
                Member member = memberService.findByUserId(userId);
            } catch (Exception e) {
                return responseService.getSuccessResult(200, "사용가능한 Id입니다.");
            }
        }

        if (userId == null && name != null && phoneNum != null) {
            try {
                Member member = memberService.findByNameAndPhoneNum(name, phoneNum);
            } catch (Exception e) {
                return responseService.getSuccessResult(200, "회원가입 가능합니다.");
            }
        }

        return responseService.getFailResult(400, "파라미터로 제대로 된 값이 전달되지 않았습니다.");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/password")
    public CommonResult correctPassword(Principal principal, @RequestBody Map<String, String> passwordRequest) {
        String password = passwordRequest.get("password");
        String userId = principal.getName();

        Member member = memberService.findByUserId(userId);

        if (passwordEncoder.matches(password, member.getPassword())) {
            return responseService.getSuccessResult();
        }
        return responseService.getFailResult();
    }

    //access token 재발급 요청 (요청 바디에 넣어서 요청)
    @PostMapping("/token")
    public SingleResult refresh(@RequestBody Map<String, String> refreshRequest) {
        String refreshToken = refreshRequest.get("refreshToken");
        String username;
        Map<String, String> result = new HashMap<>();

        try {
            username = jwtUtil.getUsernameFromToken(refreshToken);
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

    //access token 재발급 (요청 header에 쿠키로 넣어서 요청)
    @GetMapping("/token")
    public SingleResult refresh(@CookieValue(value = "refreshToken", required = false) Cookie cookie,
                                HttpServletResponse response) {
        if (cookie == null) {
            log.warn("refreshToken 쿠키 누락");
            throw new InvalidAuthenticationTokenException("refreshToken 쿠키 누락");
        }

        String refreshToken = cookie.getValue();
        String username;
        Map<String, String> result = new HashMap<>();

        try {
            username = jwtUtil.getUsernameFromToken(refreshToken);
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

            //한번 사용한 refresh 토큰은 폐기하고 새로운 refresh 토큰 발급
            String newRefreshToken = jwtUtil.generateRefreshToken(username);
            Token newToken = new Token(username, newRefreshToken);
            tokenRedisRepository.save(newToken);

            //refresh 토큰을 response cookie로 넣어줌
            cookie.setValue(newRefreshToken);
            cookie.setMaxAge(60*60*24*7);
            cookie.setPath("/api/v1/auth/access");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            //새로 발급된 access 토큰은 response body에 넣어줌
            result.put("accessToken", newAccessToken);
            return responseService.getSingleResult(result);
        } else {
            throw new InvalidAuthenticationTokenException();
        }
    }
}
