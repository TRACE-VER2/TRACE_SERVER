package com.trace.traceproject.service;

import com.trace.traceproject.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;
}
