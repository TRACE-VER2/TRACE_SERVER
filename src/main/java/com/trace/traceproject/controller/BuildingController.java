package com.trace.traceproject.controller;

import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.enums.LocationStatus;
import com.trace.traceproject.dto.response.BuildingDto;
import com.trace.traceproject.dto.response.model.CommonResult;
import com.trace.traceproject.dto.response.model.SingleResult;
import com.trace.traceproject.service.BuildingService;
import com.trace.traceproject.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/buildings")
public class BuildingController {

    private final BuildingService buildingService;
    private final ResponseService responseService;

    @GetMapping("/{id}")
    public SingleResult findById(@PathVariable("id") Long id) {
        Building building = buildingService.findById(id);
        return responseService.getSingleResult(new BuildingDto(building));
    }

    //전체 조회 & 지역별 조회 & 주소 검색
    @GetMapping
    public CommonResult findByLocation(@RequestParam(value = "location", required = false) String location,
                                       @RequestParam(value = "address", required = false) String address,
                                       @RequestParam(value = "lotNumber", required = false) String lotNumber,
                                       @PageableDefault(sort = {"location.name"}) Pageable pageable) {
        //전체 조회
        if (location == null && address == null && lotNumber == null) {
            return responseService.getSingleResult(buildingService.findAll(pageable).map(BuildingDto::new));
        }

        //지역별 조회
        if (location != null && address == null && lotNumber == null) {
            LocationStatus locationName = LocationStatus.valueOf(location);
            Page<Building> locationBuilding = buildingService.findByLocationName(locationName, pageable);
            return responseService.getSingleResult(locationBuilding.map(BuildingDto::new));
        }

        //주소 지번 검색
        if (location == null && address != null && lotNumber != null) {
            Page<Building> building = buildingService.findByAddressAndLotNumber(address, lotNumber, pageable);
            return responseService.getSingleResult(building.map(BuildingDto::new));
        }

        return responseService.getFailResult(401, "올바른 파라미터 요청이 아닙니다.");

    }
}
