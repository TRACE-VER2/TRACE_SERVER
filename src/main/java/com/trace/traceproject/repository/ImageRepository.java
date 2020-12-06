package com.trace.traceproject.repository;

import com.trace.traceproject.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
