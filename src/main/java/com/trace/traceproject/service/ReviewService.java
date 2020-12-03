package com.trace.traceproject.service;

import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.domain.Review;
import com.trace.traceproject.dto.ReviewSaveDto;
import com.trace.traceproject.dto.ReviewUpdateDto;
import com.trace.traceproject.repository.BuildingRepository;
import com.trace.traceproject.repository.MemberRepository;
import com.trace.traceproject.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        //개인이 한 건물당 남길 수 있는 리뷰 하나로 제한?

        Review review = reviewSaveDto.toEntity(member, building);

        return reviewRepository.save(review).getId();
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new IllegalStateException("해당 게시물이 존재하지 않습니다."));
    }

    @Transactional
    public void update(ReviewUpdateDto reviewUpdateDto) {
        Review review = reviewRepository.findById(reviewUpdateDto.getReviewId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 리뷰입니다."));

        //변경감지
        review.changeReview(reviewUpdateDto);
    }

    //회원이 쓴 리뷰 목록 조회(페이징 처리)
    //컨트롤러단에서 PageRequest 생성해서 전달할필요없음 (알아서 매핑해줌) default설정만 해주기
    public Slice<Review> findMemberReview(String userId, Pageable pageable){
        return reviewRepository.findByUserId(userId, pageable);
    }

    //건물에 달린 리뷰 목록 조회(페이징 처리)
    public Slice<Review> findBuildingReview(Long buildingId, Pageable pageable) {
        return reviewRepository.findByBuildingId(buildingId, pageable);
    }
}
