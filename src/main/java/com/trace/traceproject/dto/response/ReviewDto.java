package com.trace.traceproject.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.Image;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.domain.Review;
import com.trace.traceproject.domain.enums.BugStatus;
import com.trace.traceproject.domain.enums.GoodBadStatus;
import com.trace.traceproject.domain.enums.NoiseStatus;
import com.trace.traceproject.domain.enums.RentType;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

//이미지 url받아지는지 테스트용
//해당 url로 imageController에 이미지 요청하면 응답
//추후 수정
@Data
public class ReviewDto {

    private Long reviewId;
    private String userId;
    private String roomNumber; //호수
    private List<ImageDto> images; //방 사진 주소 목록
    private String rentType; //전세, 월세 여부
    private int deposit; //전세, 월세 보증금
    private int monthlyRent; //월세 가격
    private int score; //별점
    private int area; //방 크기
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate livingStart; //거주 시작 기간
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate livingEnd; //거주 완료 기간
    private boolean remodeled; //리모델링 여부
    private String waterPressure; //수압 상태
    private String lighting; //채광 상태
    private String frozen; //수압 상태
    private String bug; //벌레 여부
    private String noise; //소음 여부
    private String options; //옵션 설명명
    private String nearBy; //주변 시설
    private String trueStory; //솔직한 한마디

    private String contact; //연락처
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate durationStart; //유지 시작 기간
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate durationEnd;// 유지 완료 기간


    public ReviewDto(Review review) {
        this.reviewId = review.getId();
        this.userId = review.getMember().getUserId();
        this.roomNumber = review.getRoomNumber();
        this.images = review.getImages().stream().map(ImageDto::new).collect(Collectors.toList());
        this.rentType = review.getRentType().getValue();
        this.deposit = review.getDeposit();
        this.monthlyRent = review.getMonthlyRent();
        this.score = review.getScore();
        this.area = review.getArea();
        this.livingStart = review.getLivingStart();
        this.livingEnd = review.getLivingEnd();
        this.remodeled = review.isRemodeled();
        this.waterPressure = review.getWaterPressure().getValue();
        this.lighting = review.getLighting().getValue();
        this.frozen = review.getFrozen().getValue();
        this.bug = review.getBug().getValue();
        this.noise = review.getNoise().getValue();
        this.options = review.getOptions();
        this.nearBy = review.getNearBy();
        this.trueStory = review.getTrueStory();
        this.contact = review.getContact();
        this.durationStart = review.getDurationStart();
        this.durationEnd = review.getDurationEnd();
    }
}
