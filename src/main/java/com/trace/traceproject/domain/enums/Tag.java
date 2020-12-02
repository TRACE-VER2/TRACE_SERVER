package com.trace.traceproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString(of = {"key"})
public enum Tag {
    SUNNY("SUNNY", "햇살가득"),
    NO_BUG("NO_BUG", "벌레없음"),
    QUIET("QUIET", "조용함"),
    LARGE("LARGE", "넓음"),
    CHEAP("CHEAP", "저렴함");

    private final String key;
    private final String value;
}
