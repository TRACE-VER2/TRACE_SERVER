package com.trace.traceproject.advice;

import com.trace.traceproject.advice.exception.CAuthenticationEntryPointException;
import com.trace.traceproject.dto.response.CommonResult;
import com.trace.traceproject.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
//controller단에서 예외 발생시 json형태로 결과 반환
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    /**
     * 어떤 Exception이 발생할 때 아래 Handler를 적용할 것인지 Exception Class를 인자로 넣는다
     * 아래에서는 최상위 예외처리 객체인 Exception.class를 넣었으므로
     * 다른 ExceptionHandler에서 걸러지지 않은 예외가 있으면 최종으로 이 handler를 거쳐 처리됨
     * 그래서 메서드 명도 defaultException
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult();
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult authenticationEntryPointException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(-1002, "해당 리소스에 접근하기 위한 권한이 없습니다.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult accessDeniedException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(-1003, "보유한 권한으로 접근할 수 없는 리소스입니다.");
    }
}
