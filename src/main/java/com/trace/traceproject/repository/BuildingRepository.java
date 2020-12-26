package com.trace.traceproject.repository;

import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.enums.LocationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuildingRepository extends JpaRepository<Building, Long> {

    @EntityGraph(attributePaths = {"location"})
    @Query("select b from Building b where b.location.id = :locationId")
    Slice<Building> findByLocationId(@Param("locationId") Long locationId, Pageable pageable);

    @EntityGraph(attributePaths = {"location"})
    @Query("select b from Building b where b.location.name = :locationName")
    Slice<Building> findByLocationName(@Param("locationName") LocationStatus locationName, Pageable pageable);

//    @EntityGraph(attributePaths = {"location"})
//    @Query("select b from Building b where b.address like '%'||:address||'%' and b.lobNumber like '%'||:lobNumber||'%'")
//    Slice<Building> findByAddressAndLobNumber(@Param("address") String address, @Param("lobNumber") String lobNumber);

}
