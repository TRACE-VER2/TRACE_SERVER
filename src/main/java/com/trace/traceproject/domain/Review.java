package com.trace.traceproject.domain;

import com.trace.traceproject.domain.enums.BugStatus;
import com.trace.traceproject.domain.enums.GoodBadStatus;
import com.trace.traceproject.domain.enums.NoiseStatus;
import com.trace.traceproject.domain.enums.RentType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","rentType"})
public class Review extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;
    
    private String roomNumber; //호수

    private String image; // 방 사진

    @Enumerated(EnumType.STRING)
    private RentType rentType; //전세, 월세 여부

    private int deposit; //전세, 월세 보증금

    private int monthlyRent; //월세 가격

    private int score; //별점

    private int area; //방 크기

    private LocalDateTime livingStart; //거주 시작 기간
    private LocalDateTime livingEnd; //거주 완료 기간

    private boolean remodeled; //리모델링 여부

    @Enumerated(EnumType.STRING)
    private GoodBadStatus waterPressure; //수압 상태

    @Enumerated(EnumType.STRING)
    private GoodBadStatus lighting; //채광 상태

    @Enumerated(EnumType.STRING)
    private GoodBadStatus frozen; //수압 상태

    @Enumerated(EnumType.STRING)
    private BugStatus bug; //벌레 여부

    @Enumerated(EnumType.STRING)
    private NoiseStatus noise; //소음 여부

    @Column(columnDefinition = "TEXT")
    private String option; //옵션 설명명

    @Column(columnDefinition = "TEXT")
    private String nearBy; //주변 시설

    @Column(columnDefinition = "TEXT")
    private String trueStory; //솔직한 한마디

    /*
    방 판매 관련
     */
    
    private String contact; //연락처

    private LocalDateTime durationStart; //유지 시작 기간

    private LocalDateTime durationEnd;// 유지 완료 기간

    @Builder
    public Review(Member member, Building building, String roomNumber, String image, RentType rentType, int deposit, int monthlyRent, int score, int area, LocalDateTime livingStart, LocalDateTime livingEnd, boolean remodeled, GoodBadStatus waterPressure, GoodBadStatus lighting, GoodBadStatus frozen, BugStatus bug, NoiseStatus noise, String option, String nearBy, String trueStory, String contact, LocalDateTime durationStart, LocalDateTime durationEnd) {
        this.member = member;
        this.building = building;
        this.roomNumber = roomNumber;
        this.image = image;
        this.rentType = rentType;
        this.deposit = deposit;
        this.monthlyRent = monthlyRent;
        this.score = score;
        this.area = area;
        this.livingStart = livingStart;
        this.livingEnd = livingEnd;
        this.remodeled = remodeled;
        this.waterPressure = waterPressure;
        this.lighting = lighting;
        this.frozen = frozen;
        this.bug = bug;
        this.noise = noise;
        this.option = option;
        this.nearBy = nearBy;
        this.trueStory = trueStory;
        this.contact = contact;
        this.durationStart = durationStart;
        this.durationEnd = durationEnd;
    }
}
