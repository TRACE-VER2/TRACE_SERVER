package com.trace.traceproject.service;

import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.domain.Review;
import com.trace.traceproject.domain.enums.BugStatus;
import com.trace.traceproject.domain.enums.GoodBadStatus;
import com.trace.traceproject.domain.enums.NoiseStatus;
import com.trace.traceproject.domain.enums.RentType;
import com.trace.traceproject.dto.ReviewSaveDto;
import com.trace.traceproject.repository.BuildingRepository;
import com.trace.traceproject.repository.MemberRepository;
import com.trace.traceproject.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BuildingRepository buildingRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(ReviewSaveDto reviewSaveDto) {
        Member member = memberRepository.findByUserId(reviewSaveDto.getUserId())
                .orElseThrow(()->new IllegalStateException("유효하지 않은 회원id 입니다."));
        Building building = buildingRepository.findById(reviewSaveDto.getBuildingId())
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 빌딩입니다."));

        Review review = Review.builder()
                .member(member)
                .building(building)
                .rentType(RentType.valueOf(reviewSaveDto.getRentType()))
                .deposit(reviewSaveDto.getDeposit())
                .monthlyRent(reviewSaveDto.getMonthlyRent())
                .score(reviewSaveDto.getScore())
                .area(reviewSaveDto.getArea())
                .livingStart(reviewSaveDto.getLivingStart())
                .livingEnd(reviewSaveDto.getLivingEnd())
                .remodeled(reviewSaveDto.isRemodel())
                .waterPressure(GoodBadStatus.valueOf(reviewSaveDto.getWaterPressure()))
                .lighting(GoodBadStatus.valueOf(reviewSaveDto.getLighting()))
                .bug(BugStatus.valueOf(reviewSaveDto.getBug()))
                .noise(NoiseStatus.valueOf(reviewSaveDto.getNoise()))
                .option(reviewSaveDto.getOption())
                .nearBy(reviewSaveDto.getNearBy())
                .trueStory(reviewSaveDto.getTrueStory())
                .contact(reviewSaveDto.getContact())
                .durationStart(reviewSaveDto.getDurationStart())
                .durationEnd(reviewSaveDto.getDurationEnd())
                .build();

        return reviewRepository.save(review).getId();
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new IllegalStateException("해당 게시물이 존재하지 않습니다."));
    }


}
