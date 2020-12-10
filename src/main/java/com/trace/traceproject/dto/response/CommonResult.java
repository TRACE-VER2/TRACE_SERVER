package com.trace.traceproject.dto.response;

import lombok.Getter;
import lombok.Setter;

//실행 결과 공통 모델
@Getter
@Setter
public class CommonResult {

    //응답 성공 여부
    private boolean success;

    //응답 코드 번호
    private int code;

    //응답 메시지
    private String msg;
}
