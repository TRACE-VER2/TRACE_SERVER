package com.trace.traceproject.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
@AllArgsConstructor
@Builder
public class ReviewUpdateDto {
    private List<Long> deletedImages;
    private String roomNumer;
    private String rentType;
    private int deposit;
    private int monthlyRent;
    private int score;
    private int area;

    /**
     * Request : Get - DateTimeFormat만 사용가능 / Post - JsonFormat이 우선 (둘다 사용가능하나, 두개 동시에 있으면 JsonFomat만 적용)
     * Response : JsonFormat만 사용가능
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd") //GET 요청시
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate livingStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd") //GET 요청시
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
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
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate durationStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd") //GET 요청시
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate durationEnd;
}
