package com.trace.traceproject.repository;

import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findById(Long id);

    Optional<Image> findByFilePath(String imagePath);

    /**
     * 스프링 데이터 JPA는 다양한 반환타입 지원
     * 컬렉션의 경우 조회 데이터 없을 시 빈 컬렉션 반환 (null 아님)
     */
    Optional<Image> findFirstByBuilding(Building building);
}
