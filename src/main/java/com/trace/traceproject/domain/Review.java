package com.trace.traceproject.domain;

import com.trace.traceproject.domain.enums.BugStatus;
import com.trace.traceproject.domain.enums.GoodBadStatus;
import com.trace.traceproject.domain.enums.NoiseStatus;
import com.trace.traceproject.domain.enums.RentType;
import com.trace.traceproject.dto.ReviewInfo;
import com.trace.traceproject.dto.request.ReviewUpdateDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@ToString(of = {"id","rentType"})
public class Review extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;
    
    private String roomNumber; //호수

    //영속성 전이와 고아객체 속성 모두 적용 (image는 review에 종속적이기 때문)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Image> images = new ArrayList<>(); //방 사진

    @Enumerated(EnumType.STRING)
    private RentType rentType; //전세, 월세 여부

    private int deposit; //전세, 월세 보증금

    private int monthlyRent; //월세 가격

    private int score; //별점

    private int area; //방 크기

    private LocalDate livingStart; //거주 시작 기간
    private LocalDate livingEnd; //거주 완료 기간

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

    //그냥 option은 mysql 예약어라 ddl 자동 생성 하면 오류 발생 (mariadb 쓰면 상관없음)
    @Column(columnDefinition = "TEXT")
    private String options; //옵션 설명명

    @Column(columnDefinition = "TEXT")
    private String nearBy; //주변 시설

    @Column(columnDefinition = "TEXT")
    private String trueStory; //솔직한 한마디

    /*
    방 판매 관련
     */
    
    private String contact; //연락처

    private LocalDate durationStart; //유지 시작 기간

    private LocalDate durationEnd;// 유지 완료 기간

    /**
     * 연관관계 편의 메서드
     */
    public void addImage(Image image) {
        images.add(image);
        image.setReview(this);
    }

    /**
     * 생성 메서드
     */
    public static Review createReview(ReviewInfo info, Member member, Building building, List<Image> images) {
        Review review = new Review();
        review.member = member;
        review.building = building;
        review.roomNumber = info.getRoomNumber();
        review.rentType = info.getRentType();
        review.deposit = info.getDeposit();
        review.monthlyRent = info.getMonthlyRent();
        review.score = info.getScore();
        review.area = info.getArea();
        review.livingStart = info.getLivingStart();
        review.livingEnd = info.getLivingEnd();
        review.remodeled = info.isRemodeled();
        review.waterPressure = info.getWaterPressure();
        review.lighting = info.getLighting();
        review.frozen = info.getFrozen();
        review.bug = info.getBug();
        review.noise = info.getNoise();
        review.options = info.getOptions();
        review.nearBy = info.getNearBy();
        review.trueStory = info.getTrueStory();
        review.contact = info.getContact();
        review.durationStart = info.getDurationStart();
        review.durationEnd = info.getDurationEnd();

        for (Image image : images) {
            image.setBuilding(building); //해당 리뷰가 등록되는 빌딩도 같이 저장한다 (빌딩별 이미지 조회 위해서)
            review.addImage(image); //연관관계 편의 메서드
        }

        return review;
    }

    /**
     * 리뷰 수정
     * 파라미터로 다 받으면 재사용성측면에서 좋지만
     * 너무 많다면 의존성 생기더라도 dto로 전달하는 것 고려
     * 이미지를 제외한 나머지 정보를 수정한다
     */
    public void changeReviewInfo(ReviewUpdateDto reviewUpdateDto) {
        this.roomNumber = reviewUpdateDto.getRoomNumer();
//        this.image = reviewUpdateDto.getImage(); // 이미지는 따로 처리
        this.rentType = RentType.valueOf(reviewUpdateDto.getRentType());
        this.deposit = reviewUpdateDto.getDeposit();
        this.monthlyRent = reviewUpdateDto.getMonthlyRent();
        this.score = reviewUpdateDto.getScore();
        this.area = reviewUpdateDto.getArea();
        this.livingStart = reviewUpdateDto.getLivingStart();
        this.livingEnd = reviewUpdateDto.getLivingEnd();
        this.remodeled = reviewUpdateDto.isRemodeled();
        this.waterPressure = GoodBadStatus.valueOf(reviewUpdateDto.getWaterPressure());
        this.lighting = GoodBadStatus.valueOf(reviewUpdateDto.getLighting());
        this.frozen = GoodBadStatus.valueOf(reviewUpdateDto.getFrozen());
        this.bug = BugStatus.valueOf(reviewUpdateDto.getBug());
        this.noise = NoiseStatus.valueOf(reviewUpdateDto.getNoise());
        this.options = reviewUpdateDto.getOptions();
        this.nearBy = reviewUpdateDto.getNearBy();
        this.trueStory = reviewUpdateDto.getTrueStory();
        this.contact = reviewUpdateDto.getContact();
        this.durationStart = reviewUpdateDto.getDurationStart();
        this.durationEnd = reviewUpdateDto.getDurationEnd();
    }
}
