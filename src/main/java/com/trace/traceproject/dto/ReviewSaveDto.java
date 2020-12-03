package com.trace.traceproject.dto;

import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.domain.Review;
import com.trace.traceproject.domain.enums.BugStatus;
import com.trace.traceproject.domain.enums.GoodBadStatus;
import com.trace.traceproject.domain.enums.NoiseStatus;
import com.trace.traceproject.domain.enums.RentType;
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
    private boolean remodeled;
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

    public Review toEntity(Member member, Building building) {
        return Review.builder()
                .member(member)
                .building(building)
                .rentType(RentType.valueOf(rentType))
                .deposit(deposit)
                .monthlyRent(monthlyRent)
                .score(score)
                .area(area)
                .livingStart(livingStart)
                .livingEnd(livingEnd)
                .remodeled(remodeled)
                .waterPressure(GoodBadStatus.valueOf(waterPressure))
                .lighting(GoodBadStatus.valueOf(lighting))
                .bug(BugStatus.valueOf(bug))
                .noise(NoiseStatus.valueOf(noise))
                .option(option)
                .nearBy(nearBy)
                .trueStory(trueStory)
                .contact(contact)
                .durationStart(durationStart)
                .durationEnd(durationEnd)
                .build();
    }
}
