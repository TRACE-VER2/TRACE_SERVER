package com.trace.traceproject.dto.response;

import com.trace.traceproject.domain.Image;
import lombok.Data;

@Data
public class ImageDto {
    private Long imageId;
    private String filename;
    private String filePath;

    public ImageDto(Image image) {
        imageId = image.getId(); // 추후 이미지 삭제하기 위한 Key
        filePath = image.getFilePath();
    }
}
