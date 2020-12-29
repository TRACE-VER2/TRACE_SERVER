package com.trace.traceproject.controller;

import com.trace.traceproject.domain.Review;
import com.trace.traceproject.dto.request.ReviewUpdateDto;
import com.trace.traceproject.dto.response.ReviewDto;
import com.trace.traceproject.dto.request.ReviewSaveDto;
import com.trace.traceproject.dto.response.model.CommonResult;
import com.trace.traceproject.service.ImageService;
import com.trace.traceproject.service.ResponseService;
import com.trace.traceproject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ImageService imageService;
    private final ResponseService responseService;

    @PostMapping
    public CommonResult write(@RequestParam("images") List<MultipartFile> files, Principal principal, ReviewSaveDto reviewSaveDto) {
        if (files == null || files.isEmpty()) {
            files = new ArrayList<>();
        }

        String userId = principal.getName();

        reviewService.save(files, userId, reviewSaveDto);

        return responseService.getSuccessResult(200, "리뷰 등록 성공");
    }

    // 리뷰id로 조회하지 않고, 빌딩별, 회원별 리뷰조회만 가능
    @GetMapping
    public CommonResult findById(@RequestParam(name = "buildingId", required = false) Long buildingId,
                                 @RequestParam(name = "userId", required = false) String userId,
                                 @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        if (buildingId != null && userId == null) {
            Slice<Review> buildingReview = reviewService.findBuildingReview(buildingId, pageable);
            return responseService.getSingleResult(buildingReview.map(ReviewDto::new));
        }

        if (buildingId == null && userId != null) {
            Slice<Review> memberReview = reviewService.findMemberReview(userId, pageable);
            return responseService.getSingleResult(memberReview.map(ReviewDto::new));
        }

        return responseService.getFailResult(400, "파라미터로 제대로 된 값이 전달되지 않았습니다.");
    }

    @DeleteMapping("/{id}")
    public CommonResult delete(Principal principal, @PathVariable("id") Long id) {
        String userId = principal.getName();
        Review review = reviewService.findById(id);

        //게시글 작성자만 삭제 가능
        if (!review.getMember().getUserId().equals(userId)) {
            new IllegalStateException("해당 리소스를 삭제하기 위한 권한이 없습니다.");
        }
        reviewService.delete(id);
        return responseService.getSuccessResult();
    }

    @PatchMapping("/{id}")
    public CommonResult update(Principal principal, @PathVariable("id") Long id,
                               @RequestParam("images") List<MultipartFile> files, ReviewUpdateDto reviewUpdateDto) {
        String userId = principal.getName();
        Review review = reviewService.findById(id);

        //게시글 작성자만 수정 가능
        if (!review.getMember().getUserId().equals(userId)) {
            new IllegalStateException("해당 리소스를 삭제하기 위한 권한이 없습니다.");
        }

        if (files == null || files.isEmpty()) {
            files = new ArrayList<>();
        }

        reviewService.update(id, files, reviewUpdateDto);

        return responseService.getSuccessResult(200, "리뷰 수정 성공");
    }
}
