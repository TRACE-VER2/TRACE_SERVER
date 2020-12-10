package com.trace.traceproject.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//Jwt가 유효한 토큰인지 인증하기 위한 Filter
//Security설정시 UsernamePasswordAuthenticationFilter앞에 셋팅
public class JwtAuthenticationFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    //Jwt Provider 주입
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //Request로 들어오는 Jwt Token의 유효성을 검증 (jwtTokenProvider.validateToken)하는 filter를 filterChain에 등록
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //헤더에서 JWT를 받아옴
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        //유효한 토큰인지 확인
        if (token != null && jwtTokenProvider.validateToken(token)) {
            //토큰이 유요하면 토큰으로부터 유저정보(Authentication) 받아옴
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            //SecurityContext에 Authentication객체를 저장
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
