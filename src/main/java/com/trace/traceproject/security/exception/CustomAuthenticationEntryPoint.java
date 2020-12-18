package com.trace.traceproject.security.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//토큰 검증단에서 발생한 예외 처리하는 부분
//토큰 검증단에서 발생한 예외를 잡아서 controller로 리다이렉트해주는 역할 (예외처리 위임)
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendRedirect("/exception/entrypoint");
    }
}
