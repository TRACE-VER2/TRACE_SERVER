package com.trace.traceproject.controller;

import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.enums.LocationStatus;
import com.trace.traceproject.dto.response.BuildingDto;
import com.trace.traceproject.dto.response.model.SingleResult;
import com.trace.traceproject.service.BuildingService;
import com.trace.traceproject.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/v1/buildings")
public class BuildingController {

    private final BuildingService buildingService;
    private final ResponseService responseService;

    @GetMapping("/{id}")
    public SingleResult findById(@PathVariable("id") Long id) {
        Building building = buildingService.findById(id);
        return responseService.getSingleResult(new BuildingDto(building));
    }

    //전체 조회 & 지역별 조회
    @GetMapping
    public SingleResult findByLocation(@RequestParam(value = "location", required = false) String location,
                                       @PageableDefault(sort = {"location.name"}) Pageable pageable) {
        //전체 조회
        if (location == null) {
            return responseService.getSingleResult(buildingService.findAll(pageable).map(BuildingDto::new));
        }

        LocationStatus locationName = LocationStatus.valueOf(location);
        Slice<Building> locationBuilding = buildingService.findByLocationName(locationName, pageable);
        return responseService.getSingleResult(locationBuilding.map(BuildingDto::new));
    }
}
