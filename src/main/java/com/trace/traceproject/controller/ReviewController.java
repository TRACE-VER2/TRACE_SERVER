package com.trace.traceproject.controller;

import com.trace.traceproject.domain.Review;
import com.trace.traceproject.dto.request.BuildingReviewDto;
import com.trace.traceproject.dto.request.ReviewSaveDto;
import com.trace.traceproject.dto.response.SingleResult;
import com.trace.traceproject.service.ResponseService;
import com.trace.traceproject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ResponseService responseService;

    @PostMapping("/api/v1/reviews")
    public String write(@RequestParam("images") List<MultipartFile> files, ReviewSaveDto reviewSaveDto) {
        if (files == null || files.isEmpty()) {
            files = new ArrayList<>();
        }

        reviewService.save(files, reviewSaveDto);

        return "success";
    }

    // 테스트용 메서드 (리뷰id로 조회하지 않고, 빌딩별, 회원별 리뷰조회만 가능)
    @GetMapping("/api/v1/reviews/{buildingId}")
    public SingleResult findById(@PathVariable("buildingId") Long buildingId,
                                 @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        Slice<Review> buildingReview = reviewService.findBuildingReview(buildingId, pageable);
        return responseService.getSingleResult(buildingReview.map(BuildingReviewDto::new));
    }
}
