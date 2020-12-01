package com.trace.traceproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoiseStatus {
    QUIET("QUIET", "조용"),
    NOISY("NOISY", "시끄러움"),
    OFTEN("OFTEN", "종종 들림");

    private final String key;
    private final String value;
}
