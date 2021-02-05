package com.trace.traceproject.service;

import com.trace.traceproject.advice.exception.NoSuchEntityException;
import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.enums.LocationStatus;
import com.trace.traceproject.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;

    public Building findById(Long id) {
        return buildingRepository.findById(id)
                .orElseThrow(()-> new NoSuchEntityException("유효하지 않은 building id 입니다."));
    }

    public Page<Building> findByLocationId(Long locationId, Pageable pageable) {
        return buildingRepository.findByLocationId(locationId, pageable);
    }

    public Page<Building> findByLocationName(LocationStatus locationName, Pageable pageable) {
        return buildingRepository.findByLocationName(locationName, pageable);
    }

    public Page<Building> findAll(Pageable pageable) {
        return buildingRepository.findAll(pageable);
    }

    public Page<Building> findByAddressAndLotNumber(String address, String lotNumber, Pageable pageable) {
        return buildingRepository.findByAddressAndLotNumber(address, lotNumber, pageable);
    }
}
