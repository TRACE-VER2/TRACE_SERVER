package com.trace.traceproject.service;

import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.domain.Review;
import com.trace.traceproject.dto.ReviewSaveDto;
import com.trace.traceproject.dto.ReviewUpdateDto;
import com.trace.traceproject.repository.BuildingRepository;
import com.trace.traceproject.repository.MemberRepository;
import com.trace.traceproject.repository.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ReviewServiceTest {

    @Autowired ReviewService reviewService;
    @Autowired MemberRepository memberRepository;
    @Autowired BuildingRepository buildingRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 리뷰등록() throws Exception {
        //given
        Member member = new Member("syleemk", "1234", "이글루", "syleemk@naver.com", "01012341234");
        memberRepository.save(member);
        Building building = new Building(null, "성북구", LocalDateTime.now());
        buildingRepository.save(building);

        //when
        ReviewSaveDto reviewSaveDto = ReviewSaveDto.builder()
                .userId(member.getUserId())
                .buildingId(building.getId())
                .rentType("KEY_MONEY")
                .deposit(100)
                .waterPressure("GOOD")
                .lighting("GOOD")
                .frozen("GOOD")
                .bug("ALWAYS")
                .noise("QUIET")
                .option("냉장고")
                .build();

        Long reviewId = reviewService.save(reviewSaveDto);
        Review review = reviewService.findById(reviewId);

        //then
        assertThat(reviewId).isGreaterThan(0);
        System.out.println("review = " + review);
    }
    
    @Test
    public void 리뷰수정() throws Exception {
        //given
        Member member = new Member("dasog94", "1234", "이글루", "dasog@naver.com", "01015678234");
        memberRepository.save(member);
        Building building = new Building(null, "성북구", LocalDateTime.now());
        buildingRepository.save(building);

        ReviewSaveDto reviewSaveDto = ReviewSaveDto.builder()
                .userId(member.getUserId())
                .buildingId(building.getId())
                .rentType("KEY_MONEY")
                .deposit(100)
                .waterPressure("GOOD")
                .lighting("GOOD")
                .frozen("GOOD")
                .bug("ALWAYS")
                .noise("QUIET")
                .option("냉장고")
                .build();

        Long reviewId = reviewService.save(reviewSaveDto);

        ReviewUpdateDto reviewUpdateDto = ReviewUpdateDto.builder()
                .reviewId(reviewId)
                .rentType("KEY_MONEY")
                .deposit(150)
                .waterPressure("GOOD")
                .lighting("GOOD")
                .frozen("GOOD")
                .bug("ALWAYS")
                .noise("QUIET")
                .option("냉장고")
                .build();

        reviewService.update(reviewUpdateDto);

        em.flush();
        em.clear();

        Review review = reviewService.findById(reviewId);

        assertThat(review.getDeposit()).isEqualTo(150);
    }
}