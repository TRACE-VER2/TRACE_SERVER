package com.trace.traceproject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Building extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "building_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    private String address;
    
    private Integer oneRoomPrice;

    private LocalDateTime completionDate;

    @Builder
    public Building(Location location, String address, Integer oneRoomPrice, LocalDateTime completionDate) {
        this.location = location;
        this.address = address;
        this.oneRoomPrice = oneRoomPrice;
        this.completionDate = completionDate;
    }
}
