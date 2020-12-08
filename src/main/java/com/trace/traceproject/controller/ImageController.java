package com.trace.traceproject.controller;

import com.trace.traceproject.domain.Image;
import com.trace.traceproject.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

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

/*    @GetMapping(value = "api/v1/images",
            produces = {IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE})
    public ResponseEntity<Resource> findByImagePath(@RequestParam("imagePath") String imagePath) throws IOException {
        Image image = imageService.findByImagePath(imagePath);
        Path path = Paths.get(image.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));

        return ResponseEntity.ok()
                .contentType(parseMediaType("image/jpeg"))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getOrigFilename() + "\"")
                .body(resource);
    }*/
}
