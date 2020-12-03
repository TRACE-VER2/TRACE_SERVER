package com.trace.traceproject.repository;

import com.trace.traceproject.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    //회원별 리뷰 조회
    @EntityGraph(attributePaths = {"building"})
    @Query("select r from Review r where r.member.userId = :userId")
    Slice<Review> findByUserId(@Param("userId") String userId, Pageable pageable);

    //건물별 리뷰 조회
    @EntityGraph(attributePaths = {"member"})
    @Query("select r from Review r where r.building.id = :buildingId")
    Slice<Review> findByBuildingId(@Param("buildingId") Long buildingId, Pageable pageable);
}
