package com.trace.traceproject.service;

import com.trace.traceproject.dto.response.model.CommonResult;
import com.trace.traceproject.dto.response.model.ListResult;
import com.trace.traceproject.dto.response.model.SingleResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

//결과 모델에 데이터를 넣어주는 역할을 하는 Service
@Service
public class ResponseService {
    
    //enum으로 api요청 결과에 대한 code, message를 정의
    @AllArgsConstructor
    @Getter
    public enum CommonResponse {
        SUCCESS(0, "성공하였습니다"),
        FAIL(-1, "실패하였습니다.");
        
        int code;
        String msg;
    }

    //단일건 결과를 처리하는 메서드
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    //다중건 결과를 처리하는 메서드
    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    //성공 결과만 처리하는 메서드
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    //성공 결과만 처리하는 메서드
    public CommonResult getSuccessResult(int code, String message) {
        CommonResult result = new CommonResult();
        result.setSuccess(true);
        result.setCode(code);
        result.setMsg(message);
        return result;
    }

    //실패 결과만 처리하는 메서드
    public CommonResult getFailResult() {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
        return result;
    }

    //실패 결과만 처리하는 메서드
    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    //결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}
