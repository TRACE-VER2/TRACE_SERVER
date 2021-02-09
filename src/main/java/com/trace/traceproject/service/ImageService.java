package com.trace.traceproject.service;

import com.trace.traceproject.advice.exception.NoSuchEntityException;
import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.Image;
import com.trace.traceproject.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public Image findById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("잘못된 이미지 id입니다"));
    }

    public Image findByImagePath(String imagePath) {
        return imageRepository.findByFilePath(imagePath)
                .orElseThrow(() -> new NoSuchEntityException("잘못된 이미지 주소입니다"));
    }

    public Optional<Image> findFirstByBuilding(Building building) {
        return imageRepository.findFirstByBuilding(building);
    }
}
