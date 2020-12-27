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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private String address;//지번주소 (동까지)

    private String lotNumber;//지번
    
    private Integer oneRoomPrice;

    private String completionDate;

    @Builder
    public Building(Location location, String address, String lotNumber, Integer oneRoomPrice, String completionDate) {
        this.location = location;
        this.address = address;
        this.lotNumber = lotNumber;
        this.oneRoomPrice = oneRoomPrice;
        this.completionDate = completionDate;
    }
}
