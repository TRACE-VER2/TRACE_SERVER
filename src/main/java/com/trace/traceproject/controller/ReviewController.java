package com.trace.traceproject.controller;

import com.trace.traceproject.dto.ReviewSaveDto;
import com.trace.traceproject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/api/v1/reviews")
    public String write(@RequestParam("images") List<MultipartFile> files, ReviewSaveDto reviewSaveDto) {
        if (files == null || files.isEmpty()) {
            files = new ArrayList<>();
        }

        reviewService.save(files, reviewSaveDto);

        return "success";
    }
}
