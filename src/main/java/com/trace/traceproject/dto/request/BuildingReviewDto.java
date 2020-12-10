package com.trace.traceproject.dto.request;

import com.trace.traceproject.domain.Image;
import com.trace.traceproject.domain.Review;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//이미지 url받아지는지 테스트용
//해당 url로 imageController에 이미지 요청하면 응답
//추후 수정
@Data
public class BuildingReviewDto {

    private List<Long> imageIdList = new ArrayList<>();
    private List<String> imagePathList = new ArrayList<>();
    private List<String> imageNameList = new ArrayList<>();

    public BuildingReviewDto(Review review) {
        List<Image> images = review.getImages();
        for (Image image : images) {
            imageIdList.add(image.getId());
            imagePathList.add(image.getFilePath());
            imageNameList.add(image.getOrigFilename());
        }
    }
}
