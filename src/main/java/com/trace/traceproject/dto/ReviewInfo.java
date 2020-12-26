package com.trace.traceproject.dto;

import com.trace.traceproject.domain.enums.BugStatus;
import com.trace.traceproject.domain.enums.GoodBadStatus;
import com.trace.traceproject.domain.enums.NoiseStatus;
import com.trace.traceproject.domain.enums.RentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
@Builder
public class ReviewInfo {
    private String roomNumber;
    private RentType rentType;
    private int deposit;
    private int monthlyRent;
    private int score;
    private int area;
    private LocalDate livingStart;
    private LocalDate livingEnd;
    private boolean remodeled;
    private GoodBadStatus waterPressure;
    private GoodBadStatus lighting;
    private GoodBadStatus frozen;
    private BugStatus bug;
    private NoiseStatus noise;
    private String options;
    private String nearBy;
    private String trueStory;
    private String contact;
    private LocalDate durationStart;
    private LocalDate durationEnd;
}
