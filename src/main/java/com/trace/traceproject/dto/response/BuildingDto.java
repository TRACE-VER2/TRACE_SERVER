package com.trace.traceproject.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trace.traceproject.domain.Building;
import com.trace.traceproject.domain.Location;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
public class BuildingDto {
    private Long id;
    private String location;
    private String address;//지번주소 (동까지)
    private String lotNumber;//지번
    private Integer oneRoomPrice;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime completionDate;

    public BuildingDto(Building building) {
        this.id = building.getId();
        this.location = building.getLocation().getName().getValue();
        this.address = building.getAddress();
        this.lotNumber = building.getLotNumber();
        this.oneRoomPrice = building.getOneRoomPrice();
        this.completionDate = building.getCompletionDate();
    }
}
