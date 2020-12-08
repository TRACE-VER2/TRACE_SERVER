package com.trace.traceproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 응답 body 감싸주는 dto (엔티티 바로 return하면 안됨)
 */
@Data
@AllArgsConstructor
@Builder
public class ResponseDto<T> {
    T data;
}
