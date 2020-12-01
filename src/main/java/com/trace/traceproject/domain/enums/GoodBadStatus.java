package com.trace.traceproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoodBadStatus {
    GOOD("GOOD","좋음"),
    BAD("BAD","나쁨");

    private final String key;
    private final String value;
}
