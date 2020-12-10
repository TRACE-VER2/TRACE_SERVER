package com.trace.traceproject.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

//단건 결과 모델
@Getter
@Setter
public class ListResult<T> extends CommonResult{
    private List<T> list;
}
