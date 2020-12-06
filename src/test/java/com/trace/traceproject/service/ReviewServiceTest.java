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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class ReviewServiceTest {

    @Autowired ReviewService reviewService;
    @Autowired ReviewRepository reviewRepository;
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

        Long reviewId = reviewService.save(Collections.emptyList(), reviewSaveDto);
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

        Long reviewId = reviewService.save(Collections.emptyList(), reviewSaveDto);

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

    @Test
    public void 리뷰조회_회원() throws Exception {
        //given
        Member member = new Member("dasog94", "1234", "이글루", "dasog@naver.com", "01015678234");
        memberRepository.save(member);

        Building building1 = new Building(null, "성북구", LocalDateTime.now());
        Building building2 = new Building(null, "노원구", LocalDateTime.now());
        Building building3 = new Building(null, "강북구", LocalDateTime.now());
        buildingRepository.save(building1);
        buildingRepository.save(building2);
        buildingRepository.save(building3);

        Review review1 = Review.builder().member(member).building(building1).trueStory("ㅇㅇㅇㅇ").build();
        Review review2 = Review.builder().member(member).building(building2).trueStory("ㄷㄷㄷ").build();
        Review review3 = Review.builder().member(member).building(building3).trueStory("ㅅㅅㅅ").build();
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);

        //when
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));
        Slice<Review> page = reviewService.findMemberReview(member.getUserId(), pageRequest);

        //then
        assertThat(page.getContent().size()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void 리뷰조회_빌딩() throws Exception {
        //given
        Member member1 = new Member("dasog94", "1234", "이글루", "dasog1@naver.com", "01015678231");
        Member member2 = new Member("dasog95", "1234", "이글루", "dasog2@naver.com", "01015678232");
        Member member3 = new Member("dasog96", "1234", "이글루", "dasog3@naver.com", "01015678233");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Building building = new Building(null, "성북구", LocalDateTime.now());
        buildingRepository.save(building);

        Review review1 = Review.builder().member(member1).building(building).trueStory("ㅇㅇㅇㅇ").build();
        Review review2 = Review.builder().member(member2).building(building).trueStory("ㄷㄷㄷ").build();
        Review review3 = Review.builder().member(member3).building(building).trueStory("ㅅㅅㅅ").build();
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);

        //when
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));
        Slice<Review> page = reviewService.findBuildingReview(building.getId(), pageRequest);

        //then
        assertThat(page.getContent().size()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }
}