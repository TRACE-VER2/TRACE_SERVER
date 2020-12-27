package com.trace.traceproject.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trace.traceproject.domain.enums.BugStatus;
import com.trace.traceproject.domain.enums.GoodBadStatus;
import com.trace.traceproject.domain.enums.NoiseStatus;
import com.trace.traceproject.domain.enums.RentType;
import com.trace.traceproject.dto.ReviewInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@Builder
public class ReviewSaveDto {
    private Long buildingId;
    private String roomNumber;
    private String rentType;
    private int deposit;
    private int monthlyRent;
    private int score;
    private int area;

    @DateTimeFormat(pattern = "yyyy-MM-dd") //GET 요청시
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM", timezone = "Asia/Seoul") //POST 요청시
    private LocalDate livingStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd") //GET 요청시
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM", timezone = "Asia/Seoul") //POST 요청시
    private LocalDate livingEnd;

    private boolean remodeled;
    private String waterPressure;
    private String lighting;
    private String frozen;
    private String bug;
    private String noise;
    private String options;
    private String nearBy;
    private String trueStory;
    private String contact;

    @DateTimeFormat(pattern = "yyyy-MM-dd") //GET 요청시
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") //POST 요청시
    private LocalDate durationStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd") //GET 요청시
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") //POST 요청시
    private LocalDate durationEnd;
/*
    public Review toEntity(Member member, Building building) {
        return Review.builder()
                .member(member)
                .building(building)
                .roomNumber(roomNumer)
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
*/

    public ReviewInfo getReviewInfo() {
        return ReviewInfo.builder()
                .roomNumber(roomNumber)
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
                .frozen(GoodBadStatus.valueOf(frozen))
                .bug(BugStatus.valueOf(bug))
                .noise(NoiseStatus.valueOf(noise))
                .options(options)
                .nearBy(nearBy)
                .trueStory(trueStory)
                .contact(contact)
                .durationStart(durationStart)
                .durationEnd(durationEnd)
                .build();
    }
}
