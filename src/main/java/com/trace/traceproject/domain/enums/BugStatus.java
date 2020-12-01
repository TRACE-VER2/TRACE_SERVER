package com.trace.traceproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BugStatus {
    SOMETIMES("SOMETIMES", "가끔"),
    NOTATALL("NOTATALL","전혀"),
    QUITEALOT("QUITEALOT","꽤 많이"),
    ALWAYS("ALWAYS","항상 같이");

    private final String key;
    private final String value;
}
