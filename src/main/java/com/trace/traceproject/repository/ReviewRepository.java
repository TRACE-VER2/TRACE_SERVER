package com.trace.traceproject.repository;

import com.trace.traceproject.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * JPA에서 컬렉션은 페치조인해서는 안된다 -> 엔티티 중복 생긴다
 * 중복제거하려면 distinct 키워드 넣어야하는데, 그렇게되면 전체 조회해서 데이터 전부 메모리에 올린후 제거한다
 * 게다가 페이징쿼리 안나간다, 또 컬렉션 두개이상 페치조인 불가능
 *
 * 따라서 컬렉션 조회 N+1문제 해결하려면 batch_size옵션 사용하거나 @BatchSize 애노테이션 써야한다.
 * 
 * 이거 적은 이유는 내가 images 생각없이 엔티티그래프에 넣었기 때문 :(
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Override
    Optional<Review> findById(Long id);

    //회원별 리뷰 조회
    @EntityGraph(attributePaths = {"building"})
    @Query("select r from Review r where r.member.userId = :userId")
    Page<Review> findByUserId(@Param("userId") String userId, Pageable pageable);

    //건물별 리뷰 조회
    @EntityGraph(attributePaths = {"member"})
    @Query("select r from Review r where r.building.id = :buildingId")
    Page<Review> findByBuildingId(@Param("buildingId") Long buildingId, Pageable pageable);
}
