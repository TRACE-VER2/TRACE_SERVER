package com.trace.traceproject.controller;

import com.trace.traceproject.domain.Image;
import com.trace.traceproject.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.http.MediaType.*;

/**
 * S3 연동 방식으로 바꿔서 더이상 사용안함
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ImageController {

    private final ImageService imageService;

    //s3 이미지 url보내주는 방법
    @GetMapping(value = "api/v1/images/{imageId}",
            produces = {IMAGE_JPEG_VALUE})
    public String findById(@PathVariable("imageId") Long imageId) throws IOException {
        Image image = imageService.findById(imageId);

        return image.getFilePath();
    }

/*
    //로컬에서 이미지 리턴하는 방법
    @GetMapping(value = "api/v1/images/{imageId}",
            produces = {IMAGE_JPEG_VALUE})
    public ResponseEntity<Resource> findById(@PathVariable("imageId") Long imageId) throws IOException {
        Image image = imageService.findById(imageId);
        Path path = Paths.get(image.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));

        return ResponseEntity.ok()
                .contentType(parseMediaType("image/jpeg"))
//                .contentType(parseMediaType("application/octet-stream"))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getOrigFilename() + "\"")
                .body(resource);
    }
*/

}
