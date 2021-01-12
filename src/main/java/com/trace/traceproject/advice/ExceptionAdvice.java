package com.trace.traceproject.advice;

import com.trace.traceproject.advice.exception.CAuthenticationEntryPointException;
import com.trace.traceproject.advice.exception.InvalidAuthenticationTokenException;
import com.trace.traceproject.advice.exception.NoSuchEntityException;
import com.trace.traceproject.advice.exception.PasswordMismatchException;
import com.trace.traceproject.dto.response.model.CommonResult;
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
        e.printStackTrace();
        return responseService.getFailResult(500, e.getMessage());
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)//인증실패
    public CommonResult authenticationEntryPointException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(401, "해당 리소스에 접근하기 위한 권한이 없습니다.");
    }

    /**
     * PreAuthorize 어노테이션으로 권한 인증 설정시 AccessDeniedException만 발생
     * 클라이언트 단에서 인증 에러와, 인가 에러를 구분하는게 크게 의미없으므로 그냥 똑같이 처리
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)//인가실패
    public CommonResult accessDeniedException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(403, "해당 리소스에 접근하기 위한 권한이 없습니다.");
    }

    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult passwordMismatchException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(401, "비밀번호가 일치하지 않습니다.");
    }

    @ExceptionHandler(InvalidAuthenticationTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult expiredAuthenticationTokenException(HttpServletRequest request, Exception e) {
        String message = e.getMessage();
        if (message == null) {
            message = "유효하지 않은 토큰입니다.";
        }
        return responseService.getFailResult(401, message);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult noSuchEntityException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(404, e.getMessage());
    }
}
