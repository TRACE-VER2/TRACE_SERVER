package com.trace.traceproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LocationStatus {
    JJOKMOON("JJOKMOON", "쪽문"),
    JUNGMOON("JUNGMOON", "정문"),
    CHULMOON("CHULMOON", "철문"),
    DAEMYUNG("DAEMYUNG", "대명/대학로"),
    HANSUNGSHIN("HANSUNGSHIN","한성/성신");

    private final String key;
    private final String value;
}
