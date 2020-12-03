package com.trace.traceproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ReviewSaveDto {
    private String userId;
    private Long buildingId;
    private String roomNumer;
    private String rentType;
    private int deposit;
    private int monthlyRent;
    private int score;
    private int area;
    private LocalDateTime livingStart;
    private LocalDateTime livingEnd;
    private boolean remodel;
    private String waterPressure;
    private String lighting;
    private String frozen;
    private String bug;
    private String noise;
    private String option;
    private String nearBy;
    private String trueStory;
    private String contact;
    private LocalDateTime durationStart;
    private LocalDateTime durationEnd;
}
