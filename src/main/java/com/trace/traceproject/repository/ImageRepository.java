package com.trace.traceproject.repository;

import com.trace.traceproject.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findById(Long id);

    Optional<Image> findByFilePath(String imagePath);
}
