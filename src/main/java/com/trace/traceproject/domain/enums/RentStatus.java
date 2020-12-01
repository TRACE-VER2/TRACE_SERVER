package com.trace.traceproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentStatus {
    KEY_MONEY("KEY_MONEY","전세"),
    MONTHLY("MONTHLY", "월세");

    private final String key;
    private final String value;
}
