package com.trace.traceproject.security.jwt;

import com.trace.traceproject.advice.exception.InvalidAuthenticationTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Jwt가 유효한 토큰인지 인증하기 위한 Filter
//(인증Filter, SpringSecurity가 기본 제공하는 인증필터인 UsernamePasswordAuthenticationFilter 대체)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 Authorization 토큰을 받아옴
        String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        //토큰이 null이 아니고 Bearer 방식인경우
        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")){
            jwt = tokenHeader.substring(7); //Bearer 제거

            if(!jwt.equals("") && jwtUtil.validateToken(jwt))
                username = jwtUtil.getUsernameFromToken(jwt);

            if (username == null) {
                logger.info("token maybe expired : username is null");
            } // access Token이 redis에 저장되어있으면 로그아웃 처리되어 만료된 토큰임
            else if (redisTemplate.opsForValue().get(jwt) != null) {
                logger.warn("this token already logout");
            } else {
                //DB access 대신 jwt 파싱 정보로 인증 토큰 생성
                Authentication auth = jwtUtil.getAuthentication(jwt);
                //인증 처리
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }


        filterChain.doFilter(request, response);
    }


}
