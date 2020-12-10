package com.trace.traceproject.dto.response;

import lombok.Getter;
import lombok.Setter;

//단건 결과 모델
@Getter
@Setter
public class SingleResult<T> extends CommonResult{
    private T data;
}
